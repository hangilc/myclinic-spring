package jp.chang.myclinic.db.myclinic;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.Optional;

public interface KizaiMasterRepository extends CrudRepository<KizaiMaster, KizaiMasterId> {

	@Query("select m from KizaiMaster m where m.kizaicode = :kizaicode and m.validFrom <= FUNCTION('date', :at) " +
			"and (m.validUpto = '0000-00-00' or m.validUpto >= FUNCTION('date', :at))")
	Optional<KizaiMaster> findByKizaicodeAndDate(@Param("kizaicode") int kizaicode, @Param("at") Date at);

	@Query("select m from KizaiMaster m where m.name = :name and m.validFrom <= FUNCTION('date', :at) " +
			"and (m.validUpto = '0000-00-00' or m.validUpto >= FUNCTION('date', :at))")
	Optional<KizaiMaster> findByNameAndDate(@Param("name") String name, @Param("at") Date at);

}