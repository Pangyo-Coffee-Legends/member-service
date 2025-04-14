CREATE TABLE `roles` (
                         `role_no` BIGINT NOT NULL AUTO_INCREMENT COMMENT '권한 고유 번호',
                         `role_name` VARCHAR(50) NOT NULL COMMENT 'ROLE_ADMIN, ROLE_MEMBER',
                         `role_description` VARCHAR(200) NOT NULL COMMENT '권한 설명',
                         PRIMARY KEY (`role_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `members` (
                           `mb_no` BIGINT NOT NULL AUTO_INCREMENT COMMENT '회원 번호',
                           `role_no` BIGINT NOT NULL COMMENT '권한 번호',
                           `mb_name` VARCHAR(50) NOT NULL COMMENT '회원명',
                           `mb_email` VARCHAR(100) NOT NULL COMMENT '이메일 주소 (예: test@test.com)',
                           `mb_password` VARCHAR(200) NOT NULL COMMENT '비밀번호',
                           `phone_number` VARCHAR(15) NOT NULL COMMENT '전화번호 (예: 010-1234-5678)',
                           `created_at` DATETIME NOT NULL COMMENT '가입 일시',
                           `withdrawn_at` DATETIME DEFAULT NULL COMMENT '탈퇴 일시',
                           PRIMARY KEY (`mb_no`),
                           CONSTRAINT `FK_member_role` FOREIGN KEY (`role_no`)
                               REFERENCES `roles` (`role_no`)
                               ON DELETE RESTRICT
                               ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
----