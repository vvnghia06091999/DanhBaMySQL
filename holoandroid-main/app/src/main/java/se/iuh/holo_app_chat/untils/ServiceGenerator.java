package se.iuh.holo_app_chat.untils;

import androidx.annotation.NonNull;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.iuh.holo_app_chat.services.interceptor.TokenAuthenticator;

public class ServiceGenerator {
    private static final String BASE_URL = "http://13.229.79.81/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass)
    {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit= builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken)
    {

        if(authToken!=null){
            httpClient.addInterceptor(new Interceptor() {
                @NonNull
                @Override
                public Response intercept(@NonNull Chain chain) throws IOException {
                    Request original = chain.request();
                    Request request = new Request.Builder().header("Authorization", "Bearer " + authToken)
                            .url(original.url())
                            .method(original.method(), original.body()).build();
                    return chain.proceed(request);
                }
            });
            httpClient.addInterceptor(new TokenAuthenticator());
        }
        OkHttpClient client = httpClient.build();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }
}
