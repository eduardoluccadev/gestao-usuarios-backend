INSERT INTO users (email, full_name, is_active, password_hash)
VALUES ('admin@empresa.com', 'Administrador', 1, '123456');

INSERT INTO user_roles (user_id, role)
SELECT id, 'ADMIN' FROM users WHERE email = 'admin@empresa.com';
