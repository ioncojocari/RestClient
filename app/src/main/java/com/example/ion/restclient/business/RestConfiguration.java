package com.example.ion.restclient.business;

import android.util.Log;

public enum  RestConfiguration {
    LOCAL("http://192.168.178.28:9000","","api")
    ,SERVER("http://www.androidarticles.tk","","api");
    private static RestConfiguration configuration=SERVER;
    private final String hostName;
    private final String projectName;
    private final String preProjectText;
    private final String postProjectText;
    private final String fullPath;
    public static final String imagesSuffix="/files/";
    RestConfiguration(String hostName,String projectName,String postProjectText){
        this(hostName,"",projectName,postProjectText);
    }

    RestConfiguration(String hostName,String preProjectText,String projectName,String postProjectText){
        this.hostName=hostName;
        this.postProjectText=postProjectText;
        this.projectName=projectName;
        this.preProjectText=preProjectText;
        StringBuilder fullPathBuilder=new StringBuilder();
        if(hostName!=null &&!hostName.isEmpty()){
            fullPathBuilder.append(hostName);
        }
        if(preProjectText!=null &&!preProjectText.isEmpty()){
            fullPathBuilder.append("/").append(preProjectText);
        }
        if(projectName!=null &&!projectName.isEmpty()){
            fullPathBuilder.append("/").append(projectName);
        }
        if(postProjectText!=null &&!postProjectText.isEmpty()){
            fullPathBuilder.append("/").append(postProjectText);
        }
        fullPath=fullPathBuilder.toString();
        Log.d("RestConfiguration",fullPath);
    }

    public final String getFullPath(){
        return fullPath;
    }

    public final String getHostName() {
        return hostName;
    }

    public final String getProjectName() {
        return projectName;
    }

    public final String getPreProjectText() {
        return preProjectText;
    }

    public final String getPostProjectText() {
        return postProjectText;
    }

    public static RestConfiguration  getConfiguration(){
        return configuration;
    }

    public static void setConfiguration(RestConfiguration configuration){
        RestConfiguration.configuration=configuration;
    }


}
