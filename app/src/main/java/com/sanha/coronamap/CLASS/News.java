package com.sanha.coronamap.CLASS;

public class News {
    public String newsContent;
    public String newsDate;
    public String newsLink;

    public News(){

    }
    public News(String ct, String dt, String lk){
        newsContent = ct;
        newsDate    = dt;
        newsLink = lk;
    }

    public String getNewsContent(){
        return newsContent;
    }
    public String getNewsLink(){
        return newsLink;
    }
    public String getNewsDate(){
        return newsDate;
    }
    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public void setNewsLink(String newsLink) {
        this.newsLink = newsLink;
    }

    public void setNewsDate(String newsDate) {
        this.newsDate = newsDate;
    }
}
