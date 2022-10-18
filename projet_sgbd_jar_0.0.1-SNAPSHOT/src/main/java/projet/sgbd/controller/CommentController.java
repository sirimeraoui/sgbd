/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.sgbd.controller;

/**
 *
 * @author sirin
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projet.sgbd.entity.Comment;
import projet.sgbd.entity.User;
import projet.sgbd.repository.CommentRepository;
import projet.sgbd.repository.UserRepository;

@Controller
@RequestMapping(value = "/")
public class CommentController {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    /*
     * @GetMapping("/Comments/filters")
     * public String viewFilteredComments(Model
     * model, @RequestParam(value="search-content") final String content,
     * 
     * @RequestParam(value="filters") final String filter) throws ParseException
     * {
     * if(filter == "textContent")
     * {
     * model.addAttribute("listComments",commentRepository.findByTexteContaining(
     * content));
     * model.addAttribute("title","Comentaires");
     * return "index";
     * }else {
     * SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
     * Date date = formatter.parse(content);
     * model.addAttribute("listComments",commentRepository.findByDate(date));
     * model.addAttribute("title","Comentaires");
     * return "index";
     * }
     * }
     * }
     */
    @GetMapping("/")
    public String rootPage(Model model) {
        return "redirect:/comments";
    }

    @GetMapping("/comments")
    public String viewHomePage(Model model) {
        model.addAttribute("listComments", commentRepository.findAll());
        model.addAttribute("title", "Commentaires");
        return "index";
    }

    @GetMapping("comments/filter1")
    public String viewFilter1(Model model, @RequestParam(name = "text-content", defaultValue = "") String content) {
        if (content.isEmpty()) {
            return "redirect:/comments";
        }
        model.addAttribute("listComments", commentRepository.findByTexteContainingIgnoreCase(content));
        model.addAttribute("filter1", content);
        model.addAttribute("type", "text");
        return "index";
    }

    @GetMapping("comments/filter2")
    public String viewFilter2(Model model, @RequestParam(name = "date-content", defaultValue = "") String content)
            throws ParseException {
        if (!content.equals("")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            model.addAttribute("listComments", commentRepository.findByDate(sdf.parse(content)));
        } else {
            model.addAttribute("listComments", commentRepository.findAll());
        }
        model.addAttribute("type", "date");
        model.addAttribute("filter2", content);
        return "index";
    }

    @GetMapping("comments/newComment")
    public String viewNewComment(Model model) {
        Comment comment = new Comment();
        model.addAttribute("comment", comment);
        model.addAttribute("title", "Nouveau Commentaire");
        model.addAttribute("listUsers", userRepository.findAll());
        return "new_comment";
    }

    @PostMapping("comments/saveNewComment")
    public String saveNewComment(@ModelAttribute("comment") @Valid Comment comment, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("listUsers", userRepository.findAll());
            return "new_comment";
        }
        commentRepository.save(comment);
        return "redirect:/";
    }

    @GetMapping("comments/updateComment")
    public String viewUpdateComment(@RequestParam(name = "id") long id, Model model) {
        Optional<Comment> optional = commentRepository.findById(id);
        Comment comment = null;
        if (optional.isPresent())
            comment = optional.get();
        else
            throw new RuntimeException("Comment not found for id:: " + id);
        model.addAttribute("title", "Mise à jour Commentaire");
        model.addAttribute("listUsers", userRepository.findAll());
        model.addAttribute("comment", comment);
        return "update_comment";

    }

    @PostMapping("comments/saveUpdateComment")
    public String saveUpdateComment(@ModelAttribute("comment") @Valid Comment comment, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("listUsers", userRepository.findAll());
            return "update_comment";
        }
        // save employee to DB
        // might need to save the comment to the user.comments' List next
        commentRepository.save(comment);
        return "redirect:/";
    }

    @GetMapping("comments/deleteComment")
    public String deleteComment(@RequestParam(name = "id") long id) {
        commentRepository.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("comments/cancelFormC")
    public String cancelFormC() {
        return "redirect:/comments";
    }

}
