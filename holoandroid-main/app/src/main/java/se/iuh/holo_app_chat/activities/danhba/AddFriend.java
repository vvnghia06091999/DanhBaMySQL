package se.iuh.holo_app_chat.activities.danhba;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

public class AddFriend extends AppCompatActivity {
    private Button btn_tim,btn_timdanhba;
    private EditText edt_nhapSDT;
    private RecyclerView rv_listRequest,rv_timTuDanhBa;
    private List<UserResponse> userResponses;
    private List<RelationshipRespone> relationshipRespones;
    private FriendRequestAdapter friendRequestAdapter;
    private List<DanhBa> danhBas = new ArrayList<>();
    private TimDanhBaAdapter timDanhBaAdapter;
    private List<String> tenDB =new ArrayList<>();
    private List<UserResponse> UR;
    private NotificationManagerCompat notificationManagerCompat;
    private int id;
    private Toolbar toolbar;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://54.255.234.239:3000");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationManagerCompat = NotificationManagerCompat.from(this);
        setContentView(R.layout.activity_add_friend);
        mSocket.on("ChapNhanKetBan", ChapNhanKetBan);
        mSocket.on("GuiKetBan", GuiKetBan);
        mSocket.on("Server", Sever);
        mSocket.connect();
        toolbar=(Toolbar) findViewById(R.id.app_bar_idPP);
        toolbar.setTitle("Thêm Bạn Bè");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv_listRequest=(RecyclerView) findViewById(R.id.rv_listViewAF);
        rv_listRequest.setHasFixedSize(true);
        rv_listRequest.setLayoutManager(new LinearLayoutManager(AddFriend.this));
        btn_tim=(Button) findViewById(R.id.btn_tim);
        btn_timdanhba=(Button) findViewById(R.id.btn_timdanhba);
        edt_nhapSDT=(EditText) findViewById(R.id.edt_nhapSdt);
        String stid =  getIntent().getExtras().getString("id");
        id = Integer.parseInt(stid);
        initList(id);
        btn_tim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edt_nhapSDT.getText()+"";
                //Toast.makeText(AddFriend.this,phone+"",Toast.LENGTH_LONG).show();
                getUserByPhone(phone);
            }
        });
        btn_timdanhba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(AddFriend.this);
