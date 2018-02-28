package jp.chang.myclinic.practice.javafx.refer;

import jp.chang.myclinic.drawer.*;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.TextAtOpt;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;
import jp.chang.myclinic.drawer.printer.PrinterConsts;
import jp.chang.myclinic.dto.PatientDTO;

import java.util.ArrayList;
import java.util.List;

public class ReferDrawer {
    private DrawerCompiler compiler = new DrawerCompiler();
    private Box contentBox = new Box(30, 115, 170, 210);
    private Point titlePoint = new Point(contentBox.getCx(), 41);
    private Point referHospitalPoint = new Point(30, 58);
    private Point referDoctorPoint = new Point(30, 58+6);
    private Point patientNamePoint = new Point(30, 80);
    private Point patientInfoPoint = new Point(50, 90);
    private Point diagnosisPoint = new Point(30, 102);
    private Point issueDatePoint = new Point(30, 220);
    private Point addressPoint = new Point(118, 220);

    public ReferDrawer(){
        Box page = Box.of(PaperSize.A4);
        setupFonts();
    }

    public List<Op> getOps(){
        return compiler.getOps();
    }

    public void setPatient(PatientDTO patient){
        if( patient == null ){

        } else {

        }
    }

    public void setTitle(String title){
        DrawerCompiler c = this.compiler;
        Point p = titlePoint;
        c.setFont("serif-5-bold");
        TextAtOpt opt = new TextAtOpt();
        opt.extraSpace = 5;
        c.textAt(title, p.getX(), p.getY(), HAlign.Center, VAlign.Center, opt);
    }

    public void setReferHospital(String name){
        DrawerCompiler c = this.compiler;
        Point p = referHospitalPoint;
        c.setFont("serif-4");
        c.textAt(name, p.getX(), p.getY(), HAlign.Left, VAlign.Bottom);
    }

    public void setReferDoctor(String text){
        DrawerCompiler c = this.compiler;
        Point p = referDoctorPoint;
        c.setFont("serif-4");
        c.textAt(text, p.getX(), p.getY(), HAlign.Left, VAlign.Bottom);
    }

    public void setPatientName(String name){
        DrawerCompiler c = compiler;
        Point p = patientNamePoint;
        c.setFont("serif-5");
        c.textAt(name, p.getX(), p.getY(), HAlign.Left, VAlign.Bottom);
    }

    public void setPatientInfo(String text){
        DrawerCompiler c = compiler;
        Point p = patientInfoPoint;
        c.setFont("serif-4");
        c.textAt(text, p.getX(), p.getY(), HAlign.Left, VAlign.Bottom);
    }

    public void setDiagnosis(String text){
        DrawerCompiler c = compiler;
        Point p = diagnosisPoint;
        c.setFont("serif-5");
        c.textAt(text, p.getX(), p.getY(), HAlign.Left, VAlign.Bottom);
    }

    public void setIssueDate(String text){
        DrawerCompiler c = compiler;
        Point p = issueDatePoint;
        c.setFont("serif-4");
        c.textAt(text, p.getX(), p.getY(), HAlign.Left, VAlign.Bottom);
    }

    public void setAddress(String addr1, String addr2, String addr3, String addr4, String clinicName, String doctorName){
        DrawerCompiler c = this.compiler;
        Point p = addressPoint;
        c.setFont("serif-4");
        double x = p.getX();
        double y = p.getY() + 4;
        double lineHeight = 4 + 2;
        c.textAt(addr1, x, y, HAlign.Left, VAlign.Bottom);
        y += lineHeight;
        c.textAt(addr2, x, y, HAlign.Left, VAlign.Bottom);
        y += lineHeight;
        c.textAt(addr3, x, y, HAlign.Left, VAlign.Bottom);
        y += lineHeight;
        c.textAt(addr4, x, y, HAlign.Left, VAlign.Bottom);
        y += lineHeight;
        y += 4;
        c.textAt(clinicName, x, y, HAlign.Left, VAlign.Bottom);
        y += lineHeight;
        String txt = "院長";
        DrawerCompiler.Measure mes = c.measureText(txt);
        c.textAt(txt, x, y, HAlign.Left, VAlign.Center);
        x += mes.cx + 4;
        c.setFont("serif-6");
        mes = c.measureText(doctorName);
        c.textAt(doctorName, x, y, HAlign.Left, VAlign.Center);
        x += mes.cx + 8;
        c.setFont("serif-4");
        c.textAt("㊞", x, y, HAlign.Left, VAlign.Center);
    }

    public void setContent(String content){
        DrawerCompiler c = this.compiler;
        Box box = contentBox;
        String[] contentLines = content.split("\\r\\n|\\r|\\n");
        c.setFont("serif-4");
        List<String> lines = new ArrayList<>();
        for(String contentLine: contentLines){
            c.breakLine(contentLine, box.getWidth()).forEach(lines::add);
        }
        double leading = 0.8;
        c.multilineText(lines, box, HAlign.Left, VAlign.Top, leading);
    }

    private void setupFonts(){
        compiler.createFont("serif-6", "MS Mincho", 6);
        compiler.createFont("serif-5", "MS Mincho", 5);
        compiler.createFont("serif-5-bold", "MS Mincho", 5, PrinterConsts.FW_BOLD, false);
        compiler.createFont("serif-4", "MS Mincho", 4);
    }
}
