package com.example.addhotelandlocation;

public class HotelDetail {
    String hotelname;
    String address ;
    String imageurl;
    float rating;
public HotelDetail(){

}

    public HotelDetail(String hotelname, String address, String imageurl, float rating) {
        this.hotelname = hotelname;
        this.address = address;
        this.imageurl = imageurl;
        this.rating = rating;
    }

    public String getHotelname() {
        return hotelname;
    }

    public void setHotelname(String hotelname) {
        this.hotelname = hotelname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
