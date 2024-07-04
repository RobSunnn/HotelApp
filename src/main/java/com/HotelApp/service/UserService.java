package com.HotelApp.service;

import com.HotelApp.domain.models.binding.ChangeUserPasswordBindingModel;
import com.HotelApp.domain.models.binding.EditUserProfileBindingModel;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.domain.models.view.UserView;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public interface UserService {

    boolean registerUser(UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes);

    boolean checkIfEmailExist(String email);

    UserView findUserByEmail(String email);

    void changeUserRole(String email, String command);

    UserView findUserDetails(String userEmail);

    void addUserImage(MultipartFile image, String userEmail, RedirectAttributes redirectAttributes);

    boolean editProfileInfo(EditUserProfileBindingModel editUserProfileBindingModel, String userEmail, BindingResult bindingResult, RedirectAttributes redirectAttributes);

    boolean changeUserPassword(String userEmail, ChangeUserPasswordBindingModel changeUserPasswordBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes);

}
