package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.ForbiddenRequestEntity;
import com.HotelApp.domain.models.view.ForbiddenRequestView;
import com.HotelApp.repository.ForbiddenRequestRepository;
import com.HotelApp.service.ForbiddenRequestsService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.HotelApp.config.ApplicationBeanConfiguration.modelMapper;

@EnableScheduling
@Service
public class ForbiddenRequestsServiceImpl implements ForbiddenRequestsService {

    private final ForbiddenRequestRepository forbiddenRequestRepository;

    public ForbiddenRequestsServiceImpl(ForbiddenRequestRepository forbiddenRequestRepository) {
        this.forbiddenRequestRepository = forbiddenRequestRepository;
    }

    @Override
    public List<ForbiddenRequestView> getAllNotChecked() {
        return forbiddenRequestRepository
                .findAll()
                .stream()
                .filter(forbiddenRequest -> !forbiddenRequest.isChecked())
                .map(forbiddenRequest -> modelMapper().map(forbiddenRequest, ForbiddenRequestView.class))
                .toList();
    }

    @Override
    public void checkAll() {
        forbiddenRequestRepository.saveAll(
                forbiddenRequestRepository
                        .findAll()
                        .stream()
                        .filter(forbiddenRequest -> !forbiddenRequest.isChecked())
                        .map(forbiddenRequest -> forbiddenRequest.setChecked(true))
                        .toList()
        );
    }

    @Scheduled(cron = "@monthly")
    public void clearCheckedForbiddenRequests() {
        forbiddenRequestRepository.deleteAll(
                forbiddenRequestRepository.findAll()
                        .stream()
                        .filter(ForbiddenRequestEntity::isChecked)
                        .toList()
        );
    }
}
