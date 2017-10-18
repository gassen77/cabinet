/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gassen
 */
@Entity
@Table(name = "detail_modele_table_correspondance")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetailModeleTableCorrespondance.findAll", query = "SELECT d FROM DetailModeleTableCorrespondance d"),
    @NamedQuery(name = "DetailModeleTableCorrespondance.findById", query = "SELECT d FROM DetailModeleTableCorrespondance d WHERE d.id = :id"),
    @NamedQuery(name = "DetailModeleTableCorrespondance.findByAffichable", query = "SELECT d FROM DetailModeleTableCorrespondance d WHERE d.affichable = :affichable")})
public class DetailModeleTableCorrespondance implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Column(name = "affichable")
    private Boolean affichable;
    @JoinColumn(name = "table_correspondance", referencedColumnName = "colonne")
    @ManyToOne
    private TableCorrespondance tableCorrespondance;
    @JoinColumn(name = "modele", referencedColumnName = "id")
    @ManyToOne
    private ModeleTableCorrespondance modele;

    public DetailModeleTableCorrespondance() {
    }

    public DetailModeleTableCorrespondance(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getAffichable() {
        return affichable;
    }

    public void setAffichable(Boolean affichable) {
        this.affichable = affichable;
    }

    public TableCorrespondance getTableCorrespondance() {
        return tableCorrespondance;
    }

    public void setTableCorrespondance(TableCorrespondance tableCorrespondance) {
        this.tableCorrespondance = tableCorrespondance;
    }

    public ModeleTableCorrespondance getModele() {
        return modele;
    }

    public void setModele(ModeleTableCorrespondance modele) {
        this.modele = modele;
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
        if (!(object instanceof DetailModeleTableCorrespondance)) {
            return false;
        }
        DetailModeleTableCorrespondance other = (DetailModeleTableCorrespondance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DetailModeleTableCorrespondance[ id=" + id + " ]";
    }
    
}
