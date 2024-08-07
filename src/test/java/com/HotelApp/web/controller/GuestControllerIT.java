package com.HotelApp.web.controller;

import com.HotelApp.domain.entity.CategoryEntity;
import com.HotelApp.domain.entity.GuestEntity;
import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoomEntity;
import com.HotelApp.domain.entity.enums.CategoriesEnum;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.domain.models.binding.AddGuestBindingModel;
import com.HotelApp.repository.GuestRepository;
import com.HotelApp.repository.HotelRepository;
import com.HotelApp.repository.RoomRepository;
import com.HotelApp.service.impl.CustomUser;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.HotelApp.common.constants.AppConstants.*;
import static com.HotelApp.common.constants.BindingConstants.GUEST_REGISTER_BINDING_MODEL;
import static com.HotelApp.common.constants.FailConstants.ERRORS;
import static com.HotelApp.common.constants.ValidationConstants.*;
import static com.HotelApp.service.constants.TestConstants.*;
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
    private GuestRepository guestRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Mock
    private HotelRepository hotelRepository;

    @Autowired
    private EncryptionService encryptionService;


    @BeforeEach
    void setUp() {
        CustomUser customUser = new CustomUser(
                TEST_EMAIL, TEST_PASSWORD,
                Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + RoleEnum.MODERATOR)),
                USER_FULL_NAME
        );
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

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
        mockMvc.perform(get(GUEST_ADD_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(ADD_GUEST_VIEW))
                .andExpect(model().attributeExists(GUEST_REGISTER_BINDING_MODEL))
                .andExpect(model().attributeExists(CATEGORIES_COLLECTION))
                .andExpect(model().attribute(GUEST_REGISTER_BINDING_MODEL, instanceOf(AddGuestBindingModel.class)));
    }


    @Test
    void testAddGuestFormSuccess() throws Exception {
        AddGuestBindingModel addGuestBindingModel = new AddGuestBindingModel()
                .setFirstName(MOCK_FIRST_NAME)
                .setLastName(MOCK_LAST_NAME)
                .setEmail(encryptionService.encrypt(TEST_EMAIL))
                .setAge(33)
                .setDocumentId(encryptionService.encrypt(MOCK_GUEST_DOCUMENT))
                .setDaysToStay(3)
                .setRoomNumber(1);

        roomRepository.save(mockRoom());

        mockMvc.perform(post(GUEST_ADD_URL)
                        .flashAttr(GUEST_REGISTER_BINDING_MODEL, addGuestBindingModel)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.redirectUrl").value(GUEST_ADD_SUCCESS_URL));

        assertEquals(1, guestRepository.count());
    }

    @Test
    void testAddGuestFormValidationFail() throws Exception {
        AddGuestBindingModel addGuestBindingModel = new AddGuestBindingModel()
                .setFirstName(MOCK_FIRST_NAME)
                .setLastName(MOCK_LAST_NAME)
                .setAge(23)
                .setRoomNumber(3);

        MvcResult result = mockMvc.perform(post(GUEST_ADD_URL)
                        .flashAttr(GUEST_REGISTER_BINDING_MODEL, addGuestBindingModel)
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(jsonResponse);

        // Extract and assert on an error array
        JsonNode errorsNode = jsonNode.get(ERRORS);
        assertNotNull(errorsNode);
        assertTrue(errorsNode.isArray());

        List<JsonNode> errorsList = new ArrayList<>();
        errorsNode.forEach(errorsList::add);
        errorsList.sort(Comparator.comparing(node -> node.get(CODE).asText()));

        assertEquals(4, errorsList.size());

        assertEquals(DOCUMENT_ID_EMPTY, errorsList.get(0).get(DEFAULT_MESSAGE).asText());

        assertEquals(EMPTY_DAYS, errorsList.get(1).get(DEFAULT_MESSAGE).asText());

        assertEquals(INVALID_EMAIL, errorsList.get(2).get(DEFAULT_MESSAGE).asText());

        assertEquals(DOCUMENT_ID_EMPTY, errorsList.get(3).get(DEFAULT_MESSAGE).asText());
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
                .setFirstName(MOCK_FIRST_NAME)
                .setLastName(MOCK_LAST_NAME)
                .setAge(33)
                .setDocumentId(MOCK_GUEST_DOCUMENT)
                .setRoom(room)
                .setCheckInTime(LocalDateTime.now().minusDays(3))
                .setCheckOutTime(LocalDateTime.now());

        guestRepository.save(guest);

        mockMvc.perform(get(GUEST_LEAVE_URL))
                .andExpect(status().isOk())
                .andExpect(view().name(GUEST_LEAVE_VIEW))
                .andExpect(model().attributeExists(GUESTS_COLLECTION))
                .andExpect(model().attribute(GUESTS_COLLECTION, hasSize(1)))
                .andExpect(model().attribute(GUESTS_COLLECTION, hasItem(
                        allOf(
                                hasProperty(FIRST_NAME_FIELD, is(MOCK_FIRST_NAME)),
                                hasProperty(LAST_NAME_FIELD, is(MOCK_LAST_NAME)),
                                hasProperty(AGE_FIELD, is(33)),
                                hasProperty(DOCUMENT_FIELD, is(MOCK_GUEST_DOCUMENT))
                        )
                )));
    }

    @Test
    void testGuestLeaveWithoutAttributes() throws Exception {
        mockMvc.perform(get(GUEST_LEAVE_URL))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(MODERATOR_URL));
    }

    private HotelInfoEntity mockHotelInfoEntity() {
        HotelInfoEntity hotelInfo = new HotelInfoEntity();
        hotelInfo.setId(1L);
        hotelInfo.setName(HOTEL_NAME);
        hotelInfo.setAddress(HOTEL_ADDRESS);
        hotelInfo.setPhoneNumber(HOTEL_PHONE);
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