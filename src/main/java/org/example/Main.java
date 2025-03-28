package org.example;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        Container.init();
        System.out.println("==프로그램 시작==");
        JDBCConnTest jdbcConnTest = new JDBCConnTest();

        while (true) {
            System.out.print("명령어 > ");
            String cmd = Container.getSc().nextLine().trim();

            if (cmd.equals("exit")) {
                break;
            }
            if (cmd.equals("article write")) {
                System.out.println("==글쓰기==");
                System.out.print("제목 : ");
                String title = Container.getSc().nextLine().trim();
                System.out.print("내용 : ");
                String body = Container.getSc().nextLine().trim();

                Article article = new Article(0, title, body);
                jdbcConnTest.connect(article, cmd);

                System.out.println(article);
            } else if (cmd.equals("article list")) {
                List<Article> articles_data = jdbcConnTest.connect(null, cmd);
                if (articles_data.isEmpty()) {
                    System.out.println("게시글 없음");
                    continue;
                }

                System.out.println("==목록==");
                System.out.println("   번호    /    제목    /    내용    ");
                for (Article article : articles_data) {
                    System.out.printf("   %d     /   %s    /    %s    \n", article.getId(), article.getTitle(), article.getBody());
                }
            } else if (cmd.startsWith("article modify")) {

                List<Article> preArticle = jdbcConnTest.connect(null, "article list " + cmd.split(" ")[2]);
                System.out.printf("수정할 제목 : %s\n", preArticle.get(0).getTitle());
                System.out.printf("수정할 내용 : %s\n", preArticle.get(0).getBody());

                System.out.print("수정할 제목 : ");
                String title = Container.getSc().nextLine().trim();
                System.out.print("수정할 내용 : ");
                String body = Container.getSc().nextLine().trim();


                Article article = new Article(0, title, body);
                jdbcConnTest.connect(article, cmd);

            } else if (cmd.startsWith("article delete")) {
                jdbcConnTest.connect(null, cmd);

            }
        }

        System.out.println("==프로그램 종료==");

        Container.close();
    }
}