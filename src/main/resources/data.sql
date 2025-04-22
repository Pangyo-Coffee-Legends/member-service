-- 데이터 삽입 구문 수정
INSERT INTO roles (role_name, role_description) VALUES ('ROLE_USER', '일반 사용자');
INSERT INTO roles (role_name, role_description) VALUES ('ROLE_ADMIN', '관리자');

INSERT INTO members (role_no, mb_name, mb_email, mb_password, phone_number, created_at) VALUES (2, 'Admin', 'admin@test.com', 'Admin123!', '010-1111-2222', CURRENT_TIMESTAMP)