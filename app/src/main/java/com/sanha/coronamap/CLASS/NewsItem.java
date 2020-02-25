package com.sanha.coronamap.CLASS;
public class NewsItem {

    private String mName;
    private String mLink;

    public NewsItem(String name, String link) {

        this.mName = name;
        this.mLink = link;
    }

    public String getLink() {

        return mLink;
    }

    public void setLink(String link) {

        this.mLink = link;
    }

    public String getName() {

        return mName;
    }

    public void setName(String name) {

        this.mName = name;
    }

    @Override
    public String toString() {

        return this.mName;
    }
}
