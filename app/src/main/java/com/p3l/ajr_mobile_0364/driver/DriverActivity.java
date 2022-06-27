package com.p3l.ajr_mobile_0364.driver;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.p3l.ajr_mobile_0364.model.response.CarResponse;
import com.p3l.ajr_mobile_0364.model.response.DriverResponse;
import com.p3l.ajr_mobile_0364.model.response.TransactionResponse;
import com.p3l.ajr_mobile_0364.preference.UserPreferences;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DriverActivity extends AppCompatActivity {

    private UserPreferences userPreferences;
    private FloatingActionButton floatingActionButton1,floatingActionButton2;
    private BottomNavigationView bottomNavigationView;
    private RequestQueue queue;
    private CircleImageView profilpic;
    private LinearLayout layoutLoading, layoutUpcoming;
    private TextView tvUser, tvInfo1, tvInfo2, tvInfo3, tvData1, tvData2, tvOrder1, tvOrder2, tvOrder3, tvOrder4, tvOrder5, tvOrder6, tvOrder7;

    private BottomNavigationView.OnNavigationItemSelectedListener navigation = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            if(item.getItemId() == R.id.nav_home){
                startActivity(new Intent(DriverActivity.this, DriverActivity.class));
            }else if(item.getItemId() == R.id.nav_order){
                startActivity(new Intent(DriverActivity.this, OrderActivity.class));
            }else if(item.getItemId() == R.id.nav_rating){
                startActivity(new Intent(DriverActivity.this, RatingActivity.class));
            }else if(item.getItemId() == R.id.nav_myProfile){
                startActivity(new Intent(DriverActivity.this, ProfileActivity.class));
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        bottomNavigationView = findViewById(R.id.btmNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigation);
        queue = Volley.newRequestQueue(this);
        tvUser = findViewById(R.id.tv_availability);
        tvInfo1 = findViewById(R.id.tv_amount);
        tvInfo2 = findViewById(R.id.tv_rate);
        tvInfo3 = findViewById(R.id.tv_rank);
        tvData1 = findViewById(R.id.tv_Id);
        tvData2 = findViewById(R.id.tv_Nama);
        tvOrder1 = findViewById(R.id.tv_idTransaction);
        tvOrder2 = findViewById(R.id.tv_custName);
        tvOrder3 = findViewById(R.id.tv_transactionType);
        tvOrder4 = findViewById(R.id.tv_startDate);
        tvOrder5 = findViewById(R.id.tv_endDate);
        tvOrder6 = findViewById(R.id.tv_platNum);
        profilpic = findViewById(R.id.profile_pic);
        layoutLoading = findViewById(R.id.layout_loading);
        layoutUpcoming = findViewById(R.id.ll_upcomingOrder);
        userPreferences = new UserPreferences(DriverActivity.this);
        floatingActionButton1 = findViewById(R.id.fab_availability_on);
        floatingActionButton2 = findViewById(R.id.fab_availability_off);

        tvData1.setText(userPreferences.getUserLoginId());

        if(!userPreferences.checkLogin()){
            startActivity(new Intent(DriverActivity.this, LoginActivity.class));
            finish();
        }else{
            updateAverageRate(userPreferences.getUserLoginId(),userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getUpcomingOrder(userPreferences.getUserLoginId(),userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
            getDriverById(userPreferences.getUserLoginId(),userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
        }

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAvailability(userPreferences.getUserLoginId(),userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType(),"Available");
            }
        });

        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAvailability(userPreferences.getUserLoginId(),userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType(),"Unavailable");
            }
        });
    }

    private void getUpcomingOrder(String id, String token, String token_type){
        setLoading(true);

        StringRequest stringRequest = new StringRequest(GET, Api.GET_UPCOMING_ORDER + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                TransactionResponse transactionResponse = gson.fromJson(response, TransactionResponse.class);
                if(transactionResponse.getTransaction() == null){
                    Toast.makeText(DriverActivity.this,transactionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    layoutUpcoming.setVisibility(View.INVISIBLE);
                }else{
                    getDataCar(transactionResponse.getTransaction().getIdCar(), userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
                    layoutUpcoming.setVisibility(View.VISIBLE);
                    tvOrder1.setText(transactionResponse.getTransaction().getId());
                    tvOrder2.setText(transactionResponse.getTransaction().getIdCustomer());
                    tvOrder3.setText(transactionResponse.getTransaction().getJenis_transaksi());
                    tvOrder4.setText(dateTimeChanger(transactionResponse.getTransaction().getTgl_mulai_sewa()));
                    tvOrder5.setText(dateTimeChanger(transactionResponse.getTransaction().getTgl_selesai_sewa()));
                    Toast.makeText(DriverActivity.this,transactionResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DriverActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(DriverActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getDriverById(String id, String token, String token_type){
        setLoading(true);

        StringRequest stringRequest = new StringRequest(GET, Api.GET_DRV_BY_ID + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                DriverResponse driverResponse = gson.fromJson(response, DriverResponse.class);
                loadImage(driverResponse.getUser().getUrl_foto_driver());
                tvUser.setText("You are "+driverResponse.getUser().getStatus_ketersediaan_driver());
                tvInfo2.setText(driverResponse.getUser().getRerata_rating());
                tvData2.setText(driverResponse.getUser().getNama_driver());
                if(driverResponse.getUser().getStatus_ketersediaan_driver().equalsIgnoreCase("Available")){
                    floatingActionButton2.show();
                    floatingActionButton1.hide();
                }else if(driverResponse.getUser().getStatus_ketersediaan_driver().equalsIgnoreCase("Unavailable")){
                    floatingActionButton2.hide();
                    floatingActionButton1.show();
                }else if(driverResponse.getUser().getStatus_ketersediaan_driver().equalsIgnoreCase("Occupied")){
                    floatingActionButton2.hide();
                    floatingActionButton1.hide();
                }
                Toast.makeText(DriverActivity.this, driverResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DriverActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(DriverActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void getDataCar(String id, String token, String token_type){
        setLoading(true);

        StringRequest stringRequest = new StringRequest(GET, Api.GET_CAR_BY_ID + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                CarResponse carResponse = gson.fromJson(response, CarResponse.class);
                tvOrder6.setText(carResponse.getCar().getNo_plat());
                Toast.makeText(DriverActivity.this,carResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DriverActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(DriverActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateAverageRate(String id, String token, String token_type){
        setLoading(true);
        User user= new User(id, "", "");

        StringRequest stringRequest = new StringRequest(POST, Api.UPDATE_AVERAGE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                DriverResponse driverResponse = gson.fromJson(response, DriverResponse.class);
                tvInfo1.setText(driverResponse.getAmount_order().toString());

                if(driverResponse.getAmount_order() == 0){
                    tvInfo3.setText("-");
                }else if(driverResponse.getAmount_order() <= 10){
                    tvInfo3.setText("Newbie");
                }else if(driverResponse.getAmount_order() >= 25){
                    tvInfo3.setText("Intern");
                }else if(driverResponse.getAmount_order() >= 50){
                    tvInfo3.setText("Contract");
                }else if(driverResponse.getAmount_order() > 60){
                    tvInfo3.setText("Permanent");
                }
                setLoading(false);
                Toast.makeText(DriverActivity.this, driverResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);
                try {
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(DriverActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(DriverActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateAvailability(String id, String token, String token_type, String availability){
        setLoading(true);
        User user= new User(id, availability, "");

        StringRequest stringRequest = new StringRequest(POST, Api.UPDATE_AVAILABILITY, new Response.Listener<String>() {
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
                    Toast.makeText(DriverActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(DriverActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private String dateTimeChanger(String dateTime){
        DateTimeFormatter formatString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd MMM YYYY HH:mm");
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime,formatString);
        String newdate = parsedDateTime.format(formatDate);

        return newdate;
    }

    private Date dateTime(String dateTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date newDate = null;
        try {
            newDate = sdf.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return newDate;
    }

    private void loadImage(String url){
        Glide.with(DriverActivity.this).load(Api.BASE_URL_IMG + url).into(profilpic);
    }
}