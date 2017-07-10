package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.DbGateway;
import jp.chang.myclinic.dto.PharmaDrugDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/json")
@Transactional
public class PharmaDrugController {

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/get-pharma-drug")
    public PharmaDrugDTO getPharmaDrug(@RequestParam("iyakuhincode") int iyakuhincode){
        return dbGateway.getPharmaDrugByIyakuhincode(iyakuhincode);
    }

    @RequestMapping(value="/find-pharma-drug", method= RequestMethod.GET)
    public Optional<PharmaDrugDTO> findPharmaDrug(@RequestParam("iyakuhincode") int iyakuhincode){
        return dbGateway.findPharmaDrugByIyakuhincode(iyakuhincode);
    }

    @RequestMapping(value="/collect-pharma-drug-by-iyakuhincodes", method=RequestMethod.GET)
    public List<PharmaDrugDTO> collectPharmaDrugByIyakuhincodes(@RequestParam("iyakuhincode[]") List<Integer> iyakuhincodes){
        return dbGateway.collectPharmaDrugByIyakuhincodes(iyakuhincodes);
    }

}
