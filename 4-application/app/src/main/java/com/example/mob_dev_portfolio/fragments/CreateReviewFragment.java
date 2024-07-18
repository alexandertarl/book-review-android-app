package com.example.mob_dev_portfolio.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.activities.BookReviewsActivity;
import com.example.mob_dev_portfolio.daos.BookReviewDao;
import com.example.mob_dev_portfolio.databases.BookReview;

import java.util.Calendar;
import java.util.concurrent.Executors;

public class CreateReviewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private EditText bookNameEdt;
    private EditText bookAuthorEdt;
    private EditText scoreEdt;
    private EditText likeEdt;
    private ImageButton likeButton;
    private EditText dateEdt;
    private EditText commentEdt;
    private Button submit;

    private boolean likeValue = false;

    public CreateReviewFragment() {
        // Required empty public constructor
    }

    public static CreateReviewFragment newInstance(String param1, String param2) {
        CreateReviewFragment fragment = new CreateReviewFragment();
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
        View view = inflater.inflate(R.layout.fragment_create_review, container, false);

        // fragment logic here
        BookReviewDao dao = BookReviewsActivity.db.bookReviewDao();
        // initialise variables for inputting
        bookNameEdt = view.findViewById(R.id.edtBookName);
        bookAuthorEdt = view.findViewById(R.id.edtBookAuthor);
        scoreEdt = view.findViewById(R.id.edtScoreRating);

        likeButton = view.findViewById(R.id.likeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                likeButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_favorite_24));
                likeValue = true;
            }
        });

        dateEdt = view.findViewById(R.id.edtDate);
        commentEdt = view.findViewById(R.id.edtComment);

        // code to have a popup date picker on an edittext
        // taken from GeeksForGeeks, by chaitanyamunje last updated 20-07-2022
        // accessed 11-05-2023
        // https://www.geeksforgeeks.org/how-to-popup-datepicker-while-clicking-on-edittext-in-android/
        dateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // instance of calendar
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // on below line we are setting date to our edit text.
                                dateEdt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // display our date picker dialog.
                datePickerDialog.show();
            }
        });
        // end of referenced code

        submit = view.findViewById(R.id.submitButton);
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                try {
                    // string values inputted
                    String bookNameValue = bookNameEdt.getText().toString();

                    // makes sure book name must be inputted
                    if (bookNameValue.isEmpty()) {
                        throw new NullPointerException();
                    }
                    String bookAuthorValue = bookAuthorEdt.getText().toString();
                    int scoreValue = Integer.parseInt(scoreEdt.getText().toString());
                    String dateValue = dateEdt.getText().toString();
                    String commentValue = commentEdt.getText().toString();

                    BookReviewsActivity.executor = Executors.newFixedThreadPool(4);
                    BookReviewsActivity.executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            // create object to input into database
                            BookReview br = new BookReview();

                            br.setReviewId(0);
                            br.setBookName(bookNameValue);
                            br.setBookAuthor(bookAuthorValue);

                            // validate value being 0-10
                            if (scoreValue >= 0 && scoreValue <= 10) {
                                br.setScoreRating(scoreValue);
                            } else {
                                // if not value in range, go to exception and don't submit
                                return;
                            }
                            br.setLike(likeValue);
                            br.setDate(dateValue);
                            br.setComment(commentValue);
                            dao.insertAll(br);

                        }
                    });
                    if (scoreValue >= 0 && scoreValue <= 10) {
                        Toast.makeText(getContext(), "Review Submitted", Toast.LENGTH_LONG).show();
                    } else {
                        // if not value in range, go to exception and dont submit
                        Toast.makeText(getContext(), "Error Submitting Review, Invalid Score", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error Submitting Review", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}