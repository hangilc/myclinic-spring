package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class CheckChouki extends CheckBase {

    private static Logger logger = LoggerFactory.getLogger(CheckChouki.class);
    private static Pattern shohousenPattern = Pattern.compile("^処方せん料");
    private static String choukiName = "調基（その他）";

    CheckChouki(VisitFull2DTO visit, List<DiseaseFullDTO> diseases, boolean fixit) {
        super(visit, diseases, fixit);
    }

    void check(){
        int choukiCount = countShinryouByName(choukiName);
        if( shinryouExists(shohousenPattern) ){
            if( choukiCount > 0 ){
                error("処方せん料、調基の同時算定");
            }
        } else {
            if( countDrugs() == 0 ){
                if( choukiCount > 0 ){
                    error("調基請求不可");
                }
            } else {
                if( choukiCount > 1 ){
                    error("調基重複");
                    if( isFixit() ){
                        fixChoukiChoufuku();
                        info("FIXED");
                    }
                } else if( choukiCount == 0 ){
                    error("調基抜け");
                    if( isFixit() ){
                        fixChoukiNuke();
                        info("FIXED");
                    }
                }
            }
        }
    }

    private void fixChoukiChoufuku(){
        List<ShinryouDTO> shinryouList = getShinryouList().stream()
                .filter(s -> s.master.name.equals(choukiName))
                .map(s -> s.shinryou)
                .collect(Collectors.toList());
        assert shinryouList.size() > 1;
        for(int i = 1; i<shinryouList.size();i++){
            ShinryouDTO shinryou = shinryouList.get(i);
            try {
                boolean ok = Service.api.deleteShinryouCall(shinryou.shinryouId).execute().body();
                assert ok;
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private void fixChoukiNuke(){
        try {
            VisitDTO visit = getVisit();
            ShinryouMasterDTO master = Service.api.findShinryouMasterByNameCall(choukiName, visit.visitedAt)
                    .execute().body();
            assert master != null;
            ShinryouDTO shinryou = new ShinryouDTO();
            shinryou.visitId = visit.visitId;
            shinryou.shinryoucode = master.shinryoucode;
            int shinryouId = Service.api.enterShinryouCall(shinryou).execute().body();
            assert shinryouId > 0;
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
