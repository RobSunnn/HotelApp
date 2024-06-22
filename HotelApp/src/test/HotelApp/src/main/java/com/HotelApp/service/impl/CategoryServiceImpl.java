package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.CategoryEntity;
import com.HotelApp.domain.entity.enums.CategoriesEnum;
import com.HotelApp.repository.CategoriesRepository;
import com.HotelApp.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoriesRepository categoriesRepository;

    public CategoryServiceImpl(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public void initCategories() {
        categoriesRepository.save(new CategoryEntity(CategoriesEnum.SINGLE));
        categoriesRepository.save(new CategoryEntity(CategoriesEnum.DOUBLE));
        categoriesRepository.save(new CategoryEntity(CategoriesEnum.STUDIO));
        categoriesRepository.save(new CategoryEntity(CategoriesEnum.DELUXE));
        categoriesRepository.save(new CategoryEntity(CategoriesEnum.PRESIDENT));
    }

    @Override
    public long getCount() {
        return this.categoriesRepository.count();
    }
}
