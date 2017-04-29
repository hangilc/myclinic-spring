package jp.chang.myclinic.dto;

public class PatientDTO {
	public int patientId;
	public String lastName;
	public String firstName;
	public String lastNameYomi;
	public String firstNameYomi;
	public String birthday;
	public String sex;
	public String address;
	public String phone;

	@Override
	public String toString(){
		return "PatientDTO[" +
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