package com.codepath.apps.restclienttemplate;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.codepath.apps.restclienttemplate.models.Tweet;

public class TweetDataSourceFactory extends DataSource.Factory<Long, Tweet> {
    TwitterClient client;
    // Use to hold a reference to the
    public MutableLiveData<TweetDataSource> postLiveData;

    public TweetDataSourceFactory(TwitterClient client) {
        this.client = client;
    }

    @Override
    public DataSource<Long, Tweet> create() {
        TweetDataSource dataSource = new TweetDataSource(this.client);
        // Keep reference to the data source with a MutableLiveData reference
        postLiveData = new MutableLiveData<>();
        postLiveData.postValue(dataSource);
        return dataSource;
    }
}
