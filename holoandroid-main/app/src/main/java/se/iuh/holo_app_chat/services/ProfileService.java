package se.iuh.holo_app_chat.services;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import se.iuh.holo_app_chat.services.response.LoginResponse;
import se.iuh.holo_app_chat.services.response.UserResponse;

public interface ProfileService {
    @GET("profile")
    Call<UserResponse> getUser();

    @FormUrlEncoded
    @POST("auth/refreshToken")
    Call<LoginResponse> refreshToken(@Field("refreshToken") String refreshToken);
}
