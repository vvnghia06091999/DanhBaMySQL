package se.iuh.holo_app_chat.activities.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import se.iuh.holo_app_chat.R;

public class ForgetPass extends AppCompatActivity {
    ImageView imageNext1, imageNext2;
    EditText et_nhap;
    ImageView imageReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        et_nhap = (EditText) findViewById(R.id.et_input);
        imageNext1 = (ImageView) findViewById(R.id.imageNext1);
        imageNext2 = (ImageView) findViewById(R.id.image_1);
        imageReturn = (ImageView) findViewById(R.id.image_Return);
        imageReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_nhap.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    checkInput();
                    return true;
                }
                return false;
            }

        });

        et_nhap.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    checkInput();
                    return true;
                }
                return false;
            }
        });


        imageNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });
        imageNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInput();
            }
        });

    }

    void checkInput() {
        if (et_nhap.length() == 0 || !(et_nhap.getText().toString().matches("^[0][0-9]{9}") || et_nhap.getText().toString().matches("^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$")))
            et_nhap.setError("Vui lòng nhập thông tin như sau:\n1. Số điện thoại phải có 10 số bắt đầu 0\n2. Email phải đúng định dạng");
        else {
            Intent intent = new Intent(ForgetPass.this, InputToken.class);
            startActivity(intent);
        }
    }
}