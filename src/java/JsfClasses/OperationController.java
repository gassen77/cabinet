package JsfClasses;

import entities.Operation;
import JsfClasses.util.JsfUtil;
import JsfClasses.util.PaginationHelper;
import JsfClasses.util.RapportUtil;
import SessionBeans.ActeFacade;
import SessionBeans.BanqueFacade;
import SessionBeans.ClientFacade;
import SessionBeans.DentFacade;
import SessionBeans.DetailRapportComFacade;
import SessionBeans.DetailoperationFacade;
import SessionBeans.DetailordonanceFacade;
import SessionBeans.FamilleacteFacade;
import SessionBeans.LettrageComFacade;
import SessionBeans.MedicamentFacade;
import SessionBeans.ModeleRapportComFacade;
import SessionBeans.ModereglementFacade;
import SessionBeans.OperationFacade;
import SessionBeans.ProduitFacade;
import SessionBeans.TypeoperationFacade;
import SessionBeans.UserFacade;
import SessionBeans.ZoneRapportComFacade;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.mysql.jdbc.Driver;
import com.mysql.jdbc.Statement;
import entities.Acte;
import entities.Banque;
import entities.Client;
import entities.Dent;
import entities.DetailRapportCom;
import entities.Detailoperation;
import entities.DetailoperationPK;
import entities.Detailordonance;
import entities.DetailordonancePK;
import entities.Familleacte;
import entities.LettrageCom;
import entities.Medicament;
import entities.ModeleRapportCom;
import entities.Modereglement;
import entities.Produit;
import entities.Typeoperation;
import entities.User;
import entities.ZoneRapportCom;
import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
import javax.transaction.UserTransaction;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignImage;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PositionTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;

@ManagedBean(name = "operationController")
@ViewScoped
public class OperationController implements Serializable {

    private Operation current;
    private DataModel items = null;
    @EJB
    private SessionBeans.OperationFacade ejbFacade;
    @EJB
    private SessionBeans.UserFacade ejbUser;
    private DataModel listeDetails;
    private DataModel listeDetailsOrdonance;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String page;
    private String typeClient;
     private String nompage;
     private Familleacte familleacte;
     private Integer pageDestination;
    @EJB
    private ClientFacade ejbclient;
    @EJB
    private ModereglementFacade ejbmodereglement;
    @EJB
    private BanqueFacade ejbbanque;
    @EJB
    private TypeoperationFacade ejbtypeoperation;
    @EJB
    private ProduitFacade ejbProduit;
    @EJB
    private ActeFacade ejbActe;
    @EJB
    private DentFacade ejbDent;
    @EJB
    private DetailoperationFacade ejbdetailoperation;
      @EJB
    private DetailordonanceFacade ejbdetailordonance;
     @EJB
    private MedicamentFacade ejbmedicament;
     @EJB
    private LettrageComFacade jpaLettrageCom;
      @EJB
     private ModeleRapportComFacade ejbModele;
        @EJB
         private ZoneRapportComFacade ejbZone;
         @EJB
          private DetailRapportComFacade ejbDetailModel;
    private Map<String, Object> mapRechercheList;
        private Date dateDebut;
    private Date dateFin;
    private InputText inputTextReference;
    private InputText inputTextRecu;
    private InputText inputTextEmetteur;
    private InputText inputTextMontantOperation;
 //   private AutoComplete inputTextBanque;
    private Boolean inputTextEcheance;
    @Resource
    private UserTransaction utx = null;
    private DataTable dataTableProduit;
        private DataTable dataTableOrdonance;
       private Boolean visiteregle;
              private Driver monDriver;
    private Connection connection1;
    private int Xligne;
       public String optionOrient;
          @EJB
    private FamilleacteFacade ejbfamilleacte;
    private String clientFiltrage;
    private String modereglementFiltrage;
    private String banqueFiltrage;
    private BigDecimal montantoperationFiltrage;
    private BigDecimal numerooperationFiltrage;
    public Boolean getVisiteregle() {
        return visiteregle;
    }

    public void setVisiteregle(Boolean visiteregle) {
        this.visiteregle = visiteregle;
    }

    public Map<String, Object> getMapRechercheList() {
        return mapRechercheList;
    }

    public String getBanqueFiltrage() {
        return banqueFiltrage;
    }

    public void setBanqueFiltrage(String banqueFiltrage) {
        this.banqueFiltrage = banqueFiltrage;
    }

    public String getClientFiltrage() {
        return clientFiltrage;
    }

    public void setClientFiltrage(String clientFiltrage) {
        this.clientFiltrage = clientFiltrage;
    }

    public String getModereglementFiltrage() {
        return modereglementFiltrage;
    }

    public void setModereglementFiltrage(String modereglementFiltrage) {
        this.modereglementFiltrage = modereglementFiltrage;
    }

    public BigDecimal getMontantoperationFiltrage() {
        return montantoperationFiltrage;
    }

    public void setMontantoperationFiltrage(BigDecimal montantoperationFiltrage) {
        this.montantoperationFiltrage = montantoperationFiltrage;
    }

    public BigDecimal getNumerooperationFiltrage() {
        return numerooperationFiltrage;
    }

    public void setNumerooperationFiltrage(BigDecimal numerooperationFiltrage) {
        this.numerooperationFiltrage = numerooperationFiltrage;
    }

   

    public void setMapRechercheList(Map<String, Object> mapRechercheList) {
        this.mapRechercheList = mapRechercheList;
    }

    public DataTable getDataTableProduit() {
        return dataTableProduit;
    }
   public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }
    public void setDataTableProduit(DataTable dataTableProduit) {
        this.dataTableProduit = dataTableProduit;
    }

    public DataTable getDataTableOrdonance() {
        return dataTableOrdonance;
    }

    public void setDataTableOrdonance(DataTable dataTableOrdonance) {
        this.dataTableOrdonance = dataTableOrdonance;
    }

    public Integer getPageDestination() {
        return pageDestination;
    }

    public void setPageDestination(Integer pageDestination) {
        this.pageDestination = pageDestination;
    }

    public String getTypeClient() {
        return typeClient;
    }

    public void setTypeClient(String typeClient) {
        this.typeClient = typeClient;
    }

//    public OperationControllerConverter getOperationConverter() {
//        return operationConverter;
//    }
//
//    public void setOperationConverter(OperationControllerConverter operationConverter) {
//        this.operationConverter = operationConverter;
//    }
    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();

        try {
            page = (String) map.get("page");
        } catch (Exception e) {
        }
         try {
            typeClient = (String) map.get("typeClient");
        } catch (Exception e) {
        }
        
        try {
            current = (Operation) map.get("current");
        } catch (Exception e) {
            current = null;
        }
        try {
            items = (DataModel) map.get("items");
        } catch (Exception e) {
        }
  try {
           nompage = (String) map.get("nompage");
        } catch (Exception e) {
            
        }
         
        try {
            mapRechercheList = (Map<String, Object>) map.get("mapRechercheList");
        } catch (Exception e) {
            mapRechercheList = null;
        }
        try {
            listeDetails = (DataModel) map.get("listeDetails");
        } catch (Exception e) {
        }
          try {
            listeDetailsOrdonance = (DataModel) map.get("listeDetailsOrdonance");
        } catch (Exception e) {
        }
            try {
            optionOrient = (String) map.get("optionOrient");
        } catch (Exception e) {
        }
