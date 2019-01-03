package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface KizaiMasterRepository extends CrudRepository<KizaiMaster, KizaiMasterId> {

	@Query("select m from KizaiMaster m where m.kizaicode = :kizaicode and m.validFrom <= :at " +
			"and (m.validUpto is null or m.validUpto >= :at)")
	Optional<KizaiMaster> findByKizaicodeAndDate(@Param("kizaicode") int kizaicode, @Param("at") LocalDate at);

	@Query("select m from KizaiMaster m where m.name = :name and m.validFrom <= :at " +
			"and (m.validUpto is null or m.validUpto >= :at)")
	Optional<KizaiMaster> findByNameAndDate(@Param("name") String name, @Param("at") LocalDate at);

	@Query("select m from KizaiMaster m where m.name like CONCAT('%', :text, '%') and " +
			" m.validFrom <= :at " +
			" and (m.validUpto is null or m.validUpto >= :at)")
	List<KizaiMaster> searchByName(@Param("text") String text, @Param("at") LocalDate at, Sort sort);


}