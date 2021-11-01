package com.uc.aflmoviedb.view.fragments;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.uc.aflmoviedb.R;
import com.uc.aflmoviedb.adapter.CompanyAdapter;
import com.uc.aflmoviedb.adapter.CreditAdapter;
import com.uc.aflmoviedb.adapter.SimilarAdapter;
import com.uc.aflmoviedb.helper.Const;
import com.uc.aflmoviedb.model.Credit;
import com.uc.aflmoviedb.model.Genre;
import com.uc.aflmoviedb.model.Movie;
import com.uc.aflmoviedb.model.NowPlaying;
import com.uc.aflmoviedb.model.Popular;
import com.uc.aflmoviedb.model.Search;
import com.uc.aflmoviedb.model.Similar;
import com.uc.aflmoviedb.model.Upcoming;
import com.uc.aflmoviedb.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailsFragment newInstance(String param1, String param2) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<Integer> genres;
    private MovieViewModel viewModel;
    private RecyclerView rv_similar, rv_credit, rv_company;
    private ImageView backdrop, poster;
    private TextView title, overview, release_date, score, popularity, genre, lbl_similar, lbl_casts, lbl_companies;
    private ProgressBar pbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        Bundle bundle = this.getArguments();
        
        if(bundle.getParcelable("movie") instanceof NowPlaying.Results){
            NowPlaying.Results movie = bundle.getParcelable("movie");
            genres = bundle.getIntegerArrayList("genre_ids");
            loadNowPlaying(view, movie, genres);
        } else if(bundle.getParcelable("movie") instanceof Upcoming.Results){
            Upcoming.Results movie = bundle.getParcelable("movie");
            genres = bundle.getIntegerArrayList("genre_ids");
            loadUpcoming(view, movie, genres);
        } else if(bundle.getParcelable("movie") instanceof Popular.Results){
            Popular.Results movie = bundle.getParcelable("movie");
            genres = bundle.getIntegerArrayList("genre_ids");
            loadPopular(view, movie, genres);
        } else {
            Search.Results movie = bundle.getParcelable("movie");
            genres = bundle.getIntegerArrayList("genre_ids");
            loadSearch(view, movie, genres);
        }

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadNowPlaying(View view, NowPlaying.Results movie, ArrayList<Integer> genres){
        rv_similar = view.findViewById(R.id.rv_movie_details_similar);
        rv_credit = view.findViewById(R.id.rv_movie_details_casts);
        rv_company = view.findViewById(R.id.rv_movie_details_companies);

        lbl_similar = view.findViewById(R.id.lbl_movie_details_similar);
        lbl_casts = view.findViewById(R.id.lbl_movie_details_casts);
        lbl_companies = view.findViewById(R.id.lbl_movie_details_companies);

        backdrop = view.findViewById(R.id.image_backdrop);
        poster = view.findViewById(R.id.lbl_movie_details_poster);
        title = view.findViewById(R.id.lbl_movie_details_title);
        overview = view.findViewById(R.id.lbl_movie_details_overview);
        score = view.findViewById(R.id.lbl_movie_details_score);
        popularity = view.findViewById(R.id.lbl_movie_details_popularity);
        release_date = view.findViewById(R.id.lbl_movie_details_release_date);
        genre = view.findViewById(R.id.lbl_movie_details_genres);

        pbar = view.findViewById(R.id.pbar_movie_details);

        viewModel = new ViewModelProvider(MovieDetailsFragment.this).get(MovieViewModel.class);

        rv_similar.setVisibility(View.GONE);
        rv_credit.setVisibility(View.GONE);
        rv_company.setVisibility(View.GONE);

        lbl_similar.setVisibility(View.GONE);
        lbl_casts.setVisibility(View.GONE);
        lbl_companies.setVisibility(View.GONE);

        backdrop.setVisibility(View.GONE);
        poster.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        overview.setVisibility(View.GONE);
        score.setVisibility(View.GONE);
        popularity.setVisibility(View.GONE);
        release_date.setVisibility(View.GONE);
        genre.setVisibility(View.GONE);

        title.setText(movie.getTitle());
        if(!movie.getOverview().isEmpty()) {
            overview.setText(movie.getOverview());
            overview.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        } else {
            overview.setText("No Overview yet!");
        }
        String score_string = "Score: " + movie.getVote_average() + "/10.0" + " (" + movie.getVote_count() + ")";
        score.setText(score_string);
        String popularity_Sting = "Popularity: " + movie.getPopularity();
        popularity.setText(popularity_Sting);
        release_date.setText(String.format("%4.4s", movie.getRelease_date()));
        Glide.with(getContext())
                .load(Const.IMG_URL + movie.getBackdrop_path())
                .apply(bitmapTransform(new BlurTransformation(22)))
                .into(backdrop);
        Glide.with(getContext()).load(Const.IMG_URL + movie.getPoster_path()).into(poster);

        viewModel.getGenres(genres);
        viewModel.getResultGenres().observe(getViewLifecycleOwner(), new Observer<List<Genre.Genres>>() {
            @Override
            public void onChanged(List<Genre.Genres> genres) {
                StringBuilder genreToSet = new StringBuilder();
                for (int i = 0; i < genres.size(); i++) {
                    if (i != genres.size() - 1) {
                        genreToSet.append(genres.get(i).getName() + ", ");
                    } else {
                        genreToSet.append(genres.get(i).getName());
                    }
                }

                genre.setText("Genres: " + genreToSet);
            }
        });

        viewModel.getSimilar(movie.getId());
        viewModel.getResultSimilar().observe(getViewLifecycleOwner(), new Observer<Similar>() {
            @Override
            public void onChanged(Similar similar) {
                rv_similar.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                SimilarAdapter adapter = new SimilarAdapter(getActivity());
                adapter.setListSimilar(similar.getResults());
                rv_similar.setAdapter(adapter);
            }
        });

        viewModel.getCredit(movie.getId());
        viewModel.getResultCredit().observe(getViewLifecycleOwner(), new Observer<Credit>() {
            @Override
            public void onChanged(Credit credit) {
                rv_credit.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                CreditAdapter adapter = new CreditAdapter(getActivity());
                adapter.setListCreditCast(credit.getCast());
                rv_credit.setAdapter(adapter);
            }
        });

        viewModel.getMovie(movie.getId());
        viewModel.getResultMovie().observe(getViewLifecycleOwner(), new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                rv_company.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                CompanyAdapter adapter = new CompanyAdapter(getActivity());
                adapter.setListCompanies(movie.getProduction_companies());
                rv_company.setAdapter(adapter);
            }
        });

        backdrop.setVisibility(View.VISIBLE);
        poster.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        overview.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);
        popularity.setVisibility(View.VISIBLE);
        release_date.setVisibility(View.VISIBLE);
        genre.setVisibility(View.VISIBLE);

        lbl_similar.setVisibility(View.VISIBLE);
        lbl_casts.setVisibility(View.VISIBLE);
        lbl_companies.setVisibility(View.VISIBLE);

        rv_similar.setVisibility(View.VISIBLE);
        rv_credit.setVisibility(View.VISIBLE);
        rv_company.setVisibility(View.VISIBLE);

        pbar.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadUpcoming(View view, Upcoming.Results movie, ArrayList<Integer> genres){
        rv_similar = view.findViewById(R.id.rv_movie_details_similar);
        rv_credit = view.findViewById(R.id.rv_movie_details_casts);
        rv_company = view.findViewById(R.id.rv_movie_details_companies);

        lbl_similar = view.findViewById(R.id.lbl_movie_details_similar);
        lbl_casts = view.findViewById(R.id.lbl_movie_details_casts);
        lbl_companies = view.findViewById(R.id.lbl_movie_details_companies);

        backdrop = view.findViewById(R.id.image_backdrop);
        poster = view.findViewById(R.id.lbl_movie_details_poster);
        title = view.findViewById(R.id.lbl_movie_details_title);
        overview = view.findViewById(R.id.lbl_movie_details_overview);
        score = view.findViewById(R.id.lbl_movie_details_score);
        popularity = view.findViewById(R.id.lbl_movie_details_popularity);
        release_date = view.findViewById(R.id.lbl_movie_details_release_date);
        genre = view.findViewById(R.id.lbl_movie_details_genres);

        pbar = view.findViewById(R.id.pbar_movie_details);

        viewModel = new ViewModelProvider(MovieDetailsFragment.this).get(MovieViewModel.class);

        rv_similar.setVisibility(View.GONE);
        rv_credit.setVisibility(View.GONE);
        rv_company.setVisibility(View.GONE);

        lbl_similar.setVisibility(View.GONE);
        lbl_casts.setVisibility(View.GONE);
        lbl_companies.setVisibility(View.GONE);

        backdrop.setVisibility(View.GONE);
        poster.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        overview.setVisibility(View.GONE);
        score.setVisibility(View.GONE);
        popularity.setVisibility(View.GONE);
        release_date.setVisibility(View.GONE);
        genre.setVisibility(View.GONE);

        title.setText(movie.getTitle());
        if(!movie.getOverview().isEmpty()) {
            overview.setText(movie.getOverview());
            overview.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        } else {
            overview.setText("No Overview yet!");
        }
        String score_string = "Score: " + movie.getVote_average() + "/10.0" + " (" + movie.getVote_count() + ")";
        score.setText(score_string);
        String popularity_Sting = "Popularity: " + movie.getPopularity();
        popularity.setText(popularity_Sting);
        release_date.setText(String.format("%4.4s", movie.getRelease_date()));
        Glide.with(getContext())
                .load(Const.IMG_URL + movie.getBackdrop_path())
                .apply(bitmapTransform(new BlurTransformation(22)))
                .into(backdrop);
        Glide.with(getContext()).load(Const.IMG_URL + movie.getPoster_path()).into(poster);

        viewModel.getGenres(genres);
        viewModel.getResultGenres().observe(getViewLifecycleOwner(), new Observer<List<Genre.Genres>>() {
            @Override
            public void onChanged(List<Genre.Genres> genres) {
                StringBuilder genreToSet = new StringBuilder();
                for (int i = 0; i < genres.size(); i++) {
                    if (i != genres.size() - 1) {
                        genreToSet.append(genres.get(i).getName() + ", ");
                    } else {
                        genreToSet.append(genres.get(i).getName());
                    }
                }

                genre.setText("Genres: " + genreToSet);
            }
        });

        viewModel.getSimilar(movie.getId());
        viewModel.getResultSimilar().observe(getViewLifecycleOwner(), new Observer<Similar>() {
            @Override
            public void onChanged(Similar similar) {
                rv_similar.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                SimilarAdapter adapter = new SimilarAdapter(getActivity());
                adapter.setListSimilar(similar.getResults());
                rv_similar.setAdapter(adapter);
            }
        });

        viewModel.getCredit(movie.getId());
        viewModel.getResultCredit().observe(getViewLifecycleOwner(), new Observer<Credit>() {
            @Override
            public void onChanged(Credit credit) {
                rv_credit.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                CreditAdapter adapter = new CreditAdapter(getActivity());
                adapter.setListCreditCast(credit.getCast());
                rv_credit.setAdapter(adapter);
            }
        });

        viewModel.getMovie(movie.getId());
        viewModel.getResultMovie().observe(getViewLifecycleOwner(), new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                rv_company.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                CompanyAdapter adapter = new CompanyAdapter(getActivity());
                adapter.setListCompanies(movie.getProduction_companies());
                rv_company.setAdapter(adapter);
            }
        });

        backdrop.setVisibility(View.VISIBLE);
        poster.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        overview.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);
        popularity.setVisibility(View.VISIBLE);
        release_date.setVisibility(View.VISIBLE);
        genre.setVisibility(View.VISIBLE);

        lbl_similar.setVisibility(View.VISIBLE);
        lbl_casts.setVisibility(View.VISIBLE);
        lbl_companies.setVisibility(View.VISIBLE);

        rv_similar.setVisibility(View.VISIBLE);
        rv_credit.setVisibility(View.VISIBLE);
        rv_company.setVisibility(View.VISIBLE);

        pbar.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadPopular(View view, Popular.Results movie, ArrayList<Integer> genres){
        rv_similar = view.findViewById(R.id.rv_movie_details_similar);
        rv_credit = view.findViewById(R.id.rv_movie_details_casts);
        rv_company = view.findViewById(R.id.rv_movie_details_companies);

        lbl_similar = view.findViewById(R.id.lbl_movie_details_similar);
        lbl_casts = view.findViewById(R.id.lbl_movie_details_casts);
        lbl_companies = view.findViewById(R.id.lbl_movie_details_companies);

        backdrop = view.findViewById(R.id.image_backdrop);
        poster = view.findViewById(R.id.lbl_movie_details_poster);
        title = view.findViewById(R.id.lbl_movie_details_title);
        overview = view.findViewById(R.id.lbl_movie_details_overview);
        score = view.findViewById(R.id.lbl_movie_details_score);
        popularity = view.findViewById(R.id.lbl_movie_details_popularity);
        release_date = view.findViewById(R.id.lbl_movie_details_release_date);
        genre = view.findViewById(R.id.lbl_movie_details_genres);

        pbar = view.findViewById(R.id.pbar_movie_details);

        viewModel = new ViewModelProvider(MovieDetailsFragment.this).get(MovieViewModel.class);

        rv_similar.setVisibility(View.GONE);
        rv_credit.setVisibility(View.GONE);
        rv_company.setVisibility(View.GONE);

        lbl_similar.setVisibility(View.GONE);
        lbl_casts.setVisibility(View.GONE);
        lbl_companies.setVisibility(View.GONE);

        backdrop.setVisibility(View.GONE);
        poster.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        overview.setVisibility(View.GONE);
        score.setVisibility(View.GONE);
        popularity.setVisibility(View.GONE);
        release_date.setVisibility(View.GONE);
        genre.setVisibility(View.GONE);

        title.setText(movie.getTitle());
        if(!movie.getOverview().isEmpty()) {
            overview.setText(movie.getOverview());
            overview.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        } else {
            overview.setText("No Overview yet!");
        }
        String score_string = "Score: " + movie.getVote_average() + "/10.0" + " (" + movie.getVote_count() + ")";
        score.setText(score_string);
        String popularity_Sting = "Popularity: " + movie.getPopularity();
        popularity.setText(popularity_Sting);
        release_date.setText(String.format("%4.4s", movie.getRelease_date()));
        Glide.with(getContext())
                .load(Const.IMG_URL + movie.getBackdrop_path())
                .apply(bitmapTransform(new BlurTransformation(22)))
                .into(backdrop);
        Glide.with(getContext()).load(Const.IMG_URL + movie.getPoster_path()).into(poster);

        viewModel.getGenres(genres);
        viewModel.getResultGenres().observe(getViewLifecycleOwner(), new Observer<List<Genre.Genres>>() {
            @Override
            public void onChanged(List<Genre.Genres> genres) {
                StringBuilder genreToSet = new StringBuilder();
                for (int i = 0; i < genres.size(); i++) {
                    if (i != genres.size() - 1) {
                        genreToSet.append(genres.get(i).getName() + ", ");
                    } else {
                        genreToSet.append(genres.get(i).getName());
                    }
                }

                genre.setText("Genres: " + genreToSet);
            }
        });

        viewModel.getSimilar(movie.getId());
        viewModel.getResultSimilar().observe(getViewLifecycleOwner(), new Observer<Similar>() {
            @Override
            public void onChanged(Similar similar) {
                rv_similar.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                SimilarAdapter adapter = new SimilarAdapter(getActivity());
                adapter.setListSimilar(similar.getResults());
                rv_similar.setAdapter(adapter);
            }
        });

        viewModel.getCredit(movie.getId());
        viewModel.getResultCredit().observe(getViewLifecycleOwner(), new Observer<Credit>() {
            @Override
            public void onChanged(Credit credit) {
                rv_credit.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                CreditAdapter adapter = new CreditAdapter(getActivity());
                adapter.setListCreditCast(credit.getCast());
                rv_credit.setAdapter(adapter);
            }
        });

        viewModel.getMovie(movie.getId());
        viewModel.getResultMovie().observe(getViewLifecycleOwner(), new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                rv_company.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                CompanyAdapter adapter = new CompanyAdapter(getActivity());
                adapter.setListCompanies(movie.getProduction_companies());
                rv_company.setAdapter(adapter);
            }
        });

        backdrop.setVisibility(View.VISIBLE);
        poster.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        overview.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);
        popularity.setVisibility(View.VISIBLE);
        release_date.setVisibility(View.VISIBLE);
        genre.setVisibility(View.VISIBLE);

        lbl_similar.setVisibility(View.VISIBLE);
        lbl_casts.setVisibility(View.VISIBLE);
        lbl_companies.setVisibility(View.VISIBLE);

        rv_similar.setVisibility(View.VISIBLE);
        rv_credit.setVisibility(View.VISIBLE);
        rv_company.setVisibility(View.VISIBLE);

        pbar.setVisibility(View.GONE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadSearch(View view, Search.Results movie, ArrayList<Integer> genres){
        rv_similar = view.findViewById(R.id.rv_movie_details_similar);
        rv_credit = view.findViewById(R.id.rv_movie_details_casts);
        rv_company = view.findViewById(R.id.rv_movie_details_companies);

        lbl_similar = view.findViewById(R.id.lbl_movie_details_similar);
        lbl_casts = view.findViewById(R.id.lbl_movie_details_casts);
        lbl_companies = view.findViewById(R.id.lbl_movie_details_companies);

        backdrop = view.findViewById(R.id.image_backdrop);
        poster = view.findViewById(R.id.lbl_movie_details_poster);
        title = view.findViewById(R.id.lbl_movie_details_title);
        overview = view.findViewById(R.id.lbl_movie_details_overview);
        score = view.findViewById(R.id.lbl_movie_details_score);
        popularity = view.findViewById(R.id.lbl_movie_details_popularity);
        release_date = view.findViewById(R.id.lbl_movie_details_release_date);
        genre = view.findViewById(R.id.lbl_movie_details_genres);

        pbar = view.findViewById(R.id.pbar_movie_details);

        viewModel = new ViewModelProvider(MovieDetailsFragment.this).get(MovieViewModel.class);

        rv_similar.setVisibility(View.GONE);
        rv_credit.setVisibility(View.GONE);
        rv_company.setVisibility(View.GONE);

        lbl_similar.setVisibility(View.GONE);
        lbl_casts.setVisibility(View.GONE);
        lbl_companies.setVisibility(View.GONE);

        backdrop.setVisibility(View.GONE);
        poster.setVisibility(View.GONE);
        title.setVisibility(View.GONE);
        overview.setVisibility(View.GONE);
        score.setVisibility(View.GONE);
        popularity.setVisibility(View.GONE);
        release_date.setVisibility(View.GONE);
        genre.setVisibility(View.GONE);

        title.setText(movie.getTitle());
        if(!movie.getOverview().isEmpty()) {
            overview.setText(movie.getOverview());
            overview.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        } else {
            overview.setText("No Overview yet!");
        }
        String score_string = "Score: " + movie.getVote_average() + "/10.0" + " (" + movie.getVote_count() + ")";
        score.setText(score_string);
        String popularity_Sting = "Popularity: " + movie.getPopularity();
        popularity.setText(popularity_Sting);
        release_date.setText(String.format("%4.4s", movie.getRelease_date()));
        Glide.with(getContext())
                .load(Const.IMG_URL + movie.getBackdrop_path())
                .apply(bitmapTransform(new BlurTransformation(22)))
                .into(backdrop);
        Glide.with(getContext()).load(Const.IMG_URL + movie.getPoster_path()).into(poster);

        viewModel.getGenres(genres);
        viewModel.getResultGenres().observe(getViewLifecycleOwner(), new Observer<List<Genre.Genres>>() {
            @Override
            public void onChanged(List<Genre.Genres> genres) {
                StringBuilder genreToSet = new StringBuilder();
                for (int i = 0; i < genres.size(); i++) {
                    if (i != genres.size() - 1) {
                        genreToSet.append(genres.get(i).getName() + ", ");
                    } else {
                        genreToSet.append(genres.get(i).getName());
                    }
                }

                genre.setText("Genres: " + genreToSet);
            }
        });

        viewModel.getSimilar(movie.getId());
        viewModel.getResultSimilar().observe(getViewLifecycleOwner(), new Observer<Similar>() {
            @Override
            public void onChanged(Similar similar) {
                rv_similar.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                SimilarAdapter adapter = new SimilarAdapter(getActivity());
                adapter.setListSimilar(similar.getResults());
                rv_similar.setAdapter(adapter);
            }
        });

        viewModel.getCredit(movie.getId());
        viewModel.getResultCredit().observe(getViewLifecycleOwner(), new Observer<Credit>() {
            @Override
            public void onChanged(Credit credit) {
                rv_credit.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                CreditAdapter adapter = new CreditAdapter(getActivity());
                adapter.setListCreditCast(credit.getCast());
                rv_credit.setAdapter(adapter);
            }
        });

        viewModel.getMovie(movie.getId());
        viewModel.getResultMovie().observe(getViewLifecycleOwner(), new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                rv_company.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                CompanyAdapter adapter = new CompanyAdapter(getActivity());
                adapter.setListCompanies(movie.getProduction_companies());
                rv_company.setAdapter(adapter);
            }
        });

        backdrop.setVisibility(View.VISIBLE);
        poster.setVisibility(View.VISIBLE);
        title.setVisibility(View.VISIBLE);
        overview.setVisibility(View.VISIBLE);
        score.setVisibility(View.VISIBLE);
        popularity.setVisibility(View.VISIBLE);
        release_date.setVisibility(View.VISIBLE);
        genre.setVisibility(View.VISIBLE);

        lbl_similar.setVisibility(View.VISIBLE);
        lbl_casts.setVisibility(View.VISIBLE);
        lbl_companies.setVisibility(View.VISIBLE);

        rv_similar.setVisibility(View.VISIBLE);
        rv_credit.setVisibility(View.VISIBLE);
        rv_company.setVisibility(View.VISIBLE);

        pbar.setVisibility(View.GONE);
    }

}