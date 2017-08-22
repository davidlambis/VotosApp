package com.twitterclient.Images.UI;

import com.twitterclient.Entities.Image;

import java.util.List;

public interface ImagesView {

    void showImages();

    void hideImages();

    void showProgress();

    void hideProgress();

    void onError(String error);

    void setContent(List<Image> items);
}
