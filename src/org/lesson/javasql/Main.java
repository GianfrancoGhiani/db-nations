package org.lesson.javasql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        //import
        Scanner input = new Scanner(System.in);

        String url= "jdbc:mysql://localhost:3306/db_nations";
        String userPass = "root";
        try(Connection conn = DriverManager.getConnection(url, userPass, userPass)){

            String mainSql = """
                    select c.name, c.country_id , r.name, cont.name
                    from countries c
                    join regions r on r.region_id = c.region_id
                    join continents cont on cont.continent_id = r.continent_id
                    where c.name like ?
                    order by c.name
                    """;
            List<Integer> idList = new ArrayList<>();
            try(PreparedStatement ps1 = conn.prepareStatement(mainSql)){
                String searching = null;
                do {
                    System.out.println("Search for something:");
                    searching = input.nextLine();
                    searching = searching.replace("-", "");
//                    System.out.println("you have searched for: " + searching);
                }while (searching.isBlank());
                searching = "%"+ searching + "%";
                ps1.setString(1, searching);

                try(ResultSet rs = ps1.executeQuery()){
                    while (rs.next()){
                        System.out.print(rs.getString(1) + " ");
                        System.out.print(rs.getInt(2) + " ");
                        idList.add(rs.getInt(2));
                        System.out.print(rs.getString(3) + " ");
                        System.out.print(rs.getString(4) + "\n");
                    }
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }



            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
            String idSql = """
                    select c.name, l.`language` , cs.population , cs.gdp, cs.`year` 
                    from countries c
                    join regions r on r.region_id = c.region_id 
                    join continents cont on cont.continent_id = r.continent_id
                    join country_languages cl on cl.country_id =c.country_id 
                    join languages l on l.language_id = cl.language_id 
                    join country_stats cs on cs.country_id =c.country_id 
                    where c.country_id = ?
                    and cs.`year`  =  (select max(`year`) from country_stats cs2 where cs2.country_id = c.country_id)
                    """;
            int idSearching = -1 ;
            do {
                System.out.println("wich country is in your interest? search for id pls");
                try {
                    idSearching = Integer.parseInt(input.nextLine());
                } catch (IllegalArgumentException e){
                    System.out.println("You must be search for an id");
                }
            }while (!(idList.contains(idSearching)));
            System.out.println(idSearching);
            try (PreparedStatement ps2 = conn.prepareStatement(idSql)){
                ps2.setInt(1, idSearching);
                try(ResultSet rs = ps2.executeQuery()){
                    System.out.println("Chosen country: " + idSearching);

                    while (rs.next()){
                        if(rs.isFirst()){
                            System.out.print("Details for: "+rs.getString(1) + ", \n");
                            System.out.print("Languages: ");
                        }
                        System.out.print(rs.getString(2) + ", ");
                        if (rs.isLast()){
                            System.out.println("\nYear: "+rs.getInt(5) +",");
                            System.out.println("Population: "+ rs.getInt(3) + ", ");
                            System.out.println("GDP: "+rs.getLong(4) + " ");

                        }

                    }
                }catch (SQLException e){
                    System.out.println(e.getMessage());
                }
            } catch (SQLException e){
                System.out.println(e.getMessage());
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }

}