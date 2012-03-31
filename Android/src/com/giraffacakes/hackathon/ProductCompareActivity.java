package com.giraffacakes.hackathon;

import java.util.ArrayList;
import java.util.List;

import com.giraffacakes.hackathon.animation.AlphaAnimation;
import com.giraffacakes.hackathon.data.AmazonResult;
import com.giraffacakes.hackathon.data.BlogResult;
import com.giraffacakes.hackathon.data.DataService;
import com.giraffacakes.hackathon.data.Product;
import com.giraffacakes.hackathon.data.Tweet;
import com.giraffacakes.hackathon.data.TweetResult;
import com.giraffacakes.hackathon.data.Video;
import com.giraffacakes.hackathon.data.YoutubeResult;
import com.giraffacakes.hackathon.util.AsyncBitmapLoader;
import com.giraffacakes.hackathon.util.DialogFactory;
import com.giraffacakes.hackathon.widget.BlogAdapter;
import com.giraffacakes.hackathon.widget.TripleBarWidget;
import com.giraffacakes.hackathon.widget.TweetAdapter;
import com.giraffacakes.hackathon.widget.VideoAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProductCompareActivity extends Activity {
	private DataService dataService;
	
	private String product1Query;
	private String product2Query;
	
	private Product selectedProduct1;
	private Product selectedProduct2;
	
	// Product 1
	private AmazonResult product1AmazonResult;
	private TweetResult product1TweetResults;
	private BlogResult product1BlogResults;
	private YoutubeResult product1YoutubeResults;
	private TextView product1Name;
	private TripleBarWidget product1SentimentTwitter;
	private TripleBarWidget product1SentimentBlogger;
	private ListView product1Tweets;
	private ListView product1Blogs;
	private GridView product1Videos;
	private Button product1Button;
	private TweetAdapter tweetAdapter1;
	private VideoAdapter videoAdapter1;
	private BlogAdapter blogAdapter1;
	private ImageView product1AmazonImage;
	private TextView product1AmazonPrice;
	private ProgressBar product1AmazonProgress;
	
	private ImageView twitterCollapse1Tab;
	private RelativeLayout twitterCollapse1;
	
	private ImageView bloggerCollapse1Tab;
	private RelativeLayout bloggerCollapse1;
	
	private ImageView youtubeCollapse1Tab;
	private RelativeLayout youtubeCollapse1;
	
	private ImageView amazonCollapse1Tab;
	private LinearLayout amazonCollapse1;
	
	// Product 2
	private AmazonResult product2AmazonResult;
	private TweetResult product2TweetResults;
	private BlogResult product2BlogResults;
	private YoutubeResult product2YoutubeResults;
	private TextView product2Name;
	private TripleBarWidget product2SentimentTwitter;
	private TripleBarWidget product2SentimentBlogger;
	private ListView product2Tweets;
	private GridView product2Videos;
	private ListView product2Blogs;
	private Button product2Button;
	private TweetAdapter tweetAdapter2;
	private VideoAdapter videoAdapter2;
	private BlogAdapter blogAdapter2;
	private ImageView product2AmazonImage;
	private TextView product2AmazonPrice;
	private ProgressBar product2AmazonProgress;
	
	private ImageView twitterCollapse2Tab;
	private RelativeLayout twitterCollapse2;
	
	private ImageView bloggerCollapse2Tab;
	private RelativeLayout bloggerCollapse2;
	
	private ImageView youtubeCollapse2Tab;
	private RelativeLayout youtubeCollapse2;
	
	private ImageView amazonCollapse2Tab;
	private LinearLayout amazonCollapse2;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productcompare);
        
        dataService = new DataService("http://ec2-46-137-54-184.eu-west-1.compute.amazonaws.com/data");
        //updateProduct();
        
        initProduct1();
        initProduct2();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	promptUser();
    	
    	
    }
    
    private void promptUser() {
    	final AlertDialog product2Dialog = DialogFactory.textEntryDialog(
        		this, "Second product", "Please enter the second product name.",
        		"", "...", new Handler() {
        			@Override
        			public void handleMessage(Message msg) {
        				String input = ((String) msg.obj).trim();
        				
        				if (input != null) {
        					product2Query = input.replace(" ", "%20");
        					product2Name.setText(input);
        				}
        				
        				makeChoices();
        			}
        		});
        	
        
    	
    	AlertDialog product1Dialog = DialogFactory.textEntryDialog(
    		this, "First product", "Please enter the first product name.",
    		"", "...", new Handler() {
    			@Override
    			public void handleMessage(Message msg) {
    				String input = ((String) msg.obj).trim();
    				
    				if (input != null) {
    					product1Query = input.replace(" ", "%20");
    					product1Name.setText(input);
    				}
    				
    				product2Dialog.show();
    			}
    		});
    	
    	product1Dialog.show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		
		return true;
    }
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_search:
			promptUser();
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
    }
    
    private void makeChoices() {
    	Log.i("GiraffaCakes", String.format("%s\n%s", product1Query, product2Query));
    	
    	GetChoicesTask gct = new GetChoicesTask();
    	gct.execute(product1Query, product2Query);
    }
    
    private class GetChoicesTask extends AsyncTask<String, Void, List[]> {
		@Override
		protected List[] doInBackground(String... args) {
			String prod1 = args[0];
			String prod2 = args[1];
			
			product1AmazonResult = (AmazonResult) dataService.getResultData("amazon", prod1);
	    	product2AmazonResult = (AmazonResult) dataService.getResultData("amazon", prod2);
	    	
	    	List[] array = new List[2];
	    	
	    	if (product1AmazonResult != null)
	    		array[0] = product1AmazonResult.getProducts();
	    	else
	    		array[0] = null;
	    	
	    	if (product2AmazonResult != null)
	    		array[1] = product2AmazonResult.getProducts();
	    	else
	    		array[1] = null;
	    	
	    	return array;
		}
		
		@Override
		protected void onPostExecute(List[] result) {
			Log.i("GiraffaCakes", "onPostExecute()");
			
			if (result[0] == null) {
	    		AlertDialog errorDialog = DialogFactory.warningDialog(ProductCompareActivity.this, "Query unsuccessful", String.format("Amazon has no matches for \"%s\".", product1Query.replace("%20", " ")));
	    		errorDialog.show();
	    		return;
	    	} else if (result[1] == null) {
	    		AlertDialog errorDialog = DialogFactory.warningDialog(ProductCompareActivity.this, "Query unsuccessful", String.format("Amazon has no matches for \"%s\".", product2Query.replace("%20", " ")));
	    		errorDialog.show();
	    		return;
	    	}
			
			final List<Product> products1 = (List<Product>) result[0];
			final List<Product> products2 = (List<Product>) result[1];
			
			List<String> prodNames1 = new ArrayList<String>();
			List<String> prodNames2 = new ArrayList<String>();
			
			for (Product p1 : products1) {
				prodNames1.add(p1.getTitle());
			}
			
			for (Product p2 : products2) {
				prodNames2.add(p2.getTitle());
			}
			
			final AlertDialog dialog2 = DialogFactory.choiceDialog(
					ProductCompareActivity.this, "Second product",
					"Please choose second product from this list of Amazon products.",
					prodNames2,
					new Handler() {
						@Override
		    			public void handleMessage(Message msg) {
		    				String input = ((String) msg.obj);
		    				
		    				for (Product p2 : products2) {
		    					if (p2.getTitle().equals(input)) {
		    						selectedProduct2 = p2;
		    						break;
		    					}
		    				}
		    				
		    				Log.w("GiraffaCakes", selectedProduct2.getTitle());
		    				
		    				ProductCompareActivity.this.updateProduct();
		    			}
					});
			
			AlertDialog dialog1 = DialogFactory.choiceDialog(
				ProductCompareActivity.this, "First product",
				"Please choose first product from this list of Amazon products.",
				prodNames1,
				new Handler() {
					@Override
	    			public void handleMessage(Message msg) {
	    				String input = ((String) msg.obj);
	    				
	    				for (Product p1 : products1) {
	    					if (p1.getTitle().equals(input)) {
	    						selectedProduct1 = p1;
	    						break;
	    					}
	    				}
	    				
	    				Log.w("GiraffaCakes", selectedProduct1.getTitle());
	    				
	    				
	    			}
				});
			
			dialog1.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface arg0) {
					dialog2.show();
				}
			});
			
			dialog1.show();
	    }
    }
    
    private void viewTweet(String url) {
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(Uri.parse(url));
    	
    	this.startActivity(intent);
    }
    
    private void viewYoutube(String url) {
    	Intent intent = new Intent(Intent.ACTION_VIEW);
    	intent.setData(Uri.parse(url));
    	
    	this.startActivity(intent);
    }
    
    private void initProduct1() {
    	View p1 = findViewById(R.id.productcompare_product1);
    	
    	product1Name = (TextView) p1.findViewById(R.id.productview1_name);
    	product1SentimentTwitter = (TripleBarWidget) p1.findViewById(R.id.productview1_sentimentTwitter);
    	product1SentimentBlogger = (TripleBarWidget) p1.findViewById(R.id.productview1_sentimentBlogger);
    	product1Tweets = (ListView) p1.findViewById(R.id.productview1_tweetlist);
    	product1Videos = (GridView) p1.findViewById(R.id.productview1_videolist);
    	product1Blogs = (ListView) p1.findViewById(R.id.productview1_bloglist);
    	product1Button = (Button) p1.findViewById(R.id.productview1_buybutton);
    	product1AmazonImage = (ImageView) p1.findViewById(R.id.amazon_image1);
    	product1AmazonPrice = (TextView) p1.findViewById(R.id.amazon_price1);
    	product1AmazonProgress = (ProgressBar) p1.findViewById(R.id.amazon1_progress);
    	
    	amazonCollapse1 = (LinearLayout) p1.findViewById(R.id.amazon1_collapser);
    	amazonCollapse1.setVisibility(View.VISIBLE);
    	amazonCollapse1Tab = (ImageView) p1.findViewById(R.id.productview1_amazontab);
    	
    	twitterCollapse1 = (RelativeLayout) p1.findViewById(R.id.twitter1_collapser);
    	twitterCollapse1.setVisibility(View.INVISIBLE);
    	twitterCollapse1.setAlpha(0f);
    	twitterCollapse1Tab = (ImageView) p1.findViewById(R.id.productview1_twittertab);
    	
    	bloggerCollapse1 = (RelativeLayout) p1.findViewById(R.id.blogger1_collapser);
    	bloggerCollapse1.setVisibility(View.INVISIBLE);
    	bloggerCollapse1.setAlpha(0f);
    	bloggerCollapse1Tab = (ImageView) p1.findViewById(R.id.productview1_bloggertab);
    	
    	youtubeCollapse1 = (RelativeLayout) p1.findViewById(R.id.youtube1_collapser);
    	youtubeCollapse1.setVisibility(View.INVISIBLE);
    	youtubeCollapse1.setAlpha(0f);
    	youtubeCollapse1Tab = (ImageView) p1.findViewById(R.id.productview1_youtubetab);
    	
    	//tweetAdapter1 = new TweetAdapter(this, product1TweetResults.getDocs());
    	List<Tweet> tweets1 = new ArrayList<Tweet>();
    	tweetAdapter1 = new TweetAdapter(this, tweets1);
    	product1Tweets.setAdapter(tweetAdapter1);
    	product1Tweets.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position, long id) {
				String url = product1TweetResults.getDocs().get(position).getUrl();
				viewTweet(url);
			}
    	});
    	
    	List<Tweet> blogs1 = new ArrayList<Tweet>();
    	blogAdapter1 = new BlogAdapter(this, blogs1);
    	product1Blogs.setAdapter(blogAdapter1);
    	product1Blogs.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position, long id) {
				String url = product1BlogResults.getDocs().get(position).getUrl();
				viewTweet(url);
			}
    	});
    	
    	//videoAdapter1 = new VideoAdapter(this, product1YoutubeResults.getVideos());
    	List<Video> videos1 = new ArrayList<Video>();
    	videoAdapter1 = new VideoAdapter(this, videos1);
    	product1Videos.setAdapter(videoAdapter1);
    	product1Videos.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position, long id) {
				String url = product1YoutubeResults.getVideos().get(position).getUrl();
				viewYoutube(url);
			}
    	});
    }
    
    private void initProduct2() {
    	View p2 = findViewById(R.id.productcompare_product2);
    	
    	product2Name = (TextView) p2.findViewById(R.id.productview2_name);
    	product2SentimentTwitter = (TripleBarWidget) p2.findViewById(R.id.productview2_sentimentTwitter);
    	product2SentimentBlogger = (TripleBarWidget) p2.findViewById(R.id.productview2_sentimentBlogger);
    	product2Tweets = (ListView) p2.findViewById(R.id.productview2_tweetlist);
    	product2Videos = (GridView) p2.findViewById(R.id.productview2_videolist);
    	product2Blogs = (ListView) p2.findViewById(R.id.productview2_bloglist);
    	product2Button = (Button) p2.findViewById(R.id.productview2_buybutton);
    	product2AmazonImage = (ImageView) p2.findViewById(R.id.amazon_image2);
    	product2AmazonPrice = (TextView) p2.findViewById(R.id.amazon_price2);
    	product2AmazonProgress = (ProgressBar) p2.findViewById(R.id.amazon2_progress);
    	
    	amazonCollapse2 = (LinearLayout) p2.findViewById(R.id.amazon2_collapser);
    	amazonCollapse2.setVisibility(View.VISIBLE);
    	amazonCollapse2Tab = (ImageView) p2.findViewById(R.id.productview2_amazontab);
    	
    	twitterCollapse2 = (RelativeLayout) p2.findViewById(R.id.twitter2_collapser);
    	twitterCollapse2.setVisibility(View.INVISIBLE);
    	twitterCollapse2.setAlpha(0f);
    	twitterCollapse2Tab = (ImageView) p2.findViewById(R.id.productview2_twittertab);
    	
    	bloggerCollapse2 = (RelativeLayout) p2.findViewById(R.id.blogger2_collapser);
    	bloggerCollapse2.setVisibility(View.INVISIBLE);
    	bloggerCollapse2.setAlpha(0f);
    	bloggerCollapse2Tab = (ImageView) p2.findViewById(R.id.productview2_bloggertab);
    	
    	youtubeCollapse2 = (RelativeLayout) p2.findViewById(R.id.youtube2_collapser);
    	youtubeCollapse2.setVisibility(View.INVISIBLE);
    	youtubeCollapse2.setAlpha(0f);
    	youtubeCollapse2Tab = (ImageView) p2.findViewById(R.id.productview2_youtubetab);
    	
    	//tweetAdapter2 = new TweetAdapter(this, product2TweetResults.getDocs());
    	List<Tweet> tweets2 = new ArrayList<Tweet>();
    	tweetAdapter2 = new TweetAdapter(this, tweets2);
    	product2Tweets.setAdapter(tweetAdapter2);
    	product2Tweets.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position, long id) {
				String url = product2TweetResults.getDocs().get(position).getUrl();
				viewTweet(url);
			}
    	});
    	
    	List<Tweet> blogs2 = new ArrayList<Tweet>();
    	blogAdapter2 = new BlogAdapter(this, blogs2);
    	product2Blogs.setAdapter(blogAdapter2);
    	product2Blogs.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position, long id) {
				String url = product2BlogResults.getDocs().get(position).getUrl();
				viewTweet(url);
			}
    	});
    	
    	//videoAdapter2 = new VideoAdapter(this, product2YoutubeResults.getVideos());
    	List<Video> videos2 = new ArrayList<Video>();
    	videoAdapter2 = new VideoAdapter(this, videos2);
    	product2Videos.setAdapter(videoAdapter2);
    	product2Videos.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> av, View v, int position, long id) {
				String url = product2YoutubeResults.getVideos().get(position).getUrl();
				viewYoutube(url);
			}
    	});
    }
    
    private void showProduct1(String source) {
    	if (product1AmazonResult == null)
    		return;
    	
    	if (source.equals("amazon")) {
	    	Product product = selectedProduct1;
	    	
	    	//product1Name.setText(product.getTitle());
	    	product1AmazonPrice.setText(product.getPrice());
	    	
	    	AsyncBitmapLoader abl = new AsyncBitmapLoader(
	    			product1AmazonImage, null,
	    			product1AmazonProgress, product.getImg(), null);
	    	product1AmazonImage.setTag(product.getImg());
	    	abl.execute(product.getImg());
	    	
	    	product1Button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					viewTweet(selectedProduct1.getUrl());
				}
	    	});
    	} else if (source.equals("twitter")) {
	    	// Set up tweet adapter
	    	if (product1TweetResults != null) {
	    		tweetAdapter1.setTweets(product1TweetResults.getDocs());
	    		
	    		// Twitter sentiment
	        	List<Integer> sentimentTwitter = product1TweetResults.getSentiments();
	        	product1SentimentTwitter.setValues(
	        			sentimentTwitter.get(2),
	        			sentimentTwitter.get(1),
	        			sentimentTwitter.get(0));
	    	}
    	} else if (source.equals("blogger")) {
	    	if (product1BlogResults != null) {
	    		blogAdapter1.setTweets(product1BlogResults.getDocs());
	    		
	    		// Blogger sentiment
	        	List<Integer> sentimentBlogger = product1BlogResults.getSentiments();
	        	product1SentimentBlogger.setValues(
	        			sentimentBlogger.get(2),
	        			sentimentBlogger.get(1),
	        			sentimentBlogger.get(0));
	    	}
    	} else if (source.equals("youtube")) {
	    	if (product1YoutubeResults != null) {
	    		videoAdapter1.setVideos(product1YoutubeResults.getVideos());
	    	}
    	}
    }
    
    private void showProduct2(String source) {
    	if (product2AmazonResult == null)
    		return;
    	
    	if (source.equals("amazon")) {
	    	Product product = selectedProduct2;
	    	
	    	//product2Name.setText(product.getTitle());
	    	product2AmazonPrice.setText(product.getPrice());
	    	
	    	AsyncBitmapLoader abl = new AsyncBitmapLoader(
	    			product2AmazonImage, null,
	    			product2AmazonProgress, product.getImg(), null);
	    	product2AmazonImage.setTag(product.getImg());
	    	abl.execute(product.getImg());
	    	
	    	product2Button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					viewTweet(selectedProduct2.getUrl());
				}
	    	});
    	} else if (source.equals("twitter")) {
	    	// Set up tweet adapter
	    	if (product2TweetResults != null) {
	    		tweetAdapter2.setTweets(product2TweetResults.getDocs());
	    		
	    		// Twitter sentiment
	        	List<Integer> sentimentTwitter = product2TweetResults.getSentiments();
	        	product2SentimentTwitter.setValues(
	        			sentimentTwitter.get(2),
	        			sentimentTwitter.get(1),
	        			sentimentTwitter.get(0));
	    	}
    	} else if (source.equals("blogger")) {
	    	if (product2BlogResults != null) {
	    		blogAdapter2.setTweets(product2BlogResults.getDocs());
	    		
	    		// Blogger sentiment
	        	List<Integer> sentimentBlogger = product2BlogResults.getSentiments();
	        	product2SentimentBlogger.setValues(
	        			sentimentBlogger.get(2),
	        			sentimentBlogger.get(1),
	        			sentimentBlogger.get(0));
	    	}
    	} else if (source.equals("youtube")) {
	    	if (product2YoutubeResults != null) {
	    		videoAdapter2.setVideos(product2YoutubeResults.getVideos());
	    	}
    	}
    }
    
    private void updateProduct() {
    	//AmazonDownloadTask adt = new AmazonDownloadTask();
    	//adt.execute();
    	
    	showProduct1("amazon");
    	showProduct2("amazon");
    	
    	TwitterDownloadTask tdt = new TwitterDownloadTask();
    	tdt.execute(product1Query, product2Query);
    	
    	YoutubeDownloadTask ydt = new YoutubeDownloadTask();
    	ydt.execute(product1Query, product2Query);
    	
    	BloggerDownloadTask bdt = new BloggerDownloadTask();
    	bdt.execute(product1Query, product2Query);
    	
    	
    }
    
    private class AmazonDownloadTask extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... arg0) {
			product1AmazonResult = (AmazonResult) dataService.getResultData("amazon", arg0[0]);
	    	product2AmazonResult = (AmazonResult) dataService.getResultData("amazon", arg0[1]);
	    	
	    	return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			Log.i("GiraffaCakes", "onPostExecute()");
			
			showProduct1("amazon");
	        showProduct2("amazon");
	    }
    }
    
    private class TwitterDownloadTask extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... arg0) {
			product1TweetResults = (TweetResult) dataService.getResultData("twitter", arg0[0]);
	    	product2TweetResults = (TweetResult) dataService.getResultData("twitter", arg0[1]);
	    	
	    	return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			Log.i("GiraffaCakes", "onPostExecute()");
			
			showProduct1("twitter");
	        showProduct2("twitter");
	    }
    }
    
    private class BloggerDownloadTask extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... arg0) {
	    	product1BlogResults = (BlogResult) dataService.getResultData("blogger", arg0[0]);
	    	product2BlogResults = (BlogResult) dataService.getResultData("blogger", arg0[1]);

	    	return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			Log.i("GiraffaCakes", "onPostExecute()");
			
			showProduct1("blogger");
	        showProduct2("blogger");
	    }
    }
    
    private class YoutubeDownloadTask extends AsyncTask<String, Void, Integer> {
		@Override
		protected Integer doInBackground(String... arg0) {
	    	product1YoutubeResults = (YoutubeResult) dataService.getResultData("youtube", arg0[0]);
	    	product2YoutubeResults = (YoutubeResult) dataService.getResultData("youtube", arg0[1]);
	    	
	    	return 0;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			Log.i("GiraffaCakes", "onPostExecute()");
			
			showProduct1("youtube");
	        showProduct2("youtube");
	    }
    }
    
    public void toggleAmazon(View v) {
    	System.err.println("Collapse Amazon");
    	
    	if (v.getId() == amazonCollapse1Tab.getId()) {
	    	if (amazonCollapse1.getAlpha() == 1f) {
	    		return;
	    	} else {
	    		// Show twitter stuff
	    		amazonCollapse1.setVisibility(View.VISIBLE);
	    		
	    		(new AlphaAnimation(amazonCollapse1, 1f)).start();
	    		(new AlphaAnimation(twitterCollapse1, 0f)).start();
	    		(new AlphaAnimation(bloggerCollapse1, 0f)).start();
	    		(new AlphaAnimation(youtubeCollapse1, 0f)).start();
	    		
	    		amazonCollapse1.setFocusable(true);
	    		twitterCollapse1.setFocusable(false);
	    		bloggerCollapse1.setFocusable(false);
	    		youtubeCollapse1.setFocusable(false);
	    		
	    		twitterCollapse1.setVisibility(View.INVISIBLE);
	    		bloggerCollapse1.setVisibility(View.INVISIBLE);
	    		youtubeCollapse1.setVisibility(View.INVISIBLE);
	    	}
    	} else {
    		if (amazonCollapse2.getAlpha() == 1f) {
	    		return;
	    	} else {
	    		// Show twitter stuff
	    		amazonCollapse2.setVisibility(View.VISIBLE);
	    		
	    		(new AlphaAnimation(amazonCollapse2, 1f)).start();
	    		(new AlphaAnimation(twitterCollapse2, 0f)).start();
	    		(new AlphaAnimation(bloggerCollapse2, 0f)).start();
	    		(new AlphaAnimation(youtubeCollapse2, 0f)).start();
	    		
	    		amazonCollapse2.setFocusable(true);
	    		twitterCollapse2.setFocusable(false);
	    		bloggerCollapse2.setFocusable(false);
	    		youtubeCollapse2.setFocusable(false);
	    		
	    		twitterCollapse2.setVisibility(View.INVISIBLE);
	    		bloggerCollapse2.setVisibility(View.INVISIBLE);
	    		youtubeCollapse2.setVisibility(View.INVISIBLE);
	    	}
    	}
    }
    
    public void toggleTwitter(View v) {
    	System.err.println("Collapse twitter");
    	
    	if (v.getId() == twitterCollapse1Tab.getId()) {
	    	if (twitterCollapse1.getAlpha() == 1f) {
	    		return;
	    	} else {
	    		// Show twitter stuff
	    		twitterCollapse1.setVisibility(View.VISIBLE);
	    		
	    		(new AlphaAnimation(amazonCollapse1, 0f)).start();
	    		(new AlphaAnimation(twitterCollapse1, 1f)).start();
	    		(new AlphaAnimation(bloggerCollapse1, 0f)).start();
	    		(new AlphaAnimation(youtubeCollapse1, 0f)).start();
	    		
	    		amazonCollapse1.setFocusable(false);
	    		twitterCollapse1.setFocusable(true);
	    		bloggerCollapse1.setFocusable(false);
	    		youtubeCollapse1.setFocusable(false);
	    		
	    		amazonCollapse1.setVisibility(View.INVISIBLE);
	    		bloggerCollapse1.setVisibility(View.INVISIBLE);
	    		youtubeCollapse1.setVisibility(View.INVISIBLE);
	    	}
    	} else {
    		if (twitterCollapse2.getAlpha() == 1f) {
	    		return;
	    	} else {
	    		// Show twitter stuff
	    		twitterCollapse2.setVisibility(View.VISIBLE);
	    		
	    		(new AlphaAnimation(amazonCollapse2, 0f)).start();
	    		(new AlphaAnimation(twitterCollapse2, 1f)).start();
	    		(new AlphaAnimation(bloggerCollapse2, 0f)).start();
	    		(new AlphaAnimation(youtubeCollapse2, 0f)).start();

	    		amazonCollapse2.setFocusable(false);
	    		twitterCollapse2.setFocusable(true);
	    		bloggerCollapse2.setFocusable(false);
	    		youtubeCollapse2.setFocusable(false);
	    		
	    		amazonCollapse2.setVisibility(View.INVISIBLE);
	    		bloggerCollapse2.setVisibility(View.INVISIBLE);
	    		youtubeCollapse2.setVisibility(View.INVISIBLE);
	    	}
    	}
    }
    
    public void toggleBlogger(View v) {
    	System.err.println("Collapse blogger");
    	
    	if (v.getId() == bloggerCollapse1Tab.getId()) {
	    	if (bloggerCollapse1.getAlpha() == 1f) {
	    		return;
	    	} else {
	    		// Show blogger stuff
	    		bloggerCollapse1.setVisibility(View.VISIBLE);
	    		
	    		(new AlphaAnimation(amazonCollapse1, 0f)).start();
	    		(new AlphaAnimation(twitterCollapse1, 0f)).start();
	    		(new AlphaAnimation(bloggerCollapse1, 1f)).start();
	    		(new AlphaAnimation(youtubeCollapse1, 0f)).start();

	    		amazonCollapse1.setFocusable(false);
	    		twitterCollapse1.setFocusable(false);
	    		bloggerCollapse1.setFocusable(true);
	    		youtubeCollapse1.setFocusable(false);
	    		
	    		amazonCollapse1.setVisibility(View.INVISIBLE);
	    		twitterCollapse1.setVisibility(View.INVISIBLE);
	    		youtubeCollapse1.setVisibility(View.INVISIBLE);
	    	}
    	} else {
    		if (bloggerCollapse2.getAlpha() == 1f) {
	    		return;
	    	} else {
	    		// Show blogger stuff
	    		bloggerCollapse2.setVisibility(View.VISIBLE);
	    		
	    		(new AlphaAnimation(amazonCollapse2, 0f)).start();
	    		(new AlphaAnimation(twitterCollapse2, 0f)).start();
	    		(new AlphaAnimation(bloggerCollapse2, 1f)).start();
	    		(new AlphaAnimation(youtubeCollapse2, 0f)).start();

	    		amazonCollapse2.setFocusable(false);
	    		twitterCollapse2.setFocusable(false);
	    		bloggerCollapse2.setFocusable(true);
	    		youtubeCollapse2.setFocusable(false);
	    		
	    		amazonCollapse1.setVisibility(View.INVISIBLE);
	    		twitterCollapse2.setVisibility(View.INVISIBLE);
	    		youtubeCollapse2.setVisibility(View.INVISIBLE);
	    	}
    	}
    }
    
    public void toggleYoutube(View v) {
    	System.err.println("Collapse youtube");
    	
    	if (v.getId() == youtubeCollapse1Tab.getId()) {
	    	if (youtubeCollapse1.getAlpha() == 1f) {
	    		return;
	    	} else {
	    		// Show blogger stuff
	    		youtubeCollapse1.setVisibility(View.VISIBLE);
	    		
	    		(new AlphaAnimation(amazonCollapse1, 0f)).start();
	    		(new AlphaAnimation(twitterCollapse1, 0f)).start();
	    		(new AlphaAnimation(bloggerCollapse1, 0f)).start();
	    		(new AlphaAnimation(youtubeCollapse1, 1f)).start();

	    		amazonCollapse1.setFocusable(false);
	    		twitterCollapse1.setFocusable(false);
	    		bloggerCollapse1.setFocusable(false);
	    		youtubeCollapse1.setFocusable(true);
	    		
	    		amazonCollapse1.setVisibility(View.INVISIBLE);
	    		twitterCollapse1.setVisibility(View.INVISIBLE);
	    		bloggerCollapse1.setVisibility(View.INVISIBLE);
	    	}
    	} else {
    		if (youtubeCollapse2.getAlpha() == 1f) {
	    		return;
	    	} else {
	    		// Show blogger stuff
	    		youtubeCollapse2.setVisibility(View.VISIBLE);
	    		
	    		(new AlphaAnimation(amazonCollapse2, 0f)).start();
	    		(new AlphaAnimation(twitterCollapse2, 0f)).start();
	    		(new AlphaAnimation(bloggerCollapse2, 0f)).start();
	    		(new AlphaAnimation(youtubeCollapse2, 1f)).start();

	    		amazonCollapse2.setFocusable(false);
	    		twitterCollapse2.setFocusable(false);
	    		bloggerCollapse2.setFocusable(false);
	    		youtubeCollapse2.setFocusable(true);
	    		
	    		amazonCollapse2.setVisibility(View.INVISIBLE);
	    		twitterCollapse2.setVisibility(View.INVISIBLE);
	    		bloggerCollapse2.setVisibility(View.INVISIBLE);
	    	}
    	}
    }
}