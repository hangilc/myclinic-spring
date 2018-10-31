package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IyakuhinMasterRepository extends CrudRepository<IyakuhinMaster, IyakuhinMasterId> {

	IyakuhinMaster findTopByIyakuhincodeOrderByValidFromDesc(int iyakuhincode);

	@Query("select m.iyakuhincode, m.name from IyakuhinMaster m where iyakuhincode in :iyakuhincodes " +
			" group by m.iyakuhincode, m.name ")
	List<Object[]> findNameForIyakuhincode(@Param("iyakuhincodes") List<Integer> iyakuhincodes, Sort sort);

	@Query("select m from IyakuhinMaster m where m.name like CONCAT('%', :text, '%') and " +
			" m.validFrom <= :at and (m.validUpto is null or m.validUpto >= :at)")
	List<IyakuhinMaster> searchByName(@Param("text") String text, @Param("at") LocalDate at, Sort sort);

	@Query("select m from IyakuhinMaster m where m.iyakuhincode = :iyakuhincode and " +
			" m.validFrom <= DATE(:at) " +
			" and (m.validUpto is null or m.validUpto >= DATE(:at)) ")
	Optional<IyakuhinMaster> tryFind(@Param("iyakuhincode") int iyakuhincode, @Param("at") LocalDate at);

	@Query("select m from IyakuhinMaster m where m.iyakuhincode = :iyakuhincode " +
			" and m.validFrom <= DATE(:at) " +
			" and (m.validUpto is null or m.validUpto >= DATE(:at) )")
	Optional<IyakuhinMaster> findByIyakuhincodeAndDate(@Param("iyakuhincode") int iyakuhincode,
                                                       @Param("at") LocalDate at);
}