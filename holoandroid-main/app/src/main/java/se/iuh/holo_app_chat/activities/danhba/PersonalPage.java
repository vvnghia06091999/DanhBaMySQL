package se.iuh.holo_app_chat.activities.danhba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.iuh.holo_app_chat.R;
import se.iuh.holo_app_chat.activities.dashboard.DashboardActivity;
import se.iuh.holo_app_chat.services.DanhBaServiece;
import se.iuh.holo_app_chat.services.RelationshipServiece;
import se.iuh.holo_app_chat.services.response.RelationshipRespone;
import se.iuh.holo_app_chat.services.response.UserResponse;
import se.iuh.holo_app_chat.untils.ServieceDanhBa;

public class PersonalPage extends AppCompatActivity {
    private TextView txt_tenNGuoiDung,txt_gioiTinh,txt_ngaySinh,txt_email;
    private ImageView img_backPP,img_anhBia,img_anhDaiDien;
    private Button btn_nhanTin,btn_ketBan,btn_huyKetBan,btn_chapNhanKetBan,btn_HuyYeuCau;
    private String idUser1,idUser2,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_page);

        txt_tenNGuoiDung=(TextView) findViewById(R.id.txt_tenNDPP);
        txt_gioiTinh=(TextView) findViewById(R.id.txt_GioiTinhPP);
        txt_ngaySinh=(TextView) findViewById(R.id.txt_NgaySinhPP);
        txt_email=(TextView) findViewById(R.id.txt_EmailPP);

        img_anhBia=(ImageView) findViewById(R.id.img_anhBiaPP);
        img_anhDaiDien=(ImageView) findViewById(R.id.img_avatarPP);
        img_backPP=(ImageView) findViewById(R.id.img_backPP);

        btn_nhanTin=(Button) findViewById(R.id.btn_NhanTinPP);
        btn_ketBan=(Button) findViewById(R.id.btn_KetBanPP);
        btn_huyKetBan=(Button) findViewById(R.id.btn_HuyKetBanPP);
        btn_HuyYeuCau=(Button) findViewById(R.id.btn_HuyYeuCau);
        btn_chapNhanKetBan = (Button) findViewById(R.id.btn_ChapNhanKB);

        idUser1 = getIntent().getExtras().getString("idUser1");
        idUser2 = getIntent().getExtras().getString("idUser2");
        status = getIntent().getExtras().getString("status");
        img_backPP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PersonalPage.this, AddFriend.class);
                i.putExtra("id",new String(idUser1+""));
                startActivity(i);
            }
        });
        if (status.equals("0")){
            btn_huyKetBan.setVisibility(View.INVISIBLE);
            btn_ketBan.setVisibility(View.VISIBLE);
            btn_HuyYeuCau.setVisibility(View.INVISIBLE);
            btn_chapNhanKetBan.setVisibility(View.INVISIBLE);
        }else  if (status.equals("1")){
            img_backPP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(PersonalPage.this, DashboardActivity.class);
                    i.putExtra("gd",new String("DanhBa"));
                    startActivity(i);
                }
            });
            btn_huyKetBan.setVisibility(View.VISIBLE);
            btn_ketBan.setVisibility(View.INVISIBLE);
            btn_HuyYeuCau.setVisibility(View.INVISIBLE);
            btn_chapNhanKetBan.setVisibility(View.INVISIBLE);
        }else  if (status.equals("2")){
            btn_huyKetBan.setVisibility(View.INVISIBLE);
            btn_ketBan.setVisibility(View.INVISIBLE);
            btn_HuyYeuCau.setVisibility(View.VISIBLE);
            btn_chapNhanKetBan.setVisibility(View.INVISIBLE);
        }else  if (status.equals("3")){
            btn_huyKetBan.setVisibility(View.INVISIBLE);
            btn_ketBan.setVisibility(View.INVISIBLE);
            btn_HuyYeuCau.setVisibility(View.INVISIBLE);
            btn_chapNhanKetBan.setVisibility(View.VISIBLE);
        }
        init();
        btn_huyKetBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PersonalPage.this,btn_huyKetBan.getText()+"",Toast.LENGTH_SHORT).show();
                deleteRelationship();
            }
        });
        btn_ketBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PersonalPage.this,btn_ketBan.getText()+"",Toast.LENGTH_SHORT).show();
                createRelationship();
            }
        });
        btn_chapNhanKetBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PersonalPage.this,btn_chapNhanKetBan.getText()+"",Toast.LENGTH_SHORT).show();
                updateRelationship();
            }
        });
        btn_HuyYeuCau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(PersonalPage.this,btn_HuyYeuCau.getText()+"",Toast.LENGTH_SHORT).show();
                deleteRelationship();
            }
        });
    }

    private void init(){
        DanhBaServiece danhBaServiece = ServieceDanhBa.createService(DanhBaServiece.class);
        Call<UserResponse> userResponseCall = danhBaServiece.getUser(idUser2);
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                txt_tenNGuoiDung.setText(userResponse.getFullName());
                if (userResponse.isGender())
                    txt_gioiTinh.setText("Nam");
                else
                    txt_gioiTinh.setText("Nữ");
                txt_email.setText(userResponse.getEmail());
                txt_ngaySinh.setText(userResponse.getDob().toString());
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }
    private void deleteRelationship(){
        final RelationshipServiece relationshipServiece = ServieceDanhBa.createService(RelationshipServiece.class);
        Call<RelationshipRespone> relationshipResponeCall = relationshipServiece.getRelationship(idUser1+"",idUser2+"");
        relationshipResponeCall.enqueue(new Callback<RelationshipRespone>() {
            @Override
            public void onResponse(Call<RelationshipRespone> call, Response<RelationshipRespone> response) {
                RelationshipRespone relationshipRespone = response.body();
                Call<Void> voidCall = relationshipServiece.deleteFriend(relationshipRespone.getIdUser1()+"",relationshipRespone.getIdUser2()+"");
                voidCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        //Toast.makeText(PersonalPage.this,"Xoa ban thanh cong",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(PersonalPage.this,PersonalPage.class);
                        i.putExtra("idUser1",new String(idUser1+""));
                        i.putExtra("idUser2",new String(idUser2+""));
                        i.putExtra("status",new String("0")); // Ket Ban
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(PersonalPage.this,t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<RelationshipRespone> call, Throwable t) {

            }
        });
    }
    private void updateRelationship(){
        final RelationshipServiece relationshipServiece = ServieceDanhBa.createService(RelationshipServiece.class);
        Call<RelationshipRespone> relationshipResponeCall = relationshipServiece.getRelationship(idUser1+"",idUser2+"");
        relationshipResponeCall.enqueue(new Callback<RelationshipRespone>() {
            @Override
            public void onResponse(Call<RelationshipRespone> call, Response<RelationshipRespone> response) {
                RelationshipRespone relationshipRespone = response.body();
                Call<Void> voidCall = relationshipServiece.updateReltionship(relationshipRespone.getIdUser1()+"",
                        relationshipRespone.getIdUser2()+"",idUser1+"","1");
                voidCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                       // Toast.makeText(context,"Them thanh cong",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(PersonalPage.this,PersonalPage.class);
                        i.putExtra("idUser1",new String(idUser1+""));
                        i.putExtra("idUser2",new String(idUser2+""));
                        i.putExtra("status",new String("1")); //Huy Ket Ban
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onFailure(Call<RelationshipRespone> call, Throwable t) {

            }
        });
    }
    private void createRelationship(){
        RelationshipServiece relationshipServiece = ServieceDanhBa.createService(RelationshipServiece.class);
        Call<RelationshipRespone> relationshipResponeCall = relationshipServiece.createReltionship(idUser1+"",idUser2+"",idUser1+"","2");
        relationshipResponeCall.enqueue(new Callback<RelationshipRespone>() {
            @Override
            public void onResponse(Call<RelationshipRespone> call, Response<RelationshipRespone> response) {
                RelationshipRespone relationshipRespone = response.body();
                Intent i = new Intent(PersonalPage.this,PersonalPage.class);
                i.putExtra("idUser1",new String(idUser1+""));
                i.putExtra("idUser2",new String(idUser2+""));
                i.putExtra("status",new String("2")); //Huy Yeu Cau
                startActivity(i);
            }

            @Override
            public void onFailure(Call<RelationshipRespone> call, Throwable t) {

            }
        });
    }
}