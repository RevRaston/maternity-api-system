package uk.ac.plymouth.maternityapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Allocation {

    private int id;

    @JsonProperty("admissionID")
    private int admissionId;

    @JsonProperty("employeeID")
    private int employeeId;

    @JsonProperty("roomID")
    private int roomId;

    private String startTime;
    private String endTime;

    public Allocation() {
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

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}