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
@Table(name = "imagesclient")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Imagesclient.findAll", query = "SELECT i FROM Imagesclient i"),
    @NamedQuery(name = "Imagesclient.findById", query = "SELECT i FROM Imagesclient i WHERE i.id = :id"),
    @NamedQuery(name = "Imagesclient.findByDatecreation", query = "SELECT i FROM Imagesclient i WHERE i.datecreation = :datecreation"),
    @NamedQuery(name = "Imagesclient.findByPhoto", query = "SELECT i FROM Imagesclient i WHERE i.photo = :photo"),
    @NamedQuery(name = "Imagesclient.findByDescription", query = "SELECT i FROM Imagesclient i WHERE i.description = :description"),
    @NamedQuery(name = "Imagesclient.findByCommentaire", query = "SELECT i FROM Imagesclient i WHERE i.commentaire = :commentaire")})
public class Imagesclient implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Column(name = "datecreation")
    @Temporal(TemporalType.DATE)
    private Date datecreation;
    @Size(max = 250)
    @Column(name = "photo")
    private String photo;
    @Size(max = 250)
    @Column(name = "description")
    private String description;
    @Size(max = 250)
    @Column(name = "commentaire")
    private String commentaire;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne
    private Client client;

    public Imagesclient() {
    }

    public Imagesclient(Long id) {
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
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
        if (!(object instanceof Imagesclient)) {
            return false;
        }
        Imagesclient other = (Imagesclient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Imagesclient[ id=" + id + " ]";
    }
    
}
