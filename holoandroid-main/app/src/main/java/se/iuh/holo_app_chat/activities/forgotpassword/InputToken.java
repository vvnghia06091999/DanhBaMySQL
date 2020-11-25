package se.iuh.holo_app_chat.activities.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import se.iuh.holo_app_chat.R;

public class InputToken extends AppCompatActivity {
    ImageView imageNext1, imageNext2;
    EditText et_num1, et_num2, et_num3, et_num4;
    ImageView imageReturn;
    TextView tv_guiLai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_token);
        et_num1 = (EditText) findViewById(R.id.et_number1);
        et_num2 = (EditText) findViewById(R.id.et_number2);
        et_num3 = (EditText) findViewById(R.id.et_number3);
        et_num4 = (EditText) findViewById(R.id.et_number4);

        et_num1.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        et_num2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        et_num3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        et_num4.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
        tv_guiLai = (TextView) findViewById(R.id.tv_guilai);
        imageNext1 = (ImageView) findViewById(R.id.imageNext1);
        imageNext2 = (ImageView) findViewById(R.id.imageNext2);
        imageReturn = (ImageView) findViewById(R.id.image_Return);
        et_num1.requestFocus();
        onInput();

        imageNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkToken();
            }
        });
        imageNext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkToken();
            }
        });

        et_num4.setOnKeyListener(new View.OnKeyListener() {
            @Override

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    checkToken();
                    return true;
                }
                return false;
            }
        });
        tv_guiLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_num1.setText("");
                et_num2.setText("");
                et_num3.setText("");
                et_num4.setText("");
                et_num1.requestFocus();
            }
        });
        imageReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void onInput() {
        et_num1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty())
                    et_num2.requestFocus();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_num2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty())
                    et_num3.requestFocus();
                else
                    et_num1.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_num3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty())
                    et_num4.requestFocus();
                else
                    et_num2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        et_num4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty())
                    et_num3.requestFocus();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    void checkToken() {
        if (et_num1.length() == 0 || et_num2.length() == 0 || et_num3.length() == 0 || et_num4.length() == 0)
            Toast.makeText(InputToken.this, "Vui lòng nhập mã xác thực", Toast.LENGTH_SHORT).show();
        else {
            Intent intent = new Intent(InputToken.this, CreatePassword.class);
            startActivity(intent);
        }
    }
}