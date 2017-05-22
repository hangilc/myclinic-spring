package jp.chang.myclinic;

import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@ConfigurationProperties(prefix="myclinic.clinic")
public class ClinicInfo {

    private String name = "";

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

}