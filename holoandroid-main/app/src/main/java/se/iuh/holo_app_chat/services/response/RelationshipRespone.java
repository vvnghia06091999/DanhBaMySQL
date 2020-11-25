package se.iuh.holo_app_chat.services.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RelationshipRespone {
    @Expose
    @SerializedName("idUser1")
    private int idUser1;
    @Expose
    @SerializedName("idUser2")
    private int idUser2;
    @Expose
    @SerializedName("idUserAction")
    private int idUserAction;
    @Expose
    @SerializedName("status")
    private String status;

    public int getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(int idUser1) {
        this.idUser1 = idUser1;
    }

    public int getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(int idUser2) {
        this.idUser2 = idUser2;
    }

    public int getIdUserAction() {
        return idUserAction;
    }

    public void setIdUserAction(int idUserAction) {
        this.idUserAction = idUserAction;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) {
        this.status = status;
    }
}
