package com.HotelApp.domain.entity;

import com.HotelApp.domain.entity.enums.CategoriesEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "rooms")
public class RoomEntity extends BaseEntity{

    @Column(nullable = false, unique = true, name = "room_number")
    private Integer roomNumber;

    @Column(nullable = false, name = "is_reserved")
    private boolean isReserved;

    @Column(nullable = false, name = "room_price")
    private BigDecimal price;

    @ManyToOne
    private CategoryEntity category;

    public RoomEntity() {}

    public RoomEntity(Integer roomNumber, boolean isReserved, BigDecimal price, CategoryEntity category) {

        this.roomNumber = roomNumber;
        this.isReserved = isReserved;
        this.price = price;
        this.category = category;

    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public RoomEntity setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
        return this;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public RoomEntity setReserved(boolean reserved) {
        isReserved = reserved;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public RoomEntity setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public RoomEntity setCategory(CategoryEntity category) {
        this.category = category;
        return this;
    }
}
