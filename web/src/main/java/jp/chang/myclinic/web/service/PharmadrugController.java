package jp.chang.myclinic.web.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hangil on 2017/03/01.
 */

@RestController
@RequestMapping(value="/service", params="_q")
@Transactional(readOnly=true)

public class PharmadrugController {

}
