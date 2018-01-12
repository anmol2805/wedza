package com.anmol.wedza.Model;

/**
 * Created by anmol on 1/12/2018.
 */

public class Wish {
    String wish,wishedby,profilepic,uid;

    public Wish() {
    }

    public Wish(String wish, String wishedby, String profilepic, String uid) {
        this.wish = wish;
        this.wishedby = wishedby;
        this.profilepic = profilepic;
        this.uid = uid;
    }

    public String getWish() {
        return wish;
    }

    public void setWish(String wish) {
        this.wish = wish;
    }

    public String getWishedby() {
        return wishedby;
    }

    public void setWishedby(String wishedby) {
        this.wishedby = wishedby;
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
