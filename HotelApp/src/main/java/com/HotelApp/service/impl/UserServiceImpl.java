package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.repository.UserRepository;
import com.HotelApp.service.RoleService;
import com.HotelApp.service.UserService;
import com.HotelApp.validation.constants.ValidationConstants;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.HotelApp.config.SecurityConfiguration.passwordEncoder;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public boolean registerUser(UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult) {
        if (checkIfEmailExist(userRegisterBindingModel)) {
            bindingResult.addError(new FieldError("userRegisterBindingModel", "email", ValidationConstants.EMAIL_EXIST));
            return false;
        }

        UserEntity user = userRepository.save(mapUser(userRegisterBindingModel));

        return user.getEmail() != null;
    }


    private UserEntity mapUser(UserRegisterBindingModel userRegisterBindingModel) {

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            return new UserEntity(); // TODO: better solution for field match
        }

        UserEntity user = new UserEntity()
                .setEmail(userRegisterBindingModel.getEmail())
                .setAge(userRegisterBindingModel.getAge())
                .setFirstName(userRegisterBindingModel.getFirstName())
                .setLastName(userRegisterBindingModel.getLastName())
                .setPassword(passwordEncoder().encode(userRegisterBindingModel.getPassword()))
                .setCreated(LocalDateTime.now());

        if (roleService.getCount() == 0) {
            roleService.initRoles();
        }

        if (userRepository.count() == 0) {
            user.setRole(roleService.getAllRoles());
        } else {
            user.setRole(roleService.getAllRoles().stream().filter(r -> r.getName().equals(RoleEnum.USER)).toList());
        }

        return user;
    }
    @Override
    public boolean checkIfEmailExist(UserRegisterBindingModel userRegisterBindingModel) {
        Optional<UserEntity> user = userRepository.findByEmail(userRegisterBindingModel.getEmail());

        return user.isPresent();
    }

    @Override
    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }
}
