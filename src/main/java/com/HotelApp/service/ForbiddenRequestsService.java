package com.HotelApp.service;

import com.HotelApp.domain.entity.ForbiddenRequestEntity;
import com.HotelApp.domain.models.view.ForbiddenRequestView;

import java.util.List;

public interface ForbiddenRequestsService {

    List<ForbiddenRequestView> getAllNotChecked();

    void checkAll();
}
