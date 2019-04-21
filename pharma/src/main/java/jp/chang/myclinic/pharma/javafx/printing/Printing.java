package jp.chang.myclinic.pharma.javafx.printing;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawer;
import jp.chang.myclinic.drawer.drugbag.DrugBagDrawerData;
import jp.chang.myclinic.drawer.presccontent.PrescContentDrawer;
import jp.chang.myclinic.drawer.presccontent.PrescContentDrawerData;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.drawer.techou.TechouDrawer;
import jp.chang.myclinic.drawer.techou.TechouDrawerData;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.pharma.CFUtil;
import jp.chang.myclinic.pharma.Globals;
import jp.chang.myclinic.pharma.drawercreator.DrugBagDataCreator;
import jp.chang.myclinic.pharma.drawercreator.PrescContentDataCreator;
import jp.chang.myclinic.pharma.drawercreator.TechouDataCreator;
import jp.chang.myclinic.pharma.javafx.drawerpreview.DrawerPreviewDialog;
import jp.chang.myclinic.utilfx.GuiUtil;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Printing {

    private static Logger logger = LoggerFactory.getLogger(Printing.class);

    public static void printAll(List<DrugFullDTO> drugs, PatientDTO patient) {
        printPrescContent(drugs, patient);
        printDrugBagSet(drugs, patient);
        printTechou(drugs, patient);
    }

    public static void printAllExceptTechou(List<DrugFullDTO> drugs, PatientDTO patient) {
        printPrescContent(drugs, patient);
        printDrugBagSet(drugs, patient);
    }

    public static void previewDrugBag(DrugCategory category) {
        DrugBagDataCreator creator = new DrugBagDataCreator(category, null, null, Globals.getClinicInfo());
        DrugBagDrawerData data = creator.createData();
        DrugBagDrawer drawer = new DrugBagDrawer(data);
        List<Op> ops = drawer.getOps();
        DrawerPreviewDialog previewDialog = new DrawerPreviewDialog(Globals.getPrinterEnv(),
                128, 182, 0.6) {
            @Override
            protected String getDefaultPrinterSettingName() {
                return Globals.getDrugBagPrinterSetting();
            }

            @Override
            protected void setDefaultPrinterSettingName(String newName) {
                Globals.setDrugBagPrinterSetting(newName);
            }
        };
        previewDialog.setTitle("薬袋印刷");
        previewDialog.addStylesheet("Pharma.css");
        previewDialog.setSinglePage(ops);
        previewDialog.show();
    }

    public static void previewTechou(List<DrugFullDTO> drugs, PatientDTO patient) {
        TechouDataCreator creator = new TechouDataCreator(patient, LocalDate.now(), drugs, Globals.getClinicInfo());
        TechouDrawerData drawerData = creator.createData();
        List<Op> ops = new TechouDrawer(drawerData).getOps();
        DrawerPreviewDialog previewDialog = new DrawerPreviewDialog(Globals.getPrinterEnv(), 99, 120, 1.0) {
            @Override
            protected String getDefaultPrinterSettingName() {
                return Globals.getTechouPrinterSetting();
            }

            @Override
            protected void setDefaultPrinterSettingName(String newName) {
                Globals.setTechouPrinterSetting(newName);
            }
        };
        previewDialog.setTitle("薬手帳印刷");
        previewDialog.addStylesheet("Pharma.css");
        previewDialog.setSinglePage(ops);
        previewDialog.show();
    }

    private static void print(List<Op> page, String setting) {
        printPages(Collections.singletonList(page), setting);
    }

    private static void printPages(List<List<Op>> pages, String setting) {
        PrinterEnv printerEnv = Globals.getPrinterEnv();
        if (printerEnv != null) {
            printerEnv.print(pages, setting);
        } else {
            DrawerPrinter printer = new DrawerPrinter();
            printer.printPages(pages);
        }
    }

    private static List<Op> createPrescContentOps(List<DrugFullDTO> drugs, PatientDTO patient) {
        PrescContentDataCreator creator = new PrescContentDataCreator(patient, LocalDate.now(), drugs);
        PrescContentDrawerData drawerData = creator.createData();
        return new PrescContentDrawer(drawerData).getOps();
    }

    private static void printPrescContent(List<DrugFullDTO> drugs, PatientDTO patient) {
        try {
            List<Op> ops = createPrescContentOps(drugs, patient);
            print(ops, getPrescContentPrinterSetting());
        } catch(Exception ex){
            GuiUtil.alertException("処方内容の印刷に失敗しました。", ex);
        }
    }

    private static CompletableFuture<List<Op>> createDrugBagOps(DrugFullDTO drug, PatientDTO patient) {
        return Service.api.findPharmaDrug(drug.drug.iyakuhincode)
                .thenApply(pharmaDrug -> {
                    DrugBagDataCreator creator = new DrugBagDataCreator(drug, patient,
                            pharmaDrug, Globals.getClinicInfo());
                    DrugBagDrawer drawer = new DrugBagDrawer(creator.createData());
                    return drawer.getOps();
                });
    }

    private static void printDrugBagSet(List<DrugFullDTO> drugs, PatientDTO patient) {
        String setting = getDrugBagPrinterSetting();
        CFUtil.map(drugs, drug -> createDrugBagOps(drug, patient))
                .thenAccept(pages -> {
                    printPages(pages, setting);
                })
                .exceptionally(HandlerFX.exceptionally());
    }

    private static List<Op> createTechouOps(List<DrugFullDTO> drugs, PatientDTO patient) {
        TechouDataCreator creator = new TechouDataCreator(patient, LocalDate.now(),
                drugs, Globals.getClinicInfo());
        TechouDrawerData drawerData = creator.createData();
        return new TechouDrawer(drawerData).getOps();
    }

    private static void printTechou(List<DrugFullDTO> drugs, PatientDTO patient) {
        try {
            List<Op> ops = createTechouOps(drugs, patient);
            print(ops, getTechouPrinterSetting());
        } catch(Exception ex){
            GuiUtil.alertException("お薬手帳の印刷に失敗しました。", ex);
        }
    }

    private static String getPrescContentPrinterSetting() {
        return Globals.getPrescContentPrinterSetting();
    }

    private static String getDrugBagPrinterSetting() {
        return Globals.getDrugBagPrinterSetting();
    }

    private static String getTechouPrinterSetting() {
        return Globals.getTechouPrinterSetting();
    }

}
