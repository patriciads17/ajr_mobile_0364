package com.p3l.ajr_mobile_0364.driver;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;

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
import com.p3l.ajr_mobile_0364.model.User;
import com.p3l.ajr_mobile_0364.model.response.DriverResponse;
import com.p3l.ajr_mobile_0364.preference.UserPreferences;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private UserPreferences userPreferences;
    private BottomNavigationView bottomNavigationView;
    private Button btn1, btn2, btn4;
    private RequestQueue queue;
    private LinearLayout layoutLoading;
    private CircleImageView profilpic;
    private TextView tvData1, tvData2, tvData3, tvData4 ;

    private BottomNavigationView.OnNavigationItemSelectedListener navigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.nav_home){
                startActivity(new Intent(ProfileActivity.this, DriverActivity.class));
            }else if(item.getItemId() == R.id.nav_order){
                startActivity(new Intent(ProfileActivity.this, OrderActivity.class));
            }else if(item.getItemId() == R.id.nav_rating){
                startActivity(new Intent(ProfileActivity.this, RatingActivity.class));
            }else if(item.getItemId() == R.id.nav_myProfile){
                startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
            }
            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);
        queue = Volley.newRequestQueue(this);
        tvData1 = findViewById(R.id.tv_id);
        tvData2 = findViewById(R.id.tv_name);
        tvData3 = findViewById(R.id.tv_email);
        tvData4 = findViewById(R.id.tv_phonenum);
        btn1 = findViewById(R.id.btn_edit);
        btn2 = findViewById(R.id.btn_profile);
        btn4 = findViewById(R.id.btn_logout);
        profilpic = findViewById(R.id.profile_pic);
        layoutLoading = findViewById(R.id.layout_loading);
        userPreferences = new UserPreferences(ProfileActivity.this);

        if(!userPreferences.checkLogin()){
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }else{
            getDriverById(userPreferences.getUserLoginId(),userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class).putExtra("edited",true));
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class).putExtra("edited",false));
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout(userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
                userPreferences.logout();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void getDriverById(String id, String token, String token_type){
        setLoading(true);

        StringRequest stringRequest = new StringRequest(GET, Api.GET_DRV_BY_ID + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                DriverResponse driverResponse = gson.fromJson(response, DriverResponse.class);
                loadImage(driverResponse.getUser().getUrl_foto_driver());
                tvData1.setText(driverResponse.getUser().getId());
                tvData2.setText(driverResponse.getUser().getNama_driver());
                tvData3.setText(driverResponse.getUser().getEmail());
                tvData4.setText(driverResponse.getUser().getNo_telp_driver());
                Toast.makeText(ProfileActivity.this, driverResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProfileActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void logout(String token, String token_type){
        setLoading(true);
        User user= new User("driver");

        StringRequest stringRequest = new StringRequest(POST, Api.LOGOUT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                DriverResponse driverResponse = gson.fromJson(response, DriverResponse.class);
                Toast.makeText(ProfileActivity.this, driverResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ProfileActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void loadImage(String url){
        Glide.with(ProfileActivity.this).load(Api.BASE_URL_IMG + url).into(profilpic);
    }
}