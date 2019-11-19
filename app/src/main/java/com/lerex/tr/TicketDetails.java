package com.lerex.tr;

public class TicketDetails {

    private int num;
    private String date,cost;

    public TicketDetails(int num, String date, String cost) {
        this.num = num;
        this.date = date;
        this.cost = cost;
    }

    public int getNum() {
        return num;
    }

    public String getDate() {
        return date;
    }

    public String getCost() {
        return cost;
    }
}
