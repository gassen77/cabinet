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
@Table(name = "cabinet")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cabinet.findAll", query = "SELECT c FROM Cabinet c"),
    @NamedQuery(name = "Cabinet.findById", query = "SELECT c FROM Cabinet c WHERE c.id = :id"),
    @NamedQuery(name = "Cabinet.findByCode", query = "SELECT c FROM Cabinet c WHERE c.code = :code"),
    @NamedQuery(name = "Cabinet.findByLibelle", query = "SELECT c FROM Cabinet c WHERE c.libelle = :libelle"),
    @NamedQuery(name = "Cabinet.findByAdresse", query = "SELECT c FROM Cabinet c WHERE c.adresse = :adresse"),
    @NamedQuery(name = "Cabinet.findByFax", query = "SELECT c FROM Cabinet c WHERE c.fax = :fax"),
    @NamedQuery(name = "Cabinet.findByTel", query = "SELECT c FROM Cabinet c WHERE c.tel = :tel"),
    @NamedQuery(name = "Cabinet.findByMatricule", query = "SELECT c FROM Cabinet c WHERE c.matricule = :matricule"),
    @NamedQuery(name = "Cabinet.findByVille", query = "SELECT c FROM Cabinet c WHERE c.ville = :ville"),
    @NamedQuery(name = "Cabinet.findByMatriculefiscale", query = "SELECT c FROM Cabinet c WHERE c.matriculefiscale = :matriculefiscale"),
    @NamedQuery(name = "Cabinet.findByCodepostal", query = "SELECT c FROM Cabinet c WHERE c.codepostal = :codepostal")})
public class Cabinet implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "libelle")
    private String libelle;
    @Size(max = 70)
    @Column(name = "adresse")
    private String adresse;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 25)
    @Column(name = "fax")
    private String fax;
    @Size(max = 25)
    @Column(name = "tel")
    private String tel;
    @Size(max = 45)
    @Column(name = "matricule")
    private String matricule;
    @Size(max = 50)
    @Column(name = "ville")
    private String ville;
    @Size(max = 50)
    @Column(name = "matriculefiscale")
    private String matriculefiscale;
    @Column(name = "codepostal")
    private Integer codepostal;

    public Cabinet() {
    }

    public Cabinet(Long id) {
        this.id = id;
    }

    public Cabinet(Long id, String code, String libelle) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getMatriculefiscale() {
        return matriculefiscale;
    }

    public void setMatriculefiscale(String matriculefiscale) {
        this.matriculefiscale = matriculefiscale;
    }

    public Integer getCodepostal() {
        return codepostal;
    }

    public void setCodepostal(Integer codepostal) {
        this.codepostal = codepostal;
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
        if (!(object instanceof Cabinet)) {
            return false;
        }
        Cabinet other = (Cabinet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Cabinet[ id=" + id + " ]";
    }
    
}
