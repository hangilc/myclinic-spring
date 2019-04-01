package jp.chang.myclinic.support.clinicinfo;

import jp.chang.myclinic.dto.ClinicInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public interface ClinicInfoProvider {

    CompletableFuture<ClinicInfoDTO> getClinicInfo();

}
