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
@Table(name = "table_correspondance")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TableCorrespondance.findAll", query = "SELECT t FROM TableCorrespondance t"),
    @NamedQuery(name = "TableCorrespondance.findByColonne", query = "SELECT t FROM TableCorrespondance t WHERE t.colonne = :colonne"),
    @NamedQuery(name = "TableCorrespondance.findByCorrespondance", query = "SELECT t FROM TableCorrespondance t WHERE t.correspondance = :correspondance"),
    @NamedQuery(name = "TableCorrespondance.findByAffichable", query = "SELECT t FROM TableCorrespondance t WHERE t.affichable = :affichable"),
    @NamedQuery(name = "TableCorrespondance.findByOrdre", query = "SELECT t FROM TableCorrespondance t WHERE t.ordre = :ordre")})
public class TableCorrespondance implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "colonne")
    private String colonne;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "correspondance")
    private String correspondance;
    @Column(name = "affichable")
    private Boolean affichable;
    @Column(name = "ordre")
    private Integer ordre;
    @OneToMany(mappedBy = "tableCorrespondance")
    private List<DetailModeleTableCorrespondance> detailModeleTableCorrespondanceList;

    public TableCorrespondance() {
    }

    public TableCorrespondance(String colonne) {
        this.colonne = colonne;
    }

    public TableCorrespondance(String colonne, String correspondance) {
        this.colonne = colonne;
        this.correspondance = correspondance;
    }

    public String getColonne() {
        return colonne;
    }

    public void setColonne(String colonne) {
        this.colonne = colonne;
    }

    public String getCorrespondance() {
        return correspondance;
    }

    public void setCorrespondance(String correspondance) {
        this.correspondance = correspondance;
    }

    public Boolean getAffichable() {
        return affichable;
    }

    public void setAffichable(Boolean affichable) {
        this.affichable = affichable;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    @XmlTransient
    public List<DetailModeleTableCorrespondance> getDetailModeleTableCorrespondanceList() {
        return detailModeleTableCorrespondanceList;
    }

    public void setDetailModeleTableCorrespondanceList(List<DetailModeleTableCorrespondance> detailModeleTableCorrespondanceList) {
        this.detailModeleTableCorrespondanceList = detailModeleTableCorrespondanceList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (colonne != null ? colonne.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TableCorrespondance)) {
            return false;
        }
        TableCorrespondance other = (TableCorrespondance) object;
        if ((this.colonne == null && other.colonne != null) || (this.colonne != null && !this.colonne.equals(other.colonne))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TableCorrespondance[ colonne=" + colonne + " ]";
    }
    
}
