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
public class KouhiController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/enter-kouhi", method=RequestMethod.POST)
	public int enterKouhi(@RequestBody KouhiDTO kouhiDTO){
		if( kouhiDTO.kouhiId != 0 ){
			throw new RuntimeException("Invalid kouhiId (should be zero).");
		}
		return dbGateway.enterKouhi(kouhiDTO);
	}

	@RequestMapping(value="/update-kouhi", method=RequestMethod.POST)
	public boolean updateKouhi(@RequestBody KouhiDTO kouhiDTO){
		if( kouhiDTO.kouhiId == 0 ){
			throw new RuntimeException("Invalid kouhiId (should be non-zero).");
		}
		dbGateway.updateKouhi(kouhiDTO);
		return true;
	}

	@RequestMapping(value="/delete-kouhi", method=RequestMethod.POST)
	public boolean deleteKouhi(@RequestBody KouhiDTO kouhiDTO){
		dbGateway.deleteKouhi(kouhiDTO.kouhiId);
		return true;
	}

	@RequestMapping(value="/find-available-kouhi", method=RequestMethod.GET)
	public List<KouhiDTO> findAvailableKouhi(@RequestParam("patient-id") int patientId, @RequestParam("at") String atString){
		LocalDate at = LocalDate.parse(atString, DateTimeFormatter.ISO_LOCAL_DATE);
		return dbGateway.findAvailableKouhi(patientId, at);
	}

	@RequestMapping(value="get-kouhi", method=RequestMethod.GET)
	public KouhiDTO getKouhi(@RequestParam("kouhi-id") int kouhiId){
		return dbGateway.getKouhi(kouhiId);
	}

}