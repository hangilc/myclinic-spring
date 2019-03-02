package jp.chang.myclinic.dbmysql.core;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface ShinryouMasterRepository extends CrudRepository<ShinryouMaster, ShinryouMasterId> {

	@Query("select m from ShinryouMaster m where m.shinryoucode = :shinryoucode and m.validFrom <= FUNCTION('date', :at) " +
		"and (m.validUpto = '0000-00-00' or m.validUpto >= FUNCTION('date', :at))")
	Optional<ShinryouMaster> findByShinryoucodeAndDate(@Param("shinryoucode") int shinryoucode, @Param("at") Date at);

	@Query("select m from ShinryouMaster m where m.shinryoucode = :shinryoucode and m.validFrom <= DATE(:at) " +
			"and (m.validUpto = '0000-00-00' or m.validUpto >= FUNCTION('date', :at))")
	ShinryouMaster findOneByShinryoucodeAndDate(@Param("shinryoucode") int shinryoucode, @Param("at") Date at);

	@Query("select m from ShinryouMaster m where m.name = :name and m.validFrom <= FUNCTION('date', :at) " +
			"and (m.validUpto = '0000-00-00' or m.validUpto >= FUNCTION('date', :at))")
	Optional<ShinryouMaster> findByNameAndDate(@Param("name") String name, @Param("at") Date at);

	@Query("select m from ShinryouMaster m where m.name like CONCAT('%', :text, '%') " +
			" and m.validFrom <= DATE(:at) " +
			" and (m.validUpto = '0000-00-00' or m.validUpto >= DATE(:at)) ")
	List<ShinryouMaster> search(@Param("text") String text, @Param("at") String at, Sort sort);

}