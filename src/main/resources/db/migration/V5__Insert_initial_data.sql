INSERT INTO roles (name) VALUES ('ADMIN'), ('USER');

INSERT INTO users (name, email, password) VALUES 
    ('John Doe', 'john@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8ioctKi0pI2TJ4DEr6E3n3G0s7b9G'),
    ('Jane Smith', 'jane@example.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8ioctKi0pI2TJ4DEr6E3n3G0s7b9G');

INSERT INTO user_roles (user_id, role_id) VALUES 
    (1, 1),
    (1, 2),
    (2, 2);

INSERT INTO posts (title, content, author_id) VALUES 
    ('First Post', 'This is the content of the first post.', 1),
    ('Second Post', 'This is the content of the second post.', 2);
