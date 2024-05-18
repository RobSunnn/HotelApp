package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.SubscriberEntity;
import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.service.CommentService;
import com.HotelApp.service.SubscriberService;
import com.HotelApp.validation.constants.BindingConstants;
import jakarta.validation.Valid;
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

    private final SubscriberService subscriberService;

    private final CommentService commentService;

    public AboutController(SubscriberService subscriberService,
                           CommentService commentService) {
        this.subscriberService = subscriberService;
        this.commentService = commentService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        if (!model.containsAttribute("addSubscriberBindingModel")) {
            model.addAttribute("addSubscriberBindingModel", new AddSubscriberBindingModel());
        }
        if (!model.containsAttribute("addCommentBindingModel")) {
            model.addAttribute("addCommentBindingModel",new AddCommentBindingModel());
        }

    }

    @GetMapping
    public String about() {
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

    @PostMapping("/subscribe")
    public String subscribe(@Valid AddSubscriberBindingModel addSubscriberBindingModel,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BindingConstants.SUBSCRIBER_BINDING_MODEL, addSubscriberBindingModel);
            redirectAttributes.addFlashAttribute(BindingConstants.BINDING_RESULT_PATH + BindingConstants.SUBSCRIBER_BINDING_MODEL, bindingResult);

            return "redirect:/about";
        }

        subscriberService.addNewSubscriber(addSubscriberBindingModel);

        redirectAttributes.addFlashAttribute("successSubscribeMessage", "Thank you for subscribing!");

        return "redirect:/about";
    }

}
