DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS shop_offday;
DROP TABLE IF EXISTS nail_menu;

CREATE TABLE nail_menu (
    menu_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    menu_name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    duration_minutes INT NOT NULL,
    description VARCHAR(500) NULL,
    use_yn CHAR(1) DEFAULT 'Y' NOT NULL
);

CREATE TABLE reservation (
    reservation_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(50) NOT NULL,
    customer_phone VARCHAR(20) NOT NULL,
    password VARCHAR(100) NOT NULL,
    menu_id BIGINT NOT NULL,
    res_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING' NOT NULL,
    memo VARCHAR(1000) NULL,
    ins_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    upd_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES nail_menu(menu_id)
);

CREATE TABLE shop_offday (
    offday_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    off_date DATE NOT NULL,
    start_time TIME NULL,
    end_time TIME NULL,
    off_reason VARCHAR(250) NULL
);
