/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gassen
 */
@Entity
@Table(name = "detail_convention")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetailConvention.findAll", query = "SELECT d FROM DetailConvention d"),
    @NamedQuery(name = "DetailConvention.findById", query = "SELECT d FROM DetailConvention d WHERE d.id = :id"),
    @NamedQuery(name = "DetailConvention.findByClasseProduit", query = "SELECT d FROM DetailConvention d WHERE d.classeProduit = :classeProduit"),
    @NamedQuery(name = "DetailConvention.findByRemise", query = "SELECT d FROM DetailConvention d WHERE d.remise = :remise"),
    @NamedQuery(name = "DetailConvention.findByPrixHt", query = "SELECT d FROM DetailConvention d WHERE d.prixHt = :prixHt"),
    @NamedQuery(name = "DetailConvention.findByPrixTtc", query = "SELECT d FROM DetailConvention d WHERE d.prixTtc = :prixTtc"),
    @NamedQuery(name = "DetailConvention.findByPrixMinHt", query = "SELECT d FROM DetailConvention d WHERE d.prixMinHt = :prixMinHt"),
    @NamedQuery(name = "DetailConvention.findByPrixMinTtc", query = "SELECT d FROM DetailConvention d WHERE d.prixMinTtc = :prixMinTtc")})
public class DetailConvention implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Column(name = "classe_produit")
    private Long classeProduit;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "remise")
    private BigDecimal remise;
    @Column(name = "prix_ht")
    private BigDecimal prixHt;
    @Column(name = "prix_ttc")
    private BigDecimal prixTtc;
    @Column(name = "prix_min_ht")
    private BigDecimal prixMinHt;
    @Column(name = "prix_min_ttc")
    private BigDecimal prixMinTtc;
    @JoinColumn(name = "classe_client", referencedColumnName = "id")
    @ManyToOne
    private ClasseClient classeClient;
    @JoinColumn(name = "convention", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Convention convention;

    public DetailConvention() {
    }

    public DetailConvention(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClasseProduit() {
        return classeProduit;
    }

    public void setClasseProduit(Long classeProduit) {
        this.classeProduit = classeProduit;
    }

    public BigDecimal getRemise() {
        return remise;
    }

    public void setRemise(BigDecimal remise) {
        this.remise = remise;
    }

    public BigDecimal getPrixHt() {
        return prixHt;
    }

    public void setPrixHt(BigDecimal prixHt) {
        this.prixHt = prixHt;
    }

    public BigDecimal getPrixTtc() {
        return prixTtc;
    }

    public void setPrixTtc(BigDecimal prixTtc) {
        this.prixTtc = prixTtc;
    }

    public BigDecimal getPrixMinHt() {
        return prixMinHt;
    }

    public void setPrixMinHt(BigDecimal prixMinHt) {
        this.prixMinHt = prixMinHt;
    }

    public BigDecimal getPrixMinTtc() {
        return prixMinTtc;
    }

    public void setPrixMinTtc(BigDecimal prixMinTtc) {
        this.prixMinTtc = prixMinTtc;
    }

    public ClasseClient getClasseClient() {
        return classeClient;
    }

    public void setClasseClient(ClasseClient classeClient) {
        this.classeClient = classeClient;
    }

    public Convention getConvention() {
        return convention;
    }

    public void setConvention(Convention convention) {
        this.convention = convention;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailConvention)) {
            return false;
        }
        DetailConvention other = (DetailConvention) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DetailConvention[ id=" + id + " ]";
    }
    
}
