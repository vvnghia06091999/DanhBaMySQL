package se.iuh.holo_app_chat.activities.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import se.iuh.holo_app_chat.R;

public class RegisterStep3Activity extends AppCompatActivity {

    ProgressBar loadingProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_step3);

        initComp();
        doWork();
    }

    private void initComp() {
        loadingProgressBar = findViewById(R.id.loading);
        loadingProgressBar.setVisibility(View.VISIBLE);
    }

    private void doWork() {

    }
}