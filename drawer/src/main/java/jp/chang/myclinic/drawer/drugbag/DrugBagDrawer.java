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
        titleBox = paper.innerBox(0, 35, 128, 9.88);
        patientNameBox = paper.innerBox(10, 52.88, 108, 6.35);
        patientNameYomiBox = paper.innerBox(10, 61.23, 108, 4.94);
        drugBox = paper.innerBox(15, 71.17, 98, 17.83);
        drugNameBox = paper.innerBox(18+1.5, 91, 84, 16);
        descBox = paper.innerBox(27.5, 111, 74.8, 20+1);
        prescribedAtBox = paper.innerBox(64, 134, 54, 3.53);
        Box footer = paper.innerBox(10, 140, 108, 37);
        clinicNameBox = footer.innerBox(0, 5, 70, 6.35);
        clinicAddrBox = footer.innerBox(0, 14.35, 70, 22.65);
        stampBox = footer.innerBox(78, 5, 20, 20);
        stampLabelBox = footer.innerBox(78, 27, 20, 3.53);
        setupFonts();
        compiler.createPen("regular-pen", data.color.r, data.color.g, data.color.b);
        compiler.setPen("regular-pen");
        compiler.setTextColor(data.color.r, data.color.g, data.color.b);
        setupTitle();
        setupPatientName();
        setupPatientNameYomi();
        setupInstructions();
        setupDrugName();
    }

    public List<Op> getOps(){
        return compiler.getOps();
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
        List<String> lines = new ArrayList<>();
        double width = drugBox.getWidth();
        for(String instr: data.instructions){
            List<String> chunks = compiler.breakLine(instr, width);
            if( chunks.size() > 1 ){
                halign = HAlign.Left;
            }
            System.out.println(chunks);
            lines.addAll(chunks);
        }
        compiler.multilineText(lines, drugBox, halign, VAlign.Top, 2);
    }

    private void setupDrugName(){
        compiler.setFont(REGULAR_FONT);
        List<String> lines = compiler.breakLine(data.drugName, drugNameBox.getWidth());
        if( lines.size() > 1 ){
            compiler.multilineText(lines, drugNameBox, HAlign.Left, VAlign.Top, 0.5);
        } else {
            compiler.textIn(data.drugName, drugNameBox, HAlign.Center, VAlign.Top);
        }
    }
}
