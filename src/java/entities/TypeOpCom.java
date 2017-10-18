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
@Table(name = "type_op_com")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TypeOpCom.findAll", query = "SELECT t FROM TypeOpCom t"),
    @NamedQuery(name = "TypeOpCom.findByNiTop", query = "SELECT t FROM TypeOpCom t WHERE t.niTop = :niTop"),
    @NamedQuery(name = "TypeOpCom.findByCodeTop", query = "SELECT t FROM TypeOpCom t WHERE t.codeTop = :codeTop"),
    @NamedQuery(name = "TypeOpCom.findByNomTop", query = "SELECT t FROM TypeOpCom t WHERE t.nomTop = :nomTop"),
    @NamedQuery(name = "TypeOpCom.findByNomaTop", query = "SELECT t FROM TypeOpCom t WHERE t.nomaTop = :nomaTop")})
public class TypeOpCom implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NI_TOP")
    private Long niTop;
    @Size(max = 30)
    @Column(name = "CODE_TOP")
    private String codeTop;
    @Size(max = 50)
    @Column(name = "NOM_TOP")
    private String nomTop;
    @Size(max = 100)
    @Column(name = "NOMA_TOP")
    private String nomaTop;
    @OneToMany(mappedBy = "typeOperation")
    private List<ModeleRapportCom> modeleRapportComList;
    @OneToMany(mappedBy = "typeModele")
    private List<Modelesortie> modelesortieList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "typeOpCom")
    private DictionnaireModeleRapportCom dictionnaireModeleRapportCom;

    public TypeOpCom() {
    }

    public TypeOpCom(Long niTop) {
        this.niTop = niTop;
    }

    public Long getNiTop() {
        return niTop;
    }

    public void setNiTop(Long niTop) {
        this.niTop = niTop;
    }

    public String getCodeTop() {
        return codeTop;
    }

    public void setCodeTop(String codeTop) {
        this.codeTop = codeTop;
    }

    public String getNomTop() {
        return nomTop;
    }

    public void setNomTop(String nomTop) {
        this.nomTop = nomTop;
    }

    public String getNomaTop() {
        return nomaTop;
    }

    public void setNomaTop(String nomaTop) {
        this.nomaTop = nomaTop;
    }

    @XmlTransient
    public List<ModeleRapportCom> getModeleRapportComList() {
        return modeleRapportComList;
    }

    public void setModeleRapportComList(List<ModeleRapportCom> modeleRapportComList) {
        this.modeleRapportComList = modeleRapportComList;
    }

    @XmlTransient
    public List<Modelesortie> getModelesortieList() {
        return modelesortieList;
    }

    public void setModelesortieList(List<Modelesortie> modelesortieList) {
        this.modelesortieList = modelesortieList;
    }

    public DictionnaireModeleRapportCom getDictionnaireModeleRapportCom() {
        return dictionnaireModeleRapportCom;
    }

    public void setDictionnaireModeleRapportCom(DictionnaireModeleRapportCom dictionnaireModeleRapportCom) {
        this.dictionnaireModeleRapportCom = dictionnaireModeleRapportCom;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (niTop != null ? niTop.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TypeOpCom)) {
            return false;
        }
        TypeOpCom other = (TypeOpCom) object;
        if ((this.niTop == null && other.niTop != null) || (this.niTop != null && !this.niTop.equals(other.niTop))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TypeOpCom[ niTop=" + niTop + " ]";
    }
    
}
