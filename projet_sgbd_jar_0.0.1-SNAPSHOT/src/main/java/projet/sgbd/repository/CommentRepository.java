package projet.sgbd.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import projet.sgbd.entity.Comment;

/**
 *
 * @author sirin
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long>{
    public List<Comment> findByTexteContainingIgnoreCase(String text);  
    public List<Comment> findByDate(Date date); 

}
