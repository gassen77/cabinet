/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import entities.ModeleSortieFiltrage;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 *
 * @author OnlySoft
 */
@Stateless
public class ModeleSortieFiltrageFacade extends AbstractFacade<ModeleSortieFiltrage> {
      @PersistenceContext(unitName = "cabinet")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    public ModeleSortieFiltrageFacade() {
        super(ModeleSortieFiltrage.class);
    }
}
