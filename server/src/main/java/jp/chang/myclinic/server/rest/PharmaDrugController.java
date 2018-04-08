package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.server.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.PharmaDrugDTO;
import jp.chang.myclinic.dto.PharmaDrugNameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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
    public List<PharmaDrugDTO> collectPharmaDrugByIyakuhincodes(@RequestParam(value="iyakuhincode[]", required=false) List<Integer> iyakuhincodes){
        if( iyakuhincodes == null ){
            iyakuhincodes = Collections.emptyList();
        }
        return dbGateway.collectPharmaDrugByIyakuhincodes(iyakuhincodes);
    }

    @RequestMapping(value="/enter-pharma-drug", method=RequestMethod.POST)
    public boolean enterPharmaDrug(@RequestBody PharmaDrugDTO pharmaDrugDTO){
        dbGateway.enterPharmaDrug(pharmaDrugDTO);
        return true;
    }

    @RequestMapping(value="/update-pharma-drug", method=RequestMethod.POST)
    public boolean updatePharmaDrug(@RequestBody PharmaDrugDTO pharmaDrugDTO){
        dbGateway.updatePharmaDrug(pharmaDrugDTO);
        return true;
    }

    @RequestMapping(value="/deleteById-pharma-drug", method=RequestMethod.POST)
    public boolean deletePharmaDrug(@RequestParam("iyakuhincode") int iyakuhincode){
        dbGateway.deletePharmaDrug(iyakuhincode);
        return true;
    }

    @RequestMapping(value="/search-pharma-drug-names", method=RequestMethod.GET)
    public List<PharmaDrugNameDTO> searchPharmaDrugNames(@RequestParam("text") String text){
        return dbGateway.searchPharmaDrugNames(text);
    }

    @RequestMapping(value="/list-all-pharma-drug-names", method=RequestMethod.GET)
    public List<PharmaDrugNameDTO> listAllPharmaDrugNames(){
        return dbGateway.listAllPharmaDrugNames();
    }

}
