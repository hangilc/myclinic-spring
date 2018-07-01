package jp.chang.myclinic.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/json")
class HokenshoController {

    private static Logger logger = LoggerFactory.getLogger(HokenshoController.class);

    @Value("${myclinic.scanner.paper-scan-directory:#{null}}")
    private String storageDir;

    @RequestMapping(value="/list-hokensho", method=RequestMethod.GET)
    public List<String> listHokensho(@RequestParam("patient-id") int patientId) throws IOException {
        if( storageDir == null ){
            logger.error("Failed to get property: myclinic.scanner.paper-scan-directory");
            throw new RuntimeException("Paper scan directory is not available.");
        }
        String pat = String.format("glob:%d-hokensho-*.{jpg,jpeg}", patientId);
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pat);
        return Files.list(Paths.get(storageDir, "" + patientId))
                .filter(p -> matcher.matches(p.getFileName()))
                .map(p -> p.getFileName().toString())
                .collect(Collectors.toList());
    }
}
