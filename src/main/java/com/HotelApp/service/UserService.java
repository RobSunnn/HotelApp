package com.HotelApp.service;

import com.HotelApp.domain.models.binding.ChangeUserPasswordBindingModel;
import com.HotelApp.domain.models.binding.EditUserProfileBindingModel;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.domain.models.view.UserView;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


public interface UserService {

    ResponseEntity<?> registerUser(UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes);

    boolean checkIfEmailExist(String email);

    UserView findUserByEmail(String email);

    void changeUserRole(String email, String command);

    UserView findUserDetails();

    String addUserImage(MultipartFile image, RedirectAttributes redirectAttributes);

    ResponseEntity<?> editProfileInfo(EditUserProfileBindingModel editUserProfileBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes);

    ResponseEntity<?> changeUserPassword(ChangeUserPasswordBindingModel changeUserPasswordBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes);

}
