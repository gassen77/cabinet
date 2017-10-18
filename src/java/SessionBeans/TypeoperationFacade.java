/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import entities.Typeoperation;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gassen
 */
@Stateless
public class TypeoperationFacade extends AbstractFacade<Typeoperation> {
   @PersistenceContext(unitName = "cabinet")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public TypeoperationFacade() {
        super(Typeoperation.class);
    }
    
}
