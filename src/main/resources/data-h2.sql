-- filling up users table
INSERT INTO users (name, password) VALUES ('Иванов','xxx');
INSERT INTO users (name, password) VALUES ('Петров','xxx');
INSERT INTO users (name, password) VALUES ('Сидоров','xxx');
INSERT INTO users (name, password) VALUES ('Smith','123');
INSERT INTO users (name, password) VALUES ('user','pass');

INSERT INTO authorities (username) VALUES ('Иванов');
INSERT INTO authorities (username) VALUES ('Петров');
INSERT INTO authorities (username) VALUES ('Сидоров');
INSERT INTO authorities (username) VALUES ('Smith');
INSERT INTO authorities (username) VALUES ('user');
-- filling up books table
INSERT INTO books (ISBN, author, name) VALUES('652-3-16-148410-0', 'А.С. Пушкин', 'Евгений Онегин');
INSERT INTO books (ISBN, author, name, takerid) VALUES('747-3-16-148410-0', 'А.С. Пушкин', 'Капитанская дочка', 3);
INSERT INTO books (ISBN, author, name, takerid) VALUES('501-3-16-148410-0', 'А.С. Пушкин', 'Повести Белкина', 1);
INSERT INTO books (ISBN, author, name) VALUES('629-3-16-148410-0', 'М.Ю. Лермонтов', 'Мцыри');
INSERT INTO books (ISBN, author, name, takerid) VALUES('709-3-16-148410-0', 'М.Ю. Лермонтов', 'Кавказский пленник', 3);
INSERT INTO books (ISBN, author, name) VALUES('434-3-16-148410-0', 'И.С. Тургенев', 'Отцы и дети');
INSERT INTO books (ISBN, author, name, takerid) VALUES('616-3-16-148410-0', 'И.А. Гончаров', 'Обломов', 4);
INSERT INTO books (ISBN, author, name) VALUES('604-3-16-148410-0', 'Ф.М. Достоевский', 'Преступление и наказание');
INSERT INTO books (ISBN, author, name, takerid) VALUES('743-3-16-148410-0', 'Л.Н. Толстой', 'Воскресение', 2);
INSERT INTO books (ISBN, author, name, takerid) VALUES('393-3-16-148410-0', 'А.П. Чехов', 'Пьесы', 3);
INSERT INTO books (ISBN, author, name, takerid) VALUES('523-3-16-148410-0', 'В.И. Ленин', 'Избранное', 5);
INSERT INTO books (ISBN, author, name, takerid) VALUES('453-3-16-148410-0', 'Н.А. Некрасов', 'Крестьянские дети', 3);
INSERT INTO books (ISBN, author, name) VALUES('397-3-16-148410-0', 'П.А. Кропоткин', 'Поля, фабрики и мастерские');
INSERT INTO books (ISBN, author, name, takerid) VALUES('935-3-16-148410-0', 'И.А. Крылов', 'Басни', 1);
INSERT INTO books (ISBN, author, name) VALUES('409-3-16-148410-0', 'Гомер', 'Одиссея');
INSERT INTO books (ISBN, author, name, takerid) VALUES('517-3-16-148410-0', 'А.С. Грибоедов', 'Горе от ума', 5);
INSERT INTO books (ISBN, author, name, takerid) VALUES('927-3-16-148410-0', 'Н.В. Гоголь', 'Вечера на хуторе близ Диканьки', 1);
INSERT INTO books (ISBN, author, name, takerid) VALUES('711-3-16-148410-0', 'П.П. Ершов', 'Конёк-горбунок', 1);
INSERT INTO books (ISBN, author, name, takerid) VALUES('480-3-16-148410-0', 'М.Е. Салтыков-Щедрин', 'Господа Головлёвы', 4);
INSERT INTO books (ISBN, author, name) VALUES('351-3-16-148410-0', 'А.Н. Островский', 'Гроза');
