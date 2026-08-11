package Application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab productsTab;

    @FXML
    private ProductController productPageController;

    @FXML
    public void initialize() {

        tabPane.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldTab, newTab) -> {

                    if (newTab == productsTab) {

                        productPageController.loadProducts();

                    }
                });
    }
}