package JsfClasses;

import entities.Imagesclient;
import JsfClasses.util.JsfUtil;
import JsfClasses.util.PaginationHelper;
import SessionBeans.ClientFacade;
import SessionBeans.ImagesclientFacade;

import entities.Client;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import org.primefaces.event.FileUploadEvent;

@ManagedBean(name = "imagesclientController")
@ViewScoped
public class ImagesclientController implements Serializable {

    private Imagesclient current;
    private DataModel items = null;
    @EJB
    private SessionBeans.ImagesclientFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
     private String page;
    private Integer pageDestination;      
    private Map<String,Object>mapRechercheList;
  @EJB
    private ClientFacade ejbclient;
    public ImagesclientController() {
        FacesContext fc = FacesContext.getCurrentInstance();
          ejbclient = (ClientFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "ClientJpa");

    }
public Map<String, Object> getMapRechercheList() {
        return mapRechercheList;
    }
    public void setMapRechercheList(Map<String, Object> mapRechercheList) {
        this.mapRechercheList = mapRechercheList;
    }

   
    
    public Integer getPageDestination() {
        return pageDestination;
    }
    public void setPageDestination(Integer pageDestination) {
        this.pageDestination = pageDestination;
    }


    @PostConstruct
    public void init() {
 FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();   
       
        try {
           page = (String) map.get("page");
        } catch (Exception e) {
            
        }
        try {
            current = (Imagesclient) map.get("current");
        } catch (Exception e) {
            current = null;
        }
                try{
        items=(DataModel) map.get("items");
        }catch(Exception e){}
              
                   
                
                 try{
        mapRechercheList= (Map<String, Object>) map.get("mapRechercheList");
        }catch(Exception e){
        mapRechercheList=null;
        }
         
        if ((page != null)) {
            if ((page.equalsIgnoreCase("Create"))) {
                initPrepareCreate();
            }
            if (page.equalsIgnoreCase("List")) {
                initRechercheList();
            }
            if ((page.equalsIgnoreCase("Edit")) && (current != null)) {
                initPrepareEdit();
            }
        }
    }
     
