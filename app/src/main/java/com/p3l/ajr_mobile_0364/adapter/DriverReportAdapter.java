package com.p3l.ajr_mobile_0364.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3l.ajr_mobile_0364.R;
import com.p3l.ajr_mobile_0364.model.Report;

import java.util.List;

public class DriverReportAdapter extends
        RecyclerView.Adapter<DriverReportAdapter.ViewHolder> {
    private List<Report> reportList;
    private Context context;

    public DriverReportAdapter(List<Report> reportList, Context context) {
        this.reportList = reportList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_report, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.tvData1.setText(report.getId());
        holder.tvData2.setText(report.getNama_driver());
        holder.tvData3.setText(report.getJumlah_transaksi());
        holder.tvData4.setText(report.getRerata_rating());
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
        TextView tvData1, tvData2, tvData3, tvData4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvData1 = itemView.findViewById(R.id.tv_data1);
            tvData2 = itemView.findViewById(R.id.tv_data2);
            tvData3 = itemView.findViewById(R.id.tv_data3);
            tvData4 = itemView.findViewById(R.id.tv_data4);
        }
    }
}
