package com.p3l.ajr_mobile_0364.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.p3l.ajr_mobile_0364.R;
import com.p3l.ajr_mobile_0364.model.Transaction;
import com.p3l.ajr_mobile_0364.preference.UserPreferences;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RatingAdapter extends
        RecyclerView.Adapter<RatingAdapter.ViewHolder> {
    private List<Transaction> transactionList;
    private Context context;

    public RatingAdapter(List<Transaction> transactionList, Context context) {
        this.transactionList = transactionList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_rating, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.tvData1.setText(transaction.getIdCustomer());
        holder.tvData2.setText(dateTimeChanger(transaction.getTgl_transaksi()));
        holder.tvData3.setText(transaction.getRating_driver());
        holder.tvData4.setText(transaction.getKomentar_driver());
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvData1, tvData2, tvData3, tvData4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvData1 = itemView.findViewById(R.id.tv_idUser);
            tvData2 = itemView.findViewById(R.id.tv_tgl_transaksi);
            tvData3 = itemView.findViewById(R.id.tv_rate2);
            tvData4 = itemView.findViewById(R.id.tv_comment);

        }
    }

    private String dateTimeChanger(String dateTime){
        DateTimeFormatter formatString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd MMM YYYY HH:mm");
        LocalDateTime parsedDateTime = LocalDateTime.parse(dateTime,formatString);
        String newdate = parsedDateTime.format(formatDate);

        return newdate;
    }
}