package com.mmps.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.mmps.R;
import com.mmps.adapters.YouTubeLinksAdapter;
import com.mmps.app.App;
import com.mmps.listeners.RecyclerViewItemListener;
import com.mmps.models.YouTubeModel;

import java.util.ArrayList;

public class VideosActivity extends YouTubeBaseActivity {

	private Toolbar toolbar;
	private ArrayList<YouTubeModel> youTubeModels;
	private YouTubePlayer youTubePlayer;
	private String API_KEY = "AIzaSyDOZlRWzH6WgRmy3-aBMY6t5tKfhyQv-sU";
	//private String API_KEY = "AIzaSyBJJMCJ92_TdVfplxxv0ftrQESv5FcUspY";
	private RecyclerView rvYouTubeLink;
	private int currentPosition = 0;
	private String TAG = getClass().getSimpleName();

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(App.localeManager.setLocale(base));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videos);

		initToolbar();

		if (getIntent().hasExtra("youtube")) {
			try {
				youTubeModels = (ArrayList<YouTubeModel>) getIntent().getExtras().get("youtube");


				YouTubePlayerView frag =
						(YouTubePlayerView) findViewById(R.id.youtube_fragment);
				frag.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
					@Override
					public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
						VideosActivity.this.youTubePlayer = youTubePlayer;
						VideosActivity.this.youTubePlayer.setShowFullscreenButton(true);
						VideosActivity.this.youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
						VideosActivity.this.youTubePlayer.loadVideo(youTubeModels.get(0).getDescription().replace("https://www.youtube.com/embed/", ""));
						currentPosition = 0;

						VideosActivity.this.youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
							@Override
							public void onLoading() {

							}

							@Override
							public void onLoaded(String s) {
								if (VideosActivity.this.youTubePlayer != null && !s.isEmpty()) {
									VideosActivity.this.youTubePlayer.play();
								}
							}

							@Override
							public void onAdStarted() {

							}

							@Override
							public void onVideoStarted() {

							}

							@Override
							public void onVideoEnded() {
								Log.e(TAG, "onVideoEnded: ");
								currentPosition++;
								VideosActivity.this.youTubePlayer.cueVideo(youTubeModels.get(currentPosition).getDescription().replace("https://www.youtube.com/embed/", ""));
							}

							@Override
							public void onError(YouTubePlayer.ErrorReason errorReason) {
								Log.e(TAG, "onError: " + errorReason.toString());
							}
						});
					}

					@Override
					public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
						Log.e(TAG, "onError: " + youTubeInitializationResult.toString());
					}
				});

				rvYouTubeLink = findViewById(R.id.rvVideoLinks);
				rvYouTubeLink.setLayoutManager(new LinearLayoutManager(this));
				rvYouTubeLink.setAdapter(new YouTubeLinksAdapter(this, youTubeModels, API_KEY));

				rvYouTubeLink.addOnItemTouchListener(new RecyclerViewItemListener(VideosActivity.this, rvYouTubeLink, new RecyclerViewItemListener.OnItemClickListener() {
					@Override
					public void onItemClick(View view, int position) {
						VideosActivity.this.youTubePlayer.cueVideo(youTubeModels.get(position).getDescription().replace("https://www.youtube.com/embed/", ""));
						currentPosition = position;
					}

					@Override
					public void onLongItemClick(View view, int position) {

					}
				}));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void initToolbar() {
		toolbar = findViewById(R.id.toolbar);
		TextView tvTitle = toolbar.findViewById(R.id.tv_title);
		tvTitle.setText(getString(R.string.videos));
		toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
		/*setSupportActionBar(toolbar);
		getSupportActionBar().setTitle("");*/
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}

		return false;
	}
}
