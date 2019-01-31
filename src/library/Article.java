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
public class Article {
    protected String id;
    protected Integer prixUni;
    protected Integer qte;
    public Article(){
        id="INCONNU";
        prixUni=0;
        qte=0;
    }
    public Article(String id,Integer prix, Integer qte){
        this.id=id;
        this.prixUni=prix;
        this.qte=qte;
    }
    protected Integer addQte(Integer nb){
        Integer tot=qte+nb;
        if(nb>=0)
            return tot;
        else{
            if(tot>=0)
                return tot;
            else
                return -1;
        }         
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
