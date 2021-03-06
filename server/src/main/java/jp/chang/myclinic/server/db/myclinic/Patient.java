package jp.chang.myclinic.server.db.myclinic;

import javax.persistence.*;

@Entity
@Table(name="patient")
class Patient {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="patient_id")
	private Integer patientId;

	@Column(name="last_name")
	private String lastName;

	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name_yomi")
	private String lastNameYomi;

	@Column(name="first_name_yomi")
	private String firstNameYomi;

	private String sex;

	@Column(name="birth_day")
	private String birthday;

	private String address;
	private String phone;

	public Integer getPatientId(){
		return patientId;
	}

	public void setPatientId(Integer patientId){
		this.patientId = patientId;
	}

	public String getLastName(){
		return lastName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getFirstName(){
		return firstName;
	}

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}

	public String getLastNameYomi(){
		return lastNameYomi;
	}

	public void setLastNameYomi(String lastNameYomi){
		this.lastNameYomi = lastNameYomi;
	}

	public String getFirstNameYomi(){
		return firstNameYomi;
	}

	public void setFirstNameYomi(String firstNameYomi){
		this.firstNameYomi = firstNameYomi;
	}

	public String getSex(){
		return sex;
	}

	public void setSex(String sex){
		this.sex = sex;
	}

	public String getBirthday(){
		return birthday;
	}

	public void setBirthday(String birthday){
		this.birthday = birthday;
	}

	public String getAddress(){
		return address;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getPhone(){
		return phone;
	}

	public void setPhone(String phone){
		this.phone = phone;
	}

	@Override
	public String toString(){
		return "Patient[" +
			"patientId=" + patientId + ", " +
			"lastName=" + lastName + ", " +
			"firstName=" + firstName + ", " +
			"lastNameYomi=" + lastNameYomi + ", " +
			"firstNameYomi=" + firstNameYomi + ", " +
			"sex=" + sex + ", " +
			"birthday=" + birthday + ", " +
			"address=" + address + ", " +
			"phone=" + phone + 
			"]";
	}
}