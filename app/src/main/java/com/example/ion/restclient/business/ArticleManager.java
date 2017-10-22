package com.example.ion.restclient.business;

import com.example.ion.restclient.models.Article;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Before calling methods begin ,commit and close realm transactions
 */
public class ArticleManager {
    private static final ArticleManager articleManager;
    public final static int DEFAULT_LOADS =20;
    public final static int MAX_ARTICLES_SIZE=100;
    public final static int REMAIN_AFTER_MAX_SIZE=100;
    public final static int MIN_ARTICLES_SIZE=10;
    public final static String LOAD_LAST="loadLast";
    public static final String LOAD_PREV="loadPrev";
    private static final String TAG = "Article Manager";
    public Realm realm;
    private ArticleManager(){

    };

    public static ArticleManager getInstance(){
        Realm realm=Realm.getDefaultInstance();
        return getInstance(realm);
    }

    public static ArticleManager getInstance(Realm realm){
        articleManager.realm=realm;
        return articleManager;
    }

    public static RealmResults<Article> getArticlesAsync( ){
        return getArticlesAsync(Realm.getDefaultInstance());
    }

    public static RealmResults<Article> getArticlesAsync(Realm realm){
        RealmResults<Article> result;
        result=realm.where(Article.class).findAllSortedAsync(Article.ID_FIELD, Sort.DESCENDING);
        return result;
    }

    public long getMaxId(){
        long result;
        Number max=realm.where(Article.class).max(Article.ID_FIELD);
        if(max!=null) {
            result = max.longValue();
        }else{
            result=-1;
        }
        return result;
    }

    /**
     *  should be in transaction and commited at the end;
     * @return max article id
     */
    public long getMinId(){
        long result;
        Number min=realm.where(Article.class).min(Article.ID_FIELD);
        if(min!=null) {
            result = min.longValue();
        }else{
            result=-1;
        }
        return result;
    }

    public long getLastChange(){
        Number nr= realm.where(Article.class).max(Article.UPDATED_FIELD);
        if(nr!=null){
            return nr.longValue();
        }else{
            return -1;
        }
    }

    public void onDestroy(){
        if(realm.isInTransaction()){
            realm.commitTransaction();
        }
        realm.close();
        realm=null;
    }

    static {
        articleManager=new ArticleManager();
    }

}
