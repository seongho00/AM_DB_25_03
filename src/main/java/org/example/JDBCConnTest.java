
package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCConnTest {
    Article article;
    Connection conn = null;


    public List<Article> connect(Article article, String cmd) {
        this.article = article;
        String[] actionMethodName = cmd.split(" ");
        // 중복코드 없애는 방법 : 함수는 하나로 실행하고 여기서 연결시도.
        // switch 구문을 통해 필요한 함수 실행.

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            String url = "jdbc:mariadb://127.0.0.1:3306/AM_DB_25_03?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul";
            conn = DriverManager.getConnection(url, "root", "");
            System.out.println("연결 성공!");

            switch (actionMethodName[1]) {
                case "write":
                    doWrite();
                    break;
                case "list":
                    return showList();

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
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
        return null;

    }


    public void doWrite() throws SQLException {

        PreparedStatement pstmt = null;


        String sql = "insert into article (title,body) values (?,?)";
        // 코드가 완전히 똑같아도 상관없음.
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, article.getTitle()); // 여기 값을 입력받아야함.
        // 어떻게 연결 짖지?
        // 그럼 main 함수가 아니라 기본 함수로 바꿔서 함수를 실행시키는 것도 가능하지 않을까?
        // 오 된다.

        pstmt.setString(2, article.getBody());
        pstmt.executeUpdate(); // 몇 열에 저장되었는지 return 하는 함수


    }


    public List<Article> showList() throws SQLException {

        ResultSet rs = null;
        PreparedStatement pstmt = null;
        List<Article> articles_data = new ArrayList<>();


        // 그럼 함수를 실행할때마다 연결을 해야하는 것은 맞을텐데, 함수마다 try구문을 이용하는건 중복코드가 너무 많을 것 같다.
        // switch 구문을 이용해서 연결 먼저하는것도 좋을 것같은데?

        String sql = "select * from article";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String body = rs.getString("body");

            articles_data.add(new Article(id, title, body));
        }
        return articles_data;

    }
}



