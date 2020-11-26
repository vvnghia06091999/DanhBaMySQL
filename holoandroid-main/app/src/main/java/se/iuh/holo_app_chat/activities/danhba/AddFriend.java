package se.iuh.holo_app_chat.activities.danhba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
    private Button btn_tim;
    private EditText edt_nhapSDT;
    private RecyclerView rv_listRequest;
    private List<UserResponse> userResponses = new ArrayList<>() ;
    private List<RelationshipRespone> relationshipRespones = new ArrayList<>();
    private FriendRequestAdapter friendRequestAdapter;
    private int id;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        toolbar=(Toolbar) findViewById(R.id.app_bar_idPP);
        toolbar.setTitle("Thêm Bạn Bè");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv_listRequest=(RecyclerView) findViewById(R.id.rv_listViewAF);
        rv_listRequest.setHasFixedSize(true);
        rv_listRequest.setLayoutManager(new LinearLayoutManager(AddFriend.this));
        btn_tim=(Button) findViewById(R.id.btn_tim);
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

    }

    private void initList(final int id) {
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
                return true;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }
}