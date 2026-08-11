package Application.database;

import Application.models.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public static List<Customer> getAllCustomers() {

        List<Customer> customers = new ArrayList<>();

        String sql = """
                SELECT id, name, phone
                FROM customers
                ORDER BY name
                """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                customers.add(
                        new Customer(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("phone")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    public static int addCustomer(String name, String phone) {

        String sql = """
                INSERT INTO customers(name, phone)
                VALUES(?, ?)
                """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(1, name);
            statement.setString(2, phone);

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();

            if (keys.next()) {
                return keys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static boolean updateCustomer(
            int id,
            String name,
            String phone
    ) {

        String sql = """
            UPDATE customers
            SET name = ?, phone = ?
            WHERE id = ?
            """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, name);
            statement.setString(2, phone);
            statement.setInt(3, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteCustomer(int id) {

        String sql = """
            DELETE FROM customers
            WHERE id = ?
            """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}