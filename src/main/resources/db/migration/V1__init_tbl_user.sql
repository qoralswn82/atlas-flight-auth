-- =============================================================================
-- ATF_AUTH 인증 사용자 (TBL_USER)
-- (db/auth_tbl_user_ddl.sql 과 동일 — Flyway 마이그레이션용)
-- =============================================================================

CREATE TABLE TBL_USER
(
    USER_ID   VARCHAR(30)                          NOT NULL COMMENT '사용자_아이디' PRIMARY KEY,
    PASSWORD  VARCHAR(255)                         NOT NULL COMMENT '비밀번호_해시(Base64)',
    SALT      VARCHAR(128)                         NOT NULL COMMENT '비밀번호_salt(Base64)',
    REG_DT    DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '등록_일시',
    RGTR_ID   VARCHAR(30)                                   COMMENT '등록자_아이디',
    MDFCN_DT  DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '수정_일시',
    MDFR_ID   VARCHAR(30)                                   COMMENT '수정자_아이디'
) COMMENT '인증_사용자' COLLATE = UTF8MB4_UNICODE_CI;
