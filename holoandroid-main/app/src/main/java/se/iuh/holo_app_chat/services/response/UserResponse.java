package se.iuh.holo_app_chat.services.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UserResponse extends BaseResponse{
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("fullName")
    private String fullName;
    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("photoURL")
    private String photoURL;
    @Expose
    @SerializedName("phone")
    private String phone;
    @Expose
    @SerializedName("gender")
    private boolean gender;
    @Expose
    @SerializedName("dob")
    private Date dob;
    @Expose
    @SerializedName("disabled")
    private boolean disabled;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
