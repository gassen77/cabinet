/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gassen
 */
@Entity
@Table(name = "detail_depot")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetailDepot.findAll", query = "SELECT d FROM DetailDepot d"),
    @NamedQuery(name = "DetailDepot.findByDepot", query = "SELECT d FROM DetailDepot d WHERE d.detailDepotPK.depot = :depot"),
    @NamedQuery(name = "DetailDepot.findByProduit", query = "SELECT d FROM DetailDepot d WHERE d.detailDepotPK.produit = :produit"),
    @NamedQuery(name = "DetailDepot.findByQte", query = "SELECT d FROM DetailDepot d WHERE d.qte = :qte")})
public class DetailDepot implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetailDepotPK detailDepotPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qte")
    private BigDecimal qte;
    @JoinColumn(name = "produit", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Produit produit1;
    @JoinColumn(name = "depot", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Depot depot1;

    public DetailDepot() {
    }

    public DetailDepot(DetailDepotPK detailDepotPK) {
        this.detailDepotPK = detailDepotPK;
    }

    public DetailDepot(long depot, long produit) {
        this.detailDepotPK = new DetailDepotPK(depot, produit);
    }

    public DetailDepotPK getDetailDepotPK() {
        return detailDepotPK;
    }

    public void setDetailDepotPK(DetailDepotPK detailDepotPK) {
        this.detailDepotPK = detailDepotPK;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public Produit getProduit1() {
        return produit1;
    }

    public void setProduit1(Produit produit1) {
        this.produit1 = produit1;
    }

    public Depot getDepot1() {
        return depot1;
    }

    public void setDepot1(Depot depot1) {
        this.depot1 = depot1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detailDepotPK != null ? detailDepotPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailDepot)) {
            return false;
        }
        DetailDepot other = (DetailDepot) object;
        if ((this.detailDepotPK == null && other.detailDepotPK != null) || (this.detailDepotPK != null && !this.detailDepotPK.equals(other.detailDepotPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DetailDepot[ detailDepotPK=" + detailDepotPK + " ]";
    }
    
}
