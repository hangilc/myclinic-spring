package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.TextDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/json")
@Transactional
class TextController {
    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/update-text", method= RequestMethod.POST)
    public boolean updateText(@RequestBody TextDTO textDTO){
        dbGateway.updateText(textDTO);
        return true;
    }

}
