package com.nikith_shetty.interrupt60;

/**
 * Created by Nikith_Shetty on 13/02/2017.
 */
//these constants are associated with PostsData
public class PostData{

    public interface DiplayAs{
        String ACTIVITY = "ACTIVITY";
        String WEBVIEW = "WEBVIEW";
        String NONE = "NONE";
    }

    //data members
    private String id;
    private String title;
    private String imgUrl;
    private String content;
    private String webLink;
    private String timestamp;
    private String displayAs;


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

    public static PostData createPost(String id, String imgurl, String title,
                                      String content, String disp, String web, String time){
        PostData n = new PostData();
        n.setId(id);
        n.setImgUrl(imgurl);
        n.setTitle(title);
        n.setContent(content);
        n.setDisplayAs(disp);
        n.setWebLink(web);
        n.setTimestamp(time);
        return n;
    }

    public String toString(){
        return "id: " + id + ", Title: " + title + ", Disp As: " + displayAs +
                ", ImgURL: " + imgUrl + ", Content: " + content +
                ", WebLink: " + webLink + ", Timestamp: " + timestamp;
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
    }public String getDisplayAs() {
        return displayAs;
    }public void    setDisplayAs(String displayAs) {
        this.displayAs = displayAs;
    }public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}