     public void initPrepareCreate() {
          current = new Imagesclient();
          
        selectedItemIndex = -1;
    }
      public void initRechercheList() {
        recreateModel();
    }
       public void initPrepareEdit() {
    }
    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
    }
    public Imagesclient getSelected() {
        if (current == null) {
            current = new Imagesclient();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ImagesclientFacade getFacade() {
        return ejbFacade;
    }

   public PaginationHelper getPagination() {
        if (pagination == null) {
            List<String>s=new ArrayList<String>();
            List<Object>o=new ArrayList<Object>();
            pagination = new PaginationHelper(10,"Select o from Imagesclient o",s,o) {

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

         public String premierePage() {
        getPagination().setPage(0);
        recreateModel();
        return null;
    }
    public String dernierePage() {
        getPagination().setPage(getPagination().totalPages()-1);
        recreateModel();
        return null;
    }
    public String previous() {
        this.pageDestination=getPagination().getPage();
        return goPageDestination();
    }
    public String next() {
        this.pageDestination=getPagination().getPage();
        this.pageDestination=this.pageDestination+2;
        return goPageDestination();
    }
    public String goPageDestination() {
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
   
     public void setMettreJourAttributeRecherche(String mettreJourAttributeRecherche){
        FacesContext fc=FacesContext.getCurrentInstance();
        UIComponent component = UIViewRoot.getCurrentComponent(fc);
        UIPanel panel=(UIPanel)component;
        Iterator it=panel.getChildren().iterator();
        while(it.hasNext()){
            UIComponent u=(UIComponent)it.next();
            if(u.getClass().equals(HtmlInputText.class)){
                this.mapRechercheList.put(u.getId(),mettreJourAttributeRecherche);
            }
        }
    }
    public String getMettreJourAttributeRecherche(){
        FacesContext fc=FacesContext.getCurrentInstance();
        HtmlInputText component = (HtmlInputText) UIViewRoot.getCurrentComponent(fc);
        return (String) this.mapRechercheList.get(component.getId());
    }
    public String rechercherListItems(){
        String requette="Select o from Imagesclient o where ";
        String order="order by ";
        Iterator it=this.mapRechercheList.entrySet().iterator();
        List<String>ss=new ArrayList<String>();
        List<Object>oo=new ArrayList<Object>();
        while(it.hasNext()){
            Object o=it.next();
            Entry<String,Object> e=(Entry<String,Object>)o;
            String nomComponent=e.getKey();
            String valueComponent=e.getValue().toString();
            if(valueComponent!=null){
                if(valueComponent.isEmpty()==false){   
                    StringTokenizer st=new StringTokenizer(nomComponent,"_");
                    if(st.countTokens()==1){
                        requette=requette+"o."+nomComponent+" like :"+nomComponent+" and ";
                        ss.add(nomComponent);
                        oo.add("%"+valueComponent+"%");
                        order=order+"o."+nomComponent+",";
                    }else{
                        String nomCompletColonne="o.";
                        String lastSuffixColumn="";
                        while(st.hasMoreTokens()){
                            String stt=st.nextToken();
                            nomCompletColonne=nomCompletColonne+stt+".";
                            lastSuffixColumn=stt;
                        }
                        if(nomCompletColonne.endsWith(".")){
                            nomCompletColonne=nomCompletColonne.substring(0,nomCompletColonne.lastIndexOf("."));
                        }
                        requette=requette+nomCompletColonne+" like :"+lastSuffixColumn+" and ";
                        ss.add(lastSuffixColumn);
                        oo.add("%"+valueComponent+"%");
                        order=order+nomCompletColonne+",";
                    }
                }
            }
        }
        if(order.endsWith(",")){
            order=order.substring(0,order.lastIndexOf(","));
        }
        if(order.endsWith("order by ")){
            order=order.substring(0,order.lastIndexOf("order by "));
        }
        if(requette.endsWith(" and ")){
            requette=requette.substring(0,requette.lastIndexOf(" and "));
        }
        if(requette.endsWith(" where ")){
            requette=requette.substring(0,requette.lastIndexOf(" where "));
        }
        requette=requette.trim();
        requette=requette+" "+order;
        try{
            getPagination().setRequetteJpql(requette);
            getPagination().setArrayNames(ss);
            getPagination().setArrayValues(oo);
            getPagination().setPage(0);
            items=null;
        }catch(Exception ex){
            
        }
        return null;
    }

    public String prepareList() {
        recreateModel();
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();   
        this.mapRechercheList=new HashMap<String, Object>();
        page = "List";
        map.put("page", page);
        map.put("current", current);
        map.put("items", items);
        map.put("mapRechercheList",mapRechercheList);
        return "List_Imagesclient";
    }

    public String prepareView() {
        current = (Imagesclient) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Imagesclient();
        current.setDatecreation(new Date());
        selectedItemIndex = -1;
       FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();   
        map.put("page", page);
        map.put("current", current);
        return "Create_Imagesclient";
    }

    public String create() {
        try {
              Long l = new Long(1);
        try
        {
         l = getFacade().findByParameterSingleResultCountsansparam("Select max(c.id) from Imagesclient c");
         if (l==null)
           {
            l = new Long(1);
           }
        }
        
        catch(Exception e)
        {
          
            e.printStackTrace();
        }
            current.setId(l+1);
            current.setDatecreation(new Date());
            
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Transaction reussi");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Transaction echouee");
            return null;
        }
    }

    public String prepareEdit() {
        current = (Imagesclient) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
       page = "Edit";
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("current", current);
        return "Edit_Imagesclient";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Transaction reussi");
            return prepareList();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Transaction echouee");
            return null;
        }
    }

    public String destroy() {
        current = (Imagesclient) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreateModel();
        return "List";
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
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage("Transaction reussi");
        } catch (Exception e) {
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


    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = Imagesclient.class)
    public static class ImagesclientControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
       try{     ImagesclientController controller = (ImagesclientController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "imagesclientController");
            return controller.ejbFacade.find(getKey(value));}catch(Exception e){return null;}
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Imagesclient) {
                Imagesclient o = (Imagesclient) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ImagesclientController.class.getName());
            }
        }
    }
      public List<Client> autocompleteClient(String code) {
        List<Client> result = new ArrayList<Client>();
        try {
            result = (List<Client>) requeteClient(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Client>();
            return result;
        }
    }

    public List<Client> requeteClient(String code) {
       String q = "Select b from Client b where b.code like :code or b.nom like :code";
        List<Client> l = this.ejbclient.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }
    
    public void handleFileUpload(FileUploadEvent event) {
        System.out.println("handleFileUpload");
        try {
            
              InputStream input = event.getFile().getInputstream();
          //  String nomImage = this.nomImage(event.getFile().getFileName());
            String nomImage="photo"+current.getClient().getNom()+current.getDatecreation().getDay()+current.getDatecreation().getMonth()+current.getDatecreation().getYear();
              File directory = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/pages/images/imagespatients"));
            directory.mkdirs();
         //   System.out.println("nomImage "+nomImage);
            OutputStream output = new FileOutputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/pages/images/imagespatients") + "/" + nomImage + ".jpg"));
            int data = input.read();
            while (data != -1) {
                output.write(data);
                data = input.read();
            }
            input.close();
            output.close();
            JsfUtil.addSuccessMessage("Le Fichier :" + event.getFile().getFileName() + " est telecharge.");
            this.getSelected().setPhoto("/pages/images/imagespatients/" + nomImage + ".jpg");
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("echec " + event.getFile().getFileName() + " n'est pas telecharger.");
        }
    }
  protected static Semaphore semaphoreImage = new Semaphore(1, true);
     public String nomImage(String s) {
        try {
            String s3 = "";
            DateFormat f = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
            semaphoreImage.acquire();

            Date date = new Date();
            s3 = current.getPhoto();
            System.out.println("s3 "+s3);
            semaphoreImage.release();
            return s3;
        } catch (Exception r) {
            r.printStackTrace();
            try {
                semaphoreImage.release();
            } catch (Exception releaseExcp) {
                System.out.println("releaseExcp");
                releaseExcp.printStackTrace();
            }
        }
        return "";
    }
}
