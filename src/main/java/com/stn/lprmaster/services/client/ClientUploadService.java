package com.stn.lprmaster.services.client;

import com.stn.ester.core.exceptions.BadRequestException;
import com.stn.ester.helpers.FileHelper;
import com.stn.lprmaster.client.retrofit.ServiceGenerator;
import com.stn.lprmaster.client.request.UploadEncoded;
import com.stn.lprmaster.client.request.UploadViaUrl;
import com.stn.lprmaster.client.response.UploadResponse;
import com.stn.lprmaster.client.retrofit.service.UploadService;
import com.stn.lprmaster.misc.MasterNodeHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientUploadService {
    private UploadService uploadService;

    public ClientUploadService() {
        uploadService = ServiceGenerator.createBaseService(UploadService.class);
    }

    @Deprecated
    public ResponseEntity uploadRaw(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null) {
            throw new BadRequestException("No file provided.");
        }
        String filename = multipartFile.getOriginalFilename();
        String extensionFile = FileHelper.getExtensionFile(filename);
        String encodedBase64 = MasterNodeHelper.convertBytesToBase64(extensionFile, multipartFile.getBytes());
        Call<UploadResponse> uploadResponseCall = uploadService.uploadEncoded(new UploadEncoded(filename, encodedBase64));
        return doUpload(uploadResponseCall);
    }

    @Deprecated
    public ResponseEntity uploadEncoded(String filename, String encodedFile) {
        Call<UploadResponse> uploadResponseCall = uploadService.uploadEncoded(new UploadEncoded(filename, encodedFile));
        return doUpload(uploadResponseCall);
    }

    @Deprecated
    public ResponseEntity uploadViaUrl(List<String> url) {
        Call<UploadResponse> uploadResponseCall = uploadService.uploadViaUrl(new UploadViaUrl(url));
        return doUpload(uploadResponseCall);
    }

    @Deprecated
    private ResponseEntity doUpload(Call<UploadResponse> uploadResponseCall) {
        Map<String, Object> result = new HashMap<>();
        HttpStatus httpStatus;
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
