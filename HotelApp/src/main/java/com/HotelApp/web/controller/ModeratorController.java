package com.HotelApp.web.controller;

import com.HotelApp.domain.models.view.CommentView;
import com.HotelApp.domain.models.view.ContactRequestView;
import com.HotelApp.service.CommentService;
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

    private final CommentService commentService;

    public ModeratorController(HotelService hotelService,
                               CommentService commentService) {
        this.hotelService = hotelService;
        this.commentService = commentService;
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping
    public String moderatorPanel(Model model) {

        int allNotApprovedComments = hotelService.getAllNotApprovedComments().size();
        int allContactRequests = hotelService.getAllNotCheckedContactRequest().size();

        model.addAttribute("allNotApprovedComments", allNotApprovedComments);
        model.addAttribute("allContactRequests", allContactRequests);

        return "moderator/moderator-panel";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/comments")
    public String comments(Model model) {

        List<CommentView> allNotApprovedComments = hotelService.getAllNotApprovedComments();
        if (allNotApprovedComments.isEmpty()) {
            return "redirect:/moderatorPanel";
        }

        model.addAttribute("allNotApprovedComments", allNotApprovedComments);

        return "moderator/not-approved-comments";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/contactRequests")
    public String contactRequest(Model model) {

        List<ContactRequestView> allNotCheckedContactRequest = hotelService.getAllNotCheckedContactRequest();
        if (allNotCheckedContactRequest.isEmpty()) {
            return "redirect:/moderatorPanel";
        }

        model.addAttribute("allNotCheckedContactRequest", allNotCheckedContactRequest);
        return "moderator/all-contact-requests";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/approveComment")
    public String approveComment(@RequestParam("id") Long id) {

        commentService.approve(id);
        return "redirect:/moderatorPanel/comments";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/requestChecked")
    public String checkContactRequest(@RequestParam("id") Long id) {

        hotelService.checkedContactRequest(id);
        return "redirect:/moderatorPanel/contactRequests";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/commentNotApproved")
    public String commentNotApproved(@RequestParam("id") Long id) {

        commentService.doNotApprove(id);
        return "redirect:/moderatorPanel/comments";
    }
}
