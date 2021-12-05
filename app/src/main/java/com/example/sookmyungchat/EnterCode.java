package com.example.sookmyungchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EnterCode extends AppCompatActivity {

    private EditText editcode;
    private String real_code;
    private Button codebutton;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);

        real_code = getString(R.string.enter_code);


        codebutton = findViewById(R.id.codebutton);
        codebutton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                editcode = findViewById(R.id.editText);
                code = editcode.getText().toString();
                if(code.equals(real_code)) {
                    Intent intent = new Intent(getApplication(), MainActivity.class);
                    startActivity(intent);
                } else{
                    Toast.makeText(EnterCode.this, "암호를 다시 입력해주세요!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}