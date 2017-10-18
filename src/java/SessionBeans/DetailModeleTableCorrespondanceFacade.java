/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import entities.DetailModeleTableCorrespondance;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author WAYEDS
 */
@Stateless
public class DetailModeleTableCorrespondanceFacade extends AbstractFacade<DetailModeleTableCorrespondance> {
    @PersistenceContext(unitName = "cabinet")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
   

    public DetailModeleTableCorrespondanceFacade() {
        super(DetailModeleTableCorrespondance.class);
    }
    
}
