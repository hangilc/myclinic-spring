package jp.chang.myclinic.serverpostgresql.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShinryouMasterRepository extends CrudRepository<ShinryouMaster, ShinryouMasterId> {

	@Query("select m from ShinryouMaster m where m.shinryoucode = :shinryoucode and m.validFrom <= :at " +
		"and (m.validUpto is null or m.validUpto >= :at)")
	Optional<ShinryouMaster> findByShinryoucodeAndDate(@Param("shinryoucode") int shinryoucode, @Param("at") LocalDate at);

	@Query("select m from ShinryouMaster m where m.shinryoucode = :shinryoucode and m.validFrom <= :at " +
			"and (m.validUpto is null or m.validUpto >= :at)")
	ShinryouMaster findOneByShinryoucodeAndDate(@Param("shinryoucode") int shinryoucode, @Param("at") LocalDate at);

	@Query("select m from ShinryouMaster m where m.name = :name and m.validFrom <= :at " +
			"and (m.validUpto is null or m.validUpto >= :at)")
	Optional<ShinryouMaster> findByNameAndDate(@Param("name") String name, @Param("at") LocalDate at);

	@Query("select m from ShinryouMaster m where m.name like CONCAT('%', :text, '%') " +
			" and m.validFrom <= :at " +
			" and (m.validUpto is null or m.validUpto >= :at) ")
	List<ShinryouMaster> search(@Param("text") String text, @Param("at") LocalDate at, Sort sort);

}