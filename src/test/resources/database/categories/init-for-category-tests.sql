INSERT INTO books (id, title, author, isbn, price)
VALUES (4, 'Sample Book 4', 'Author A', '1236567163331', 19.99);

INSERT INTO books (id, title, author, isbn, price)
VALUES (5, 'Sample Book 5', 'Author B', '1236567163330', 34.99);

INSERT INTO books (id, title, author, isbn, price)
VALUES (6, 'Sample Book 6', 'Author C', '1236567163332', 12.99);

INSERT INTO categories (id, name, description)
VALUES (3, 'cooking', null);

INSERT INTO categories (id, name, description)
VALUES (4, 'IT guides', null);

INSERT INTO categories (id, name, description)
VALUES (5, 'novel', null);

INSERT INTO books_categories (book_id, category_id)
VALUES (4, 3);

INSERT INTO books_categories (book_id, category_id)
VALUES (5, 4);

INSERT INTO books_categories (book_id, category_id)
VALUES (6, 4);

INSERT INTO books_categories (book_id, category_id)
VALUES (6, 5);