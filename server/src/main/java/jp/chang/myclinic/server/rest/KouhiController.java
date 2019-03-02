package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dbgateway.DbGatewayInterface;
import jp.chang.myclinic.dto.KouhiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/json")
@Transactional
public class KouhiController {

	@Autowired
	private DbGatewayInterface dbGateway;

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