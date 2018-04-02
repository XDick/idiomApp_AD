package com.example.administrator.havingdate;

import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2018/3/16.
 */

public class FamousPeople extends DataSupport {
    private String title;
    private String  imageUrl="default";
    private  String pre;
    private String content;
    private String love;


    public FamousPeople(String title, String pre , String imageUrl, String content, String love){
        this.title =title;
        this.pre= pre;
        this.imageUrl = imageUrl;
        this.content = content;
        this.love =love;
    }

    public FamousPeople(String like){

        this.love=like;
    }

    public FamousPeople(){}

    public String getImageUrl() {
        return imageUrl;
    }
    public String getTitle() {
        return title;
    }
    public String getBody(){ return pre; }
    public String getContent(){return content;}
    public String getLove(){return love;}

    public void setLove(String like){this.love =like;}

}
