package com.HotelApp.web.controller;

import com.HotelApp.domain.models.service.CustomUser;
import org.junit.jupiter.api.AfterEach;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ModeratorControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        CustomUser customUser = new CustomUser(
                "moderator@test.bg", "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_MODERATOR")),
                "Moderator Full Name"
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testModeratorPanel() throws Exception {
        mockMvc.perform(get("/moderator"))
                .andExpect(status().isOk())
                .andExpect(view().name("moderator/moderator-panel"))
                .andExpect(model().attribute("allNotApprovedComments", 0))
                .andExpect(model().attribute("allContactRequests", 0))
                .andExpect(model().attribute("allOnlineReservations", 0));
    }

    @Test
    public void testAllNotApproveComments_ShouldRedirectToModeratorPanel_WhenCommentsAreEmpty() throws Exception {
        mockMvc.perform(get("/moderator/comments"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/moderator"));
    }

    @Test
    public void testAllNotCheckedOnlineReservations_ShouldRedirectToModeratorPanel_WhenThereAreNoReservations() throws Exception {
        mockMvc.perform(get("/moderator/onlineReservations"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/moderator"));
    }

    @Test
    public void testAllNotCheckedContactRequests_ShouldRedirectToModeratorPanel_WhenThereAreNoContactRequests() throws Exception {
        mockMvc.perform(get("/moderator/contactRequests"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/moderator"));
    }

    @Test
    public void testGetAllFreeRoomsInHotel() throws Exception {
        mockMvc.perform(get("/hotel/freeRooms"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("freeRooms"))
                .andExpect(view().name("hotel/free-rooms"));
    }

    @Test
    public void testGetAllGuestsInHotel() throws Exception {
        mockMvc.perform(get("/hotel/allGuests"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allGuests"))
                .andExpect(view().name("hotel/all-guests"));
    }

    @Test
    public void testGetAllSubscribersInHotel() throws Exception {
        mockMvc.perform(get("/hotel/allSubscribers"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allSubscribers"))
                .andExpect(view().name("hotel/all-subscribers"));
    }

    @Test
    public void testGetAllHappyGuests() throws Exception {
        mockMvc.perform(get("/hotel/allHappyGuests"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("allHappyGuests"))
                .andExpect(view().name("hotel/all-happy-guests"));
    }

}