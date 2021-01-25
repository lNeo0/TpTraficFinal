package sample;

import java.awt.Point;

import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class DessinVoiture extends Circle {
    /**vrai si la voiture est actuellement selectionnee*/
    boolean selected;
    /**position de la voiture dans la grille (null si non posé)*/
    private Point position;
    /**ancienne position de la voiture dans la grille (null si non posé)*/
    private Point anciennePosition;
    /**couleur pour voiture*/
    public static Color couleur = Color.RED;
    /**couleur courante de la voiture*/
    Color cj = couleur;
    /**couleur pour voiture  selectionnee*/
    public static Color couleurSelected = Color.GAINSBORO;
    /**animation du deplacement*/
    Timeline animation;


    /**
     *constructeur
     *@param centerX coordonnee X du centre du disque
     *@param centerY coordonnee Y du centre du disque
     *@param radius taille en pixel du rayon du disque
     */
    public DessinVoiture(double centerX, double centerY, double radius) {
        super(centerX, centerY, radius);
        selected = false;
        getCouleur();
        setFill(cj);
    }

    /**active ou desactive la selection */
    public void switchSelected()
    {
        selected = !selected;
        getCouleur();
        colorerVoiture();
        if(selected && animation!=null) animation.pause();
        if(!selected && animation!=null) animation.play();
    }


    public Color getCouleur()
    {
        cj=(selected?couleurSelected:couleur);
        return cj;
    }

    /**remplit le disque avec la couleur courante*/
    public void colorerVoiture()
    {
        setFill(cj);
    }

    /**
     * @return the position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @param _position the position to set
     */
    public void setPosition(Point _position) {
        this.position = _position;
    }


    /**
     * @return the anciennePosition
     */
    public Point getAnciennePosition() {
        return anciennePosition;
    }

    public void moveToPosition(Point _point)
    {
        this.anciennePosition.setLocation(this.position);
        this.position.setLocation(_point);
    }

    /**
     * @param anciennePosition the anciennePosition to set
     */
    public void setAnciennePosition(Point anciennePosition) {
        this.anciennePosition = anciennePosition;
    }

    public String toString()
    {
        return "("+position.x+","+position.y+") avant en "+ "("+anciennePosition.x+","+anciennePosition.y+") ";
    }

    public Timeline getAnimation()
    {
        return animation;
    }

    public void setAnimation(Timeline animation)
    {
        this.animation = animation;
    }
}

