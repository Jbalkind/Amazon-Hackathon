package com.giraffacakes.hackathon.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class AsyncBitmapLoader extends AsyncTask<String, Integer, Bitmap> {
	private ImageView imageView;
	private ImageView overlayIcon;
	private ProgressBar progressBar;
	private Map<String, Bitmap> cache;
	private String key;
	
	public AsyncBitmapLoader(ImageView imageView, ImageView overlayIcon, ProgressBar progressBar, String key, Map<String, Bitmap> cache) {
		this.imageView = imageView;
		this.progressBar = progressBar;
		this.overlayIcon = overlayIcon;
		this.cache = cache;
		this.key = key;
	}
	
	@Override
	protected void onPreExecute() {
		imageView.setVisibility(View.INVISIBLE);
		if (overlayIcon != null)
			overlayIcon.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.VISIBLE);
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		if (params.length == 0)
			return null;
		
		String url = params[0];
		
		return loadBitmap(url);
	}

	public static Bitmap loadBitmap(String url) {
		URL newurl;
		
		try {
			newurl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		
		Bitmap result = null;
		
		try {
			result = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	protected void onPostExecute(Bitmap image) {
		if (image == null) {
			return;
		}
		
		/*
		 * It may be the case that another image is now wanting
		 * to use this image view. If the ids don't match, just
		 * discard the results of this task.
		 */
		if (!key.equals((String) imageView.getTag())) {
			System.err.println("KEYS DONT MATCH");
			
			return;
		}
		
		if (cache != null) {
			cache.put(key, image);
		}
		
		if (imageView != null && imageView.isEnabled()) {
			progressBar.setVisibility(View.INVISIBLE);
			imageView.setImageBitmap(image);
			imageView.setVisibility(View.VISIBLE);
			if (overlayIcon != null)
				overlayIcon.setVisibility(View.VISIBLE);
		} else {
			System.err.println("DISABLED");
		}
	}
}
