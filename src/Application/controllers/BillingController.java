package Application.controllers;

import Application.database.ProductDAO;
import Application.models.BillItem;
import Application.models.Product;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import javafx.scene.layout.GridPane;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import Application.database.BillingDAO;
import Application.database.CustomerDAO;
import Application.models.Customer;

public class BillingController {
    @FXML
    private Label invoiceNoLabel;

    @FXML
    private Label DateTimeLabel;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<BillItem> billTable;

    @FXML
    private TableColumn<BillItem, Integer> billNumberColumn;

    @FXML
    private TableColumn<BillItem, String> billProductNameColumn;

    @FXML
    private TableColumn<BillItem, Integer> billQuantityColumn;

    @FXML
    private TableColumn<BillItem, Double> billUnitPriceColumn;

    @FXML
    private TableColumn<BillItem, Double> billDiscountColumn;

    @FXML
    private TableColumn<BillItem, Double> billGstColumn;

    @FXML
    private TableColumn<BillItem, Double> billTotalColumn;

    @FXML
    private TableColumn<BillItem, Void> billRemoveColumn;

    @FXML
    private ChoiceBox<Customer> customerChoiceBox;

    @FXML
    private void handleNewCustomer(ActionEvent event) {

        Dialog<ButtonType> dialog = new Dialog<>();

        dialog.setTitle("New Customer");
        dialog.setHeaderText("Add Customer");

        ButtonType addButton =
                new ButtonType(
                        "Add",
                        ButtonBar.ButtonData.OK_DONE
                );

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        addButton,
                        ButtonType.CANCEL
                );

        TextField nameField = new TextField();
        nameField.setPromptText("Customer Name");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        GridPane grid = new GridPane();

        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Phone:"), 0, 1);
        grid.add(phoneField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {

            if (button == addButton) {

                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();

                if (name.isEmpty() || phone.isEmpty()) {

                    showAlert(
                            "Name and phone number are required."
                    );

                    return null;
                }

                int customerId =
                        CustomerDAO.addCustomer(
                                name,
                                phone
                        );

                if (customerId == -1) {

                    showAlert(
                            "Failed to add customer."
                    );

                    return null;
                }

                loadCustomers();

                for (Customer customer :
                        customerChoiceBox.getItems()) {

                    if (customer.getId() == customerId) {

                        customerChoiceBox.setValue(
                                customer
                        );

                        break;
                    }
                }

                return addButton;
            }

            return null;
        });

