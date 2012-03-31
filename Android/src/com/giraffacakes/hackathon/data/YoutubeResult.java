package com.giraffacakes.hackathon.data;

import java.util.List;

public class YoutubeResult extends Result {
	private List<Video> videos;

	public List<Video> getVideos() {
		return videos;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}
	
	public String toString() {
		return String.format("%d videos", videos.size());
	}
}
