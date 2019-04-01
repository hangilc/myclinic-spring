package jp.chang.myclinic.support.clinicinfo;

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

    @Override
    public CompletableFuture<ClinicInfoDTO> getClinicInfo() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            ClinicInfoDTO info = mapper.readValue(filePath.toFile(), ClinicInfoDTO.class);
            System.out.println(info);
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
