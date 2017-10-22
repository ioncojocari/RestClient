package com.example.ion.restclient.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.ion.restclient.IDisplayResponse;
import com.example.ion.restclient.R;
import com.example.ion.restclient.business.ActivityOptionsManager;
import com.example.ion.restclient.business.ArticleManager;
import com.example.ion.restclient.business.InitLoader;
import com.example.ion.restclient.models.Article;
import com.example.ion.restclient.models.ArticleRealmAdapter;
import com.example.ion.restclient.models.Response;
import com.example.ion.restclient.business.CommandManager;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements IDisplayResponse {
    public static final String INTENT_FILTER_NAME="commands-status";
    private static final String TAG="MainActivity";
    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.swipyRefreshLayout)
    protected SwipyRefreshLayout swypeRefreshLayout;
    private RealmResults<Article> articlesAsync;
    private ArticleRealmAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ActivityOptionsManager optionsManager;
    private CommandManager cmdManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void init(){
        ButterKnife.bind(this);
        cmdManager=new CommandManager(this);
        swypeRefreshLayout.setOnRefreshListener(swypeListener);
        initRecyclerView();
        initBroadcastReceiver();
        getSupportLoaderManager().initLoader(R.id.init_loader,Bundle.EMPTY,initLoaderCallback);
    }

    private void initRecyclerView(){
        articlesAsync=ArticleManager.getArticlesAsync();
        layoutManager = new LinearLayoutManager(getApplicationContext());
        optionsManager=new ActivityOptionsManager(this);
        adapter=new ArticleRealmAdapter(articlesAsync,optionsManager,recyclerView);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void initBroadcastReceiver(){
        IntentFilter intFilt = new IntentFilter(INTENT_FILTER_NAME);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,intFilt);
    }

    @Override
    public void error(@Nullable String message) {
        if(swypeRefreshLayout.isRefreshing()){
            swypeRefreshLayout.setRefreshing(false);
            adapter.refreshed();
        }
    }

    @Override
    public void success(@Nullable String message) {
        if(swypeRefreshLayout.isRefreshing()){
            swypeRefreshLayout.setRefreshing(false);
            adapter.refreshed();
        }
    }

    private SwipyRefreshLayout.OnRefreshListener swypeListener=new SwipyRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh(SwipyRefreshLayoutDirection direction) {
            if(direction==SwipyRefreshLayoutDirection.TOP) {
                cmdManager.loadLast();
            }else if(direction==SwipyRefreshLayoutDirection.BOTTOM){
                cmdManager.loadPrev();
            }
            adapter.refreshing(direction);
        }
    };

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message=intent.getStringExtra(Response.MESSAGE_FIELD);
            boolean succesful=intent.getBooleanExtra(Response.SUCCESSFUL_FIELD,false);
            if (succesful){
                success(message);
            }else{
                error(message);
            }
        }
    };
    //start command.loadLast();
    private LoaderManager.LoaderCallbacks<Boolean> initLoaderCallback=new LoaderManager.LoaderCallbacks<Boolean>() {
        @Override
        public Loader<Boolean> onCreateLoader(int id, Bundle args) {
            return new InitLoader(getBaseContext());
        }

        @Override
        public void onLoadFinished(Loader<Boolean> loader, Boolean data) {

        }

        @Override
        public void onLoaderReset(Loader<Boolean> loader) {

        }
    };


}
