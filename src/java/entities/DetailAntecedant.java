/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gassen
 */
@Entity
@Table(name = "detail_antecedant")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetailAntecedant.findAll", query = "SELECT d FROM DetailAntecedant d"),
    @NamedQuery(name = "DetailAntecedant.findById", query = "SELECT d FROM DetailAntecedant d WHERE d.id = :id")})
public class DetailAntecedant implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne
    private Client client;
    @JoinColumn(name = "antecedant", referencedColumnName = "id")
    @ManyToOne
    private Antecedant antecedant;

    public DetailAntecedant() {
    }

    public DetailAntecedant(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Antecedant getAntecedant() {
        return antecedant;
    }

    public void setAntecedant(Antecedant antecedant) {
        this.antecedant = antecedant;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetailAntecedant)) {
            return false;
        }
        DetailAntecedant other = (DetailAntecedant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.DetailAntecedant[ id=" + id + " ]";
    }
    
}
