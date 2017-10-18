/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import entities.DetailModeleSortie;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 *
 * @author OnlySoft
 */
@Stateless
public class DetailModeleSortieFacade extends AbstractFacade<DetailModeleSortie> {
      @PersistenceContext(unitName = "cabinet")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    public DetailModeleSortieFacade() {
        super(DetailModeleSortie.class);
    }
}
