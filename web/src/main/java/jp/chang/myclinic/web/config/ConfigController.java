package jp.chang.myclinic.web.config;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hangil on 2017/03/01.
 */

@RestController
@RequestMapping(value="/config")
@Transactional(readOnly=true)

public class ConfigController {

    @RequestMapping(value="*", method= RequestMethod.GET)
    public Map<String,String> noConfig(){
        return new HashMap<String, String>();
    }
}
