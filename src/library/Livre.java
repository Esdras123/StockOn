/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

/**
 *
 * @author Esdras
 */
public class Livre extends Article {
    private String maisonEdition;
    private String classeConcernee;
    private String matiereConcernee;
    private String auteur;
    private String titre;
    public Livre(){
        maisonEdition="Inconnu";
        classeConcernee="Inconnu";
        matiereConcernee="Inconnu";
        auteur="Inconnu";
        super.id="Inconnu";
        titre="Inconnu";
        super.prixUni=0;
        super.qte=0;
    }
    public Livre(String identite,String nom,String maison, String classe, 
    String matiere, String auteur, Integer prix,Integer quantite){
        maisonEdition=maison;
        classeConcernee=classe;
        matiereConcernee=matiere;
        this.auteur=auteur;
        id=identite;
        this.titre=nom;
        super.prixUni=prix;
        super.qte=quantite;
    }

    public String getMaisonEdition() {
        return maisonEdition;
    }

    public void setMaisonEdition(String maisonEdition) {
        this.maisonEdition = maisonEdition;
    }

    public String getClasseConcernee() {
        return classeConcernee;
    }

    public void setClasseConcernee(String classeConcernee) {
        this.classeConcernee = classeConcernee;
    }

    public String getMatiereConcernee() {
        return matiereConcernee;
    }

    public void setMatiereConcernee(String matiereConcernee) {
        this.matiereConcernee = matiereConcernee;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPrixUni() {
        return prixUni;
    }

    public void setPrixUni(Integer prixUni) {
        this.prixUni = prixUni;
    }

    public Integer getQte() {
        return qte;
    }

    public void setQte(Integer qte) {
        this.qte = qte;
    }

}

