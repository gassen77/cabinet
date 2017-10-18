/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import entities.TypeOpCom;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 *
 * @author OnlySoft
 */
@Stateless
public class TypeOpComFacade extends AbstractFacade<TypeOpCom> {
      @PersistenceContext(unitName = "cabinet")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    public TypeOpComFacade() {
        super(TypeOpCom.class);
    }
}
