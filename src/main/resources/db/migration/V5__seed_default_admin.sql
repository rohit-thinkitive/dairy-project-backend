-- Default admin user: username=admin, password=admin123
-- BCrypt hash of 'admin123'
INSERT INTO admin_users (username, password_hash, role)
VALUES ('admin', '$2a$10$MELXKhpUJ.hsVAY/GAydReWfQZO9yC9Ql23vbKJ7LiWpMHBg0OrGW', 'SUPER_ADMIN');
