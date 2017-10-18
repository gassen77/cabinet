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
@Table(name = "typeantecedant")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Typeantecedant.findAll", query = "SELECT t FROM Typeantecedant t"),
    @NamedQuery(name = "Typeantecedant.findById", query = "SELECT t FROM Typeantecedant t WHERE t.id = :id"),
    @NamedQuery(name = "Typeantecedant.findByCode", query = "SELECT t FROM Typeantecedant t WHERE t.code = :code"),
    @NamedQuery(name = "Typeantecedant.findByLibelle", query = "SELECT t FROM Typeantecedant t WHERE t.libelle = :libelle")})
public class Typeantecedant implements Serializable {
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
    @OneToMany(mappedBy = "typeantecedant")
    private List<Antecedant> antecedantList;

    public Typeantecedant() {
    }

    public Typeantecedant(Long id) {
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
    public List<Antecedant> getAntecedantList() {
        return antecedantList;
    }

    public void setAntecedantList(List<Antecedant> antecedantList) {
        this.antecedantList = antecedantList;
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
        if (!(object instanceof Typeantecedant)) {
            return false;
        }
        Typeantecedant other = (Typeantecedant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Typeantecedant[ id=" + id + " ]";
    }
    
}
