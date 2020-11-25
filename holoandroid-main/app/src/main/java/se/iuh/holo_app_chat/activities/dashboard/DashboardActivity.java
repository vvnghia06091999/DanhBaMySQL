package se.iuh.holo_app_chat.activities.dashboard;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;

import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import se.iuh.holo_app_chat.R;
import se.iuh.holo_app_chat.activities.danhba.DanhBaAdapter;
import se.iuh.holo_app_chat.untils.SharedPrefManager;

public class DashboardActivity extends AppCompatActivity {

    BottomNavigationView navigation;
    SharedPrefManager sharedPrefManager;
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.pageTinNhan:
                    openFragment(TinNhanFragment.newInstance("", ""));
                    return true;
                case R.id.pageDanhBa:
                    openFragment(DanhBaFragment.newInstance("", ""));
                    return true;
                case R.id.pageTaiKhoan:
                    openFragment(TaiKhoanFragment.newInstance("", ""));
                    return true;
                case R.id.pageNhom:
                    openFragment(NhomFragment.newInstance("", ""));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        sharedPrefManager = new SharedPrefManager(this);
        String gd =  getIntent().getExtras().getString("gd");
        if (gd.equals("DanhBa")){
            openFragment(DanhBaFragment.newInstance("",""));
        }
        else
            openFragment(TinNhanFragment.newInstance("",""));
    }




    public void openFragment(Fragment fragment) {
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.dashboard1, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}