package com.uc.aflmoviedb.view.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.uc.aflmoviedb.R;
import com.uc.aflmoviedb.adapter.NowPlayingAdapter;
import com.uc.aflmoviedb.model.NowPlaying;
import com.uc.aflmoviedb.viewmodel.MovieViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NowPlayingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NowPlayingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NowPlayingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NowPlayingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NowPlayingFragment newInstance(String param1, String param2) {
        NowPlayingFragment fragment = new NowPlayingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private RecyclerView rv_now_playing;
    private MovieViewModel viewModel;
    private boolean isLoading;
    private int page = 1;
    private NowPlayingAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);

        rv_now_playing = view.findViewById(R.id.rv_now_playing_fragment);
        adapter = new NowPlayingAdapter(getActivity());
        rv_now_playing.setAdapter(adapter);
        viewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);
        page = 1;
        viewModel.getNowPlaying(page);
        viewModel.getResultNowPlaying().observe(getActivity(), showNowPlaying);

        rv_now_playing.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isLoading = false;
                if (dy > 0) {
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rv_now_playing.getLayoutManager();
                    if (!isLoading) {
                        if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == adapter.getListNowPlaying().size() - 1) {
                            page++;
                            viewModel.getNowPlaying(page);
                            viewModel.getResultNowPlaying().observe(getActivity(), new Observer<NowPlaying>() {
                                @Override
                                public void onChanged(NowPlaying nowPlaying) {
                                    adapter.getListNowPlaying().remove(adapter.getListNowPlaying().size() - 1);
                                    for (int i = 0; i < nowPlaying.getResults().size(); i++) {
                                        adapter.getListNowPlaying().add(nowPlaying.getResults().get(i));
                                    }
                                    adapter.getListNowPlaying().add(null);
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

    private Observer<NowPlaying> showNowPlaying = new Observer<NowPlaying>() {
        @Override
        public void onChanged(NowPlaying nowPlaying) {
            rv_now_playing.setLayoutManager(new LinearLayoutManager(getActivity()));
            if (adapter.getListNowPlaying().size() == 0) {
                adapter.setListNowPlaying(nowPlaying.getResults());
            }
            adapter.getListNowPlaying().add(null);
        }
    };
}