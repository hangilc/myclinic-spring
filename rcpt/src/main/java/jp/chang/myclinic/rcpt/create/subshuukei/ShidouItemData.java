package jp.chang.myclinic.rcpt.create.subshuukei;

import java.time.LocalDate;

class ShidouItemData extends ShinryouItemData {

    private LocalDate visitedAt;

    ShidouItemData(String name, LocalDate visitedAt) {
        super(name);
        this.visitedAt = visitedAt;
    }

    LocalDate getVisitedAt() {
        return visitedAt;
    }
}
