package com.giraffacakes.hackathon.data;

public class Video {
	private String thumb;
	private String url;

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String toString() {
		return String.format("%s", url);
	}
}
