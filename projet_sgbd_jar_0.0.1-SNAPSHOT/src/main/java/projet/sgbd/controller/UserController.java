/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projet.sgbd.controller;

import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projet.sgbd.entity.Comment;
import projet.sgbd.entity.User;
import projet.sgbd.repository.CommentRepository;
import projet.sgbd.repository.UserRepository;

/**
 *
 * @author sirin
 */
@Controller
public class UserController {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String viewUsersPage(Model model) {
        model.addAttribute("listUsers", userRepository.findAll());
        model.addAttribute("title", "Utilisateurs");

        return "users";
    }

    @GetMapping("users/newUser")
    public String viewNewUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        return "new_user";
    }

    @PostMapping("users/saveNewUser")
    public String saveNewUser(@ModelAttribute("user") @Valid User user, Errors errors, Model model) {
        String login = user.getLogin();
        // un champ est vide et login pas unique
        if (errors.hasErrors() && !userRepository.findByLogin(login).isEmpty()) {

            model.addAttribute("message", "Login déja attribué");
            model.addAttribute("user", user);
            return "new_user";
        }
        // un champs est vide et login unique
        else if (errors.hasErrors()) {
            model.addAttribute("title", "Nouvel Utilisateur");
            return "new_user";
        }
        // champs remplies et login non unique
        else if (!userRepository.findByLogin(login).isEmpty()) {
            model.addAttribute("title", "Nouvel Utilisateur");
            model.addAttribute("message", "Login déja attribué");
            model.addAttribute("user", user);
            return "new_user";
        }
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("users/updateUser")
    public String viewUpdateUser(@RequestParam(name = "id") long id, Model model) {
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if (optional.isPresent())
            user = optional.get();
        else
            throw new RuntimeException("User not found for id:: " + id);
        model.addAttribute("user", user);

        return "update_user";
    }

    @PostMapping("users/saveUpdateUser")
    public String saveUpdateUser(@ModelAttribute("user") @Valid User user, Errors errors, Model model) {
        long id = user.getId();
        String login = user.getLogin();
        int update = userRepository.findByLoginAndId(login, id).size();
        boolean notexist = userRepository.findByLogin(login).isEmpty();
        if (errors.hasErrors() && (update != 1 && notexist == false)) {
            model.addAttribute("message", "Login déja attribué");
            model.addAttribute("user", user);

            return "update_user";
        } else if (errors.hasErrors()) {
            model.addAttribute("title", "Editer Utilisateur");
            return "update_user";
        }
        // tester UniqueConstraint :
        // si le login entré est différent du login de la lgine User qu'on souahite
        // éditer et qu'il existe dans une autre ligne de la table
        else if (update != 1 && notexist == false) {
            model.addAttribute("message", "Login déja attribué");
            model.addAttribute("user", user);

            return "update_user";
        }

        // pas d'erreurs et le login entré soit existe mais uniquement dans la meme
        // ligne qu'on souhaite réecrire soit qu'il n'extiste pas dans la table
        userRepository.save(user);
        return "redirect:/users";

    }

    @GetMapping("users/deleteUser")
    public String deleteUser(@RequestParam(name = "id") long id) {
        userRepository.deleteById(id);
        return "redirect:/users";

    }

    @GetMapping("users/userComments")
    public String showUserComments(@RequestParam(name = "id") long id, Model model) {
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if (optional.isPresent())
            user = optional.get();
        else
            throw new RuntimeException("User not found for id:: " + id);

        model.addAttribute("listComments", user.getComments());
        return "user_comments";
    }

    @GetMapping("users/deleteComment")
    public String deleteComment(@RequestParam(name = "id") long id, Model model) {
        Comment comment = commentRepository.findById(id).get();
        model.addAttribute("listComments", comment.getUser().getComments());
        commentRepository.deleteById(id);
        return "user_comments";
    }

    @GetMapping("users/cancelFormU")
    public String cancelFormU() {
        return "redirect:/users";
    }

}
