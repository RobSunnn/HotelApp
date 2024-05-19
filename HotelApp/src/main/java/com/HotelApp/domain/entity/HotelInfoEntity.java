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



    public HotelInfoEntity() {
        this.users = new ArrayList<>();
    }

    public HotelInfoEntity(BigDecimal start) {
        this.totalProfit = start;
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
}
