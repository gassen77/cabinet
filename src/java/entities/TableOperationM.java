/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.math.BigDecimal;

/**
 *
 * @author AYMAN
 */
public class TableOperationM {
    private Operation operation;
    private BigDecimal acompte;
    private BigDecimal MontantReglement;
    private LettrageCom lettrageCom;
    private boolean check;
   // private DetailVersement detailVers;
    public boolean isCheck() {
        return check;
    }

//    public DetailVersement getDetailVers() {
//        return detailVers;
//    }
//
//    public void setDetailVers(DetailVersement detailVers) {
//        this.detailVers = detailVers;
//    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public LettrageCom getLettrageCom() {
        return lettrageCom;
    }

    public void setLettrageCom(LettrageCom lettrageCom) {
        this.lettrageCom = lettrageCom;
    }
    
    

    public BigDecimal getMontantReglement() {
        return MontantReglement;
    }

    public void setMontantReglement(BigDecimal MontantReglement) {
        this.MontantReglement = MontantReglement;
    }

    public BigDecimal getAcompte() {
        return acompte;
    }

    public void setAcompte(BigDecimal acompte) {
        this.acompte = acompte;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }
   
    
}
