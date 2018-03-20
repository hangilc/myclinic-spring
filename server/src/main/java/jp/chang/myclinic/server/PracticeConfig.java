package jp.chang.myclinic.server;

import jp.chang.myclinic.dto.PracticeConfigDTO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="myclinic.practice")
public class PracticeConfig {

    private boolean kouhatsuKasan;

    public boolean isKouhatsuKasan() {
        return kouhatsuKasan;
    }

    public void setKouhatsuKasan(boolean kouhatsuKasan) {
        this.kouhatsuKasan = kouhatsuKasan;
    }

    public PracticeConfigDTO toDTO(){
        PracticeConfigDTO dto = new PracticeConfigDTO();
        dto.kouhatsuKasan = isKouhatsuKasan();
        return dto;
    }
}
