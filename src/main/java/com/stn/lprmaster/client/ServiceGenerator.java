package com.stn.lprmaster.client;

import com.stn.lprmaster.misc.ConstantValue;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class ServiceGenerator {

    @Value("${client.base-url}")
    private String baseUrlTemp;
    private static String baseUrl;

    @PostConstruct
    public void initBaseUrl() {
        ServiceGenerator.baseUrl = baseUrlTemp;
    }

    private static OkHttpClient.Builder builder() {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient().newBuilder();
        okhttpBuilder.connectTimeout(ConstantValue.TIMEOUT, TimeUnit.SECONDS);
        okhttpBuilder.writeTimeout(ConstantValue.TIMEOUT, TimeUnit.SECONDS);
        okhttpBuilder.readTimeout(ConstantValue.TIMEOUT, TimeUnit.SECONDS);
        return okhttpBuilder;
    }

    private static HttpLoggingInterceptor interceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    public static <S> S createBaseService(Class<S> serviceClass) {
        OkHttpClient.Builder builder = builder();
        builder.addInterceptor(interceptor());

        builder.addInterceptor(chain -> {
            Request request = chain.request();
            Request newReq = request.newBuilder()
                    .header("Accept", "application/json")
                    .build();
            return chain.proceed(newReq);
        });

        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(serviceClass);
    }
}
