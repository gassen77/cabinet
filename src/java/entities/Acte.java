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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "acte")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Acte.findAll", query = "SELECT a FROM Acte a"),
    @NamedQuery(name = "Acte.findById", query = "SELECT a FROM Acte a WHERE a.id = :id"),
    @NamedQuery(name = "Acte.findByCodeacte", query = "SELECT a FROM Acte a WHERE a.codeacte = :codeacte"),
    @NamedQuery(name = "Acte.findByLibelleacte", query = "SELECT a FROM Acte a WHERE a.libelleacte = :libelleacte")})
public class Acte implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Size(max = 30)
    @Column(name = "codeacte")
    private String codeacte;
    @Size(max = 200)
    @Column(name = "libelleacte")
    private String libelleacte;
    @OneToMany(mappedBy = "idacte")
    private List<Detailoperation> detailoperationList;
    @JoinColumn(name = "idfamilleacte", referencedColumnName = "id")
    @ManyToOne
    private Familleacte idfamilleacte;

    public Acte() {
    }

    public Acte(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeacte() {
        return codeacte;
    }

    public void setCodeacte(String codeacte) {
        this.codeacte = codeacte;
    }

    public String getLibelleacte() {
        return libelleacte;
    }

    public void setLibelleacte(String libelleacte) {
        this.libelleacte = libelleacte;
    }


    @XmlTransient
    public List<Detailoperation> getDetailoperationList() {
        return detailoperationList;
    }

    public void setDetailoperationList(List<Detailoperation> detailoperationList) {
        this.detailoperationList = detailoperationList;
    }

    public Familleacte getIdfamilleacte() {
        return idfamilleacte;
    }

    public void setIdfamilleacte(Familleacte idfamilleacte) {
        this.idfamilleacte = idfamilleacte;
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
        if (!(object instanceof Acte)) {
            return false;
        }
        Acte other = (Acte) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Acte[ id=" + id + " ]";
    }
    
}
