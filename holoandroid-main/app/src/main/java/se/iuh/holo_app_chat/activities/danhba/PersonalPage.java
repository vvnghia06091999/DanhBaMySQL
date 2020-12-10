package se.iuh.holo_app_chat.activities.danhba;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

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
    private ImageView img_anhBia,img_anhDaiDien;
    private Button btn_nhanTin,btn_ketBan,btn_huyKetBan,btn_chapNhanKetBan,btn_HuyYeuCau;
    private String idUser1,idUser2,status;
    private NotificationManagerCompat notificationManagerCompat;
    private Toolbar toolbar;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://54.255.234.239:3000");
        } catch (URISyntaxException e) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        notificationManagerCompat = NotificationManagerCompat.from(this);
        mSocket.on("GuiKetBan", GuiKetBan);
        mSocket.connect();
        mSocket.on("Server", Sever);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_page);

        toolbar=(Toolbar) findViewById(R.id.app_bar_idAF);
        toolbar.setTitle("Trang cá nhân");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        txt_tenNGuoiDung=(TextView) findViewById(R.id.txt_tenNDPP);
        txt_gioiTinh=(TextView) findViewById(R.id.txt_GioiTinhPP);
        txt_ngaySinh=(TextView) findViewById(R.id.txt_NgaySinhPP);
        txt_email=(TextView) findViewById(R.id.txt_EmailPP);

        img_anhBia=(ImageView) findViewById(R.id.img_anhBiaPP);
        img_anhDaiDien=(ImageView) findViewById(R.id.img_avatarPP);

        btn_nhanTin=(Button) findViewById(R.id.btn_NhanTinPP);
        btn_ketBan=(Button) findViewById(R.id.btn_KetBanPP);
        btn_huyKetBan=(Button) findViewById(R.id.btn_HuyKetBanPP);
        btn_HuyYeuCau=(Button) findViewById(R.id.btn_HuyYeuCau);
        btn_chapNhanKetBan = (Button) findViewById(R.id.btn_ChapNhanKB);

        idUser1 = getIntent().getExtras().getString("idUser1");
        idUser2 = getIntent().getExtras().getString("idUser2");
        status = getIntent().getExtras().getString("status");

        initButton(status);
        init();

        btn_huyKetBan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PersonalPage.this);
                builder.setMessage("Bạn có muốn xóa bạn bè ?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRelationship();
                    }
                });
                //Toast.makeText(PersonalPage.this,btn_huyKetBan.getText()+"",Toast.LENGTH_SHORT).show();
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
                        mSocket.emit("Update","Update");
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
                        mSocket.emit("Update","Update");
                        mSocket.emit("ChapNhanKetBan",idUser2);
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
                mSocket.emit("Update","Update");
                mSocket.emit("GuiKetBan",idUser2);
            }

            @Override
            public void onFailure(Call<RelationshipRespone> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if(status.equals("1"))
                    finish();
                else{
                    Intent i = new Intent(PersonalPage.this, AddFriend.class);
                    i.putExtra("id",new String(idUser1+""));
                    startActivity(i);
                }
                return true;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    private Emitter.Listener Sever = new Emitter.Listener() {

        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final RelationshipServiece relationshipServiece = ServieceDanhBa.createService(RelationshipServiece.class);
                    Call<RelationshipRespone> relationshipResponeCall = relationshipServiece.getRelationship(idUser1,idUser2);
                    relationshipResponeCall.enqueue(new Callback<RelationshipRespone>() {
                        @Override
                        public void onResponse(Call<RelationshipRespone> call, Response<RelationshipRespone> response) {

                            RelationshipRespone relationshipRespone = response.body();
                            initButton(relationshipRespone.getStatus());
                        }

                        @Override
                        public void onFailure(Call<RelationshipRespone> call, Throwable t) {
                            //Toast.makeText(AddFriend.this,t.getMessage()+"ERROR",Toast.LENGTH_LONG).show();
                            initButton("0");
                        }
                    });
                }
            });
        }
    };

    private void initButton(String status){
        if (status.equals("0")){
            btn_huyKetBan.setVisibility(View.INVISIBLE);
            btn_ketBan.setVisibility(View.VISIBLE);
            btn_HuyYeuCau.setVisibility(View.INVISIBLE);
            btn_chapNhanKetBan.setVisibility(View.INVISIBLE);
        }else  if (status.equals("1")){
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
    }

    private Emitter.Listener GuiKetBan = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String idu;
                    try {
                        idu = data.getString("tinNhan");
                        if(idu.equals(idUser1+"")){
                            // THong bao co loi moi ket ban
                            notifi();
                        }
                    } catch ( JSONException e) {
                        return;
                    }
                }
            });
        }
    };

    private void notifi(){
        String title = "Thông Báo";
        String me = "Có lời mời kết bạn mới!";
        Intent i = new Intent(this, AddFriend.class);
        i.putExtra("id",idUser1+"");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, null)
                .setSmallIcon(R.drawable.ic_baseline_add_24)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(me).setPriority(NotificationCompat.PRIORITY_HIGH).build();
        notificationManagerCompat.notify(0,notification);
    }

}