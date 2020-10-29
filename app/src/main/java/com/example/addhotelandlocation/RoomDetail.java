package com.example.addhotelandlocation;

import com.google.firebase.firestore.Exclude;

public class RoomDetail {
    String roomimage,roomsize,roomtype;
    int roomprice;

    public RoomDetail(String roomimage, String roomsize, String roomtype, int roomprice) {
        this.roomimage = roomimage;
        this.roomsize = roomsize;
        this.roomtype = roomtype;
        this.roomprice = roomprice;
    }

    public RoomDetail() {
    }

    public String getRoomimage() {
        return roomimage;
    }

    public void setRoomimage(String roomimage) {
        this.roomimage = roomimage;
    }
    @Exclude
    public String getRoomsize() {
        return roomsize;
    }
    @Exclude
    public void setRoomsize(String roomsize) {
        this.roomsize = roomsize;
    }

    public String getRoomtype() {
        return roomtype;
    }

    public void setRoomtype(String roomtype) {
        this.roomtype = roomtype;
    }

    public int getRoomprice() {
        return roomprice;
    }

    public void setRoomprice(int roomprice) {
        this.roomprice = roomprice;
    }
}
