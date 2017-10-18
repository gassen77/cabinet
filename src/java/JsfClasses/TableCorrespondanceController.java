package JsfClasses;

import entities.DetailModeleTableCorrespondance;
import entities.ModeleTableCorrespondance;
import entities.TableCorrespondance;
import JsfClasses.util.JsfUtil;

import JsfClasses.util.PaginationHelper;
import SessionBeans.TableCorrespondanceFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.transaction.UserTransaction;

@ManagedBean(name = "tableCorrespondanceController")
@ViewScoped
public class TableCorrespondanceController implements Serializable {

    private TableCorrespondance current;
    private DataModel items = null;
    @EJB
    private SessionBeans.TableCorrespondanceFacade ejbFacade;
     @EJB
    private SessionBeans.ModeleTableCorrespondanceFacade ejbFacadeModele;
     @EJB
    private SessionBeans.DetailModeleTableCorrespondanceFacade ejbFacadedetailModeleTableCorrespondance;
       @Resource
    private UserTransaction utx = null;
     
    private PaginationHelper pagination;
    private int selectedItemIndex;
      private Integer pageDestination;
         private String codeProListFiltrage;
    private String nomProListFiltrage;
    
    public Integer getPageDestination(){
        return pageDestination;
    }
    public void setPageDestination(Integer pageDestination) {
        this.pageDestination = pageDestination;
    }
      private String page;
      
      private List<ModeleTableCorrespondance> listeModele;

    public List<ModeleTableCorrespondance> getListeModele() {
        return listeModele;
    }

    public void setListeModele(List<ModeleTableCorrespondance> listeModele) {
        this.listeModele = listeModele;
    }
      
 @PostConstruct
    public void initAfterConstructor() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        try {
            pagination = (PaginationHelper) map.get("pagination");
        } catch (Exception e) {
        }
         try {
            page = (String) map.get("page");
        } catch (Exception e) {
        }
          try {
            listeModele = (List<ModeleTableCorrespondance>) map.get("listeModele");
        } catch (Exception e) {
        }
          try {
            current = (TableCorrespondance) map.get("current");
        } catch (Exception e) {
        }
          try {
            codeProListFiltrage = (String) map.get("codeProListFiltrage");
        } catch (Exception e) {
        }
        try {
            nomProListFiltrage = (String) map.get("nomProListFiltrage");
        } catch (Exception e) {
        }
 }
   public String getCodeProListFiltrage() {
        return codeProListFiltrage;
    }

    public void setCodeProListFiltrage(String codeProListFiltrage) {
        this.codeProListFiltrage = codeProListFiltrage;
    }

    public String getBool()
    {
        System.out.println("fct getbool");
        String bool="";
      
      
        try
        {
              TableCorrespondance T = (TableCorrespondance) getItems().getRowData();
        if (T.getAffichable())
           
        {
                bool="Oui";
                System.out.println("bool "+bool);
    }
        else
        {
            bool="Non";
        }
        }
        catch(Exception e)
        {
            e.printStackTrace();
           bool="Non";  
        }
       
        return bool;
    }
    
    public String getNomProListFiltrage() {
        return nomProListFiltrage;
    }

    public void setNomProListFiltrage(String nomProListFiltrage) {
        this.nomProListFiltrage = nomProListFiltrage;
    }
    
  public String rechercheMultipleCriteresTableCorres() {
        String requette = "Select o from TableCorrespondance o where ";
        List<String> n = new ArrayList<String>();
        List<Object> o = new ArrayList<Object>();
        String order = "";
        if (this.codeProListFiltrage != null) {
            if (this.codeProListFiltrage.isEmpty() == false) {
                requette = requette + "o.colonne like :colonne and ";
                n.add("colonne");
                o.add("%" + this.codeProListFiltrage + "%");
                order = order + " colonne";
            }
        }
        if (this.nomProListFiltrage != null) {
            if (this.nomProListFiltrage.isEmpty() == false) {
                requette = requette + "o.correspondance like :correspondance and ";
                n.add("correspondance");
                o.add("%" + nomProListFiltrage + "%");
                order = order + " correspondance";
            }
        }
        
        if (requette.endsWith(" and ")) {
            requette = requette.substring(0, requette.lastIndexOf(" and "));
        }
        String orderByRequette = "";
       
        
         if(order.contains("colonne")){
            orderByRequette=orderByRequette+"o.colonne,";
        }
        if(order.contains("correspondance")){
            orderByRequette=orderByRequette+"o.correspondance,";
        }
        if (orderByRequette.endsWith(",")) {
            orderByRequette = orderByRequette.substring(0, orderByRequette.lastIndexOf(","));
        }
        orderByRequette = "order by " + orderByRequette;
        requette = requette + " " + orderByRequette;
        requette = requette.trim();
        if (requette.endsWith(" order by")) {
            requette = requette.substring(0, requette.lastIndexOf(" order by"));
        }
        requette = requette.trim();
        if (requette.endsWith(" where")) {
            requette = requette.substring(0, requette.lastIndexOf(" where"));
        }
        System.out.println("requete "+requette);
        try {
            getPagination().setRequetteJpql(requette);
            getPagination().setArrayNames(n);
            getPagination().setArrayValues(o);
            getPagination().setPage(0);
            items = null;
        } catch (Exception er) {
            er.printStackTrace();
        }
        return null;
    }

 
   public void envoyerParametres() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        map.put("pagination", pagination);
        map.put("page", page);
        map.put("current", current);
        map.put("codeProListFiltrage", codeProListFiltrage);
        map.put("nomProListFiltrage", nomProListFiltrage);
        map.put("listeModele", listeModele);
    } 
   
     public String premierePage() {
        getPagination().setPage(0);
        recreateModel();
        return null;
    }
       public String previous() {
        this.pageDestination=getPagination().getPage();
      
        goPageDestination();
        return null;
        
    }
         public String goPageDestination() {
        this.rechercheMultipleCriteresTableCorres();
        items=null;
        if(pageDestination!=null){
            if(pageDestination>0){
                if(pageDestination<=getPagination().totalPages()){
                    getPagination().setPage(pageDestination.intValue()-1);
                    recreateModel();
                }
            }
        }
        pageDestination=null;
     
      
        return null;
    }
    public TableCorrespondanceController() {
    }

    public TableCorrespondance getSelected() {
        if (current == null) {
            current = new TableCorrespondance();
            selectedItemIndex = -1;
        }
        return current;
    }

    private TableCorrespondanceFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            List<String>s=new ArrayList<String>();
            List<Object>o=new ArrayList<Object>();
            pagination = new PaginationHelper(10,"Select o from Ville o",s,o) {

               @Override
                public int getItemsCount() {
                    return getFacade().countMultipleCritiria(getRequetteJpql(),getArrayNames(),getArrayValues());
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findByParameterMultipleCreteria(getRequetteJpql(),getArrayNames(),getArrayValues(),getPageFirstItem(),getPageSize()));
                }
            };          
        }
        return pagination;
    }
    public String prepareList() {
        rechercheMultipleCriteresInit();
        recreateModel();
         this.envoyerParametres();
        return "ListTableCorrespondance";
    }
      public String dernierePage() {
        getPagination().setPage(getPagination().totalPages()-1);
        recreateModel();
        return null;
    }
