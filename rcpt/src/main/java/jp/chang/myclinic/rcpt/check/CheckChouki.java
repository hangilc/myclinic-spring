package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.dto.VisitFull2DTO;

class CheckChouki extends CheckBase {

    CheckChouki(Scope scope) {
        super(scope);
    }

    void check(boolean fixit){
        int choukiCount = countShinryouMasterInVisits(getShinryouMaster().調基);
        int shohousenCount = countShohousenGroupInVisits();
        if( shohousenCount > 0 ){
            if( choukiCount > 0 ) {
                error("処方せん料、調基の同時算定");
            }
        } else {
            if (countDrugInVisits(d -> true) == 0) {
                if (choukiCount > 0) {
                    error("調基請求不可", fixit, () -> {
                        removeExtraShinryouMasterInVisits(getShinryouMaster().調基, 0);
                    });
                }
            } else {
                if (choukiCount > 1) {
                    error("調基重複", fixit, () -> {
                        removeExtraShinryouMasterInVisits(getShinryouMaster().調基, 1);
                    });
                } else if (choukiCount == 0) {
                    error("調基抜け", fixit, () -> {
                        VisitFull2DTO visit = findVisit(v -> v.drugs.size() > 0);
                        enterShinryou(visit, getShinryouMaster().調基);
                    });
                }
            }
        }
    }

}
