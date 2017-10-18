/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import entities.ModeleRapportCom;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 *
 * @author OnlySoft
 */
@Stateless
public class ModeleRapportComFacade extends AbstractFacade<ModeleRapportCom> {
      @PersistenceContext(unitName = "cabinet")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }
    public ModeleRapportComFacade() {
        super(ModeleRapportCom.class);
    }
}
