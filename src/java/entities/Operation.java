/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gassen
 */
@Entity
@Table(name = "operation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Operation.findAll", query = "SELECT o FROM Operation o"),
    @NamedQuery(name = "Operation.findById", query = "SELECT o FROM Operation o WHERE o.id = :id"),
    @NamedQuery(name = "Operation.findByNumerooperation", query = "SELECT o FROM Operation o WHERE o.numerooperation = :numerooperation"),
    @NamedQuery(name = "Operation.findByDateoperation", query = "SELECT o FROM Operation o WHERE o.dateoperation = :dateoperation"),
    @NamedQuery(name = "Operation.findByMontantoperation", query = "SELECT o FROM Operation o WHERE o.montantoperation = :montantoperation"),
    @NamedQuery(name = "Operation.findByMontantdebite", query = "SELECT o FROM Operation o WHERE o.montantdebite = :montantdebite"),
    @NamedQuery(name = "Operation.findByMontantcredite", query = "SELECT o FROM Operation o WHERE o.montantcredite = :montantcredite"),
    @NamedQuery(name = "Operation.findByMontantcheque", query = "SELECT o FROM Operation o WHERE o.montantcheque = :montantcheque"),
    @NamedQuery(name = "Operation.findByMontantespece", query = "SELECT o FROM Operation o WHERE o.montantespece = :montantespece"),
    @NamedQuery(name = "Operation.findByMontanttraite", query = "SELECT o FROM Operation o WHERE o.montanttraite = :montanttraite"),
    @NamedQuery(name = "Operation.findByMontantvirement", query = "SELECT o FROM Operation o WHERE o.montantvirement = :montantvirement"),
    @NamedQuery(name = "Operation.findByDateSys", query = "SELECT o FROM Operation o WHERE o.dateSys = :dateSys"),
    @NamedQuery(name = "Operation.findByLibelleoperation", query = "SELECT o FROM Operation o WHERE o.libelleoperation = :libelleoperation"),
    @NamedQuery(name = "Operation.findByReference", query = "SELECT o FROM Operation o WHERE o.reference = :reference"),
    @NamedQuery(name = "Operation.findByRecu", query = "SELECT o FROM Operation o WHERE o.recu = :recu"),
    @NamedQuery(name = "Operation.findByEmetteur", query = "SELECT o FROM Operation o WHERE o.emetteur = :emetteur"),
    @NamedQuery(name = "Operation.findByDateechenace", query = "SELECT o FROM Operation o WHERE o.dateechenace = :dateechenace"),
    @NamedQuery(name = "Operation.findByDetail", query = "SELECT o FROM Operation o WHERE o.detail = :detail"),
    @NamedQuery(name = "Operation.findByDateconsultation", query = "SELECT o FROM Operation o WHERE o.dateconsultation = :dateconsultation"),
    @NamedQuery(name = "Operation.findByMotifconsultation", query = "SELECT o FROM Operation o WHERE o.motifconsultation = :motifconsultation")})
