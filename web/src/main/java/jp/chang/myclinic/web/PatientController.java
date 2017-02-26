package jp.chang.myclinic.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.chang.myclinic.web.json.JsonPatient;

class Patient {
	int patientId;
}

@RestController
@RequestMapping(value="/service", params="_q")
public class PatientController {

	@RequestMapping(value="", method=RequestMethod.GET, params={"_q=get_patient", "patient_id"})
	public JsonPatient getPatient(@RequestParam(value="patient_id") int patientId) {
		JsonPatient patient = new JsonPatient();
		patient.setPatientId(patientId);
		return patient;
	}

}
