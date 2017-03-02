package jp.chang.myclinic.web.service.json;

import jp.chang.myclinic.model.Visit;
import jp.chang.myclinic.model.Patient;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonSearchVisitResult {

	@JsonProperty("patient_id")
	private int patientId;

	public int getPatientId(){
		return patientId;
	}

	public void setPatientId(int patientId){
		this.patientId = patientId;
	}

	private String lastName;

	@JsonProperty("last_name")
	public String getLastName(){
		return lastName;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	@JsonProperty("first_name")
	private String firstName;

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

	@JsonProperty("visit_id")
	public int visitId;

	public int getVisitId(){
		return visitId;
	}	

	public void setVisitId(int visitId){
		this.visitId = visitId;
	}

	public static JsonSearchVisitResult fromVisit(Visit visit){
		Patient patient = visit.getPatient();
		JsonSearchVisitResult json = new JsonSearchVisitResult();
		json.setPatientId(patient.getPatientId());
		json.setLastName(patient.getLastName());
		json.setFirstName(patient.getFirstName());
		json.setLastNameYomi(patient.getLastNameYomi());
		json.setFirstNameYomi(patient.getFirstNameYomi());
		json.setVisitId(visit.getVisitId());
		return json;
	}

}