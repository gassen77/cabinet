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
@Table(name = "detail_dictionnaire_rapport_com")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetailDictionnaireRapportCom.findAll", query = "SELECT d FROM DetailDictionnaireRapportCom d"),
    @NamedQuery(name = "DetailDictionnaireRapportCom.findByIdDetailDictionnaire", query = "SELECT d FROM DetailDictionnaireRapportCom d WHERE d.idDetailDictionnaire = :idDetailDictionnaire"),
    @NamedQuery(name = "DetailDictionnaireRapportCom.findByExpressionLibelle", query = "SELECT d FROM DetailDictionnaireRapportCom d WHERE d.expressionLibelle = :expressionLibelle"),
    @NamedQuery(name = "DetailDictionnaireRapportCom.findByLibelle", query = "SELECT d FROM DetailDictionnaireRapportCom d WHERE d.libelle = :libelle")})
public class DetailDictionnaireRapportCom implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_detail_dictionnaire")
    private Integer idDetailDictionnaire;
    @Size(max = 350)
    @Column(name = "expression_libelle")
    private String expressionLibelle;
    @Size(max = 200)
    @Column(name = "libelle")
    private String libelle;
    @JoinColumn(name = "id_dictionnaire", referencedColumnName = "id_type_operation_com")
    @ManyToOne(optional = false)
    private DictionnaireModeleRapportCom idDictionnaire;

    public DetailDictionnaireRapportCom() {
    }

    public DetailDictionnaireRapportCom(Integer idDetailDictionnaire) {
        this.idDetailDictionnaire = idDetailDictionnaire;
    }

    public Integer getIdDetailDictionnaire() {
        return idDetailDictionnaire;
    }

    public void setIdDetailDictionnaire(Integer idDetailDictionnaire) {
        this.idDetailDictionnaire = idDetailDictionnaire;
    }

    public String getExpressionLibelle() {
        return expressionLibelle;
    }

    public void setExpressionLibelle(String expressionLibelle) {
        this.expressionLibelle = expressionLibelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public DictionnaireModeleRapportCom getIdDictionnaire() {
        return idDictionnaire;
    }

    public void setIdDictionnaire(DictionnaireModeleRapportCom idDictionnaire) {
        this.idDictionnaire = idDictionnaire;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetailDictionnaire != null ? idDetailDictionnaire.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailDictionnaireRapportCom)) {
            return false;
        }
        DetailDictionnaireRapportCom other = (DetailDictionnaireRapportCom) object;
        if ((this.idDetailDictionnaire == null && other.idDetailDictionnaire != null) || (this.idDetailDictionnaire != null && !this.idDetailDictionnaire.equals(other.idDetailDictionnaire))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DetailDictionnaireRapportCom[ idDetailDictionnaire=" + idDetailDictionnaire + " ]";
    }
    
}
