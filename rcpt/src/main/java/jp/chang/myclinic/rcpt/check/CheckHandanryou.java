package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;

import java.util.*;

class CheckHandanryou extends CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckHandanryou.class);
    private Map<Integer, String> handanryouMap;  // shinryoucode -> name

    CheckHandanryou(Scope scope) {
        super(scope);
        this.handanryouMap = getHandanryouMap(getShinryouMaster());
    }

    private Map<Integer, String> getHandanryouMap(ResolvedShinryouMap sm) {
        Map<Integer, String> result = new LinkedHashMap<>();
        result.put(sm.尿便検査判断料, "尿便検査判断料");
        result.put(sm.血液検査判断料, "血液検査判断料");
        result.put(sm.生化Ⅰ判断料, "生化Ⅰ判断料");
        result.put(sm.生化Ⅱ判断料, "生化Ⅱ判断料");
        result.put(sm.免疫検査判断料, "免疫検査判断料");
        result.put(sm.微生物検査判断料, "微生物検査判断料");
        return result;
    }

    void check(boolean fixit) {
        Map<Integer, Integer> map = new HashMap<>(); // shinryoucode -> count
        Set<Integer> handanryouShinryoucodes = handanryouMap.keySet();
        forEachShinryouInVisits(s -> {
            int shinryoucode = s.shinryou.shinryoucode;
            if (handanryouShinryoucodes.contains(shinryoucode)) {
                map.merge(shinryoucode, 1, (a, b) -> a + b);
            }
        });
        List<Integer> problemShinryoucodes = new ArrayList<>();
        map.forEach((shinryoucode, count) -> {
            if (count > 1) {
                problemShinryoucodes.add(shinryoucode);
            }
        });
        for (int problemShinryoucode : problemShinryoucodes) {
            String name = handanryouMap.get(problemShinryoucode);
            error(name + "重複", fixit, () ->
                removeExtraShinryouMasterInVisits(problemShinryoucode, 1)
            );
        }
    }

}
