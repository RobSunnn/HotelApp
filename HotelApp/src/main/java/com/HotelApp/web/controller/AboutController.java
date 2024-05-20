package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.domain.models.view.CommentView;
import com.HotelApp.service.CommentService;
import com.HotelApp.service.HotelService;
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

import java.util.List;


@Controller
@RequestMapping("/about")
public class AboutController {

    private final HotelService hotelService;

    public AboutController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
//        if (!model.containsAttribute("addSubscriberBindingModel")) {
//            model.addAttribute("addSubscriberBindingModel", new AddSubscriberBindingModel());
//        }
        if (!model.containsAttribute("addCommentBindingModel")) {
            model.addAttribute("addCommentBindingModel",new AddCommentBindingModel());
        }

    }

    @GetMapping
    public String about(Model model) {

        List<CommentView> allApprovedComments = hotelService.getAllApprovedComments();

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

        hotelService.addCommentToDatabase(addCommentBindingModel);
        redirectAttributes.addFlashAttribute("successCommentMessage", "Thank you for your comment!");

        return "redirect:/about";
    }

//    @PostMapping("/subscribe")
//    public String subscribe(@Valid AddSubscriberBindingModel addSubscriberBindingModel,
//                            BindingResult bindingResult,
//                            RedirectAttributes redirectAttributes) {
//
//        if (bindingResult.hasErrors()) {
//            redirectAttributes.addFlashAttribute(BindingConstants.SUBSCRIBER_BINDING_MODEL, addSubscriberBindingModel);
//            redirectAttributes.addFlashAttribute(BindingConstants.BINDING_RESULT_PATH + BindingConstants.SUBSCRIBER_BINDING_MODEL, bindingResult);
//
//            return "redirect:/about";
//        }
//
//        hotelService.addNewSubscriber(addSubscriberBindingModel);
//
//        redirectAttributes.addFlashAttribute("successSubscribeMessage", "Thank you for subscribing!");
//
//        return "redirect:/about";
//    }

}
