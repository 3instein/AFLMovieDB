package com.uc.aflmoviedb.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.uc.aflmoviedb.R;
import com.uc.aflmoviedb.adapter.PopularAdapter;
import com.uc.aflmoviedb.model.Popular;
import com.uc.aflmoviedb.viewmodel.MovieViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PopularFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PopularFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Popular.
     */
    // TODO: Rename and change types and number of parameters
    public static PopularFragment newInstance(String param1, String param2) {
        PopularFragment fragment = new PopularFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PopularFragment() {
        // Required empty public constructor
    }

    private RecyclerView rv_popular;
    private MovieViewModel viewModel;
    private boolean isLoading;
    private int page = 1;
    private PopularAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_popular, container, false);

        rv_popular = view.findViewById(R.id.rv_popular_fragment);
        adapter = new PopularAdapter(getActivity());
        rv_popular.setAdapter(adapter);
        viewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);
        page = 1;
        viewModel.getPopular(page);
        viewModel.getResultPopular().observe(getActivity(), showPopular);

        rv_popular.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isLoading = false;
                if (dy > 0) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv_popular.getLayoutManager();
                    if (!isLoading) {
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.getListPopular().size() - 1) {
                            page++;
                            viewModel.getPopular(page);
                            viewModel.getResultPopular().observe(getActivity(), new Observer<Popular>() {
                                @Override
                                public void onChanged(Popular popular) {
                                    adapter.getListPopular().remove(adapter.getListPopular().size() - 1);
                                    for (int i = 0; i < popular.getResults().size(); i++) {
                                        adapter.getListPopular().add(popular.getResults().get(i));
                                    }
                                    adapter.getListPopular().add(null);
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }
            }
        });

        return view;
    }

    private Observer<Popular> showPopular = new Observer<Popular>() {
        @Override
        public void onChanged(Popular popular) {
            rv_popular.setLayoutManager(new LinearLayoutManager(getActivity()));
            if (adapter.getListPopular().size() == 0) {
                adapter.setListPopular(popular.getResults());
            }
            adapter.getListPopular().add(null);
        }
    };
}