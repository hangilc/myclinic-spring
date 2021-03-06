package jp.chang.myclinic.util.dto_logic;

public class HokenLib {

    //private static Logger logger = LoggerFactory.getLogger(HokenLib.class);

    private HokenLib() {

    }

    public static boolean isValidTodoufukenBangou(int bangou) {
        return bangou >= 1 && bangou <= 47;
    }

    public static int calcCheckingDigit(int numberWithoutCheckingDigit){
        if( numberWithoutCheckingDigit < 0 ){
            throw new RuntimeException("Negative number for calcCheckingDigit.");
        }
        int s = 0;
        int m = 2;
        while( numberWithoutCheckingDigit > 0 ){
            int d = numberWithoutCheckingDigit % 10;
            int dm = d * m;
            if (dm >= 10) {
                dm = (dm / 10) + (dm % 10);
            }
            s += dm;
            numberWithoutCheckingDigit /= 10;
            if (m == 2) {
                m = 1;
            } else {
                m = 2;
            }
        }
        s = s % 10;
        int v = 10 - s;
        if( v == 10 ){
            v = 0;
        }
        return v;
    }

    public static boolean hasValidCheckingDigit(int bangou){
        int v = calcCheckingDigit(bangou/10);
        return v == (bangou % 10);
    }

}
