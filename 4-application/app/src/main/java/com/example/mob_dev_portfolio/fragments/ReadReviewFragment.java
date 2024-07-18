package com.example.mob_dev_portfolio.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.activities.BookReviewsActivity;
import com.example.mob_dev_portfolio.daos.BookReviewDao;
import com.example.mob_dev_portfolio.databases.BookReview;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;


public class ReadReviewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static int reviewId;

    public ReadReviewFragment(int value) {
        // Required empty public constructor
        reviewId = value;
    }

    public static ReadReviewFragment newInstance(String param1, String param2) {
        ReadReviewFragment fragment = new ReadReviewFragment(reviewId);
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

        View view = inflater.inflate(R.layout.fragment_read_review, container, false);

        // get row in database
        BookReviewDao dao = BookReviewsActivity.db.bookReviewDao();
        BookReviewsActivity.executor = Executors.newFixedThreadPool(4);
        BookReviewsActivity.executor.execute(new Runnable() {
            @Override
            public void run() {

                BookReview reviews = dao.getbyBookName(reviewId+1);

                TextView name = (TextView) view.findViewById(R.id.textName);
                name.append(" "+reviews.getBookName());

                TextView author = (TextView) view.findViewById(R.id.authorName);
                author.append(" "+reviews.getBookAuthor());

                TextView rating = (TextView) view.findViewById(R.id.textRating);
                String ratingstr = Integer.toString(reviews.getScoreRating());
                rating.append(" "+ratingstr + "/10");

                // gets value of if book is liked (true), or not (false)
                TextView ifLike = (TextView) view.findViewById(R.id.textLike);
                // if isliked is true
                if (reviews.isLike()) {
                    ifLike.append("You added this book to your liked list!");
                } else {
                    ifLike.append("This book is not on your liked list.");
                }

                TextView date = (TextView) view.findViewById(R.id.textDate);
                date.append(" "+reviews.getDate());

                TextView comment = (TextView) view.findViewById(R.id.textWrittenComment);
                comment.append(reviews.getComment());
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}