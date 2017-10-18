package JsfClasses;

import entities.Client;
import JsfClasses.util.JsfUtil;
import JsfClasses.util.PaginationHelper;
import SessionBeans.ClasseClientFacade;
import SessionBeans.ClientFacade;

import SessionBeans.LaboratoireFacade;
import SessionBeans.NationaliteFacade;
import SessionBeans.ProfessionFacade;
import SessionBeans.SexeFacade;
import SessionBeans.TypeclientFacade;
import SessionBeans.VilleFacade;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Driver;
import entities.ClasseClient;
import entities.Laboratoire;
import entities.Nationalite;
import entities.Profession;
import entities.Sexe;
import entities.Typeclient;
import entities.Ville;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import org.primefaces.event.FileUploadEvent;

@ManagedBean(name = "clientController")
@ViewScoped
public class ClientController implements Serializable {

    private Client current;
    private DataModel items = null;
    @EJB
    private SessionBeans.ClientFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
   private String page;
   private String nompage;
   private String typepage;
      private Integer pageDestination;
          private Driver monDriver;
    private Connection connection1;
    private int Xligne;
       private String clientFiltrage;
       public String optionOrient;
        private Map<String,Object>mapRechercheList;
         @EJB
    private VilleFacade ejbville;
         @EJB
    private SexeFacade ejbsexe;
         @EJB
    private ProfessionFacade ejbprofession;
         @EJB
    private NationaliteFacade ejbnationalite;
         @EJB
    private LaboratoireFacade ejblaboratoire;
         @EJB
    private ClasseClientFacade ejbclasseclient;
          @EJB
    private TypeclientFacade ejbtypeclient;
          
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
 public String getClientFiltrage() {
        return clientFiltrage;
    }

    public void setClientFiltrage(String clientFiltrage) {
        this.clientFiltrage = clientFiltrage;
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
           typepage = (String) map.get("typepage");
        } catch (Exception e) {
            
        }
           try {
           nompage = (String) map.get("nompage");
        } catch (Exception e) {
            
        }
        try {
            current = (Client) map.get("current");
        } catch (Exception e) {
            current = null;
        }
                try{
        items=(DataModel) map.get("items");
        }catch(Exception e){}
                
