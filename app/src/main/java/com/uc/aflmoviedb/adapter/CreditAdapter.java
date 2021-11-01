package com.uc.aflmoviedb.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uc.aflmoviedb.R;
import com.uc.aflmoviedb.helper.Const;
import com.uc.aflmoviedb.model.Credit;

import java.util.List;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.CreditViewHolder> {
    public CreditAdapter(){}

    private Context context;
    private List<Credit.Cast> listCreditCast;

    public CreditAdapter(Context context){
        this.context = context;
    }

    private List<Credit.Cast> getListCreditCast(){
        return listCreditCast;
    }
    public void setListCreditCast(List<Credit.Cast> listCreditCast){
        this.listCreditCast = listCreditCast;
    }

    @NonNull
    @Override
    public CreditAdapter.CreditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movie_details_misc, parent, false);
        return new CreditAdapter.CreditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditAdapter.CreditViewHolder holder, int position) {
        final Credit.Cast results = getListCreditCast().get(position);
        Glide.with(context).load(Const.IMG_URL + results.getProfile_path()).into(holder.img_cast);
        holder.img_cast.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, results.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListCreditCast().size();
    }

    public class CreditViewHolder extends RecyclerView.ViewHolder {
        ImageView img_cast;
        CardView cv;

        public CreditViewHolder(@NonNull View itemView) {
            super(itemView);
            img_cast = itemView.findViewById(R.id.img_movie_details_misc);
            cv = itemView.findViewById(R.id.cv_card_movie_details_misc);
        }
    }
}
