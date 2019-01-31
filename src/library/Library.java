/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.sql.*;
import javax.swing.event.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 *
 * @author Esdras
 */
public class Library extends Application {
    public static Stage primaryStage;
    ArrayList<VBox> tabListe=new ArrayList<>();
    final private String url="jdbc:mysql://localhost:3306/baseDonnee";
    final private String user="root";
    final private String password="esdras";
    Connection connexion;
    public Library() throws SQLException{
        this.connexion = DriverManager.getConnection(url,user,password);
    }
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage=primaryStage;
        VBox vbox= new VBox();       
        MenuBar menu=creerMenu();
        VBox vbox2= ongletAccueil();
        ComboBox<String> choix=(ComboBox<String>) vbox2.getChildren().get(0);
        choix.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                 if(newValue.equals("Articles")){
                    vbox2.getChildren().set(1,tableArticle());
                 }
                else{
                    if(newValue.equals("Livres"))
                        vbox2.getChildren().set(1,tableLivre());
                    if(newValue.equals("Cahiers"))
                        vbox2.getChildren().set(1,tableCahier());
                }
            }   
        });
        vbox.getChildren().addAll(menu,vbox2);
        StackPane root = new StackPane();
        root.getChildren().add(vbox);
        GraphicsEnvironment graphique= GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rect= graphique.getMaximumWindowBounds();
        Scene scene = new Scene(root,rect.getWidth()*3/4, rect.getHeight()*3/4);
        primaryStage.setTitle("Library");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    public MenuBar creerMenu(){
        MenuBar menu= new MenuBar();
        
        //creation des differents menus de la barre de menu
        Menu choix =new Menu("Dossier");
        menu.getMenus().addAll(choix,new Menu("Aide"));
        
        //creation sous menu ***sauver***
        MenuItem sauver = new MenuItem("Sauvegarder");
        sauver.setId("Sauvegarder");
        sauver.setDisable(true);
        sauver.setOnAction((ActionEvent e)->{
           saveBd(); 
        });
        //creation sous menu ***restaurer***
        MenuItem restaurer = new MenuItem("Restaurer");
        restaurer.setId("Restaurer");
        restaurer.setDisable(true);
        restaurer.setOnAction((ActionEvent e)->{
            openBd();
        });
        //creation sous menu ***quitter***
        MenuItem quitter = new MenuItem("Quitter");
        quitter.setOnAction((ActionEvent t) -> {
            System.exit(0);
        });
        quitter.setId("Quitter");
        
        //ajout de ces sous menus au menu Dossier
        choix.getItems().addAll(sauver, restaurer, new SeparatorMenuItem(), quitter);
             
        //creation sous menu ***aide***
        MenuItem aide= new MenuItem("Aide");
        aide.setId("Aide");
        
        //ajout de ce sous menu au menu Aide
        menu.getMenus().get(1).getItems().add(aide);        
          
        return menu;
    }
    private VBox ongletAccueil(){
        VBox vbox= new VBox();
        vbox.setPadding(new Insets(45));
        VBox tableau= new VBox();
        vbox.setId("Liste des Articles");
        ComboBox<String> choix = new ComboBox<String>();
        choix.getItems().addAll("Articles","Livres","Cahiers");
        choix.getSelectionModel().select(0);
        int pp= choix.getSelectionModel().getSelectedIndex();
        if(pp==0){
            tableau=tableArticle();
        }
        else{
            if(pp==1)
                tableau=tableLivre();
            else
                tableau=tableCahier();
        }
        tableau.setPadding(new Insets(30,0,30,30));
        ButtonBar barre2= new ButtonBar();
        barre2.setPadding(new Insets(50,60,20,10));
        Button quitter =new Button("Quitter");
        quitter.setId("Quitter");
        quitter.setAlignment(Pos.BOTTOM_RIGHT);
        quitter.setOnAction((ActionEvent t) -> {
            System.exit(0);
        });
        barre2.getButtons().addAll(quitter);
        vbox.getChildren().addAll(choix,tableau,barre2);
        return vbox;
    }
     private VBox tableLivre(){
        TableView<Livre> table=new TableView<>();
        initTableLivre(table);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(table,creerLivre(table));
        vbox.setPadding(new Insets(30));
        return vbox;
    }
    private VBox tableCahier(){
        TableView<Cahier> table=new TableView<>();
        initTableCahier(table);
        
        VBox vbox = new VBox();                                                                                                                                                                                                                         
        vbox.getChildren().addAll(table,creerCahier(table));
        vbox.setPadding(new Insets(30));
        return vbox;
    }
    private VBox tableArticle(){
        GraphicsEnvironment graphique= GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rect= graphique.getMaximumWindowBounds();
        TableView<Article> table=new TableView<>();
        TableColumn<Article, String> colId = new TableColumn<>("ID");
        TableColumn<Article, Integer> colQte = new TableColumn<>("Quantité");
        TableColumn<Article, Integer> colPrix = new TableColumn<>("P.U");
        table.getColumns().addAll(colId,colPrix,colQte);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixUni"));
        colQte.setCellValueFactory(new PropertyValueFactory<>("qte"));
        table.setItems(FXCollections.observableList(listeArticle()));
        colId.setPrefWidth(0.33*(rect.getWidth()*3/4-2*table.getPadding().getLeft()));
        colPrix.setPrefWidth(0.1*(rect.getWidth()*3/4-2*table.getPadding().getLeft()));
        colQte.setPrefWidth(0.1*(rect.getWidth()*3/4-2*table.getPadding().getLeft()));
        VBox vbox= new VBox();
        vbox.getChildren().addAll(table);
        vbox.setPadding(new Insets(30));
        return vbox;
    }
    private List<Article> listeArticle(){
        List<Article> liste=null;
        try{
            liste = new ArrayList<Article>();
            String Requete= "SELECT id, prix, qte FROM cahier";
            String Requete2="SELECT id, prix, qte FROM livre";
            Statement stmt= connexion.createStatement();
            ResultSet rset=stmt.executeQuery(Requete);            
            while (rset.next()){
            Article article2=rsetToArticle(rset);
                liste.add(article2);
            }
            rset=stmt.executeQuery(Requete2);
            while (rset.next()){
            Article article2=rsetToArticle(rset);
           liste.add(article2);
            }
            rset.close();
        }
        catch(SQLException exc){
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, exc);
        }
        return liste;
    }
    private List<Livre> listeLivre(){
        List<Livre> liste=null;
            try{
                liste=new ArrayList<Livre>();
                String Requete= "SELECT * FROM livre";
                Statement stmt= connexion.createStatement();
                ResultSet rset= stmt.executeQuery(Requete);
                while (rset.next()){
                    Livre livre=rsetToLivre(rset);
                    liste.add(livre);
                }
                rset.close();
            }
            catch(SQLException exc){
                Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, exc);
            }
        return liste;
    }
    private List<Cahier> listeCahier(){
        List<Cahier> liste=null;
            try{
                liste=new ArrayList<Cahier>();
                String Requete= "SELECT * FROM cahier";
                Statement stmt= connexion.createStatement();
                ResultSet rset= stmt.executeQuery(Requete);
                while (rset.next()){
                    Cahier cahier=rsetToCahier(rset);
                    liste.add(cahier);
                }
                rset.close();
            }
            catch(SQLException exc){
                Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, exc);
            }
        return liste;
    }
    private Article rsetToArticle(ResultSet rset) throws SQLException{
        Article article= new Article(rset.getString("id"),rset.getInt("prix"),rset.getInt("qte"));
        return article;
    }
    private Livre rsetToLivre(ResultSet rset) throws SQLException{
        Livre livre= new Livre(rset.getString("id"),rset.getString("nom"),rset.getString("maisonEdition"),rset.getString("classe"), 
    rset.getString("matiere"),rset.getString("auteur"), rset.getInt("prix"),rset.getInt("qte"));
        return livre;
    }
    private Cahier rsetToCahier(ResultSet rset) throws SQLException{
        Cahier cahier=new Cahier(rset.getString("id"),rset.getString("marque"),rset.getString("nature"),rset.getInt("nbPages"),rset.getInt("prix"),rset.getInt("qte"));
        return cahier;
    }
     public VBox creerLivre(TableView<Livre> table){ 
        TextField addId = new TextField();
        addId.setPromptText("Id");
        addId.setPrefWidth(table.getColumns().get(0).getPrefWidth());
        TextField addTitre = new TextField();
        addTitre.setPromptText("Titre");
        addTitre.setPrefWidth(table.getColumns().get(1).getPrefWidth());
        TextField addMaison = new TextField();
        addMaison.setPromptText("Maison d'Edition");
        addMaison.setPrefWidth(table.getColumns().get(2).getPrefWidth());
        TextField addClasse = new TextField();
        addClasse.setPromptText("Classe concernée");
        addClasse.setPrefWidth(table.getColumns().get(3).getPrefWidth());
        TextField addMatiere = new TextField();
        addMatiere.setPromptText("Matière");
        addMatiere.setPrefWidth(table.getColumns().get(4).getPrefWidth());
        TextField addAuteur = new TextField();
        addAuteur.setPromptText("Auteur");
        addAuteur.setPrefWidth(table.getColumns().get(5).getPrefWidth());
        TextField addPrix = new TextField();
        addPrix.setPromptText("Prix Unitaire");
        addPrix.setPrefWidth(table.getColumns().get(6).getPrefWidth());
        TextField addQte = new TextField();
        addQte.setPromptText("Quantité");
        addQte.setPrefWidth(table.getColumns().get(7).getPrefWidth());
        Button addBouton =new Button("Ajouter");
        Button modifier = new Button("Modifier");
        Button effacer = new Button("Effacer");
        HBox hbox2 = new HBox();
        Button ajouterQte = new Button("Ajouter ou Retirer une quantité");
        TextField textIn = new TextField();
        textIn.setPromptText("Ajouter ou Retirer une quantité");
        Button supprimer = new Button("Supprimer");
        hbox2.getChildren().addAll(textIn,ajouterQte);
        effacer.setOnAction((ActionEvent e)->{
            cleanText(addId, addTitre, addMaison, addClasse, addMatiere, addAuteur, addPrix, addQte);
        });
        modifier.setDisable(false);
        addBouton.setDisable(false);
        addBouton.setOnAction((ActionEvent t) ->{
        try {   
            Statement stmt =connexion.createStatement();
            int rs= stmt.executeUpdate("INSERT INTO livre VALUES ('"
            +addId.getText()+"','"+addTitre.getText()+"','"+addMaison.getText()+"','"+addClasse.getText()+"','"+addMatiere.getText()+"','" +
            addAuteur.getText()+"'," +
            Integer.parseInt(addPrix.getText())+","+Integer.parseInt(addQte.getText())+")");
            stmt.close();
            table.setItems(FXCollections.observableList(listeLivre()));
            cleanText(addId, addTitre, addMaison, addClasse, addMatiere, addAuteur, addPrix, addQte);
        }catch (SQLException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
        }
        });       
        table.getSelectionModel().selectedItemProperty().addListener((obs,ancVal,nouvVal)->{
            if(nouvVal!=null){
                modifier.setDisable(false);
                addBouton.setDisable(false);
                addId.setText(nouvVal.getId());
                addTitre.setText(nouvVal.getTitre());
                addMaison.setText(nouvVal.getMaisonEdition());
                addClasse.setText(nouvVal.getClasseConcernee());
                addMatiere.setText(nouvVal.getMatiereConcernee());
                addAuteur.setText(nouvVal.getAuteur());
                addPrix.setText(nouvVal.getPrixUni().toString());
                addQte.setText(nouvVal.getQte().toString());
                modifier.setOnAction((ActionEvent e)->{
                    try {   
                        Statement stmt =connexion.createStatement();
                        int rs= stmt.executeUpdate("UPDATE livre SET id='"+addId.getText()+"',nom='"+addTitre.getText()+"',maisonEdition='"+addMaison.getText()+"',classe='"+addClasse.getText()+
                        "',matiere='"+addMatiere.getText()+"',auteur='" +
                        addAuteur.getText()+"',prix=" +
                        Integer.parseInt(addPrix.getText())+",qte="+Integer.parseInt(addQte.getText())+" WHERE id='"+nouvVal.getId()+"'");
                        stmt.close();
                        table.setItems(FXCollections.observableList(listeLivre()));
                        cleanText(addId, addTitre, addMaison, addClasse, addMatiere, addAuteur, addPrix, addQte);          
                        }catch (SQLException ex) {
                            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                supprimer.setOnAction((ActionEvent e)->{
                    Alert pope = new Alert(AlertType.CONFIRMATION);
                    pope.setTitle("Confirmer la suppression");
                    pope.setHeaderText(null);
                    pope.setContentText("Supprimer ?");
                    Optional<ButtonType> reponse=pope.showAndWait();
                    if(reponse.get()==ButtonType.OK){
                    try {
                        Statement stmt = connexion.createStatement();
                        String requete= "DELETE FROM livre WHERE id='"+nouvVal.getId()+"'";
                        int entier = stmt.executeUpdate(requete);
                        table.setItems(FXCollections.observableList(listeLivre()));
                    }catch (SQLException ex) {
                        Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                });
                ajouterQte.setOnAction((ActionEvent e)->{
                    try{
                        Statement stmt=connexion.createStatement();
                        String texte =textIn.getText();
                        if(texte!=null){
                            nouvVal.qte= nouvVal.addQte(Integer.parseInt(texte));
                            int req= stmt.executeUpdate("UPDATE livre SET qte ="+nouvVal.qte+" WHERE id = '"+nouvVal.id+"'");
                            table.setItems(FXCollections.observableList(listeLivre()));
                        }
                        textIn.clear();
                        stmt.close();
                    }catch(SQLException ex){
                    Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
                    }   
                });
        }
    });  
        HBox hbox= new HBox();
        hbox.getChildren().addAll(addId,addTitre,addMaison,addClasse,addMatiere,addAuteur,addPrix,addQte,addBouton,modifier, effacer,supprimer);
        hbox.setPadding(new Insets(10,0,10,0));
        VBox vbox = new VBox();
        vbox.getChildren().addAll(hbox,hbox2);
        return vbox;
    }
    public void cleanText(TextField...texte){
        for(TextField txt: texte)
            txt.clear();
    }
    public void initTableLivre(TableView<Livre> table){
        GraphicsEnvironment graphique= GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rect= graphique.getMaximumWindowBounds();
        TableColumn<Livre, String> colId = new TableColumn<>("ID");
        TableColumn<Livre, String> colTitre = new TableColumn<>("Titre");
        TableColumn<Livre, String> colMaison = new TableColumn<>("Maison d'Edition");
        TableColumn<Livre, String> colClasse = new TableColumn<>("Classe Concernée");
        TableColumn<Livre, String> colMatiere = new TableColumn<>("Matière Concernée");
        TableColumn<Livre, String> colAuteur = new TableColumn<>("Auteur");
        TableColumn<Livre, Integer> colPrix = new TableColumn<>("P.U");
        TableColumn<Livre, Integer> colQte = new TableColumn<>("Quantité");
        table.getColumns().addAll(colId,colTitre,colMaison,colClasse,colMatiere,colAuteur,colPrix,colQte);
        colId.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())/12);
        colTitre.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())/12);
        colMaison.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())/12);
        colClasse.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())/12);
        colMatiere.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())/12);
        colAuteur.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())/12);
        colPrix.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())/12);
        colQte.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())/12);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colMaison.setCellValueFactory(new PropertyValueFactory<>("maisonEdition"));
        colClasse.setCellValueFactory(new PropertyValueFactory<>("classeConcernee"));
        colMatiere.setCellValueFactory(new PropertyValueFactory<>("matiereConcernee"));
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixUni"));
        colQte.setCellValueFactory(new PropertyValueFactory<>("qte"));
        table.setItems(FXCollections.observableList(listeLivre()));
    }
    public void initTableCahier(TableView<Cahier> table){
        GraphicsEnvironment graphique= GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rect= graphique.getMaximumWindowBounds();
        TableColumn<Cahier, String> colId = new TableColumn<>("ID");
        TableColumn<Cahier, String> colMarque = new TableColumn<>("Marque");
        TableColumn<Cahier, String> colNature = new TableColumn<>("Nature(Cartonné?)");
        TableColumn<Cahier, Integer> colNbPages = new TableColumn<>("Nombre de Pages");
        TableColumn<Cahier, Integer> colPrix = new TableColumn<>("P.U");
        TableColumn<Cahier, Integer> colQte = new TableColumn<>("Quantité");
        table.getColumns().addAll(colId,colMarque,colNature,colNbPages,colPrix,colQte);
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colMarque.setCellValueFactory(new PropertyValueFactory<>("marque"));
        colNature.setCellValueFactory(new PropertyValueFactory<>("nature"));
        colNbPages.setCellValueFactory(new PropertyValueFactory<>("nbPages"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixUni"));
        colQte.setCellValueFactory(new PropertyValueFactory<>("qte"));
        colId.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())*1/9);
        colMarque.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())*1/9);
        colNature.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())*1/9);
        colNbPages.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())*1/9);
        colPrix.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())*1/9);
        colQte.setPrefWidth((rect.getWidth()*3/4-2*table.getPadding().getLeft())*1/9);
        table.setItems(FXCollections.observableList(listeCahier()));
    }
    public VBox creerCahier(TableView<Cahier> table){
        TextField addId = new TextField();
        addId.setPromptText("Id");
        addId.setPrefWidth(table.getColumns().get(0).getPrefWidth());
        TextField addMarque = new TextField();
        addMarque.setPromptText("Marque");
        addMarque.setPrefWidth(table.getColumns().get(1).getPrefWidth());
        TextField addNature = new TextField();
        addNature.setPromptText("Nature");
        addNature.setPrefWidth(table.getColumns().get(2).getPrefWidth());
        TextField addNbPages = new TextField();
        addNbPages.setPromptText("Nombre de pages");
        addNbPages.setPrefWidth(table.getColumns().get(3).getPrefWidth());
        TextField addPrix = new TextField();
        addPrix.setPromptText("Prix Unitaire");
        addPrix.setPrefWidth(table.getColumns().get(4).getPrefWidth());
        TextField addQte = new TextField();
        addQte.setPromptText("Quantité");
        addQte.setPrefWidth(table.getColumns().get(5).getPrefWidth());
        Button addBouton =new Button("Ajouter");
        Button modifier = new Button("Modifier");
        Button effacer = new Button("Effacer");
        HBox hbox2 = new HBox();
        Button ajouterQte = new Button("Ajouter ou Retirer une quantité");
        TextField textIn = new TextField();
        textIn.setPromptText("Ajouter ou Retirer une quantité");
        Button supprimer = new Button("Supprimer");
        hbox2.getChildren().addAll(textIn,ajouterQte);
        effacer.setOnAction((ActionEvent e)->{
            cleanText(addId, addMarque, addNature, addNbPages, addPrix, addQte);  
        });
        modifier.setDisable(false);
        addBouton.setOnAction((ActionEvent t) ->{
                try {
                Statement stmt =connexion.createStatement();
                int rs= stmt.executeUpdate("INSERT INTO cahier "
                        + "VALUES('"+addId.getText()+"','"+addMarque.getText()+"','"+
                        addNature.getText()+"',"+Integer.parseInt(addPrix.getText())+","+
                        Integer.parseInt(addQte.getText())+","+Integer.parseInt(addNbPages.getText())+")");
                table.setItems(FXCollections.observableList(listeCahier()));
                cleanText(addId, addMarque, addNature, addNbPages, addPrix, addQte);  
                stmt.close();
                 table.setItems(FXCollections.observableList(listeCahier()));
                }catch (SQLException ex) {
                Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
                }
        });
        table.getSelectionModel().selectedItemProperty().addListener((obs,ancVal,nouvVal)->{
            if(nouvVal!=null){
                TableRow<Cahier> ligne= new TableRow<Cahier>();
                modifier.setDisable(false);
                addBouton.setDisable(false);
                addId.setText(nouvVal.getId());
                addMarque.setText(nouvVal.getMarque());
                addNature.setText(nouvVal.getNature());
                addPrix.setText(nouvVal.getPrixUni().toString());
                addQte.setText(nouvVal.getQte().toString());
                addNbPages.setText(nouvVal.getNbPages().toString());
                modifier.setOnAction((ActionEvent e)->{
                    try {   
                        Statement stmt= connexion.createStatement();
                        int rs= stmt.executeUpdate("UPDATE cahier SET id='"
                        +addId.getText()+"',marque='"+addMarque.getText()+"',nature='"+
                        addNature.getText()+"',prix="+Integer.parseInt(addPrix.getText())+",qte="+
                        Integer.parseInt(addQte.getText())+",nbPages="+Integer.parseInt(addNbPages.getText())+" WHERE id='"+nouvVal.getId()+"'");
                        table.setItems(FXCollections.observableList(listeCahier()));
                        cleanText(addId, addMarque, addNature, addNbPages, addPrix, addQte);
                    }catch (SQLException ex) {
                        Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                    supprimer.setOnAction((ActionEvent e)->{
                    Alert pope = new Alert(AlertType.CONFIRMATION);
                    pope.setTitle("Confirmer la suppression");
                    pope.setHeaderText(null);
                    pope.setContentText("Supprimer ?");
                    Optional<ButtonType> reponse=pope.showAndWait();
                    if(reponse.get()==ButtonType.OK){
                    try {
                        Statement stmt = connexion.createStatement();
                        String requete= "DELETE FROM cahier WHERE id='"+nouvVal.getId()+"'";
                        int entier = stmt.executeUpdate(requete);
                        table.setItems(FXCollections.observableList(listeCahier()));
                    }catch (SQLException ex) {
                        Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    }
                });
                ajouterQte.setOnAction((ActionEvent e)->{
                    try{
                        Statement stmt=connexion.createStatement();
                        String texte =textIn.getText();
                        if(texte!=null){
                            nouvVal.qte= nouvVal.addQte(Integer.parseInt(texte));
                            int req= stmt.executeUpdate("UPDATE cahier SET qte ="+nouvVal.qte+" WHERE id = '"+nouvVal.id+"'");
                            table.setItems(FXCollections.observableList(listeCahier()));
                        }
                        textIn.clear();
                        stmt.close();
                    }catch(SQLException ex){
                    Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
                    }   
                });
            }
        });
        HBox hbox= new HBox();
        hbox.getChildren().addAll(addId,addMarque,addNature,addNbPages,addPrix,addQte,addBouton,modifier,effacer,supprimer);
        hbox.setPadding(new Insets(10,0,10,0));
        VBox vbox2 = new VBox();
        vbox2.getChildren().addAll(hbox, hbox2);
        return vbox2;
    }
    public void saveBd(){
        FileChooser fc= new FileChooser();
        fc.setTitle("Choisir un fichier");
        fc.getExtensionFilters().addAll(new ExtensionFilter("SQL Files","*.sql"), 
                new ExtensionFilter("All Files", "*."));
        File fichier=fc.showSaveDialog(Library.primaryStage);
        if(fichier!=null){
          String commande1="exit";  
          String commande2= "mysqldump -u '"+user+"' -p --opt baseDonnee > '"+fichier+"'";
          String commande3;
          Runtime r=Runtime.getRuntime();
          Process process=null;
          try{
              process=r.exec(commande1);
              process=r.exec(commande2);
          }
          catch(Exception err){
              Alert alerte= new Alert(AlertType.WARNING);
              alerte.setTitle("Erreur");
              alerte.setHeaderText("Erreur lors de la sauvegarde");
              alerte.setContentText("Impossible de sauvegarder les données");
              alerte.showAndWait();
          }
        }
    }
    public void openBd(){
        FileChooser fc= new FileChooser();
        fc.setTitle("Choisir un fichier");
        fc.getExtensionFilters().addAll(new ExtensionFilter("SQL Files","*.sql"), 
                new ExtensionFilter("All Files", "*."));
        File fichier=fc.showOpenDialog(Library.primaryStage);
        if(fichier!=null){
        try {
            Statement stmt = connexion.createStatement();
            String requete= "USE baseDonnee";
            String requete2="source '"+fichier+"'";
            ResultSet rs=stmt.executeQuery(requete);
            rs=stmt.executeQuery(requete2);
            stmt.close();
        }catch (SQLException ex) {
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
            Alert alerte= new Alert(AlertType.WARNING);
            alerte.setTitle("Erreur");
            alerte.setHeaderText("Erreur lors de la restauration");
            alerte.setContentText("Impossible de restaurer les données");
            alerte.showAndWait();
            }
        }
    }
    public void finalize() throws SQLException{
        connexion.close();
    }
}
