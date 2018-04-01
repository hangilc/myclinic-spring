package jp.chang.myclinic.server;

import jp.chang.myclinic.dto.PracticeConfigDTO;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="myclinic.practice")
public class PracticeConfig {

    private String kouhatsuKasan;

    public String getKouhatsuKasan() {
        return kouhatsuKasan;
    }

    public void setKouhatsuKasan(String kouhatsuKasan) {
        this.kouhatsuKasan = kouhatsuKasan;
    }

    public PracticeConfigDTO toDTO(){
        PracticeConfigDTO dto = new PracticeConfigDTO();
        dto.kouhatsuKasan = getKouhatsuKasan();
        return dto;
    }
}
