package Application;

import Application.database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SeedProducts {

    public static void main(String[] args) {

        String sql = """
            INSERT INTO products
            (product_name, category, unit, purchase_price,
             selling_price, stock_quantity, reorder_level, description)
            SELECT ?, ?, ?, ?, ?, ?, ?, ?
            WHERE NOT EXISTS (
                SELECT 1 FROM products
                WHERE product_name = ? AND unit = ?
            )
            """;

        Object[][] products = {

                {"Basmati Rice", "Grocery", "5 kg", 450.0, 520.0, 50, 10,
                        "Premium quality basmati rice"},

                {"Wheat Flour", "Grocery", "5 kg", 220.0, 260.0, 40, 10,
                        "Good quality wheat flour"},

                {"Sugar", "Grocery", "1 kg", 42.0, 48.0, 60, 15,
                        "Fine granulated sugar"},

                {"Tata Salt", "Grocery", "1 kg", 25.0, 30.0, 50, 10,
                        "Iodized cooking salt"},

                {"Toor Dal", "Grocery", "1 kg", 120.0, 140.0, 40, 10,
                        "Premium quality toor dal"},

                {"Sunflower Oil", "Grocery", "1 L", 110.0, 125.0, 35, 10,
                        "Refined sunflower cooking oil"},

                {"Maggi Noodles", "Grocery", "70 g", 12.0, 15.0, 70, 15,
                        "Instant noodles"},

                {"Parle-G Biscuits", "Grocery", "250 g", 20.0, 25.0, 80, 20,
                        "Popular glucose biscuits"},

                {"Amul Milk", "Dairy", "1 L", 52.0, 58.0, 30, 10,
                        "Fresh dairy milk"},

                {"Amul Butter", "Dairy", "100 g", 55.0, 65.0, 25, 8,
                        "Fresh salted butter"},

                {"Curd", "Dairy", "500 g", 30.0, 38.0, 30, 10,
                        "Fresh dairy curd"},

                {"Tea", "Beverage", "250 g", 110.0, 130.0, 30, 8,
                        "Premium tea leaves"},

                {"Coffee", "Beverage", "100 g", 90.0, 110.0, 25, 8,
                        "Instant coffee powder"},

                {"Soft Drink", "Beverage", "750 ml", 35.0, 45.0, 40, 10,
                        "Refreshing carbonated drink"},

                {"Shampoo", "Personal Care", "180 ml", 120.0, 145.0, 20, 5,
                        "Daily use hair shampoo"},

                {"Bath Soap", "Personal Care", "100 g", 30.0, 38.0, 40, 10,
                        "Daily use bathing soap"},

                {"Toothpaste", "Personal Care", "150 g", 85.0, 100.0, 25, 8,
                        "Daily dental care toothpaste"},

                {"Washing Powder", "Household", "1 kg", 75.0, 90.0, 30, 8,
                        "Laundry detergent powder"},

                {"Dishwash Liquid", "Household", "500 ml", 70.0, 85.0, 25, 8,
                        "Dish cleaning liquid"},

                {"Floor Cleaner", "Household", "1 L", 95.0, 115.0, 20, 5,
                        "Liquid floor cleaning solution"}
        };

        try (Connection connection = DBConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int added = 0;

            for (Object[] product : products) {

                statement.setString(1, (String) product[0]);
                statement.setString(2, (String) product[1]);
                statement.setString(3, (String) product[2]);
                statement.setDouble(4, (Double) product[3]);
                statement.setDouble(5, (Double) product[4]);
                statement.setInt(6, (Integer) product[5]);
                statement.setInt(7, (Integer) product[6]);
                statement.setString(8, (String) product[7]);

                // Duplicate check
                statement.setString(9, (String) product[0]);
                statement.setString(10, (String) product[2]);

                if (statement.executeUpdate() > 0) {
                    added++;
                }
            }

            System.out.println(added + " products added successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}