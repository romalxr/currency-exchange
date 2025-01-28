-- Создание таблицы Currencies
CREATE TABLE IF NOT EXISTS Currencies (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,           -- Айди валюты, автоинкремент, первичный ключ
    Code VARCHAR(10) NOT NULL UNIQUE,               -- Код валюты (уникальный индекс)
    FullName VARCHAR(100) NOT NULL,                 -- Полное имя валюты
    Sign VARCHAR(10)                                -- Символ валюты
);

-- Пример данных для Currencies
INSERT OR IGNORE INTO Currencies (Code, FullName, Sign) VALUES
('USD', 'United States Dollar', '$'),
('EUR', 'Euro', '€'),
('JPY', 'Japanese Yen', '¥'),
('GBP', 'British Pound', '£'),
('AUD', 'Australian Dollar', 'A$');

-- Создание таблицы ExchangeRates
CREATE TABLE IF NOT EXISTS ExchangeRates (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    BaseCurrencyId INTEGER NOT NULL,
    TargetCurrencyId INTEGER NOT NULL,
    Rate DECIMAL(10, 6) NOT NULL,
    UNIQUE(BaseCurrencyId, TargetCurrencyId),
    FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies(ID),
    FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies(ID)
);

-- Пример данных для ExchangeRates
INSERT OR IGNORE INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES
(1, 2, 0.85),
(2, 1, 1.176471),
(1, 3, 109.53),
(3, 1, 0.009126),
(1, 4, 0.74),
(4, 1, 1.351351),
(1, 5, 1.34),
(5, 1, 0.746269);
