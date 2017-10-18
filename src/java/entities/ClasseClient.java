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
@Table(name = "classe_client")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClasseClient.findAll", query = "SELECT c FROM ClasseClient c"),
    @NamedQuery(name = "ClasseClient.findById", query = "SELECT c FROM ClasseClient c WHERE c.id = :id"),
    @NamedQuery(name = "ClasseClient.findByCode", query = "SELECT c FROM ClasseClient c WHERE c.code = :code"),
    @NamedQuery(name = "ClasseClient.findByLibelle", query = "SELECT c FROM ClasseClient c WHERE c.libelle = :libelle")})
public class ClasseClient implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Size(max = 100)
    @Column(name = "libelle")
    private String libelle;
    @OneToMany(mappedBy = "classeClient")
    private List<Client> clientList;
    @OneToMany(mappedBy = "classeClient")
    private List<DetailConvention> detailConventionList;

    public ClasseClient() {
    }

    public ClasseClient(Long id) {
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

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @XmlTransient
    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    @XmlTransient
    public List<DetailConvention> getDetailConventionList() {
        return detailConventionList;
    }

    public void setDetailConventionList(List<DetailConvention> detailConventionList) {
        this.detailConventionList = detailConventionList;
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
        if (!(object instanceof ClasseClient)) {
            return false;
        }
        ClasseClient other = (ClasseClient) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ClasseClient[ id=" + id + " ]";
    }
    
}
