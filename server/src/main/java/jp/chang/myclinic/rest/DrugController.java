package jp.chang.myclinic.db;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/json")
@Transactional
public class DrugController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/get-drug-full", method=RequestMethod.GET)
	public DrugFullDTO getDrugFull(@RequestParam("drug-id") int drugId){
		return dbGateway.getDrugFull(drugId);
	}

	@RequestMapping(value="/list-drug-full", method=RequestMethod.GET)
	public List<DrugFullDTO> listDrugFull(@RequestParam("visit-id") int visitId){
		return dbGateway.listDrugFull(visitId);
	}
}