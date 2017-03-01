package jp.chang.myclinic.web.service;

import jp.chang.myclinic.model.IyakuhinMaster;
import jp.chang.myclinic.model.IyakuhinMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by hangil on 2017/03/01.
 */

@RestController
@RequestMapping(value="/service", params="_q")
@Transactional(readOnly=true)

public class IyakuhinMasterController {

    @Autowired
    IyakuhinMasterRepository iyakuhinMasterRepository;

    @RequestMapping(value="", method= RequestMethod.GET, params={"_q=search_most_recent_iyakuhin_master", "text"})
    public List<IyakuhinMaster> searchMostRecentIyakuhinMaster(@RequestParam(value="text") String text){
        text = text.trim();
        text = text.replace("\u3000", " "); // zenkaku space
        String[] words = text.split("\\s", 2);
        if( words.length == 1 ){
            return iyakuhinMasterRepository.searchByName(text);
        } else {
            return iyakuhinMasterRepository.searchByName(words[0], words[1]);
        }
    }

    @RequestMapping(value="", method= RequestMethod.POST, params={"_q=batch_get_most_recent_iyakuhin_master"})
    public List<IyakuhinMaster> batchGetMostRecentIyakuhinMaster(@RequestBody BodyIyakuhincodes body){
        return body.getIyakuhincodes().stream()
                .map(code -> iyakuhinMasterRepository.findTopByIyakuhincodeOrderByValidFromDesc(code))
                .collect(Collectors.toList());
    }
}

class BodyIyakuhincodes {
    public List<Integer> getIyakuhincodes() {
        return iyakuhincodes;
    }

    public void setIyakuhincodes(List<Integer> iyakuhincodes) {
        this.iyakuhincodes = iyakuhincodes;
    }

    private List<Integer> iyakuhincodes;
}
