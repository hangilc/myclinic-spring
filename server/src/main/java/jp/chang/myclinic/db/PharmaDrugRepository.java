package jp.chang.myclinic.db;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Created by hangil on 2017/06/15.
 */
public interface PharmaDrugRepository extends CrudRepository<PharmaDrug, Integer> {

    Optional<PharmaDrug> findByIyakuhincode(int iyakuhincode);

}
