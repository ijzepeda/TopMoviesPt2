package com.ijzepeda.topmoviespt2;

/**
 * Created by ivan.zepeda on 14/01/2016.
 */
public class Review {
    String reviewId;
    String commentAuthor;
    String commentTxt;
    String commentURL;

    public Review() {
    }

    public Review(String reviewId, String commentAuthor, String commentTxt, String commentURL) {
        this.reviewId = reviewId;
        this.commentAuthor = commentAuthor;
        this.commentTxt = commentTxt;
        this.commentURL = commentURL;
    }

    public Review(String commentAuthor, String commentTxt, String commentDate) {
        this.commentAuthor = commentAuthor;
        this.commentTxt = commentTxt;
        this.commentURL = commentDate;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public void setCommentAuthor(String commentAuthor) {
        this.commentAuthor = commentAuthor;
    }

    public String getCommentTxt() {
        return commentTxt;
    }

    public void setCommentTxt(String commentTxt) {
        this.commentTxt = commentTxt;
    }

    public String getCommentURL() {
        return commentURL;
    }

    public void setCommentURL(String commentURL) {
        this.commentURL = commentURL;
    }


    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }
}
