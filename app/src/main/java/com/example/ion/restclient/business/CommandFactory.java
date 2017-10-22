package com.example.ion.restclient.business;

import android.content.Context;
import android.util.Log;
import com.example.ion.restclient.APP;
import com.example.ion.restclient.business.ws.IArticleService;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import io.realm.Realm;
import static android.content.ContentValues.TAG;

public class CommandFactory {
    @Inject
    protected IArticleService service;
    private final Map<Commands, Command> commands;
    private Context context;

    public CommandFactory(Context context){
        this.context=context;
        APP.getComponent().inject(this);
        commands=new HashMap<Commands,Command>();
        addCommand(Commands.LOAD_LAST,loadLast);
        addCommand(Commands.LOAD_PREV,loadPrev);
    }

    private void addCommand(Commands commandEnum,Command command){
        commands.put(commandEnum,command);
    }

    public void executeCommand(Commands commandEnum) {
        if (commands.containsKey(commandEnum)) {
            commands.get(commandEnum).execute();
        }
    }

    public void onDestroy(){
        context=null;
    }

    private final Command loadLast=new Command() {
        @Override
        public void execute() {
            Realm realm=Realm.getDefaultInstance();
            ArticleManager manager=ArticleManager.getInstance(realm);
            realm.beginTransaction();
            long id=0;
            try {
                id=manager.getMaxId();
                realm.commitTransaction();
            }catch (Exception e){
                Log.d(TAG,"exception e"+e.getMessage());
                realm.cancelTransaction();
            }finally {
                realm.close();
            }
            if(context!=null) {
                service.getLast(id).enqueue(new SaveArticlesCallback(context));
            }
        }
    };

    private final Command loadPrev=new Command() {
        @Override
        public void execute() {
            Realm realm=Realm.getDefaultInstance();

            ArticleManager manager=ArticleManager.getInstance(realm);
            long id=0;
            realm.beginTransaction();
            try {
                id=manager.getMinId();
                realm.commitTransaction();
            }catch (Exception e){
                Log.d(TAG,"exception e"+e.getMessage());
                realm.cancelTransaction();
            }finally {
                realm.close();
            }
            if(context!=null)
                service.getBeforeId(id).enqueue(new SaveArticlesCallback(context));
        }
    };

}
