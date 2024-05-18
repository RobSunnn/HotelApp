package com.HotelApp.web.controller;

import com.HotelApp.domain.models.view.CommentView;
import com.HotelApp.service.CommentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/moderatorPanel")
public class ModeratorController {

    private final CommentService commentService;

    public ModeratorController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping
    public String moderatorPanel() {
        return "moderator-panel";
    }

    @GetMapping("/comments")
    public String comments(Model model) {

        List<CommentView> allNotApprovedComments = commentService.getAllNotApprovedComments();
        model.addAttribute("allNotApprovedComments", allNotApprovedComments);

        return "not-approved-comments";
    }

    @PostMapping("/approveComment")
    public String approveComment(@RequestParam("id") Long id) {

        //TODO: think of a way to handle exception when comment is not selected also for guest leave the same

        commentService.approve(id);

        return "redirect:/moderatorPanel/comments";
    }

    @PostMapping("/commentNotApproved")
    public String commentNotApproved(@RequestParam("id") Long id) {

        commentService.doNotApprove(id);

        return "redirect:/moderatorPanel/comments";
    }
}
