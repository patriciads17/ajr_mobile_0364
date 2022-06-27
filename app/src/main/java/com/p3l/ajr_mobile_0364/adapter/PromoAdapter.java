package com.p3l.ajr_mobile_0364.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p3l.ajr_mobile_0364.R;
import com.p3l.ajr_mobile_0364.model.Promo;
import com.p3l.ajr_mobile_0364.model.Transaction;

import java.util.List;

public class PromoAdapter extends
        RecyclerView.Adapter<PromoAdapter.ViewHolder> {
    private List<Promo> promoList;
    private Context context;

    public PromoAdapter(List<Promo> promoList, Context context) {
        this.promoList = promoList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_promo, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Promo promo = promoList.get(position);
        holder.tvData1.setText(promo.getKode_promo());
        holder.tvData2.setText(promo.getJenis_promo());
        holder.tvData3.setText(promo.getSyarat_promo());
        holder.tvData4.setText(promo.getBesar_potongan() + "%");
    }

    public void setPromoList(List<Promo> promoList){
        this.promoList = promoList;
    }

    @Override
    public int getItemCount() {
        return promoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvData1, tvData2, tvData3, tvData4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvData1 = itemView.findViewById(R.id.tv_codeDisc);
            tvData2 = itemView.findViewById(R.id.tv_typeDisc);
            tvData3 = itemView.findViewById(R.id.tv_condition);
            tvData4 = itemView.findViewById(R.id.tv_disc);
        }
    }
}
