# TBL_USER (인증 사용자)

인증 모듈에서 로그인·로그아웃에 필요한 최소 정보(식별자·비밀번호·salt·감사 컬럼)만 저장합니다. 이름·이메일·연락처 등 프로필 정보는 고객(`atlas-flight-customer`) 모듈에서 관리합니다.

| 컬럼명    | 타입           | NULL | 기본값              | 설명 |
|-----------|----------------|------|---------------------|------|
| USER_ID   | VARCHAR(30)    | N    | —                   | PK. 로그인 ID (고객 모듈 `CUSTOMER_ID`와 동일 값으로 연동 권장) |
| PASSWORD  | VARCHAR(255)   | N    | —                   | 단방향 해시 비밀번호 (Base64 인코딩 저장 시 길이 여유) |
| SALT      | VARCHAR(128)   | N    | —                   | 비밀번호 salt (예: Base64 인코딩된 랜덤 바이트) |
| REG_DT    | DATETIME       | N    | `CURRENT_TIMESTAMP` | 등록 일시 |
| RGTR_ID   | VARCHAR(30)    | Y    | —                   | 등록자 ID |
| MDFCN_DT  | DATETIME       | N    | `CURRENT_TIMESTAMP` | 수정 일시 |
| MDFR_ID   | VARCHAR(30)    | Y    | —                   | 수정자 ID |

## 보안·운영 참고

- 비밀번호는 평문을 저장하지 않습니다. 필요 시 해시 알고리즘(bcrypt/PBKDF2 등)으로 교체 시 `PASSWORD`·`SALT` 정책을 함께 검토합니다.
- `USER_ID`와 고객 도메인 ID의 정합성은 분산 트랜잭션 밖에서 맞춰야 하므로, 회원가입 순서·실패 시 보상 트랜잭션(역순 롤백 API 등)을 운영 정책으로 정의하는 것이 좋습니다.
- 예전 스키마에 `DEL_YN` 컬럼이 남아 있다면 DB에서 `ALTER TABLE TBL_USER DROP COLUMN DEL_YN;` 로 제거한 뒤 애플리케이션을 맞춥니다.
