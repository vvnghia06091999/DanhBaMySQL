package se.iuh.holo_app_chat.activities.danhba;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.iuh.holo_app_chat.R;
import se.iuh.holo_app_chat.services.RelationshipServiece;
import se.iuh.holo_app_chat.services.response.RelationshipRespone;
import se.iuh.holo_app_chat.services.response.UserResponse;
import se.iuh.holo_app_chat.untils.ServieceDanhBa;

public class TimDanhBaAdapter extends RecyclerView.Adapter<TimDanhBaAdapter.TimDanhBaViewHoler>{
    private Context context;
    private List<UserResponse> userResponses;
    private List<DanhBa> danhBas;
    private int id;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://54.255.234.239:3000");
        } catch (URISyntaxException e) {}
    }

    public TimDanhBaAdapter(int id,List<UserResponse> userResponses,List<DanhBa> danhBas,Context context){
        this.danhBas = danhBas;
        this.id = id;
        this.userResponses= userResponses;
        //init(this.userResponses);
        this.context=context;
        mSocket.connect();
    }

//    private void getContact(){
//        Cursor  cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                null,null,null,null);
//        while (cursor.moveToNext()){
//            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            String sdt = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            DanhBa db = new DanhBa(name,sdt);
//            danhBas.add(db);
//        }
//    }

    @NonNull
    @Override
    public TimDanhBaViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_danhba,parent,false);
        TimDanhBaViewHoler timDanhBaViewHoler = new TimDanhBaViewHoler(view);
        return timDanhBaViewHoler;
    }

    @Override
    public void onBindViewHolder(@NonNull final TimDanhBaViewHoler holder, int position) {
        UserResponse userResponse = userResponses.get(position);
        //DanhBa danhBa = danhBas.get(position);
        holder.txt_id.setText(userResponse.getId()+"");
        //holder.txt_tendanhba.setText(danhBa.getTen());
        holder.txt_tennguoidung.setText(userResponse.getFullName());
        final RelationshipServiece relationshipServiece = ServieceDanhBa.createService(RelationshipServiece.class);
        Call<RelationshipRespone> relationshipResponeCall = relationshipServiece.getRelationship(id+"",userResponse.getId()+"");
        relationshipResponeCall.enqueue(new Callback<RelationshipRespone>() {
            @Override
            public void onResponse(Call<RelationshipRespone> call, Response<RelationshipRespone> response) {
                RelationshipRespone relationshipRespone = response.body();
                if(relationshipRespone.getStatus().equals("1")){
                    holder.txt_status.setVisibility(View.VISIBLE);
                    holder.txt_status.setText("Đã là bạn bè");
                    holder.btn_KetBan.setVisibility(View.INVISIBLE);
                }else  if(relationshipRespone.getStatus().equals("2")){
                    holder.txt_status.setVisibility(View.VISIBLE);
                    holder.txt_status.setText("Đang chờ chấp nhận");
                    holder.btn_KetBan.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<RelationshipRespone> call, Throwable t) {

            }
        });
    }

    private boolean kiemtra(String sdt){
        for (DanhBa db : danhBas){
            if(db.getSoDienThoai().equals(sdt)){
                return true;
            }
        }
        return false;
    }

    private void init(List<UserResponse> listDB){
//        for (UserResponse ur : listDB){
//            if (kiemtra(ur.getPhone()) == false){
//                listDB.remove(ur);
//            }
//        }
        for (int i=0;i<listDB.size();i++){
            if (kiemtra(listDB.get(i).getPhone()) == false){
                listDB.remove(listDB.get(i));
            }
        }
    }

    @Override
    public int getItemCount() {
        return userResponses.size();
    }


    public class TimDanhBaViewHoler extends RecyclerView.ViewHolder {
        private TextView txt_tendanhba,txt_tennguoidung,txt_status,txt_id;
        private Button btn_KetBan;
        public TimDanhBaViewHoler(@NonNull View itemView) {
            super(itemView);

            txt_tendanhba = (TextView) itemView.findViewById(R.id.item_tenDB);
            txt_tennguoidung = (TextView) itemView.findViewById((R.id.item_tenChat));
            txt_id = (TextView) itemView.findViewById((R.id.txt_idDB));
            txt_status = (TextView) itemView.findViewById((R.id.item_status));
            btn_KetBan = (Button) itemView.findViewById(R.id.btn_ketBanDB);

            btn_KetBan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateRelationship(txt_id.getText()+"");
                    mSocket.emit("Update","Update");
                    txt_status.setVisibility(View.VISIBLE);
                    txt_status.setText("Đang chờ chấp nhận");
                    btn_KetBan.setVisibility(View.INVISIBLE);
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
                        relationshipRespone.getIdUser2()+"",id+"","2");
                voidCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
//                        Toast.makeText(context,"Them thanh cong",Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(context, AddFriend.class);
//                        i.putExtra("id",new String(id+""));
//                        context.startActivity(i);

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
}
