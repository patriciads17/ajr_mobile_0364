package com.p3l.ajr_mobile_0364.driver;

import static com.android.volley.Request.Method.GET;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.p3l.ajr_mobile_0364.adapter.TransactionAdapter;
import com.p3l.ajr_mobile_0364.api.Api;
import com.p3l.ajr_mobile_0364.model.response.CarResponse;
import com.p3l.ajr_mobile_0364.model.response.TransactionResponse;
import com.p3l.ajr_mobile_0364.preference.UserPreferences;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    public static final int LAUNCH_ORDER_ACTIVITY = 123;

    private UserPreferences userPreferences;
    private SwipeRefreshLayout srTransaction;
    private TransactionAdapter transactionAdapter;
    private BottomNavigationView bottomNavigationView;
    private RequestQueue queue;

    private BottomNavigationView.OnNavigationItemSelectedListener navigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.nav_home){
                startActivity(new Intent(OrderActivity.this, DriverActivity.class));
            }else if(item.getItemId() == R.id.nav_order){
                startActivity(new Intent(OrderActivity.this, OrderActivity.class));
            }else if(item.getItemId() == R.id.nav_rating){
                startActivity(new Intent(OrderActivity.this, RatingActivity.class));
            }else if(item.getItemId() == R.id.nav_myProfile){
                startActivity(new Intent(OrderActivity.this, ProfileActivity.class));
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);
        queue = Volley.newRequestQueue(this);
        userPreferences = new UserPreferences(OrderActivity.this);
        srTransaction = findViewById(R.id.sr_transaction);

        if(!userPreferences.checkLogin()){
            startActivity(new Intent(OrderActivity.this, LoginActivity.class));
            finish();
        }else{
            getTransactionDriver(userPreferences.getUserLoginId(),userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }

        srTransaction.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTransactionDriver(userPreferences.getUserLoginId(),userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            }
        });

        RecyclerView rv_transaction = findViewById(R.id.rv_transaction);
        transactionAdapter = new TransactionAdapter(new ArrayList<>(), this);
        rv_transaction.setLayoutManager(new LinearLayoutManager(OrderActivity.this, LinearLayoutManager.VERTICAL, false));
        rv_transaction.setAdapter(transactionAdapter);
        getTransactionDriver(userPreferences.getUserLoginId(),userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
    }

    private void getTransactionDriver(String id, String token, String token_type){
        srTransaction.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(GET, Api.GET_DRIVER_ORDER + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                TransactionResponse transactionResponse = gson.fromJson(response, TransactionResponse.class);
                transactionAdapter.setTransactionList(transactionResponse.getTransactionList());
                Toast.makeText(OrderActivity.this, transactionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                srTransaction.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                srTransaction.setRefreshing(false);
                try {
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(OrderActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(OrderActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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