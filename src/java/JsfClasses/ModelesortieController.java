package JsfClasses;

import com.mysql.jdbc.Driver;
import entities.Depot;
import entities.DetailModeleSortie;
import entities.ModeleSortieFiltrage;
import entities.Modelesortie;

import entities.TableCorrespondance;
import entities.TypeOpCom;
import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import JsfClasses.util.JsfUtil;
import JsfClasses.util.PaginationHelper;
import SessionBeans.ModelesortieFacade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
//import net.sf.jasperreports.engine.JasperManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;
import org.primefaces.component.datatable.DataTable;
import SessionBeans.DepotFacade;
import SessionBeans.DetailModeleSortieFacade;
import SessionBeans.ModereglementFacade;
import SessionBeans.ModeleSortieFiltrageFacade;
import SessionBeans.TableCorrespondanceFacade;
import SessionBeans.TypeOpComFacade;
import entities.Modereglement;

@ManagedBean(name = "modelesortieController")
@ViewScoped
public class ModelesortieController implements Serializable {
    private Modelesortie current;
    private DataModel items = null;
    private ModelesortieFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @Resource
    private UserTransaction utx = null;
    private DetailModeleSortieFacade ejbDetailModeleFacade;
    private List<DetailModeleSortie>detailsModele;
    private DataTable dataTableDetails;
    private TypeOpComFacade ejbTypeOperation;
    private TableCorrespondanceFacade ejbTableCorrespondance;
    private ModereglementFacade ejbModeReglement;
    private ModeleSortieFiltrageFacade ejbModeleSortieFiltrage;
    private DepotFacade ejbDepotCom;
    @ManagedProperty(value="#{authetification}")
    private Boolean boutonAjouter;
    private Boolean boutonModifier;
    private Boolean boutonSupprimer;
    private Boolean boutonEtat;
    private boolean affichereglement;

   
     private String codeFiltre;
    private String libelleFiltre;

    public boolean isAffichereglement() {
 
        return affichereglement;
    }

    public void setAffichereglement(boolean affichereglement) {
        this.affichereglement = affichereglement;
    }
    
    public String getCodeFiltre() {
        return codeFiltre;
    }

    public void setCodeFiltre(String codeFiltre) {
        this.codeFiltre = codeFiltre;
    }

    public String getLibelleFiltre() {
        return libelleFiltre;
    }

    public void setLibelleFiltre(String libelleFiltre) {
        this.libelleFiltre = libelleFiltre;
    }
public String styleReg()
{
if (current.getReglementRapport()!=null)
{
    if (current.getReglementRapport()==false)
    return "display:none;";
            return "";
}
return "display:none;";
            }
public String styleSolde()
{
if (current.getSolde()!=null)
{
    if (current.getSolde()==false)
    return "display:none;";
            return "";
}
return "display:none;";
            }
    public String affiche()
    {
          System.out.println("affichereglement "+affichereglement);
     setAffichereglement(true);
        System.out.println("affichereglement "+affichereglement);
        return null;
    }
     public String rechercheMultipleCriteres() {
   
      String requette = "SELECT c FROM Modelesortie c WHERE c.idModeleSortie is not null and ";
        List<String> n = new ArrayList<String>();
        List<Object> o = new ArrayList<Object>();
        //WHERE e.cin = :cin
        //  e.numeroAffiliationAssuranceSociale = :numeroAffiliationAssuranceSociale
        String order = "";

        if (this.codeFiltre != null) {
            if (this.codeFiltre.isEmpty() == false) {
                requette = requette + "c.codeModeleSortie like :code and ";
                n.add("code");
                o.add("%" + this.codeFiltre + "%");
                //order=order+" nomTi";
            }

        }
        if (this.libelleFiltre != null) {
            if (this.libelleFiltre.isEmpty() == false) {
                requette = requette + "c.libelleModeleSortie like :libelle ";
                n.add("libelle");
                o.add("%" + this.libelleFiltre + "%");
                //order=order+" nomTi";
            }

        }
   
       
   

        if (requette.endsWith(" and ")) {
            requette = requette.substring(0, requette.lastIndexOf(" and "));
        }

        // requette=requette+" "+orderByRequette;
        try {
            System.out.println(requette);
            List<Modelesortie> lop = this.getFacade().findByParameterMultipleCreteria(requette, n, o);
            if (lop == null) {
                lop = new ArrayList<Modelesortie>();
            }
            items = new ListDataModel(lop);
        } catch (Exception er) {
            er.printStackTrace();
        }
        return "";
    }
  

    
    
    public SelectItem[] getDepotsItems() {
        try{
            List<Depot>ldp=this.ejbDepotCom.findAll();
            SelectItem[] itemsDepotss = new SelectItem[ldp.size()];
            Iterator itr=ldp.iterator();
            int xxx=0;
            while(itr.hasNext()){
                Depot d=(Depot)itr.next();
                itemsDepotss[xxx]=new SelectItem(d.getLibelle(),d.getLibelle());
                xxx++;
            }
            return itemsDepotss;
        }catch(Exception e){
            return new SelectItem[0];
        }
    }

    public Boolean getBoutonAjouter() {
        return boutonAjouter;
    }
    public void setBoutonAjouter(Boolean boutonAjouter) {
        this.boutonAjouter = boutonAjouter;
    }
    public Boolean getBoutonEtat() {
        return boutonEtat;
    }
    public void setBoutonEtat(Boolean boutonEtat) {
        this.boutonEtat = boutonEtat;
    }
    public Boolean getBoutonModifier() {
        return boutonModifier;
    }
    public void setBoutonModifier(Boolean boutonModifier) {
        this.boutonModifier = boutonModifier;
    }
    public Boolean getBoutonSupprimer() {
        return boutonSupprimer;
    }
    public void setBoutonSupprimer(Boolean boutonSupprimer) {
        this.boutonSupprimer = boutonSupprimer;
    }
    public ModelesortieController() {
       
        FacesContext facesContext=FacesContext.getCurrentInstance();
        ejbDetailModeleFacade = (DetailModeleSortieFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "detailModeleSortieJpa");
        ejbFacade = (ModelesortieFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "modeleSortieJpa");
        ejbTypeOperation = (TypeOpComFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "typeOpComJpa");
        ejbTableCorrespondance = (TableCorrespondanceFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "tableCorrespondanceJpa");
        ejbModeReglement = (ModereglementFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "modeReglementJpa");
        ejbModeleSortieFiltrage = (ModeleSortieFiltrageFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "modeleSortieFiltrageJpa");
        ejbDepotCom= (DepotFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "depotJpa");
    }
    @PostConstruct
    public void initConstructorAfter(){
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();
     
              try{
            this.affichereglement=(Boolean) map.get("affichereglement");
        }catch(Exception e){}
        try{
            this.boutonAjouter=(Boolean) map.get("boutonAjouter");
        }catch(Exception e){}
        try{
            this.boutonModifier=(Boolean) map.get("boutonModifier");
        }catch(Exception e){}
        try{
            this.boutonSupprimer=(Boolean) map.get("boutonSupprimer");
        }catch(Exception e){}
        try{
            this.boutonEtat=(Boolean) map.get("boutonEtat");
        }catch(Exception e){}
        try{
            this.current=(Modelesortie) map.get("current");
        }catch(Exception e){}
        try{
            this.dataTableDetails=(DataTable) map.get("dataTableDetails");
        }catch(Exception e){}
        try{
            this.detailsModele=(List<DetailModeleSortie>) map.get("detailsModele");
        }catch(Exception e){}
        try{
            this.depotsSelectionnees=(List<String>) map.get("depotsSelectionnees");
        }catch(Exception e){}
        try{
            this.sizeDepotsSelectionnes=(Integer) map.get("sizeDepotsSelectionnes");
        }catch(Exception e){}
        try{
            this.modeleProduitCom=(Boolean)map.get("modeleProduitCom");
        }catch(Exception e){}
        try{
            this.modeleTiersCom=(Boolean)map.get("modeleTiersCom");
        }catch(Exception e){}
        try{
            this.tableChoisi=(String) map.get("tableChoisi");
        }catch(Exception e){}
        try{
            this.selectedItemIndex=(Integer)map.get("selectedItemIndex");
        }catch(Exception e){}
        try{
            this.nomPage=(String) map.get("nomPage");
        }catch(Exception e){}
        try{
            this.listBol=(List<Boolean>) map.get("listBol");
        }catch(Exception e){}
        try{
            this.listFiltrage=(List<Boolean>) map.get("listFiltrage");
        }catch(Exception e){}
        try{
            this.items=(DataModel) map.get("items");
        }catch(Exception e){}
        try{
            this.listSommable=(List<Boolean>) map.get("listSommable");
        }catch(Exception e){}
        try{
            this.nomModeleChoisi=(String) map.get("nomModeleChoisi");
        }catch(Exception e){}
    }
    public DataTable getDataTableDetails() {
        return dataTableDetails;
    }

    public void setDataTableDetails(DataTable dataTableDetails) {
        this.dataTableDetails = dataTableDetails;
    }

    public List<DetailModeleSortie> getDetailsModele() {
        return detailsModele;
    }

    public void setDetailsModele(List<DetailModeleSortie> detailsModele) {
        this.detailsModele = detailsModele;
    }

    public Modelesortie getSelected() {
        if (current == null) {
            current = new Modelesortie();
            selectedItemIndex = -1;
        }
        return current;
    }

