package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.DbGateway;
import jp.chang.myclinic.dto.HotlineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/json")
@Transactional
public class HotlineController {

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/get-last-hotline-id", method=RequestMethod.GET)
    public int getLastHotlineId(){
        return dbGateway.getLastHotlineId();
    }

    @RequestMapping(value="/list-hotline-in-range", method=RequestMethod.GET)
    public List<HotlineDTO> listHotlineInRange(@RequestParam("lower-hotline-id") int lowerHotlineId,
                                               @RequestParam("upper-hotline-id") int upperHotlineId){
        return dbGateway.listHotlineInRange(lowerHotlineId, upperHotlineId);
    }

    @RequestMapping(value="/enter-hotline", method=RequestMethod.POST)
    public int enterHotline(@RequestBody HotlineDTO hotlineDTO){
        return dbGateway.enterHotline(hotlineDTO);
    }

    @RequestMapping(value="/list-todays-hotline", method=RequestMethod.GET)
    public List<HotlineDTO> listTodaysHotline(){
        return dbGateway.listTodaysHotline();
    }

    @RequestMapping(value="/list-recent-hotline", method=RequestMethod.GET)
    public List<HotlineDTO> listRecentHotline(@RequestParam("threshold-hotline-id") int thresholdHotlineId){
        return dbGateway.listRecentHotline(thresholdHotlineId);
    }
}
