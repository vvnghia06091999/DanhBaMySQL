package se.iuh.holo_app_chat.untils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServieceDanhBa {
    private static final String BASE_URL = "http://54.255.234.239:3000";
    //private static final String BASE_URL = "http://192.168.56.1:3000";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static <S> S createService(Class<S> serviceClass)
    {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit= builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
