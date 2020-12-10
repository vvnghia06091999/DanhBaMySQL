package se.iuh.holo_app_chat.activities.dashboard;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.iuh.holo_app_chat.R;
import se.iuh.holo_app_chat.activities.danhba.AddFriend;
import se.iuh.holo_app_chat.activities.danhba.DanhBaAdapter;
import se.iuh.holo_app_chat.services.DanhBaServiece;
import se.iuh.holo_app_chat.services.RelationshipServiece;
import se.iuh.holo_app_chat.services.response.RelationshipRespone;
import se.iuh.holo_app_chat.services.response.UserResponse;
import se.iuh.holo_app_chat.untils.ServieceDanhBa;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DanhBaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DanhBaFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<RelationshipRespone> relationshipRespones;
    private DanhBaAdapter danhBaAdapter;
    private List<UserResponse> userResponses;
    private NotificationManagerCompat notificationManagerCompat;
    private int id =1;
    private FloatingActionButton button;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://54.255.234.239:3000");
        } catch (URISyntaxException e) {}
    }


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DanhBaFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DanhBaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DanhBaFragment newInstance(String param1, String param2) {
        DanhBaFragment fragment = new DanhBaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);\

        }*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSocket.on("GuiKetBan", GuiKetBan);
        mSocket.on("Server", Sever);
        mSocket.connect();
        View view=inflater.inflate(R.layout.fragment_danh_ba, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.rv_danhBa1);
        //id = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initList(id);
            }
        }).start();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        button = (FloatingActionButton) view.findViewById(R.id.btn_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), AddFriend.class);
                i.putExtra("id",new String(id+""));
                startActivity(i);
            }
        });
        return view;
    }

    private void initList(final int id) {
        notificationManagerCompat = NotificationManagerCompat.from(getContext());
        relationshipRespones = new ArrayList<>();
        userResponses = new ArrayList<>();
        RelationshipServiece relationshipServiece = ServieceDanhBa.createService(RelationshipServiece.class);
        Call<List<RelationshipRespone>> relationshipResponeCall = relationshipServiece.getRelationship(id+"");
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
                                    danhBaAdapter = new DanhBaAdapter(id,userResponses,getContext());
                                    recyclerView.setAdapter(danhBaAdapter);
                                    //danhBaAdapter.notifyDataSetChanged();
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
                                        danhBaAdapter = new DanhBaAdapter(id,userResponses,getContext());
                                        recyclerView.setAdapter(danhBaAdapter);
                                        //danhBaAdapter.notifyDataSetChanged();
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
                    Toast.makeText(getContext(),"Khong Co Ban Be",Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onFailure(Call<List<RelationshipRespone>> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private Emitter.Listener Sever = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    relationshipRespones = new ArrayList<>();
                    userResponses = new ArrayList<>();
                    RelationshipServiece relationshipServiece = ServieceDanhBa.createService(RelationshipServiece.class);
                    Call<List<RelationshipRespone>> relationshipResponeCall = relationshipServiece.getRelationship(id+"");
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
                                                    danhBaAdapter = new DanhBaAdapter(id,userResponses,getContext());
                                                    recyclerView.setAdapter(danhBaAdapter);
                                                    //danhBaAdapter.notifyDataSetChanged();
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
                                                    danhBaAdapter = new DanhBaAdapter(id,userResponses,getContext());
                                                    recyclerView.setAdapter(danhBaAdapter);
                                                    //danhBaAdapter.notifyDataSetChanged();
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
                                Toast.makeText(getContext(),"Khong Co Ban Be",Toast.LENGTH_LONG).show();
                            }

                        }
                        @Override
                        public void onFailure(Call<List<RelationshipRespone>> call, Throwable t) {
                            Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    };

    private Emitter.Listener GuiKetBan = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
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

    private void notifi(){
        String title = "Thông Báo";
        String me = "Có lời mời kết bạn mới!";
        Intent i = new Intent(getContext(), AddFriend.class);
        i.putExtra("id",new String(id+""));
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(getContext(), null)
                .setSmallIcon(R.drawable.ic_baseline_add_24)
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(me).setPriority(NotificationCompat.PRIORITY_HIGH).build();
        notificationManagerCompat.notify(0,notification);
    }

}