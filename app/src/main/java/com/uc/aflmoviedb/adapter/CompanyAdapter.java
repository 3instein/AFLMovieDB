package com.uc.aflmoviedb.adapter;

import android.content.Context;
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
import com.uc.aflmoviedb.model.Movie;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder> {
    public CompanyAdapter(){}

    private Context context;
    private List<Movie.ProductionCompanies> listCompanies;

    public CompanyAdapter(Context context) {
        this.context = context;
    }

    private List<Movie.ProductionCompanies> getListCompanies(){
        return listCompanies;
    }
    public void setListCompanies(List<Movie.ProductionCompanies> listCompanies){
        this.listCompanies = listCompanies;
    }

    @NonNull
    @Override
    public CompanyAdapter.CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movie_details_misc, parent, false);
        return new CompanyAdapter.CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyAdapter.CompanyViewHolder holder, int position) {
        final Movie.ProductionCompanies results = getListCompanies().get(position);
        Glide.with(context).load(Const.IMG_URL + results.getLogo_path()).into(holder.img_company);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, results.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getListCompanies().size();
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder {
        ImageView img_company;
        CardView cv;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_company = itemView.findViewById(R.id.img_movie_details_misc);
            cv = itemView.findViewById(R.id.cv_card_movie_details_misc);
        }
    }
}
