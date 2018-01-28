package jp.chang.myclinic.dto;

public class VisitPatientDTO {
	public VisitDTO visit;
	public PatientDTO patient;

	@Override
	public String toString() {
		return "VisitPatientDTO{" +
				"visit=" + visit +
				", patient=" + patient +
				'}';
	}
}