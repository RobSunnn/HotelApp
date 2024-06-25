package com.HotelApp.service.helpers;

import com.HotelApp.domain.entity.UserEntity;
import com.HotelApp.domain.models.view.UserView;
import com.HotelApp.util.encryptionUtil.EncryptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTransformationService {

    private static final Logger log = LoggerFactory.getLogger(UserTransformationService.class);


    @Cacheable(value = "userViewsCache", key = "'allUserViews'")
    public List<UserView> transformUsers(List<UserEntity> users) {
        log.info("Transforming users to user views");

        return users.stream()
                .map(this::mapAsUserView)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "singleUserView", key = "'singleUserView'")
    public UserView mapAsUserView(UserEntity user) {
        UserView userView = new UserView()
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setEmail(user.getEmail())
                .setFullName(user.getFirstName() + " " + user.getLastName())
                .setAge(user.getAge())
                .setRoles(user.getRoles());

        try {
            String encryptedEmail = EncryptionUtil.encrypt(user.getEmail());
            userView.setEncryptedEmail(encryptedEmail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Blob userImage = user.getUserImage();

        if (userImage != null) {
            try {
                userView.setUserImage(userImage.getBytes(1, (int) userImage.length()));
            } catch (SQLException ignored) {
            }
        }

        return userView;
    }
}
