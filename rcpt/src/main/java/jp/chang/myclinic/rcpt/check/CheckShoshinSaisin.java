package jp.chang.myclinic.rcpt.check;

class CheckShoshinSaisin extends CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckShoshinSaisin.class);

    CheckShoshinSaisin(Scope scope) {
        super(scope);
    }

    void check(boolean fixit){
        forEachVisit(visit -> {
            int nShoshin = countShoshinGroup(visit);
            int nSaishin = countSaishinGroup(visit);
            int n = nShoshin + nSaishin;
            if( n == 0 ){
                error("初診再診もれ");
            } else if( n > 1 ){
                error("初診再診重複");
            }
        });
    }

}
