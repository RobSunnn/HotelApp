package com.HotelApp.service;

import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.domain.models.view.UserView;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    boolean registerUser(UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult);

    boolean checkIfEmailExist(UserRegisterBindingModel userRegisterBindingModel);

    UserView findUserByEmail(String email);

    void makeUserAdmin(String email);

    void makeUserModerator(String email);

    void takeRights(String email);

    List<UserView> findAllUsers();

    UserView findUserDetails(String userEmail);

    void addUserImage(MultipartFile image, String userEmail);
}
