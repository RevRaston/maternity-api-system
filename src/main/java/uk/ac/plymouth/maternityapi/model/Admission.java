package uk.ac.plymouth.maternityapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Admission {

    private int id;
    private String admissionDate;
    private String dischargeDate;

    @JsonProperty("patientID")
    private int patientId;

    public Admission() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(String dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
}