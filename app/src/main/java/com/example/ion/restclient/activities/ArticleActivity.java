package com.example.ion.restclient.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.ion.restclient.APP;
import com.example.ion.restclient.utils.MyUtils;
import com.example.ion.restclient.R;
import com.example.ion.restclient.models.Article;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {
    @BindView(R.id.imageView)
    protected ImageView imageView;
    @BindView(R.id.titleTextView)
    protected TextView titleTextView;
    @BindView(R.id.contentTextView)
    protected TextView contentTextView;
    private Article article;
    @Inject
    protected MyUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        APP.getUtilsComponent().inject(this);
        init(savedInstanceState);
    }

    public void init(Bundle savedInstanceState){
        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(Article.OBJECT)){
                article=savedInstanceState.getParcelable(Article.OBJECT);
            }
        }else {
            Bundle extra = getIntent().getExtras();
            if (extra!=null&&extra.containsKey(Article.OBJECT)) {
                article = extra.getParcelable(Article.OBJECT);
            } else {
                finish();
            }
        }
        showArticle();
    }

    private void displayArticle(Article article){
        String url= utils.getUrl(article.getUrl());
        RequestCreator request=Picasso.with(ArticleActivity.this).load(url).networkPolicy(NetworkPolicy.OFFLINE);
        request.into(imageView);
        contentTextView.setText(article.getContent());
        titleTextView.setText(article.getTitle());
    }

    private void showArticle(){
        if (article!=null) {
            displayArticle(article);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(article!=null){
            outState.putParcelable(Article.OBJECT,article);
        }
    }
}
