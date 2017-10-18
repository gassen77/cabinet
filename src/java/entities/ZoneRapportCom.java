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
@Table(name = "zone_rapport_com")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ZoneRapportCom.findAll", query = "SELECT z FROM ZoneRapportCom z"),
    @NamedQuery(name = "ZoneRapportCom.findByIdZoneRapport", query = "SELECT z FROM ZoneRapportCom z WHERE z.idZoneRapport = :idZoneRapport"),
    @NamedQuery(name = "ZoneRapportCom.findByNomZone", query = "SELECT z FROM ZoneRapportCom z WHERE z.nomZone = :nomZone"),
    @NamedQuery(name = "ZoneRapportCom.findByTopRapport", query = "SELECT z FROM ZoneRapportCom z WHERE z.topRapport = :topRapport"),
    @NamedQuery(name = "ZoneRapportCom.findByLeftRapport", query = "SELECT z FROM ZoneRapportCom z WHERE z.leftRapport = :leftRapport"),
    @NamedQuery(name = "ZoneRapportCom.findByHeightRapport", query = "SELECT z FROM ZoneRapportCom z WHERE z.heightRapport = :heightRapport"),
    @NamedQuery(name = "ZoneRapportCom.findByWidthRapport", query = "SELECT z FROM ZoneRapportCom z WHERE z.widthRapport = :widthRapport"),
    @NamedQuery(name = "ZoneRapportCom.findByTracageLigne", query = "SELECT z FROM ZoneRapportCom z WHERE z.tracageLigne = :tracageLigne")})
public class ZoneRapportCom implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_zone_rapport")
    private Integer idZoneRapport;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nom_zone")
    private String nomZone;
    @Column(name = "top_rapport")
    private Integer topRapport;
    @Column(name = "left_rapport")
    private Integer leftRapport;
    @Column(name = "height_rapport")
    private Integer heightRapport;
    @Column(name = "width_rapport")
    private Integer widthRapport;
    @Column(name = "tracage_ligne")
    private Boolean tracageLigne;
    @JoinColumn(name = "modele", referencedColumnName = "id_modele_rapport")
    @ManyToOne(optional = false)
    private ModeleRapportCom modele;
    @OneToMany(mappedBy = "zoneRapport")
    private List<DetailRapportCom> detailRapportComList;

    public ZoneRapportCom() {
    }

    public ZoneRapportCom(Integer idZoneRapport) {
        this.idZoneRapport = idZoneRapport;
    }

    public ZoneRapportCom(Integer idZoneRapport, String nomZone) {
        this.idZoneRapport = idZoneRapport;
        this.nomZone = nomZone;
    }

    public Integer getIdZoneRapport() {
        return idZoneRapport;
    }

    public void setIdZoneRapport(Integer idZoneRapport) {
        this.idZoneRapport = idZoneRapport;
    }

    public String getNomZone() {
        return nomZone;
    }

    public void setNomZone(String nomZone) {
        this.nomZone = nomZone;
    }

    public Integer getTopRapport() {
        return topRapport;
    }

    public void setTopRapport(Integer topRapport) {
        this.topRapport = topRapport;
    }

    public Integer getLeftRapport() {
        return leftRapport;
    }

    public void setLeftRapport(Integer leftRapport) {
        this.leftRapport = leftRapport;
    }

    public Integer getHeightRapport() {
        return heightRapport;
    }

    public void setHeightRapport(Integer heightRapport) {
        this.heightRapport = heightRapport;
    }

    public Integer getWidthRapport() {
        return widthRapport;
    }

    public void setWidthRapport(Integer widthRapport) {
        this.widthRapport = widthRapport;
    }

    public Boolean getTracageLigne() {
        return tracageLigne;
    }

    public void setTracageLigne(Boolean tracageLigne) {
        this.tracageLigne = tracageLigne;
    }

    public ModeleRapportCom getModele() {
        return modele;
    }

    public void setModele(ModeleRapportCom modele) {
        this.modele = modele;
    }

    @XmlTransient
    public List<DetailRapportCom> getDetailRapportComList() {
        return detailRapportComList;
    }

    public void setDetailRapportComList(List<DetailRapportCom> detailRapportComList) {
        this.detailRapportComList = detailRapportComList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idZoneRapport != null ? idZoneRapport.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ZoneRapportCom)) {
            return false;
        }
        ZoneRapportCom other = (ZoneRapportCom) object;
        if ((this.idZoneRapport == null && other.idZoneRapport != null) || (this.idZoneRapport != null && !this.idZoneRapport.equals(other.idZoneRapport))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ZoneRapportCom[ idZoneRapport=" + idZoneRapport + " ]";
    }
    
}
