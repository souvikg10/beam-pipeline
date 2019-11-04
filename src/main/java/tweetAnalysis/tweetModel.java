package tweetAnalysis;

import java.io.Serializable;

import org.apache.beam.vendor.grpc.v1p21p0.com.google.gson.annotations.SerializedName;

public class tweetModel implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SerializedName("year")
    private int year;
    @SerializedName("tweet")
    private String tweet;

    public tweetModel(int year, String tweet) {
        this.setYear(year);
        this.setTweet(tweet);
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    
}