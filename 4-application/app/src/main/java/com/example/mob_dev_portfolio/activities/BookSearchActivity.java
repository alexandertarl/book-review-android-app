package com.example.mob_dev_portfolio.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.daos.BookReviewDao;
import com.example.mob_dev_portfolio.databases.BookReviewDatabase;
import com.example.mob_dev_portfolio.databinding.ActivityBookSearchBinding;
import com.example.mob_dev_portfolio.fragments.BookSearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookSearchActivity extends AppCompatActivity {

    ActivityBookSearchBinding activityBookSearchBinding;
    String searchQuery;
    Boolean performGet = false;
    ImageView likeClick;
    ImageView searchClick;
    BottomNavigationView bottomNavigationView;
    ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBookSearchBinding = ActivityBookSearchBinding.inflate(getLayoutInflater());
        setContentView(activityBookSearchBinding.getRoot());

        // enable toolbar
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);

        // when clicking search button
        searchClick = (ImageView) findViewById(R.id.searchImage);
        searchClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // go to book search fragment
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.searchFragment, new BookSearchFragment());
                ft.commit();
            }
        });

        // initialise database
        BookReviewDatabase db = Room.databaseBuilder(
                        getApplicationContext(),
                        BookReviewDatabase.class,
                        "book_reviews")
                        .build();

        this.executor = Executors.newFixedThreadPool(4);

        BookReviewDao dao = db.bookReviewDao();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.newReview) {
                    // go to book reviews activity
                    Intent intent = new Intent(
                            getApplicationContext(),
                            BookReviewsActivity.class);

                    // code to pass a value from one activity to another
                    // taken from Stack Overflow post by DeRagan 18-08-2010
                    // accessed 13-04-2023
                    // https://stackoverflow.com/questions/3510649/how-to-pass-a-value-from-one-activity-to-another-in-android
                    Bundle bundle = new Bundle();
                    bundle.putInt("newFragment", 2);
                    intent.putExtras(bundle);
                    // end of code - but is also used below

                    startActivity(intent);
                    return true;
                }
                if (item.getItemId() == R.id.reviews) {

                    // go to book reviews activity
                    Intent intent = new Intent(
                            getApplicationContext(),
                            BookReviewsActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putInt("newFragment", 1);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    return true;
                }
                if (item.getItemId() == R.id.like) {
                    // go to book reviews activity

                    // go to book reviews activity
                    Intent intent = new Intent(
                            getApplicationContext(),
                            BookReviewsActivity.class);


                    Bundle bundle = new Bundle();
                    bundle.putInt("newFragment", 3);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

    }
}