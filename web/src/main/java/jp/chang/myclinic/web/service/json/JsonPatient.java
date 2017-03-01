package jp.chang.myclinic.web.service.json;

import jp.chang.myclinic.model.Patient;
import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonPatient {

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

	public static JsonPatient fromPatient(Patient patient){
		JsonPatient json = new JsonPatient();
		json.setPatientId(patient.getPatientId());
		json.setLastName(patient.getLastName());
		json.setFirstName(patient.getFirstName());
		json.setLastNameYomi(patient.getLastNameYomi());
		json.setFirstNameYomi(patient.getFirstNameYomi());
		json.setBirthday(patient.getBirthday());
		json.setSex(patient.getSex());
		json.setAddress(patient.getAddress());
		json.setPhone(patient.getPhone());
		return json;
	}

}