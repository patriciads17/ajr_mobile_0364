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
import com.p3l.ajr_mobile_0364.adapter.PromoAdapter;
import com.p3l.ajr_mobile_0364.adapter.RatingAdapter;
import com.p3l.ajr_mobile_0364.api.Api;
import com.p3l.ajr_mobile_0364.driver.OrderActivity;
import com.p3l.ajr_mobile_0364.driver.ProfileActivity;
import com.p3l.ajr_mobile_0364.driver.RatingActivity;
import com.p3l.ajr_mobile_0364.model.response.PromoResponse;
import com.p3l.ajr_mobile_0364.model.response.TransactionResponse;
import com.p3l.ajr_mobile_0364.preference.UserPreferences;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PromoActivity extends AppCompatActivity {

    private UserPreferences userPreferences;
    private BottomNavigationView bottomNavigationView;
    private RequestQueue queue;
    private LinearLayout layoutLoading;
    private SwipeRefreshLayout srPromo;
    private PromoAdapter promoAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener navigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.nav_home){
                startActivity(new Intent(PromoActivity.this, CustomerActivity.class));
            }else if(item.getItemId() == R.id.nav_car){
                startActivity(new Intent(PromoActivity.this, CarActivity.class));
            }else if(item.getItemId() == R.id.nav_promo){
                startActivity(new Intent(PromoActivity.this, PromoActivity.class));
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);

        bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);
        queue = Volley.newRequestQueue(this);
        layoutLoading = findViewById(R.id.layout_loading);
        userPreferences = new UserPreferences(PromoActivity.this);
        srPromo = findViewById(R.id.sr_promo);

        if(!userPreferences.checkLogin()){
            startActivity(new Intent(PromoActivity.this, LoginActivity.class));
            finish();
        }
        else {
            getAvailablePromo(userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }

        srPromo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAvailablePromo(userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            }
        });

        RecyclerView rvpromo = findViewById(R.id.rv_promo);
        promoAdapter = new PromoAdapter(new ArrayList<>(), this);
        rvpromo.setLayoutManager(new LinearLayoutManager(PromoActivity.this, LinearLayoutManager.VERTICAL, false));
        rvpromo.setAdapter(promoAdapter);
    }

    private void getAvailablePromo(String token, String token_type){
        srPromo.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(GET, Api.GET_PROMO, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                PromoResponse promoResponse = gson.fromJson(response, PromoResponse.class);
                promoAdapter.setPromoList(promoResponse.getPromo());
                Toast.makeText(PromoActivity.this, promoResponse.getMessage(), Toast.LENGTH_SHORT).show();
                srPromo.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                srPromo.setRefreshing(false);
                try {
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(PromoActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(PromoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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