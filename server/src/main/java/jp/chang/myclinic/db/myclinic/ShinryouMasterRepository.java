package jp.chang.myclinic.db.myclinic;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;

public interface ShinryouMasterRepository extends CrudRepository<ShinryouMaster, ShinryouMasterId> {

	@Query("select m from ShinryouMaster m where m.shinryoucode = ?1 and m.validFrom <= FUNCTION('date', ?2) " +
		"and (m.validUpto = '0000-00-00' or m.validUpto >= FUNCTION('date', ?2))")
	ShinryouMaster findByShinryoucodeAndDate(int shinryoucode, Date at);

}