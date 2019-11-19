package com.lerex.tr;

public class TicketDetails {

    private String name,date,cost,sellerId,buyerId;
    private int numberOfTickets;
    public TicketDetails(){
        this.numberOfTickets = -1;
        this.date = "00/00/00";
        this.cost = "-1";
        this.name = "-1";
        this.sellerId = "-1";
    }
    public TicketDetails(String name, String date, String cost,int numberOfTickets, String sellerId) {
        this.numberOfTickets = numberOfTickets;
        this.date = date;
        this.cost = cost;
        this.name = name;
        this.sellerId = sellerId;
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
}
