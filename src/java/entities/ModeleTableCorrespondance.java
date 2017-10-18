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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "modele_table_correspondance")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModeleTableCorrespondance.findAll", query = "SELECT m FROM ModeleTableCorrespondance m"),
    @NamedQuery(name = "ModeleTableCorrespondance.findById", query = "SELECT m FROM ModeleTableCorrespondance m WHERE m.id = :id"),
    @NamedQuery(name = "ModeleTableCorrespondance.findByCode", query = "SELECT m FROM ModeleTableCorrespondance m WHERE m.code = :code"),
    @NamedQuery(name = "ModeleTableCorrespondance.findByLibelle", query = "SELECT m FROM ModeleTableCorrespondance m WHERE m.libelle = :libelle"),
    @NamedQuery(name = "ModeleTableCorrespondance.findByFlagAfficher", query = "SELECT m FROM ModeleTableCorrespondance m WHERE m.flagAfficher = :flagAfficher")})
public class ModeleTableCorrespondance implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "code")
    private String code;
    @Size(max = 200)
    @Column(name = "libelle")
    private String libelle;
    @Column(name = "flag_afficher")
    private Boolean flagAfficher;
    @OneToMany(mappedBy = "modele")
    private List<DetailModeleTableCorrespondance> detailModeleTableCorrespondanceList;

    public ModeleTableCorrespondance() {
    }

    public ModeleTableCorrespondance(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Boolean getFlagAfficher() {
        return flagAfficher;
    }

    public void setFlagAfficher(Boolean flagAfficher) {
        this.flagAfficher = flagAfficher;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModeleTableCorrespondance)) {
            return false;
        }
        ModeleTableCorrespondance other = (ModeleTableCorrespondance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ModeleTableCorrespondance[ id=" + id + " ]";
    }
    
}
