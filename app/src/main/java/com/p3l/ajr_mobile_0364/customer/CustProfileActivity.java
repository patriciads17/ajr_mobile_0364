package com.p3l.ajr_mobile_0364.customer;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.p3l.ajr_mobile_0364.LoginActivity;
import com.p3l.ajr_mobile_0364.R;
import com.p3l.ajr_mobile_0364.api.Api;
import com.p3l.ajr_mobile_0364.driver.EditProfileActivity;
import com.p3l.ajr_mobile_0364.model.User;
import com.p3l.ajr_mobile_0364.model.response.CustomerResponse;
import com.p3l.ajr_mobile_0364.model.response.DriverResponse;
import com.p3l.ajr_mobile_0364.preference.UserPreferences;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustProfileActivity extends AppCompatActivity {

    private static final String[] GENDER = new String[]{"Female", "Male"};
    private AutoCompleteTextView tfData3;
    private RequestQueue queue;
    private TextView tvData1,tvData2;
    private UserPreferences userPreferences;
    private Button btn1;
    private LinearLayout layoutLoading;
    private TextInputLayout btn2,tl1;
    private CircleImageView profilpic;
    private CheckBox cb1;
    private TextInputEditText tfData1, tfData2, tfData4, tfData5, tfData6, tfData7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_profile);

        queue = Volley.newRequestQueue(this);
        btn1 = findViewById(R.id.btn_save);
        btn2 = findViewById(R.id.date_picker);
        tfData1 = findViewById(R.id.tf_name);
        tfData2 = findViewById(R.id.tf_birth);
        tfData3 = findViewById(R.id.tf_gender);
        tfData4 = findViewById(R.id.tf_address);
        tfData5 = findViewById(R.id.tf_phonenum);
        tfData6 = findViewById(R.id.tf_email);
        tfData7 = findViewById(R.id.tf_newpass);
        tvData1 = findViewById(R.id.tv_id);
        tvData2 = findViewById(R.id.tv_sts_akun);
        profilpic = findViewById(R.id.profile_pic);
        cb1 = findViewById(R.id.cb_pass);
        tl1 = findViewById(R.id.tl_pass);
        layoutLoading = findViewById(R.id.layout_loading);
        userPreferences = new UserPreferences(CustProfileActivity.this);

        tfData7.setVisibility(View.INVISIBLE);
        tl1.setVisibility(View.INVISIBLE);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        tfData2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                tfData2.setText(materialDatePicker.getHeaderText());
            }
        });

        if(!userPreferences.checkLogin()){
            startActivity(new Intent(CustProfileActivity.this, LoginActivity.class));
            finish();
        }else{
            getCustomerById(userPreferences.getUserLoginId(), userPreferences.getUserLoginToken(), userPreferences.getUserLoginTokenType());
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCustomer(userPreferences.getUserLoginId(), userPreferences.getUserLoginToken(), userPreferences.getUserLoginTokenType());
                startActivity(new Intent(CustProfileActivity.this, CustomerActivity.class));
                finish();
            }
        });

        cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((cb1.isChecked())){
                    tfData7.setVisibility(View.VISIBLE);
                    tl1.setVisibility(View.VISIBLE);
                }else{
                    tfData7.setVisibility(View.INVISIBLE);
                    tl1.setVisibility(View.INVISIBLE);
                }

            }
        });

        ArrayAdapter<String> adapterGender =
                new ArrayAdapter<>(this, R.layout.item_list, GENDER);
        tfData3.setAdapter(adapterGender);
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
                tvData2.setText(customerResponse.getUser().getStatus_akun());
                tfData1.setText(customerResponse.getUser().getNama_customer());
                tfData2.setText(customerResponse.getUser().getTgl_lahir_customer());
                tfData3.setText(customerResponse.getUser().getGender_customer());
                tfData4.setText(customerResponse.getUser().getAlamat_customer());
                tfData5.setText(customerResponse.getUser().getNo_telp_customer());
                tfData6.setText(customerResponse.getUser().getEmail());
                Toast.makeText(CustProfileActivity.this, customerResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CustProfileActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(CustProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateCustomer(String id, String token, String token_type){
        setLoading(true);
        User user= new User(
                id,
                tfData6.getText().toString(),
                tfData7.getText().toString(),
                tfData1.getText().toString(),
                tfData4.getText().toString(),
                tfData3.getText().toString(),
                tfData2.getText().toString(),
                tfData5.getText().toString()
        );

        StringRequest stringRequest = new StringRequest(POST, Api.UPDATE_CUST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                CustomerResponse customerResponse = gson.fromJson(response, CustomerResponse.class);
                setLoading(false);
                Toast.makeText(CustProfileActivity.this, customerResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setLoading(false);
                try {
                    String responseBody = new String(error.networkResponse.data,
                            StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);
                    Toast.makeText(CustProfileActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(CustProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        Glide.with(CustProfileActivity.this).load(Api.BASE_URL_IMG + url).into(profilpic);
    }
}