package com.nikith_shetty.interrupt60;

/**
 * Created by Nikith_Shetty on 13/02/2017.
 */

public class EventData {

    private String event_id;
    private String event_name;
    private String imgUrl;
    private String event_desc;
    private String venue;
    private String dateTime;
    private String fee;
    private String contact;

    public EventData(){
    }

    public EventData(String id, String name, String imgurl, String desc, String venue, String f, String dT, String con){
        this.event_id = id;
        this.event_name = name;
        this.imgUrl = imgurl;
        this.event_desc = desc;
        this.venue = venue;
        this.fee = f;
        this.dateTime = dT;
        this.contact = con;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEvent_desc() {
        return event_desc;
    }

    public void setEvent_desc(String event_desc) {
        this.event_desc = event_desc;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
