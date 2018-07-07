package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    List<Tweet> mTweets;
    Context context;
    TwitterClient client;

    // pass in Tweets array into constructor
    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
    }

    // for each row, inflate layout and cache references into ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind values based on position of element
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        // get data according to position
        final Tweet tweet = mTweets.get(position);
        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvTimestamp.setText(tweet.timeStamp);
        holder.tvHandle.setText("@" + tweet.user.screenName);
        holder.tvRetweets.setText(Long.toString(tweet.retweetCount));
        holder.tvFavs.setText(Long.toString(tweet.favCount));

        if (tweet.favorited) {
            Drawable icon = context.getResources().getDrawable(R.drawable.ic_filled_heart);
            holder.btFavorite.setCompoundDrawablesWithIntrinsicBounds( null, icon, null, null );
        } else {
            Drawable icon = context.getResources().getDrawable(R.drawable.ic_heart);
            holder.btFavorite.setCompoundDrawablesWithIntrinsicBounds( null, icon, null, null );
        }

        if (tweet.retweeted) {
            Drawable icon = context.getResources().getDrawable(R.drawable.ic_retweeted_green);
            holder.btRetweet.setCompoundDrawablesWithIntrinsicBounds( null, icon, null, null );
        } else {
            Drawable icon = context.getResources().getDrawable(R.drawable.ic_retweet);
            holder.btRetweet.setCompoundDrawablesWithIntrinsicBounds( null, icon, null, null );
        }

        Glide.with(context)
                .load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
    }

    // toggle favorite button and make API call
    public void favoriteToggle(View v, final Tweet tweet) {
        client = TwitterApp.getRestClient(context);
        if (tweet.favorited) {
            // unlike
            client.unFavTweet(tweet.uid, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    tweet.favCount -= 1;
                    notifyDataSetChanged();
                    tweet.favorited = false;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("UnFavTweet", errorResponse.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("UnFavTweet", errorResponse.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("UnFavTweet", responseString);
                }
            });
        } else {
            client.favTweet(tweet.uid, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    tweet.favCount += 1;
                    notifyDataSetChanged();
                    tweet.favorited = true;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("favTweet", errorResponse.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("favTweet", errorResponse.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("favTweet", responseString);
                }
            });
        }
    }

    // toggle retweet button and make API call
    public void retweetToggle(View v, final Tweet tweet) {
        client = TwitterApp.getRestClient(context);
        if (tweet.retweeted) {
            // unlike
            client.unRetweetTweet(tweet.uid, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    tweet.retweetCount -= 1;
                    notifyDataSetChanged();
                    tweet.retweeted = false;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("unReTweet", errorResponse.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("unReTweet", errorResponse.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("unReTweet", responseString);
                }
            });
        } else {
            client.retweetTweet(tweet.uid, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    tweet.retweetCount += 1;
                    notifyDataSetChanged();
                    tweet.retweeted = true;
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("reTweet", errorResponse.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("reTweet", errorResponse.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("reTweet", responseString);
                }
            });
        }
    }

    @Override
    public int getItemCount() { return mTweets.size(); }

    // create ViewHolder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvTimestamp;
        public TextView tvHandle;
        public TextView tvRetweets;
        public TextView tvFavs;
        public Button btReply;
        public Button btRetweet;
        public Button btFavorite;

        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById assignments
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUserName);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvHandle = itemView.findViewById(R.id.tvHandle);
            tvRetweets = itemView.findViewById(R.id.tvRetweets);
            tvFavs = itemView.findViewById(R.id.tvFavs);
            btReply = itemView.findViewById(R.id.btReply);
            btRetweet = itemView.findViewById(R.id.btRetweet);
            btFavorite = itemView.findViewById(R.id.btFavorite);

            itemView.setOnClickListener(this);

            btFavorite.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Tweet tweet = mTweets.get(pos);
                    favoriteToggle(v, tweet);
                    Drawable icon;
                    if (tweet.favorited) {
                        icon = context.getResources().getDrawable(R.drawable.ic_filled_heart);
                    } else {
                        icon = context.getResources().getDrawable(R.drawable.ic_heart);
                    }
                    btFavorite.setCompoundDrawablesWithIntrinsicBounds( null, icon, null, null );
                }
            });

            btRetweet.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Tweet tweet = mTweets.get(pos);
                    retweetToggle(v, tweet);
                    Drawable icon;
                    if (tweet.retweeted) {
                        icon = context.getResources().getDrawable(R.drawable.ic_retweeted_green);
                    } else {
                        icon = context.getResources().getDrawable(R.drawable.ic_retweet);
                    }
                    btRetweet.setCompoundDrawablesWithIntrinsicBounds( null, icon, null, null );
                }
            });
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                Tweet tweet = mTweets.get(pos);
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                context.startActivity(intent);
            }
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
