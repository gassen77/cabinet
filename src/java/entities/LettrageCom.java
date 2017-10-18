/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "lettrage_com")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LettrageCom.findAll", query = "SELECT l FROM LettrageCom l"),
    @NamedQuery(name = "LettrageCom.findByNiLet", query = "SELECT l FROM LettrageCom l WHERE l.niLet = :niLet"),
    @NamedQuery(name = "LettrageCom.findByNumPiece", query = "SELECT l FROM LettrageCom l WHERE l.numPiece = :numPiece"),
    @NamedQuery(name = "LettrageCom.findByMontantPiece", query = "SELECT l FROM LettrageCom l WHERE l.montantPiece = :montantPiece"),
    @NamedQuery(name = "LettrageCom.findByMontantReg", query = "SELECT l FROM LettrageCom l WHERE l.montantReg = :montantReg"),
    @NamedQuery(name = "LettrageCom.findBySoldePiece", query = "SELECT l FROM LettrageCom l WHERE l.soldePiece = :soldePiece")})
public class LettrageCom implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "NI_LET")
    private Long niLet;
    @Column(name = "NUM_PIECE")
    private Long numPiece;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "MONTANT_PIECE")
    private BigDecimal montantPiece;
    @Column(name = "MONTANT_REG")
    private BigDecimal montantReg;
    @Column(name = "SOLDE_PIECE")
    private BigDecimal soldePiece;
    @JoinColumn(name = "NI_OP", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Operation niOp;
    @JoinColumn(name = "Type_OP_AL", referencedColumnName = "id")
    @ManyToOne
    private Typeoperation typeOPAL;
    @JoinColumn(name = "NI_PIECE", referencedColumnName = "id")
    @ManyToOne
    private Operation niPiece;
    @JoinColumn(name = "Type_OP_L", referencedColumnName = "id")
    @ManyToOne
    private Typeoperation typeOPL;

    public LettrageCom() {
    }

    public LettrageCom(Long niLet) {
        this.niLet = niLet;
    }

    public Long getNiLet() {
        return niLet;
    }

    public void setNiLet(Long niLet) {
        this.niLet = niLet;
    }

    public Long getNumPiece() {
        return numPiece;
    }

    public void setNumPiece(Long numPiece) {
        this.numPiece = numPiece;
    }

    public BigDecimal getMontantPiece() {
        return montantPiece;
    }

    public void setMontantPiece(BigDecimal montantPiece) {
        this.montantPiece = montantPiece;
    }

    public BigDecimal getMontantReg() {
        return montantReg;
    }

    public void setMontantReg(BigDecimal montantReg) {
        this.montantReg = montantReg;
    }

    public BigDecimal getSoldePiece() {
        return soldePiece;
    }

    public void setSoldePiece(BigDecimal soldePiece) {
        this.soldePiece = soldePiece;
    }

    public Operation getNiOp() {
        return niOp;
    }

    public void setNiOp(Operation niOp) {
        this.niOp = niOp;
    }

    public Typeoperation getTypeOPAL() {
        return typeOPAL;
    }

    public void setTypeOPAL(Typeoperation typeOPAL) {
        this.typeOPAL = typeOPAL;
    }

    public Operation getNiPiece() {
        return niPiece;
    }

    public void setNiPiece(Operation niPiece) {
        this.niPiece = niPiece;
    }

    public Typeoperation getTypeOPL() {
        return typeOPL;
    }

    public void setTypeOPL(Typeoperation typeOPL) {
        this.typeOPL = typeOPL;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (niLet != null ? niLet.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LettrageCom)) {
            return false;
        }
        LettrageCom other = (LettrageCom) object;
        if ((this.niLet == null && other.niLet != null) || (this.niLet != null && !this.niLet.equals(other.niLet))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.LettrageCom[ niLet=" + niLet + " ]";
    }
    
}
