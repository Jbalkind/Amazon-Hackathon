package com.giraffacakes.hackathon.data;

import java.util.List;

public class TweetResult extends Result {
	private List<Tweet> docs;
	private List<Integer> sentiments;
	
	public List<Tweet> getDocs() {
		return docs;
	}
	
	public void setDocs(List<Tweet> docs) {
		this.docs = docs;
	}
	
	public List<Integer> getSentiments() {
		return sentiments;
	}
	
	public void setSentiments(List<Integer> sentiments) {
		this.sentiments = sentiments;
	}
	
	public String toString() {
		return String.format("[%d, %d, %d]", sentiments.get(0), sentiments.get(1), sentiments.get(2));
	}
}
