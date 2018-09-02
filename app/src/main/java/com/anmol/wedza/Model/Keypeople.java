package com.anmol.wedza.Model;

/**
 * Created by anmol on 1/8/2018.
 */

public class Keypeople {
    String name,img,contact,work;

    public Keypeople() {
    }

    public Keypeople(String name, String img, String contact, String work) {
        this.name = name;
        this.img = img;
        this.contact = contact;
        this.work = work;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }
}
