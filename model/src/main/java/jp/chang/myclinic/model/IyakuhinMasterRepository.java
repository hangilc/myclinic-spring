package jp.chang.myclinic.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public interface IyakuhinMasterRepository extends CrudRepository<IyakuhinMaster, IyakuhinMasterId> {

	@Query("select m from IyakuhinMaster m where m.iyakuhincode = ?1 and m.validFrom <= FUNCTION('date', ?2) " +
		"and (m.validUpto = '0000-00-00' or m.validUpto >= FUNCTION('date', ?2))")
	IyakuhinMaster findByIyakuhincodeAndDate(int iyakuhincode, Date at);

	IyakuhinMaster findTopByIyakuhincodeOrderByValidFromDesc(int iyakuhincode);

	@Query("select m.iyakuhincode from IyakuhinMaster m where m.name like CONCAT('%', :text, '%') Group By m.iyakuhincode")
	List<Integer> searchIyakuhincodeByName(@Param("text") String text);

	@Query("select m.iyakuhincode from IyakuhinMaster m where m.name like CONCAT('%', :text1, '%', :text2, '%') Group By m.iyakuhincode")
	List<Integer> searchIyakuhincodeByName(@Param("text1") String text1, @Param("text2") String text2);

	default List<IyakuhinMaster> searchByName(String text){
		List<Integer> iyakuhincodes = searchIyakuhincodeByName(text);
		return iyakuhincodes.stream()
				.map(code -> findTopByIyakuhincodeOrderByValidFromDesc(code))
				.collect(Collectors.toList());
	}

	default List<IyakuhinMaster> searchByName(String text1, String text2){
		List<Integer> iyakuhincodes = searchIyakuhincodeByName(text1, text2);
		return iyakuhincodes.stream()
				.map(code -> findTopByIyakuhincodeOrderByValidFromDesc(code))
				.collect(Collectors.toList());
	}
}