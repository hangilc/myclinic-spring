package jp.chang.myclinic.practice.refer;

import jp.chang.myclinic.drawer.*;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.TextAtOpt;
import jp.chang.myclinic.drawer.printer.PrinterConsts;
import jp.chang.myclinic.dto.PatientDTO;

import java.util.List;

public class ReferDrawer {
    private DrawerCompiler compiler = new DrawerCompiler();
    private Point titlePoint = new Point(105, 41);
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
        setTitle("紹介状");
    }

    public List<Op> getOps(){
        return compiler.getOps();
    }

    public void setPatient(PatientDTO patient){

    }

    public void setTitle(String title){
        DrawerCompiler c = this.compiler;
        Point p = titlePoint;
        c.setFont("serif-5-bold");
        TextAtOpt opt = new TextAtOpt();
        opt.extraSpace = 5;
        c.textAt(title, p.getX(), p.getY(), HAlign.Center, VAlign.Center, opt);
    }

    private void setupFonts(){
        compiler.createFont("serif-6", "MS Mincho", 6);
        compiler.createFont("serif-5", "MS Mincho", 5);
        compiler.createFont("serif-5-bold", "MS Mincho", 5, PrinterConsts.FW_BOLD, false);
        compiler.createFont("serif-4", "MS Mincho", 4);
    }
}
