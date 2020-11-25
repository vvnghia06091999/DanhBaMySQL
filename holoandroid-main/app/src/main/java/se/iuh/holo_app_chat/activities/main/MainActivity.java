package se.iuh.holo_app_chat.activities.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import se.iuh.holo_app_chat.R;
import se.iuh.holo_app_chat.activities.dashboard.DashboardActivity;
import se.iuh.holo_app_chat.activities.login.LoginActivity;
import se.iuh.holo_app_chat.activities.register.RegisterStep1Activity;
import se.iuh.holo_app_chat.untils.SharedPrefManager;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin= (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        btnRegister= (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RegisterStep1Activity.class);
                startActivity(i);
                finish();
            }
        });

        sharedPrefManager = new SharedPrefManager(this);
        if (sharedPrefManager.getSPSLogin()){
            startActivity(new Intent(MainActivity.this, DashboardActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }
}