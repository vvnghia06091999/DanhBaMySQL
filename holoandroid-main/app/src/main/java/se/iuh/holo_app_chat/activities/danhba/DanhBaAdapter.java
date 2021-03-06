package se.iuh.holo_app_chat.activities.danhba ;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Comparator;
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


public class DanhBaAdapter extends RecyclerView.Adapter<DanhBaAdapter.DanhBaViewHoler> {
    private Context context;
    private List<UserResponse> listDB;
    private int id;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://54.255.234.239:3000");
        } catch (URISyntaxException e) {}
    }

    public DanhBaAdapter(int id,List<UserResponse> listDB,Context context){
        this.id = id;
        this.listDB= listDB;
        Collections.sort(this.listDB,new SapXepDanhBa());
        this.context=context;
    }

    @NonNull
    @Override
    public DanhBaViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.activity_danhbaholder,parent,false);
        DanhBaViewHoler danhBaViewHoler=new DanhBaViewHoler(view);
        mSocket.connect();
        return danhBaViewHoler;
    }

    @Override
    public void onBindViewHolder(@NonNull DanhBaAdapter.DanhBaViewHoler holder, int position) {
        UserResponse user = listDB.get(position);
        holder.tv_tenNguoiDung.setText(user.getFullName());
        holder.tv_ID.setText(user.getId()+"");
    }

    @Override
    public int getItemCount() {
        return listDB.size();
    }

    public class DanhBaViewHoler extends RecyclerView.ViewHolder{
        private TextView tv_tenNguoiDung,tv_ID;
        private ImageView tv_img;

        public DanhBaViewHoler(@NonNull final View itemView) {
            super(itemView);

            tv_tenNguoiDung = (TextView) itemView.findViewById(R.id.item_name);
            tv_img=(ImageView) itemView.findViewById(R.id.item_img);
            tv_ID=(TextView) itemView.findViewById(R.id.txt_id);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,tv_ID.getText(),Toast.LENGTH_SHORT).show();
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final String[] items = {"Trang cá nhân", "Chặn người này", "Xóa bạn"};
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    builder.setTitle(tv_tenNguoiDung.getText()+"");
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(which==0){
                                Intent i = new Intent(context,PersonalPage.class);
                                i.putExtra("idUser1",new String(id+""));
                                i.putExtra("idUser2",new String(tv_ID.getText()+""));
                                i.putExtra("status",new String("1")); // Huy ket Ban
                                context.startActivity(i);
                            }else if(which==1){

                            }else{
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setMessage("Bạn có muốn xóa bạn bè ?");
                                builder1.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                       deleteFriend(tv_ID.getText()+"");
                                        mSocket.emit("Update","Update");
                                    }
                                });
                                //Toast.makeText(PersonalPage.this,btn_huyKetBan.getText()+"",Toast.LENGTH_SHORT).show();
                                builder1.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                AlertDialog alertDialog = builder1.create();
                                alertDialog.show();
                            }
                        }
                    });
                    AlertDialog al=builder.create();
                    al.show();
                    return false;
                }
            });
        }
    }
    private void deleteFriend(final String idUser){
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
                        Toast.makeText(context,"Xoa ban thanh cong",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, DashboardActivity.class);
                        i.putExtra("gd",new String("DanhBa"));
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

    private class SapXepDanhBa implements Comparator<UserResponse>{

        @Override
        public int compare(UserResponse o1, UserResponse o2) {
            return o1.getFullName().compareTo(o2.getFullName());
        }
    }

}
