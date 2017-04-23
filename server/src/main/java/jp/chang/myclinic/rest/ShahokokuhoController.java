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

@RestController
@RequestMapping("/json")
@Transactional
public class ShahokokuhoController {

	@Autowired
	private DbGateway dbGateway;

	@RequestMapping(value="/enter-shahokokuho", method=RequestMethod.POST)
	public int enterShahokokuho(@RequestBody ShahokokuhoDTO shahokokuhoDTO){
		return dbGateway.enterShahokokuho(shahokokuhoDTO);
	}

}