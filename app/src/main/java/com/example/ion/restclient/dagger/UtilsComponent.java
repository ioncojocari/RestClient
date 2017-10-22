package com.example.ion.restclient.dagger;

import com.example.ion.restclient.utils.GCMService;
import com.example.ion.restclient.activities.ArticleActivity;
import com.example.ion.restclient.business.SaveArticlesCallback;
import com.example.ion.restclient.dagger.modules.ContextModule;
import com.example.ion.restclient.dagger.modules.ScheduleModule;
import com.example.ion.restclient.dagger.modules.UtilsModule;
import com.example.ion.restclient.dagger.scopes.UtilsComponentScope;
import com.example.ion.restclient.models.ArticleRealmAdapter;
import dagger.Component;

@Component(modules = {UtilsModule.class, ScheduleModule.class,ContextModule.class})
@UtilsComponentScope
public interface UtilsComponent {
    void inject(ArticleActivity articleActivity);
    void inject(GCMService service);
    void inject(SaveArticlesCallback saveArticlesCallback);
    void inject(ArticleRealmAdapter articleRealmAdapter);
}
