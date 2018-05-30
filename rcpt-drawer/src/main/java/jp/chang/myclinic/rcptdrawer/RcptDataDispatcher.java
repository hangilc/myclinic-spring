package jp.chang.myclinic.rcptdrawer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class RcptDataDispatcher {

    private static Logger logger = LoggerFactory.getLogger(RcptDataDispatcher.class);

    private interface Dispatcher {
        void dispatch(RcptDrawer drawer, String arg);
    }

    private RcptDrawer rcptDrawer;
    private DispatchHook hook;

    RcptDataDispatcher(RcptDrawer rcptDrawer){
        this.rcptDrawer = rcptDrawer;
    }

    public void dispatch(String cmd, String arg){
        Dispatcher dispatcher = map.get(cmd);
        if( dispatcher != null ){
            dispatcher.dispatch(rcptDrawer, arg);
        } else {
            System.err.println("Unknown cmd: " + cmd);
        }
    }

    private static int toInt(String arg){
        try {
            return Integer.parseInt(arg);
        } catch(NumberFormatException ex){
            logger.error("Invalid integer: " + arg);
            throw new RuntimeException("Invalid arg");
        }
    }

    private static Map<String, Dispatcher> map = new HashMap<>();
    static {
        map.put("patient_id", (drawer, arg) -> {
            int patientId = toInt(arg);
            drawer.putPatientId(patientId);
        });
        map.put("kikancode", RcptDrawer::putKikanCode);
        map.put("fukenbangou", (drawer, arg) -> {
            drawer.putFukenBangou(toInt(arg));
        });
        map.put("shinryou.nen", (drawer, arg) -> {
            drawer.putShinryouNen(toInt(arg));
        });
        map.put("shinryou.tsuki", (drawer, arg) -> {
            drawer.putShinryouMonth(toInt(arg));
        });
        map.put("hokenshabangou", (drawer, arg) -> {
            drawer.putHokenshaBangou(toInt(arg));
        });
        map.put("hihokenshashou", RcptDrawer::putHihokenshashou);
        map.put("kouhifutanshabangou1", (drawer, arg) -> {
            drawer.putKouhiFutanshaBangou1(toInt(arg));
        });
        map.put("kouhijukyuushabangou1", (drawer, arg) -> {
            drawer.putKouhiJukyuushaBangou1(toInt(arg));
        });
        map.put("kouhifutanshabangou2", (drawer, arg) -> {
            drawer.putKouhiFutanshaBangou2(toInt(arg));
        });
        map.put("kouhijukyuushabangou2", (drawer, arg) -> {
            drawer.putKouhiJukyuushaBangou2(toInt(arg));
        });
        map.put("shozaichimeishou.line1", RcptDrawer::putShozaichiMeishouLine1);
        map.put("shozaichimeishou.line2", RcptDrawer::putShozaichiMeishouLine2);
        map.put("shozaichimeishou.line4", RcptDrawer::putShozaichiMeishouLine3); // 4 -> 3 ; for historical reason
        map.put("shimei", RcptDrawer::putShimei);
        map.put("seibetsu", (drawer, arg) -> {
            if( "otoko".equals(arg) ){
                drawer.markSeibetsuOtoko();
            } else if( "onna".equals(arg) ){
                drawer.markSeibetsuOnna();;
            } else {
                System.err.println("Unknown seibtsu: " + arg);
            }
        });
        map.put("seinengappi.gengou", (drawer, arg) -> {
            if( arg != null ){
                switch(arg){
                    case "meiji": drawer.markSeinengappiMeiji(); break;
                    case "taishou": drawer.markSeinengappiTaishou(); break;
                    case "shouwa": drawer.markSeinengappiShouwa(); break;
                    case "heisei": drawer.markSeinengappiHeisei(); break;
                    default: System.err.println("Unknown seinenngappi.gengou: " + arg); break;
                }
            } else {
                System.err.println("seinengappi.gengou null");
            }
        });
        map.put("seinengappi.nen", (drawer, arg) -> {
           drawer.putSeinengappiNen(toInt(arg));
        });
        map.put("seinengappi.tsuki", (drawer, arg) -> {
           drawer.putSeinengappiMonth(toInt(arg));
        });
        map.put("seinengappi.hi", (drawer, arg) -> {
           drawer.putSeinengappiDay(toInt(arg));
        });
        map.put("shoubyoumei.1", (drawer, arg) -> {
           drawer.putShoubyoumei(1, arg);
        });
        map.put("shoubyoumei.2", (drawer, arg) -> {
           drawer.putShoubyoumei(2, arg);
        });
        map.put("shoubyoumei.3", (drawer, arg) -> {
           drawer.putShoubyoumei(3, arg);
        });
        map.put("shoubyoumei.4", (drawer, arg) -> {
           drawer.putShoubyoumei(4, arg);
        });
        map.put("shoubyoumei_extra", (drawer, arg) -> {
            String[] parts = arg.split(":");
            if( parts.length != 5 ){
                throw new RuntimeException("Invalid shoubyoumei.extra: " + arg);
            }
            drawer.putShoubyoumeiEtra(toInt(parts[0]), parts[1],
                    toInt(parts[2]), toInt(parts[3]), toInt(parts[4]));
        });
        map.put("shinryoukaishi.nen.1", (drawer, arg) -> {
            drawer.putShoubyoumeiKaishiNen(1, toInt(arg));
        });
        map.put("shinryoukaishi.nen.2", (drawer, arg) -> {
            drawer.putShoubyoumeiKaishiNen(2, toInt(arg));
        });
        map.put("shinryoukaishi.nen.3", (drawer, arg) -> {
            drawer.putShoubyoumeiKaishiNen(3, toInt(arg));
        });
        map.put("shinryoukaishi.nen.4", (drawer, arg) -> {
            drawer.putShoubyoumeiKaishiNen(4, toInt(arg));
        });
        map.put("shinryoukaishi.tsuki.1", (drawer, arg) -> {
            drawer.putShoubyoumeiKaishiMonth(1, toInt(arg));
        });
        map.put("shinryoukaishi.tsuki.2", (drawer, arg) -> {
            drawer.putShoubyoumeiKaishiMonth(2, toInt(arg));
        });
        map.put("shinryoukaishi.tsuki.3", (drawer, arg) -> {
            drawer.putShoubyoumeiKaishiMonth(3, toInt(arg));
        });
        map.put("shinryoukaishi.tsuki.4", (drawer, arg) -> {
            drawer.putShoubyoumeiKaishiMonth(4, toInt(arg));
        });
        map.put("shinryoukaishi.hi.1", (drawer, arg) -> {
            drawer.putShoubyoumeiKaishiDay(1, toInt(arg));
        });
        map.put("shinryoukaishi.hi.2", (drawer, arg) -> {
            drawer.putShoubyoumeiKaishiDay(2, toInt(arg));
        });
        map.put("shinryoukaishi.hi.3", (drawer, arg) -> {
            drawer.putShoubyoumeiKaishiDay(3, toInt(arg));
        });
        map.put("shinryoukaishi.hi.4", (drawer, arg) -> {
            drawer.putShoubyoumeiKaishiDay(4, toInt(arg));
        });
        map.put("tenki.chiyu", (drawer, arg) -> {
            String[] parts = arg.split(",");
            drawer.putChiyu(parts);
        });
        map.put("tenki.chuushi", (drawer, arg) -> {
            String[] parts = arg.split(",");
            drawer.putChuushi(parts);
        });
        map.put("tenki.shibou", (drawer, arg) -> {
            String[] parts = arg.split(",");
            drawer.putShibou(parts);
        });
        map.put("hokenshubetsu", (drawer, arg) -> {
            Objects.requireNonNull(arg);
            switch(arg){
                case "shakoku": {
                    drawer.markHokenshubetsuShakoku();
                    break;
                }
                case "kouhi": {
                    drawer.markHokenshubetsuKouhi();
                    break;
                }
                case "roujin": // fall through
                case "koukikourei": {
                    drawer.markHokenshubetsuKoukikourei();
                    break;
                }
                case "taishoku": {
                    drawer.markHokenshubetsuTaishoku();
                    break;
                }
                default: {
                    System.err.println("Unknown hokenshubetsu: " + arg);
                    break;
                }
            }
        });
        map.put("hokentandoku", (drawer, arg) -> {
            Objects.requireNonNull(arg);
            switch(arg){
                case "tandoku": {
                    drawer.markHokentandokuTandoku();
                    break;
                }
                case "hei2": {
                    drawer.markHokentandokuHei2();
                    break;
                }
                case "hei3": {
                    drawer.markHokentandokuHei3();
                    break;
                }
                default: {
                    System.err.println("Unknown hokentandoku: " + arg);
                    break;
                }
            }
        });
        map.put("hokenfutan", (drawer, arg) -> {
            Objects.requireNonNull(arg);
            switch(arg){
                case "honnin": {
                    drawer.markHokenfutanHonnin();
                    break;
                }
                case "sansai": {
                    drawer.markHokenfutanSansai();
                    break;
                }
                case "kazokku": {
                    drawer.markHokenfutanKazoku();
                    break;
                }
                case "kourei9": // fall through
                case "kourei8": {
                    drawer.markHokenfutanKourei9();
                    break;
                }
                 case "kourei7": {
                    drawer.markHokenfutanKourei7();
                    break;
                }
                default: {
                    System.err.println("Unknown hokenfutan: " + arg);
                    break;
                }
            }
        });
        map.put("kyuufuwariai", (drawer, arg) -> {
            int value = Integer.parseInt(arg);
            switch(value){
                case 10: {
                    drawer.markKyuufuwari10();
                    break;
                }
                case 9: {
                    drawer.markKyuufuwari9();
                    break;
                }
                case 8: {
                    drawer.markKyuufuwari8();
                    break;
                }
                case 7: {
                    drawer.markKyuufuwari7();
                    break;
                }
                default: {
                    drawer.putKyuufuwariOther("" + value);
                    break;
                }
            }
        });
        map.put("shinryounissuu.hoken", (drawer, arg) -> drawer.putShinryouNissuuHoken(toInt(arg)));
        map.put("shinryounissuu.kouhi.1", (drawer, arg) -> drawer.putShinryouNissuuKouhi1(toInt(arg)));
        map.put("shinryounissuu.kouhi.2", (drawer, arg) -> drawer.putShinryouNissuuKouhi2(toInt(arg)));
        map.put("shoshinkasan", (drawer, arg) -> {
            if( arg != null ){
                switch(arg){
                    case "jikangai": {
                        drawer.markShoshinJikangai();
                        break;
                    }
                    case "kyuujitsu": {
                        drawer.markShoshinKyuujitsu();
                        break;
                    }
                    case "shinya": {
                        drawer.markShoshinShinya();
                        break;
                    }
                    default: {
                        System.err.println("Unknown shoshin kasan: " + arg);
                        break;
                    }
                }
            } else {
                System.err.println("shinryoukasan null");
            }
        });
        map.put("shoshin.kai", (drawer, arg) -> {
            drawer.putShoshinTimes(toInt(arg));
        });
        map.put("shoshin.ten", (drawer, arg) -> {
            drawer.putShoshinTen(toInt(arg));
        });
        map.put("saishin.saishin.tanka", (drawer, arg) -> {
            drawer.putSaishinSaishinTanka(toInt(arg));
        });
        map.put("saishin.saishin.kai", (drawer, arg) -> {
            drawer.putSaishinSaishinTimes(toInt(arg));
        });
        map.put("saishin.saishin.ten", (drawer, arg) -> {
            drawer.putSaishinSaishinTen(toInt(arg));
        });
        map.put("saishin.gairaikanri.tanka", (drawer, arg) -> {
            drawer.putSaishinGairaiKanriTanka(toInt(arg));
        });
        map.put("saishin.gairaikanri.kai", (drawer, arg) -> {
            drawer.putSaishinGairaiKanriTimes(toInt(arg));
        });
        map.put("saishin.gairaikanri.ten", (drawer, arg) -> {
            drawer.putSaishinGairaiKanriTen(toInt(arg));
        });
        map.put("saishin.jikangai.tanka", (drawer, arg) -> {
            drawer.putSaishinJikangaiTanka(toInt(arg));
        });
        map.put("saishin.jikangai.kai", (drawer, arg) -> {
            drawer.putSaishinJikangaiTimes(toInt(arg));
        });
        map.put("saishin.jikangai.ten", (drawer, arg) -> {
            drawer.putSaishinJikangaiTen(toInt(arg));
        });
        map.put("saishin.kyuujitsu.tanka", (drawer, arg) -> {
            drawer.putSaishinKyuujitsuTanka(toInt(arg));
        });
        map.put("saishin.kyuujitsu.kai", (drawer, arg) -> {
            drawer.putSaishinKyuujitsuTimes(toInt(arg));
        });
        map.put("saishin.kyuujitsu.ten", (drawer, arg) -> {
            drawer.putSaishinKyuujitsuTen(toInt(arg));
        });
        map.put("saishin.shinya.tanka", (drawer, arg) -> {
            drawer.putSaishinShinyaTanka(toInt(arg));
        });
        map.put("saishin.shinya.kai", (drawer, arg) -> {
            drawer.putSaishinShinyaTimes(toInt(arg));
        });
        map.put("saishin.shinya.ten", (drawer, arg) -> {
            drawer.putSaishinShinyaTen(toInt(arg));
        });
    }

}
