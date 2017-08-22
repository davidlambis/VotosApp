package com.twitterclient.Images.DI;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Session;
import com.twitterclient.Api.CustomTwitterApiClient;
import com.twitterclient.Entities.Image;
import com.twitterclient.Images.ImagesInteractor;
import com.twitterclient.Images.ImagesInteractorImpl;
import com.twitterclient.Images.ImagesPresenter;
import com.twitterclient.Images.ImagesPresenterImpl;
import com.twitterclient.Images.ImagesRepository;
import com.twitterclient.Images.ImagesRepositoryImpl;
import com.twitterclient.Images.UI.Adapters.IOnItemClickListener;
import com.twitterclient.Images.UI.Adapters.ImagesAdapter;
import com.twitterclient.Images.UI.ImagesView;
import com.twitterclient.Libs.Base.EventBus;
import com.twitterclient.Libs.Base.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ImagesModule {
    private ImagesView view;
    private IOnItemClickListener clickListener;

    public ImagesModule(ImagesView view, IOnItemClickListener listener) {
        this.view = view;
        this.clickListener = listener;
    }

    @Provides
    @Singleton
    ImagesAdapter providesAdapter(List<Image> dataset, ImageLoader imageLoader, IOnItemClickListener clickListener) {
        return new ImagesAdapter(dataset, imageLoader, clickListener);
    }

    @Provides
    @Singleton
    IOnItemClickListener providesOnItemClickListener() {
        return this.clickListener;
    }

    @Provides
    @Singleton
    List<Image> providesItemsList() {
        return new ArrayList<Image>();
    }


    @Provides
    @Singleton
    ImagesPresenter providesImagesPresenter(EventBus eventBus, ImagesView view, ImagesInteractor interactor) {
        return new ImagesPresenterImpl(eventBus, view, interactor);
    }

    @Provides
    @Singleton
    ImagesView providesImagesView() {
        return this.view;
    }

    @Provides
    @Singleton
    ImagesInteractor providesImagesInteractor(ImagesRepository repository) {
        return new ImagesInteractorImpl(repository);
    }

    @Provides
    @Singleton
    ImagesRepository providesImagesRepository(EventBus eventBus, CustomTwitterApiClient client) {
        return new ImagesRepositoryImpl(eventBus, client);
    }

    @Provides
    @Singleton
    CustomTwitterApiClient providesCustomTwitterApiClient(Session session) {
        return new CustomTwitterApiClient(session);
    }

    @Provides
    @Singleton
    Session providesTwitterSession() {
        return Twitter.getSessionManager().getActiveSession();
    }

}
