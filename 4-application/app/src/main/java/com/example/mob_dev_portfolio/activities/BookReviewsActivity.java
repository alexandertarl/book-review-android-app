package com.example.mob_dev_portfolio.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;

import com.example.mob_dev_portfolio.R;
import com.example.mob_dev_portfolio.databases.BookReviewDatabase;
import com.example.mob_dev_portfolio.databinding.ActivityBookReviewsBinding;
import com.example.mob_dev_portfolio.fragments.CreateReviewFragment;
import com.example.mob_dev_portfolio.fragments.LikedReviewsFragment;
import com.example.mob_dev_portfolio.fragments.ReviewListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.concurrent.ExecutorService;

public class BookReviewsActivity extends AppCompatActivity {

    private ActivityBookReviewsBinding binding;
    ImageView likeClick;
    ImageView searchClick;
    BottomNavigationView bottomNavigationView;
    public static BookReviewDatabase db;
    public static ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookReviewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int fragment = 0;

        try {
            // code to pass a value from one activity to another
            // taken from Stack Overflow post by DeRagan 18-08-2010
            // accessed 13-04-2023
            // https://stackoverflow.com/questions/3510649/how-to-pass-a-value-from-one-activity-to-another-in-android
            Bundle bundle = getIntent().getExtras();
            fragment = bundle.getInt("newFragment");
            // end of code
        } catch (Exception e) {
            // when loading
        }


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragment == 1) {
            // go to review list fragment
            ft.replace(R.id.listFragment, new ReviewListFragment());
            ft.commit();
        }
        else if (fragment == 2) {
            ft.replace(R.id.listFragment, new CreateReviewFragment());
            ft.commit();
        }
        else if (fragment == 3) {
                // go to liked review fragment
                ft.replace(R.id.listFragment, new LikedReviewsFragment());
                ft.commit();
        }
        else {
            // set fragment
            ft.replace(R.id.listFragment, new ReviewListFragment());
            ft.commit();
        }

        // enable toolbar
        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(t);

        // when clicking search button
        searchClick = (ImageView) findViewById(R.id.searchImage);
        searchClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("search button pressed");
                Intent intent = new Intent(
                        getApplicationContext(),
                        BookSearchActivity.class);
                startActivity(intent);
            }
        });

        db = Room.databaseBuilder(
                getApplicationContext(),
                BookReviewDatabase.class,
                "book_reviews")
                .build();

        // when clicking each item on bottom bar, takes user to correct fragment
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.newReview) {

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.listFragment, new CreateReviewFragment());
                    ft.commit();
                    return true;
                }
                if (item.getItemId() == R.id.reviews) {

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.listFragment, new ReviewListFragment());
                    ft.commit();

                    return true;
                }
                if (item.getItemId() == R.id.like) {

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.listFragment, new LikedReviewsFragment());
                    ft.commit();

                    return true;
                }
                return false;
            }
        });

    }
}