package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dbgateway.DbGatewayInterface;
import jp.chang.myclinic.dto.RoujinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/json")
@Transactional
public class RoujinController {

	@Autowired
	private DbGatewayInterface dbGateway;

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

	@RequestMapping(value="/get-roujin", method=RequestMethod.GET)
	public RoujinDTO getRoujin(@RequestParam("roujin-id") int roujinId){
		return dbGateway.getRoujin(roujinId);
	}

}