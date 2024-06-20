package com.HotelApp.web.controller;


import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.domain.models.service.CustomUserDetails;
import com.HotelApp.domain.models.view.GuestView;
import com.HotelApp.repository.GuestRepository;
import com.HotelApp.repository.RoomRepository;
import com.HotelApp.service.GuestService;
import com.HotelApp.service.HappyGuestService;
import com.HotelApp.service.HotelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.context.support.TestExecutionEvent.TEST_EXECUTION;
import static org.springframework.security.test.context.support.TestExecutionEvent.TEST_METHOD;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GuestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GuestService guestService;


    @Autowired
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setup() {
        // Mock the user details service to return a valid user
        UserDetails userDetails = new CustomUserDetails(
                "moderator@example.com",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MODERATOR")),
                "John Doe"
        );
        when(userDetailsService.loadUserByUsername("moderator@example.com")).thenReturn(userDetails);
    }

    @Test
    @WithMockUser(username = "moderator@example.com",  roles = {"MODERATOR"})
    public void testAddGuestSuccess() throws Exception {
        // Perform the request and assert the expected results
        mockMvc.perform(get("/guests/addGuestSuccess"))
                .andExpect(status().isOk())
                .andExpect(view().name("moderator/add-guest-success"));
    }
}
