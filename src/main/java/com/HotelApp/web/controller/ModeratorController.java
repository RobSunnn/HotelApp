package com.HotelApp.web.controller;

import com.HotelApp.domain.models.view.CommentView;
import com.HotelApp.domain.models.view.ContactRequestView;
import com.HotelApp.service.CommentService;
import com.HotelApp.service.ContactRequestService;
import com.HotelApp.service.HotelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {

    private final HotelService hotelService;

    private final CommentService commentService;

    private final ContactRequestService contactRequestService;

    public ModeratorController(HotelService hotelService,
                               CommentService commentService, ContactRequestService contactRequestService) {
        this.hotelService = hotelService;
        this.commentService = commentService;
        this.contactRequestService = contactRequestService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        Map<String, Integer> infoForHotel = hotelService.getInfoForHotel();
        model.addAllAttributes(infoForHotel);
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
            return "redirect:/moderator";
        }

        model.addAttribute("allNotApprovedComments", allNotApprovedComments);
        return "moderator/not-approved-comments";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @GetMapping("/contactRequests")
    public String contactRequest(Model model) {
        List<ContactRequestView> allNotCheckedContactRequest = hotelService.getAllNotCheckedContactRequest();
        if (allNotCheckedContactRequest.isEmpty()) {
            return "redirect:/moderator";
        }

        model.addAttribute("allNotCheckedContactRequest", allNotCheckedContactRequest);
        return "moderator/all-contact-requests";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/approveComment")
    public String approveComment(@RequestParam("id") Long id) {
        commentService.approve(id);
        return "redirect:/moderator/comments";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/approveAll")
    public String approveAllComments() {
        commentService.approveAll();
        return "redirect:/moderator/comments";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/commentNotApproved")
    public String commentNotApproved(@RequestParam("id") Long id) {
        commentService.doNotApprove(id);
        return "redirect:/moderator/comments";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/requestChecked")
    public String checkContactRequest(@RequestParam("id") Long id) {
        contactRequestService.checkedContactRequest(id);
        return "redirect:/moderator/contactRequests";
    }

    @PreAuthorize("hasRole('MODERATOR')")
    @PostMapping("/allRequestsChecked")
    public String allContactRequestsChecked() {
        contactRequestService.allRequestsChecked();
        return "redirect:/moderator/contactRequests";
    }
}
