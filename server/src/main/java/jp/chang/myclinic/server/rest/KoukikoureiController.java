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
public class KoukikoureiController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/enter-koukikourei", method=RequestMethod.POST)
	public int enterKoukikourei(@RequestBody KoukikoureiDTO koukikoureiDTO){
		if( koukikoureiDTO.koukikoureiId != 0 ){
			throw new RuntimeException("Invalid koukikooureiId (should be zero).");
		}
		return dbGateway.enterKoukikourei(koukikoureiDTO);
	}

	@RequestMapping(value="/update-koukikourei", method=RequestMethod.POST)
	public boolean updateKoukikourei(@RequestBody KoukikoureiDTO koukikoureiDTO){
		if( koukikoureiDTO.koukikoureiId == 0 ){
			throw new RuntimeException("Invalid koukikooureiId (should be non-zero).");
		}
		dbGateway.updateKoukikourei(koukikoureiDTO);
		return true;
	}

	@RequestMapping(value="/delete-koukikourei", method=RequestMethod.POST)
	public boolean deleteKoukikourei(@RequestBody KoukikoureiDTO koukikoureiDTO){
		dbGateway.deleteKoukikourei(koukikoureiDTO.koukikoureiId);
		return true;
	}

	@RequestMapping(value="/find-available-koukikourei", method=RequestMethod.GET)
	public List<KoukikoureiDTO> findAvailableKoukikourei(@RequestParam("patient-id") int patientId, @RequestParam("at") String atString){
		LocalDate at = LocalDate.parse(atString, DateTimeFormatter.ISO_LOCAL_DATE);
		return dbGateway.findAvailableKoukikourei(patientId, at);
	}

	@RequestMapping(value="get-koukikourei", method=RequestMethod.GET)
	public KoukikoureiDTO getKoukikourei(@RequestParam("koukikourei-id") int koukikoureiId){
		return dbGateway.getKoukikourei(koukikoureiId);
	}

}