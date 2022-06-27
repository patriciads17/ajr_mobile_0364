package com.p3l.ajr_mobile_0364.adapter;

import static com.android.volley.Request.Method.GET;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.p3l.ajr_mobile_0364.R;
import com.p3l.ajr_mobile_0364.api.Api;
import com.p3l.ajr_mobile_0364.driver.DriverActivity;
import com.p3l.ajr_mobile_0364.driver.OrderActivity;
import com.p3l.ajr_mobile_0364.model.Car;
import com.p3l.ajr_mobile_0364.model.Transaction;
import com.p3l.ajr_mobile_0364.model.response.CarResponse;
import com.p3l.ajr_mobile_0364.model.response.DriverResponse;
import com.p3l.ajr_mobile_0364.preference.UserPreferences;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionAdapter extends
        RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactionList;
    private Context context;
    private TextView  tvData1, tvData2, tvOrder1, tvOrder2, tvOrder3, tvOrder4, tvOrder5, tvOrder6, tvOrder7;
    private Button btn1;
    private UserPreferences userPreferences;
    private RequestQueue queue;
    private Date startDate, endDate;
    private Long dateDiff;

    public TransactionAdapter(List<Transaction> transactionList, Context context) {
        this.transactionList = transactionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_complete_transaction, parent, false);

        userPreferences = new UserPreferences(parent.getContext());
        queue = Volley.newRequestQueue(parent.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.tvData1.setText(transaction.getId());
        holder.tvData2.setText(dateTimeChanger(transaction.getTgl_transaksi()));

        holder.cvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_detail_transaction);
                int width = WindowManager.LayoutParams.MATCH_PARENT;
                int height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setLayout(width,height);
                dialog.show();

                getDriverById(userPreferences.getUserLoginId(),userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());
                getDataCar(transaction.getIdCar(), userPreferences.getUserLoginToken(),userPreferences.getUserLoginTokenType());

                tvOrder1 = dialog.findViewById(R.id.tv_idTransaction);
                tvOrder2 = dialog.findViewById(R.id.tv_custName);
                tvOrder3 = dialog.findViewById(R.id.tv_transactionType);
                tvOrder4 = dialog.findViewById(R.id.tv_startDate);
                tvOrder5 = dialog.findViewById(R.id.tv_endDate);
                tvOrder6 = dialog.findViewById(R.id.tv_platNum);
                tvOrder7 = dialog.findViewById(R.id.tv_salary);
                btn1 = dialog.findViewById(R.id.btn_close);

                tvOrder1.setText(transaction.getId());
                tvOrder2.setText(transaction.getIdCustomer());
                tvOrder3.setText(transaction.getJenis_transaksi());
                tvOrder4.setText(dateTimeChanger(transaction.getTgl_mulai_sewa()));
                tvOrder5.setText(dateTimeChanger(transaction.getTgl_selesai_sewa()));

                startDate = dateTime(transaction.getTgl_mulai_sewa());
                endDate = dateTime(transaction.getTgl_pengembalian());
                dateDiff = ((endDate.getTime() - startDate.getTime())/(1000 * 60 * 60 * 24))%365;

                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvData1, tvData2;
        CardView cvData;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvData1 = itemView.findViewById(R.id.tv_IdTransaction);
            tvData2 = itemView.findViewById(R.id.tv_tgl_transaksi);
            cvData = itemView.findViewById(R.id.cv_order);
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

    private void getDataCar(String id, String token, String token_type){

        StringRequest stringRequest = new StringRequest(GET, Api.GET_CAR_BY_ID + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                CarResponse carResponse = gson.fromJson(response, CarResponse.class);
                tvOrder6.setText(carResponse.getCar().getNo_plat());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        };
        queue.add(stringRequest);
    }

    private void getDriverById(String id, String token, String token_type){

        StringRequest stringRequest = new StringRequest(GET, Api.GET_DRV_BY_ID + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                DriverResponse driverResponse = gson.fromJson(response, DriverResponse.class);
                long cost = Long.parseLong(driverResponse.getUser().getTarif_harian_driver()) * dateDiff;
                tvOrder7.setText(Long.toString(cost));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        };
        queue.add(stringRequest);
    }
}
