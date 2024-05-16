package com.HotelApp.DBInit;

import com.HotelApp.service.AdminService;
import com.HotelApp.service.CategoryService;
import com.HotelApp.service.RoomService;
import com.HotelApp.service.impl.TestService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBRunner implements CommandLineRunner {

    private final RoomService roomService;

    private final CategoryService categoryService;

    private final TestService testService;

    private final AdminService adminService;

    public DBRunner(RoomService roomService, CategoryService categoryService, TestService testService, AdminService adminService) {
        this.roomService = roomService;
        this.categoryService = categoryService;
        this.testService = testService;
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (testService.getCount() == 0) {
            testService.initTestEntity();
        }

        if (categoryService.getCount() == 0) {
            categoryService.initCategories();
        }

        if (roomService.getCount() == 0) {
            roomService.initRooms();
        }

        if (adminService.getCount() == 0) {
            adminService.init();
        }

    }
}
