package com.example.pkmcombat_frontend;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button registerButtom;
    private Button sessionButton;
    private Context context=this;
    private String url="http://10.0.2.2:8000/login/";
    private String onResponse="Successful login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        registerButtom= findViewById(R.id.btnRegister);
        sessionButton= findViewById(R.id.btnLogin);

        sessionButton.setOnClickListener(v -> {
            JSONObject requestBody=new JSONObject();
            try{
                requestBody.put("name", etUsername.getText().toString());
                requestBody.put("encrypted_pass", etPassword.getText().toString());

                SendRequestsForLoginOrRegister sendRequestsForLoginOrRegister=new SendRequestsForLoginOrRegister();
                sendRequestsForLoginOrRegister.sendPostRequest(context, url, requestBody,onResponse);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
        registerButtom.setOnClickListener(v -> {
            Intent myIntent=new Intent(context, RegisterActivity.class);
            startActivity(myIntent);
        });
    }
}