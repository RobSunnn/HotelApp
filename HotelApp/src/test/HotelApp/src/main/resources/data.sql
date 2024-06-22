-- INSERT INTO roles (id, name)
-- VALUES
--     (1, 'ADMIN'),
--     (2, 'MODERATOR'),
--     (3, 'USER');



INSERT INTO categories(id, name)
VALUES
    (1, 'SINGLE'),
    (2, 'STUDIO'),
    (3, 'DOUBLE'),
    (4, 'DELUXE'),
    (5, 'PRESIDENT');


INSERT INTO users(id, first_name, last_name, email, age, password)
VALUES
    (2, 'Admin', 'Adminov', 'admin@adminov.com', 666, '$2a$10$UR6PZq8ugBOwyBvUc36lYOyxz5A8cmjwtYQ6epEESfCxD8dK1GOwe');

INSERT INTO users_roles (`user_id`, `roles_id`)
VALUES (2, 1);
INSERT INTO users_roles (`user_id`, `roles_id`)
VALUES (2, 2);
INSERT INTO users_roles (`user_id`, `roles_id`)
VALUES (2, 3);


INSERT INTO room_types(id, name, description, capacity, picture_url, category_id)
VALUES
    (1, 'Single Room' , 'With thoughtful touches and impeccable attention to detail, our single hotel room offers a retreat where you can recharge, rejuvenate, and experience true hospitality at its finest. Whether you''re here for business or pleasure, we invite you to make yourself at home and discover the perfect blend of comfort and sophistication.', 1, 'https://hmhub.in/wp-content/uploads/2018/07/bed-bedroom-chair-comfort-271619-1024x673.jpg', 1),
    (2, 'Studio' , 'With its blend of contemporary design, thoughtful amenities, and prime location, our studio suite offers the perfect balance of style and comfort for your stay in the city. Whether you''re here for a weekend getaway or an extended business trip, we invite you to experience the ultimate in urban living at our hotel.', 2, 'https://upload.wikimedia.org/wikipedia/commons/9/97/Studio_apartment.webp', 2),
    (3, 'Double Room' , 'Whether you''re here for a romantic getaway, a family vacation, or a weekend escape with friends, our double hotel room offers the perfect blend of comfort, convenience, and style for your stay. We invite you to make yourself at home and experience the warmth of true hospitality during your time with us.', 2, 'https://i0.wp.com/homequirer.com/wp-content/uploads/2022/01/Double-Room.jpg?resize=1200%2C675&ssl=1', 3),
    (4, 'Deluxe Room' , 'Step into luxury and sophistication with our deluxe hotel room, where every detail has been meticulously crafted to elevate your stay to new heights of comfort and style. From the moment you enter, you''ll be enveloped in an atmosphere of refinement and indulgence, promising a truly unforgettable experience.', 2, 'https://static.wixstatic.com/media/b4110a_30b6ed0d033e4857bcd3306005921a8b~mv2.jpg/v1/fill/w_980,h_572,al_c,q_85,usm_0.66_1.00_0.01,enc_auto/b4110a_30b6ed0d033e4857bcd3306005921a8b~mv2.jpg', 4),
    (5, 'Presidential Room' , 'Welcome to the epitome of luxury and refinement, where opulence meets sophistication in our distinguished presidential suite. From the moment you step inside, you''ll be enveloped in an atmosphere of grandeur and elegance, promising an unparalleled experience fit for royalty.', 4, 'https://themirahotel.com.vn/wp-content/uploads/2016/11/620x458px-1a-1-1920x813.jpg', 5);

