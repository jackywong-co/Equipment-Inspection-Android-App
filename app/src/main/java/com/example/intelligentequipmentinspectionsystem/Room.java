package com.example.intelligentequipmentinspectionsystem;

import java.util.List;

public class Room {
    private String roomName;
    private String roomLocation;
    private String roomId;

    public Room() {
    }

    public Room(String roomName, String roomLocation, String roomId) {
        this.roomName = roomName;
        this.roomLocation = roomLocation;
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomLocation() {
        return roomLocation;
    }

    public void setRoomLocation(String roomLocation) {
        this.roomLocation = roomLocation;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
