package jp.chang.myclinic.web.json;

import jp.chang.myclinic.model.Patient;
import java.sql.Date;

public class JsonPatient {
	private int patientId;

	public int getPatientId(){
		return patientId;
	}

	public void setPatientId(int patientId){
		this.patientId = patientId;
	}

	private String lastName;

	public String getLastName(){
		return lastName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	private String firstName;

	public String getFirstName(){
		return firstName;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	private String lastNameYomi;

	public String getLastNameYomi(){
		return lastNameYomi;
	}

	public void setLastNameYomi(String lastNameYomi){
		this.lastNameYomi = lastNameYomi;
	}

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
		json.setAddress(patient.getAddress());
		json.setPhone(patient.getPhone());
		return json;
	}

}