-- 테스트 데이터 추가
INSERT INTO user_table (username, password, name, nickname, age, email, phone, business_number, authorities)
VALUES
    ('user1', '$2a$10$IJdbFwJsMrApa40b1GvgVu3e9Nd5ETAJyKxAPc0hzKTDnGZrdYjaO', 'John', 'johndoe', 25, 'john@example.com', '+1234567890', NULL, 'ROLE_USER'),
    ('user2', '$2a$10$C7gmT3APEhau07bMKtCC9elOQzY8OEmAIhBMRKM6p064/hi32aIn.', 'Alice', 'aliceinwonderland', 30, 'alice@example.com', '+2345678901', NULL, 'ROLE_USER'),
    ('user3', '$2a$10$JjzvTZklRJ.o7AVXBSfv0eT.KOrvevPmzNVb1VZ9hU7KKm.AylgXe', 'Bob', 'bobthebuilder', 35, 'bob@example.com', '+3456789012', '456789123', 'ROLE_BUSINESS_USER'),
    ('user4', '$2a$10$tw7nhhvArOc0nZVZ4p41quhepD6wyaQh0ZDxNUjc1QfgI6nFe1Dt.', 'Emma', 'emmalicious', 40, 'emma@example.com', '+4567890123', NULL, 'ROLE_USER'),
    ('user5', '$2a$10$.rn07T.fAqC./fJ8f0Vx0eNrBZcA9Zb0xL6WN0Ayr5XY/FA7khPbS', 'Michael', 'mikesmith', 45, 'michael@example.com', '+5678901234', '7894561', 'ROLE_USER'),
    ('user6', '$2a$10$pzc13eEsWZxd1QdyaWpPZO8WM48ira3ahrRPwuBG/tsgs2bX.GCaC', 'null', 'null', NULL, 'null', 'null', NULL, 'ROLE_INACTIVE_USER');
