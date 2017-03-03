package jp.chang.myclinic.web.service.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.chang.myclinic.model.Patient;
import jp.chang.myclinic.model.Wqueue;
import jp.chang.myclinic.model.WqueueState;

import java.sql.Date;

/**
 * Created by hangil on 2017/03/03.
 */
public class JsonFullWqueue {
    @JsonProperty("visit_id")
    private Integer visitId;

    public Integer getVisitId(){
        return visitId;
    }

    public void setVisitId(Integer visitId){
        this.visitId = visitId;
    }

    @JsonProperty("wait_state")
    private int waitState;

    public int getWaitState(){
        return waitState;
    }

    @JsonProperty("patient_id")
    private int patientId;

    public int getPatientId(){
        return patientId;
    }

    public void setPatientId(int patientId){
        this.patientId = patientId;
    }

    @JsonProperty("last_name")
    private String lastName;

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    private String firstName;

    @JsonProperty("first_name")
    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    @JsonProperty("last_name_yomi")
    private String lastNameYomi;

    public String getLastNameYomi(){
        return lastNameYomi;
    }

    public void setLastNameYomi(String lastNameYomi){
        this.lastNameYomi = lastNameYomi;
    }

    @JsonProperty("first_name_yomi")
    private String firstNameYomi;

    public String getFirstNameYomi(){
        return firstNameYomi;
    }

    public void setFirstNameYomi(String firstNameYomi){
        this.firstNameYomi = firstNameYomi;
    }

    private String sex;

    public String getSex(){
        return sex;
    }

    public void setSex(String sex){
        this.sex = sex;
    }

    @JsonProperty("birth_day")
    private Date birthday;

    public Date getBirthday(){
        return birthday;
    }

    public void setBirthday(Date birthday){
        this.birthday = birthday;
    }

    private String address;

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    private String phone;

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setWaitState(int waitState){
        this.waitState = waitState;
    }

    public static JsonFullWqueue fromWqueue(Wqueue src){
        JsonFullWqueue dst = new JsonFullWqueue();
        dst.setVisitId(src.getVisitId());
        dst.setWaitState(src.getWaitState().ordinal());
        Patient patient = src.getVisit().getPatient();
        dst.setPatientId(patient.getPatientId());
        dst.setLastName(patient.getLastName());
        dst.setFirstName(patient.getFirstName());
        dst.setLastNameYomi(patient.getLastNameYomi());
        dst.setFirstNameYomi(patient.getFirstNameYomi());
        dst.setBirthday(patient.getBirthday());
        dst.setSex(patient.getSex());
        dst.setAddress(patient.getAddress());
        dst.setPhone(patient.getPhone());
        return dst;
    }

}
