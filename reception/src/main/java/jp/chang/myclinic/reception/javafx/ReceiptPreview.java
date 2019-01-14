package jp.chang.myclinic.reception.javafx;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.MeisaiDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.reception.Globals;
import jp.chang.myclinic.reception.drawerpreviewfx.DrawerPreviewStage;
import jp.chang.myclinic.reception.receipt.ReceiptDrawer;
import jp.chang.myclinic.reception.receipt.ReceiptDrawerData;
import jp.chang.myclinic.reception.receipt.ReceiptDrawerDataCreator;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReceiptPreview {

    private static Logger logger = LoggerFactory.getLogger(ReceiptPreview.class);

    public static void previewReceipt(MeisaiDTO meisai, PatientDTO patient, VisitDTO visit, Integer charge) {
        ClinicInfoDTO clinicInfo = Globals.getAppVars().getClinicInfo();
        ReceiptDrawerData data = ReceiptDrawerDataCreator.create(meisai, patient, visit, charge, clinicInfo);
        ReceiptDrawer receiptDrawer = new ReceiptDrawer(data);
        List<Op> ops = receiptDrawer.getOps();
        try {
            PrinterEnv printerEnv = Globals.getAppVars().getPrinterEnv();
            DrawerPreviewStage stage = new DrawerPreviewStage(ops, PaperSize.A6_Landscape,
                    printerEnv,
                    () -> Globals.getAppVars().getReceiptPrinterSetting(),
                    setting -> {
                        Globals.getAppVars().modifyAppProperties(props -> {
                            props.receiptPrinterSetting = setting;
                            return props;
                        });
                    }
            );
            stage.show();
        } catch (Exception e) {
            logger.error("Failed to get printer env.", e);
            GuiUtil.alertException("Failed to get printer env.", e);
        }
    }

    public static void previewReceipt(PatientDTO patient, VisitDTO visit, Integer charge) {
        ClinicInfoDTO clinicInfo = Globals.getAppVars().getClinicInfo();
        ReceiptDrawerData data = ReceiptDrawerDataCreator.create(null, patient, visit, charge, clinicInfo);
        ReceiptDrawer receiptDrawer = new ReceiptDrawer(data);
        List<Op> ops = receiptDrawer.getOps();
        try {
            PrinterEnv printerEnv = Globals.getAppVars().getPrinterEnv();
            DrawerPreviewStage stage = new DrawerPreviewStage(ops, PaperSize.A6_Landscape,
                    printerEnv,
                    () -> Globals.getAppVars().getReceiptPrinterSetting(),
                    setting -> {
                        Globals.getAppVars().modifyAppProperties(props -> {
                            props.receiptPrinterSetting = setting;
                            return props;
                        });
                    }
            );
            stage.show();
        } catch (Exception e) {
            logger.error("Failed to get printer env.", e);
            GuiUtil.alertException("Failed to get printer env.", e);
        }
    }

}
