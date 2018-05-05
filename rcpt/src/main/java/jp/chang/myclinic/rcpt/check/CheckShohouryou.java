package jp.chang.myclinic.rcpt.check;

class CheckShohouryou extends CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckShohouryou.class);

    CheckShohouryou(Scope scope) {
        super(scope);
    }

    void check(){
        forEachVisit(visit -> {
            if( countDrug(visit, d -> true) > 0 ){
                int n = countShohouryou(visit);
                int n7 = countShohouryou7(visit);
                int nChoukiNaifuku = countChoukiNaifukuDrug(visit);
                if( nChoukiNaifuku < 7 ){
                    if( n == 0 ){
                        String em = "処方料を追加します。";
                        error("処方料抜け", em, () -> enterShohouryou(visit));
                    } else if( n > 1 ){
                        String em = messageForRemoveExtra("処方料", n, 1);
                        error("処方料重複", em, () -> removeExtraShohouryou(visit, 1));
                    }
                    if( n7 > 0 ){
                        String em = messageForRemoveExtra("処方料７", n7, 0);
                        error("処方料７不可", em, () -> removeExtraShohouryou7(visit, 0));
                    }
                } else {
                    if( n7 == 0 ){
                        String em = "処方料７を追加します。";
                        error("処方料７抜け", em, () -> enterShohouryou7(visit));
                    } else if( n7 > 1 ){
                        String em = messageForRemoveExtra("処方料７", n7, 1);
                        error("処方料７重複", em, () -> removeExtraShohouryou7(visit, 1));
                    }
                    if( n > 0 ){
                        String em = messageForRemoveExtra("処方料", n, 0);
                        error("処方料不可", em, () -> removeExtraShohouryou(visit, 0));
                    }
                }
            }
        });
    }

}
