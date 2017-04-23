package jp.chang.myclinic.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import jp.chang.myclinic.db.DbGateway;
import jp.chang.myclinic.dto.*;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/json")
@Transactional
public class PatientController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/get-patient", method=RequestMethod.GET)
	public PatientDTO getPatient(@RequestParam("patient-id") int patientId){
		return dbGateway.getPatient(patientId);
	}

	@RequestMapping(value="/enter-patient", method=RequestMethod.POST)
	public int enterPatient(@RequestBody PatientDTO patient){
		return dbGateway.enterPatient(patient);
	}

}