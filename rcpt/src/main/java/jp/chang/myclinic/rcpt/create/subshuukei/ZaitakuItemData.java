package jp.chang.myclinic.rcpt.create.subshuukei;

import java.time.LocalDate;

class ZaitakuItemData extends ShinryouItemData {

    private LocalDate visitedAt;

    ZaitakuItemData(String name, LocalDate visitedA) {
        super(name);
        this.visitedAt = visitedA;
    }

    LocalDate getVisitedAt() {
        return visitedAt;
    }
}