//        try {
//            inputTextBanque = (AutoComplete) map.get("inputTextBanque");
//        } catch (Exception e) {
//        }
        try {
            inputTextEmetteur = (InputText) map.get("inputTextEmetteur");
        } catch (Exception e) {
        }
        try {
            inputTextMontantOperation = (InputText) map.get("inputTextMontantOperation");
        } catch (Exception e) {
        }

        try {
            inputTextRecu = (InputText) map.get("inputTextRecu");
        } catch (Exception e) {
        }
        try {
            inputTextReference = (InputText) map.get("inputTextReference");
        } catch (Exception e) {
        }
         try {
            inputTextEcheance = (Boolean) map.get("inputTextEcheance");
        } catch (Exception e) {
        }
        try {
            dataTableProduit = (DataTable) map.get("dataTableProduit");
        } catch (Exception e) {
        }
        try {
            dataTableOrdonance = (DataTable) map.get("dataTableOrdonance");
        } catch (Exception e) {
        }
         try {
            visiteregle = (Boolean) map.get("visiteregle");
        } catch (Exception e) {
        }

           try {
            modele = (ModeleRapportCom) map.get("modele");
        } catch (Exception e) {
        }
            try {
            dateDebut = (Date) map.get("dateDebut");
        } catch (Exception e) {
        }
               try {
            dateFin = (Date) map.get("dateFin");
        } catch (Exception e) {
        }
                try {
            clientFiltrage = (String) map.get("clientFiltrage");
        } catch (Exception e) {
        }
                    try {
            modereglementFiltrage = (String) map.get("modereglementFiltrage");
        } catch (Exception e) {
        }
                        try {
            banqueFiltrage = (String) map.get("banqueFiltrage");
        } catch (Exception e) {
        }
                            try {
            montantoperationFiltrage = (BigDecimal) map.get("montantoperationFiltrage");
        } catch (Exception e) {
        }
                                 try {
            numerooperationFiltrage = (BigDecimal) map.get("numerooperationFiltrage");
        } catch (Exception e) {
        }
   
        if ((page != null)) {
            if ((page.equalsIgnoreCase("Create"))) {
                initPrepareCreate();
            }
            if ((page.equalsIgnoreCase("CreateDivers"))) {
                initPrepareCreateDivers();
            }
            if ((page.equalsIgnoreCase("CreateFournisseur"))) {
                initPrepareCreateFournisseur();
            }
             if ((page.equalsIgnoreCase("CreateConsultation"))) {
                initPrepareCreateConsultation();
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
        current = new Operation();
        current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
        current.setMontantcredite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
        current.setMontantdebite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
        current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
        current.setMontantoperation(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
        current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
        current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
        current.setDateSys(new Date());
        current.setDateoperation(new Date());
        selectedItemIndex = -1;
    }

    public void initPrepareCreateDivers() {
//         nompage="Depense Divers";
//        current = new Operation();
//        current.setBanque( new Banque());
//        current.setModereglement(new Modereglement());
//        current.setMontantoperation(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//        current.setDateSys(new Date());
//        current.setDateoperation(new Date());
//        current.setModereglement(new Modereglement());
//        current.getModereglement().setId(new Long(1));
//        current.getModereglement().setCode("1");
//        current.getModereglement().setLibelle("ESPECE");
//        selectedItemIndex = -1;
    }

    public void initPrepareCreateFournisseur() {
//          nompage="Depense Fournisseur";
//        current = new Operation();
//        current.setMontantoperation(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//        current.setDateSys(new Date());
//        current.setDateoperation(new Date());
//
//        inputTextEcheance=true;
//        selectedItemIndex = -1;
    }

    public Boolean getInputTextEcheance() {
        return inputTextEcheance;
    }

    public void setInputTextEcheance(Boolean inputTextEcheance) {
        this.inputTextEcheance = inputTextEcheance;
    }

    
  
    
    public void initPrepareCreateConsultation() {
//          nompage="visite Patient";
//        current = new Operation();
//        current.setDetail(false);
//        current.setMontantoperation(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//        current.setDateSys(new Date());
//        current.setDateoperation(new Date());
//        current.setDateconsultation(new Date());
//        visiteregle=false;
//        inputTextEcheance =true;
//        selectedItemIndex = -1;
    }
    
     public String getNompage() {
        return nompage;
    }

    public void setNompage(String nompage) {
        this.nompage = nompage;
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

    public OperationController() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ejbclient = (ClientFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "ClientJpa");
        ejbmodereglement = (ModereglementFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "ModereglementJpa");
        ejbbanque = (BanqueFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "banqueJpa");
        ejbtypeoperation = (TypeoperationFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "TypeoperationJpa");
        ejbUser = (UserFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "UserJpa");
        ejbProduit = (ProduitFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "produitJpa");
       ejbActe = (ActeFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "acteJpa");
        ejbDent= (DentFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "dentJpa");
       ejbdetailoperation = (DetailoperationFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "detailoperationJpa");
       ejbmedicament= (MedicamentFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "medicamentJpa");
   ejbdetailordonance= (DetailordonanceFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "detailordonanceJpa");
     jpaLettrageCom = (LettrageComFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "lettrageComJpa"); 
 ejbModele = (ModeleRapportComFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "modeleJpa");
        ejbZone = (ZoneRapportComFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "zoneJpa");
        ejbDetailModel = (DetailRapportComFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "detailModelJpa");
        ejbfamilleacte = (FamilleacteFacade) fc.getApplication().getELResolver().getValue(fc.getELContext(), null, "FamilleacteJpa");
  
    }

    public Operation getSelected() {
        if (current == null) {
            current = new Operation();
            selectedItemIndex = -1;
        }
        return current;
    }

    private OperationFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
              Typeoperation typeop=new Typeoperation();
           try{
               System.out.println("nom page : "+nompage);
                typeop =ejbtypeoperation.findByParameterSingleResult("SELECT t FROM Typeoperation t WHERE t.libelle = :lib","lib",nompage );
       
         
        }catch(Exception e)
        
        {e.printStackTrace();}
            List<String> s = new ArrayList<String>();
            List<Object> o = new ArrayList<Object>();
               if(dateDebut ==null)
             {
                 dateDebut= new Date();
                   this.dateDebut.setHours(0);
            this.dateDebut.setMinutes(0);
            this.dateDebut.setSeconds(0);
           
             }
              if(dateFin ==null)
             {
            
                  dateFin= new Date();
                   this.dateFin.setHours(23);
            this.dateFin.setMinutes(59);
            this.dateFin.setSeconds(59);
             }
               System.out.println("date debut "+dateDebut);
             System.out.println("date fin "+dateFin);
            s.add("dateSys1");
            s.add("dateSys2");
            o.add(dateDebut);
            o.add(dateFin);
            s.add("typeoperation");
            o.add(typeop.getId());
            pagination = new PaginationHelper(10, "Select o from Operation o where o.typeoperation.id=:typeoperation and o.dateoperation<=:dateSys2 and o.dateoperation>=:dateSys1 ", s, o) {

                @Override
                public int getItemsCount() {
                    return getFacade().countMultipleCritiria(getRequetteJpql(), getArrayNames(), getArrayValues());
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findByParameterMultipleCreteria(getRequetteJpql(), getArrayNames(), getArrayValues(), getPageFirstItem(), getPageSize()));
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
        getPagination().setPage(getPagination().totalPages() - 1);
        recreateModel();
        return null;
    }

    public String previous() {
        this.pageDestination = getPagination().getPage();
        return goPageDestination();
    }

    public String next() {
        this.pageDestination = getPagination().getPage();
        this.pageDestination = this.pageDestination + 2;
        return goPageDestination();
    }

    public String goPageDestination() {
        items = null;
        if (pageDestination != null) {
            if (pageDestination > 0) {
                if (pageDestination <= getPagination().totalPages()) {
                    getPagination().setPage(pageDestination.intValue() - 1);
                    recreateModel();
                }
            }
        }
        pageDestination = null;
        return null;
    }

    public void setMettreJourAttributeRecherche(String mettreJourAttributeRecherche) {
        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent component = UIViewRoot.getCurrentComponent(fc);
        UIPanel panel = (UIPanel) component;
        Iterator it = panel.getChildren().iterator();
        while (it.hasNext()) {
            UIComponent u = (UIComponent) it.next();
            if (u.getClass().equals(HtmlInputText.class)) {
                this.mapRechercheList.put(u.getId(), mettreJourAttributeRecherche);
            }
        }
    }

    public String getMettreJourAttributeRecherche() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HtmlInputText component = (HtmlInputText) UIViewRoot.getCurrentComponent(fc);
        return (String) this.mapRechercheList.get(component.getId());
    }

    
      public String rechercheMultipleCriteres() {
        String requette88 = "Select o from Operation o where o.typeoperation.id=:nomTop and ";
        List<String> n = new ArrayList<String>();
        List<Object> o = new ArrayList<Object>();
        String order = "";
        n.add("nomTop");
        String nomToop = this.page;
          System.out.println("nompage : "+nompage);
         Typeoperation typeop = new Typeoperation();
            try {
                typeop = ejbtypeoperation.findByParameterSingleResult("Select u from Typeoperation u where u.libelle=:libelle", "libelle", nompage);
                System.out.println("typeop " + typeop.getLibelle());
            } catch (Exception e) {
            }
//        if ("Fournisseur".equalsIgnoreCase(fournisseurClient)) {
//            nomToop = nomToop + " Fournisseur";
//        }
        o.add(typeop.getId());
          System.out.println("type operation : "+typeop.getLibelle());
        if ((dateDebut != null) & (dateFin != null)) {
            requette88 = requette88 + "o.dateoperation<=:date2 and o.dateoperation>=:date1 and ";
            this.dateDebut.setHours(0);
            this.dateDebut.setMinutes(0);
            this.dateDebut.setSeconds(0);
            this.dateFin.setHours(23);
            this.dateFin.setMinutes(59);
            this.dateFin.setSeconds(59);
            System.out.println("dateDebut "+dateDebut);
            System.out.println("dateFin "+dateFin);
            n.add("date1");
            n.add("date2");
            o.add(dateDebut);
            o.add(dateFin);
            order = order + " dateoperation";
        }
    
        if (this.modereglementFiltrage != null) {
            if (this.modereglementFiltrage.isEmpty() == false) {
                requette88 = requette88 + "o.modereglement.libelle like :modereglement and ";
                n.add("modereglement");
                o.add("%" + this.modereglementFiltrage + "%");
                
            }
        }
        if (clientFiltrage != null) {
            if (this.clientFiltrage.isEmpty() == false) {
                requette88 = requette88 + "o.client.nom like :client and ";
                n.add("client");
                o.add("%" + clientFiltrage + "%");
             
            }
        }
          if (banqueFiltrage != null) {
            if (this.banqueFiltrage.isEmpty() == false) {
                requette88 = requette88 + "o.banque.libelle like :banque and ";
                n.add("banque");
                o.add("%" + banqueFiltrage + "%");
             
            }
        }
        if ((numerooperationFiltrage != null)) {
            if (numerooperationFiltrage.setScale(3, RoundingMode.UP).compareTo(new BigDecimal(0).setScale(3, RoundingMode.UP)) >= 0) {
                requette88 = requette88 + "o.numerooperation like :numerooperation and ";
                n.add("numerooperation");
                o.add("%" + numerooperationFiltrage + "%");
             
            }
        }
           if ((montantoperationFiltrage != null)) {
            if (montantoperationFiltrage.setScale(3, RoundingMode.UP).compareTo(new BigDecimal(0).setScale(3, RoundingMode.UP)) >= 0) {
                requette88 = requette88 + "o.montantoperation like :montantoperation and ";
                n.add("montantoperation");
                o.add("%" + montantoperationFiltrage + "%");
             
            }
        }
       
     
        if (requette88.endsWith(" and ")) {
            requette88 = requette88.substring(0, requette88.lastIndexOf(" and "));
        }
        String orderByRequette = "";
        
//            orderByRequette = orderByRequette + "o.numerooperation DESC,";
//        
//        if (order.contains("dateSys")) {
//            orderByRequette = orderByRequette + "o.dateSys,";
//        }
//     
//       
//        if (order.contains("nomTi")) {
//            orderByRequette = orderByRequette + "o.niTi.nomTi,";
//        }
//      
//        
//        if (order.contains("montantTtcOp")) {
//            orderByRequette = orderByRequette + "o.montantTtcOp,";
//        }
//        if (orderByRequette.endsWith(",")) {
//            orderByRequette = orderByRequette.substring(0, orderByRequette.lastIndexOf(","));
//        }
        orderByRequette = "order by o.id,o.dateoperation" + orderByRequette;
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
              List<Operation> lop = this.ejbFacade.findByParameterMultipleCreteria(requette88, n, o);
            items = new ListDataModel(lop);
    //        items = null;
        } catch (Exception er) {
       //     er.printStackTrace();
        }
        return null;
    }

 
    
    public String rechercherListItems() {
        String requette = "Select o from Operation o where ";
        String order = "order by ";
        Iterator it = this.mapRechercheList.entrySet().iterator();
        List<String> ss = new ArrayList<String>();
        List<Object> oo = new ArrayList<Object>();
        while (it.hasNext()) {
            Object o = it.next();
            Entry<String, Object> e = (Entry<String, Object>) o;
            String nomComponent = e.getKey();
            String valueComponent = e.getValue().toString();
            if (valueComponent != null) {
                if (valueComponent.isEmpty() == false) {
                    StringTokenizer st = new StringTokenizer(nomComponent, "_");
                    if (st.countTokens() == 1) {
                        requette = requette + "o." + nomComponent + " like :" + nomComponent + " and ";
                        ss.add(nomComponent);
                        oo.add("%" + valueComponent + "%");
                        order = order + "o." + nomComponent + ",";
                    } else {
                        String nomCompletColonne = "o.";
                        String lastSuffixColumn = "";
                        while (st.hasMoreTokens()) {
                            String stt = st.nextToken();
                            nomCompletColonne = nomCompletColonne + stt + ".";
                            lastSuffixColumn = stt;
                        }
                        if (nomCompletColonne.endsWith(".")) {
                            nomCompletColonne = nomCompletColonne.substring(0, nomCompletColonne.lastIndexOf("."));
                        }
                        requette = requette + nomCompletColonne + " like :" + lastSuffixColumn + " and ";
                        ss.add(lastSuffixColumn);
                        oo.add("%" + valueComponent + "%");
                        order = order + nomCompletColonne + ",";
                    }
                }
            }
        }
        if (order.endsWith(",")) {
            order = order.substring(0, order.lastIndexOf(","));
        }
        if (order.endsWith("order by ")) {
            order = order.substring(0, order.lastIndexOf("order by "));
        }
        if (requette.endsWith(" and ")) {
            requette = requette.substring(0, requette.lastIndexOf(" and "));
        }
        if (requette.endsWith(" where ")) {
            requette = requette.substring(0, requette.lastIndexOf(" where "));
        }
        requette = requette.trim();
        requette = requette + " " + order;
        try {
            getPagination().setRequetteJpql(requette);
            getPagination().setArrayNames(ss);
            getPagination().setArrayValues(oo);
            getPagination().setPage(0);
            items = null;
        } catch (Exception ex) {
        }
        return null;
    }

    public String prepareList() {
        nompage="facture";
        recreateModel();
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        this.mapRechercheList = new HashMap<String, Object>();
        page = "List";
        map.put("page", page);
        map.put("current", current);
        map.put("items", items);
        map.put("nompage", nompage);
        map.put("mapRechercheList", mapRechercheList);
        return "List_operation";
    }

    public String prepareListDivers() {
          nompage="Depense Divers";
        recreateModel();
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        this.mapRechercheList = new HashMap<String, Object>();
        page = "List";
        dateDebut= new Date();
        dateFin = new Date();
        map.put("page", page);
        map.put("current", current);
        map.put("items", items);
            map.put("optionOrient", optionOrient);
         map.put("nompage", nompage);
        map.put("mapRechercheList", mapRechercheList);
         map.put("dateDebut", dateDebut);
        map.put("dateFin", dateFin); 
         map.put("clientFiltrage", clientFiltrage); 
          map.put("modereglementFiltrage", modereglementFiltrage); 
           map.put("banqueFiltrage", banqueFiltrage); 
            map.put("montantoperationFiltrage", montantoperationFiltrage); 
             map.put("numerooperationFiltrage", numerooperationFiltrage); 

        return "List_operation_divers";
    }

    public String prepareListFournisseur() {
         nompage="Depense Fournisseur";
        recreateModel();
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        this.mapRechercheList = new HashMap<String, Object>();
        page = "List";
        this.modele = new ModeleRapportCom();
        List<ModeleRapportCom> findByParameter = ejbModele.findByParameter("Select m from ModeleRapportCom m where m.nomModele =:top", "top", "facture client");
        if (!(findByParameter.isEmpty())) {
            modele = findByParameter.get(0);
        }
  dateDebut= new Date();
        dateFin = new Date();
        map.put("page", page);
         map.put("modele", modele);
         map.put("nompage", nompage);
        map.put("current", current);
        map.put("items", items);
         map.put("dateDebut", dateDebut);
        map.put("dateFin", dateFin); 
            map.put("optionOrient", optionOrient);
        map.put("mapRechercheList", mapRechercheList);
        return "List_operation_fournisseur";
    }
    
    
            
            public String prepareListOrdonance() {
           nompage="facture";
        recreateModel();
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        this.mapRechercheList = new HashMap<String, Object>();
          dateDebut= new Date();
        dateFin = new Date();
        page = "List";
        map.put("page", page);
        map.put("current", current);
        map.put("items", items);
        map.put("optionOrient", optionOrient);
        map.put("dateDebut", dateDebut);
        map.put("dateFin", dateFin); 
        map.put("nompage", nompage);
        map.put("mapRechercheList", mapRechercheList);
        return "List_operation_ordonance";
    }

     public String prepareListConsultation() {
           nompage="facture";
             dateDebut= new Date();
        dateFin = new Date();
        recreateModel();
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        this.mapRechercheList = new HashMap<String, Object>();
           this.modele = new ModeleRapportCom();
        List<ModeleRapportCom> findByParameter = ejbModele.findByParameter("Select m from ModeleRapportCom m where m.nomModele =:top", "top", "visite patient");
        if (!(findByParameter.isEmpty())) {
            modele = findByParameter.get(0);
        }
        page = "List";
        map.put("page", page);
           map.put("modele", modele);
        map.put("current", current);
        map.put("items", items);
        map.put("optionOrient", optionOrient);
        map.put("dateDebut", dateDebut);
        map.put("dateFin", dateFin); 
        map.put("nompage", nompage);
        map.put("mapRechercheList", mapRechercheList);
        return "List_operation_consultation";
    }

    public String prepareView() {
        current = (Operation) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        page = "Create";
//          operationConverter = new OperationControllerConverter();
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        map.put("page", page);
        map.put("current", current);
        return "Create_operation";
    }

    public String prepareCreateDivers() {
        page = "CreateDivers";
        typeClient="Divers";
       current=new Operation();
       current.setBanque( new Banque());
       current.setModereglement(new Modereglement());
        inputTextEmetteur=new InputText();
        inputTextRecu=new InputText();
        inputTextReference=new InputText();
 //     inputTextBanque = new AutoComplete();
 //       inputTextBanque.setDisabled(true);
   
          inputTextEmetteur.setDisabled(true);
        inputTextRecu.setDisabled(true);
        inputTextReference.setDisabled(true);
        inputTextEcheance=true;
        current.getModereglement().setId(new Long(1));
        current.getModereglement().setCode("1");
        current.getModereglement().setLibelle("ESPECE");   
        nompage="Depense Divers";
        current.setMontantoperation(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
        current.setDateSys(new Date());
        current.setDateoperation(new Date());
        selectedItemIndex = -1;
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        map.put("page", page);
        map.put("nompage", nompage);
        map.put("current", current);
     //   map.put("inputTextBanque", inputTextBanque);
        map.put("inputTextEmetteur", inputTextEmetteur);
        map.put("inputTextRecu", inputTextRecu);
        map.put("inputTextReference", inputTextReference);
        map.put("inputTextEcheance", inputTextEcheance);
          map.put("typeClient", typeClient);
        return "Create_operation_divers";
    }
   public String prepareCreateConsultation()
{
    typeClient="Patient";
      current=new Operation();
     page = "CreateConsultation";
        List<Detailoperation> listeDetailss = new ArrayList<Detailoperation>();
        for (int i = 0; i < 5; i++) {
            Detailoperation d = new Detailoperation();
           d.setIdacte(new Acte());
           d.setIddent( new Dent());
           d.setTarif(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            listeDetailss.add(d);
        }
          List<Detailordonance> listeDetailssordonance = new ArrayList<Detailordonance>();
        for (int i = 0; i < 5; i++) {
            Detailordonance d = new Detailordonance();
           d.setIdmedicament(new Medicament());
         d.setPosologie("");
         d.setDuree("");
         d.setQteordonance(BigDecimal.ONE.setScale(3, RoundingMode.UP));
            listeDetailssordonance.add(d);
        }

        this.listeDetails = new ListDataModel(listeDetailss);
          this.listeDetailsOrdonance = new ListDataModel(listeDetailssordonance);
        current.setDetail(true);
        inputTextMontantOperation = new InputText();      
        dataTableProduit = new DataTable();
        dataTableProduit.setRendered(true);
        dataTableOrdonance = new DataTable();
        dataTableOrdonance.setRendered(true);
        inputTextMontantOperation.setDisabled(true);
    
         nompage="visite Patient";
        current.setMontantoperation(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
        current.setDateSys(new Date());
        current.setDateoperation(new Date());
        current.setDateconsultation(new Date());
        visiteregle=false;
        inputTextEcheance =true;
        selectedItemIndex = -1;
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        map.put("page", page);
        map.put("nompage", nompage);
        map.put("typeClient", typeClient);
        map.put("current", current);
        map.put("listeDetails", listeDetails);    
        map.put("listeDetailsOrdonance", listeDetailsOrdonance);    
        map.put("inputTextMontantOperation", inputTextMontantOperation);
        map.put("dataTableProduit", dataTableProduit);
        map.put("dataTableOrdonance", dataTableOrdonance);
        map.put("visiteregle", visiteregle);
        return "Create_operation_consultation";
}
    public String prepareCreateFournisseur() {
           page = "CreateFournisseur";
           typeClient="Fournisseur";
       current=new Operation();
         current.setBanque( new Banque());
       current.setModereglement(new Modereglement());
       current.setDetail(true);
        inputTextEmetteur=new InputText();
        inputTextRecu=new InputText();
        inputTextReference=new InputText();

//        inputTextBanque = new AutoComplete();
  //        inputTextBanque.setDisabled(true);
          inputTextEmetteur.setDisabled(true);
        inputTextRecu.setDisabled(true);
        inputTextReference.setDisabled(true);
        inputTextEcheance=true;
        current.getModereglement().setId(new Long(1));
        current.getModereglement().setCode("1");
        current.getModereglement().setLibelle("ESPECE");   
        page = "CreateFournisseur";
        List<Detailoperation> listeDetailss = new ArrayList<Detailoperation>();
        for (int i = 0; i < 5; i++) {
            Detailoperation d = new Detailoperation();
            d.setQte(new BigDecimal(0).setScale(3, RoundingMode.UP));
            Produit p = new Produit();
            //  d.setIdproduit(p);
            d.setPrixtotal(new BigDecimal(0).setScale(3, RoundingMode.UP));
            d.setPrixunitaire(new BigDecimal(0).setScale(3, RoundingMode.UP));
            listeDetailss.add(d);
        }

        this.listeDetails = new ListDataModel(listeDetailss);
        inputTextMontantOperation = new InputText();
        dataTableProduit = new DataTable();
        dataTableProduit.setRendered(true);
        inputTextEmetteur.setDisabled(true);
        inputTextMontantOperation.setDisabled(true);
          current.setDateSys(new Date());
        current.setDateoperation(new Date());
 
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        map.put("page", page);
        map.put("current", current);
        map.put("listeDetails", listeDetails);
    //    map.put("inputTextBanque", inputTextBanque);
        map.put("inputTextEmetteur", inputTextEmetteur);
        map.put("inputTextRecu", inputTextRecu);
        map.put("inputTextEcheance", inputTextEcheance);
        map.put("inputTextReference", inputTextReference);
        map.put("inputTextMontantOperation", inputTextMontantOperation);
        map.put("dataTableProduit", dataTableProduit);
        map.put("typeClient", typeClient);
        return "Create_operation_fournisseur";
    }

    public String create() {
        try {
            Long l = new Long(1);
            try {
                l = getFacade().findByParameterSingleResultCountsansparam("Select max(c.id) from Operation c");
                if (l == null) {
                    l = new Long(1);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            current.setId(l + 1);
            //   current.setUser(new Long(2));
            User userr = new User();
            try {
                userr = ejbUser.findByParameterSingleResult("Select u from User u where u.login=:login", "login", "gassen");
                System.out.println("userr " + userr.getLogin());
            } catch (Exception e) {
            }
            current.setUser(userr);
            //    current.setUser(userr.getId());
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Transaction reussi");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Transaction echouee");
            return null;
        }
    }

    public String createDivers() {
        try {
            Long l = new Long(1);
            try {
                l = getFacade().findByParameterSingleResultCountsansparam("Select max(c.id) from Operation c");
                if (l == null) {
                    l = new Long(1);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            current.setId(l + 1);
            //   current.setUser(new Long(2));
            //       System.out.println("createDivers "+au);
            User userr = new User();
            try {
                userr = ejbUser.findByParameterSingleResult("Select u from User u where u.login=:login", "login", "gassen");
                System.out.println("userr " + userr.getLogin());
            } catch (Exception e) {
            }
            Typeoperation typeop = new Typeoperation();
            try {
                typeop = ejbtypeoperation.findByParameterSingleResult("Select u from Typeoperation u where u.libelle=:libelle", "libelle", "Depense Divers");
                System.out.println("typeop " + typeop.getLibelle());
            } catch (Exception e) {
            }
            //         current.setUser1(userr);
            current.setUser(userr);
            current.setTypeoperation(typeop);
            current.setMontantoperation(current.getMontantoperation().setScale(3, RoundingMode.UP));
            current.setMontantcredite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
              current.setMontantdebite(current.getMontantoperation().setScale(3, RoundingMode.UP));
            if(current.getModereglement()!=null)
            {
            if(current.getModereglement().getLibelle().equalsIgnoreCase("ESPECE"))
            {
               current.setMontantespece(current.getMontantoperation());
               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
             if(current.getModereglement().getLibelle().equalsIgnoreCase("CHEQUE"))
            {
               current.setMontantcheque(current.getMontantoperation());
               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               
            }
              if(current.getModereglement().getLibelle().equalsIgnoreCase("VIREMENT"))
            {
               current.setMontantvirement(current.getMontantoperation());
               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
                if(current.getModereglement().getLibelle().equalsIgnoreCase("TRAITE"))
            {
               current.setMontanttraite(current.getMontantoperation());
               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
            }
                current.setLibelleoperation("Depense Divers");
//             current.setTypeoperation(typeop.getId());
            getFacade().create(current);
            JsfUtil.addSuccessMessage("Transaction reussi");
            return prepareCreateDivers();
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addErrorMessage("Transaction echouee");
            return null;
        }
    }

    public String createFournisseur() {
        try {

            utx.begin();
              Exception transactionException = null;
            Long l = new Long(1);
            try {
                l = getFacade().findByParameterSingleResultCountsansparam("Select max(c.id) from Operation c");
                if (l == null) {
                    l = new Long(1);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
//          inputTextBanque.setDisabled(true);
        
            current.setId(l + 1);
            //   current.setUser(new Long(2));
            //       System.out.println("createDivers "+au);
            User userr = new User();
            try {
                userr = ejbUser.findByParameterSingleResult("Select u from User u where u.login=:login", "login", "gassen");
                System.out.println("userr " + userr.getLogin());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Typeoperation typeop = new Typeoperation();
            try {
                typeop = ejbtypeoperation.findByParameterSingleResult("Select u from Typeoperation u where u.libelle=:libelle", "libelle", "Depense Fournisseur");
                System.out.println("typeop " + typeop.getLibelle());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //         current.setUser1(userr);
            current.setUser(userr);
            current.setTypeoperation(typeop);

            Iterator i = this.chargementliste().iterator();
            Integer in = new Integer(0);

                   if(current.getDetail()==false)
            {
                this.listeDetails = new ListDataModel(new ArrayList<Detailoperation>());
            }
                   else
                   {
            while (i.hasNext()) {
                Detailoperation d = (Detailoperation) i.next();
                if (d.getIdproduit() != null) {
                    if (d.getIdproduit().getId() != null) {
//                        d.setIdoperation(l+1);
                        DetailoperationPK dPK = new DetailoperationPK();
                        dPK.setIdoperation(l + 1);
                        dPK.setOrdre(in);

                        d.setDetailoperationPK(dPK);
                        d.setIdproduit(d.getIdproduit());
                        d.setQte(d.getQte().setScale(3, RoundingMode.UP));
                        d.setPrixunitaire(d.getPrixunitaire().setScale(3, RoundingMode.UP));
                        d.setPrixtotal((d.getQte().multiply(d.getPrixunitaire())).setScale(3, RoundingMode.UP));
                        d.setTarif(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                      
                        try {
                            this.ejbdetailoperation.create(d);
                        } catch (Exception e1) {
                            System.out.println("e1");
                            e1.printStackTrace();

                        }
                    }
                    in++;
                }




            }
               current.setMontantoperation(calcultotal().setScale(3, RoundingMode.UP));
                   }
         
            current.setMontantoperation(current.getMontantoperation().setScale(3, RoundingMode.UP));
        current.setMontantcredite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
              current.setMontantdebite(current.getMontantoperation().setScale(3, RoundingMode.UP));
            if(current.getModereglement()!=null)
            {
            if(current.getModereglement().getLibelle().equalsIgnoreCase("ESPECE"))
            {
               current.setMontantespece(current.getMontantoperation());
               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
             if(current.getModereglement().getLibelle().equalsIgnoreCase("CHEQUE"))
            {
               current.setMontantcheque(current.getMontantoperation());
               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               
            }
              if(current.getModereglement().getLibelle().equalsIgnoreCase("VIREMENT"))
            {
               current.setMontantvirement(current.getMontantoperation());
               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
                if(current.getModereglement().getLibelle().equalsIgnoreCase("TRAITE"))
            {
               current.setMontanttraite(current.getMontantoperation());
               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
            }
                current.setLibelleoperation("Depense Fournisseur");
           
             
            getFacade().create(current);
             try {

                utx.commit();


            } catch (javax.transaction.RollbackException ex) {
               ex.printStackTrace();
                transactionException = ex;
            } catch (Exception ex) {ex.printStackTrace();
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Transaction réussie");
                return prepareCreateFournisseur();
            } else {

                JsfUtil.addErrorMessage("Transaction échouée");
                return null;
            }
        } catch (Exception e) {
      //      e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception ex) {
      //          ex.printStackTrace();
            }

            JsfUtil.addErrorMessage("Transaction échouée");
            return null;
        }

     

    }

    
     public String createConsultation() {
        try {

            utx.begin();
            Long l = new Long(1);
            try {
                l = getFacade().findByParameterSingleResultCountsansparam("Select max(c.id) from Operation c");
                if (l == null) {
                    l = new Long(1);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            current.setId(l + 1);
            System.out.println("l id est : "+current.getId());
            //   current.setUser(new Long(2));
            //       System.out.println("createDivers "+au);
            User userr = new User();
            try {
                userr = ejbUser.findByParameterSingleResult("Select u from User u where u.login=:login", "login", "gassen");
                System.out.println("userr " + userr.getLogin());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Typeoperation typeop = new Typeoperation();
            try {
                typeop = ejbtypeoperation.findByParameterSingleResult("Select u from Typeoperation u where u.libelle=:libelle", "libelle", "facture");
                System.out.println("typeop " + typeop.getLibelle());
            } catch (Exception e) {
                e.printStackTrace();
            }
            //         current.setUser1(userr);
            current.setUser(userr);
            current.setTypeoperation(typeop);
             System.out.println("le montant operation est ! "+current.getMontantoperation());
              System.out.println("le montant credit est ! "+current.getMontantcredite());
           current.setMontantoperation(calcultotaltarif().setScale(3, RoundingMode.UP));
           current.setMontantcredite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
              current.setMontantdebite(current.getMontantoperation().setScale(3, RoundingMode.UP));
              current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setLibelleoperation("facture client");
   getFacade().create(current);
            Iterator i = this.chargementliste().iterator();
            System.out.println("liiiiiiiist "+listeDetails.getRowCount());
            Integer in = new Integer(0);
  if(current.getDetail()==false)
            {
                this.listeDetails = new ListDataModel(new ArrayList<Detailoperation>());
            }
                   else
                   {
            while (i.hasNext()) {
                Detailoperation d = (Detailoperation) i.next();
                if (d.getIdacte() != null) {
                    if (d.getIdacte().getId() != null) {
                        System.out.println("ligne ");
                        DetailoperationPK dPK = new DetailoperationPK();
                        dPK.setIdoperation(l + 1);
                        dPK.setOrdre(in);
                        System.out.println("ordre "+dPK.getOrdre());
                        d.setDetailoperationPK(dPK);
                      d.setIdacte(d.getIdacte());
                      d.setIddent(d.getIddent());
                      d.setTarif(d.getTarif().setScale(3, RoundingMode.UP));
                          d.setQte(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                        d.setPrixunitaire(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                        d.setPrixtotal(BigDecimal.ZERO.setScale(3, RoundingMode.UP).setScale(3, RoundingMode.UP));
                        System.out.println("denttt "+d.getIddent().getLibelledent());
                        try {
                            this.ejbdetailoperation.create(d);
                        } catch (Exception e1) {
                            System.out.println("e1");
                            e1.printStackTrace();

                        }
                    }
                    in++;
                }




            }
                   }
           
            //////////////////////////////////
            
            
             Iterator i1 = this.chargementlisteOrdonance().iterator();
            Integer in1 = new Integer(0);

            while (i1.hasNext()) {
                Detailordonance d1 = (Detailordonance) i1.next();
                if (d1.getIdmedicament() != null) {
                    if (d1.getIdmedicament().getId() != null) {
                          DetailordonancePK do1 = new DetailordonancePK();
                        do1.setId(l+1);
                        do1.setOrdre(in1);

                        d1.setDetailordonancePK(do1);
                      d1.setIdmedicament(d1.getIdmedicament());
                      d1.setIdoperation(new Operation(l+1));
                      d1.setDuree(d1.getDuree());
                      d1.setPosologie(d1.getPosologie());
                      d1.setQteordonance(d1.getQteordonance().setScale(3, RoundingMode.UP));
                    
                        try {
                            this.ejbdetailordonance.create(d1);
                        } catch (Exception e1) {
                            System.out.println("e1");
                            e1.printStackTrace();

                        }
                    }
                    in1++;
                }




            }
       
    //    visiteregle=true;
      //      System.out.println("visiteregle "+visiteregle);  
            if(visiteregle==true)
            {
                 
                LettrageCom pp =  new LettrageCom();                 
                   pp.setNiOp(new Operation(l + 1));
                    try {
                      pp.setNiLet(maxNiLET());
                    } catch (Exception e) {
                        pp.setNiLet(new Long(1));
                    }
                   pp.setNiPiece(new Operation(l + 1));
                    pp.setNumPiece(current.getNumerooperation());
                    pp.setMontantPiece(current.getMontantoperation());
                    pp.setMontantReg(current.getMontantoperation());
                    pp.setSoldePiece(BigDecimal.ZERO);
                    pp.setTypeOPL(getLikeTypeOPComBylibelleReglement("client"));
                     pp.setTypeOPAL(typeop);
                    jpaLettrageCom.create(pp);
                 
                }
            
            /////////////////////////////////
          
         
            utx.commit();
            JsfUtil.addSuccessMessage("Transaction reussi");
            return prepareCreateConsultation();
        } catch (Exception e) {
            System.out.println("final catch");
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception rrrt) {
            }
            try {
            } catch (Exception rg) {
                System.out.println("rg");
                rg.printStackTrace();
            }
            JsfUtil.addErrorMessage("Echec");
        }



        return prepareCreateConsultation();

    }

      public Typeoperation getLikeTypeOPComBylibelleReglement(String type) {
        String q = "";
        
           
            q =" SELECT t FROM Typeoperation t WHERE t.libelle = 'Reglement client' ";
        
        Typeoperation l = (Typeoperation) this.ejbtypeoperation.execCommandeSansParam(q);
        System.out.println("getLikeTypeOPComBylibelleReglement l : "+l.getLibelle());
        return l;
    }

       public Long maxNiLET(){
           String q = "Select max(c.niLet) from LettrageCom c";
     Long b = Long.valueOf(0);
try {
            List<String> s = new ArrayList<String>();
            List<Object> o = new ArrayList<Object>();
            try {
                 b = new Long(this.jpaLettrageCom.findByParameterMultipleCreteriaObject(q, s, o).toString());
              
            } catch (Exception not) {
       
            }
        } catch (Exception e) {
        }   
        return b+1;
    }
    
    public String prepareEdit() {
        current = (Operation) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        page = "Edit";
//          operationConverter = new OperationControllerConverter();
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("current", current);
        return "Edit_operation";
    }

    public String prepareEditDivers() {
        typeClient="Divers";
        current = (Operation) getItems().getRowData();
         inputTextEmetteur=new InputText();
        inputTextRecu=new InputText();
        inputTextReference=new InputText();
    //    inputTextBanque= new AutoComplete();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        page = "Edit";
        if(current.getModereglement()!=null)
        {
         if (((current.getModereglement().getLibelle()).trim().equalsIgnoreCase("CHEQUE")) || (current.getModereglement().getLibelle().trim().equalsIgnoreCase("TRAITE")) || ((current.getModereglement().getLibelle().trim().equalsIgnoreCase("VIREMENT")))) {
        
            System.out.println("cas 1");
            inputTextReference.setDisabled(false);
            inputTextRecu.setDisabled(false);
            inputTextEmetteur.setDisabled(false);
//          inputTextBanque.setDisabled(false);
     current.setBanque(new Banque());
             inputTextEcheance=false;
            current.setBanque(new Banque());
         
        } else {
              System.out.println("cas 2 else");
            
            inputTextReference.setDisabled(true);
            inputTextRecu.setDisabled(true);
            inputTextEmetteur.setDisabled(true);
//        inputTextBanque.setDisabled(true);
   current.setBanque(new Banque());
            inputTextEcheance=true;
            current.setReference("");
            current.setRecu("");
              current.setEmetteur("");
              current.setBanque(new Banque());
              current.setDateechenace(new Date());
           
        }
        }
        
//          operationConverter = new OperationControllerConverter();
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("current", current);
    //    FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextBanque", inputTextBanque);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextEmetteur", inputTextEmetteur);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextRecu", inputTextRecu);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextReference", inputTextReference);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextEcheance", inputTextEcheance);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("typeClient", typeClient);
        return "Edit_operation_divers";
    }

    public String prepareEditFournisseur() {
        typeClient="Fournsieeur";
        current = (Operation) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        page = "Edit";
         inputTextEmetteur=new InputText();
        inputTextRecu=new InputText();
        inputTextReference=new InputText();
      //  inputTextBanque= new AutoComplete();
        
        if(current.getModereglement()!=null)
        {
         if (((current.getModereglement().getLibelle()).trim().equalsIgnoreCase("CHEQUE")) || (current.getModereglement().getLibelle().trim().equalsIgnoreCase("TRAITE")) || ((current.getModereglement().getLibelle().trim().equalsIgnoreCase("VIREMENT")))) {
        
            System.out.println("cas 1");
            inputTextReference.setDisabled(false);
            inputTextRecu.setDisabled(false);
            inputTextEmetteur.setDisabled(false);
//          inputTextBanque.setDisabled(false);
           current.setBanque(new Banque());
            inputTextEcheance=false;
            
         
        } else {
              System.out.println("cas 2 else");
            
            inputTextReference.setDisabled(true);
            inputTextRecu.setDisabled(true);
            inputTextEmetteur.setDisabled(true);
//          inputTextBanque.setDisabled(true);
           current.setBanque(new Banque());
            inputTextEcheance=true;
            current.setReference("");
            current.setRecu("");
              current.setEmetteur("");
              current.setBanque(new Banque());
              current.setDateechenace(new Date());
           
        }
        }
  try {
            this.listeDetails = new ListDataModel(this.ejbdetailoperation.findByParameter("Select d from Detailoperation d where d.operation.id=:op", "op", current.getId()));
             System.out.println("size listeDetails "+listeDetails.getRowCount());
         } catch (Exception e) {
            this.listeDetails = new ListDataModel(new ArrayList<Detailoperation>());
        }
  
     if (((List<Detailoperation>) listeDetails.getWrappedData()).isEmpty()) {
            System.out.println("emptyyyyy ajouter ligne");
            Detailoperation dt = new Detailoperation();
            dt.setQte(new BigDecimal(1).setScale(3, RoundingMode.UP));
            dt.setIdproduit(new Produit());
             dt.setTarif(new BigDecimal(0).setScale(3, RoundingMode.UP));
            ((List<Detailoperation>) listeDetails.getWrappedData()).add(dt);
        }
     
    //    FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextBanque", inputTextBanque);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextEmetteur", inputTextEmetteur);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextRecu", inputTextRecu);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextReference", inputTextReference);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextEcheance", inputTextEcheance);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("current", current);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("listeDetails", listeDetails);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("typeClient", typeClient);

        return "Edit_operation_fournisseur";
    }
    
    public String prepareEditConsultation() {
        typeClient="Patient";
        current = (Operation) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        page = "Edit";
        System.out.println("currenttt "+current.getId());
         try {
            this.listeDetails = new ListDataModel(this.ejbdetailoperation.findByParameter("Select d from Detailoperation d where d.operation.id=:op", "op", current.getId()));
             System.out.println("size listeDetails "+listeDetails.getRowCount());
         } catch (Exception e) {
            this.listeDetails = new ListDataModel(new ArrayList<Detailoperation>());
        }
          try {
            this.listeDetailsOrdonance = new ListDataModel(this.ejbdetailordonance.findByParameter("Select d from Detailordonance d where d.idoperation.id=:op", "op", current.getId()));
         System.out.println("size listeDetailsOrdonance "+listeDetailsOrdonance.getRowCount());
          } catch (Exception e) {
            this.listeDetailsOrdonance = new ListDataModel(new ArrayList<Detailoperation>());
        }
         
            if (((List<Detailordonance>) listeDetailsOrdonance.getWrappedData()).isEmpty()) {
            Detailordonance dt = new Detailordonance();
                dt.setQteordonance(new BigDecimal(1).setScale(3, RoundingMode.UP));
                dt.setDuree("");
                dt.setPosologie("");
                dt.setIdmedicament(new Medicament());
            ((List<Detailordonance>) listeDetailsOrdonance.getWrappedData()).add(dt);
        }
             if (((List<Detailoperation>) listeDetails.getWrappedData()).isEmpty()) {
            System.out.println("emptyyyyy ajouter ligne");
            Detailoperation dt = new Detailoperation();
            dt.setQte(new BigDecimal(1).setScale(3, RoundingMode.UP));
            dt.setIdproduit(new Produit());
             dt.setTarif(new BigDecimal(0).setScale(3, RoundingMode.UP));
            ((List<Detailoperation>) listeDetails.getWrappedData()).add(dt);
        }
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("current", current);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("listeDetails", listeDetails);
         FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("listeDetailsOrdonance", listeDetailsOrdonance);
              FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("typeClient", typeClient);

         return "Edit_operation_consultation";
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


    
    
    public void changeModeReglement() {
        System.out.println("fct  changeModeReglement");
        System.out.println("page : "+page);
      
        if (((current.getModereglement().getLibelle()).trim().equalsIgnoreCase("CHEQUE")) || (current.getModereglement().getLibelle().trim().equalsIgnoreCase("TRAITE")) || ((current.getModereglement().getLibelle().trim().equalsIgnoreCase("VIREMENT")))) {
        
            System.out.println("cas 1");
            inputTextReference.setDisabled(false);
            inputTextRecu.setDisabled(false);
            inputTextEmetteur.setDisabled(false);
//           inputTextBanque.setDisabled(false);
           current.setBanque(new Banque());
            inputTextEcheance=false;
            
         
        } else {
              System.out.println("cas 2 else");
            
            inputTextReference.setDisabled(true);
            inputTextRecu.setDisabled(true);
            inputTextEmetteur.setDisabled(true);
//            inputTextBanque.setDisabled(true);
           current.setBanque(new Banque());
            inputTextEcheance=true;
             current.setReference("");
              current.setRecu("");
              current.setEmetteur("");
          //    current.setBanque(new Banque());
              current.setDateechenace(new Date());
           
        }
         
         
        
    }
    
    public String updateDivers() {
     
          try {
             current.setMontantoperation(current.getMontantoperation().setScale(3, RoundingMode.UP));
            current.setMontantcredite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
              current.setMontantdebite(current.getMontantoperation().setScale(3, RoundingMode.UP));
            System.out.println("mode reglement "+current.getModereglement().getLibelle());
           if(current.getModereglement()!=null)
            {
            if(current.getModereglement().getLibelle().equalsIgnoreCase("ESPECE"))
            {
               current.setMontantespece(current.getMontantoperation());
               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
             
            }
             if(current.getModereglement().getLibelle().equalsIgnoreCase("CHEQUE"))
            {
               current.setMontantcheque(current.getMontantoperation());
               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               
            }
              if(current.getModereglement().getLibelle().equalsIgnoreCase("VIREMENT"))
            {
               current.setMontantvirement(current.getMontantoperation());
               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
                if(current.getModereglement().getLibelle().equalsIgnoreCase("TRAITE"))
            {
               current.setMontanttraite(current.getMontantoperation());
               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
            }
            System.out.println("avant edit");
            getFacade().edit(current);
            System.out.println("apres edit");
            JsfUtil.addSuccessMessage("Transaction reussi");
            return prepareListDivers();
          } catch (Exception e) {
              System.out.println("catch 22");
              e.printStackTrace();
            JsfUtil.addErrorMessage("Transaction echouee");
            return null;
        }          
    }

    
    
    public String updateFournisseur() {
     
          try {
                utx.begin();
             current.setMontantoperation(current.getMontantoperation().setScale(3, RoundingMode.UP));
            current.setMontantcredite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
              current.setMontantdebite(current.getMontantoperation().setScale(3, RoundingMode.UP));
            System.out.println("mode reglement "+current.getModereglement().getLibelle());
           if(current.getModereglement()!=null)
            {
            if(current.getModereglement().getLibelle().equalsIgnoreCase("ESPECE"))
            {
               current.setMontantespece(current.getMontantoperation());
               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
             
            }
             if(current.getModereglement().getLibelle().equalsIgnoreCase("CHEQUE"))
            {
               current.setMontantcheque(current.getMontantoperation());
               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               
            }
              if(current.getModereglement().getLibelle().equalsIgnoreCase("VIREMENT"))
            {
               current.setMontantvirement(current.getMontantoperation());
               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
                if(current.getModereglement().getLibelle().equalsIgnoreCase("TRAITE"))
            {
               current.setMontanttraite(current.getMontantoperation());
               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
            }
            System.out.println("avant edit");
            getFacade().edit(current);
            System.out.println("apres edit");
             Iterator i = this.chargementliste().iterator();
            Integer in = new Integer(0);
     String deleteRequest = "Delete from Detailoperation d where d.operation.id=" + this.current.getId();
      System.out.println("deleteRequestOrdonance : " + this.ejbdetailoperation.executerRemoveInstruction(deleteRequest, new ArrayList<String>(), new ArrayList<Object>()));
                
            while (i.hasNext()) {
                Detailoperation d = (Detailoperation) i.next();
                if (d.getIdproduit() != null) {
                    if (d.getIdproduit().getId() != null) {
                        DetailoperationPK dPK = new DetailoperationPK();
                        dPK.setIdoperation(current.getId());
                        dPK.setOrdre(in);

                        d.setDetailoperationPK(dPK);
                        d.setIdproduit(d.getIdproduit());
                        d.setQte(d.getQte().setScale(3, RoundingMode.UP));
                        d.setPrixunitaire(d.getPrixunitaire().setScale(3, RoundingMode.UP));
                        d.setPrixtotal((d.getQte().multiply(d.getPrixunitaire())).setScale(3, RoundingMode.UP));
                        try {
                            this.ejbdetailoperation.create(d);
                        } catch (Exception e1) {
                            System.out.println("e1");
                            e1.printStackTrace();

                        }
                    }
                    in++;
                }




            }
              System.out.println("affichage detail :"+current.getDetail());
            if(current.getDetail()==false)
            {
                System.out.println("deleteRequestOrdonance : " + this.ejbdetailoperation.executerRemoveInstruction(deleteRequest, new ArrayList<String>(), new ArrayList<Object>()));
        
            }
              utx.commit();
            JsfUtil.addSuccessMessage("Transaction reussi");
            return prepareListFournisseur();
          } catch (Exception e) {
  
            try {
             
                utx.rollback();
            } catch (Exception rrrt) {
            }
            try {
           
            } catch (Exception rg) {
            }
            JsfUtil.addErrorMessage("Echec");
            return null;
        }          
    }
//    public String updateFournisseur() {
//        try {
//            
//               utx.begin();
//            current.setMontantoperation(current.getMontantoperation().setScale(3, RoundingMode.UP));
//            current.setMontantcredite(current.getMontantoperation().setScale(3, RoundingMode.UP));
//            current.setMontantdebite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//            System.out.println("mode reglement "+current.getModereglement().getLibelle());
//           if(current.getModereglement()!=null)
//            {
//            if(current.getModereglement().getLibelle().equalsIgnoreCase("ESPECE"))
//            {
//               current.setMontantespece(current.getMontantoperation());
//               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//             
//            }
//             if(current.getModereglement().getLibelle().equalsIgnoreCase("CHEQUE"))
//            {
//               current.setMontantcheque(current.getMontantoperation());
//               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//               
//            }
//              if(current.getModereglement().getLibelle().equalsIgnoreCase("VIREMENT"))
//            {
//               current.setMontantvirement(current.getMontantoperation());
//               current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//            }
//                if(current.getModereglement().getLibelle().equalsIgnoreCase("TRAITE"))
//            {
//               current.setMontanttraite(current.getMontantoperation());
//               current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//               current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//               current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
//            }
//            }
//            System.out.println("avant edit");
//            getFacade().edit(current);
//            System.out.println("apres edit");
//               String deleteRequest = "Delete from Detailoperation d where d.operation.id=" + this.current.getId();
//             
//                  Integer sizeFactureFournisseur = this.listeDetails.getRowCount();
//              System.out.println("sizeFactureFournisseur "+sizeFactureFournisseur);
//                  
//                           System.out.println("listeDetails=="+listeDetails.getRowCount());
//              Iterator i = this.chargementliste().iterator();
//            System.out.println("liiiiiiiist "+listeDetails.getRowCount());
//            Integer in = new Integer(0);
//
//           
//   System.out.println("deleteRequest : " + this.ejbdetailoperation.executerRemoveInstruction(deleteRequest, new ArrayList<String>(), new ArrayList<Object>()));                   
//   
//            while (i.hasNext()) {
//                Detailoperation d = (Detailoperation) i.next();
//                if (d.getIdproduit() != null) {
//                    if (d.getIdproduit().getId() != null) {
////                        d.setIdoperation(l+1);
//                        DetailoperationPK dPK = new DetailoperationPK();
//                        dPK.setIdoperation(current.getId());
//                        dPK.setOrdre(in);
//                        d.setDetailoperationPK(dPK);
//                        d.setIdproduit(d.getIdproduit());
//                        d.setQte(d.getQte().setScale(3, RoundingMode.UP));
//                        d.setPrixunitaire(d.getPrixunitaire().setScale(3, RoundingMode.UP));
//                        d.setPrixtotal((d.getQte().multiply(d.getPrixunitaire())).setScale(3, RoundingMode.UP));
//                        try {
//                            this.ejbdetailoperation.create(d);
//                        } catch (Exception e1) {
//                            System.out.println("e1");
//                            e1.printStackTrace();
//
//                        }
//                    }
//                    in++;
//                }
//
//
//
//
//            }
//            
//           utx.commit();
//            JsfUtil.addSuccessMessage("Transaction reussi");
//            return prepareListFournisseur();
//        } catch (Exception e) {
//            System.out.println("final catch");
//            e.printStackTrace();
//            try {
//                utx.rollback();
//            } catch (Exception rrrt) {
//                  JsfUtil.addErrorMessage("Echec");
//     JsfUtil.addErrorMessage("Transaction échouée");
//                  return null;
//            }
//           
//             return null;
//        }
//    }
    public String updateConsultation() {
        try {
            
           utx.begin();  
           current.setMontantoperation(calcultotaltarif().setScale(3, RoundingMode.UP));
           current.setMontantcredite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
              current.setMontantdebite(current.getMontantoperation().setScale(3, RoundingMode.UP));
            System.out.println("le montant operation est ! "+current.getMontantoperation());
            System.out.println("le montant credit est ! "+current.getMontantcredite());
            current.setMontantdebite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            getFacade().edit(current);
           
              String deleteRequest = "Delete from Detailoperation d where d.operation.id=" + this.current.getId();
                String deleteRequestOrdonance = "Delete from Detailordonance d where d.idoperation.id=" + this.current.getId();
            
                  Integer sizeFactureConsultation = this.listeDetails.getRowCount();
              System.out.println("sizeFactureConsultation "+sizeFactureConsultation);
                  
                    System.out.println("deleteRequest : " + this.ejbdetailoperation.executerRemoveInstruction(deleteRequest, new ArrayList<String>(), new ArrayList<Object>()));                   
            System.out.println("listeDetails=="+listeDetails.getRowCount());
              Iterator i = this.chargementliste().iterator();
            System.out.println("liiiiiiiist "+listeDetails.getRowCount());
            Integer in = new Integer(0);
  if(current.getDetail()==false)
            {
                this.listeDetails = new ListDataModel(new ArrayList<Detailoperation>());
            }
                   else
                   {
            while (i.hasNext()) {
                Detailoperation d = (Detailoperation) i.next();
                if (d.getIdacte() != null) {
                    if (d.getIdacte().getId() != null) {
                        System.out.println("ligne ");
                        DetailoperationPK dPK = new DetailoperationPK();
                        dPK.setIdoperation(current.getId());
                        dPK.setOrdre(in);
                        System.out.println("ordre "+dPK.getOrdre());
                        d.setDetailoperationPK(dPK);
                      d.setIdacte(d.getIdacte());
                      d.setIddent(d.getIddent());
                      d.setTarif(d.getTarif().setScale(3, RoundingMode.UP));
                        System.out.println("denttt "+d.getIddent().getLibelledent());
                        try {
                            this.ejbdetailoperation.create(d);
                        } catch (Exception e1) {
                            System.out.println("e1");
                            e1.printStackTrace();

                        }
                    }
                    in++;
                }
            }
                   }
            
           Iterator i1 = this.chargementlisteOrdonance().iterator();
              System.out.println("deleteRequestOrdonance : " + this.ejbdetailordonance.executerRemoveInstruction(deleteRequestOrdonance, new ArrayList<String>(), new ArrayList<Object>()));
                  
            Integer in1 = new Integer(0);

            while (i1.hasNext()) {
                Detailordonance d1 = (Detailordonance) i1.next();
                if (d1.getIdmedicament() != null) {
                    if (d1.getIdmedicament().getId() != null) {
                          DetailordonancePK do1 = new DetailordonancePK();
                        do1.setId(current.getId());
                        do1.setOrdre(in1);

                        d1.setDetailordonancePK(do1);
                      d1.setIdmedicament(d1.getIdmedicament());
                      d1.setIdoperation(new Operation(current.getId()));
                      d1.setDuree(d1.getDuree());
                      d1.setPosologie(d1.getPosologie());
                      d1.setQteordonance(d1.getQteordonance().setScale(3, RoundingMode.UP));
                    
                        try {
                            this.ejbdetailordonance.create(d1);
                        } catch (Exception e1) {
                            System.out.println("e1");
                            e1.printStackTrace();

                        }
                    }
                    in1++;
                }
            }
             Typeoperation typeop = new Typeoperation();
            try {
                typeop = ejbtypeoperation.findByParameterSingleResult("Select u from Typeoperation u where u.libelle=:libelle", "libelle", "facture");
                System.out.println("typeop " + typeop.getLibelle());
            } catch (Exception e) {
                e.printStackTrace();
            }
              if(visiteregle==true)
            {
            
          String deleteRequestlettrage = "Delete from LettrageCom d where d.niPiece.id=" + this.current.getId();
        //   String deleteRequestoperation = "Delete from Operation d where d.id=" + this.current.getId(); 
            System.out.println("deleteRequestlettrage : " + jpaLettrageCom.executerRemoveInstruction(deleteRequestlettrage, new ArrayList<String>(), new ArrayList<Object>()));                   
         //   System.out.println("deleteRequestoperation : " + ejbFacade.executerRemoveInstruction(deleteRequestoperation, new ArrayList<String>(), new ArrayList<Object>()));                   
      
            
            
                
                LettrageCom pp =  new LettrageCom();                 
                   pp.setNiOp(new Operation(current.getId()));
                    try {
                      pp.setNiLet(maxNiLET());
                    } catch (Exception e) {
                        pp.setNiLet(new Long(1));
                    }
                   pp.setNiPiece(current);
                   pp.setNiOp(current);
                    pp.setNumPiece(current.getNumerooperation());
                    pp.setMontantPiece(current.getMontantoperation());
                    pp.setMontantReg(current.getMontantoperation());
                    pp.setSoldePiece(BigDecimal.ZERO);
                    pp.setTypeOPL(getLikeTypeOPComBylibelleReglement("client"));
                     pp.setTypeOPAL(typeop);
                    jpaLettrageCom.create(pp);
                 
                }

               utx.commit();
            JsfUtil.addSuccessMessage("Transaction reussi");
            return prepareListConsultation();
        }  catch (Exception e) {
  
            try {
               
                utx.rollback();
            } catch (Exception rrrt) {
            }
           
            JsfUtil.addErrorMessage("Echec");
            return null;
        }
    }
    public String destroy() {
        current = (Operation) getItems().getRowData();
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
        String q = "Select b from Client b where b.typeclient.libelle='"+typeClient+"' and (b.code like :code or b.nom like :code) ";
        List<Client> l = this.ejbclient.findByParameterAutocomplete(q, "code", code + "%", 50);
        //      System.out.println("l.size "+l.size());
        return l;
    }

    public List<Modereglement> autocompleteModeReglement(String code) {
        List<Modereglement> result = new ArrayList<Modereglement>();
        try {
            result = (List<Modereglement>) requeteModeReglement(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Modereglement>();
            return result;
        }
    }

    public List<Modereglement> requeteModeReglement(String code) {

        String q = "Select b from Modereglement b where b.code like :code or b.libelle like :code";
        List<Modereglement> l = this.ejbmodereglement.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }

    public List<Banque> autocompleteBanque(String code) {
        List<Banque> result = new ArrayList<Banque>();
        try {
            result = (List<Banque>) requeteBanque(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Banque>();
            return result;
        }
    }

    public List<Banque> requeteBanque(String code) {

        String q = "Select b from Banque b where b.code like :code or b.libelle like :code";
        List<Banque> l = this.ejbbanque.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }
    
     public List<Medicament> autocompleteMedicament(String code) {
        List<Medicament> result = new ArrayList<Medicament>();
        try {
            result = (List<Medicament>) requeteMedicament(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Medicament>();
            return result;
        }
    }

    public List<Medicament> requeteMedicament(String code) {

        String q = "Select b from Medicament b where b.codemedicament like :code or b.libellemedicament like :code";
        List<Medicament> l = this.ejbmedicament.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }

    public List<Typeoperation> autocompleteTypeOperation(String code) {
        List<Typeoperation> result = new ArrayList<Typeoperation>();
        try {
            result = (List<Typeoperation>) requeteTypeoperation(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Typeoperation>();
            return result;
        }
    }

    public List<Typeoperation> requeteTypeoperation(String code) {

        String q = "Select b from Typeoperation b where b.code like :code or b.libelle like :code";
        List<Typeoperation> l = this.ejbtypeoperation.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }

    public List<Produit> autocompleteProduitNom(String code) {
        List<Produit> result = new ArrayList<Produit>();
        try {
            result = (List<Produit>) getLikeProduitNom(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Produit>();
            return result;
        }
    }

    public List<Produit> getLikeProduitNom(String code) {
        String q = "Select m from Produit m where m.libelleproduit like :libelle ";
        List<Produit> l = this.ejbProduit.findByParameterAutocomplete(q, "libelle", code + "%", 50);
        return l;
    }

    public List<Produit> autocompleteProduitCode(String code) {
        List<Produit> result = new ArrayList<Produit>();
        try {
            result = (List<Produit>) getLikeProduitCode(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Produit>();
            return result;
        }
    }

    
     public List<Familleacte> autocompleteFamilleActe(String code) {
        List<Familleacte> result = new ArrayList<Familleacte>();
        try {
            result = (List<Familleacte>) requeteFamilleacte(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Familleacte>();
            return result;
        }
    }

    public List<Familleacte> requeteFamilleacte(String code) {
        String q = "Select b from Familleacte b where b.code like :code or b.libelle like :code";
        List<Familleacte> l = this.ejbfamilleacte.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }
    public List<Produit> getLikeProduitCode(String code) {
        String q = "Select m from Produit m where m.codeproduit like :code";
        List<Produit> l = this.ejbProduit.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }
    
     public List<Acte> autocompleteActe(String code) {
   
         System.out.println("famiillee acte "+listeDetails.getRowIndex());
        List<Acte> result = new ArrayList<Acte>();
        try {
            result = (List<Acte>) getLikeActe(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Acte>();
            return result;
        }
    }

    public List<Acte> getLikeActe(String code) {
        String q = "Select m from Acte m where m.libelleacte like :code";
        List<Acte> l = this.ejbActe.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }
    
     public List<Dent> autocompleteDent(String code) {
        List<Dent> result = new ArrayList<Dent>();
        try {
            result = (List<Dent>) getLikeDent(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Dent>();
            return result;
        }
    }

    public List<Dent> getLikeDent(String code) {
        String q = "Select m from Dent m where m.libelledent like :code";
        List<Dent> l = this.ejbDent.findByParameterAutocomplete(q, "code", code + "%", 50);
        return l;
    }

    public void ajouterLigneProduit() {
        int sizeList = this.listeDetails.getRowCount();
        int iii = this.listeDetails.getRowIndex();
        if (iii == sizeList - 1) {
            int i = 0;
            while (i < 5) {
                Detailoperation dt = new Detailoperation();
                dt.setQte(new BigDecimal(1).setScale(3, RoundingMode.UP));
                dt.setTarif(new BigDecimal(0).setScale(3, RoundingMode.UP));
                Produit p = new Produit();

                //     dt.setIdproduit(p);


                ((List<Detailoperation>) this.listeDetails.getWrappedData()).add(dt);
                i++;
            }
        } else {
            List<Detailoperation> l = new ArrayList<Detailoperation>();
            Iterator ii = ((List<Detailoperation>) this.listeDetails.getWrappedData()).iterator();;
            Integer x = new Integer(0);
            while (ii.hasNext()) {
                Detailoperation e = (Detailoperation) ii.next();

                if (x == iii) {
                    Detailoperation dt = new Detailoperation();
                    dt.setQte(new BigDecimal(1).setScale(3, RoundingMode.HALF_UP));

                    Produit p = new Produit();

                    //         dt.setIdproduit(p);

                    l.add(dt);
                    x++;

                    l.add(e);
                } else {
                    l.add(e);
                }
                x++;
            }
            listeDetails = new ListDataModel(l);
        }
    }

     public void ajouterLigneOrdonance() {
        int sizeList = this.listeDetailsOrdonance.getRowCount();
        int iii = this.listeDetailsOrdonance.getRowIndex();
        if (iii == sizeList - 1) {
            int i = 0;
            while (i < 5) {
                Detailordonance dt = new Detailordonance();
                dt.setQteordonance(new BigDecimal(1).setScale(3, RoundingMode.UP));
                dt.setDuree("");
                dt.setPosologie("");
                dt.setIdmedicament(new Medicament());
               


                ((List<Detailordonance>) this.listeDetailsOrdonance.getWrappedData()).add(dt);
                i++;
            }
        } else {
            List<Detailordonance> l = new ArrayList<Detailordonance>();
            Iterator ii = ((List<Detailordonance>) this.listeDetailsOrdonance.getWrappedData()).iterator();;
            Integer x = new Integer(0);
            while (ii.hasNext()) {
                Detailordonance e = (Detailordonance) ii.next();

                if (x == iii) {
                    Detailordonance dt = new Detailordonance();
                   dt.setQteordonance(new BigDecimal(1).setScale(3, RoundingMode.UP));
                dt.setDuree("");
                dt.setPosologie("");
                dt.setIdmedicament(new Medicament());

                    l.add(dt);
                    x++;

                    l.add(e);
                } else {
                    l.add(e);
                }
                x++;
            }
            listeDetailsOrdonance = new ListDataModel(l);
        }
    }

    
    public DataModel getListeDetails() {
        return listeDetails;
    }

    public void setListeDetails(DataModel listeDetails) {
        this.listeDetails = listeDetails;
    }

    public void supprimerLigneProduit() {
        Integer i = this.listeDetails.getRowIndex();
        Iterator ii = ((List<Detailoperation>) this.listeDetails.getWrappedData()).iterator();
        List<Detailoperation> l = new ArrayList<Detailoperation>();
        Integer x = new Integer(0);
        while (ii.hasNext()) {
            Detailoperation e = (Detailoperation) ii.next();
            if (!(i.equals(x))) {
                l.add(e);
            }
            x++;
        }
        listeDetails = new ListDataModel(new ArrayList<Detailoperation>());
        Iterator iii = l.iterator();
        while (iii.hasNext()) {
            Detailoperation et = (Detailoperation) iii.next();
            ((List<Detailoperation>) listeDetails.getWrappedData()).add(et);
        }
        if (((List<Detailoperation>) listeDetails.getWrappedData()).isEmpty()) {
            System.out.println("emptyyyyy ajouter ligne");
            Detailoperation dt = new Detailoperation();
            dt.setQte(new BigDecimal(1).setScale(3, RoundingMode.UP));
            dt.setIdproduit(new Produit());
             dt.setTarif(new BigDecimal(0).setScale(3, RoundingMode.UP));
            ((List<Detailoperation>) listeDetails.getWrappedData()).add(dt);
        }
        Calculsupprimerligne();
    }
    public void supprimerLigneOrdonance() {
        Integer i = this.listeDetailsOrdonance.getRowIndex();
        Iterator ii = ((List<Detailordonance>) this.listeDetailsOrdonance.getWrappedData()).iterator();;
        List<Detailordonance> l = new ArrayList<Detailordonance>();
        Integer x = new Integer(0);
        while (ii.hasNext()) {
            Detailordonance e = (Detailordonance) ii.next();
            if (!(i.equals(x))) {
                l.add(e);
            }
            x++;
        }
        listeDetailsOrdonance = new ListDataModel(new ArrayList<Detailordonance>());
        Iterator iii = l.iterator();
        while (iii.hasNext()) {
            Detailordonance et = (Detailordonance) iii.next();
            ((List<Detailordonance>) listeDetailsOrdonance.getWrappedData()).add(et);
        }
        if (((List<Detailordonance>) listeDetailsOrdonance.getWrappedData()).isEmpty()) {
            Detailordonance dt = new Detailordonance();
                dt.setQteordonance(new BigDecimal(1).setScale(3, RoundingMode.UP));
                dt.setDuree("");
                dt.setPosologie("");
                dt.setIdmedicament(new Medicament());
            ((List<Detailordonance>) listeDetailsOrdonance.getWrappedData()).add(dt);
        }
    }

    public void calculligne() {
        Integer in = this.listeDetails.getRowIndex();
        Detailoperation d = ((List<Detailoperation>) this.listeDetails.getWrappedData()).get(in);
        try {
            d.setQte(d.getQte().setScale(3, RoundingMode.UP));
            d.setPrixunitaire(d.getPrixunitaire().setScale(3, RoundingMode.UP));
            d.setPrixtotal((d.getQte().multiply(d.getPrixunitaire())).setScale(3, RoundingMode.UP));

            System.out.println("prix unitaire : " + d.getPrixunitaire());
            System.out.println("qte :" + d.getQte());
            System.out.println("prix total :" + d.getPrixtotal());
        } catch (Exception e) {
            e.printStackTrace();
        }
        current.setMontantoperation(calcultotal().setScale(3, RoundingMode.UP));
    }
    public void calculConsultation() {
        System.out.println("calcul consutation");
        Integer in = this.listeDetails.getRowIndex();
        Detailoperation d = ((List<Detailoperation>) this.listeDetails.getWrappedData()).get(in);
        try {
          
            d.setTarif(d.getTarif().setScale(3, RoundingMode.UP));
        } catch (Exception e) {
            e.printStackTrace();
        }
        current.setMontantoperation(calcultotaltarif().setScale(3, RoundingMode.UP));
        current.setMontantcredite(calcultotaltarif().setScale(3, RoundingMode.UP));
    }
     
    public DataModel chargementliste() {
        ListDataModel l = new ListDataModel(new ArrayList<Detailoperation>());
        try {
            ((List<Detailoperation>) l.getWrappedData()).addAll((List<Detailoperation>) this.listeDetails.getWrappedData());
        } catch (Exception e) {
        }
        return l;
    }
       public DataModel chargementlisteOrdonance() {
        ListDataModel l = new ListDataModel(new ArrayList<Detailordonance>());
        try {
            ((List<Detailordonance>) l.getWrappedData()).addAll((List<Detailordonance>) this.listeDetailsOrdonance.getWrappedData());
        } catch (Exception e) {
        }
        return l;
    }

       public BigDecimal Calculsupprimerligne() {
           System.out.println("Calculsupprimerligne ");
           BigDecimal b = new BigDecimal(0).setScale(3, RoundingMode.UP);
           calcultotaltarif();
           System.out.println("fin calcultotaltarif");
           current.setMontantoperation(calcultotaltarif().setScale(3, RoundingMode.UP));
           System.out.println("montant oppppppp :"+current.getMontantoperation());
           return b;
       }
       
         public BigDecimal calcultotaltarif() {
        System.out.println("calcultotaltarif ");
        BigDecimal b = new BigDecimal(0).setScale(3, RoundingMode.UP);
        Iterator i = this.chargementliste().iterator();
        Integer j = new Integer(0);
        while (i.hasNext()) {
            while (i.hasNext()) {
                Detailoperation d = (Detailoperation) i.next();
                if (d.getIdacte() != null) {
                    if (d.getIdacte().getId() != null) {
                        b = b.add(d.getTarif()).setScale(6, RoundingMode.UP);

                    }
                }
                j++;
            }
        }
             System.out.println("b "+b);
        return b;
    }

    public BigDecimal calcultotal() {
        BigDecimal b = new BigDecimal(0).setScale(3, RoundingMode.UP);
        Iterator i = this.chargementliste().iterator();
        Integer j = new Integer(0);
        while (i.hasNext()) {
            while (i.hasNext()) {
                Detailoperation d = (Detailoperation) i.next();
                if (d.getIdproduit() != null) {
                    if (d.getIdproduit().getId() != null) {
                        b = b.add(d.getPrixtotal()).setScale(6, RoundingMode.UP);

                    }
                }
                j++;
            }
        }
        
        return b;
    }

  
    public void affichagelisteproduits() {
        System.out.println("fct affichagelisteproduits");
        System.out.println("details :" + current.getDetail());
        System.out.println("dataTableProduit " + dataTableProduit);
        if (current.getDetail() == false) {
            System.out.println("cas true");
            dataTableProduit.setRendered(false);
            inputTextMontantOperation.setDisabled(false);
        } else {
            System.out.println("cas false");
            dataTableProduit.setRendered(true);
            inputTextMontantOperation.setDisabled(true);
        }
    }

  


 

    public DataModel getListeDetailsOrdonance() {
        return listeDetailsOrdonance;
    }

    public void setListeDetailsOrdonance(DataModel listeDetailsOrdonance) {
        this.listeDetailsOrdonance = listeDetailsOrdonance;
    }

//    public AutoComplete getInputTextBanque() {
//        return inputTextBanque;
//    }
//
//    public void setInputTextBanque(AutoComplete inputTextBanque) {
//        this.inputTextBanque = inputTextBanque;
//    }

 


   

    public InputText getInputTextEmetteur() {
        return inputTextEmetteur;
    }

    public void setInputTextEmetteur(InputText inputTextEmetteur) {
        this.inputTextEmetteur = inputTextEmetteur;
    }

    public InputText getInputTextMontantOperation() {
        return inputTextMontantOperation;
    }

    public void setInputTextMontantOperation(InputText inputTextMontantOperation) {
        this.inputTextMontantOperation = inputTextMontantOperation;
    }

    public InputText getInputTextRecu() {
        return inputTextRecu;
    }

    public void setInputTextRecu(InputText inputTextRecu) {
        this.inputTextRecu = inputTextRecu;
    }

    public InputText getInputTextReference() {
        return inputTextReference;
    }

    public void setInputTextReference(InputText inputTextReference) {
        this.inputTextReference = inputTextReference;
    }

   

    @FacesConverter(forClass = Operation.class)
    public static class OperationControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
          try{  OperationController controller = (OperationController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "operationController");
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
            if (object instanceof Operation) {
                Operation o = (Operation) object;
                System.out.println("iddd " + o.getId());
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + OperationController.class.getName());
            }
        }
    }
    
    
    /// Impression Pdf
      public String RapportFournisseur() {
        try {
            System.out.println("debut RapportFournisseur ");
           Operation  operationPourLepdf = (Operation) getItems().getRowData();
            System.out.println("id operation  "+operationPourLepdf.getId().toString());
            System.out.println("montant operation "+operationPourLepdf.getMontantoperation());
            JasperPrint jasperPrint = this.obtenirRapportFacture(operationPourLepdf.getId().toString());
            System.out.println("fin jasperprint");
            DateFormat dateFormat1 = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
            Date d = new Date();
            String dd = dateFormat1.format(d);
            String nomFichier = "Rapport_Operation_Fournisseur_" + dd.replace(" ", "_") + ".pdf";
            System.out.println("generer list items");
            RapportUtil.genererListItemsPdf(jasperPrint, nomFichier);
        } catch (Exception edrf) {edrf.printStackTrace();
        }
        return null;
    }
      
        public String rapportConsultation() {
        try {
            System.out.println("debut RapportFournisseur ");
           Operation  operationPourLepdf = (Operation) getItems().getRowData();
            System.out.println("id operation  "+operationPourLepdf.getId().toString());
            System.out.println("montant operation "+operationPourLepdf.getMontantoperation());
            JasperPrint jasperPrint = this.obtenirRapportConsultation(operationPourLepdf.getId().toString());
            System.out.println("fin jasperprint");
            DateFormat dateFormat1 = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
            Date d = new Date();
            String dd = dateFormat1.format(d);
            String nomFichier = "Rapport_Operation_Fournisseur_" + dd.replace(" ", "_") + ".pdf";
            System.out.println("generer list items");
            RapportUtil.genererListItemsPdf(jasperPrint, nomFichier);
        } catch (Exception edrf) {edrf.printStackTrace();
        }
        return null;
    }
      
      private ModeleRapportCom modele;

    public ModeleRapportCom getModele() {
        return modele;
    }

    public void setModele(ModeleRapportCom modele) {
        this.modele = modele;
    }
      
          public JasperPrint obtenirRapportFacture(String idOperation) {
        try {
            System.out.println("fct obtenirRapportFacture");
            List<String> fieldsajoutes = new ArrayList<String>();
            ModeleRapportCom mod1 = modele;
            System.out.println("le modele est : "+modele.getNomModele());
            if ((modele == null)) {
                List<ModeleRapportCom> findByParameter = ejbModele.findByParameter("Select m from ModeleRapportCom m where m.typeOperation.nomTop=:top", "top", "facture");

                if (!(findByParameter.isEmpty())) {
                    System.out.println("modele rapport com size : "+findByParameter.size());
                    mod1 = findByParameter.get(0);
                }
         
            }
            HashMap parameters = new HashMap();
            JRDesignQuery query = new JRDesignQuery();

            query.setText(mod1.getRequette());
            System.out.println("ma requete : "+query.getText());
            JasperDesign jasperDesign = new JasperDesign();
            JRDesignParameter prmCodeInterneOperation = new JRDesignParameter();
            prmCodeInterneOperation.setName("codeInterneOperation");
            prmCodeInterneOperation.setValueClass(String.class);
            prmCodeInterneOperation.setForPrompting(true);
            jasperDesign.addParameter(prmCodeInterneOperation);
            parameters.put("codeInterneOperation", idOperation);
            jasperDesign.setQuery(query);
            jasperDesign.setName("factureDesign");
            jasperDesign.setPageHeight(mod1.getPageHeight());
            jasperDesign.setLeftMargin(mod1.getMargeLeft());
            jasperDesign.setRightMargin(mod1.getMargeRight());
            jasperDesign.setTopMargin(mod1.getMargeTop());
            jasperDesign.setBottomMargin(mod1.getMatgeBottom());
            if (mod1.getOrientation() == 1) {
                jasperDesign.setOrientation(OrientationEnum.PORTRAIT);
            }
            if (mod1.getOrientation() == 2) {
                jasperDesign.setOrientation(OrientationEnum.LANDSCAPE);
            }

            Integer nbLigneParPage = new Integer(0);
            ZoneRapportCom zHead = null;
            ZoneRapportCom zColumnHeader = null;
            ZoneRapportCom zBody = null;
            ZoneRapportCom zFooter = null;
            boolean tracageBackGround=true;
            int debutDetails=0;
            int debutFooter=0;
            int longeurDetails=0;
            try {
                zHead = this.ejbZone.findByParameterSingleResult("Select z from ZoneRapportCom z where z.modele=:modele and z.nomZone='Header'", "modele", mod1);
                zColumnHeader = this.ejbZone.findByParameterSingleResult("Select z from ZoneRapportCom z where z.modele=:modele and z.nomZone='Column Header'", "modele", mod1);
                zBody = this.ejbZone.findByParameterSingleResult("Select z from ZoneRapportCom z where z.modele=:modele and z.nomZone='Body'", "modele", mod1);
                zFooter = this.ejbZone.findByParameterSingleResult("Select z from ZoneRapportCom z where z.modele=:modele and z.nomZone='Footer'", "modele", mod1);
     //       tln("head : "+zHead.getNomZone());racageBackGround= authentificationBean.flagprivilegeTracageFactureComplet;
                System.out.println("head : "+zHead.getNomZone());
                 System.out.println("zColumnHeader : "+zColumnHeader.getNomZone());
                 System.out.println("zBody : "+zBody.getNomZone());
                  System.out.println("zFooter : "+zFooter.getNomZone());
                nbLigneParPage = mod1.getPageHeight() - zHead.getHeightRapport() - zFooter.getHeightRapport() - zColumnHeader.getHeightRapport();
                longeurDetails=nbLigneParPage;
                debutDetails=zHead.getHeightRapport() + zColumnHeader.getHeightRapport();
                nbLigneParPage = nbLigneParPage / zBody.getHeightRapport();
                if (nbLigneParPage > 0) {
                    JRDesignParameter prm22 = new JRDesignParameter();
                    prm22.setName("nbLigneParPage");
                    prm22.setValueClass(Integer.class);
                    prm22.setForPrompting(true);
                    jasperDesign.addParameter(prm22);
                    parameters.put("nbLigneParPage", nbLigneParPage);
                }
            } catch (Exception zer) {
            zer.printStackTrace();
            }
            try {
                JRDesignBand bandHeader = new JRDesignBand();
                bandHeader.setHeight(zHead.getHeightRapport());
                jasperDesign.setPageHeader(bandHeader);

                Iterator i1 = (this.ejbDetailModel.findByParameter("Select d from DetailRapportCom d where d.zoneRapport=:zoneRapport", "zoneRapport", zHead)).iterator();
                while (i1.hasNext()) {
                    DetailRapportCom dd1 = (DetailRapportCom) i1.next();
                    if (dd1.getType().equalsIgnoreCase("info")) {
                        System.out.println("info "+dd1.getNom());
                      
                        JRDesignTextField textField = new JRDesignTextField();
                        textField.setStretchWithOverflow(dd1.getStretchWithOverFlow());
                        textField.setBold(dd1.getGras());
                        textField.setItalic(dd1.getItalique());
                        textField.setUnderline(dd1.getSouligner());
                        textField.setBlankWhenNull(true);
                        if (dd1.getAlignement() == 1) {
                            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                        } else {
                            if (dd1.getAlignement() == 2) {
                                textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                            } else {
                                textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                            }
                        }
                        textField.setHeight(dd1.getHeightRapport());
                        textField.setWidth(dd1.getWidthRapport());
                        textField.setX(dd1.getLeftRapport());
                        textField.getLineBox().setLeftPadding(2);
                        textField.getLineBox().setRightPadding(2);
                        textField.setY(dd1.getTopRapport());
                        String s = "";

                        StringTokenizer tokenDd1 = new StringTokenizer(dd1.getExpression(), "$");
                        while (tokenDd1.hasMoreTokens()) {
                            String valToken = tokenDd1.nextToken();
                            if (valToken.indexOf("F{") > -1) {
                                s = valToken.substring(valToken.indexOf("F{") + 2, valToken.indexOf("}"));
                                System.out.println("colonne : "+s);
                                try {
                                    JRDesignField field = new JRDesignField();
                                    field.setName(s);
                                    field.setValueClass(java.lang.String.class);
                                    if (fieldsajoutes.contains(s) == false) {
                                        jasperDesign.addField(field);
                                        fieldsajoutes.add(s);
                                    }
                                } catch (Exception errre) {
     //                             errre.printStackTrace();
                                }
                            }
                        }
                        JRDesignExpression expression = new JRDesignExpression();
                        expression.setValueClass(java.lang.String.class);
                        expression.setText(dd1.getExpression());
                        System.out.println("expression : "+dd1.getExpression());
                        textField.setExpression(expression);
                        textField.setFontSize(dd1.getFontSize());
                        textField.setForecolor(Color.black);
                        textField.setBackcolor(Color.white);
                        bandHeader.addElement(textField);
                    } else {
                        System.out.println("dd1 expression "+dd1.getExpression());
                        JRDesignStaticText textStatic = new JRDesignStaticText();
                        textStatic.setFontSize(dd1.getFontSize());
                        textStatic.setX(dd1.getLeftRapport());
                        textStatic.setY(dd1.getTopRapport());
                        textStatic.setWidth(dd1.getWidthRapport());
                        textStatic.setHeight(dd1.getHeightRapport());
                        textStatic.setText(dd1.getExpression());
                        textStatic.setBold(dd1.getGras());
                        textStatic.setItalic(dd1.getItalique());
                        textStatic.setUnderline(dd1.getSouligner());
                        if (dd1.getAlignement() == 1) {
                            textStatic.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                        } else {
                            if (dd1.getAlignement() == 2) {
                                textStatic.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                            } else {
                                textStatic.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                            }
                        }
                        textStatic.setForecolor(Color.black);
                        JRDesignStyle hStyle = new JRDesignStyle();
                        //  hStyle.set
                        textStatic.getLineBox().setLeftPadding(2);
                        textStatic.getLineBox().setRightPadding(2);
                        textStatic.setBackcolor(Color.white);
                        bandHeader.addElement(textStatic);
                    }
                }

            } catch (Exception ze1) {
              ze1.printStackTrace();
            }
            Integer minPosition = new Integer(Integer.MAX_VALUE);
            try {
                JRDesignBand bandColumnHeader = new JRDesignBand();
                bandColumnHeader.setHeight(zColumnHeader.getHeightRapport());
                jasperDesign.setColumnHeader(bandColumnHeader);
                Iterator i1 = (this.ejbDetailModel.findByParameter("Select d from DetailRapportCom d where d.zoneRapport=:zoneRapport", "zoneRapport", zColumnHeader)).iterator();
                JRDesignLine lineTopColumnHeader = null;
                if (zColumnHeader.getTracageLigne()) {
                    lineTopColumnHeader = new JRDesignLine();
                    lineTopColumnHeader.setBackcolor(Color.black);
                    lineTopColumnHeader.setForecolor(Color.black);
                    lineTopColumnHeader.setY(0);
                    lineTopColumnHeader.setMode(ModeEnum.OPAQUE);
                    //         lineTopColumnHeader.setStretchType(stretchType);
                }
                Integer lastIndex = null;
                while (i1.hasNext()) {
                    DetailRapportCom dd1 = (DetailRapportCom) i1.next();
                    if (dd1.getType().equalsIgnoreCase("fixe")) {
                        System.out.println("champ fixe colonne:"+dd1.getNom());
                        if (zColumnHeader.getTracageLigne()) {
                            JRDesignLine l = new JRDesignLine();
                            l.setBackcolor(Color.black);
                            l.setForecolor(Color.black);
                            l.setX(dd1.getLeftRapport());
                            l.setY(0);
                            l.setMode(ModeEnum.OPAQUE);
                            l.setWidth(1);
                            l.setHeight(zColumnHeader.getHeightRapport());
                            bandColumnHeader.addElement(l);
                            if (dd1.getLeftRapport() < minPosition) {
                                minPosition = dd1.getLeftRapport();
                            }
                        }
                        JRDesignStaticText textStatic = new JRDesignStaticText();
                        textStatic.setFontSize(dd1.getFontSize());
                        textStatic.setX(dd1.getLeftRapport());
                        textStatic.setY(dd1.getTopRapport());
                        textStatic.setWidth(dd1.getWidthRapport());
                        textStatic.getLineBox().setLeftPadding(2);
                        textStatic.getLineBox().setRightPadding(2);
                        textStatic.getLineBox().setTopPadding(1);
                        textStatic.getLineBox().setBottomPadding(1);
                        textStatic.setHeight(dd1.getHeightRapport());
                        textStatic.setText(dd1.getExpression());
                        textStatic.setBold(dd1.getGras());
                        textStatic.setItalic(dd1.getItalique());
                        textStatic.setUnderline(dd1.getSouligner());
                        if (dd1.getAlignement() == 1) {
                            textStatic.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                        } else {
                            if (dd1.getAlignement() == 2) {
                                textStatic.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                            } else {
                                textStatic.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                            }
                        }
                        textStatic.setForecolor(Color.black);
                        textStatic.setMode(ModeEnum.TRANSPARENT);
                        textStatic.setBackcolor(Color.white);
                        lastIndex = textStatic.getWidth() + textStatic.getX();
                        bandColumnHeader.addElement(textStatic);
                    }
                }
                if (zColumnHeader.getTracageLigne()) {
                    if (lastIndex != null) {
                        JRDesignLine l = new JRDesignLine();
                        l.setBackcolor(Color.black);
                        l.setForecolor(Color.black);
                        l.setX(lastIndex);
                        l.setY(0);
                        l.setMode(ModeEnum.OPAQUE);
                        l.setWidth(1);
                        l.setHeight(zColumnHeader.getHeightRapport());
                        bandColumnHeader.addElement(l);
                        lineTopColumnHeader.setHeight(1);
                        lineTopColumnHeader.setWidth(lastIndex - minPosition);
                        lineTopColumnHeader.setX(minPosition);
                        bandColumnHeader.addElement(lineTopColumnHeader);
                        JRDesignLine l2 = new JRDesignLine();
                        l2.setBackcolor(Color.black);
                        l2.setForecolor(Color.black);
                        l2.setX(minPosition);
                        l2.setY(bandColumnHeader.getHeight() - 1);
                        l2.setMode(ModeEnum.OPAQUE);
                        l2.setWidth(lastIndex - minPosition);
                        l2.setHeight(1);
                        bandColumnHeader.addElement(l2);
                    }
                }
            } catch (Exception ze1) {
             ze1.printStackTrace();
            }
            Integer lastIndex2 = null;
            try {
                if (nbLigneParPage > 0) {
                    JRDesignParameter prm2 = new JRDesignParameter();
                    prm2.setName("bodySize");
                    prm2.setValueClass(Integer.class);
                    prm2.setForPrompting(true);
                    jasperDesign.addParameter(prm2);
                    Integer ints = this.ejbdetailoperation.findByParameterSingleResultCount("Select count(d.detailoperationPK.ordre) from Detailoperation d where d.operation.id=:niOp", "niOp", new BigDecimal(idOperation).setScale(0, RoundingMode.HALF_UP)).intValue();
                    System.out.println("ints "+ints);
                    if (ints==0)
                    {ints=1;}
                    parameters.put("bodySize", ints);
                    jasperDesign.setLanguage(JasperReport.LANGUAGE_GROOVY);
                    JRDesignField niPro = new JRDesignField();
                    niPro.setName("typeoperation");
                    niPro.setValueClass(String.class);
                    jasperDesign.addField(niPro);
                    JRDesignVariable varCal = new JRDesignVariable();
                    varCal.setName("count");
                    varCal.setCalculation(CalculationEnum.COUNT);
                    JRDesignExpression jvarCal = new JRDesignExpression();
                    jvarCal.setText("$F{typeoperation}");
                    jvarCal.setValueClass(String.class);
                    varCal.setExpression(jvarCal);
                    varCal.setValueClass(Integer.class);
                    varCal.setResetType(ResetTypeEnum.REPORT);
                    jasperDesign.addVariable(varCal);
                    JRDesignVariable varCalPage = new JRDesignVariable();
                    varCalPage.setName("countPage");
                    varCalPage.setCalculation(CalculationEnum.COUNT);
                    JRDesignExpression jvarCalPage = new JRDesignExpression();
                    jvarCalPage.setText("$F{typeoperation}");
                    jvarCalPage.setValueClass(String.class);
                    varCalPage.setExpression(jvarCalPage);
                    varCalPage.setValueClass(Integer.class);
                    varCalPage.setResetType(ResetTypeEnum.PAGE);
                    jasperDesign.addVariable(varCalPage);
                    JRDesignVariable var = new JRDesignVariable();
                    var.setName("lastLine");
                    JRDesignExpression jj = new JRDesignExpression();
                    jj.setText("(($V{count}==$P{bodySize})||($V{countPage}==$P{nbLigneParPage}))?true:false");
                    jj.setValueClass(Boolean.class);
                    var.setExpression(jj);
                    var.setValueClass(Boolean.class);
                    jasperDesign.addVariable(var);
                }
                JRDesignBand bandBody = new JRDesignBand();
                bandBody.setHeight(zBody.getHeightRapport());
//                jasperDesign.setDetail(bandBody);

                ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandBody);
                
                
                JRDesignBand bandBackGround = new JRDesignBand();
           if(tracageBackGround)
           {      
bandBackGround.setHeight(mod1.getPageHeight());

jasperDesign.setBackground(bandBackGround);
           }
                Iterator i1 = (this.ejbDetailModel.findByParameter("Select d from DetailRapportCom d where d.zoneRapport=:zoneRapport", "zoneRapport", zBody)).iterator();
                while (i1.hasNext()) {
                    DetailRapportCom dd1 = (DetailRapportCom) i1.next();
                    JRDesignLine l = null;
               //     if (zBody.getTracageLigne() && authentificationBean.getFlagprivilegeTracageFactureComplet()==false) {
  if (zBody.getTracageLigne()) {

                        l = new JRDesignLine();
                        l.setBackcolor(Color.black);
                        l.setForecolor(Color.black);
                        l.setX(dd1.getLeftRapport());
                        l.setY(0);
                        l.setMode(ModeEnum.OPAQUE);
                        l.setWidth(1);
                        //   l.setMode(ModeEnum.OPAQUE);
                      l.setHeight(zBody.getHeightRapport());
             

                        bandBody.addElement(l);
                        if (lastIndex2 == null) {
                            lastIndex2 = new Integer(0);
                        }
                        if (lastIndex2 < dd1.getLeftRapport() + dd1.getWidthRapport()) {
                            lastIndex2 = dd1.getLeftRapport() + dd1.getWidthRapport();
                        }
                    }
             //      else  if (tracageBackGround){
                 if (tracageBackGround){       
                     JRDesignLine l1 = null;
                  
                        l1 = new JRDesignLine();
                        l1.setBackcolor(Color.black);
                        l1.setForecolor(Color.black);
                        l1.setX(dd1.getLeftRapport());
                        l1.setY(debutDetails);
                        l1.setMode(ModeEnum.OPAQUE);
                        l1.setWidth(1);
                        //   l.setMode(ModeEnum.OPAQUE);
                      l1.setHeight(longeurDetails);
                    //    System.out.println("ligne Ver ");

                        bandBackGround.addElement(l1);
                    
                     if (lastIndex2 == null) {
                            lastIndex2 = new Integer(0);
                        }
                        if (lastIndex2 < dd1.getLeftRapport() + dd1.getWidthRapport()) {
                            lastIndex2 = dd1.getLeftRapport() + dd1.getWidthRapport();
                        }
                    }
                    if (dd1.getType().equals("info")) {
                        if (dd1.getExpression().toLowerCase().contains("image")) {
                            String s = "";


                            StringTokenizer tokenDd1 = new StringTokenizer(dd1.getExpression(), "$");
                            while (tokenDd1.hasMoreTokens()) {
                                String valToken = tokenDd1.nextToken();
                                if (valToken.indexOf("F{") > -1) {
                                    s = valToken.substring(valToken.indexOf("F{") + 2, valToken.indexOf("}"));
                                     System.out.println("s 2:"+s);
                                    try {
                                        JRDesignField field = new JRDesignField();
                                        field.setName(s);
                                        field.setValueClass(java.lang.String.class);
                                        if (fieldsajoutes.contains(s) == false) {
                                            jasperDesign.addField(field);
                                            fieldsajoutes.add(s);
                                        }
                                    } catch (Exception errre) {
       
     //                                   errre.printStackTrace();
                                    }
                                }
                            }
                            JRDesignImage imageFacture = new JRDesignImage(jasperDesign);
                            imageFacture.setX(dd1.getLeftRapport() + 1);
                            imageFacture.setY(dd1.getTopRapport());
                            imageFacture.setWidth(dd1.getWidthRapport() - 1);
                            imageFacture.setHeight(imageFacture.getWidth() + 1);
                            if ((bandBody.getHeight()) < (imageFacture.getHeight() + imageFacture.getY())) {
                                bandBody.setHeight(imageFacture.getHeight() + imageFacture.getY());
                            }
                            JRDesignExpression expressionImage = new JRDesignExpression();
                            expressionImage.setValueClass(String.class);
                            String realPathWebFolder = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/web/");
                            if (realPathWebFolder.endsWith("\\web\\web")) {
                                realPathWebFolder = realPathWebFolder.substring(0, realPathWebFolder.lastIndexOf("\\web"));
                            }
                            realPathWebFolder = realPathWebFolder.replace("\\", "\\\\");
                            expressionImage.setText("\"" + realPathWebFolder + "\"" + "+" + dd1.getExpression());
                            imageFacture.setExpression(expressionImage);
                            imageFacture.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                            imageFacture.setOnErrorType(OnErrorTypeEnum.BLANK);
                            bandBody.addElement(imageFacture);
                        } else {
                            JRDesignTextField textField = new JRDesignTextField();
                            textField.setStretchWithOverflow(dd1.getStretchWithOverFlow());
                            textField.setBold(dd1.getGras());
                            textField.setItalic(dd1.getItalique());
                            textField.setUnderline(dd1.getSouligner());
                            textField.setBlankWhenNull(true);
                            if (dd1.getAlignement() == 1) {
                                textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                            } else {
                                if (dd1.getAlignement() == 2) {
                                    textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                                } else {
                                    textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                                }
                            }
                            textField.setHeight(dd1.getHeightRapport());
                            textField.setWidth(dd1.getWidthRapport());
                            textField.setX(dd1.getLeftRapport());
                            textField.setY(dd1.getTopRapport());
                            String s = "";

                            StringTokenizer tokenDd1 = new StringTokenizer(dd1.getExpression(), "$");
                            while (tokenDd1.hasMoreTokens()) {
                                String valToken = tokenDd1.nextToken();
                                if (valToken.indexOf("F{") > -1) {
                                    s = valToken.substring(valToken.indexOf("F{") + 2, valToken.indexOf("}"));
                                     System.out.println("s3 :"+s);
                                    try {
                                        JRDesignField field = new JRDesignField();
                                        field.setName(s);
                                        field.setValueClass(java.lang.String.class);
                                        if (fieldsajoutes.contains(s) == false) {
                                            jasperDesign.addField(field);
                                            fieldsajoutes.add(s);
                                        }
                                    } catch (Exception errre) {
                                   errre.printStackTrace();
                                    }
                                }
                            }
                            JRDesignExpression expression = new JRDesignExpression();
                            expression.setValueClass(java.lang.String.class);
                            expression.setText(dd1.getExpression());
                            textField.setExpression(expression);
                            textField.setFontSize(dd1.getFontSize());
                            textField.setForecolor(Color.black);
                            textField.setBackcolor(Color.white);
                            textField.getLineBox().setTopPadding(1);
                            textField.getLineBox().setLeftPadding(2);
                            textField.getLineBox().setRightPadding(2);
                            textField.getLineBox().setBottomPadding(1);
                            textField.setMode(ModeEnum.TRANSPARENT);
                            bandBody.addElement(textField);
                        }
                    }
                }
              //     if (zBody.getTracageLigne() && authentificationBean.getFlagprivilegeTracageFactureComplet()==false) {
//                if (zBody.getTracageLigne()) {
//                    if (lastIndex2 != null) {
//                        JRDesignLine l = new JRDesignLine();
//                        l.setBackcolor(Color.black);
//                        l.setForecolor(Color.black);
//                        l.setX(lastIndex2);
//                        l.setY(0);
//                        l.setMode(ModeEnum.OPAQUE);
//                        l.setWidth(1);
//                        l.setHeight(bandBody.getHeight());
//                        bandBody.addElement(l);
//                        if (nbLigneParPage > 0) {
//                            JRDesignLine l2 = new JRDesignLine();
//                            l2.setBackcolor(Color.black);
//                            l2.setForecolor(Color.black);
//                            l2.setX(minPosition);
//                            l2.setY(bandBody.getHeight() - 1);
//                            l2.setMode(ModeEnum.OPAQUE);
//                            l2.setWidth(lastIndex2 - minPosition);
//                            l2.setHeight(1);
//                            l2.setPositionType(PositionTypeEnum.FIX_RELATIVE_TO_BOTTOM);
//                            JRDesignExpression ex = new JRDesignExpression();
//                            ex.setText("$V{lastLine}");
//                            ex.setValueClass(Boolean.class);
//                            l2.setPrintWhenExpression(ex);
//                            bandBody.addElement(l2);
//                        }
//                    }
//                }
                 if (tracageBackGround){
                
                    JRDesignLine l1 = new JRDesignLine();
                        l1.setBackcolor(Color.black);
                        l1.setForecolor(Color.black);
                        l1.setX(lastIndex2);
                        l1.setY(debutDetails);
                        l1.setMode(ModeEnum.OPAQUE);
                        l1.setWidth(1);
                        l1.setHeight(longeurDetails);
                        bandBackGround.addElement(l1);
                        if (nbLigneParPage > 0) {
                            JRDesignLine l21 = new JRDesignLine();
                            l21.setBackcolor(Color.black);
                            l21.setForecolor(Color.black);
                            l21.setX(minPosition);
                            l21.setY(debutDetails+longeurDetails - 1);
                            l21.setMode(ModeEnum.OPAQUE);
                            l21.setWidth(lastIndex2 - minPosition);
                            l21.setHeight(1);
                            l21.setPositionType(PositionTypeEnum.FIX_RELATIVE_TO_BOTTOM);
//                            JRDesignExpression ex = new JRDesignExpression();
//                            ex.setText("$V{lastLine}");
//                            ex.setValueClass(Boolean.class);
//                            l21.setPrintWhenExpression(ex);
                            bandBackGround.addElement(l21);
                        }
                
                
                
                }
            } catch (Exception ze1) {
              ze1.printStackTrace();
            }
            JRDesignBand bandFooter = null;
            try {
                bandFooter = new JRDesignBand();
                bandFooter.setHeight(zFooter.getHeightRapport());
                jasperDesign.setPageFooter(bandFooter);
                Iterator i1 = (this.ejbDetailModel.findByParameter("Select d from DetailRapportCom d where d.zoneRapport=:zoneRapport", "zoneRapport", zFooter)).iterator();
                if (nbLigneParPage == 0) {
                    if (zFooter.getTracageLigne()) {
                        if (lastIndex2 != null) {
                            JRDesignLine l = new JRDesignLine();
                            l.setBackcolor(Color.black);
                            l.setForecolor(Color.black);
                            l.setX(minPosition);
                            l.setY(0);
                            l.setMode(ModeEnum.OPAQUE);
                            l.setWidth(lastIndex2 - minPosition);
                            l.setHeight(1);
                            bandFooter.addElement(l);
                        }
                    }
                }
                while (i1.hasNext()) {
                    DetailRapportCom dd1 = (DetailRapportCom) i1.next();
                    if (dd1.getType().equalsIgnoreCase("info")) {
                        System.out.println("info footer "+dd1.getNom());
                        JRDesignTextField textField = new JRDesignTextField();
                        textField.setStretchWithOverflow(dd1.getStretchWithOverFlow());
                        textField.setBold(dd1.getGras());
                        textField.setItalic(dd1.getItalique());
                        textField.setUnderline(dd1.getSouligner());
                        textField.setBlankWhenNull(true);
                        textField.getLineBox().setTopPadding(1);
                        if (dd1.getAlignement() == 1) {
                            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                        } else {
                            if (dd1.getAlignement() == 2) {
                                textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                            } else {
                                textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                            }
                        }
                        textField.setHeight(dd1.getHeightRapport());
                        textField.setWidth(dd1.getWidthRapport());
                        textField.setX(dd1.getLeftRapport());
                        textField.setY(dd1.getTopRapport());
                        JRDesignExpression ex = new JRDesignExpression();
                        ex.setText("$V{count}==$P{bodySize}");
                        ex.setValueClass(Boolean.class);
                        textField.setPrintWhenExpression(ex);
                        String s = "";

                        StringTokenizer tokenDd1 = new StringTokenizer(dd1.getExpression(), "$");
                        while (tokenDd1.hasMoreTokens()) {
                            String valToken = tokenDd1.nextToken();
                            if (valToken.indexOf("F{") > -1) {
                                s = valToken.substring(valToken.indexOf("F{") + 2, valToken.indexOf("}"));
                                System.out.println("s footer "+s);
                                try {
                                    JRDesignField field = new JRDesignField();
                                    field.setName(s);
                                    field.setValueClass(java.lang.String.class);
                                    if (fieldsajoutes.contains(s) == false) {
                                        jasperDesign.addField(field);
                                        fieldsajoutes.add(s);
                                    }
                                } catch (Exception errre) {
                                errre.printStackTrace();
                                }
                            }
                        }
                        JRDesignExpression expression = new JRDesignExpression();
                        expression.setValueClass(java.lang.String.class);
                        expression.setText(dd1.getExpression());
                        System.out.println("exxxxpre "+dd1.getExpression());
                        textField.setExpression(expression);
                        textField.setFontSize(dd1.getFontSize());
                        textField.setForecolor(Color.black);
                        textField.getLineBox().setLeftPadding(2);
                        textField.getLineBox().setRightPadding(2);
                        textField.setBackcolor(Color.white);
                        bandFooter.addElement(textField);
                    } else {
                        JRDesignStaticText textStatic = new JRDesignStaticText();
                        textStatic.setFontSize(dd1.getFontSize());
                        textStatic.setX(dd1.getLeftRapport());
                        textStatic.setY(dd1.getTopRapport());
                        textStatic.setWidth(dd1.getWidthRapport());
                        textStatic.setHeight(dd1.getHeightRapport());
                        textStatic.setText(dd1.getExpression());
                        textStatic.setBold(dd1.getGras());
                        JRDesignExpression ex = new JRDesignExpression();
                        ex.setText("$V{count}==$P{bodySize}");
                        ex.setValueClass(Boolean.class);
                        textStatic.setPrintWhenExpression(ex);
                        textStatic.getLineBox().setTopPadding(1);
                        textStatic.getLineBox().setLeftPadding(2);
                        textStatic.getLineBox().setRightPadding(2);
                        textStatic.setItalic(dd1.getItalique());
                        textStatic.setUnderline(dd1.getSouligner());
                        if (dd1.getAlignement() == 1) {
                            textStatic.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                        } else {
                            if (dd1.getAlignement() == 2) {
                                textStatic.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                            } else {
                                textStatic.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                            }
                        }
                        textStatic.setForecolor(Color.black);
                        textStatic.setBackcolor(Color.white);
                        bandFooter.addElement(textStatic);
                    }
                }

            } catch (Exception ze1) {
             ze1.printStackTrace();
            }
    //        System.out.println("idOperation avant montantTTcTouteLettresDepuisFacture "+new BigDecimal(idOperation).setScale(0, RoundingMode.HALF_UP));
            String montantToutLettre = this.montantTTcTouteLettresDepuisFacture(new BigDecimal(idOperation).setScale(0, RoundingMode.UP));
            System.out.println("montantToutLettre : "+montantToutLettre);
            JRDesignParameter prm1 = new JRDesignParameter();
            prm1.setName("montantTTcTouteLettres");
            prm1.setValueClass(java.lang.String.class);
            prm1.setForPrompting(true);
            jasperDesign.addParameter(prm1);
            parameters.put("montantTTcTouteLettres", montantToutLettre);
            JRDesignParameter prm13 = new JRDesignParameter();
            prm13.setName("reglements");
            prm13.setValueClass(java.lang.String.class);
            prm13.setForPrompting(true);
            jasperDesign.addParameter(prm13);

    // comm        parameters.put("reglements", reglements(idOperation));
            if (bandFooter != null) {
                JRDesignStaticText textStatic = new JRDesignStaticText();
                textStatic.setFontSize(8);
                textStatic.setX(0);
                textStatic.setY(bandFooter.getHeight() - 65);
                textStatic.setWidth(250);
                textStatic.setHeight(15);
                textStatic.setText("page");
                textStatic.getLineBox().setTopPadding(1);
                textStatic.getLineBox().setLeftPadding(1);
                textStatic.getLineBox().setRightPadding(1);
                textStatic.getLineBox().setBottomPadding(1);
                textStatic.setForecolor(Color.black);
                textStatic.setBackcolor(Color.white);
                textStatic.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                bandFooter.addElement(textStatic);
                JRDesignTextField textField = new JRDesignTextField();
                textField.getLineBox().setTopPadding(1);
                textField.getLineBox().setLeftPadding(1);
                textField.getLineBox().setRightPadding(1);
                textField.getLineBox().setBottomPadding(1);
                textField.setHeight(15);
                textField.setWidth(30);
                textField.setX(250);
                textField.setY(bandFooter.getHeight() - 65);
                textField.setFontSize(8);
                JRDesignExpression ex = new JRDesignExpression();
                ex.setText(" $V{PAGE_NUMBER}");
                ex.setValueClass(String.class);
                textField.setExpression(ex);
                textField.setEvaluationTime(EvaluationTimeEnum.NOW);
                bandFooter.addElement(textField);
                JRDesignStaticText textStatic2 = new JRDesignStaticText();
                textStatic2.setFontSize(6);
                textStatic2.setX(280);
                textStatic2.setY(bandFooter.getHeight() - 65);
                textStatic2.setWidth(6);
                textStatic2.setHeight(15);
                textStatic2.setText(" /");
                textStatic2.getLineBox().setTopPadding(1);
                textStatic2.getLineBox().setLeftPadding(1);
                textStatic2.getLineBox().setRightPadding(1);
                textStatic2.getLineBox().setBottomPadding(1);
                textStatic2.setForecolor(Color.black);
                textStatic2.setBackcolor(Color.white);
                textStatic2.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                bandFooter.addElement(textStatic2);
                JRDesignTextField textField2 = new JRDesignTextField();
                textField2.getLineBox().setTopPadding(1);
                textField2.getLineBox().setLeftPadding(1);
                textField2.getLineBox().setRightPadding(1);
                textField2.getLineBox().setBottomPadding(1);
                textField2.setHeight(15);
                textField2.setWidth(30);
                textField2.setX(286);
                textField2.setY(bandFooter.getHeight() - 65);
                textField2.setFontSize(8);
                JRDesignExpression ex2 = new JRDesignExpression();
                ex2.setText(" $V{PAGE_NUMBER}");
                ex2.setValueClass(String.class);
                textField2.setExpression(ex2);
                textField2.setEvaluationTime(EvaluationTimeEnum.REPORT);
                bandFooter.addElement(textField2);
            }
            JRDesignParameter prm12 = new JRDesignParameter();
            prm12.setName("user_operation");
            prm12.setValueClass(java.lang.String.class);
            prm12.setForPrompting(true);
            jasperDesign.addParameter(prm12);
            StringTokenizer getUrl = new StringTokenizer(this.getFacade().urlCourante(), "**");
            String url = getUrl.nextToken();
            String login = getUrl.nextToken();
            String password = getUrl.nextToken();
            String nomBaseDeDonnes = url.substring(url.lastIndexOf("/") + 1);
      //      System.out.println("id Operation " + idOperation);

            //   parameters.put("user_operation", this.userEditerPar(idOperation));
           String user1="su";
            try {
        Operation    o = getFacade().findByParameterSingleResult("Select o from Operation o where o.id=:idOperation", "idOperation", new Long(idOperation));
        user1= o.getUser().getLogin();
                  System.out.println("user1 "+user1);
              }
              catch(Exception e)
              {
                  e.printStackTrace();
              }
                    
                          parameters.put("user_operation", user1);

      //      JRDesignParameter prmReg = new JRDesignParameter();
      //      prmReg.setName("reglement_chaine");
      //      prmReg.setValueClass(java.lang.String.class);
     //       prmReg.setForPrompting(true);
     //       jasperDesign.addParameter(prmReg);
     //comm       parameters.put("reglement_chaine", this.reglementDeLaFactureEnChaine(idOperation));
   
            JRFileVirtualizer virtualizer = new JRFileVirtualizer(JsfClasses.util.ParametresRapports.getNbMaxPageToCut(), JsfClasses.util.ParametresRapports.getPathRapportTemporaire());
            parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
      
            String defaultPDFFont = "Draft 15cpi";

  //          JRProperties.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
  //          JRProperties.setProperty("net.sf.jasperreports.default.font.name", defaultPDFFont);
//            JRProperties.setProperty("net.sf.jasperreports.default.pdf.embedded", "false");

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        //    System.out.println("apres compilation");
            Driver monDriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(monDriver);
            Connection connection = DriverManager.getConnection(url, login, password);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            connection.close();
       //     System.out.println("fin");
            return jasperPrint;
        } catch (Exception e) {
      e.printStackTrace();
        }
        return null;
    }
 
          
              public JasperPrint obtenirRapportConsultation(String idOperation) {
        try {
            System.out.println("fct obtenirRapportConsultation");
            List<String> fieldsajoutes = new ArrayList<String>();
            ModeleRapportCom mod1 = modele;
            System.out.println("le modele est : "+modele.getNomModele());
            if ((modele == null)) {
                List<ModeleRapportCom> findByParameter = ejbModele.findByParameter("Select m from ModeleRapportCom m where m.typeOperation.nomTop=:top", "top", "visite patient");

                if (!(findByParameter.isEmpty())) {
                    System.out.println("modele rapport com size : "+findByParameter.size());
                    mod1 = findByParameter.get(0);
                }
         
            }
            HashMap parameters = new HashMap();
            JRDesignQuery query = new JRDesignQuery();

            query.setText(mod1.getRequette());
            System.out.println("ma requete : "+query.getText());
            JasperDesign jasperDesign = new JasperDesign();
            JRDesignParameter prmCodeInterneOperation = new JRDesignParameter();
            prmCodeInterneOperation.setName("codeInterneOperation");
            prmCodeInterneOperation.setValueClass(String.class);
            prmCodeInterneOperation.setForPrompting(true);
            jasperDesign.addParameter(prmCodeInterneOperation);
            parameters.put("codeInterneOperation", idOperation);
            jasperDesign.setQuery(query);
            jasperDesign.setName("factureDesign");
            jasperDesign.setPageHeight(mod1.getPageHeight());
            jasperDesign.setLeftMargin(mod1.getMargeLeft());
            jasperDesign.setRightMargin(mod1.getMargeRight());
            jasperDesign.setTopMargin(mod1.getMargeTop());
            jasperDesign.setBottomMargin(mod1.getMatgeBottom());
            if (mod1.getOrientation() == 1) {
                jasperDesign.setOrientation(OrientationEnum.PORTRAIT);
            }
            if (mod1.getOrientation() == 2) {
                jasperDesign.setOrientation(OrientationEnum.LANDSCAPE);
            }

            Integer nbLigneParPage = new Integer(0);
            ZoneRapportCom zHead = null;
            ZoneRapportCom zColumnHeader = null;
            ZoneRapportCom zBody = null;
            ZoneRapportCom zFooter = null;
            boolean tracageBackGround=true;
            int debutDetails=0;
            int debutFooter=0;
            int longeurDetails=0;
            try {
                zHead = this.ejbZone.findByParameterSingleResult("Select z from ZoneRapportCom z where z.modele=:modele and z.nomZone='Header'", "modele", mod1);
                zColumnHeader = this.ejbZone.findByParameterSingleResult("Select z from ZoneRapportCom z where z.modele=:modele and z.nomZone='Column Header'", "modele", mod1);
                zBody = this.ejbZone.findByParameterSingleResult("Select z from ZoneRapportCom z where z.modele=:modele and z.nomZone='Body'", "modele", mod1);
                zFooter = this.ejbZone.findByParameterSingleResult("Select z from ZoneRapportCom z where z.modele=:modele and z.nomZone='Footer'", "modele", mod1);
     //       tln("head : "+zHead.getNomZone());racageBackGround= authentificationBean.flagprivilegeTracageFactureComplet;
                System.out.println("head : "+zHead.getNomZone());
                 System.out.println("zColumnHeader : "+zColumnHeader.getNomZone());
                 System.out.println("zBody : "+zBody.getNomZone());
                  System.out.println("zFooter : "+zFooter.getNomZone());
                nbLigneParPage = mod1.getPageHeight() - zHead.getHeightRapport() - zFooter.getHeightRapport() - zColumnHeader.getHeightRapport();
                longeurDetails=nbLigneParPage;
                debutDetails=zHead.getHeightRapport() + zColumnHeader.getHeightRapport();
                nbLigneParPage = nbLigneParPage / zBody.getHeightRapport();
                if (nbLigneParPage > 0) {
                    JRDesignParameter prm22 = new JRDesignParameter();
                    prm22.setName("nbLigneParPage");
                    prm22.setValueClass(Integer.class);
                    prm22.setForPrompting(true);
                    jasperDesign.addParameter(prm22);
                    parameters.put("nbLigneParPage", nbLigneParPage);
                }
            } catch (Exception zer) {
            zer.printStackTrace();
            }
            try {
                JRDesignBand bandHeader = new JRDesignBand();
                bandHeader.setHeight(zHead.getHeightRapport());
                jasperDesign.setPageHeader(bandHeader);

                Iterator i1 = (this.ejbDetailModel.findByParameter("Select d from DetailRapportCom d where d.zoneRapport=:zoneRapport", "zoneRapport", zHead)).iterator();
                while (i1.hasNext()) {
                    DetailRapportCom dd1 = (DetailRapportCom) i1.next();
                    if (dd1.getType().equalsIgnoreCase("info")) {
                        System.out.println("info "+dd1.getNom());
                      
                        JRDesignTextField textField = new JRDesignTextField();
                        textField.setStretchWithOverflow(dd1.getStretchWithOverFlow());
                        textField.setBold(dd1.getGras());
                        textField.setItalic(dd1.getItalique());
                        textField.setUnderline(dd1.getSouligner());
                        textField.setBlankWhenNull(true);
                        if (dd1.getAlignement() == 1) {
                            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                        } else {
                            if (dd1.getAlignement() == 2) {
                                textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                            } else {
                                textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                            }
                        }
                        textField.setHeight(dd1.getHeightRapport());
                        textField.setWidth(dd1.getWidthRapport());
                        textField.setX(dd1.getLeftRapport());
                        textField.getLineBox().setLeftPadding(2);
                        textField.getLineBox().setRightPadding(2);
                        textField.setY(dd1.getTopRapport());
                        String s = "";

                        StringTokenizer tokenDd1 = new StringTokenizer(dd1.getExpression(), "$");
                        while (tokenDd1.hasMoreTokens()) {
                            String valToken = tokenDd1.nextToken();
                            if (valToken.indexOf("F{") > -1) {
                                s = valToken.substring(valToken.indexOf("F{") + 2, valToken.indexOf("}"));
                                System.out.println("colonne : "+s);
                                try {
                                    JRDesignField field = new JRDesignField();
                                    field.setName(s);
                                    field.setValueClass(java.lang.String.class);
                                    if (fieldsajoutes.contains(s) == false) {
                                        jasperDesign.addField(field);
                                        fieldsajoutes.add(s);
                                    }
                                } catch (Exception errre) {
     //                             errre.printStackTrace();
                                }
                            }
                        }
                        JRDesignExpression expression = new JRDesignExpression();
                        expression.setValueClass(java.lang.String.class);
                        expression.setText(dd1.getExpression());
                        System.out.println("expression : "+dd1.getExpression());
                        textField.setExpression(expression);
                        textField.setFontSize(dd1.getFontSize());
                        textField.setForecolor(Color.black);
                        textField.setBackcolor(Color.white);
                        bandHeader.addElement(textField);
                    } else {
                        System.out.println("dd1 expression "+dd1.getExpression());
                        JRDesignStaticText textStatic = new JRDesignStaticText();
                        textStatic.setFontSize(dd1.getFontSize());
                        textStatic.setX(dd1.getLeftRapport());
                        textStatic.setY(dd1.getTopRapport());
                        textStatic.setWidth(dd1.getWidthRapport());
                        textStatic.setHeight(dd1.getHeightRapport());
                        textStatic.setText(dd1.getExpression());
                        textStatic.setBold(dd1.getGras());
                        textStatic.setItalic(dd1.getItalique());
                        textStatic.setUnderline(dd1.getSouligner());
                        if (dd1.getAlignement() == 1) {
                            textStatic.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                        } else {
                            if (dd1.getAlignement() == 2) {
                                textStatic.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                            } else {
                                textStatic.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                            }
                        }
                        textStatic.setForecolor(Color.black);
                        JRDesignStyle hStyle = new JRDesignStyle();
                        //  hStyle.set
                        textStatic.getLineBox().setLeftPadding(2);
                        textStatic.getLineBox().setRightPadding(2);
                        textStatic.setBackcolor(Color.white);
                        bandHeader.addElement(textStatic);
                    }
                }

            } catch (Exception ze1) {
              ze1.printStackTrace();
            }
            Integer minPosition = new Integer(Integer.MAX_VALUE);
            try {
                JRDesignBand bandColumnHeader = new JRDesignBand();
                bandColumnHeader.setHeight(zColumnHeader.getHeightRapport());
                jasperDesign.setColumnHeader(bandColumnHeader);
                Iterator i1 = (this.ejbDetailModel.findByParameter("Select d from DetailRapportCom d where d.zoneRapport=:zoneRapport", "zoneRapport", zColumnHeader)).iterator();
                JRDesignLine lineTopColumnHeader = null;
                if (zColumnHeader.getTracageLigne()) {
                    lineTopColumnHeader = new JRDesignLine();
                    lineTopColumnHeader.setBackcolor(Color.black);
                    lineTopColumnHeader.setForecolor(Color.black);
                    lineTopColumnHeader.setY(0);
                    lineTopColumnHeader.setMode(ModeEnum.OPAQUE);
                    //         lineTopColumnHeader.setStretchType(stretchType);
                }
                Integer lastIndex = null;
                while (i1.hasNext()) {
                    DetailRapportCom dd1 = (DetailRapportCom) i1.next();
                    if (dd1.getType().equalsIgnoreCase("fixe")) {
                        System.out.println("champ fixe colonne:"+dd1.getNom());
                        if (zColumnHeader.getTracageLigne()) {
                            JRDesignLine l = new JRDesignLine();
                            l.setBackcolor(Color.black);
                            l.setForecolor(Color.black);
                            l.setX(dd1.getLeftRapport());
                            l.setY(0);
                            l.setMode(ModeEnum.OPAQUE);
                            l.setWidth(1);
                            l.setHeight(zColumnHeader.getHeightRapport());
                            bandColumnHeader.addElement(l);
                            if (dd1.getLeftRapport() < minPosition) {
                                minPosition = dd1.getLeftRapport();
                            }
                        }
                        JRDesignStaticText textStatic = new JRDesignStaticText();
                        textStatic.setFontSize(dd1.getFontSize());
                        textStatic.setX(dd1.getLeftRapport());
                        textStatic.setY(dd1.getTopRapport());
                        textStatic.setWidth(dd1.getWidthRapport());
                        textStatic.getLineBox().setLeftPadding(2);
                        textStatic.getLineBox().setRightPadding(2);
                        textStatic.getLineBox().setTopPadding(1);
                        textStatic.getLineBox().setBottomPadding(1);
                        textStatic.setHeight(dd1.getHeightRapport());
                        textStatic.setText(dd1.getExpression());
                        textStatic.setBold(dd1.getGras());
                        textStatic.setItalic(dd1.getItalique());
                        textStatic.setUnderline(dd1.getSouligner());
                        if (dd1.getAlignement() == 1) {
                            textStatic.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                        } else {
                            if (dd1.getAlignement() == 2) {
                                textStatic.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                            } else {
                                textStatic.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                            }
                        }
                        textStatic.setForecolor(Color.black);
                        textStatic.setMode(ModeEnum.TRANSPARENT);
                        textStatic.setBackcolor(Color.white);
                        lastIndex = textStatic.getWidth() + textStatic.getX();
                        bandColumnHeader.addElement(textStatic);
                    }
                }
                if (zColumnHeader.getTracageLigne()) {
                    if (lastIndex != null) {
                        JRDesignLine l = new JRDesignLine();
                        l.setBackcolor(Color.black);
                        l.setForecolor(Color.black);
                        l.setX(lastIndex);
                        l.setY(0);
                        l.setMode(ModeEnum.OPAQUE);
                        l.setWidth(1);
                        l.setHeight(zColumnHeader.getHeightRapport());
                        bandColumnHeader.addElement(l);
                        lineTopColumnHeader.setHeight(1);
                        lineTopColumnHeader.setWidth(lastIndex - minPosition);
                        lineTopColumnHeader.setX(minPosition);
                        bandColumnHeader.addElement(lineTopColumnHeader);
                        JRDesignLine l2 = new JRDesignLine();
                        l2.setBackcolor(Color.black);
                        l2.setForecolor(Color.black);
                        l2.setX(minPosition);
                        l2.setY(bandColumnHeader.getHeight() - 1);
                        l2.setMode(ModeEnum.OPAQUE);
                        l2.setWidth(lastIndex - minPosition);
                        l2.setHeight(1);
                        bandColumnHeader.addElement(l2);
                    }
                }
            } catch (Exception ze1) {
             ze1.printStackTrace();
            }
            Integer lastIndex2 = null;
            try {
                if (nbLigneParPage > 0) {
                    JRDesignParameter prm2 = new JRDesignParameter();
                    prm2.setName("bodySize");
                    prm2.setValueClass(Integer.class);
                    prm2.setForPrompting(true);
                    jasperDesign.addParameter(prm2);
                    Integer ints = this.ejbdetailoperation.findByParameterSingleResultCount("Select count(d.detailoperationPK.ordre) from Detailoperation d where d.operation.id=:niOp", "niOp", new BigDecimal(idOperation).setScale(0, RoundingMode.HALF_UP)).intValue();
                    System.out.println("ints "+ints);
                    if (ints==0)
                    {ints=1;}
                    parameters.put("bodySize", ints);
                    jasperDesign.setLanguage(JasperReport.LANGUAGE_GROOVY);
                    JRDesignField niPro = new JRDesignField();
                    niPro.setName("typeoperation");
                    niPro.setValueClass(String.class);
                    jasperDesign.addField(niPro);
                    JRDesignVariable varCal = new JRDesignVariable();
                    varCal.setName("count");
                    varCal.setCalculation(CalculationEnum.COUNT);
                    JRDesignExpression jvarCal = new JRDesignExpression();
                    jvarCal.setText("$F{typeoperation}");
                    jvarCal.setValueClass(String.class);
                    varCal.setExpression(jvarCal);
                    varCal.setValueClass(Integer.class);
                    varCal.setResetType(ResetTypeEnum.REPORT);
                    jasperDesign.addVariable(varCal);
                    JRDesignVariable varCalPage = new JRDesignVariable();
                    varCalPage.setName("countPage");
                    varCalPage.setCalculation(CalculationEnum.COUNT);
                    JRDesignExpression jvarCalPage = new JRDesignExpression();
                    jvarCalPage.setText("$F{typeoperation}");
                    jvarCalPage.setValueClass(String.class);
                    varCalPage.setExpression(jvarCalPage);
                    varCalPage.setValueClass(Integer.class);
                    varCalPage.setResetType(ResetTypeEnum.PAGE);
                    jasperDesign.addVariable(varCalPage);
                    JRDesignVariable var = new JRDesignVariable();
                    var.setName("lastLine");
                    JRDesignExpression jj = new JRDesignExpression();
                    jj.setText("(($V{count}==$P{bodySize})||($V{countPage}==$P{nbLigneParPage}))?true:false");
                    jj.setValueClass(Boolean.class);
                    var.setExpression(jj);
                    var.setValueClass(Boolean.class);
                    jasperDesign.addVariable(var);
                }
                JRDesignBand bandBody = new JRDesignBand();
                bandBody.setHeight(zBody.getHeightRapport());
//                jasperDesign.setDetail(bandBody);

                ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandBody);
                
                
                JRDesignBand bandBackGround = new JRDesignBand();
           if(tracageBackGround)
           {      
bandBackGround.setHeight(mod1.getPageHeight());

jasperDesign.setBackground(bandBackGround);
           }
                Iterator i1 = (this.ejbDetailModel.findByParameter("Select d from DetailRapportCom d where d.zoneRapport=:zoneRapport", "zoneRapport", zBody)).iterator();
                while (i1.hasNext()) {
                    DetailRapportCom dd1 = (DetailRapportCom) i1.next();
                    JRDesignLine l = null;
               //     if (zBody.getTracageLigne() && authentificationBean.getFlagprivilegeTracageFactureComplet()==false) {
  if (zBody.getTracageLigne()) {

                        l = new JRDesignLine();
                        l.setBackcolor(Color.black);
                        l.setForecolor(Color.black);
                        l.setX(dd1.getLeftRapport());
                        l.setY(0);
                        l.setMode(ModeEnum.OPAQUE);
                        l.setWidth(1);
                        //   l.setMode(ModeEnum.OPAQUE);
                      l.setHeight(zBody.getHeightRapport());
             

                        bandBody.addElement(l);
                        if (lastIndex2 == null) {
                            lastIndex2 = new Integer(0);
                        }
                        if (lastIndex2 < dd1.getLeftRapport() + dd1.getWidthRapport()) {
                            lastIndex2 = dd1.getLeftRapport() + dd1.getWidthRapport();
                        }
                    }
             //      else  if (tracageBackGround){
                 if (tracageBackGround){       
                     JRDesignLine l1 = null;
                  
                        l1 = new JRDesignLine();
                        l1.setBackcolor(Color.black);
                        l1.setForecolor(Color.black);
                        l1.setX(dd1.getLeftRapport());
                        l1.setY(debutDetails);
                        l1.setMode(ModeEnum.OPAQUE);
                        l1.setWidth(1);
                        //   l.setMode(ModeEnum.OPAQUE);
                      l1.setHeight(longeurDetails);
                    //    System.out.println("ligne Ver ");

                        bandBackGround.addElement(l1);
                    
                     if (lastIndex2 == null) {
                            lastIndex2 = new Integer(0);
                        }
                        if (lastIndex2 < dd1.getLeftRapport() + dd1.getWidthRapport()) {
                            lastIndex2 = dd1.getLeftRapport() + dd1.getWidthRapport();
                        }
                    }
                    if (dd1.getType().equals("info")) {
                        if (dd1.getExpression().toLowerCase().contains("image")) {
                            String s = "";


                            StringTokenizer tokenDd1 = new StringTokenizer(dd1.getExpression(), "$");
                            while (tokenDd1.hasMoreTokens()) {
                                String valToken = tokenDd1.nextToken();
                                if (valToken.indexOf("F{") > -1) {
                                    s = valToken.substring(valToken.indexOf("F{") + 2, valToken.indexOf("}"));
                                     System.out.println("s 2:"+s);
                                    try {
                                        JRDesignField field = new JRDesignField();
                                        field.setName(s);
                                        field.setValueClass(java.lang.String.class);
                                        if (fieldsajoutes.contains(s) == false) {
                                            jasperDesign.addField(field);
                                            fieldsajoutes.add(s);
                                        }
                                    } catch (Exception errre) {
       
     //                                   errre.printStackTrace();
                                    }
                                }
                            }
                            JRDesignImage imageFacture = new JRDesignImage(jasperDesign);
                            imageFacture.setX(dd1.getLeftRapport() + 1);
                            imageFacture.setY(dd1.getTopRapport());
                            imageFacture.setWidth(dd1.getWidthRapport() - 1);
                            imageFacture.setHeight(imageFacture.getWidth() + 1);
                            if ((bandBody.getHeight()) < (imageFacture.getHeight() + imageFacture.getY())) {
                                bandBody.setHeight(imageFacture.getHeight() + imageFacture.getY());
                            }
                            JRDesignExpression expressionImage = new JRDesignExpression();
                            expressionImage.setValueClass(String.class);
                            String realPathWebFolder = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/web/");
                            if (realPathWebFolder.endsWith("\\web\\web")) {
                                realPathWebFolder = realPathWebFolder.substring(0, realPathWebFolder.lastIndexOf("\\web"));
                            }
                            realPathWebFolder = realPathWebFolder.replace("\\", "\\\\");
                            expressionImage.setText("\"" + realPathWebFolder + "\"" + "+" + dd1.getExpression());
                            imageFacture.setExpression(expressionImage);
                            imageFacture.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                            imageFacture.setOnErrorType(OnErrorTypeEnum.BLANK);
                            bandBody.addElement(imageFacture);
                        } else {
                            JRDesignTextField textField = new JRDesignTextField();
                            textField.setStretchWithOverflow(dd1.getStretchWithOverFlow());
                            textField.setBold(dd1.getGras());
                            textField.setItalic(dd1.getItalique());
                            textField.setUnderline(dd1.getSouligner());
                            textField.setBlankWhenNull(true);
                            if (dd1.getAlignement() == 1) {
                                textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                            } else {
                                if (dd1.getAlignement() == 2) {
                                    textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                                } else {
                                    textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                                }
                            }
                            textField.setHeight(dd1.getHeightRapport());
                            textField.setWidth(dd1.getWidthRapport());
                            textField.setX(dd1.getLeftRapport());
                            textField.setY(dd1.getTopRapport());
                            String s = "";

                            StringTokenizer tokenDd1 = new StringTokenizer(dd1.getExpression(), "$");
                            while (tokenDd1.hasMoreTokens()) {
                                String valToken = tokenDd1.nextToken();
                                if (valToken.indexOf("F{") > -1) {
                                    s = valToken.substring(valToken.indexOf("F{") + 2, valToken.indexOf("}"));
                                     System.out.println("s3 :"+s);
                                    try {
                                        JRDesignField field = new JRDesignField();
                                        field.setName(s);
                                        field.setValueClass(java.lang.String.class);
                                        if (fieldsajoutes.contains(s) == false) {
                                            jasperDesign.addField(field);
                                            fieldsajoutes.add(s);
                                        }
                                    } catch (Exception errre) {
                                   errre.printStackTrace();
                                    }
                                }
                            }
                            JRDesignExpression expression = new JRDesignExpression();
                            expression.setValueClass(java.lang.String.class);
                            expression.setText(dd1.getExpression());
                            textField.setExpression(expression);
                            textField.setFontSize(dd1.getFontSize());
                            textField.setForecolor(Color.black);
                            textField.setBackcolor(Color.white);
                            textField.getLineBox().setTopPadding(1);
                            textField.getLineBox().setLeftPadding(2);
                            textField.getLineBox().setRightPadding(2);
                            textField.getLineBox().setBottomPadding(1);
                            textField.setMode(ModeEnum.TRANSPARENT);
                            bandBody.addElement(textField);
                        }
                    }
                }
              //     if (zBody.getTracageLigne() && authentificationBean.getFlagprivilegeTracageFactureComplet()==false) {
//                if (zBody.getTracageLigne()) {
//                    if (lastIndex2 != null) {
//                        JRDesignLine l = new JRDesignLine();
//                        l.setBackcolor(Color.black);
//                        l.setForecolor(Color.black);
//                        l.setX(lastIndex2);
//                        l.setY(0);
//                        l.setMode(ModeEnum.OPAQUE);
//                        l.setWidth(1);
//                        l.setHeight(bandBody.getHeight());
//                        bandBody.addElement(l);
//                        if (nbLigneParPage > 0) {
//                            JRDesignLine l2 = new JRDesignLine();
//                            l2.setBackcolor(Color.black);
//                            l2.setForecolor(Color.black);
//                            l2.setX(minPosition);
//                            l2.setY(bandBody.getHeight() - 1);
//                            l2.setMode(ModeEnum.OPAQUE);
//                            l2.setWidth(lastIndex2 - minPosition);
//                            l2.setHeight(1);
//                            l2.setPositionType(PositionTypeEnum.FIX_RELATIVE_TO_BOTTOM);
//                            JRDesignExpression ex = new JRDesignExpression();
//                            ex.setText("$V{lastLine}");
//                            ex.setValueClass(Boolean.class);
//                            l2.setPrintWhenExpression(ex);
//                            bandBody.addElement(l2);
//                        }
//                    }
//                }
                 if (tracageBackGround){
                
                    JRDesignLine l1 = new JRDesignLine();
                        l1.setBackcolor(Color.black);
                        l1.setForecolor(Color.black);
                        l1.setX(lastIndex2);
                        l1.setY(debutDetails);
                        l1.setMode(ModeEnum.OPAQUE);
                        l1.setWidth(1);
                        l1.setHeight(longeurDetails);
                        bandBackGround.addElement(l1);
                        if (nbLigneParPage > 0) {
                            JRDesignLine l21 = new JRDesignLine();
                            l21.setBackcolor(Color.black);
                            l21.setForecolor(Color.black);
                            l21.setX(minPosition);
                            l21.setY(debutDetails+longeurDetails - 1);
                            l21.setMode(ModeEnum.OPAQUE);
                            l21.setWidth(lastIndex2 - minPosition);
                            l21.setHeight(1);
                            l21.setPositionType(PositionTypeEnum.FIX_RELATIVE_TO_BOTTOM);
//                            JRDesignExpression ex = new JRDesignExpression();
//                            ex.setText("$V{lastLine}");
//                            ex.setValueClass(Boolean.class);
//                            l21.setPrintWhenExpression(ex);
                            bandBackGround.addElement(l21);
                        }
                
                
                
                }
            } catch (Exception ze1) {
              ze1.printStackTrace();
            }
            JRDesignBand bandFooter = null;
            try {
                bandFooter = new JRDesignBand();
                bandFooter.setHeight(zFooter.getHeightRapport());
                jasperDesign.setPageFooter(bandFooter);
                Iterator i1 = (this.ejbDetailModel.findByParameter("Select d from DetailRapportCom d where d.zoneRapport=:zoneRapport", "zoneRapport", zFooter)).iterator();
                if (nbLigneParPage == 0) {
                    if (zFooter.getTracageLigne()) {
                        if (lastIndex2 != null) {
                            JRDesignLine l = new JRDesignLine();
                            l.setBackcolor(Color.black);
                            l.setForecolor(Color.black);
                            l.setX(minPosition);
                            l.setY(0);
                            l.setMode(ModeEnum.OPAQUE);
                            l.setWidth(lastIndex2 - minPosition);
                            l.setHeight(1);
                            bandFooter.addElement(l);
                        }
                    }
                }
                while (i1.hasNext()) {
                    DetailRapportCom dd1 = (DetailRapportCom) i1.next();
                    if (dd1.getType().equalsIgnoreCase("info")) {
                        System.out.println("info footer "+dd1.getNom());
                        JRDesignTextField textField = new JRDesignTextField();
                        textField.setStretchWithOverflow(dd1.getStretchWithOverFlow());
                        textField.setBold(dd1.getGras());
                        textField.setItalic(dd1.getItalique());
                        textField.setUnderline(dd1.getSouligner());
                        textField.setBlankWhenNull(true);
                        textField.getLineBox().setTopPadding(1);
                        if (dd1.getAlignement() == 1) {
                            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                        } else {
                            if (dd1.getAlignement() == 2) {
                                textField.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                            } else {
                                textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                            }
                        }
                        textField.setHeight(dd1.getHeightRapport());
                        textField.setWidth(dd1.getWidthRapport());
                        textField.setX(dd1.getLeftRapport());
                        textField.setY(dd1.getTopRapport());
                        JRDesignExpression ex = new JRDesignExpression();
                        ex.setText("$V{count}==$P{bodySize}");
                        ex.setValueClass(Boolean.class);
                        textField.setPrintWhenExpression(ex);
                        String s = "";

                        StringTokenizer tokenDd1 = new StringTokenizer(dd1.getExpression(), "$");
                        while (tokenDd1.hasMoreTokens()) {
                            String valToken = tokenDd1.nextToken();
                            if (valToken.indexOf("F{") > -1) {
                                s = valToken.substring(valToken.indexOf("F{") + 2, valToken.indexOf("}"));
                                System.out.println("s footer "+s);
                                try {
                                    JRDesignField field = new JRDesignField();
                                    field.setName(s);
                                    field.setValueClass(java.lang.String.class);
                                    if (fieldsajoutes.contains(s) == false) {
                                        jasperDesign.addField(field);
                                        fieldsajoutes.add(s);
                                    }
                                } catch (Exception errre) {
                                errre.printStackTrace();
                                }
                            }
                        }
                        JRDesignExpression expression = new JRDesignExpression();
                        expression.setValueClass(java.lang.String.class);
                        expression.setText(dd1.getExpression());
                        System.out.println("exxxxpre "+dd1.getExpression());
                        textField.setExpression(expression);
                        textField.setFontSize(dd1.getFontSize());
                        textField.setForecolor(Color.black);
                        textField.getLineBox().setLeftPadding(2);
                        textField.getLineBox().setRightPadding(2);
                        textField.setBackcolor(Color.white);
                        bandFooter.addElement(textField);
                    } else {
                        JRDesignStaticText textStatic = new JRDesignStaticText();
                        textStatic.setFontSize(dd1.getFontSize());
                        textStatic.setX(dd1.getLeftRapport());
                        textStatic.setY(dd1.getTopRapport());
                        textStatic.setWidth(dd1.getWidthRapport());
                        textStatic.setHeight(dd1.getHeightRapport());
                        textStatic.setText(dd1.getExpression());
                        textStatic.setBold(dd1.getGras());
                        JRDesignExpression ex = new JRDesignExpression();
                        ex.setText("$V{count}==$P{bodySize}");
                        ex.setValueClass(Boolean.class);
                        textStatic.setPrintWhenExpression(ex);
                        textStatic.getLineBox().setTopPadding(1);
                        textStatic.getLineBox().setLeftPadding(2);
                        textStatic.getLineBox().setRightPadding(2);
                        textStatic.setItalic(dd1.getItalique());
                        textStatic.setUnderline(dd1.getSouligner());
                        if (dd1.getAlignement() == 1) {
                            textStatic.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                        } else {
                            if (dd1.getAlignement() == 2) {
                                textStatic.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                            } else {
                                textStatic.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                            }
                        }
                        textStatic.setForecolor(Color.black);
                        textStatic.setBackcolor(Color.white);
                        bandFooter.addElement(textStatic);
                    }
                }

            } catch (Exception ze1) {
             ze1.printStackTrace();
            }
    //        System.out.println("idOperation avant montantTTcTouteLettresDepuisFacture "+new BigDecimal(idOperation).setScale(0, RoundingMode.HALF_UP));
            String montantToutLettre = this.montantTTcTouteLettresDepuisFacture(new BigDecimal(idOperation).setScale(0, RoundingMode.UP));
            System.out.println("montantToutLettre : "+montantToutLettre);
            JRDesignParameter prm1 = new JRDesignParameter();
            prm1.setName("montantTTcTouteLettres");
            prm1.setValueClass(java.lang.String.class);
            prm1.setForPrompting(true);
            jasperDesign.addParameter(prm1);
            parameters.put("montantTTcTouteLettres", montantToutLettre);
            JRDesignParameter prm13 = new JRDesignParameter();
            prm13.setName("reglements");
            prm13.setValueClass(java.lang.String.class);
            prm13.setForPrompting(true);
            jasperDesign.addParameter(prm13);

    // comm        parameters.put("reglements", reglements(idOperation));
            if (bandFooter != null) {
                JRDesignStaticText textStatic = new JRDesignStaticText();
                textStatic.setFontSize(8);
                textStatic.setX(0);
                textStatic.setY(bandFooter.getHeight() - 65);
                textStatic.setWidth(250);
                textStatic.setHeight(15);
                textStatic.setText("page");
                textStatic.getLineBox().setTopPadding(1);
                textStatic.getLineBox().setLeftPadding(1);
                textStatic.getLineBox().setRightPadding(1);
                textStatic.getLineBox().setBottomPadding(1);
                textStatic.setForecolor(Color.black);
                textStatic.setBackcolor(Color.white);
                textStatic.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                bandFooter.addElement(textStatic);
                JRDesignTextField textField = new JRDesignTextField();
                textField.getLineBox().setTopPadding(1);
                textField.getLineBox().setLeftPadding(1);
                textField.getLineBox().setRightPadding(1);
                textField.getLineBox().setBottomPadding(1);
                textField.setHeight(15);
                textField.setWidth(30);
                textField.setX(250);
                textField.setY(bandFooter.getHeight() - 65);
                textField.setFontSize(8);
                JRDesignExpression ex = new JRDesignExpression();
                ex.setText(" $V{PAGE_NUMBER}");
                ex.setValueClass(String.class);
                textField.setExpression(ex);
                textField.setEvaluationTime(EvaluationTimeEnum.NOW);
                bandFooter.addElement(textField);
                JRDesignStaticText textStatic2 = new JRDesignStaticText();
                textStatic2.setFontSize(6);
                textStatic2.setX(280);
                textStatic2.setY(bandFooter.getHeight() - 65);
                textStatic2.setWidth(6);
                textStatic2.setHeight(15);
                textStatic2.setText(" /");
                textStatic2.getLineBox().setTopPadding(1);
                textStatic2.getLineBox().setLeftPadding(1);
                textStatic2.getLineBox().setRightPadding(1);
                textStatic2.getLineBox().setBottomPadding(1);
                textStatic2.setForecolor(Color.black);
                textStatic2.setBackcolor(Color.white);
                textStatic2.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                bandFooter.addElement(textStatic2);
                JRDesignTextField textField2 = new JRDesignTextField();
                textField2.getLineBox().setTopPadding(1);
                textField2.getLineBox().setLeftPadding(1);
                textField2.getLineBox().setRightPadding(1);
                textField2.getLineBox().setBottomPadding(1);
                textField2.setHeight(15);
                textField2.setWidth(30);
                textField2.setX(286);
                textField2.setY(bandFooter.getHeight() - 65);
                textField2.setFontSize(8);
                JRDesignExpression ex2 = new JRDesignExpression();
                ex2.setText(" $V{PAGE_NUMBER}");
                ex2.setValueClass(String.class);
                textField2.setExpression(ex2);
                textField2.setEvaluationTime(EvaluationTimeEnum.REPORT);
                bandFooter.addElement(textField2);
            }
            JRDesignParameter prm12 = new JRDesignParameter();
            prm12.setName("user_operation");
            prm12.setValueClass(java.lang.String.class);
            prm12.setForPrompting(true);
            jasperDesign.addParameter(prm12);
            StringTokenizer getUrl = new StringTokenizer(this.getFacade().urlCourante(), "**");
            String url = getUrl.nextToken();
            String login = getUrl.nextToken();
            String password = getUrl.nextToken();
            String nomBaseDeDonnes = url.substring(url.lastIndexOf("/") + 1);
      //      System.out.println("id Operation " + idOperation);

            //   parameters.put("user_operation", this.userEditerPar(idOperation));
           String user1="su";
            try {
        Operation    o = getFacade().findByParameterSingleResult("Select o from Operation o where o.id=:idOperation", "idOperation", new Long(idOperation));
        user1= o.getUser().getLogin();
                  System.out.println("user1 "+user1);
              }
              catch(Exception e)
              {
                  e.printStackTrace();
              }
                    
                          parameters.put("user_operation", user1);

      //      JRDesignParameter prmReg = new JRDesignParameter();
      //      prmReg.setName("reglement_chaine");
      //      prmReg.setValueClass(java.lang.String.class);
     //       prmReg.setForPrompting(true);
     //       jasperDesign.addParameter(prmReg);
     //comm       parameters.put("reglement_chaine", this.reglementDeLaFactureEnChaine(idOperation));
   
            JRFileVirtualizer virtualizer = new JRFileVirtualizer(JsfClasses.util.ParametresRapports.getNbMaxPageToCut(), JsfClasses.util.ParametresRapports.getPathRapportTemporaire());
            parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);
      
            String defaultPDFFont = "Draft 15cpi";

  //          JRProperties.setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
  //          JRProperties.setProperty("net.sf.jasperreports.default.font.name", defaultPDFFont);
//            JRProperties.setProperty("net.sf.jasperreports.default.pdf.embedded", "false");

            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        //    System.out.println("apres compilation");
            Driver monDriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(monDriver);
            Connection connection = DriverManager.getConnection(url, login, password);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);
            connection.close();
       //     System.out.println("fin");
            return jasperPrint;
        } catch (Exception e) {
      e.printStackTrace();
        }
        return null;
    }
 
          
          public String montantTTcTouteLettresDepuisFacture(BigDecimal b) {
        Statement statement = null;
        ResultSet rs = null;
        String s = "";
        try {
            Driver monDriver = new com.mysql.jdbc.Driver();
            StringTokenizer getUrl = new StringTokenizer(this.getFacade().urlCourante(), "**");
            String url = getUrl.nextToken();
            String login = getUrl.nextToken();
            String password = getUrl.nextToken();
            String nomBaseDeDonnes = url.substring(url.lastIndexOf("/") + 1);
            DriverManager.registerDriver(monDriver);
            Connection connection = DriverManager.getConnection(url, login, password);
            statement = (Statement) connection.createStatement();
            rs = statement.executeQuery("SELECT montantoperation FROM `cabinet`.`operation` where id=" + b.toString());
            if (rs != null) {
                if (rs.next()) {
                    s = montantTtcTouteLettres(new BigDecimal(rs.getString(1)).setScale(3, RoundingMode.HALF_UP));
                }
            }
            rs.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
       //     e.printStackTrace();
        }
        return s;
    }
     public String montantTtcTouteLettres(BigDecimal ttc) {
        String mont = "";
        RuleBasedNumberFormat rbnf = new RuleBasedNumberFormat(Locale.FRENCH, RuleBasedNumberFormat.SPELLOUT);
        StringTokenizer st = new StringTokenizer(ttc.setScale(3, RoundingMode.UP).toString(), ".");
        String s1 = st.nextToken();
        String s2 = st.nextToken();
        Integer in1 = new Integer(s1);
        Integer in2 = new Integer(s2);
        mont = rbnf.format(in1);
        mont = mont + " dinars";
        if (in2 > 0) {
            mont = mont + " et ";
            mont = mont + rbnf.format(in2);
            if (in2 > 1) {
                mont = mont + " millimes";
            } else {
                if (in2 == 1) {
                    mont = mont + " millime";
                }
            }
        }
        mont = mont.replaceAll("moins", "");
        return mont;
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
       public void impressionPdfListOperations() throws JRException, IOException {
       
            try {

                optionOrient = "1";
                etablirconnection();
                JRDesignBand bandTiltle = new JRDesignBand();
                bandTiltle.setHeight(110);
                DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date d22 = new Date();
                String ddd = dateFormat3.format(d22);
                JasperDesign jasperDesign = new JasperDesign();
                JRDesignFrame jrFame = new JRDesignFrame();
                jasperDesign.setName("ExtraitOperationDivers");
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
                libelleTitre.setText("Liste des Operations Divers");
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

                
                 Typeoperation typeop = new Typeoperation();
            try {
                System.out.println("nom operation rapport : "+nompage);
                typeop = ejbtypeoperation.findByParameterSingleResult("Select u from Typeoperation u where u.libelle=:libelle", "libelle", nompage);
                System.out.println("typeop " + typeop.getLibelle());
            } catch (Exception e) {
            }
                
                JRDesignQuery query = new JRDesignQuery();           
            query.setText("SELECT cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal, operation.`id` AS operation_id, operation.`modereglement` AS operation_modereglement, operation.`banque` AS operation_banque, operation.`typeoperation` AS operation_typeoperation, operation.`client` AS operation_client, operation.`numerooperation` AS operation_numerooperation, operation.`dateoperation` AS operation_dateoperation, operation.`montantoperation` AS operation_montantoperation, operation.`montantdebite` AS operation_montantdebite, operation.`montantcredite` AS operation_montantcredite, operation.`montantcheque` AS operation_montantcheque, operation.`montantespece` AS operation_montantespece, operation.`montanttraite` AS operation_montanttraite, operation.`montantvirement` AS operation_montantvirement, operation.`date_sys` AS operation_date_sys, operation.`user` AS operation_user, operation.`libelleoperation` AS operation_libelleoperation, operation.`reference` AS operation_reference, operation.`recu` AS operation_recu, operation.`emetteur` AS operation_emetteur, operation.`dateechenace` AS operation_dateechenace, operation.`detail` AS operation_detail, operation.`dateconsultation` AS operation_dateconsultation, operation.`motifconsultation` AS operation_motifconsultation, client.`nom` AS client_nom, client.`datecreation` AS client_datecreation, client.`code` AS client_code, modereglement.`id` AS modereglement_id, modereglement.`code` AS modereglement_code, modereglement.`libelle` AS modereglement_libelle FROM `operation` operation LEFT JOIN `client` client ON client.`id` = operation.`client` LEFT JOIN `modereglement` modereglement ON operation.`modereglement` = modereglement.`id`, `cabinet` cabinet  WHERE operation.`typeoperation`="+typeop.getId()+"");
              System.out.println("Query" + query.getText());
                jasperDesign.setQuery(query);
                jasperDesign.setName("ListeDiverssrapport");
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
                    JRDesignStaticText libelleColonneClient = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "client_nom", "(($F{client_nom}!=null)&($F{client_nom}.equals(\"\")==false))?$F{client_nom}:Character.toString(' ')", libelleColonneClient, "CLIENT", 0, 60, bandSomme);
                    libelleColonneClient.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneClient);
                }


                {
                    JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_montantoperation", "(($F{operation_montantoperation}!=null)&($F{operation_montantoperation}.equals(\"\")==false))?$new BigDecimal($F{operation_montantoperation}).setScale(3,RoundingMode.FLOOR).toString():Character.toString(' ')", libelleColonneCode, "MONTANT", 0, 60, bandSomme);
                    libelleColonneCode.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneCode);
                }


                {
                    JRDesignStaticText libelleColonneDesignation = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_numerooperation", "(($F{operation_numerooperation}!=null)&($F{operation_numerooperation}.equals(\"\")==false))?$F{operation_numerooperation}:Character.toString(' ')", libelleColonneDesignation, "NUMERO OPERATION", 0, 200, bandSomme);
                    libelleColonneDesignation.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneDesignation);
                }

                 {
                    JRDesignStaticText libelleColonneDate = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_dateoperation", "(($F{operation_dateoperation}!=null)&($F{operation_dateoperation}.equals(\"\")==false))?$F{operation_dateoperation}:Character.toString(' ')", libelleColonneDate, "DATE", 0, 80, bandSomme);
                    libelleColonneDate.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneDate);
                }
                    {
                    JRDesignStaticText libelleColonneModeReglement = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "modereglement_libelle", "(($F{modereglement_libelle}!=null)&($F{modereglement_libelle}.equals(\"\")==false))?$F{modereglement_libelle}:Character.toString(' ')", libelleColonneModeReglement, "MODE REGLEMENT", 0, 150, bandSomme);
                    libelleColonneModeReglement.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneModeReglement);
                }
//                {
//                    JRDesignStaticText libelleColonneProduit = new JRDesignStaticText();
//                    styleLibelleColumnHeader(jasperDesign, bandHeader, "produit_prixachat", "($F{produit_prixachat}!=null)?$F{produit_prixachat}:Character.toString(' ')", libelleColonneProduit, "PRIX", 0, 60, bandSomme);
//                    libelleColonneProduit.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
//                    columnHeader.addElement(libelleColonneProduit);
//                }
//
//                   {
//                    JRDesignStaticText libelleColonneFournisseur = new JRDesignStaticText();
//                    styleLibelleColumnHeader(jasperDesign, bandHeader, "fournisseur_nom", "(($F{fournisseur_nom}!=null)&($F{fournisseur_nom}.equals(\"\")==false))?$F{fournisseur_nom}:Character.toString(' ')", libelleColonneFournisseur, "FOURNISSEUR", 0, 160, bandSomme);
//                    libelleColonneFournisseur.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
//                    columnHeader.addElement(libelleColonneFournisseur);
//                }
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
                String q2 = "Liste Des Operations Divers";

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
                String nomFichier = "RapportListeOperationDivers.pdf";
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

  
       
       
           public void impressionPdfFournisseur() throws JRException, IOException {
       
            try {
               current = (Operation) getItems().getRowData();
                optionOrient = "1";
                etablirconnection();
                JRDesignBand bandTiltle = new JRDesignBand();
                bandTiltle.setHeight(110);
                DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date d22 = new Date();
                String ddd = dateFormat3.format(d22);
                JasperDesign jasperDesign = new JasperDesign();
                JRDesignFrame jrFame = new JRDesignFrame();
                jasperDesign.setName("ExtraitOperationFournisseur");
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
                libelleTitre.setText("Facture Fournisseur");
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

                
                 Typeoperation typeop = new Typeoperation();
            try {
                System.out.println("nom operation rapport : "+nompage);
                typeop = ejbtypeoperation.findByParameterSingleResult("Select u from Typeoperation u where u.libelle=:libelle", "libelle", nompage);
                System.out.println("typeop " + typeop.getLibelle());
            } catch (Exception e) {
            }
                
                JRDesignQuery query = new JRDesignQuery();          
       //     query.setText("SELECT cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal, operation.`id` AS operation_id, operation.`modereglement` AS operation_modereglement, operation.`banque` AS operation_banque, operation.`typeoperation` AS operation_typeoperation, operation.`client` AS operation_client, operation.`numerooperation` AS operation_numerooperation, operation.`dateoperation` AS operation_dateoperation, operation.`montantoperation` AS operation_montantoperation, operation.`montantdebite` AS operation_montantdebite, operation.`montantcredite` AS operation_montantcredite, operation.`montantcheque` AS operation_montantcheque, operation.`montantespece` AS operation_montantespece, operation.`montanttraite` AS operation_montanttraite, operation.`montantvirement` AS operation_montantvirement, operation.`date_sys` AS operation_date_sys, operation.`user` AS operation_user, operation.`libelleoperation` AS operation_libelleoperation, operation.`reference` AS operation_reference, operation.`recu` AS operation_recu, operation.`emetteur` AS operation_emetteur, operation.`dateechenace` AS operation_dateechenace, operation.`detail` AS operation_detail, operation.`dateconsultation` AS operation_dateconsultation, operation.`motifconsultation` AS operation_motifconsultation, client.`nom` AS client_nom, client.`datecreation` AS client_datecreation, client.`code` AS client_code, modereglement.`id` AS modereglement_id, modereglement.`code` AS modereglement_code, modereglement.`libelle` AS modereglement_libelle FROM `operation` operation LEFT JOIN `client` client ON client.`id` = operation.`client` LEFT JOIN `modereglement` modereglement ON operation.`modereglement` = modereglement.`id`, `cabinet` cabinet  WHERE operation.`typeoperation`="+typeop.getId()+"");
          query.setText("SELECT cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal, operation.`id` AS operation_id, operation.`modereglement` AS operation_modereglement, operation.`banque` AS operation_banque, operation.`typeoperation` AS operation_typeoperation, operation.`client` AS operation_client, operation.`numerooperation` AS operation_numerooperation, operation.`dateoperation` AS operation_dateoperation, operation.`montantoperation` AS operation_montantoperation, operation.`montantdebite` AS operation_montantdebite, operation.`montantcredite` AS operation_montantcredite, operation.`montantcheque` AS operation_montantcheque, operation.`montantespece` AS operation_montantespece, operation.`montanttraite` AS operation_montanttraite, operation.`montantvirement` AS operation_montantvirement, operation.`date_sys` AS operation_date_sys, operation.`user` AS operation_user, operation.`libelleoperation` AS operation_libelleoperation, operation.`reference` AS operation_reference, operation.`recu` AS operation_recu, operation.`emetteur` AS operation_emetteur, operation.`dateechenace` AS operation_dateechenace, operation.`detail` AS operation_detail, operation.`dateconsultation` AS operation_dateconsultation, operation.`motifconsultation` AS operation_motifconsultation, detailoperation.`idoperation` AS detailoperation_idoperation, detailoperation.`idproduit` AS detailoperation_idproduit, detailoperation.`qte` AS detailoperation_qte, detailoperation.`prixunitaire` AS detailoperation_prixunitaire, detailoperation.`prixtotal` AS detailoperation_prixtotal, detailoperation.`ordre` AS detailoperation_ordre, detailoperation.`idacte` AS detailoperation_idacte, detailoperation.`iddent` AS detailoperation_iddent, detailoperation.`tarif` AS detailoperation_tarif, client.`id` AS client_id, client.`code` AS client_code, client.`nom` AS client_nom, client.`ville` AS client_ville, client.`cin` AS client_cin, client.`numerofiche` AS client_numerofiche, client.`adresse` AS client_adresse, client.`codepostale` AS client_codepostale, client.`tel` AS client_tel, client.`fax` AS client_fax, client.`mail` AS client_mail, client.`por` AS client_por, client.`datenaissance` AS client_datenaissance, client.`datecreation` AS client_datecreation, client.`dernieredatemodif` AS client_dernieredatemodif, client.`photo` AS client_photo, client.`classe_client` AS client_classe_client, client.`nationalite` AS client_nationalite, client.`sexe` AS client_sexe, client.`profession` AS client_profession, client.`laboratoire` AS client_laboratoire, client.`plafond` AS client_plafond, client.`actif` AS client_actif, client.`typeclient` AS client_typeclient, modereglement.`id` AS modereglement_id, modereglement.`code` AS modereglement_code, modereglement.`libelle` AS modereglement_libelle, produit.`id` AS produit_id, produit.`unite` AS produit_unite, produit.`categorieproduit` AS produit_categorieproduit, produit.`fournisseur` AS produit_fournisseur, produit.`code` AS produit_code, produit.`libelle` AS produit_libelle, produit.`reference` AS produit_reference, produit.`qteenstock` AS produit_qteenstock, produit.`datecreation` AS produit_datecreation, produit.`datemodification` AS produit_datemodification, produit.`image` AS produit_image, produit.`commentaire` AS produit_commentaire, produit.`famille` AS produit_famille, produit.`prixachat` AS produit_prixachat, banque.`id` AS banque_id, banque.`code` AS banque_code, banque.`libelle` AS banque_libelle FROM `operation` operation LEFT JOIN `detailoperation` detailoperation ON operation.`id` = detailoperation.`idoperation` LEFT JOIN `client` client ON operation.`client` = client.`id` LEFT JOIN `modereglement` modereglement ON operation.`modereglement` = modereglement.`id` LEFT JOIN `banque` banque ON operation.`banque` = banque.`id` LEFT JOIN `produit` produit ON detailoperation.`idproduit` = produit.`id`, `cabinet` cabinet  WHERE operation.`typeoperation`="+typeop.getId()+" and operation.`id`="+ current.getId()+"");
                System.out.println("Query" + query.getText());
                jasperDesign.setQuery(query);
                jasperDesign.setName("OperationFournisseurrapport");
                jasperDesign.setLeftMargin(10);
                jasperDesign.setRightMargin(10);
                jasperDesign.setTopMargin(10);
                jasperDesign.setBottomMargin(10);
                //   jrFame.setBackcolor(Color.WHITE);
                //   jrFame.setForecolor(Color.getHSBColor(51, 51, 255));



                /**************************************************/
                JRDesignBand bandFooter = new JRDesignBand();
                bandFooter.setHeight(100);

               JRDesignStaticText libelleMontant = new JRDesignStaticText();
                libelleMontant.setWidth(160);
                libelleMontant.setHeight(15);
               

                    libelleMontant.setText("TOTAL TTC : " + current.getMontantoperation());

                

                libelleMontant.setFontSize(10);
                libelleMontant.setForecolor(Color.BLACK);
                libelleMontant.setX(380);
                libelleMontant.setY(20);           
                 bandFooter.addElement(libelleMontant);
                 
                 
                JRDesignStaticText textFieldDate = new JRDesignStaticText();
                textFieldDate.setFontSize(10);
                textFieldDate.setX(0);
                textFieldDate.setY(80);
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
                textField1.setY(80);
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
                textField2.setY(80);
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
/////////////////////////////////////////////tracage complet//
//                  JRDesignBand bandBackGround = new JRDesignBand();
//                JRDesignLine l1 = new JRDesignLine();
//                        l1.setBackcolor(Color.black);
//                        l1.setForecolor(Color.black);
//                        l1.setX(0);
//                        l1.setY(0);
//                        l1.setMode(ModeEnum.OPAQUE);
//                        l1.setWidth(1);
//                        l1.setHeight(200);
//                        bandBackGround.addElement(l1);
//                        
//                            JRDesignLine l21 = new JRDesignLine();
//                            l21.setBackcolor(Color.black);
//                            l21.setForecolor(Color.black);
//                            l21.setX(0);
//                            l21.setY(0);
//                            l21.setMode(ModeEnum.OPAQUE);
//                            l21.setWidth(200);
//                            l21.setHeight(1);
//                            l21.setPositionType(PositionTypeEnum.FIX_RELATIVE_TO_BOTTOM);
////                            JRDesignExpression ex = new JRDesignExpression();
////                            ex.setText("$V{lastLine}");
////                            ex.setValueClass(Boolean.class);
////                            l21.setPrintWhenExpression(ex);
//                            bandBackGround.addElement(l21);
//                        
//                    bandBackGround.setHeight(259);
//
//jasperDesign.setBackground(bandBackGround);                
//                
                
                /*************************************************************/
                JRDesignBand bandHeader = new JRDesignBand();
                BigDecimal bandHeaderHeight = new BigDecimal(0).setScale(0, RoundingMode.DOWN);
                bandHeader.setHeight(bandHeaderHeight.intValue());
                JRDesignBand columnHeader = new JRDesignBand();
                columnHeader.setHeight(15);
                JRDesignBand bandSomme = new JRDesignBand();
                bandSomme.setHeight(259);
//                JRDesignLine l1 = null;
//                l1 = new JRDesignLine();
//                        l1.setBackcolor(Color.black);
//                        l1.setForecolor(Color.black);
//                        l1.setX(0);
//                        l1.setY(0);
//                        l1.setMode(ModeEnum.OPAQUE);
//                        l1.setWidth(1);
//                        //   l.setMode(ModeEnum.OPAQUE);
//                      l1.setHeight(259);
//                bandSomme.addElement(l1);
                
                {
                    JRDesignStaticText libelleColonneProduitCode = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "produit_code", "(($F{produit_code}!=null)&($F{produit_code}.equals(\"\")==false))?$F{produit_code}:Character.toString(' ')", libelleColonneProduitCode, "CODE PRODUIT", 0, 100, bandSomme);
                    libelleColonneProduitCode.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneProduitCode);
                }


                {
                    JRDesignStaticText libelleColonneProduitlLibelle = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "produit_libelle", "(($F{produit_libelle}!=null)&($F{produit_libelle}.equals(\"\")==false))?$F{produit_libelle}:Character.toString(' ')", libelleColonneProduitlLibelle, "LIBELLE PRODUIT", 0,150, bandSomme);
                    libelleColonneProduitlLibelle.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneProduitlLibelle);
                }


                {
                    JRDesignStaticText libelleColonneProduiQte = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "detailoperation_qte", "(($F{detailoperation_qte}!=null)&($F{detailoperation_qte}.equals(\"\")==false))?new BigDecimal($F{detailoperation_qte}).setScale(3,RoundingMode.FLOOR).toString():Character.toString(' ')", libelleColonneProduiQte, "QTE", 0, 60, bandSomme);
                    libelleColonneProduiQte.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneProduiQte);
                }

                 {
                    JRDesignStaticText libelleColonneProduitPrixUnitaire = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "detailoperation_prixunitaire", "(($F{detailoperation_prixunitaire}!=null)&($F{detailoperation_prixunitaire}.equals(\"\")==false))?new BigDecimal($F{detailoperation_prixunitaire}).setScale(3,RoundingMode.FLOOR).toString():Character.toString(' ')", libelleColonneProduitPrixUnitaire, "PRIX U", 0, 80, bandSomme);
                    libelleColonneProduitPrixUnitaire.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneProduitPrixUnitaire);
                }
                    {
                    JRDesignStaticText libelleColonneProduitPrixLigne = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "detailoperation_prixtotal", "(($F{detailoperation_prixtotal}!=null)&($F{detailoperation_prixtotal}.equals(\"\")==false))?new BigDecimal($F{detailoperation_prixtotal}).setScale(3,RoundingMode.FLOOR).toString():Character.toString(' ')", libelleColonneProduitPrixLigne, "TOTAL", 0, 150, bandSomme);
                    libelleColonneProduitPrixLigne.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneProduitPrixLigne);
                }
//                {
//                    JRDesignStaticText libelleColonneProduit = new JRDesignStaticText();
//                    styleLibelleColumnHeader(jasperDesign, bandHeader, "produit_prixachat", "($F{produit_prixachat}!=null)?$F{produit_prixachat}:Character.toString(' ')", libelleColonneProduit, "PRIX", 0, 60, bandSomme);
//                    libelleColonneProduit.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
//                    columnHeader.addElement(libelleColonneProduit);
//                }
//
//                   {
//                    JRDesignStaticText libelleColonneFournisseur = new JRDesignStaticText();
//                    styleLibelleColumnHeader(jasperDesign, bandHeader, "fournisseur_nom", "(($F{fournisseur_nom}!=null)&($F{fournisseur_nom}.equals(\"\")==false))?$F{fournisseur_nom}:Character.toString(' ')", libelleColonneFournisseur, "FOURNISSEUR", 0, 160, bandSomme);
//                    libelleColonneFournisseur.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
//                    columnHeader.addElement(libelleColonneFournisseur);
//                }
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
                String q2 = "Facture Fournisseur";

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
                String nomFichier = "RapportFournisseur.pdf";
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

           
            
           public void impressionPdfConsultation() throws JRException, IOException {
       
            try {
               current = (Operation) getItems().getRowData();
                optionOrient = "1";
                etablirconnection();
                JRDesignBand bandTiltle = new JRDesignBand();
                bandTiltle.setHeight(110);
                DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date d22 = new Date();
                String ddd = dateFormat3.format(d22);
                JasperDesign jasperDesign = new JasperDesign();
                JRDesignFrame jrFame = new JRDesignFrame();
                jasperDesign.setName("ExtraitOperationConsultation");
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
                libelleTitre.setText("Consultation Client");
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

                
                 Typeoperation typeop = new Typeoperation();
            try {
                System.out.println("nom operation rapport : "+nompage);
                typeop = ejbtypeoperation.findByParameterSingleResult("Select u from Typeoperation u where u.libelle=:libelle", "libelle", nompage);
                System.out.println("typeop " + typeop.getLibelle());
            } catch (Exception e) {
            }
                
                JRDesignQuery query = new JRDesignQuery();          
       //     query.setText("SELECT cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal, operation.`id` AS operation_id, operation.`modereglement` AS operation_modereglement, operation.`banque` AS operation_banque, operation.`typeoperation` AS operation_typeoperation, operation.`client` AS operation_client, operation.`numerooperation` AS operation_numerooperation, operation.`dateoperation` AS operation_dateoperation, operation.`montantoperation` AS operation_montantoperation, operation.`montantdebite` AS operation_montantdebite, operation.`montantcredite` AS operation_montantcredite, operation.`montantcheque` AS operation_montantcheque, operation.`montantespece` AS operation_montantespece, operation.`montanttraite` AS operation_montanttraite, operation.`montantvirement` AS operation_montantvirement, operation.`date_sys` AS operation_date_sys, operation.`user` AS operation_user, operation.`libelleoperation` AS operation_libelleoperation, operation.`reference` AS operation_reference, operation.`recu` AS operation_recu, operation.`emetteur` AS operation_emetteur, operation.`dateechenace` AS operation_dateechenace, operation.`detail` AS operation_detail, operation.`dateconsultation` AS operation_dateconsultation, operation.`motifconsultation` AS operation_motifconsultation, client.`nom` AS client_nom, client.`datecreation` AS client_datecreation, client.`code` AS client_code, modereglement.`id` AS modereglement_id, modereglement.`code` AS modereglement_code, modereglement.`libelle` AS modereglement_libelle FROM `operation` operation LEFT JOIN `client` client ON client.`id` = operation.`client` LEFT JOIN `modereglement` modereglement ON operation.`modereglement` = modereglement.`id`, `cabinet` cabinet  WHERE operation.`typeoperation`="+typeop.getId()+"");
        query.setText("SELECT cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal, client.`id` AS client_id, client.`code` AS client_code, client.`nom` AS client_nom, detailoperation.`idoperation` AS detailoperation_idoperation, detailoperation.`idproduit` AS detailoperation_idproduit, detailoperation.`qte` AS detailoperation_qte, detailoperation.`prixunitaire` AS detailoperation_prixunitaire, detailoperation.`prixtotal` AS detailoperation_prixtotal, detailoperation.`ordre` AS detailoperation_ordre, detailoperation.`idacte` AS detailoperation_idacte, detailoperation.`iddent` AS detailoperation_iddent, detailoperation.`tarif` AS detailoperation_tarif, operation.`id` AS operation_id, operation.`modereglement` AS operation_modereglement, operation.`banque` AS operation_banque, operation.`typeoperation` AS operation_typeoperation, operation.`client` AS operation_client, operation.`numerooperation` AS operation_numerooperation, operation.`dateoperation` AS operation_dateoperation, operation.`montantoperation` AS operation_montantoperation, operation.`montantdebite` AS operation_montantdebite, operation.`montantcredite` AS operation_montantcredite, operation.`montantcheque` AS operation_montantcheque, operation.`montantespece` AS operation_montantespece, operation.`montanttraite` AS operation_montanttraite, operation.`montantvirement` AS operation_montantvirement, operation.`date_sys` AS operation_date_sys, operation.`user` AS operation_user, operation.`libelleoperation` AS operation_libelleoperation, operation.`reference` AS operation_reference, operation.`recu` AS operation_recu, operation.`emetteur` AS operation_emetteur, operation.`dateechenace` AS operation_dateechenace, operation.`detail` AS operation_detail, operation.`dateconsultation` AS operation_dateconsultation, operation.`motifconsultation` AS operation_motifconsultation, dent.`id` AS dent_id, dent.`code` AS dent_code, dent.`libelle` AS dent_libelle, acte.`id` AS acte_id, acte.`code` AS acte_code, acte.`libelle` AS acte_libelle, acte.`idfamilleacte` AS acte_idfamilleacte FROM `operation` operation LEFT JOIN `client` client ON client.`id` = operation.`client` LEFT JOIN `detailoperation` detailoperation ON operation.`id` = detailoperation.`idoperation` LEFT JOIN `dent` dent ON detailoperation.`iddent` = dent.`id` LEFT JOIN `acte` acte ON detailoperation.`idacte` = acte.`id`, `cabinet` cabinet WHERE operation.`typeoperation`="+typeop.getId()+" and operation.`id`="+ current.getId()+"");
          
          System.out.println("Query" + query.getText());
                jasperDesign.setQuery(query);
                jasperDesign.setName("OperationConsultationrapport");
                jasperDesign.setLeftMargin(10);
                jasperDesign.setRightMargin(10);
                jasperDesign.setTopMargin(10);
                jasperDesign.setBottomMargin(10);
                //   jrFame.setBackcolor(Color.WHITE);
                //   jrFame.setForecolor(Color.getHSBColor(51, 51, 255));



                /**************************************************/
                JRDesignBand bandFooter = new JRDesignBand();
                bandFooter.setHeight(100);

               JRDesignStaticText libelleMontant = new JRDesignStaticText();
                libelleMontant.setWidth(160);
                libelleMontant.setHeight(15);
               

                    libelleMontant.setText("TOTAL TTC : " + current.getMontantoperation());

                

                libelleMontant.setFontSize(10);
                libelleMontant.setForecolor(Color.BLACK);
                libelleMontant.setX(380);
                libelleMontant.setY(20);           
                 bandFooter.addElement(libelleMontant);
                 
                 
                JRDesignStaticText textFieldDate = new JRDesignStaticText();
                textFieldDate.setFontSize(10);
                textFieldDate.setX(0);
                textFieldDate.setY(80);
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
                textField1.setY(80);
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
                textField2.setY(80);
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
                    JRDesignStaticText libelleColonneActe = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "acte_libelle", "(($F{acte_libelle}!=null)&($F{acte_libelle}.equals(\"\")==false))?$F{acte_libelle}:Character.toString(' ')", libelleColonneActe, "ACTE", 0, 100, bandSomme);
                    libelleColonneActe.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneActe);
                }


                {
                    JRDesignStaticText libelleColonneDent = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "dent_libelle", "(($F{dent_libelle}!=null)&($F{dent_libelle}.equals(\"\")==false))?$F{dent_libelle}:Character.toString(' ')", libelleColonneDent, "DENT", 0,60, bandSomme);
                    libelleColonneDent.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneDent);
                }



                 {
                    JRDesignStaticText libelleColonneTarif = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "detailoperation_tarif", "(($F{detailoperation_tarif}!=null)&($F{detailoperation_tarif}.equals(\"\")==false))?new BigDecimal($F{detailoperation_tarif}).setScale(3,RoundingMode.FLOOR).toString():Character.toString(' ')", libelleColonneTarif, "TARIF", 0, 80, bandSomme);
                    libelleColonneTarif.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneTarif);
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
                String q2 = "Consultation Patient";

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
                String nomFichier = "RapportConsultationClient.pdf";
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

       
                 public void impressionPdfOrdonance() throws JRException, IOException {
       
            try {
                  Xligne = 0;
               current = (Operation) getItems().getRowData();
                optionOrient = "1";
                etablirconnection();
                JRDesignBand bandTiltle = new JRDesignBand();
                bandTiltle.setHeight(110);
                DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date d22 = new Date();
                String ddd = dateFormat3.format(d22);
                JasperDesign jasperDesign = new JasperDesign();
                JRDesignFrame jrFame = new JRDesignFrame();
                jasperDesign.setName("ExtraitOrdonance");
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
                libelleTitre.setText("Ordonance Patient");
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

                
                 Typeoperation typeop = new Typeoperation();
            try {
                System.out.println("nom operation rapport : "+nompage);
                typeop = ejbtypeoperation.findByParameterSingleResult("Select u from Typeoperation u where u.libelle=:libelle", "libelle", nompage);
                System.out.println("typeop " + typeop.getLibelle());
            } catch (Exception e) {
            }
                
                JRDesignQuery query = new JRDesignQuery();          
       //     query.setText("SELECT cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal, operation.`id` AS operation_id, operation.`modereglement` AS operation_modereglement, operation.`banque` AS operation_banque, operation.`typeoperation` AS operation_typeoperation, operation.`client` AS operation_client, operation.`numerooperation` AS operation_numerooperation, operation.`dateoperation` AS operation_dateoperation, operation.`montantoperation` AS operation_montantoperation, operation.`montantdebite` AS operation_montantdebite, operation.`montantcredite` AS operation_montantcredite, operation.`montantcheque` AS operation_montantcheque, operation.`montantespece` AS operation_montantespece, operation.`montanttraite` AS operation_montanttraite, operation.`montantvirement` AS operation_montantvirement, operation.`date_sys` AS operation_date_sys, operation.`user` AS operation_user, operation.`libelleoperation` AS operation_libelleoperation, operation.`reference` AS operation_reference, operation.`recu` AS operation_recu, operation.`emetteur` AS operation_emetteur, operation.`dateechenace` AS operation_dateechenace, operation.`detail` AS operation_detail, operation.`dateconsultation` AS operation_dateconsultation, operation.`motifconsultation` AS operation_motifconsultation, client.`nom` AS client_nom, client.`datecreation` AS client_datecreation, client.`code` AS client_code, modereglement.`id` AS modereglement_id, modereglement.`code` AS modereglement_code, modereglement.`libelle` AS modereglement_libelle FROM `operation` operation LEFT JOIN `client` client ON client.`id` = operation.`client` LEFT JOIN `modereglement` modereglement ON operation.`modereglement` = modereglement.`id`, `cabinet` cabinet  WHERE operation.`typeoperation`="+typeop.getId()+"");
       query.setText("SELECT client.`id` AS client_id, client.`code` AS client_code, client.`nom` AS client_nom, detailordonance.`id` AS detailordonance_id, detailordonance.`idoperation` AS detailordonance_idoperation, detailordonance.`idmedicament` AS detailordonance_idmedicament, detailordonance.`qte` AS detailordonance_qte, detailordonance.`posologie` AS detailordonance_posologie, detailordonance.`duree` AS detailordonance_duree, detailordonance.`ordre` AS detailordonance_ordre, operation.`id` AS operation_id, operation.`modereglement` AS operation_modereglement, operation.`banque` AS operation_banque, operation.`typeoperation` AS operation_typeoperation, operation.`client` AS operation_client, operation.`recu` AS operation_recu, cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal, medicament.`id` AS medicament_id, medicament.`code` AS medicament_code, medicament.`libelle` AS medicament_libelle, operation.`libelleoperation` AS operation_libelleoperation FROM `operation` operation LEFT JOIN `client` client ON client.`id` = operation.`client` LEFT JOIN `detailordonance` detailordonance ON operation.`id` = detailordonance.`idoperation` LEFT JOIN `medicament` medicament ON detailordonance.`idmedicament` = medicament.`id`, `cabinet` cabinet  WHERE operation.`id`="+ current.getId()+"");   
        System.out.println("Query" + query.getText());
                jasperDesign.setQuery(query);
                jasperDesign.setName("Ordonance");
                jasperDesign.setLeftMargin(10);
                jasperDesign.setRightMargin(10);
                jasperDesign.setTopMargin(10);
                jasperDesign.setBottomMargin(10);
                //   jrFame.setBackcolor(Color.WHITE);
                //   jrFame.setForecolor(Color.getHSBColor(51, 51, 255));



                /**************************************************/
                JRDesignBand bandFooter = new JRDesignBand();
                bandFooter.setHeight(100);

              
                 
                 
                JRDesignStaticText textFieldDate = new JRDesignStaticText();
                textFieldDate.setFontSize(10);
                textFieldDate.setX(0);
                textFieldDate.setY(80);
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
                textField1.setY(80);
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
                textField2.setY(80);
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
                    JRDesignStaticText libelleColonneMedicament = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "medicament_libelle", "(($F{medicament_libelle}!=null)&($F{medicament_libelle}.equals(\"\")==false))?$F{medicament_libelle}:Character.toString(' ')", libelleColonneMedicament, "MEDICAMENT", 0, 120, bandSomme);
                    libelleColonneMedicament.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneMedicament);
                }


               

                 {
                    JRDesignStaticText libelleColonneQte = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "detailordonance_qte", "(($F{detailordonance_qte}!=null)&($F{detailordonance_qte}.equals(\"\")==false))?new BigDecimal($F{detailordonance_qte}).setScale(3,RoundingMode.FLOOR).toString():Character.toString(' ')", libelleColonneQte, "QTE", 0, 80, bandSomme);
                    libelleColonneQte.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneQte);
                }
                 
                  {
                    JRDesignStaticText libelleColonnePosologie = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "detailordonance_posologie", "(($F{detailordonance_posologie}!=null)&($F{detailordonance_posologie}.equals(\"\")==false))?$F{detailordonance_posologie}:Character.toString(' ')", libelleColonnePosologie, "POSOLOGIE", 0, 150, bandSomme);
                    libelleColonnePosologie.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonnePosologie);
                }
                 
                  {
                    JRDesignStaticText libelleColonneDuree = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "detailordonance_duree", "(($F{detailordonance_duree}!=null)&($F{detailordonance_duree}.equals(\"\")==false))?$F{detailordonance_duree}:Character.toString(' ')", libelleColonneDuree, "DUREE", 0, 150, bandSomme);
                    libelleColonneDuree.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneDuree);
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
                String q2 = "Ordonance Patient";

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
                String nomFichier = "OrdonancePatient.pdf";
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

       
           public void impressionPdfOperationDivers() throws JRException, IOException {
        try {
            current = (Operation) getItems().getRowData();
//            System.out.println("nom Client"+current.getNiTi().getNomTi());
        //    System.out.println("typeR"+typeR);
          
            optionOrient = "1";
            Xligne=0;
            JRDesignFrame jrFame = new JRDesignFrame();
            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy ");
            DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d22 = new Date();
            String ddd = dateFormat3.format(d22);
            JasperDesign jasperDesign = new JasperDesign();
            jasperDesign.setPageHeight(440);
            jasperDesign.setPageWidth(595);
            
             etablirconnection();
             JRDesignQuery query = new JRDesignQuery();
           
              query.setText("SELECT cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal, operation.`id` AS operation_id, operation.`modereglement` AS operation_modereglement, operation.`banque` AS operation_banque, operation.`typeoperation` AS operation_typeoperation, operation.`client` AS operation_client, operation.`numerooperation` AS operation_numerooperation, operation.`dateoperation` AS operation_dateoperation, operation.`montantoperation` AS operation_montantoperation, operation.`montantdebite` AS operation_montantdebite, operation.`montantcredite` AS operation_montantcredite, operation.`montantcheque` AS operation_montantcheque, operation.`montantespece` AS operation_montantespece, operation.`montanttraite` AS operation_montanttraite, operation.`montantvirement` AS operation_montantvirement, operation.`date_sys` AS operation_date_sys, operation.`user` AS operation_user, operation.`libelleoperation` AS operation_libelleoperation, operation.`reference` AS operation_reference, operation.`recu` AS operation_recu, operation.`emetteur` AS operation_emetteur, operation.`dateechenace` AS operation_dateechenace, operation.`detail` AS operation_detail, operation.`dateconsultation` AS operation_dateconsultation, operation.`motifconsultation` AS operation_motifconsultation, client.`nom` AS client_nom, client.`datecreation` AS client_datecreation, client.`code` AS client_code, modereglement.`id` AS modereglement_id, modereglement.`code` AS modereglement_code, modereglement.`libelle` AS modereglement_libelle FROM `operation` operation LEFT JOIN `client` client ON client.`id` = operation.`client` LEFT JOIN `modereglement` modereglement ON operation.`modereglement` = modereglement.`id`, `cabinet` cabinet  WHERE operation.`id`="+ current.getId()+"");
          
         
            jasperDesign.setQuery(query);
            jasperDesign.setName("Rapport Operation Divers");

            jasperDesign.setLeftMargin(10);
            jasperDesign.setRightMargin(10);
            jasperDesign.setTopMargin(10);
            jasperDesign.setBottomMargin(10);


            jrFame.setBackcolor(Color.WHITE);
          

            jrFame.setHeight(400);
            jrFame.setWidth(570);
            jrFame.getLineBox().setLeftPadding(0);
            jrFame.getLineBox().setTopPadding(0);
            jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

            JRDesignBand bandTiltle = new JRDesignBand();

            JRDesignStaticText libelleDateSys = new JRDesignStaticText();
            libelleDateSys.setWidth(150);
            libelleDateSys.setHeight(20);
            libelleDateSys.setText("DATE : " + ddd);
            libelleDateSys.setFontSize(10);
            libelleDateSys.setForecolor(Color.BLACK);
            libelleDateSys.setX(250);
            libelleDateSys.setY(0);
            jrFame.addElement(libelleDateSys);
            
            
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

          
            
            
            
            

             

            JRDesignStaticText libelleTitre = new JRDesignStaticText();
            libelleTitre.setWidth(200);
            libelleTitre.setHeight(25);
            libelleTitre.setX(150);
            libelleTitre.setY(80);
            libelleTitre.setText("Operation Divers");
            libelleTitre.setFontSize(16);
            libelleTitre.setForecolor(Color.BLUE);
            jrFame.addElement(libelleTitre);




            
            bandTiltle.setHeight(405);

 if(current.getClient()!=null)
 {
            JRDesignStaticText libelleCode = new JRDesignStaticText();
            libelleCode.setWidth(130);
            libelleCode.setHeight(15);
            libelleCode.setText("Code Client :  "+current.getClient().getCode());
            libelleCode.setFontSize(10);
            libelleCode.setForecolor(Color.BLACK);
            libelleCode.setX(40);
            libelleCode.setY(110);
            jrFame.addElement(libelleCode);


            JRDesignStaticText libelleNomClient = new JRDesignStaticText();
            libelleNomClient.setWidth(230);
            libelleNomClient.setHeight(15);
            libelleNomClient.setText("Nom Client :  "+current.getClient().getNom());
            libelleNomClient.setFontSize(10);
            libelleNomClient.setForecolor(Color.BLACK);
            libelleNomClient.setX(150);
            libelleNomClient.setY(110);
            jrFame.addElement(libelleNomClient);

 }
            JRDesignStaticText libelleDate = new JRDesignStaticText();
            libelleDate.setWidth(200);
            libelleDate.setHeight(15);
            libelleDate.setText("Date : "+dateFormat2.format(current.getDateoperation()));
            libelleDate.setFontSize(10);
            libelleDate.setForecolor(Color.BLACK);
            libelleDate.setX(20);
            libelleDate.setY(150);
            jrFame.addElement(libelleDate);
            
            JRDesignStaticText libelleMontant = new JRDesignStaticText();
            libelleMontant.setWidth(200);
            libelleMontant.setHeight(15);
            libelleMontant.setText("Montant :  "+current.getMontantoperation());
            libelleMontant.setFontSize(10);
            libelleMontant.setForecolor(Color.BLACK);
            libelleMontant.setX(20);
            libelleMontant.setY(170);
            jrFame.addElement(libelleMontant);
            
            JRDesignStaticText libelleMontantTTlettre = new JRDesignStaticText();
            libelleMontantTTlettre.setWidth(480);
            libelleMontantTTlettre.setHeight(15);
            libelleMontantTTlettre.setText(montantTtcTouteLettres(current.getMontantoperation()));
            libelleMontantTTlettre.setFontSize(10);
            libelleMontantTTlettre.setForecolor(Color.BLACK);
            libelleMontantTTlettre.setX(20);
            libelleMontantTTlettre.setY(185);
            jrFame.addElement(libelleMontantTTlettre);

            
            if(current.getModereglement()!=null)
            {
            JRDesignStaticText libelleMode = new JRDesignStaticText();
            libelleMode.setWidth(200);
            libelleMode.setHeight(15);
            libelleMode.setText("Mode Reglement :  "+current.getModereglement().getLibelle());
            libelleMode.setFontSize(10); 
            libelleMode.setForecolor(Color.BLACK);
            libelleMode.setX(100);
            libelleMode.setY(205);
            jrFame.addElement(libelleMode);
            }
              if(current.getReference()!=null){
                  
            JRDesignStaticText libelleRef = new JRDesignStaticText();
            libelleRef.setWidth(200);
            libelleRef.setHeight(15);
            libelleRef.setText("Référence :  "+current.getReference());
            libelleRef.setFontSize(10);
            libelleRef.setForecolor(Color.BLACK);
            libelleRef.setX(100);
            libelleRef.setY(225);
            jrFame.addElement(libelleRef);
              }
              if(current.getBanque() !=null)
              {
            JRDesignStaticText libelleBanque = new JRDesignStaticText();
            libelleBanque.setWidth(200);
            libelleBanque.setHeight(15);
            libelleBanque.setText("Banque :  "+current.getBanque().getLibelle());
            libelleBanque.setFontSize(10);
            libelleBanque.setForecolor(Color.BLACK);
            libelleBanque.setX(100);
            libelleBanque.setY(245);
            jrFame.addElement(libelleBanque);
              }
              if(current.getDateechenace()!=null)
              {
            JRDesignStaticText libelleEcheance = new JRDesignStaticText();
            libelleEcheance.setWidth(200);
            libelleEcheance.setHeight(15);
            libelleEcheance.setText("Echéance :  "+current.getDateechenace());
            libelleEcheance.setFontSize(10);
            libelleEcheance.setForecolor(Color.BLACK);
            libelleEcheance.setX(100);
            libelleEcheance.setY(265);
            jrFame.addElement(libelleEcheance);
              }
            JRDesignStaticText libelleCachet = new JRDesignStaticText();
            libelleCachet.setWidth(200);
            libelleCachet.setHeight(15);
            libelleCachet.setText("Cachet & Signature");
            libelleCachet.setFontSize(10);
            libelleCachet.setForecolor(Color.BLACK);
            libelleCachet.setX(250);
            libelleCachet.setY(300);
            jrFame.addElement(libelleCachet);
            

            /*******************************************************/
            bandTiltle.addElement(jrFame);
            jasperDesign.setTitle(bandTiltle);
            /*******************************************************/



         
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap parameters = new HashMap();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

            DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
            Date d = new Date();
            String dd = dateFormat15.format(d);
            String nomFichier = "Rapport_OperationDivers" + dd.replace(" ", "_");
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment;filename=" + nomFichier + ".pdf");
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.setContentType("application/pdf");
            context.responseComplete();
        } catch (SQLException ex) {
            Logger.getLogger(OperationController.class.getName()).log(Level.SEVERE, null, ex);
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
}
