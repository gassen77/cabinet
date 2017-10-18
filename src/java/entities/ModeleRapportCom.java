/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "modele_rapport_com")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModeleRapportCom.findAll", query = "SELECT m FROM ModeleRapportCom m"),
    @NamedQuery(name = "ModeleRapportCom.findByIdModeleRapport", query = "SELECT m FROM ModeleRapportCom m WHERE m.idModeleRapport = :idModeleRapport"),
    @NamedQuery(name = "ModeleRapportCom.findByNomModele", query = "SELECT m FROM ModeleRapportCom m WHERE m.nomModele = :nomModele"),
    @NamedQuery(name = "ModeleRapportCom.findByRequette", query = "SELECT m FROM ModeleRapportCom m WHERE m.requette = :requette"),
    @NamedQuery(name = "ModeleRapportCom.findByMargeTop", query = "SELECT m FROM ModeleRapportCom m WHERE m.margeTop = :margeTop"),
    @NamedQuery(name = "ModeleRapportCom.findByMargeLeft", query = "SELECT m FROM ModeleRapportCom m WHERE m.margeLeft = :margeLeft"),
    @NamedQuery(name = "ModeleRapportCom.findByMargeRight", query = "SELECT m FROM ModeleRapportCom m WHERE m.margeRight = :margeRight"),
    @NamedQuery(name = "ModeleRapportCom.findByMatgeBottom", query = "SELECT m FROM ModeleRapportCom m WHERE m.matgeBottom = :matgeBottom"),
    @NamedQuery(name = "ModeleRapportCom.findByPageHeight", query = "SELECT m FROM ModeleRapportCom m WHERE m.pageHeight = :pageHeight"),
    @NamedQuery(name = "ModeleRapportCom.findByPageWidth", query = "SELECT m FROM ModeleRapportCom m WHERE m.pageWidth = :pageWidth"),
    @NamedQuery(name = "ModeleRapportCom.findByOrientation", query = "SELECT m FROM ModeleRapportCom m WHERE m.orientation = :orientation")})
public class ModeleRapportCom implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_modele_rapport")
    private Integer idModeleRapport;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    @Column(name = "nom_modele")
    private String nomModele;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "requette")
    private String requette;
    @Column(name = "marge_top")
    private Integer margeTop;
    @Column(name = "marge_left")
    private Integer margeLeft;
    @Column(name = "marge_right")
    private Integer margeRight;
    @Column(name = "matge_bottom")
    private Integer matgeBottom;
    @Column(name = "page_height")
    private Integer pageHeight;
    @Column(name = "page_width")
    private Integer pageWidth;
    @Column(name = "orientation")
    private Integer orientation;
    @JoinColumn(name = "type_operation", referencedColumnName = "NI_TOP")
    @ManyToOne
    private TypeOpCom typeOperation;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modele")
    private List<ZoneRapportCom> zoneRapportComList;

    public ModeleRapportCom() {
    }

    public ModeleRapportCom(Integer idModeleRapport) {
        this.idModeleRapport = idModeleRapport;
    }

    public ModeleRapportCom(Integer idModeleRapport, String nomModele, String requette) {
        this.idModeleRapport = idModeleRapport;
        this.nomModele = nomModele;
        this.requette = requette;
    }

    public Integer getIdModeleRapport() {
        return idModeleRapport;
    }

    public void setIdModeleRapport(Integer idModeleRapport) {
        this.idModeleRapport = idModeleRapport;
    }

    public String getNomModele() {
        return nomModele;
    }

    public void setNomModele(String nomModele) {
        this.nomModele = nomModele;
    }

    public String getRequette() {
        return requette;
    }

    public void setRequette(String requette) {
        this.requette = requette;
    }

    public Integer getMargeTop() {
        return margeTop;
    }

    public void setMargeTop(Integer margeTop) {
        this.margeTop = margeTop;
    }

    public Integer getMargeLeft() {
        return margeLeft;
    }

    public void setMargeLeft(Integer margeLeft) {
        this.margeLeft = margeLeft;
    }

    public Integer getMargeRight() {
        return margeRight;
    }

    public void setMargeRight(Integer margeRight) {
        this.margeRight = margeRight;
    }

    public Integer getMatgeBottom() {
        return matgeBottom;
    }

    public void setMatgeBottom(Integer matgeBottom) {
        this.matgeBottom = matgeBottom;
    }

    public Integer getPageHeight() {
        return pageHeight;
    }

    public void setPageHeight(Integer pageHeight) {
        this.pageHeight = pageHeight;
    }

    public Integer getPageWidth() {
        return pageWidth;
    }

    public void setPageWidth(Integer pageWidth) {
        this.pageWidth = pageWidth;
    }

    public Integer getOrientation() {
        return orientation;
    }

    public void setOrientation(Integer orientation) {
        this.orientation = orientation;
    }

    public TypeOpCom getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(TypeOpCom typeOperation) {
        this.typeOperation = typeOperation;
    }

    @XmlTransient
    public List<ZoneRapportCom> getZoneRapportComList() {
        return zoneRapportComList;
    }

    public void setZoneRapportComList(List<ZoneRapportCom> zoneRapportComList) {
        this.zoneRapportComList = zoneRapportComList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idModeleRapport != null ? idModeleRapport.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModeleRapportCom)) {
            return false;
        }
        ModeleRapportCom other = (ModeleRapportCom) object;
        if ((this.idModeleRapport == null && other.idModeleRapport != null) || (this.idModeleRapport != null && !this.idModeleRapport.equals(other.idModeleRapport))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ModeleRapportCom[ idModeleRapport=" + idModeleRapport + " ]";
    }
    
}
