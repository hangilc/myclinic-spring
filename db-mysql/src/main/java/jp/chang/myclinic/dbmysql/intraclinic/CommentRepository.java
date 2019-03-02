package jp.chang.myclinic.dbmysql.intraclinic;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Integer> {

    List<Comment> findByPostId(int postId, Sort sort);
}
