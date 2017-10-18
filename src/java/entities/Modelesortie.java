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
@Table(name = "modelesortie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Modelesortie.findAll", query = "SELECT m FROM Modelesortie m"),
    @NamedQuery(name = "Modelesortie.findByIdModeleSortie", query = "SELECT m FROM Modelesortie m WHERE m.idModeleSortie = :idModeleSortie"),
    @NamedQuery(name = "Modelesortie.findByCodeModeleSortie", query = "SELECT m FROM Modelesortie m WHERE m.codeModeleSortie = :codeModeleSortie"),
    @NamedQuery(name = "Modelesortie.findByLibelleModeleSortie", query = "SELECT m FROM Modelesortie m WHERE m.libelleModeleSortie = :libelleModeleSortie"),
    @NamedQuery(name = "Modelesortie.findByPaysagePortait", query = "SELECT m FROM Modelesortie m WHERE m.paysagePortait = :paysagePortait"),
    @NamedQuery(name = "Modelesortie.findByReglement", query = "SELECT m FROM Modelesortie m WHERE m.reglement = :reglement"),
    @NamedQuery(name = "Modelesortie.findByLongeurReglement", query = "SELECT m FROM Modelesortie m WHERE m.longeurReglement = :longeurReglement"),
    @NamedQuery(name = "Modelesortie.findByFontSizeReglement", query = "SELECT m FROM Modelesortie m WHERE m.fontSizeReglement = :fontSizeReglement"),
    @NamedQuery(name = "Modelesortie.findBySolde", query = "SELECT m FROM Modelesortie m WHERE m.solde = :solde"),
    @NamedQuery(name = "Modelesortie.findByLongueurSolde", query = "SELECT m FROM Modelesortie m WHERE m.longueurSolde = :longueurSolde"),
    @NamedQuery(name = "Modelesortie.findByFontSizeSolde", query = "SELECT m FROM Modelesortie m WHERE m.fontSizeSolde = :fontSizeSolde"),
    @NamedQuery(name = "Modelesortie.findByOrdreReglement", query = "SELECT m FROM Modelesortie m WHERE m.ordreReglement = :ordreReglement"),
    @NamedQuery(name = "Modelesortie.findByOrdreSolde", query = "SELECT m FROM Modelesortie m WHERE m.ordreSolde = :ordreSolde"),
    @NamedQuery(name = "Modelesortie.findByReglementRapport", query = "SELECT m FROM Modelesortie m WHERE m.reglementRapport = :reglementRapport"),
    @NamedQuery(name = "Modelesortie.findByTypeTiers", query = "SELECT m FROM Modelesortie m WHERE m.typeTiers = :typeTiers"),
    @NamedQuery(name = "Modelesortie.findByHtTtc", query = "SELECT m FROM Modelesortie m WHERE m.htTtc = :htTtc")})
