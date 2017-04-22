package jp.chang.myclinic.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PharmaDrugRepository extends CrudRepository<PharmaDrug, Integer> {

    @Query("select p.iyakuhincode from PharmaDrug p, IyakuhinMaster m where p.iyakuhincode = m.iyakuhincode " +
        " and m.name like CONCAT('%', :text, '%') Group By p.iyakuhincode")
    List<Integer> searchIyakuhincodeByName(@Param("text") String text);

}