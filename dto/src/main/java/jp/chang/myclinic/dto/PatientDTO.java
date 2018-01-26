package jp.chang.myclinic.dto;

import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PatientDTO that = (PatientDTO) o;
		return patientId == that.patientId &&
				Objects.equals(lastName, that.lastName) &&
				Objects.equals(firstName, that.firstName) &&
				Objects.equals(lastNameYomi, that.lastNameYomi) &&
				Objects.equals(firstNameYomi, that.firstNameYomi) &&
				Objects.equals(birthday, that.birthday) &&
				Objects.equals(sex, that.sex) &&
				Objects.equals(address, that.address) &&
				Objects.equals(phone, that.phone);
	}

	@Override
	public int hashCode() {

		return Objects.hash(patientId, lastName, firstName, lastNameYomi, firstNameYomi, birthday, sex, address, phone);
	}
}