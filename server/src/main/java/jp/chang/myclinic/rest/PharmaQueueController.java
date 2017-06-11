package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.DbGateway;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by hangil on 2017/06/11.
 */
@RestController
@RequestMapping("/json")
@Transactional
public class PharmaQueueController {

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/list-pharma-queue-full-for-prescription", method= RequestMethod.GET)
    List<PharmaQueueFullDTO> listPharmaQueueFullForPrescription(){
        return dbGateway.listPharmaQueueFullForPrescription();
    }

    @RequestMapping(value="/list-pharma-queue-full-for-today", method= RequestMethod.GET)
    List<PharmaQueueFullDTO> listPharmaQueueFullForToday(){
        return dbGateway.listPharmaQueueFullForToday();
    }

}
