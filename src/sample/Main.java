package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application implements EventHandler<MouseEvent> {

    /** taille d'une case en pixels */
    int tailleCase;
    /**les voitures*/
    List<Voiture> voitures;
    /**les dessins des voitures*/
    public List<DessinVoiture> dessinsVoitures;
    /** scene de jeu */
    Scene scene;
    /**troupe des acteurs*/
    Group troupe;
    /**vitesse de l'animation*/
    long tempo = 1000;


    /** lancement automatique de l'application graphique */
    public void start(Stage primaryStage) {
        tailleCase = 100;
        voitures = new ArrayList<>();
        dessinsVoitures= new ArrayList<>();
        construirePlateauJeu(primaryStage);
    }

    /** construction du th�atre et de la sc�ne */
    void construirePlateauJeu(Stage primaryStage) {
        // definir la scene principale
        troupe = new Group();
        scene = new Scene(troupe, tailleCase + 5 * tailleCase, 2 * tailleCase + 4 * tailleCase, Color.DARKGRAY);
        // definir les acteurs et les habiller
        dessinEnvironnement(troupe);

        primaryStage.setTitle("Trafic Routier Maxime_CONSIGNY");
        primaryStage.setScene(scene);
        // afficher
        primaryStage.show();
        //-----lancer le timer pour faire vivre la matrice
        int[] nbTop = {0};
        Timeline littleCycle = new Timeline(new KeyFrame(Duration.millis(tempo),
                event-> {
                    //Fait apparaitre une nouvelle voiture au hasard
                    if((nbTop[0]%2) == 0){
                        ajoutVoiture(nbTop[0]);
                    }
                    //Fait changer les feux de valeur
                    if((nbTop[0]%2) == 0){
                        gererFeu();
                    }
                    animDeplacement();
                    nbTop[0]++;
                }));
        littleCycle.setCycleCount(Timeline.INDEFINITE);
        littleCycle.play();
    }


    public void ajoutVoiture(int id){
        int decalage = tailleCase / 2;
        int radius = 1 * decalage / 3;
        Random hasard = new Random();
        int max = 4;
        int min = 1;
        int origine = hasard.nextInt(max - min + 1) + min;
        Voiture v;

        if((id%6) == 0){
            v = new Voiture(id, origine,5, origine,0);
        }
        else{
            v = new Voiture(id, 0,origine, 5,origine);
        }


        voitures.add(v);
        System.out.println(v);
        int cx = decalage + v.getX() * tailleCase;
        int cy = decalage + v.getY() * tailleCase;
        DessinVoiture dv = new DessinVoiture(cx, cy, radius);
        troupe.getChildren().add(dv);
        dv.setSmooth(true);
        dv.setOnMouseClicked(this);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setOffsetX(3.0);
        dropShadow.setOffsetY(3.0);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
        dv.setEffect(dropShadow);
        dessinsVoitures.add(dv);
    }

    /**
     * creation des dessins des routes, carrefours et voitures
     */
    void dessinEnvironnement(Group troupe) {
        ReseauRoutier.creerReseau();
        List<Noeud> noeuds = ReseauRoutier.getNoeuds();
        //voitures.add(new Voiture(0, 1,5, 1,0));

        /**

         voitures.add(new Voiture(1, 0,4, 5,4));
         voitures.add(new Voiture(2, 4, 0, 4, 5 ));
         */

        int decalage = tailleCase / 2;
        // creation des carrefours
        int radius = 1 * decalage / 3;
        for (Noeud noeud:noeuds)
        {
            int cx = decalage + noeud.getX() * tailleCase;
            int cy = decalage + noeud.getY() * tailleCase;
            Circle c = new Circle(cx, cy, radius);
            c.setFill(Color.BLACK);
            c.setOpacity(0.2);
            troupe.getChildren().add(c);
            c.setSmooth(true);
        }
        // creation des routes
        for (Noeud noeud:noeuds)
        {
            int ox = decalage + noeud.getX() * tailleCase;
            int oy = decalage + noeud.getY() * tailleCase;
            for (Arc arc:noeud.getArcSortants())
            {
                Noeud dest=arc.getEnd();
                int dx = decalage + dest.getX() * tailleCase;
                int dy = decalage + dest.getY() * tailleCase;
                Line l = new Line(ox, oy, dx, dy);
                l.setStrokeWidth(6);
                l.setStroke(Color.DARKGOLDENROD);
                troupe.getChildren().add(l);
            }
        }
        //dessin des voitures
        for(Voiture v:voitures)
        {
            System.out.println(v);
            int cx = decalage + v.getX() * tailleCase;
            int cy = decalage + v.getY() * tailleCase;
            DessinVoiture dv = new DessinVoiture(cx, cy, radius);
            troupe.getChildren().add(dv);
            dv.setSmooth(true);
            dv.setOnMouseClicked(this);
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(5.0);
            dropShadow.setOffsetX(3.0);
            dropShadow.setOffsetY(3.0);
            dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
            dv.setEffect(dropShadow);
            dessinsVoitures.add(dv);
        }


    }

    public void addDessinsVoiture(DessinVoiture dv){
        this.dessinsVoitures.add(dv);
    }

    /** reponse aux evenements de souris */
    public void handle(MouseEvent me) {

        Object o = me.getSource();
        if (o instanceof DessinVoiture) {
            DessinVoiture dv = (DessinVoiture) o;
            dv.switchSelected();
            Voiture v = voitures.get(dessinsVoitures.indexOf(dv));
            v.setPause(!v.isPause());
        }
    }


    /**realise l'animation pour le deplacement

     * */
    private void animDeplacement()
    {
        int decalage = tailleCase / 2;
        for(int i=0; i<voitures.size(); i++)
        {
            Voiture v = voitures.get(i);
            v.getOrigine().delCar();
            Noeud n = v.prochainNoeud();
            if(!v.isArrivee() && !v.isPause())
            {
                n.addCar(v);
                System.out.println("deplacement de " + v);
                //Noeud n = v.prochainNoeud();
                if(n!=null)
                {
                    DessinVoiture dv = dessinsVoitures.get(i);
                    if(!dv.selected)
                    {
                        Timeline timeline = new Timeline();
                        int xdest = decalage + n.getX() * tailleCase;
                        int ydest = decalage + n.getY() * tailleCase;
                        KeyFrame bougeVoiture = new KeyFrame(new Duration(tempo),
                                new KeyValue(dv.centerXProperty(), xdest),
                                new KeyValue(dv.centerYProperty(), ydest));
                        timeline.getKeyFrames().add(bougeVoiture);
                        timeline.play();
                        dv.setAnimation(timeline);
                    }
                }
            }
            else{
                if(v.isArrivee()){
                    troupe.getChildren().remove(dessinsVoitures.get(i));
                    voitures.remove(i);
                    dessinsVoitures.remove(i);
                }
            }
        }
    }

    public void gererFeu(){
        List<Noeud> noeuds = ReseauRoutier.getNoeuds();

        for(Noeud n:noeuds){
            n.switchFeu();
        }
    }



    public static void main(String[] args) {
        launch(args);
    }

}
