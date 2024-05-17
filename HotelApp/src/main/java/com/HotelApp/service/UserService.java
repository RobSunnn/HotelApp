package com.HotelApp.service;

import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.domain.models.view.UserView;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface UserService {

    boolean registerUser(UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult);

    boolean checkIfEmailExist(UserRegisterBindingModel userRegisterBindingModel);

    List<UserEntity> findAllUsers();

    UserEntity findUserByEmail(String email);

    void makeUserAdmin(String email);

    void makeUserModerator(String email);

    void takeRights(String email);

    UserView findUserDetails(String userEmail);

}
