package com.p3l.ajr_mobile_0364.customer;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.p3l.ajr_mobile_0364.LoginActivity;
import com.p3l.ajr_mobile_0364.R;
import com.p3l.ajr_mobile_0364.api.Api;
import com.p3l.ajr_mobile_0364.driver.OrderActivity;
import com.p3l.ajr_mobile_0364.driver.ProfileActivity;
import com.p3l.ajr_mobile_0364.driver.RatingActivity;
import com.p3l.ajr_mobile_0364.model.User;
import com.p3l.ajr_mobile_0364.model.response.CustomerResponse;
import com.p3l.ajr_mobile_0364.preference.UserPreferences;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerActivity extends AppCompatActivity {

    private UserPreferences userPreferences;
    private BottomNavigationView bottomNavigationView;
    private RequestQueue queue;
    private CircleImageView profilpic;
    private TextView tvData1, tvData2, tvData3, tvData4, tvData5, tvData6, tvData7, tvData8;
    private Button btn1, btn2;
    private LinearLayout layoutLoading;

    private BottomNavigationView.OnNavigationItemSelectedListener navigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.nav_home){
                startActivity(new Intent(CustomerActivity.this, CustomerActivity.class));
            }else if(item.getItemId() == R.id.nav_car){
                startActivity(new Intent(CustomerActivity.this, CarActivity.class));
            }else if(item.getItemId() == R.id.nav_promo) {
                startActivity(new Intent(CustomerActivity.this, PromoActivity.class));
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);
        queue = Volley.newRequestQueue(this);
        layoutLoading = findViewById(R.id.layout_loading);
        userPreferences = new UserPreferences(CustomerActivity.this);
        profilpic = findViewById(R.id.profile_pic);
        btn1 = findViewById(R.id.btn_edit);
        btn2 = findViewById(R.id.btn_logout);
        tvData1 = findViewById(R.id.tv_id);
        tvData2 = findViewById(R.id.tv_name);
        tvData3 = findViewById(R.id.tv_email);
        tvData4 = findViewById(R.id.tv_statusAcc);
        tvData5 = findViewById(R.id.tv_tglLahir);
        tvData6 = findViewById(R.id.tv_gender);
        tvData7 = findViewById(R.id.tv_address);
        tvData8 = findViewById(R.id.tv_phoneNum);

        if(!userPreferences.checkLogin()){
            startActivity(new Intent(CustomerActivity.this, LoginActivity.class));
            finish();
        }else{
            getCustomerById(userPreferences.getUserLoginId(),userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerActivity.this, CustProfileActivity.class));
                finish();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
                userPreferences.logout();
                startActivity(new Intent(CustomerActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void logout(String token, String token_type){
        setLoading(true);
        User user= new User("user");

        StringRequest stringRequest = new StringRequest(POST, Api.LOGOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                CustomerResponse customerResponse = gson.fromJson(response, CustomerResponse.class);
                Toast.makeText(CustomerActivity.this, customerResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CustomerActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(CustomerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getCustomerById(String id, String token, String token_type){
        setLoading(true);

        StringRequest stringRequest = new StringRequest(GET, Api.GET_CUST_BY_ID + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                CustomerResponse customerResponse = gson.fromJson(response, CustomerResponse.class);
                loadImage(customerResponse.getUser().getUrl_pp_customer());
                tvData1.setText(customerResponse.getUser().getId());
                tvData2.setText(customerResponse.getUser().getNama_customer());
                tvData3.setText(customerResponse.getUser().getEmail());
                tvData4.setText(customerResponse.getUser().getStatus_akun());
                tvData5.setText(customerResponse.getUser().getTgl_lahir_customer());
                tvData6.setText(customerResponse.getUser().getGender_customer());
                tvData7.setText(customerResponse.getUser().getAlamat_customer());
                tvData8.setText(customerResponse.getUser().getNo_telp_customer());
                Toast.makeText(CustomerActivity.this, customerResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CustomerActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(CustomerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void loadImage(String url){
        Glide.with(CustomerActivity.this).load(Api.BASE_URL_IMG + url).into(profilpic);
    }
}