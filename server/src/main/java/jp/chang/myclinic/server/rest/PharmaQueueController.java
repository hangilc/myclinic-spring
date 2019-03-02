package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dbgateway.DbGatewayInterface;
import jp.chang.myclinic.dto.PharmaQueueFullDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/json")
@Transactional
public class PharmaQueueController {

    @Autowired
    private DbGatewayInterface dbGateway;

    @RequestMapping(value="/list-pharma-queue-full-for-prescription", method= RequestMethod.GET)
    public List<PharmaQueueFullDTO> listPharmaQueueFullForPrescription(){
        return dbGateway.listPharmaQueueFullForPrescription();
    }

    @RequestMapping(value="/list-pharma-queue-full-for-today", method= RequestMethod.GET)
    public List<PharmaQueueFullDTO> listPharmaQueueFullForToday(){
        return dbGateway.listPharmaQueueFullForToday();
    }

    @RequestMapping(value="/get-pharma-queue-full", method=RequestMethod.GET)
    public PharmaQueueFullDTO getPharmaQueueFull(@RequestParam("visit-id") int visitId){
        return dbGateway.getPharmaQueueFull(visitId);
    }

    @RequestMapping(value="/try-delete-pharma-queue", method=RequestMethod.POST)
    public boolean tryDeletePharmaQueue(@RequestParam("visit-id") int visitId){
        dbGateway.findPharmaQueue(visitId).ifPresent(pharmaQueueDTO -> dbGateway.deletePharmaQueue(pharmaQueueDTO));
        return true;
    }
}
