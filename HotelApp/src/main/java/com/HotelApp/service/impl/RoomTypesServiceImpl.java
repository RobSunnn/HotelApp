package com.HotelApp.service.impl;

import com.HotelApp.domain.entity.RoomTypeEntity;
import com.HotelApp.domain.entity.enums.CategoriesEnum;
import com.HotelApp.domain.models.view.RoomTypeView;
import com.HotelApp.repository.CategoriesRepository;
import com.HotelApp.repository.RoomTypeRepository;
import com.HotelApp.service.RoomTypesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoomTypesServiceImpl implements RoomTypesService {
    private final RoomTypeRepository roomTypeRepository;

    private final CategoriesRepository categoriesRepository;

    public RoomTypesServiceImpl(RoomTypeRepository roomTypeRepository,
                                CategoriesRepository categoriesRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public Page<RoomTypeView> getRoomTypes(Pageable pageable) {
        return roomTypeRepository.findAll(pageable)
                .map(RoomTypesServiceImpl::map);
    }

    private static RoomTypeView map(RoomTypeEntity roomTypeEntity) {
        return new RoomTypeView()
                .setName(roomTypeEntity.getName())
                .setDescription(roomTypeEntity.getDescription())
                .setCapacity(roomTypeEntity.getCapacity())
                .setCategory(roomTypeEntity.getCategory())
                .setPictureUrl(roomTypeEntity.getPictureUrl());
    }

    @Override
    public long getRoomTypesCount() {
        return roomTypeRepository.count();
    }

    @Override
    public void initRoomTypes() {

        roomTypeRepository.save(new RoomTypeEntity("Single Room",
                "With thoughtful touches and impeccable attention to detail, our single hotel room offers a retreat where you can recharge, rejuvenate, and experience true hospitality at its finest. Whether you''re here for business or pleasure, we invite you to make yourself at home and discover the perfect blend of comfort and sophistication.",
                1, "https://images.adsttc.com/media/images/6019/902f/f91c/8198/f400/00d4/large_jpg/loft-23.jpg?1612288037",
                categoriesRepository.findByName(CategoriesEnum.SINGLE)));

        roomTypeRepository.save(new RoomTypeEntity("Studio",
                "With its blend of contemporary design, thoughtful amenities, and prime location, our studio suite offers the perfect balance of style and comfort for your stay in the city. Whether you''re here for a weekend getaway or an extended business trip, we invite you to experience the ultimate in urban living at our hotel.",
                2, "https://image-tc.galaxy.tf/wijpeg-cpdilo03fm3hl1oe6816gv0vu/cts-studio-room-2522-1920-x-1080.jpg",
                categoriesRepository.findByName(CategoriesEnum.STUDIO)));

        roomTypeRepository.save(new RoomTypeEntity("Double Room",
                "Whether you''re here for a romantic getaway, a family vacation, or a weekend escape with friends, our double hotel room offers the perfect blend of comfort, convenience, and style for your stay. We invite you to make yourself at home and experience the warmth of true hospitality during your time with us.",
                2, "https://elmalekfurniture.com/wp-content/uploads/2022/06/Screenshot-2022-06-24-at-6.11.56-PM.jpg",
                categoriesRepository.findByName(CategoriesEnum.DOUBLE)));

        roomTypeRepository.save(new RoomTypeEntity("Deluxe Room",
                "Step into luxury and sophistication with our deluxe hotel room, where every detail has been meticulously crafted to elevate your stay to new heights of comfort and style. From the moment you enter, you''ll be enveloped in an atmosphere of refinement and indulgence, promising a truly unforgettable experience.",
                2, "https://static.wixstatic.com/media/b4110a_30b6ed0d033e4857bcd3306005921a8b~mv2.jpg/v1/fill/w_980,h_572,al_c,q_85,usm_0.66_1.00_0.01,enc_auto/b4110a_30b6ed0d033e4857bcd3306005921a8b~mv2.jpg",
                categoriesRepository.findByName(CategoriesEnum.DELUXE)));

        roomTypeRepository.save(new RoomTypeEntity("Presidential Room",
                "Welcome to the epitome of luxury and refinement, where opulence meets sophistication in our distinguished presidential suite. From the moment you step inside, you''ll be enveloped in an atmosphere of grandeur and elegance, promising an unparalleled experience fit for royalty.",
                4, "https://i.pinimg.com/564x/e6/2b/6c/e62b6c0597695706d32a37d61dc9d668.jpg",
                categoriesRepository.findByName(CategoriesEnum.PRESIDENT)));
    }

}
