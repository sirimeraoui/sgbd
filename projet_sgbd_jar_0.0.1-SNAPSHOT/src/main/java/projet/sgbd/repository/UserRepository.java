/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.sgbd.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projet.sgbd.entity.User;

/**
 *
 * @author sirin
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    public List<User> findByLogin(String login);
    /*public List<User> findById(Long id);*/
     public List<User> findByLoginAndId(String login,Long Id);
   
}
