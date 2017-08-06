package jp.chang.myclinic.db.myclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IyakuhinMasterRepository extends CrudRepository<IyakuhinMaster, IyakuhinMasterId> {

//	@Query("select m from IyakuhinMaster m where m.iyakuhincode = ?1 and m.validFrom <= FUNCTION('date', ?2) " +
//		"and (m.validUpto = '0000-00-00' or m.validUpto >= FUNCTION('date', ?2))")
//	IyakuhinMaster findByIyakuhincodeAndDate(int iyakuhincode, Date at);

	IyakuhinMaster findTopByIyakuhincodeOrderByValidFromDesc(int iyakuhincode);

//	@Query("select m.iyakuhincode from IyakuhinMaster m where m.name like CONCAT('%', :text, '%') Group By m.iyakuhincode")
//	List<Integer> searchIyakuhincodeByName(@Param("text") String text);
//
//	@Query("select m.iyakuhincode from IyakuhinMaster m where m.name like CONCAT('%', :text1, '%', :text2, '%') Group By m.iyakuhincode")
//	List<Integer> searchIyakuhincodeByName(@Param("text1") String text1, @Param("text2") String text2);
//
//	default List<IyakuhinMaster> searchByName(String text){
//		List<Integer> iyakuhincodes = searchIyakuhincodeByName(text);
//		return iyakuhincodes.stream()
//				.map(code -> findTopByIyakuhincodeOrderByValidFromDesc(code))
//				.collect(Collectors.toList());
//	}
//
//	default List<IyakuhinMaster> searchByName(String text1, String text2){
//		List<Integer> iyakuhincodes = searchIyakuhincodeByName(text1, text2);
//		return iyakuhincodes.stream()
//				.map(code -> findTopByIyakuhincodeOrderByValidFromDesc(code))
//				.collect(Collectors.toList());
//	}

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
}