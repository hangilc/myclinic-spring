package jp.chang.myclinic.server.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import jp.chang.myclinic.server.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.*;

import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/json")
@Transactional
public class ShahokokuhoController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/enter-shahokokuho", method=RequestMethod.POST)
	public int enterShahokokuho(@RequestBody ShahokokuhoDTO shahokokuhoDTO){
		if( shahokokuhoDTO.shahokokuhoId != 0 ){
			throw new RuntimeException("Invalid shahokokuhoId (should be zero).");
		}
		return dbGateway.enterShahokokuho(shahokokuhoDTO);
	}

	@RequestMapping(value="/update-shahokokuho", method=RequestMethod.POST)
	public boolean updateShahokokuho(@RequestBody ShahokokuhoDTO shahokokuhoDTO){
		if( shahokokuhoDTO.shahokokuhoId == 0 ){
			throw new RuntimeException("Invalid shahokokuhoId (should be non-zero).");
		}
		dbGateway.updateShahokokuho(shahokokuhoDTO);
		return true;
	}

	@RequestMapping(value="/find-available-shahokokuho", method=RequestMethod.GET)
	public List<ShahokokuhoDTO> findAvailableShahokokuho(@RequestParam("patient-id") int patientId, @RequestParam("at") String atString){
		LocalDate at = LocalDate.parse(atString, DateTimeFormatter.ISO_LOCAL_DATE);
		return dbGateway.findAvailableShahokokuho(patientId, at);
	}

	@RequestMapping(value="/delete-shahokokuho", method=RequestMethod.POST)
	public boolean deleteShahokokuho(@RequestBody ShahokokuhoDTO shahokokuhoDTO){
		dbGateway.deleteShahokokuho(shahokokuhoDTO.shahokokuhoId);
		return true;
	}

	@RequestMapping(value="get-shahokokuho", method=RequestMethod.GET)
	public ShahokokuhoDTO getShahokokuho(@RequestParam("shahokokuho-id") int shahokokuhoId){
		return dbGateway.getShahokokuho(shahokokuhoId);
	}

}