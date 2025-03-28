package org.example;


import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Container.init();
        System.out.println("==프로그램 시작==");



        int lastArticleId = 0;
        JDBCConnTest jdbcConnTest = new JDBCConnTest();

        while (true) {
            System.out.print("명령어 > ");
            String cmd = Container.getSc().nextLine().trim();

            if (cmd.equals("exit")) {
                break;
            }
            if (cmd.equals("article write")) {
                System.out.println("==글쓰기==");
                int id = lastArticleId + 1;
                System.out.print("제목 : ");
                String title = Container.getSc().nextLine().trim();
                System.out.print("내용 : ");
                String body = Container.getSc().nextLine().trim();

                Article article = new Article(id, title, body);
                jdbcConnTest.connect(article, cmd);

                lastArticleId++;
                System.out.println(article);
            } else if (cmd.equals("article list")) {
                List<Article> articles_data = jdbcConnTest.connect(null, cmd);
                if (articles_data.size() == 0) {
                    System.out.println("게시글 없음");
                    continue;
                }

                System.out.println("==목록==");
                System.out.println("   번호    /    제목    /    내용    ");
                for (Article article : articles_data) {
                    System.out.printf("   %d     /   %s    /    %s    \n", article.getId(), article.getTitle(), article.getBody());
                }
            } else if (cmd.startsWith("article modify")) {
                jdbcConnTest.connect(null, cmd);

            }
        }

        System.out.println("==프로그램 종료==");

        Container.close();
    }
}