package jp.chang.myclinic.practice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jp.chang.myclinic.dto.ReferItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class ReferServiceFile implements ReferService {

    private List<ReferItemDTO> refers;

    public ReferServiceFile(Path dataFile) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            this.refers = mapper.readValue(dataFile.toFile(),
                new TypeReference<List<ReferItemDTO>>(){});
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public List<ReferItemDTO> listRefer() {
        return refers;
    }
}
