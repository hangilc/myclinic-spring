package jp.chang.myclinic.server.db.myclinic;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by hangil on 2017/06/07.
 */
public interface TextRepository extends CrudRepository<Text, Integer> {

    List<Text> findByVisitId(int visitId);
}
