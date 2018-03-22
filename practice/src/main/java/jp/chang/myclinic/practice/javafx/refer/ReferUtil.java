package jp.chang.myclinic.practice.javafx.refer;

import jp.chang.myclinic.practice.PracticeEnv;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

class ReferUtil {

    private static Logger logger = LoggerFactory.getLogger(ReferUtil.class);

    private ReferUtil() {

    }

    public static void changeDefaultPrinterSetting(String newSetting){
        try {
            PracticeEnv.INSTANCE.setAppProperty(PracticeEnv.REFER_PRINTER_SETTING_KEY, newSetting);
        } catch (IOException e) {
            logger.error("Failed to set default printer setting of refer.", e);
            GuiUtil.alertException("紹介状の既定印刷設定の保存に失敗しました。", e);
        }
    }

}
