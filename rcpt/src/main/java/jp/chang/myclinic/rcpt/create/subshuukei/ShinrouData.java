package jp.chang.myclinic.rcpt.create.subshuukei;

import java.time.LocalDate;

class ShinrouData {

    private LocalDate visitedAt;

    ShinrouData(LocalDate visitedAt) {
        this.visitedAt = visitedAt;
    }

    public LocalDate getVisitedAt() {
        return visitedAt;
    }
}
