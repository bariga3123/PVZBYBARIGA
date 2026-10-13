-- ============================================
--  ТЕСТОВЫЕ ДАННЫЕ ДЛЯ ПВЗ-СИСТЕМЫ
--  Пароли: admin123 / user123 (BCrypt)
-- ============================================

-- ---------- ПОЛЬЗОВАТЕЛИ ----------
-- admin / admin123
INSERT INTO users (username, password, email, phone, role) VALUES
('admin', '$2a$10$8OqR4//MMSoexvGzogdZF.wEvx29R0i9lwTdUOYa50gSRx/Sv3DEa',
 'admin@pvz.ru', '+7-900-111-11-11', 'ADMIN');

INSERT INTO users (username, password, email, phone, role) VALUES
('user', '$2a$10$MuhDREdjy6kW2x67y2zV6.o7OVt53NsMe/j6p/7/FSxDrBouNFguy',
 'user@pvz.ru', '+7-900-222-22-22', 'USER');
-- ---------- УСЛУГИ ----------
INSERT INTO services (name, price, category) VALUES
('Хранение посылки (1 день)', 30.00, 'Хранение'),
('Хранение посылки (3 дня)',   80.00, 'Хранение'),
('Доставка курьером',         250.00, 'Доставка'),
('Упаковка хрупкого груза',   150.00, 'Упаковка'),
('Проверка содержимого',       50.00, 'Доп. услуги');

-- ---------- ЯЧЕЙКИ ----------
INSERT INTO cells (cell_number, size, is_occupied) VALUES
('A-01', 'SMALL',  FALSE),
('A-02', 'SMALL',  FALSE),
('A-03', 'MEDIUM', FALSE),
('B-01', 'MEDIUM', FALSE),
('B-02', 'LARGE',  FALSE),
('B-03', 'LARGE',  FALSE);

-- ---------- ПОЛУЧАТЕЛИ ----------
-- Получатель привязан к user (id=2)
INSERT INTO customers (full_name, address, phone, user_id) VALUES
('Иванов Иван Иванович', 'г. Москва, ул. Ленина, д. 5, кв. 10',
 '+7-900-222-22-22', 2);

-- ---------- ПОСЫЛКИ ----------
-- Посылки пользователя user (customer_id=1)
INSERT INTO parcels (tracking_number, description, weight, arrival_date, storage_until, status, customer_id, service_id) VALUES
('TRK-2026-0001', 'Книги',           1.20, '2026-10-10', '2026-10-20', 'NEW', 1, 1),
('TRK-2026-0002', 'Электроника',     2.50, '2026-10-11', '2026-10-21', 'NEW', 1, 2),
('TRK-2026-0003', 'Одежда',          0.80, '2026-10-12', '2026-10-22', 'NEW', 1, NULL);