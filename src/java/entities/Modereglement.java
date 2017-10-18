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
@Table(name = "modereglement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Modereglement.findAll", query = "SELECT m FROM Modereglement m"),
    @NamedQuery(name = "Modereglement.findById", query = "SELECT m FROM Modereglement m WHERE m.id = :id"),
    @NamedQuery(name = "Modereglement.findByCode", query = "SELECT m FROM Modereglement m WHERE m.code = :code"),
    @NamedQuery(name = "Modereglement.findByLibelle", query = "SELECT m FROM Modereglement m WHERE m.libelle = :libelle")})
public class Modereglement implements Serializable {
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
    @Column(name = "libelle")
    private String libelle;
    @OneToMany(mappedBy = "modereglement")
    private List<Operation> operationList;

    public Modereglement() {
    }

    public Modereglement(Long id) {
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
    public List<Operation> getOperationList() {
        return operationList;
    }

    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
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
        if (!(object instanceof Modereglement)) {
            return false;
        }
        Modereglement other = (Modereglement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Modereglement[ id=" + id + " ]";
    }
    
}
