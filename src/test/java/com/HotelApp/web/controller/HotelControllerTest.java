package com.HotelApp.web.controller;

import com.HotelApp.domain.models.service.CustomUser;
import com.HotelApp.domain.models.view.ForbiddenRequestView;
import com.HotelApp.service.ForbiddenRequestsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ForbiddenRequestsService forbiddenRequestsService;

    @BeforeEach
    void setUp() {
        CustomUser customUser = new CustomUser(
                "admin@test.bg", "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")),
                "ADMIN Full Name"
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testAdminPanel() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("hotel/admin-panel"))
                .andExpect(model().attributeExists("totalProfit"))
                .andExpect(model().attribute("forbiddenRequestsSize", 0));
    }
}