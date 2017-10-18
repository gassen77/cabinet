/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import entities.Acte;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gassen
 */
@Stateless
public class ActeFacade extends AbstractFacade<Acte> {
      @PersistenceContext(unitName = "cabinet")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ActeFacade() {
        super(Acte.class);
    }
    
}
