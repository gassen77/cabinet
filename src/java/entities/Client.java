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
@Table(name = "client")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Client.findAll", query = "SELECT c FROM Client c"),
    @NamedQuery(name = "Client.findById", query = "SELECT c FROM Client c WHERE c.id = :id"),
    @NamedQuery(name = "Client.findByCode", query = "SELECT c FROM Client c WHERE c.code = :code"),
    @NamedQuery(name = "Client.findByNom", query = "SELECT c FROM Client c WHERE c.nom = :nom"),
    @NamedQuery(name = "Client.findByCin", query = "SELECT c FROM Client c WHERE c.cin = :cin"),
    @NamedQuery(name = "Client.findByNumerofiche", query = "SELECT c FROM Client c WHERE c.numerofiche = :numerofiche"),
    @NamedQuery(name = "Client.findByAdresse", query = "SELECT c FROM Client c WHERE c.adresse = :adresse"),
    @NamedQuery(name = "Client.findByCodepostale", query = "SELECT c FROM Client c WHERE c.codepostale = :codepostale"),
    @NamedQuery(name = "Client.findByTel", query = "SELECT c FROM Client c WHERE c.tel = :tel"),
    @NamedQuery(name = "Client.findByFax", query = "SELECT c FROM Client c WHERE c.fax = :fax"),
    @NamedQuery(name = "Client.findByMail", query = "SELECT c FROM Client c WHERE c.mail = :mail"),
    @NamedQuery(name = "Client.findByPor", query = "SELECT c FROM Client c WHERE c.por = :por"),
    @NamedQuery(name = "Client.findByDatenaissance", query = "SELECT c FROM Client c WHERE c.datenaissance = :datenaissance"),
    @NamedQuery(name = "Client.findByDatecreation", query = "SELECT c FROM Client c WHERE c.datecreation = :datecreation"),
    @NamedQuery(name = "Client.findByDernieredatemodif", query = "SELECT c FROM Client c WHERE c.dernieredatemodif = :dernieredatemodif"),
    @NamedQuery(name = "Client.findByPhoto", query = "SELECT c FROM Client c WHERE c.photo = :photo"),
    @NamedQuery(name = "Client.findByPlafond", query = "SELECT c FROM Client c WHERE c.plafond = :plafond"),
    @NamedQuery(name = "Client.findByActif", query = "SELECT c FROM Client c WHERE c.actif = :actif")})
public class Client implements Serializable {
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
    @Column(name = "cin")
    private String cin;
    @Column(name = "numerofiche")
    private Long numerofiche;
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
    @Column(name = "datenaissance")
    @Temporal(TemporalType.DATE)
    private Date datenaissance;
    @Column(name = "datecreation")
    @Temporal(TemporalType.DATE)
    private Date datecreation;
    @Column(name = "dernieredatemodif")
    @Temporal(TemporalType.DATE)
    private Date dernieredatemodif;
    @Size(max = 250)
    @Column(name = "photo")
    private String photo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "plafond")
    private BigDecimal plafond;
    @Column(name = "actif")
    private Boolean actif;
    @JoinColumn(name = "ville", referencedColumnName = "id")
    @ManyToOne
    private Ville ville;
    @JoinColumn(name = "sexe", referencedColumnName = "id")
    @ManyToOne
    private Sexe sexe;
    @JoinColumn(name = "profession", referencedColumnName = "id")
    @ManyToOne
    private Profession profession;
    @JoinColumn(name = "nationalite", referencedColumnName = "id")
    @ManyToOne
    private Nationalite nationalite;
    @JoinColumn(name = "laboratoire", referencedColumnName = "id")
    @ManyToOne
    private Laboratoire laboratoire;
    @JoinColumn(name = "typeclient", referencedColumnName = "id")
    @ManyToOne
    private Typeclient typeclient;
    @JoinColumn(name = "classe_client", referencedColumnName = "id")
    @ManyToOne
    private ClasseClient classeClient;
    @OneToMany(mappedBy = "client")
    private List<Operation> operationList;
    @OneToMany(mappedBy = "client")
    private List<Imagesclient> imagesclientList;
    @OneToMany(mappedBy = "client")
    private List<DetailAntecedant> detailAntecedantList;
    @OneToMany(mappedBy = "client")
    private List<Rendezvous> rendezvousList;

    public Client() {
    }

    public Client(Long id) {
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

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public Long getNumerofiche() {
        return numerofiche;
    }

    public void setNumerofiche(Long numerofiche) {
        this.numerofiche = numerofiche;
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

    public Date getDatenaissance() {
        return datenaissance;
    }

    public void setDatenaissance(Date datenaissance) {
        this.datenaissance = datenaissance;
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
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

    public BigDecimal getPlafond() {
        return plafond;
    }

    public void setPlafond(BigDecimal plafond) {
        this.plafond = plafond;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Nationalite getNationalite() {
        return nationalite;
    }

    public void setNationalite(Nationalite nationalite) {
        this.nationalite = nationalite;
    }

    public Laboratoire getLaboratoire() {
        return laboratoire;
    }

    public void setLaboratoire(Laboratoire laboratoire) {
        this.laboratoire = laboratoire;
    }

    public Typeclient getTypeclient() {
        return typeclient;
    }

    public void setTypeclient(Typeclient typeclient) {
        this.typeclient = typeclient;
    }

    public ClasseClient getClasseClient() {
        return classeClient;
    }

    public void setClasseClient(ClasseClient classeClient) {
        this.classeClient = classeClient;
    }

    @XmlTransient
    public List<Operation> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
    }

    @XmlTransient
    public List<Imagesclient> getImagesclientList() {
        return imagesclientList;
    }

    public void setImagesclientList(List<Imagesclient> imagesclientList) {
        this.imagesclientList = imagesclientList;
    }

    @XmlTransient
    public List<DetailAntecedant> getDetailAntecedantList() {
        return detailAntecedantList;
    }

    public void setDetailAntecedantList(List<DetailAntecedant> detailAntecedantList) {
        this.detailAntecedantList = detailAntecedantList;
    }

    @XmlTransient
    public List<Rendezvous> getRendezvousList() {
        return rendezvousList;
    }

    public void setRendezvousList(List<Rendezvous> rendezvousList) {
        this.rendezvousList = rendezvousList;
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
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Client[ id=" + id + " ]";
    }
    
}
