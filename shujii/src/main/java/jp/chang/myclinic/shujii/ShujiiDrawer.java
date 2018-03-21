package jp.chang.myclinic.shujii;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class ShujiiDrawer {

    private static Logger logger = LoggerFactory.getLogger(ShujiiDrawer.class);
    private DrawerCompiler compiler = new DrawerCompiler();
    private Point doctorNamePoint = new Point(10, 10);
    private Point clinicNamePoint = new Point(10, 15);
    private Point clinicAddrPoint = new Point(10, 20);
    private Point clinicPhonePoint = new Point(10, 35);
    private Point clinicFaxPoint = new Point(10, 50);
    private Box detailBox = new Box(10, 50, 200, 90);

    ShujiiDrawer(){
        compiler.createFont("small", "MS Gothic", 4, 0, false);
        compiler.createFont("default", "MS Gothic", 6, 0, false);
    }

    public List<Op> getOps(){
        return compiler.getOps();
    }

    public void setDoctorName(String value){
        compiler.setFont("small");
        compiler.textAt(value, doctorNamePoint, HAlign.Left, VAlign.Bottom);
    }

    public void setClinicName(String value){
        compiler.setFont("small");
        compiler.textAt(value, clinicNamePoint, HAlign.Left, VAlign.Bottom);
    }

    public void setClinicAddr(String value){
        compiler.setFont("small");
        compiler.textAt(value, clinicAddrPoint, HAlign.Left, VAlign.Bottom);
    }

    public void setClinicPhone(String value){
        compiler.setFont("small");
        compiler.textAt(value, clinicPhonePoint, HAlign.Left, VAlign.Bottom);
    }

    public void setClinicFax(String value){
        compiler.setFont("small");
        compiler.textAt(value, clinicFaxPoint, HAlign.Left, VAlign.Bottom);
    }

    public void setDetail(String detail){
        compiler.setFont("default");
        compiler.paragraph(detail, detailBox, HAlign.Left, VAlign.Top, 0);
    }
}