public void rechercheMultipleCriteresInit() {
        String requette88 = "SELECT t from TableCorrespondance t ";
        List<String> n = new ArrayList<String>();
        List<Object> o = new ArrayList<Object>();
         requette88=requette88.trim();
        try {
            getPagination().setRequetteJpql(requette88);
            getPagination().setArrayNames(n);
            getPagination().setArrayValues(o);
          //  items = getPagination().createPageDataModel();
             items=null;
        } catch (Exception er) {
            er.printStackTrace();
        }
    }

    public String prepareView() {
        current = (TableCorrespondance) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new TableCorrespondance();
        listeModele=new ArrayList<ModeleTableCorrespondance>();
        listeModele=ejbFacadeModele.findAll();
        System.out.println("size"+listeModele.size());
        Iterator it=listeModele.iterator();
        int i=0;
        while(it.hasNext())
        {ModeleTableCorrespondance mo=(ModeleTableCorrespondance)it.next();
             System.out.println(mo.getLibelle());
              mo.setFlagAfficher(false);
             listeModele.set(i, mo);
        i++;
        }
      
        selectedItemIndex = -1;
          this.envoyerParametres();
        return "CreateTableCorrespondance";
    }

    public String create() {
       try{
                utx.begin();
                Exception transactionException = null;
            getFacade().create(current);
            
           
            Iterator it=listeModele.iterator();
            int i=0;
            while(it.hasNext())
            {ModeleTableCorrespondance mo=(ModeleTableCorrespondance)it.next();
             System.out.println(mo.getLibelle()); System.out.print(mo.getFlagAfficher());
             DetailModeleTableCorrespondance d=new DetailModeleTableCorrespondance();
             d.setId(new Integer(1));
             d.setModele(mo);
             d.setAffichable(mo.getFlagAfficher());
             d.setTableCorrespondance(current);
             if(mo.getFlagAfficher()==true){d.setAffichable(true);
             }
             ejbFacadedetailModeleTableCorrespondance.create(d);
             
             }
 
            try {
                
                utx.commit();
                
    
            } catch (Exception ex1) {
                ex1.printStackTrace();
                transactionException = ex1;return null;
            } 
            
            JsfUtil.addSuccessMessage("Transaction reussie");
            return prepareCreate();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Transaction echouee");
            return null;
        }
    }
   
    public boolean active(ModeleTableCorrespondance model,TableCorrespondance table)
    {List<DetailModeleTableCorrespondance> detail=new ArrayList<DetailModeleTableCorrespondance>();
     String q="Select d from DetailModeleTableCorrespondance d where d.tableCorrespondance =:table and d.modele=:modele" ;
     detail=ejbFacadedetailModeleTableCorrespondance.execCommandeList2Param(q, "table", table, "modele", model);
     int s=detail.size();
     System.out.println("siiiiiize"+s);
     boolean flag=false;
     if(s!=0){flag=detail.get(0).getAffichable();}
    return flag;
    }
    
     public boolean trouve(ModeleTableCorrespondance model,TableCorrespondance table)
    {List<DetailModeleTableCorrespondance> detail=new ArrayList<DetailModeleTableCorrespondance>();
     String q="Select d from DetailModeleTableCorrespondance d where d.tableCorrespondance =:table and d.modele=:modele" ;
     detail=ejbFacadedetailModeleTableCorrespondance.execCommandeList2Param(q, "table", table, "modele", model);
     int s=detail.size();
     System.out.println("siiiiiize"+s);
     boolean flag=false;
     if(s!=0){flag=true;}
    return flag;
    }
     public DetailModeleTableCorrespondance detailtrouve(ModeleTableCorrespondance model,TableCorrespondance table)
    {List<DetailModeleTableCorrespondance> detail=new ArrayList<DetailModeleTableCorrespondance>();
     String q="Select d from DetailModeleTableCorrespondance d where d.tableCorrespondance =:table and d.modele=:modele" ;
     detail=ejbFacadedetailModeleTableCorrespondance.execCommandeList2Param(q, "table", table, "modele", model);
     int s=detail.size();
     System.out.println("siiiiiize"+s);
     DetailModeleTableCorrespondance det=new DetailModeleTableCorrespondance();
     if(s!=0){det=detail.get(0);}
    return det;
    }
    
    public String prepareEdit() {
        current = (TableCorrespondance) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        listeModele=new ArrayList<ModeleTableCorrespondance>();
        listeModele=ejbFacadeModele.findAll();
        System.out.println("size"+listeModele.size());
        Iterator it=listeModele.iterator();
        int i=0;
        while(it.hasNext())
        {ModeleTableCorrespondance mo=(ModeleTableCorrespondance)it.next();
             System.out.println(mo.getLibelle());
             mo.setFlagAfficher(active(mo, current));
             listeModele.set(i, mo);
        i++;
        }
        
         this.envoyerParametres();
        
        return "Edit";
    }

    public String update() {
       try{
                utx.begin();
                Exception transactionException = null;
            getFacade().edit(current);
            
        Iterator it=listeModele.iterator();
            int i=0;
            while(it.hasNext())
            {ModeleTableCorrespondance mo=(ModeleTableCorrespondance)it.next();
             System.out.println(mo.getLibelle()); System.out.print(mo.getFlagAfficher());
             DetailModeleTableCorrespondance d=new DetailModeleTableCorrespondance();
             if(trouve(mo, current)==true){
             d= detailtrouve(mo, current);
             d.setAffichable(mo.getFlagAfficher());
                 
             ejbFacadedetailModeleTableCorrespondance.edit(d);}
             else{
             d.setId(new Integer(1));
             d.setModele(mo);
             d.setAffichable(mo.getFlagAfficher());
             d.setTableCorrespondance(current);
             if(mo.getFlagAfficher()==true){d.setAffichable(true);
             }
             ejbFacadedetailModeleTableCorrespondance.create(d);
             
             }     
            
            }
            
            
            
            
            
            
            
            
            
            
            
             try {
                
                utx.commit();
                
    
            } catch (Exception ex1) {
                ex1.printStackTrace();
                transactionException = ex1;return null;
            } 
            JsfUtil.addSuccessMessage("Transaction reussie");
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Transaction echouee");
            return null;
        }
    }

    public String destroy() {
     
        current = (TableCorrespondance) getItems().getRowData();
        
        performDestroy();
        recreateModel();
        return prepareList();
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
      try{
                utx.begin();
                
                Exception transactionException = null;
         String deleteRequest = "Delete from DetailModeleTableCorrespondance d where d.tableCorrespondance.colonne='"+ current.getColonne()+"'";
       System.out.println("deleteRequest : " + this.ejbFacadedetailModeleTableCorrespondance.executerRemoveInstruction(deleteRequest, new ArrayList<String>(), new ArrayList<Object>()));
               getFacade().remove(current);
 
            try {
               
                utx.commit();
                
    
            } catch (Exception ex1) {
                ex1.printStackTrace();
                transactionException = ex1;
              
            } 
          
            JsfUtil.addSuccessMessage("Transaction reussie");
        
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Transaction echouee");
           
        }
    }
        
        
        
        
    

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    public String next() {
         this.pageDestination=getPagination().getPage();
        this.pageDestination=this.pageDestination+2;
        goPageDestination();
        return null;
    }

    

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = TableCorrespondance.class)
    public static class TableCorrespondanceControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TableCorrespondanceController controller = (TableCorrespondanceController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tableCorrespondanceController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TableCorrespondance) {
                TableCorrespondance o = (TableCorrespondance) object;
                return getStringKey(o.getColonne());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TableCorrespondanceController.class.getName());
            }
        }
    }
}
