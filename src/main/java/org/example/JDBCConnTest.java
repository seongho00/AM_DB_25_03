
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

    private void doModify() throws SQLException {
        String[] cmdBits = cmd.split(" ");

        String id = cmdBits[2];


        String sql = "select * from article where id = " + id;
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        while (rs.next()) {
            System.out.print("원래 제목 : \n" + rs.getString("title"));
            System.out.print("원래 내용 : \n" + rs.getString("body"));
        }

        sql = "update article set title = ?, body = ?  where id = " + id;
        pstmt = conn.prepareStatement(sql);

        System.out.print("수정할 제목 : ");
        pstmt.setString(1, Container.getSc().nextLine());
        System.out.print("수정할 내용 : ");
        pstmt.setString(2, Container.getSc().nextLine());
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

        String sql = "select * from article order by id desc"; // 맨 처음부터 내림차순으로 주면 최신 데이터부터 보여줌.
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        while (rs.next()) { // 데이터가 없을 때까지 반복한다.
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String body = rs.getString("body");

            articles_data.add(new Article(id, title, body));
        }
        return articles_data;

    }
}



