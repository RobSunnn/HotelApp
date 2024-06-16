package com.HotelApp.service;

import com.HotelApp.domain.models.binding.ChangeUserPasswordBindingModel;
import com.HotelApp.domain.models.binding.EditUserProfileBindingModel;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.domain.models.view.UserView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

public interface UserService {

    Map<String, Object> registerUser(UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes);

    boolean checkIfEmailExist(String email);

    UserView findUserByEmail(String email);

    void changeUserRole(String email, String command);

    String decryptEmail(String email, String ivParam, String key);

    Page<UserView> findAllUsers(Pageable pageable);

    UserView findUserDetails(String userEmail);

    void addUserImage(MultipartFile image, String userEmail, RedirectAttributes redirectAttributes);

    boolean editProfileInfo(EditUserProfileBindingModel editUserProfileBindingModel, String userEmail, BindingResult bindingResult, RedirectAttributes redirectAttributes);

    boolean changeUserPassword(String userEmail, ChangeUserPasswordBindingModel changeUserPasswordBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes);
}
