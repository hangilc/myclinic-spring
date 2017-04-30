package jp.chang.myclinic.db;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
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