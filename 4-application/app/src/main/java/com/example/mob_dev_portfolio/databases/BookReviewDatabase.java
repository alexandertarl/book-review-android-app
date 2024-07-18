package com.example.mob_dev_portfolio.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mob_dev_portfolio.daos.BookReviewDao;

@Database(entities = {BookReview.class}, version = 1)
public abstract class BookReviewDatabase extends RoomDatabase {

    public abstract BookReviewDao bookReviewDao();
}
