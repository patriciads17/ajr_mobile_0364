package com.p3l.ajr_mobile_0364.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3l.ajr_mobile_0364.R;
import com.p3l.ajr_mobile_0364.manager.CarReportActivity;
import com.p3l.ajr_mobile_0364.model.Report;
import com.p3l.ajr_mobile_0364.model.Transaction;

import java.util.List;

public class IncomeReportAdapter extends
        RecyclerView.Adapter<IncomeReportAdapter.ViewHolder> {
    private List<Report> reportList;
    private Context context;

    public IncomeReportAdapter(List<Report> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_income_report, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.tvData1.setText(report.getNama_customer());
        holder.tvData2.setText(report.getNama_mobil());
        holder.tvData3.setText(report.getJenis_transaksi());
        holder.tvData4.setText(report.getJumlah_transaksi());
        holder.tvData5.setText(report.getJumlah_pendapatan());
    }

    public void setReportList(List<Report> reportList) {
        this.reportList = reportList;
    }

    public void cleanRecyview(){
        reportList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvData1, tvData2, tvData3, tvData4, tvData5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvData1 = itemView.findViewById(R.id.tv_name_cust);
            tvData2 = itemView.findViewById(R.id.tv_name_car);
            tvData3 = itemView.findViewById(R.id.tv_type);
            tvData4 = itemView.findViewById(R.id.tv_amount);
            tvData5 = itemView.findViewById(R.id.tv_income);

        }
    }
}
