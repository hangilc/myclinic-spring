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
                case "kazoku": {
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
        map.put("shidou.ten", (drawer, arg) -> {
            drawer.putShidouTen(toInt(arg));
        });
        // TODO: add zaitaku.[yakan, sinya, houmon, yakuzai]
        map.put("zaitaku.oushin.tanka", (drawer, arg) -> {
            drawer.putZaitakuOushinTanka(toInt(arg));
        });
        map.put("zaitaku.oushin.kai", (drawer, arg) -> {
            drawer.putZaitakuOushinTimes(toInt(arg));
        });
        map.put("zaitaku.oushin.ten", (drawer, arg) -> {
            drawer.putZaitakuOushinTen(toInt(arg));
        });
        map.put("zaitaku.sonota.ten", (drawer, arg) -> {
            drawer.putZaitakuSonotaTen(toInt(arg));
        });
        map.put("touyaku.naifuku.yakuzai.kai", (drawer, arg) -> {
            drawer.putTouyakuNaifukuYakuzaiTimes(toInt(arg));
        });
        map.put("touyaku.naifuku.yakuzai.ten", (drawer, arg) -> {
            drawer.putTouyakuNaifukuYakuzaiTen(toInt(arg));
        });
        map.put("touyaku.naifuku.chouzai.tanka", (drawer, arg) -> {
            drawer.putTouyakuNaifukuChouzaiTanka(toInt(arg));
        });
        map.put("touyaku.naifuku.chouzai.kai", (drawer, arg) -> {
            drawer.putTouyakuNaifukuChouzaiTimes(toInt(arg));
        });
        map.put("touyaku.naifuku.chouzai.ten", (drawer, arg) -> {
            drawer.putTouyakuNaifukuChouzaiTen(toInt(arg));
        });
        map.put("touyaku.tonpuku.yakuzai.kai", (drawer, arg) -> {
            drawer.putTouyakuTonpukuYakuzaiTimes(toInt(arg));
        });
        map.put("touyaku.tonpuku.yakuzai.ten", (drawer, arg) -> {
            drawer.putTouyakuTonpukuYakuzaiTen(toInt(arg));
        });
        map.put("touyaku.gaiyou.yakuzai.kai", (drawer, arg) -> {
            drawer.putTouyakuGaiyouYakuzaiTimes(toInt(arg));
        });
        map.put("touyaku.gaiyou.yakuzai.ten", (drawer, arg) -> {
            drawer.putTouyakuGaiyouYakuzaiTen(toInt(arg));
        });
        map.put("touyaku.gaiyou.chouzai.tanka", (drawer, arg) -> {
            drawer.putTouyakuGaiyouChouzaiTanka(toInt(arg));
        });
        map.put("touyaku.gaiyou.chouzai.kai", (drawer, arg) -> {
            drawer.putTouyakuGaiyouChouzaiTimes(toInt(arg));
        });
        map.put("touyaku.gaiyou.chouzai.ten", (drawer, arg) -> {
            drawer.putTouyakuGaiyouChouzaiTen(toInt(arg));
        });
        map.put("touyaku.shohou.tanka", (drawer, arg) -> {
            drawer.putTouyakuShohouTanka(toInt(arg));
        });
        map.put("touyaku.shohou.kai", (drawer, arg) -> {
            drawer.putTouyakuShohouTimes(toInt(arg));
        });
        map.put("touyaku.shohou.ten", (drawer, arg) -> {
            drawer.putTouyakuShohouTen(toInt(arg));
        });
        map.put("touyaku.madoku.kai", (drawer, arg) -> {
            drawer.putTouyakuMadokuTimes(toInt(arg));
        });
        map.put("touyaku.madoku.ten", (drawer, arg) -> {
            drawer.putTouyakuMadokuTen(toInt(arg));
        });
        map.put("touyaku.chouki.ten", (drawer, arg) -> {
            drawer.putTouyakuChoukiTen(toInt(arg));
        });
        map.put("chuusha.hika.kai", (drawer, arg) -> {
            drawer.putChuushaHikaTimes(toInt(arg));
        });
        map.put("chuusha.hika.ten", (drawer, arg) -> {
            drawer.putChuushaHikaTen(toInt(arg));
        });
        map.put("chuusha.joumyaku.kai", (drawer, arg) -> {
            drawer.putChuushaJoumyakuTimes(toInt(arg));
        });
        map.put("chuusha.joumyaku.ten", (drawer, arg) -> {
            drawer.putChuushaJoumyakuTen(toInt(arg));
        });
        map.put("chuusha.sonota.kai", (drawer, arg) -> {
            drawer.putChuushaSonotaTimes(toInt(arg));
        });
        map.put("chuusha.sonota.ten", (drawer, arg) -> {
            drawer.putChuushaSonotaTen(toInt(arg));
        });
        map.put("shochi.kai", (drawer, arg) -> {
            drawer.putShochiTimes(toInt(arg));
        });
        map.put("shochi.ten", (drawer, arg) -> {
            drawer.putShochiTen(toInt(arg));
        });
        map.put("shochi.yakuzai.ten", (drawer, arg) -> {
            drawer.putShochiYakuzaiTen(toInt(arg));
        });
        map.put("shujutsu.kai", (drawer, arg) -> {
            drawer.putShujutsuTimes(toInt(arg));
        });
        map.put("shujutsu.ten", (drawer, arg) -> {
            drawer.putShujutsuTen(toInt(arg));
        });
        map.put("shujutsu.yakuzai.ten", (drawer, arg) -> {
            drawer.putShujutsuYakuzaiTen(toInt(arg));
        });
        map.put("kensa.kai", (drawer, arg) -> {
            drawer.putKensaTimes(toInt(arg));
        });
        map.put("kensa.ten", (drawer, arg) -> {
            drawer.putKensaTen(toInt(arg));
        });
        map.put("kensa.yakuzai.ten", (drawer, arg) -> {
            drawer.putKensaYakuzaiTen(toInt(arg));
        });
        map.put("gazou.kai", (drawer, arg) -> {
            drawer.putGazouTimes(toInt(arg));
        });
        map.put("gazou.ten", (drawer, arg) -> {
            drawer.putGazouTen(toInt(arg));
        });
        map.put("gazou.yakuzai.ten", (drawer, arg) -> {
            drawer.putGazouYakuzaiTen(toInt(arg));
        });
        map.put("sonota.shohousen.kai", (drawer, arg) -> {
            drawer.putSonotaShohousenTimes(toInt(arg));
        });
        map.put("sonota.shohousen.ten", (drawer, arg) -> {
            drawer.putSonotaShohousenTen(toInt(arg));
        });
        map.put("sonota.sonota.ten", (drawer, arg) -> {
            drawer.putSonotaSonotaTen(toInt(arg));
        });
        map.put("sonota.yakuzai.ten", (drawer, arg) -> {
            drawer.putSonotaYakuzaiTen(toInt(arg));
        });
        map.put("kyuufu.hoken.seikyuuten", (drawer, arg) -> {
            drawer.putKyuufuHokenSeikyuuten(toInt(arg));
        });
        map.put("tekiyou", (drawer, arg) -> {
            String[] parts = arg.split(":");
            assert parts.length == 4 || parts.length == 3;
            String index = "";
            if( !parts[0].isEmpty() ){
                index = String.format("(%s)", parts[0]);
            }
            String tankaTimes = "";
            if( parts.length == 4 && !parts[2].isEmpty() && !parts[3].isEmpty() ){
                tankaTimes = String.format("%sx%s", parts[2], parts[3]);
            }
            drawer.addTekiyou(index, parts[1], tankaTimes);
        });
        map.put("tekiyou_begin_drugs", (drawer, arg) -> {
            String[] parts = arg.split(":");
            drawer.setDrugBegin(parts[0], parts[1], parts[2]);
        });
        map.put("tekiyou_drug", (drawer, arg) -> {
            String[] parts = arg.split(":");
            drawer.addDrug(parts[0], parts[1]);
        });
        map.put("tekiyou_end_drugs", (drawer, arg) -> {
            drawer.flushDrugBegin();
        });
   }

}
