package com.example.ion.restclient.utils;

/**
 * Created by mrT on 27.09.2017.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.squareup.picasso.Cache;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class CustomImageCache implements Cache {
    private final File mCacheDir;

    public CustomImageCache(File picturesDir) {
        mCacheDir = picturesDir;
    }

    @Override
    public Bitmap get(String key) {
        key = String.format("%s.jpg", transform(key) );
        File f = new File(mCacheDir, key);
        return getBitmapFromFile(f.getAbsolutePath());
    }

    @Override
    public void set(String key, Bitmap bitmap) {
        key = String.format("%s.jpg", transform(key) );
        File f = new File(mCacheDir, key);
        saveBitmapToFile(bitmap, f.getAbsolutePath());
    }

    @Override
    public int size() {
        String[] children = mCacheDir.list();
        return children != null ? children.length : 0;
    }

    @Override
    public int maxSize() {
        return 1000;
    }

    @Override
    public void clear() {
        String[] children = mCacheDir.list();
        for (int i = 0; i < children.length; i++) {
            new File(mCacheDir, children[i]).delete();
        }
    }

    @Override
    public void clearKeyUri(String keyPrefix) {
        String key = String.format("%s.jpg", transform(keyPrefix) );
        File f = new File(mCacheDir, key);
        if(f.exists()&&f.isFile()){
            f.delete();
        }
    }

    private String transform(String s) {
        return s.replaceAll("[^A-Za-z0-9]", "").substring(15);
    }

    //region Util methods (could be moved to another Util class)
    public void saveBitmapToFile(Bitmap bmp, String absolutePath){
        bitmapToFile( getBytesFromBitmap(bmp), absolutePath);
    }

    private void bitmapToFile(byte[] data, String absolutePath) {
        if (absolutePath == null) {
            return;
        }
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(absolutePath);
            fos.write(data, 0, data.length);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        } catch (IOException e){
            e.printStackTrace();
            return;
        }
    }

    private byte[] getBytesFromBitmap(Bitmap bmp) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 75, bos);
        return bos.toByteArray();
    }

    public static Bitmap getBitmapFromFile(String absolutePath){
        File file = new File(absolutePath);
        if (file.exists()) {
            return BitmapFactory.decodeFile(absolutePath);
        }
        return null;
    }
    //endregion
}