package com.redray.khadoomhome.tickets.models;



public class Get_Tickets_Items {

    private String ticket_id;
    private String ticketName;
    private String ticket_desc;
    private String date;



    public String getTicket_Id() {
        return ticket_id;
    }

    public void setTicket_Id(String ticket_id) {
        this.ticket_id = ticket_id;
    }



    public String getTicket_Name() {
        return ticketName;
    }

    public void setTicket_Name(String ticket_name) {
        this.ticketName = ticket_name;
    }



    public String getTicket_desc() {
        return ticket_desc;
    }

    public void setTicket_desc(String ticket_desc) {
        this.ticket_desc = ticket_desc;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



}
