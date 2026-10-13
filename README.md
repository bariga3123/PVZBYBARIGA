# 📦 ПВЗ-Система

Информационно-справочная система пункта выдачи заказов.
Учебный проект на **Spring Boot 4 + Thymeleaf + H2 + Spring Security**.

---

## 🚀 Быстрый старт

### Требования
- Java 17+ (проект собран на Java 26)
- Maven 3.8+ (или используйте `mvnw`)

### Запуск

```bash
git clone <ссылка-на-репозиторий>
cd demo
mvn spring-boot:run
```

Или через IntelliJ IDEA: открыть `DemoApplication.java` → Run.

Приложение стартует на **http://localhost:8080**

---

## 🔑 Тестовые аккаунты

| Роль  | Логин  | Пароль    |
|-------|--------|-----------|
| ADMIN | admin  | admin123  |
| USER  | user   | user123   |

> Данные создаются автоматически при старте из `src/main/resources/data.sql`.
> Если меняешь пароли в БД — сгенерируй новый BCrypt-хеш через `PasswordGenerator` (см. логи при старте).

---

## 📋 Функционал

### Гость
- Главная, каталог услуг, контакты, FAQ
- Регистрация и вход

### Пользователь (USER)
- Личный кабинет: просмотр и редактирование профиля
- Создание заявки на посылку
- Просмотр своих заявок и их статусов
- Отмена заявки (только в статусе NEW)

### Администратор (ADMIN)
- Панель со статистикой (`/admin/dashboard`)
- Управление заявками и смена статусов (`/admin/requests`)
- CRUD: посылки, получатели, ячейки, услуги
- Управление справочниками

---

## 🗺️ Основные URL

| URL | Кто видит | Что там |
|-----|-----------|---------|
| `/` | Все | Главная |
| `/catalog` | Все | Каталог услуг |
| `/contacts`, `/faq` | Все | Справочные страницы |
| `/login`, `/register` | Все | Вход / регистрация |
| `/profile`, `/profile/edit` | USER, ADMIN | Профиль |
| `/requests`, `/requests/new` | USER | Мои заявки |
| `/admin/dashboard` | ADMIN | Панель админа |
| `/admin/requests` | ADMIN | Все заявки |
| `/admin/parcels`, `/admin/customers`, `/admin/cells`, `/admin/services` | ADMIN | CRUD справочников |
| `/h2-console` | Все (для разработки) | Консоль БД |

---

## 🗄️ База данных

**H2 (файловая БД)**: файл `pvzdb.mv.db` в корне проекта.

Настройки — `src/main/resources/application.properties`.

**H2 Console:**
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./pvzdb`
- User: `sa`
- Password: *(пусто)*

### Схема БД

```
users      → id, username, password, role, email, phone
customers  → id, user_id, full_name, address, phone
parcels    → id, tracking_number, description, weight,
             arrival_date, storage_until, status,
             customer_id, service_id
orders     → id, parcel_id, cell_id, status, comment, issue_date
cells      → id, cell_number, size, is_occupied
services   → id, name, price, category
```

---

## 🏗️ Архитектура

```
Controller → Service → Repository → H2
     ↓
 Thymeleaf (templates/)
```

- **Controller** — `controller/` (пользовательские), `controller/admin/` (админские)
- **Service** — бизнес-логика + `@Transactional`
- **Repository** — Spring Data JPA
- **DTO** — валидация форм (`@NotNull`, `@Size`, `@Email`)
- **Security** — BCrypt + `UserDetailsService`
- **Templates** — Thymeleaf с фрагментами (`fragments/header`, `fragments/footer`, `fragments/alerts`)

---

## 📝 Лицензия

Учебный проект. Свободное использование.