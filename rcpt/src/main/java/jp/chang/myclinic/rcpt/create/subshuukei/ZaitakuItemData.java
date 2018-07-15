package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Shinryou;

import java.time.LocalDate;

class ZaitakuItemData extends ShinryouItemData {

    private LocalDate visitedAt;

    ZaitakuItemData(Shinryou shinryou, LocalDate visitedA) {
        super(shinryou);
        this.visitedAt = visitedA;
    }

    LocalDate getVisitedAt() {
        return visitedAt;
    }
}
