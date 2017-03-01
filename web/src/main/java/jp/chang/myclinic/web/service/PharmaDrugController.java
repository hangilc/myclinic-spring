package jp.chang.myclinic.web.service;

import jp.chang.myclinic.model.PharmaDrug;
import jp.chang.myclinic.model.PharmaDrugRepository;
import jp.chang.myclinic.web.service.json.JsonPharmaDrug;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by hangil on 2017/03/01.
 */

@RestController
@RequestMapping(value="/service", params="_q")
@Transactional(readOnly=true)
public class PharmaDrugController {
    @Autowired
    private PharmaDrugRepository pharmaDrugRepository;

    @RequestMapping(value="", method= RequestMethod.POST, params={"_q=enter_pharma_drug"})
    @Transactional()
    public int enterPharmaDrug(@RequestBody JsonPharmaDrug jsonPharmaDrug){
        PharmaDrug pharmaDrug = JsonPharmaDrug.toPharmaDrug(jsonPharmaDrug);
        pharmaDrugRepository.save(pharmaDrug);
        return pharmaDrug.getIyakuhincode();
    }

    @RequestMapping(value="", method= RequestMethod.GET, params={"_q=get_pharma_drug", "iyakuhincode"})
    public JsonPharmaDrug getPharmaDrug(@RequestParam("iyakuhincode") int iyakuhincode){
        PharmaDrug pharmaDrug = pharmaDrugRepository.findOne(iyakuhincode);
        return JsonPharmaDrug.fromPharmaDrug(pharmaDrug);
    }

    @RequestMapping(value="", method= RequestMethod.GET, params={"_q=search_pharma_drug", "text"})
    public List<Integer> searchIyakuhincodeByName(@RequestParam("text") String text){
        return pharmaDrugRepository.searchIyakuhincodeByName(text);
    }

}
