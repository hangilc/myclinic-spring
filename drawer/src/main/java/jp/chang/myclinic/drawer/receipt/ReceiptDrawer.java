package jp.chang.myclinic.drawer.receipt;

import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.Op;

import java.util.List;

/**
 * Created by hangil on 2017/05/20.
 */
public class ReceiptDrawer {

    private DrawerCompiler compiler = new DrawerCompiler();

    public ReceiptDrawer(){

    }

    public List<Op> getOps(){
        return compiler.getOps();
    }
}
