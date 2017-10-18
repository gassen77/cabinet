/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JsfClasses;

import entities.ModeleRapportCom;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import SessionBeans.ModeleRapportComFacade;

/**
 *
 * @author OnlySoft
 */
public class ModeleRapportC implements Converter{
public Integer getKey(String value) {
            Integer key;
            key = new Integer(value);
            return key;
        }
public String getStringKey(Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            try{
            ModeleRapportComFacade controller = (ModeleRapportComFacade) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "modeleJpa");
            return controller.find(getKey(value));
            }catch(Exception e){
            return null;
        }
        }

    @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            try{
                ModeleRapportCom o = (ModeleRapportCom) object;
                return getStringKey(o.getIdModeleRapport());
            }catch(Exception e){
            return "";
            }
        }
    
}
