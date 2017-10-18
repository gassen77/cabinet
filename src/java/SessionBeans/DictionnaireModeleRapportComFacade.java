/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import entities.DictionnaireModeleRapportCom;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 *
 * @author GHASSEN
 */
@Stateless
public class DictionnaireModeleRapportComFacade extends AbstractFacade<DictionnaireModeleRapportCom> {
      @PersistenceContext(unitName = "cabinet")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    public DictionnaireModeleRapportComFacade() {
        super(DictionnaireModeleRapportCom.class);
    }
}
