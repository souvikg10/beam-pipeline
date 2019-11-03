package tweetAnalysis;

import java.io.Serializable;
import java.util.Date;

import org.apache.beam.vendor.grpc.v1p21p0.com.google.gson.annotations.SerializedName;

public class tweetModel implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @SerializedName("date")
    private Date date;
    @SerializedName("tweet")
    private String tweet;

    public tweetModel(Date date, String tweet) {
        this.setDate(date);
        this.setTweet(tweet);
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    
}