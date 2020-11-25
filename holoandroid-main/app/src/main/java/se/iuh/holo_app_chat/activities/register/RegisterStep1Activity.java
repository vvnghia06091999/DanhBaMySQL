package se.iuh.holo_app_chat.activities.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.razir.progressbutton.ButtonTextAnimatorExtensionsKt;
import com.github.razir.progressbutton.DrawableButton;
import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressButtonHolderKt;
import com.github.razir.progressbutton.ProgressParams;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import se.iuh.holo_app_chat.R;
import se.iuh.holo_app_chat.activities.main.MainActivity;

public class RegisterStep1Activity extends AppCompatActivity {
    Button btnSendOTP;
    EditText txtUserName;
    ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity_step1);

        initComp();

        doWork();
    }

    private void initComp() {
        imgBack= findViewById(R.id.imgBack);

        btnSendOTP= findViewById(R.id.btnSendOTP);

        txtUserName= findViewById(R.id.txtUserName);

        //Enable fade In / Fade out animations
        ButtonTextAnimatorExtensionsKt.attachTextChangeAnimator(btnSendOTP);
        ProgressButtonHolderKt.bindProgressButton(this, btnSendOTP);
    }

    private void doWork() {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(RegisterStep1Activity.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressCenter(btnSendOTP);
            }
        });
    }
    private void showProgressCenter(final Button button) {
        DrawableButtonExtensionsKt.showProgress(button, new Function1<ProgressParams, Unit>() {
            @Override
            public Unit invoke(ProgressParams progressParams) {
                progressParams.setProgressColor(Color.WHITE);
                progressParams.setGravity(DrawableButton.GRAVITY_CENTER);
                return Unit.INSTANCE;
            }
        });
        button.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String userName = txtUserName.getText().toString().trim();
                Intent i = new Intent(RegisterStep1Activity.this, RegisterStep2Activity.class);
                i.putExtra("userName", userName);
                startActivity(i);
                finish();
//                button.setEnabled(true);
//                DrawableButtonExtensionsKt.hideProgress(button, "Đăng nhập");
            }
        }, 100);
    }
}