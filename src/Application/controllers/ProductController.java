package Application.controllers;

import Application.database.DBConnection;
import Application.database.ProductDAO;
import Application.models.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class ProductController {

    @FXML
    private TextField productNameField;
    @FXML
    private ChoiceBox<String> categoryChoiceBox;
    @FXML
    private TextField unitField;
    @FXML
    private TextField purchasePriceField;
    @FXML
    private TextField sellingPriceField;
    @FXML
    private TextField stockQuantityField;
    @FXML
    private TextField reorderLevelField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private void handleClear(ActionEvent event) {
        productNameField.clear();
        unitField.clear();
        purchasePriceField.clear();
        sellingPriceField.clear();
        stockQuantityField.clear();
        reorderLevelField.clear();
        descriptionArea.clear();
        categoryChoiceBox.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleAddProduct(ActionEvent event) {

        System.out.println("ADD PRODUCT CLICKED");

        String productName = productNameField.getText();
        String category = categoryChoiceBox.getValue();
        String unit = unitField.getText();

        double purchasePrice =
                Double.parseDouble(purchasePriceField.getText());

        double sellingPrice =
                Double.parseDouble(sellingPriceField.getText());

        int stockQuantity =
                Integer.parseInt(stockQuantityField.getText());

        int reorderLevel =
                Integer.parseInt(reorderLevelField.getText());

        String description = descriptionArea.getText();

        if (ProductDAO.productExists(productName, unit)) {

            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setTitle("Duplicate Product");
            alert.setHeaderText("Product already exists");
            alert.setContentText(
                    productName + " with unit " + unit
                            + " is already in the database."
            );

            alert.showAndWait();

            return;
        }

        Product product = new Product(
                productName,
                category,
                unit,
                purchasePrice,
                sellingPrice,
                stockQuantity,
                reorderLevel,
                description
        );

        ProductDAO.addProduct(product);

        loadProducts();
    }

    private int selectedProductId = -1;

    private void handleProductSelection() {

        Product selectedProduct =
                productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            return;
        }

        selectedProductId = selectedProduct.getId();

        productNameField.setText(selectedProduct.getProductName());
        categoryChoiceBox.setValue(selectedProduct.getCategory());
        unitField.setText(selectedProduct.getUnit());

        purchasePriceField.setText(
                String.valueOf(selectedProduct.getPurchasePrice())
        );

        sellingPriceField.setText(
                String.valueOf(selectedProduct.getSellingPrice())
        );

        stockQuantityField.setText(
                String.valueOf(selectedProduct.getStockQuantity())
        );

        reorderLevelField.setText(
                String.valueOf(selectedProduct.getReorderLevel())
        );

        descriptionArea.setText(
                selectedProduct.getDescription()
        );
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

    @FXML
    private void handleUpdateProduct(ActionEvent event) {

        if (selectedProductId == -1) {
            showAlert("Please select a product first.");
            return;
        }

        String productName = productNameField.getText();
        String category = categoryChoiceBox.getValue();
        String unit = unitField.getText();

        double purchasePrice =
                Double.parseDouble(purchasePriceField.getText());

        double sellingPrice =
                Double.parseDouble(sellingPriceField.getText());

        int stockQuantity =
                Integer.parseInt(stockQuantityField.getText());

        int reorderLevel =
                Integer.parseInt(reorderLevelField.getText());

        String description = descriptionArea.getText();

        Product product = new Product(
                selectedProductId,
                productName,
                category,
                unit,
                purchasePrice,
                sellingPrice,
                stockQuantity,
                reorderLevel,
                description
        );

        ProductDAO.updateProduct(product);

        loadProducts();

        handleClear(event);

        selectedProductId = -1;
    }

    @FXML
    private void handleDeleteProduct(ActionEvent event) {

        if (selectedProductId == -1) {
            showAlert("Please select a product first.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Delete Product");
        alert.setHeaderText("Delete Product?");
        alert.setContentText(
                "Are you sure you want to delete this product?"
        );

        ButtonType result = alert.showAndWait().orElse(
                ButtonType.CANCEL
        );

        if (result == ButtonType.OK) {

            ProductDAO.deleteProduct(selectedProductId);

            loadProducts();

            handleClear(event);

            selectedProductId = -1;
        }
    }

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, String> unitColumn;

    @FXML
    private TableColumn<Product, Double> purchasePriceColumn;

    @FXML
    private TableColumn<Product, Double> sellingPriceColumn;

    @FXML
    private TableColumn<Product, Integer> stockQuantityColumn;

    public void loadProducts() {

        List<Product> products = ProductDAO.getAllProducts();

        productTable.setItems(
                FXCollections.observableArrayList(products)
        );
    }

    private void setupTableColumns() {

        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        productNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProductName()));

        categoryColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategory()));

        unitColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUnit()));

        purchasePriceColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPurchasePrice()).asObject());

        sellingPriceColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getSellingPrice()).asObject());

        stockQuantityColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getStockQuantity()).asObject());
    }

    @FXML
    public void initialize() {

        categoryChoiceBox.getItems().addAll(
                "Grocery",
                "Dairy",
                "Beverage",
                "Personal Care",
                "Household"
        );

        setupTableColumns();
        loadProducts();

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldProduct, selectedProduct) -> {

            if (selectedProduct != null) {

                selectedProductId = selectedProduct.getId();

                productNameField.setText(
                        selectedProduct.getProductName()
                );

                categoryChoiceBox.setValue(
                        selectedProduct.getCategory()
                );

                unitField.setText(
                        selectedProduct.getUnit()
                );

                purchasePriceField.setText(
                        String.valueOf(
                                selectedProduct.getPurchasePrice()
                        )
                );

                sellingPriceField.setText(
                        String.valueOf(
                                selectedProduct.getSellingPrice()
                        )
                );

                stockQuantityField.setText(
                        String.valueOf(
                                selectedProduct.getStockQuantity()
                        )
                );

                reorderLevelField.setText(
                        String.valueOf(
                                selectedProduct.getReorderLevel()
                        )
                );

                descriptionArea.setText(
                        selectedProduct.getDescription()
                );
            }
        });

        productTable.getSelectionModel().selectedItemProperty().addListener((obs, oldProduct, newProduct) -> {

            if (newProduct != null) {
                handleProductSelection();
            }
        });
    }

    private void showAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("Product");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}