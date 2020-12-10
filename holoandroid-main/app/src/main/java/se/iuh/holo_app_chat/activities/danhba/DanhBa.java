package se.iuh.holo_app_chat.activities.danhba;

public class DanhBa {
    private String ten, soDienThoai;

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public DanhBa(String ten, String soDienThoai) {
        this.ten = ten;
        this.soDienThoai = soDienThoai;
    }

    @Override
    public String toString() {
        return "DanhBa{" +
                "ten='" + ten + '\'' +
                ", soDienThoai='" + soDienThoai + '\'' +
                '}';
    }
}
