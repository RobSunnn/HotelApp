package com.HotelApp.web.controller;

import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.domain.models.view.RoomTypeView;
import com.HotelApp.service.RoomTypesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    private final RoomTypesService roomTypesService;

    public HomeController(RoomTypesService roomTypesService) {
        this.roomTypesService = roomTypesService;
    }

    @ModelAttribute
    public void addAttributes(Model model,
                              @PageableDefault(
                                      size = 5,
                                      sort = "id"
                              )
                              Pageable pageable) {

        Page<RoomTypeView> roomTypes = roomTypesService.getRoomTypes(pageable);
        int totalPages = roomTypes.getTotalPages();

        Page<RoomTypeView> lastPage = roomTypesService
                .getRoomTypes(PageRequest.of(totalPages - 1, pageable.getPageSize(), pageable.getSort()));

        model.addAttribute("allRooms", roomTypes);
        model.addAttribute("lastPageRooms", lastPage);

        if (!model.containsAttribute("addSubscriberBindingModel")) {
            model.addAttribute("addSubscriberBindingModel", new AddSubscriberBindingModel());
        }

        model.addAttribute("imageUrls", new String[]{"/images/hotel.jpg", "/images/hotel1.jpg", "/images/hotel2.jpg"});
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }


}
