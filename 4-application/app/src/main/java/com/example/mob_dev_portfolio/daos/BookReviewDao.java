package com.example.mob_dev_portfolio.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mob_dev_portfolio.databases.BookReview;

import java.util.List;

@Dao
public interface BookReviewDao {

    // adds a book review to the database
    @Insert
    void insertAll(BookReview... bookReviews);

    // removes a book review from the database
    @Delete
    void delete(BookReview bookReview);

    // gets all reviews in the database
    @Query("SELECT * FROM bookreview")
    List<BookReview> getAllReviews();

    // gets book reviews for specific book
    @Query("SELECT * FROM bookreview WHERE `review_id` = :id LIMIT 1")
    BookReview getbyBookName(int id);

    @Query("DELETE FROM bookreview")
    void deleteAll();

    @Query("SELECT * FROM bookreview WHERE `like` = :val")
    List<BookReview> getLikedBooks(Boolean val);
}
