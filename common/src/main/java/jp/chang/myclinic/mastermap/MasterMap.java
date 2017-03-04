package jp.chang.myclinic.mastermap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by hangil on 2017/03/03.
 */
@Component
public class MasterMap {
    @Value("${myclinic.mastermap.location:}")
    private String location;

    public String getLocation(){
        return location;
    }
}
