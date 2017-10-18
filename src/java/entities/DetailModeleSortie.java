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
@Table(name = "detail_modele_sortie")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetailModeleSortie.findAll", query = "SELECT d FROM DetailModeleSortie d"),
    @NamedQuery(name = "DetailModeleSortie.findByIdDetailModeleSortie", query = "SELECT d FROM DetailModeleSortie d WHERE d.idDetailModeleSortie = :idDetailModeleSortie"),
    @NamedQuery(name = "DetailModeleSortie.findByNomColonne", query = "SELECT d FROM DetailModeleSortie d WHERE d.nomColonne = :nomColonne"),
    @NamedQuery(name = "DetailModeleSortie.findByLongueurColonne", query = "SELECT d FROM DetailModeleSortie d WHERE d.longueurColonne = :longueurColonne"),
    @NamedQuery(name = "DetailModeleSortie.findByOrdreColonne", query = "SELECT d FROM DetailModeleSortie d WHERE d.ordreColonne = :ordreColonne"),
    @NamedQuery(name = "DetailModeleSortie.findByTableColonne", query = "SELECT d FROM DetailModeleSortie d WHERE d.tableColonne = :tableColonne"),
    @NamedQuery(name = "DetailModeleSortie.findByClePrimTable", query = "SELECT d FROM DetailModeleSortie d WHERE d.clePrimTable = :clePrimTable"),
    @NamedQuery(name = "DetailModeleSortie.findBySommable", query = "SELECT d FROM DetailModeleSortie d WHERE d.sommable = :sommable"),
    @NamedQuery(name = "DetailModeleSortie.findByTypeColonne", query = "SELECT d FROM DetailModeleSortie d WHERE d.typeColonne = :typeColonne"),
    @NamedQuery(name = "DetailModeleSortie.findByFontSizeColonne", query = "SELECT d FROM DetailModeleSortie d WHERE d.fontSizeColonne = :fontSizeColonne"),
    @NamedQuery(name = "DetailModeleSortie.findByOrderByInstruction", query = "SELECT d FROM DetailModeleSortie d WHERE d.orderByInstruction = :orderByInstruction"),
    @NamedQuery(name = "DetailModeleSortie.findByNumeroDeOrderBy", query = "SELECT d FROM DetailModeleSortie d WHERE d.numeroDeOrderBy = :numeroDeOrderBy"),
    @NamedQuery(name = "DetailModeleSortie.findByAppelePar", query = "SELECT d FROM DetailModeleSortie d WHERE d.appelePar = :appelePar"),
    @NamedQuery(name = "DetailModeleSortie.findByTableAppelePar", query = "SELECT d FROM DetailModeleSortie d WHERE d.tableAppelePar = :tableAppelePar")})
public class DetailModeleSortie implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_detail_modele_sortie")
    private Integer idDetailModeleSortie;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 300)
    @Column(name = "nom_colonne")
    private String nomColonne;
    @Basic(optional = false)
    @NotNull
    @Column(name = "longueur_colonne")
    private int longueurColonne;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ordre_colonne")
    private int ordreColonne;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "table_colonne")
    private String tableColonne;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "cle_prim_table")
    private String clePrimTable;
    @Column(name = "sommable")
    private Boolean sommable;
    @Size(max = 45)
    @Column(name = "typeColonne")
    private String typeColonne;
    @Column(name = "fontSizeColonne")
    private Integer fontSizeColonne;
    @Column(name = "orderByInstruction")
    private Boolean orderByInstruction;
    @Column(name = "numeroDeOrderBy")
    private Integer numeroDeOrderBy;
    @Size(max = 70)
    @Column(name = "appelePar")
    private String appelePar;
    @Size(max = 70)
    @Column(name = "tableAppelePar")
    private String tableAppelePar;
    @JoinColumn(name = "modele_sortie", referencedColumnName = "id_modele_sortie")
    @ManyToOne(optional = false)
    private Modelesortie modeleSortie;

    public DetailModeleSortie() {
    }

    public DetailModeleSortie(Integer idDetailModeleSortie) {
        this.idDetailModeleSortie = idDetailModeleSortie;
    }

    public DetailModeleSortie(Integer idDetailModeleSortie, String nomColonne, int longueurColonne, int ordreColonne, String tableColonne, String clePrimTable) {
        this.idDetailModeleSortie = idDetailModeleSortie;
        this.nomColonne = nomColonne;
        this.longueurColonne = longueurColonne;
        this.ordreColonne = ordreColonne;
        this.tableColonne = tableColonne;
        this.clePrimTable = clePrimTable;
    }

    public Integer getIdDetailModeleSortie() {
        return idDetailModeleSortie;
    }

    public void setIdDetailModeleSortie(Integer idDetailModeleSortie) {
        this.idDetailModeleSortie = idDetailModeleSortie;
    }

    public String getNomColonne() {
        return nomColonne;
    }

    public void setNomColonne(String nomColonne) {
        this.nomColonne = nomColonne;
    }

    public int getLongueurColonne() {
        return longueurColonne;
    }

    public void setLongueurColonne(int longueurColonne) {
        this.longueurColonne = longueurColonne;
    }

    public int getOrdreColonne() {
        return ordreColonne;
    }

    public void setOrdreColonne(int ordreColonne) {
        this.ordreColonne = ordreColonne;
    }

    public String getTableColonne() {
        return tableColonne;
    }

    public void setTableColonne(String tableColonne) {
        this.tableColonne = tableColonne;
    }

    public String getClePrimTable() {
        return clePrimTable;
    }

    public void setClePrimTable(String clePrimTable) {
        this.clePrimTable = clePrimTable;
    }

    public Boolean getSommable() {
        return sommable;
    }

    public void setSommable(Boolean sommable) {
        this.sommable = sommable;
    }

    public String getTypeColonne() {
        return typeColonne;
    }

    public void setTypeColonne(String typeColonne) {
        this.typeColonne = typeColonne;
    }

    public Integer getFontSizeColonne() {
        return fontSizeColonne;
    }

    public void setFontSizeColonne(Integer fontSizeColonne) {
        this.fontSizeColonne = fontSizeColonne;
    }

    public Boolean getOrderByInstruction() {
        return orderByInstruction;
    }

    public void setOrderByInstruction(Boolean orderByInstruction) {
        this.orderByInstruction = orderByInstruction;
    }

    public Integer getNumeroDeOrderBy() {
        return numeroDeOrderBy;
    }

    public void setNumeroDeOrderBy(Integer numeroDeOrderBy) {
        this.numeroDeOrderBy = numeroDeOrderBy;
    }

    public String getAppelePar() {
        return appelePar;
    }

    public void setAppelePar(String appelePar) {
        this.appelePar = appelePar;
    }

    public String getTableAppelePar() {
        return tableAppelePar;
    }

    public void setTableAppelePar(String tableAppelePar) {
        this.tableAppelePar = tableAppelePar;
    }

    public Modelesortie getModeleSortie() {
        return modeleSortie;
    }

    public void setModeleSortie(Modelesortie modeleSortie) {
        this.modeleSortie = modeleSortie;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetailModeleSortie != null ? idDetailModeleSortie.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailModeleSortie)) {
            return false;
        }
        DetailModeleSortie other = (DetailModeleSortie) object;
        if ((this.idDetailModeleSortie == null && other.idDetailModeleSortie != null) || (this.idDetailModeleSortie != null && !this.idDetailModeleSortie.equals(other.idDetailModeleSortie))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DetailModeleSortie[ idDetailModeleSortie=" + idDetailModeleSortie + " ]";
    }
    
}
