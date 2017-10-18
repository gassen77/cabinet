/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gassen
 */
@Entity
@Table(name = "detailordonance")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Detailordonance.findAll", query = "SELECT d FROM Detailordonance d"),
    @NamedQuery(name = "Detailordonance.findById", query = "SELECT d FROM Detailordonance d WHERE d.detailordonancePK.id = :id"),
    @NamedQuery(name = "Detailordonance.findByQteordonance", query = "SELECT d FROM Detailordonance d WHERE d.qteordonance = :qteordonance"),
    @NamedQuery(name = "Detailordonance.findByPosologie", query = "SELECT d FROM Detailordonance d WHERE d.posologie = :posologie"),
    @NamedQuery(name = "Detailordonance.findByDuree", query = "SELECT d FROM Detailordonance d WHERE d.duree = :duree"),
    @NamedQuery(name = "Detailordonance.findByOrdre", query = "SELECT d FROM Detailordonance d WHERE d.detailordonancePK.ordre = :ordre")})
public class Detailordonance implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DetailordonancePK detailordonancePK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "qteordonance")
    private BigDecimal qteordonance;
    @Size(max = 50)
    @Column(name = "posologie")
    private String posologie;
    @Size(max = 50)
    @Column(name = "duree")
    private String duree;
    @JoinColumn(name = "idmedicament", referencedColumnName = "id")
    @ManyToOne
    private Medicament idmedicament;
    @JoinColumn(name = "idoperation", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Operation idoperation;

    public Detailordonance() {
    }

    public Detailordonance(DetailordonancePK detailordonancePK) {
        this.detailordonancePK = detailordonancePK;
    }

    public Detailordonance(long id, int ordre) {
        this.detailordonancePK = new DetailordonancePK(id, ordre);
    }

    public DetailordonancePK getDetailordonancePK() {
        return detailordonancePK;
    }

    public void setDetailordonancePK(DetailordonancePK detailordonancePK) {
        this.detailordonancePK = detailordonancePK;
    }

    public BigDecimal getQteordonance() {
        return qteordonance;
    }

    public void setQteordonance(BigDecimal qteordonance) {
        this.qteordonance = qteordonance;
    }

  

    public String getPosologie() {
        return posologie;
    }

    public void setPosologie(String posologie) {
        this.posologie = posologie;
    }

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public Medicament getIdmedicament() {
        return idmedicament;
    }

    public void setIdmedicament(Medicament idmedicament) {
        this.idmedicament = idmedicament;
    }

    public Operation getIdoperation() {
        return idoperation;
    }

    public void setIdoperation(Operation idoperation) {
        this.idoperation = idoperation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (detailordonancePK != null ? detailordonancePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Detailordonance)) {
            return false;
        }
        Detailordonance other = (Detailordonance) object;
        if ((this.detailordonancePK == null && other.detailordonancePK != null) || (this.detailordonancePK != null && !this.detailordonancePK.equals(other.detailordonancePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Detailordonance[ detailordonancePK=" + detailordonancePK + " ]";
    }
    
}