        dialog.showAndWait();
    }

    @FXML
    private void handleNewBill(ActionEvent event) {

        clearBill();

        customerChoiceBox.getSelectionModel()
                .clearSelection();

        mobileNumberField.clear();

        invoiceNoLabel.setText("NEW");

        searchField.clear();

        loadProductGrid(
                ProductDAO.getAllProducts()
        );
    }

    @FXML
    private void handleClearBill(ActionEvent event) {

        clearBill();

        customerChoiceBox.getSelectionModel()
                .clearSelection();

        mobileNumberField.clear();

        invoiceNoLabel.setText("NEW");
    }

    @FXML
    private TextField mobileNumberField;

    @FXML
    private GridPane productGridPane;

    @FXML
    private Label totalItemsLabel;

    @FXML
    private Label subTotalLabel;

    @FXML
    private Label discountLabel;

    @FXML
    private Label taxableAmountLabel;

    @FXML
    private Label gstLabel;

    @FXML
    private Label grandTotalLabel;

    @FXML
    private ChoiceBox<String> paymentMethodChoiceBox;

    @FXML
    private TextField receivedAmountField;

    @FXML
    private Label returnAmountLabel;

    private void updateBillSummary() {

        double subtotal = 0;
        double discount = 0;
        double gst = 0;

        int totalItems = 0;

        for (BillItem item : billItems) {

            subtotal += item.getQuantity() * item.getUnitPrice();
            discount += item.getDiscount();
            gst += item.getGst();
            totalItems += item.getQuantity();
        }

        double taxableAmount = subtotal - discount;
        double grandTotal = taxableAmount + gst;

        totalItemsLabel.setText(String.valueOf(totalItems));
        subTotalLabel.setText(String.format("%.2f", subtotal));
        discountLabel.setText(String.format("%.2f", discount));
        taxableAmountLabel.setText(String.format("%.2f", taxableAmount));
        gstLabel.setText(String.format("%.2f", gst));
        grandTotalLabel.setText(String.format("%.2f", grandTotal));
    }

    private void calculateReturnAmount() {

        String receivedText = receivedAmountField.getText().trim();

        if (receivedText.isEmpty()) {
            returnAmountLabel.setText("₹ 0.00");
            return;
        }

        try {

            double receivedAmount =
                    Double.parseDouble(receivedText);

            double grandTotal =
                    Double.parseDouble(grandTotalLabel.getText());

            double returnAmount =
                    receivedAmount - grandTotal;

            if (returnAmount < 0) {
                returnAmountLabel.setText("₹ 0.00");
            } else {
                returnAmountLabel.setText(
                        String.format("₹ %.2f", returnAmount)
                );
            }

        } catch (NumberFormatException e) {

            returnAmountLabel.setText("₹ 0.00");
        }
    }

    private final ObservableList<BillItem> billItems = FXCollections.observableArrayList();

    private Product selectedProduct;

    private void updateDateTime() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd MMM yyyy | hh:mm:ss a");

        DateTimeLabel.setText(
                LocalDateTime.now().format(formatter)
        );
    }

    private void loadCustomers() {

        List<Customer> customers =
                CustomerDAO.getAllCustomers();

        customerChoiceBox.getItems().setAll(customers);
    }

    private void setupCustomerSelection() {

        customerChoiceBox.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (obs, oldCustomer, newCustomer) -> {

                            if (newCustomer != null) {

                                mobileNumberField.setText(
                                        newCustomer.getPhone()
                                );

                            } else {

                                mobileNumberField.clear();
                            }
                        }
                );
    }

    private void setupBillTable() {

        billNumberColumn.setCellValueFactory(
                data -> new SimpleIntegerProperty(
                        billItems.indexOf(data.getValue()) + 1
                ).asObject()
        );

        billProductNameColumn.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getProductName()
                )
        );

        billQuantityColumn.setCellValueFactory(
                data -> new SimpleIntegerProperty(
                        data.getValue().getQuantity()
                ).asObject()
        );

        billQuantityColumn.setCellFactory(
                TextFieldTableCell.forTableColumn(
                        new IntegerStringConverter()
                )
        );

        billQuantityColumn.setOnEditCommit(event -> {

            int newQuantity = event.getNewValue();

            if (newQuantity <= 0) {
                billTable.refresh();
                return;
            }

            event.getRowValue().setQuantity(newQuantity);

            billTable.refresh();

            updateBillSummary();
        });

        billTable.setEditable(true);

        billUnitPriceColumn.setCellValueFactory(
                data -> new SimpleDoubleProperty(
                        data.getValue().getUnitPrice()
                ).asObject()
        );

        billDiscountColumn.setCellValueFactory(
                data -> new SimpleDoubleProperty(
                        data.getValue().getDiscount()
                ).asObject()
        );

        billGstColumn.setCellValueFactory(
                data -> new SimpleDoubleProperty(
                        data.getValue().getGst()
                ).asObject()
        );

        billTotalColumn.setCellValueFactory(
                data -> new SimpleDoubleProperty(
                        data.getValue().getTotal()
                ).asObject()
        );

        billTable.setItems(billItems);

        billRemoveColumn.setCellFactory(column -> new TableCell<>() {

            private final Button removeButton = new Button("Remove");

            {
                removeButton.setOnAction(event -> {

                    BillItem item = getTableView()
                            .getItems()
                            .get(getIndex());

                    billItems.remove(item);

                    updateBillSummary();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {

                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(removeButton);
                }
            }
        });
    }

    @FXML
    private void handleSearch(ActionEvent event) {

        String search = searchField.getText().trim();

        if (search.isEmpty()) {
            loadProductGrid(ProductDAO.getAllProducts());
            return;
        }

        List<Product> products = ProductDAO.searchProducts(search);

        if (products.isEmpty()) {
            showAlert("No products found.");
            return;
        }

        loadProductGrid(products);
    }

    @FXML
    private void handleGrocery(ActionEvent event) {

        List<Product> products =
                ProductDAO.getProductsByCategory("Grocery");

        loadProductGrid(products);
    }

    @FXML
    private void handleDairy(ActionEvent event) {
        loadProductGrid(
                ProductDAO.getProductsByCategory("Dairy")
        );
    }

    @FXML
    private void handleBeverage(ActionEvent event) {
        loadProductGrid(
                ProductDAO.getProductsByCategory("Beverage")
        );
    }

    @FXML
    private void handlePersonalCare(ActionEvent event) {
        loadProductGrid(
                ProductDAO.getProductsByCategory("Personal Care")
        );
    }

    @FXML
    private void handleHousehold(ActionEvent event) {
        loadProductGrid(
                ProductDAO.getProductsByCategory("Household")
        );
    }

    @FXML
    private void handleAllItems(ActionEvent event) {
        loadProductGrid(ProductDAO.getAllProducts());
    }

    @FXML
    private void handleAddItem(ActionEvent event) {

        if (selectedProduct == null) {
            showAlert("Please select a product first.");
            return;
        }

        // Check if same product name already exists in bill
        for (BillItem item : billItems) {

            if (item.getProductName().equalsIgnoreCase(
                    selectedProduct.getProductName())) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.setTitle("Product Already Added");
                alert.setHeaderText(
                        selectedProduct.getProductName()
                                + " is already in the bill."
                );
                alert.setContentText(
                        "What would you like to do?"
                );

                ButtonType addQuantityButton =
                        new ButtonType("Add Quantity");

                ButtonType addNewButton =
                        new ButtonType("Add as New");

                ButtonType cancelButton =
                        new ButtonType(
                                "Cancel",
                                ButtonBar.ButtonData.CANCEL_CLOSE
                        );

                alert.getButtonTypes().setAll(
                        addQuantityButton,
                        addNewButton,
                        cancelButton
                );

                ButtonType result = alert.showAndWait().orElse(cancelButton);

                // Add quantity to existing row
                if (result == addQuantityButton) {

                    item.setQuantity(
                            item.getQuantity() + 1
                    );

                    billTable.refresh();
                    updateBillSummary();
                }

                // Add as separate row
                else if (result == addNewButton) {

                    addNewBillItem();
                }

                selectedProduct = null;
                return;
            }
        }

        // Product name does not exist in bill
        addNewBillItem();

        selectedProduct = null;
    }

    private void addNewBillItem() {

        BillItem item = new BillItem(
                selectedProduct.getId(),
                selectedProduct.getProductName(),
                1,
                selectedProduct.getSellingPrice(),
                0,
                selectedProduct.getSellingPrice() * 0.05
        );

        billItems.add(item);

        billTable.refresh();
        updateBillSummary();
    }

    private void loadProductGrid(List<Product> products) {

        productGridPane.getChildren().clear();

        int column = 0;
        int row = 0;

        for (Product product : products) {

            Button productButton = new Button(
                    product.getProductName()
                            + "\n₹" + product.getSellingPrice()
            );

            productButton.setPrefWidth(140);
            productButton.setPrefHeight(75);

            productButton.setOnAction(event -> {
                selectedProduct = product;

                System.out.println(
                        "Selected: " + product.getProductName()
                );
            });

            productGridPane.add(productButton, column, row);

            column++;

            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }

    @FXML
    private void handlePay(ActionEvent event) {

        if (billItems.isEmpty()) {
            showAlert("Bill is empty.");
            return;
        }

        double grandTotal =
                Double.parseDouble(grandTotalLabel.getText());

        String receivedText =
                receivedAmountField.getText().trim();

        if (receivedText.isEmpty()) {
            showAlert("Enter received amount.");
            return;
        }

        double receivedAmount;

        try {
            receivedAmount = Double.parseDouble(receivedText);
        } catch (NumberFormatException e) {
            showAlert("Enter a valid received amount.");
            return;
        }

        if (receivedAmount < grandTotal) {
            showAlert(
                    String.format(
                            "Insufficient amount.\nRequired: ₹%.2f",
                            grandTotal
                    )
            );
            return;
        }

        double returnAmount = receivedAmount - grandTotal;

        returnAmountLabel.setText(
                String.format("₹ %.2f", returnAmount)
        );

        double total =
                Double.parseDouble(grandTotalLabel.getText());

        Customer selectedCustomer =
                customerChoiceBox.getValue();

        int customerId = -1;

        if (selectedCustomer != null) {
            customerId = selectedCustomer.getId();
        }

        int billId = BillingDAO.saveBill(
                customerId,
                total,
                billItems
        );

        if (billId == -1) {
            showAlert("Payment failed. Bill was not saved.");
            return;
        }

        invoiceNoLabel.setText(
                String.format("INV-%06d", billId)
        );

        showAlert(
                "Payment successful!\nInvoice No: "
                        + String.format("INV-%06d", billId)
        );

        clearBill();
    }

    private void clearBill() {

        billItems.clear();

        selectedProduct = null;

        receivedAmountField.clear();

        returnAmountLabel.setText("₹ 0.00");

        updateBillSummary();

        billTable.refresh();
    }

    @FXML
    public void initialize() {

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        e -> updateDateTime()
                ),
                new KeyFrame(Duration.seconds(1))
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        setupBillTable();

        loadProductGrid(ProductDAO.getAllProducts());

        paymentMethodChoiceBox.getItems().addAll(
                "Cash",
                "UPI",
                "Card"
        );

        paymentMethodChoiceBox.setValue("Cash");

        receivedAmountField.textProperty().addListener((obs, oldValue, newValue) -> calculateReturnAmount());

        loadCustomers();

        mobileNumberField.setEditable(false);

        updateBillSummary();
    }

    private void showAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Billing");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}