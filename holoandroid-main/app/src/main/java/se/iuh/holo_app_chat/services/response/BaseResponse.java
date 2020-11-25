package se.iuh.holo_app_chat.services.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {
    @Expose
    @SerializedName("status") String status;
    @Expose
    @SerializedName("data") Object data;

}
