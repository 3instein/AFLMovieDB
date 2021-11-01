package com.uc.aflmoviedb.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.uc.aflmoviedb.R;
import com.uc.aflmoviedb.helper.Const;
import com.uc.aflmoviedb.model.NowPlaying;
import com.uc.aflmoviedb.view.fragments.MovieDetailsFragment;

import java.util.ArrayList;
import java.util.List;

public class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.NowPlayingViewHolder> {

    final int VIEW_TYPE_LOADING = 0;
    final int VIEW_TYPE_ITEM = 1;

    public NowPlayingAdapter(){

    }

    private Context context;
    private List<NowPlaying.Results> listNowPlaying = new ArrayList<>();

    public NowPlayingAdapter(Context context){
        this.context = context;
    }

    public List<NowPlaying.Results> getListNowPlaying(){
        return listNowPlaying;
    }

    public void setListNowPlaying(List<NowPlaying.Results> listNowPlaying){
        this.listNowPlaying = listNowPlaying;
    }

    @NonNull
    @Override
    public NowPlayingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_now_playing, parent, false);
            return new NowPlayingAdapter.NowPlayingViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NowPlayingViewHolder holder, int position) {
        final NowPlaying.Results results = getListNowPlaying().get(position);
        if(results != null) {
            holder.lbl_title.setText(results.getTitle());
            if (!results.getOverview().isEmpty()) {
                holder.lbl_overview.setText(results.getOverview());
            } else {
                holder.lbl_overview.setText("No overview");
            }
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
    public int getItemViewType(int position) {
        return listNowPlaying.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return getListNowPlaying().size();
    }

    public class NowPlayingViewHolder extends RecyclerView.ViewHolder {
        ImageView img_poster;
        TextView lbl_title, lbl_overview, lbl_release_date;
        CardView cv;
        public NowPlayingViewHolder(@NonNull View itemView) {
            super(itemView);
            img_poster = itemView.findViewById(R.id.img_poster_card_nowplaying);
            lbl_title = itemView.findViewById(R.id.lbl_title_card_nowplaying);
            lbl_overview = itemView.findViewById(R.id.lbl_overview_card_nowplaying);
            lbl_release_date = itemView.findViewById(R.id.lbl_release_date_card_nowplaying);
            cv = itemView.findViewById(R.id.cv_card_now_playing);
        }
    }

    public class LoadingHolder extends NowPlayingViewHolder {

        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
