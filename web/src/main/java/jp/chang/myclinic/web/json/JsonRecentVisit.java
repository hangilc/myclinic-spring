package jp.chang.myclinic.web.json;

import jp.chang.myclinic.model.Visit;
import jp.chang.myclinic.model.Patient;

public class JsonRecentVisit {
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

	public int visitId;

	public int getVisitId(){
		return visitId;
	}	

	public void setVisitId(int visitId){
		this.visitId = visitId;
	}

	public static JsonRecentVisit fromVisit(Visit visit){
		Patient patient = visit.getPatient();
		JsonRecentVisit json = new JsonRecentVisit();
		json.setPatientId(patient.getPatientId());
		json.setLastName(patient.getLastName());
		json.setFirstName(patient.getFirstName());
		json.setLastNameYomi(patient.getLastNameYomi());
		json.setFirstNameYomi(patient.getFirstNameYomi());
		json.setVisitId(visit.getVisitId());
		return json;
	}

}