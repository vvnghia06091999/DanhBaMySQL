package se.iuh.holo_app_chat.services.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse extends BaseResponse {

    @Expose
    @SerializedName("userId") int userId;

    @Expose
    @SerializedName("fullName") String fullName;

    @Expose
    @SerializedName("token") String token;

    @Expose
    @SerializedName("refreshToken") String refreshToken;

    @Expose
    @SerializedName("photoURL") String photoURL;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
