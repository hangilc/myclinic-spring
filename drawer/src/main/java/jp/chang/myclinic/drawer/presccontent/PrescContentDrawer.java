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
    private double padding = 5;
    private double lastY;

    public PrescContentDrawer(PrescContentDrawerData data){
        this.data = data;
        Box page = new Box(0, 0, pageWidth, pageHeight);
        Box dispBox = page.inset(padding);
        compiler.createFont("regular", "MS Gothic", 4.6);
        compiler.setFont("regular");
        List<String> lines = new ArrayList<>();
        lines.add(data.patientName + "様 " + data.prescDate);
        lines.add("");
        for(String drug: data.drugs){
            addDrug(lines, dispBox, drug);
        }
        lastY = compiler.multilineText(lines, dispBox, HAlign.Left, VAlign.Top, 0);
    }

    public List<Op> getOps(){
        return compiler.getOps();
    }

    public double getPageWidth(){
        return pageWidth;
    }

    public double getPageHeight(){
        return lastY + padding;
    }

    private void addDrug(List<String> lines, Box box, String drug){
        lines.addAll(compiler.breakLine(drug, box.getWidth()));
    }
}
