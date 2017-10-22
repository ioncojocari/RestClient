package com.example.ion.restclient.dagger;

import com.example.ion.restclient.APP;
import com.example.ion.restclient.business.UpdateArticlesService;
import com.example.ion.restclient.dagger.modules.ArticleServiceModule;
import com.example.ion.restclient.dagger.modules.ContextModule;
import com.example.ion.restclient.dagger.modules.NetworkModule;
import com.example.ion.restclient.dagger.modules.ScheduleModule;
import com.example.ion.restclient.dagger.modules.UtilsModule;
import com.example.ion.restclient.dagger.scopes.ArticleServiceScope;
import com.example.ion.restclient.business.CommandFactory;
import dagger.Component;

@Component(modules = {ArticleServiceModule.class,UtilsModule.class, NetworkModule.class,ScheduleModule.class,ContextModule.class})
@ArticleServiceScope
public interface AppComponent {
    void inject(CommandFactory commandFactory);
    void inject(UpdateArticlesService service);
    void inject(APP app);
}
