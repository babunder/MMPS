package com.mmps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mmps.R;
import com.mmps.models.YouTubeModel;

import java.util.ArrayList;

/**
 * @author Aditya Kulkarni
 */

public class YouTubeLinksAdapter extends RecyclerView.Adapter{
    private Context context;
    private ArrayList<YouTubeModel> youTubeModels;
    private String API_KEY;

    public YouTubeLinksAdapter(Context context, ArrayList<YouTubeModel> youTubeModels, String API_KEY) {
        this.context = context;
        this.youTubeModels = youTubeModels;
        this.API_KEY = API_KEY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.youtube_link_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder)holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return youTubeModels != null ? youTubeModels.size() : 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        YouTubeThumbnailView youTubeThumbnailView;
        TextView tvYouTubeLink;
        public ViewHolder(View itemView) {
            super(itemView);

            tvYouTubeLink = itemView.findViewById(R.id.tvYouTubeLink);
            youTubeThumbnailView = itemView.findViewById(R.id.ytThumbnail);
        }

        void bindView(final int position){
            tvYouTubeLink.setText(youTubeModels.get(position).getTitle());
            youTubeThumbnailView.initialize(API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(youTubeModels.get(position).getDescription().replace("https://www.youtube.com/embed/", ""));
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                        @Override
                        public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                            youTubeThumbnailLoader.release();
                        }

                        @Override
                        public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                        }
                    });

                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });
        }
    }
}
