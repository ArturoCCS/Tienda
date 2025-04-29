package com.example.model;

import com.example.apptiendita.CardController;
import com.example.interfaces.Displayable;
import com.example.interfaces.Keyable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;

import static com.example.utility.FormUtility.message;


public abstract class ViewOperable {

    @FXML
    private VBox container;
    @FXML
    private GridPane grid;
    @FXML
    private Label pageIndicator;
    @FXML
    private TextField pageInput;
    @FXML
    private Pagination pagination;
    @FXML
    private HBox paginationArea;
    @FXML
    private ScrollPane scroll;

    private String modo;

    private AnchorPane anchorPaneVenta;

    private static final int ITEMS_PER_PAGE = 20;
    private static final double CELL_WIDTH = 300;

    private int lastPageIndex = -1;
    private int lastColumnCount = -1;
    protected List<? extends Keyable> catalog;
    protected List<? extends Keyable> catalogFiltered;

    private int columnCount = 1;

    private CardController cardController;

    public VBox getContainer() {
        return container;
    }

    public void setContainer(VBox container) {
        this.container = container;
    }

    public GridPane getGrid() {
        return grid;
    }

    public void setGrid(GridPane grid) {
        this.grid = grid;
    }

    public ScrollPane getScroll() {
        return scroll;
    }

    public void setScroll(ScrollPane scroll) {
        this.scroll = scroll;
    }

    public AnchorPane getAnchorPaneVenta() {
        return anchorPaneVenta;
    }

    public void setAnchorPaneVenta(AnchorPane anchorPaneVenta) {
        this.anchorPaneVenta = anchorPaneVenta;
    }

    public CardController getCardController() {
        return cardController;
    }

