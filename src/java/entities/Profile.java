/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "profile")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Profile.findAll", query = "SELECT p FROM Profile p"),
    @NamedQuery(name = "Profile.findByUser", query = "SELECT p FROM Profile p WHERE p.profilePK.user = :user"),
    @NamedQuery(name = "Profile.findByTableinterne", query = "SELECT p FROM Profile p WHERE p.profilePK.tableinterne = :tableinterne"),
    @NamedQuery(name = "Profile.findByAjouter", query = "SELECT p FROM Profile p WHERE p.ajouter = :ajouter"),
    @NamedQuery(name = "Profile.findByModifier", query = "SELECT p FROM Profile p WHERE p.modifier = :modifier"),
    @NamedQuery(name = "Profile.findBySupprimer", query = "SELECT p FROM Profile p WHERE p.supprimer = :supprimer"),
    @NamedQuery(name = "Profile.findByLogiciel", query = "SELECT p FROM Profile p WHERE p.profilePK.logiciel = :logiciel")})
public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ProfilePK profilePK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "ajouter")
    private String ajouter;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "modifier")
    private String modifier;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "supprimer")
    private String supprimer;
    @JoinColumn(name = "user", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private User user1;

    public Profile() {
    }

    public Profile(ProfilePK profilePK) {
        this.profilePK = profilePK;
    }

    public Profile(ProfilePK profilePK, String ajouter, String modifier, String supprimer) {
        this.profilePK = profilePK;
        this.ajouter = ajouter;
        this.modifier = modifier;
        this.supprimer = supprimer;
    }

    public Profile(int user, String tableinterne, String logiciel) {
        this.profilePK = new ProfilePK(user, tableinterne, logiciel);
    }

    public ProfilePK getProfilePK() {
        return profilePK;
    }

    public void setProfilePK(ProfilePK profilePK) {
        this.profilePK = profilePK;
    }

    public String getAjouter() {
        return ajouter;
    }

    public void setAjouter(String ajouter) {
        this.ajouter = ajouter;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getSupprimer() {
        return supprimer;
    }

    public void setSupprimer(String supprimer) {
        this.supprimer = supprimer;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (profilePK != null ? profilePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Profile)) {
            return false;
        }
        Profile other = (Profile) object;
        if ((this.profilePK == null && other.profilePK != null) || (this.profilePK != null && !this.profilePK.equals(other.profilePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Profile[ profilePK=" + profilePK + " ]";
    }
    
}
