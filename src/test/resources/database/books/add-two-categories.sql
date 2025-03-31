INSERT INTO categories (id, name, description)
VALUES (1, 'sci-fi', null);

INSERT INTO categories (id, name, description)
VALUES (2, 'fantasy', null);

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1);

INSERT INTO books_categories (book_id, category_id)
VALUES (2, 2);

INSERT INTO books_categories (book_id, category_id)
VALUES (3, 1);

INSERT INTO books_categories (book_id, category_id)
VALUES (3, 2);