public class Operation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Long id;
    @Column(name = "numerooperation")
    private Long numerooperation;
    @Column(name = "dateoperation")
    @Temporal(TemporalType.DATE)
    private Date dateoperation;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montantoperation")
    private BigDecimal montantoperation;
    @Column(name = "montantdebite")
    private BigDecimal montantdebite;
    @Column(name = "montantcredite")
    private BigDecimal montantcredite;
    @Column(name = "montantcheque")
    private BigDecimal montantcheque;
    @Column(name = "montantespece")
    private BigDecimal montantespece;
    @Column(name = "montanttraite")
    private BigDecimal montanttraite;
    @Column(name = "montantvirement")
    private BigDecimal montantvirement;
    @Column(name = "date_sys")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSys;
    @Size(max = 400)
    @Column(name = "libelleoperation")
    private String libelleoperation;
    @Size(max = 50)
    @Column(name = "reference")
    private String reference;
    @Size(max = 10)
    @Column(name = "recu")
    private String recu;
    @Size(max = 70)
    @Column(name = "emetteur")
    private String emetteur;
    @Column(name = "dateechenace")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateechenace;
    @Column(name = "detail")
    private Boolean detail;
    @Column(name = "dateconsultation")
    @Temporal(TemporalType.DATE)
    private Date dateconsultation;
    @Size(max = 400)
    @Column(name = "motifconsultation")
    private String motifconsultation;
    @JoinColumn(name = "user", referencedColumnName = "id")
    @ManyToOne
    private User user;
    @JoinColumn(name = "typeoperation", referencedColumnName = "id")
    @ManyToOne
    private Typeoperation typeoperation;
    @JoinColumn(name = "modereglement", referencedColumnName = "id")
    @ManyToOne
    private Modereglement modereglement;
    @JoinColumn(name = "client", referencedColumnName = "id")
    @ManyToOne
    private Client client;
    @JoinColumn(name = "banque", referencedColumnName = "id")
    @ManyToOne
    private Banque banque;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "niOp")
    private List<LettrageCom> lettrageComList;
    @OneToMany(mappedBy = "niPiece")
    private List<LettrageCom> lettrageComList1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idoperation")
    private List<Detailordonance> detailordonanceList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operation")
    private List<Detailoperation> detailoperationList;

    public Operation() {
    }

    public Operation(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumerooperation() {
        return numerooperation;
    }

    public void setNumerooperation(Long numerooperation) {
        this.numerooperation = numerooperation;
    }

    public Date getDateoperation() {
        return dateoperation;
    }

    public void setDateoperation(Date dateoperation) {
        this.dateoperation = dateoperation;
    }

    public BigDecimal getMontantoperation() {
        return montantoperation;
    }

    public void setMontantoperation(BigDecimal montantoperation) {
        this.montantoperation = montantoperation;
    }

    public BigDecimal getMontantdebite() {
        return montantdebite;
    }

    public void setMontantdebite(BigDecimal montantdebite) {
        this.montantdebite = montantdebite;
    }

    public BigDecimal getMontantcredite() {
        return montantcredite;
    }

    public void setMontantcredite(BigDecimal montantcredite) {
        this.montantcredite = montantcredite;
    }

    public BigDecimal getMontantcheque() {
        return montantcheque;
    }

    public void setMontantcheque(BigDecimal montantcheque) {
        this.montantcheque = montantcheque;
    }

    public BigDecimal getMontantespece() {
        return montantespece;
    }

    public void setMontantespece(BigDecimal montantespece) {
        this.montantespece = montantespece;
    }

    public BigDecimal getMontanttraite() {
        return montanttraite;
    }

    public void setMontanttraite(BigDecimal montanttraite) {
        this.montanttraite = montanttraite;
    }

    public BigDecimal getMontantvirement() {
        return montantvirement;
    }

    public void setMontantvirement(BigDecimal montantvirement) {
        this.montantvirement = montantvirement;
    }

    public Date getDateSys() {
        return dateSys;
    }

    public void setDateSys(Date dateSys) {
        this.dateSys = dateSys;
    }

    public String getLibelleoperation() {
        return libelleoperation;
    }

    public void setLibelleoperation(String libelleoperation) {
        this.libelleoperation = libelleoperation;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getRecu() {
        return recu;
    }

    public void setRecu(String recu) {
        this.recu = recu;
    }

    public String getEmetteur() {
        return emetteur;
    }

    public void setEmetteur(String emetteur) {
        this.emetteur = emetteur;
    }

    public Date getDateechenace() {
        return dateechenace;
    }

    public void setDateechenace(Date dateechenace) {
        this.dateechenace = dateechenace;
    }

    public Boolean getDetail() {
        return detail;
    }

    public void setDetail(Boolean detail) {
        this.detail = detail;
    }

    public Date getDateconsultation() {
        return dateconsultation;
    }

    public void setDateconsultation(Date dateconsultation) {
        this.dateconsultation = dateconsultation;
    }

    public String getMotifconsultation() {
        return motifconsultation;
    }

    public void setMotifconsultation(String motifconsultation) {
        this.motifconsultation = motifconsultation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Typeoperation getTypeoperation() {
        return typeoperation;
    }

    public void setTypeoperation(Typeoperation typeoperation) {
        this.typeoperation = typeoperation;
    }

    public Modereglement getModereglement() {
        return modereglement;
    }

    public void setModereglement(Modereglement modereglement) {
        this.modereglement = modereglement;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Banque getBanque() {
        return banque;
    }

    public void setBanque(Banque banque) {
        this.banque = banque;
    }

    @XmlTransient
    public List<LettrageCom> getLettrageComList() {
        return lettrageComList;
    }

    public void setLettrageComList(List<LettrageCom> lettrageComList) {
        this.lettrageComList = lettrageComList;
    }

    @XmlTransient
    public List<LettrageCom> getLettrageComList1() {
        return lettrageComList1;
    }

    public void setLettrageComList1(List<LettrageCom> lettrageComList1) {
        this.lettrageComList1 = lettrageComList1;
    }

    @XmlTransient
    public List<Detailordonance> getDetailordonanceList() {
        return detailordonanceList;
    }

    public void setDetailordonanceList(List<Detailordonance> detailordonanceList) {
        this.detailordonanceList = detailordonanceList;
    }

    @XmlTransient
    public List<Detailoperation> getDetailoperationList() {
        return detailoperationList;
    }

    public void setDetailoperationList(List<Detailoperation> detailoperationList) {
        this.detailoperationList = detailoperationList;
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
        if (!(object instanceof Operation)) {
            return false;
        }
        Operation other = (Operation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Operation[ id=" + id + " ]";
    }
    
}
