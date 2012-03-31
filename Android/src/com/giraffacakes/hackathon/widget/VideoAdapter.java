package com.giraffacakes.hackathon.widget;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.giraffacakes.hackathon.R;
import com.giraffacakes.hackathon.data.Video;
import com.giraffacakes.hackathon.util.AsyncBitmapLoader;

public class VideoAdapter extends BaseAdapter {
	private List<Video> videos;
	private Context context;
	
	public VideoAdapter(Context context, List<Video> videos) {
		this.videos = videos;
		this.context = context;
	}
	
	public List<Video> getVideos() {
		return videos;
	}
	
	public void setVideos(List<Video> videos) {
		this.videos = videos;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return videos.size();
	}

	@Override
	public Video getItem(int position) {
		return videos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.videothumbnail, null);
			
			holder = new ViewHolder();
			holder.thumbnail = (ImageView) convertView.findViewById(R.id.video_thumbnail);
			holder.progress = (ProgressBar) convertView.findViewById(R.id.video_progress);
			holder.playIcon = (ImageView) convertView.findViewById(R.id.video_play);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String thumbnailURL = getItem(position).getThumb();
		
		AsyncBitmapLoader abl = new AsyncBitmapLoader(holder.thumbnail, holder.playIcon, holder.progress, thumbnailURL, null);
		holder.thumbnail.setTag(thumbnailURL);
		abl.execute(thumbnailURL);
		
		return convertView;
	}

	static class ViewHolder {
		ImageView thumbnail;
		ImageView playIcon;
		ProgressBar progress;
	}
}

