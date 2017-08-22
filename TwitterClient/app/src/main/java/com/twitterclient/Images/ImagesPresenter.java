package com.twitterclient.Images;

import com.twitterclient.Images.Events.ImagesEvent;

public interface ImagesPresenter {

    void onResume();

    void onPause();

    void onDestroy();

    void getImageTweets();

    void onEventMainThread(ImagesEvent event);
}
