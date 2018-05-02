package jp.chang.myclinic.rcpt.check;

class CheckChouki {

    /*
    private static Logger logger = LoggerFactory.getLogger(CheckChouki.class);
    private RcptUnit unit;
    private Masters masters;

    CheckChouki(RcptUnit unit, Masters masters) {
        this.unit = unit;
        this.masters = masters;
    }

    void check(boolean fixit) throws Exception {
        int choukiCount = countShinryouByShinryoucode(Masters.調基.shinryoucode);
        if (shohousenExists()) {
            if (choukiCount > 0) {
                error("処方せん料、調基の同時算定");
            }
        } else {
            if (countDrugs() == 0) {
                if (choukiCount > 0) {
                    error("調基請求不可");
                }
            } else {
                if (choukiCount > 1) {
                    error("調基重複");
                    if (isFixit()) {
                        fixChoukiChoufuku();
                        info("FIXED");
                    }
                } else if (choukiCount == 0) {
                    error("調基抜け");
                    if (isFixit()) {
                        fixChoukiNuke();
                        info("FIXED");
                    }
                }
            }
        }
    }

    private boolean shohousenExists(){
        return shinryouExists(s -> {
            int shinryoucode = s.master.shinryoucode;
            return shinryoucode == Masters.処方せん料.shinryoucode ||
                    shinryoucode == Masters.処方せん料７.shinryoucode;
        });
    }

    private void fixChoukiChoufuku() throws Exception {
        List<ShinryouDTO> choukiList = getShinryouList().stream()
                .filter(s -> s.master.shinryoucode == Masters.調基.shinryoucode)
                .map(s -> s.shinryou)
                .collect(Collectors.toList());
        assert choukiList.size() > 1;
        for (int i = 1; i < choukiList.size(); i++) {
            ShinryouDTO shinryou = choukiList.get(i);
            boolean ok = Service.api.deleteShinryouCall(shinryou.shinryouId).execute().body();
            assert ok;
        }
    }

    private void fixChoukiNuke() throws Exception {
        VisitDTO visit = getVisit();
        ShinryouDTO shinryou = new ShinryouDTO();
        shinryou.visitId = visit.visitId;
        shinryou.shinryoucode = Masters.調基.shinryoucode;
        int shinryouId = Service.api.enterShinryouCall(shinryou).execute().body();
        assert shinryouId > 0;
    }
    */
}
