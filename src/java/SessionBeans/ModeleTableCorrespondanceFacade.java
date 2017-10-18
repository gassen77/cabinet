/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import entities.ModeleTableCorrespondance;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author WAYEDS
 */
@Stateless
public class ModeleTableCorrespondanceFacade extends AbstractFacade<ModeleTableCorrespondance> {
 
  @PersistenceContext(unitName = "cabinet")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    public ModeleTableCorrespondanceFacade() {
        super(ModeleTableCorrespondance.class);
    }
    
}
