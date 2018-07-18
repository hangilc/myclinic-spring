package jp.chang.myclinic.tracker.model;

import jp.chang.myclinic.dto.VisitDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Visit {

    private static DateTimeFormatter sqlFormatter = DateTimeFormatter.ofPattern("yyyy-LL-dd HH:mm:ss");
    private ModelRegistry registry;
    private int visitId;
    private LocalDateTime visitedAt;
    private int patientId;
    private Patient patient;

    public Visit(ModelRegistry registry, VisitDTO dto) {
        this.registry = registry;
        this.visitId = dto.visitId;
        this.visitedAt = LocalDateTime.parse(dto.visitedAt, sqlFormatter);
        this.patientId = dto.patientId;
    }

    public Patient getPatient(){
        if( patient == null ){
            this.patient = registry.getPatient(patientId);
        }
        return patient;
    }

}
