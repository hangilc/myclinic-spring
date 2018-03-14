package jp.chang.myclinic.server.rest;

import jp.chang.myclinic.dto.ReferItemDTO;
import jp.chang.myclinic.server.ReferList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/json")
public class ReferController {

    private static Logger logger = LoggerFactory.getLogger(ReferController.class);

    @Autowired
    private ReferList referList;

    @RequestMapping(value="/get-refer-list", method= RequestMethod.GET)
    public List<ReferItemDTO> getReferList(){
        if( referList != null ) {
            return referList.getList().stream().map(this::toDTO).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private ReferItemDTO toDTO(ReferList.ReferItem item){
        ReferItemDTO dto = new ReferItemDTO();
        dto.hospital = item.getHospital();
        dto.section = item.getSection();
        dto.doctor = item.getDoctor();
        return dto;
    }

}
