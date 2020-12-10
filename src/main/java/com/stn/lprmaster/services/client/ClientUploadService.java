package com.stn.lprmaster.services.client;

import com.stn.ester.core.exceptions.BadRequestException;
import com.stn.ester.helpers.FileHelper;
import com.stn.lprmaster.client.ServiceGenerator;
import com.stn.lprmaster.client.request.UploadEncoded;
import com.stn.lprmaster.client.response.UploadResponse;
import com.stn.lprmaster.client.service.UploadService;
import com.stn.lprmaster.misc.MasterNodeHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ClientUploadService {
    private UploadService uploadService;

    public ClientUploadService() {
        uploadService = ServiceGenerator.createBaseService(UploadService.class);
    }

    public ResponseEntity uploadRaw(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            throw new BadRequestException("No file provided.");
        }
        String extensionFile = FileHelper.getExtensionFile(multipartFile.getOriginalFilename());
        String encodedBase64 = MasterNodeHelper.convertBytesToBase64(extensionFile, multipartFile.getBytes());
        return doUploadEncoded(multipartFile.getOriginalFilename(), encodedBase64);
    }

    public ResponseEntity uploadEncoded(String filename, String encodedFile) {
        return doUploadEncoded(filename, encodedFile);
    }

    private ResponseEntity doUploadEncoded(String filename, String encodedFile) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus httpStatus;
        Call<UploadResponse> uploadResponseCall = uploadService.uploadEncoded(new UploadEncoded(filename, encodedFile));
        try {
            Response<UploadResponse> response = uploadResponseCall.execute();
            UploadResponse uploadResponse = response.body();
            httpStatus = HttpStatus.OK;
            assert uploadResponse != null;
            result.put("message", uploadResponse.getMessage());
            result.put("data", uploadResponse.getData());
        } catch (Exception exception) {
            exception.printStackTrace();
            httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
            result.put("message", exception.getMessage());
        }
        return new ResponseEntity<>(result, httpStatus);
    }
}
