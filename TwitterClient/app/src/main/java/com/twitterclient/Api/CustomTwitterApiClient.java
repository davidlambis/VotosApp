package com.twitterclient.Api;

import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;

public class CustomTwitterApiClient extends TwitterApiClient{

    public CustomTwitterApiClient(Session session) {
        super(session);
    }

    public ITimelineService getTimeLineService(){
        return getService(ITimelineService.class);
    }
}
