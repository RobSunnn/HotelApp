package com.HotelApp.web.controller;

import com.HotelApp.domain.models.view.CommentView;
import com.HotelApp.service.HotelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/moderatorPanel")
public class ModeratorController {

    private final HotelService hotelService;

    public ModeratorController(HotelService hotelService) {

        this.hotelService = hotelService;
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping
    public String moderatorPanel(Model model) {

        int allNotApprovedComments = hotelService.getAllNotApprovedComments().size();
        model.addAttribute("allNotApprovedComments", allNotApprovedComments);
        return "moderator-panel";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/comments")
    public String comments(Model model) {

        List<CommentView> allNotApprovedComments = hotelService.getAllNotApprovedComments();
        model.addAttribute("allNotApprovedComments", allNotApprovedComments);

        return "not-approved-comments";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/approveComment")
    public String approveComment(@RequestParam("id") Long id) {

        //TODO: think of a way to handle exception when comment is not selected also for guest leave the same

        hotelService.approveComment(id);

        return "redirect:/moderatorPanel/comments";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/commentNotApproved")
    public String commentNotApproved(@RequestParam("id") Long id) {

        hotelService.doNotApproveComment(id);

        return "redirect:/moderatorPanel/comments";
    }
}
