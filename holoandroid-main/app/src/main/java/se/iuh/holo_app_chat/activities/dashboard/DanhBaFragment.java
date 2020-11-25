package se.iuh.holo_app_chat.activities.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DanhBaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DanhBaFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<RelationshipRespone> relationshipRespones = new ArrayList<>();
    private ImageView img_buttonAdd;
    private DanhBaAdapter danhBaAdapter;
    private  List<UserResponse> userResponses = new ArrayList<>() ;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_danh_ba, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.rv_danhBa1);
        initList(1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        img_buttonAdd=(ImageView) view.findViewById(R.id.img_buttonAdd);
        img_buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(), AddFriend.class);
                i.putExtra("id",new String("1"));
                startActivity(i);
            }
        });
        return view;
    }

    private void initList(final int id) {
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
                                    danhBaAdapter = new DanhBaAdapter(1,userResponses,getContext());
                                    recyclerView.setAdapter(danhBaAdapter);
                                    danhBaAdapter.notifyDataSetChanged();
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
                                        danhBaAdapter = new DanhBaAdapter(1,userResponses,getContext());
                                        recyclerView.setAdapter(danhBaAdapter);
                                        danhBaAdapter.notifyDataSetChanged();
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

}