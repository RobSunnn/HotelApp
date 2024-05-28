package com.HotelApp.service;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.models.binding.*;
import com.HotelApp.domain.models.view.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.List;

public interface HotelService {

    Long getCount();

    void init();

    HotelInfoEntity getHotelInfo();

    boolean registerUser(UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult);

    UserView findUserDetails(String userEmail);

    List<UserView> findAllUsers();

    UserView findUserByEmail(String userEmail);

    void makeUserAdmin(String email);

    void makeUserModerator(String email);

    void takeRightsOfUser(String email);

    void takeMoney(BigDecimal roomPrice);

    boolean registerGuest(AddGuestBindingModel addGuestBindingModel);

    void checkout(Integer roomNumber);

    List<RoomView> seeAllFreeRooms();

    List<HappyGuestView> seeAllHappyGuests();

    List<SubscriberView> seeAllSubscribers();

    List<GuestView> seeAllGuests();

    BigDecimal getTotalProfit();

    void addNewSubscriber(AddSubscriberBindingModel addSubscriberBindingModel);

    void addCommentToDatabase(AddCommentBindingModel addCommentBindingModel);

    List<CommentView> getAllNotApprovedComments();

    @Transactional
    Page<CommentView> getAllApprovedComments(Pageable pageable);

    void approveComment(Long id);

    void doNotApproveComment(Long id);

    void sendForm(ContactRequestBindingModel contactRequestBindingModel);

    List<ContactRequestView> getAllNotCheckedContactRequest();

    void checkedContactRequest(Long id);
}
