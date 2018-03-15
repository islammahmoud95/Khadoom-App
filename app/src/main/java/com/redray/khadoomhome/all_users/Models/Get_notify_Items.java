package com.redray.khadoomhome.all_users.Models;



public class Get_notify_Items {

    private String id;
    private String title;
    private String content;
    private String time_notify;
    private String type_Notify;
    private String redirect_id;



    public String getNotify_id() {
        return id;
    }

    public void setNotify_id(String id) {
        this.id = id;
    }


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


    public String getNotify_Type() {
        return type_Notify;
    }

    public void setNotify_Type(String type_Notify) {
        this.type_Notify = type_Notify;
    }


    public String getRedirect_ID() {
        return redirect_id;
    }

    public void setRedirect_ID(String redirect_id) {
        this.redirect_id = redirect_id;
    }

    public String getTime_notify() {
        return time_notify;
    }

    public void setTime_notify(String time_notify) {
        this.time_notify = time_notify;
    }


}
