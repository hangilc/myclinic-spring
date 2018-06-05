package jp.chang.myclinic.drawer.drugbag;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;
import jp.chang.myclinic.drawer.Op;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hangil on 2017/06/14.
 */
public class DrugBagDrawer {

    private DrawerCompiler compiler = new DrawerCompiler();
    private DrugBagDrawerData data;
    private Box paper = new Box(0, 0, 128, 182);
    private Box titleBox;
    private Box patientNameBox;
    private Box patientNameYomiBox;
    private Box drugBox;
    private Box drugNameBox;
    private Box descBox;
    private Box prescribedAtBox;
    private Box footerBox;
    private Box clinicNameBox;
    private Box clinicAddrBox;
    private Box stampBox;
    private Box stampLabelBox;
    private static String GOTHIC = "MS GOTHIC";
    private static String MINCHO = "MS MINCHO";
    private static String LARGE_FONT = "large-font";
    private static String MEDIUM_FONT = "medium-font";
    private static String REGULAR_FONT = "regular-font";
    private static String SMALL_FONT = "small-font";
    private static double LARGE_FONT_SIZE = 9.88;
    private static double MEDIUM_FONT_SIZE = 6.35;
    private static double REGULAR_FONT_SIZE = 4.94;
    private static double SMALL_FONT_SIZE = 3.43;
    private static double DRUGBOX_FONT_SIZE = REGULAR_FONT_SIZE;

    public DrugBagDrawer(DrugBagDrawerData data){
        this.data = data;
        titleBox = innerBox(paper, 0, 35, 128, 9.88);
        patientNameBox = innerBox(paper, 10, 52.88, 108, 6.35);
        patientNameYomiBox = innerBox(paper, 10, 61.23, 108, 4.94);
        drugBox = innerBox(paper, 15, 71.17, 98, 17.83);
        drugNameBox = innerBox(paper, 18+1.5+4, 91, 84, 16);
        descBox = innerBox(paper, 27.5, 111, 74.8, 20+1);
        prescribedAtBox = innerBox(paper, 64, 134, 54, 3.53);
        Box footer = innerBox(paper, 10, 140, 108, 37);
        footerBox = footer;
        clinicNameBox = innerBox(footer, 0, 5, 70, 6.35);
        clinicAddrBox = innerBox(footer, 0, 14.35, 70, 22.65);
        stampBox = innerBox(footer, 78, 5, 20, 20);
        stampLabelBox = innerBox(footer, 78, 27, 20, 3.53);
        setupFonts();
        compiler.createPen("regular-pen", data.color.r, data.color.g, data.color.b);
        compiler.setPen("regular-pen");
        compiler.setTextColor(data.color.r, data.color.g, data.color.b);
        setupTitle();
        setupPatientName();
        setupPatientNameYomi();
        setupInstructions();
        setupDrugName();
        setupDrugDescription();
        setupPrescribedAt();
        compiler.frameTop(footerBox);
        compiler.box(stampBox);
        setupStampLabel();
        setupClinicName();
        setupClinicAddr();
    }

    public List<Op> getOps(){
        return compiler.getOps();
    }

    private static Box innerBox(Box box, double left, double top, double width, double height){
        return box.innerBox(left, top, left + width, top + height);
    }

    private void setupFonts(){
        compiler.createFont(LARGE_FONT, GOTHIC, LARGE_FONT_SIZE);
        compiler.createFont(MEDIUM_FONT, GOTHIC, MEDIUM_FONT_SIZE);
        compiler.createFont(REGULAR_FONT, GOTHIC, REGULAR_FONT_SIZE);
        compiler.createFont(SMALL_FONT, GOTHIC, SMALL_FONT_SIZE);
    }

    private void setupTitle(){
        compiler.setFont(LARGE_FONT);
        compiler.textIn(data.title, titleBox, HAlign.Center, VAlign.Center);
    }

    private void setupPatientName(){
        String text = data.patientName + " 様";
        compiler.setFont(MEDIUM_FONT);
        compiler.textIn(text, patientNameBox, HAlign.Center, VAlign.Top);
    }

    private void setupPatientNameYomi(){
        String text = "(" + data.patientNameYomi + ")";
        compiler.setFont(REGULAR_FONT);
        compiler.textIn(text, patientNameYomiBox, HAlign.Center, VAlign.Top);
    }

    private void setupInstructions(){
        compiler.setFont(REGULAR_FONT);
        HAlign halign = HAlign.Center;
        Box box = drugBox;
        double width = box.getWidth();
        double fontSize = compiler.getCurrentFontSize();
        double leading = 2;
        for(String instr: data.instructions){
            List<String> chunks = compiler.breakLine(instr, width);
            if( chunks.size() > 1 ){
                compiler.multilineText(chunks, box, HAlign.Left, VAlign.Top, leading);
                box = box.shrinkHeight(chunks.size() * (fontSize + leading), Box.VertAnchor.Bottom);
            } else {
                compiler.textIn(instr, box, HAlign.Center, VAlign.Top);
                box = box.shrinkHeight(fontSize + leading, Box.VertAnchor.Bottom);
            }
        }
    }

    private void setupDrugName(){
        compiler.setFont(REGULAR_FONT);
        List<String> lines = compiler.breakLine(data.drugName, drugNameBox.getWidth());
        if( lines.size() > 1 ){
            compiler.multilineText(lines, drugNameBox, HAlign.Left, VAlign.Bottom, 0.5);
        } else {
            compiler.textIn(data.drugName, drugNameBox, HAlign.Center, VAlign.Bottom);
        }
    }

    private void setupDrugDescription(){
        compiler.setFont(SMALL_FONT);
        compiler.box(descBox);
        Box box = descBox.inset(1, 0.8);
        List<String> lines = compiler.breakLine(data.drugDescription, box.getWidth());
        compiler.multilineText(lines, box, HAlign.Left, VAlign.Top, 0.5);
    }

    private void setupPrescribedAt(){
        String label = "調剤年月日 " + data.prescribedAt;
        compiler.setFont(SMALL_FONT);
        compiler.textIn(label, prescribedAtBox, HAlign.Left, VAlign.Top);
    }

    private void setupStampLabel(){
        compiler.setFont(SMALL_FONT);
        compiler.textIn("調剤者の印", stampLabelBox, HAlign.Center, VAlign.Top);
    }

    private void setupClinicName(){
        compiler.setFont(MEDIUM_FONT);
        compiler.textIn(data.clinicName, clinicNameBox, HAlign.Left, VAlign.Top);
    }

    private void setupClinicAddr(){
        compiler.setFont(SMALL_FONT);
        compiler.multilineText(data.clinicAddr, clinicAddrBox, HAlign.Left, VAlign.Top, 1.4);
    }
}
