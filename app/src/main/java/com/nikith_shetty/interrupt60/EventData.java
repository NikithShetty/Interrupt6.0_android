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
        this.event_id = null;
        this.event_name = null;
        this.imgUrl = null;
        this.event_desc = null;
        this.venue = null;
        this.fee = null;
        this.dateTime = null;
        this.contact = null;
    }

    public static EventData createEvent(String id, String name, String imgurl, String desc, String dT,
                     String venue, String f, String con){
        EventData n = new EventData();
        n.setEvent_id(id);
        n.setEvent_name(name);
        n.setImgUrl(imgurl);
        n.setEvent_desc(desc);
        n.setVenue(venue);
        n.setFee(f);
        n.setDateTime(dT);
        n.setContact(con);
        return n;
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
