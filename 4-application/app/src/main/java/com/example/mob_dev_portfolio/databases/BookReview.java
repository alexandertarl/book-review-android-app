package com.example.mob_dev_portfolio.databases;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mob_dev_portfolio.daos.BookReviewDao;

@Entity
public class BookReview {

    // columns in database
    @PrimaryKey(autoGenerate = true)
//    @PrimaryKey
    @ColumnInfo(name = "review_id")
    private int reviewId;

    @ColumnInfo(name = "book_name")
    private String bookName;

    @ColumnInfo(name = "book_author")
    private String bookAuthor;

    @ColumnInfo(name = "score_rating")
    private int scoreRating;

    @ColumnInfo(name = "like")
    private boolean like;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "comment")
    private String comment;

    @Override
    public String toString() {
        return this.bookName;
    }

    // getters and setters as class variables are private
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public int getScoreRating() {
        return scoreRating;
    }

    public void setScoreRating(int scoreRating) {
        this.scoreRating = scoreRating;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
