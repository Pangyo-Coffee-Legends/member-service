//package com.nhnacademy.memberservice.init;
//
//import com.nhnacademy.memberservice.member.domain.Member;
//import com.nhnacademy.memberservice.member.repository.MemberRepository;
//import com.nhnacademy.memberservice.role.domain.Role;
//import com.nhnacademy.memberservice.role.repository.RoleRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class TestUserInitializer implements CommandLineRunner {
//
//    private final MemberRepository memberRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        Role role1 = new Role();
//        Role role2 = new Role();
//
//        Role testAdminRole = roleRepository.save(role1.ofNewRole(
//                "ROLE_ADMIN",
//                "관리자"
//        ));
//
//        Role testRole = roleRepository.save(role2.ofNewRole(
//                "ROLE_USER",
//                "사용자"
//        ));
//
//        Role testRole2 = role2.ofNewRole(
//                "ROLE_USER_TEST_TEST",
//                "테스트 사용자11"
//        );
//
//        roleRepository.save(testRole2);
//
//        Member member1 = new Member();
//
//        Member testAdminMember = member1.ofNewMember(
//                testAdminRole,
//                "Admin",
//                "admin@test.com",
//                passwordEncoder.encode("Admin123!"),
//                "010-2222-3333"
//        );
//
////        Member testAdminMember = member1.ofNewMember(
////                testAdminRole,
////                "Admin",
////                "gussl23@naver.com",
////                passwordEncoder.encode("1234"),
////                "010-2222-3333"
////        );
//
//        Member member2 = new Member();
//
//        Member testMember = member2.ofNewMember(
//                testRole,
//                "Test",
//                "test@test.com",
//                passwordEncoder.encode("Test123!"),
//                "010-1111-2222"
//        );
//
////        Member member2 = new Member();
////
////        Member testMember = member2.ofNewMember(
////                testRole,
////                "Test",
////                "didakd22@naver.com",
////                passwordEncoder.encode("1234"),
////                "010-1111-2222"
////        );
//
//        Member member3 = new Member();
//
//        Member testMember2 = member2.ofNewMember(
//                testRole2,
//                "Test22",
//                "didakd33@naver.com",
//                passwordEncoder.encode("1234"),
//                "010-1111-2222"
//        );
//
//        Member testMember3 = member2.ofNewMember(
//                testRole2,
//                "Test33",
//                "didakd44@naver.com",
//                passwordEncoder.encode("1234"),
//                "010-1111-2222"
//        );
//
//        Member testMember4 = member2.ofNewMember(
//                testRole2,
//                "Test44",
//                "didakd55@naver.com",
//                passwordEncoder.encode("1234"),
//                "010-1111-2222"
//        );
//
//        memberRepository.save(testMember);
//        memberRepository.save(testMember2);
//        memberRepository.save(testMember3);
//        memberRepository.save(testMember4);
//        memberRepository.save(testAdminMember);
//    }
//}
