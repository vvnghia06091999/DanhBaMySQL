package com.example.danhba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHoler> {
    private Context context;
    private List<User> userList;

    public UserAdapter(List<User> userList,Context context){
        this.userList = userList;
        this.context=context;
    }

    @NonNull
    @Override
    public UserViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user,parent,false);
        return new UserViewHoler(view);
    }

    @Override
    public void onBindViewHolder(UserAdapter.UserViewHoler holder, int position) {
        User user=userList.get(position);
        holder.tv_tenNguoiDung.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHoler extends RecyclerView.ViewHolder {
        private TextView tv_tenNguoiDung;
        private ImageView tv_img;

        public UserViewHoler(View itemView) {
            super(itemView);
            tv_tenNguoiDung = (TextView) itemView.findViewById(R.id.item_name);
            //tv_img=(ImageView) itemView.findViewById(R.id.item_img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return true;
                }
            });
        }
    }
}
