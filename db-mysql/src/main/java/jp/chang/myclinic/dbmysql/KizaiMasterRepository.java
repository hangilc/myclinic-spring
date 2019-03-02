package jp.chang.myclinic.dbmysql;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface KizaiMasterRepository extends CrudRepository<KizaiMaster, KizaiMasterId> {

	@Query("select m from KizaiMaster m where m.kizaicode = :kizaicode and m.validFrom <= FUNCTION('date', :at) " +
			"and (m.validUpto = '0000-00-00' or m.validUpto >= FUNCTION('date', :at))")
	Optional<KizaiMaster> findByKizaicodeAndDate(@Param("kizaicode") int kizaicode, @Param("at") Date at);

	@Query("select m from KizaiMaster m where m.name = :name and m.validFrom <= FUNCTION('date', :at) " +
			"and (m.validUpto = '0000-00-00' or m.validUpto >= FUNCTION('date', :at))")
	Optional<KizaiMaster> findByNameAndDate(@Param("name") String name, @Param("at") Date at);

	@Query("select m from KizaiMaster m where m.name like CONCAT('%', :text, '%') and " +
			" m.validFrom <= FUNCTION('date', :at) " +
			" and (m.validUpto = '0000-00-00' or m.validUpto >= FUNCTION('date', :at))")
	List<KizaiMaster> searchByName(@Param("text") String text, @Param("at") Date at, Sort sort);


}