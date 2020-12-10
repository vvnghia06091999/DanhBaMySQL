package se.iuh.holo_app_chat.activities.danhba;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.iuh.holo_app_chat.R;
import se.iuh.holo_app_chat.services.DanhBaServiece;
import se.iuh.holo_app_chat.services.response.UserResponse;
import se.iuh.holo_app_chat.untils.ServieceDanhBa;

public class TimDanhBaActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TimDanhBaAdapter timDanhBaAdapter;
    private List<UserResponse> UR;
    private int id;
    private List<DanhBa> danhBas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim_danh_ba);
        String stid =  getIntent().getExtras().getString("id");
        id = Integer.parseInt(stid);
        recyclerView =(RecyclerView) findViewById(R.id.rv_danhbadienthoai);
        UR = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,null,null,null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String sdt = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            DanhBa db = new DanhBa(name,sdt);
            danhBas.add(db);
        }
        Toast.makeText(TimDanhBaActivity.this,danhBas.size()+"",Toast.LENGTH_LONG).show();
        DanhBaServiece danhBaServiece = ServieceDanhBa.createService(DanhBaServiece.class);
        Call<List<UserResponse>> userResponseCall = danhBaServiece.getAll();
        userResponseCall.enqueue(new Callback<List<UserResponse>>() {
            @Override
            public void onResponse(Call<List<UserResponse>> call, Response<List<UserResponse>> response) {
                UR = response.body();
                timDanhBaAdapter = new TimDanhBaAdapter(id,UR,danhBas,TimDanhBaActivity.this);
                recyclerView.setAdapter(timDanhBaAdapter);
            }

            @Override
            public void onFailure(Call<List<UserResponse>> call, Throwable t) {
            }
        });
    }
}