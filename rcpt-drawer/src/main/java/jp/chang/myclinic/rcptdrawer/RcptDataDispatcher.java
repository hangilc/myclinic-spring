package jp.chang.myclinic.rcptdrawer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

class RcptDataDispatcher {

    private static Logger logger = LoggerFactory.getLogger(RcptDataDispatcher.class);

    private interface Dispatcher {
        void dispatch(RcptDrawer drawer, String arg, DispatchHook hook);
    }

    private RcptDrawer rcptDrawer;
    private DispatchHook hook;

    RcptDataDispatcher(RcptDrawer rcptDrawer, DispatchHook hook){
        this.rcptDrawer = rcptDrawer;
        this.hook = hook;
    }

    public void dispatch(String cmd, String arg){
        Dispatcher dispatcher = map.get(cmd);
        if( dispatcher != null ){
            dispatcher.dispatch(rcptDrawer, arg, hook);
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
        map.put("patient_id", (drawer, arg, hook) -> {
            int patientId = toInt(arg);
            drawer.putPatientId(patientId);
            hook.onPatientId(patientId);
        });
        map.put("kikancode", (drawer, arg, hook) -> {
            drawer.putKikanCode(arg);
        });
        map.put("fukenbangou", (drawer, arg, hook) -> {
            drawer.putFukenBangou(toInt(arg));
        });
        map.put("shinryou.nen", (drawer, arg, hook) -> {
            drawer.putShinryouNen(toInt(arg));
        });
        map.put("shinryou.tsuki", (drawer, arg, hook) -> {
            drawer.putShinryouMonth(toInt(arg));
        });
        map.put("hokenshabangou", (drawer, arg, hook) -> {
            drawer.putHokenshaBangou(toInt(arg));
        });
        map.put("hihokenshashou", (drawer, arg, hook) -> {
            drawer.putHihokenshashou(arg);
        });
        map.put("kouhifutanshabangou1", (drawer, arg, hook) -> {
            drawer.putKouhiFutanshaBangou1(toInt(arg));
        });
        map.put("kouhijukyuushabangou1", (drawer, arg, hook) -> {
            drawer.putKouhiJukyuushaBangou1(toInt(arg));
        });
        map.put("kouhifutanshabangou2", (drawer, arg, hook) -> {
            drawer.putKouhiFutanshaBangou2(toInt(arg));
        });
        map.put("kouhijukyuushabangou2", (drawer, arg, hook) -> {
            drawer.putKouhiJukyuushaBangou2(toInt(arg));
        });
        map.put("shozaichimeishou.line1", (drawer, arg, hook) -> {
            drawer.putShozaichiMeishouLine1(arg);
        });
        map.put("shozaichimeishou.line2", (drawer, arg, hook) -> {
            drawer.putShozaichiMeishouLine2(arg);
        });
        map.put("shozaichimeishou.line3", (drawer, arg, hook) -> {
            drawer.putShozaichiMeishouLine3(arg);
        });
        map.put("shimei", (drawer, arg, hook) -> {
            drawer.putShimei(arg);
        });
        map.put("seibetsu", (drawer, arg, hook) -> {
            if( "otoko".equals(arg) ){
                drawer.markSeibetsuOtoko();
            } else if( "onna".equals(arg) ){
                drawer.markSeibetsuOnna();;
            } else {
                System.err.println("Unknown seibtsu: " + arg);
            }
        });
        map.put("seinengappi.gengou", (drawer, arg, hook) -> {
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
        map.put("seinengappi.nen", (drawer, arg, hook) -> {
           drawer.putSeinengappiNen(toInt(arg));
        });
        map.put("seinengappi.tsuki", (drawer, arg, hook) -> {
           drawer.putSeinengappiMonth(toInt(arg));
        });
        map.put("seinengappi.hi", (drawer, arg, hook) -> {
           drawer.putSeinengappiDay(toInt(arg));
        });
        map.put("shoubyoumei.1", (drawer, arg, hook) -> {
           drawer.putShoubyoumei(1, arg);
        });
        map.put("shoubyoumei.2", (drawer, arg, hook) -> {
           drawer.putShoubyoumei(2, arg);
        });
        map.put("shoubyoumei.3", (drawer, arg, hook) -> {
           drawer.putShoubyoumei(3, arg);
        });
        map.put("shoubyoumei.4", (drawer, arg, hook) -> {
           drawer.putShoubyoumei(4, arg);
        });
    }

}
