/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gassen
 */
@Entity
@Table(name = "laboratoire")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Laboratoire.findAll", query = "SELECT l FROM Laboratoire l"),
    @NamedQuery(name = "Laboratoire.findById", query = "SELECT l FROM Laboratoire l WHERE l.id = :id"),
    @NamedQuery(name = "Laboratoire.findByLibelle", query = "SELECT l FROM Laboratoire l WHERE l.libelle = :libelle"),
    @NamedQuery(name = "Laboratoire.findByAdresse", query = "SELECT l FROM Laboratoire l WHERE l.adresse = :adresse"),
    @NamedQuery(name = "Laboratoire.findByTel", query = "SELECT l FROM Laboratoire l WHERE l.tel = :tel"),
    @NamedQuery(name = "Laboratoire.findByPor", query = "SELECT l FROM Laboratoire l WHERE l.por = :por"),
    @NamedQuery(name = "Laboratoire.findByMail", query = "SELECT l FROM Laboratoire l WHERE l.mail = :mail")})
public class Laboratoire implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 254)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 150)
    @Column(name = "adresse")
    private String adresse;
    @Size(max = 50)
    @Column(name = "tel")
    private String tel;
    @Size(max = 50)
    @Column(name = "por")
    private String por;
    @Size(max = 50)
    @Column(name = "mail")
    private String mail;
    @OneToMany(mappedBy = "laboratoire")
    private List<Client> clientList;

    public Laboratoire() {
    }

    public Laboratoire(Long id) {
        this.id = id;
    }

    public Laboratoire(Long id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPor() {
        return por;
    }

    public void setPor(String por) {
        this.por = por;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @XmlTransient
    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
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
        if (!(object instanceof Laboratoire)) {
            return false;
        }
        Laboratoire other = (Laboratoire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Laboratoire[ id=" + id + " ]";
    }
    
}
