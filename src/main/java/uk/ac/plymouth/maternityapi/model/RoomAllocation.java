package uk.ac.plymouth.maternityapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomAllocation {

    private int id;

    @JsonProperty("admissionID")
    private int admissionId;

    @JsonProperty("roomNumber")
    private String roomNumber;

    public RoomAllocation() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(int admissionId) {
        this.admissionId = admissionId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}