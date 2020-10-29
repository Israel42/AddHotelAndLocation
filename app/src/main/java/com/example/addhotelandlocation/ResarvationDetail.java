package com.example.addhotelandlocation;

public class ResarvationDetail {
    String name, checkin, checkout, transactionid, roomsize, price, phonenumber, roomtype,hotelname;

    public ResarvationDetail(String name, String checkin, String checkout, String transactionid, String roomsize, String price, String phonenumber, String roomtype, String hotelname) {
        this.name = name;
        this.checkin = checkin;
        this.checkout = checkout;
        this.transactionid = transactionid;
        this.roomsize = roomsize;
        this.price = price;
        this.phonenumber = phonenumber;
        this.roomtype = roomtype;
        this.hotelname = hotelname;
    }

    public ResarvationDetail() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getRoomsize() {
        return roomsize;
    }

    public void setRoomsize(String roomsize) {
        this.roomsize = roomsize;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public String getHotelname() {
        return hotelname;
    }

    public void setHotelname(String hotelname) {
        this.hotelname = hotelname;
    }
}