package com.example.foodapp.Domain;

public class OrderFood {
//    private String food;
    private String price;
    private String date;
    public OrderFood(){}
    public OrderFood(String date, String price){
//        this.food=food;
        this.price=price;
        this.date = date;

    }
//    public String getFood() {
//        return food;
//    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }
}
