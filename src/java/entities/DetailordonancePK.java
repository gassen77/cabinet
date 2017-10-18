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
public class DetailordonancePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ordre")
    private int ordre;

    public DetailordonancePK() {
    }

    public DetailordonancePK(long id, int ordre) {
        this.id = id;
        this.ordre = ordre;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        hash += (int) id;
        hash += (int) ordre;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailordonancePK)) {
            return false;
        }
        DetailordonancePK other = (DetailordonancePK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.ordre != other.ordre) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DetailordonancePK[ id=" + id + ", ordre=" + ordre + " ]";
    }
    
}
