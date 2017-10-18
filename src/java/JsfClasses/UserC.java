/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JsfClasses;

import entities.User;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 *
 * @author OnlySoft
 */
public class UserC implements Converter{
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
            UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.getFacade().find(getKey(value));
            }catch(Exception e){
            return null;
        }
        }

    @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            try{
                User o = (User) object;
                return getStringKey(o.getId());
            }catch(Exception e){
            return "";
            }
        }
    
}
