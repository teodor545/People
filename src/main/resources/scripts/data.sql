
INSERT INTO T_PEOPLE (FULL_NAME, PIN) VALUES
('John Doe', '1234567890'),
('Alice Smith', '0987654321'),
('Bob Johnson', NULL),
('Emma Brown', '0123456789'),
('Michael Davis', '1111222233'),
('Sophia Wilson', '9876543210'),
('James Taylor', '5555666677'),
('Olivia Martinez', NULL),
('William Anderson', '8765432109'),
('Emily Thomas', '3333444455')
RETURNING ID;


INSERT INTO T_MAILS (T_PEOPLE_ID, EMAIL_TYPE, EMAIL) VALUES
(1, 'work', 'john.doe@example.com'),
(1, 'home', 'john.doe@abv.bg'),
(1, 'home', 'john.doe@gmail.com'),
(2, 'work', 'alice.smith@example.com'),
(3, 'work', 'bob.johnson@example.com'),
(4, 'home', 'emma.brown@gmail.com'),
(4, 'work', 'emma.brown23@fastmail.com'),
(5, 'home', 'michael.davis@yahoo.com'),
(6, 'work', 'sophia.wilson@example.com'),
(7, 'home', 'james.taylor@gmail.com'),
(8, 'work', 'olivia.martinez@example.com'),
(9, 'home', 'william.anderson@yahoo.com');


INSERT INTO T_ADDRESSES (T_PEOPLE_ID, ADDR_TYPE, ADDR_INFO) VALUES
(1, 'home', '123 Main St, CityA, StateX, 12345'),
(1, 'work', '456 Business Blvd, CityA, StateX, 12345'),
(2, 'home', '789 Elm St, CityB, StateY, 56789'),
(3, 'home', '321 Oak St, CityC, StateZ, 98765'),
(4, 'home', '654 Pine St, CityD, StateW, 54321'),
(5, 'home', '987 Maple St, CityE, StateV, 13579'),
(6, 'work', '246 Cedar St, CityF, StateU, 97531'),
(7, 'home', '135 Walnut St, CityG, StateT, 24680'),
(8, 'home', '468 Birch St, CityH, StateS, 80246'),
(9, 'home', '357 Spruce St, CityI, StateR, 68024');