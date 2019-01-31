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
public class Cahier extends Article{
    private String marque;
    private String nature;
    private Integer nbPages;
    public Cahier(){
        marque="Inconnu";
        nature="NON";
        nbPages=0;
        super.qte=0;
        super.prixUni=0;
        super.id="Inconnu";
    }
    public Cahier(String identite,String marq,String nat, Integer nb, Integer prix,Integer qte){
        marque=marq.toUpperCase();
        nature=nat.toUpperCase();
        nbPages=nb;
        super.qte=qte;
        super.prixUni=prix;
        super.id=identite;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public Integer getNbPages() {
        return nbPages;
    }

    public void setNbPages(Integer nbPages) {
        this.nbPages = nbPages;
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
