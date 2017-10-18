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
@Table(name = "medicament")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Medicament.findAll", query = "SELECT m FROM Medicament m"),
    @NamedQuery(name = "Medicament.findById", query = "SELECT m FROM Medicament m WHERE m.id = :id"),
    @NamedQuery(name = "Medicament.findByCodemedicament", query = "SELECT m FROM Medicament m WHERE m.codemedicament = :codemedicament"),
    @NamedQuery(name = "Medicament.findByLibellemedicament", query = "SELECT m FROM Medicament m WHERE m.libellemedicament = :libellemedicament")})
public class Medicament implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Size(max = 30)
    @Column(name = "codemedicament")
    private String codemedicament;
    @Size(max = 200)
    @Column(name = "libellemedicament")
    private String libellemedicament;
    @OneToMany(mappedBy = "idmedicament")
    private List<Detailordonance> detailordonanceList;

    public Medicament() {
    }

    public Medicament(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodemedicament() {
        return codemedicament;
    }

    public void setCodemedicament(String codemedicament) {
        this.codemedicament = codemedicament;
    }

    public String getLibellemedicament() {
        return libellemedicament;
    }

    public void setLibellemedicament(String libellemedicament) {
        this.libellemedicament = libellemedicament;
    }


    @XmlTransient
    public List<Detailordonance> getDetailordonanceList() {
        return detailordonanceList;
    }

    public void setDetailordonanceList(List<Detailordonance> detailordonanceList) {
        this.detailordonanceList = detailordonanceList;
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
        if (!(object instanceof Medicament)) {
            return false;
        }
        Medicament other = (Medicament) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Medicament[ id=" + id + " ]";
    }
    
}
