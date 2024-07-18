package com.example.mob_dev_portfolio.fragments;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.activities.BookReviewsActivity;
import com.example.mob_dev_portfolio.activities.BookSearchActivity;
import com.example.mob_dev_portfolio.daos.BookReviewDao;
import com.example.mob_dev_portfolio.databases.BookReview;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ReviewListFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String EXAMPLE_CHANNEL_ID = "type1";
    private String notifText = "Books you saved to read soon: ";
    private List<String> tbrList = new ArrayList<>();

    private SharedPreferences toBeReadBooksSP;

    private String mParam1;
    private String mParam2;

    public ReviewListFragment() {
        // Required empty public constructor
    }

    public static ReviewListFragment newInstance(String param1, String param2) {
        ReviewListFragment fragment = new ReviewListFragment();
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

        View view = inflater.inflate(R.layout.fragment_review_list, container, false);

        BookReviewDao dao = BookReviewsActivity.db.bookReviewDao();
        BookReviewsActivity.executor = Executors.newFixedThreadPool(4);
        BookReviewsActivity.executor.execute(new Runnable() {
            @Override
            public void run() {

                List<BookReview> reviews = dao.getAllReviews();

                ArrayAdapter<BookReview> adapter = new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        reviews);

                ListView lv = (ListView) view.findViewById(R.id.list_view_book_reviews);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv.setAdapter(adapter);
                    }
                });

                // on click listener for when review is clicked
                // takes the user to a new fragment with details of the review
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Fragment f = new ReadReviewFragment(i);
                        getParentFragmentManager().beginTransaction().replace(R.id.listFragment, f).addToBackStack(null).commit();
                    }
                });
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}