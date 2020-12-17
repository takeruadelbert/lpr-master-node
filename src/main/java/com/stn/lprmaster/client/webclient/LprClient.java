package com.stn.lprmaster.client.webclient;

import com.stn.ester.core.exceptions.BadRequestException;
import com.stn.ester.helpers.FileHelper;
import com.stn.lprmaster.client.endpoint.Endpoint;
import com.stn.lprmaster.client.request.UploadEncoded;
import com.stn.lprmaster.client.request.UploadViaUrl;
import com.stn.lprmaster.client.response.UploadResponse;
import com.stn.lprmaster.misc.MasterNodeHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class LprClient {
    private static final String BASE_URL = "http://stream-to-frame-service";

    @Autowired
    private RestTemplate restTemplate;

    public UploadResponse uploadRaw(MultipartFile multipartFile) {
        try {
            if (multipartFile == null) {
                throw new BadRequestException("No file provided.");
            }
            String filename = multipartFile.getOriginalFilename();
            String extensionFile = FileHelper.getExtensionFile(filename);
            String encodedBase64 = MasterNodeHelper.convertBytesToBase64(extensionFile, multipartFile.getBytes());
            String url = String.format("%s%s", BASE_URL, Endpoint.uploadEncodedURL);
            return restTemplate.postForObject(url, new UploadEncoded(filename, encodedBase64), UploadResponse.class);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    public UploadResponse uploadEncoded(String filename, String encodedFile) {
        try {
            String url = String.format("%s%s", BASE_URL, Endpoint.uploadEncodedURL);
            return restTemplate.postForObject(url, new UploadEncoded(filename, encodedFile), UploadResponse.class);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    public Object uploadViaUrl(List<String> url) {
        try {
            String hostUrl = String.format("%s%s", BASE_URL, Endpoint.uploadViaURLURL);
            return restTemplate.postForObject(hostUrl, new UploadViaUrl(url), UploadResponse.class);
        } catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }
}