public class Modelesortie implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_modele_sortie")
    private Integer idModeleSortie;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "code_modele_sortie")
    private String codeModeleSortie;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 80)
    @Column(name = "libelle_modele_sortie")
    private String libelleModeleSortie;
    @Column(name = "paysagePortait")
    private Boolean paysagePortait;
    @Column(name = "reglement")
    private Integer reglement;
    @Column(name = "longeurReglement")
    private Integer longeurReglement;
    @Column(name = "fontSizeReglement")
    private Integer fontSizeReglement;
    @Column(name = "solde")
    private Boolean solde;
    @Column(name = "longueurSolde")
    private Integer longueurSolde;
    @Column(name = "fontSizeSolde")
    private Integer fontSizeSolde;
    @Column(name = "ordreReglement")
    private Integer ordreReglement;
    @Column(name = "ordreSolde")
    private Integer ordreSolde;
    @Column(name = "reglementRapport")
    private Boolean reglementRapport;
    @Size(max = 50)
    @Column(name = "typeTiers")
    private String typeTiers;
    @Column(name = "htTtc")
    private Boolean htTtc;
    @JoinColumn(name = "typeModele", referencedColumnName = "NI_TOP")
    @ManyToOne
    private TypeOpCom typeModele;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modeleSortiee")
    private List<ModeleSortieFiltrage> modeleSortieFiltrageList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modeleSortie")
    private List<DetailModeleSortie> detailModeleSortieList;

    public Modelesortie() {
    }

    public Modelesortie(Integer idModeleSortie) {
        this.idModeleSortie = idModeleSortie;
    }

    public Modelesortie(Integer idModeleSortie, String codeModeleSortie, String libelleModeleSortie) {
        this.idModeleSortie = idModeleSortie;
        this.codeModeleSortie = codeModeleSortie;
        this.libelleModeleSortie = libelleModeleSortie;
    }

    public Integer getIdModeleSortie() {
        return idModeleSortie;
    }

    public void setIdModeleSortie(Integer idModeleSortie) {
        this.idModeleSortie = idModeleSortie;
    }

    public String getCodeModeleSortie() {
        return codeModeleSortie;
    }

    public void setCodeModeleSortie(String codeModeleSortie) {
        this.codeModeleSortie = codeModeleSortie;
    }

    public String getLibelleModeleSortie() {
        return libelleModeleSortie;
    }

    public void setLibelleModeleSortie(String libelleModeleSortie) {
        this.libelleModeleSortie = libelleModeleSortie;
    }

    public Boolean getPaysagePortait() {
        return paysagePortait;
    }

    public void setPaysagePortait(Boolean paysagePortait) {
        this.paysagePortait = paysagePortait;
    }

    public Integer getReglement() {
        return reglement;
    }

    public void setReglement(Integer reglement) {
        this.reglement = reglement;
    }

    public Integer getLongeurReglement() {
        return longeurReglement;
    }

    public void setLongeurReglement(Integer longeurReglement) {
        this.longeurReglement = longeurReglement;
    }

    public Integer getFontSizeReglement() {
        return fontSizeReglement;
    }

    public void setFontSizeReglement(Integer fontSizeReglement) {
        this.fontSizeReglement = fontSizeReglement;
    }

    public Boolean getSolde() {
        return solde;
    }

    public void setSolde(Boolean solde) {
        this.solde = solde;
    }

    public Integer getLongueurSolde() {
        return longueurSolde;
    }

    public void setLongueurSolde(Integer longueurSolde) {
        this.longueurSolde = longueurSolde;
    }

    public Integer getFontSizeSolde() {
        return fontSizeSolde;
    }

    public void setFontSizeSolde(Integer fontSizeSolde) {
        this.fontSizeSolde = fontSizeSolde;
    }

    public Integer getOrdreReglement() {
        return ordreReglement;
    }

    public void setOrdreReglement(Integer ordreReglement) {
        this.ordreReglement = ordreReglement;
    }

    public Integer getOrdreSolde() {
        return ordreSolde;
    }

    public void setOrdreSolde(Integer ordreSolde) {
        this.ordreSolde = ordreSolde;
    }

    public Boolean getReglementRapport() {
        return reglementRapport;
    }

    public void setReglementRapport(Boolean reglementRapport) {
        this.reglementRapport = reglementRapport;
    }

    public String getTypeTiers() {
        return typeTiers;
    }

    public void setTypeTiers(String typeTiers) {
        this.typeTiers = typeTiers;
    }

    public Boolean getHtTtc() {
        return htTtc;
    }

    public void setHtTtc(Boolean htTtc) {
        this.htTtc = htTtc;
    }

    public TypeOpCom getTypeModele() {
        return typeModele;
    }

    public void setTypeModele(TypeOpCom typeModele) {
        this.typeModele = typeModele;
    }

    @XmlTransient
    public List<ModeleSortieFiltrage> getModeleSortieFiltrageList() {
        return modeleSortieFiltrageList;
    }

    public void setModeleSortieFiltrageList(List<ModeleSortieFiltrage> modeleSortieFiltrageList) {
        this.modeleSortieFiltrageList = modeleSortieFiltrageList;
    }

    @XmlTransient
    public List<DetailModeleSortie> getDetailModeleSortieList() {
        return detailModeleSortieList;
    }

    public void setDetailModeleSortieList(List<DetailModeleSortie> detailModeleSortieList) {
        this.detailModeleSortieList = detailModeleSortieList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idModeleSortie != null ? idModeleSortie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Modelesortie)) {
            return false;
        }
        Modelesortie other = (Modelesortie) object;
        if ((this.idModeleSortie == null && other.idModeleSortie != null) || (this.idModeleSortie != null && !this.idModeleSortie.equals(other.idModeleSortie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Modelesortie[ idModeleSortie=" + idModeleSortie + " ]";
    }
    
}
