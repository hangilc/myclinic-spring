package jp.chang.myclinic.model;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.sql.Date;
import java.util.List;

public interface IyakuhinMasterRepository extends CrudRepository<IyakuhinMaster, IyakuhinMasterId> {

	@Query("select m from IyakuhinMaster m where m.iyakuhincode = ?1 and m.validFrom <= FUNCTION('date', ?2) " +
		"and (m.validUpto = '0000-00-00' or m.validUpto >= FUNCTION('date', ?2))")
	IyakuhinMaster findByIyakuhincodeAndDate(int iyakuhincode, Date at);

	IyakuhinMaster findTopByIyakuhincodeOrderByValidFromDesc(int iyakuhincode);

}