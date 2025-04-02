package org.example;

import org.example.util.DBUtil;
import org.example.util.SecSql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class App {

    public void run() {
        System.out.println("==프로그램 시작==");
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.print("명령어 > ");
            String cmd = sc.nextLine().trim();

            Connection conn = null;

            try {
                Class.forName("org.mariadb.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            String url = "jdbc:mariadb://127.0.0.1:3306/AM_DB_25_03?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";

            try {
                conn = DriverManager.getConnection(url, "root", "");

                int actionResult = doAction(conn, sc, cmd);

                if (actionResult == -1) {
                    System.out.println("==프로그램 종료==");
                    sc.close();
                    break;
                }

            } catch (SQLException e) {
                System.out.println("에러 1 : " + e);
            } finally {
                try {
                    if (conn != null && !conn.isClosed()) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private int doAction(Connection conn, Scanner sc, String cmd) {

        if (cmd.equals("exit")) {
            return -1;
        }

        if (cmd.equals("member join")) {
            System.out.println("==회원가입==");
            System.out.print("아이디 : ");
            String regId = sc.nextLine();

            SecSql sql = new SecSql();
            sql.append("SELECT *");
            sql.append("FROM member");
            sql.append("WHERE loginId = ?;", regId);

            List<Map<String, Object>> memberListMap = DBUtil.selectRows(conn, sql);

            if (!memberListMap.isEmpty()) {
                System.out.println("이미 가입된 ID입니다.");
                return 0;
            }
            
            System.out.print("비밀번호 : ");
            String regPw = sc.nextLine();
            if (regPw.isEmpty()) {
                System.out.println("비밀번호를 입력해주세요");
                return 0;
            }

            System.out.print("이름 : ");
            String name = sc.nextLine();
            if (name.isEmpty()) {
                System.out.println("이름을 입력해주세요");
                return 0;
            }


            sql = new SecSql();

            sql.append("INSERT INTO member");
            sql.append("SET regDate = NOW(),");
            sql.append("updateDate = NOW(),");
            sql.append("loginId = ?,", regId);
            sql.append("loginPw = ?,", regPw);
            sql.append("name = ?", name);

            DBUtil.insert(conn, sql);

            System.out.println("회원가입이 완료되었습니다.");

        }

        if (cmd.equals("article write")) {
            System.out.println("==글쓰기==");
            System.out.print("제목 : ");
            String title = sc.nextLine();
            System.out.print("내용 : ");
            String body = sc.nextLine();

            SecSql sql = new SecSql();

            sql.append("INSERT INTO article");
            sql.append("SET regDate = NOW(),");
            sql.append("updateDate = NOW(),");
            sql.append("title = ?,", title);
            sql.append("`body` = ?;", body);

            int id = DBUtil.insert(conn, sql);

            System.out.println(id + "번 글이 생성됨");


        } else if (cmd.equals("article list")) {
            System.out.println("==목록==");

            List<Article> articles = new ArrayList<>();

            SecSql sql = new SecSql();
            sql.append("SELECT *");
            sql.append("FROM article");
            sql.append("ORDER BY id DESC");

            List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

            for (Map<String, Object> articleMap : articleListMap) {
                articles.add(new Article(articleMap));
            }

            if (articles.size() == 0) {
                System.out.println("게시글이 없습니다");
                return 0;
            }

            System.out.println("  번호  /   제목  ");
            for (Article article : articles) {
                System.out.printf("  %d     /   %s   \n", article.getId(), article.getTitle());
            }
        } else if (cmd.startsWith("article modify")) {

            int id = 0;

            try {
                id = Integer.parseInt(cmd.split(" ")[2]);
            } catch (Exception e) {
                System.out.println("번호는 정수로 입력해");
                return 0;
            }

            System.out.println("==수정==");
            System.out.print("새 제목 : ");
            String title = sc.nextLine().trim();
            System.out.print("새 내용 : ");
            String body = sc.nextLine().trim();

            SecSql sql = new SecSql();
            sql.append("UPDATE article");
            sql.append("SET updateDate = NOW()");
            if (!title.isEmpty()) {
                sql.append(", title = ?", title);
            }
            if (!body.isEmpty()) {
                sql.append(",`body` = ?", body);
            }
            sql.append("WHERE id = ?;", id);

            DBUtil.update(conn, sql);

            System.out.println(id + "번 글이 수정되었습니다.");

        } else if (cmd.startsWith("article detail")) {

            int id = 0;

            try {
                id = Integer.parseInt(cmd.split(" ")[2]);
            } catch (Exception e) {
                System.out.println("번호는 정수로 입력해");
                return 0;
            }

            Article foundArticle = null;

            SecSql sql = new SecSql();
            sql.append("SELECT *");
            sql.append("FROM article");
            sql.append("WHERE id = ?;", id);


            List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);


            for (Map<String, Object> articleMap : articleListMap) {
                foundArticle = new Article(articleMap);
            }


            if (foundArticle == null) {
                System.out.println("게시글이 없습니다");
                return 0;
            }

            System.out.println("번호 : " + foundArticle.getId());
            System.out.println("작성 날짜 : " + foundArticle.getRegDate());
            System.out.println("수정 날짜 : " + foundArticle.getUpdateDate());
            System.out.println("제목 : " + foundArticle.getTitle());
            System.out.println("내용 : " + foundArticle.getBody());


        } else if (cmd.startsWith("article delete")) {
            int id = 0;

            try {
                id = Integer.parseInt(cmd.split(" ")[2]);
            } catch (Exception e) {
                System.out.println("번호는 정수로 입력해");
                return 0;
            }

            SecSql sql = new SecSql();
            sql.append("Select *");
            sql.append("FROM article");
            sql.append("WHERE id = ?;", id);

            Map<String, Object> selectRow = DBUtil.selectRow(conn, sql);

            if (selectRow.isEmpty()) {
                System.out.println(id + "번 게시물은 없습니다.");
                return 0;
            }

            sql = new SecSql();
            sql.append("DELETE FROM article");
            sql.append("WHERE id = ?;", id);

            DBUtil.delete(conn, sql);

            System.out.println(id + "번 글이 삭제되었습니다.");

        }
        return 0;
    }
}