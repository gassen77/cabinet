/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gassen
 */
@Entity
@Table(name = "rendezvous")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rendezvous.findAll", query = "SELECT r FROM Rendezvous r"),
    @NamedQuery(name = "Rendezvous.findById", query = "SELECT r FROM Rendezvous r WHERE r.id = :id"),
    @NamedQuery(name = "Rendezvous.findByDatecreation", query = "SELECT r FROM Rendezvous r WHERE r.datecreation = :datecreation"),
    @NamedQuery(name = "Rendezvous.findByDaterendezvous", query = "SELECT r FROM Rendezvous r WHERE r.daterendezvous = :daterendezvous"),
    @NamedQuery(name = "Rendezvous.findByHeurerendezvous", query = "SELECT r FROM Rendezvous r WHERE r.heurerendezvous = :heurerendezvous"),
    @NamedQuery(name = "Rendezvous.findByPhoto", query = "SELECT r FROM Rendezvous r WHERE r.photo = :photo"),
    @NamedQuery(name = "Rendezvous.findByRaison", query = "SELECT r FROM Rendezvous r WHERE r.raison = :raison")})
public class Rendezvous implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Column(name = "datecreation")
    @Temporal(TemporalType.DATE)
    private Date datecreation;
    @Column(name = "daterendezvous")
    @Temporal(TemporalType.DATE)
    private Date daterendezvous;
    @Column(name = "heurerendezvous")
    @Temporal(TemporalType.DATE)
    private Date heurerendezvous;
    @Size(max = 250)
    @Column(name = "photo")
    private String photo;
    @Size(max = 250)
    @Column(name = "raison")
    private String raison;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne
    private Client client;

    public Rendezvous() {
    }

    public Rendezvous(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public Date getDaterendezvous() {
        return daterendezvous;
    }

    public void setDaterendezvous(Date daterendezvous) {
        this.daterendezvous = daterendezvous;
    }

    public Date getHeurerendezvous() {
        return heurerendezvous;
    }

    public void setHeurerendezvous(Date heurerendezvous) {
        this.heurerendezvous = heurerendezvous;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRaison() {
        return raison;
    }

    public void setRaison(String raison) {
        this.raison = raison;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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
        if (!(object instanceof Rendezvous)) {
            return false;
        }
        Rendezvous other = (Rendezvous) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Rendezvous[ id=" + id + " ]";
    }
    
}
