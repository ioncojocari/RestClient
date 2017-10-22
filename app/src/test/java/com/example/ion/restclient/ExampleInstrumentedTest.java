package com.example.ion.restclient;


import android.util.Log;

import com.example.ion.restclient.models.Article;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(JUnit4.class)

public class ExampleInstrumentedTest {
    @Test
    public void test_if_articles_equals_works(){
        String title="title";
        String content="content";
        String url="url";
        String secondUrl="second _url";
        Article first=new Article(title,content,url);
        Article second=new Article(title,content,url);
        Article third=new Article(title,content,secondUrl);
        assertTrue(first.equals(second));
        assertFalse(third.equals(first));
        assertFalse(third.equals(second));
        assertTrue(first.hashCode()==second.hashCode());
    }


}
