package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class UserInterface {
    GridPane loginPage;
    HBox headerBar;
    HBox footerBar;
    VBox body;
    Button signInButton;
    Customer loggedInCustomer;
    Label welcomeLabel;

    ProductList productList = new ProductList();
    VBox productPage;
    Button placeOrderButton = new Button("Place Order");
    ObservableList<Product> itemsInCart = FXCollections.observableArrayList();
    ObservableList<Product> productsOrdered = FXCollections.observableArrayList();
   public BorderPane createContent() {
        BorderPane root = new BorderPane();
        root.setPrefSize(800,600);
//        root.getChildren().add(loginPage);
       root.setTop(headerBar);
//       root.setCenter(loginPage);
       body = new VBox();
       body.setPadding(new Insets(10));
       body.setAlignment(Pos.CENTER);
       root.setCenter(body);
       productPage = productList.getAllProducts();
      body.getChildren().add(productPage);

      root.setBottom(footerBar);
        return root;
    }

    public UserInterface(){
         createLoginPage();
         createHeaderBar();
         createFooterBar();
    }
    private void createLoginPage(){
         Text userNameText = new Text("User Name");
         Text passwordText = new Text("Password");

        TextField userName = new TextField();
        userName.setPromptText("Type your Username here");
        PasswordField password = new PasswordField();
        password.setPromptText("Type your Password here");

        Button loginButton = new Button("Login");
        Label messageLabel = new Label("Hi!");

        loginPage = new GridPane();
//        loginPage.setStyle("-fx-background-color:grey;");
        loginPage.setAlignment(Pos.CENTER);
        loginPage.setHgap(10);
        loginPage.setVgap(10);
        loginPage.add(userNameText,0,0);
        loginPage.add(userName,1,0);
        loginPage.add(passwordText,0,1);
        loginPage.add(password,1,1);
        loginPage.add(loginButton,1,2);
        loginPage.add(messageLabel,0,2);

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @java.lang.Override
            public void handle(ActionEvent actionEvent) {
                String name = userName.getText();
                String pass = password.getText();
                Login login = new Login();
                loggedInCustomer = login.customerLogin(name,pass);
                if(loggedInCustomer!=null){
            messageLabel.setText("Welcome! "+loggedInCustomer.getName() );
            welcomeLabel.setText("Welcome-"+loggedInCustomer.getName());
            headerBar.getChildren().add(welcomeLabel);
                    body.getChildren().clear();
                    body.getChildren().add(productPage);
                }
                else{
            messageLabel.setText("LogIn failed! Please provide Correct Username and Password");
                }
            }
        });

    }

    private void createHeaderBar() {
       Button homeButton = new Button();
        Image image = new Image("C:\\Users\\c8949053\\IdeaProjects\\ECommerce\\src\\ecom.png");
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(120);
        imageView.setFitWidth(120);
        homeButton.setGraphic(imageView);

        TextField searchBar = new TextField();
        searchBar.setPromptText("Search here");
        searchBar.setPrefWidth(280);

        Button searchButton = new Button("Search");

        signInButton = new Button("Sign In");
        welcomeLabel = new Label();

        Button cartButton = new Button("Cart");

        Button orderButton = new Button("Orders");

        headerBar = new HBox(20);
//        headerBar.setStyle("-fx-background-color: grey");
        headerBar.setPadding(new Insets(10));
        headerBar.setSpacing(10);
        headerBar.setAlignment(Pos.CENTER);
        headerBar.getChildren().addAll(homeButton, searchBar, searchButton, signInButton, cartButton, orderButton);

        signInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(loginPage);
                headerBar.getChildren().remove(signInButton);
            }
        });

        cartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                VBox prodPage = productList.getProductsInCart(itemsInCart);
                prodPage.setAlignment(Pos.CENTER);
                prodPage.setSpacing(10);
                prodPage.getChildren().add(placeOrderButton);
                body.getChildren().add(prodPage);
                footerBar.setVisible(false);
;            }
        });

        orderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                VBox orderPage = productList.getOrders(productsOrdered);
                orderPage.setAlignment(Pos.CENTER);
                orderPage.setSpacing(10);
                body.getChildren().add(orderPage);
//                footerBar.setVisible(false);

            }
        });

        placeOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(itemsInCart==null){
                    showDialog("Please add a product first to place Order");
                    return;
                }
                if(loggedInCustomer==null){
                    showDialog("Please login first to place Order");
                    return;
                }
                int count = Order.placeMultipleOrder(loggedInCustomer,itemsInCart);
                if(count!=0)
                {
                    showDialog("Order for "+count+" products placed Successfully!");
                }
                else {
                    showDialog("Order Failed!");
                }
            }
        });

        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();
                body.getChildren().add(productPage);
                footerBar.setVisible(true);
                if(loggedInCustomer==null &&
                headerBar.getChildren().indexOf(signInButton)==-1)
                {
                    headerBar.getChildren().add(signInButton);
                }
            }
        });

    }
        private void createFooterBar(){
            Button buyNow = new Button("Buy Now");
            Button addcartButton = new Button("Add To Cart");
            footerBar = new HBox(20);
            footerBar.setPadding(new Insets(10));
            footerBar.setSpacing(10);
            footerBar.setAlignment(Pos.CENTER);
            footerBar.getChildren().addAll(buyNow,addcartButton);

            buyNow.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Product product= productList.getSelectedProduct();
                    if(product==null){
                        showDialog("Please select a product first to place Order");
                        return;
                    }
                    if(loggedInCustomer==null){
                        showDialog("Please login first to place Order");
                        return;
                    }
                    boolean status = Order.placeOrder(loggedInCustomer,product);
                    if(status==true)
                    {
                        showDialog("Order placed Successfully!");
                    }
                    else {
                        showDialog("Order Failed!");
                    }
                }
            });

            addcartButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    Product product= productList.getSelectedProduct();
                    if(product==null){
                        showDialog("Please select a product first to add it to Cart");
                        return;
                    }
                    itemsInCart.add(product);
                    showDialog("Selected Item has been added to the Cart successfully");
                }
            });

   }
   private void showDialog(String message){
       Alert alert= new Alert(Alert.AlertType.INFORMATION);
       alert.setHeaderText(null);
       alert.setContentText(message);
       alert.setTitle("Message");
       alert.showAndWait();
   }
}
