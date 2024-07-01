package com.HotelApp.repository;

import com.HotelApp.domain.entity.CategoryEntity;
import com.HotelApp.domain.entity.enums.CategoriesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoryEntity, Long> {
    CategoryEntity findByName(CategoriesEnum name);
}
