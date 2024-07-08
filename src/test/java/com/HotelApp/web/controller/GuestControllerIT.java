package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.CategoryEntity;
import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.entity.enums.CategoriesEnum;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.domain.models.service.CustomUser;
import com.HotelApp.repository.CategoriesRepository;
import com.HotelApp.repository.GuestRepository;
import com.HotelApp.repository.HotelRepository;
import com.HotelApp.repository.RoomRepository;
import com.HotelApp.service.GuestService;
import com.HotelApp.util.encryptionUtil.EncryptionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class GuestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private GuestService guestService;

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Autowired
    private EncryptionService encryptionService;


    @BeforeEach
    void setUp() {// Example setup in a test case
        CustomUser customUser = new CustomUser(
                "moderator@test.bg", "password",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_MODERATOR")),
                "Moderator Full Name"
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        guestRepository.deleteAll();
        roomRepository.deleteAll();
        hotelRepository.deleteAll();
    }

    @AfterEach
    void teardown() {
        guestRepository.deleteAll();
        roomRepository.deleteAll();
        hotelRepository.deleteAll();
    }

    @Test
    void testAddGuestPageAttributes() throws Exception {
        mockMvc.perform(get("/guests/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("moderator/add-guest"))
                .andExpect(model().attributeExists("addGuestBindingModel"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("addGuestBindingModel", instanceOf(AddGuestBindingModel.class)));
    }


    @Test
    void testAddGuestFormSuccess() throws Exception {
        AddGuestBindingModel addGuestBindingModel = new AddGuestBindingModel()
                .setFirstName("Testing")
                .setLastName("ADD GUEST")
                .setEmail(encryptionService.encrypt("test@mail.bg"))
                .setAge(33)
                .setDocumentId(encryptionService.encrypt("ABC321"))
                .setDaysToStay(3)
                .setRoomNumber(1);

        roomRepository.save(mockRoom());

        mockMvc.perform(post("/guests/add")
                        .flashAttr("addGuestBindingModel", addGuestBindingModel)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.redirectUrl").value("/guests/addGuestSuccess"));

        assertEquals(1, guestRepository.count());
    }

    @Test
    void testAddGuestFormValidationFail() throws Exception {
        AddGuestBindingModel addGuestBindingModel = new AddGuestBindingModel()
                .setFirstName("Test")
                .setLastName("Testing")
                .setAge(23)
                .setRoomNumber(3);

        MvcResult result =   mockMvc.perform(post("/guests/add")
                        .flashAttr("addGuestBindingModel", addGuestBindingModel))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonResponse);

        // Extract and assert on errors array
        JsonNode errorsNode = jsonNode.get("errors");
        assertNotNull(errorsNode);
        assertTrue(errorsNode.isArray());

        List<JsonNode> errorsList = new ArrayList<>();
        errorsNode.forEach(errorsList::add);
        errorsList.sort(Comparator.comparing(node -> node.get("code").asText()));

        assertEquals(3, errorsList.size());

        // Example assertions on specific error messages
        assertEquals("We need the document id of the guest.",
                errorsList.get(0).get("defaultMessage").asText());

        assertEquals("You should enter the days that guest want to stay.",
                errorsList.get(1).get("defaultMessage").asText());

        assertEquals("The guest need to leave their email, so put it in a correct way.",
                errorsList.get(2).get("defaultMessage").asText());


    }

    @Test
    void testGuestLeavePageWithAttributes() throws Exception {
        HotelInfoEntity hotelInfoEntity = hotelRepository.save(mockHotelInfoEntity());

        RoomEntity room = new RoomEntity()
                .setRoomNumber(1)
                .setReserved(false)
                .setPrice(BigDecimal.valueOf(1000))
                .setCategory(mockCategory())
                .setHotelInfoEntity(hotelInfoEntity);

        roomRepository.save(room);

        GuestEntity guest = new GuestEntity()
                .setFirstName("Testing")
                .setLastName("GUEST LEAVE")
                .setAge(33)
                .setDocumentId("ABC321")
                .setRoom(room)
                .setCheckInTime(LocalDateTime.now().minusDays(3))
                .setCheckOutTime(LocalDateTime.now());

        guestRepository.save(guest);

        mockMvc.perform(get("/guests/leave"))
                .andExpect(status().isOk())
                .andExpect(view().name("moderator/guest-leave"))
                .andExpect(model().attributeExists("guests"))
                .andExpect(model().attribute("guests", hasSize(1)))
                .andExpect(model().attribute("guests", hasItem(
                        allOf(
                                hasProperty("firstName", is("Testing")),
                                hasProperty("lastName", is("GUEST LEAVE")),
                                hasProperty("age", is(33)),
                                hasProperty("documentId", is("ABC321"))
                        )
                )));
    }

    @Test
    void testGuestLeaveWithoutAttributes() throws Exception {
        mockMvc.perform(get("/guests/leave"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/moderator"));

    }

    private HotelInfoEntity mockHotelInfoEntity() {
        HotelInfoEntity hotelInfo = new HotelInfoEntity();
        hotelInfo.setId(1L);
        hotelInfo.setName("Great Hotel");
        hotelInfo.setAddress("Somewhere");
        hotelInfo.setPhoneNumber("0987-654-321");
        hotelInfo.setTotalProfit(BigDecimal.ZERO);

        return hotelInfo;
    }

    private CategoryEntity mockCategory() {
        CategoryEntity category = new CategoryEntity();
        category.setName(CategoriesEnum.DELUXE);
        category.setId(1L);

        return category;
    }

    private RoomEntity mockRoom() {
        return new RoomEntity()
                .setRoomNumber(1)
                .setReserved(false)
                .setPrice(BigDecimal.valueOf(1000))
                .setCategory(mockCategory())
                .setHotelInfoEntity(mockHotelInfoEntity());
    }
}