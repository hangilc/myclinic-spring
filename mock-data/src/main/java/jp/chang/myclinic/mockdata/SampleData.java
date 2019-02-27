package jp.chang.myclinic.mockdata;

import jp.chang.myclinic.dto.ClinicInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class SampleData {

    public static ClinicInfoDTO sampleClinicInfo = new ClinicInfoDTO();
    static {
        ClinicInfoDTO info = sampleClinicInfo;
        info.name = "テストクリニック";
        info.postalCode = "123-4567";
        info.address = "某県某所１丁目２－３４";
        info.tel = "03-1234-8888";
        info.fax = "03-1234-7777";
        info.todoufukencode = "13";
        info.tensuuhyoucode = "1";
        info.kikancode = "0000000";
        info.homepage = "http://example.com";
        info.doctorName = "試験 データ";
    }
}
