package se.iuh.holo_app_chat.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import se.iuh.holo_app_chat.services.response.UserResponse;

public interface DanhBaServiece {
    @GET("/users/{id}")
    Call<UserResponse> getUser(@Path(value = "id") String id);
    @GET("/users")
    Call<List<UserResponse>> getAll();
    @GET("/users/getUseByPhone/{phone}")
    Call<UserResponse> getUserByPhone(@Path(value = "phone") String phone);
}
