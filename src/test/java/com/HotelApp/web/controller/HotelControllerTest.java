package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.service.impl.CustomUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.HotelApp.common.constants.AppConstants.HOTEL_PROFIT;
import static com.HotelApp.common.constants.InfoConstants.FORBIDDEN_REQUESTS_SIZE;
import static com.HotelApp.service.constants.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        CustomUser customUser = new CustomUser(
                TEST_EMAIL, TEST_PASSWORD,
                Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + RoleEnum.ADMIN)),
                USER_FULL_NAME
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testAdminPanel() throws Exception {
        mockMvc.perform(get(ADMIN_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(ADMIN_VIEW))
                .andExpect(model().attributeExists(HOTEL_PROFIT))
                .andExpect(model().attribute(FORBIDDEN_REQUESTS_SIZE, 0));
    }
}