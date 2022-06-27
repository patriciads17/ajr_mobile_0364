package com.p3l.ajr_mobile_0364.customer;

import static com.android.volley.Request.Method.GET;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
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
import com.p3l.ajr_mobile_0364.adapter.CarAdapter;
import com.p3l.ajr_mobile_0364.adapter.PromoAdapter;
import com.p3l.ajr_mobile_0364.api.Api;
import com.p3l.ajr_mobile_0364.model.response.CarResponse;
import com.p3l.ajr_mobile_0364.model.response.PromoResponse;
import com.p3l.ajr_mobile_0364.preference.UserPreferences;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CarActivity extends AppCompatActivity {

    private UserPreferences userPreferences;
    private BottomNavigationView bottomNavigationView;
    private RequestQueue queue;
    private LinearLayout layoutLoading;
    private SwipeRefreshLayout srCar;
    private CarAdapter carAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener navigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.nav_home){
                startActivity(new Intent(CarActivity.this, CustomerActivity.class));
            }else if(item.getItemId() == R.id.nav_car){
                startActivity(new Intent(CarActivity.this, CarActivity.class));
            }else if(item.getItemId() == R.id.nav_promo){
                startActivity(new Intent(CarActivity.this, PromoActivity.class));
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);
        queue = Volley.newRequestQueue(this);
        layoutLoading = findViewById(R.id.layout_loading);
        userPreferences = new UserPreferences(CarActivity.this);
        srCar = findViewById(R.id.sr_car);

        if(!userPreferences.checkLogin()){
            startActivity(new Intent(CarActivity.this, LoginActivity.class));
            finish();
        }
        else {
            getCarBrosur(userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }

        srCar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               getCarBrosur(userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            }
        });

        RecyclerView rvcar = findViewById(R.id.rv_Car);
        carAdapter = new CarAdapter(new ArrayList<>(), this);
        rvcar.setLayoutManager(new LinearLayoutManager(CarActivity.this, LinearLayoutManager.VERTICAL, false));
        rvcar.setAdapter(carAdapter);
        getCarBrosur(userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
    }

    private void getCarBrosur(String token, String token_type){
        srCar.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(GET, Api.GET_CAR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                CarResponse carResponse = gson.fromJson(response, CarResponse.class);
                carAdapter.setCarList(carResponse.getCarList());
                Toast.makeText(CarActivity.this, carResponse.getMessage(), Toast.LENGTH_SHORT).show();
                srCar.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                srCar.setRefreshing(false);
                try {
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(CarActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(CarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
}