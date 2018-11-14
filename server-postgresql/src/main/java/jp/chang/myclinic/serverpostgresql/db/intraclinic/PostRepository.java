package jp.chang.myclinic.serverpostgresql.db.intraclinic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {

    List<Post> findTop7ByOrderByPostIdDesc();
    Page<Post> findAll(Pageable pageable);

    Post findByPostId(int id);

    void deleteByPostId(int id);
}
