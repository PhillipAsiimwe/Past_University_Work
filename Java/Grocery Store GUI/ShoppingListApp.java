import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.text.Format;

/**
 * Created by Phllip Amanya 
 */
public class ShoppingListApp extends Application{
    Label prdlabel,shplabel,contlabel,totplabel;
    TextField tfield;
    ListView<GroceryItem> productslist;
    ListView<String> shoppinlist,contentslist;
    Button buy,retr,chek,chek2;
    Shopper shops=new Shopper() ;
    GroceryBag contes=new GroceryBag();
    GroceryItem[] products = {
            new FreezerItem("Smart-Ones Frozen Entrees", 1.99f, 0.311f),
            new GroceryItem("SnackPack Pudding", 0.99f, 0.396f),
            new FreezerItem("Breyers Chocolate Icecream", 2.99f, 2.27f),
            new GroceryItem("Nabob Coffee", 3.99f, 0.326f),
            new GroceryItem("Gold Seal Salmon", 1.99f, 0.213f),
            new GroceryItem("Ocean Spray Cranberry Cocktail", 2.99f, 2.26f),
            new GroceryItem("Heinz Beans Original", 0.79f, 0.477f),
            new RefrigeratorItem("Lean Ground Beef", 4.94f, 0.75f),
            new FreezerItem("5-Alive Frozen Juice", 0.75f, 0.426f),
            new GroceryItem("Coca-Cola 12-pack", 3.49f, 5.112f),
            new GroceryItem("Toilet Paper - 48 pack", 40.96f, 10.89f),
            new RefrigeratorItem("2L Sealtest Milk", 2.99f, 2.06f),
            new RefrigeratorItem("Extra-Large Eggs", 1.79f, 0.77f),
            new RefrigeratorItem("Yoplait Yogurt 6-pack", 4.74f, 1.02f),
            new FreezerItem("Mega-Sized Chocolate Icecream", 67.93f, 15.03f)};

    public void start(Stage primaryStage) {
        Pane aPane = new Pane();

        prdlabel = new Label("Products");
        prdlabel.relocate(10, 10);
        prdlabel.setPrefSize(200, 35);

        shplabel = new Label("Shopping Cart");
        shplabel.relocate(220, 10);
        shplabel.setPrefSize(200, 35);

        contlabel = new Label("Contents");
        contlabel.relocate(430, 10);
        contlabel.setPrefSize(300, 35);

        totplabel = new Label("Total Price");
        totplabel.relocate(565, 365);
        totplabel.setPrefSize(65, 25);

        tfield = new TextField();
        tfield.setText("$0.00");
        tfield.setStyle("-fx-alignment:center-right;");
        tfield.relocate(630, 365);
        tfield.setPrefSize(100, 25);

        productslist = new ListView<GroceryItem>();
        productslist.relocate(10, 45);
        productslist.setPrefSize(200, 300);

        shoppinlist = new ListView<String>();
        shoppinlist.relocate(220, 45);
        shoppinlist.setPrefSize(200, 300);

        contentslist = new ListView<String>();
        contentslist.relocate(430, 45);
        contentslist.setPrefSize(300, 300);

        buy = new Button("Buy");
        buy.relocate(10, 365);
        buy.setPrefSize(200, 25);
        buy.setDisable(true);

        retr = new Button("Return");
        retr.relocate(220, 365);
        retr.setPrefSize(200, 25);
        retr.setDisable(true);

        chek = new Button("Checkout");
        chek.relocate(430, 365);
        chek.setPrefSize(120, 25);

        chek2 = new Button("Restart Shopping");
        chek2.relocate(430, 365);
        chek2.setPrefSize(120, 25);
        chek2.setVisible(false);

        productslist.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                buy.setDisable(false);
                update();
            }
        });

        shoppinlist.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int h = shoppinlist.getSelectionModel().getSelectedIndex();
                if (shops.getCart()[h] instanceof GroceryBag) {
                    contes = (GroceryBag) shops.getCart()[h];
                    update();
                } else {
                    contes = new GroceryBag();
                    retr.setDisable(false);
                    update();
                }
            }
        });

        buy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GroceryItem item=productslist.getSelectionModel().getSelectedItem();
                shops.addItem(item);
                update();
            }
        });

        retr.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String tremovs=shoppinlist.getSelectionModel().getSelectedItem();
                for (int i=0;i<shops.getNumItems();i++){
                    if (shops.getCart()[i].getDescription()==tremovs){
                        shops.removeItem(shops.getCart()[i]);
                        break;
                    }
                }
                update();
            }
        });

        chek.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (int i=0;i<shops.getNumItems();i++){
                    Carryable item=shops.getCart()[i];
                    System.out.println(String.format("%-40S",item.getDescription())+String.format("%10.2f",item.getPrice()));
                }
                System.out.println("--------------------------------------------------");
                System.out.println(String.format("%-40S","total")+String.format("%10.2f",shops.computeTotalCost()));
                shops.packBags();
                update();
                productslist.setDisable(true);
                buy.setDisable(true);
                retr.setDisable(true);
                chek2.setVisible(true);
            }
        });

        chek2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                shops=new Shopper() ;
                contes=new GroceryBag();
                productslist.setDisable(false);
                update();
                chek2.setVisible(false);
            }
        });

        aPane.getChildren().addAll(chek, retr, buy, tfield, totplabel, contlabel, shplabel, prdlabel,productslist,contentslist,shoppinlist,chek2);

        primaryStage.setTitle("My DVD Collenction");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(aPane, 740, 390));
        primaryStage.show();
        update();
    }

    public void update(){
        float total=shops.computeTotalCost();
        tfield.setText(String.valueOf(total));

        String[] names=new String[shops.getNumItems()];
        for (int i=0;i<names.length;i++){names[i]=shops.getCart()[i].getDescription();}

        String[] cont=new String[contes.getNumItems()];
        for (int i=0;i<contes.getNumItems();i++){cont[i]=contes.getItems()[i].toString();}

        shoppinlist.setItems(FXCollections.observableArrayList(names));
        productslist.setItems(FXCollections.observableArrayList(products));
        contentslist.setItems(FXCollections.observableArrayList(cont));

        if (shoppinlist.getItems().isEmpty()){chek.setDisable(true);retr.setDisable(true);}
        else{chek.setDisable(false);}
        if (chek2.isVisible()){retr.setDisable(true);}
    }

}
