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
import javax.validation.constraints.Size;

/**
 *
 * @author gassen
 */
@Embeddable
public class ProfilePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Column(name = "user")
    private int user;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "tableinterne")
    private String tableinterne;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "logiciel")
    private String logiciel;

    public ProfilePK() {
    }

    public ProfilePK(int user, String tableinterne, String logiciel) {
        this.user = user;
        this.tableinterne = tableinterne;
        this.logiciel = logiciel;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getTableinterne() {
        return tableinterne;
    }

    public void setTableinterne(String tableinterne) {
        this.tableinterne = tableinterne;
    }

    public String getLogiciel() {
        return logiciel;
    }

    public void setLogiciel(String logiciel) {
        this.logiciel = logiciel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) user;
        hash += (tableinterne != null ? tableinterne.hashCode() : 0);
        hash += (logiciel != null ? logiciel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProfilePK)) {
            return false;
        }
        ProfilePK other = (ProfilePK) object;
        if (this.user != other.user) {
            return false;
        }
        if ((this.tableinterne == null && other.tableinterne != null) || (this.tableinterne != null && !this.tableinterne.equals(other.tableinterne))) {
            return false;
        }
        if ((this.logiciel == null && other.logiciel != null) || (this.logiciel != null && !this.logiciel.equals(other.logiciel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ProfilePK[ user=" + user + ", tableinterne=" + tableinterne + ", logiciel=" + logiciel + " ]";
    }
    
}
