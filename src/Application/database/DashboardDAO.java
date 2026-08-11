package Application.database;

import Application.models.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardDAO {

    // ================= TOTAL SALES =================

    public static double getTotalSales() {

        String sql = """
                SELECT COALESCE(SUM(total), 0)
                FROM bills
                """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    // ================= TOTAL ORDERS =================

    public static int getTotalOrders() {

        String sql = """
                SELECT COUNT(*)
                FROM bills
                """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    // ================= TOTAL PRODUCTS =================

    public static int getTotalProducts() {

        String sql = """
                SELECT COUNT(*)
                FROM products
                """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    // ================= TOTAL PROFIT =================

    public static double getTotalProfit() {

        String sql = """
                SELECT COALESCE(
                    SUM(
                        (bi.price - p.purchase_price)
                        * bi.quantity
                    ),
                    0
                )
                FROM bill_items bi
                JOIN products p
                    ON bi.product_id = p.id
                """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {
                return resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }


    // ================= LOW STOCK =================

    public static List<Product> getLowStockProducts() {

        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT *
                FROM products
                WHERE stock_quantity <= reorder_level
                ORDER BY stock_quantity ASC
                """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                products.add(
                        new Product(
                                resultSet.getInt("id"),
                                resultSet.getString("product_name"),
                                resultSet.getString("category"),
                                resultSet.getString("unit"),
                                resultSet.getDouble("purchase_price"),
                                resultSet.getDouble("selling_price"),
                                resultSet.getInt("stock_quantity"),
                                resultSet.getInt("reorder_level"),
                                resultSet.getString("description")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }


    // ================= TOP SELLING =================

    public static class TopProduct {

        private final int productId;
        private final String productName;
        private final int quantity;
        private final double revenue;

        public TopProduct(
                int productId,
                String productName,
                int quantity,
                double revenue
        ) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.revenue = revenue;
        }

        public int getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getRevenue() {
            return revenue;
        }
    }


    public static List<TopProduct> getTopSellingProducts() {

        List<TopProduct> products =
                new ArrayList<>();

        String sql = """
                SELECT
                    p.id,
                    p.product_name,
                    SUM(bi.quantity) AS sold_quantity,
                    SUM(bi.quantity * bi.price) AS revenue
                FROM bill_items bi
                JOIN products p
                    ON bi.product_id = p.id
                GROUP BY p.id, p.product_name
                ORDER BY sold_quantity DESC
                LIMIT 5
                """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                products.add(
                        new TopProduct(
                                resultSet.getInt("id"),
                                resultSet.getString("product_name"),
                                resultSet.getInt("sold_quantity"),
                                resultSet.getDouble("revenue")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }


    // ================= RECENT TRANSACTIONS =================

    public static class RecentTransaction {

        private final int invoiceId;
        private final String customerName;
        private final double amount;
        private final String time;

        public RecentTransaction(
                int invoiceId,
                String customerName,
                double amount,
                String time
        ) {
            this.invoiceId = invoiceId;
            this.customerName = customerName;
            this.amount = amount;
            this.time = time;
        }

        public int getInvoiceId() {
            return invoiceId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public double getAmount() {
            return amount;
        }

        public String getTime() {
            return time;
        }
    }


    public static List<RecentTransaction>
    getRecentTransactions() {

        List<RecentTransaction> transactions =
                new ArrayList<>();

        String sql = """
                SELECT
                    b.id,
                    COALESCE(c.name, 'Walk-in Customer') AS customer_name,
                    b.total,
                    b.bill_date
                FROM bills b
                LEFT JOIN customers c
                    ON b.customer_id = c.id
                ORDER BY b.id DESC
                LIMIT 5
                """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                transactions.add(
                        new RecentTransaction(
                                resultSet.getInt("id"),
                                resultSet.getString(
                                        "customer_name"
                                ),
                                resultSet.getDouble("total"),
                                resultSet.getString("bill_date")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }


    // ================= SALES CHART =================

    public static Map<String, Double> getLast7DaysSales() {

        Map<String, Double> sales =
                new LinkedHashMap<>();

        String sql = """
                SELECT
                    DATE(bill_date) AS sale_date,
                    COALESCE(SUM(total), 0) AS total
                FROM bills
                WHERE DATE(bill_date)
                    >= DATE('now', '-6 days')
                GROUP BY DATE(bill_date)
                ORDER BY sale_date
                """;

        try (
                Connection connection = DBConnection.connect();
                PreparedStatement statement =
                        connection.prepareStatement(sql);
                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                sales.put(
                        resultSet.getString("sale_date"),
                        resultSet.getDouble("total")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sales;
    }
}