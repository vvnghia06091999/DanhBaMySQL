package se.iuh.holo_app_chat.activities.dashboard;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.iuh.holo_app_chat.R;
import se.iuh.holo_app_chat.services.LoginService;
import se.iuh.holo_app_chat.services.ProfileService;
import se.iuh.holo_app_chat.services.response.UserResponse;
import se.iuh.holo_app_chat.untils.ServiceGenerator;
import se.iuh.holo_app_chat.untils.SharedPrefManager;

public class TaiKhoanFragment extends Fragment {

    Button button;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaiKhoanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TinNhanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaiKhoanFragment newInstance(String param1, String param2) {
        TaiKhoanFragment fragment = new TaiKhoanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tai_khoan, container, false);
    }

    SharedPrefManager sharedPrefManager;
    ProfileService profileService;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button= getView().findViewById(R.id.button);

        final Context mContext= getContext();
        sharedPrefManager= new SharedPrefManager(mContext);
        profileService= ServiceGenerator.createService(ProfileService.class, sharedPrefManager.getSPToken());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<UserResponse> getProfileCall=profileService.getUser();
                getProfileCall.enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if(response.isSuccessful()){
                            UserResponse userResponse= response.body();
                            Toast.makeText(mContext, userResponse.getFullName(),Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        Toast.makeText(mContext,"Lỗi kết nối"+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}