package jp.chang.myclinic.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;

public interface KizaiMasterRepository extends CrudRepository<KizaiMaster, KizaiMasterId> {

	@Query("select m from KizaiMaster m where m.kizaicode = ?1 and m.validFrom <= FUNCTION('date', ?2) " +
		"and (m.validUpto = '0000-00-00' or m.validUpto >= FUNCTION('date', ?2))")
	KizaiMaster findByKizaicodeAndDate(int kizaicode, Date at);

}