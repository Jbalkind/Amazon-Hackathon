package com.giraffacakes.hackathon.widget;

import java.util.List;

import com.giraffacakes.hackathon.R;
import com.giraffacakes.hackathon.data.Tweet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BlogAdapter extends BaseAdapter {
	private List<Tweet> tweets;
	private Context context;
	
	public BlogAdapter(Context context, List<Tweet> tweets) {
		this.tweets = tweets;
		this.context = context;
	}
	
	public List<Tweet> getTweets() {
		return tweets;
	}
	
	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return tweets.size();
	}

	@Override
	public Tweet getItem(int position) {
		return tweets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.blog, null);
			
			holder = new ViewHolder();
			holder.content = (TextView) convertView.findViewById(com.giraffacakes.hackathon.R.id.blog_content);
			//holder.timestamp = (TextView) convertView.findViewById(R.id.blog_timestamp);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Tweet tweet = getItem(position);
		
		holder.content.setText(tweet.getTitle());
		//holder.timestamp.setText(String.format("%d", tweet.getDate()));
		
		return convertView;
	}

	static class ViewHolder {
		TextView content;
		//TextView timestamp;
	}
}
