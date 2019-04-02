package jp.chang.myclinic.support.clinicinfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jp.chang.myclinic.dto.ClinicInfoDTO;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

public class ClinicInfoFileProvider implements ClinicInfoProvider {

    private Path filePath;

    public ClinicInfoFileProvider(Path filePath){
        this.filePath = filePath;
    }

    public static class ClinicInfoMixin {
        @JsonProperty("postal-code")
        public String postalCode;
        @JsonProperty("doctor-name")
        public String doctorName;
    }

    private static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    static {
        mapper.addMixIn(ClinicInfoDTO.class, ClinicInfoMixin.class);
    }

    @Override
    public CompletableFuture<ClinicInfoDTO> getClinicInfo() {
        try {
            ClinicInfoDTO info = mapper.readValue(filePath.toFile(), ClinicInfoDTO.class);
            return CompletableFuture.completedFuture(info);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        ClinicInfoProvider provider = new ClinicInfoFileProvider(Paths.get("config/clinic-info.yml"));
        System.out.println(provider.getClinicInfo().join());
    }
}
