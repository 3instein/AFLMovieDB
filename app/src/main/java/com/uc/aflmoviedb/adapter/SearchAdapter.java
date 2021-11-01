package com.uc.aflmoviedb.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uc.aflmoviedb.R;
import com.uc.aflmoviedb.helper.Const;
import com.uc.aflmoviedb.model.Search;
import com.uc.aflmoviedb.view.fragments.MovieDetailsFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    public SearchAdapter() {
    }

    private Context context;
    private List<Search.Results> listSearch = new ArrayList<>();

    public SearchAdapter(Context context) {
        this.context = context;
    }

    public List<Search.Results> getListSearch() {
        return listSearch;
    }

    public void setListSearch(List<Search.Results> listSearch) {
        this.listSearch = listSearch;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_upcoming, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        final Search.Results results = getListSearch().get(position);
        if (results != null) {
            holder.lbl_title.setText(results.getTitle());
            holder.lbl_overview.setText(results.getOverview());
            holder.lbl_release_date.setText(results.getRelease_date());
            Glide.with(context).load(Const.IMG_URL + results.getPoster_path()).into(holder.img_poster);
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment movieDetails = new MovieDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("movie", results);
                    bundle.putIntegerArrayList("genre_ids", (ArrayList<Integer>) results.getGenre_ids());
                    movieDetails.setArguments(bundle);
                    Navigation.findNavController(view).navigate(R.id.MovieDetailsFragment, bundle);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getListSearch().size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView img_poster;
        TextView lbl_title, lbl_overview, lbl_release_date;
        CardView cv;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            img_poster = itemView.findViewById(R.id.img_poster_card_upcoming);
            lbl_title = itemView.findViewById(R.id.lbl_title_card_upcoming);
            lbl_overview = itemView.findViewById(R.id.lbl_overview_card_upcoming);
            lbl_release_date = itemView.findViewById(R.id.lbl_release_date_card_upcoming);
            cv = itemView.findViewById(R.id.cv_card_upcoming);
        }
    }
}
