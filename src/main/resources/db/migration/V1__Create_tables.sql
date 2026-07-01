-- Таблица категорий
CREATE TABLE IF NOT EXISTS category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    PRIMARY KEY (id),
    CONSTRAINT UQ_category_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Таблица точек интереса
CREATE TABLE IF NOT EXISTS point_of_interest (
    id BIGINT NOT NULL AUTO_INCREMENT,
    category_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    coord_x DOUBLE NOT NULL,
    coord_y DOUBLE NOT NULL,
    avg_rating DECIMAL(3, 2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT FK_poi_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Таблица оценок
CREATE TABLE IF NOT EXISTS rating (
    id BIGINT NOT NULL AUTO_INCREMENT,
    poi_id BIGINT NOT NULL,
    value INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT FK_rating_poi FOREIGN KEY (poi_id) REFERENCES point_of_interest(id) ON DELETE CASCADE,
    CONSTRAINT CHK_rating_value CHECK (value BETWEEN 1 AND 10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Таблица гайдов
CREATE TABLE IF NOT EXISTS guide (
    id BIGINT NOT NULL AUTO_INCREMENT,
    poi_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT FK_guide_poi FOREIGN KEY (poi_id) REFERENCES point_of_interest(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;