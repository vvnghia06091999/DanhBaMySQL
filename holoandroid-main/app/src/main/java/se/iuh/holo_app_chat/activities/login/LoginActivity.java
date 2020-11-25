package se.iuh.holo_app_chat.activities.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.razir.progressbutton.ButtonTextAnimatorExtensionsKt;
import com.github.razir.progressbutton.DrawableButton;
import com.github.razir.progressbutton.DrawableButtonExtensionsKt;
import com.github.razir.progressbutton.ProgressButtonHolderKt;
import com.github.razir.progressbutton.ProgressParams;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import se.iuh.holo_app_chat.R;
import se.iuh.holo_app_chat.activities.dashboard.DashboardActivity;
import se.iuh.holo_app_chat.activities.forgotpassword.ForgetPass;
import se.iuh.holo_app_chat.activities.main.MainActivity;
import se.iuh.holo_app_chat.activities.register.RegisterStep1Activity;
import se.iuh.holo_app_chat.services.LoginService;
import se.iuh.holo_app_chat.services.response.LoginResponse;
import se.iuh.holo_app_chat.untils.ServiceGenerator;
import se.iuh.holo_app_chat.untils.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {
    EditText usernameEditText, passwordEditText;
    Button loginButton;
    ImageView imgBack;
    TextView registerNow, forgotPassword;
    SharedPrefManager sharedPrefManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        sharedPrefManager = new SharedPrefManager(this);

        usernameEditText = findViewById(R.id.txtUserName);
        passwordEditText = findViewById(R.id.txtPassword);

        loginButton = findViewById(R.id.btnLogin);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        imgBack = findViewById(R.id.imgBack);
        registerNow = findViewById(R.id.registerNow);

        //Enable fade In / Fade out animations
        ButtonTextAnimatorExtensionsKt.attachTextChangeAnimator(loginButton);
        ProgressButtonHolderKt.bindProgressButton(this, loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (isUserNameValid(usernameEditText) && isPasswordValid(passwordEditText))
                 //   login(usernameEditText, passwordEditText, loginButton);
                Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                i.putExtra("gd",new String("TinNhan"));
                startActivity(i);

            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
                Intent i = new Intent(LoginActivity.this, RegisterStep1Activity.class);
                startActivity(i);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgetPass.class);
                startActivity(i);
            }
        });
    }


    // A placeholder username validation check
    private boolean isUserNameValid(EditText editText) {
        String username = editText.getText().toString();
        if (username.trim().isEmpty() | username == null | username.length() <= 0) {
            editText.setError("Bắt buộc nhập");
            return false;
        }
        if (username.contains("@")) {
            editText.setError("Email không hợp lệ");
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else if (!username.matches("(09|03|07|08|05)+([0-9]{8})")) {
            editText.setError("Số điện thoại không hợp lệ");
            return false;
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(EditText editText) {
        String password = editText.getText().toString();
        if (password == null || password.trim().length() <= 5) {
            editText.setError("Mật khẩu từ 6 ký tự trở lên");
            return false;
        }
        return true;
    }

    private void login(final EditText etUsername, final EditText etPassword, final Button button) {

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
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                LoginService service = ServiceGenerator.createService(LoginService.class);
                Call<LoginResponse> loginCall;
                if (username.contains("@")) {
                    loginCall = service.loginWithEmail(username, password);
                } else {
                    loginCall = service.loginWithPhone(username, password);
                }

                loginCall.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            LoginResponse loginResponse = response.body();
                            //Toast.makeText(LoginActivity.this,loginResponse.getToken(), Toast.LENGTH_SHORT).show();
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NAME, loginResponse.getFullName());
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_PHOTO_URL, loginResponse.getPhotoURL());
                            sharedPrefManager.saveSPInt(SharedPrefManager.SP_USER_ID, loginResponse.getUserId());
                            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_LOGIN, true);
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_TOKEN, loginResponse.getToken());
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_REFRESH_TOKEN, loginResponse.getRefreshToken());
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                            button.setEnabled(true);
                            DrawableButtonExtensionsKt.hideProgress(button, "Đăng nhập");
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Lỗi Server" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        button.setEnabled(true);
                        DrawableButtonExtensionsKt.hideProgress(button, "Đăng nhập");
                    }
                });
            }
        }, 100);
    }
}