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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name = "dictionnaire_modele_rapport_com")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DictionnaireModeleRapportCom.findAll", query = "SELECT d FROM DictionnaireModeleRapportCom d"),
    @NamedQuery(name = "DictionnaireModeleRapportCom.findByIdTypeOperationCom", query = "SELECT d FROM DictionnaireModeleRapportCom d WHERE d.idTypeOperationCom = :idTypeOperationCom"),
    @NamedQuery(name = "DictionnaireModeleRapportCom.findByRequetteSql", query = "SELECT d FROM DictionnaireModeleRapportCom d WHERE d.requetteSql = :requetteSql")})
public class DictionnaireModeleRapportCom implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_type_operation_com")
    private Long idTypeOperationCom;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4000)
    @Column(name = "requette_sql")
    private String requetteSql;
    @JoinColumn(name = "id_type_operation_com", referencedColumnName = "NI_TOP", insertable = false, updatable = false)
    @OneToOne(optional = false)
    private TypeOpCom typeOpCom;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idDictionnaire")
    private List<DetailDictionnaireRapportCom> detailDictionnaireRapportComList;

    public DictionnaireModeleRapportCom() {
    }

    public DictionnaireModeleRapportCom(Long idTypeOperationCom) {
        this.idTypeOperationCom = idTypeOperationCom;
    }

    public DictionnaireModeleRapportCom(Long idTypeOperationCom, String requetteSql) {
        this.idTypeOperationCom = idTypeOperationCom;
        this.requetteSql = requetteSql;
    }

    public Long getIdTypeOperationCom() {
        return idTypeOperationCom;
    }

    public void setIdTypeOperationCom(Long idTypeOperationCom) {
        this.idTypeOperationCom = idTypeOperationCom;
    }

    public String getRequetteSql() {
        return requetteSql;
    }

    public void setRequetteSql(String requetteSql) {
        this.requetteSql = requetteSql;
    }

    public TypeOpCom getTypeOpCom() {
        return typeOpCom;
    }

    public void setTypeOpCom(TypeOpCom typeOpCom) {
        this.typeOpCom = typeOpCom;
    }

    @XmlTransient
    public List<DetailDictionnaireRapportCom> getDetailDictionnaireRapportComList() {
        return detailDictionnaireRapportComList;
    }

    public void setDetailDictionnaireRapportComList(List<DetailDictionnaireRapportCom> detailDictionnaireRapportComList) {
        this.detailDictionnaireRapportComList = detailDictionnaireRapportComList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTypeOperationCom != null ? idTypeOperationCom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DictionnaireModeleRapportCom)) {
            return false;
        }
        DictionnaireModeleRapportCom other = (DictionnaireModeleRapportCom) object;
        if ((this.idTypeOperationCom == null && other.idTypeOperationCom != null) || (this.idTypeOperationCom != null && !this.idTypeOperationCom.equals(other.idTypeOperationCom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DictionnaireModeleRapportCom[ idTypeOperationCom=" + idTypeOperationCom + " ]";
    }
    
}
