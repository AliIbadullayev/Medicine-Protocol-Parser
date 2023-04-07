package org.testorg.client.pojo;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

public class Observation {
    private Long id;
    private Long patient_id;
    private int ecg;
    private int pulse;
    private int sat;
    private float co2_insp;
    private float co2_exp;
    private Timestamp observation_time;

    public Timestamp getObservation_time() {
        return observation_time;
    }

    public void setObservation_time(Timestamp observation_time) {
        this.observation_time = observation_time;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(Long patient_id) {
        this.patient_id = patient_id;
    }

    public int getEcg() {
        return ecg;
    }

    public void setEcg(int ecg) {
        this.ecg = ecg;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getSat() {
        return sat;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }

    public float getCo2_insp() {
        return co2_insp;
    }

    public void setCo2_insp(float co2_insp) {
        this.co2_insp = co2_insp;
    }

    public float getCo2_exp() {
        return co2_exp;
    }

    public void setCo2_exp(float co2_exp) {
        this.co2_exp = co2_exp;
    }


    @Override
    public String toString() {
        return "Observation{" +
                "id=" + id +
                ", patient_id=" + patient_id +
                ", ecg=" + ecg +
                ", pulse=" + pulse +
                ", sat=" + sat +
                ", co2_insp=" + co2_insp +
                ", co2_exp=" + co2_exp +
                '}';
    }
}
