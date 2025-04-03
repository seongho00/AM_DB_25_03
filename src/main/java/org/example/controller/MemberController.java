package org.example.controller;

import org.example.Member;

import org.example.service.MemberService;

import java.sql.Connection;
import java.util.Map;
import java.util.Scanner;

public class MemberController {
    Member isLoginedMember;
    private boolean isLogined;
    private Connection conn;
    private Scanner sc;
    private MemberService memberService;

    public MemberController(Scanner sc, Connection conn) {
        this.sc = sc;
        this.conn = conn;
        this.memberService = new MemberService();
    }

    public void doJoin() {
        String loginId = null;
        String loginPw = null;
        String loginPwConfirm = null;
        String name = null;
        System.out.println("==회원가입==");
        while (true) {
            System.out.print("로그인 아이디 : ");
            loginId = sc.nextLine().trim();

            if (loginId.length() == 0 || loginId.contains(" ")) {
                System.out.println("아이디 똑바로 써");
                continue;
            }

            boolean isLoginIdDup = memberService.isLoginIdDup(conn, loginId);

            System.out.println(isLoginIdDup);

            if (isLoginIdDup) {
                System.out.println(loginId + "은(는) 이미 사용중");
                continue;
            }
            break;

        }
        while (true) {
            System.out.print("비밀번호 : ");
            loginPw = sc.nextLine().trim();

            if (loginPw.length() == 0 || loginPw.contains(" ")) {
                System.out.println("비번 똑바로 써");
                continue;
            }

            boolean loginCheckPw = true;

            while (true) {
                System.out.print("비번 확인 : ");
                loginPwConfirm = sc.nextLine().trim();

                if (loginPwConfirm.length() == 0 || loginPwConfirm.contains(" ")) {
                    System.out.println("비번 확인 똑바로 써");
                    continue;
                }

                if (loginPw.equals(loginPwConfirm) == false) {
                    System.out.println("일치하지 않아");
                    loginCheckPw = false;
                }
                break;
            }
            if (loginCheckPw) {
                break;
            }
        }
        while (true) {
            System.out.print("이름 : ");
            name = sc.nextLine();

            if (name.length() == 0 || name.contains(" ")) {
                System.out.println("이름 똑바로 써");
                continue;
            }
            break;
        }

        int id = memberService.doJoin(conn, loginId, loginPw, name);

        System.out.println(id + "번 회원이 가입됨");
    }

    public void doLogin() {
        if (isLogined) {
            System.out.println("이미 로그인 상태입니다.");
            return;
        }

        int availablecount = 3;
        Member foundMember = null;
        System.out.println("==회원가입==");

        while (true) {
            System.out.print("로그인 아이디 : ");
            String loginId = sc.nextLine().trim();
            Map<String, Object> memberMap = memberService.getLoginId(conn, loginId);

            if (memberMap.isEmpty()) {
                System.out.println("회원가입한 아이디가 없습니다.");
                continue;
            }

            foundMember = new Member(memberMap);
            break;
        }

        while (true) {
            System.out.print("로그인 비밀번호 : ");
            String loginPw = sc.nextLine().trim();

            if (!foundMember.getLoginPw().equals(loginPw)) {
                System.out.println("비밀번호가 틀렸습니다. 가능한 로그인 시도 횟수 : " + availablecount);
                availablecount--;
                if (availablecount == 0) {
                    System.out.println("로그인 가능 횟수 초과");
                    break;
                }
                continue;
            }
            break;
        }

        if (availablecount == 0) {
            return;
        }

        isLoginedMember = foundMember;
        isLogined = true;
        System.out.println("로그인 되었습니다.");
    }

    public void doLogout() {
        if (!isLogined) {
            System.out.println("이미 로그아웃 상태입니다.");
            return;
        }

        isLoginedMember = null;
        isLogined = false;
        System.out.println("로그아웃 되었습니다.");
    }

}