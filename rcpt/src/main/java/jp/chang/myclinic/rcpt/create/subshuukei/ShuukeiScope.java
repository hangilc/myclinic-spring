package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.mastermap.generated.ResolvedShinryouMap;

import java.time.LocalDate;

class ShuukeiScope {

    ResolvedShinryouMap shinryouMasterMap;
    LocalDate visitedAt;

    ShuukeiScope(ResolvedShinryouMap shinryouMasterMap, LocalDate visitedAt) {
        this.shinryouMasterMap = shinryouMasterMap;
        this.visitedAt = visitedAt;
    }

}
