/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author gassen
 */
@Embeddable
public class DetailDepotPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "depot")
    private long depot;
    @Basic(optional = false)
    @NotNull
    @Column(name = "produit")
    private long produit;

    public DetailDepotPK() {
    }

    public DetailDepotPK(long depot, long produit) {
        this.depot = depot;
        this.produit = produit;
    }

    public long getDepot() {
        return depot;
    }

    public void setDepot(long depot) {
        this.depot = depot;
    }

    public long getProduit() {
        return produit;
    }

    public void setProduit(long produit) {
        this.produit = produit;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) depot;
        hash += (int) produit;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailDepotPK)) {
            return false;
        }
        DetailDepotPK other = (DetailDepotPK) object;
        if (this.depot != other.depot) {
            return false;
        }
        if (this.produit != other.produit) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DetailDepotPK[ depot=" + depot + ", produit=" + produit + " ]";
    }
    
}
