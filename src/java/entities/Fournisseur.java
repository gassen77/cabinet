/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
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
@Table(name = "fournisseur")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Fournisseur.findAll", query = "SELECT f FROM Fournisseur f"),
    @NamedQuery(name = "Fournisseur.findById", query = "SELECT f FROM Fournisseur f WHERE f.id = :id"),
    @NamedQuery(name = "Fournisseur.findByCode", query = "SELECT f FROM Fournisseur f WHERE f.code = :code"),
    @NamedQuery(name = "Fournisseur.findByNom", query = "SELECT f FROM Fournisseur f WHERE f.nom = :nom"),
    @NamedQuery(name = "Fournisseur.findByMatriculefiscale", query = "SELECT f FROM Fournisseur f WHERE f.matriculefiscale = :matriculefiscale"),
    @NamedQuery(name = "Fournisseur.findByAdresse", query = "SELECT f FROM Fournisseur f WHERE f.adresse = :adresse"),
    @NamedQuery(name = "Fournisseur.findByCodepostale", query = "SELECT f FROM Fournisseur f WHERE f.codepostale = :codepostale"),
    @NamedQuery(name = "Fournisseur.findByTel", query = "SELECT f FROM Fournisseur f WHERE f.tel = :tel"),
    @NamedQuery(name = "Fournisseur.findByFax", query = "SELECT f FROM Fournisseur f WHERE f.fax = :fax"),
    @NamedQuery(name = "Fournisseur.findByMail", query = "SELECT f FROM Fournisseur f WHERE f.mail = :mail"),
    @NamedQuery(name = "Fournisseur.findByPor", query = "SELECT f FROM Fournisseur f WHERE f.por = :por"),
    @NamedQuery(name = "Fournisseur.findByDatecreation", query = "SELECT f FROM Fournisseur f WHERE f.datecreation = :datecreation"),
    @NamedQuery(name = "Fournisseur.findByDatecreationfiche", query = "SELECT f FROM Fournisseur f WHERE f.datecreationfiche = :datecreationfiche"),
    @NamedQuery(name = "Fournisseur.findByDernieredatemodif", query = "SELECT f FROM Fournisseur f WHERE f.dernieredatemodif = :dernieredatemodif"),
    @NamedQuery(name = "Fournisseur.findByPhoto", query = "SELECT f FROM Fournisseur f WHERE f.photo = :photo"),
    @NamedQuery(name = "Fournisseur.findByActif", query = "SELECT f FROM Fournisseur f WHERE f.actif = :actif")})
public class Fournisseur implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Size(max = 30)
    @Column(name = "code")
    private String code;
    @Size(max = 50)
    @Column(name = "nom")
    private String nom;
    @Size(max = 50)
    @Column(name = "matriculefiscale")
    private String matriculefiscale;
    @Size(max = 150)
    @Column(name = "adresse")
    private String adresse;
    @Size(max = 10)
    @Column(name = "codepostale")
    private String codepostale;
    @Size(max = 50)
    @Column(name = "tel")
    private String tel;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 50)
    @Column(name = "fax")
    private String fax;
    @Size(max = 50)
    @Column(name = "mail")
    private String mail;
    @Size(max = 50)
    @Column(name = "por")
    private String por;
    @Column(name = "datecreation")
    @Temporal(TemporalType.DATE)
    private Date datecreation;
    @Column(name = "datecreationfiche")
    @Temporal(TemporalType.DATE)
    private Date datecreationfiche;
    @Column(name = "dernieredatemodif")
    @Temporal(TemporalType.DATE)
    private Date dernieredatemodif;
    @Size(max = 250)
    @Column(name = "photo")
    private String photo;
    @Column(name = "actif")
    private Boolean actif;
    @OneToMany(mappedBy = "fournisseur")
    private List<Produit> produitList;
    @JoinColumn(name = "ville", referencedColumnName = "id")
    @ManyToOne
    private Ville ville;
    @JoinColumn(name = "nationalite", referencedColumnName = "id")
    @ManyToOne
    private Nationalite nationalite;

    public Fournisseur() {
    }

    public Fournisseur(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getMatriculefiscale() {
        return matriculefiscale;
    }

    public void setMatriculefiscale(String matriculefiscale) {
        this.matriculefiscale = matriculefiscale;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCodepostale() {
        return codepostale;
    }

    public void setCodepostale(String codepostale) {
        this.codepostale = codepostale;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPor() {
        return por;
    }

    public void setPor(String por) {
        this.por = por;
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public Date getDatecreationfiche() {
        return datecreationfiche;
    }

    public void setDatecreationfiche(Date datecreationfiche) {
        this.datecreationfiche = datecreationfiche;
    }

    public Date getDernieredatemodif() {
        return dernieredatemodif;
    }

    public void setDernieredatemodif(Date dernieredatemodif) {
        this.dernieredatemodif = dernieredatemodif;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    @XmlTransient
    public List<Produit> getProduitList() {
        return produitList;
    }

    public void setProduitList(List<Produit> produitList) {
        this.produitList = produitList;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public Nationalite getNationalite() {
        return nationalite;
    }

    public void setNationalite(Nationalite nationalite) {
        this.nationalite = nationalite;
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
        if (!(object instanceof Fournisseur)) {
            return false;
        }
        Fournisseur other = (Fournisseur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Fournisseur[ id=" + id + " ]";
    }
    
}
