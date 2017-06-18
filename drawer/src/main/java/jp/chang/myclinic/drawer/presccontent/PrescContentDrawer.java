package jp.chang.myclinic.drawer.presccontent;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;
import jp.chang.myclinic.drawer.Op;

import java.util.ArrayList;
import java.util.List;

public class PrescContentDrawer {

    private DrawerCompiler compiler = new DrawerCompiler();
    private PrescContentDrawerData data;
    private int pageWidth = 148;
    private int pageHeight = 210;

    public PrescContentDrawer(PrescContentDrawerData data){
        this.data = data;
        Box page = new Box(0, 0, pageWidth, pageHeight);
        Box dispBox = page.inset(5);
        compiler.createFont("regular", "MS Gothic", 4.6);
        compiler.setFont("regular");
        List<String> lines = new ArrayList<>();
        lines.add(data.patientName + "様 " + data.prescDate);
        lines.add("");
        for(String drug: data.drugs){
            addDrug(lines, dispBox, drug);
        }
        compiler.multilineText(lines, dispBox, HAlign.Left, VAlign.Top, 0);
    }

    public List<Op> getOps(){
        return compiler.getOps();
    }

    private void addDrug(List<String> lines, Box box, String drug){
        lines.addAll(compiler.breakLine(drug, box.getWidth()));
    }
}
