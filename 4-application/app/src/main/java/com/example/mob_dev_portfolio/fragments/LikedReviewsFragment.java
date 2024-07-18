package com.example.mob_dev_portfolio.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.activities.BookReviewsActivity;
import com.example.mob_dev_portfolio.daos.BookReviewDao;
import com.example.mob_dev_portfolio.databases.BookReview;

import java.util.List;
import java.util.concurrent.Executors;

public class LikedReviewsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public LikedReviewsFragment() {
        // Required empty public constructor
    }

    public static LikedReviewsFragment newInstance(String param1, String param2) {
        LikedReviewsFragment fragment = new LikedReviewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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

        View view = inflater.inflate(R.layout.fragment_liked_reviews, container, false);

        // sets listview for liked books
        BookReviewDao dao = BookReviewsActivity.db.bookReviewDao();
        BookReviewsActivity.executor = Executors.newFixedThreadPool(4);
        BookReviewsActivity.executor.execute(new Runnable() {
            @Override
            public void run() {
                List<BookReview> reviews = dao.getLikedBooks(true);

                ArrayAdapter<BookReview> adapter = new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        reviews);

                ListView lv = (ListView) view.findViewById(R.id.list_view_liked_reviews);
                lv.setAdapter(adapter);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}