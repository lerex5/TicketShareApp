package com.lerex.tr;

public class TicketDetails {

    private String name,date,cost,sellerId,buyerId,city,theatre,time,firebaseId;
    private int numberOfTickets,transactionMode;

    // 0 - Available , 1 - In Transaction , 2 - Sold , 3 - Inactive
    public TicketDetails(){
        this.numberOfTickets = -1;
        this.date = "00/00/00";
        this.cost = "-1";
        this.name = "-1";
        this.city = "-1";
        this.theatre = "-1";
        this.sellerId = "-1";
        this.transactionMode=0;
        this.firebaseId="-1";
        this.time="-1";
    }
    public TicketDetails(String name, String cost, String date,int numberOfTickets, String sellerId,String city,String theatre,String firebaseId,String time) {
        this.numberOfTickets = numberOfTickets;
        this.date = date;
        this.cost = cost;
        this.name = name;
        this.sellerId = sellerId;
        this.city=city;
        this.theatre=theatre;
        this.transactionMode=0;
        this.firebaseId=firebaseId;
        this.time=time;
    }

    public int getNumberOfTickets() {
        return numberOfTickets;
    }

    public String getDate() {
        return date;
    }

    public String getCost() {
        return cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getCity() {   return city;   }

    public void setCity(String city) {        this.city = city;    }

    public String getTheatre() {        return theatre;    }

    public void setTheatre(String theatre) {        this.theatre = theatre;    }

    public int getTransactionMode() {
        return transactionMode;
    }

    public void setTransactionMode(int transactionMode) {
        this.transactionMode = transactionMode;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
