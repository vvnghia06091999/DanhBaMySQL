package se.iuh.holo_app_chat.activities.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import se.iuh.holo_app_chat.R;

public class RegisterStep2Activity extends AppCompatActivity {

    Button btnVerifyOTP;
    ProgressBar loadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_step2);

        initComp();
        doWork();
    }


    private void initComp() {

        btnVerifyOTP= findViewById(R.id.btnVerifyOTP);

        loadingProgressBar = findViewById(R.id.loading);
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void doWork() {
        btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(RegisterStep2Activity.this, RegisterStep3Activity.class);
                startActivity(i);
            }
        });
    }
}