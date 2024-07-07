package com.HotelApp.service;

import com.HotelApp.domain.models.view.RoomTypeView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomTypesService {

    Page<RoomTypeView> getRoomTypes(Pageable pageable);

    long getRoomTypesCount();

    void initRoomTypes();

}