    private ModelesortieFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    if("journaux".equals(nomPage)){
                        return new ListDataModel(getFacade().findByParameter("Select m from Modelesortie m where m.typeModele.niTop>=0 and m.typeModele.nomTop!='Statistiques Produit 1' and m.idModeleSortie>=:idModeleSortie and m.typeModele.niTop not in (Select mm.typeModele.niTop from Modelesortie mm where mm.typeModele.nomTop like '%Dyn')","idModeleSortie",new Integer(0)));
                    }else{
                        return new ListDataModel(getFacade().findByParameter("Select m from Modelesortie m where m.idModeleSortie>=:idModeleSortie and m.typeModele IS NULL","idModeleSortie",new Integer(0)));
                    }
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        this.nomPage="";
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();
        map.put("boutonAjouter",this.boutonAjouter);
        map.put("boutonModifier",this.boutonModifier);
        map.put("boutonSupprimer",this.boutonSupprimer);
        map.put("boutonEtat",this.boutonEtat);
        return "modele_sortie_list";
    }

    public String prepareView() {
        current = (Modelesortie) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        this.nomPage="";
        return "View";
    }

    public String prepareCreate() {
        current = new Modelesortie();
        dataTableDetails=new DataTable();
        this.detailsModele=new ArrayList<DetailModeleSortie>();
        this.depotsSelectionnees=new ArrayList<String>();
        this.sizeDepotsSelectionnes=new Integer(100);
        this.modeleProduitCom=false;
        this.modeleTiersCom=false;
        this.tableChoisi="";
        selectedItemIndex = -1;
        this.nomPage="";
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();
        map.put("boutonAjouter",this.boutonAjouter);
        map.put("affichereglement",this.affichereglement);
        map.put("boutonModifier",boutonModifier);
        map.put("boutonSupprimer",boutonSupprimer);
        map.put("boutonEtat",boutonEtat);
        map.put("current",current);
        map.put("dataTableDetails",dataTableDetails);
        map.put("detailsModele",detailsModele);
        map.put("depotsSelectionnees",depotsSelectionnees);
        map.put("sizeDepotsSelectionnes",sizeDepotsSelectionnes);
        map.put("modeleProduitCom",modeleProduitCom);
        map.put("modeleTiersCom",modeleTiersCom);
        map.put("tableChoisi",tableChoisi);
        map.put("selectedItemIndex",selectedItemIndex);
        map.put("nomPage",nomPage);
        return "modele_sortie_creation";
    }

    public SelectItem[] getItemsAvailableSelectOneNomTables() {
        SelectItem[] items12 = new SelectItem[35];
        items12[0] = new SelectItem("---", "");
        items12[1] = new SelectItem("banque_com", "banque");
        items12[2] = new SelectItem("banque_versement", "banque versement");
        items12[3] = new SelectItem("caissier_com", "caissier");
        items12[4] = new SelectItem("categorie_com", "categorie");
        items12[5] = new SelectItem("categorie_produit_com", "categorie produit");       
        items12[6] = new SelectItem("chauffeur_com", "chauffeur");
        items12[7] = new SelectItem("classe_prix_com", "classe prix");
        items12[8] = new SelectItem("convoyeur_com", "convoyeur");
        items12[9] = new SelectItem("demandeur_com", "demandeur");
        items12[10] = new SelectItem("devise", "devise");
        items12[11] = new SelectItem("depot_com", "depot");
        items12[12] = new SelectItem("famille_com", "famille");
        items12[13] = new SelectItem("gouvernorat_com", "gouvernorat");
        items12[14] = new SelectItem("incoterme", "incoterme");
        items12[15] = new SelectItem("livreur_com", "livreur");
        items12[16] = new SelectItem("motifimpaye", "motif impayé");
        items12[17] = new SelectItem("nature_com", "nature");
    
        items12[18] = new SelectItem("nature_caisse_com", "nature caisse");
        items12[19] = new SelectItem("nature_od_oc", "nature od oc");
        items12[20] = new SelectItem("nature_prix", "nature prix");
        items12[21] = new SelectItem("produit_com", "produit");
        items12[22] = new SelectItem("pays_com", "pays");
        items12[23] = new SelectItem("point_vente_com", "point vente");
       

        items12[24] = new SelectItem("rayon_com", "rayon");
        items12[25] = new SelectItem("representant_com", "representant");
        items12[26] = new SelectItem("regime_financier", "regime financier");
        items12[27] = new SelectItem("secteur_com", "secteur");
        items12[28] = new SelectItem("sous_famille_com", "sous famille");
        items12[29] = new SelectItem("tiers_com", "Tiers");
        items12[30] = new SelectItem("tireur_com", "tireur");
        items12[31] = new SelectItem("tva_com", "tva");
        items12[32] = new SelectItem("tiers_caisse_com", "tiers caisse");
        items12[33] = new SelectItem("unite_com", "unite");
        items12[34] = new SelectItem("ville_com", "ville");



        return items12;
    }
    private String tableChoisi;

    public String getTableChoisi() {
        return tableChoisi;
    }

    public void setTableChoisi(String tableChoisi) {
        this.tableChoisi = tableChoisi;
    }
    
    private List<Boolean>listBol;

    public List<Boolean> getListBol() {
        return listBol;
    }

    public void setListBol(List<Boolean> listBol) {
        this.listBol = listBol;
    }
    
    public Boolean getSelct(){
        try{
        return this.listBol.get(this.dataTableDetails.getRowIndex()); 
        }catch(Exception e){}
        return false;
    }
    
    public void setSelct(Boolean b){
        try{
        this.listBol.set(this.dataTableDetails.getRowIndex(),b); 
        }catch(Exception e){}
    }
    
    private List<Boolean>listFiltrage;
    
    public Boolean getFiltrable(){
        try{
        return this.listFiltrage.get(this.dataTableDetails.getRowIndex()); 
        }catch(Exception e){}
        return false;
    }
    
    public void setFiltrable(Boolean b){
        try{
        this.listFiltrage.set(this.dataTableDetails.getRowIndex(),b); 
        }catch(Exception e){}
    }
    private boolean modeleProduitCom;
    public boolean isModeleProduitCom() {
        return modeleProduitCom;
    }
    public void setModeleProduitCom(boolean modeleProduitCom) {
        this.modeleProduitCom = modeleProduitCom;
    }
    public String create(){
        try {
            utx.begin();
  //          this.current.setIdModeleSortie((Integer)this.clePrimaire.numInterne(this.authentificationBean.baseDonneesChoisi+".Modelesortie"));
                this.current.setIdModeleSortie(new Integer(1));

            getFacade().create(current);
            Iterator i=this.detailsModele.iterator();
            Integer inx=new Integer(0);
            while(i.hasNext()){
                DetailModeleSortie dd=(DetailModeleSortie)i.next();
                dd.setNomColonne(this.correspondreColonneBackward(dd.getNomColonne()));
                if(this.listBol.get(inx)){
                    dd.setModeleSortie(current);
        //            dd.setIdDetailModeleSortie((Integer)this.clePrimaire.numInterne(this.authentificationBean.baseDonneesChoisi+".DetailModeleSortie"));
   dd.setIdDetailModeleSortie((new Integer(1)));

                    this.ejbDetailModeleFacade.create(dd);
                }
                if(this.listFiltrage.get(inx)){
                    ModeleSortieFiltrage msf=new ModeleSortieFiltrage();
                  //  msf.setIdModeleSortieFiltrage((Integer)this.clePrimaire.numInterne(this.authentificationBean.baseDonneesChoisi+".ModeleSortieFiltrage"));
                    msf.setIdModeleSortieFiltrage(new Integer(1));
                   
                    msf.setModeleSortiee(this.current);
                    msf.setNomColonneFiltrage(dd.getNomColonne());
                    msf.setTypeColonneFiltrage(dd.getTypeColonne());
                    msf.setClePrimTableColonne(dd.getClePrimTable());
                    msf.setNomTableColonne(dd.getTableColonne());
                    msf.setAppelePar(dd.getAppelePar());
                    msf.setTableAppeleApr(dd.getTableAppelePar());
                    this.ejbModeleSortieFiltrage.create(msf);
                }
                inx++;
            }
            if(this.modeleProduitCom){
                Iterator itChaineDepot=this.depotsSelectionnees.iterator();
                while(itChaineDepot.hasNext()){
                    String sDepot=(String)itChaineDepot.next();
                    Depot d=this.ejbDepotCom.findByParameterSingleResult("Select d from DepotCom d where d.nomDepot=:nomDepot","nomDepot",sDepot);
                  
                }
            }
            this.utx.commit();
            JsfUtil.addSuccessMessage("Modele Cree");
            prepareCreate();
        } catch (Exception e) {e.printStackTrace();
            JsfUtil.addErrorMessage("Operation Echouee");
            try{
                utx.rollback();
            }catch(Exception rz){}
        }
        return null;
    }

    public String prepareEdit() {
        return "modele_sortie_edition";
    }

    private Boolean existe(DetailModeleSortie d, List<Integer> ll22) {
        Iterator i=ll22.iterator();
        while(i.hasNext()){
            Integer l2=(Integer)i.next();
            if(l2.equals(d.getIdDetailModeleSortie())){
                return true;
            }
        }
        return false;
    }
    
    public String destroy() {
        current = (Modelesortie) getItems().getRowData();
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
             JsfUtil.addSuccessMessage("Transaction reussie");
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
    
    
    private String clientFournisseur;

    public String getClientFournisseur() {
        return clientFournisseur;
    }

    public void setClientFournisseur(String clientFournisseur) {
        this.clientFournisseur = clientFournisseur;
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
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = Modelesortie.class)
    public static class ModelesortieControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ModelesortieController controller = (ModelesortieController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "modelesortieController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Modelesortie) {
                Modelesortie o = (Modelesortie) object;
                return getStringKey(o.getIdModeleSortie());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ModelesortieController.class.getName());
            }
        }
    }
    
    private List<String>depotsSelectionnees;
    public List<String> getDepotsSelectionnees() {
        return depotsSelectionnees;
    }
    public void setDepotsSelectionnees(List<String> depotsSelectionnees) {
        this.depotsSelectionnees = depotsSelectionnees;
    }
    private Integer sizeDepotsSelectionnes;
    public Integer getSizeDepotsSelectionnes() {
        return sizeDepotsSelectionnes;
    }
    public void setSizeDepotsSelectionnes(Integer sizeDepotsSelectionnes) {
        this.sizeDepotsSelectionnes = sizeDepotsSelectionnes;
    }
    private boolean modeleTiersCom;
    public boolean isModeleTiersCom() {
        return modeleTiersCom;
    }
    public void setModeleTiersCom(boolean modeleTiersCom) {
        this.modeleTiersCom = modeleTiersCom;
    }
      public void handleChangeEvent(){
        this.current.setCodeModeleSortie(this.tableChoisi);
        try{
            if(this.tableChoisi.toLowerCase().equals("produit_com")){
                this.modeleProduitCom=true;
            }else{
                this.modeleProduitCom=false;
            }
        }catch(Exception e){
            this.modeleProduitCom=false;
        }
        try{
            if(this.tableChoisi.toLowerCase().equals("tiers_com")){
                this.modeleTiersCom=true;
            }else{
                this.modeleTiersCom=false;
            }
        }catch(Exception e){
            this.modeleTiersCom=false;
        }
        try{
            Driver monDriver = new com.mysql.jdbc.Driver();
            StringTokenizer getUrl=new StringTokenizer(this.getFacade().urlCourante(),"**");
            String url =getUrl.nextToken();
            String login=getUrl.nextToken();
            String password =getUrl.nextToken();
            String nomBaseDeDonnes=url.substring(url.lastIndexOf("/")+1);
            DriverManager.registerDriver(monDriver);
            Connection connection = DriverManager.getConnection(url,login, password);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT column_name as nomColonne,COLUMNS.table_name as tableColonne,"
                    + "(SELECT KEY_COLUMN_USAGE.column_name FROM `information_schema`.`KEY_COLUMN_USAGE` where table_schema='"+nomBaseDeDonnes+"' "
                    + "and table_name='"+this.tableChoisi+"' and constraint_name='PRIMARY' limit 1) as nom_table,data_type FROM `information_schema`.`COLUMNS` "
                    + "where Table_SCHEMA='"+nomBaseDeDonnes+"' and TABLE_NAME='"+this.tableChoisi+"' "
                    + "and column_key!='PRI' and column_key!='MUL' and column_name not like '%user%' and column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1) order by (column_name) ASC");
            this.detailsModele=new ArrayList<DetailModeleSortie>();
            listBol=new ArrayList<Boolean>();
            listFiltrage=new ArrayList<Boolean>();
            Integer x=new Integer(1);
            if (rs != null) {
                    while (rs.next()) {
                        DetailModeleSortie d25=new DetailModeleSortie();
                        d25.setNomColonne(this.correspondreColonne(rs.getString(1)));
                        d25.setTableColonne(rs.getString(2));
                        d25.setClePrimTable(rs.getString(3));
                        d25.setOrdreColonne(x);
                        d25.setLongueurColonne(150);
                        d25.setFontSizeColonne(10);
                        d25.setTypeColonne(rs.getString(4));
                        if (d25.getNomColonne().equalsIgnoreCase("")==false)
                       
                        { this.detailsModele.add(d25);
                        listBol.add(false);
                        listFiltrage.add(false);
                        x++;}
                    }
                }
             rs = statement.executeQuery("SELECT c.table_name,c.column_name,t.table_name,t.column_name,t.referenced_column_name,c.data_type "
                     + "FROM `information_schema`.`COLUMNS` c join (SELECT * FROM `information_schema`.`KEY_COLUMN_USAGE` "
                     + "where table_name='"+tableChoisi+"' and constraint_name!='PRIMARY' and table_schema='"+nomBaseDeDonnes+"' and referenced_table_name !='user') t "
                     + "on t.referenced_table_name=c.table_name where c.table_schema='"+nomBaseDeDonnes+"' and column_key!='PRI' and column_key!='MUL' "
                     + "and c.column_name not like '%user%' and c.column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1)");
             if (rs != null) {
                    while (rs.next()) {
                        DetailModeleSortie d25=new DetailModeleSortie();
                        d25.setTableColonne(rs.getString(1));
                        d25.setNomColonne(this.correspondreColonne(rs.getString(2)));
                        d25.setTableAppelePar(rs.getString(3));
                        d25.setAppelePar(rs.getString(4));
                        d25.setClePrimTable(rs.getString(5));
                        d25.setOrdreColonne(x);
                        d25.setLongueurColonne(150);
                        d25.setFontSizeColonne(10);
                        d25.setTypeColonne(rs.getString(6));
                        if(((d25.getNomColonne().equalsIgnoreCase("")==false)))
                        {      this.detailsModele.add(d25);
                        listBol.add(false);
                        listFiltrage.add(false);
                        x++;}
                    }
                }
             if(modeleProduitCom){
                rs = statement.executeQuery("SELECT column_name as nomColonne,data_type FROM `information_schema`.`COLUMNS` "
                    + "where Table_SCHEMA='"+nomBaseDeDonnes+"' and TABLE_NAME='produit_com_unite_production' "
                    + "and column_key!='PRI' and column_key!='MUL' and column_name not like '%user%' and column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1) order by (column_name) ASC");
                if (rs != null) {
                    while (rs.next()) {
                        DetailModeleSortie d25=new DetailModeleSortie();
                        d25.setTableColonne("produit_com_unite_production");
                        d25.setNomColonne(this.correspondreColonne(rs.getString(1)));
                        d25.setOrdreColonne(x);
                        d25.setClePrimTable(" ");
                        d25.setLongueurColonne(150);
                        d25.setFontSizeColonne(10);
                        d25.setTypeColonne(rs.getString(2));
                        this.detailsModele.add(d25);
                        listBol.add(false);
                        listFiltrage.add(false);
                        x++;
                    }
                }
                rs = statement.executeQuery("SELECT c.table_name,c.column_name,t.table_name,t.column_name,t.referenced_column_name,c.data_type "
                     + "FROM `information_schema`.`COLUMNS` c join (SELECT * FROM `information_schema`.`KEY_COLUMN_USAGE` "
                     + "where table_name='produit_com_unite_production' and constraint_name!='PRIMARY' and table_schema='"+nomBaseDeDonnes+"' and referenced_table_name !='user') t "
                     + "on t.referenced_table_name=c.table_name where c.table_schema='"+nomBaseDeDonnes+"' and column_key!='PRI' and column_key!='MUL' "
                     + "and c.column_name not like '%user%' and c.table_name!='produit_com' and c.column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1)");
                 if (rs != null) {
                        while (rs.next()) {
                            DetailModeleSortie d25=new DetailModeleSortie();
                            d25.setTableColonne(rs.getString(1));
                            d25.setNomColonne(this.correspondreColonne(rs.getString(2)));
                            d25.setTableAppelePar(rs.getString(3));
                            d25.setAppelePar(rs.getString(4));
                            d25.setClePrimTable(rs.getString(5));
                            d25.setOrdreColonne(x);
                            d25.setLongueurColonne(150);
                            d25.setFontSizeColonne(10);
                            d25.setTypeColonne(rs.getString(6));
                           
                        { 
                            this.detailsModele.add(d25);
                            listBol.add(false);
                            listFiltrage.add(false);
                            x++;
                        }
                         
                        }
                    }
                            Collections.sort(detailsModele, new CustomComparator());
                           int i=0;
                        Iterator ii=this.detailsModele.iterator();
        
        while(ii.hasNext()){
            DetailModeleSortie e=(DetailModeleSortie)ii.next();
            e.setOrdreColonne(i+1);
            i++;
        }   }else{
                 if(modeleTiersCom){
                     rs = statement.executeQuery("SELECT column_name as nomColonne,data_type FROM `information_schema`.`COLUMNS` "
                        + "where Table_SCHEMA='"+nomBaseDeDonnes+"' and TABLE_NAME='tiers_com_unite_production' "
                        + "and column_key!='PRI' and column_key!='MUL' and column_name not like '%user%' and column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1) order by (column_name) ASC");
                    if (rs != null) {
                        while (rs.next()) {
                            DetailModeleSortie d25=new DetailModeleSortie();
                            d25.setTableColonne("tiers_com_unite_production");
                            d25.setNomColonne(this.correspondreColonne(rs.getString(1)));
                            d25.setOrdreColonne(x);
                            d25.setClePrimTable(" ");
                            d25.setLongueurColonne(150);
                            d25.setFontSizeColonne(10);
                            d25.setTypeColonne(rs.getString(2));
                             if (d25.getNomColonne().equalsIgnoreCase("")==false)
                             {
                            this.detailsModele.add(d25);
                            
                            listBol.add(false);
                            listFiltrage.add(false);
                            x++;}
                        }
                    }
                    rs = statement.executeQuery("SELECT c.table_name,c.column_name,t.table_name,t.column_name,t.referenced_column_name,c.data_type "
                         + "FROM `information_schema`.`COLUMNS` c join (SELECT * FROM `information_schema`.`KEY_COLUMN_USAGE` "
                         + "where table_name='tiers_com_unite_production' and constraint_name!='PRIMARY' and table_schema='"+nomBaseDeDonnes+"' and referenced_table_name !='user') t "
                         + "on t.referenced_table_name=c.table_name where c.table_schema='"+nomBaseDeDonnes+"' and column_key!='PRI' and column_key!='MUL' "
                         + "and c.column_name not like '%user%' and c.table_name!='tiers_com' and c.column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1)");
                     if (rs != null) {
                        while (rs.next()) {
                            DetailModeleSortie d25=new DetailModeleSortie();
                            d25.setTableColonne(rs.getString(1));
                            d25.setNomColonne(this.correspondreColonne(rs.getString(2)));
                            d25.setTableAppelePar(rs.getString(3));
                            d25.setAppelePar(rs.getString(4));
                            d25.setClePrimTable(rs.getString(5));
                            d25.setOrdreColonne(x);
                            d25.setLongueurColonne(150);
                            d25.setFontSizeColonne(10);
                            d25.setTypeColonne(rs.getString(6));
                
                        { 
                            this.detailsModele.add(d25);
                            
                            listBol.add(false);
                            listFiltrage.add(false);
                            
                            x++;
                        }
                        }
                    }
                           Collections.sort(detailsModele, new CustomComparator());
                           int i=0;
                        Iterator ii=this.detailsModele.iterator();
        
        while(ii.hasNext()){
            DetailModeleSortie e=(DetailModeleSortie)ii.next();
            e.setOrdreColonne(i+1);
            i++;
        }   
                 }
             }
             rs.close();
             statement.close();
             connection.close();
      
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
   
    public void inputChangement(ValueChangeEvent event) {
                PhaseId phaseId = event.getPhaseId();
                String newValue = (String) event.getNewValue();
                if (phaseId.equals(PhaseId.ANY_PHASE)){
                    event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
                    event.queue();
                    }
                else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)){
                        System.out.println(newValue);
                }
    }
    
    public void supprimerLigneModeleListener(){
        Iterator ii=this.detailsModele.iterator();
        Integer i=this.dataTableDetails.getRowIndex();
        List<DetailModeleSortie>l=new ArrayList<DetailModeleSortie>();
        Integer x=new Integer(0);
        while(ii.hasNext()){
            DetailModeleSortie e=(DetailModeleSortie)ii.next();
            if(!(i.equals(x))){
                l.add(e);
            }
            x++;
        }
        detailsModele=new ArrayList<DetailModeleSortie>();
        detailsModele=l;
    }
    
    public String supprimerModele(){
        try{
            utx.begin();
            Modelesortie m=(Modelesortie) this.items.getRowData();
            boolean modeleProduitComm=m.getCodeModeleSortie().equalsIgnoreCase("produit_com");
            System.out.println("modele:"+m.getIdModeleSortie());
            try{
                Iterator i=this.ejbDetailModeleFacade.findByParameter("Select d from DetailModeleSortie d where d.modeleSortie=:m","m",m).iterator();
                while(i.hasNext()){
                    DetailModeleSortie d=(DetailModeleSortie)i.next();
                    this.ejbDetailModeleFacade.remove(d);
                }
            }catch(Exception e){}
            try{
                Iterator i5=this.ejbModeleSortieFiltrage.findByParameter("Select d from ModeleSortieFiltrage d where d.modeleSortiee=:m","m",m).iterator();
                while(i5.hasNext()){
                    ModeleSortieFiltrage d5=(ModeleSortieFiltrage)i5.next();
                    this.ejbModeleSortieFiltrage.remove(d5);
                }
            }catch(Exception e){}
         
            this.getFacade().remove(m);
            utx.commit();
             JsfUtil.addSuccessMessage("Transaction reussie");
            this.prepareList();
        }catch(Exception er){er.printStackTrace();
            try{
                utx.rollback();
            }catch(Exception r){}
            JsfUtil.addErrorMessage("Transaction echouee");
        }
        return null;
    }

  public String modifierSetup(){
        this.nomPage="";
        this.dataTableDetails=new DataTable();
        this.current=(Modelesortie)this.items.getRowData();
        this.tableChoisi=this.current.getCodeModeleSortie();
        try{
            if(this.tableChoisi.toLowerCase().equals("tiers_com")){
                this.modeleTiersCom=true;
            }else{
                this.modeleTiersCom=false;
            }
        }catch(Exception negd){
            this.modeleTiersCom=false;
        }
        try{
            if(this.tableChoisi.toLowerCase().equals("produit_com")){
                this.modeleProduitCom=true;
            }else{
                this.modeleProduitCom=false;
            }
        }catch(Exception negd){
            this.modeleProduitCom=false;
        }
        this.depotsSelectionnees=new ArrayList<String>();
        if(this.modeleProduitCom){
           
        }
        try{
            Driver monDriver = new com.mysql.jdbc.Driver();
            StringTokenizer getUrl=new StringTokenizer(this.getFacade().urlCourante(),"**");
            String url =getUrl.nextToken();
            String login=getUrl.nextToken();
            String password =getUrl.nextToken();
            String nomBaseDeDonnes=url.substring(url.lastIndexOf("/")+1);
            DriverManager.registerDriver(monDriver);
            Connection connection = DriverManager.getConnection(url,login, password);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT column_name as nomColonne,COLUMNS.table_name as tableColonne,"
                    + "(SELECT KEY_COLUMN_USAGE.column_name FROM `information_schema`.`KEY_COLUMN_USAGE` where table_schema='"+nomBaseDeDonnes+"' "
                    + "and table_name='"+this.tableChoisi+"' and constraint_name='PRIMARY' limit 1) as nom_table,data_type FROM `information_schema`.`COLUMNS` "
                    + "where Table_SCHEMA='"+nomBaseDeDonnes+"' and TABLE_NAME='"+this.tableChoisi+"' "
                    + "and column_key!='PRI' and column_key!='MUL' and column_name not like '%user%' and column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1) order by (column_name) ASC");
           String q="SELECT column_name as nomColonne,COLUMNS.table_name as tableColonne,"
                    + "(SELECT KEY_COLUMN_USAGE.column_name FROM `information_schema`.`KEY_COLUMN_USAGE` where table_schema='"+nomBaseDeDonnes+"' "
                    + "and table_name='"+this.tableChoisi+"' and constraint_name='PRIMARY' limit 1) as nom_table,data_type FROM `information_schema`.`COLUMNS` "
                    + "where Table_SCHEMA='"+nomBaseDeDonnes+"' and TABLE_NAME='"+this.tableChoisi+"' "
                    + "and column_key!='PRI' and column_key!='MUL' and column_name not like '%user%' and column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1) order by (column_name) ASC";
            System.out.println("q "+q); 
       //     List <ModeleSortieParametrage> listParams=new ArrayList<ModeleSortieParametrage>();
           this.detailsModele=new ArrayList<DetailModeleSortie>();
            listBol=new ArrayList<Boolean>();
            listFiltrage=new ArrayList<Boolean>();
            Integer x=new Integer(1);
            
            if (rs != null) {
                    while (rs.next()) {
                        DetailModeleSortie d25=new DetailModeleSortie();
                  //         ModeleSortieParametrage modeParam=new ModeleSortieParametrage();
                        String nomColonne=rs.getString(1);
                        d25.setNomColonne(this.correspondreColonne(nomColonne));
                        d25.setTableColonne(rs.getString(2));
                        d25.setClePrimTable(rs.getString(3));
                        d25.setTypeColonne(rs.getString(4));
                        d25.setFontSizeColonne(10);
                        d25.setModeleSortie(this.current);
                        try{
                            DetailModeleSortie ddfd=this.ejbDetailModeleFacade.findByParameterSingleResult("Select d from DetailModeleSortie d "
                                    + "where d.modeleSortie=:modeleSortie and d.nomColonne='"+nomColonne+"' and "
                                    + "d.appelePar IS NULL","modeleSortie",this.current);
                     
                            d25.setOrdreColonne(ddfd.getOrdreColonne());
                            d25.setLongueurColonne(ddfd.getLongueurColonne());
                            d25.setOrderByInstruction(ddfd.getOrderByInstruction());
                            d25.setNumeroDeOrderBy(ddfd.getNumeroDeOrderBy());
                            d25.setAppelePar(ddfd.getAppelePar());
                            d25.setTableAppelePar(ddfd.getTableAppelePar());
                            d25.setIdDetailModeleSortie(ddfd.getIdDetailModeleSortie());
                            d25.setTypeColonne(ddfd.getTypeColonne());
                            
                            try{
                                ddfd.getFontSizeColonne().toString();
                                d25.setFontSizeColonne(ddfd.getFontSizeColonne());
                            }catch(Exception e){d25.setFontSizeColonne(10);}
                           listBol.add(true);
                   //         modeParam.setSelect(true);
                        }catch(Exception ertu){
                   
                     //          modeParam.setSelect(false);
                            listBol.add(false);
                        
                            d25.setOrdreColonne(x);
                            d25.setLongueurColonne(150);
                            d25.setOrderByInstruction(false);
                            d25.setNumeroDeOrderBy(0);
                              if (d25.getNomColonne().equalsIgnoreCase("")==false)
                       
                        {  listBol.add(false);
                        }
                           
                        }
                        try{
                            List<String>ss=new ArrayList<String>();
                            List<Object>oo=new ArrayList<Object>();
                            ss.add("modeleSortiee");
                            ss.add("nomColonneFiltrage");
                            oo.add(this.current);
                            oo.add(nomColonne);
                       /*     ModeleSortieFiltrage ddf2=this.ejbModeleSortieFiltrage.findByParameterSingleResultMultipleCreteria("Select d from ModeleSortieFiltrage d "
                                    + "where d.modeleSortiee=:modeleSortiee and d.nomColonneFiltrage=:nomColonneFiltrage "
                                    + "and d.appelePar IS NULL",ss,oo);*/
                          listFiltrage.add(true);
//                               modeParam.setFiltre(true);
                        }catch(Exception ertu){
                            listFiltrage.add(false);
    //                        modeParam.setFiltre(false);
                        }
                                                 if (d25.getNomColonne().equalsIgnoreCase("")==false)
                         {   this.detailsModele.add(d25);
                             System.out.println("x " +x+" "+d25.getNomColonne());
//  modeParam.setModele(d25);
  //                      listParams.add(modeParam);                      
                             x++;}
                    }
                }
             rs = statement.executeQuery("SELECT c.table_name,c.column_name,t.table_name,t.column_name,t.referenced_column_name,c.data_type "
                     + "FROM `information_schema`.`COLUMNS` c join (SELECT * FROM `information_schema`.`KEY_COLUMN_USAGE` "
                     + "where table_name='"+tableChoisi+"' and constraint_name!='PRIMARY' and table_schema='"+nomBaseDeDonnes+"' and referenced_table_name !='user') t "
                     + "on t.referenced_table_name=c.table_name where c.table_schema='"+nomBaseDeDonnes+"' and column_key!='PRI' and column_key!='MUL' and c.column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1)"
                     + "and c.column_name not like '%user%'");
             String q1="SELECT c.table_name,c.column_name,t.table_name,t.column_name,t.referenced_column_name,c.data_type "
                     + "FROM `information_schema`.`COLUMNS` c join (SELECT * FROM `information_schema`.`KEY_COLUMN_USAGE` "
                     + "where table_name='"+tableChoisi+"' and constraint_name!='PRIMARY' and table_schema='"+nomBaseDeDonnes+"' and referenced_table_name !='user') t "
                     + "on t.referenced_table_name=c.table_name where c.table_schema='"+nomBaseDeDonnes+"' and column_key!='PRI' and column_key!='MUL' and c.column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1)"
                     + "and c.column_name not like '%user%'";
             System.out.println("q1 "+q1);
             if (rs != null) {
                    while (rs.next()) {
                        DetailModeleSortie d25=new DetailModeleSortie();
            //            ModeleSortieParametrage modeParam=new ModeleSortieParametrage();
                        String nomColonne=rs.getString(2);
                        d25.setTableColonne(rs.getString(1));
                        d25.setNomColonne(this.correspondreColonne(nomColonne));
                        d25.setTableAppelePar(rs.getString(3));
                        d25.setAppelePar(rs.getString(4));
                        d25.setClePrimTable(rs.getString(5));
                        d25.setTypeColonne(rs.getString(6));
                        d25.setOrdreColonne(x);
                        d25.setLongueurColonne(150);
                        d25.setFontSizeColonne(10);
                        try{
                            DetailModeleSortie ddfd=this.ejbDetailModeleFacade.findByParameterSingleResult("Select d from DetailModeleSortie d "
                                    + "where d.modeleSortie=:modeleSortie and d.nomColonne='"+nomColonne+"' and "
                                    + "d.appelePar='"+d25.getAppelePar()+"'","modeleSortie",this.current);
                          //  listBol.add(true);
                            d25.setOrdreColonne(ddfd.getOrdreColonne());
                            d25.setLongueurColonne(ddfd.getLongueurColonne());
                            d25.setOrderByInstruction(ddfd.getOrderByInstruction());
                            d25.setNumeroDeOrderBy(ddfd.getNumeroDeOrderBy());
                            d25.setAppelePar(ddfd.getAppelePar());
                            d25.setTableAppelePar(ddfd.getTableAppelePar());
                            d25.setIdDetailModeleSortie(ddfd.getIdDetailModeleSortie());
                            d25.setTypeColonne(ddfd.getTypeColonne());
                            try{
                                ddfd.getFontSizeColonne().toString();
                                d25.setFontSizeColonne(ddfd.getFontSizeColonne());
                            }catch(Exception e){d25.setFontSizeColonne(10);}
                        
                         listBol.add(true);
              //              modeParam.setSelect(true);
                        }catch(Exception ertu){
                   
                //               modeParam.setSelect(false);
                            listBol.add(false);
                            d25.setOrdreColonne(x);
                            d25.setLongueurColonne(150);
                            d25.setOrderByInstruction(false);
                            d25.setNumeroDeOrderBy(0);
                            //           if((this.afficheColonne(rs.getString(2)))&&((d25.getNomColonne().equalsIgnoreCase("")==false)))
                          //  listBol.add(false);
                        }
                        try{
                            List<String>ss=new ArrayList<String>();
                            List<Object>oo=new ArrayList<Object>();
                            ss.add("modeleSortiee");
                            ss.add("nomColonneFiltrage");
                            oo.add(this.current);
                            oo.add(nomColonne);
                  //          ModeleSortieFiltrage ddf2=this.ejbModeleSortieFiltrage.findByParameterSingleResultMultipleCreteria("Select d from ModeleSortieFiltrage d where d.modeleSortiee=:modeleSortiee and d.nomColonneFiltrage=:nomColonneFiltrage",ss,oo);
                             listFiltrage.add(true);
                    //           modeParam.setFiltre(true);
                        }catch(Exception ertu){
                            listFiltrage.add(false);
                      //      modeParam.setFiltre(false);
                        }
                                     //   if((this.afficheColonne(rs.getString(2)))&&((d25.getNomColonne().equalsIgnoreCase("")==false)))
                       
                        {
                            
                        this.detailsModele.add(d25);
                                System.out.println("x1 " +x+" "+d25.getNomColonne());
                        //           modeParam.setModele(d25);
                        //listParams.add(modeParam);
                        x++;}
                    }
                }
             if(modeleProduitCom){
                rs = statement.executeQuery("SELECT column_name as nomColonne,data_type FROM `information_schema`.`COLUMNS` "
                    + "where Table_SCHEMA='"+nomBaseDeDonnes+"' and TABLE_NAME='produit_com_unite_production' "
                    + "and column_key!='PRI' and column_key!='MUL' and column_name not like '%user%' and column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1) order by (column_name) ASC");
              String q2="SELECT column_name as nomColonne,data_type FROM `information_schema`.`COLUMNS` "
                    + "where Table_SCHEMA='"+nomBaseDeDonnes+"' and TABLE_NAME='produit_com_unite_production' "
                    + "and column_key!='PRI' and column_key!='MUL' and column_name not like '%user%' and column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1) order by (column_name) ASC";
                 System.out.println("q2 "+q2);
                if (rs != null) {
                    while (rs.next()) {
                        DetailModeleSortie d25=new DetailModeleSortie();
                     //   ModeleSortieParametrage modeParam=new ModeleSortieParametrage();
                        d25.setTableColonne("produit_com_unite_production");
                        String nomColonne=rs.getString(1);
                        d25.setNomColonne(this.correspondreColonne(nomColonne));
                        d25.setOrdreColonne(x);
                        d25.setClePrimTable(" ");
                        d25.setTypeColonne(rs.getString(2));
                        d25.setLongueurColonne(150);
                        d25.setFontSizeColonne(10);
                        try{
                            DetailModeleSortie ddfd=this.ejbDetailModeleFacade.findByParameterSingleResult("Select d from DetailModeleSortie d "
                                    + "where d.modeleSortie=:modeleSortie and d.nomColonne='"+nomColonne+"' "
                                    + "and d.appelePar IS Null","modeleSortie",this.current);
                    
                            d25.setOrdreColonne(ddfd.getOrdreColonne());
                            d25.setLongueurColonne(ddfd.getLongueurColonne());
                            d25.setOrderByInstruction(ddfd.getOrderByInstruction());
                            d25.setNumeroDeOrderBy(ddfd.getNumeroDeOrderBy());
                            d25.setAppelePar(ddfd.getAppelePar());
                            d25.setTableAppelePar(ddfd.getTableAppelePar());
                            d25.setIdDetailModeleSortie(ddfd.getIdDetailModeleSortie());
                            d25.setTypeColonne(ddfd.getTypeColonne());
                            try{
                                ddfd.getFontSizeColonne().toString();
                                d25.setFontSizeColonne(ddfd.getFontSizeColonne());
                            }catch(Exception e){d25.setFontSizeColonne(10);}
                        listBol.add(true);
                       //     modeParam.setSelect(true);
                        }catch(Exception ertu){
                   
                         //      modeParam.setSelect(false);
                            listBol.add(false);
                            d25.setOrdreColonne(x);
                            d25.setLongueurColonne(150);
                            d25.setOrderByInstruction(false);
                            d25.setNumeroDeOrderBy(0);
                      
                        }
                        try{
                            List<String>ss=new ArrayList<String>();
                            List<Object>oo=new ArrayList<Object>();
                            ss.add("modeleSortiee");
                            ss.add("nomColonneFiltrage");
                            oo.add(this.current);
                            oo.add(nomColonne);
                            ModeleSortieFiltrage ddf2=this.ejbModeleSortieFiltrage.findByParameterSingleResultMultipleCreteria("Select d from ModeleSortieFiltrage d "
                                    + "where d.modeleSortiee=:modeleSortiee and d.nomColonneFiltrage=:nomColonneFiltrage and "
                                    + "d.appelePar IS NULL",ss,oo);
                         
						   listFiltrage.add(true);
                           //    modeParam.setFiltre(true);
                        }catch(Exception ertu){
                            listFiltrage.add(false);
                         //   modeParam.setFiltre(false);
                        }
                                   if(((d25.getNomColonne().equalsIgnoreCase("")==false)))
                                   {    this.detailsModele.add(d25);
                                       System.out.println("x " +x+" "+d25.getNomColonne()+" ordre "+d25.getOrdreColonne());
                        x++;
              //             modeParam.setModele(d25);
                //        listParams.add(modeParam);
                                   }
                    }
                }
                rs = statement.executeQuery("SELECT c.table_name,c.column_name,t.table_name,t.column_name,t.referenced_column_name,c.data_type "
                     + "FROM `information_schema`.`COLUMNS` c join (SELECT * FROM `information_schema`.`KEY_COLUMN_USAGE` "
                     + "where table_name='produit_com_unite_production' and constraint_name!='PRIMARY' and table_schema='"+nomBaseDeDonnes+"' and referenced_table_name !='user') t "
                     + "on t.referenced_table_name=c.table_name where c.table_schema='"+nomBaseDeDonnes+"' and column_key!='PRI' and column_key!='MUL' "
                     + "and c.column_name not like '%user%' and c.column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1) and c.table_name!='produit_com'");
                     String q3="SELECT c.table_name,c.column_name,t.table_name,t.column_name,t.referenced_column_name,c.data_type "
                     + "FROM `information_schema`.`COLUMNS` c join (SELECT * FROM `information_schema`.`KEY_COLUMN_USAGE` "
                     + "where table_name='produit_com_unite_production' and constraint_name!='PRIMARY' and table_schema='"+nomBaseDeDonnes+"' and referenced_table_name !='user') t "
                     + "on t.referenced_table_name=c.table_name where c.table_schema='"+nomBaseDeDonnes+"' and column_key!='PRI' and column_key!='MUL' "
                     + "and c.column_name not like '%user%' and c.column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1) and c.table_name!='produit_com'";
                System.out.println("q3 "+q3);
                if (rs != null) {
                        while (rs.next()) {
                            DetailModeleSortie d25=new DetailModeleSortie();
                  //          ModeleSortieParametrage modeParam=new ModeleSortieParametrage();
                            d25.setTableColonne(rs.getString(1));
                            String nomColonne=rs.getString(2);
                            d25.setNomColonne(this.correspondreColonne(nomColonne));
                            d25.setTableAppelePar(rs.getString(3));
                            d25.setAppelePar(rs.getString(4));
                            System.out.println("appelé par "+d25.getAppelePar());
                            d25.setClePrimTable(rs.getString(5));
                            d25.setOrdreColonne(x);
                            d25.setLongueurColonne(150);
                            d25.setTypeColonne(rs.getString(6));
                            d25.setFontSizeColonne(10);
                            try{
                                DetailModeleSortie ddfd=this.ejbDetailModeleFacade.findByParameterSingleResult("Select d from DetailModeleSortie d where "
                                        + "d.modeleSortie=:modeleSortie and d.nomColonne='"+nomColonne+"' "
                                        + "and d.appelePar='"+d25.getAppelePar()+"'","modeleSortie",this.current);
                            
                                d25.setOrdreColonne(ddfd.getOrdreColonne());
                                d25.setLongueurColonne(ddfd.getLongueurColonne());
                                d25.setOrderByInstruction(ddfd.getOrderByInstruction());
                                d25.setNumeroDeOrderBy(ddfd.getNumeroDeOrderBy());
                                d25.setAppelePar(ddfd.getAppelePar());
                                d25.setTableAppelePar(ddfd.getTableAppelePar());
                                d25.setIdDetailModeleSortie(ddfd.getIdDetailModeleSortie());
                                d25.setTypeColonne(ddfd.getTypeColonne());
                                try{
                                    ddfd.getFontSizeColonne().toString();
                                    d25.setFontSizeColonne(ddfd.getFontSizeColonne());
                         
                                }catch(Exception e){d25.setFontSizeColonne(10);}
                                
                           listBol.add(true);
                    //        modeParam.setSelect(true);
                            }catch(Exception e){e.printStackTrace();
                      //          modeParam.setSelect(false);
                            listBol.add(false);
                                d25.setOrdreColonne(x);
                                d25.setLongueurColonne(150);
                                d25.setOrderByInstruction(false);
                                d25.setNumeroDeOrderBy(0);
                                  //         if((this.afficheColonne(rs.getString(2)))&&((d25.getNomColonne().equalsIgnoreCase("")==false)))
                       
                            }
                            try{
                                List<String>ss=new ArrayList<String>();
                                List<Object>oo=new ArrayList<Object>();
                                ss.add("modeleSortiee");
                                ss.add("nomColonneFiltrage");
                                oo.add(this.current);
                                oo.add(nomColonne);
                                ModeleSortieFiltrage ddf2=this.ejbModeleSortieFiltrage.findByParameterSingleResultMultipleCreteria("Select d from ModeleSortieFiltrage d "
                                        + "where d.modeleSortiee=:modeleSortiee and d.nomColonneFiltrage=:nomColonneFiltrage and "
                                        + "d.appelePar='"+d25.getAppelePar()+"'",ss,oo);
                                 listFiltrage.add(true);
                        //       modeParam.setFiltre(true);
                        }catch(Exception ertu){
                            listFiltrage.add(false);
                          //  modeParam.setFiltre(false);
                            }
                                  //     if((this.afficheColonne(rs.getString(2)))&&((d25.getNomColonne().equalsIgnoreCase("")==false)))
                                       {this.detailsModele.add(d25);
                            System.out.println("x " +x+" "+d25.getNomColonne()+" ordre "+d25.getOrdreColonne());
                            //          modeParam.setModele(d25);
                     //   listParams.add(modeParam); 
                            x++;}
                        }
                    }

             }else{
                 if(modeleTiersCom){
                     rs = statement.executeQuery("SELECT column_name as nomColonne,data_type FROM `information_schema`.`COLUMNS` "
                        + "where Table_SCHEMA='"+nomBaseDeDonnes+"' and TABLE_NAME='tiers_com_unite_production' "
                        + "and column_key!='PRI' and column_key!='MUL' and column_name not like '%user%' and column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1)and column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1) order by (column_name) ASC");
                    if (rs != null) {
                        while (rs.next()) {
                            DetailModeleSortie d25=new DetailModeleSortie();
                       //       ModeleSortieParametrage modeParam=new ModeleSortieParametrage();
                            d25.setTableColonne("tiers_com_unite_production");
                            String nomColonne=rs.getString(1);
                            d25.setNomColonne(this.correspondreColonne(nomColonne));
                            d25.setOrdreColonne(x);
                            d25.setClePrimTable(" ");
                            d25.setLongueurColonne(150);
                            d25.setTypeColonne(rs.getString(2));
                            d25.setFontSizeColonne(10);
                            try{
                                DetailModeleSortie ddfd=this.ejbDetailModeleFacade.findByParameterSingleResult("Select d from DetailModeleSortie d "
                                        + "where d.modeleSortie=:modeleSortie and d.nomColonne='"+nomColonne+"' "
                                        + "and d.appelePar IS NULL","modeleSortie",this.current);
                           
                                d25.setOrdreColonne(ddfd.getOrdreColonne());
                                d25.setLongueurColonne(ddfd.getLongueurColonne());
                                d25.setOrderByInstruction(ddfd.getOrderByInstruction());
                                d25.setNumeroDeOrderBy(ddfd.getNumeroDeOrderBy());
                                d25.setAppelePar(ddfd.getAppelePar());
                                d25.setTableAppelePar(ddfd.getTableAppelePar());
                                d25.setIdDetailModeleSortie(ddfd.getIdDetailModeleSortie());
                                d25.setTypeColonne(ddfd.getTypeColonne());
                                try{
                                    ddfd.getFontSizeColonne().toString();
                                    d25.setFontSizeColonne(ddfd.getFontSizeColonne());
                                }catch(Exception e){d25.setFontSizeColonne(10);}
                            listBol.add(true);
                         //   modeParam.setSelect(true);
                        }catch(Exception ertu){
                   
                           //    modeParam.setSelect(false);
                            listBol.add(false);
                                d25.setOrdreColonne(x);
                                d25.setLongueurColonne(150);
                                d25.setOrderByInstruction(false);
                                d25.setNumeroDeOrderBy(0);
                          
                            }
                            try{
                                List<String>ss=new ArrayList<String>();
                                List<Object>oo=new ArrayList<Object>();
                                ss.add("modeleSortiee");
                                ss.add("nomColonneFiltrage");
                                oo.add(this.current);
                                oo.add(nomColonne);
                                ModeleSortieFiltrage ddf2=this.ejbModeleSortieFiltrage.findByParameterSingleResultMultipleCreteria("Select d from ModeleSortieFiltrage d "
                                        + "where d.modeleSortiee=:modeleSortiee and d.nomColonneFiltrage=:nomColonneFiltrage "
                                        + "and d.appelePar IS NULL",ss,oo);
                                listFiltrage.add(true);
                             //  modeParam.setFiltre(true);
                        }catch(Exception ertu){
                            listFiltrage.add(false);
                            //modeParam.setFiltre(false);
                        
                            }
                             if(((d25.getNomColonne().equalsIgnoreCase("")==false)))
                             {   this.detailsModele.add(d25);
                                 System.out.println("x " +x+" "+d25.getNomColonne()+" ordre "+d25.getOrdreColonne());
                             // modeParam.setModele(d25);
                     //   listParams.add(modeParam);
                                 x++;}
                        }
                    }
                    rs = statement.executeQuery("SELECT c.table_name,c.column_name,t.table_name,t.column_name,t.referenced_column_name,c.data_type "
                         + "FROM `information_schema`.`COLUMNS` c join (SELECT * FROM `information_schema`.`KEY_COLUMN_USAGE` "
                         + "where table_name='tiers_com_unite_production' and constraint_name!='PRIMARY' and table_schema='"+nomBaseDeDonnes+"' and referenced_table_name !='user') t "
                         + "on t.referenced_table_name=c.table_name where c.table_schema='"+nomBaseDeDonnes+"' and column_key!='PRI' and column_key!='MUL' "
                         + "and c.column_name not like '%user%' and c.column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='occurence' and dtb.affichable=1) and c.table_name!='tiers_com'");
                     if (rs != null) {
                        while (rs.next()) {
                            DetailModeleSortie d25=new DetailModeleSortie();
                       //       ModeleSortieParametrage modeParam=new ModeleSortieParametrage();
                            d25.setTableColonne(rs.getString(1));
                            String nomColonne=rs.getString(2);
                            d25.setNomColonne(this.correspondreColonne(nomColonne));
                            d25.setTableAppelePar(rs.getString(3));
                            d25.setAppelePar(rs.getString(4));
                            d25.setClePrimTable(rs.getString(5));
                            d25.setTypeColonne(rs.getString(6));
                            d25.setOrdreColonne(x);
                            d25.setLongueurColonne(150);
                            d25.setFontSizeColonne(10);
                            try{
                                DetailModeleSortie ddfd=this.ejbDetailModeleFacade.findByParameterSingleResult("Select d from DetailModeleSortie d "
                                        + "where d.modeleSortie=:modeleSortie and d.nomColonne='"+nomColonne+"' and "
                                        + "d.appelePar='"+d25.getAppelePar()+"'","modeleSortie",this.current);
                              
                                d25.setOrdreColonne(ddfd.getOrdreColonne());
                                d25.setLongueurColonne(ddfd.getLongueurColonne());
                                d25.setOrderByInstruction(ddfd.getOrderByInstruction());
                                d25.setNumeroDeOrderBy(ddfd.getNumeroDeOrderBy());
                                d25.setAppelePar(ddfd.getAppelePar());
                                d25.setTableAppelePar(ddfd.getTableAppelePar());
                                d25.setIdDetailModeleSortie(ddfd.getIdDetailModeleSortie());
                                d25.setTypeColonne(ddfd.getTypeColonne());
                                try{
                                    ddfd.getFontSizeColonne().toString();
                                    d25.setFontSizeColonne(ddfd.getFontSizeColonne());
                                }catch(Exception e){d25.setFontSizeColonne(10);}
                              listBol.add(true);
                         //   modeParam.setSelect(true);
                        }catch(Exception ertu){
                   
                           //    modeParam.setSelect(false);
                            listBol.add(false);
                        
                                d25.setOrdreColonne(x);
                                d25.setLongueurColonne(150);
                                d25.setOrderByInstruction(false);
                                d25.setNumeroDeOrderBy(0);
                               //  if((this.afficheColonne(rs.getString(2)))&&((d25.getNomColonne().equalsIgnoreCase("")==false)))
                         
                            }
                            try{
                                List<String>ss=new ArrayList<String>();
                                List<Object>oo=new ArrayList<Object>();
                                ss.add("modeleSortiee");
                                ss.add("nomColonneFiltrage");
                                oo.add(this.current);
                                oo.add(nomColonne);
                                ModeleSortieFiltrage ddf2=this.ejbModeleSortieFiltrage.findByParameterSingleResultMultipleCreteria("Select d from ModeleSortieFiltrage d "
                                        + "where d.modeleSortiee=:modeleSortiee and d.nomColonneFiltrage=:nomColonneFiltrage "
                                        + "and d.appelePar='"+d25.getAppelePar()+"'",ss,oo);
                               listFiltrage.add(true);
                     //          modeParam.setFiltre(true);
                        }catch(Exception ertu){
                            listFiltrage.add(false);
                     //       modeParam.setFiltre(false);
                        }
                         //  if((this.afficheColonne(rs.getString(2)))&&((d25.getNomColonne().equalsIgnoreCase("")==false)))
                           {  this.detailsModele.add(d25);
                       //      modeParam.setModele(d25);
                       // listParams.add(modeParam);
                               System.out.println("x " +x+" "+d25.getNomColonne()+" ordre "+d25.getOrdreColonne());
                            x++;}
                        }
                    }

                 }
             }
               
          //   Collections.sort(listParams, new CustomComparatorModeleParametrage());
                           int i=1;
            //            Iterator ii=listParams.iterator();
             detailsModele=new ArrayList<DetailModeleSortie>();
            listBol=new ArrayList<Boolean>();
            listFiltrage=new ArrayList<Boolean>();
     /*   while(ii.hasNext()){
            ModeleSortieParametrage e=(ModeleSortieParametrage)ii.next();
            //
            if (e.getModele().getNomColonne().equalsIgnoreCase("")==false)
            {
                if (e.isSelect())
            {detailsModele.add(e.getModele());
            listBol.add((e.isSelect()));
            listFiltrage.add(e.isFiltre());
            
            }
            else
            {       DetailModeleSortie detmodele = e.getModele();
            detmodele.setOrdreColonne(i);
            detailsModele.add(detmodele);
            listBol.add((e.isSelect()));
            listFiltrage.add(e.isFiltre()); 
            }
            i++;
            }
        } */						
             rs.close();
             statement.close();
             connection.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();
        map.put("boutonAjouter",boutonAjouter);
        map.put("boutonModifier",boutonModifier);
        map.put("boutonSupprimer",boutonSupprimer);
        map.put("boutonEtat",boutonEtat);
        map.put("current",current);
        map.put("nomPage",nomPage);
        map.put("dataTableDetails",dataTableDetails);
        map.put("tableChoisi",tableChoisi);
        map.put("modeleTiersCom",modeleTiersCom);
        map.put("modeleProduitCom",modeleProduitCom);
        map.put("depotsSelectionnees",depotsSelectionnees);
        map.put("detailsModele",detailsModele);  
        map.put("listBol",listBol);
        map.put("listFiltrage",listFiltrage);
        map.put("sizeDepotsSelectionnes",sizeDepotsSelectionnes);
        return this.prepareEdit();
    }
     public String update() {
        try {
            utx.begin();
            this.ejbDetailModeleFacade.executerRemoveInstruction("Delete from DetailModeleSortie d where d.modeleSortie.idModeleSortie="+this.current.getIdModeleSortie(),new ArrayList<String>(),new ArrayList<Object>());
            this.ejbModeleSortieFiltrage.executerRemoveInstruction("Delete from ModeleSortieFiltrage d where d.modeleSortiee.idModeleSortie="+this.current.getIdModeleSortie(),new ArrayList<String>(),new ArrayList<Object>());
            getFacade().edit(current);
            Iterator i=this.detailsModele.iterator();
            Integer inx=new Integer(0);
            while(i.hasNext()){
                DetailModeleSortie d=(DetailModeleSortie)i.next();
                d.setNomColonne(this.correspondreColonneBackward(d.getNomColonne()));
                if(this.listBol.get(inx)){
                    DetailModeleSortie ddd=new DetailModeleSortie();
          //          ddd.setIdDetailModeleSortie((Integer)this.clePrimaire.numInterne(this.authentificationBean.baseDonneesChoisi+".DetailModeleSortie"));
                  ddd.setIdDetailModeleSortie(new Integer(1));

                    ddd.setClePrimTable(d.getClePrimTable());
                    ddd.setLongueurColonne(d.getLongueurColonne());
                    ddd.setModeleSortie(current);
                    ddd.setNomColonne(d.getNomColonne());
                    ddd.setOrdreColonne(d.getOrdreColonne());
                    ddd.setTableColonne(d.getTableColonne());
                    ddd.setOrderByInstruction(d.getOrderByInstruction());
                    ddd.setNumeroDeOrderBy(d.getNumeroDeOrderBy());
                    ddd.setFontSizeColonne(d.getFontSizeColonne());
                    ddd.setAppelePar(d.getAppelePar());
                    ddd.setTableAppelePar(d.getTableAppelePar());
                    ddd.setTypeColonne(d.getTypeColonne());
                    ddd.setSommable(d.getSommable());
                    this.ejbDetailModeleFacade.create(ddd);
                }
                if(this.listFiltrage.get(inx)){
                    ModeleSortieFiltrage msf=new ModeleSortieFiltrage();
       //             msf.setIdModeleSortieFiltrage((Integer)this.clePrimaire.numInterne(this.authentificationBean.baseDonneesChoisi+".ModeleSortieFiltrage"));
      msf.setIdModeleSortieFiltrage(new Integer(1));

                    msf.setModeleSortiee(this.current);
                    msf.setNomColonneFiltrage(d.getNomColonne());
                    msf.setTypeColonneFiltrage(d.getTypeColonne());
                    msf.setClePrimTableColonne(d.getClePrimTable());
                    msf.setNomTableColonne(d.getTableColonne());
                    msf.setAppelePar(d.getAppelePar());
                    msf.setTableAppeleApr(d.getTableAppelePar());
                    this.ejbModeleSortieFiltrage.create(msf);
                }
                inx++;
            }
            if(this.modeleProduitCom){
                Iterator itChaineDepot=this.depotsSelectionnees.iterator();
                String whereDelete="";
                String queryDelete="Delete from ProduitDepotModeleSortie p where p.modelesortie=:modelesortie and ";
                Integer compteur=new Integer(0);
                List<String>compSs=new ArrayList<String>();
                List<Object>compOo=new ArrayList<Object>();
                compSs.add("modelesortie");
                compOo.add(current); 
                while(itChaineDepot.hasNext()){
                    String sDepot=(String)itChaineDepot.next();
                    Depot d=this.ejbDepotCom.findByParameterSingleResult("Select d from DepotCom d where d.nomDepot=:nomDepot","nomDepot",sDepot);
                    try{
                        List<String>sc=new ArrayList<String>();
                        List<Object>co=new ArrayList<Object>();
                        sc.add("modelesortie");
                        sc.add("depotCom");
                        co.add(current);
                        co.add(d);
                      
                      
                        whereDelete=whereDelete+"(p.produitDepotModeleSortiePK!=:produitDepotModeleSortiePK"+compteur+") and ";
                    }catch(Exception fhrh){
                     
                       }
                    compteur++;
                }
                if(whereDelete.endsWith(" and ")){
                    whereDelete=whereDelete.substring(0,whereDelete.lastIndexOf(" and "));
                }
                queryDelete=queryDelete+whereDelete;
                if(queryDelete.endsWith(" and ")){
                    queryDelete=queryDelete.substring(0,queryDelete.lastIndexOf(" and "));
                }
                if(queryDelete.endsWith(" where ")){
                    queryDelete=queryDelete.substring(0,queryDelete.lastIndexOf(" where "));
                }
                //this.ejbProduitDepotModeleSortie.executerRemoveInstruction(queryDelete,compSs,compOo);
            }
            utx.commit();
          JsfUtil.addSuccessMessage("Transaction reussie");
            return prepareList();
        } catch (Exception e) {e.printStackTrace();
            JsfUtil.addErrorMessage("Transaction echouee");
            try{
                utx.rollback();
            }catch(Exception r){}
            return null;
        }
    }
    
    public Integer maxNIModeleSortie(){
    //    return (Integer)this.clePrimaire.numInterne(this.authentificationBean.baseDonneesChoisi+".Modelesortie");
    return Integer.valueOf(1);
    }
    
    protected TypeOpCom typeModele;
    
    public String prepareCreateJournaux(){
        this.current=new Modelesortie();
        this.detailsModele=new ArrayList<DetailModeleSortie>();
        this.listSommable=new ArrayList<Boolean>();
        this.dataTableDetails=new DataTable();
        this.listBol=new ArrayList<Boolean>();
        this.listFiltrage=new ArrayList<Boolean>();
        this.current.setCodeModeleSortie("operation_com");
        this.tableChoisi="operation_com";
        nomModeleChoisi="";
        this.affichereglement=false;
        nomPage="journaux";
        try{
            Driver monDriver = new com.mysql.jdbc.Driver();
            StringTokenizer getUrl=new StringTokenizer(this.getFacade().urlCourante(),"**");
            String url =getUrl.nextToken();
            String login=getUrl.nextToken();
            String password =getUrl.nextToken();
            String nomBaseDeDonnes=url.substring(url.lastIndexOf("/")+1);
            DriverManager.registerDriver(monDriver);
            Connection connection = DriverManager.getConnection(url,login, password);
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT column_name as nomColonne,COLUMNS.table_name as tableColonne,"
                    + "(SELECT KEY_COLUMN_USAGE.column_name FROM `information_schema`.`KEY_COLUMN_USAGE` where table_schema='"+nomBaseDeDonnes+"' "
                    + "and table_name='"+this.tableChoisi+"' and constraint_name='PRIMARY') as nom_table,DATA_TYPE as typeColonne"
                    + ",if(data_type!='varchar' and data_type!='date' and data_type!='datetime' and data_type!='longtext',true,false) as sommable  "
                    + "FROM `information_schema`.`COLUMNS` where Table_SCHEMA='"+nomBaseDeDonnes+"' and TABLE_NAME='"+this.tableChoisi+"' "
                    + "and column_key!='PRI' and column_key!='MUL' and column_name not like '%flag%' and column_name!='facture' and column_name!='facturemajore' and column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='journaux' and dtb.affichable=1)"
                    + " order by (column_name) ASC");
            this.detailsModele=new ArrayList<DetailModeleSortie>();
            listBol=new ArrayList<Boolean>();
            listFiltrage=new ArrayList<Boolean>();
            Integer x=new Integer(1);
            if (rs != null) {
                    while (rs.next()) {
                        DetailModeleSortie d25=new DetailModeleSortie();
                        d25.setNomColonne(this.correspondreColonne(rs.getString(1)));
                        d25.setTableColonne(rs.getString(2));
                        d25.setClePrimTable(rs.getString(3));
                        d25.setOrdreColonne(x);
                        d25.setLongueurColonne(150);
                        d25.setFontSizeColonne(10);
                        d25.setSommable(false);
                        d25.setTypeColonne(rs.getString(4));
                        listSommable.add(rs.getBoolean(5));
                        this.detailsModele.add(d25);
                        listFiltrage.add(false);
                        listBol.add(false);
                        x++;
                    }
             }
            rs = statement.executeQuery("SELECT c.table_name,c.column_name,t.table_name,t.column_name,t.referenced_column_name,c.data_type"
                    + ",if(c.data_type!='varchar' and c.data_type!='date' and c.data_type!='datetime' and c.data_type!='longtext',true,false) as sommable "
                    + "FROM `information_schema`.`COLUMNS` c join (SELECT * FROM `information_schema`.`KEY_COLUMN_USAGE` "
                    + "where table_name='"+this.tableChoisi+"' and constraint_name!='PRIMARY' and table_schema='"+nomBaseDeDonnes+"') t "
                    + "on t.referenced_table_name=c.table_name where c.table_schema='"+nomBaseDeDonnes+"' and column_key!='PRI' and column_key!='MUL' "
                    + "and if(c.table_name='user',if(c.column_name='login',true,false),true) and c.table_name!='type_op_com' "
                    + "and c.column_name not like '%flag%' and c.column_name!='facture' and c.column_name!='facturemajore' and c.column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='journaux' and dtb.affichable=1)"
                    + "order by t.table_name,t.column_name,c.column_name");
            if (rs != null) {
                while (rs.next()) {
                    DetailModeleSortie d25=new DetailModeleSortie();
                    d25.setTableColonne(rs.getString(1));
                    d25.setNomColonne(this.correspondreColonne(rs.getString(2)));
                    d25.setTableAppelePar(rs.getString(3));
                    d25.setAppelePar(rs.getString(4));
                    d25.setClePrimTable(rs.getString(5));
                    d25.setTypeColonne(rs.getString(6));
                    d25.setOrdreColonne(x);
                    d25.setLongueurColonne(150);
                    d25.setFontSizeColonne(10);
              d25.setTypeColonne(rs.getString(6));
                     
                    d25.setSommable(false);
//                       if((this.afficheColonne(rs.getString(2))))
//                        {
                    listSommable.add(rs.getBoolean(7));
                    this.detailsModele.add(d25);
                    listFiltrage.add(false);
                    listBol.add(false);
                    x++;
//                        }
                }
            }
             rs.close();
             statement.close();
             connection.close();
        }catch(Exception e){
            return "";
        }
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();
        map.put("boutonAjouter",boutonAjouter);
        map.put("boutonModifier",boutonModifier);
        map.put("affichereglement",this.affichereglement);
        map.put("boutonSupprimer",boutonSupprimer);
        map.put("boutonEtat",boutonEtat);
        map.put("current",current);
        map.put("detailsModele",detailsModele);
        map.put("listSommable",listSommable);
        map.put("dataTableDetails",dataTableDetails);
        map.put("listBol",listBol);
        map.put("listFiltrage",listFiltrage);
        map.put("tableChoisi",tableChoisi);
        map.put("nomModeleChoisi",nomModeleChoisi);
        map.put("nomPage",nomPage);
        return "modele_sortie_journaux_create";
    }
    
    private List<Boolean>listSommable;
    
    public Boolean getSommable(){
        try{
        return this.listSommable.get(this.dataTableDetails.getRowIndex()); 
        }catch(Exception e){}
        return false;
    }
    
    public void setSommable(Boolean b){
        try{
        this.listSommable.set(this.dataTableDetails.getRowIndex(),b); 
        }catch(Exception e){}
    }
    
    private String nomModeleChoisi;

    public String getNomModeleChoisi() {
        return nomModeleChoisi;
    }

    public void setNomModeleChoisi(String nomModeleChoisi) {
        this.nomModeleChoisi = nomModeleChoisi;
    }
    
    public SelectItem[] getItemsAvailableSelectOneNomTablesJournaux() {
        SelectItem[] items12 = new SelectItem[19];
        items12[0] = new SelectItem("---","");
        items12[1] = new SelectItem("facture","facture client");
        items12[2] = new SelectItem("facture fournisseur","facture fournisseur");
        items12[3] = new SelectItem("devis","devis client");
        items12[4] = new SelectItem("devis fournisseur","devis fournisseur");
        items12[5] = new SelectItem("livraison","livraison client");
        items12[6] = new SelectItem("livraison fournisseur","livraison fournisseur");
        items12[7] = new SelectItem("commande","commande client");
        items12[8] = new SelectItem("commande fournisseur","commande fournisseur");
        items12[9] = new SelectItem("Facture Non Reglee Client","facture non reglee client");
        items12[10] = new SelectItem("Facture Non Reglee Fournisseur","facture non reglee fournisseur");
        items12[11] = new SelectItem("Rapprochement Facture Client","rapprochement facture client");
        items12[12] = new SelectItem("Rapprochement Facture Frns","rapprochement facture fournisseur");
        items12[13] = new SelectItem("Rapprochement Reglement Client","rapprochement reglement client");
        items12[14] = new SelectItem("Rapprochement Reglement Fr","rapprochement reglement fournisseur");
        items12[15] = new SelectItem("Bl Non Facturee Client","B.L non facturee client");
        items12[16] = new SelectItem("Bl Non Facturee Fournisseur","B.L non facturee fournisseur");
        items12[17] = new SelectItem("Bl Factures Client","B.L factures client");
        items12[18] = new SelectItem("Bl Factures Fournisseur","B.L factures fournisseur");
        return items12;
    }
    public void handleChangeEventModeleTypeChoisi(){
        this.current.setCodeModeleSortie(this.nomModeleChoisi);
    }
    public Long maxNITypeOp(){
   //     return (Long)this.clePrimaire.numInterne(this.authentificationBean.baseDonneesChoisi+".TypeOpCom");
    return Long.valueOf(1);
    }
    public String prepareEditJournaux(){
        nomPage="journaux";
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();
        map.put("boutonAjouter",boutonAjouter);
        map.put("boutonModifier",boutonModifier);
        map.put("boutonSupprimer",boutonSupprimer);
        map.put("boutonEtat",boutonEtat);
        map.put("current",current);
        map.put("detailsModele",detailsModele);
        map.put("listSommable",listSommable);
        map.put("dataTableDetails",dataTableDetails);
        map.put("listBol",listBol);
        map.put("tableChoisi",tableChoisi);
        map.put("nomPage",nomPage);
        map.put("listFiltrage",listFiltrage);
        return "modele_sortie_journaux_edit";
    }
    public String prepareListJournaux(){
        nomPage="journaux";
        this.recreateModel();
        FacesContext fc=FacesContext.getCurrentInstance();
        Map<String,Object>map=fc.getExternalContext().getRequestMap();
        map.put("boutonAjouter",boutonAjouter);
        map.put("boutonModifier",boutonModifier);
        map.put("boutonSupprimer",boutonSupprimer);
        map.put("boutonEtat",boutonEtat);
        map.put("nomPage",nomPage);
        map.put("items",this.items);
        return "modele_sortie_journaux_list";
    }
    
    private String nomPage;
    private TypeOpCom typeModeleChoisi;

    public TypeOpCom getTypeModeleChoisi() {
        return typeModeleChoisi;
    }

    public void setTypeModeleChoisi(TypeOpCom typeModeleChoisi) {
        this.typeModeleChoisi = typeModeleChoisi;
    }
    public String createJournaux() {
        try {
            utx.begin();
            TypeOpCom t = ejbTypeOperation.findByParameterSingleResult("Select t from TypeOpCom t where t.nomTop=:nomTop","nomTop",this.nomModeleChoisi);
            this.current.setTypeModele(t);
        //    this.current.setIdModeleSortie((Integer)this.clePrimaire.numInterne(this.authentificationBean.baseDonneesChoisi+".Modelesortie"));
                      this.current.setIdModeleSortie(new Integer(1));

            getFacade().create(current);
            Iterator i=this.detailsModele.iterator();
            Integer inx=new Integer(0);
            while(i.hasNext()){
                DetailModeleSortie dd=(DetailModeleSortie)i.next();
                dd.setNomColonne(this.correspondreColonneBackward(dd.getNomColonne()));
                if(this.listBol.get(inx)){
                    dd.setModeleSortie(current);
            //        dd.setIdDetailModeleSortie((Integer)this.clePrimaire.numInterne(this.authentificationBean.baseDonneesChoisi+".DetailModeleSortie"));
        dd.setIdDetailModeleSortie(new Integer(1));

                    if((this.listSommable.get(inx)==true)&&(dd.getSommable()==true)){
                            dd.setSommable(true);
                        }else{
                            dd.setSommable(false);
                        }
                    this.ejbDetailModeleFacade.create(dd);
                }
                if(this.listFiltrage.get(inx)){
                    ModeleSortieFiltrage msf=new ModeleSortieFiltrage();
                //    msf.setIdModeleSortieFiltrage((Integer)this.clePrimaire.numInterne(this.authentificationBean.baseDonneesChoisi+".ModeleSortieFiltrage"));
          msf.setIdModeleSortieFiltrage(new Integer(1));

                    msf.setModeleSortiee(this.current);
                    msf.setNomColonneFiltrage(dd.getNomColonne());
                    msf.setTypeColonneFiltrage(dd.getTypeColonne());
                    msf.setClePrimTableColonne(dd.getClePrimTable());
                    msf.setNomTableColonne(dd.getTableColonne());
                    msf.setTableAppeleApr(dd.getTableAppelePar());
                    msf.setAppelePar(dd.getAppelePar());
                    this.ejbModeleSortieFiltrage.create(msf);
                }
                inx++;
            }
            this.utx.commit();
             JsfUtil.addSuccessMessage("Transaction reussie");
            prepareCreateJournaux();
        } catch (Exception e) {e.printStackTrace();
            JsfUtil.addErrorMessage("Transaction echouee");
            try{
                utx.rollback();
            }catch(Exception rz){}
        }
        return null;
    }
    public Boolean client(){
        try{
            Modelesortie m=(Modelesortie)this.items.getRowData();
            if(m.getTypeModele().getNomTop().toLowerCase().contains("fournisseur")){
                return false;
            }
            return true;
        }catch(Exception rere){
            return false;
        }
    }
    public String supprimerModeleJournaux(){
        try{
            utx.begin();
            Modelesortie m=(Modelesortie) this.items.getRowData();
            this.ejbDetailModeleFacade.executerRemoveInstruction("delete from DetailModeleSortie d where d.modeleSortie.idModeleSortie="+m.getIdModeleSortie(),new ArrayList<String>(),new ArrayList<Object>());
            this.ejbModeleSortieFiltrage.executerRemoveInstruction("delete from ModeleSortieFiltrage d where d.modeleSortiee.idModeleSortie="+m.getIdModeleSortie(),new ArrayList<String>(),new ArrayList<Object>());
            this.getFacade().remove(m);
            utx.commit();
             JsfUtil.addSuccessMessage("Transaction reussie");
            this.prepareListJournaux();
        }catch(Exception er){
            er.printStackTrace();
            try{
                utx.rollback();
            }catch(Exception r){}
            JsfUtil.addErrorMessage("Transaction echouee");
        }
        return null;
    }

   public String modifierSetupJournaux(){
        this.current=(Modelesortie)this.items.getRowData();
        this.detailsModele=new ArrayList<DetailModeleSortie>();
        this.listSommable=new ArrayList<Boolean>();
        this.dataTableDetails=new DataTable();
        this.listBol=new ArrayList<Boolean>();
        this.listFiltrage=new ArrayList<Boolean>();
        this.tableChoisi="operation_com";
        nomPage="journaux";
        try{
            Driver monDriver = new com.mysql.jdbc.Driver();
            StringTokenizer getUrl=new StringTokenizer(this.getFacade().urlCourante(),"**");
            String url =getUrl.nextToken();
            String login=getUrl.nextToken();
            String password =getUrl.nextToken();
            String nomBaseDeDonnes=url.substring(url.lastIndexOf("/")+1);
            DriverManager.registerDriver(monDriver);
            Connection connection = DriverManager.getConnection(url,login, password);
            Statement statement = connection.createStatement();
            
            String ordreModele=",ifnull((SELECT det.ordre_colonne FROM "+nomBaseDeDonnes+".detail_modele_sortie det where  modele_sortie="+current.getIdModeleSortie()+" and det.nom_Colonne=column_name and det.table_Colonne=table_name),100) as ordre";
            String q="SELECT column_name as nomColonne,COLUMNS.table_name as tableColonne,"
                    + "(SELECT KEY_COLUMN_USAGE.column_name FROM `information_schema`.`KEY_COLUMN_USAGE` where table_schema='"+nomBaseDeDonnes+"' "
                    + "and table_name='"+this.tableChoisi+"' and constraint_name='PRIMARY') as nom_table,DATA_TYPE as typeColonne"
            //    +ordreModele    
             + ",if(data_type!='varchar' and data_type!='date' and data_type!='datetime' and data_type!='longtext',true,false) as sommable  "
                    + "FROM `information_schema`.`COLUMNS` where Table_SCHEMA='"+nomBaseDeDonnes+"' and TABLE_NAME='"+this.tableChoisi+"' "
                    + "and column_key!='PRI' and column_key!='MUL' and column_name not like '%flag%' and column_name!='facture' and column_name!='facturemajore' and column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='journaux' and dtb.affichable=1)" 
                    + " order by (column_name) ASC";
            System.out.println(" requette 1 Journaux "+q);
            ResultSet rs = statement.executeQuery("SELECT column_name as nomColonne,COLUMNS.table_name as tableColonne,"
                    + "(SELECT KEY_COLUMN_USAGE.column_name FROM `information_schema`.`KEY_COLUMN_USAGE` where table_schema='"+nomBaseDeDonnes+"' "
                    + "and table_name='"+this.tableChoisi+"' and constraint_name='PRIMARY') as nom_table,DATA_TYPE as typeColonne"
                    //+ordreModele
                    + ",if(data_type!='varchar' and data_type!='date' and data_type!='datetime' and data_type!='longtext',true,false) as sommable  "
                    + "FROM `information_schema`.`COLUMNS` where Table_SCHEMA='"+nomBaseDeDonnes+"' and TABLE_NAME='"+this.tableChoisi+"' "
                    + "and column_key!='PRI' and column_key!='MUL' and column_name not like '%flag%' and column_name!='facture' and column_name!='facturemajore' and column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='journaux' and dtb.affichable=1)" 
                    + " order by (column_name) ASC ");
    
            this.detailsModele=new ArrayList<DetailModeleSortie>();
            listBol=new ArrayList<Boolean>();
            listFiltrage=new ArrayList<Boolean>();
      //      List <ModeleSortieParametrage> listParams=new ArrayList<ModeleSortieParametrage>();
            Integer x=new Integer(1);
            if (rs != null) {
                    while (rs.next()) {
                        String nomColonne=rs.getString(1);
                        DetailModeleSortie d25=new DetailModeleSortie();
        //                ModeleSortieParametrage modeParam=new ModeleSortieParametrage();
                        d25.setNomColonne(this.correspondreColonne(nomColonne));
                        d25.setTableColonne(rs.getString(2));
                        d25.setClePrimTable(rs.getString(3));
                        d25.setOrdreColonne(x);
                        d25.setLongueurColonne(150);
                        d25.setFontSizeColonne(10);
                        d25.setTypeColonne(rs.getString(4));
                        listSommable.add(rs.getBoolean(5));
                        try{
                            DetailModeleSortie ddit=this.ejbDetailModeleFacade.findByParameterSingleResult("Select d from DetailModeleSortie d "
                                    + "where d.modeleSortie.idModeleSortie="+current.getIdModeleSortie()+" and d.nomColonne=:nomColonne "
                                    + "and d.appelePar IS NULL","nomColonne",nomColonne);
                            d25.setSommable(ddit.getSommable());
                            d25.setLongueurColonne(ddit.getLongueurColonne());
                            d25.setFontSizeColonne(ddit.getFontSizeColonne());
                            d25.setOrderByInstruction(ddit.getOrderByInstruction());
                            d25.setNumeroDeOrderBy(ddit.getNumeroDeOrderBy());
                            d25.setOrdreColonne(ddit.getOrdreColonne());
                            listBol.add(true);
          //                  modeParam.setSelect(true);
                        }catch(Exception ertu){
                            d25.setSommable(false);
            //                   modeParam.setSelect(false);
                            listBol.add(false);
                        }
                        try{
                            ModeleSortieFiltrage msff=this.ejbModeleSortieFiltrage.findByParameterSingleResult("Select m from ModeleSortieFiltrage m "
                                    + "where m.nomColonneFiltrage=:nomColonneFiltrage and m.modeleSortiee.idModeleSortie="+current.getIdModeleSortie()+" "
                                    + "and m.appelePar IS NULL","nomColonneFiltrage",nomColonne);
                            listFiltrage.add(true);
              //                 modeParam.setFiltre(true);
                        }catch(Exception ertu){
                            listFiltrage.add(false);
                //            modeParam.setFiltre(false);
                        }
                        System.out.println("d25 nom "+d25.getNomColonne()+" avant "+nomColonne);
                  //      modeParam.setModele(d25);
                    //    listParams.add(modeParam);
                        this.detailsModele.add(d25);
                        x++;
                    }
             }
          
               rs = statement.executeQuery("SELECT c.table_name,c.column_name,t.table_name,t.column_name,t.referenced_column_name,c.data_type"
                    + ",if(c.data_type!='varchar' and c.data_type!='date' and c.data_type!='datetime' and c.data_type!='longtext',true,false) as sommable "
                    + "FROM `information_schema`.`COLUMNS` c join (SELECT * FROM `information_schema`.`KEY_COLUMN_USAGE` "
                    + "where table_name='"+this.tableChoisi+"' and constraint_name!='PRIMARY' and table_schema='"+nomBaseDeDonnes+"') t "
                    + "on t.referenced_table_name=c.table_name where c.table_schema='"+nomBaseDeDonnes+"' and column_key!='PRI' and column_key!='MUL' "
                    + "and if(c.table_name='user',if(c.column_name='login',true,false),true) and c.table_name!='type_op_com' "
                    + "and c.column_name not like '%flag%' and c.column_name!='facture' and c.column_name!='facturemajore' and c.column_name in (SELECT dtb.table_correspondance  FROM detail_modele_table_correspondance dtb inner join modele_table_correspondance  m on m.id = dtb.modele where m.libelle='journaux' and dtb.affichable=1)"
                    + "order by t.table_name,t.column_name,c.column_name");
            if (rs != null) {
                while (rs.next()) {
                    String nomColonne=rs.getString(2);
                    DetailModeleSortie d25=new DetailModeleSortie();
                      //                ModeleSortieParametrage modeParam=new ModeleSortieParametrage();
                    d25.setTableColonne(rs.getString(1));
                    d25.setNomColonne(this.correspondreColonne(nomColonne));
                    d25.setTableAppelePar(rs.getString(3));
                    d25.setAppelePar(rs.getString(4));
                    d25.setClePrimTable(rs.getString(5));
                    d25.setTypeColonne(rs.getString(6));
                    d25.setOrdreColonne(x);
                    d25.setLongueurColonne(150);
                    d25.setFontSizeColonne(10);
                    d25.setSommable(false);
                    listSommable.add(rs.getBoolean(7));
                    try{
                        DetailModeleSortie ddit=this.ejbDetailModeleFacade.findByParameterSingleResult("Select d from DetailModeleSortie d "
                                + "where d.modeleSortie.idModeleSortie="+current.getIdModeleSortie()+" and d.nomColonne=:nomColonne and "
                                + "d.appelePar='"+d25.getAppelePar()+"'","nomColonne",nomColonne);
                        d25.setSommable(ddit.getSommable());
                        d25.setLongueurColonne(ddit.getLongueurColonne());
                        d25.setFontSizeColonne(ddit.getFontSizeColonne());
                        d25.setOrderByInstruction(ddit.getOrderByInstruction());
                        d25.setNumeroDeOrderBy(ddit.getNumeroDeOrderBy());
                        d25.setOrdreColonne(ddit.getOrdreColonne());
                        listBol.add(true);
                        //    modeParam.setSelect(true);
                    }catch(Exception ertu){
                        d25.setSommable(false);
                          //  modeParam.setSelect(false);
                        listBol.add(false);
                    }
                    try{
                        ModeleSortieFiltrage msff=this.ejbModeleSortieFiltrage.findByParameterSingleResult("Select m from ModeleSortieFiltrage m where "
                                + "m.nomColonneFiltrage=:nomColonneFiltrage and m.modeleSortiee.idModeleSortie="+current.getIdModeleSortie()+" "
                                + "and m.appelePar='"+d25.getAppelePar()+"'","nomColonneFiltrage",nomColonne);
                        listFiltrage.add(true);
                            //modeParam.setFiltre(true);
                    }catch(Exception ertu){
                        listFiltrage.add(false);
                          // modeParam.setFiltre(false);
                    }
                                            System.out.println("d25 nom "+d25.getNomColonne()+" avant "+nomColonne);
                         //  modeParam.setModele(d25);
                       // listParams.add(modeParam);                 
                        this.detailsModele.add(d25);
                    x++;
                }
            }
           //*
             
            // Collections.sort(listParams, new CustomComparatorModeleParametrage());
                           int i=1;
              //          Iterator ii=listParams.iterator();
             this.detailsModele=new ArrayList<DetailModeleSortie>();
            listBol=new ArrayList<Boolean>();
            listFiltrage=new ArrayList<Boolean>();
//        while(ii.hasNext()){
//           // ModeleSortieParametrage e=(ModeleSortieParametrage)ii.next();
//            //
//            if (e.isSelect())
//            {detailsModele.add(e.getModele());
//            listBol.add((e.isSelect()));
//            listFiltrage.add(e.isFiltre());
//            
//            }
//            else
//            {       DetailModeleSortie detmodele = e.getModele();
//            detmodele.setOrdreColonne(i);
//            detailsModele.add(detmodele);
//            listBol.add((e.isSelect()));
//            listFiltrage.add(e.isFiltre()); 
//            }
//            i++;
//        } 
        
           // */
            rs.close();
            statement.close();
            connection.close();
        }catch(Exception e){e.printStackTrace();
            return "";
        }
        return this.prepareEditJournaux();
    }
        
        
    public String updateJournaux() {
        try {
            utx.begin();
            this.ejbDetailModeleFacade.executerRemoveInstruction("delete from DetailModeleSortie d where d.modeleSortie.idModeleSortie="+current.getIdModeleSortie(),new ArrayList<String>(),new ArrayList<Object>());
            this.ejbModeleSortieFiltrage.executerRemoveInstruction("delete from ModeleSortieFiltrage d where d.modeleSortiee.idModeleSortie="+current.getIdModeleSortie(),new ArrayList<String>(),new ArrayList<Object>());
            getFacade().edit(current);
            Iterator i=this.detailsModele.iterator();
            Integer inx=new Integer(0);
            while(i.hasNext()){
                DetailModeleSortie d=(DetailModeleSortie)i.next();
                String nomColonne=this.correspondreColonneBackward(d.getNomColonne());
                d.setNomColonne(nomColonne);
                if(this.listBol.get(inx)){
                    DetailModeleSortie ddd=new DetailModeleSortie();
           //         ddd.setIdDetailModeleSortie((Integer)this.clePrimaire.numInterne(this.authentificationBean.baseDonneesChoisi+".DetailModeleSortie"));
                    ddd.setIdDetailModeleSortie(new Integer(1));

                    ddd.setClePrimTable(d.getClePrimTable());
                    ddd.setLongueurColonne(d.getLongueurColonne());
                    ddd.setModeleSortie(current);
                    ddd.setNomColonne(d.getNomColonne());
                    ddd.setOrdreColonne(d.getOrdreColonne());
                    ddd.setTableColonne(d.getTableColonne());
                    ddd.setFontSizeColonne(d.getFontSizeColonne());
                    ddd.setTypeColonne(d.getTypeColonne());
                    ddd.setOrderByInstruction(d.getOrderByInstruction());
                    ddd.setNumeroDeOrderBy(d.getNumeroDeOrderBy());
                    ddd.setAppelePar(d.getAppelePar());
                    ddd.setTableAppelePar(d.getTableAppelePar());
                    if((d.getSommable()==true)){
                        ddd.setSommable(true);
                    }else{
                        ddd.setSommable(false);
                    }
                    this.ejbDetailModeleFacade.create(ddd);
                }
                if(this.listFiltrage.get(inx)){
                    ModeleSortieFiltrage msf=new ModeleSortieFiltrage();
              //      msf.setIdModeleSortieFiltrage((Integer)this.clePrimaire.numInterne(this.authentificationBean.baseDonneesChoisi+".ModeleSortieFiltrage"));
                      msf.setIdModeleSortieFiltrage(new Integer(1));

                    msf.setModeleSortiee(this.current);
                    msf.setNomColonneFiltrage(d.getNomColonne());
                    msf.setTypeColonneFiltrage(d.getTypeColonne());
                    msf.setClePrimTableColonne(d.getClePrimTable());
                    msf.setNomTableColonne(d.getTableColonne());
                    msf.setAppelePar(d.getAppelePar());
                    msf.setTableAppeleApr(d.getTableAppelePar());
                    this.ejbModeleSortieFiltrage.create(msf);
                }
                inx++;
            }
            utx.commit();
            JsfUtil.addSuccessMessage("Transaction reussie");
            return prepareListJournaux();
        } catch (Exception e) {e.printStackTrace();
            JsfUtil.addErrorMessage("Transaction echouee");
            try{
                utx.rollback();
            }catch(Exception r){}
            return null;
        }
    }
    public Boolean existeDansListe(DetailModeleSortie d254,List<String> ldbNPDel){
        Iterator i=ldbNPDel.iterator();
        while(i.hasNext()){
            String ss=(String)i.next();
            if(ss.equals(d254.getNomColonne())){
                return true;
            }
        }
        return false;
    }
    
    private String pageActuelle;
    private Date date1;
    private Date date2;
    private Modelesortie modeleChoisi;

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public Modelesortie getModeleChoisi() {
        return modeleChoisi;
    }

    public void setModeleChoisi(Modelesortie modeleChoisi) {
        this.modeleChoisi = modeleChoisi;
    }

    public String getNomPage() {
        return nomPage;
    }

    public void setNomPage(String nomPage) {
        this.nomPage = nomPage;
    }
    private List<String>detailsModeleValeurFiltrage;
    
    public void initJournaux(){
        date1=new Date();
        date2=new Date();
        modeleChoisi=new Modelesortie();
        dataTableDetails=new DataTable();
        this.seuilPieceSoldee=new BigDecimal("0.000").setScale(3, RoundingMode.FLOOR);
        this.reglement=false;
        sizeReglement=new Integer(50);
        this.emailReceiver="";
        this.emailSender="";
        this.passwordEmailSender="";
        this.detailsModele=new ArrayList<DetailModeleSortie>();
    }

    public String getPageActuelle() {
        return pageActuelle;
    }

    public void setPageActuelle(String pageActuelle) {
        this.pageActuelle = pageActuelle;
    }
    
  
    public String getValFiltrage(){
        try{
        return this.detailsModeleValeurFiltrage.get(this.dataTableDetails.getRowIndex()); 
        }catch(Exception e){}
        return "";
    }
    
    public void setValFiltrage(String b){
        try{
        this.detailsModeleValeurFiltrage.set(this.dataTableDetails.getRowIndex(),b); 
        }catch(Exception e){}
    }
    
    protected TypeOpCom typeOperation;
    public SelectItem[] getItemsAvailableSelectOneNomTablesJournal() {
        return JsfUtil.getSelectItems(this.getFacade().findByParameter("Select m from Modelesortie m where m.typeModele=:typeModele","typeModele",typeOperation), true);
    }
    
    public String requetteSqlRapport(){
        try{
            StringTokenizer getUrl=new StringTokenizer(this.getFacade().urlCourante(),"**");
            String url =getUrl.nextToken();
            String login=getUrl.nextToken();
            String password =getUrl.nextToken();
            String nomBaseDeDonnes=url.substring(url.lastIndexOf("/")+1);
            List<DetailModeleSortie>dets=this.ejbDetailModeleFacade.findByParameter("Select d from DetailModeleSortie d where d.modeleSortie.idModeleSortie=:idModeleSortie","idModeleSortie",modeleChoisi.getIdModeleSortie());
            Iterator i =dets.iterator();
            String requetteSqlSelect="Select operation_com.facture,operation_com.MONTANT_TTC_OP AS ttcOperation,";
            String requetteSqlFrom="from "+nomBaseDeDonnes+".operation_com operation_com ";
            if(this.modeleChoisi.getReglementRapport() || this.modeleChoisi.getSolde()){
                requetteSqlSelect=requetteSqlSelect+"sum(MONTANT_REG),";
                requetteSqlFrom=requetteSqlFrom+" LEFT OUTER JOIN "+nomBaseDeDonnes+".lettrage_com lettrage_com ON "+nomBaseDeDonnes+".operation_com.NI_OP="+nomBaseDeDonnes+".lettrage_com.NI_PIECE ";
            }
            Connection connection = DriverManager.getConnection(url,login, password);
            Statement statement = connection.createStatement();
            while(i.hasNext()){
                DetailModeleSortie dde=(DetailModeleSortie)i.next();
                requetteSqlSelect=requetteSqlSelect+dde.getTableColonne()+"."+dde.getNomColonne()+",";
                if(requetteSqlFrom.contains(dde.getTableColonne())==false){
                    ResultSet rs = statement.executeQuery("SELECT COLUMN_NAME FROM information_schema.KEY_COLUMN_USAGE WHERE REFERENCED_TABLE_NAME='"+dde.getTableColonne()+"' and CONSTRAINT_SCHEMA='"+nomBaseDeDonnes+"' and CONSTRAINT_NAME!='PRIMARY' and TABLE_NAME='operation_com'");
                    String cleFkOperationCom="";
                    if (rs != null) {
                            if(rs.next()) {
                                cleFkOperationCom=rs.getString(1);
                            }
                        }
                rs.close();
                requetteSqlFrom=requetteSqlFrom+" LEFT OUTER JOIN "+nomBaseDeDonnes+"."+dde.getTableColonne()+" "+dde.getTableColonne().toLowerCase()+" ON "+nomBaseDeDonnes+".operation_com."+cleFkOperationCom+"="+nomBaseDeDonnes+"."+dde.getTableColonne().toLowerCase()+"."+dde.getClePrimTable()+" ";
                }
            }
            if(requetteSqlSelect.endsWith(",")){
                requetteSqlSelect=requetteSqlSelect.substring(0,requetteSqlSelect.length()-1);
            }
            String requetteSqlWhere="where NI_Top="+typeOperation.getNiTop().toString()+" and ";
            if((date1 != null) && (date2 != null)){
                this.date1.setHours(0);
                this.date1.setMinutes(0);
                this.date1.setSeconds(0);
                this.date2.setHours(23);
                this.date2.setMinutes(59);
                this.date2.setSeconds(59);
                if(date1.before(date2) || date1.equals(date2)){
                    DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String d1=dateFormat1.format(date1);
                    String d2=dateFormat1.format(date2);
                    requetteSqlWhere=requetteSqlWhere+"date_sys<='"+d2+"' and date_sys>='"+d1+"' and ";
                }
            }
            Iterator it2=this.detailsModele.iterator();
            Integer x=new Integer(0);
            while(it2.hasNext()){
                DetailModeleSortie detModSort=(DetailModeleSortie)it2.next();
                if(this.detailsModeleValeurFiltrage.get(x)!=null){
                    if(this.detailsModeleValeurFiltrage.get(x).isEmpty()==false){
                        if((detModSort.getTypeColonne().equals("date")==false) & (detModSort.getTypeColonne().equals("datetime")==false)){
                            requetteSqlWhere=requetteSqlWhere+detModSort.getTableColonne()+"."+detModSort.getNomColonne()+" like '%"+this.detailsModeleValeurFiltrage.get(x)+"%' and ";
                        }else{
                            try{
                            StringTokenizer sttsuak=new StringTokenizer(this.detailsModeleValeurFiltrage.get(x),"-/");
                            if(sttsuak.countTokens()==3){
                                String f1=sttsuak.nextToken();
                                String f2=sttsuak.nextToken();
                                String f3=sttsuak.nextToken();
                                String dateRecherche="";
                                if(f1.length()==4){
                                    dateRecherche=f1+"-"+f2+"-"+f3;
                                }else{
                                    dateRecherche=f3+"-"+f2+"-"+f1;
                                }
                                requetteSqlWhere=requetteSqlWhere+detModSort.getTableColonne()+"."+detModSort.getNomColonne()+" like '%"+dateRecherche+"%' and ";
                                }else{
                                    requetteSqlWhere=requetteSqlWhere+detModSort.getTableColonne()+"."+detModSort.getNomColonne()+" like '%"+this.detailsModeleValeurFiltrage.get(x)+"%' and ";
                                }
                            }catch(Exception exce87){
                                exce87.printStackTrace();
                            }
                        }
                        if(requetteSqlFrom.contains(detModSort.getTableColonne())==false){
                            ResultSet rs = statement.executeQuery("SELECT COLUMN_NAME FROM information_schema.KEY_COLUMN_USAGE WHERE REFERENCED_TABLE_NAME='"+detModSort.getTableColonne()+"' and CONSTRAINT_SCHEMA='"+nomBaseDeDonnes+"' and CONSTRAINT_NAME!='PRIMARY' and TABLE_NAME='operation_com'");
                            String cleFkOperationCom="";
                            if (rs != null) {
                                    if(rs.next()) {
                                        cleFkOperationCom=rs.getString(1);
                                    }
                            }
                            rs.close();
                            requetteSqlFrom=requetteSqlFrom+" LEFT OUTER JOIN "+nomBaseDeDonnes+"."+detModSort.getTableColonne()+" "+detModSort.getTableColonne().toLowerCase()+" ON "+nomBaseDeDonnes+".operation_com."+cleFkOperationCom+"="+nomBaseDeDonnes+"."+detModSort.getTableColonne()+"."+detModSort.getClePrimTable()+" ";
                        }
                    }
                }
                x++;
            }
            if(requetteSqlWhere.endsWith(" and ")){
                requetteSqlWhere=requetteSqlWhere.substring(0,requetteSqlWhere.lastIndexOf(" and "));
            }
            if(this.modeleChoisi.getReglementRapport()){
                if(this.modeleChoisi.getReglement()!=0){
                    requetteSqlWhere=requetteSqlWhere+" and (("+nomBaseDeDonnes+".lettrage_com.NI_OP in (Select operation_com2.NI_OP from "+nomBaseDeDonnes+".operation_com operation_com2 where "+nomBaseDeDonnes+".operation_com2.NI_MR="+this.modeleChoisi.getReglement()+")) or (ISNULL("+nomBaseDeDonnes+".lettrage_com.NI_OP))) group by("+nomBaseDeDonnes+".operation_com.NI_OP)";
                }else{
                    requetteSqlWhere=requetteSqlWhere+" group by("+nomBaseDeDonnes+".operation_com.NI_OP)";
                }
            }else{
                if(this.modeleChoisi.getSolde()){
                    requetteSqlWhere=requetteSqlWhere+" group by("+nomBaseDeDonnes+".operation_com.NI_OP)";
                }
            }
            try{
                List<DetailModeleSortie>detOrdre=this.ejbDetailModeleFacade.findByParameter("Select d from DetailModeleSortie d where d.modeleSortie.idModeleSortie=:idModeleSortie and d.orderByInstruction="+true+" order by (d.numeroDeOrderBy)","idModeleSortie",this.modeleChoisi.getIdModeleSortie());
                if(detOrdre.isEmpty()==false){
                    String orderByInstruction=" order by ";
                    if(detOrdre.size()==1){
                        orderByInstruction=orderByInstruction+"("+detOrdre.get(0).getTableColonne()+"."+detOrdre.get(0).getNomColonne()+")";
                    }else{
                        Iterator itDetOrdre=detOrdre.iterator();
                        while(itDetOrdre.hasNext()){
                            DetailModeleSortie detailOrdre=(DetailModeleSortie)itDetOrdre.next();
                            orderByInstruction=orderByInstruction+detailOrdre.getTableColonne()+"."+detailOrdre.getNomColonne()+",";
                        }
                    }
                    if(orderByInstruction.endsWith(",")){
                        orderByInstruction=orderByInstruction.substring(0,orderByInstruction.lastIndexOf(","));
                    }
                    requetteSqlWhere=requetteSqlWhere+orderByInstruction;
                }
            }catch(Exception e){e.printStackTrace();}
            statement.close();
            connection.close();
            System.out.println(requetteSqlSelect+" "+requetteSqlFrom+" "+requetteSqlWhere);
            return requetteSqlSelect+" "+requetteSqlFrom+" "+requetteSqlWhere;
        }catch(Exception e){e.printStackTrace();
        }
        return "";
    }
    private Integer sizeReglement;

    public Integer getSizeReglement() {
        return sizeReglement;
    }

    public void setSizeReglement(Integer sizeReglement) {
        this.sizeReglement = sizeReglement;
    }
    
    private Boolean reglement;

    public Boolean getReglement() {
        return reglement;
    }

    public void setReglement(Boolean reglement) {
        this.reglement = reglement;
    }
    
    public JasperPrint jasperPrintListItems(){
        Connection connection=null;
        Statement statement=null;
        ResultSet rs = null;
        try{
            String nomSoc="";
            String adresseSoc="";
            String telSoc="";
            String faxSoc="";
            StringTokenizer sttt=new StringTokenizer(this.pageActuelle.toUpperCase()," ");
            String part1=sttt.nextToken().trim();
            String part2=sttt.nextToken().trim();
            if(part1.charAt(part1.length()-1)!='S'){
                part1=part1+"S";
            }
            if(part2.charAt(part2.length()-1)!='S'){
                part2=part2+"S";
            }
//            String nomRapport="JOURNAL "+sttt.nextToken()+"S "+sttt.nextToken()+"S";
            String nomRapport="JOURNAL "+part1+" "+part2;
            Driver monDriver = new com.mysql.jdbc.Driver();
            StringTokenizer getUrl=new StringTokenizer(this.getFacade().urlCourante(),"**");
            String url =getUrl.nextToken();
            String nomBaseDeDonnes=url.substring(url.lastIndexOf("/")+1);
            String login=getUrl.nextToken();
            String password =getUrl.nextToken();
            DriverManager.registerDriver(monDriver);
            connection = DriverManager.getConnection(url,login, password);
            List<DetailModeleSortie>lMs=this.ejbDetailModeleFacade.findByParameter("Select d from DetailModeleSortie d where d.modeleSortie=:modeleSortie order by (d.ordreColonne) ASC","modeleSortie",this.modeleChoisi);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT LIBELLE_SOCIETE,ADRESSE,TEL,FAX FROM `"+nomBaseDeDonnes+"`.`horizon_sys` where id='"+1+"'");
            if (rs != null) {
                    while (rs.next()) {
                        nomSoc=rs.getString(1);
                        adresseSoc=rs.getString(2);
                        telSoc=rs.getString(3);
                        faxSoc=rs.getString(4);
                    }
               }
            rs.close();
            statement.close();
            JasperDesign jasperDesign=new JasperDesign();
            JRDesignQuery query = new JRDesignQuery();
            query.setText(this.requetteSqlRapport());
            String whereSqlClause="";
            String intro="Periode: ";
            try{
                intro=intro+"Du : "+new SimpleDateFormat("dd/MM/yyyy").format(this.date1)+" Au : "+new SimpleDateFormat("dd/MM/yyyy").format(this.date2);
               
            }catch(Exception hyufo){}
            jasperDesign.setQuery(query);
            jasperDesign.setName("occurence"+this.modeleChoisi.getCodeModeleSortie()+"Design");
            jasperDesign.setLeftMargin(20);  
            jasperDesign.setRightMargin(20);  
            jasperDesign.setTopMargin(20);  
            jasperDesign.setBottomMargin(20);
            Integer widthDesZones=new Integer(555);
            if(this.modeleChoisi.getPaysagePortait()){
                widthDesZones=new Integer(802);
                jasperDesign.setOrientation(OrientationEnum.LANDSCAPE);
                jasperDesign.setPageWidth(842);
                jasperDesign.setPageHeight(595);
            }else{
                jasperDesign.setOrientation(OrientationEnum.PORTRAIT);
                jasperDesign.setPageWidth(595);
                jasperDesign.setPageHeight(842);
            }
            JRDesignBand bandHeader=new JRDesignBand();
            bandHeader.setHeight(110);
            JRDesignParameter parmWhere=new JRDesignParameter();
            parmWhere.setValueClass(String.class);
            parmWhere.setName("intro");
            parmWhere.setForPrompting(true);
            jasperDesign.addParameter(parmWhere);
            JRDesignBand bandPageHeader=new JRDesignBand();
            bandPageHeader.setHeight(15);
            JRDesignTextField textFieldPageHeader = new JRDesignTextField();
                textFieldPageHeader.setFontSize(10);
                textFieldPageHeader.setX(0);
                textFieldPageHeader.setY(0);
                textFieldPageHeader.setWidth(widthDesZones);
                textFieldPageHeader.setHeight(15);
                textFieldPageHeader.getLineBox().setLeftPadding(2);
                textFieldPageHeader.getLineBox().setRightPadding(2);
//                textFieldPageHeader.setBorder(new Byte("0"));
                textFieldPageHeader.setStretchWithOverflow(true);
                textFieldPageHeader.setBlankWhenNull(true);
                JRDesignExpression expressionPageHeader = new JRDesignExpression();
                expressionPageHeader.setValueClass(java.lang.String.class);
                expressionPageHeader.setText("$P{intro}");
                textFieldPageHeader.setExpression(expressionPageHeader);
                bandPageHeader.addElement(textFieldPageHeader);
            jasperDesign.setPageHeader(bandPageHeader);
            HashMap parameters=new HashMap();
            parameters.put("intro",intro);
            jasperDesign.setTitle(bandHeader);
            JRDesignFrame frameHeader=new JRDesignFrame();
            frameHeader.setWidth(widthDesZones);frameHeader.setY(0);frameHeader.setX(0);
            frameHeader.setHeight(110);
            frameHeader.setBackcolor(Color.white);
            frameHeader.setForecolor(Color.blue);
            frameHeader.setMode(ModeEnum.OPAQUE);
            bandHeader.addElement(frameHeader);
            JRDesignStaticText textStaticSoc = new JRDesignStaticText();
            textStaticSoc.setFontSize(10);
            textStaticSoc.setX(20);
            textStaticSoc.setY(0);
            textStaticSoc.getLineBox().setLeftPadding(2);
            textStaticSoc.getLineBox().setRightPadding(2);
            textStaticSoc.setWidth(widthDesZones-40);
            textStaticSoc.setHeight(20);
            textStaticSoc.setText("Societe : "+nomSoc);
            frameHeader.addElement(textStaticSoc);
            JRDesignStaticText textStaticAdressSoc = new JRDesignStaticText();
            textStaticAdressSoc.setFontSize(10);
            textStaticAdressSoc.setX(20);
            textStaticAdressSoc.setY(20);
            textStaticAdressSoc.getLineBox().setLeftPadding(2);
            textStaticAdressSoc.getLineBox().setRightPadding(2);
            textStaticAdressSoc.setWidth(widthDesZones-40);
            textStaticAdressSoc.setHeight(20);
            textStaticAdressSoc.setText("Adresse : "+adresseSoc);
            frameHeader.addElement(textStaticAdressSoc);
            JRDesignStaticText textStaticTelFaxSoc = new JRDesignStaticText();
            textStaticTelFaxSoc.setFontSize(10);
            textStaticTelFaxSoc.setX(20);
            textStaticTelFaxSoc.setY(40);
            textStaticTelFaxSoc.getLineBox().setLeftPadding(2);
            textStaticTelFaxSoc.getLineBox().setRightPadding(2);
            textStaticTelFaxSoc.setWidth(widthDesZones-40);
            textStaticTelFaxSoc.setHeight(20);
            textStaticTelFaxSoc.setText("Tel : "+telSoc+" Fax : "+faxSoc);
            Integer xModeleSort=new Integer(0);
            Iterator itModeles=detailsModele.iterator();
                  int posY=60;
                    while(itModeles.hasNext()){
                
						  DetailModeleSortie detModSor=(DetailModeleSortie)itModeles.next();
                String val=detailsModeleValeurFiltrage.get(xModeleSort);
               if(val!=null){
                        if((val.isEmpty()==false)&&(posY+20<110)){
                            JRDesignStaticText textStaticwhere = new JRDesignStaticText();
                textStaticwhere.setFontSize(10);
                textStaticwhere.setX(20);
                textStaticwhere.setY(posY);
                textStaticwhere.getLineBox().setLeftPadding(2);
                textStaticwhere.getLineBox().setRightPadding(2);
                textStaticwhere.setWidth(widthDesZones-40);
                textStaticwhere.setHeight(20);
				 if(detModSor.getTypeColonne().contains("date")){
                                try{
                                    StringTokenizer sttsuak=new StringTokenizer(val,"-/");
                                    if(sttsuak.countTokens()==3){
                                        String f1=sttsuak.nextToken();
                                        String f2=sttsuak.nextToken();
                                        String f3=sttsuak.nextToken();
                                        if(f1.length()==4){
                                            val=f3+"-"+f2+"-"+f1;
                                        }else{
                                            val=f1+"-"+f2+"-"+f3;
                                        }
                                    }
                                
                                }catch(Exception exce87){}
                            }
                textStaticwhere.setText(this.correspondreColonne(detModSor.getNomColonne())+":"+val);
                frameHeader.addElement(textStaticwhere);
                posY=posY+20;
                           
                        }
                    }
				xModeleSort++;	 
                    }
            frameHeader.addElement(textStaticTelFaxSoc);
            JRDesignStaticText textStaticTitreRapport = new JRDesignStaticText();
            textStaticTitreRapport.setFontSize(14);
            textStaticTitreRapport.setX(20);
            textStaticTitreRapport.setY(65);
            textStaticTitreRapport.getLineBox().setLeftPadding(2);
            textStaticTitreRapport.getLineBox().setRightPadding(2);
            textStaticTitreRapport.setWidth(widthDesZones-40);
            textStaticTitreRapport.setHeight(40);
            textStaticTitreRapport.setText(nomRapport);
            textStaticTitreRapport.setBold(true);
            textStaticTitreRapport.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
            frameHeader.addElement(textStaticTitreRapport);
            JRDesignBand bandColumnHeader=new JRDesignBand();
            bandColumnHeader.setHeight(20);
            jasperDesign.setColumnHeader(bandColumnHeader);
            Iterator it2=lMs.iterator();
            Integer posActuelle=0;
            JRDesignBand bandBody=new JRDesignBand();
            bandBody.setHeight(15);
            JRDesignBand bandSummary=new JRDesignBand();
            bandSummary.setHeight(30);
         //   jasperDesign.setDetail(bandBody);
           			       ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandBody);
            Integer xxx=new Integer(0);
            JRDesignField field55 = new JRDesignField();  
            field55.setName("facture");
            field55.setValueClass(Boolean.class);
            jasperDesign.addField(field55);
            while(it2.hasNext()){
                DetailModeleSortie nCol=(DetailModeleSortie)it2.next();
                JRDesignStaticText textStaticColHeader = new JRDesignStaticText();
                Integer widthElement=nCol.getLongueurColonne();
                textStaticColHeader.setFontSize(10);
                textStaticColHeader.setX(posActuelle);
                textStaticColHeader.setY(0);
                textStaticColHeader.setWidth(widthElement);
                textStaticColHeader.setHeight(20);
                textStaticColHeader.setForecolor(Color.blue);
                textStaticColHeader.setBackcolor(Color.white);
                textStaticColHeader.setMode(ModeEnum.getByValue(new Byte("1")));
                textStaticColHeader.getLineBox().setLeftPadding(2);
                textStaticColHeader.getLineBox().setRightPadding(2);
//                textStaticColHeader.setBorder(new Byte("1"));
//                textStaticColHeader.setBorderColor(Color.black);
                textStaticColHeader.getLineBox().getLeftPen().setLineWidth(1);
textStaticColHeader.getLineBox().getTopPen().setLineWidth(1);
textStaticColHeader.getLineBox().getRightPen().setLineWidth(1);
textStaticColHeader.getLineBox().getTopPen().setLineWidth(1);
textStaticColHeader.getLineBox().getBottomPen().setLineWidth(1);  
                textStaticColHeader.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                textStaticColHeader.setText(this.correspondreColonne(nCol.getNomColonne()));
                JRDesignField field = new JRDesignField();  
                field.setName(nCol.getNomColonne());
                field.setValueClass(java.lang.String.class);
                jasperDesign.addField(field);
                bandColumnHeader.addElement(textStaticColHeader);
                JRDesignTextField textField = new JRDesignTextField();
                textField.setFontSize(nCol.getFontSizeColonne());
                textField.setX(posActuelle);
                textField.setY(0);
                textField.setWidth(widthElement);
                textField.setHeight(15);
                textField.getLineBox().setLeftPadding(2);
                textField.getLineBox().setRightPadding(2);
//                textField.setBorder(new Byte("1"));
//                textField.setBorderColor(Color.black);
                textField.getLineBox().getLeftPen().setLineWidth(1);
textField.getLineBox().getTopPen().setLineWidth(1);
textField.getLineBox().getRightPen().setLineWidth(1);
textField.getLineBox().getTopPen().setLineWidth(1);
textField.getLineBox().getBottomPen().setLineWidth(1);  
                textField.setStretchWithOverflow(false);
                textField.setBlankWhenNull(true);
                JRDesignExpression expression = new JRDesignExpression();
                expression.setValueClass(java.lang.String.class);
                expression.setText("$F{"+nCol.getNomColonne()+"}");
                textField.setExpression(expression);
                if((nCol.getTypeColonne().equals("date")==true) | (nCol.getTypeColonne().equals("datetime")==true)){
                    JRDesignExpression expressionDate = new JRDesignExpression();
                    expressionDate.setValueClass(java.lang.String.class);
                    expressionDate.setText("new SimpleDateFormat(Character.toString('d')+Character.toString('d')+Character.toString('-')+Character.toString('M')+Character.toString('M')+Character.toString('-')+Character.toString('y')+Character.toString('y')+Character.toString('y')+Character.toString('y')).format((Date)new SimpleDateFormat(Character.toString('y')+Character.toString('y')+Character.toString('y')+Character.toString('y')+Character.toString('-')+Character.toString('M')+Character.toString('M')+Character.toString('-')+Character.toString('d')+Character.toString('d')).parse($F{"+nCol.getNomColonne()+"}))");
                    textField.setExpression(expressionDate);
                }else{
                if(nCol.getTypeColonne().equals("varchar")==false){
                                  if(nCol.getTypeColonne().equals("longtext")==false){
                                      if(nCol.getNomColonne().contains("NUM_OP")==false){
                                      JRDesignExpression expressionNumericNonFact = new JRDesignExpression();
                                      expressionNumericNonFact.setValueClass(java.lang.String.class);
                                      expressionNumericNonFact.setText("$F{"+nCol.getNomColonne()+"}");
                                      textField.setExpression(expressionNumericNonFact);
                                        if(nCol.getSommable()){
                                            JRDesignTextField textField2 = new JRDesignTextField();
                                            textField2.setFontSize(nCol.getFontSizeColonne());
                                            textField2.setX(posActuelle);
                                            textField2.setY(15);
                                            textField2.setWidth(widthElement);
                                            textField2.setHeight(15);
                                            textField2.getLineBox().setLeftPadding(2);
                                            textField2.getLineBox().setRightPadding(2);
//                                            textField2.setBorder(new Byte("1"));
//                                            textField2.setBorderColor(Color.black);
                                            textField2.getLineBox().getLeftPen().setLineWidth(1);
textField2.getLineBox().getTopPen().setLineWidth(1);
textField2.getLineBox().getRightPen().setLineWidth(1);
textField2.getLineBox().getTopPen().setLineWidth(1);
textField2.getLineBox().getBottomPen().setLineWidth(1);  
                                            textField2.setStretchWithOverflow(false);
                                            JRDesignVariable variable2 = new JRDesignVariable();
                                            variable2.setCalculation(CalculationEnum.SUM);
                                            JRDesignExpression expressionVar = new JRDesignExpression();  
                                            expressionVar.setValueClass(java.math.BigDecimal.class);  
                                            expressionVar.setText("($F{"+nCol.getNomColonne()+"}==null)?BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR):new BigDecimal($F{"+nCol.getNomColonne()+"}).setScale(3,RoundingMode.FLOOR)");
                                            variable2.setExpression(expressionVar);
                                            variable2.setName(nCol.getNomColonne());
                                            variable2.setValueClass(java.math.BigDecimal.class);
                                            JRDesignExpression expressionVarInit = new JRDesignExpression();  
                                            expressionVarInit.setValueClass(java.math.BigDecimal.class);
                                            expressionVarInit.setText("BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR)");
                                            jasperDesign.addVariable(variable2);
                                            JRDesignExpression expression2 = new JRDesignExpression();  
                                            expression2.setValueClass(java.math.BigDecimal.class);  
                                            expression2.setText("$V{"+nCol.getNomColonne()+"}");
                                            textField2.setExpression(expression2);
                                            textField2.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                                            bandSummary.addElement(textField2);
                                        }
                                  }else{
                                          JRDesignExpression expressionNumericNonFact = new JRDesignExpression();
                                      expressionNumericNonFact.setValueClass(java.lang.String.class);
                                      expressionNumericNonFact.setText("$F{"+nCol.getNomColonne()+"}");
                                      textField.setExpression(expressionNumericNonFact);
                                        if(nCol.getSommable()){
                                            JRDesignTextField textField2 = new JRDesignTextField();
                                            textField2.setFontSize(10);
                                            textField2.setX(posActuelle);
                                            textField2.setY(15);
//                                            textField2.setY(0);
                                            textField2.setWidth(widthElement);
                                            textField2.setHeight(15);
                                            textField2.getLineBox().setLeftPadding(2);
                                            textField2.getLineBox().setRightPadding(2);
//                                            textField2.setBorder(new Byte("1"));
//                                            textField2.setBorderColor(Color.black);
                                            textField2.getLineBox().getLeftPen().setLineWidth(1);
textField2.getLineBox().getTopPen().setLineWidth(1);
textField2.getLineBox().getRightPen().setLineWidth(1);
textField2.getLineBox().getTopPen().setLineWidth(1);
textField2.getLineBox().getBottomPen().setLineWidth(1);  
                                            textField2.setStretchWithOverflow(false);
                                            JRDesignVariable variable2 = new JRDesignVariable();
                                            variable2.setCalculation(CalculationEnum.SUM);
                                            JRDesignExpression expressionVar = new JRDesignExpression();  
                                            expressionVar.setValueClass(java.math.BigDecimal.class);
                                            expressionVar.setText("new BigDecimal($F{"+nCol.getNomColonne()+"}).setScale(3,RoundingMode.FLOOR)");
                                            variable2.setExpression(expressionVar);
                                            variable2.setName(nCol.getNomColonne());
                                            variable2.setValueClass(java.math.BigDecimal.class);
                                            JRDesignExpression expressionVarInit = new JRDesignExpression();  
                                            expressionVarInit.setValueClass(java.math.BigDecimal.class);
                                            expressionVarInit.setText("BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR)");
                                            jasperDesign.addVariable(variable2);
                                            JRDesignExpression expression2 = new JRDesignExpression();  
                                            expression2.setValueClass(java.math.BigDecimal.class);  
                                            expression2.setText("$V{"+nCol.getNomColonne()+"}");
                                            textField2.setExpression(expression2);
                                            textField2.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                                            bandSummary.addElement(textField2);
                                        }
                                      }
                        }
                                 textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                }
                }
                bandBody.addElement(textField);
                posActuelle=posActuelle+widthElement;
                xxx++;
            }
            if(this.modeleChoisi.getReglementRapport()){
                JRDesignField fieldReg = new JRDesignField();  
                fieldReg.setName("sum(MONTANT_REG)");
                fieldReg.setValueClass(java.lang.String.class);
                jasperDesign.addField(fieldReg);
                JRDesignStaticText textStaticColHeaderSumReglm = new JRDesignStaticText();
                textStaticColHeaderSumReglm.setFontSize(this.modeleChoisi.getFontSizeReglement());
                textStaticColHeaderSumReglm.setX(posActuelle);
                textStaticColHeaderSumReglm.setY(0);
                textStaticColHeaderSumReglm.setWidth(this.modeleChoisi.getLongeurReglement());
                textStaticColHeaderSumReglm.setHeight(20);
                textStaticColHeaderSumReglm.setForecolor(Color.blue);
                textStaticColHeaderSumReglm.setBackcolor(Color.white);
                textStaticColHeaderSumReglm.setMode(ModeEnum.getByValue(new Byte("1")));
                textStaticColHeaderSumReglm.getLineBox().setLeftPadding(2);
                textStaticColHeaderSumReglm.getLineBox().setRightPadding(2);
//                textStaticColHeaderSumReglm.setBorder(new Byte("1"));
//                textStaticColHeaderSumReglm.setBorderColor(Color.black);
                textStaticColHeaderSumReglm.getLineBox().getLeftPen().setLineWidth(1);
textStaticColHeaderSumReglm.getLineBox().getTopPen().setLineWidth(1);
textStaticColHeaderSumReglm.getLineBox().getRightPen().setLineWidth(1);
textStaticColHeaderSumReglm.getLineBox().getTopPen().setLineWidth(1);
textStaticColHeaderSumReglm.getLineBox().getBottomPen().setLineWidth(1);  
                textStaticColHeaderSumReglm.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                textStaticColHeaderSumReglm.setText("Reglement");
                bandColumnHeader.addElement(textStaticColHeaderSumReglm);
                JRDesignTextField textFieldReg = new JRDesignTextField();
                textFieldReg.setFontSize(this.modeleChoisi.getFontSizeReglement());
                textFieldReg.setX(posActuelle);
                posActuelle=posActuelle+this.modeleChoisi.getLongeurReglement();
                textFieldReg.setY(0);
                textFieldReg.setWidth(this.modeleChoisi.getLongeurReglement());
                textFieldReg.setHeight(15);
                textFieldReg.getLineBox().setLeftPadding(2);
                textFieldReg.getLineBox().setRightPadding(2);
//                textFieldReg.setBorder(new Byte("1"));
//                textFieldReg.setBorderColor(Color.black);
                   textFieldReg.getLineBox().getLeftPen().setLineWidth(1);
textFieldReg.getLineBox().getTopPen().setLineWidth(1);
textFieldReg.getLineBox().getRightPen().setLineWidth(1);
textFieldReg.getLineBox().getTopPen().setLineWidth(1);
textFieldReg.getLineBox().getBottomPen().setLineWidth(1);  
                textFieldReg.setStretchWithOverflow(false);
                textFieldReg.setBlankWhenNull(true);
                textFieldReg.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                JRDesignExpression expressionReg = new JRDesignExpression();
                expressionReg.setValueClass(java.lang.String.class);
                expressionReg.setText("$F{sum(MONTANT_REG)}");
                textFieldReg.setExpression(expressionReg);
                bandBody.addElement(textFieldReg);
                JRDesignTextField textSumReg = new JRDesignTextField();
                textSumReg.setFontSize(this.modeleChoisi.getFontSizeReglement());
                textSumReg.setX(textFieldReg.getX());
                textSumReg.setY(15);
//                textSumReg.setY(0);
                textSumReg.setWidth(this.modeleChoisi.getLongeurReglement());
                textSumReg.setHeight(15);
                textSumReg.getLineBox().setLeftPadding(2);
                textSumReg.getLineBox().setRightPadding(2);
//                textSumReg.setBorder(new Byte("1"));
//                textSumReg.setBorderColor(Color.black);
                    textSumReg.getLineBox().getLeftPen().setLineWidth(1);
textSumReg.getLineBox().getTopPen().setLineWidth(1);
textSumReg.getLineBox().getRightPen().setLineWidth(1);
textSumReg.getLineBox().getTopPen().setLineWidth(1);
textSumReg.getLineBox().getBottomPen().setLineWidth(1);  
                textSumReg.setStretchWithOverflow(false);
                JRDesignVariable variableSumReg2 = new JRDesignVariable();
                variableSumReg2.setCalculation(CalculationEnum.SUM);
                JRDesignExpression expressionVarSumReg = new JRDesignExpression();  
                expressionVarSumReg.setValueClass(java.math.BigDecimal.class);
                expressionVarSumReg.setText("($F{sum(MONTANT_REG)}==null)?BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR):new BigDecimal($F{sum(MONTANT_REG)}).setScale(3,RoundingMode.FLOOR)");
                variableSumReg2.setExpression(expressionVarSumReg);
                variableSumReg2.setName("variableSumReg2");
                variableSumReg2.setValueClass(java.math.BigDecimal.class);
                JRDesignExpression expressionVarInitSumReg = new JRDesignExpression();  
                expressionVarInitSumReg.setValueClass(java.math.BigDecimal.class);
                expressionVarInitSumReg.setText("BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR)");
                variableSumReg2.setInitialValueExpression(expressionVarInitSumReg);
                jasperDesign.addVariable(variableSumReg2);
                JRDesignExpression expressionSumReg2 = new JRDesignExpression();  
                expressionSumReg2.setValueClass(java.math.BigDecimal.class);  
                expressionSumReg2.setText("$V{variableSumReg2}");
                textSumReg.setExpression(expressionSumReg2);
                textSumReg.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                bandSummary.addElement(textSumReg);
            }
            if(this.modeleChoisi.getSolde()){
                try{
                    JRDesignField fieldReg = new JRDesignField();  
                    fieldReg.setName("sum(MONTANT_REG)");
                    fieldReg.setValueClass(java.lang.String.class);
                    jasperDesign.addField(fieldReg);
                }catch(Exception e){}
                JRDesignStaticText textStaticColHeaderSumSolde = new JRDesignStaticText();
                textStaticColHeaderSumSolde.setFontSize(this.modeleChoisi.getFontSizeSolde());
                textStaticColHeaderSumSolde.setX(posActuelle);
                textStaticColHeaderSumSolde.setY(0);
                textStaticColHeaderSumSolde.setWidth(this.modeleChoisi.getLongueurSolde());
                textStaticColHeaderSumSolde.setHeight(20);
                textStaticColHeaderSumSolde.setForecolor(Color.blue);
                textStaticColHeaderSumSolde.setBackcolor(Color.white);
                textStaticColHeaderSumSolde.setMode(ModeEnum.getByValue(new Byte("1")));
                textStaticColHeaderSumSolde.getLineBox().setLeftPadding(2);
                textStaticColHeaderSumSolde.getLineBox().setRightPadding(2);
//                textStaticColHeaderSumSolde.setBorder(new Byte("1"));
//                textStaticColHeaderSumSolde.setBorderColor(Color.black);
                   textStaticColHeaderSumSolde.getLineBox().getLeftPen().setLineWidth(1);
textStaticColHeaderSumSolde.getLineBox().getTopPen().setLineWidth(1);
textStaticColHeaderSumSolde.getLineBox().getRightPen().setLineWidth(1);
textStaticColHeaderSumSolde.getLineBox().getTopPen().setLineWidth(1);
textStaticColHeaderSumSolde.getLineBox().getBottomPen().setLineWidth(1);  
                textStaticColHeaderSumSolde.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                textStaticColHeaderSumSolde.setText("Solde");
                JRDesignField fieldSolde = new JRDesignField();  
                fieldSolde.setName("ttcOperation");
                fieldSolde.setValueClass(java.math.BigDecimal.class);
                jasperDesign.addField(fieldSolde);
                JRDesignVariable variableSolde = new JRDesignVariable();
                JRDesignExpression expressionVarSolde = new JRDesignExpression();  
                expressionVarSolde.setValueClass(java.math.BigDecimal.class);
                expressionVarSolde.setText("($F{sum(MONTANT_REG)}==null)?$F{ttcOperation}.setScale(3,RoundingMode.FLOOR):$F{ttcOperation}.setScale(3,RoundingMode.FLOOR).subtract(new BigDecimal($F{sum(MONTANT_REG)}).setScale(3,RoundingMode.FLOOR))");
                variableSolde.setExpression(expressionVarSolde);
                variableSolde.setName("soldeVariableFacture");
                variableSolde.setValueClass(java.math.BigDecimal.class);
                JRDesignExpression expressionVarInitSolde = new JRDesignExpression();  
                expressionVarInitSolde.setValueClass(java.math.BigDecimal.class);
                expressionVarInitSolde.setText("BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR)");
                variableSolde.setInitialValueExpression(expressionVarInitSolde);
                jasperDesign.addVariable(variableSolde);
                bandColumnHeader.addElement(textStaticColHeaderSumSolde);
                JRDesignTextField textFieldSol = new JRDesignTextField();
                textFieldSol.setFontSize(this.modeleChoisi.getFontSizeSolde());
                textFieldSol.setX(posActuelle);
                posActuelle=posActuelle+this.modeleChoisi.getLongueurSolde();
                textFieldSol.setY(0);
                textFieldSol.setWidth(this.modeleChoisi.getLongueurSolde());
                textFieldSol.setHeight(15);
                textFieldSol.getLineBox().setLeftPadding(2);
                textFieldSol.getLineBox().setRightPadding(2);
//                textFieldSol.setBorder(new Byte("1"));
//                textFieldSol.setBorderColor(Color.black);
                    textFieldSol.getLineBox().getLeftPen().setLineWidth(1);
textFieldSol.getLineBox().getTopPen().setLineWidth(1);
textFieldSol.getLineBox().getRightPen().setLineWidth(1);
textFieldSol.getLineBox().getTopPen().setLineWidth(1);
textFieldSol.getLineBox().getBottomPen().setLineWidth(1);  
                textFieldSol.setStretchWithOverflow(false);
                textFieldSol.setBlankWhenNull(true);
                textFieldSol.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                JRDesignExpression expressionSol = new JRDesignExpression();
                expressionSol.setValueClass(java.math.BigDecimal.class);
                expressionSol.setText("$V{soldeVariableFacture}");
                textFieldSol.setExpression(expressionSol);
                bandBody.addElement(textFieldSol);
                JRDesignTextField textSumSol = new JRDesignTextField();
                textSumSol.setFontSize(this.modeleChoisi.getFontSizeSolde());
                textSumSol.setX(textFieldSol.getX());
                textSumSol.setY(15);
//                textSumSol.setY(0);
                textSumSol.setWidth(this.modeleChoisi.getLongueurSolde());
                textSumSol.setHeight(15);
                textSumSol.getLineBox().setLeftPadding(2);
                textSumSol.getLineBox().setRightPadding(2);
//                textSumSol.setBorder(new Byte("1"));
//                textSumSol.setBorderColor(Color.black);
                      textSumSol.getLineBox().getLeftPen().setLineWidth(1);
textSumSol.getLineBox().getTopPen().setLineWidth(1);
textSumSol.getLineBox().getRightPen().setLineWidth(1);
textSumSol.getLineBox().getTopPen().setLineWidth(1);
textSumSol.getLineBox().getBottomPen().setLineWidth(1);  
                textSumSol.setStretchWithOverflow(false);
                JRDesignVariable variableSumSol2 = new JRDesignVariable();
                variableSumSol2.setCalculation(CalculationEnum.SUM);
                JRDesignExpression expressionVarSumSol = new JRDesignExpression();  
                expressionVarSumSol.setValueClass(java.math.BigDecimal.class);
                expressionVarSumSol.setText("$V{soldeVariableFacture}");
                variableSumSol2.setExpression(expressionVarSumSol);
                variableSumSol2.setName("variableSumSol2");
                variableSumSol2.setValueClass(java.math.BigDecimal.class);
                JRDesignExpression expressionVarInitSumSol = new JRDesignExpression();  
                expressionVarInitSumSol.setValueClass(java.math.BigDecimal.class);
                expressionVarInitSumSol.setText("BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR)");
                variableSumSol2.setInitialValueExpression(expressionVarInitSumSol);
                jasperDesign.addVariable(variableSumSol2);
                JRDesignExpression expressionSumSol2 = new JRDesignExpression();  
                expressionSumSol2.setValueClass(java.math.BigDecimal.class);  
                expressionSumSol2.setText("$V{variableSumSol2}");
                textSumSol.setExpression(expressionSumSol2);
                textSumSol.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                bandSummary.addElement(textSumSol);
            }
            jasperDesign.setSummary(bandSummary);
            JRDesignBand bandFooter=new JRDesignBand();
            bandFooter.setHeight(15);
            jasperDesign.setPageFooter(bandFooter);
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
            JRDesignTextField textField = new JRDesignTextField();
                textField.setFontSize(10);
                textField.setX(100);
                textField.setY(0);
                textField.setWidth(150);
                textField.setHeight(15);
                textField.getLineBox().setLeftPadding(2);
                textField.getLineBox().setRightPadding(2);
                textField.setStretchWithOverflow(false);
                textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                JRDesignExpression expression = new JRDesignExpression();  
                expression.setValueClass(java.lang.String.class);
                expression.setText("$V{PAGE_NUMBER}.toString()+'/'");
                textField.setExpression(expression);
                textField.setEvaluationTime(EvaluationTimeEnum.NOW);
                bandFooter.addElement(textField);
                JRDesignTextField textField2 = new JRDesignTextField();
                textField2.setFontSize(10);
                textField2.setX(250);
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
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,connection);
                connection.close();
                return jasperPrint;
        }catch(Exception er){
            er.printStackTrace();
        }
        return null;
    }
    
    
    public String genererListItemsPdf(){
        try{
                JasperPrint jasperPrint = this.jasperPrintListItems();
                DateFormat dateFormat1 = new SimpleDateFormat("dd MM yyyy HH mm ss");
                Date d=new Date();
                String dd=dateFormat1.format(d);
                StringTokenizer sttt=new StringTokenizer(this.pageActuelle.toUpperCase()," ");
                String part1=sttt.nextToken().trim();
                String part2=sttt.nextToken().trim();
                if(part1.charAt(part1.length()-1)!='S'){
                    part1=part1+"S";
                }
                if(part2.charAt(part2.length()-1)!='S'){
                    part2=part2+"S";
                }
                String nomRapport="JOURNAL "+part1+" "+part2;
                String nomFichier=nomRapport;
                byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
		FacesContext context = FacesContext.getCurrentInstance();
                HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		HttpServletResponse response = (HttpServletResponse) context
				.getExternalContext().getResponse();
                response.addHeader("Content-disposition","attachment;filename="+nomFichier);
		response.setContentLength(bytes.length);
		response.getOutputStream().write(bytes);
		response.setContentType("application/pdf");
		context.responseComplete();
                this.emailReceiver="";
                this.emailSender="";
                this.passwordEmailSender="";
        }catch(Exception er){
            er.printStackTrace();
        }
        return "";
    }
    private String emailSender;
    private String emailReceiver;
    private String passwordEmailSender;

    public String getEmailReceiver() {
        return emailReceiver;
    }

    public void setEmailReceiver(String emailReceiver) {
        this.emailReceiver = emailReceiver;
    }

    public String getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(String emailSender) {
        this.emailSender = emailSender;
    }

    public String getPasswordEmailSender() {
        return passwordEmailSender;
    }

    public void setPasswordEmailSender(String passwordEmailSender) {
        this.passwordEmailSender = passwordEmailSender;
    }
    
    public String genererListItemsExcel(){
        try{
                JasperPrint jasperPrint = this.jasperPrintListItems();
                StringTokenizer sttt=new StringTokenizer(this.pageActuelle.toUpperCase()," ");
                String part1=sttt.nextToken().trim();
                String part2=sttt.nextToken().trim();
                if(part1.charAt(part1.length()-1)!='S'){
                    part1=part1+"S";
                }
                if(part2.charAt(part2.length()-1)!='S'){
                    part2=part2+"S";
                }
                String nomRapport="JOURNAL "+part1+" "+part2;
                nomRapport=nomRapport.replace(" ","_");
                DateFormat dateFormat1 = new SimpleDateFormat("dd MM yyyy HH mm ss");
                Date d=new Date();
                String dd=dateFormat1.format(d);
                String nomFichier="Rapport "+nomRapport+" "+dd+".xls";
                System.out.println(nomFichier);
                OutputStream ouputStream=new FileOutputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport/")+"\\"+nomFichier));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JRXlsExporter exporterXLS = new JRXlsExporter();
                exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
                exporterXLS.exportReport();
                ouputStream.write(byteArrayOutputStream.toByteArray());
                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition","attachment; filename="+nomFichier+".xls");
		response.setContentLength(byteArrayOutputStream.toByteArray().length);
		response.getOutputStream().write(byteArrayOutputStream.toByteArray());
		context.responseComplete();
        }catch(Exception er){
            er.printStackTrace();
        }
        return "";
    }
    public String sendEmailPdf() {
        try{
                JasperPrint jasperPrint = this.jasperPrintListItems();
                DateFormat dateFormat1 = new SimpleDateFormat("dd MM yyyy HH mm ss");
                Date d=new Date();
                String dd=dateFormat1.format(d);
                StringTokenizer sttt=new StringTokenizer(this.pageActuelle.toUpperCase()," ");
                String part1=sttt.nextToken().trim();
                String part2=sttt.nextToken().trim();
                if(part1.charAt(part1.length()-1)!='S'){
                    part1=part1+"S";
                }
                if(part2.charAt(part2.length()-1)!='S'){
                    part2=part2+"S";
                }
                String nomRapport="JOURNAL "+part1+" "+part2;
                nomRapport=nomRapport.replace(" ","_");
                String nomFichier="Rapport "+nomRapport+" "+dd+".pdf";
                byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
                FileOutputStream fs = new FileOutputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport")+"\\"+nomFichier));
                BufferedOutputStream bs = new BufferedOutputStream(fs);
                bs.write(bytes);
                bs.close();
                File ffile=new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport")+"\\"+nomFichier);
                
                final String usernameEmail = this.emailSender;
		final String passwordEmail = this.passwordEmailSender;
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
                Session session = Session.getInstance(props,null);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.emailSender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.emailReceiver));
			message.setSubject("Rapport Du Logiciel Horizon");
			message.setText("cet email contient le rapport : "+nomFichier);
                        Multipart mp = new MimeMultipart( );
                        MimeBodyPart mbp1 = new MimeBodyPart( );
                        mbp1.setContent(nomFichier, "text/plain");
                        mp.addBodyPart(mbp1);
                        MimeBodyPart mbp = new MimeBodyPart( );
                        mbp.setFileName(ffile.getName( ));
                        mbp.setDataHandler(new DataHandler(new FileDataSource(ffile)));
                        mp.addBodyPart(mbp);
                        message.setContent(mp);
                        Transport transport = session.getTransport("smtp");
                        transport.connect("smtp.gmail.com",this.emailSender,this.passwordEmailSender);
                        transport.sendMessage(message, message.getAllRecipients());
                        transport.close();
			JsfUtil.addSuccessMessage("mail envoyer");
        }catch(Exception er){
            JsfUtil.addErrorMessage("Verifier votre connexion internet, votre login et votre password");
            er.printStackTrace();
        }
        return "";
	}
    public String sendEmailExcel(){
        try{
                DateFormat dateFormat1 = new SimpleDateFormat("dd MM yyyy HH mm ss");
                Date d=new Date();
                String dd=dateFormat1.format(d);
                StringTokenizer sttt=new StringTokenizer(this.pageActuelle.toUpperCase()," ");
                String part1=sttt.nextToken().trim();
                String part2=sttt.nextToken().trim();
                if(part1.charAt(part1.length()-1)!='S'){
                    part1=part1+"S";
                }
                if(part2.charAt(part2.length()-1)!='S'){
                    part2=part2+"S";
                }
                String nomRapport="JOURNAL "+part1+" "+part2;
                nomRapport=nomRapport.replace(" ","_");
                String nomFichier="Rapport "+nomRapport+" "+dd+".xls";
                JasperPrint jasperPrint = this.jasperPrintListItems();
                System.out.println(nomFichier);
                OutputStream ouputStream=new FileOutputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport/")+"\\"+nomFichier));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JRXlsExporter exporterXLS = new JRXlsExporter();
                exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
                exporterXLS.exportReport();
                ouputStream.write(byteArrayOutputStream.toByteArray());
                
                File ffile=new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport")+"\\"+nomFichier);
                final String usernameEmail = this.emailSender;
		final String passwordEmail = this.passwordEmailSender;
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
                Session session = Session.getInstance(props,null);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.emailSender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.emailReceiver));
			message.setSubject("Rapport Du Logiciel Horizon");
			message.setText("cet email contient le rapport : "+nomFichier);
                        Multipart mp = new MimeMultipart( );
                        MimeBodyPart mbp1 = new MimeBodyPart( );
                        mbp1.setContent(nomFichier, "text/plain");
                        mp.addBodyPart(mbp1);
                        MimeBodyPart mbp = new MimeBodyPart( );
                        mbp.setFileName(ffile.getName( ));
                        mbp.setDataHandler(new DataHandler(new FileDataSource(ffile)));
                        mp.addBodyPart(mbp);
                        message.setContent(mp);
                        Transport transport = session.getTransport("smtp");
                        transport.connect("smtp.gmail.com",this.emailSender,this.passwordEmailSender);
                        transport.sendMessage(message, message.getAllRecipients());
                        transport.close();
			JsfUtil.addSuccessMessage("mail envoyer");
        }catch(Exception er){
            JsfUtil.addErrorMessage("Verifier votre connexion internet, votre login et votre password");
            er.printStackTrace();
        }
        return "";
    }
    
    public String correspondreColonne(String nomColonne){
        String s=nomColonne;
        try{
            TableCorrespondance t=this.ejbTableCorrespondance.findByParameterSingleResult("Select t from TableCorrespondance t where t.colonne=:colonne","colonne",nomColonne);
            s=t.getCorrespondance();
            return s;
        }catch(NoResultException nnded){}
        catch(Exception r){}
        s=nomColonne;
        return s;
    }
        public int ordreColonne(String nomColonne){
        String s=nomColonne;
          
        try{
            TableCorrespondance t=this.ejbTableCorrespondance.findByParameterSingleResult("Select t from TableCorrespondance t where t.correspondance=:colonne","colonne",nomColonne);
           try{ int ordre=t.getOrdre();
           if ((nomColonne.equalsIgnoreCase("raison sociale"))&&modeleProduitCom)
           {return 46;}
               
                 return ordre;
           }catch (Exception e){e.printStackTrace();
               System.out.println("eee");
               return 1000;}
     
        }catch(NoResultException nnded){
            System.out.println("eee1 "+nomColonne);
        }
        catch(Exception r){           System.out.println("eee2 "+nomColonne);}
    
        return 1000;
    }
 
    public boolean afficheColonne(String nomColonne){
      boolean s=false;
        try{
            TableCorrespondance t=this.ejbTableCorrespondance.findByParameterSingleResult("Select t from TableCorrespondance t where t.colonne=:colonne","colonne",nomColonne);
            s=t.getAffichable();
            return s;
        }catch(NoResultException nnded){}
        catch(Exception r){}
    
        return s;
    }
    public String correspondreColonneBackward(String nomColonneAffiche){
        String s=nomColonneAffiche;
        try{
            TableCorrespondance t=this.ejbTableCorrespondance.findByParameterSingleResult("Select t from TableCorrespondance t where t.correspondance=:correspondance","correspondance",nomColonneAffiche);
            s=t.getColonne();
            return s;
        }catch(NoResultException nnded){}
        catch(Exception r){}
        s=nomColonneAffiche;
        return s;
    }
    public String correspondreColonneTable(){
        try{
            return correspondreColonne(this.detailsModele.get(this.dataTableDetails.getRowIndex()).getNomColonne());
        }catch(Exception r){}
        return "";
    }
    
    public SelectItem[] getItemsAvailableSelectOneModeReglement() {
        List<Modereglement>l=this.ejbModeReglement.findAll();
        Iterator i=l.iterator();
        SelectItem[] sel=new SelectItem[l.size()+1];
        sel[0]=new SelectItem(new Integer(0), "Total");
        Integer x=new Integer(1);
        while(i.hasNext()){
            Modereglement mm=(Modereglement)i.next();
       //     sel[x]=new SelectItem(mm.getId(),mm.getId());
            x++;
        }
        return sel;
    }
    
    public List<String> autocompleteCodeNomDynamique(String code){
        List<String> result = new ArrayList<String>();
        try{
        result=(List<String>) getLikeDynamiqueComCodeNom(code);
        return result;
        }catch(Exception d){d.printStackTrace();
        result = new ArrayList<String>();
        return result;
         }
    }

    public List<String> getLikeDynamiqueComCodeNom(String code){
         List<String>l=new ArrayList<String>();
         try{
             DetailModeleSortie dettt=this.detailsModele.get(this.dataTableDetails.getRowIndex());
             String nomTable=dettt.getTableColonne();
             String colonne=dettt.getNomColonne();
             Driver monDriver = new com.mysql.jdbc.Driver();
             StringTokenizer getUrl=new StringTokenizer(this.getFacade().urlCourante(),"**");
             String url =getUrl.nextToken();
             String login=getUrl.nextToken();
             String password =getUrl.nextToken();
             String nomBaseDeDonnes=url.substring(url.lastIndexOf("/")+1);
             DriverManager.registerDriver(monDriver);
             Connection connection = DriverManager.getConnection(url,login, password);
             Statement statement = connection.createStatement();
             ResultSet rs =null;
             if((dettt.getTypeColonne().equals("date")==false) & (dettt.getTypeColonne().equals("datetime")==false)){
             rs = statement.executeQuery("SELECT DISTINCT("+nomTable+"."+colonne+") from "+nomBaseDeDonnes+"."+nomTable+" where "+nomTable+"."+colonne+" like '%"+code+"%' LIMIT 100");
             if (rs != null) {
                    while (rs.next()) {
                        l.add(rs.getString(1));
                    }
                }
             }else{
                 String dateDeRecherche="";
                 while(code.contains("-")){
                     dateDeRecherche=dateDeRecherche+code.substring(code.lastIndexOf("-")+1,code.length())+"-";
                     code=code.substring(0,code.lastIndexOf("-"));
                 }
                 if(code.isEmpty()==false){
                     dateDeRecherche=dateDeRecherche+code;
                 }
                 rs = statement.executeQuery("SELECT DISTINCT(DATE_FORMAT("+nomTable+"."+colonne+",'%d-%m-%Y')) from "+nomBaseDeDonnes+"."+nomTable+" where "+nomTable+"."+colonne+" like '%"+dateDeRecherche+"%' LIMIT 100");
                 if (rs != null) {
                        while (rs.next()) {
                            l.add(rs.getString(1));
                        }
                 }
             }
              rs.close();
              statement.close();
              connection.close();
          }catch(Exception ere){
             l=new ArrayList<String>();
          }
        return l;
    }
    
 
    private BigDecimal seuilPieceSoldee;

    public BigDecimal getSeuilPieceSoldee() {
        return seuilPieceSoldee;
    }

    public void setSeuilPieceSoldee(BigDecimal seuilPieceSoldee) {
        this.seuilPieceSoldee = seuilPieceSoldee;
    }
  
    protected TypeOpCom typeOperationFactureAux;
    

    protected TypeOpCom tyt=null;
    
    private Boolean rapprochementFacture;
    public Boolean getRapprochementFacture() {
        return rapprochementFacture;
    }
    public void setRapprochementFacture(Boolean rapprochementFacture) {
        this.rapprochementFacture = rapprochementFacture;
    }
    public JasperPrint genererRapprochementFactures(){
            Connection connection=null;
            Statement statement=null;
            ResultSet rs = null;
            boolean afficherLibelleTotal=false;
            try{
                String nomSoc="";
                String adresseSoc="";
                String telSoc="";
                String faxSoc="";
                String nomRapport=this.pageActuelle;
                Driver monDriver = new com.mysql.jdbc.Driver();
                StringTokenizer getUrl=new StringTokenizer(this.getFacade().urlCourante(),"**");
                String url =getUrl.nextToken();
                String nomBaseDeDonnes=url.substring(url.lastIndexOf("/")+1);
                String login=getUrl.nextToken();
                String password =getUrl.nextToken();
                DriverManager.registerDriver(monDriver);
                connection = DriverManager.getConnection(url,login, password);
                List<DetailModeleSortie>lMs=this.ejbDetailModeleFacade.findByParameter("Select d from DetailModeleSortie d where d.modeleSortie=:modeleSortie order by (d.ordreColonne) ASC","modeleSortie",this.modeleChoisi);
                statement = connection.createStatement();
                rs = statement.executeQuery("SELECT LIBELLE_SOCIETE,ADRESSE,TEL,FAX FROM `"+nomBaseDeDonnes+"`.`horizon_sys` where id='"+1+"'");
                if (rs != null) {
                        while (rs.next()) {
                            nomSoc=rs.getString(1);
                            adresseSoc=rs.getString(2);
                            telSoc=rs.getString(3);
                            faxSoc=rs.getString(4);
                        }
                   }
                rs.close();
                statement.close();
                JasperDesign jasperDesign=new JasperDesign();
                JRDesignQuery query = new JRDesignQuery();
                query.setText(this.requetteSqlRapprochementFactures());
                System.out.println("Rapprochement : "+query.getText());
                String whereSqlClause="";
                String intro="Periode: ";
                try{
                    intro=intro+"Du : "+new SimpleDateFormat("dd/MM/yyyy").format(this.date1)+" Au : "+new SimpleDateFormat("dd/MM/yyyy").format(this.date2);
               
                }catch(Exception hyufo){hyufo.printStackTrace();}
                jasperDesign.setQuery(query);
                JRDesignField operGrpFild=new JRDesignField();
                operGrpFild.setName("niReglemente");
                operGrpFild.setValueClass(String.class);
                jasperDesign.addField(operGrpFild);
                JRDesignField montantReglemente=new JRDesignField();
                montantReglemente.setName("montantReglemente");
                montantReglemente.setValueClass(BigDecimal.class);
                jasperDesign.addField(montantReglemente);
                JRDesignField montantFacturee=new JRDesignField();
                montantFacturee.setName("montantFacturee");
                montantFacturee.setValueClass(BigDecimal.class);
                jasperDesign.addField(montantFacturee);
                JRDesignField numeroFacturee=new JRDesignField();
                numeroFacturee.setName("numeroFacturee");
                numeroFacturee.setValueClass(String.class);
                jasperDesign.addField(numeroFacturee);
                JRDesignField dateFacturee=new JRDesignField();
                dateFacturee.setName("dateFacturee");
                dateFacturee.setValueClass(Date.class);
                jasperDesign.addField(dateFacturee);
                JRDesignField typeFacturee=new JRDesignField();
                typeFacturee.setName("typeFacturee");
                typeFacturee.setValueClass(String.class);
                jasperDesign.addField(typeFacturee);
                JRDesignGroup groupOper=new JRDesignGroup();
                JRDesignExpression exprGroupOper=new JRDesignExpression();
                exprGroupOper.setText("$F{niReglemente}");
                exprGroupOper.setValueClass(String.class);
                groupOper.setExpression(exprGroupOper);
                groupOper.setName("groupOper");
                jasperDesign.addGroup(groupOper);
                JRDesignBand bandGroupFooter=new JRDesignBand();
                bandGroupFooter.setHeight(15);
           //     groupOper.setGroupFooter(bandGroupFooter);
                ((JRDesignSection) groupOper.getGroupFooterSection()).addBand(bandGroupFooter);
                jasperDesign.setName("Design V5021");
                jasperDesign.setLeftMargin(20);  
                jasperDesign.setRightMargin(20);  
                jasperDesign.setTopMargin(20);  
                jasperDesign.setBottomMargin(20);
                Integer widthDesZones=new Integer(555);
                if(this.modeleChoisi.getPaysagePortait()){
                    widthDesZones=new Integer(802);
                    jasperDesign.setOrientation(OrientationEnum.LANDSCAPE);
                    jasperDesign.setPageWidth(842);
                    jasperDesign.setPageHeight(595);
                }else{
                    jasperDesign.setOrientation(OrientationEnum.PORTRAIT);
                    jasperDesign.setPageWidth(595);
                    jasperDesign.setPageHeight(842);
                }
                JRDesignBand bandHeader=new JRDesignBand();
                bandHeader.setHeight(110);
                JRDesignParameter parmWhere=new JRDesignParameter();
                parmWhere.setValueClass(String.class);
                parmWhere.setName("intro");
                parmWhere.setForPrompting(true);
                jasperDesign.addParameter(parmWhere);
                JRDesignBand bandPageHeader=new JRDesignBand();
                bandPageHeader.setHeight(15);
                JRDesignTextField textFieldPageHeader = new JRDesignTextField();
                    textFieldPageHeader.setFontSize(10);
                    textFieldPageHeader.setX(0);
                    textFieldPageHeader.setY(0);
                    textFieldPageHeader.setWidth(widthDesZones);
                    textFieldPageHeader.setHeight(15);
                    textFieldPageHeader.getLineBox().setLeftPadding(2);
                    textFieldPageHeader.getLineBox().setRightPadding(2);
                 
                    textFieldPageHeader.setStretchWithOverflow(true);
                    textFieldPageHeader.setBlankWhenNull(true);
                    JRDesignExpression expressionPageHeader = new JRDesignExpression();
                    expressionPageHeader.setValueClass(java.lang.String.class);
                    expressionPageHeader.setText("$P{intro}");
                    textFieldPageHeader.setExpression(expressionPageHeader);
                    bandPageHeader.addElement(textFieldPageHeader);
                jasperDesign.setPageHeader(bandPageHeader);
                HashMap parameters=new HashMap();
                parameters.put("intro",intro);
                jasperDesign.setTitle(bandHeader);
                JRDesignFrame frameHeader=new JRDesignFrame();
                frameHeader.setWidth(widthDesZones);frameHeader.setY(0);frameHeader.setX(0);
                frameHeader.setHeight(110);
                frameHeader.setBackcolor(Color.white);
                frameHeader.setForecolor(Color.blue);
                frameHeader.setMode(ModeEnum.OPAQUE);
                bandHeader.addElement(frameHeader);
                JRDesignStaticText textStaticSoc = new JRDesignStaticText();
                textStaticSoc.setFontSize(10);
                textStaticSoc.setX(20);
                textStaticSoc.setY(0);
                textStaticSoc.getLineBox().setLeftPadding(2);
                textStaticSoc.getLineBox().setRightPadding(2);
                textStaticSoc.setWidth(widthDesZones-40);
                textStaticSoc.setHeight(20);
                textStaticSoc.setText("Societe : "+nomSoc);
                frameHeader.addElement(textStaticSoc);
                JRDesignStaticText textStaticAdressSoc = new JRDesignStaticText();
                textStaticAdressSoc.setFontSize(10);
                textStaticAdressSoc.setX(20);
                textStaticAdressSoc.setY(20);
                textStaticAdressSoc.getLineBox().setLeftPadding(2);
                textStaticAdressSoc.getLineBox().setRightPadding(2);
                textStaticAdressSoc.setWidth(widthDesZones-40);
                textStaticAdressSoc.setHeight(20);
                textStaticAdressSoc.setText("Adresse : "+adresseSoc);
                frameHeader.addElement(textStaticAdressSoc);
                JRDesignStaticText textStaticTelFaxSoc = new JRDesignStaticText();
                textStaticTelFaxSoc.setFontSize(10);
                textStaticTelFaxSoc.setX(20);
                textStaticTelFaxSoc.setY(40);
                textStaticTelFaxSoc.getLineBox().setLeftPadding(2);
                textStaticTelFaxSoc.getLineBox().setRightPadding(2);
                textStaticTelFaxSoc.setWidth(widthDesZones-40);
                textStaticTelFaxSoc.setHeight(20);
                textStaticTelFaxSoc.setText("Tel : "+telSoc+" Fax : "+faxSoc);
                frameHeader.addElement(textStaticTelFaxSoc);
                Integer xModeleSort=new Integer(0);
            Iterator itModeles=detailsModele.iterator();
                  int posY=60;
                    while(itModeles.hasNext()){
                
						  DetailModeleSortie detModSor=(DetailModeleSortie)itModeles.next();
                String val=detailsModeleValeurFiltrage.get(xModeleSort);
               if(val!=null){
                        if((val.isEmpty()==false)&&(posY+20<110)){
                            JRDesignStaticText textStaticwhere = new JRDesignStaticText();
                textStaticwhere.setFontSize(10);
                textStaticwhere.setX(20);
                textStaticwhere.setY(posY);
                textStaticwhere.getLineBox().setLeftPadding(2);
                textStaticwhere.getLineBox().setRightPadding(2);
                textStaticwhere.setWidth(widthDesZones-40);
                textStaticwhere.setHeight(20);
				 if(detModSor.getTypeColonne().contains("date")){
                                try{
                                    StringTokenizer sttsuak=new StringTokenizer(val,"-/");
                                    if(sttsuak.countTokens()==3){
                                        String f1=sttsuak.nextToken();
                                        String f2=sttsuak.nextToken();
                                        String f3=sttsuak.nextToken();
                                        if(f1.length()==4){
                                            val=f3+"-"+f2+"-"+f1;
                                        }else{
                                            val=f1+"-"+f2+"-"+f3;
                                        }
                                    }
                                
                                }catch(Exception exce87){}
                            }
                textStaticwhere.setText(this.correspondreColonne(detModSor.getNomColonne())+":"+val);
                frameHeader.addElement(textStaticwhere);
                posY=posY+20;
                           
                        }
                    }
				xModeleSort++;	 
                    }
                JRDesignStaticText textStaticTitreRapport = new JRDesignStaticText();
                textStaticTitreRapport.setFontSize(14);
                textStaticTitreRapport.setX(20);
                textStaticTitreRapport.setY(65);
                textStaticTitreRapport.getLineBox().setLeftPadding(2);
                textStaticTitreRapport.getLineBox().setRightPadding(2);
                textStaticTitreRapport.setWidth(widthDesZones-40);
                textStaticTitreRapport.setHeight(40);
                textStaticTitreRapport.setText(nomRapport);
                textStaticTitreRapport.setBold(true);
                textStaticTitreRapport.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                frameHeader.addElement(textStaticTitreRapport);
                JRDesignBand bandColumnHeader=new JRDesignBand();
                bandColumnHeader.setHeight(20);
                jasperDesign.setColumnHeader(bandColumnHeader);
                Iterator it2=lMs.iterator();
                Integer posActuelle=0;
                JRDesignBand bandBody=new JRDesignBand();
                bandBody.setHeight(16);
                JRDesignBand bandSummary=new JRDesignBand();
                bandSummary.setHeight(30);
           //     jasperDesign.setDetail(bandBody);
                ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandBody);
                Integer xxx=new Integer(0);
                JRDesignField field55 = new JRDesignField();  
                field55.setName("flagFacturee");
                field55.setValueClass(Boolean.class);
                jasperDesign.addField(field55);
                JRDesignVariable variableMontantReglement = new JRDesignVariable();
                variableMontantReglement.setCalculation(CalculationEnum.SUM);
                JRDesignExpression expressionvariableMontantReglement = new JRDesignExpression();  
                expressionvariableMontantReglement.setValueClass(java.math.BigDecimal.class);
                expressionvariableMontantReglement.setText("($F{montantReglemente}==null)?new BigDecimal(\"0\").setScale(3,RoundingMode.FLOOR):$F{montantReglemente}.setScale(3,RoundingMode.FLOOR)");
                variableMontantReglement.setExpression(expressionvariableMontantReglement);
                variableMontantReglement.setName("varVariableMontantReglemente");
                variableMontantReglement.setValueClass(java.math.BigDecimal.class);
                variableMontantReglement.setResetType(ResetTypeEnum.GROUP);
                variableMontantReglement.setResetGroup(groupOper);
                JRDesignExpression expressionvariableMontantReglementInit = new JRDesignExpression();  
                expressionvariableMontantReglementInit.setValueClass(java.math.BigDecimal.class);
                expressionvariableMontantReglementInit.setText("BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR)");
                jasperDesign.addVariable(variableMontantReglement);
                while(it2.hasNext()){
                    DetailModeleSortie nCol=(DetailModeleSortie)it2.next();
                    JRDesignStaticText textStaticColHeader = new JRDesignStaticText();
                    Integer widthElement=nCol.getLongueurColonne();
                    textStaticColHeader.setFontSize(10);
                    textStaticColHeader.setX(posActuelle);
                    textStaticColHeader.setY(0);
                    textStaticColHeader.setWidth(widthElement);
                    textStaticColHeader.setHeight(20);
                    textStaticColHeader.setForecolor(Color.blue);
                    textStaticColHeader.setBackcolor(Color.white);
                    textStaticColHeader.setMode(ModeEnum.getByValue(new Byte("1")));
                    textStaticColHeader.getLineBox().setLeftPadding(2);
                    textStaticColHeader.getLineBox().setRightPadding(2);
                   textStaticColHeader.getLineBox().getLeftPen().setLineWidth(1);
textStaticColHeader.getLineBox().getTopPen().setLineWidth(1);
textStaticColHeader.getLineBox().getRightPen().setLineWidth(1);
textStaticColHeader.getLineBox().getTopPen().setLineWidth(1);
textStaticColHeader.getLineBox().getBottomPen().setLineWidth(1); 
                    textStaticColHeader.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    textStaticColHeader.setText(this.correspondreColonne(nCol.getNomColonne()));
                    JRDesignField field = new JRDesignField();  
                    field.setName(nCol.getNomColonne());
                    field.setValueClass(java.lang.String.class);
                    jasperDesign.addField(field);
                    bandColumnHeader.addElement(textStaticColHeader);
                    JRDesignTextField textField = new JRDesignTextField();
                    textField.setFontSize(nCol.getFontSizeColonne());
                    textField.setX(posActuelle);
                    textField.setY(0);
                    textField.setWidth(widthElement);
                    textField.setHeight(15);
                    textField.getLineBox().setLeftPadding(2);
                    textField.getLineBox().setRightPadding(2);
                  textField.getLineBox().getLeftPen().setLineWidth(1);
textField.getLineBox().getTopPen().setLineWidth(1);
textField.getLineBox().getRightPen().setLineWidth(1);
textField.getLineBox().getTopPen().setLineWidth(1);
textField.getLineBox().getBottomPen().setLineWidth(1); 
                    textField.setStretchWithOverflow(false);
                    textField.setBlankWhenNull(true);
                    JRDesignExpression expression = new JRDesignExpression();
                    expression.setValueClass(java.lang.String.class);
                    expression.setText("$F{"+nCol.getNomColonne()+"}");
                    textField.setExpression(expression);
                    if((nCol.getTypeColonne().equals("date")==true) | (nCol.getTypeColonne().equals("datetime")==true)){
                        JRDesignExpression expressionDate = new JRDesignExpression();
                        expressionDate.setValueClass(java.lang.String.class);
                        expressionDate.setText("new SimpleDateFormat(Character.toString('d')+Character.toString('d')+Character.toString('-')+Character.toString('M')+Character.toString('M')+Character.toString('-')+Character.toString('y')+Character.toString('y')+Character.toString('y')+Character.toString('y')).format((Date)new SimpleDateFormat(Character.toString('y')+Character.toString('y')+Character.toString('y')+Character.toString('y')+Character.toString('-')+Character.toString('M')+Character.toString('M')+Character.toString('-')+Character.toString('d')+Character.toString('d')).parse($F{"+nCol.getNomColonne()+"}))");
                        textField.setExpression(expressionDate);
                    }else{
                    if(nCol.getTypeColonne().equals("varchar")==false){
                                      if(nCol.getTypeColonne().equals("longtext")==false){
                                          if(nCol.getNomColonne().contains("NUM_OP")==false){
                                          JRDesignExpression expressionNumericNonFact = new JRDesignExpression();
                                          expressionNumericNonFact.setValueClass(java.lang.String.class);
                                          expressionNumericNonFact.setText("$F{"+nCol.getNomColonne()+"}");
                                          textField.setExpression(expressionNumericNonFact);
                                            if(nCol.getSommable()){
                                                JRDesignTextField textField2 = new JRDesignTextField();
                                                textField2.setFontSize(nCol.getFontSizeColonne());
                                                textField2.setX(posActuelle);
                                                textField2.setY(15);
                                                textField2.setWidth(widthElement);
                                                textField2.setHeight(15);
                                                textField2.getLineBox().setLeftPadding(2);
                                                textField2.getLineBox().setRightPadding(2);
                                                       textField2.getLineBox().getLeftPen().setLineWidth(1);
textField2.getLineBox().getTopPen().setLineWidth(1);
textField2.getLineBox().getRightPen().setLineWidth(1);
textField2.getLineBox().getTopPen().setLineWidth(1);
textField2.getLineBox().getBottomPen().setLineWidth(1); 
                                                textField2.setStretchWithOverflow(false);
                                                afficherLibelleTotal=true;
                                                JRDesignVariable variable2 = new JRDesignVariable();
                                                variable2.setCalculation(CalculationEnum.SUM);
                                                JRDesignExpression expressionVar = new JRDesignExpression();  
                                                expressionVar.setValueClass(java.math.BigDecimal.class);  
                                                expressionVar.setText("($F{"+nCol.getNomColonne()+"}==null)?BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR):new BigDecimal($F{"+nCol.getNomColonne()+"}).setScale(3,RoundingMode.FLOOR)");
                                                variable2.setExpression(expressionVar);
                                                variable2.setName(nCol.getNomColonne());
                                                variable2.setValueClass(java.math.BigDecimal.class);
                                                variable2.setIncrementType(IncrementTypeEnum.GROUP);
                                                variable2.setIncrementGroup(groupOper);
                                                JRDesignExpression expressionVarInit = new JRDesignExpression();  
                                                expressionVarInit.setValueClass(java.math.BigDecimal.class);
                                                expressionVarInit.setText("BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR)");
                                                jasperDesign.addVariable(variable2);
                                                JRDesignExpression expression2 = new JRDesignExpression();  
                                                expression2.setValueClass(java.math.BigDecimal.class);  
                                                expression2.setText("$V{"+nCol.getNomColonne()+"}");
                                                textField2.setExpression(expression2);
                                                textField2.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                                                bandSummary.addElement(textField2);
                                            JRDesignVariable var1=new JRDesignVariable();
                                            var1.setCalculation(CalculationEnum.SUM);
                                            JRDesignExpression expressionVar214 = new JRDesignExpression();  
                                            expressionVar214.setValueClass(java.math.BigDecimal.class);
                                            expressionVar214.setText("($F{"+nCol.getNomColonne()+"}==null)?BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR):new BigDecimal($F{"+nCol.getNomColonne()+"}).setScale(3,RoundingMode.FLOOR)");
                                            var1.setExpression(expressionVar214);
                                            var1.setName(nCol.getNomColonne()+"var1");
                                            var1.setValueClass(java.math.BigDecimal.class);
                                            JRDesignExpression expressionVarInit214 = new JRDesignExpression();  
                                            expressionVarInit214.setValueClass(java.math.BigDecimal.class);
                                            expressionVarInit214.setText("BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR)");
                                            var1.setInitialValueExpression(expressionVarInit214);
                                            var1.setResetType(ResetTypeEnum.GROUP);
                                            var1.setIncrementType(IncrementTypeEnum.GROUP);
                                            var1.setIncrementGroup(groupOper);
                                            var1.setResetGroup(groupOper);
                                            jasperDesign.addVariable(var1);
                                            }
                                      }else{
                                              JRDesignExpression expressionNumericNonFact = new JRDesignExpression();
                                          expressionNumericNonFact.setValueClass(java.lang.String.class);
                                          expressionNumericNonFact.setText("$F{"+nCol.getNomColonne()+"}");
                                          textField.setExpression(expressionNumericNonFact);
                                            if(nCol.getSommable()){
                                                JRDesignTextField textField2 = new JRDesignTextField();
                                                textField2.setFontSize(10);
                                                textField2.setX(posActuelle);
                                                textField2.setY(15);
                                                textField2.setWidth(widthElement);
                                                textField2.setHeight(15);
                                                textField2.getLineBox().setLeftPadding(2);
                                                textField2.getLineBox().setRightPadding(2);
                                                       textField2.getLineBox().getLeftPen().setLineWidth(1);
textField2.getLineBox().getTopPen().setLineWidth(1);
textField2.getLineBox().getRightPen().setLineWidth(1);
textField2.getLineBox().getTopPen().setLineWidth(1);
textField2.getLineBox().getBottomPen().setLineWidth(1); 
                                                textField2.setStretchWithOverflow(false);
                                                JRDesignVariable variable2 = new JRDesignVariable();
                                                variable2.setCalculation(CalculationEnum.SUM);
                                                JRDesignExpression expressionVar = new JRDesignExpression();  
                                                expressionVar.setValueClass(java.math.BigDecimal.class);
                                                expressionVar.setText("new BigDecimal($F{"+nCol.getNomColonne()+"}).setScale(3,RoundingMode.FLOOR)");
                                                variable2.setExpression(expressionVar);
                                                variable2.setName(nCol.getNomColonne());
                                                afficherLibelleTotal=true;
                                                variable2.setValueClass(java.math.BigDecimal.class);
                                                variable2.setIncrementType(IncrementTypeEnum.GROUP);
                                                variable2.setIncrementGroup(groupOper);
                                                JRDesignExpression expressionVarInit = new JRDesignExpression();  
                                                expressionVarInit.setValueClass(java.math.BigDecimal.class);
                                                expressionVarInit.setText("BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR)");
                                                jasperDesign.addVariable(variable2);
                                                JRDesignExpression expression2 = new JRDesignExpression();  
                                                expression2.setValueClass(java.math.BigDecimal.class);  
                                                expression2.setText("$V{"+nCol.getNomColonne()+"}");
                                                textField2.setExpression(expression2);
                                                textField2.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                                                bandSummary.addElement(textField2);
                                            }
                                          }
                            }
                                     textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                    }
                    }
                    bandGroupFooter.addElement(textField);
                    posActuelle=posActuelle+widthElement;
                    xxx++;
                }
                if(afficherLibelleTotal){
                    JRDesignStaticText textStaticColHeader25 = new JRDesignStaticText();
                    textStaticColHeader25.setFontSize(10);
                    textStaticColHeader25.setX(0);
                    textStaticColHeader25.setY(0);
                    textStaticColHeader25.setWidth(widthDesZones);
                    textStaticColHeader25.setHeight(15);
                    textStaticColHeader25.setForecolor(Color.blue);
                    textStaticColHeader25.setBackcolor(Color.white);
                    textStaticColHeader25.setMode(ModeEnum.getByValue(new Byte("1")));
                    textStaticColHeader25.getLineBox().setLeftPadding(2);
                    textStaticColHeader25.getLineBox().setRightPadding(2);
                    textStaticColHeader25.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    textStaticColHeader25.setText("Total");
                    bandSummary.addElement(textStaticColHeader25);
                }
                if(this.modeleChoisi.getReglementRapport()){
                    JRDesignTextField textFieldType748895 = new JRDesignTextField();
                    textFieldType748895.setFontSize(0);
                    textFieldType748895.setX(0);
                    textFieldType748895.setY(0);
                    textFieldType748895.setWidth(1);
                    textFieldType748895.setHeight(16);
                    textFieldType748895.getLineBox().setLeftPadding(2);
                    textFieldType748895.getLineBox().setRightPadding(2);
                           textFieldType748895.getLineBox().getLeftPen().setLineWidth(1);
textFieldType748895.getLineBox().getTopPen().setLineWidth(1);
textFieldType748895.getLineBox().getRightPen().setLineWidth(1);
textFieldType748895.getLineBox().getTopPen().setLineWidth(1);
textFieldType748895.getLineBox().getBottomPen().setLineWidth(1); 
                    textFieldType748895.setStretchWithOverflow(false);
                    textFieldType748895.setBlankWhenNull(true);
                    textFieldType748895.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                    bandBody.addElement(textFieldType748895);
                    JRDesignStaticText textStaticColHeaderSumReglm = new JRDesignStaticText();
                    textStaticColHeaderSumReglm.setFontSize(this.modeleChoisi.getFontSizeReglement());
                    textStaticColHeaderSumReglm.setX(posActuelle);
                    textStaticColHeaderSumReglm.setY(0);
                    textStaticColHeaderSumReglm.setWidth(this.modeleChoisi.getLongeurReglement());
                    textStaticColHeaderSumReglm.setHeight(20);
                    textStaticColHeaderSumReglm.setForecolor(Color.blue);
                    textStaticColHeaderSumReglm.setBackcolor(Color.white);
                    textStaticColHeaderSumReglm.setMode(ModeEnum.getByValue(new Byte("1")));
                    textStaticColHeaderSumReglm.getLineBox().setLeftPadding(2);
                    textStaticColHeaderSumReglm.getLineBox().setRightPadding(2);
                        textStaticColHeaderSumReglm.getLineBox().getLeftPen().setLineWidth(1);
textStaticColHeaderSumReglm.getLineBox().getTopPen().setLineWidth(1);
textStaticColHeaderSumReglm.getLineBox().getRightPen().setLineWidth(1);
textStaticColHeaderSumReglm.getLineBox().getTopPen().setLineWidth(1);
textStaticColHeaderSumReglm.getLineBox().getBottomPen().setLineWidth(1); 
                    textStaticColHeaderSumReglm.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    textStaticColHeaderSumReglm.setText("Rapprochement");
                    bandColumnHeader.addElement(textStaticColHeaderSumReglm);
                    JRDesignTextField textFieldType = new JRDesignTextField();
                    textFieldType.setFontSize(this.modeleChoisi.getFontSizeReglement());
                    textFieldType.setX(posActuelle);
                    JRDesignVariable var111=new JRDesignVariable();
                    var111.setCalculation(CalculationEnum.NOTHING);
                    JRDesignExpression expressionVar214111 = new JRDesignExpression();  
                    expressionVar214111.setValueClass(String.class);
                    expressionVar214111.setText("($F{typeFacturee}==null)?\" \""
                                                                        + ":"
                                                                        + "(($F{flagFacturee}.booleanValue()==false)?(($F{typeFacturee}.toLowerCase().contains(\"facture\"))?\"avoir\":(($F{typeFacturee}.toLowerCase().contains(\"livraison\"))?\"b.retour\":\" \"))"
                                                                                                                    + ":"
                                                                                                                    + "(($F{typeFacturee}.toLowerCase().contains(\"fournisseur\"))?($F{typeFacturee}.toLowerCase().substring(0,$F{typeFacturee}.indexOf(\"fournisseur\")).trim()):($F{typeFacturee}.toLowerCase())))");
                    var111.setExpression(expressionVar214111);
                    var111.setName("typeFacturee");
                    var111.setValueClass(String.class);
                    jasperDesign.addVariable(var111);
                    textFieldType.setY(0);
                    textFieldType.setWidth(this.modeleChoisi.getLongeurReglement()/5);
                    textFieldType.setHeight(15);
                    textFieldType.getLineBox().setLeftPadding(2);
                    textFieldType.getLineBox().setRightPadding(2);
                        textFieldType.getLineBox().getLeftPen().setLineWidth(1);
textFieldType.getLineBox().getTopPen().setLineWidth(1);
textFieldType.getLineBox().getRightPen().setLineWidth(1);
textFieldType.getLineBox().getTopPen().setLineWidth(1);
textFieldType.getLineBox().getBottomPen().setLineWidth(1);
                    textFieldType.setStretchWithOverflow(false);
                    textFieldType.setBlankWhenNull(true);
                    textFieldType.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                    JRDesignExpression expressionType = new JRDesignExpression();
                    expressionType.setValueClass(java.lang.String.class);
                    expressionType.setText("$V{typeFacturee}+\" N\"");
                    textFieldType.setExpression(expressionType);
                    bandBody.addElement(textFieldType);
                    posActuelle=posActuelle+textFieldType.getWidth();
                    JRDesignTextField textNumeroFacture = new JRDesignTextField();
                    textNumeroFacture.setFontSize(this.modeleChoisi.getFontSizeReglement());
                    textNumeroFacture.setX(posActuelle);
                    textNumeroFacture.setY(0);
                    textNumeroFacture.setWidth(this.modeleChoisi.getLongeurReglement()/5);
                    textNumeroFacture.setHeight(15);
                    textNumeroFacture.getLineBox().setLeftPadding(2);
                    textNumeroFacture.getLineBox().setRightPadding(2);
                        textNumeroFacture.getLineBox().getLeftPen().setLineWidth(1);
textNumeroFacture.getLineBox().getTopPen().setLineWidth(1);
textNumeroFacture.getLineBox().getRightPen().setLineWidth(1);
textNumeroFacture.getLineBox().getTopPen().setLineWidth(1);
textNumeroFacture.getLineBox().getBottomPen().setLineWidth(1);
                    textNumeroFacture.setStretchWithOverflow(false);
                    textNumeroFacture.setBlankWhenNull(true);
                    textNumeroFacture.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                    JRDesignExpression expressionNumeroFacture = new JRDesignExpression();
                    expressionNumeroFacture.setValueClass(java.lang.String.class);
                    expressionNumeroFacture.setText("$F{numeroFacturee}");
                    textNumeroFacture.setExpression(expressionNumeroFacture);
                    bandBody.addElement(textNumeroFacture);
                    posActuelle=posActuelle+textNumeroFacture.getWidth();
                    JRDesignTextField textDateFacture = new JRDesignTextField();
                    textDateFacture.setFontSize(this.modeleChoisi.getFontSizeReglement());
                    textDateFacture.setX(posActuelle);
                    textDateFacture.setY(0);
                    textDateFacture.setWidth(this.modeleChoisi.getLongeurReglement()/5);
                    textDateFacture.setHeight(15);
                    textDateFacture.getLineBox().setLeftPadding(2);
                    textDateFacture.getLineBox().setRightPadding(2);
                    textDateFacture.getLineBox().getLeftPen().setLineWidth(1);
textDateFacture.getLineBox().getTopPen().setLineWidth(1);
textDateFacture.getLineBox().getRightPen().setLineWidth(1);
textDateFacture.getLineBox().getTopPen().setLineWidth(1);
textDateFacture.getLineBox().getBottomPen().setLineWidth(1);
                    textDateFacture.setStretchWithOverflow(false);
                    textDateFacture.setBlankWhenNull(true);
                    textDateFacture.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                    JRDesignExpression expressionDateFacture = new JRDesignExpression();
                    expressionDateFacture.setValueClass(java.lang.String.class);
                    expressionDateFacture.setText("new SimpleDateFormat(Character.toString('d')+Character.toString('d')+Character.toString('-')+Character.toString('M')+Character.toString('M')+Character.toString('-')+Character.toString('y')+Character.toString('y')+Character.toString('y')+Character.toString('y')).format($F{dateFacturee})");
                    textDateFacture.setExpression(expressionDateFacture);
                    bandBody.addElement(textDateFacture);
                    posActuelle=posActuelle+textDateFacture.getWidth();
                    Integer widthMontant=this.modeleChoisi.getLongeurReglement()-textDateFacture.getWidth()-textNumeroFacture.getWidth()-textFieldType.getWidth();
                    JRDesignTextField textMontantFacture = new JRDesignTextField();
                    textMontantFacture.setFontSize(this.modeleChoisi.getFontSizeReglement());
                    textMontantFacture.setX(posActuelle);
                    textMontantFacture.setY(0);
                    textMontantFacture.setWidth(widthMontant);
                    textMontantFacture.setHeight(15);
                    textMontantFacture.getLineBox().setLeftPadding(2);
                    textMontantFacture.getLineBox().setRightPadding(2);
               textMontantFacture.getLineBox().getLeftPen().setLineWidth(1);
textMontantFacture.getLineBox().getTopPen().setLineWidth(1);
textMontantFacture.getLineBox().getRightPen().setLineWidth(1);
textMontantFacture.getLineBox().getTopPen().setLineWidth(1);
textMontantFacture.getLineBox().getBottomPen().setLineWidth(1);
                    textMontantFacture.setStretchWithOverflow(false);
                    textMontantFacture.setBlankWhenNull(true);
                    textMontantFacture.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                    JRDesignExpression expressionMontantFacture = new JRDesignExpression();
                    expressionMontantFacture.setValueClass(BigDecimal.class);
                    expressionMontantFacture.setText("($F{montantFacturee}==null)?new BigDecimal(0).setScale(3,RoundingMode.FLOOR):$F{montantFacturee}.setScale(3,RoundingMode.FLOOR)");
                    textMontantFacture.setExpression(expressionMontantFacture);
                    bandBody.addElement(textMontantFacture);
                    JRDesignVariable variableMontantFacture = new JRDesignVariable();
                    variableMontantFacture.setCalculation(CalculationEnum.SUM);
                    JRDesignExpression expressionvariableMontantFacture = new JRDesignExpression();  
                    expressionvariableMontantFacture.setValueClass(java.math.BigDecimal.class);
                    expressionvariableMontantFacture.setText("($F{montantFacturee}==null)?new BigDecimal(\"0\").setScale(3,RoundingMode.FLOOR):$F{montantFacturee}.setScale(3,RoundingMode.FLOOR)");
                    variableMontantFacture.setExpression(expressionvariableMontantFacture);
                    variableMontantFacture.setName("varVariableMontantFacture");
                    variableMontantFacture.setValueClass(java.math.BigDecimal.class);
                    variableMontantFacture.setResetType(ResetTypeEnum.GROUP);
                    variableMontantFacture.setResetGroup(groupOper);
                    JRDesignExpression expressionvariableMontantFactureInit = new JRDesignExpression();  
                    expressionvariableMontantFactureInit.setValueClass(java.math.BigDecimal.class);
                    expressionvariableMontantFactureInit.setText("BigDecimal.ZERO.setScale(3,RoundingMode.FLOOR)");
                    jasperDesign.addVariable(variableMontantFacture);
                    JRDesignTextField textSommeGlobalGroupFooter = new JRDesignTextField();
                    textSommeGlobalGroupFooter.setFontSize(this.modeleChoisi.getFontSizeReglement());
                    textSommeGlobalGroupFooter.setX(textFieldType.getX());
                    textSommeGlobalGroupFooter.setY(0);
                    textSommeGlobalGroupFooter.setWidth(this.modeleChoisi.getLongeurReglement());
                    textSommeGlobalGroupFooter.setHeight(15);
                    textSommeGlobalGroupFooter.getLineBox().setLeftPadding(2);
                    textSommeGlobalGroupFooter.getLineBox().setRightPadding(2);
                     textSommeGlobalGroupFooter.getLineBox().getLeftPen().setLineWidth(1);
textSommeGlobalGroupFooter.getLineBox().getTopPen().setLineWidth(1);
textSommeGlobalGroupFooter.getLineBox().getRightPen().setLineWidth(1);
textSommeGlobalGroupFooter.getLineBox().getTopPen().setLineWidth(1);
textSommeGlobalGroupFooter.getLineBox().getBottomPen().setLineWidth(1);
                    textSommeGlobalGroupFooter.setStretchWithOverflow(false);
                    textSommeGlobalGroupFooter.setBlankWhenNull(true);
                    textSommeGlobalGroupFooter.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
                    JRDesignExpression expressionTextSommeGlobalGroupFooter = new JRDesignExpression();
                    expressionTextSommeGlobalGroupFooter.setValueClass(java.lang.String.class);
                    JRDesignVariable variableSolde = new JRDesignVariable();
                    variableSolde.setCalculation(CalculationEnum.NOTHING);
                    JRDesignExpression expressionVariableSolde = new JRDesignExpression();  
                    expressionVariableSolde.setValueClass(java.math.BigDecimal.class);
                    expressionVariableSolde.setText("$F{montantReglemente}.setScale(3,RoundingMode.FLOOR).subtract($V{varVariableMontantFacture}.setScale(3,RoundingMode.FLOOR))");
                    variableSolde.setExpression(expressionVariableSolde);
                    variableSolde.setIncrementGroup(groupOper);
                    variableSolde.setIncrementType(IncrementTypeEnum.GROUP);
                    variableSolde.setName("varVarSolde");
                    variableSolde.setValueClass(java.math.BigDecimal.class);
                    jasperDesign.addVariable(variableSolde);
                    expressionTextSommeGlobalGroupFooter.setText("\"Total : \"+$V{varVariableMontantFacture}+\"         Solde : \"+$V{varVarSolde}");
                    textSommeGlobalGroupFooter.setExpression(expressionTextSommeGlobalGroupFooter);
                    bandGroupFooter.addElement(textSommeGlobalGroupFooter);
                }
                jasperDesign.setSummary(bandSummary);
                JRDesignBand bandFooter=new JRDesignBand();
                bandFooter.setHeight(15);
                jasperDesign.setPageFooter(bandFooter);
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
                JRDesignTextField textField = new JRDesignTextField();
                    textField.setFontSize(10);
                    textField.setX(100);
                    textField.setY(0);
                    textField.setWidth(150);
                    textField.setHeight(15);
                    textField.getLineBox().setLeftPadding(2);
                    textField.getLineBox().setRightPadding(2);
                    textField.setStretchWithOverflow(false);
                    textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
                    JRDesignExpression expression = new JRDesignExpression();  
                    expression.setValueClass(java.lang.String.class);
                    expression.setText("$V{PAGE_NUMBER}.toString()+'/'");
                    textField.setExpression(expression);
                    textField.setEvaluationTime(EvaluationTimeEnum.NOW);
                    bandFooter.addElement(textField);
                    JRDesignTextField textField2 = new JRDesignTextField();
                    textField2.setFontSize(10);
                    textField2.setX(250);
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
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,connection);
                    connection.close();
                    return jasperPrint;
            }catch(Exception egf){egf.printStackTrace();}
            return null;
        }
      public String requetteSqlRapprochementFactures(){
            try{
                StringTokenizer getUrl=new StringTokenizer(this.getFacade().urlCourante(),"**");
                String url =getUrl.nextToken();
                String login=getUrl.nextToken();
                String password =getUrl.nextToken();
                String nomBaseDeDonnes=url.substring(url.lastIndexOf("/")+1);
                List<DetailModeleSortie>dets=this.ejbDetailModeleFacade.findByParameter("Select d from DetailModeleSortie d where d.modeleSortie.idModeleSortie=:idModeleSortie","idModeleSortie",modeleChoisi.getIdModeleSortie());
                Iterator i =dets.iterator();
                String requetteSqlSelect="Select operation_com.ni_op as niReglemente,operation_com.montant_cop as montantReglemente, "
                        + "lettrage_com.MONTANT_REG as montantFacturee,"
                        + "(Select o2.num_op from operation_com o2 where o2.ni_op=lettrage_com.ni_piece) as numeroFacturee,"
                        + "(Select o3.date_sys from operation_com o3 where o3.ni_op=lettrage_com.ni_piece) as dateFacturee, "
                        + "(Select o4.facture from operation_com o4 where o4.ni_op=lettrage_com.ni_piece) as flagFacturee,"
                        + "(Select t1.nom_top from type_op_com t1 where t1.ni_top=(Select o5.ni_top from operation_com o5 where o5.ni_op=lettrage_com.ni_piece)) as typeFacturee, ";
                String requetteSqlFrom="from "+nomBaseDeDonnes+".operation_com operation_com JOIN "+nomBaseDeDonnes+".lettrage_com lettrage_com "
                        + "ON "+nomBaseDeDonnes+".operation_com.NI_OP="+nomBaseDeDonnes+".lettrage_com.ni_op "
                        + "left join "+nomBaseDeDonnes+".type_op_com type_reglement on type_reglement.ni_top=operation_com.ni_top ";
                Connection connection = DriverManager.getConnection(url,login, password);
                Statement statement = connection.createStatement();
                while(i.hasNext()){
                    DetailModeleSortie dde=(DetailModeleSortie)i.next();
                    requetteSqlSelect=requetteSqlSelect+dde.getTableColonne()+"."+dde.getNomColonne()+",";
                    if(requetteSqlFrom.contains(dde.getTableColonne())==false){
                        ResultSet rs = statement.executeQuery("SELECT COLUMN_NAME FROM information_schema.KEY_COLUMN_USAGE WHERE REFERENCED_TABLE_NAME='"+dde.getTableColonne()+"' and CONSTRAINT_SCHEMA='"+nomBaseDeDonnes+"' and CONSTRAINT_NAME!='PRIMARY' and TABLE_NAME='operation_com'");
                        String cleFkOperationCom="";
                        if (rs != null) {
                                if(rs.next()) {
                                    cleFkOperationCom=rs.getString(1);
                                }
                            }
                    rs.close();
                    if(requetteSqlFrom.toLowerCase().contains((" LEFT OUTER JOIN "+nomBaseDeDonnes+"."+dde.getTableColonne()+" "+dde.getTableColonne().toLowerCase()).toLowerCase())==false){
                        requetteSqlFrom=requetteSqlFrom+" LEFT OUTER JOIN "+nomBaseDeDonnes+"."+dde.getTableColonne()+" "+dde.getTableColonne().toLowerCase()+" ON "+nomBaseDeDonnes+".operation_com."+cleFkOperationCom+"="+nomBaseDeDonnes+"."+dde.getTableColonne().toLowerCase()+"."+dde.getClePrimTable()+" ";
                    }
                    }
                }
                if(requetteSqlSelect.endsWith(",")){
                    requetteSqlSelect=requetteSqlSelect.substring(0,requetteSqlSelect.length()-1);
                }
                String requetteSqlWhere="where ";
                if((date1 != null) && (date2 != null)){
                    this.date1.setHours(0);
                    this.date1.setMinutes(0);
                    this.date1.setSeconds(0);
                    this.date2.setHours(23);
                    this.date2.setMinutes(59);
                    this.date2.setSeconds(59);
                    if(date1.before(date2) || date1.equals(date2)){
                        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String d1=dateFormat1.format(date1);
                        String d2=dateFormat1.format(date2);
                        requetteSqlWhere=requetteSqlWhere+"operation_com.date_sys<='"+d2+"' and operation_com.date_sys>='"+d1+"' and ";
                    }
                }
                Iterator it2=this.detailsModele.iterator();
                Integer x=new Integer(0);
                while(it2.hasNext()){
                    DetailModeleSortie detModSort=(DetailModeleSortie)it2.next();
                    if(this.detailsModeleValeurFiltrage.get(x)!=null){
                        if(this.detailsModeleValeurFiltrage.get(x).isEmpty()==false){
                            if((detModSort.getTypeColonne().equals("date")==false) & (detModSort.getTypeColonne().equals("datetime")==false)){
                                requetteSqlWhere=requetteSqlWhere+detModSort.getTableColonne()+"."+detModSort.getNomColonne()+" like '%"+this.detailsModeleValeurFiltrage.get(x)+"%' and ";
                            }else{
                                try{
                                StringTokenizer sttsuak=new StringTokenizer(this.detailsModeleValeurFiltrage.get(x),"-/");
                                if(sttsuak.countTokens()==3){
                                    String f1=sttsuak.nextToken();
                                    String f2=sttsuak.nextToken();
                                    String f3=sttsuak.nextToken();
                                    String dateRecherche="";
                                    if(f1.length()==4){
                                        dateRecherche=f1+"-"+f2+"-"+f3;
                                    }else{
                                        dateRecherche=f3+"-"+f2+"-"+f1;
                                    }
                                    requetteSqlWhere=requetteSqlWhere+detModSort.getTableColonne()+"."+detModSort.getNomColonne()+" like '%"+dateRecherche+"%' and ";
                                    }else{
                                        requetteSqlWhere=requetteSqlWhere+detModSort.getTableColonne()+"."+detModSort.getNomColonne()+" like '%"+this.detailsModeleValeurFiltrage.get(x)+"%' and ";
                                    }
                                }catch(Exception exce87){
                                    exce87.printStackTrace();
                                }
                            }
                            if(requetteSqlFrom.contains(detModSort.getTableColonne())==false){
                                ResultSet rs = statement.executeQuery("SELECT COLUMN_NAME FROM information_schema.KEY_COLUMN_USAGE WHERE REFERENCED_TABLE_NAME='"+detModSort.getTableColonne()+"' and CONSTRAINT_SCHEMA='"+nomBaseDeDonnes+"' and CONSTRAINT_NAME!='PRIMARY' and TABLE_NAME='operation_com'");
                                String cleFkOperationCom="";
                                if (rs != null) {
                                        if(rs.next()) {
                                            cleFkOperationCom=rs.getString(1);
                                        }
                                }
                                rs.close();
                                requetteSqlFrom=requetteSqlFrom+" LEFT OUTER JOIN "+nomBaseDeDonnes+"."+detModSort.getTableColonne()+" "+detModSort.getTableColonne().toLowerCase()+" ON "+nomBaseDeDonnes+".operation_com."+cleFkOperationCom+"="+nomBaseDeDonnes+"."+detModSort.getTableColonne()+"."+detModSort.getClePrimTable()+" ";
                            }
                        }
                    }
                    x++;
                }
                if(requetteSqlWhere.endsWith(" and ")){
                    requetteSqlWhere=requetteSqlWhere.substring(0,requetteSqlWhere.lastIndexOf(" and "));
                }
                String typeReglementAux=typeOperationFactureAux.getNomTop();
                requetteSqlWhere=requetteSqlWhere+" and type_reglement.nom_top='"+typeReglementAux+"'";
                String orderByInstruction=" order by operation_com.ni_op,operation_com.date_sys ";
                requetteSqlWhere=requetteSqlWhere+orderByInstruction;
//                try{
//                    List<DetailModeleSortie>detOrdre=this.ejbDetailModeleFacade.findByParameter("Select d from DetailModeleSortie d where d.modeleSortie.idModeleSortie=:idModeleSortie and d.orderByInstruction="+true+" order by (d.numeroDeOrderBy)","idModeleSortie",this.modeleChoisi.getIdModeleSortie());
//                    if(detOrdre.isEmpty()==false){
//                        
//                        if(detOrdre.size()==1){
//                            orderByInstruction=orderByInstruction+"("+detOrdre.get(0).getTableColonne()+"."+detOrdre.get(0).getNomColonne()+")";
//                        }else{
//                            Iterator itDetOrdre=detOrdre.iterator();
//                            while(itDetOrdre.hasNext()){
//                                DetailModeleSortie detailOrdre=(DetailModeleSortie)itDetOrdre.next();
//                                orderByInstruction=orderByInstruction+detailOrdre.getTableColonne()+"."+detailOrdre.getNomColonne()+",";
//                            }
//                        }
//                        if(orderByInstruction.endsWith(",")){
//                            orderByInstruction=orderByInstruction.substring(0,orderByInstruction.lastIndexOf(","));
//                        }
//                        requetteSqlWhere=requetteSqlWhere+orderByInstruction;
//                    }
//                }catch(Exception e){e.printStackTrace();}
                statement.close();
                connection.close();
                return requetteSqlSelect+" "+requetteSqlFrom+" "+requetteSqlWhere;
            }catch(Exception e){e.printStackTrace();
            }
        return "";
    }
        
        public String genererListItemsRapprochementFacturesPdf(){
            try{
                    JasperPrint jasperPrint = this.genererRapprochementFactures();
                    DateFormat dateFormat1 = new SimpleDateFormat("dd MM yyyy HH mm ss");
                    Date d=new Date();
                    String dd=dateFormat1.format(d);
                    String nomFichier="Rapport "+this.modeleChoisi.getCodeModeleSortie()+".pdf";
                    byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
                    FacesContext context = FacesContext.getCurrentInstance();
                    HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
                    HttpServletResponse response = (HttpServletResponse) context
                                    .getExternalContext().getResponse();
    response.addHeader("Content-disposition","attachment;filename="+nomFichier);
                    response.setContentLength(bytes.length);
                    response.getOutputStream().write(bytes);
                    response.setContentType("application/pdf");
                    context.responseComplete();
            }catch(Exception er){
            }
            return "";
        }
    public String genererListItemsRapprochementFacturesExcel(){
        try{
                JasperPrint jasperPrint = this.genererRapprochementFactures();
                Date d=new Date();
                String nomFichier="Rapport "+this.modeleChoisi.getCodeModeleSortie()+" "+d.getTime()+".xls";
                OutputStream ouputStream=new FileOutputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport/")+"\\"+nomFichier));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JRXlsExporter exporterXLS = new JRXlsExporter();
                exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
                exporterXLS.exportReport();
                ouputStream.write(byteArrayOutputStream.toByteArray());
                FacesContext context = FacesContext.getCurrentInstance();
                HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition","attachment; filename="+nomFichier+".xls");
		response.setContentLength(byteArrayOutputStream.toByteArray().length);
		response.getOutputStream().write(byteArrayOutputStream.toByteArray());
		context.responseComplete();
        }catch(Exception er){
        }
        return "";
    }
    public String sendEmailRapprochementFacturesPdf(){
        try{
                JasperPrint jasperPrint = this.genererRapprochementFactures();
                Date d=new Date();
                String nomFichier="Rapport "+this.modeleChoisi.getCodeModeleSortie()+" "+d.getTime()+".pdf";
                byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
                FileOutputStream fs = new FileOutputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport")+"\\"+nomFichier));
                BufferedOutputStream bs = new BufferedOutputStream(fs);
                bs.write(bytes);
                bs.close();
                File ffile=new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport")+"\\"+nomFichier);
                Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
                Session session = Session.getInstance(props,null);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.emailSender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.emailReceiver));
			message.setSubject("Rapport Du Logiciel Horizon");
			message.setText("cet email contient le rapport : "+nomFichier);
                        Multipart mp = new MimeMultipart( );
                        MimeBodyPart mbp1 = new MimeBodyPart( );
                        mbp1.setContent(nomFichier, "text/plain");
                        mp.addBodyPart(mbp1);
                        MimeBodyPart mbp = new MimeBodyPart( );
                        mbp.setFileName(ffile.getName( ));
                        mbp.setDataHandler(new DataHandler(new FileDataSource(ffile)));
                        mp.addBodyPart(mbp);
                        message.setContent(mp);
                        Transport transport = session.getTransport("smtp");
                        transport.connect("smtp.gmail.com",this.emailSender,this.passwordEmailSender);
                        transport.sendMessage(message, message.getAllRecipients());
                        transport.close();
			JsfUtil.addSuccessMessage("mail envoyer");
        }catch(Exception er){
            JsfUtil.addErrorMessage("Veuillez Verifier Le Login Et Password");
        }
        return "";
    }
    public String sendEmailRapprochementFacturesExcel(){
        try{
                JasperPrint jasperPrint = this.genererRapprochementFactures();
                Date d=new Date();
                String nomFichier="Rapport "+this.modeleChoisi.getCodeModeleSortie()+" "+d.getTime()+".xls";
                OutputStream ouputStream=new FileOutputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport/")+"\\"+nomFichier));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                JRXlsExporter exporterXLS = new JRXlsExporter();
                exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
                exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
                exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
                exporterXLS.exportReport();
                ouputStream.write(byteArrayOutputStream.toByteArray());
                File ffile=new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport")+"\\"+nomFichier);
                Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
                Session session = Session.getInstance(props,null);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(this.emailSender));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.emailReceiver));
			message.setSubject("Rapport Du Logiciel Horizon");
			message.setText("cet email contient le rapport : "+nomFichier);
                        Multipart mp = new MimeMultipart( );
                        MimeBodyPart mbp1 = new MimeBodyPart( );
                        mbp1.setContent(nomFichier, "text/plain");
                        mp.addBodyPart(mbp1);
                        MimeBodyPart mbp = new MimeBodyPart( );
                        mbp.setFileName(ffile.getName( ));
                        mbp.setDataHandler(new DataHandler(new FileDataSource(ffile)));
                        mp.addBodyPart(mbp);
                        message.setContent(mp);
                        Transport transport = session.getTransport("smtp");
                        transport.connect("smtp.gmail.com",this.emailSender,this.passwordEmailSender);
                        transport.sendMessage(message, message.getAllRecipients());
                        transport.close();
			JsfUtil.addSuccessMessage("mail envoyer");
        }catch(Exception er){
            JsfUtil.addErrorMessage("Veuillez Verifier Le Login Et Password");
        }
        return "";
    }
   
    public class CustomComparator implements Comparator<DetailModeleSortie> {
    @Override
    public int compare(DetailModeleSortie o1, DetailModeleSortie o2) {
//
    return ordreColonne(o1.getNomColonne())-  ordreColonne(o2.getNomColonne()); 
    }
}

}