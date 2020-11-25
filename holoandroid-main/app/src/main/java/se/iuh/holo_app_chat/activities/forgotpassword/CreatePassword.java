package se.iuh.holo_app_chat.activities.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import se.iuh.holo_app_chat.R;

public class CreatePassword extends AppCompatActivity {
    Button btn_capnhat;
    EditText et_mk, et_nhaplaimk;
    TextView tv_hien;
    ImageView image_Return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);
        et_mk = (EditText) findViewById(R.id.et_nhapmk);
        et_mk.requestFocus();
        et_nhaplaimk = (EditText) findViewById(R.id.et_nhaplaimk);
        et_mk.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        et_nhaplaimk.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        btn_capnhat = (Button) findViewById(R.id.btn_capnhat);
        image_Return = (ImageView) findViewById(R.id.image_Return);
        tv_hien = (TextView) findViewById(R.id.tv_guilai);
        et_mk.requestFocus();
        tv_hien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_hien.getText().toString().trim().equals("HIỆN")) {
                    tv_hien.setText("ẨN");
                    et_mk.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_mk.setSelection(et_mk.length());
                    et_nhaplaimk.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_nhaplaimk.setSelection(et_nhaplaimk.length());
                } else if (tv_hien.getText().toString().trim().equals("ẨN")) {
                    tv_hien.setText("HIỆN");
                    et_mk.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_mk.setSelection(et_mk.length());
                    et_nhaplaimk.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_nhaplaimk.setSelection(et_nhaplaimk.length());
                }
            }
        });

        btn_capnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_mk.getText().length() == 0) {
                    Toast.makeText(CreatePassword.this, "Vui lòng nhập mật khẩu mới!", Toast.LENGTH_SHORT).show();
                    et_mk.requestFocus();

                } else if (et_mk.getText().toString().length() < 8) {
                    Toast.makeText(CreatePassword.this, "Mật khẩu phải từ 8 kí tự!", Toast.LENGTH_SHORT).show();
                    et_mk.selectAll();
                    et_mk.requestFocus();

                } else if (et_nhaplaimk.getText().length() == 0) {
                    Toast.makeText(CreatePassword.this, "Vui lòng nhập lại mật khẩu mới!", Toast.LENGTH_SHORT).show();
                    et_nhaplaimk.requestFocus();

                } else if (!et_nhaplaimk.getText().toString().equals(et_mk.getText().toString())) {
                    Toast.makeText(CreatePassword.this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
                    et_nhaplaimk.selectAll();
                    et_nhaplaimk.requestFocus();

                }
            }
        });

        image_Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}