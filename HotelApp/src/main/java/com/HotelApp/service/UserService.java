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

import java.util.List;

public interface UserService {

    boolean registerUser(UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes);

    boolean checkIfEmailExist(String email);

    UserView findUserByEmail(String email);

    void makeUserAdmin(String email);

    void makeUserModerator(String email);

    void takeRights(String email);

    Page<UserView> findAllUsers(Pageable pageable);

    UserView findUserDetails(String userEmail);

    void addUserImage(MultipartFile image, String userEmail, RedirectAttributes redirectAttributes);

    boolean editProfileInfo(EditUserProfileBindingModel editUserProfileBindingModel, String userEmail, BindingResult bindingResult, RedirectAttributes redirectAttributes);

    boolean changeUserPassword(String userEmail, ChangeUserPasswordBindingModel changeUserPasswordBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes);
}
