package se.iuh.holo_app_chat.services;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import se.iuh.holo_app_chat.services.response.LoginResponse;
import se.iuh.holo_app_chat.services.response.UserResponse;

public interface LoginService {

    @FormUrlEncoded
    @POST("auth/login/email")
    Call<LoginResponse> loginWithEmail(@Field("email") String email,
                                  @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/login/phone")
    Call<LoginResponse> loginWithPhone(@Field("phone") String phone,
                                  @Field("password") String password);

}
