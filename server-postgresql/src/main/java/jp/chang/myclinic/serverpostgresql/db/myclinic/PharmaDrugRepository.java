package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PharmaDrugRepository extends CrudRepository<PharmaDrug, Integer> {

    PharmaDrug findByIyakuhincode(int iyakuhincode);

    @Query("select p from PharmaDrug p where p.iyakuhincode = :iyakuhincode")
    Optional<PharmaDrug> tryFindByIyakuhincode(@Param("iyakuhincode") int iyakuhincode);

    @Query("select p from PharmaDrug p where p.iyakuhincode in :iyakuhincodes")
    List<PharmaDrug> collectByIyakuhincodes(@Param("iyakuhincodes") List<Integer> iyakuhincodes);

    @Query("select m.iyakuhincode, m.name, m.yomi from PharmaDrug p, IyakuhinMaster m where " +
            " p.iyakuhincode = m.iyakuhincode and m.name like CONCAT('%', :text, '%') " +
            " group by m.iyakuhincode, m.name ")
    List<Object[]> searchNames(@Param("text") String text);

    @Query("select m.iyakuhincode, m.name, m.yomi from PharmaDrug p, IyakuhinMaster m where " +
            " p.iyakuhincode = m.iyakuhincode group by m.iyakuhincode, m.name, m.yomi ")
    List<Object[]> findAllPharmaDrugNames();

    PharmaDrug findById(int iyakuhincode);

    void deleteById(int iyakuhincode);
}
