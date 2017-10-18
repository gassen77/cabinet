/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author gassen
 */
@Embeddable
public class DetailoperationPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "idoperation")
    private long idoperation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ordre")
    private int ordre;

    public DetailoperationPK() {
    }

    public DetailoperationPK(long idoperation, int ordre) {
        this.idoperation = idoperation;
        this.ordre = ordre;
    }

    public long getIdoperation() {
        return idoperation;
    }

    public void setIdoperation(long idoperation) {
        this.idoperation = idoperation;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idoperation;
        hash += (int) ordre;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailoperationPK)) {
            return false;
        }
        DetailoperationPK other = (DetailoperationPK) object;
        if (this.idoperation != other.idoperation) {
            return false;
        }
        if (this.ordre != other.ordre) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DetailoperationPK[ idoperation=" + idoperation + ", ordre=" + ordre + " ]";
    }
    
}
