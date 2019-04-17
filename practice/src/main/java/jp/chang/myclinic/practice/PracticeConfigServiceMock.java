package jp.chang.myclinic.practice;

import jp.chang.myclinic.drawer.printer.PrinterEnv;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.mockdata.MockData;
import jp.chang.myclinic.mockdata.MockPrinterEnv;
import jp.chang.myclinic.mockdata.SampleData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PracticeConfigServiceMock implements PracticeConfigService {

    private PrinterEnv printerEnv = MockPrinterEnv.create();

    @Override
    public String getShohousenPrinterSetting() {
        return null;
    }

    @Override
    public void setShohousenPrinterSetting(String settingName) {

    }

    @Override
    public PrinterEnv getPrinterEnv() {
        return printerEnv;
    }

    @Override
    public String getKouhatsuKasan() {
        return null;
    }
}