    public void setCardController(CardController cardController) {
        this.cardController = cardController;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public void setupPagination(List<? extends Keyable> dataListToShow) {
        int totalPages = calculateTotalPages(dataListToShow.size());
        pageIndicator.setText(String.valueOf(totalPages));

        Button prevButton = createPaginationButton("<", true);
        Button nextButton = createPaginationButton(">", false);

        HBox paginationBox = createPaginationBox(prevButton, nextButton);

        updatePagination(0, totalPages, paginationBox, dataListToShow, prevButton, nextButton);
        pagination.setPageCount(totalPages);

        paginationArea.getChildren().setAll(paginationBox);
    }



    public void setDataList(List<? extends Keyable> catalog) {
        this.catalog = catalog;
    }

    public void resetGrid(List<? extends Keyable> catalogFiltered){
        this.lastPageIndex = -1;
        this.catalogFiltered = catalogFiltered;
    }

    protected void setupScrollListener() {
        scroll.widthProperty().addListener((obs, oldVal, newVal) -> {
            columnCount = Math.max((int) (newVal.doubleValue() / CELL_WIDTH), 1);
            createPage(newVal.doubleValue(), pagination.getCurrentPageIndex(), catalogFiltered == null ? catalog : catalogFiltered);
        });
        scroll.setFitToWidth(true);
    }

    private int calculateTotalPages(int totalItems) {
        return (int) Math.ceil((double) totalItems / ITEMS_PER_PAGE);
    }

    private Button createPaginationButton(String label, boolean disabled) {
        Button button = new Button(label);
        button.setDisable(disabled);
        button.getStyleClass().add("page");
        return button;
    }

    private HBox createPaginationBox(Button prevButton, Button nextButton) {
        HBox paginationBox = new HBox(10, prevButton, nextButton);
        paginationBox.setAlignment(Pos.CENTER);
        paginationBox.setPadding(new Insets(10));
        return paginationBox;
    }


    private void updatePagination(int currentPage, int totalPages, HBox paginationBox, List<? extends Keyable> dataListToShow, Button prevButton, Button nextButton) {
        paginationBox.getChildren().clear();
        paginationBox.getChildren().add(prevButton);

        pageInput.setPromptText(String.valueOf(currentPage + 1));
        pageInput.setOnAction(event -> {
            try {
                int pageIndex = Integer.parseInt(pageInput.getText().trim()) - 1;
                if (pageIndex >= 0 && pageIndex < totalPages) {
                    updatePagination(pageIndex, totalPages, paginationBox, dataListToShow, prevButton, nextButton);
                    createPage(scroll.getWidth(), pageIndex, dataListToShow);
                } else {
                    new Alert(Alert.AlertType.WARNING, "Por favor, ingresa un número válido entre 1 y " + totalPages).show();
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Por favor, ingresa un número válido.").show();
            } finally {
                pageInput.clear();
                grid.requestFocus();
            }
        });

        if (totalPages <= 10) {
            for (int i = 0; i < totalPages; i++) {
                createPageButton(i, currentPage, paginationBox, dataListToShow, totalPages, prevButton, nextButton);
            }
        } else {
            if (currentPage < 4) {
                for (int i = 0; i < 3; i++) createPageButton(i, currentPage, paginationBox, dataListToShow, totalPages, prevButton, nextButton);
                paginationBox.getChildren().add(createEllipsis());
                createPageButton(totalPages - 1, currentPage, paginationBox, dataListToShow, totalPages, prevButton, nextButton);
            } else if (currentPage > totalPages - 5) {
                createPageButton(0, currentPage, paginationBox, dataListToShow, totalPages, prevButton, nextButton);
                paginationBox.getChildren().add(createEllipsis());
                for (int i = totalPages - 4; i < totalPages; i++) createPageButton(i, currentPage, paginationBox, dataListToShow, totalPages, prevButton, nextButton);
            } else {
                createPageButton(0, currentPage, paginationBox, dataListToShow, totalPages, prevButton, nextButton);
                paginationBox.getChildren().add(createEllipsis());
                for (int i = currentPage - 1; i <= currentPage + 1; i++) createPageButton(i, currentPage, paginationBox, dataListToShow, totalPages, prevButton, nextButton);
                paginationBox.getChildren().add(createEllipsis());
                createPageButton(totalPages - 1, currentPage, paginationBox, dataListToShow, totalPages, prevButton, nextButton);
            }
        }

        paginationBox.getChildren().add(nextButton);
        prevButton.setDisable(currentPage == 0);
        nextButton.setDisable(currentPage == totalPages - 1);

        prevButton.setOnAction(event -> {
            updatePagination(currentPage - 1, totalPages, paginationBox, dataListToShow, prevButton, nextButton);
            createPage(scroll.getWidth(), currentPage - 1, dataListToShow);
        });

        nextButton.setOnAction(event -> {
            updatePagination(currentPage + 1, totalPages, paginationBox, dataListToShow, prevButton, nextButton);
            createPage(scroll.getWidth(), currentPage + 1, dataListToShow);
        });

        createPage(scroll.getWidth(), currentPage, dataListToShow);
    }

    private void createPageButton(int pageIndex, int currentPage, HBox paginationBox, List<? extends Keyable> dataListToShow, int totalPages, Button prevButton, Button nextButton) {
        Button pageButton = new Button(String.valueOf(pageIndex + 1));
        pageButton.getStyleClass().add(pageIndex == currentPage ? "selected-page" : "page");
        pageButton.setOnAction(event -> {
            updatePagination(pageIndex, totalPages, paginationBox, dataListToShow, prevButton, nextButton);
            createPage(scroll.getWidth(), pageIndex, dataListToShow);
        });
        paginationBox.getChildren().add(pageButton);
    }

    private Label createEllipsis() {
        Label ellipsis = new Label(" ");
        ellipsis.getStyleClass().add("ellipsis");
        return ellipsis;
    }

    private void createPage(double width, int pageIndex, List<? extends Keyable> dataListToShow) {
        int columnCount = Math.max((int) (width / CELL_WIDTH), 1);

        if (pageIndex == lastPageIndex && columnCount == lastColumnCount) return;
        lastPageIndex = pageIndex;
        lastColumnCount = columnCount;

        grid.getChildren().clear();

        if (grid.getColumnConstraints().size() != columnCount) {
            grid.getColumnConstraints().clear();
            for (int i = 0; i < columnCount; i++) {
                ColumnConstraints colConst = new ColumnConstraints();
                colConst.setPercentWidth(100.0 / columnCount);
                colConst.setFillWidth(true);
                grid.getColumnConstraints().add(colConst);
            }
        }

        double cellWidth = width / columnCount;

        int start = pageIndex * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, dataListToShow.size());
        int column = 0;
        int row = 2;

        for (int i = start; i < end; i++) {
            addDataToGrid(dataListToShow.get(i), column, row, cellWidth);
            if (++column == columnCount) {
                column = 0;
                row++;
            }
        }
        centerGrid();

    }

    private void addDataToGrid(Keyable data, int column, int row, double cellWidth) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("card.fxml"));
            AnchorPane anchorPane = loader.load();
            cardController = loader.getController();

            if (data instanceof Displayable) {
                cardController.setProducto((Displayable) data);

                if(modo!=null){
                    insertAction(anchorPane,(Displayable) data);
                }
            }



            anchorPane.setPrefWidth(cellWidth - 20);
            anchorPane.setMaxWidth(Double.MAX_VALUE);
            anchorPane.getStyleClass().add("responsive-card");

            anchorPane.getProperties().put("controller", cardController);
            grid.add(anchorPane, column, row);
            GridPane.setMargin(anchorPane, new Insets(0, 10, 20, 10));
            GridPane.setHgrow(anchorPane, Priority.ALWAYS);
        } catch (IOException e) {
            message("Error", e.getMessage());
        }
    }

    public abstract void insertAction(AnchorPane anchorPane, Displayable data);

    private void centerGrid() {
        double marginSize = 20;
        grid.setPadding(new Insets(0, marginSize, 0, marginSize));
    }

}
