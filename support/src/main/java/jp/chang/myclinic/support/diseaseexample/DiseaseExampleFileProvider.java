package jp.chang.myclinic.support.diseaseexample;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jp.chang.myclinic.dto.ClinicInfoDTO;
import jp.chang.myclinic.dto.DiseaseExampleDTO;
import jp.chang.myclinic.support.clinicinfo.ClinicInfoFileProvider;
import jp.chang.myclinic.support.clinicinfo.ClinicInfoProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DiseaseExampleFileProvider implements DiseaseExampleProvider {

    private Path filePath;

    public DiseaseExampleFileProvider(Path filePath) {
        this.filePath = filePath;
    }

    public static class DiseaseExampleMixin {
        @JsonProperty("adj-list")
        public List<String> adjList;
    }

    private static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    static {
        mapper.addMixIn(DiseaseExampleDTO.class, DiseaseExampleMixin.class);
    }

    @Override
    public CompletableFuture<List<DiseaseExampleDTO>> listDiseaseExample() {
        try {
            List<DiseaseExampleDTO> info = mapper.readValue(filePath.toFile(),
                    new TypeReference<List<DiseaseExampleDTO>>(){});
            return CompletableFuture.completedFuture(info);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        DiseaseExampleProvider provider = new DiseaseExampleFileProvider(Paths.get("config/disease-example.yml"));
        System.out.println(provider.listDiseaseExample().join());
    }

}
