package com.HotelApp.DBInit;

import com.HotelApp.service.AdminService;
import com.HotelApp.service.CategoryService;
import com.HotelApp.service.RoomService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBRunner implements CommandLineRunner {

    private final RoomService roomService;

    private final CategoryService categoryService;

    private final AdminService adminService;

    public DBRunner(RoomService roomService, CategoryService categoryService, AdminService adminService) {
        this.roomService = roomService;
        this.categoryService = categoryService;
        this.adminService = adminService;
    }

    @Override
    public void run(String... args) throws Exception {

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
