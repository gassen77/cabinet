/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import entities.DetailDictionnaireRapportCom;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
/**
 *
 * @author GHASSEN
 */
@Stateless
public class DetailDictionnaireRapportComFacade extends AbstractFacade<DetailDictionnaireRapportCom> {
      @PersistenceContext(unitName = "cabinet")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    public DetailDictionnaireRapportComFacade() {
        super(DetailDictionnaireRapportCom.class);
    }
}
