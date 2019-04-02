package jp.chang.myclinic.support.clinicinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jp.chang.myclinic.dto.ClinicInfoDTO;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClinicInfoFileProvider implements ClinicInfoProvider {

    public static class ClinicInfoMixin {
        @JsonProperty("postal-code")
        public String postalCode;
        @JsonProperty("doctor-name")
        public String doctorName;
    }

    private ClinicInfoDTO clinicInfo;

    public ClinicInfoFileProvider(Path filePath){
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            mapper.addMixIn(ClinicInfoDTO.class, ClinicInfoMixin.class);
            this.clinicInfo = mapper.readValue(filePath.toFile(), ClinicInfoDTO.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClinicInfoDTO getClinicInfo() {
        return clinicInfo;
    }

    public static void main(String[] args){
        ClinicInfoProvider provider = new ClinicInfoFileProvider(Paths.get("config/clinic-info.yml"));
        System.out.println(provider.getClinicInfo());
    }
}
