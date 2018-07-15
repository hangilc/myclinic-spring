package jp.chang.myclinic.rcpt.create.subshuukei;

import jp.chang.myclinic.rcpt.create.Shinryou;

import java.time.LocalDate;

class ShidouItemData extends ShinryouItemData {

    private LocalDate visitedAt;

    ShidouItemData(Shinryou shinryou, LocalDate visitedAt) {
        super(shinryou);
        this.visitedAt = visitedAt;
    }

    LocalDate getVisitedAt() {
        return visitedAt;
    }
}
