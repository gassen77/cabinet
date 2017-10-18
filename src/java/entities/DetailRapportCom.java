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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gassen
 */
@Entity
@Table(name = "detail_rapport_com")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetailRapportCom.findAll", query = "SELECT d FROM DetailRapportCom d"),
    @NamedQuery(name = "DetailRapportCom.findByIdDetailCom", query = "SELECT d FROM DetailRapportCom d WHERE d.idDetailCom = :idDetailCom"),
    @NamedQuery(name = "DetailRapportCom.findByTopRapport", query = "SELECT d FROM DetailRapportCom d WHERE d.topRapport = :topRapport"),
    @NamedQuery(name = "DetailRapportCom.findByLeftRapport", query = "SELECT d FROM DetailRapportCom d WHERE d.leftRapport = :leftRapport"),
    @NamedQuery(name = "DetailRapportCom.findByHeightRapport", query = "SELECT d FROM DetailRapportCom d WHERE d.heightRapport = :heightRapport"),
    @NamedQuery(name = "DetailRapportCom.findByWidthRapport", query = "SELECT d FROM DetailRapportCom d WHERE d.widthRapport = :widthRapport"),
    @NamedQuery(name = "DetailRapportCom.findByNom", query = "SELECT d FROM DetailRapportCom d WHERE d.nom = :nom"),
    @NamedQuery(name = "DetailRapportCom.findByType", query = "SELECT d FROM DetailRapportCom d WHERE d.type = :type"),
    @NamedQuery(name = "DetailRapportCom.findByExpression", query = "SELECT d FROM DetailRapportCom d WHERE d.expression = :expression"),
    @NamedQuery(name = "DetailRapportCom.findByFontSize", query = "SELECT d FROM DetailRapportCom d WHERE d.fontSize = :fontSize"),
    @NamedQuery(name = "DetailRapportCom.findByGras", query = "SELECT d FROM DetailRapportCom d WHERE d.gras = :gras"),
    @NamedQuery(name = "DetailRapportCom.findByItalique", query = "SELECT d FROM DetailRapportCom d WHERE d.italique = :italique"),
    @NamedQuery(name = "DetailRapportCom.findBySouligner", query = "SELECT d FROM DetailRapportCom d WHERE d.souligner = :souligner"),
    @NamedQuery(name = "DetailRapportCom.findByAlignement", query = "SELECT d FROM DetailRapportCom d WHERE d.alignement = :alignement"),
    @NamedQuery(name = "DetailRapportCom.findByStretchWithOverFlow", query = "SELECT d FROM DetailRapportCom d WHERE d.stretchWithOverFlow = :stretchWithOverFlow")})
public class DetailRapportCom implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_detail_com")
    private Integer idDetailCom;
    @Column(name = "top_rapport")
    private Integer topRapport;
    @Column(name = "left_rapport")
    private Integer leftRapport;
    @Column(name = "height_rapport")
    private Integer heightRapport;
    @Column(name = "width_rapport")
    private Integer widthRapport;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "nom")
    private String nom;
    @Size(max = 5)
    @Column(name = "type")
    private String type;
    @Size(max = 500)
    @Column(name = "expression")
    private String expression;
    @Column(name = "font_size")
    private Integer fontSize;
    @Column(name = "gras")
    private Boolean gras;
    @Column(name = "italique")
    private Boolean italique;
    @Column(name = "souligner")
    private Boolean souligner;
    @Column(name = "alignement")
    private Integer alignement;
    @Column(name = "stretch_with_over_flow")
    private Boolean stretchWithOverFlow;
    @JoinColumn(name = "zone_rapport", referencedColumnName = "id_zone_rapport")
    @ManyToOne
    private ZoneRapportCom zoneRapport;

    public DetailRapportCom() {
    }

    public DetailRapportCom(Integer idDetailCom) {
        this.idDetailCom = idDetailCom;
    }

    public DetailRapportCom(Integer idDetailCom, String nom) {
        this.idDetailCom = idDetailCom;
        this.nom = nom;
    }

    public Integer getIdDetailCom() {
        return idDetailCom;
    }

    public void setIdDetailCom(Integer idDetailCom) {
        this.idDetailCom = idDetailCom;
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Boolean getGras() {
        return gras;
    }

    public void setGras(Boolean gras) {
        this.gras = gras;
    }

    public Boolean getItalique() {
        return italique;
    }

    public void setItalique(Boolean italique) {
        this.italique = italique;
    }

    public Boolean getSouligner() {
        return souligner;
    }

    public void setSouligner(Boolean souligner) {
        this.souligner = souligner;
    }

    public Integer getAlignement() {
        return alignement;
    }

    public void setAlignement(Integer alignement) {
        this.alignement = alignement;
    }

    public Boolean getStretchWithOverFlow() {
        return stretchWithOverFlow;
    }

    public void setStretchWithOverFlow(Boolean stretchWithOverFlow) {
        this.stretchWithOverFlow = stretchWithOverFlow;
    }

    public ZoneRapportCom getZoneRapport() {
        return zoneRapport;
    }

    public void setZoneRapport(ZoneRapportCom zoneRapport) {
        this.zoneRapport = zoneRapport;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetailCom != null ? idDetailCom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailRapportCom)) {
            return false;
        }
        DetailRapportCom other = (DetailRapportCom) object;
        if ((this.idDetailCom == null && other.idDetailCom != null) || (this.idDetailCom != null && !this.idDetailCom.equals(other.idDetailCom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DetailRapportCom[ idDetailCom=" + idDetailCom + " ]";
    }
    
}
