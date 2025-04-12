# member-service
## 0412 - 미성 추가사항 및 수정사항 인수인계(확인 후 삭제!!)
## [공통 체크사항] 
#### 1. MemberRepository는 Spring Data JPA의 JpaRepository를 상속하므로 보통은 직접 구현체(impl)를 만들 필요가 없는걸로 알고 있는데 저희가 직접 커스텀하기 위함으로 생성하는건가요? 일단 애매해서 이건 이야기 한번 해보고 할려고 보류했습니다!
#### 2. controller 각각 맵핑 어노테이션 파라미터 URL 경로 한번 더 보고 수정할 부분 체크 해야 할 거 같습니다.
#### 3. 이슈 재등록하고 sql 테이블 제작사항 close 한 상태로 변경하고 풀리퀘스트 처리 해야됨
#### 4. controller,service member랑role 테스트코드 추가
```text
**📄 기능 설명**
- users, roles 테이블을 AIoT2팀 전용 MySQL 데이터베이스에 구성하여 사용자 정보 및 권한 데이터를 영구적으로 저장

- User 엔티티를 중심으로 MVC 아키텍처에 기반한 Entity, Repository, DTO, Service 계층 구현

**✅ 기대 효과**
- 회원가입 시 입력한 사용자 정보 및 권한이 실제 데이터베이스에 안전하게 저장되어,
인증 과정에서 이를 기반으로 로그인 및 접근 제어가 가능해짐

- 개발 환경과 운영 환경 모두에서 일관된 사용자 인증 체계 구축 가능
```
### Member
- MemberResponse @AllArgsConstructor 추가 
- (MemberServiceImpl 에서 member 생성 및 조회할때 memberResponse 파라미터 값으로 받아 리턴하여 핸들링 하기위함)
- MemberServiceImpl 클래스 추가 
- exception/MemberNotFoundException 추가
- MemberController 추가
- MemberUpdateRequest @AllArgsConstructor 추가
- Member isWithdrawn 메서드 추가
### Role
- RoleRepository findByRoleName(String roleName) 생성
- RoleController,RoleServiceImpl 생성
### pom.xml
```xml
<dependency>
            <!-- controller 관련 dependency -->
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>RELEASE</version>
            <scope>compile</scope>
        </dependency>

```
- controller 관련 dependency 추가

```xml
<!--        UNIT TEST 위한 Dependency-->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <scope>test</scope>
        </dependency>
```
- test 위해서 추가