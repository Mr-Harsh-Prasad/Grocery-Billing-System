package Application.database;

import Application.models.BillItem;

import java.sql.*;
import java.util.List;

public class BillingDAO {

    public static int saveBill(
            int customerId,
            double total,
            List<BillItem> billItems
    ) {

        String billSql = """
                INSERT INTO bills(customer_id, total)
                VALUES(?, ?)
                """;

        String itemSql = """
                INSERT INTO bill_items(
                    bill_id,
                    product_id,
                    quantity,
                    price
                )
                VALUES(?, ?, ?, ?)
                """;
        String stockSql = """
                UPDATE products
                SET stock_quantity = stock_quantity - ?
                WHERE id = ?
                AND stock_quantity >= ?
                """;

        try (
                Connection connection = DBConnection.connect()
        ) {

            connection.setAutoCommit(false);

            try (
                    PreparedStatement billStatement = connection.prepareStatement(billSql, Statement.RETURN_GENERATED_KEYS)
            ) {

                if (customerId == -1) {
                    billStatement.setNull(1, Types.INTEGER);
                } else {
                    billStatement.setInt(1, customerId);
                }

                billStatement.setDouble(2, total);
                billStatement.executeUpdate();

                ResultSet keys = billStatement.getGeneratedKeys();

                if (!keys.next()) {
                    connection.rollback();
                    return -1;
                }

                int billId = keys.getInt(1);

                try (PreparedStatement itemStatement = connection.prepareStatement(itemSql)
                ) {

                    for (BillItem item : billItems) {

                        itemStatement.setInt(1, billId);

                        itemStatement.setInt(2, item.getProductId());

                        itemStatement.setInt(3, item.getQuantity());

                        itemStatement.setDouble(4, item.getUnitPrice());

                        itemStatement.addBatch();
                    }

                    itemStatement.executeBatch();

                    try (
                            PreparedStatement stockStatement = connection.prepareStatement(stockSql)
                    ) {

                        for (BillItem item : billItems) {

                            stockStatement.setInt(1, item.getQuantity());

                            stockStatement.setInt(2, item.getProductId());

                            stockStatement.setInt(3, item.getQuantity());

                            int updated =
                                    stockStatement.executeUpdate();

                            if (updated == 0) {
                                connection.rollback();

                                System.out.println("Insufficient stock for: " + item.getProductName());

                                return -1;
                            }
                        }
                    }
                }

                connection.commit();
                return billId;

            } catch (SQLException e) {

                connection.rollback();
                e.printStackTrace();
                return -1;
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return -1;
        }
    }
}