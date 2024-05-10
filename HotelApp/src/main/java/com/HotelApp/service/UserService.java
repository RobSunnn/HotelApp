package com.HotelApp.service;

import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import org.springframework.validation.BindingResult;

public interface UserService {

    boolean registerUser(UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult);

    boolean checkIfEmailExist(UserRegisterBindingModel userRegisterBindingModel);
}
