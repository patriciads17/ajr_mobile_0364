package com.p3l.ajr_mobile_0364.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.p3l.ajr_mobile_0364.R;
import com.p3l.ajr_mobile_0364.api.Api;
import com.p3l.ajr_mobile_0364.customer.CustomerActivity;
import com.p3l.ajr_mobile_0364.model.Car;

import java.util.List;

public class CarAdapter extends
        RecyclerView.Adapter<CarAdapter.ViewHolder> {
    private List<Car> carList;
    private Context context;

    public CarAdapter(List<Car> carList, Context context) {
        this.carList = carList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.rv_car, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.tvData1.setText("Daily Cost: Rp"+car.getTarif_harian_mobil().toString()+"/day");
        holder.tvData2.setText(car.getNama_mobil());
        holder.tvData3.setText(car.getTipe_mobil());
        holder.tvData4.setText(car.getJenis_transmisi());
        holder.tvData5.setText(car.getJenis_bahan_bakar());
        holder.tvData6.setText(car.getWarna_mobil());
        holder.tvData7.setText(car.getVol_bagasi());
        holder.tvData8.setText(car.getFasilitas_mobil());
        Glide.with(context).load(Api.BASE_URL_IMG + car.getUrl_car_img()).into(holder.imgCar);
    }
    public void setCarList(List<Car> carList){
        this.carList = carList;
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCar;
        TextView tvData1, tvData2, tvData3, tvData4, tvData5, tvData6, tvData7, tvData8 ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvData1 = itemView.findViewById(R.id.tv_costCar);
            tvData2 = itemView.findViewById(R.id.tv_namaCar);
            tvData3 = itemView.findViewById(R.id.tv_typeCar);
            tvData4 = itemView.findViewById(R.id.tv_transCar);
            tvData5 = itemView.findViewById(R.id.tv_fuelCar);
            tvData6 = itemView.findViewById(R.id.tv_colorCar);
            tvData7 = itemView.findViewById(R.id.tv_baggageCar);
            tvData8 = itemView.findViewById(R.id.tv_facilCar);
            imgCar = itemView.findViewById(R.id.profile_pic);
        }
    }


}
