package org.example.controller;

import org.example.container.Container;
import org.example.dto.Article;
import org.example.service.ArticleService;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ArticleController {

    private Scanner sc;

    private ArticleService articleService;

    public ArticleController() {
        this.sc = Container.sc;
        this.articleService = Container.articleService;
    }


    public void doWrite() {
        if (!Container.session.isLogined) {
            System.out.println("로그인이 필요한 서비스입니다.");
            return;
        }

        System.out.println("==글쓰기==");
        System.out.print("제목 : ");
        String title = sc.nextLine();
        System.out.print("내용 : ");
        String body = sc.nextLine();

        int id = articleService.doWrite(title, body);

        System.out.println(id + "번 글이 생성됨");
    }

    public void showList(String cmd) {
        int selectPage = 1;
        String rearchWord;
        List<Article> articles = null;

        try {
            if (cmd.startsWith("article research")) {
                rearchWord = cmd.split(" ")[2];
                articles = articleService.getArticleByWord(rearchWord);
            } else if (cmd.equals("article list")) {
                articles = articleService.getArticles();
            } else {
                selectPage = Integer.parseInt(cmd.split(" ")[2]);
                articles = articleService.getArticles();
            }

            if (articles.size() == 0) {
                System.out.println("게시글이 없습니다");
                return;
            }

            System.out.println("==목록==");

            int totalPage = articles.size() % 10 == 0 ? articles.size() / 10 : articles.size() / 10 + 1;

            System.out.println("  번호  /   작성자    /   제목  ");
            for (int i = (selectPage - 1) * 10; i < (selectPage - 1) * 10 + 10; i++) {
                Article article = articles.get(i);
                System.out.printf("  %d    /    %s    /   %s   \n", article.getId(), article.getName(), article.getTitle());
            }

            for (int i = 1; i < totalPage; i++) {
                System.out.printf(" %d /", i);
            }
            System.out.printf(" %d ", totalPage);
            System.out.println();


        } catch (Exception e) {
            System.out.println("번호는 정수로 입력해");
        }


    }

    public void doModify(String cmd) {
        if (!Container.session.isLogined) {
            System.out.println("로그인이 필요한 서비스입니다.");
            return;
        }

        int id = 0;

        try {
            id = Integer.parseInt(cmd.split(" ")[2]);
        } catch (Exception e) {
            System.out.println("번호는 정수로 입력해");
            return;
        }

        Map<String, Object> articleMap = articleService.getArticleById(id);

        if (articleMap.isEmpty()) {
            System.out.println(id + "번 글은 없어");
            return;
        }

        Article article = new Article(articleMap);
        if (article.getMemberId() != Container.session.loginedMemberId) {
            System.out.println("당신의 게시글이 아닙니다.");
            return;
        }

        System.out.println("==수정==");
        System.out.print("새 제목 : ");
        String title = sc.nextLine().trim();
        System.out.print("새 내용 : ");
        String body = sc.nextLine().trim();

        articleService.doUpdate(id, title, body);

        System.out.println(id + "번 글이 수정되었습니다.");

    }

    public void showDetail(String cmd) {

        int id = 0;

        try {
            id = Integer.parseInt(cmd.split(" ")[2]);
        } catch (Exception e) {
            System.out.println("번호는 정수로 입력해");
            return;
        }

        System.out.println("==상세보기==");

        Map<String, Object> articleMap = articleService.getArticleById(id);

        if (articleMap.isEmpty()) {
            System.out.println(id + "번 글은 없어");
            return;
        }

        Article article = new Article(articleMap);

        System.out.println("번호 : " + article.getId());
        System.out.println("작성날짜 : " + article.getRegDate());
        System.out.println("수정날짜 : " + article.getUpdateDate());
        System.out.println("작성자 : " + article.getName());
        System.out.println("제목 : " + article.getTitle());
        System.out.println("내용 : " + article.getBody());
    }

    public void doDelete(String cmd) {
        if (!Container.session.isLogined) {
            System.out.println("로그인이 필요한 서비스입니다.");
            return;
        }

        int id = 0;

        try {
            id = Integer.parseInt(cmd.split(" ")[2]);
        } catch (Exception e) {
            System.out.println("번호는 정수로 입력해");
            return;
        }

        Map<String, Object> articleMap = articleService.getArticleById(id);

        if (articleMap.isEmpty()) {
            System.out.println(id + "번 글은 없어");
            return;
        }

        Article article = new Article(articleMap);

        if (article.getMemberId() != Container.session.loginedMemberId) {
            System.out.println("당신의 게시글이 아닙니다.");
            return;
        }

        System.out.println("==삭제==");

        articleService.doDelete(id);

        System.out.println(id + "번 글이 삭제되었습니다.");
    }

    public void showMemberList() {
        if (!Container.session.isLogined) {
            System.out.println("로그인이 필요한 서비스입니다.");
            return;
        }

        List<Map<String, Object>> articleListMap = articleService.getArticlesByMemberId();

        if (articleListMap.isEmpty()) {
            System.out.println("작성 글 없음.");
            return;
        }

        for (Map<String, Object> articleMap : articleListMap) {
            Article article = new Article(articleMap);
            System.out.printf("  %d    /    %s    /   %s   \n", article.getId(), article.getName(), article.getTitle());
        }
    }
}
