package com.anmol.wedza.Model;

/**
 * Created by anmol on 1/7/2018.
 */

public class Comment2 {
    String comment,commentedby,profilepic,uid;

    public Comment2() {
    }

    public Comment2(String comment, String commentedby, String profilepic, String uid) {
        this.comment = comment;
        this.commentedby = commentedby;
        this.profilepic = profilepic;
        this.uid = uid;
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
}
