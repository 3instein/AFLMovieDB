package com.uc.aflmoviedb.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputLayout;
import com.uc.aflmoviedb.R;
import com.uc.aflmoviedb.adapter.SearchAdapter;
import com.uc.aflmoviedb.model.Search;
import com.uc.aflmoviedb.viewmodel.MovieViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieSearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MovieSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieSearchFragment newInstance(String param1, String param2) {
        MovieSearchFragment fragment = new MovieSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private TextInputLayout search;
    private MovieViewModel viewModel;
    private RecyclerView rv_result;
    private ProgressBar pbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_movie_search, container, false);

        pbar = view.findViewById(R.id.pbar_movie_search);
        pbar.setVisibility(View.GONE);
        rv_result = view.findViewById(R.id.rv_movie_search);
        search = view.findViewById(R.id.movieSearch);
        viewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);

        search.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pbar.setVisibility(View.VISIBLE);
                String query = search.getEditText().getText().toString().trim();
                if(query.isEmpty()){
                    query = "&nbsp";
                }
                viewModel.getSearch(query);
                viewModel.getResultSearch().observe(getActivity(), new Observer<Search>() {
                    @Override
                    public void onChanged(Search search) {
                        rv_result.setLayoutManager(new LinearLayoutManager(getActivity()));
                        SearchAdapter adapter = new SearchAdapter(getActivity());
                        adapter.setListSearch(search.getResults());
                        rv_result.setAdapter(adapter);
                    }
                });
                pbar.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }
}