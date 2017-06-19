package jp.chang.myclinic.db;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by hangil on 2017/06/15.
 */
public interface PharmaDrugRepository extends CrudRepository<PharmaDrug, Integer> {

    Optional<PharmaDrug> findByIyakuhincode(int iyakuhincode);

    @Query("select p from PharmaDrug p where p.iyakuhincode in :iyakuhincodes")
    List<PharmaDrug> collectByIyakuhincodes(@Param("iyakuhincodes") Set<Integer> iyakuhincodes);
}
