package jp.chang.myclinic.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.*;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/json")
@Transactional
public class RoujinController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/enter-roujin", method=RequestMethod.POST)
	public int enterRoujin(@RequestBody RoujinDTO roujinDTO){
		return dbGateway.enterRoujin(roujinDTO);
	}

	@RequestMapping(value="/delete-roujin", method=RequestMethod.POST)
	public boolean deleteRoujin(@RequestBody RoujinDTO roujinDTO){
		dbGateway.deleteRoujin(roujinDTO.roujinId);
		return true;
	}

	@RequestMapping(value="/find-available-roujin", method=RequestMethod.GET)
	public List<RoujinDTO> findAvailableRoujin(@RequestParam("patient-id") int patientId, @RequestParam("at") String atString){
		LocalDate at = LocalDate.parse(atString, DateTimeFormatter.ISO_LOCAL_DATE);
		return dbGateway.findAvailableRoujin(patientId, at);
	}

}