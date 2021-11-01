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
import com.uc.aflmoviedb.adapter.UpcomingAdapter;
import com.uc.aflmoviedb.model.Upcoming;
import com.uc.aflmoviedb.viewmodel.MovieViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpcomingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpcomingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingFragment newInstance(String param1, String param2) {
        UpcomingFragment fragment = new UpcomingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView rv_upcoming;
    private MovieViewModel viewModel;
    private boolean isLoading;
    private int page = 1;
    private UpcomingAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);

        rv_upcoming = view.findViewById(R.id.rv_upcoming_fragment);
        adapter = new UpcomingAdapter(getActivity());
        rv_upcoming.setAdapter(adapter);
        viewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);
        page = 1;
        viewModel.getUpcoming(page);
        viewModel.getResultUpcoming().observe(getActivity(), showUpcoming);

        rv_upcoming.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isLoading = false;
                if (dy > 0) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv_upcoming.getLayoutManager();
                    if (!isLoading) {
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.getListUpcoming().size() - 1) {
                            page++;
                            viewModel.getUpcoming(page);
                            viewModel.getResultUpcoming().observe(getActivity(), new Observer<Upcoming>() {
                                @Override
                                public void onChanged(Upcoming upcoming) {
                                    adapter.getListUpcoming().remove(adapter.getListUpcoming().size() - 1);
                                    for (int i = 0; i < upcoming.getResults().size(); i++) {
                                        adapter.getListUpcoming().add(upcoming.getResults().get(i));
                                    }
                                    adapter.getListUpcoming().add(null);
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

    private Observer<Upcoming> showUpcoming = new Observer<Upcoming>() {
        @Override
        public void onChanged(Upcoming upcoming) {
            rv_upcoming.setLayoutManager(new LinearLayoutManager(getActivity()));
            if (adapter.getListUpcoming().size() == 0) {
                adapter.setListUpcoming(upcoming.getResults());
            }
            adapter.getListUpcoming().add(null);
        }
    };

}