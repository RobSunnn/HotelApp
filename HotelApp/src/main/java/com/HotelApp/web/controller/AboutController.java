package com.HotelApp.web.controller;

import com.HotelApp.common.constants.BindingConstants;
import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.view.CommentView;
import com.HotelApp.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
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

@Controller
@RequestMapping("/about")
public class AboutController {

    private final CommentService commentService;

    public AboutController(CommentService commentService) {
        this.commentService = commentService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        if (!model.containsAttribute("addCommentBindingModel")) {
            model.addAttribute("addCommentBindingModel", new AddCommentBindingModel());
        }
    }

    @GetMapping
    public String about(Model model,
                        @PageableDefault(
                                size = 3,
                                sort = "id"
                        )
                        Pageable pageable, HttpServletRequest request) {

        request.getSession(true);
        Page<CommentView> allApprovedComments = commentService.getApprovedComments(pageable);

        model.addAttribute("comments", allApprovedComments);

        return "about";
    }

    @PostMapping("/addComment")
    public String addComment(@Valid AddCommentBindingModel addCommentBindingModel,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addCommentBindingModel", addCommentBindingModel);
            redirectAttributes.addFlashAttribute(BindingConstants.BINDING_RESULT_PATH + "addCommentBindingModel", bindingResult);
            return "redirect:/about";
        }

        commentService.addCommentToDatabase(addCommentBindingModel);
        redirectAttributes.addFlashAttribute("successCommentMessage", "Thank you for your comment!");

        return "redirect:/about";
    }

}
