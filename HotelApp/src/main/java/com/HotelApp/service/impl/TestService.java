package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.TestEntity;
import com.HotelApp.repository.TestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {

    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public List<String> takeParams() {
        TestEntity giveMeParams = testRepository.findById(1L).orElseThrow(() -> new RuntimeException("No test entity"));
        String serviceID = giveMeParams.getServiceID();
        String templateID = giveMeParams.getTemplateID();
        String publicKey = giveMeParams.getPublicKey();

        return List.of(publicKey, serviceID, templateID);

    }

    public Long getCount() {
        return testRepository.count();
    }

    public void initTestEntity() {

        TestEntity testEntity = new TestEntity();
        testEntity.setServiceID("service_3m9okqc");
        testEntity.setTemplateID("template_gexa6zi");
        testEntity.setPublicKey("BwzIjmmEVLNY7N-2r");

        testRepository.save(testEntity);
    }
}
