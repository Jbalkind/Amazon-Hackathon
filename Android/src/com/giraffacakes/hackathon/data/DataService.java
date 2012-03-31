package com.giraffacakes.hackathon.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.Gson;

public class DataService {
	private String serviceLocation;
	
	private String testTweet1 =
			"{'title':'Test Tweet 1', 'content':'Blah blah blah iPhone 5', 'url':'http://twitter.com/', 'date':'123454321'}";
	private String testTweet2 =
			"{'title':'Test Tweet 2', 'content':'lololol iPhone 5 wut', 'url':'http://twitter.com/', 'date':'123456789'}";
	private String testTweet3 =
			"{'title':'Test Tweet 3', 'content':'My iPhone 5 herp derp', 'url':'http://twitter.com/', 'date':'123456789'}";
	
	private String testVideo1 =
			"{'thumbnail':'http://i.ytimg.com/vi/IytNBm8WA1c/3.jpg', 'url':'http://www.youtube.com/watch?v=1234567'}";
	private String testVideo2 =
			"{'thumbnail':'http://i.ytimg.com/vi/IytNBm8WA1c/1.jpg', 'url':'http://www.youtube.com/watch?v=1234567'}";
	private String testVideo3 =
			"{'thumbnail':'http://i.ytimg.com/vi/IytNBm8WA1c/2.jpg', 'url':'http://www.youtube.com/watch?v=1234567'}";
	
	private String testData =
			String.format("{'name':'iPhone 5', 'sentimentOverall':[3,5,8], 'sentimentTwitter':[2,1,4], 'sentimentBlogger':[8,5,7], 'keywords':[], 'tweets':[%s, %s, %s, %s, %s, %s, %s, %s, %s], 'videos':[%s, %s, %s]}", testTweet1, testTweet2, testTweet3, testTweet1, testTweet2, testTweet3, testTweet1, testTweet2, testTweet3, testVideo1, testVideo2, testVideo3);
	
	public DataService(String serviceLocation) {
		this.serviceLocation = serviceLocation;
	}
	
	public String getServiceLocation() {
		return serviceLocation;
	}

	public void setServiceLocation(String serviceLocation) {
		this.serviceLocation = serviceLocation;
	}

	public Result getResultData(String type, String query){
		Result result = null;
		
		Log.i("GiraffaCakes", String.format("Querying %s/%s/%s", serviceLocation, type, query));
		
        try {
	        Gson gson = new Gson();
	        Reader reader = new InputStreamReader(downloadData(String.format("%s/%s/%s",
	        		serviceLocation, type, query)));
	        
	        if (type.equals("twitter")) {
	        	result = gson.fromJson(reader, TweetResult.class);
	        } else if (type.equals("blogger")) {
	        	result = gson.fromJson(reader, BlogResult.class);
	        } else if (type.equals("youtube")) {
	        	result = gson.fromJson(reader, YoutubeResult.class);
	        } else if (type.equals("amazon")) {
	        	result = gson.fromJson(reader, AmazonResult.class);
	        }
	        
	        Log.i("GiraffaCakes", result.toString());
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
        
        return result;
    }
	
	private InputStream downloadData(String url){
		if (url.equals("TEST")) {
			Log.v("GiraffaCakes", testData);
			return new ByteArrayInputStream(testData.getBytes());
		}
		
        DefaultHttpClient httpClient = new DefaultHttpClient();
        URI uri;
        InputStream data = null;
        
        try {
            uri = new URI(url);
            HttpGet method = new HttpGet(uri);
            HttpResponse response = httpClient.execute(method);
            data = response.getEntity().getContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return data;
    }
}
