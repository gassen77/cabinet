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
@Table(name = "modele_sortie_filtrage")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModeleSortieFiltrage.findAll", query = "SELECT m FROM ModeleSortieFiltrage m"),
    @NamedQuery(name = "ModeleSortieFiltrage.findByIdModeleSortieFiltrage", query = "SELECT m FROM ModeleSortieFiltrage m WHERE m.idModeleSortieFiltrage = :idModeleSortieFiltrage"),
    @NamedQuery(name = "ModeleSortieFiltrage.findByNomColonneFiltrage", query = "SELECT m FROM ModeleSortieFiltrage m WHERE m.nomColonneFiltrage = :nomColonneFiltrage"),
    @NamedQuery(name = "ModeleSortieFiltrage.findByTypeColonneFiltrage", query = "SELECT m FROM ModeleSortieFiltrage m WHERE m.typeColonneFiltrage = :typeColonneFiltrage"),
    @NamedQuery(name = "ModeleSortieFiltrage.findByNomTableColonne", query = "SELECT m FROM ModeleSortieFiltrage m WHERE m.nomTableColonne = :nomTableColonne"),
    @NamedQuery(name = "ModeleSortieFiltrage.findByClePrimTableColonne", query = "SELECT m FROM ModeleSortieFiltrage m WHERE m.clePrimTableColonne = :clePrimTableColonne"),
    @NamedQuery(name = "ModeleSortieFiltrage.findByAppelePar", query = "SELECT m FROM ModeleSortieFiltrage m WHERE m.appelePar = :appelePar"),
    @NamedQuery(name = "ModeleSortieFiltrage.findByTableAppeleApr", query = "SELECT m FROM ModeleSortieFiltrage m WHERE m.tableAppeleApr = :tableAppeleApr")})
public class ModeleSortieFiltrage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_modele_sortie_filtrage")
    private Integer idModeleSortieFiltrage;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_colonne_filtrage")
    private String nomColonneFiltrage;
    @Size(max = 50)
    @Column(name = "type_colonne_filtrage")
    private String typeColonneFiltrage;
    @Size(max = 50)
    @Column(name = "nom_table_colonne")
    private String nomTableColonne;
    @Size(max = 50)
    @Column(name = "cle_prim_table_colonne")
    private String clePrimTableColonne;
    @Size(max = 70)
    @Column(name = "appelePar")
    private String appelePar;
    @Size(max = 70)
    @Column(name = "tableAppeleApr")
    private String tableAppeleApr;
    @JoinColumn(name = "modele_sortiee", referencedColumnName = "id_modele_sortie")
    @ManyToOne(optional = false)
    private Modelesortie modeleSortiee;

    public ModeleSortieFiltrage() {
    }

    public ModeleSortieFiltrage(Integer idModeleSortieFiltrage) {
        this.idModeleSortieFiltrage = idModeleSortieFiltrage;
    }

    public ModeleSortieFiltrage(Integer idModeleSortieFiltrage, String nomColonneFiltrage) {
        this.idModeleSortieFiltrage = idModeleSortieFiltrage;
        this.nomColonneFiltrage = nomColonneFiltrage;
    }

    public Integer getIdModeleSortieFiltrage() {
        return idModeleSortieFiltrage;
    }

    public void setIdModeleSortieFiltrage(Integer idModeleSortieFiltrage) {
        this.idModeleSortieFiltrage = idModeleSortieFiltrage;
    }

    public String getNomColonneFiltrage() {
        return nomColonneFiltrage;
    }

    public void setNomColonneFiltrage(String nomColonneFiltrage) {
        this.nomColonneFiltrage = nomColonneFiltrage;
    }

    public String getTypeColonneFiltrage() {
        return typeColonneFiltrage;
    }

    public void setTypeColonneFiltrage(String typeColonneFiltrage) {
        this.typeColonneFiltrage = typeColonneFiltrage;
    }

    public String getNomTableColonne() {
        return nomTableColonne;
    }

    public void setNomTableColonne(String nomTableColonne) {
        this.nomTableColonne = nomTableColonne;
    }

    public String getClePrimTableColonne() {
        return clePrimTableColonne;
    }

    public void setClePrimTableColonne(String clePrimTableColonne) {
        this.clePrimTableColonne = clePrimTableColonne;
    }

    public String getAppelePar() {
        return appelePar;
    }

    public void setAppelePar(String appelePar) {
        this.appelePar = appelePar;
    }

    public String getTableAppeleApr() {
        return tableAppeleApr;
    }

    public void setTableAppeleApr(String tableAppeleApr) {
        this.tableAppeleApr = tableAppeleApr;
    }

    public Modelesortie getModeleSortiee() {
        return modeleSortiee;
    }

    public void setModeleSortiee(Modelesortie modeleSortiee) {
        this.modeleSortiee = modeleSortiee;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idModeleSortieFiltrage != null ? idModeleSortieFiltrage.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ModeleSortieFiltrage)) {
            return false;
        }
        ModeleSortieFiltrage other = (ModeleSortieFiltrage) object;
        if ((this.idModeleSortieFiltrage == null && other.idModeleSortieFiltrage != null) || (this.idModeleSortieFiltrage != null && !this.idModeleSortieFiltrage.equals(other.idModeleSortieFiltrage))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ModeleSortieFiltrage[ idModeleSortieFiltrage=" + idModeleSortieFiltrage + " ]";
    }
    
}
