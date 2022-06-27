package com.p3l.ajr_mobile_0364.manager;

import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.p3l.ajr_mobile_0364.LoginActivity;
import com.p3l.ajr_mobile_0364.R;
import com.p3l.ajr_mobile_0364.adapter.RatingAdapter;
import com.p3l.ajr_mobile_0364.api.Api;
import com.p3l.ajr_mobile_0364.driver.DriverActivity;
import com.p3l.ajr_mobile_0364.driver.EditProfileActivity;
import com.p3l.ajr_mobile_0364.driver.OrderActivity;
import com.p3l.ajr_mobile_0364.driver.ProfileActivity;
import com.p3l.ajr_mobile_0364.driver.RatingActivity;
import com.p3l.ajr_mobile_0364.model.User;
import com.p3l.ajr_mobile_0364.model.response.DriverResponse;
import com.p3l.ajr_mobile_0364.preference.UserPreferences;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ManagerActivity extends AppCompatActivity {

    private UserPreferences userPreferences;
    private BottomNavigationView bottomNavigationView;
    private RequestQueue queue;
    private LinearLayout layoutLoading;
    private Button btn1, btn2, btn3, btn4;
    private TextView tvData1;

    private BottomNavigationView.OnNavigationItemSelectedListener navigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.nav_home){
                startActivity(new Intent(ManagerActivity.this, ManagerActivity.class));
            }else if(item.getItemId() == R.id.nav_leader){
                startActivity(new Intent(ManagerActivity.this, LeaderboardActivity.class));
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);
        queue = Volley.newRequestQueue(this);
        btn1 = findViewById(R.id.btn_car);
        btn2 = findViewById(R.id.btn_driver);
        btn3 = findViewById(R.id.btn_income);
        btn4 = findViewById(R.id.btn_logout);
        tvData1 = findViewById(R.id.tv_loginUser);
        layoutLoading = findViewById(R.id.layout_loading);
        userPreferences = new UserPreferences(ManagerActivity.this);

        tvData1.setText("Hi, " + userPreferences.getNameLogin() + "!");

        if(!userPreferences.checkLogin()){
            startActivity(new Intent(ManagerActivity.this, LoginActivity.class));
            finish();
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManagerActivity.this, CarReportActivity.class));
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManagerActivity.this, DriverReportActivity.class));
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ManagerActivity.this, IncomeReportActivity.class));
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
                userPreferences.logout();
                startActivity(new Intent(ManagerActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void logout(String token, String token_type){
        setLoading(true);
        User user= new User("employee");

        StringRequest stringRequest = new StringRequest(POST, Api.LOGOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                DriverResponse driverResponse = gson.fromJson(response, DriverResponse.class);
                Toast.makeText(ManagerActivity.this, driverResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ManagerActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ManagerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
}