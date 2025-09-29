import java.sql.*;

public class MyJDBC {
    public static void main(String[] args) {
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/admin_schema", "root", "Disenu23147$");

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS");

            while(resultSet.next()){
                System.out.println(resultSet.getString("username"));
                System.out.println(resultSet.getString("password"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

    }
}
