/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import entities.TableCorrespondance;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 *
 * @author OnlySoft
 */
@Stateless
public class TableCorrespondanceFacade extends AbstractFacade<TableCorrespondance> {
      @PersistenceContext(unitName = "cabinet")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    public TableCorrespondanceFacade() {
        super(TableCorrespondance.class);
    }
}
