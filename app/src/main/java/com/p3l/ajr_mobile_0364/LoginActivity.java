package com.p3l.ajr_mobile_0364;

import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.p3l.ajr_mobile_0364.api.Api;
import com.p3l.ajr_mobile_0364.customer.CustomerActivity;
import com.p3l.ajr_mobile_0364.driver.DriverActivity;
import com.p3l.ajr_mobile_0364.driver.ProfileActivity;
import com.p3l.ajr_mobile_0364.manager.ManagerActivity;
import com.p3l.ajr_mobile_0364.model.User;
import com.p3l.ajr_mobile_0364.model.response.DriverResponse;
import com.p3l.ajr_mobile_0364.model.response.LoginResponse;
import com.p3l.ajr_mobile_0364.preference.UserPreferences;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail,etPassword;
    private MaterialButton btnLogin;
    private LinearLayout layoutLoading;
    private RequestQueue queue;
    private UserPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userPreferences = new UserPreferences(LoginActivity.this);
        queue = Volley.newRequestQueue(this);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        layoutLoading = findViewById(R.id.layout_loading);

        checkLogin();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLoading(true);
                if (validateForm()) {
                    User user = new User(
                            etEmail.getText().toString().trim(),
                            etPassword.getText().toString().trim()
                    );

                    StringRequest stringRequest = new StringRequest(POST, Api.LOGIN, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Gson gson = new Gson();
                            LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);
                            if(loginResponse.getToken_scope() == null ){
                                userPreferences.setLogin(loginResponse.getUser().getId(),loginResponse.getAccess_token(), loginResponse.getToken_type());
                                if(loginResponse.getUser().getId().startsWith("DRV")){
                                    if(loginResponse.getUser().getStatus_akun().equalsIgnoreCase("Inactive")){
                                        logout(userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
                                        userPreferences.logout();
                                        Toast.makeText(LoginActivity.this, "Your account not activated!", Toast.LENGTH_SHORT).show();
                                    }else if(loginResponse.getUser().getStatus_akun().equalsIgnoreCase("Active")) {
                                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                        checkLogin();
                                    }
                                }else if(loginResponse.getUser().getId().startsWith("CUS")){
                                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                                    checkLogin();
                                }
                            }else{
                                userPreferences.setLoginManager(loginResponse.getUser().getId(), loginResponse.getUser().getNama_pegawai(), loginResponse.getAccess_token(), loginResponse.getToken_scope(), loginResponse.getToken_type());
                                checkLogin();
                            }
                            setLoading(false);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            setLoading(false);
                            try {
                                String responseBody = new String(error.networkResponse.data,
                                        StandardCharsets.UTF_8);
                                JSONObject errors = new JSONObject(responseBody);
                                Toast.makeText(LoginActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(LoginActivity.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Accept", "application/json");
                            return headers;
                        }
                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            Gson gson = new Gson();
                            String requestBody = gson.toJson(user);
                            return requestBody.getBytes(StandardCharsets.UTF_8);
                        }
                        @Override
                        public String getBodyContentType() {
                            return "application/json";
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
    }

    private boolean validateForm(){
        if(etEmail.getText().toString().isEmpty() || etPassword.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this,"Please fill all the field!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setLoading(boolean isLoading) {
        if (isLoading) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.VISIBLE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.INVISIBLE);
        }
    }

    private void logout(String token, String token_type){
        setLoading(true);
        User user= new User("driver");

        StringRequest stringRequest = new StringRequest(POST, Api.LOGOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                DriverResponse driverResponse = gson.fromJson(response, DriverResponse.class);
                setLoading(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);
                try {
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                } catch (Exception e) {
                }
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", token_type+" "+token);
                headers.put("Accept", "application/json");
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                String requestBody = gson.toJson(user);
                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

        };
        queue.add(stringRequest);
    }

    private void checkLogin(){
        if(userPreferences.checkLogin()){
            if(userPreferences.getUserLoginId().startsWith("DRV")){
                startActivity(new Intent(LoginActivity.this, DriverActivity.class));
            }else if(userPreferences.getUserLoginId().startsWith("CUS")){
                startActivity(new Intent(LoginActivity.this, CustomerActivity.class));
            }else if(userPreferences.getUserLoginId().startsWith("MGR")){
                startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
            }
            finish();
        }
    }
}