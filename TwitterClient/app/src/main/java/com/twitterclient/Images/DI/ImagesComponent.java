package com.twitterclient.Images.DI;


import com.twitterclient.Images.ImagesPresenter;
import com.twitterclient.Images.UI.ImagesFragment;
import com.twitterclient.Libs.DI.LibsModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {LibsModule.class, ImagesModule.class})
public interface ImagesComponent {
    void inject(ImagesFragment fragment);

    ImagesPresenter getPresenter();
}