//                View view = getLayoutInflater().inflate(R.layout.dialog_danhba,null);
//                rv_timTuDanhBa =(RecyclerView) view.findViewById(R.id.rv_timdanhBa);
//                builder.setTitle("Gợi ý từ danh bạ");
//                builder.setView(view);
//                UR = new ArrayList<>();
//                DanhBaServiece danhBaServiece = ServieceDanhBa.createService(DanhBaServiece.class);
//                Call<List<UserResponse>> userResponseCall = danhBaServiece.getAll();
//                userResponseCall.enqueue(new Callback<List<UserResponse>>() {
//                    @Override
//                    public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
//                        UR = response.body();
//                        //builder.setMessage(UR.size()+"");
////                        Cursor  cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
////                                null,null,null,null);
////                        while (cursor.moveToNext()){
////                            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
////                            String sdt = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
////                            DanhBa db = new DanhBa(name,sdt);
////                            danhBas.add(db);
////                        }
//                        timDanhBaAdapter = new TimDanhBaAdapter(id,UR,danhBas,AddFriend.this);
//                        Toast.makeText(AddFriend.this,UR.size()+"",Toast.LENGTH_LONG).show();
//                        rv_timTuDanhBa.setAdapter(timDanhBaAdapter);
//                        AlertDialog alertDialog = builder.create();
//                        alertDialog.show();
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<UserResponse>> call, Throwable t) {
//                        builder.setMessage("Không tìm thấy liên lạc");
//                        AlertDialog alertDialog = builder.create();
//                        alertDialog.show();
//                    }
//                });
//
//            }
//        });
//                Intent i = new Intent(AddFriend.this, TimDanhBaActivity.class);
//                i.putExtra("id", "1");
//                startActivity(i);
            }
        });
    }


    private void initList(final int id) {
        userResponses = new ArrayList<>();
        relationshipRespones = new ArrayList<>();
        RelationshipServiece relationshipServiece = ServieceDanhBa.createService(RelationshipServiece.class);
        final Call<List<RelationshipRespone>> relationshipResponeCall = relationshipServiece.getFriendRequets(id+"");
        relationshipResponeCall.enqueue(new Callback<List<RelationshipRespone>>() {
            @Override
            public void onResponse(Call<List<RelationshipRespone>> call, Response<List<RelationshipRespone>> response) {
                if(response.isSuccessful()){
                    relationshipRespones = response.body();
                    DanhBaServiece danhBaServiece = ServieceDanhBa.createService(DanhBaServiece.class);
                    for (int i = 0; i < relationshipRespones.size();i++){
                        if(id != relationshipRespones.get(i).getIdUser1()){
                            Call<UserResponse> userResponseCall = danhBaServiece.getUser(relationshipRespones.get(i).getIdUser1()+"");
                            userResponseCall.enqueue(new Callback<UserResponse>() {
                                @Override
                                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                    UserResponse userResponse = response.body();
                                    if(userResponses.add(userResponse)){
                                        friendRequestAdapter = new FriendRequestAdapter(AddFriend.this,userResponses,id);
                                        rv_listRequest.setAdapter(friendRequestAdapter);
                                        friendRequestAdapter.notifyDataSetChanged();
                                    }
                                }
                                @Override
                                public void onFailure(Call<UserResponse> call, Throwable t) {

                                }
                            });
                        }else{
                            final Call<UserResponse> userResponseCall = danhBaServiece.getUser(relationshipRespones.get(i).getIdUser2()+"");
                            userResponseCall.enqueue(new Callback<UserResponse>() {
                                @Override
                                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                    UserResponse userResponse = response.body();
                                    if(userResponses.add(userResponse)){
                                        //Toast.makeText(AddFriend.this,userResponses.size()+"",Toast.LENGTH_SHORT).show();
                                        friendRequestAdapter = new FriendRequestAdapter(AddFriend.this,userResponses,id);
                                        rv_listRequest.setAdapter(friendRequestAdapter);
                                        friendRequestAdapter.notifyDataSetChanged();
                                    }
                                }
                                @Override
                                public void onFailure(Call<UserResponse> call, Throwable t) {

                                }
                            });
                        }
                    }

                }
                else{
                    Toast.makeText(AddFriend.this,"Khong Co Ban Be",Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<List<RelationshipRespone>> call, Throwable t) {
                Toast.makeText(AddFriend.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUserByPhone(String phone){
        DanhBaServiece danhBaServiece = ServieceDanhBa.createService(DanhBaServiece.class);
        Call<UserResponse> userResponseCall = danhBaServiece.getUserByPhone(phone+"");
        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse == null){
                    Toast.makeText(AddFriend.this,"Khong Tim Thay",Toast.LENGTH_LONG).show();
                }else{
                    //Toast.makeText(AddFriend.this,userResponse.getFullName()+"",Toast.LENGTH_LONG).show();
                    getPersonalPage(userResponse.getId()+"");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }
    private void getPersonalPage(final String idUser){
        //Toast.makeText(AddFriend.this,idUser+"KKKK",Toast.LENGTH_LONG).show();
        final RelationshipServiece relationshipServiece = ServieceDanhBa.createService(RelationshipServiece.class);
        Call<RelationshipRespone> relationshipResponeCall = relationshipServiece.getRelationship(id+"",idUser);
        relationshipResponeCall.enqueue(new Callback<RelationshipRespone>() {
            @Override
            public void onResponse(Call<RelationshipRespone> call, Response<RelationshipRespone> response) {
                
                    RelationshipRespone relationshipRespone = response.body();
                    if(relationshipRespone.getStatus().equals("1")){
                    Intent i = new Intent(AddFriend.this,PersonalPage.class);
                    i.putExtra("idUser1",new String(id+""));
                    i.putExtra("idUser2",new String(idUser+""));
                    i.putExtra("status",new String("1")); // Huy ket Ban
                    startActivity(i);
                }else if(relationshipRespone.getStatus().equals("2")&&relationshipRespone.getIdUserAction()==id){
                    Intent i = new Intent(AddFriend.this,PersonalPage.class);
                    i.putExtra("idUser1",new String(id+""));
                    i.putExtra("idUser2",new String(idUser+""));
                    i.putExtra("status",new String("2")); //Huy Yeu Cau
                    startActivity(i);
                }else if(relationshipRespone.getStatus().equals("2")&&relationshipRespone.getIdUserAction()!=id){
                    Intent i = new Intent(AddFriend.this,PersonalPage.class);
                    i.putExtra("idUser1",new String(id+""));
                    i.putExtra("idUser2",new String(idUser+""));
                    i.putExtra("status",new String("3")); // Chap Nhan Ket Ban
                    startActivity(i);
                    }

            }

            @Override
            public void onFailure(Call<RelationshipRespone> call, Throwable t) {
                //Toast.makeText(AddFriend.this,t.getMessage()+"ERROR",Toast.LENGTH_LONG).show();
                Intent i = new Intent(AddFriend.this,PersonalPage.class);
                i.putExtra("idUser1",new String(id+""));
                i.putExtra("idUser2",new String(idUser+""));
                i.putExtra("status",new String("0")); // Ket Ban
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                //home la id mac dinh cho nut quay lai onBackPressed quay lai trang luc nãy
                Intent i = new Intent(AddFriend.this, DashboardActivity.class);
                i.putExtra("gd",new String("DanhBa"));
                startActivity(i);
                //finish();
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
                    relationshipRespones = new ArrayList<>();
                    userResponses = new ArrayList<>();
                    RelationshipServiece relationshipServiece = ServieceDanhBa.createService(RelationshipServiece.class);
                    final Call<List<RelationshipRespone>> relationshipResponeCall = relationshipServiece.getFriendRequets(id+"");
                    relationshipResponeCall.enqueue(new Callback<List<RelationshipRespone>>() {
                        @Override
                        public void onResponse(Call<List<RelationshipRespone>> call, Response<List<RelationshipRespone>> response) {
                            if(response.isSuccessful()){
                                relationshipRespones = response.body();
                                DanhBaServiece danhBaServiece = ServieceDanhBa.createService(DanhBaServiece.class);
                                for (int i = 0; i < relationshipRespones.size();i++){
                                    if(id != relationshipRespones.get(i).getIdUser1()){
                                        Call<UserResponse> userResponseCall = danhBaServiece.getUser(relationshipRespones.get(i).getIdUser1()+"");
                                        userResponseCall.enqueue(new Callback<UserResponse>() {
                                            @Override
                                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                                UserResponse userResponse = response.body();
                                                if(userResponses.add(userResponse)){
                                                    friendRequestAdapter = new FriendRequestAdapter(AddFriend.this,userResponses,id);
                                                    rv_listRequest.setAdapter(friendRequestAdapter);
                                                    friendRequestAdapter.notifyDataSetChanged();
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<UserResponse> call, Throwable t) {

                                            }
                                        });
                                    }else{
                                        final Call<UserResponse> userResponseCall = danhBaServiece.getUser(relationshipRespones.get(i).getIdUser2()+"");
                                        userResponseCall.enqueue(new Callback<UserResponse>() {
                                            @Override
                                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                                UserResponse userResponse = response.body();
                                                if(userResponses.add(userResponse)){
                                                    //Toast.makeText(AddFriend.this,userResponses.size()+"",Toast.LENGTH_SHORT).show();
                                                    friendRequestAdapter = new FriendRequestAdapter(AddFriend.this,userResponses,id);
                                                    rv_listRequest.setAdapter(friendRequestAdapter);
                                                    friendRequestAdapter.notifyDataSetChanged();
                                                }
                                            }
                                            @Override
                                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                            }
                                        });
                                    }
                                }
                            }
                            else{
                                Toast.makeText(AddFriend.this,"Khong Co Ban Be",Toast.LENGTH_LONG).show();
                            }

                        }
                        @Override
                        public void onFailure(Call<List<RelationshipRespone>> call, Throwable t) {
                            Toast.makeText(AddFriend.this,t.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    };

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
                        if(idu.equals(id+"")){
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
    private Emitter.Listener ChapNhanKetBan = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String idu;
                    try {
                        idu = data.getString("tinNhan");
                        if(idu.equals(id+"")){
                            // THong bao co nguoi chap nhan ket ban
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
        i.putExtra("id",id+"");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, null)
                .setSmallIcon(R.drawable.ic_baseline_add_24)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(me).setPriority(NotificationCompat.PRIORITY_HIGH).build();
        notificationManagerCompat.notify(0,notification);
    }
}