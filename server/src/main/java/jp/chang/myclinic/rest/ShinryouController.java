package jp.chang.myclinic.db;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.*;

@RestController
@RequestMapping("/json")
@Transactional
public class ShinryouController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/get-shinryou-full", method=RequestMethod.GET)
	public ShinryouFullDTO getShinryouFull(@RequestParam("shinryou-id") int shinryouId){
		return dbGateway.getShinryouFull(shinryouId);
	}

}