-- =============================================================================
-- ATF_AUTH 인증 사용자 (TBL_USER)
-- 로그인/로그아웃에 필요한 자격 증명만 보관합니다. 프로필은 고객 모듈에서 관리합니다.
-- =============================================================================

CREATE TABLE TBL_USER
(
    CUSTOMER_ID VARCHAR(30)                        NOT NULL COMMENT '고객_아이디' PRIMARY KEY,
    PASSWORD  VARCHAR(255)                         NOT NULL COMMENT '비밀번호_해시(Base64)',
    SALT      VARCHAR(128)                         NOT NULL COMMENT '비밀번호_salt(Base64)',
    REG_DT    DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '등록_일시',
    RGTR_ID   VARCHAR(30)                                   COMMENT '등록자_아이디',
    MDFCN_DT  DATETIME DEFAULT CURRENT_TIMESTAMP() NOT NULL COMMENT '수정_일시',
    MDFR_ID   VARCHAR(30)                                   COMMENT '수정자_아이디'
) COMMENT '인증_사용자' COLLATE = UTF8MB4_UNICODE_CI;
