package com.HotelApp.DBInit;

import com.HotelApp.service.CategoryService;
import com.HotelApp.service.HotelService;
import com.HotelApp.service.RoomService;
import com.HotelApp.service.RoomTypesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBRunner implements CommandLineRunner {
    private final HotelService hotelService;

    private final RoomService roomService;

    private final RoomTypesService roomTypesService;

    private final CategoryService categoryService;

    public DBRunner(HotelService hotelService,
                    RoomService roomService,
                    RoomTypesService roomTypesService,
                    CategoryService categoryService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
        this.roomTypesService = roomTypesService;
        this.categoryService = categoryService;

    }

    @Override
    public void run(String... args) {
        if (roomTypesService.getRoomTypesCount() == 0) {
            roomTypesService.initRoomTypes();
        }

        if (hotelService.getCount() == 0) {
            hotelService.init();
        }

        if (categoryService.getCount() == 0) {
            categoryService.initCategories();
        }

        if (roomService.getCount() == 0) {
            roomService.initRooms(hotelService.getHotelInfo());
        }
    }
}
