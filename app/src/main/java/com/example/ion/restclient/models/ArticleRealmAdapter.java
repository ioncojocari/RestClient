package com.example.ion.restclient.models;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ion.restclient.APP;
import com.example.ion.restclient.business.ActivityOptionsManager;
import com.example.ion.restclient.utils.MyUtils;
import com.example.ion.restclient.R;
import com.example.ion.restclient.activities.ArticleActivity;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class ArticleRealmAdapter extends RealmRecyclerViewAdapter<Article,RecyclerView.ViewHolder> {

    private static final String TAG="ArticleRealmAdapter";
    @Inject
    protected MyUtils utils;
    private boolean firstView=false;
    private boolean lastView=false;
    private final static int EMPTY_VIEW_TYPE=0;
    private final static int SIMPLE_VIEW_TYPE=1;
    private ActivityOptionsManager optionsManager;
    private SwipyRefreshLayoutDirection direction;
    private RecyclerView recyclerView;
    public ArticleRealmAdapter(RealmResults<Article> articles, ActivityOptionsManager manager,
                               RecyclerView recyclerView){
        super(articles,true);
        APP.getUtilsComponent().inject(this);
        optionsManager=manager;
        this.recyclerView=recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        if(viewType==SIMPLE_VIEW_TYPE) {
            itemView = inflater.inflate(R.layout.article_row, parent, false);
            return new MyViewHolder(itemView);
        }else{
            itemView=inflater.inflate(R.layout.empty_row,parent,false);
            Log.d(TAG, "onCreateViewHolder: returning empty row");
            return new EmptyViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder  recyclerHolder, int position) {
        if(recyclerHolder instanceof MyViewHolder) {
            if(firstView){
                position--;
            }
            MyViewHolder holder=(MyViewHolder)recyclerHolder;
            Log.d(TAG, "onBindViewHolder: ,position:"+position);
            holder.title.setText(getData().get(position).getTitle());
            holder.id = getData().get(position).getId();
            String url = utils.getUrl(getData().get(position).getUrl());
            Picasso ps = Picasso.with(holder.imageView.getContext());
            ps.load(url)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if((position==0&&firstView)|| (position==getItemCount()-1&& lastView)){
            return EMPTY_VIEW_TYPE;
        }
        return SIMPLE_VIEW_TYPE;
    }

    @Override
    public int getItemCount() {
         if(getData()!=null){
             int size=getData().size();
             if(firstView|| lastView){
                 size++;
             }
             return size;
         }else{
             return 0;
         }
    }

    public void refreshed(){
        firstView=false;
        lastView=false;
        notifyDataSetChanged();
        if(direction==SwipyRefreshLayoutDirection.TOP){
            int y=-200;
            recyclerView.smoothScrollBy(0,y);
            // layoutManager.scrollToPosition(0);
        }else if(direction==SwipyRefreshLayoutDirection.BOTTOM){
            int y=200;
            recyclerView.smoothScrollBy(0,y);
            //layoutManager.scrollToPosition(getItemCount()-1);
        }
    }

    public void refreshing(SwipyRefreshLayoutDirection direction){
        this.direction=direction;
        notifyDataSetChanged();
        if(direction==SwipyRefreshLayoutDirection.TOP){
            firstView=true;
        }else if(direction==SwipyRefreshLayoutDirection.BOTTOM){
            lastView=true;
            recyclerView.smoothScrollBy(0,200);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        long id;
        TextView title;
        ImageView imageView;
        MyViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(title.getContext(), ArticleActivity.class);
                    int position=getAdapterPosition();
                    if(firstView){
                        position--;
                    }
                    intent.putExtra(Article.OBJECT,getData().get(position));
                    ActivityOptionsCompat options=
                            optionsManager.getOptions(
                            Pair.create((View)imageView,R.string.article_image),
                            Pair.create((View)title,R.string.article_title)
                    );
                    Log.d(TAG, "onClick: ImageText:"+title.getText().toString());
                    itemView.getContext().startActivity(intent,options.toBundle());
                }
            });
            if(itemView.findViewById(R.id.titleTextView)!=null) {
                title = (TextView) itemView.findViewById(R.id.titleTextView);
                imageView = (ImageView) itemView.findViewById(R.id.imageView);
            }
        }
    }

    class EmptyViewHolder extends RecyclerView.ViewHolder{
        EmptyViewHolder(final View view){
            super(view);
        }
    }

}
