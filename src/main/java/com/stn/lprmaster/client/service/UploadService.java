package com.stn.lprmaster.client.service;

import com.stn.lprmaster.client.endpoint.Endpoint;
import com.stn.lprmaster.client.request.UploadEncoded;
import com.stn.lprmaster.client.request.UploadViaUrl;
import com.stn.lprmaster.client.response.UploadResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UploadService {

    @POST(Endpoint.uploadEncodedURL)
    Call<UploadResponse> uploadEncoded(@Body UploadEncoded uploadEncodedList);

    @POST(Endpoint.uploadViaURLURL)
    Call<UploadResponse> uploadViaUrl(@Body UploadViaUrl uploadViaUrl);
}
