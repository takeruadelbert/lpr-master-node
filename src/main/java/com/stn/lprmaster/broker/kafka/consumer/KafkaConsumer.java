package com.stn.lprmaster.broker.kafka.consumer;

import com.google.gson.Gson;
import com.stn.lprmaster.broker.kafka.model.frame.Frame;
import com.stn.lprmaster.broker.kafka.model.frame.FrameResult;
import com.stn.lprmaster.broker.kafka.model.image.Image;
import com.stn.lprmaster.entities.*;
import com.stn.lprmaster.entities.enumerate.InputImageStatus;
import com.stn.lprmaster.misc.ConstantValue;
import com.stn.lprmaster.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class KafkaConsumer {

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    @Autowired
    private DataStateRepository dataStateRepository;

    @Autowired
    private InputFrameRepository inputFrameRepository;

    @Autowired
    private OutputFrameRepository outputFrameRepository;

    @Autowired
    private InputImageRepository inputImageRepository;

    @Autowired
    private OutputImageRepository outputImageRepository;

    @Autowired
    private Gson gson;

    @KafkaListener(id = "consumerID", topics = "${kafka.topic.input-frame}", groupId = "${kafka.group}", containerFactory = "kafkaListenerContainerFactory", autoStartup = "false")
    public void consume(@Payload Frame frame) {
        try {
            Optional<DataState> dataStateOptional = dataStateRepository.findFirstByIdGate(frame.getIdGate());
            if (!dataStateOptional.isPresent()) {
                log.error("Gate ID {} does not exists.", frame.getIdGate());
                return;
            }
            DataState dataState = dataStateOptional.get();
            String vehicleType = frame.getResult().getVehicleType();
            String licensePlateNumber = frame.getResult().getLicensePlateNumber();
            if (!licensePlateNumber.equals(ConstantValue.STATUS_UNDETECTED)) {
                String tokenOutput = frame.getResult().getTokenOutput();
                InputFrame savedInputFrame = inputFrameRepository.save(new InputFrame(dataState.getId(), frame.getTokenInput()));
                outputFrameRepository.save(new OutputFrame(vehicleType, licensePlateNumber, tokenOutput, savedInputFrame));
                updateLastState(dataState, ConstantValue.STATUS_DETECTED, frame.getResult());
            } else {
                updateLastState(dataState, ConstantValue.STATUS_UNDETECTED, frame.getResult());
            }
        } catch (Exception ex) {
            log.error("Error has occurred when consuming data from broker : {}", ex.getMessage());
            registry.getListenerContainer("consumerID").stop();
        }
    }

    private void updateLastState(DataState dataState, String status, FrameResult frameResult) {
        Map<String, Object> lastState = new HashMap<>();
        lastState.put("status", status);
        lastState.put("result", gson.toJsonTree(frameResult));
        dataState.setLastState(gson.toJson(lastState));
        dataStateRepository.save(dataState);
    }

    @KafkaListener(id = "consumerImageID", topics = "${kafka.topic.input-image}", groupId = "${kafka.group}", containerFactory = "kafkaListenerContainerFactoryImage", autoStartup = "false")
    public void consumeImageResult(@Payload Image image) {
        try {
            Optional<InputImage> inputImageOptional = inputImageRepository.findFirstByTicketNumber(image.getTicketNumber());
            if (!inputImageOptional.isPresent()) {
                log.error("Ticket Number {} does not exists.", image.getTicketNumber());
            } else {
                outputImageRepository.save(new OutputImage(inputImageOptional.get(), image.getResult().getVehicleType(), image.getResult().getLicensePlateNumber(), image.getResult().getTokenOutput()));
                updateInputImageStatus(inputImageOptional.get());
            }
        } catch (Exception ex) {
            log.error("Error has occurred when consuming data Image Result from broker : {}", ex.getMessage());
            registry.getListenerContainer("consumerImageID").stop();
        }
    }

    private InputImage updateInputImageStatus(InputImage inputImage) {
        inputImage.setStatus(InputImageStatus.DONE);
        return inputImageRepository.save(inputImage);
    }
}
