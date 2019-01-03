package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public interface WqueueRepository extends CrudRepository<Wqueue, Integer> {

    @Query("select q from Wqueue q where q.waitState in ?1")
    List<Wqueue> findByStateSet(Set<Integer> stateSet, Sort sort);

    @Query("select q from Wqueue q")
    Stream<Wqueue> findAllAsStream();

    @Query("select wq from Wqueue wq where wq.visitId = :visitId")
    Optional<Wqueue> tryFindByVisitId(@Param("visitId") int visitId);

    Wqueue findOneByVisitId(int visitId);

    @Query("select q, p, v from Wqueue q, Patient p, Visit v " +
            " where q.waitState in :waitStates and v.visitId = q.visitId " +
            " and p.patientId = v.patientId ")
    List<Object[]> findFullByStateSet(@Param("waitStates") Set<Integer> waitStates, Sort sort);

    List<Wqueue> findAll();
}