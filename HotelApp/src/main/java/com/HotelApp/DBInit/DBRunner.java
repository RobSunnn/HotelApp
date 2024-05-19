package com.HotelApp.DBInit;

import com.HotelApp.service.HotelService;
import com.HotelApp.service.CategoryService;
import com.HotelApp.service.RoomService;
import com.HotelApp.service.impl.TestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBRunner implements CommandLineRunner {
    private final HotelService hotelService;
    private final RoomService roomService;

    private final CategoryService categoryService;

    private final TestService testService;

    public DBRunner(HotelService hotelService, RoomService roomService, CategoryService categoryService, TestService testService) {
        this.hotelService = hotelService;
        this.roomService = roomService;
        this.categoryService = categoryService;
        this.testService = testService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (hotelService.getCount() == 0) {
            hotelService.init();
        }

        if (testService.getCount() == 0) {
            testService.initTestEntity();
        }

        if (categoryService.getCount() == 0) {
            categoryService.initCategories();
        }

        if (roomService.getCount() == 0) {
            roomService.initRooms(hotelService.getHotelInfo());
        }



    }
}
