package Application.controllers;

import Application.database.CustomerDAO;
import Application.models.Customer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class CustomerController {

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> idColumn;

    @FXML
    private TableColumn<Customer, String> nameColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;

    @FXML
    private Label customerIdLabel;

    @FXML
    private Label customerNameLabel;

    @FXML
    private Label customerPhoneLabel;

    @FXML
    private Label totalPurchaseLabel;

    @FXML
    private Label totalOrdersLabel;

    private final ObservableList<Customer> customers =
            FXCollections.observableArrayList();

    private int selectedCustomerId = -1;


    @FXML
    public void initialize() {

        setupTableColumns();

        loadCustomers();

        setupCustomerSelection();
    }


    private void setupTableColumns() {

        idColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(
                        data.getValue().getId()
                ).asObject()
        );

        nameColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getName()
                )
        );

        phoneColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getPhone()
                )
        );
    }


    private void loadCustomers() {

        customers.setAll(
                CustomerDAO.getAllCustomers()
        );

        customerTable.setItems(customers);
    }


    private void setupCustomerSelection() {

        customerTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (obs, oldCustomer, newCustomer) -> {

                            if (newCustomer == null) {
                                return;
                            }

                            selectedCustomerId =
                                    newCustomer.getId();

                            customerIdLabel.setText(
                                    String.valueOf(
                                            newCustomer.getId()
                                    )
                            );

                            customerNameLabel.setText(
                                    newCustomer.getName()
                            );

                            customerPhoneLabel.setText(
                                    newCustomer.getPhone()
                            );
                        }
                );
    }


    // ================= ADD =================

    @FXML
    private void handleAddCustomer(ActionEvent event) {

        TextInputDialog nameDialog =
                new TextInputDialog();

        nameDialog.setTitle("Add Customer");
        nameDialog.setHeaderText("Add New Customer");
        nameDialog.setContentText("Customer Name:");

        nameDialog.showAndWait().ifPresent(inputName -> {

            String name = inputName.trim();

            if (name.isEmpty()) {
                showAlert("Customer name is required.");
                return;
            }

            TextInputDialog phoneDialog =
                    new TextInputDialog();

            phoneDialog.setTitle("Add Customer");
            phoneDialog.setHeaderText(
                    "Enter customer phone number"
            );
            phoneDialog.setContentText("Phone:");

            phoneDialog.showAndWait().ifPresent(inputPhone -> {

                String phone = inputPhone.trim();

                if (phone.isEmpty()) {
                    showAlert("Phone number is required.");
                    return;
                }

                int id = CustomerDAO.addCustomer(
                        name,
                        phone
                );

                if (id == -1) {
                    showAlert("Failed to add customer.");
                    return;
                }

                loadCustomers();

                showAlert(
                        "Customer added successfully."
                );
            });
        });
    }


    // ================= UPDATE =================

    @FXML
    private void handleUpdateCustomer(ActionEvent event) {

        if (selectedCustomerId == -1) {

            showAlert(
                    "Please select a customer first."
            );

            return;
        }

        TextInputDialog nameDialog =
                new TextInputDialog(
                        customerNameLabel.getText()
                );

        nameDialog.setTitle("Update Customer");
        nameDialog.setHeaderText(
                "Update customer name"
        );
        nameDialog.setContentText("Name:");

        nameDialog.showAndWait().ifPresent(inputName -> {

            String name = inputName.trim();

            if (name.isEmpty()) {

                showAlert(
                        "Customer name is required."
                );

                return;
            }

            TextInputDialog phoneDialog =
                    new TextInputDialog(
                            customerPhoneLabel.getText()
                    );

            phoneDialog.setTitle("Update Customer");
            phoneDialog.setHeaderText(
                    "Update customer phone"
            );
            phoneDialog.setContentText("Phone:");

            phoneDialog.showAndWait().ifPresent(inputPhone -> {

                String phone = inputPhone.trim();

                if (phone.isEmpty()) {

                    showAlert(
                            "Phone number is required."
                    );

                    return;
                }

                boolean updated =
                        CustomerDAO.updateCustomer(
                                selectedCustomerId,
                                name,
                                phone
                        );

                if (!updated) {

                    showAlert(
                            "Failed to update customer."
                    );

                    return;
                }

                loadCustomers();

                handleClear(event);

                showAlert(
                        "Customer updated successfully."
                );
            });
        });
    }


    // ================= DELETE =================

    @FXML
    private void handleDeleteCustomer(ActionEvent event) {

        if (selectedCustomerId == -1) {

            showAlert(
                    "Please select a customer first."
            );

            return;
        }

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Delete Customer"
        );

        confirmation.setHeaderText(
                "Delete Customer?"
        );

        confirmation.setContentText(
                "Are you sure you want to delete "
                        + customerNameLabel.getText()
                        + "?"
        );

        ButtonType result =
                confirmation.showAndWait()
                        .orElse(ButtonType.CANCEL);

        if (result != ButtonType.OK) {
            return;
        }

        boolean deleted =
                CustomerDAO.deleteCustomer(
                        selectedCustomerId
                );

        if (!deleted) {

            showAlert(
                    "Could not delete customer."
            );

            return;
        }

        loadCustomers();

        handleClear(event);

        showAlert(
                "Customer deleted successfully."
        );
    }


    // ================= REFRESH =================

    @FXML
    private void handleRefresh(ActionEvent event) {

        loadCustomers();
    }


    // ================= CLEAR =================

    @FXML
    private void handleClear(ActionEvent event) {

        customerTable.getSelectionModel()
                .clearSelection();

        selectedCustomerId = -1;

        customerIdLabel.setText("-");
        customerNameLabel.setText("-");
        customerPhoneLabel.setText("-");
        totalPurchaseLabel.setText("₹ 0.00");
        totalOrdersLabel.setText("0");
    }


    // ================= ALERT =================

    private void showAlert(String message) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Customers");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}