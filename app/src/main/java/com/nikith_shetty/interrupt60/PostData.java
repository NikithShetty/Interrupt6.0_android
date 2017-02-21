package com.nikith_shetty.interrupt60;

/**
 * Created by Nikith_Shetty on 13/02/2017.
 */
//these constants are associated with PostsData
public class PostData{

    public interface DiplayAs{
        int ACTIVITY = 20;
        int WEBVIEW = 22;
        int NONE = 24;
    }

    //data members
    private String title, imgUrl, content, webLink;
    private int displayAs;


    public PostData(){
        title = null;
        imgUrl = null;
        content = null;
        webLink = null;
        displayAs = DiplayAs.NONE;
    }

    public static PostData createAd(String url, String weblink){
        PostData n = new PostData();
        n.setImgUrl(url);
        if(weblink==null)
            n.setDisplayAs(DiplayAs.ACTIVITY);
        else {
            n.setDisplayAs(DiplayAs.WEBVIEW);
            n.setWebLink(weblink);
        }
        return n;
    }

    public static PostData createPost(String imgurl, String title, String content, String web, int disp){
        PostData n = new PostData();
        n.setImgUrl(imgurl);
        n.setTitle(title);
        n.setContent(content);
        n.setDisplayAs(disp);
        n.setWebLink(web);
        return n;
    }

    public String toString(){
        return "Title: " + title + ", ImgURL: " + imgUrl + ", Content: "
                + content + ", WebLink: " + webLink;
    }

    //getters and setters
    public  String  getTitle() {
        return title;
    }public void    setTitle(String title) {
        this.title = title;
    }public String  getImgUrl() {
        return imgUrl;
    }public void    setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }public String  getContent() {
        return content;
    }public void    setContent(String content) {
        this.content = content;
    }public String  getWebLink() {
        return webLink;
    }public void    setWebLink(String webLink) {
        this.webLink = webLink;
    }public int     getDisplayAs() {
        return displayAs;
    }public void    setDisplayAs(int displayAs) {
        this.displayAs = displayAs;
    }
}