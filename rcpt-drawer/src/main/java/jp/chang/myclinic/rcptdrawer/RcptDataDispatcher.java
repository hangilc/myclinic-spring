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
    }

}
