package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.dto.VisitFull2DTO;
import jp.chang.myclinic.rcpt.Masters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

class CheckHandanryou extends CheckBase {

    private static Logger logger = LoggerFactory.getLogger(CheckHandanryou.class);
    private List<VisitFull2DTO> visits;
    private Masters masters;
    private Map<Integer, ShinryouMasterDTO> handanryouMap;  // shinryoucode -> Map

    CheckHandanryou(List<VisitFull2DTO> visits, Masters masters) {
        this.visits = visits;
        this.handanryouMap = getHandanryouMap(masters);
    }

    private Map<Integer, ShinryouMasterDTO> getHandanryouMap(Masters masters) {
        Map<Integer, ShinryouMasterDTO> result = new LinkedHashMap<>();
        result.put(masters.尿便検査判断料.shinryoucode, masters.尿便検査判断料);
        result.put(masters.血液検査判断料.shinryoucode, masters.血液検査判断料);
        result.put(masters.生化Ⅰ判断料.shinryoucode, masters.生化Ⅰ判断料);
        result.put(masters.生化Ⅱ判断料.shinryoucode, masters.生化Ⅱ判断料);
        result.put(masters.免疫検査判断料.shinryoucode, masters.免疫検査判断料);
        result.put(masters.微生物検査判断料.shinryoucode, masters.微生物検査判断料);
        return result;
    }

    void check(boolean fixit) throws Exception {
        Map<Integer, Integer> map = new HashMap<>(); // shinryoucode -> count
        Set<Integer> handanryouShinryoucodes = handanryouMap.keySet();
        Helper.forEachShinryouInVisits(visits, s -> {
            int shinryoucode = s.shinryou.shinryoucode;
            if( handanryouShinryoucodes.contains(shinryoucode) ){
                map.merge(shinryoucode, 1, (a, b) -> a + b);
            }
        });
        List<Integer> problemShinryoucodes = new ArrayList<>();
        map.forEach((shinryoucode, count) -> {
           if( count > 1 ){
               problemShinryoucodes.add(shinryoucode);
           }
        });
        for(int problemShinryoucode: problemShinryoucodes){
            ShinryouMasterDTO master = handanryouMap.get(problemShinryoucode);
            assert master != null;
            error(master.name + "重複");
            if( fixit ){
                Helper.removeExtraShinryouFromVisits(visits, master, 1);
                info("FIXTED");
            }
        }
    }


}
