package org.example.service;

import org.example.dao.ArticleDao;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class ArticleService {
    private ArticleDao articleDao;

    public ArticleService() {
        articleDao = new ArticleDao();
    }


    public int doWrite(Connection conn, String title, String body) {
        return articleDao.doWrite(conn, title, body);
    }

    public List<Map<String, Object>> foundArticle(Connection conn) {
        return articleDao.foundArticle(conn);
    }

    public Map<String, Object> foundArticleById(Connection conn, int id) {
        return articleDao.foundArticleById(conn, id);
    }

    public void doModify(Connection conn, String title, String body, int id) {
        articleDao.doModify(conn, title, body, id);
    }

    public void doDelete(Connection conn, int id) {
        articleDao.doDelete(conn, id);
    }
}
