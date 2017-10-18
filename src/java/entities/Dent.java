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
@Table(name = "dent")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dent.findAll", query = "SELECT d FROM Dent d"),
    @NamedQuery(name = "Dent.findById", query = "SELECT d FROM Dent d WHERE d.id = :id"),
    @NamedQuery(name = "Dent.findByCodedent", query = "SELECT d FROM Dent d WHERE d.codedent = :codedent"),
    @NamedQuery(name = "Dent.findByLibelledent", query = "SELECT d FROM Dent d WHERE d.libelledent = :libelledent")})
public class Dent implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Size(max = 30)
    @Column(name = "codedent")
    private String codedent;
    @Size(max = 200)
    @Column(name = "libelledent")
    private String libelledent;
    @OneToMany(mappedBy = "iddent")
    private List<Detailoperation> detailoperationList;

    public Dent() {
    }

    public Dent(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodedent() {
        return codedent;
    }

    public void setCodedent(String codedent) {
        this.codedent = codedent;
    }

    public String getLibelledent() {
        return libelledent;
    }

    public void setLibelledent(String libelledent) {
        this.libelledent = libelledent;
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
        if (!(object instanceof Dent)) {
            return false;
        }
        Dent other = (Dent) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Dent[ id=" + id + " ]";
    }
    
}
