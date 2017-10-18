/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gassen
 */
@Entity
@Table(name = "produit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Produit.findAll", query = "SELECT p FROM Produit p"),
    @NamedQuery(name = "Produit.findById", query = "SELECT p FROM Produit p WHERE p.id = :id"),
    @NamedQuery(name = "Produit.findByCodeproduit", query = "SELECT p FROM Produit p WHERE p.codeproduit = :codeproduit"),
    @NamedQuery(name = "Produit.findByLibelleproduit", query = "SELECT p FROM Produit p WHERE p.libelleproduit = :libelleproduit"),
    @NamedQuery(name = "Produit.findByReference", query = "SELECT p FROM Produit p WHERE p.reference = :reference"),
    @NamedQuery(name = "Produit.findByQteenstock", query = "SELECT p FROM Produit p WHERE p.qteenstock = :qteenstock"),
    @NamedQuery(name = "Produit.findByDatecreation", query = "SELECT p FROM Produit p WHERE p.datecreation = :datecreation"),
    @NamedQuery(name = "Produit.findByDatemodification", query = "SELECT p FROM Produit p WHERE p.datemodification = :datemodification"),
    @NamedQuery(name = "Produit.findByImage", query = "SELECT p FROM Produit p WHERE p.image = :image"),
    @NamedQuery(name = "Produit.findByCommentaire", query = "SELECT p FROM Produit p WHERE p.commentaire = :commentaire"),
    @NamedQuery(name = "Produit.findByPrixachat", query = "SELECT p FROM Produit p WHERE p.prixachat = :prixachat")})
public class Produit implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Size(max = 30)
    @Column(name = "codeproduit")
    private String codeproduit;
    @Size(max = 70)
    @Column(name = "libelleproduit")
    private String libelleproduit;
    @Size(max = 30)
    @Column(name = "reference")
    private String reference;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qteenstock")
    private BigDecimal qteenstock;
    @Column(name = "datecreation")
    @Temporal(TemporalType.DATE)
    private Date datecreation;
    @Column(name = "datemodification")
    @Temporal(TemporalType.DATE)
    private Date datemodification;
    @Size(max = 1000)
    @Column(name = "image")
    private String image;
    @Size(max = 1000)
    @Column(name = "commentaire")
    private String commentaire;
    @Column(name = "prixachat")
    private BigDecimal prixachat;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "produit1")
    private List<DetailDepot> detailDepotList;
    @JoinColumn(name = "unite", referencedColumnName = "id")
    @ManyToOne
    private Unite unite;
    @JoinColumn(name = "fournisseur", referencedColumnName = "id")
    @ManyToOne
    private Fournisseur fournisseur;
    @JoinColumn(name = "famille", referencedColumnName = "id")
    @ManyToOne
    private Famille famille;
    @JoinColumn(name = "categorieproduit", referencedColumnName = "id")
    @ManyToOne
    private Categorieproduit categorieproduit;
    @OneToMany(mappedBy = "idproduit")
    private List<Detailoperation> detailoperationList;

    public Produit() {
    }

    public Produit(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeproduit() {
        return codeproduit;
    }

    public void setCodeproduit(String codeproduit) {
        this.codeproduit = codeproduit;
    }

    public String getLibelleproduit() {
        return libelleproduit;
    }

    public void setLibelleproduit(String libelleproduit) {
        this.libelleproduit = libelleproduit;
    }

 

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getQteenstock() {
        return qteenstock;
    }

    public void setQteenstock(BigDecimal qteenstock) {
        this.qteenstock = qteenstock;
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public Date getDatemodification() {
        return datemodification;
    }

    public void setDatemodification(Date datemodification) {
        this.datemodification = datemodification;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public BigDecimal getPrixachat() {
        return prixachat;
    }

    public void setPrixachat(BigDecimal prixachat) {
        this.prixachat = prixachat;
    }

    @XmlTransient
    public List<DetailDepot> getDetailDepotList() {
        return detailDepotList;
    }

    public void setDetailDepotList(List<DetailDepot> detailDepotList) {
        this.detailDepotList = detailDepotList;
    }

    public Unite getUnite() {
        return unite;
    }

    public void setUnite(Unite unite) {
        this.unite = unite;
    }

    public Fournisseur getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public Famille getFamille() {
        return famille;
    }

    public void setFamille(Famille famille) {
        this.famille = famille;
    }

    public Categorieproduit getCategorieproduit() {
        return categorieproduit;
    }

    public void setCategorieproduit(Categorieproduit categorieproduit) {
        this.categorieproduit = categorieproduit;
    }

    @XmlTransient
    public List<Detailoperation> getDetailoperationList() {
        return detailoperationList;
    }

    public void setDetailoperationList(List<Detailoperation> detailoperationList) {
        this.detailoperationList = detailoperationList;
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
        if (!(object instanceof Produit)) {
            return false;
        }
        Produit other = (Produit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Produit[ id=" + id + " ]";
    }
    
}
