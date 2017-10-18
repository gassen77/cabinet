/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JsfClasses;

import entities.Modelesortie;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import SessionBeans.ModelesortieFacade;

/**
 *
 * @author OnlySoft
 */
public class ModelesortieC implements Converter{
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
            ModelesortieFacade ejbCompte = (ModelesortieFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "modeleSortieJpa");
            return ejbCompte.find(getKey(value));
            }catch(Exception e){
            return null;
        }
        }

    @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            try{
                Modelesortie o = (Modelesortie) object;
                return getStringKey(o.getIdModeleSortie());
            }catch(Exception e){
            return "";
            }
        }
    
}
