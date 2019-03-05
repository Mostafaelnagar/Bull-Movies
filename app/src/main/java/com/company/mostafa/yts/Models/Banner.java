package com.company.mostafa.yts.Models;

public class Banner {
    private String bn_Name;
    private String bn_img;
    private String id;

    public Banner() {
    }

    public Banner(String bn_Name, String bn_img, String id) {
        this.bn_Name = bn_Name;
        this.bn_img = bn_img;
        this.id = id;
    }

    public String getBn_Name() {
        return bn_Name;
    }

    public void setBn_Name(String bn_Name) {
        this.bn_Name = bn_Name;
    }

    public String getBn_img() {
        return bn_img;
    }

    public void setBn_img(String bn_img) {
        this.bn_img = bn_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
