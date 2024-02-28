-- 테스트 데이터 추가
INSERT INTO user_table (username, password, name, nickname, age, email, phone, image, business_number, authorities)
VALUES
    ('user1', '$2a$10$IJdbFwJsMrApa40b1GvgVu3e9Nd5ETAJyKxAPc0hzKTDnGZrdYjaO', 'John', 'johndoe', 25, 'john@example.com', '+1234567890', null, NULL, 'ROLE_USER'),
    ('user2', '$2a$10$C7gmT3APEhau07bMKtCC9elOQzY8OEmAIhBMRKM6p064/hi32aIn.', 'Alice', 'aliceinwonderland', 30, 'alice@example.com', '+2345678901', null, NULL, 'ROLE_USER'),
    ('user3', '$2a$10$JjzvTZklRJ.o7AVXBSfv0eT.KOrvevPmzNVb1VZ9hU7KKm.AylgXe', 'Bob', 'bobthebuilder', 35, 'bob@example.com', '+3456789012', null, '456789123', 'ROLE_BUSINESS_USER'),
    ('user4', '$2a$10$tw7nhhvArOc0nZVZ4p41quhepD6wyaQh0ZDxNUjc1QfgI6nFe1Dt.', 'Emma', 'emmalicious', 40, 'emma@example.com', '+4567890123', null, NULL, 'ROLE_USER'),
    ('user5', '$2a$10$.rn07T.fAqC./fJ8f0Vx0eNrBZcA9Zb0xL6WN0Ayr5XY/FA7khPbS', 'Michael', 'mikesmith', 45, 'michael@example.com', '+5678901234', null, '7894561', 'ROLE_USER'),
    ('user6', '$2a$10$pzc13eEsWZxd1QdyaWpPZO8WM48ira3ahrRPwuBG/tsgs2bX.GCaC', null, null, NULL, null, null , null, NULL, 'ROLE_INACTIVE_USER');

-- Item 테스트 데이터 추가
INSERT INTO Item (title, description, image, price, status, user_id)
VALUES
    ('키보드', '소음이 적습니다.', null, 20000, 'SALE', 2),
    ('마우스', '손목에 무리가 없습니다.', null, 15000, 'SALE', 3),
    ('에플펜슬', '미개봉.', null, 180000, 'SALE', 3),
    ('닌텐도', '풀박스.', null, 150000, 'SALE', 4),
    ('헤드셋', '노이즈 캔슬링 가능합니다.', null, 200000, 'SALE', 2);

-- Proposal 테이블에 레코드 삽입
INSERT INTO Proposal (status, user_id, item_id)
VALUES
    ('SALE', 3, 1),
    ('SALE', 4, 2),
    ('SALE', 3, 4),
    ('SALE', 2, 2),
    ('SALE', 2, 3),
    ('SALE', 3, 5);
