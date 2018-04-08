package jp.chang.myclinic.server.db.intraclinic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<Post, Integer> {

    List<Post> findTop7ByOrderByIdDesc();
    Page<Post> findAll(Pageable pageable);

    Post findById(int id);

    void deleteById(int id);
}
