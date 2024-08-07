package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.view.*;
import com.HotelApp.repository.HotelRepository;
import com.HotelApp.service.ForbiddenRequestsService;
import com.HotelApp.service.HotelService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.HotelApp.common.constants.AppConstants.*;

import static com.HotelApp.common.constants.FailConstants.ERRORS;
import static com.HotelApp.common.constants.InfoConstants.*;
import static com.HotelApp.common.constants.SuccessConstants.*;
import static com.HotelApp.common.constants.ValidationConstants.HOTEL_INFO_NOT_FOUND;
import static com.HotelApp.config.ApplicationBeanConfiguration.modelMapper;


@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final UserTransformationService userTransformationService;
    private final ForbiddenRequestsService forbiddenRequestsService;

    public HotelServiceImpl(
            HotelRepository hotelRepository,
            UserTransformationService userTransformationService,
            ForbiddenRequestsService forbiddenRequestsService
    ) {
        this.hotelRepository = hotelRepository;
        this.userTransformationService = userTransformationService;
        this.forbiddenRequestsService = forbiddenRequestsService;
    }

    @Override
    public Long getCount() {
        return hotelRepository.count();
    }

    @PostConstruct
    @Transactional
    @Override
    public void init() {
        if (hotelRepository.count() == 0) {
            HotelInfoEntity hotelInfo = new HotelInfoEntity();
            hotelInfo.setName(HOTEL_NAME);
            hotelInfo.setAddress(HOTEL_ADDRESS);
            hotelInfo.setPhoneNumber(HOTEL_PHONE);
            hotelInfo.setTotalProfit(BigDecimal.ZERO);

            hotelRepository.save(hotelInfo);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public HotelInfoEntity getHotelInfo() {
        return hotelRepository.findById(1L).orElseThrow(() -> new RuntimeException(HOTEL_INFO_NOT_FOUND));
    }

    @Transactional
    @Override
    public void takeMoney(BigDecimal vacationPrice) {
        HotelInfoEntity hotelInfo = getHotelInfo();

        hotelInfo.setTotalProfit(hotelInfo.getTotalProfit().add(vacationPrice));
        hotelRepository.save(hotelInfo);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoomView> seeAllFreeRooms() {
        return getHotelInfo()
                .getRooms()
                .stream()
                .filter(room -> !room.isReserved())
                .map(room -> modelMapper().map(room, RoomView.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<HappyGuestView> seeAllHappyGuests() {
        return getHotelInfo()
                .getHappyGuests()
                .stream()
                .map(happyGuestEntity -> modelMapper().map(happyGuestEntity, HappyGuestView.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<SubscriberView> seeAllSubscribers() {
        return getHotelInfo()
                .getSubscribers()
                .stream()
                .map(subscriberEntity -> modelMapper().map(subscriberEntity, SubscriberView.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<GuestView> seeAllGuests() {
        return getHotelInfo()
                .getGuests()
                .stream()
                .map(guest -> modelMapper().map(guest, GuestView.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentView> getAllNotApprovedComments() {
        return getHotelInfo()
                .getComments()
                .stream()
                .filter(comment -> !comment.getApproved())
                .map(comment -> modelMapper().map(comment, CommentView.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ContactRequestView> getAllNotCheckedContactRequest() {
        return getHotelInfo()
                .getContactRequests()
                .stream()
                .filter(contactRequest -> !contactRequest.getChecked())
                .map(contactRequest -> modelMapper().map(contactRequest, ContactRequestView.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserView> findAllUsers() {
        List<UserEntity> allUsers = getHotelInfo()
                .getUsers()
                .stream()
                .skip(1)
                .toList();

        return userTransformationService.transformUsers(allUsers);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OnlineReservationView> getAllNotCheckedOnlineReservations() {
        return getHotelInfo()
                .getOnlineReservations()
                .stream()
                .filter(onlineReservationEntity -> !onlineReservationEntity.isChecked())
                .map(onlineReservationEntity -> modelMapper().map(onlineReservationEntity, OnlineReservationView.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal getTotalProfit() {
        return getHotelInfo().getTotalProfit();
    }

    @Transactional
    @Override
    public void addAdminAttributes(Model model, HttpSession session, HttpServletRequest request) {
        Map<String, Integer> infoForHotel = getInfoForHotel();
        BigDecimal totalProfit = getTotalProfit();
        int forbiddenRequestsSize = forbiddenRequestsService.getAllNotChecked().size();

        model.addAttribute(HOTEL_PROFIT, totalProfit);
        model.addAllAttributes(infoForHotel);
        model.addAttribute(FORBIDDEN_REQUESTS_SIZE, forbiddenRequestsSize);

        String previousUrl = request.getHeader(REFERER);
        session.setAttribute(PREVIOUS_URL, previousUrl);
    }

    @Transactional
    @Override
    public void addModeratorAttributes(Model model) {
        Map<String, Integer> infoForHotel = getInfoForHotel();
        int allNotApprovedComments = getAllNotApprovedComments().size();
        int allContactRequests = getAllNotCheckedContactRequest().size();
        int allOnlineReservations = getAllNotCheckedOnlineReservations().size();

        model.addAllAttributes(infoForHotel);
        model.addAttribute(NOT_APPROVED_COMMENTS_SIZE, allNotApprovedComments);
        model.addAttribute(CONTACT_REQUESTS_SIZE, allContactRequests);
        model.addAttribute(ONLINE_RESERVATIONS_SIZE, allOnlineReservations);
    }


    @Transactional(readOnly = true)
    @Override
    public Map<String, Integer> getInfoForHotel() {
        Map<String, Integer> counts = new HashMap<>();
        counts.put(FREE_ROOMS_COUNT, seeAllFreeRooms().size());
        counts.put(GUESTS_COUNT, seeAllGuests().size());
        counts.put(ALL_SUBSCRIBERS_COUNT, seeAllSubscribers().size());
        counts.put(ALL_HAPPY_GUESTS_COUNT, seeAllHappyGuests().size());
        return counts;
    }

    protected static ResponseEntity<?> genericSuccessResponse(String redirectUrl) {
        Map<String, Object> response = new HashMap<>();
        response.put(SUCCESS, true);
        response.put(REDIRECT_URL, redirectUrl);
        return ResponseEntity.ok().body(response);
    }

    protected static ResponseEntity<?> genericFailResponse(BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();
        response.put(SUCCESS, false);
        response.put(ERRORS, bindingResult.getAllErrors());
        return ResponseEntity.badRequest().body(response);
    }
}
