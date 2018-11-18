package jp.chang.myclinic.serverpostgresql.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
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
        String pat = String.format("glob:%d-hokensho-*.{jpg,jpeg,bmp}", patientId);
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher(pat);
        Path patientDir = Paths.get(storageDir, "" + patientId);
        if( Files.exists(patientDir) && Files.isDirectory(patientDir) ){
            return Files.list(patientDir)
                    .filter(p -> matcher.matches(p.getFileName()))
                    .map(p -> p.getFileName().toString())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @RequestMapping(value="/get-hokensho", method=RequestMethod.GET)
    public ResponseEntity<Resource> getHokensho(@RequestParam("patient-id") int patientId, @RequestParam("file") String file) throws IOException {
        if( storageDir == null ){
            logger.error("Failed to get property: myclinic.scanner.paper-scan-directory");
            throw new RuntimeException("Paper scan directory is not available.");
        }
        Path path = Paths.get(storageDir, "" + patientId, file);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
