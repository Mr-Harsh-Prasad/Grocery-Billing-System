package Application.database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;

import Application.models.Product;

public class ProductDAO {
    public static void addProduct(Product product) {
        String sql = """
                INSERT INTO products
                (product_name, category, unit, purchase_price,
                 selling_price, stock_quantity, reorder_level, description)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;
        try {
            Connection connection = DBConnection.connect();

            PreparedStatement statement =
                    connection.prepareStatement(sql);

            statement.setString(1, product.getProductName());
            statement.setString(2, product.getCategory());
            statement.setString(3, product.getUnit());

            statement.setDouble(4, product.getPurchasePrice());
            statement.setDouble(5, product.getSellingPrice());

            statement.setInt(6, product.getStockQuantity());
            statement.setInt(7, product.getReorderLevel());

            statement.setString(8, product.getDescription());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();

        String sql = "SELECT * FROM products";

        try (Connection connection = DBConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("category"),
                        resultSet.getString("unit"),
                        resultSet.getDouble("purchase_price"),
                        resultSet.getDouble("selling_price"),
                        resultSet.getInt("stock_quantity"),
                        resultSet.getInt("reorder_level"),
                        resultSet.getString("description")
                );

                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static Product findProductByName(String name) {

        String sql = """
            SELECT *
            FROM products
            WHERE product_name LIKE ?
            LIMIT 1
            """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, "%" + name + "%");

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                return new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("category"),
                        resultSet.getString("unit"),
                        resultSet.getDouble("purchase_price"),
                        resultSet.getDouble("selling_price"),
                        resultSet.getInt("stock_quantity"),
                        resultSet.getInt("reorder_level"),
                        resultSet.getString("description")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<Product> searchProducts(String search) {

        List<Product> products = new ArrayList<>();

        String sql = """
            SELECT *
            FROM products
            WHERE product_name LIKE ?
            """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, "%" + search + "%");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("category"),
                        resultSet.getString("unit"),
                        resultSet.getDouble("purchase_price"),
                        resultSet.getDouble("selling_price"),
                        resultSet.getInt("stock_quantity"),
                        resultSet.getInt("reorder_level"),
                        resultSet.getString("description")
                );

                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static List<Product> getProductsByCategory(String category) {

        List<Product> products = new ArrayList<>();

        String sql = """
            SELECT *
            FROM products
            WHERE category = ?
            """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, category);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                Product product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("product_name"),
                        resultSet.getString("category"),
                        resultSet.getString("unit"),
                        resultSet.getDouble("purchase_price"),
                        resultSet.getDouble("selling_price"),
                        resultSet.getInt("stock_quantity"),
                        resultSet.getInt("reorder_level"),
                        resultSet.getString("description")
                );

                products.add(product);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public static boolean productExists(String productName, String unit) {

        String sql = """
            SELECT COUNT(*)
            FROM products
            WHERE product_name = ?
            AND unit = ?
            """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, productName);
            statement.setString(2, unit);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void updateProduct(Product product) {

        String sql = """
            UPDATE products
            SET product_name = ?,
                category = ?,
                unit = ?,
                purchase_price = ?,
                selling_price = ?,
                stock_quantity = ?,
                reorder_level = ?,
                description = ?
            WHERE id = ?
            """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, product.getProductName());
            statement.setString(2, product.getCategory());
            statement.setString(3, product.getUnit());
            statement.setDouble(4, product.getPurchasePrice());
            statement.setDouble(5, product.getSellingPrice());
            statement.setInt(6, product.getStockQuantity());
            statement.setInt(7, product.getReorderLevel());
            statement.setString(8, product.getDescription());
            statement.setInt(9, product.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteProduct(int productId) {

        String sql = "DELETE FROM products WHERE id = ?";

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, productId);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}