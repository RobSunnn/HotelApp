package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.view.CommentView;
import com.HotelApp.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.HotelApp.common.constants.AppConstants.COMMENTS;
import static com.HotelApp.common.constants.BindingConstants.COMMENT_BINDING_MODEL;

@Controller
@RequestMapping("/about")
public class AboutController {

    private final CommentService commentService;

    public AboutController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        if (!model.containsAttribute(COMMENT_BINDING_MODEL)) {
            model.addAttribute(COMMENT_BINDING_MODEL, new AddCommentBindingModel());
        }
    }

    @GetMapping
    public String about(
            Model model,
            @PageableDefault(size = 3, sort = "id")
            Pageable pageable
    ) {
        Page<CommentView> allApprovedComments = commentService.getApprovedComments(pageable);
        model.addAttribute(COMMENTS, allApprovedComments);
        return "about";
    }

    @PostMapping("/addComment")
    public String addComment(
            @Valid AddCommentBindingModel addCommentBindingModel,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        commentService.addCommentToDatabase(addCommentBindingModel, bindingResult, redirectAttributes);
        return "redirect:/about";
    }
}
