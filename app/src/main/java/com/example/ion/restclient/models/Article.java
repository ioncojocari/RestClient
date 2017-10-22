package com.example.ion.restclient.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Arrays;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Article extends RealmObject implements Parcelable{

	public static final String OBJECT="ARticle_Object";
	public static final String ID_FIELD="id";
	public static final String TITLE_FIELD="title";
	public static final String CONTENT_FIELD="content";
	public static final String URL_FIELD="url";
	public static final String CREATED_FIELD="createdDate";
	public static final String UPDATED_FIELD="updatedDate";
	@PrimaryKey
	private long id;
	private String title;
	private String content;
	private String url;
	private long createdDate;
	private long updatedDate;

	public Article(){
		
	}
	
	public Article(String title,String content,String url){
		setTitle(title);
		setContent(content);
		setUrl(url);
		long time=System.currentTimeMillis();
		setCreatedDate(time);
		setUpdatedDate(0);
	}

	protected Article(Parcel in) {
		id = in.readLong();
		title = in.readString();
		content = in.readString();
		url = in.readString();
		createdDate = in.readLong();
		updatedDate = in.readLong();
	}

	public static final Creator<Article> CREATOR = new Creator<Article>() {
		@Override
		public Article createFromParcel(Parcel in) {
			return new Article(in);
		}

		@Override
		public Article[] newArray(int size) {
			return new Article[size];
		}
	};

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public long getCreatedDate(){
		return createdDate;
	}
	
	public void setCreatedDate(long time){
		this.createdDate=time;
	}
	
	public long getId(){
		return this.id;
	}

	public boolean isArticleValid(){
		if(this.id>-1&& 
				getTitle()!=null && !getTitle().isEmpty() &&
				getContent()!=null && !getContent().isEmpty() &&
				getUrl()!=null && !getUrl().isEmpty()){
			return true;
		}
		return false;
	}


	@Override
	public boolean equals(Object a){
		if(this.hashCode()==a.hashCode())
		if(a instanceof Article){
			Article article=(Article) a;
			if(article.isArticleValid() &&
					article.getTitle().equals(this.getTitle()) &&
					article.getContent().equals(this.getContent()) &&
					article.getUrl().equals(this.getUrl())
			){
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		Object[] usedFieldForHashing={title,content,url};
		return Arrays.hashCode(usedFieldForHashing);
	}
	
	public void setId(long id) {
		this.id=id;
	}

	@Override
	public String toString() {
		return "Article{id=" + id + ", title=" + title + ", content=" + content + ", url=" + url + ", createdDate="
				+ createdDate + "}";
	}

	public void setUpdatedDate(long updated) {
		this.updatedDate = updated;
	}

	public long getUpdatedDate(){
		return updatedDate;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(title);
		dest.writeString(content);
		dest.writeString(url);
		dest.writeLong(createdDate);
		dest.writeLong(updatedDate);
	}
}
