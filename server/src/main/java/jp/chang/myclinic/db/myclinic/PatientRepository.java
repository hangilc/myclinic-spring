package jp.chang.myclinic.db.myclinic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

interface PatientRepository extends CrudRepository<Patient, Integer> {

    Stream<Patient> findByLastNameContaining(String text, Sort sort);
    Stream<Patient> findByFirstNameContaining(String text, Sort sort);
    Stream<Patient> findByLastNameYomiContaining(String text, Sort sort);
    Stream<Patient> findByFirstNameYomiContaining(String text, Sort sort);

    @Query("select p from Patient p where p.lastName like CONCAT('%', ?1, '%') " +
            " and p.firstName like CONCAT('%', ?2, '%') ")
    Stream<Patient> searchPatientByName(String lastName, String firstName, Sort sort);

    @Query("select p from Patient p where p.lastNameYomi like CONCAT('%', ?1, '%') " +
            " and p.firstNameYomi like CONCAT('%', ?2, '%') ")
    Stream<Patient> searchPatientByYomi(String lastNameYomi, String firstNameYomi, Sort sort);

    Page<Patient> findAll(Pageable pageable);

    @Query("select p from Patient p where p.patientId = :patientId")
    Optional<Patient> tryFind(@Param("patientId") int patientId);

    @Query("select p from Patient p where p.lastName like CONCAT('%', :text, '%') or " +
            " p.lastNameYomi like CONCAT('%', :text, '%') or " +
            " p.firstName like CONCAT('%', :text, '%') or " +
            " p.firstNameYomi like CONCAT('%', :text, '%')")
    List<Patient> searchPatient(@Param("text") String text, Sort sort);

    @Query("select p from Patient p where (p.lastName like CONCAT('%', :textLastName, '%') or " +
            " p.lastNameYomi like CONCAT('%', :textLastName, '%') ) and " +
            " (p.firstName like CONCAT('%', :textFirstName, '%') or " +
            " p.firstNameYomi like CONCAT('%', :textFirstName, '%') )")
    List<Patient> searchPatient(@Param("textLastName") String text1, @Param("textFirstName") String text2, Sort sort);

    // @Query("select p from Patient p where p.lastName like CONCAT('%', ?1, '%') " +
    //         " or p.firstName like CONCAT('%', ?1, '%') " +
    //         " or p.lastNameYomi like CONCAT('%', ?1, '%') " +
    //         " or p.firstNameYomi like CONCAT('%', ?1, '%') "
    // )
    // List<Patient> searchPatientByName(String text, Pageable pageable);

    // @Query("select p from Patient p where " +
    //         " (p.lastName like CONCAT('%', ?1, '%') or p.lastNameYomi like CONCAT('%', ?1, '%')) and " +
    //         " (p.firstName like CONCAT('%', ?2, '%') or p.firstNameYomi like CONCAT('%', ?2, '%')) "
    // )
    // List<Patient> searchPatientByName(String text1, String text2, Pageable pageable);

}