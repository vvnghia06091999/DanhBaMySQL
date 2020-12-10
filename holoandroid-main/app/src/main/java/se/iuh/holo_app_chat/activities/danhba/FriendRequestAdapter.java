package se.iuh.holo_app_chat.activities.danhba;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.iuh.holo_app_chat.R;
import se.iuh.holo_app_chat.activities.dashboard.DashboardActivity;
import se.iuh.holo_app_chat.services.RelationshipServiece;
import se.iuh.holo_app_chat.services.response.RelationshipRespone;
import se.iuh.holo_app_chat.services.response.UserResponse;
import se.iuh.holo_app_chat.untils.ServieceDanhBa;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder>{
    private Context context;
    private List<UserResponse> listDB;
    private int id;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://54.255.234.239:3000");
        } catch (URISyntaxException e) {}
    }

    public FriendRequestAdapter(Context context, List<UserResponse> listDB, int id) {
        this.context = context;
        this.listDB = listDB;
        this.id = id;
        mSocket.connect();
    }

    @NonNull
    @Override
    public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_friend_request,parent,false);
        FriendRequestViewHolder friendRequestViewHolder = new FriendRequestViewHolder(view);
        return friendRequestViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestViewHolder holder, int position) {
        UserResponse userResponse = listDB.get(position);
        holder.txt_tenNguoiDung.setText(userResponse.getFullName());
        holder.txt_id.setText(userResponse.getId()+"");
    }

    @Override
    public int getItemCount() {
        return listDB.size();
    }

    public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_tenNguoiDung,txt_id;
        private ImageView img_avatar;
        private Button btn_chapNhan,btn_xoa;

        public FriendRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_tenNguoiDung = (TextView) itemView.findViewById(R.id.item_tenAF);
            img_avatar=(ImageView) itemView.findViewById(R.id.item_imgAF);
            txt_id=(TextView) itemView.findViewById(R.id.txt_IDAF);
            btn_chapNhan=(Button) itemView.findViewById(R.id.btn_chapNhanAF);
            btn_xoa=(Button) itemView.findViewById(R.id.btn_xoaAF);

            btn_chapNhan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateRelationship(txt_id.getText()+"");
                    mSocket.emit("Update","Update");
                    mSocket.emit("ChapNhanKetBan",txt_id.getText().toString());
                }
            });

            btn_xoa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteRelationship(txt_id.getText()+"");
                    mSocket.emit("Update","Update");
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,PersonalPage.class);
                    i.putExtra("idUser1",new String(id+""));
                    i.putExtra("idUser2",new String(txt_id.getText()+""));
                    i.putExtra("status",new String("3")); // Chap Nhan Ket Ban
                    context.startActivity(i);
                }
            });

        }
    }

    private void updateRelationship(String idUser){
        final RelationshipServiece relationshipServiece = ServieceDanhBa.createService(RelationshipServiece.class);
        Call<RelationshipRespone> relationshipResponeCall = relationshipServiece.getRelationship(id+"",idUser);
        relationshipResponeCall.enqueue(new Callback<RelationshipRespone>() {
            @Override
            public void onResponse(Call<RelationshipRespone> call, Response<RelationshipRespone> response) {
                RelationshipRespone relationshipRespone = response.body();
                Call<Void> voidCall = relationshipServiece.updateReltionship(relationshipRespone.getIdUser1()+"",
                                                                            relationshipRespone.getIdUser2()+"",id+"","1");
                voidCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context,"Them thanh cong",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, AddFriend.class);
                        i.putExtra("id",new String(id+""));
                        context.startActivity(i);
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
    private void deleteRelationship(final String idUser){
        final RelationshipServiece relationshipServiece = ServieceDanhBa.createService(RelationshipServiece.class);
        Call<RelationshipRespone> relationshipResponeCall = relationshipServiece.getRelationship(id+"",idUser);
        relationshipResponeCall.enqueue(new Callback<RelationshipRespone>() {
            @Override
            public void onResponse(Call<RelationshipRespone> call, Response<RelationshipRespone> response) {
                RelationshipRespone relationshipRespone = response.body();
                Call<Void> voidCall = relationshipServiece.deleteFriend(relationshipRespone.getIdUser1()+"",relationshipRespone.getIdUser2()+"");
                voidCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(context,"Xoa thanh cong",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, AddFriend.class);
                        i.putExtra("id",new String(id+""));
                        context.startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<RelationshipRespone> call, Throwable t) {
            }
        });

    }
}
