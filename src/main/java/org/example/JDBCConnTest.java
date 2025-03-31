
package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCConnTest {
    private Article article;
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;
    private String cmd;


    public List<Article> connect(Article article, String cmd) {
        this.article = article;
        this.cmd = cmd;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            String url = "jdbc:mariadb://127.0.0.1:3306/AM_DB_25_03?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
            conn = DriverManager.getConnection(url, "root", "");
            System.out.println("연결 성공!");

            switch (cmd.split(" ")[1]) {
                case "write":
                    doWrite();
                    break;
                case "list":
                    return showList();
                case "modify":
                    doModify();
                    break;
                case "delete":
                    doDelete();
                    break;

                default:
                    System.out.println("잘못된 명령어입니다.");
                    break;
            }

        } catch (ClassNotFoundException e) {
            System.out.println("드라이버 로딩 실패" + e);
        } catch (SQLException e) {
            System.out.println("에러 : " + e);
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (pstmt != null && !pstmt.isClosed()) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

    private void doDelete() throws SQLException {
        String id = cmd.split(" ")[2];

        isExistData(id);


        String sql = "delete from article where id = " + id;
        pstmt = conn.prepareStatement(sql);
        pstmt.executeUpdate();

        System.out.println(id + "번 삭제 완료!");

    }


    private void doModify() throws SQLException {
        String id = cmd.split(" ")[2];

        isExistData(id);

        String sql = "update article" +
                " set title = " + article.getTitle() + "," +
                " body = " + article.getBody() +
                "  where id = " + id;


        pstmt = conn.prepareStatement(sql);

        pstmt.executeUpdate();
    }


    public void doWrite() throws SQLException {

        String sql = "insert into article (title,body) values (?,?)";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, article.getTitle());
        pstmt.setString(2, article.getBody());
        pstmt.executeUpdate();
    }

    public List<Article> showList() throws SQLException {

        List<Article> articles_data = new ArrayList<>();

        String sql = "select * from article order by id desc";
        try {
            if (!cmd.split(" ")[2].isEmpty()) {
                String id = cmd.split(" ")[2];
                sql = "select * from article where id = " + id;
            }
        } catch (Exception e) {
        } finally {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String body = rs.getString("body");

                articles_data.add(new Article(id, title, body));
            }
        }
        return articles_data;
    }

    private void isExistData(String id) throws SQLException {
        String sql = "select * from article where id = " + id;
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();
        int test_id = 0;
        while (rs.next()) {
            test_id = rs.getInt("id");
        }
        if (test_id == 0) {
            System.out.println(id + "번 게시물이 없습니다.");
        }
    }
}



