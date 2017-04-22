package jp.chang.myclinic.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface TextRepository extends CrudRepository<Text, Integer> {

	List<Text> findAllByVisitId(int visitId);
}