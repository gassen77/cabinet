/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import entities.Modelesortie;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 *
 * @author OnlySoft
 */
@Stateless
public class ModelesortieFacade extends AbstractFacade<Modelesortie> {
      @PersistenceContext(unitName = "cabinet")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    public ModelesortieFacade() {
        super(Modelesortie.class);
    }
}
