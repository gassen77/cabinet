package JsfClasses.util;

import java.io.File;
import javax.faces.context.FacesContext;

public class ParametresRapports {
    protected final static Integer bufferSize=new Integer(1024);
    protected final static String pathRapport=FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport/");
    protected final static String pathRapportTemporaire=FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport/")+"\\tempo";
    protected final static Integer nbMaxPageToCut=new Integer(2);
    public static Integer getBufferSize(){
        return bufferSize;
    }
    public static String getPathRapport(){
        return pathRapport;
    }
    public static String getPathRapportTemporaire(){
        try{
            File f=new File(pathRapportTemporaire);
            f.mkdirs();
            return pathRapportTemporaire;
        }catch(Exception e){}
        return null;
    }
    public static Integer getNbMaxPageToCut() {
        return nbMaxPageToCut;
    }
}
