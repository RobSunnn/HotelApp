package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.CommentEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.binding.AddCommentBindingModel;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.domain.models.binding.AddSubscriberBindingModel;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.domain.models.view.*;
import com.HotelApp.repository.HotelRepository;
import com.HotelApp.service.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static com.HotelApp.config.SecurityConfiguration.modelMapper;

@Service
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    private final UserService userService;

    private final RoomServiceImpl roomService;

    private final GuestService guestService;

    private final SubscriberService subscriberService;

    private final CommentService commentService;


    public HotelServiceImpl(HotelRepository hotelRepository,
                            UserService userService,
                            RoomServiceImpl roomService,
                            GuestService guestService,
                            SubscriberService subscriberService, CommentService commentService) {
        this.hotelRepository = hotelRepository;
        this.userService = userService;
        this.roomService = roomService;
        this.guestService = guestService;
        this.subscriberService = subscriberService;
        this.commentService = commentService;
    }

    /* Taking care of hotel info entity */
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
            hotelInfo.setName("Great Hotel");
            hotelInfo.setAddress("Somewhere");
            hotelInfo.setPhoneNumber("0987-654-321");
            hotelInfo.setTotalProfit(BigDecimal.ZERO);

            hotelRepository.save(hotelInfo);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public HotelInfoEntity getHotelInfo() {
        return hotelRepository.findById(1L).orElseThrow(() -> new RuntimeException("Hotel Info not found"));
    }

    /* EO: Taking care of hotel info entity */


    /*    User management     */

    @Transactional
    @Override
    public boolean registerUser(UserRegisterBindingModel userRegisterBindingModel,
                                BindingResult bindingResult) {
        return userService.registerUser(userRegisterBindingModel, bindingResult, getHotelInfo());
    }

    @Override
    public UserView findUserDetails(String userEmail) {
        return userService.findUserDetails(userEmail);
    }

    @Transactional
    @Override
    public List<UserView> findAllUsers() {

        return getHotelInfo()
                .getUsers().stream()
                .skip(1)
                .map(user -> modelMapper().map(user, UserView.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserView findUserByEmail(String userEmail) {
        UserEntity user = userService.findUserByEmail(userEmail);
        return modelMapper().map(user, UserView.class);
    }

    @Override
    public void makeUserAdmin(String email) {
        userService.makeUserAdmin(email);
    }

    @Override
    public void makeUserModerator(String email) {
        userService.makeUserModerator(email);
    }

    @Override
    public void takeRightsOfUser(String email) {
        userService.takeRights(email);
    }

    /*  EO:  User management     */

    /*  Hotel takes the guest money  */
    @Transactional
    @Override
    public void takeMoney(BigDecimal roomPrice) {
        HotelInfoEntity hotelInfo = getHotelInfo();

        hotelInfo.setTotalProfit(hotelInfo.getTotalProfit().add(roomPrice));
        hotelRepository.save(hotelInfo);
    }



    /*    Guest management     */

    @Transactional
    @Override
    public boolean registerGuest(AddGuestBindingModel addGuestBindingModel) {
        RoomEntity room = roomService.findByRoomNumber(addGuestBindingModel.getRoomNumber());
        takeMoney(room.getPrice().multiply(BigDecimal.valueOf(addGuestBindingModel.getDaysToStay())));
        room.setReserved(true);
        roomService.saveRoom(room);

        return guestService.registerGuest(addGuestBindingModel, getHotelInfo());
    }

    @Transactional
    @Override
    public void checkout(Integer roomNumber) {
        RoomEntity guestRoom = roomService.findByRoomNumber(roomNumber);
        guestRoom.setReserved(false);
        roomService.saveRoom(guestRoom);

        guestService.guestWantToLeave(guestRoom, getHotelInfo());
    }

    /*  EO:  Guest management     */


    /* ADMIN PAGE INFO */

    @Transactional
    @Override
    public List<RoomView> seeAllFreeRooms() {
        return getHotelInfo()
                .getRooms()
                .stream()
                .filter(room -> !room.isReserved())
                .map(room -> modelMapper().map(room, RoomView.class))
                .toList();
    }

    @Transactional
    @Override
    public List<HappyGuestView> seeAllHappyGuests() {
        return getHotelInfo()
                .getHappyGuests()
                .stream()
                .map(happyGuestEntity -> modelMapper().map(happyGuestEntity, HappyGuestView.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<SubscriberView> seeAllSubscribers() {
        return getHotelInfo()
                .getSubscribers()
                .stream()
                .map(subscriberEntity -> modelMapper().map(subscriberEntity, SubscriberView.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<GuestView> seeAllGuests() {
        return getHotelInfo()
                .getGuests()
                .stream()
                .map(guest -> modelMapper().map(guest, GuestView.class))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BigDecimal getTotalProfit() {
        return getHotelInfo().getTotalProfit();
    }

    /* EO: ADMIN PAGE INFO */


    @Transactional
    @Override
    public void addNewSubscriber(AddSubscriberBindingModel addSubscriberBindingModel) {
        subscriberService.addNewSubscriber(addSubscriberBindingModel, getHotelInfo());
    }

    @Transactional
    @Override
    public void addCommentToDatabase(AddCommentBindingModel addCommentBindingModel) {
        commentService.addCommentToDatabase(addCommentBindingModel, getHotelInfo());
    }

    @Transactional
    @Override
    public List<CommentView> getAllNotApprovedComments() {
        return getHotelInfo()
                .getComments()
                .stream()
                .filter(comment -> !comment.getApproved())
                .map(comment -> modelMapper().map(comment, CommentView.class))
                .toList();
    }

    @Transactional
    @Override
    public List<CommentView> getAllApprovedComments() {
        return getHotelInfo()
                .getComments()
                .stream()
                .filter(CommentEntity::getApproved)
                .map(comment -> modelMapper().map(comment, CommentView.class))
                .toList();
    }

    @Override
    public void approveComment(Long id) {
        commentService.approve(id);
    }

    @Override
    public void doNotApproveComment(Long id) {
        commentService.doNotApprove(id);
    }
}
