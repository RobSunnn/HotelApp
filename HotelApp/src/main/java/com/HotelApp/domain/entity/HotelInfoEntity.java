package com.HotelApp.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotel_info")
public class HotelInfoEntity extends BaseEntity {

    private String name;

    private String address;

    private String phoneNumber;

    private BigDecimal totalProfit;

    @OneToMany(mappedBy = "hotelInfoEntity", fetch = FetchType.EAGER)
    private List<RoomEntity> rooms;

    @OneToMany(mappedBy = "hotelInfoEntity", fetch = FetchType.LAZY)
    private List<UserEntity> users;

    @OneToMany(mappedBy = "hotelInfoEntity", fetch = FetchType.LAZY)
    private List<GuestEntity> guests;

    @OneToMany(mappedBy = "hotelInfoEntity", fetch = FetchType.LAZY)
    private List<HappyGuestEntity> happyGuests;

    @OneToMany(mappedBy = "hotelInfoEntity", fetch = FetchType.LAZY)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "hotelInfoEntity", fetch = FetchType.LAZY)
    private List<SubscriberEntity> subscribers;

    @OneToMany(mappedBy = "hotelInfoEntity", fetch = FetchType.LAZY)
    private List<ContactRequestEntity> contactRequests;

    public HotelInfoEntity() {
        this.rooms = new ArrayList<>();
        this.users = new ArrayList<>();
        this.guests = new ArrayList<>();
        this.happyGuests = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.subscribers = new ArrayList<>();
        this.contactRequests = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public HotelInfoEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public HotelInfoEntity setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public HotelInfoEntity setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public BigDecimal getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit = totalProfit;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public HotelInfoEntity setUsers(List<UserEntity> users) {
        this.users = users;
        return this;
    }

    public List<CommentEntity> getComments() {
        return comments;
    }

    public HotelInfoEntity setComments(List<CommentEntity> comments) {
        this.comments = comments;
        return this;
    }

    public List<SubscriberEntity> getSubscribers() {
        return subscribers;
    }

    public HotelInfoEntity setSubscribers(List<SubscriberEntity> subscribers) {
        this.subscribers = subscribers;
        return this;
    }

    public List<RoomEntity> getRooms() {
        return rooms;
    }

    public HotelInfoEntity setRooms(List<RoomEntity> rooms) {
        this.rooms = rooms;
        return this;
    }

    public List<GuestEntity> getGuests() {
        return guests;
    }

    public HotelInfoEntity setGuests(List<GuestEntity> guests) {
        this.guests = guests;
        return this;
    }

    public List<HappyGuestEntity> getHappyGuests() {
        return happyGuests;
    }

    public HotelInfoEntity setHappyGuests(List<HappyGuestEntity> happyGuests) {
        this.happyGuests = happyGuests;
        return this;
    }

    public List<ContactRequestEntity> getContactRequests() {
        return contactRequests;
    }

    public HotelInfoEntity setContactRequests(List<ContactRequestEntity> contactRequests) {
        this.contactRequests = contactRequests;
        return this;
    }
}
