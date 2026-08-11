package Application.controllers;

import Application.database.DashboardDAO;
import Application.database.DashboardDAO.RecentTransaction;
import Application.database.DashboardDAO.TopProduct;
import Application.models.Product;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;

import javafx.fxml.FXML;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Map;

public class DashboardController {

    // ================= CARDS =================

    @FXML
    private Label totalSalesLabel;

    @FXML
    private Label totalOrdersLabel;

    @FXML
    private Label totalProductsLabel;

    @FXML
    private Label totalProfitLabel;


    // ================= CHART =================

    @FXML
    private LineChart<String, Number> salesChart;


    // ================= LOW STOCK =================

    @FXML
    private TableView<Product> lowStockTable;

    @FXML
    private TableColumn<Product, String>
            lowStockProductColumn;

    @FXML
    private TableColumn<Product, Integer>
            lowStockQuantityColumn;


    // ================= TOP PRODUCTS =================

    @FXML
    private TableView<TopProduct> topProductsTable;

    @FXML
    private TableColumn<TopProduct, Integer>
            topProductColumn;

    @FXML
    private TableColumn<TopProduct, String>
            topProductNameColumn;

    @FXML
    private TableColumn<TopProduct, Integer>
            topSoldQuantityColumn;

    @FXML
    private TableColumn<TopProduct, Double>
            topRevenueColumn;


    // ================= RECENT TRANSACTIONS =================

    @FXML
    private TableView<RecentTransaction>
            recentTransactionsTable;

    @FXML
    private TableColumn<RecentTransaction, Integer>
            invoiceColumn;

    @FXML
    private TableColumn<RecentTransaction, String>
            customerColumn;

    @FXML
    private TableColumn<RecentTransaction, Double>
            amountColumn;

    @FXML
    private TableColumn<RecentTransaction, String>
            timeColumn;


    // ================= INITIALIZE =================

    @FXML
    public void initialize() {

        setupLowStockTable();

        setupTopProductsTable();

        setupRecentTransactionsTable();

        loadDashboard();
    }


    // ================= LOAD EVERYTHING =================

    private void loadDashboard() {

        loadSummaryCards();

        loadSalesChart();

        loadLowStock();

        loadTopProducts();

        loadRecentTransactions();
    }


    // ================= SUMMARY =================

    private void loadSummaryCards() {

        double totalSales =
                DashboardDAO.getTotalSales();

        int totalOrders =
                DashboardDAO.getTotalOrders();

        int totalProducts =
                DashboardDAO.getTotalProducts();

        double totalProfit =
                DashboardDAO.getTotalProfit();


        totalSalesLabel.setText(
                String.format(
                        "₹ %.2f",
                        totalSales
                )
        );

        totalOrdersLabel.setText(
                String.valueOf(totalOrders)
        );

        totalProductsLabel.setText(
                String.valueOf(totalProducts)
        );

        totalProfitLabel.setText(
                String.format(
                        "₹ %.2f",
                        totalProfit
                )
        );
    }


    // ================= LOW STOCK =================

    private void setupLowStockTable() {

        lowStockProductColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getProductName()
                        )
        );

        lowStockQuantityColumn.setCellValueFactory(
                data ->
                        new SimpleIntegerProperty(
                                data.getValue()
                                        .getStockQuantity()
                        ).asObject()
        );
    }


    private void loadLowStock() {

        List<Product> products =
                DashboardDAO.getLowStockProducts();

        lowStockTable.setItems(
                FXCollections.observableArrayList(
                        products
                )
        );
    }


    // ================= TOP PRODUCTS =================

    private void setupTopProductsTable() {

        topProductColumn.setCellValueFactory(
                data ->
                        new SimpleIntegerProperty(
                                data.getValue()
                                        .getProductId()
                        ).asObject()
        );

        topProductNameColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getProductName()
                        )
        );

        topSoldQuantityColumn.setCellValueFactory(
                data ->
                        new SimpleIntegerProperty(
                                data.getValue()
                                        .getQuantity()
                        ).asObject()
        );

        topRevenueColumn.setCellValueFactory(
                data ->
                        new SimpleDoubleProperty(
                                data.getValue()
                                        .getRevenue()
                        ).asObject()
        );
    }


    private void loadTopProducts() {

        List<TopProduct> products =
                DashboardDAO.getTopSellingProducts();

        topProductsTable.setItems(
                FXCollections.observableArrayList(
                        products
                )
        );
    }


    // ================= RECENT TRANSACTIONS =================

    private void setupRecentTransactionsTable() {

        invoiceColumn.setCellValueFactory(
                data ->
                        new SimpleIntegerProperty(
                                data.getValue()
                                        .getInvoiceId()
                        ).asObject()
        );

        customerColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getCustomerName()
                        )
        );

        amountColumn.setCellValueFactory(
                data ->
                        new SimpleDoubleProperty(
                                data.getValue()
                                        .getAmount()
                        ).asObject()
        );

        timeColumn.setCellValueFactory(
                data ->
                        new SimpleStringProperty(
                                data.getValue()
                                        .getTime()
                        )
        );
    }


    private void loadRecentTransactions() {

        List<RecentTransaction> transactions =
                DashboardDAO.getRecentTransactions();

        recentTransactionsTable.setItems(
                FXCollections.observableArrayList(
                        transactions
                )
        );
    }


    // ================= SALES CHART =================

    private void loadSalesChart() {

        salesChart.getData().clear();

        XYChart.Series<String, Number> series =
                new XYChart.Series<>();

        series.setName("Sales");

        Map<String, Double> sales =
                DashboardDAO.getLast7DaysSales();

        for (Map.Entry<String, Double> entry :
                sales.entrySet()) {

            series.getData().add(
                    new XYChart.Data<>(
                            entry.getKey(),
                            entry.getValue()
                    )
            );
        }

        salesChart.getData().add(series);
    }
}