package jp.chang.myclinic.dbmysql.core;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface IyakuhinMasterRepository extends CrudRepository<IyakuhinMaster, IyakuhinMasterId> {

	IyakuhinMaster findTopByIyakuhincodeOrderByValidFromDesc(int iyakuhincode);

	@Query("select m.iyakuhincode, m.name from IyakuhinMaster m where iyakuhincode in :iyakuhincodes " +
			" group by m.iyakuhincode, m.name ")
	List<Object[]> findNameForIyakuhincode(@Param("iyakuhincodes") List<Integer> iyakuhincodes, Sort sort);

	@Query("select m from IyakuhinMaster m where m.name like CONCAT('%', :text, '%') and " +
			" m.validFrom <= :at and (m.validUpto = '0000-00-00' or m.validUpto >= :at)")
	List<IyakuhinMaster> searchByName(@Param("text") String text, @Param("at") String at, Sort sort);

	@Query("select m from IyakuhinMaster m where m.iyakuhincode = :iyakuhincode and " +
			" m.validFrom <= DATE(:at) " +
			" and (m.validUpto = '0000-00-00' or m.validUpto >= DATE(:at)) ")
	Optional<IyakuhinMaster> tryFind(@Param("iyakuhincode") int iyakuhincode, @Param("at") String at);

	@Query("select m from IyakuhinMaster m where m.iyakuhincode = :iyakuhincode " +
			" and m.validFrom <= DATE(:at) " +
			" and (m.validUpto = '0000-00-00' or m.validUpto >= DATE(:at) )")
	Optional<IyakuhinMaster> findByIyakuhincodeAndDate(@Param("iyakuhincode") int iyakuhincode,
                                                       @Param("at") Date at);
}