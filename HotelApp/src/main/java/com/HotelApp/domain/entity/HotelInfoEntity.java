package com.HotelApp.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "hotel_info")
public class HotelInfoEntity extends BaseEntity {

    private BigDecimal totalProfit;

    public HotelInfoEntity() {
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

}
