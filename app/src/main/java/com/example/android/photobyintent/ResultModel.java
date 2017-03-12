package com.example.android.photobyintent;

/**
 * Created by DSalva on 12/03/2017.
 */

public class ResultModel {
    private String url;
    private String model;
    private int price;
    private int ratings;
    private int sentymental_analysis;

    public ResultModel(String url, String model, int price, int ratings, int sentymental_analysis) {
        this.url = url;
        this.model = model;
        this.price = price;
        this.ratings = ratings;
        this.sentymental_analysis = sentymental_analysis;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRatings() {
        return ratings;
    }

    public void setRatings(int ratings) {
        this.ratings = ratings;
    }

    public int getSentymental_analysis() {
        return sentymental_analysis;
    }

    public void setSentymental_analysis(int sentymental_analysis) {
        this.sentymental_analysis = sentymental_analysis;
    }
}
