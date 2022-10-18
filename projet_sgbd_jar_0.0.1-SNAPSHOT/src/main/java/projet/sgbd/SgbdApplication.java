package projet.sgbd;

import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import projet.sgbd.entity.Comment;
import projet.sgbd.entity.User;
import projet.sgbd.repository.CommentRepository;
import projet.sgbd.repository.UserRepository;

@SpringBootApplication
public class SgbdApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(SgbdApplication.class, args);
	}
        
    @Autowired
    private CommentRepository commentRepo;
      @Autowired
    private UserRepository userRepo;  
    @Override
    public void run(String... args) throws Exception {
     
    }
}
