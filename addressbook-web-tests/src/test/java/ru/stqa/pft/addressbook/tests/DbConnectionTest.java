package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import java.sql.*;

public class DbConnectionTest {

    @Test   //проверяет что из БД можно извлечь инф-ию
    public void testDbConnection() {

        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/addressbook?" + "user=root&password=");

            //извлекаем инф-ию из БД
            Statement st = conn.createStatement();
            //результат помещаем в переменную
            ResultSet rs = st.executeQuery("select group_id, group_name, group_header, group_footer from group_list");

            Groups groups = new Groups ();
            //извлечение данных построчно
            while (rs.next()) {
                //добавляем созданные объекты в объект groups
                groups.add(new GroupData().withId(rs.getInt("group_id")).withName(rs.getString("group_name"))
                        .withHeader(rs.getString("group_header")).withFooter(rs.getString("group_footer")));
            }

            //закрываем ресурсы
            rs.close();
            st.close();
            conn.close();
            
            //вывод результатов
            System.out.println(groups);


        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

    }
}
