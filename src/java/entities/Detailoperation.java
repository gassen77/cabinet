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
@Table(name = "detailoperation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detailoperation.findAll", query = "SELECT d FROM Detailoperation d"),
    @NamedQuery(name = "Detailoperation.findByIdoperation", query = "SELECT d FROM Detailoperation d WHERE d.detailoperationPK.idoperation = :idoperation"),
    @NamedQuery(name = "Detailoperation.findByQte", query = "SELECT d FROM Detailoperation d WHERE d.qte = :qte"),
    @NamedQuery(name = "Detailoperation.findByPrixunitaire", query = "SELECT d FROM Detailoperation d WHERE d.prixunitaire = :prixunitaire"),
    @NamedQuery(name = "Detailoperation.findByPrixtotal", query = "SELECT d FROM Detailoperation d WHERE d.prixtotal = :prixtotal"),
    @NamedQuery(name = "Detailoperation.findByOrdre", query = "SELECT d FROM Detailoperation d WHERE d.detailoperationPK.ordre = :ordre"),
    @NamedQuery(name = "Detailoperation.findByTarif", query = "SELECT d FROM Detailoperation d WHERE d.tarif = :tarif")})
public class Detailoperation implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetailoperationPK detailoperationPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qte")
    private BigDecimal qte;
    @Column(name = "prixunitaire")
    private BigDecimal prixunitaire;
    @Column(name = "prixtotal")
    private BigDecimal prixtotal;
    @Column(name = "tarif")
    private BigDecimal tarif;
    @JoinColumn(name = "iddent", referencedColumnName = "id")
    @ManyToOne
    private Dent iddent;
    @JoinColumn(name = "idacte", referencedColumnName = "id")
    @ManyToOne
    private Acte idacte;
     @JoinColumn(name = "idfamilleacte", referencedColumnName = "id")
    @ManyToOne
    private Familleacte idfamilleacte;
    @JoinColumn(name = "idproduit", referencedColumnName = "id")
    @ManyToOne
    private Produit idproduit;
    @JoinColumn(name = "idoperation", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Operation operation;

    public Detailoperation() {
    }

    public Detailoperation(DetailoperationPK detailoperationPK) {
        this.detailoperationPK = detailoperationPK;
    }

    public Detailoperation(long idoperation, int ordre) {
        this.detailoperationPK = new DetailoperationPK(idoperation, ordre);
    }

    public DetailoperationPK getDetailoperationPK() {
        return detailoperationPK;
    }

    public void setDetailoperationPK(DetailoperationPK detailoperationPK) {
        this.detailoperationPK = detailoperationPK;
    }

    public BigDecimal getQte() {
        return qte;
    }

    public void setQte(BigDecimal qte) {
        this.qte = qte;
    }

    public BigDecimal getPrixunitaire() {
        return prixunitaire;
    }

    public void setPrixunitaire(BigDecimal prixunitaire) {
        this.prixunitaire = prixunitaire;
    }

    public BigDecimal getPrixtotal() {
        return prixtotal;
    }

    public void setPrixtotal(BigDecimal prixtotal) {
        this.prixtotal = prixtotal;
    }

    public BigDecimal getTarif() {
        return tarif;
    }

    public void setTarif(BigDecimal tarif) {
        this.tarif = tarif;
    }

    public Dent getIddent() {
        return iddent;
    }

    public void setIddent(Dent iddent) {
        this.iddent = iddent;
    }

    public Familleacte getIdfamilleacte() {
        return idfamilleacte;
    }

    public void setIdfamilleacte(Familleacte idfamilleacte) {
        this.idfamilleacte = idfamilleacte;
    }

    public Acte getIdacte() {
        return idacte;
    }

    public void setIdacte(Acte idacte) {
        this.idacte = idacte;
    }

    public Produit getIdproduit() {
        return idproduit;
    }

    public void setIdproduit(Produit idproduit) {
        this.idproduit = idproduit;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detailoperationPK != null ? detailoperationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detailoperation)) {
            return false;
        }
        Detailoperation other = (Detailoperation) object;
        if ((this.detailoperationPK == null && other.detailoperationPK != null) || (this.detailoperationPK != null && !this.detailoperationPK.equals(other.detailoperationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Detailoperation[ detailoperationPK=" + detailoperationPK + " ]";
    }
    
}
