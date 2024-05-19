package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.HotelInfoEntity;
import com.HotelApp.domain.entity.RoleEntity;
import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.entity.enums.RoleEnum;
import com.HotelApp.domain.models.binding.UserRegisterBindingModel;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.repository.UserRepository;
import com.HotelApp.service.RoleService;
import com.HotelApp.service.UserService;
import com.HotelApp.service.exception.ForbiddenUserException;
import com.HotelApp.service.exception.UserNotFoundException;
import com.HotelApp.validation.constants.ValidationConstants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @Override
    public boolean registerUser(UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult, HotelInfoEntity hotelInfo) {
        if (checkIfEmailExist(userRegisterBindingModel)) {
            bindingResult.addError(new FieldError("userRegisterBindingModel", "email", ValidationConstants.EMAIL_EXIST));
            return false;
        }

        UserEntity user = userRepository.save(mapAsUser(userRegisterBindingModel, hotelInfo));

        hotelInfo.getUsers().add(user);

        return user.getEmail() != null;
    }




    @Override
    public boolean checkIfEmailExist(UserRegisterBindingModel userRegisterBindingModel) {
        Optional<UserEntity> user = userRepository.findByEmail(userRegisterBindingModel.getEmail());

        return user.isPresent();
    }

    @Override
    public UserEntity findUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        if (user.getId() == 1) {
            throw new ForbiddenUserException("You can't see this user :)");
        }

        return user;
    }

    private UserEntity mapAsUser(UserRegisterBindingModel userRegisterBindingModel, HotelInfoEntity hotelInfo) {

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            return new UserEntity(); // TODO: better solution for field match
        }

        UserEntity user = new UserEntity()
                .setEmail(userRegisterBindingModel.getEmail())
                .setAge(userRegisterBindingModel.getAge())
                .setFirstName(userRegisterBindingModel.getFirstName())
                .setLastName(userRegisterBindingModel.getLastName())
                .setPassword(passwordEncoder().encode(userRegisterBindingModel.getPassword()))
                .setCreated(LocalDateTime.now())
                .setHotelInfoEntity(hotelInfo);

        if (roleService.getCount() == 0) {
            roleService.initRoles();
        }

        if (userRepository.count() == 0) {
            user.setRoles(roleService.getAllRoles());
        } else {
            user.setRoles(roleService.getAllRoles().stream().filter(r -> r.getName().equals(RoleEnum.USER)).toList());
        }

        return user;
    }

//todo: add hotelInfoEntity to this service to save logs, like when
    // user is made admin to save a message with date and who is making the post if it is possible

    @Override
    public void makeUserAdmin(String email) {
        // Find the user by email
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            // Fetch the ADMIN role
            RoleEntity adminRole = roleService.getAllRoles()
                    .stream()
                    .filter(role -> role.getName().name().equals("ADMIN"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));

            List<RoleEntity> allRoles = roleService.getAllRoles();

            UserEntity user = userOptional.get();

            boolean isAdmin = user.getRoles().stream()
                    .anyMatch(role -> role.getName().name().equals(adminRole.getName().name()));

            // Check if the user already has the ADMIN role
            if (!isAdmin) {
                // Add the ADMIN role to the user
                user.setRoles(allRoles);
                userRepository.save(user);
            } else {
                System.out.println("User is already an admin.");
            }

        } else {
            // User not found
            throw new UserNotFoundException("User not found for email: " + email);
        }
    }

    @Override
    public void makeUserModerator(String email) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            RoleEntity userRole = roleService.getAllRoles()
                    .stream()
                    .filter(role -> role.getName().name().equals("USER"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("USER role not found"));

            RoleEntity moderatorRole = roleService.getAllRoles()
                    .stream()
                    .filter(role -> role.getName().name().equals("MODERATOR"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("MODERATOR role not found"));

            boolean isModerator = user.getRoles().stream()
                    .anyMatch(role -> role.getName().name().equals("MODERATOR"));

//            if (!isModerator) {
                //todo: if user is admin and we want to be only moderator it is not working properly
                user.setRoles(List.of(userRole, moderatorRole));
                userRepository.save(user);
//            } else {
//                System.out.println("User is already a moderator.");
//            }

        } else {
            throw new IllegalArgumentException("User not found for email: " + email);
        }

    }

    @Override
    public void takeRights(String email) {
        RoleEntity userRole = roleService.getAllRoles()
                .stream()
                .filter(role -> role.getName().name().equals("USER"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("USER role not found"));

        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            UserEntity user = userOptional.get();

            user.setRoles(List.of(userRole));
            userRepository.save(user);

        } else {
            throw new IllegalArgumentException("User not found for email: " + email);
        }

    }

    @Override
    public UserView findUserDetails(String userEmail) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User with email " + userEmail + " not found"));
        return mapAsUserView(user);
    }

    private UserView mapAsUserView(UserEntity user) {
        return new UserView().setFullName(user.getFullName())
                .setAge(user.getAge())
                .setEmail(user.getEmail())
                .setRoles(user.getRoles());
    }

}
