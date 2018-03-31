package jp.chang.myclinic.medicalcheck;

import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.Op;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class Drawer {

    private static Logger logger = LoggerFactory.getLogger(Drawer.class);
    private DrawerCompiler compiler = new DrawerCompiler();

    Drawer() {

    }

    List<Op> getOps(){
        return compiler.getOps();
    }

}
