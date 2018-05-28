package jp.chang.myclinic.rcptdrawer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

class RcptDataDispatcher {

    private static Logger logger = LoggerFactory.getLogger(RcptDataDispatcher.class);

    private static interface Dispatcher {
        void dispatch(RcptDrawer drawer, String arg, RcptDataDispatcher self);
    }

    private static Map<String, Dispatcher> map = new HashMap<>();
    static {
        map.put("patient_id", (drawer, arg, self) -> {
            int patientId = toInt(arg);
            drawer.putPatientId(patientId);
            self.setPatientId(patientId);
        });
    }

    private int patientId;

    public void dispatch(RcptDrawer drawer, String cmd, String arg){
        switch(cmd){
            case "patient_id": {
                break;
            }
            default: {
                System.err.println("Unknown command: " + cmd);
                break;
            }
        }
    }

    public int getPatientId() {
        return patientId;
    }

    private void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    private static int toInt(String arg){
        try {
            return Integer.parseInt(arg);
        } catch(NumberFormatException ex){
            logger.error("Invalid integer: " + arg);
            throw new RuntimeException("Invalid arg");
        }
    }

}
