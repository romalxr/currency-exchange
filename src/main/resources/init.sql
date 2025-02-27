--DROP TABLE Currencies;

CREATE TABLE IF NOT EXISTS Currencies (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    Code VARCHAR(10) NOT NULL UNIQUE,
    FullName VARCHAR(100) NOT NULL,
    Sign VARCHAR(10)
);

INSERT OR IGNORE INTO Currencies (Code, FullName, Sign) VALUES
('USD', 'United States Dollar', '$'),
('EUR', 'Euro', '€'),
('JPY', 'Japanese Yen', '¥'),
('GBP', 'British Pound', '£'),
('AUD', 'Australian Dollar', 'A$');

CREATE TABLE IF NOT EXISTS ExchangeRates (
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    BaseCurrencyId INTEGER NOT NULL,
    TargetCurrencyId INTEGER NOT NULL,
    Rate DECIMAL(10, 6) NOT NULL,
    UNIQUE(BaseCurrencyId, TargetCurrencyId),
    FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies(ID),
    FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies(ID)
);

INSERT OR IGNORE INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES
(1, 2, 0.85),
(2, 1, 1.176471),
(1, 3, 109.53),
(3, 1, 0.009126),
(1, 4, 0.74),
(4, 1, 1.351351),
(1, 5, 1.34),
(5, 1, 0.746269);
