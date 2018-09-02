package com.anmol.wedza.Model;


import java.sql.Timestamp;

/**
 * Created by anmol on 1/7/2018.
 */

public class Comment {
    String comment,commentedby,profilepic,uid;
    Timestamp time;

    public Comment(String comment, String commentedby, String profilepic, String uid, Timestamp time) {
        this.comment = comment;
        this.commentedby = commentedby;
        this.profilepic = profilepic;
        this.uid = uid;
        this.time = time;
    }

    public Comment() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentedby() {
        return commentedby;
    }

    public void setCommentedby(String commentedby) {
        this.commentedby = commentedby;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}
