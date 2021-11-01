package com.uc.aflmoviedb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uc.aflmoviedb.R;
import com.uc.aflmoviedb.helper.Const;
import com.uc.aflmoviedb.model.Similar;

import java.util.List;

public class SimilarAdapter extends RecyclerView.Adapter<SimilarAdapter.SimilarViewHolder> {
    public SimilarAdapter(){}

    private Context context;
    private List<Similar.Results> listSimilar;

    public SimilarAdapter(Context context){
        this.context = context;
    }

    private List<Similar.Results> getListSimilar(){
        return listSimilar;
    }
    public void setListSimilar(List<Similar.Results> listSimilar){
        this.listSimilar = listSimilar;
    }

    @NonNull
    @Override
    public SimilarAdapter.SimilarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_movie_details_misc, parent, false);
        return new SimilarAdapter.SimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarAdapter.SimilarViewHolder holder, int position) {
        final Similar.Results results = getListSimilar().get(position);
        Glide.with(context).load(Const.IMG_URL + results.getPoster_path()).into(holder.img_poster);
    }

    @Override
    public int getItemCount() {
        return getListSimilar().size();
    }

    public class SimilarViewHolder extends RecyclerView.ViewHolder {
        ImageView img_poster;
        CardView cv;

        public SimilarViewHolder(@NonNull View itemView) {
            super(itemView);
            img_poster = itemView.findViewById(R.id.img_movie_details_misc);
            cv = itemView.findViewById(R.id.cv_card_movie_details_misc);
        }

    }
}