                try {
            optionOrient = (String) map.get("optionOrient");
        } catch (Exception e) {
        }
                 try{
        mapRechercheList= (Map<String, Object>) map.get("mapRechercheList");
        }catch(Exception e){
        mapRechercheList=null;
        }
           try {
            clientFiltrage = (String) map.get("clientFiltrage");
        } catch (Exception e) {
        }
        if ((page != null)) {
            if ((page.equalsIgnoreCase("CreateClient"))) {
                System.out.println("init CreateClient");
                initPrepareCreateClient();
            }
              if ((page.equalsIgnoreCase("CreateFournisseur"))) {
                   System.out.println("init CreateFournisseur");
                initPrepareCreateFournisseur();
            }
                if ((page.equalsIgnoreCase("CreateTiers"))) {
                     System.out.println("init CreateTiers");
                initPrepareCreateTiers();
            }
          
            
            if ((page.equalsIgnoreCase("ListClient"))) {
              initRechercheListClient();
            }
              if ((page.equalsIgnoreCase("ListFournisseur"))) {
                   initRechercheListFournisseur();
            }
                if ((page.equalsIgnoreCase("ListTiers"))) {
                    initRechercheListTiers();
            }
          
        
            if ((page.equalsIgnoreCase("Edit")) && (current != null)) {
                initPrepareEdit();
            }
        }
    }
     
     public void initPrepareCreateClient() {
          current = new Client();
          current.setDatecreation(new Date());
          current.setDatenaissance(new Date());
          current.setDernieredatemodif(new Date());
           current.setPlafond(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
          nompage="Patient";
         Typeclient cli=new Typeclient();
           try{
                cli =ejbtypeclient.findByParameterSingleResult("SELECT t FROM Typeclient t WHERE t.libelle = :lib","lib","Patient" );
       
         
        }catch(Exception e)
        
        {e.printStackTrace();}
         System.out.println("cli "+cli.getId());
           current.setTypeclient(new Typeclient(cli.getId()));        
          selectedItemIndex = -1;
    }
     
      public void initPrepareCreateFournisseur() {
          current = new Client();
           nompage="Fournisseur";
          current.setDatecreation(new Date());
          current.setDatenaissance(new Date());
          current.setDernieredatemodif(new Date());
           current.setPlafond(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
          Typeclient cli=new Typeclient();
           try{
                cli =ejbtypeclient.findByParameterSingleResult("SELECT t FROM Typeclient t WHERE t.libelle = :lib","lib","Fournisseur" );
       
         
        }catch(Exception e)
        
        {e.printStackTrace();}
         System.out.println("cli "+cli.getId());
           current.setTypeclient(new Typeclient(cli.getId()));         
          selectedItemIndex = -1;
    }
       public void initPrepareCreateTiers() {
          current = new Client();
           nompage="Divers";
          current.setDatecreation(new Date());
          current.setDatenaissance(new Date());
          current.setDernieredatemodif(new Date());
           current.setPlafond(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
           Typeclient cli=new Typeclient();
           try{
                cli =ejbtypeclient.findByParameterSingleResult("SELECT t FROM Typeclient t WHERE t.libelle = :lib","lib","Divers" );
       
         
        }catch(Exception e)
        
        {e.printStackTrace();}
         System.out.println("cli "+cli.getId());
           current.setTypeclient(new Typeclient(cli.getId()));          
          selectedItemIndex = -1;
    }
      public void initRechercheListClient() {
        nompage="Patient";
          recreateModel();
        
    }
        public void initRechercheListFournisseur() {
        nompage="Fournisseur";
            recreateModel();
        
    }
          public void initRechercheListTiers() {
         nompage="Divers";
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

    public String getTypepage() {
        return typepage;
    }

    public void setTypepage(String typepage) {
        this.typepage = typepage;
    }

    public String getNompage() {
        return nompage;
    }

    public void setNompage(String nompage) {
        this.nompage = nompage;
    }
    
    public ClientController() {
         
         
        FacesContext fc = FacesContext.getCurrentInstance();
        ejbville = (VilleFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "VilleJpa");
        ejbsexe = (SexeFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "SexeJpa");
        ejbprofession = (ProfessionFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "ProfessionJpa");
        ejbnationalite = (NationaliteFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "NationaliteJpa");
        ejblaboratoire = (LaboratoireFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "LaboratoireJpa");
ejbclasseclient=(ClasseClientFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "ClasseclientJpa");
ejbtypeclient=(TypeclientFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "TypeclientJpa");


    }

    public Client getSelected() {
        if (current == null) {
            current = new Client();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ClientFacade getFacade() {
        return ejbFacade;
    }

      public PaginationHelper getPagination() {
          System.out.println("getPagination() "+nompage);
           Typeclient cli=new Typeclient();
           try{
                cli =ejbtypeclient.findByParameterSingleResult("SELECT t FROM Typeclient t WHERE t.libelle = :lib","lib",nompage );
       
         
        }catch(Exception e)
        
        {e.printStackTrace();}
        if (pagination == null) {
            List<String>s=new ArrayList<String>();
            List<Object>o=new ArrayList<Object>();
             s.add("typeclient");
            o.add(cli.getId());
          

            pagination = new PaginationHelper(10,"Select o from Client o where o.typeclient.id=:typeclient",s,o) {

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
        String requette="Select o from Client o where ";
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
    

    public String prepareList(String parametre) {
         recreateModel();
            String result="List_client";
    //    parametre=typepage;
        page = parametre;
        System.out.println("prepare list page: "+page);
     
         FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();   
        this.mapRechercheList=new HashMap<String, Object>();
       // page = "List";
 
         if ((page != null)) {
            if ((page.equalsIgnoreCase("ListClient"))) {
               result= "List_client";
                  nompage="Patient";
            }
              if ((page.equalsIgnoreCase("ListFournisseur"))) {
                  result= "List_fournisseur"; 
                     nompage="Fournisseur";
            }
                if ((page.equalsIgnoreCase("ListTiers"))) {
                     result= "List_tiers";
                        nompage="Divers";
            }
          
        }
         else
         {
        result= "Create_client";
         }
         System.out.println("result : "+result);
            System.out.println("prepare list nompage : "+nompage);
                map.put("page", page);
        map.put("current", current);
        map.put("items", items);
        map.put("mapRechercheList",mapRechercheList);
         map.put("nompage", nompage);
        map.put("typepage", typepage);
         map.put("optionOrient", optionOrient);
            map.put("clientFiltrage", clientFiltrage); 
         return result;
        
    }

    public String getOptionOrient() {
        return optionOrient;
    }

    public void setOptionOrient(String optionOrient) {
        this.optionOrient = optionOrient;
    }
    
    
     public String prepareList() {
         recreateModel();
            String result="List_client";
    //    parametre=typepage;
       
         FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();   
        this.mapRechercheList=new HashMap<String, Object>();
       // page = "List";
        map.put("page", page);
        map.put("current", current);
        map.put("items", items);
        map.put("mapRechercheList",mapRechercheList);
         map.put("nompage", nompage);
         map.put("clientFiltrage", clientFiltrage); 
        map.put("typepage", typepage);
         if ((page != null)) {
           if ((page.equalsIgnoreCase("CreateClient"))) {
                   result= "List_client";
            }
              if ((page.equalsIgnoreCase("CreateFournisseur"))) {
                  result= "List_fournisseur"; 
            }
                if ((page.equalsIgnoreCase("CreateTiers"))) {
                     result= "List_tiers";
            }
          
        }
         else
         {
        result= "Create_client";
         }
         System.out.println("result : "+result);
         return result;
        
    }

    public String prepareView() {
        current = (Client) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        String result="";
    //    parametre=typepage;
      //  page = parametre;
         
        System.out.println("prepare create type page : "+page);
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();   
        map.put("page", page);
        map.put("current", current);
        map.put("nompage", nompage);
        map.put("typepage", typepage);
         if ((page != null)) {
            if ((page.equalsIgnoreCase("CreateClient"))) {
               result= "Create_client";
            }
              if ((page.equalsIgnoreCase("CreateFournisseur"))) {
                  result= "Create_fournisseur"; 
            }
                if ((page.equalsIgnoreCase("CreateTiers"))) {
                     result= "Create_tiers";
            }
                  if ((page.equalsIgnoreCase("ListClient"))) {
               result= "Create_client";
            }
              if ((page.equalsIgnoreCase("ListFournisseur"))) {
                  result= "Create_fournisseur"; 
            }
                if ((page.equalsIgnoreCase("ListTiers"))) {
                     result= "Create_tiers";
            }
                
          
        }
         else
         {
        result= "Create_client";
         }
         return result;
    }
    
     public String prepareCreate(String parametre) {
        String result="";
    //    parametre=typepage;
        page = parametre;
        System.out.println("prepare create type page : "+page);
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();   
        map.put("page", page);
        map.put("current", current);
        map.put("nompage", nompage);
        map.put("typepage", typepage);
         if ((page != null)) {
            if ((page.equalsIgnoreCase("CreateClient"))) {
               result= "Create_client";
            }
              if ((page.equalsIgnoreCase("CreateFournisseur"))) {
                  result= "Create_fournisseur"; 
            }
                if ((page.equalsIgnoreCase("CreateTiers"))) {
                     result= "Create_tiers";
            }
          
        }
         else
         {
        result= "Create_client";
         }
         return result;
    }
     
       public String prepareCreateFournisseur() {
        String result="";
    //    parametre=typepage;
        page = "CreateFournisseur";
        System.out.println("prepare create type page : "+page);
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();   
        map.put("page", page);
        map.put("current", current);
        map.put("nompage", nompage);
        map.put("typepage", typepage);
        result= "Create_fournisseur";
         
         return result;
    }
       
          public String prepareCreatePatient() {
        String result="";
    //    parametre=typepage;
        page = "CreatePatient";
        System.out.println("prepare create type page : "+page);
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();   
        map.put("page", page);
        map.put("current", current);
        map.put("nompage", nompage);
        map.put("typepage", typepage);
        result= "Create_client";
         
         return result;
    }
     
         public String prepareCreateTiers() {
        String result="";
    //    parametre=typepage;
        page = "CreateTiers";
        System.out.println("prepare create type page : "+page);
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();   
        map.put("page", page);
        map.put("current", current);
        map.put("nompage", nompage);
        map.put("typepage", typepage);    
        result= "Create_tiers";
         
         return result;
    }
     

    public String create() {
        try {
             Long l = new Long(1);
        try
        {
         l = getFacade().findByParameterSingleResultCountsansparam("Select max(c.id) from Client c");
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
         //   current.setTypeclient(new Typeclient(new Long(2)));  
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Transaction reussi");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Transaction echouee");
            return null;
        }
    }

    public String prepareEdit() {
        current = (Client) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        if(current.getTypeclient().getLibelle().equalsIgnoreCase("Patient"))
        {
           page = "ListClient";
        }
        if(current.getTypeclient().getLibelle().equalsIgnoreCase("Fournisseur"))
        {
           page = "ListFournisseur";
        }
         if(current.getTypeclient().getLibelle().equalsIgnoreCase("Divers"))
        {
           page = "ListTiers";
        }
         
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("current", current);
        return "Edit_client";
    }

    public String update() {
        try {
            current.setDernieredatemodif(new Date());
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("Transaction reussi");
            return prepareList(page);
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Transaction echouee");
            return null;
        }
    }

    public String destroy() {
        current = (Client) getItems().getRowData();
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

    @FacesConverter(forClass = Client.class)
    public static class ClientControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
           try{ ClientController controller = (ClientController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "clientController");
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
            if (object instanceof Client) {
                Client o = (Client) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ClientController.class.getName());
            }
        }
    }
     public List<Ville> autocompleteVille(String code) {
        List<Ville> result = new ArrayList<Ville>();
        try {
            result = (List<Ville>) requeteville(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Ville>();
            return result;
        }
    }

    public List<Ville> requeteville(String code) {
        String q = "Select b from Ville b where b.code like :code or b.libelle like :code";
        List<Ville> l = this.ejbville.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }
     public List<Nationalite> autocompleteNationalite(String code) {
        List<Nationalite> result = new ArrayList<Nationalite>();
        try {
            result = (List<Nationalite>) requeteNationalite(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Nationalite>();
            return result;
        }
    }

    public List<Nationalite> requeteNationalite(String code) {
        String q = "Select b from Nationalite b where  b.libelle like :code";
        List<Nationalite> l = this.ejbnationalite.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }
     public List<Sexe> autocompleteSexe(String code) {
        List<Sexe> result = new ArrayList<Sexe>();
        try {
            result = (List<Sexe>) requeteSexe(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Sexe>();
            return result;
        }
    }

    public List<Sexe> requeteSexe(String code) {
        String q = "Select b from Sexe b where  b.libelle like :code";
        List<Sexe> l = this.ejbsexe.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }
      public List<Laboratoire> autocompleteLaboratoire(String code) {
        List<Laboratoire> result = new ArrayList<Laboratoire>();
        try {
            result = (List<Laboratoire>) requeteLaboratoire(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Laboratoire>();
            return result;
        }
    }

    public List<Laboratoire> requeteLaboratoire(String code) {
        String q = "Select b from Laboratoire b where  b.libelle like :code";
        List<Laboratoire> l = this.ejblaboratoire.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }
     public List<Profession> autocompleteProfession(String code) {
        List<Profession> result = new ArrayList<Profession>();
        try {
            result = (List<Profession>) requeteProfession(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Profession>();
            return result;
        }
    }

    public List<Profession> requeteProfession(String code) {
        String q = "Select b from Profession b where b.code like :code or b.libelle like :code";
        List<Profession> l = this.ejbprofession.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }
         public List<ClasseClient> autocompleteClasseClient(String code) {
        List<ClasseClient> result = new ArrayList<ClasseClient>();
        try {
            result = (List<ClasseClient>) requeteClasseClient(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<ClasseClient>();
            return result;
        }
    }

    public List<ClasseClient> requeteClasseClient(String code) {
        String q = "Select b from ClasseClient b where b.code like :code or b.libelle like :code";
        List<ClasseClient> l = this.ejbclasseclient.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }
    
      public void orientation(JasperDesign jasper, String ORIENTATION, JRDesignFrame jrFame) {
        if (ORIENTATION.equals("LANDSCAPE")) {
            jasper.setOrientation(OrientationEnum.LANDSCAPE);
            jasper.setPageWidth(842);
            jasper.setPageHeight(595);
        }
    }
       public void etablirconnection() throws SQLException {
        monDriver = new com.mysql.jdbc.Driver();
        StringTokenizer getUrl = new StringTokenizer(this.ejbFacade.urlCourante(), "**");
        String url = getUrl.nextToken();
        String nomBaseDeDonnes = url.substring(url.lastIndexOf("/") + 1);
        String login = getUrl.nextToken();
        String password = getUrl.nextToken();
        DriverManager.registerDriver(monDriver);
        System.out.println("url " + url);
        //cast???
        connection1 = (Connection) DriverManager.getConnection(url, login, password);
    }
       public void impressionPdfClient() throws JRException, IOException {
       
            try {
               Xligne=0;
                optionOrient = "1";
                etablirconnection();
                JRDesignBand bandTiltle = new JRDesignBand();
                bandTiltle.setHeight(110);
                DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date d22 = new Date();
                String ddd = dateFormat3.format(d22);
                JasperDesign jasperDesign = new JasperDesign();
                JRDesignFrame jrFame = new JRDesignFrame();
                jasperDesign.setName("ExtraitClient");
                jasperDesign.setLeftMargin(10);
                jasperDesign.setRightMargin(10);
                jasperDesign.setTopMargin(10);
                jasperDesign.setBottomMargin(10);
                jrFame.setBackcolor(Color.WHITE);
                jrFame.setForecolor(Color.getHSBColor(51, 51, 255));
                if (optionOrient.equals("2")) {
                    orientation(jasperDesign, "LANDSCAPE", jrFame);
                } else {
                    orientation(jasperDesign, "LAND", jrFame);
                }

                jrFame.setHeight(122);
                jrFame.setWidth(842);
                jrFame.getLineBox().setLeftPadding(0);
                jrFame.getLineBox().setTopPadding(0);
                jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

                JRDesignStaticText libelleTitre = new JRDesignStaticText();
                libelleTitre.setWidth(360);
                libelleTitre.setHeight(25);
                libelleTitre.setX(200);
                libelleTitre.setY(30);
                libelleTitre.setText("Liste Des "+nompage);
                libelleTitre.setFontSize(16);
                libelleTitre.setForecolor(Color.BLUE);
                jrFame.addElement(libelleTitre);
                JRDesignStaticText libelleDate = new JRDesignStaticText();
                libelleDate.setWidth(250);
                libelleDate.setHeight(15);

                libelleDate.setFontSize(10);
                libelleDate.setForecolor(Color.BLACK);
                libelleDate.setX(200);
                libelleDate.setY(55);
                jrFame.addElement(libelleDate);
                bandTiltle.setHeight(122);

                /**************************************************/
                JRDesignField fieldSTE = new JRDesignField();
                fieldSTE.setName("cabinet_libelle");
                fieldSTE.setValueClass(java.lang.String.class);
                jasperDesign.addField(fieldSTE);

                JRDesignTextField textFieldSTE = new JRDesignTextField();
                textFieldSTE.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                textFieldSTE.setStretchWithOverflow(true);
                textFieldSTE.setFontSize(10);
                textFieldSTE.setWidth(200);
                textFieldSTE.setX(0);
                textFieldSTE.setY(0);
                JRDesignExpression expressionSTE = new JRDesignExpression();
                expressionSTE.setValueClass(java.lang.String.class);
                expressionSTE.setText("$F{cabinet_libelle}");
                textFieldSTE.setExpression(expressionSTE);

                jrFame.addElement(textFieldSTE);

                /*******************************************************************/
                JRDesignField fieldAdresse = new JRDesignField();
                fieldAdresse.setName("cabinet_adresse");
                fieldAdresse.setValueClass(java.lang.String.class);
                jasperDesign.addField(fieldAdresse);

                JRDesignTextField textFieldAdresse = new JRDesignTextField();
                textFieldAdresse.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                textFieldAdresse.setStretchWithOverflow(true);
                textFieldAdresse.setFontSize(10);
                textFieldAdresse.setWidth(400);
                textFieldAdresse.setX(0);
                textFieldAdresse.setY(15);
                JRDesignExpression expressionAdresse = new JRDesignExpression();
                expressionAdresse.setValueClass(java.lang.String.class);
                expressionAdresse.setText("$F{cabinet_adresse}");
                textFieldAdresse.setExpression(expressionAdresse);

                jrFame.addElement(textFieldAdresse);

                /*************************************************************/
                JRDesignStaticText libelleTEL = new JRDesignStaticText();
                libelleTEL.setWidth(50);
                libelleTEL.setHeight(20);
                libelleTEL.setX(0);
                libelleTEL.setY(30);
                libelleTEL.setText("Tel :");
                libelleTEL.setFontSize(10);
                libelleTEL.setForecolor(Color.BLACK);
                jrFame.addElement(libelleTEL);


                JRDesignField fieldTel = new JRDesignField();
                fieldTel.setName("cabinet_tel");
                fieldTel.setValueClass(java.lang.String.class);
                jasperDesign.addField(fieldTel);



                JRDesignTextField textFieldTel = new JRDesignTextField();
                textFieldTel.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                textFieldTel.setStretchWithOverflow(true);
                textFieldTel.setFontSize(10);
                textFieldTel.setWidth(150);
                textFieldTel.setX(25);
                textFieldTel.setY(30);
                JRDesignExpression expressionTel = new JRDesignExpression();
                expressionTel.setValueClass(java.lang.String.class);
                expressionTel.setText("$F{cabinet_tel}");
                textFieldTel.setExpression(expressionTel);

                jrFame.addElement(textFieldTel);
                /*******************************************************/
                /*********************************************************/
                JRDesignStaticText libelleFax = new JRDesignStaticText();
                libelleFax.setWidth(150);
                libelleFax.setHeight(20);
                libelleFax.setX(0);
                libelleFax.setY(45);
                libelleFax.setText("Fax :");
                libelleFax.setFontSize(10);
                libelleFax.setForecolor(Color.BLACK);
                jrFame.addElement(libelleFax);

                JRDesignField fieldFax = new JRDesignField();
                fieldFax.setName("cabinet_fax");
                fieldFax.setValueClass(java.lang.String.class);
                jasperDesign.addField(fieldFax);

                JRDesignTextField textFieldFax = new JRDesignTextField();
                textFieldFax.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                textFieldFax.setStretchWithOverflow(true);
                textFieldFax.setFontSize(10);
                textFieldFax.setWidth(100);
                textFieldFax.setX(25);
                textFieldFax.setY(45);
                JRDesignExpression expressionFax = new JRDesignExpression();
                expressionFax.setValueClass(java.lang.String.class);
                expressionFax.setText("$F{cabinet_fax}");
                textFieldFax.setExpression(expressionFax);

                jrFame.addElement(textFieldFax);

                bandTiltle.addElement(jrFame);
                jasperDesign.setTitle(bandTiltle);

                //2eme band
                JRDesignBand bandBlanc = new JRDesignBand();
                bandBlanc.setHeight(200);
                JRDesignStaticText libellePgeB = new JRDesignStaticText();
                libellePgeB.setWidth(50);
                libellePgeB.setHeight(20);
                libellePgeB.setX(0);
                libellePgeB.setY(30);
                libellePgeB.setText("Tel:");
                libellePgeB.setFontSize(12);
                libellePgeB.setForecolor(Color.BLACK);
                bandBlanc.addElement(libellePgeB);
                jasperDesign.setNoData(bandBlanc);

                //3eme band bandsomme

                //    Xligne = 0;

//                JRDesignStaticText libelleUtilisateur = new JRDesignStaticText();
//                libelleUtilisateur.setWidth(160);
//                libelleUtilisateur.setHeight(15);
//                if (utilisateur == null || utilisateur.equals("")) {
//                    libelleUtilisateur.setText("");
//
//                } else {
//
//                    libelleUtilisateur.setText("Utilisateur : " + utilisateur.getLogin());
//
//                }
//
//                libelleUtilisateur.setFontSize(10);
//                libelleUtilisateur.setForecolor(Color.BLACK);
//                libelleUtilisateur.setX(10);
//                libelleUtilisateur.setY(95);
//                jrFame.addElement(libelleUtilisateur);

//                JRDesignStaticText libelleSociete = new JRDesignStaticText();
//                libelleSociete.setWidth(160);
//                libelleSociete.setHeight(15);
//                if (societe == null || societe.equals("")) {
//                    libelleSociete.setText("");
//
//                } else {
//
//                    libelleSociete.setText("Societe : " + societe.getLibelleSociete());
//
//                }
//
//                libelleSociete.setFontSize(10);
//                libelleSociete.setForecolor(Color.BLACK);
//                libelleSociete.setX(10);
//                libelleSociete.setY(106);
//                jrFame.addElement(libelleSociete);

                
                System.out.println("type client "+page);
                
                 Typeclient cli=new Typeclient();
           try{
                cli =ejbtypeclient.findByParameterSingleResult("SELECT t FROM Typeclient t WHERE t.libelle = :lib","lib",nompage );
       
         
        }catch(Exception e)
        
        {e.printStackTrace();}
         System.out.println("cli "+cli.getId()+"  designation "+cli.getLibelle());
        
                JRDesignQuery query = new JRDesignQuery();           
                query.setText("SELECT cabinet.`id` AS cabinet_id,cabinet.`code` AS cabinet_code,cabinet.`libelle` AS cabinet_libelle,cabinet.`adresse` AS cabinet_adresse,cabinet.`fax` AS cabinet_fax,cabinet.`tel` AS cabinet_tel,cabinet.`matricule` AS cabinet_matricule,cabinet.`ville` AS cabinet_ville,cabinet.`matriculefiscale` AS cabinet_matriculefiscale,cabinet.`codepostal` AS cabinet_codepostal,client.`id` AS client_id,client.`code` AS client_code,client.`nom` AS client_nom,client.`ville` AS client_ville,client.`cin` AS client_cin,client.`numerofiche` AS client_numerofiche,client.`adresse` AS client_adresse,client.`codepostale` AS client_codepostale,client.`tel` AS client_tel,client.`fax` AS client_fax,client.`mail` AS client_mail,client.`por` AS client_por,client.`datenaissance` AS client_datenaissance,client.`datecreation` AS client_datecreation,client.`dernieredatemodif` AS client_dernieredatemodif,client.`photo` AS client_photo,client.`classe_client` AS client_classe_client,client.`nationalite` AS client_nationalite,client.`sexe` AS client_sexe,client.`profession` AS client_profession,client.`laboratoire` AS client_laboratoire,client.`plafond` AS client_plafond,client.`typeclient` AS client_typeclient,ville.`id` AS ville_id,ville.`code` AS ville_code,ville.`libelle` AS ville_libelle FROM `client` client  LEFT JOIN `ville` ville ON client.`ville`  =ville.`id` ,`cabinet` cabinet where client.`typeclient` = " + cli.getId() + "");
                System.out.println("Query" + query.getText());
                jasperDesign.setQuery(query);
                jasperDesign.setName("ListeClientsrapport");
                jasperDesign.setLeftMargin(10);
                jasperDesign.setRightMargin(10);
                jasperDesign.setTopMargin(10);
                jasperDesign.setBottomMargin(10);
                //   jrFame.setBackcolor(Color.WHITE);
                //   jrFame.setForecolor(Color.getHSBColor(51, 51, 255));



                /**************************************************/
                JRDesignBand bandFooter = new JRDesignBand();
                bandFooter.setHeight(15);

                JRDesignStaticText textFieldDate = new JRDesignStaticText();
                textFieldDate.setFontSize(10);
                textFieldDate.setX(0);
                textFieldDate.setY(0);
                textFieldDate.setWidth(100);
                textFieldDate.setHeight(15);
                textFieldDate.getLineBox().setLeftPadding(2);
                textFieldDate.getLineBox().setRightPadding(2);
                textFieldDate.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                textFieldDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
                bandFooter.addElement(textFieldDate);
                JRDesignTextField textField1 = new JRDesignTextField();
                textField1.setFontSize(10);
                textField1.setX(100);
                if (optionOrient.equals("2")) {
                    textField1.setX(300);
                }
                textField1.setY(0);
                textField1.setWidth(150);
                textField1.setHeight(15);
                textField1.getLineBox().setLeftPadding(2);
                textField1.getLineBox().setRightPadding(2);
                textField1.setStretchWithOverflow(false);
                textField1.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                JRDesignExpression expression1 = new JRDesignExpression();
                expression1.setValueClass(java.lang.String.class);
                expression1.setText("$V{PAGE_NUMBER}.toString()+'/'");
                textField1.setExpression(expression1);
                textField1.setEvaluationTime(EvaluationTimeEnum.NOW);
                bandFooter.addElement(textField1);
                JRDesignTextField textField2 = new JRDesignTextField();
                textField2.setFontSize(10);
                textField2.setX(250);
                if (optionOrient.equals("2")) {
                    textField2.setX(450);
                }
                textField2.setY(0);
                textField2.setWidth(150);
                textField2.setHeight(15);
                textField2.getLineBox().setLeftPadding(2);
                textField2.getLineBox().setRightPadding(2);
                textField2.setStretchWithOverflow(false);
                textField2.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                JRDesignExpression expression2 = new JRDesignExpression();
                expression2.setValueClass(java.lang.String.class);
                expression2.setText("$V{PAGE_NUMBER}.toString()");
                textField2.setExpression(expression2);
                textField2.setEvaluationTime(EvaluationTimeEnum.REPORT);
                bandFooter.addElement(textField2);
                jasperDesign.setPageFooter(bandFooter);

                /*************************************************************/
                JRDesignBand bandHeader = new JRDesignBand();
                BigDecimal bandHeaderHeight = new BigDecimal(0).setScale(0, RoundingMode.DOWN);
                bandHeader.setHeight(bandHeaderHeight.intValue());
                JRDesignBand columnHeader = new JRDesignBand();
                columnHeader.setHeight(15);
                JRDesignBand bandSomme = new JRDesignBand();
                bandSomme.setHeight(259);


                {
                    JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "client_code", "(($F{client_code}!=null)&($F{client_code}.equals(\"\")==false))?$F{client_code}:Character.toString(' ')", libelleColonneCode, "CODE", 0, 60, bandSomme);
                    libelleColonneCode.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneCode);
                }


                {
                    JRDesignStaticText libelleColonneDesignation = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "client_nom", "(($F{client_nom}!=null)&($F{client_nom}.equals(\"\")==false))?$F{client_nom}:Character.toString(' ')", libelleColonneDesignation, "Nom", 0, 260, bandSomme);
                    libelleColonneDesignation.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneDesignation);
                }

                {
                    JRDesignStaticText libelleColonneAdresse = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "client_adresse", "(($F{client_adresse}!=null)&($F{client_adresse}.equals(\"\")==false))?$F{client_adresse}:Character.toString(' ')", libelleColonneAdresse, "Adresse", 0, 160, bandSomme);
                    libelleColonneAdresse.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneAdresse);
                }

                jasperDesign.setColumnHeader(columnHeader);
       //         jasperDesign.setDetail(bandHeader);
 ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandHeader);

                JRDesignBand bandNodata = new JRDesignBand();
                bandNodata.setHeight(200);
                JRDesignFrame jrFame1 = new JRDesignFrame();

                jrFame1.setBackcolor(Color.lightGray);



                JRDesignStaticText libelleDateSysnoData = new JRDesignStaticText();
                libelleDateSysnoData.setWidth(150);
                libelleDateSysnoData.setHeight(20);
                libelleDateSysnoData.setText("DATE : " + ddd);
                libelleDateSysnoData.setFontSize(10);
                libelleDateSysnoData.setForecolor(Color.BLACK);
                libelleDateSysnoData.setX(440);
                if (optionOrient.equals("2")) {
                    libelleDateSysnoData.setX(680);
                }


                libelleDateSysnoData.setY(10);
                jrFame1.addElement(libelleDateSysnoData);

                jrFame1.setBackcolor(Color.lightGray);
                jrFame1.setForecolor(Color.getHSBColor(51, 51, 255));
                System.out.println("orient" + optionOrient);


                jrFame1.setHeight(92);
                jrFame1.setWidth(842);
                jrFame1.getLineBox().setLeftPadding(0);
                jrFame1.getLineBox().setTopPadding(0);
                jrFame1.setMode(ModeEnum.getByValue(new Byte("1")));

                JRDesignStaticText libelleTitre1 = new JRDesignStaticText();
                libelleTitre1.setWidth(310);
                libelleTitre1.setHeight(31);
                libelleTitre1.setX(200);
                if (optionOrient.equals("2")) {
                    libelleTitre1.setX(300);
                }
                libelleTitre1.setY(50);
                String q2 = "Liste Des "+nompage;

                libelleTitre1.setText(q2);
                libelleTitre1.setFontSize(14);
                libelleTitre1.setForecolor(Color.BLUE);
                jrFame1.addElement(libelleTitre1);
                JRDesignStaticText textFieldEmptyData = new JRDesignStaticText();
                textFieldEmptyData.setFontSize(10);
                textFieldEmptyData.setX(150);
                if (optionOrient.equals("2")) {
                    textFieldEmptyData.setX(250);
                }
                textFieldEmptyData.setY(100);
                textFieldEmptyData.setWidth(390);
                textFieldEmptyData.setHeight(15);
                textFieldEmptyData.getLineBox().setLeftPadding(2);
                textFieldEmptyData.getLineBox().setRightPadding(2);

                textFieldEmptyData.setText("PAS DE DONNEES SATISFAISANT LES CRITERES SAISIS");
                textFieldEmptyData.setForecolor(Color.RED);
                bandNodata.addElement(textFieldEmptyData);

                bandNodata.addElement(jrFame1);

                jasperDesign.setNoData(bandNodata);

                jasperDesign.setWhenNoDataType(WhenNoDataTypeEnum.NO_DATA_SECTION);

                /*************************************************************************************************/
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                HashMap parameters = new HashMap();
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

                DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
                Date d = new Date();
                String dd = dateFormat15.format(d);
                String nomFichier = "RapportListe"+nompage+".pdf";
                byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
                FacesContext context = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
                response.addHeader("Content-disposition", "attachment;filename=" + nomFichier);
                response.setContentLength(bytes.length);
                response.getOutputStream().write(bytes);
                response.setContentType("application/pdf");
                context.responseComplete();

            } catch (SQLException ex) {
                ex.printStackTrace();
                Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);

            }
        
    }

    public void styleLibelleColumnHeader(JasperDesign jasperDesign, JRDesignBand bandHeader, String filename, String exp, JRDesignStaticText x, String nom, int setx, int width, JRDesignBand bandSomme) {
        /******************/
        /*********************/
        Byte byt = new Byte("1");
        x.setText(nom);
        x.setX(Xligne);
        x.setWidth(width);
        x.setBackcolor(Color.WHITE);
        x.setForecolor(Color.BLACK);
        x.setHeight(15);
        x.setMode(ModeEnum.getByValue(new Byte("1")));
        x.setY(0);
        x.setFontSize(new Integer(10));
        x.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        x.getLineBox().getLeftPen().setLineWidth(1);
x.getLineBox().getTopPen().setLineWidth(1);
x.getLineBox().getRightPen().setLineWidth(1);
x.getLineBox().getTopPen().setLineWidth(1);
x.getLineBox().getBottomPen().setLineWidth(1);  
        x.getLineBox().setLeftPadding(1);


        JRDesignTextField textField = new JRDesignTextField();
        JRDesignField field = new JRDesignField();
        if (nom.equals("DATE")) {
            field.setValueClass(java.util.Date.class);
        } else {
            field.setValueClass(java.lang.String.class);
        }

        field.setName(filename);


        try {
            jasperDesign.addField(field);
        } catch (JRException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        textField.setStretchWithOverflow(true);
        textField.setFontSize(10);
//        textField.setBorder(new Byte("1"));
//        textField.setBorderColor(Color.BLACK);
           textField.getLineBox().getLeftPen().setLineWidth(1);
textField.getLineBox().getTopPen().setLineWidth(1);
textField.getLineBox().getRightPen().setLineWidth(1);
textField.getLineBox().getTopPen().setLineWidth(1);
textField.getLineBox().getBottomPen().setLineWidth(1);  
        textField.getLineBox().setLeftPadding(1);
        textField.setWidth(width);
        textField.setX(Xligne);
        JRDesignExpression expression = new JRDesignExpression();
       
        if (nom.equals("DATE")) {
            expression.setValueClass(java.util.Date.class);
            textField.setPattern("dd/MM/yyyy");

            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        } else {
            expression.setValueClass(java.lang.String.class);
        }

        expression.setText(exp);
        textField.setExpression(expression);

        textField.setStretchWithOverflow(true);
        bandHeader.addElement(textField);
        Xligne = Xligne + width;
    }
        public String rechercheMultipleCriteres() {
        String requette88 = "Select o from Client o where o.typeclient.libelle='"+nompage+"' and ";
        List<String> n = new ArrayList<String>();
        List<Object> o = new ArrayList<Object>();
        String order = "";
       
    
     
        if (clientFiltrage != null) {
            if (this.clientFiltrage.isEmpty() == false) {
                requette88 = requette88 + "o.nom like :client and ";
                n.add("client");
                o.add("%" + clientFiltrage + "%");
             
            }
        }
        
     
        if (requette88.endsWith(" and ")) {
            requette88 = requette88.substring(0, requette88.lastIndexOf(" and "));
        }
        String orderByRequette = "";
        

        orderByRequette = "order by o.nom" + orderByRequette;
        requette88 = requette88 + " " + orderByRequette;
        requette88 = requette88.trim();
        if (requette88.endsWith(" order by")) {
            requette88 = requette88.substring(0, requette88.lastIndexOf(" order by"));
        }
        System.out.println ("requette88 : "+requette88);
        try {
            getPagination().setRequetteJpql(requette88);
            getPagination().setArrayNames(n);
            getPagination().setArrayValues(o);
            getPagination().setPage(0);
              List<Client> lop = this.ejbFacade.findByParameterMultipleCreteria(requette88, n, o);
            items = new ListDataModel(lop);
    //        items = null;
        } catch (Exception er) {
       //     er.printStackTrace();
        }
        return null;
    }
public void handleFileUpload(FileUploadEvent event) {
        System.out.println("handleFileUpload");
        try {
            
              InputStream input = event.getFile().getInputstream();
          //  String nomImage = this.nomImage(event.getFile().getFileName());
           String nomImage="photo"+current.getNom();
              File directory = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/pages/images/client"));
            directory.mkdirs();
         //   System.out.println("nomImage "+nomImage);
            OutputStream output = new FileOutputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/pages/images/client") + "/" + nomImage + ".jpg"));
            int data = input.read();
            while (data != -1) {
                output.write(data);
                data = input.read();
            }
            input.close();
            output.close();
            JsfUtil.addSuccessMessage("Le Fichier :" + event.getFile().getFileName() + " est telecharge.");
            this.getSelected().setPhoto("/pages/images/client/" + nomImage + ".jpg");
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
