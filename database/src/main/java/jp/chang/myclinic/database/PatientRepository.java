package jp.chang.myclinic.database;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PatientRepository extends CrudRepository<Patient, Integer> {

    @Query("select p from Patient p where p.lastName like CONCAT('%', ?1, '%') " +
            " or p.firstName like CONCAT('%', ?1, '%') " +
            " or p.lastNameYomi like CONCAT('%', ?1, '%') " +
            " or p.firstNameYomi like CONCAT('%', ?1, '%') "
    )
    List<Patient> searchPatientByName(String text, Pageable pageable);

    @Query("select p from Patient p where " +
            " (p.lastName like CONCAT('%', ?1, '%') or p.lastNameYomi like CONCAT('%', ?1, '%')) and " +
            " (p.firstName like CONCAT('%', ?2, '%') or p.firstNameYomi like CONCAT('%', ?2, '%')) "
    )
    List<Patient> searchPatientByName(String text1, String text2, Pageable pageable);

}