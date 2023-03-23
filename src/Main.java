import java.sql.*;

public class Main {
    // MILESTONE 1
    /*
    select c.name, c.country_id , r.name, cont.name
    from countries c
    join regions r on r.region_id = c.region_id
    join continents cont on cont.continent_id = r.continent_id
    order by c.name
    */


    public static void main(String[] args) {
        String url= "jdbc:mysql://localhost:3306/db_nations";
        String userpass = "root";
        try(Connection conn = DriverManager.getConnection(url, userpass, userpass)){
            String sql = """
                    select c.name, c.country_id , r.name, cont.name
                    from countries c
                    join regions r on r.region_id = c.region_id
                    join continents cont on cont.continent_id = r.continent_id
                    order by c.name
                    """;
            try(PreparedStatement ps = conn.prepareStatement(sql)){
                try(ResultSet rs = ps.executeQuery()){
                    while (rs.next()){
                        System.out.print(rs.getString(1) + " ");
                        System.out.print(rs.getInt(2) + " ");
                        System.out.print(rs.getString(3) + " ");
                        System.out.print(rs.getString(4) + "\n");
                    }
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

}