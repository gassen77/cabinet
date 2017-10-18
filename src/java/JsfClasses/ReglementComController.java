package JsfClasses;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.GregorianCalendar;
import entities.Banque;
import entities.LettrageCom;
import entities.Modereglement;
import entities.Operation;
import entities.Client;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import JsfClasses.util.JsfUtil;
import JsfClasses.util.PaginationHelper;
import net.sf.jasperreports.engine.JRException;
import SessionBeans.OperationFacade;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.transaction.UserTransaction;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.inputtext.InputText;
import SessionBeans.BanqueFacade;
import SessionBeans.LettrageComFacade;
import SessionBeans.ModereglementFacade;
import SessionBeans.ClientFacade;
import com.mysql.jdbc.Driver;
import entities.Cabinet;
import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRAlignment;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRExpression;
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
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.primefaces.component.calendar.Calendar;
import org.primefaces.component.dialog.Dialog;
import org.primefaces.model.DualListModel;
import SessionBeans.CabinetFacade;
import SessionBeans.TypeoperationFacade;
import SessionBeans.UserFacade;
import entities.TableOperationM;
import entities.Typeoperation;
import entities.User;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;

@ManagedBean(name = "reglementComController")
@ViewScoped
public class ReglementComController implements Serializable {
  @ManagedProperty(value="#{authetification}")
    private Authetification authentificationBean;
    private Operation current;
    private DataModel items = null;
    @EJB
    private SessionBeans.OperationFacade ejbFacade;
    
    private PaginationHelper pagination;
    private int selectedItemIndex;
    @EJB
    private ClientFacade jpaClient;
    private Connection connection;
    private String ParamTableSql;
    private String ParamTableJava;
    private String ParamTableInterface;
    private Dialog dlgOccurences;
      private List<TableOperationM> tableOperationM;
    private TableOperationM selectedTableOperationM;
    private List<LettrageCom> lettrage = null;
    private DataTable DataTableLettrage;
    private int indexDataTableLettrage;
    private Boolean reglementzero;
     boolean msgerreur;
     @EJB
     private ModereglementFacade jpaModereglement;
     @EJB
    private SessionBeans.UserFacade ejbUser;
     @EJB
    private BanqueFacade jpaBanque;
    @EJB
    private TypeoperationFacade jpaTypeoperation;
 
    private List<Operation> listOperation;
    private List<SelectItem[]> selectNumPiece;
    private BigDecimal ResteR;
    @EJB
    private LettrageComFacade jpaLettrageCom;
    private List<BigDecimal> accompte;
    @Resource
    private UserTransaction utx = null;
    private InputText inputTextReference;
    private Boolean inputTextRecu;
    private InputText inputTextEmetteur;
    private AutoComplete inputTextBanque;
    private Calendar inputTextEcheance;
    private Integer index = 0;
    private List<Operation> ListeReglement;
    private DataTable dataTablelisteReglement;
    private List<LettrageCom> ListeLettrageAM;
    private String typeR;
    private String mesg;
    private String mesgs;
    private String nomCF;
    int Xligne = 0;
    private boolean flagNomClientF;
      private boolean flagLibelleOperation;
    private boolean flagCodeClientF;
    private boolean flagPointVente;
    private boolean flagCaissier;
    private boolean flagMontantEspece;
    private boolean flagMontantCheque;
    private boolean flagMontantTraite;
    private boolean flagMontantVirement;
    private boolean flagDate;
    private boolean flagBanque;
    private boolean flagRefCheque;
    private boolean flagecheanceCheque;
    private boolean affichetabreg;
    private Banque currentBanque;
    private Client currentTiers;
    private Modereglement currentModeReglement;
    private Client filtreTiersEtat;
    private Date dateDebut;
    private Date dateFin;
    private Date dateRegAutoDebut;
    private Date dateRegAutoFin;
    private Operation selectedReg;
    private String optionOrient;
    private Date date1;
    private Date date2;
    private String pointDeVenteListFiltrage;
    private String clientListFiltrage;
    private String codeclientListFiltrage;
    private BigDecimal montantTtcListFiltrage;
    private String modeRegListFiltrage;
    private String emailSender;
    private String emailReceiver;
    private String passwordEmailSender;
    private Connection connection1;
    private Driver monDriver;
    private DataTable dataTabledetailLettrage;
    private String page;
    private DualListModel<String> colonneschoisies;
    private Client cl;
    private Long numFacture;
    private String niOp;
    private BigDecimal mon;
    private boolean  changeMontantReg=false;
    private boolean reglementdirect=false;
    private BigDecimal montantfactdirect= BigDecimal.ZERO;
    private String modereglement;
    private Boolean tofacture;
    private BigDecimal sizePage;
    private BigDecimal zoom;
    private boolean flag_reste;
    private Boolean imprimPdf;
    private BigDecimal mantantTotalAncienSolde;
    private boolean regZero;
    private String typoop;
    private String bl;
    private String LibelleOccurence;
    private String ParamTableLibelle;
     private String filtreNumPiece;
     private String nompage;
    /*****Rapport******/
    private Cabinet societe;
    @EJB
    private CabinetFacade ejbCabinet;
    public Cabinet getSociete() {
        return societe;
    }

    public String getFiltreNumPiece() {
        return filtreNumPiece;
    }

    public void setFiltreNumPiece(String filtreNumPiece) {
        this.filtreNumPiece = filtreNumPiece;
    }

    public void setSociete(Cabinet societe) {
        this.societe = societe;
    }
      public TableOperationM getSelectedTableOperationM() {
        return selectedTableOperationM;
    }

    public void setSelectedTableOperationM(TableOperationM selectedTableOperationM) {
        this.selectedTableOperationM = selectedTableOperationM;
    }

    public List<TableOperationM> getTableOperationM() {
        return tableOperationM;
    }

    public void setTableOperationM(List<TableOperationM> tableOperationM) {
        this.tableOperationM = tableOperationM;
    }

    /***********************/
    
    public boolean isFlag_reste() {
        return flag_reste;
    }
    
       public boolean isMsgerreur() {
        return msgerreur;
    }

    public void setMsgerreur(boolean msgerreur) {
        this.msgerreur = msgerreur;
    }


    public void setFlag_reste(boolean flag_reste) {
        this.flag_reste = flag_reste;
    }

    public String getBl() {
        return bl;
    }

    public void setBl(String bl) {
        this.bl = bl;
    }

    public boolean isRegZero() {
        return regZero;
    }

    public void setRegZero(boolean regZero) {
        this.regZero = regZero;
    }

  
    public void updatereglementzero()
    {
        if (reglementzero==false)
        {
             reglementzero=false;
        }
        else
        {
            reglementzero=true;
        }
    }

    public Boolean getReglementzero() {
        return reglementzero;
    }

    public void setReglementzero(Boolean reglementzero) {
        this.reglementzero = reglementzero;
    }
      
    public boolean isChangeMontantReg() {
        return changeMontantReg;
    }

    public void setChangeMontantReg(boolean changeMontantReg) {
        this.changeMontantReg = changeMontantReg;
    }

    public Boolean getTofacture() {
        return tofacture;
    }

    public void setTofacture(Boolean tofacture) {
        this.tofacture = tofacture;
    }

    public Boolean getImprimPdf() {
        return imprimPdf;
    }

    public void setImprimPdf(Boolean imprimPdf) {
        this.imprimPdf = imprimPdf;
    }

    
    
    public boolean isReglementdirect() {
        return reglementdirect;
    }

    public void setReglementdirect(boolean reglementdirect) {
        this.reglementdirect = reglementdirect;
    }

    public BigDecimal getMontantfactdirect() {
        return montantfactdirect;
    }

    public void setMontantfactdirect(BigDecimal montantfactdirect) {
        this.montantfactdirect = montantfactdirect;
    }

    public String getModereglement() {
        return modereglement;
    }

    public void setModereglement(String modereglement) {
        this.modereglement = modereglement;
    }
    
   
   
    @PostConstruct
    public void init() {

         try{
        bl = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("bl");
        }catch(Exception r){}
        
        page = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("page");
        try{
        current = (Operation) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("current");
        }catch(Exception r){}
        typeR = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("type");
       try{
        cl = (Client) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("client");
        }catch(Exception r){}
        try{
        mon = (BigDecimal) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("montant");
        }catch(Exception r){}
        try  {
         affichetabreg = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("affichetabreg");
         
        }catch(Exception r){}
         try  {
         numFacture = (Long) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("numFact");
         
        }
        catch(Exception r){}
         
          try {
           nompage = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("nompage");
        } catch (Exception e) {
            
        }
         try {
            LibelleOccurence = (String)  FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("LibelleOccurence");
        } catch (Exception r) {
        }
         
          try  {
         niOp = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("niOp");
         
        }
        catch(Exception r){}
          
          try  {
         typoop = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("typoop");
         
        }
        catch(Exception r){}
         
        try  {
         reglementdirect = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("reglementdirect");
         
        }catch(Exception r){}
         try  {
         montantfactdirect = (BigDecimal) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("montantfactdirect");
         
        }catch(Exception r){}
         
            try  {
         currentModeReglement = (Modereglement) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("currentModeReglement");
         
        }catch(Exception r){}
         
          try  {
         tofacture = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("tofacture");
         
        }catch(Exception r){}
            try  {
         regZero = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("regZero");
         
        }catch(Exception r){}
          try {
            imprimPdf = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("imprimPdf");
        } catch (Exception e) {
        }
         try {
               mantantTotalAncienSolde = (BigDecimal) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("mantantTotalAncienSolde");
          }
        catch (Exception e) {
        }
           try {
            ParamTableJava = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("ParamTableJava");
        } catch (Exception r) {
        }
        try {
            ParamTableInterface = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("ParamTableInterface");
        } catch (Exception r) {
        }

        try {
            ParamTableSql = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("ParamTableSql");
        } catch (Exception r) {
        }
        try {
            ParamTableLibelle = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("ParamTableLibelle");
        } catch (Exception r) {
        }
        try {
            dlgOccurences = (Dialog) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("dlgOccurences");
        } catch (Exception e) {
        }
         try {
            reglementzero = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("reglementzero");
        } catch (Exception r) {
        }
         try {
            inputTextReference = (InputText) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("inputTextReference");
        } catch (Exception r) {
        }
          
         try {
            inputTextRecu = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("inputTextRecu");
        } catch (Exception r) {
        }
          
         try {
            inputTextEmetteur = (InputText) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("inputTextEmetteur");
        } catch (Exception r) {
        } 
         try {
            inputTextBanque = (AutoComplete) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("inputTextBanque");
        } catch (Exception r) {
        } 
         try {
            inputTextEcheance = (Calendar) FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("inputTextEcheance");
        } catch (Exception r) {
        }

        
       montantfactdirect=mon;
        if ((page != null) && (typeR != null)) {
            if (page.equalsIgnoreCase("Create")) {
                initprepareCreateReglement();
            }
            if (page.equalsIgnoreCase("List")) {
                initprepareListReglement();
            }
            if (page.equalsIgnoreCase("Edit")) {
                initprepareEditReglement();
            }
     
             if (page.equalsIgnoreCase("Rapport")) {
                initprepareRapportReglement();
            }
            
               if (page.equalsIgnoreCase("RapportreglementNonsolde")) {
                initprepareRapportReglementNonsolde();
            }
        }
    }

    public String getPasswordEmailSender() {
        return passwordEmailSender;
    }

    public void setPasswordEmailSender(String passwordEmailSender) {
        this.passwordEmailSender = passwordEmailSender;
    }

    
  public String getParamTableInterface() {
        return ParamTableInterface;
    }

    public void setParamTableInterface(String ParamTableInterface) {
        this.ParamTableInterface = ParamTableInterface;
    }

    public String getParamTableJava() {
        return ParamTableJava;
    }

    public void setParamTableJava(String ParamTableJava) {
        this.ParamTableJava = ParamTableJava;
    }

    public String getParamTableLibelle() {
        return ParamTableLibelle;
    }

    public void setParamTableLibelle(String ParamTableLibelle) {
        this.ParamTableLibelle = ParamTableLibelle;
    }

    public String getParamTableSql() {
        return ParamTableSql;
    }

    public void setParamTableSql(String ParamTableSql) {
        this.ParamTableSql = ParamTableSql;
    }

    public Dialog getDlgOccurences() {
        return dlgOccurences;
    }

    public void setDlgOccurences(Dialog dlgOccurences) {
        this.dlgOccurences = dlgOccurences;
    }
 

   
    public DataTable getDataTabledetailLettrage() {
        return dataTabledetailLettrage;
    }

    public boolean isAffichetabreg() {
        return affichetabreg;
    }

    public void setAffichetabreg(boolean affichetabreg) {
        this.affichetabreg = affichetabreg;
    }

    public Client getCl() {
        return cl;
    }

    public void setCl(Client cl) {
        this.cl = cl;
    }

    public BigDecimal getMon() {
        return mon;
    }

    public void setMon(BigDecimal mon) {
        this.mon = mon;
    }

    public void setDataTabledetailLettrage(DataTable dataTabledetailLettrage) {
        this.dataTabledetailLettrage = dataTabledetailLettrage;
    }

    public boolean isFlagCaissier() {
        return flagCaissier;
    }

    public void setFlagCaissier(boolean flagCaissier) {
        this.flagCaissier = flagCaissier;
    }

    public boolean isFlagPointVente() {
        return flagPointVente;
    }

    public void setFlagPointVente(boolean flagPointVente) {
        this.flagPointVente = flagPointVente;
    }

   
    public Client getFiltreTiersEtat() {
        return filtreTiersEtat;
    }

    public void setFiltreTiersEtat(Client filtreTiersEtat) {
        this.filtreTiersEtat = filtreTiersEtat;
    }

    public String getEmailReceiver() {
        return emailReceiver;
    }

    public Date getDateRegAutoDebut() {
        return dateRegAutoDebut;
    }

    public void setDateRegAutoDebut(Date dateRegAutoDebut) {
        this.dateRegAutoDebut = dateRegAutoDebut;
    }

    public Date getDateRegAutoFin() {
        return dateRegAutoFin;
    }

    public void setDateRegAutoFin(Date dateRegAutoFin) {
        this.dateRegAutoFin = dateRegAutoFin;
    }

    public void setEmailReceiver(String emailReceiver) {
        this.emailReceiver = emailReceiver;
    }

    
      public String getLibelleOccurence() {
        return LibelleOccurence;
    }

    public void setLibelleOccurence(String LibelleOccurence) {
        this.LibelleOccurence = LibelleOccurence;
    }

    public String getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(String emailSender) {
        this.emailSender = emailSender;
    }

    public String getModeRegListFiltrage() {
        return modeRegListFiltrage;
    }

    public void setModeRegListFiltrage(String modeRegListFiltrage) {
        this.modeRegListFiltrage = modeRegListFiltrage;
    }

    public BigDecimal getMontantTtcListFiltrage() {
        return montantTtcListFiltrage;
    }

    public void setMontantTtcListFiltrage(BigDecimal montantTtcListFiltrage) {
        this.montantTtcListFiltrage = montantTtcListFiltrage;
    }

    public String getClientListFiltrage() {
        return clientListFiltrage;
    }

    public void setClientListFiltrage(String clientListFiltrage) {
        this.clientListFiltrage = clientListFiltrage;
    }

    public String getCodeclientListFiltrage() {
        return codeclientListFiltrage;
    }

    public void setCodeclientListFiltrage(String codeclientListFiltrage) {
        this.codeclientListFiltrage = codeclientListFiltrage;
    }

    public String getPointDeVenteListFiltrage() {
        return pointDeVenteListFiltrage;
    }

    public void setPointDeVenteListFiltrage(String pointDeVenteListFiltrage) {
        this.pointDeVenteListFiltrage = pointDeVenteListFiltrage;
    }

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

    public Operation getSelectedReg() {
        return selectedReg;
    }

    public void setSelectedReg(Operation selectedReg) {
        this.selectedReg = selectedReg;
    }

    public String getOptionOrient() {
        return optionOrient;
    }

    public void setOptionOrient(String optionOrient) {
        this.optionOrient = optionOrient;
    }

  
    public Modereglement getCurrentModeReglement() {
        return currentModeReglement;
    }

    public void setCurrentModeReglement(Modereglement currentModeReglement) {
        this.currentModeReglement = currentModeReglement;
    }

  
   

    public Client getCurrentTiers() {
        return currentTiers;
    }

    public void setCurrentTiers(Client currentTiers) {
        this.currentTiers = currentTiers;
    }

    public Banque getCurrentBanque() {
        return currentBanque;
    }

    public void setCurrentBanque(Banque currentBanque) {
        this.currentBanque = currentBanque;
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

    public boolean isFlagBanque() {
        return flagBanque;
    }

    public void setFlagBanque(boolean flagBanque) {
        this.flagBanque = flagBanque;
    }

    public boolean isFlagRefCheque() {
        return flagRefCheque;
    }

    public void setFlagRefCheque(boolean flagRefCheque) {
        this.flagRefCheque = flagRefCheque;
    }

    public boolean isFlagecheanceCheque() {
        return flagecheanceCheque;
    }

    public void setFlagecheanceCheque(boolean flagecheanceCheque) {
        this.flagecheanceCheque = flagecheanceCheque;
    }

    public int getXligne() {
        return Xligne;
    }

    public void setXligne(int Xligne) {
        this.Xligne = Xligne;
    }

    public boolean isFlagCodeClientF() {
        return flagCodeClientF;
    }

    public void setFlagCodeClientF(boolean flagCodeClientF) {
        this.flagCodeClientF = flagCodeClientF;
    }

    public boolean isFlagDate() {
        return flagDate;
    }

    public void setFlagDate(boolean flagDate) {
        this.flagDate = flagDate;
    }

    public boolean isFlagMontantCheque() {
        return flagMontantCheque;
    }

    public void setFlagMontantCheque(boolean flagMontantCheque) {
        this.flagMontantCheque = flagMontantCheque;
    }

    public boolean isFlagMontantEspece() {
        return flagMontantEspece;
    }

    public void setFlagMontantEspece(boolean flagMontantEspece) {
        this.flagMontantEspece = flagMontantEspece;
    }

    public boolean isFlagMontantTraite() {
        return flagMontantTraite;
    }

    public void setFlagMontantTraite(boolean flagMontantTraite) {
        this.flagMontantTraite = flagMontantTraite;
    }
 public BigDecimal getMantantTotalAncienSolde() {
        return mantantTotalAncienSolde;
    }

    public void setMantantTotalAncienSolde(BigDecimal mantantTotalAncienSolde) {
        this.mantantTotalAncienSolde = mantantTotalAncienSolde;
    }
    public boolean isFlagMontantVirement() {
        return flagMontantVirement;
    }

    public void setFlagMontantVirement(boolean flagMontantVirement) {
        this.flagMontantVirement = flagMontantVirement;
    }

    public boolean isFlagLibelleOperation() {
        return flagLibelleOperation;
    }

    public void setFlagLibelleOperation(boolean flagLibelleOperation) {
        this.flagLibelleOperation = flagLibelleOperation;
    }

    public boolean isFlagNomClientF() {
        return flagNomClientF;
    }

    public void setFlagNomClientF(boolean flagNomClientF) {
        this.flagNomClientF = flagNomClientF;
    }

    public void etablirconnection() throws SQLException {
        monDriver = new com.mysql.jdbc.Driver();
        StringTokenizer getUrl = new StringTokenizer(this.ejbFacade.urlCourante(), "**");
        String url = getUrl.nextToken();
        String nomBaseDeDonnes = url.substring(url.lastIndexOf("/") + 1);
        String login = getUrl.nextToken();
        String password = getUrl.nextToken();
        DriverManager.registerDriver(monDriver);
        connection1 = DriverManager.getConnection(url, login, password);
    }

    public String getNomCF() {
        return nomCF;
    }

    public void setNomCF(String nomCF) {
        this.nomCF = nomCF;
    }

   

    public String getType() {
        return typeR;
    }

    public void setType(String typeR) {
        this.typeR = typeR;
    }

    public Authetification getAuthentificationBean() {
        return authentificationBean;
    }

    public void setAuthentificationBean(Authetification authentificationBean) {
        this.authentificationBean = authentificationBean;
    }

    public String getMesg() {
        return mesg;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }

    public String getMesgs() {
        return mesgs;
    }

    public void setMesgs(String mesgs) {
        this.mesgs = mesgs;
    }
  public void ajoutOccurence(String nomtablelibelle, String nomtableinterface, String nomtablejava, String nomtablesql) {
        ParamTableLibelle = nomtablelibelle;
        ParamTableInterface = nomtableinterface;
        ParamTableJava = nomtablejava;
        ParamTableSql = nomtablesql;
        dlgOccurences.setVisible(true);
    }
    /*******************************************************************************/
    public List<LettrageCom> getListeLettrageAM() {
        return ListeLettrageAM;
    }

    public void setListeLettrageAM(List<LettrageCom> ListeLettrageAM) {
        this.ListeLettrageAM = ListeLettrageAM;
    }

    public DataTable getDataTablelisteReglement() {
        return dataTablelisteReglement;
    }

    public void setDataTablelisteReglement(DataTable dataTablelisteReglement) {
        this.dataTablelisteReglement = dataTablelisteReglement;
    }

    public List<Operation> getListeReglement() {
        return ListeReglement;
    }

    public void setListeReglement(List<Operation> ListeReglement) {
        this.ListeReglement = ListeReglement;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public AutoComplete getInputTextBanque() {
        return inputTextBanque;
    }

    public void setInputTextBanque(AutoComplete inputTextBanque) {
        this.inputTextBanque = inputTextBanque;
    }

    public Calendar getInputTextEcheance() {
        return inputTextEcheance;
    }

    public void setInputTextEcheance(Calendar inputTextEcheance) {
        this.inputTextEcheance = inputTextEcheance;
    }

    public InputText getInputTextEmetteur() {
        return inputTextEmetteur;
    }

    public void setInputTextEmetteur(InputText inputTextEmetteur) {
        this.inputTextEmetteur = inputTextEmetteur;
    }

    public Boolean getInputTextRecu() {
        return inputTextRecu;
    }

    public void setInputTextRecu(Boolean inputTextRecu) {
        this.inputTextRecu = inputTextRecu;
    }

    public InputText getInputTextReference() {
        return inputTextReference;
    }

    public void setInputTextReference(InputText inputTextReference) {
        this.inputTextReference = inputTextReference;
    }

   
  

    public List<BigDecimal> getAccompte() {
        return accompte;
    }

    public void setAccompte(List<BigDecimal> accompte) {
        this.accompte = accompte;
    }

    public BigDecimal getResteR() {
        return ResteR;
    }

    public void setResteR(BigDecimal ResteR) {
        this.ResteR = ResteR;
    }

    public List<SelectItem[]> getSelectNumPiece() {
        return selectNumPiece;
    }

    public void setSelectNumPiece(List<SelectItem[]> selectNumPiece) {
        this.selectNumPiece = selectNumPiece;
    }

   

    public DataTable getDataTableLettrage() {
        return DataTableLettrage;
    }

    public void setDataTableLettrage(DataTable DataTableLettrage) {
        this.DataTableLettrage = DataTableLettrage;
    }

    public int getIndexDataTableLettrage() {
        return indexDataTableLettrage;
    }

    public void setIndexDataTableLettrage(int indexDataTableLettrage) {
        this.indexDataTableLettrage = indexDataTableLettrage;
    }

    public DualListModel<String> getColonneschoisies() {
        return colonneschoisies;
    }

    public void setColonneschoisies(DualListModel<String> colonneschoisies) {
        this.colonneschoisies = colonneschoisies;
    }

    public List<LettrageCom> getLettrage() {
        return lettrage;
    }

    public void setLettrage(List<LettrageCom> lettrage) {
        this.lettrage = lettrage;
    }

 

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /*******************************************************************************/
    public ReglementComController() {
          
        FacesContext facesContext = FacesContext.getCurrentInstance();
      try{
         ejbCabinet = (CabinetFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "CabinetJpa"); 
         jpaTypeoperation= (TypeoperationFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "TypeoperationJpa");
         ejbFacade = (OperationFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "OperationJpa"); 
         jpaClient = (ClientFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "ClientJpa"); 
         jpaBanque = (BanqueFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "banqueJpa"); 
         jpaModereglement = (ModereglementFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "ModereglementJpa"); 
         jpaLettrageCom = (LettrageComFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "lettrageComJpa"); 
        ejbUser = (UserFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "UserJpa");

         
   

   
         
      
      }catch(Exception e){}  
    }

    public void chercherListeReg() {
        ListeReglement = new ArrayList<Operation>();
            date1.setHours(0);
            date1.setMinutes(0);
            date1.setSeconds(0);
            date2.setHours(23);
            date2.setMinutes(59);
            date2.setSeconds(59);
        recreateModel();
        String q = "Select c from Operation c where c.typeoperation=:type and  c.dateoperation>=:date1 and c.dateoperation<=:date2  Order by c.dateoperation";
        ListeReglement = (List<Operation>) this.ejbFacade.execCommandeList3Param(q, "type", getLikeTypeOPComBylibelleReglement(typeR), "date1", date1, "date2", date2);

        items = new ListDataModel(ListeReglement);

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
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findAll());

                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Operation) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String nReg() {

        return current.getNumerooperation().toString();
    }

    public String prepareCreate() {
        current = new Operation();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {

            getFacade().create(current);
            JsfUtil.addSuccessMessage("Transaction reussi");
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Transaction echouee");
            return null;
        }
    }

    public String prepareEdit() {
        current = (Operation) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
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

//    @FacesConverter(forClass = Operation.class)
//    public static class OperationControllerConverter implements Converter {
//
//        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
//            if (value == null || value.length() == 0) {
//                return null;
//            }
//          try{  ReglementComController controller = (ReglementComController) facesContext.getApplication().getELResolver().
//                    getValue(facesContext.getELContext(), null, "reglementComController");
//            return controller.ejbFacade.find(getKey(value));}catch(Exception e){return null;}
//        }
//
//        java.math.BigDecimal getKey(String value) {
//            java.math.BigDecimal key;
//            key = new java.math.BigDecimal(value);
//            return key;
//        }
//
//        String getStringKey(java.lang.Long value) {
//            StringBuffer sb = new StringBuffer();
//            sb.append(value);
//            return sb.toString();
//        }
//
//        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
//            if (object == null) {
//                return null;
//            }
//            if (object instanceof Operation) {
//                Operation o = (Operation) object;
//                return getStringKey(o.getId());
//            } else {
//                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ReglementComController.class.getName());
//            }
//        }
//    }

    /**************************prepareCreateReglement*******************************************/
    public String acceePrepareCreateReglement(String type) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Authetification userController = (Authetification) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "authetification");
            userController.refraichirEtatBouton("reglement " + type);
        } catch (Exception rtt) {
            return "";
        }
        typeR = new String();
        typeR = type;
        if (typeR.equals("client")) {
            mesg = "Règlement Client";
            mesgs = "Règlements Clients";
            nomCF = "client";
        } 
        return prepareCreateReglement();
    }

    public String prepareCreateReglement() {
       
        try {


            StringTokenizer getUrl = new StringTokenizer(this.ejbFacade.urlCourante(), "**");
            String url = getUrl.nextToken();
            String login = getUrl.nextToken();
            String password = getUrl.nextToken();
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
        }
        tableOperationM = new ArrayList<TableOperationM>();
        inputTextReference = new InputText();
       // inputTextRecu = new InputText();
        inputTextEmetteur = new InputText();
        inputTextBanque = new AutoComplete();
        inputTextEcheance = new Calendar();
        selectedTableOperationM = new TableOperationM();
        inputTextReference.setDisabled(true);
        inputTextRecu=true;
        inputTextEmetteur.setDisabled(true);
        inputTextBanque.setDisabled(true);
        inputTextEcheance.setDisabled(true);

       Modereglement m = jpaModereglement.execCommandeSansParam("Select c from Modereglement c where c.libelle='ESPECE'");
        currentBanque = new Banque();
        currentBanque.setLibelle("");
        currentModeReglement = m;
        dateRegAutoDebut = new Date();
        dateRegAutoFin = new Date();       
        currentTiers = new Client();
        listOperation = new LinkedList<Operation>();
        this.selectNumPiece = new ArrayList<SelectItem[]>();
        ResteR = new BigDecimal(0).setScale(3, RoundingMode.UP);
        current = new Operation();
     //   this.current.setImpaye("Non");
        this.current.setMontantoperation(new BigDecimal(0).setScale(3, RoundingMode.UP));
        this.current.setClient(new Client());    
        this.current.setModereglement(new Modereglement());
        this.current.setDateSys(new Date());
          this.current.setDateoperation(new Date());
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
           Date date = new Date();
            System.out.println(dateFormat.format(date));
            this.current.getDateoperation().setHours(date.getHours());
            this.current.getDateoperation().setMinutes(date.getMinutes());
            this.current.getDateoperation().setSeconds(date.getSeconds());
        this.current.setDateechenace(new Date());
        this.current.setTypeoperation(getLikeTypeOPComBylibelleReglement(typeR));
        DataTableLettrage = new DataTable();
        dataTabledetailLettrage = new DataTable();
        indexDataTableLettrage = DataTableLettrage.getRowIndex();
        this.lettrage = new ArrayList<LettrageCom>();
        accompte = new ArrayList<BigDecimal>();
        int i = 0;
        DataTableLettrage.setRendered(false);
        return "reglement_create";
    }

    /**************************prepareListReglement*******************************************/
    public String acceePrepareListReglement(String type) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Authetification userController = (Authetification) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "authetification");
         
            userController.refraichirEtatBouton("reglement " + type);
        } catch (Exception rtt) {
            return "";
        }
        typeR = new String();
        typeR = type;
       if (typeR.equals("client")) {
            mesg = "Règlement Client";
            mesgs = "Règlements Clients";
            nomCF = "client";
        } 
        return prepareListReglement();
    }

    public String prepareListReglement() {

        dataTablelisteReglement = new DataTable();
        date1 = new Date();
        date2 = new Date();
        ListeReglement = new ArrayList<Operation>();
        String q = "Select c from Operation c where c.typeoperation=:type and c.dateoperation BETWEEN :date1 and :date2";
        ListeReglement = (List<Operation>) this.ejbFacade.execCommandeList3Param(q, "type", getLikeTypeOPComBylibelleReglement(typeR), "date1", date1, "date2", date2);

        items = new ListDataModel(ListeReglement);
        return "reglement_list";
    }

    /**************************prepareEditReglement*******************************************/
    public String prepareEditReglementtest() {
        DataTableLettrage = new DataTable();
        current = (Operation) dataTablelisteReglement.getRowData();
        dataTabledetailLettrage = new DataTable();
        return "reglement_edit";
    }
/***********************************reglement*********************************************/
      
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
    
    public String createReglement() {
          //  System.out.println("user "+authentificationBean.getUser().getLogin());
           typeR="client";
            System.out.println("fct createReglement");
            System.out.println("msgerreur "+msgerreur);
        System.out.println("typeR "+typeR);
        
       
        try {
        
            utx.begin();
         User userr = new User();
            try {
                userr = ejbUser.findByParameterSingleResult("Select u from User u where u.login=:login", "login", "gassen");
                System.out.println("userr " + userr.getLogin());
            } catch (Exception e) {
            }
            current.setUser(userr);
        current.setModereglement(currentModeReglement);
        current.setClient(currentTiers);
        dlgOccurences.setVisible(false);
     
        
        if(currentBanque !=null)
        {
     if (currentBanque.getLibelle().equalsIgnoreCase("")) {
        } else {
            current.setBanque(currentBanque);
        }
        }
      
            Exception transactionException = null;
            BigDecimal un = new BigDecimal(1);
            
             Long l = new Long(1);
               Long l1 = new Long(1);
            try {
                l = ejbFacade.findByParameterSingleResultCountsansparam("Select max(c.numerooperation) from Operation c");
                 l1 = ejbFacade.findByParameterSingleResultCountsansparam("Select max(c.id) from Operation c");
                 
                if (l == null) {
                    l = new Long(1);
                }
                  if (l1 == null) {
                    l1 = new Long(1);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            System.out.println("l max numerooperation"+l);
             System.out.println("l1 max operation"+l1);
            try {
                this.current.setNumerooperation(l+1);
            } catch (Exception e) {
                this.current.setNumerooperation(new Long(1));
            }
            try {
               
                this.current.setId(l1+1);
                 System.out.println("id : "+current.getId());
            } catch (Exception e) {
                this.current.setId(l1);
            }
          
              if(current.getNumerooperation()!=null)
            {       DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy ");
            String ref = "";
                    if (current.getReference() != null) {
                        ref = current.getReference();
                    }
                    String banque = "";
                    if (current.getBanque() != null) {
                        banque = current.getBanque().getLibelle();
                    }
         
                    if (current.getModereglement().getLibelle().trim().equalsIgnoreCase("cheque")) {
                        current.setLibelleoperation("REG CHEQUE  R :" + ref + "   " + banque + "   E: " + dateFormat.format(current.getDateechenace()));
                    
                    }
                    if (current.getModereglement().getLibelle().trim().equalsIgnoreCase("traite")) {
                        current.setLibelleoperation("REG TRAITE  R :" + ref + "   " + banque + "   E: " + dateFormat.format(current.getDateechenace()));
                
                    }

                    if (((current.getModereglement().getLibelle().trim().equalsIgnoreCase("cheque")) == false) && ((current.getModereglement().getLibelle().equalsIgnoreCase("traite"))) == false) {
                        current.setLibelleoperation("REG " + current.getModereglement().getLibelle().toUpperCase());
                   
               
                    }
            }

            if (current.getModereglement().getLibelle().trim().equalsIgnoreCase("espece")) {
                current.setMontantespece(current.getMontantoperation());
                current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
            if (current.getModereglement().getLibelle().trim().equalsIgnoreCase("cheque")) {
                current.setMontantcheque(current.getMontantoperation());
                current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
            if (current.getModereglement().getLibelle().trim().equalsIgnoreCase("traite")) {
                current.setMontanttraite(current.getMontantoperation());
                current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
            if (current.getModereglement().getLibelle().trim().equalsIgnoreCase("virement")) {
                current.setMontantvirement(current.getMontantoperation());
                current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
           current.setMontantcredite(current.getMontantoperation());
           current.setMontantdebite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            getFacade().create(current);

            Iterator it = lettrage.iterator();
            while (it.hasNext()) {
                LettrageCom pp = (LettrageCom) it.next();
                if (pp.getSoldePiece() == null || pp.getSoldePiece().compareTo(new BigDecimal(0)) == 0) {
                } else {
                    BigDecimal falg = pp.getSoldePiece();
                    pp.setSoldePiece(pp.getMontantReg().subtract(pp.getSoldePiece()));
                    pp.setMontantReg(falg);
                    try {
                      pp.setNiLet(maxNiLET());
                    } catch (Exception e) {
                        pp.setNiLet(new Long(1));
                    }

                    pp.setNumPiece(pp.getNiPiece().getNumerooperation());

                 
                    //  TypeOpCom t=new TypeOpCom();t.setNiTop(new Long(1));
                    pp.setTypeOPL(getLikeTypeOPComBylibelleReglement(typeR));

                    pp.setNiOp(current);
                    System.out.println("pp.getTypeOPL "+pp.getTypeOPL());
                    System.out.println("pp.getNiOp "+pp.getNiOp());
                    System.out.println("num piece " +pp.getNumPiece());
                    jpaLettrageCom.create(pp);
                   String ql = "Select c from LettrageCom c where c.niPiece=:n";
                    List<LettrageCom> listLettrage = (List<LettrageCom>) this.jpaLettrageCom.execCommandeList(ql, "n", pp.getNiPiece());
                    BigDecimal mantantRegAFF = new BigDecimal(0).setScale(3, RoundingMode.UP);
                    Iterator it4 = listLettrage.iterator();
                    while (it4.hasNext()) {
                        LettrageCom pp12 = (LettrageCom) it4.next();
                        if (pp12.getNiPiece().equals(pp.getNiPiece())) {
                            System.out.println("pp.getNiPiece() "+pp.getNiPiece());
                            System.out.println("pp12.getNiPiece() "+pp12.getNiPiece());
                            mantantRegAFF = mantantRegAFF.add(pp12.getMontantReg());
                            System.out.println("mantantRegAFF while"+mantantRegAFF);
                        }
                    }
                    System.out.print("mantantRegAFF" + mantantRegAFF);
                  //  pp.getNiPiece().setAssiette0(mantantRegAFF);
                    ejbFacade.edit(pp.getNiPiece());
                }


            }


                utx.commit();
              JsfUtil.addSuccessMessage("Transaction réussie");
        }
             catch (Exception e) {
               msgerreur = true;
         e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception ex) {
                       ex.printStackTrace();
            }
              try {
           } catch (Exception rg) {
            }
            JsfUtil.addErrorMessage("Transaction échouée");
      
        }
        System.out.println("fin fct create reglement");
            return acceePrepareCreateReglementFromList();
            
        
    }
    

    public String saveMasterReglement() {
       


        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {

            Exception transactionException = null;
            if (current.getModereglement().getLibelle().trim().equalsIgnoreCase("espece")) {
                current.setMontantespece(current.getMontantoperation());
                current.setMontantcheque(null);
                current.setMontanttraite(null);
                current.setMontantvirement(null);
            }
            if (current.getModereglement().getLibelle().trim().equalsIgnoreCase("cheque")) {
                current.setMontantcheque(current.getMontantoperation());
                current.setMontantespece(null);
                current.setMontanttraite(null);
                current.setMontantvirement(null);
            }
            if (current.getModereglement().getLibelle().trim().equalsIgnoreCase("traite")) {
                current.setMontanttraite(current.getMontantoperation());
                current.setMontantespece(null);
                current.setMontantcheque(null);
                current.setMontantvirement(null);
            }
            if (current.getModereglement().getLibelle().trim().equalsIgnoreCase("virement")) {
                current.setMontantvirement(current.getMontantoperation());
                current.setMontantespece(null);
                current.setMontanttraite(null);
                current.setMontantcheque(null);
            }
            current.setClient(currentTiers);
            getFacade().edit(current);


            Iterator itM = this.ListeLettrageAM.iterator();
            Iterator it = this.lettrage.iterator();

            LettrageCom ecr;
            LettrageCom ecrM;
            while (itM.hasNext()) {
                ecr = (LettrageCom) itM.next();
                jpaLettrageCom.remove(ecr);

            }

            while (it.hasNext()) {
                ecr = (LettrageCom) it.next();
                if (ecr.getSoldePiece() == null || ecr.getSoldePiece().compareTo(new BigDecimal(0)) == 0) {
                } else {

                    BigDecimal falg = ecr.getSoldePiece();
                    ecr.setSoldePiece(ecr.getMontantReg().subtract(ecr.getSoldePiece()));
                    ecr.setMontantReg(falg);
                    try {
// a changer                        ecr.setNiLet(maxNiLET());
                    } catch (Exception e) {
                        ecr.setNiLet(new Long(1));
                    }

                    ecr.setNumPiece(ecr.getNiPiece().getNumerooperation());

                   
                 
                    ecr.setTypeOPL(getLikeTypeOPComBylibelleReglement(typeR));

                    ecr.setNiOp(current);
                    jpaLettrageCom.create(ecr);
                }

            }
changeMontantReg=true;

            try {

                utx.commit();


            } catch (javax.transaction.RollbackException ex) {
      //          ex.printStackTrace();
                transactionException = ex;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Transaction réussie");
                return acceePrepareListReglementFromCreate();
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

    /**************************updateReglement*******************************************/
    public String updateReglement() {



        try {
            utx.begin();
        } catch (Exception ex) {
        }
        try {

            Exception transactionException = null;
            if (current.getModereglement().getLibelle().equalsIgnoreCase("espece")) {
                current.setMontantespece(current.getMontantoperation());
                current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
            if (current.getModereglement().getLibelle().equalsIgnoreCase("cheque")) {
                current.setMontantcheque(current.getMontantoperation());
                current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
            if (current.getModereglement().getLibelle().equalsIgnoreCase("traite")) {
                current.setMontanttraite(current.getMontantoperation());
                current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontantvirement(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
            if (current.getModereglement().getLibelle().equalsIgnoreCase("virement")) {
                current.setMontantvirement(current.getMontantoperation());
                current.setMontantespece(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontanttraite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
                current.setMontantcheque(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            }
            current.setMontantcredite(current.getMontantoperation());
            current.setMontantdebite(BigDecimal.ZERO.setScale(3, RoundingMode.UP));
            current.setClient(currentTiers);
            getFacade().edit(current);


            Iterator itM = this.ListeLettrageAM.iterator();
            Iterator it = this.lettrage.iterator();

            LettrageCom ecr;
            LettrageCom ecrM;
        
            if(changeMontantReg==false)   
            { 
              
                while (itM.hasNext()) {
                ecr = (LettrageCom) itM.next();
                jpaLettrageCom.remove(ecr);

            }}

            while (it.hasNext()) {
                ecr = (LettrageCom) it.next();
                if (ecr.getSoldePiece() == null || ecr.getSoldePiece().compareTo(new BigDecimal(0)) == 0) {
                } else {

                    BigDecimal falg = ecr.getSoldePiece();
                    ecr.setSoldePiece(ecr.getMontantReg().subtract(ecr.getSoldePiece()));
                    ecr.setMontantReg(falg);
                    try {
                    ecr.setNiLet(maxNiLET());
                    } catch (Exception e) {
                        ecr.setNiLet(new Long(1));
                    }

                    ecr.setNumPiece(ecr.getNiPiece().getNumerooperation());

                 
                    ecr.setTypeOPL(getLikeTypeOPComBylibelleReglement(typeR));

                    ecr.setNiOp(current);
                    jpaLettrageCom.create(ecr);
                }

            }

  if(current.getNumerooperation()!=null)
            {       DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy ");
            String ref = "";
                    if (current.getReference() != null) {
                        ref = current.getReference();
                    }
                    String banque = "";
                    if (current.getBanque() != null) {
                        banque = current.getBanque().getLibelle();
                    }
         
                    if (current.getModereglement().getLibelle().trim().equalsIgnoreCase("cheque")) {
                        current.setLibelleoperation("REG CHEQUE  R :" + ref + "   " + banque + "   E: " + dateFormat.format(current.getDateechenace()));
                    
                    }
                    if (current.getModereglement().getLibelle().trim().equalsIgnoreCase("traite")) {
                        current.setLibelleoperation("REG TRAITE  R :" + ref + "   " + banque + "   E: " + dateFormat.format(current.getDateechenace()));
                
                    }

                    if (((current.getModereglement().getLibelle().trim().equalsIgnoreCase("cheque")) == false) && ((current.getModereglement().getLibelle().equalsIgnoreCase("traite"))) == false) {
                        current.setLibelleoperation("REG " + current.getModereglement().getLibelle().toUpperCase());
                   
               
                    }
            }

            try {

                utx.commit();


            } catch (javax.transaction.RollbackException ex) {
      //          ex.printStackTrace();
                transactionException = ex;
            } catch (Exception ex) {ex.printStackTrace();
            }
            if (transactionException == null) {
                JsfUtil.addSuccessMessage("Transaction réussie");
                return acceePrepareListReglementFromCreate();
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



        /**************************destroyReglement*******************************************/
    }

    public String destroyReglement() {
        current = (Operation) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroyReglement();
        recreateModel();
        return "List";
    }

    private void performDestroyReglement() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage("Transaction réussie");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Transaction échouée");
        }
    }

    /**************************autocomplete*******************************************/
    public List<Client> autocompleteClient(String pref) {
        List<Client> result = new ArrayList<Client>();
        try {

            result = (List<Client>) getLikeTiersComByCode(pref);

            return result;
        } catch (Exception d) {
            result = new ArrayList<Client>();

            return result;
        }
    }

    public List<Client> getLikeTiersComByCode(String code) {
        String q = "";
        
        if (typeR.equals("client")) {
            q = "Select c from Client c where c.typeclient.libelle='Patient' and (c.nom like :code or c.code like :code)";
        } 

        List<Client> l = (List<Client>) this.jpaClient.execCommandeList(q, "code", code + "%");

        return l;
    }

   

    public List<Modereglement> autocompleteModeReglement(String pref) {
        List<Modereglement> result = new ArrayList<Modereglement>();
        try {

            result = (List<Modereglement>) getLikeModeReglementComComByCode(pref);

            return result;
        } catch (Exception d) {
            d.printStackTrace();
            result = new ArrayList<Modereglement>();

            return result;
        }
    }

    public List<Modereglement> getLikeModeReglementComComByCode(String code) {
        String q = "Select c from Modereglement c where c.libelle like :code";
        List<Modereglement> l = (List<Modereglement>) this.jpaModereglement.execCommandeList(q, "code", code + "%");

        return l;

    }

    public List<Banque> autocompleteBanque(String pref) {
        List<Banque> result = new ArrayList<Banque>();
        try {

            result = (List<Banque>) getLikeBanqueComComByCode(pref);

            return result;
        } catch (Exception d) {
   
            d.printStackTrace();
            result = new ArrayList<Banque>();

            return result;
        }
    }

    public List<Banque> getLikeBanqueComComByCode(String code) {
        String q = "Select c from Banque c where c.libelle like :code";
        List<Banque> l = (List<Banque>) this.jpaBanque.execCommandeList(q, "code", code + "%");

        return l;

    }

  

    public List<Typeoperation> autocompleteTypeOpCom(String pref) {
        List<Typeoperation> result = new ArrayList<Typeoperation>();

        try {
            if (typeR.equals("client")) {
                result = (List<Typeoperation>) getLikeTypeOpComComByCode(pref);
            } 

            return result;
        } catch (Exception d) {
            d.printStackTrace();
            result = new ArrayList<Typeoperation>();

            return result;
        }
    }

    public List<Operation> autocompleteOpCom(String pref) {
        List<Operation> result = new ArrayList<Operation>();
        try {
          
            result = (List<Operation>) getLikeOperationComByCode(pref);

            return result;
        } catch (Exception d) {
     
            d.printStackTrace();
            result = new ArrayList<Operation>();

            return result;
        }
    }

  
    public List<Operation> getLikeOperationComByCode(String code) {
        String q = "Select c from Operation c where c.numerooperation like :code";
        List<Operation> l = (List<Operation>) this.ejbFacade.execCommandeList(q, "code", code + "%");
        return l;
    }

    public List<Typeoperation> getLikeTypeOpComComByCode(String code) {
               String q = "Select c from Typeoperation c where c.libelle like :code and (c.libelle='facture' or c.libelle='impaye' or c.libelle='Operation debit') ";
        List<Typeoperation> l = (List<Typeoperation>) this.jpaTypeoperation.execCommandeList(q, "code", code + "%");
        return l;
    }


    public Typeoperation getLikeTypeOpComFacture() {
        String q = "Select c from Typeoperation c where c.libelle='facture'";
        Typeoperation l = (Typeoperation) this.jpaTypeoperation.execCommandeSansParam(q);
        return l;
    }


   
  


  

   

    /*****************************Methode**************************************************/
  
   
    /*******************************Ajout Auto*******************************************************/
    public void prepareAuto() {
        int i = 0;
        dlgOccurences.setVisible(false);
           dateRegAutoDebut.setHours(0);
            dateRegAutoDebut.setMinutes(0);
            dateRegAutoDebut.setSeconds(0);
            dateRegAutoFin.setHours(23);
            dateRegAutoFin.setMinutes(59);
            dateRegAutoFin.setSeconds(59);
        lettrage = new ArrayList<LettrageCom>();
        accompte = new ArrayList<BigDecimal>();
        Typeoperation ty = new Typeoperation();
        if (typeR.equals("client")) {
            ty = jpaTypeoperation.execCommandeSansParam("Select c from Typeoperation c where c.libelle='facture'");
        } 
        regZero=false;
     
            if (this.current.getMontantoperation().compareTo(new BigDecimal(0).setScale(3, RoundingMode.UP)) == 0) {
      
       regZero=true;
            
            }
              
          if(regZero)  { DataTableLettrage.setRendered(true); 
  
        ResteR=new BigDecimal(0).setScale(3, RoundingMode.UP);
        lettrage = new ArrayList<LettrageCom>();
        accompte = new ArrayList<BigDecimal>();
       // TypeOpCom ty = new TypeOpCom();
     
        String q = "Select c from Operation c where c.typeoperation=:type  and c.client=:client and c.dateoperation BETWEEN :date1 and :date2 ";

        q = q + " order by  c.dateoperation ASC ";
         List<String>ss=new ArrayList<String>();
         List<Object>oo=new ArrayList<Object>();
         ss.add("type");   
         ss.add("client");
     ss.add("date1");
         ss.add("date2");
       
         oo.add(ty);
     
         oo.add(currentTiers);
    oo.add(dateRegAutoDebut);
         oo.add(dateRegAutoFin);
        
        List<Operation> listOp = (List<Operation>) this.ejbFacade.findByParameterMultipleCreteria(q, ss, oo);
        
        Iterator it = listOp.iterator();
        while (it.hasNext()) {
            Operation op = (Operation) it.next();
            LettrageCom pp = new LettrageCom();
            pp.setNiPiece(op);
          
            pp.setTypeOPAL(ty);
            String ql = "Select c from LettrageCom c where c.niPiece=:n";
            List<LettrageCom> listLettrage2 = (List<LettrageCom>) this.jpaLettrageCom.execCommandeList(ql, "n", pp.getNiPiece());

            Iterator it1 = listLettrage2.iterator();

            BigDecimal bg = new BigDecimal(0).setScale(3, RoundingMode.UP);
            while (it1.hasNext()) {
                LettrageCom pp2 = (LettrageCom) it1.next();
                if (pp.getNiPiece().equals(pp2.getNiPiece())) {
                    bg = bg.add(pp2.getMontantReg());
                }
            }
            List<Operation> apF1 = new ArrayList<Operation>();
            apF1.add(pp.getNiPiece());
            this.selectNumPiece.add(JsfUtil.getSelectItems(apF1, true));

            BigDecimal acc = new BigDecimal(0).setScale(3, RoundingMode.UP);
            acc = bg;

            pp.setMontantPiece(op.getMontantoperation());


            BigDecimal montant = op.getMontantoperation().subtract(acc);
            pp.setMontantReg(montant);
         {
                pp.setSoldePiece(montant);
                if (montant.compareTo(new BigDecimal(0).setScale(3, RoundingMode.UP)) == 1) {
                    ResteR = ResteR.subtract(montant);
                }
            } 
            if ((pp.getMontantPiece().compareTo(bg) == 0) || (pp.getMontantPiece().compareTo(bg) == -1)) {
            } else {
                accompte.add(bg);
                lettrage.add(pp);
         
            }

        }
             current.setMontantoperation(ResteR.negate());
         ResteR=new BigDecimal(0).setScale(3, RoundingMode.UP);} 
       
          else{ ResteR=current.getMontantoperation().setScale(3, RoundingMode.UP);
        String q = "Select c from Operation c where c.typeoperation=:type  and c.client=:client and c.dateoperation BETWEEN :date1 and :date2";

        q = q + " order by  c.dateoperation ASC ";
        List<String>ss=new ArrayList<String>();
         List<Object>oo=new ArrayList<Object>();
         ss.add("type");
         ss.add("client");
         ss.add("date1");
         ss.add("date2");
        
         oo.add(ty);
       
         oo.add(currentTiers);
         oo.add(dateRegAutoDebut);
         oo.add(dateRegAutoFin);
       
        List<Operation> listOp = (List<Operation>) this.ejbFacade.findByParameterMultipleCreteria(q, ss, oo);
        
        Iterator it = listOp.iterator();
        while (it.hasNext()) {
            Operation op = (Operation) it.next();
            LettrageCom pp = new LettrageCom();
            pp.setNiPiece(op);
         
            pp.setTypeOPAL(ty);
            String ql = "Select c from LettrageCom c where c.niPiece=:n";
            List<LettrageCom> listLettrage2 = (List<LettrageCom>) this.jpaLettrageCom.execCommandeList(ql, "n", pp.getNiPiece());

            Iterator it1 = listLettrage2.iterator();

            BigDecimal bg = new BigDecimal(0).setScale(3, RoundingMode.UP);
            while (it1.hasNext()) {
                LettrageCom pp2 = (LettrageCom) it1.next();
                if (pp.getNiPiece().equals(pp2.getNiPiece())) {
                    bg = bg.add(pp2.getMontantReg());
                }
            }



            List<Operation> apF1 = new ArrayList<Operation>();
            apF1.add(pp.getNiPiece());
            this.selectNumPiece.add(JsfUtil.getSelectItems(apF1, true));

            BigDecimal acc = new BigDecimal(0).setScale(3, RoundingMode.UP);
            acc = bg;

            pp.setMontantPiece(op.getMontantoperation());
            BigDecimal montant = op.getMontantoperation().subtract(acc);
            pp.setMontantReg(montant);
            if (ResteR.compareTo(montant) == 1) {
                pp.setSoldePiece(montant);
                if (montant.compareTo(new BigDecimal(0).setScale(3, RoundingMode.UP)) == 1) {
                    ResteR = ResteR.subtract(montant);
                }
            } else {
                pp.setSoldePiece(ResteR);
                ResteR = new BigDecimal(0).setScale(3, RoundingMode.UP);
            }
            if ((pp.getMontantPiece().compareTo(bg) == 0) || (pp.getMontantPiece().compareTo(bg) == -1)) {
            } else {
                accompte.add(bg);
                lettrage.add(pp);
            }

        }
          }
        //  return null;
    }

    public void changeMantant() {
                
        filtreNumPiece="";
        flag_reste=true;
        if(reglementdirect&&current.getMontantoperation().compareTo(montantfactdirect)>0)
        {
            
  

             this.current.setMontantoperation(montantfactdirect.setScale(3, RoundingMode.UP));
    DataTableLettrage.setRendered(false);
       
        lettrage = new ArrayList<LettrageCom>();
        accompte = new ArrayList<BigDecimal>();
        boolean test = true;
        System.out.println(currentTiers);
        int i = 0;
        while (i < 5) {
            this.listOperation = new ArrayList<Operation>();

            this.selectNumPiece.add(i, JsfUtil.getSelectItems(this.listOperation, true));
            LettrageCom e = new LettrageCom();
          
            e.setNiPiece(new Operation());

            e.setTypeOPAL(new Typeoperation());
            // e.setTypeOPL(new TypeOpCom());
            lettrage.add(e);
            i++;
        }

        try {
            this.current.setMontantoperation(current.getMontantoperation().setScale(3, RoundingMode.UP));
            if ((this.currentTiers.getId() != null) == false) {
                test = false;
                JsfUtil.addErrorMessage("Champs Client vide ou invalide");
                //currentCaissier = new CaissierCom();
                this.current.setMontantoperation(new BigDecimal(0).setScale(3, RoundingMode.UP));
            }
            if ((this.currentModeReglement != null) == false) {
                test = false;
                JsfUtil.addErrorMessage("Champs Mode de Reglement vide ou invalide");
                //currentCaissier = new CaissierCom();
                this.current.setMontantoperation(new BigDecimal(0).setScale(3, RoundingMode.UP));
            }
            if (test) {
                DataTableLettrage.setRendered(true);
                lettrage = new ArrayList<LettrageCom>();
                accompte = new ArrayList<BigDecimal>();
                int K = 0;
                while (K < 5) {
                    LettrageCom e = new LettrageCom();
                    accompte.add(K, null);
                    e.setNiPiece(new Operation());
                    //  e.setTypeOPL(new TypeOpCom());
                    e.setTypeOPL(new Typeoperation());
                    lettrage.add(e);
                    K++;

                }
            }
            Typeoperation ty = new Typeoperation();
            if (typeR.equals("client")) {
                ty = jpaTypeoperation.execCommandeSansParam("Select c from Typeoperation c where c.libelle='facture'");
            } 
            lettrage.get(0).setTypeOPAL(ty);
 
            
            changeListNumPiecePardefaut();
            ResteR = current.getMontantoperation();
          
        } catch (Exception e) {
            DataTableLettrage.setRendered(false);
        }
        }
        else
        {
            System.out.println("change mantant else");
            regZero=false;
             if (this.current.getMontantoperation().compareTo(new BigDecimal(0).setScale(3, RoundingMode.UP)) == 0) {
      
       regZero=true;
            
            }
          {
  
        DataTableLettrage.setRendered(false);
       
        lettrage = new ArrayList<LettrageCom>();
        accompte = new ArrayList<BigDecimal>();
        boolean test = true;
        int i = 0;
        while (i < 5) {
            this.listOperation = new ArrayList<Operation>();

            this.selectNumPiece.add(i, JsfUtil.getSelectItems(this.listOperation, true));
            LettrageCom e = new LettrageCom();
            e.setNiPiece(new Operation());

            e.setTypeOPAL(new Typeoperation());
            lettrage.add(e);
            i++;
        }

        try {
            this.current.setMontantoperation(current.getMontantoperation().setScale(3, RoundingMode.UP));
            if ((this.currentTiers.getId() != null) == false) {
                test = false;
                JsfUtil.addErrorMessage("Champs Client vide ou invalide");
                //currentCaissier = new CaissierCom();
                this.current.setMontantoperation(new BigDecimal(0).setScale(3, RoundingMode.UP));
            }
            if ((this.currentModeReglement != null) == false) {
                test = false;
                JsfUtil.addErrorMessage("Champs Mode de Reglement vide ou invalide");
                //currentCaissier = new CaissierCom();
                this.current.setMontantoperation(new BigDecimal(0).setScale(3, RoundingMode.UP));
            }
            if (test) {
                DataTableLettrage.setRendered(true);
                lettrage = new ArrayList<LettrageCom>();
                accompte = new ArrayList<BigDecimal>();
                int K = 0;
                while (K < 5) {
                    LettrageCom e = new LettrageCom();
                    accompte.add(K, null);


                    e.setNiPiece(new Operation());
                    //  e.setTypeOPL(new TypeOpCom());
                    e.setTypeOPL(new Typeoperation());
                    lettrage.add(e);
                    K++;

                }
            }
            Typeoperation ty = new Typeoperation();
            if (typeR.equals("client")) {
                ty = jpaTypeoperation.execCommandeSansParam("Select c from Typeoperation c where c.libelle='facture'");
            } 
            lettrage.get(0).setTypeOPAL(ty);
        
            
            changeListNumPiecePardefaut();
            ResteR = current.getMontantoperation();
          
        } catch (Exception e) {
            DataTableLettrage.setRendered(false);
        }
          }
       
        
        }
    }
     public void changeMantant2() {
        DataTableLettrage.setRendered(true);
        lettrage = new ArrayList<LettrageCom>();
        accompte = new ArrayList<BigDecimal>();
        boolean test = true;
    //    System.out.println(currentTiers);
        int i = 0;
        while (i < 5) {
            this.listOperation = new ArrayList<Operation>();

            this.selectNumPiece.add(i, JsfUtil.getSelectItems(this.listOperation, true));
            LettrageCom e = new LettrageCom();
            e.setNiPiece(new Operation());
            e.setTypeOPAL(new Typeoperation());
            lettrage.add(e);
            i++;
        }

        try {
            this.current.setMontantoperation(current.getMontantoperation().setScale(3, RoundingMode.UP));
            if ((this.currentTiers.getId() != null) == false) {
                test = false;
                JsfUtil.addErrorMessage("Champs Client vide ou invalide");
               // currentCaissier = new CaissierCom();
                this.current.setMontantoperation(new BigDecimal(0).setScale(3, RoundingMode.UP));
            }
            if ((this.currentModeReglement != null) == false) {
                test = false;
                JsfUtil.addErrorMessage("Champs Mode de Reglement vide ou invalide");
                this.current.setMontantoperation(new BigDecimal(0).setScale(3, RoundingMode.UP));
            }
            if (test) {
                lettrage = new ArrayList<LettrageCom>();
                accompte = new ArrayList<BigDecimal>();
                int K = 0;
                while (K < 5) {
                    LettrageCom e = new LettrageCom();
                    accompte.add(K, null);

                    e.setNiPiece(new Operation());
                    e.setTypeOPL(new Typeoperation());
                    lettrage.add(e);
                    K++;

                }
            }
            Typeoperation ty = new Typeoperation();
            if (typeR.equals("client")) {
                ty = jpaTypeoperation.execCommandeSansParam("Select c from Typeoperation c where c.libelle='"+typoop+"'");
            } 
            lettrage.get(0).setTypeOPAL(ty);
       
                  ResteR = current.getMontantoperation();
                         
            try{    Operation DerniereFacture = ejbFacade.execCommandeSansParam("SELECT o FROM Operation o WHERE o.numerooperation ="+numFacture+"and o.typeoperation.id="+ty.getId());
            
                accompte.set(0,new BigDecimal(0).setScale(3, RoundingMode.UP));
                LettrageCom l = new LettrageCom();
         
            l.setTypeOPAL(ty);
                l.setNiPiece(DerniereFacture);
                l.setMontantReg(DerniereFacture.getMontantoperation());
                l.setMontantPiece(DerniereFacture.getMontantoperation());
                
                l.setSoldePiece(new BigDecimal(999999999).setScale(3, RoundingMode.UP));

                lettrage.set(0, l);
       
affectAff(0);
            } catch (Exception ex) {ex.printStackTrace();
              //  System.out.println("catch DerniereFacture")
                        ;}
            
     
               
        } catch (Exception e) {
                System.out.println("catch changeMantant2");
            DataTableLettrage.setRendered(false);
        }
    }

  
    public Typeoperation getLikeTypeOPComBylibelleReglement(String type) {
        String q = "";
        
           
            q =" SELECT t FROM Typeoperation t WHERE t.libelle = 'Reglement client' ";
        
        Typeoperation l = (Typeoperation) this.jpaTypeoperation.execCommandeSansParam(q);
        System.out.println("getLikeTypeOPComBylibelleReglement l : "+l.getLibelle());
        return l;
    }

    public Typeoperation getLikeTypeOPComBylibelleFacture(String type) {
        String q = "";
        if (type.equals("client")) {
            q = "Select c from Typeoperation c where c.libelle='facture'";
        } 
        Typeoperation l = (Typeoperation) this.jpaTypeoperation.execCommandeSansParam(q);

        return l;
    }

    public void changementTPVP() {


        int index = DataTableLettrage.getRowIndex();

        Iterator it2 = lettrage.iterator();
        boolean falg = false;
        int m = 0;
        while (it2.hasNext()) {
            LettrageCom pp = (LettrageCom) it2.next();
            if (pp.getNiPiece().equals(lettrage.get(index).getNiPiece())) {
                m++;
            }
        }
        if (m >= 2) {
            lettrage.get(index).setNiPiece(new Operation());
           
            String q = "Select c from Operation c where c.typeoperation=:type  and c.client=:client";
            listOperation = (List<Operation>) this.ejbFacade.execCommandeList2Param(q, "type", getLikeTypeOPComBylibelleFacture(typeR), "client", current.getClient());
            List<Operation> apF = new ArrayList<Operation>();


            Iterator it4 = listOperation.iterator();

            while (it4.hasNext()) {
                Operation op = (Operation) it4.next();
                BigDecimal mantantRegAFF = new BigDecimal(0);
                boolean mp = false;
                String ql = "Select c from LettrageCom c where c.niPiece=:n";
                List<LettrageCom> listLettrage = (List<LettrageCom>) this.jpaLettrageCom.execCommandeList(ql, "n", op);

                Iterator it3 = listLettrage.iterator();
                while (it3.hasNext()) {
                    LettrageCom pp = (LettrageCom) it3.next();
                    if (pp.getNiPiece().equals(op)) {
                        mantantRegAFF = mantantRegAFF.add(pp.getMontantReg());
                        if (op.getMontantoperation().compareTo(mantantRegAFF) == 0) {
                            mp = true;
                        }

                    }

                }
                if (mp == false) {
                    apF.add(op);
                }
            }
            this.selectNumPiece.add(index, JsfUtil.getSelectItems(apF, true));
            JsfUtil.addErrorMessage("Erreur : Numéro Facture invalide");
            // lettrage.get(index).setAccompte(null); 
            accompte.set(index, null);
            lettrage.get(index).setMontantPiece(null);
            lettrage.get(index).setMontantReg(null);
            lettrage.get(index).setSoldePiece(null);
            BigDecimal aff = new BigDecimal(0).setScale(3, RoundingMode.UP);
            Iterator it = lettrage.iterator();
            int i = 0;
            while (it.hasNext()) {
                LettrageCom pp = (LettrageCom) it.next();
                if (pp.getSoldePiece() == null) {
                } else {
                    aff = aff.add(pp.getSoldePiece().setScale(3, RoundingMode.UP));
                }
                i++;
            }
            ResteR = current.getMontantoperation().subtract(aff);

        } else {

            accompte.set(index, new BigDecimal(0).setScale(3, RoundingMode.UP));
            BigDecimal mantantRegAFF = new BigDecimal(0);
            String ql = "Select c from LettrageCom c where c.niPiece=:n";
            List<LettrageCom> listLettrage = (List<LettrageCom>) this.jpaLettrageCom.execCommandeList(ql, "n", lettrage.get(index).getNiPiece());

            Iterator it4 = listLettrage.iterator();
            while (it4.hasNext()) {
                LettrageCom pp = (LettrageCom) it4.next();
                if (pp.getNiPiece().equals(lettrage.get(index).getNiPiece())) {
                    mantantRegAFF = mantantRegAFF.add(pp.getMontantReg());
                }
            }
            accompte.set(index, mantantRegAFF.setScale(3, RoundingMode.UP));
            lettrage.get(index).setMontantPiece(lettrage.get(index).getNiPiece().getMontantoperation());
            lettrage.get(index).setSoldePiece(new BigDecimal(0).setScale(3, RoundingMode.UP));
            ResteR = new BigDecimal(0).setScale(3, RoundingMode.UP);
            BigDecimal aff = new BigDecimal(0).setScale(3, RoundingMode.UP);
            Iterator it = lettrage.iterator();
            int i = 0;
            while (it.hasNext()) {
                LettrageCom pp = (LettrageCom) it.next();
                if (pp.getSoldePiece() == null) {
                } else {
                    aff = aff.add(pp.getSoldePiece().setScale(3, RoundingMode.UP));
                }
                i++;
            }
            ResteR = current.getMontantoperation().subtract(aff);
            lettrage.get(index).setMontantReg(lettrage.get(index).getMontantPiece().setScale(3, RoundingMode.UP).subtract(accompte.get(index)));


        }



    }

    public void changementAff() {
        int index = DataTableLettrage.getRowIndex();


        {
            BigDecimal reste = new BigDecimal(0).setScale(3, RoundingMode.UP);


            BigDecimal SoldePiece = lettrage.get(index).getSoldePiece();
            lettrage.get(index).setSoldePiece(new BigDecimal(0).setScale(3, RoundingMode.UP));
            ResteR = new BigDecimal(0).setScale(3, RoundingMode.UP);
            BigDecimal aff = new BigDecimal(0).setScale(3, RoundingMode.UP);
            Iterator it = lettrage.iterator();
            int i = 0;
            while (it.hasNext()) {
                LettrageCom pp = (LettrageCom) it.next();
                if (pp.getSoldePiece() == null) {
                } else {
                    aff = aff.add(pp.getSoldePiece().setScale(3, RoundingMode.UP));
                }
               
                i++;
            }
            ResteR = current.getMontantoperation().subtract(aff);
            lettrage.get(index).setSoldePiece(SoldePiece);
            //lettrage.get(index).setAccompte(new BigDecimal(0).setScale(3, RoundingMode.UP)); 
            //  accompte.set(index,new BigDecimal(0).setScale(3, RoundingMode.UP));

            if ((ResteR.compareTo(lettrage.get(index).getMontantReg()) == 1) || (ResteR.compareTo(lettrage.get(index).getMontantReg()) == 0)) {

                if (lettrage.get(index).getSoldePiece().compareTo(lettrage.get(index).getMontantReg()) == 1) {
                    lettrage.get(index).setSoldePiece(lettrage.get(index).getMontantReg().setScale(3, RoundingMode.UP));
                }
                if (lettrage.get(index).getSoldePiece().compareTo(lettrage.get(index).getMontantReg()) == 0) {
                    lettrage.get(index).setSoldePiece(lettrage.get(index).getMontantReg().setScale(3, RoundingMode.UP));
                }
                if (lettrage.get(index).getSoldePiece().compareTo(lettrage.get(index).getMontantReg()) == -1) {
                    lettrage.get(index).setSoldePiece(lettrage.get(index).getSoldePiece().setScale(3, RoundingMode.UP));
                }
            }
            if (ResteR.compareTo(lettrage.get(index).getMontantReg()) == -1) {

                if (lettrage.get(index).getSoldePiece().compareTo(ResteR) == 1) {
                    lettrage.get(index).setSoldePiece(ResteR.setScale(3, RoundingMode.UP));
                }
                if (lettrage.get(index).getSoldePiece().compareTo(ResteR) == 0) {
                    lettrage.get(index).setSoldePiece(ResteR.setScale(3, RoundingMode.UP));
                }
                if (lettrage.get(index).getSoldePiece().compareTo(ResteR) == -1) {
                    lettrage.get(index).setSoldePiece(lettrage.get(index).getSoldePiece().setScale(3, RoundingMode.UP));
                }

            }

        }

        ResteR = new BigDecimal(0).setScale(3, RoundingMode.UP);
        BigDecimal aff = new BigDecimal(0).setScale(3, RoundingMode.UP);
        Iterator it = lettrage.iterator();
        int i = 0;
        while (it.hasNext()) {
            LettrageCom pp = (LettrageCom) it.next();
            if (pp.getSoldePiece() == null) {
            } else {
                aff = aff.add(pp.getSoldePiece().setScale(3, RoundingMode.UP));
            }
         
            i++;
        }
        ResteR = current.getMontantoperation().subtract(aff);

    }

    public void changeListNumPiece() {
        System.out.println("fct changeListNumPiece");
        filtreNumPiece="";
        dataTabledetailLettrage=new DataTable();  
        dataTabledetailLettrage.setRendered(false);
        index = DataTableLettrage.getRowIndex();
       String q = "Select c from Operation c where c.typeoperation=:type  and c.client=:client  and  (c.montantoperation!=0.000 and ((Select SUM(l1.montantReg) from LettrageCom l1 where l1.niPiece=c) is null or c.montantoperation > (Select SUM(l.montantReg) from LettrageCom l where l.niPiece=c)))";
              q = q + " order by  c.dateoperation DESC ";
        listOperation = (List<Operation>) this.ejbFacade.execCommandeList2Param(q, "type", lettrage.get(index).getTypeOPAL(), "client", currentTiers);
        System.out.println("listOperation size : "+listOperation.size());
        List<Operation> apF = new ArrayList<Operation>();
        tableOperationM = new ArrayList<TableOperationM>();
        Iterator it4 = listOperation.iterator();
     
        while (it4.hasNext()) {
         //   System.out.println("oppp");
            Operation op = (Operation) it4.next();
            if (contiensOp(lettrage, op))
            {}
            else{
          //      System.out.println("else");
            BigDecimal mantantRegAFF = new BigDecimal(0);
            boolean mp = false;
            String ql = "Select c from LettrageCom c where c.niPiece=:n";
                    String qSum="Select SUM(l.montantReg) from LettrageCom l where l.niPiece=:n";

                    
            mantantRegAFF=this.jpaLettrageCom.findByParameterSum(qSum, "n", op);
                System.out.println("mantantRegAFF "+mantantRegAFF);
                 if (mantantRegAFF==null)
            {mantantRegAFF = new BigDecimal(0);}
            if ((mp == false)) {
                apF.add(op);
                TableOperationM e = new TableOperationM();
                e.setOperation(op);
                e.setAcompte(mantantRegAFF.setScale(3, RoundingMode.UP));
                e.setMontantReglement(op.getMontantoperation().subtract(mantantRegAFF));
                tableOperationM.add(e);


            }
            }
        }
        this.selectNumPiece.add(index, JsfUtil.getSelectItems(apF, true));
        /*******************************************/
        accompte.set(index, null);
        lettrage.get(index).setMontantPiece(null);
        lettrage.get(index).setMontantReg(null);
        lettrage.get(index).setSoldePiece(null);
        BigDecimal aff = new BigDecimal(0).setScale(3, RoundingMode.UP);
        Iterator it = lettrage.iterator();
        int i = 0;
        while (it.hasNext()) {
            LettrageCom pp = (LettrageCom) it.next();
            if (pp.getSoldePiece() == null) {
            } else {
                aff = aff.add(pp.getSoldePiece().setScale(3, RoundingMode.UP));
            }
            i++;
        }
        ResteR = current.getMontantoperation().subtract(aff);
        System.out.println("ResteR "+ResteR);
        System.out.println("fin fct");
    }

    public void changeListNumPiecePardefaut() {
                dataTabledetailLettrage=new DataTable();
        filtreNumPiece="";
        filtreNumPiece="";
    try{    int index = 0;
       String q = "Select c from Operation c where c.typeoperation=:type  and c.client=:client and (c.montantoperation!=0.000 and ((Select SUM(l1.montantReg) from LettrageCom l1 where l1.niPiece=c) is null or c.montantoperation > (Select SUM(l.montantReg) from LettrageCom l where l.niPiece=c)))";
       // System.out.println(lettrage.get(index).getTypeOPAL().getNomTop());
        listOperation = (List<Operation>) this.ejbFacade.execCommandeList2Param(q, "type", lettrage.get(index).getTypeOPAL(), "client", currentTiers);
        List<Operation> apF = new ArrayList<Operation>();
        tableOperationM = new ArrayList<TableOperationM>();
        Iterator it4 = listOperation.iterator();

        while (it4.hasNext()) {
            Operation op = (Operation) it4.next();
            BigDecimal mantantRegAFF = new BigDecimal(0);
            boolean mp = false;
            String ql = "Select c from LettrageCom c where c.niPiece=:n";
            String qSum="Select SUM(l.montantReg) from LettrageCom l where l.niPiece=:n";
            mantantRegAFF=this.jpaLettrageCom.findByParameterSum(qSum, "n", op);
            if (mantantRegAFF==null)
            {mantantRegAFF = new BigDecimal(0);}
            if (mp == false) {
                apF.add(op);
                TableOperationM e = new TableOperationM();
                e.setOperation(op);
                e.setAcompte(mantantRegAFF.setScale(3, RoundingMode.UP));
                e.setMontantReglement(op.getMontantoperation().subtract(mantantRegAFF));
                tableOperationM.add(e);


            }
        }
        this.selectNumPiece.add(index, JsfUtil.getSelectItems(apF, true));
         accompte.set(index, null);
        lettrage.get(index).setMontantPiece(null);
        lettrage.get(index).setMontantReg(null);
        lettrage.get(index).setSoldePiece(null);
        BigDecimal aff = new BigDecimal(0).setScale(3, RoundingMode.UP);
        Iterator it = lettrage.iterator();
        int i = 0;
        while (it.hasNext()) {
            LettrageCom pp = (LettrageCom) it.next();
            if (pp.getSoldePiece() == null) {
            } else {
                aff = aff.add(pp.getSoldePiece().setScale(3, RoundingMode.UP));
            }
            i++;
        }
        ResteR = current.getMontantoperation().subtract(aff);}catch(Exception e){e.printStackTrace();}

    }

    public void changeClient() {

        lettrage = new ArrayList<LettrageCom>();
        accompte = new ArrayList<BigDecimal>();
        int i = 0;
      while (i < 5) {
            LettrageCom e = new LettrageCom();
            //  if (i==0)
            e.setTypeOPL(getLikeTypeOPComBylibelleFacture(typeR));
            // else
            //  e.setTypeOPL(new TypeOpCom());  
            this.listOperation = new ArrayList<Operation>();
            accompte.add(i, null);
            this.selectNumPiece.add(i, JsfUtil.getSelectItems(this.listOperation, true));

            e.setNiPiece(new Operation());
            e.setTypeOPL(new Typeoperation());
            lettrage.add(e);
            i++;
        }

changeRefChequeTraiteVirement();
changeRefChequeTraiteVirementModif();
    }

    public void ajouteLettrage() {
        int index = DataTableLettrage.getRowIndex();
        this.listOperation = new ArrayList<Operation>();
        this.selectNumPiece.add(index, JsfUtil.getSelectItems(this.listOperation, true));
        LettrageCom e = new LettrageCom();
        accompte.add(index, null);
     
        e.setNiOp(new Operation());
        e.setTypeOPL(new Typeoperation());
        lettrage.add(index, e);

    }

    public void supprimerLettrage() {
        int index = DataTableLettrage.getRowIndex();
        LettrageCom l = lettrage.get(index);
       
        if(regZero)
        {if (l.getSoldePiece()!=null)
            current.setMontantoperation(current.getMontantoperation().subtract(l.getSoldePiece()));
        }
         else
        {if (l.getSoldePiece()!=null)
            ResteR=ResteR.add(l.getSoldePiece());}
        lettrage.remove(index);
        accompte.remove(index);
    }

    public void changeModeReglement() {
        System.out.println("fct  changeModeReglement");
        System.out.println("page : "+page);
       if (page.equalsIgnoreCase("Create"))
       {
        if (((currentModeReglement.getLibelle()).trim().equalsIgnoreCase("CHEQUE")) || (currentModeReglement.getLibelle().trim().equalsIgnoreCase("TRAITE")) || ((currentModeReglement.getLibelle().trim().equalsIgnoreCase("VIREMENT")))) {
        
            System.out.println("cas 1");
            inputTextReference.setDisabled(false);
            inputTextRecu=false;
            inputTextEmetteur.setDisabled(false);
            inputTextBanque.setDisabled(false);
            inputTextEcheance.setDisabled(false);
            
         
        } else {
              System.out.println("cas 2 else");
            inputTextReference.setDisabled(true);
            inputTextRecu=true;
            inputTextEmetteur.setDisabled(true);
            inputTextBanque.setDisabled(true);
            inputTextEcheance.setDisabled(true);
           
        }
       }  
    
       if (page.equalsIgnoreCase("Edit"))
       {
        if (  (current.getModereglement().getLibelle().trim().equalsIgnoreCase("TRAITE")) || ((current.getModereglement().getLibelle().trim().equalsIgnoreCase("VIREMENT"))) || (current.getModereglement().getLibelle().trim().equalsIgnoreCase("CHEQUE"))) {
            inputTextReference.setDisabled(false);
            inputTextRecu=false;
            inputTextEmetteur.setDisabled(false);
            inputTextBanque.setDisabled(false);
            inputTextEcheance.setDisabled(false);
        } else {
            inputTextReference.setDisabled(true);
        inputTextRecu=true;
            inputTextEmetteur.setDisabled(true);
            inputTextBanque.setDisabled(true);
            inputTextEcheance.setDisabled(true);
        }
       }         
        changeRefChequeTraiteVirement();
        changeRefChequeTraiteVirementModif();
    }

    public void affectAff(int index) {


        {
            BigDecimal reste = new BigDecimal(0).setScale(3, RoundingMode.UP);
            BigDecimal SoldePiece = lettrage.get(index).getSoldePiece();
            lettrage.get(index).setSoldePiece(new BigDecimal(0).setScale(3, RoundingMode.UP));
            ResteR = new BigDecimal(0).setScale(3, RoundingMode.UP);
            BigDecimal aff = new BigDecimal(0).setScale(3, RoundingMode.UP);
            Iterator it = lettrage.iterator();
            int i = 0;
            while (it.hasNext()) {
                LettrageCom pp = (LettrageCom) it.next();
                if (pp.getSoldePiece() == null) {
                } else {
                    aff = aff.add(pp.getSoldePiece().setScale(3, RoundingMode.UP));
                }
             
                i++;
            }
            ResteR = current.getMontantoperation().subtract(aff);
            lettrage.get(index).setSoldePiece(SoldePiece);
    if ((ResteR.compareTo(lettrage.get(index).getMontantReg()) == 1) || (ResteR.compareTo(lettrage.get(index).getMontantReg()) == 0)) {
                System.out.println("affecte aff 1");
                if (lettrage.get(index).getSoldePiece().compareTo(lettrage.get(index).getMontantReg()) == 1) {
                    lettrage.get(index).setSoldePiece(lettrage.get(index).getMontantReg().setScale(3, RoundingMode.UP));
                }
                if (lettrage.get(index).getSoldePiece().compareTo(lettrage.get(index).getMontantReg()) == 0) {
                    lettrage.get(index).setSoldePiece(lettrage.get(index).getMontantReg().setScale(3, RoundingMode.UP));
                }
                if (lettrage.get(index).getSoldePiece().compareTo(lettrage.get(index).getMontantReg()) == -1) {
                    lettrage.get(index).setSoldePiece(lettrage.get(index).getSoldePiece().setScale(3, RoundingMode.UP));
                }
            }
            if (ResteR.compareTo(lettrage.get(index).getMontantReg()) == -1) {
  System.out.println("affecte aff 2");
                if (lettrage.get(index).getSoldePiece().compareTo(ResteR) == 1) {
                    lettrage.get(index).setSoldePiece(ResteR.setScale(3, RoundingMode.UP));
                }
                if (lettrage.get(index).getSoldePiece().compareTo(ResteR) == 0) {
                    lettrage.get(index).setSoldePiece(ResteR.setScale(3, RoundingMode.UP));
                }
                if (lettrage.get(index).getSoldePiece().compareTo(ResteR) == -1) {
                    lettrage.get(index).setSoldePiece(lettrage.get(index).getSoldePiece().setScale(3, RoundingMode.UP));
                }

            }

        }

        ResteR = new BigDecimal(0).setScale(3, RoundingMode.UP);
        BigDecimal aff = new BigDecimal(0).setScale(3, RoundingMode.UP);
        Iterator it = lettrage.iterator();
        int i = 0;
        while (it.hasNext()) {
            LettrageCom pp = (LettrageCom) it.next();
            if (pp.getSoldePiece() == null) {
            } else {
                aff = aff.add(pp.getSoldePiece().setScale(3, RoundingMode.UP));
            }
         
            i++;
        }
        
        ResteR = current.getMontantoperation().subtract(aff);
    }

    public void onRowSelectOperationM() {
        try { 
            TableOperationM mm = (TableOperationM) dataTabledetailLettrage.getRowData();
            System.out.println("mm "+mm);
            Iterator it2 = lettrage.iterator();
            boolean falg = false;
            int mp = 0;
            while (it2.hasNext()) {
                LettrageCom pp = (LettrageCom) it2.next();
                try {
                    System.out.println("pp.getNiPiece() "+pp.getNiPiece());
                    System.out.println("mm.getOperation() "+mm.getOperation());
                    if (pp.getNiPiece().equals(mm.getOperation())) {
                        mp++;
                    }
                } catch (Exception e) {
                }
            }
    System.out.println("mpppppp" + mp);
            if (mp >= 2) {
                JsfUtil.addErrorMessage("Erreur : Numéro Facture invalide");
            } else {
                TableOperationM m = (TableOperationM) dataTabledetailLettrage.getRowData();
               accompte.set(index, m.getAcompte());
                LettrageCom l = new LettrageCom();
                l.setTypeOPAL(lettrage.get(index).getTypeOPAL());
              
                l.setNiPiece(m.getOperation());
                l.setMontantReg(m.getOperation().getMontantoperation().subtract(m.getAcompte()));
                l.setMontantPiece(m.getOperation().getMontantoperation());
                l.setSoldePiece(new BigDecimal(999999999).setScale(3, RoundingMode.UP));
               lettrage.set(index, l);
                affectAff(index);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

   
    }

    public void ajouterPiece() {
        index = DataTableLettrage.getRowIndex();
    }

    
     public String etatReglementFactureBl() {
        page = "Rapport";
        typeR="client";
        bl="globale";
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("type",typeR);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("bl",bl);
     
        return "Reglement_Etat_Facture_Bl";
    }
    
    public String etatReglement() {
    
        page = "Rapport";
        typeR="client";
        bl="globale";
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("type",typeR);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("bl",bl);
     
        return "Reglement_Etat";
    }
    
    
              public String etatExtraitPatient() {
    
         page = "Rapport";
        typeR="client";
        bl="globale";
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("type",typeR);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("bl",bl);
     
        return "Extrait_Patient";
    }
    
     public String etatDepense() {
    
        page = "Rapport";
        typeR="client";
        bl="globale";
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("type",typeR);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("bl",bl);
     
        return "Depense_Etat";
    }
       public String etatCaisse() {
    
        page = "Rapport";
        typeR="client";
        bl="globale";
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("type",typeR);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("bl",bl);
     
        return "Caisse";
    }
    
 public String etatReglementNonSolde() {
       
        page = "RapportreglementNonsolde";
       typeR="client";
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
                FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("type",typeR);
        return "Etat_reglement_Non_solde";
    }
    public String etatReglement1() {
        return "Reglement_Etat1";
    }

    public String create_rapports() {
  
        try {
            Driver monDriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(monDriver);
            Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/onlysoftserver", "root", "root");
            HashMap parameters = new HashMap();





            try {
//                JasperDesign design = JRXmlLoader.load(new LegacyJasperInputStream(new FileInputStream(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/report1.jrxml"))));
  //              JasperReport jasperReport = JasperCompileManager.compileReport(design);
    //            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

      //          byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
//                FacesContext context = FacesContext.getCurrentInstance();
//                HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//                HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
//                response.addHeader("Content-disposition", "attachment;filename=rapport.pdf");
//                response.setContentLength(bytes.length);
//                response.getOutputStream().write(bytes);
//                response.setContentType("application/pdf");
//                context.responseComplete();
            } catch (Exception eeeeeeeeP) {
                eeeeeeeeP.printStackTrace();
                JsfUtil.addErrorMessage("un problem de serveur");
                return null;
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("compte n'est pas trouvee");
        }
        return null;
    }

    /******************************************************************************/
    public void calcul(JasperDesign jasperDesign, JRDesignField field, int width, int setx, String f, JRDesignBand bandSomme, String var) {

        JRDesignTextField textField = new JRDesignTextField();
        JRDesignVariable sEspece = new JRDesignVariable();
        sEspece.setName(var);
        sEspece.setValueClass(java.math.BigDecimal.class);
        sEspece.setCalculation(CalculationEnum.getByValue(new Byte("2")));

        JRDesignExpression expressionE = new JRDesignExpression();
        expressionE.setValueClass(java.math.BigDecimal.class);
        expressionE.setText("new BigDecimal($F{" + f + "})");
        sEspece.setExpression(expressionE);


        JRDesignExpression expressionEI = new JRDesignExpression();
        expressionEI.setValueClass(java.math.BigDecimal.class);
        expressionEI.setText("new BigDecimal(0).setScale(3, RoundingMode.UP)");

        sEspece.setInitialValueExpression(expressionEI);
        try {
            jasperDesign.addVariable(sEspece);
        } catch (JRException ex) {
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }


        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        textField.setStretchWithOverflow(true);
        textField.setFontSize(10);
        textField.setWidth(width);
        textField.setX(setx);
        textField.setY(5);
        //  textField.setBorder(new Byte("1"));
        //  textField.setBorderColor(Color.red);
        textField.getLineBox().setTopPadding(5);
         textField.getLineBox().setLeftPadding(1);
 
        JRDesignExpression expression = new JRDesignExpression();
        expression.setValueClass(java.math.BigDecimal.class);
        expression.setText("($V{" + var + "}!=null)?$V{" + var + "}:new BigDecimal(0).setScale(3, RoundingMode.UP)");

        textField.setExpression(expression);
        textField.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));

        bandSomme.addElement(textField);


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
        x.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
        x.getLineBox().getLeftPen().setLineWidth(1);
x.getLineBox().getTopPen().setLineWidth(1);
x.getLineBox().getRightPen().setLineWidth(1);
x.getLineBox().getTopPen().setLineWidth(1);
x.getLineBox().getBottomPen().setLineWidth(1);  
   x.getLineBox().setLeftPadding(2);
        


        JRDesignTextField textField = new JRDesignTextField();
        JRDesignField field = new JRDesignField();
        if (filename.equalsIgnoreCase("DATE_SYS") || filename.equalsIgnoreCase("DATE")|| filename.contains("date") || (filename.equalsIgnoreCase("ECHANCE_OP"))) {
            field.setValueClass(java.util.Date.class);
        } else {
            field.setValueClass(java.lang.String.class);
        }
        field.setName(filename);


        try {
            jasperDesign.addField(field);
        } catch (JRException ex) {
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        if (nom.equalsIgnoreCase("ESPECE")) {
            calcul(jasperDesign, field, width, Xligne, filename, bandSomme, "varEspece");
            textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        }
        if (nom.equalsIgnoreCase("CHEQUE")) {
            calcul(jasperDesign, field, width, Xligne, filename, bandSomme, "varCheque");
            textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        }
        if (nom.equalsIgnoreCase("TRAITE")) {
            calcul(jasperDesign, field, width, Xligne, filename, bandSomme, "varTraite");
            textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        }
        if (nom.equalsIgnoreCase("VIREMENT")) {
            calcul(jasperDesign, field, width, Xligne, filename, bandSomme, "varVirement");
            textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        }


 if (nom.equalsIgnoreCase("MONTANT")) {
            calcul(jasperDesign, field, width, Xligne, filename, bandSomme, "varMontant");
            textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);

            textField.getLineBox().setRightPadding(1);
        }
        if (nom.equalsIgnoreCase("LETTRAGE")) {
            calcul(jasperDesign, field, width, Xligne, filename, bandSomme, "varLettrage");
            textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
            textField.getLineBox().setRightPadding(1);
        }
        if (nom.equalsIgnoreCase("SOLDE")) {
            calcul(jasperDesign, field, width, Xligne, filename, bandSomme, "varSolde");
            textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
            textField.getLineBox().setRightPadding(1);
        }
        if (nom.equalsIgnoreCase("CREDIT")) {
            calcul(jasperDesign, field, width, Xligne, filename, bandSomme, "varCredit");
            textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);

            textField.getLineBox().setRightPadding(1);
        }
          if (nom.equalsIgnoreCase("DEBIT")) {
            calcul(jasperDesign, field, width, Xligne, filename, bandSomme, "varDebit");
            textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);

            textField.getLineBox().setRightPadding(1);
        }




        // textField.setBlankWhenNull(true);

        textField.setFontSize(10);
    //    textField.setBorder(new Byte("1"));
        textField.getLineBox().getLeftPen().setLineWidth(1);
textField.getLineBox().getTopPen().setLineWidth(1);
textField.getLineBox().getRightPen().setLineWidth(1);
textField.getLineBox().getTopPen().setLineWidth(1);
textField.getLineBox().getBottomPen().setLineWidth(1);  
        textField.getLineBox().getLeftPen().setLineWidth(1);
textField.getLineBox().getTopPen().setLineWidth(1);
textField.getLineBox().getRightPen().setLineWidth(1);
textField.getLineBox().getTopPen().setLineWidth(1);
textField.getLineBox().getBottomPen().setLineWidth(1);  
      //  textField.setBorderColor(Color.BLACK);
        textField.getLineBox().setLeftPadding(2);
        

        textField.setWidth(width);
        textField.setX(Xligne);

        JRDesignExpression expression = new JRDesignExpression();
        if (nom.equalsIgnoreCase("DATE") ||  (nom.equalsIgnoreCase("ECHEANCE"))) {

            expression.setValueClass(java.util.Date.class);
            textField.setPattern("dd/MM/yyyy");
        } else {
            expression.setValueClass(java.lang.String.class);
        }


        expression.setText(exp);
        textField.setExpression(expression);


        if (nom.equals("BANQUE")) {
            textField.setStretchWithOverflow(false);

        } else {
            textField.setStretchWithOverflow(true);
        }
        textField.setHeight(15);
        bandHeader.addElement(textField);
        Xligne = Xligne + width;
    }

    public void orientation(JasperDesign jasper, String ORIENTATION, JRDesignFrame jrFame) {
        if (ORIENTATION.equals("LANDSCAPE")) {
            jasper.setOrientation(OrientationEnum.LANDSCAPE);
            jasper.setPageWidth(842);
            jasper.setPageHeight(595);

        } else {
            jasper.setOrientation(OrientationEnum.PORTRAIT);
            jasper.setPageWidth(595);
            jasper.setPageHeight(842);
        }
    }

   public void jasperDesignModelEtatRegNonSoldeRtf() throws JRException, IOException {
    
         try {
            
            int pageWidth = 0;
          
            if (optionOrient.equals("2")) {
                pageWidth = 842;
            } else {
                pageWidth = 595;
            }
            JRDesignFrame jrFame = new JRDesignFrame();
            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

            DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d22 = new Date();
            String ddd = dateFormat3.format(d22);
            JasperDesign jasperDesign = new JasperDesign();


            JRDesignBand bandTiltle = new JRDesignBand();

            JRDesignStaticText libelleDateSys = new JRDesignStaticText();
            libelleDateSys.setWidth(150);
            libelleDateSys.setHeight(20);
            libelleDateSys.setText("DATE : " + ddd);
            libelleDateSys.setFontSize(10);
            libelleDateSys.setForecolor(Color.BLACK);
            libelleDateSys.setX(440);
            libelleDateSys.setY(10);
            jrFame.addElement(libelleDateSys);

            JRDesignBand bandBlanc = new JRDesignBand();
            bandBlanc.setHeight(200);
            JRDesignStaticText libellePgeB = new JRDesignStaticText();
            libellePgeB.setWidth(50);
            libellePgeB.setHeight(20);
            libellePgeB.setX(0);
            libellePgeB.setY(30);
            libellePgeB.setText("Tel :");
            libellePgeB.setFontSize(12);
            libellePgeB.setForecolor(Color.BLACK);
            bandBlanc.addElement(libellePgeB);
            jasperDesign.setNoData(bandBlanc);
            
            dateDebut.setHours(0);
            dateDebut.setMinutes(0);
            dateDebut.setSeconds(0);
            dateFin.setHours(23);
            dateFin.setMinutes(59);
            dateFin.setSeconds(59);

            JRDesignBand bandSomme = new JRDesignBand();
            bandSomme.setHeight(80);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String d1 = dateFormat.format(dateDebut);
            String d2 = dateFormat.format(dateFin);
            DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            String d111 = dateFormat1.format(dateDebut);
            String d222 = dateFormat1.format(dateFin);
            Xligne = 0;
            etablirconnection();
            JRDesignQuery query = new JRDesignQuery();
            String Filt_typeReg = "";
            String ty = "";
       
            String q1 = "";

            if (filtreTiersEtat != null) {
                q1 = " and operation.`client` =" + filtreTiersEtat.getId();
            }
          
           
                     String q = "SELECT DATE_SYS,point_vente_com.`NOM_PV` AS point_vente_com_NOM_PV,caissier_com.`NOM_CAI` AS caissier_com_NOM_CAI,operation_com.`NUM_OP` AS operation_com_NUM_OP,operation_com.`DATE_OP` AS operation_com_DATE_OP,tiers_com.`CODE_TI` AS tiers_com_CODE_TI,tiers_com.`NOM_TI` AS tiers_com_NOM_TI,type_op_com.`NOM_TOP` AS type_op_com_NOM_TOP,operation_com.`MONTANT_COP` AS operation_com_MONTANT_COP,operation_com.`NI_MR` AS operation_com_NI_MR,mode_reglement_com.`NI_MR` AS mode_reglement_com_NI_MR,mode_reglement_com.`CODE_MR` AS mode_reglement_com_CODE_MR,mode_reglement_com.`NOM_MR` AS mode_reglement_com_NOM_MR,operation_com.`REFERENCE_OP` AS operation_com_REFERENCE_OP,banque_com.`NOM_BANQUE` AS banque_com_NOM_BANQUE,MONTANT_ES,MONTANT_TR,MONTANT_VIR,MONTANT_CH,ECHANCE_OP , horizon_sys.`id` AS horizon_sys_id,horizon_sys.`CODE` AS horizon_sys_CODE,horizon_sys.`LIBELLE_SOCIETE` AS horizon_sys_LIBELLE_SOCIETE, horizon_sys.`ADRESSE` AS horizon_sys_ADRESSE,horizon_sys.`FAX` AS horizon_sys_FAX,horizon_sys.`TEL` AS horizon_sys_TEL FROM `tiers_com` tiers_com INNER JOIN `operation_com` operation_com ON tiers_com.`NI_TI` = operation_com.`NI_TI` INNER JOIN `type_op_com` type_op_com ON operation_com.`NI_TOP` = type_op_com.`NI_TOP` INNER JOIN `mode_reglement_com` mode_reglement_com ON operation_com.`NI_MR` = mode_reglement_com.`NI_MR` INNER JOIN `point_vente_com` point_vente_com ON operation_com.`NI_PV` = point_vente_com.`NI_PV` LEFT JOIN `caissier_com` caissier_com ON operation_com.`NI_CAI` = caissier_com.`NI_CAI` LEFT JOIN `banque_com` banque_com ON operation_com.`NI_BANQUE` = banque_com.`NI_BANQUE` ,`horizon_sys` horizon_sys  WHERE type_op_com.`NOM_TOP` = 'reglement' " + Filt_typeReg + " and DATE_SYS >= '" + d1 + "' and DATE_SYS <= '" + d2 + "'" + q1 + " order by DATE_SYS ";
            String aa="Select operation_com.ni_op, tiers_comNI_TI.NOM_TI as NI_TINOM_TI,tiers_comNI_TI.CODE_TI as NI_TICODE_TI,operation_com.DATE_SYS as DATE_SYS,operation_com.NUM_OP as NUM_OP, operation_com.MONTANT_COP AS operation_com_MONTANT_COP,ifnull((select sum(MONTANT_REG) FROM lettrage_com lettrage_com where operation_com.NI_OP=lettrage_com.ni_op),0.000) as mtREg,(Select operation_com_MONTANT_COP-mtReg) as solde,    horizon_sys.`CODE` AS horizon_sys_CODE,horizon_sys.`LIBELLE_SOCIETE` AS horizon_sys_LIBELLE_SOCIETE,horizon_sys.`ADRESSE` AS horizon_sys_ADRESSE,horizon_sys.`TEL` AS horizon_sys_TEL,horizon_sys.`FAX` AS horizon_sys_FAX from operation_com operation_com   LEFT OUTER JOIN tiers_com tiers_comNI_TI ON operation_com.NI_TI=tiers_comNI_TI.NI_TI LEFT OUTER JOIN `horizon_sys` horizon_sys ON horizon_sys.`id` = operation_com.`societe`    where operation_com.NI_Top=1 and operation_com.date_sys<='" + d2 + "' and operation_com.date_sys>='" + d1 + "'" + q1 + "  group by(operation_com.NI_OP) having solde!=0.000 order by DATE_SYS";
            query.setText(aa);
            jasperDesign.setQuery(query);
            jasperDesign.setName("ReglementDesign");
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

            jrFame.setHeight(72);
            jrFame.setWidth(842);
            jrFame.getLineBox().setLeftPadding(0);
            jrFame.getLineBox().setTopPadding(0);
            jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

            JRDesignStaticText libelleTitre = new JRDesignStaticText();
            libelleTitre.setWidth(360);
            libelleTitre.setHeight(31);
            libelleTitre.setX(200);
            libelleTitre.setY(30);
            libelleTitre.setText("ETAT REGLEMENTS NON LETTRES");
            libelleTitre.setFontSize(18);
            libelleTitre.setForecolor(Color.BLUE);
            jrFame.addElement(libelleTitre);

            JRDesignStaticText libelleTotal = new JRDesignStaticText();
            libelleTotal.setWidth(310);
            libelleTotal.setHeight(31);
            libelleTotal.setX(0);
            libelleTotal.setY(7);
            libelleTotal.setText("TOTAL ");
            libelleTotal.setFontSize(12);
            libelleTotal.setForecolor(Color.BLACK);
            bandSomme.addElement(libelleTotal);


            JRDesignStaticText libelleDate = new JRDesignStaticText();
            libelleDate.setWidth(300);
            libelleDate.setHeight(15);
            libelleDate.setText(" PERIODE : Du  " + d111 + "  Au  " + d222);
            libelleDate.setFontSize(10);
            libelleDate.setForecolor(Color.BLACK);
            libelleDate.setX(200);
            libelleDate.setY(55);
            jrFame.addElement(libelleDate);
            bandTiltle.setHeight(82);


            /**************************************************/
            JRDesignField fieldSTE = new JRDesignField();
            fieldSTE.setName("horizon_sys_LIBELLE_SOCIETE");
            fieldSTE.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldSTE);

            JRDesignTextField textFieldSTE = new JRDesignTextField();
            textFieldSTE.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldSTE.setStretchWithOverflow(true);
            textFieldSTE.setFontSize(10);
            textFieldSTE.setWidth(300);
            textFieldSTE.setX(0);
            textFieldSTE.setY(0);
            JRDesignExpression expressionSTE = new JRDesignExpression();
            expressionSTE.setValueClass(java.lang.String.class);
            expressionSTE.setText("$F{horizon_sys_LIBELLE_SOCIETE}");
            textFieldSTE.setExpression(expressionSTE);

            jrFame.addElement(textFieldSTE);

            /*******************************************************************/
            JRDesignField fieldAdresse = new JRDesignField();
            fieldAdresse.setName("horizon_sys_Adresse");
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
            expressionAdresse.setText("$F{horizon_sys_Adresse}");
            textFieldAdresse.setExpression(expressionAdresse);

            jrFame.addElement(textFieldAdresse);

            /*************************************************************/
            JRDesignStaticText libelleTEL = new JRDesignStaticText();
            libelleTEL.setWidth(50);
            libelleTEL.setHeight(20);
            libelleTEL.setX(2);
            libelleTEL.setY(30);
            libelleTEL.setText("Tel  :");
            libelleTEL.setFontSize(10);
            libelleTEL.setForecolor(Color.BLACK);
            jrFame.addElement(libelleTEL);

            JRDesignField fieldTel = new JRDesignField();
            fieldTel.setName("horizon_sys_Tel");
            fieldTel.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldTel);

            JRDesignTextField textFieldTel = new JRDesignTextField();
            textFieldTel.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldTel.setStretchWithOverflow(true);
            textFieldTel.setFontSize(10);
            textFieldTel.setWidth(100);
            textFieldTel.setX(30);
            textFieldTel.setY(30);
            JRDesignExpression expressionTel = new JRDesignExpression();
            expressionTel.setValueClass(java.lang.String.class);
            expressionTel.setText("$F{horizon_sys_Tel}");
            textFieldTel.setExpression(expressionTel);

            jrFame.addElement(textFieldTel);
            /*******************************************************/
            /*********************************************************/
            JRDesignStaticText libelleFax = new JRDesignStaticText();
            libelleFax.setWidth(50);
            libelleFax.setHeight(20);
            libelleFax.setX(2);
            libelleFax.setY(45);
            libelleFax.setText("Fax :");
            libelleFax.setFontSize(10);
            libelleFax.setForecolor(Color.BLACK);
            jrFame.addElement(libelleFax);

            JRDesignField fieldFax = new JRDesignField();
            fieldFax.setName("horizon_sys_Fax");
            fieldFax.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldFax);

            JRDesignTextField textFieldFax = new JRDesignTextField();
            textFieldFax.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldFax.setStretchWithOverflow(true);
            textFieldFax.setFontSize(10);
            textFieldFax.setWidth(100);
            textFieldFax.setX(30);
            textFieldFax.setY(45);
            JRDesignExpression expressionFax = new JRDesignExpression();
            expressionFax.setValueClass(java.lang.String.class);
            expressionFax.setText("$F{horizon_sys_Fax}");
            textFieldFax.setExpression(expressionFax);

            jrFame.addElement(textFieldFax);
            /********************************/
            bandTiltle.addElement(jrFame);
            jasperDesign.setTitle(bandTiltle);
            /*******************************************************/
             JRDesignBand bandHeader = new JRDesignBand();
            BigDecimal bandHeaderHeight = new BigDecimal(15).setScale(0, RoundingMode.DOWN);
            bandHeader.setHeight(bandHeaderHeight.intValue());
            //    bandHeader.setHeight(100);


            JRDesignBand columnHeader = new JRDesignBand();
            columnHeader.setHeight(15);
//           
            
        
            flagCodeClientF=true;
            flagDate=true;
            
             if (flagNomClientF == true) {
                JRDesignStaticText libelleColonneNomTI = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "NI_TINOM_TI", "$F{NI_TINOM_TI}", libelleColonneNomTI, "RAISON SOCIALE", 345, 200, bandSomme);
                columnHeader.addElement(libelleColonneNomTI);
            }
            
            
            if (flagCodeClientF == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "NI_TICODE_TI", "$F{NI_TICODE_TI}", libelleColonneCode, "CODE", 500, 50, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }

            
            
            if (flagDate == true) {
                JRDesignStaticText libelleColonneDate = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "DATE_SYS", "$F{DATE_SYS}", libelleColonneDate, "DATE", 0, 60, bandSomme);
                columnHeader.addElement(libelleColonneDate);
            }


           if (flagDate == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "NUM_OP", "$F{NUM_OP}", libelleColonneCode, "OPERATION", 500, 63, bandSomme);
                columnHeader.addElement(libelleColonneCode);
           }
if (flagDate == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_com_MONTANT_COP", "$F{operation_com_MONTANT_COP}", libelleColonneCode, "MONTANT", 500, 69, bandSomme);
                columnHeader.addElement(libelleColonneCode);

}

    if (flagDate == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "mtREg", "$F{mtREg}", libelleColonneCode, "LETTRAGE", 500, 69, bandSomme);
                columnHeader.addElement(libelleColonneCode);

}       

 if (flagDate == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "solde", "$F{solde}", libelleColonneCode, "SOLDE", 500, 69, bandSomme);
                columnHeader.addElement(libelleColonneCode);

}       
     
            
            
            
 /******************************/       

            jasperDesign.setColumnHeader(columnHeader);
         //   jasperDesign.setDetail(bandHeader);
            ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandHeader);
             jasperDesign.setSummary(bandSomme);
            

            /*************************************************************/
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
            textField2.setY(0);
            textField2.setWidth(150);
            textField2.setHeight(15);
            textField2.getLineBox().setTopPadding(2);
            textField2.getLineBox().setLeftPadding(2);
            textField2.setStretchWithOverflow(false);
            textField2.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            JRDesignExpression expression2 = new JRDesignExpression();
            expression2.setValueClass(java.lang.String.class);
            expression2.setText("$V{PAGE_NUMBER}.toString()");
            textField2.setExpression(expression2);
            textField2.setEvaluationTime(EvaluationTimeEnum.REPORT);
            bandFooter.addElement(textField2);
            jasperDesign.setPageFooter(bandFooter);
            /*************************************************************************************************/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap parameters = new HashMap();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

            DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
            Date d = new Date();
            String dd = dateFormat15.format(d);
            String nomFichier = "Rapport Reglement " + dd;
                  JRRtfExporter exporter = new JRRtfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, nomFichier);         
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
            exporter.exportReport();
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment;filename=" + nomFichier);
            response.setContentLength(byteArrayOutputStream.toByteArray().length);
            response.getOutputStream().write(byteArrayOutputStream.toByteArray());
            response.setContentType("application/rtf");
            context.responseComplete();
        } catch (Exception ex) {
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    /***********************************************************/
    public void jasperDesignModelEtatRegNonSolde() throws JRException, IOException {
    
         try {
            
            int pageWidth = 0;
          
            if (optionOrient.equals("2")) {
                pageWidth = 842;
            } else {
                pageWidth = 595;
            }
          
         //   System.out.println("optionOrient" + optionOrient);
            JRDesignFrame jrFame = new JRDesignFrame();
            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

            DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d22 = new Date();
            String ddd = dateFormat3.format(d22);
            JasperDesign jasperDesign = new JasperDesign();


            JRDesignBand bandTiltle = new JRDesignBand();

            JRDesignStaticText libelleDateSys = new JRDesignStaticText();
            libelleDateSys.setWidth(150);
            libelleDateSys.setHeight(20);
            libelleDateSys.setText("DATE : " + ddd);
            libelleDateSys.setFontSize(10);
            libelleDateSys.setForecolor(Color.BLACK);
            libelleDateSys.setX(440);
            libelleDateSys.setY(10);
            jrFame.addElement(libelleDateSys);

            JRDesignBand bandBlanc = new JRDesignBand();
            bandBlanc.setHeight(200);
            JRDesignStaticText libellePgeB = new JRDesignStaticText();
            libellePgeB.setWidth(50);
            libellePgeB.setHeight(20);
            libellePgeB.setX(0);
            libellePgeB.setY(30);
            libellePgeB.setText("Tel :");
            libellePgeB.setFontSize(12);
            libellePgeB.setForecolor(Color.BLACK);
            bandBlanc.addElement(libellePgeB);
            jasperDesign.setNoData(bandBlanc);
            
            dateDebut.setHours(0);
            dateDebut.setMinutes(0);
            dateDebut.setSeconds(0);
            dateFin.setHours(23);
            dateFin.setMinutes(59);
            dateFin.setSeconds(59);

            JRDesignBand bandSomme = new JRDesignBand();
            bandSomme.setHeight(80);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String d1 = dateFormat.format(dateDebut);
            String d2 = dateFormat.format(dateFin);
            DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            String d111 = dateFormat1.format(dateDebut);
            String d222 = dateFormat1.format(dateFin);
            Xligne = 0;
         //   System.out.println("d1" + d1);
           //  System.out.println("d2 "+d2);
            etablirconnection();
            JRDesignQuery query = new JRDesignQuery();
            String Filt_typeReg = "";
            String ty = "";

            //     and (mode_reglement_com.`NOM_MR` = '"+ty+"' "+Filt_typeReg+") 
       
           

     /*      
        
         System.out.println("apres test");
         flagNomClientF=true;
         flagCodeClientF=true;
         flagDate=true;
         
         
  System.out.println("flag cheque"+flagMontantCheque);
             System.out.println("flag espece"+flagMontantEspece);
              System.out.println("flag traite"+flagMontantTraite);
               System.out.println("flag virement"+flagMontantVirement);
                System.out.println("flag code"+flagCodeClientF);
                 System.out.println("flag banque"+flagBanque);
                  System.out.println("flag point vente"+flagPointVente);
                   System.out.println("flag caissier"+flagCaissier);
                    System.out.println("flag reference"+flagRefCheque);
                     System.out.println("flag echeance"+flagecheanceCheque);
                      System.out.println("flag date"+flagDate);
         
            System.out.println(Filt_typeReg);*/
 /*******************************/
            
       
            
            
            
            
            String q1 = "";

            if (filtreTiersEtat != null) {
                q1 = " and operation_com.`NI_TI` =" + filtreTiersEtat.getId();
            }
               String q = "SELECT DATE_SYS,point_vente_com.`NOM_PV` AS point_vente_com_NOM_PV,caissier_com.`NOM_CAI` AS caissier_com_NOM_CAI,operation_com.`NUM_OP` AS operation_com_NUM_OP,operation_com.`DATE_OP` AS operation_com_DATE_OP,tiers_com.`CODE_TI` AS tiers_com_CODE_TI,tiers_com.`NOM_TI` AS tiers_com_NOM_TI,type_op_com.`NOM_TOP` AS type_op_com_NOM_TOP,operation_com.`MONTANT_COP` AS operation_com_MONTANT_COP,operation_com.`NI_MR` AS operation_com_NI_MR,mode_reglement_com.`NI_MR` AS mode_reglement_com_NI_MR,mode_reglement_com.`CODE_MR` AS mode_reglement_com_CODE_MR,mode_reglement_com.`NOM_MR` AS mode_reglement_com_NOM_MR,operation_com.`REFERENCE_OP` AS operation_com_REFERENCE_OP,banque_com.`NOM_BANQUE` AS banque_com_NOM_BANQUE,MONTANT_ES,MONTANT_TR,MONTANT_VIR,MONTANT_CH,ECHANCE_OP , horizon_sys.`id` AS horizon_sys_id,horizon_sys.`CODE` AS horizon_sys_CODE,horizon_sys.`LIBELLE_SOCIETE` AS horizon_sys_LIBELLE_SOCIETE, horizon_sys.`ADRESSE` AS horizon_sys_ADRESSE,horizon_sys.`FAX` AS horizon_sys_FAX,horizon_sys.`TEL` AS horizon_sys_TEL FROM `tiers_com` tiers_com INNER JOIN `operation_com` operation_com ON tiers_com.`NI_TI` = operation_com.`NI_TI` INNER JOIN `type_op_com` type_op_com ON operation_com.`NI_TOP` = type_op_com.`NI_TOP` INNER JOIN `mode_reglement_com` mode_reglement_com ON operation_com.`NI_MR` = mode_reglement_com.`NI_MR` INNER JOIN `point_vente_com` point_vente_com ON operation_com.`NI_PV` = point_vente_com.`NI_PV` LEFT JOIN `caissier_com` caissier_com ON operation_com.`NI_CAI` = caissier_com.`NI_CAI` LEFT JOIN `banque_com` banque_com ON operation_com.`NI_BANQUE` = banque_com.`NI_BANQUE` ,`horizon_sys` horizon_sys  WHERE type_op_com.`NOM_TOP` = 'reglement' " + Filt_typeReg + " and DATE_SYS >= '" + d1 + "' and DATE_SYS <= '" + d2 + "'" + q1 + " order by DATE_SYS ";
            String aa="Select operation_com.ni_op, tiers_comNI_TI.NOM_TI as NI_TINOM_TI,tiers_comNI_TI.CODE_TI as NI_TICODE_TI,operation_com.DATE_SYS as DATE_SYS,operation_com.NUM_OP as NUM_OP, operation_com.MONTANT_COP AS operation_com_MONTANT_COP,ifnull((select sum(MONTANT_REG) FROM lettrage_com lettrage_com where operation_com.NI_OP=lettrage_com.ni_op),0.000) as mtREg,(Select operation_com_MONTANT_COP-mtReg) as solde,    horizon_sys.`CODE` AS horizon_sys_CODE,horizon_sys.`LIBELLE_SOCIETE` AS horizon_sys_LIBELLE_SOCIETE,horizon_sys.`ADRESSE` AS horizon_sys_ADRESSE,horizon_sys.`TEL` AS horizon_sys_TEL,horizon_sys.`FAX` AS horizon_sys_FAX from operation_com operation_com   LEFT OUTER JOIN tiers_com tiers_comNI_TI ON operation_com.NI_TI=tiers_comNI_TI.NI_TI LEFT OUTER JOIN `horizon_sys` horizon_sys ON horizon_sys.`id` = operation_com.`societe`    where operation_com.NI_Top=1 and operation_com.date_sys<='" + d2 + "' and operation_com.date_sys>='" + d1 + "'" + q1 + "  group by(operation_com.NI_OP) having solde!=0.000 order by DATE_SYS";
            query.setText(aa);
            jasperDesign.setQuery(query);
         //    System.out.println("aaa "+aa);
            jasperDesign.setName("ReglementDesign");

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

            jrFame.setHeight(72);
            jrFame.setWidth(842);
            jrFame.getLineBox().setLeftPadding(0);
            jrFame.getLineBox().setTopPadding(0);
            jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

            JRDesignStaticText libelleTitre = new JRDesignStaticText();
            libelleTitre.setWidth(360);
            libelleTitre.setHeight(31);
            libelleTitre.setX(200);
            libelleTitre.setY(30);
            libelleTitre.setText("ETAT REGLEMENTS NON LETTRES");
            libelleTitre.setFontSize(18);
            libelleTitre.setForecolor(Color.BLUE);
            jrFame.addElement(libelleTitre);

            JRDesignStaticText libelleTotal = new JRDesignStaticText();
            libelleTotal.setWidth(310);
            libelleTotal.setHeight(31);
            libelleTotal.setX(0);
            libelleTotal.setY(7);
            libelleTotal.setText("TOTAL ");
            libelleTotal.setFontSize(12);
            libelleTotal.setForecolor(Color.BLACK);
            bandSomme.addElement(libelleTotal);


            JRDesignStaticText libelleDate = new JRDesignStaticText();
            libelleDate.setWidth(200);
            libelleDate.setHeight(15);
            libelleDate.setText(" PERIODE : Du  " + d111 + "  Au  " + d222);
            libelleDate.setFontSize(10);
            libelleDate.setForecolor(Color.BLACK);
            libelleDate.setX(200);
            libelleDate.setY(55);
            jrFame.addElement(libelleDate);
            bandTiltle.setHeight(82);


            /**************************************************/
            JRDesignField fieldSTE = new JRDesignField();
            fieldSTE.setName("horizon_sys_LIBELLE_SOCIETE");
            fieldSTE.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldSTE);

            JRDesignTextField textFieldSTE = new JRDesignTextField();
            textFieldSTE.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldSTE.setStretchWithOverflow(true);
            textFieldSTE.setFontSize(10);
            textFieldSTE.setWidth(300);
            textFieldSTE.setX(0);
            textFieldSTE.setY(0);
            JRDesignExpression expressionSTE = new JRDesignExpression();
            expressionSTE.setValueClass(java.lang.String.class);
            expressionSTE.setText("$F{horizon_sys_LIBELLE_SOCIETE}");
            textFieldSTE.setExpression(expressionSTE);

            jrFame.addElement(textFieldSTE);

            /*******************************************************************/
            JRDesignField fieldAdresse = new JRDesignField();
            fieldAdresse.setName("horizon_sys_Adresse");
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
            expressionAdresse.setText("$F{horizon_sys_Adresse}");
            textFieldAdresse.setExpression(expressionAdresse);

            jrFame.addElement(textFieldAdresse);

            /*************************************************************/
            JRDesignStaticText libelleTEL = new JRDesignStaticText();
            libelleTEL.setWidth(50);
            libelleTEL.setHeight(20);
            libelleTEL.setX(2);
            libelleTEL.setY(30);
            libelleTEL.setText("Tel  :");
            libelleTEL.setFontSize(10);
            libelleTEL.setForecolor(Color.BLACK);
            jrFame.addElement(libelleTEL);

            JRDesignField fieldTel = new JRDesignField();
            fieldTel.setName("horizon_sys_Tel");
            fieldTel.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldTel);

            JRDesignTextField textFieldTel = new JRDesignTextField();
            textFieldTel.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldTel.setStretchWithOverflow(true);
            textFieldTel.setFontSize(10);
            textFieldTel.setWidth(100);
            textFieldTel.setX(30);
            textFieldTel.setY(30);
            JRDesignExpression expressionTel = new JRDesignExpression();
            expressionTel.setValueClass(java.lang.String.class);
            expressionTel.setText("$F{horizon_sys_Tel}");
            textFieldTel.setExpression(expressionTel);

            jrFame.addElement(textFieldTel);
            /*******************************************************/
            /*********************************************************/
            JRDesignStaticText libelleFax = new JRDesignStaticText();
            libelleFax.setWidth(50);
            libelleFax.setHeight(20);
            libelleFax.setX(2);
            libelleFax.setY(45);
            libelleFax.setText("Fax :");
            libelleFax.setFontSize(10);
            libelleFax.setForecolor(Color.BLACK);
            jrFame.addElement(libelleFax);

            JRDesignField fieldFax = new JRDesignField();
            fieldFax.setName("horizon_sys_Fax");
            fieldFax.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldFax);

            JRDesignTextField textFieldFax = new JRDesignTextField();
            textFieldFax.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldFax.setStretchWithOverflow(true);
            textFieldFax.setFontSize(10);
            textFieldFax.setWidth(100);
            textFieldFax.setX(30);
            textFieldFax.setY(45);
            JRDesignExpression expressionFax = new JRDesignExpression();
            expressionFax.setValueClass(java.lang.String.class);
            expressionFax.setText("$F{horizon_sys_Fax}");
            textFieldFax.setExpression(expressionFax);

            jrFame.addElement(textFieldFax);
            /********************************/
            bandTiltle.addElement(jrFame);
            jasperDesign.setTitle(bandTiltle);
            /*******************************************************/
             JRDesignBand bandHeader = new JRDesignBand();
            BigDecimal bandHeaderHeight = new BigDecimal(15).setScale(0, RoundingMode.DOWN);
            bandHeader.setHeight(bandHeaderHeight.intValue());
            //    bandHeader.setHeight(100);


            JRDesignBand columnHeader = new JRDesignBand();
            columnHeader.setHeight(15);
//           
            flagNomClientF=true;
            flagCodeClientF=true;
            flagDate=true;
            
             if (flagNomClientF == true) {
                JRDesignStaticText libelleColonneNomTI = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "NI_TINOM_TI", "$F{NI_TINOM_TI}", libelleColonneNomTI, "RAISON SOCIALE", 345, 200, bandSomme);
                columnHeader.addElement(libelleColonneNomTI);
            }
            
            
            if (flagCodeClientF == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "NI_TICODE_TI", "$F{NI_TICODE_TI}", libelleColonneCode, "CODE", 500, 50, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }

            
            
            if (flagDate == true) {
                JRDesignStaticText libelleColonneDate = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "DATE_SYS", "$F{DATE_SYS}", libelleColonneDate, "DATE", 0, 60, bandSomme);
                columnHeader.addElement(libelleColonneDate);
            }


           if (flagDate == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "NUM_OP", "$F{NUM_OP}", libelleColonneCode, "OPERATION", 500, 63, bandSomme);
                columnHeader.addElement(libelleColonneCode);
           }
if (flagDate == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_com_MONTANT_COP", "$F{operation_com_MONTANT_COP}", libelleColonneCode, "MONTANT", 500, 69, bandSomme);
                columnHeader.addElement(libelleColonneCode);

}

    if (flagDate == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "mtREg", "$F{mtREg}", libelleColonneCode, "LETTRAGE", 500, 69, bandSomme);
                columnHeader.addElement(libelleColonneCode);

}       

 if (flagDate == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "solde", "$F{solde}", libelleColonneCode, "SOLDE", 500, 69, bandSomme);
                columnHeader.addElement(libelleColonneCode);

}       
     
            
            
            
 /******************************/       

            jasperDesign.setColumnHeader(columnHeader);
         //   jasperDesign.setDetail(bandHeader);
            ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandHeader);
             jasperDesign.setSummary(bandSomme);
            

            /***********************************Somme Total***************************************/
            //  JRDesignTextField textFieldSomme = new JRDesignTextField(); 
//            JRDesignField montant = new JRDesignField();
//            montant.setName("operation_com_MONTANT_COP");
//            montant.setValueClass(java.math.BigDecimal.class);
//            jasperDesign.addField(montant);
//
//
//
//
//            JRDesignVariable varSomme = new JRDesignVariable();
//            varSomme.setName("varTotal");
//            varSomme.setValueClass(java.math.BigDecimal.class);
//            varSomme.setCalculation(new Byte("2"));
//            jasperDesign.addVariable(varSomme);
//
//            JRDesignExpression expressionE = new JRDesignExpression();
//            expressionE.setValueClass(java.math.BigDecimal.class);
//            expressionE.setText("$F{operation_com_MONTANT_COP}");
//            varSomme.setExpression(expressionE);
//
//
//            JRDesignExpression expressionEI = new JRDesignExpression();
//            expressionEI.setValueClass(java.math.BigDecimal.class);
//            expressionEI.setText("new BigDecimal(0).setScale(3, RoundingMode.UP)");
//
//            varSomme.setInitialValueExpression(expressionEI);
//
//
//            JRDesignTextField textField = new JRDesignTextField();
//            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
//            textField.setStretchWithOverflow(true);
//            textField.setFontSize(12);
//            textField.setWidth(100);
//            textField.setX(75);
//            textField.setY(45);
//            JRDesignExpression expression = new JRDesignExpression();
//            expression.setValueClass(java.math.BigDecimal.class);
//            expression.setText("$V{varTotal}");
//            textField.setExpression(expression);
//            textField.setHorizontalAlignment(new Byte("3"));
//
//
//            JRDesignStaticText libelleTotalF = new JRDesignStaticText();
//            libelleTotalF.setWidth(150);
//            libelleTotalF.setHeight(31);
//            libelleTotalF.setX(0);
//            libelleTotalF.setY(45);
//            libelleTotalF.setText("TOTAL GENERAL :");
//            libelleTotalF.setSize(12);
//            libelleTotalF.setForecolor(Color.BLUE);
//            bandSomme.addElement(libelleTotalF);
//
//
//            bandSomme.addElement(textField);
            /*************************************************************/
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
            textField2.setY(0);
            textField2.setWidth(150);
            textField2.setHeight(15);
            textField2.getLineBox().setTopPadding(2);
            textField2.getLineBox().setLeftPadding(2);
            textField2.setStretchWithOverflow(false);
            textField2.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            JRDesignExpression expression2 = new JRDesignExpression();
            expression2.setValueClass(java.lang.String.class);
            expression2.setText("$V{PAGE_NUMBER}.toString()");
            textField2.setExpression(expression2);
            textField2.setEvaluationTime(EvaluationTimeEnum.REPORT);
            bandFooter.addElement(textField2);
            jasperDesign.setPageFooter(bandFooter);
            /*************************************************************************************************/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap parameters = new HashMap();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

            DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
            Date d = new Date();
            String dd = dateFormat15.format(d);
            String nomFichier = "Rapport Reglement " + dd;
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment;filename=" + nomFichier + " .pdf");
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.setContentType("application/pdf");
            context.responseComplete();
        } catch (Exception ex) {
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /***************************************************************************************/
    public void jasperDesignModelRtf() throws JRException, IOException {
  /*      System.out.println("flag cheque"+flagMontantCheque);
             System.out.println("flag espece"+flagMontantEspece);
              System.out.println("flag traite"+flagMontantTraite);
               System.out.println("flag virement"+flagMontantVirement);
                System.out.println("flag code"+flagCodeClientF);
                 System.out.println("flag banque"+flagBanque);
                  System.out.println("flag point vente"+flagPointVente);
                   System.out.println("flag caissier"+flagCaissier);
                    System.out.println("flag reference"+flagRefCheque);
                     System.out.println("flag echeance"+flagecheanceCheque);
                      System.out.println("flag date"+flagDate);*/
         try {
            
            int pageWidth = 0;
          
            if (optionOrient.equals("2")) {
                pageWidth = 842;
            } else {
                pageWidth = 595;
            }
          
         //   System.out.println("optionOrient" + optionOrient);
            JRDesignFrame jrFame = new JRDesignFrame();
            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

            DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d22 = new Date();
            String ddd = dateFormat3.format(d22);
            JasperDesign jasperDesign = new JasperDesign();


            JRDesignBand bandTiltle = new JRDesignBand();

            JRDesignStaticText libelleDateSys = new JRDesignStaticText();
            libelleDateSys.setWidth(150);
            libelleDateSys.setHeight(20);
            libelleDateSys.setText("DATE : " + ddd);
            libelleDateSys.setFontSize(10);
            libelleDateSys.setForecolor(Color.BLACK);
            libelleDateSys.setX(440);
            libelleDateSys.setY(10);
            jrFame.addElement(libelleDateSys);

            JRDesignBand bandBlanc = new JRDesignBand();
            bandBlanc.setHeight(200);
            JRDesignStaticText libellePgeB = new JRDesignStaticText();
            libellePgeB.setWidth(50);
            libellePgeB.setHeight(20);
            libellePgeB.setX(0);
            libellePgeB.setY(30);
            libellePgeB.setText("Tel :");
            libellePgeB.setFontSize(12);
            libellePgeB.setForecolor(Color.BLACK);
            bandBlanc.addElement(libellePgeB);
            jasperDesign.setNoData(bandBlanc);
            
            dateDebut.setHours(0);
            dateDebut.setMinutes(0);
            dateDebut.setSeconds(0);
            dateFin.setHours(23);
            dateFin.setMinutes(59);
            dateFin.setSeconds(59);

            JRDesignBand bandSomme = new JRDesignBand();
            bandSomme.setHeight(80);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String d1 = dateFormat.format(dateDebut);
            String d2 = dateFormat.format(dateFin);
            DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            String d111 = dateFormat1.format(dateDebut);
            String d222 = dateFormat1.format(dateFin);
            Xligne = 0;
    //        System.out.println("d1" + d1);
     //        System.out.println("d2 "+d2);
            etablirconnection();
            JRDesignQuery query = new JRDesignQuery();
            String Filt_typeReg = "";
            String ty = "";

            //     and (mode_reglement_com.`NOM_MR` = '"+ty+"' "+Filt_typeReg+") 
            
        for (int j = 0; j < colonneschoisies.getTarget().size(); j++) {
        //    System.out.println("entree boucle");
        //    System.out.println("target"+colonneschoisies.getTarget().get(j));
           if (colonneschoisies.getTarget().get(j).equals("CHEQUE"))
                    {
            //            System.out.println("cheque existe");
                        flagMontantCheque=true ;
                    }
              if (colonneschoisies.getTarget().get(j).equals("ESPECE"))
                    {
            //            System.out.println("espece existe");
                        flagMontantEspece=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("TRAITE"))
                    {
            //            System.out.println("TRAITE existe");
                        flagMontantTraite=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("VIREMENT"))
                    {
              //          System.out.println("VIREMENT existe");
                        flagMontantVirement=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("CODE"))
                    {
            //            System.out.println("CODE existe");
                        flagCodeClientF=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("BANQUE"))
                    {
               //         System.out.println("BANQUE existe");
                        flagBanque=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("PT VENTE"))
                    {
                  //      System.out.println("PT VENTE existe");
                        flagPointVente=true ;
                    }

if (colonneschoisies.getTarget().get(j).equals("CAISSIER"))
                    {
                  //      System.out.println("CAISSIER existe");
                        flagCaissier=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("REFERENCE"))
                    {
                 //       System.out.println("REFERENCE existe");
                        flagRefCheque=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("ECHEANCE"))
                    {
                 //       System.out.println("ECHEANCE existe");
                        flagecheanceCheque=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("RAISON SOCIALE"))
                    {
                  //      System.out.println("RAISON SOCIALE existe");
                   flagNomClientF=true;
                    }
if (colonneschoisies.getTarget().get(j).equals("LIBELLE OPERATION"))
                    {
                  //      System.out.println("RAISON SOCIALE existe");
                   flagLibelleOperation=true;
                    }
 flagDate=false;
if (colonneschoisies.getTarget().get(j).equals("DATE"))
                    {
                  //      System.out.println("Date existe");
                   flagDate=true;
                    }
           

           // System.out.println("compteur j "+j);
           
        }
        /* System.out.println("apres test");
  System.out.println("flag cheque"+flagMontantCheque);
             System.out.println("flag espece"+flagMontantEspece);
              System.out.println("flag traite"+flagMontantTraite);
               System.out.println("flag virement"+flagMontantVirement);
                System.out.println("flag code"+flagCodeClientF);
                 System.out.println("flag banque"+flagBanque);
                  System.out.println("flag point vente"+flagPointVente);
                   System.out.println("flag caissier"+flagCaissier);
                    System.out.println("flag reference"+flagRefCheque);
                     System.out.println("flag echeance"+flagecheanceCheque);
                      System.out.println("flag date"+flagDate);*/
            if ((flagMontantCheque == true) || (flagMontantTraite == true) || (flagMontantEspece == true) || (flagMontantVirement == true)) {
                Filt_typeReg = Filt_typeReg + " and ( ";

                if (flagMontantCheque == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'cheque'";
                }
                if (flagMontantTraite == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'traite'";
                }
                if (flagMontantEspece == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'espece'";
                }
                if (flagMontantVirement == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'virement'";
                }
                Filt_typeReg = Filt_typeReg + " )";


            }
            System.out.println(Filt_typeReg);
          
            String q1 = "";

            if (filtreTiersEtat != null) {
                q1 = " and operation_com.`NI_TI` =" + filtreTiersEtat.getId();
            }
                String q = "SELECT DATE_SYS,point_vente_com.`NOM_PV` AS point_vente_com_NOM_PV,caissier_com.`NOM_CAI` AS caissier_com_NOM_CAI,operation_com.`NUM_OP` AS operation_com_NUM_OP,operation_com.`DATE_OP` AS operation_com_DATE_OP,tiers_com.`CODE_TI` AS tiers_com_CODE_TI,tiers_com.`NOM_TI` AS tiers_com_NOM_TI,type_op_com.`NOM_TOP` AS type_op_com_NOM_TOP,operation_com.`MONTANT_COP` AS operation_com_MONTANT_COP,operation_com.`NI_MR` AS operation_com_NI_MR,mode_reglement_com.`NI_MR` AS mode_reglement_com_NI_MR,mode_reglement_com.`CODE_MR` AS mode_reglement_com_CODE_MR,mode_reglement_com.`NOM_MR` AS mode_reglement_com_NOM_MR,operation_com.`REFERENCE_OP` AS operation_com_REFERENCE_OP,banque_com.`NOM_BANQUE` AS banque_com_NOM_BANQUE,MONTANT_ES,MONTANT_TR,MONTANT_VIR,MONTANT_CH,ECHANCE_OP , horizon_sys.`id` AS horizon_sys_id,horizon_sys.`CODE` AS horizon_sys_CODE,horizon_sys.`LIBELLE_SOCIETE` AS horizon_sys_LIBELLE_SOCIETE, horizon_sys.`ADRESSE` AS horizon_sys_ADRESSE,horizon_sys.`FAX` AS horizon_sys_FAX,horizon_sys.`TEL` AS horizon_sys_TEL FROM `tiers_com` tiers_com INNER JOIN `operation_com` operation_com ON tiers_com.`NI_TI` = operation_com.`NI_TI` INNER JOIN `type_op_com` type_op_com ON operation_com.`NI_TOP` = type_op_com.`NI_TOP` INNER JOIN `mode_reglement_com` mode_reglement_com ON operation_com.`NI_MR` = mode_reglement_com.`NI_MR` INNER JOIN `point_vente_com` point_vente_com ON operation_com.`NI_PV` = point_vente_com.`NI_PV` LEFT JOIN `caissier_com` caissier_com ON operation_com.`NI_CAI` = caissier_com.`NI_CAI` LEFT JOIN `banque_com` banque_com ON operation_com.`NI_BANQUE` = banque_com.`NI_BANQUE` ,`horizon_sys` horizon_sys  WHERE type_op_com.`NOM_TOP` = 'reglement' " + Filt_typeReg + " and DATE_SYS >= '" + d1 + "' and DATE_SYS <= '" + d2 + "'" + q1 + " order by DATE_SYS ";


            query.setText(q);
            jasperDesign.setQuery(query);
            jasperDesign.setName("ReglementDesign");

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

            jrFame.setHeight(72);
            jrFame.setWidth(842);
            jrFame.getLineBox().setLeftPadding(0);
            jrFame.getLineBox().setTopPadding(0);
            jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

            JRDesignStaticText libelleTitre = new JRDesignStaticText();
            libelleTitre.setWidth(360);
            libelleTitre.setHeight(31);
            libelleTitre.setX(200);
            libelleTitre.setY(30);
            libelleTitre.setText("ETAT REGLEMENTS CLIENTS");
            libelleTitre.setFontSize(18);
            libelleTitre.setForecolor(Color.BLUE);
            jrFame.addElement(libelleTitre);

            JRDesignStaticText libelleTotal = new JRDesignStaticText();
            libelleTotal.setWidth(310);
            libelleTotal.setHeight(31);
            libelleTotal.setX(0);
            libelleTotal.setY(7);
            libelleTotal.setText("TOTAL ");
            libelleTotal.setFontSize(12);
            libelleTotal.setForecolor(Color.BLACK);
            bandSomme.addElement(libelleTotal);


            JRDesignStaticText libelleDate = new JRDesignStaticText();
            libelleDate.setWidth(200);
            libelleDate.setHeight(15);
            libelleDate.setText(" PERIODE : Du  " + d111 + "  Au  " + d222);
            libelleDate.setFontSize(10);
            libelleDate.setForecolor(Color.BLACK);
            libelleDate.setX(200);
            libelleDate.setY(55);
            jrFame.addElement(libelleDate);
            bandTiltle.setHeight(82);


            /**************************************************/
            JRDesignField fieldSTE = new JRDesignField();
            fieldSTE.setName("horizon_sys_LIBELLE_SOCIETE");
            fieldSTE.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldSTE);

            JRDesignTextField textFieldSTE = new JRDesignTextField();
            textFieldSTE.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldSTE.setStretchWithOverflow(true);
            textFieldSTE.setFontSize(10);
            textFieldSTE.setWidth(300);
            textFieldSTE.setX(0);
            textFieldSTE.setY(0);
            JRDesignExpression expressionSTE = new JRDesignExpression();
            expressionSTE.setValueClass(java.lang.String.class);
            expressionSTE.setText("$F{horizon_sys_LIBELLE_SOCIETE}");
            textFieldSTE.setExpression(expressionSTE);

            jrFame.addElement(textFieldSTE);

            /*******************************************************************/
            JRDesignField fieldAdresse = new JRDesignField();
            fieldAdresse.setName("horizon_sys_Adresse");
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
            expressionAdresse.setText("$F{horizon_sys_Adresse}");
            textFieldAdresse.setExpression(expressionAdresse);

            jrFame.addElement(textFieldAdresse);

            /*************************************************************/
            JRDesignStaticText libelleTEL = new JRDesignStaticText();
            libelleTEL.setWidth(50);
            libelleTEL.setHeight(20);
            libelleTEL.setX(2);
            libelleTEL.setY(30);
            libelleTEL.setText("Tel  :");
            libelleTEL.setFontSize(10);
            libelleTEL.setForecolor(Color.BLACK);
            jrFame.addElement(libelleTEL);

            JRDesignField fieldTel = new JRDesignField();
            fieldTel.setName("horizon_sys_Tel");
            fieldTel.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldTel);

            JRDesignTextField textFieldTel = new JRDesignTextField();
            textFieldTel.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldTel.setStretchWithOverflow(true);
            textFieldTel.setFontSize(10);
            textFieldTel.setWidth(100);
            textFieldTel.setX(30);
            textFieldTel.setY(30);
            JRDesignExpression expressionTel = new JRDesignExpression();
            expressionTel.setValueClass(java.lang.String.class);
            expressionTel.setText("$F{horizon_sys_Tel}");
            textFieldTel.setExpression(expressionTel);

            jrFame.addElement(textFieldTel);
            /*******************************************************/
            /*********************************************************/
            JRDesignStaticText libelleFax = new JRDesignStaticText();
            libelleFax.setWidth(50);
            libelleFax.setHeight(20);
            libelleFax.setX(2);
            libelleFax.setY(45);
            libelleFax.setText("Fax :");
            libelleFax.setFontSize(10);
            libelleFax.setForecolor(Color.BLACK);
            jrFame.addElement(libelleFax);

            JRDesignField fieldFax = new JRDesignField();
            fieldFax.setName("horizon_sys_Fax");
            fieldFax.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldFax);

            JRDesignTextField textFieldFax = new JRDesignTextField();
            textFieldFax.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldFax.setStretchWithOverflow(true);
            textFieldFax.setFontSize(10);
            textFieldFax.setWidth(100);
            textFieldFax.setX(30);
            textFieldFax.setY(45);
            JRDesignExpression expressionFax = new JRDesignExpression();
            expressionFax.setValueClass(java.lang.String.class);
            expressionFax.setText("$F{horizon_sys_Fax}");
            textFieldFax.setExpression(expressionFax);

            jrFame.addElement(textFieldFax);
            /********************************/
            bandTiltle.addElement(jrFame);
            jasperDesign.setTitle(bandTiltle);
            /*******************************************************/
             JRDesignBand bandHeader = new JRDesignBand();
            BigDecimal bandHeaderHeight = new BigDecimal(15).setScale(0, RoundingMode.DOWN);
            bandHeader.setHeight(bandHeaderHeight.intValue());
            //    bandHeader.setHeight(100);


            JRDesignBand columnHeader = new JRDesignBand();
            columnHeader.setHeight(15);
            List<String> Colonneslibelleschoix = new LinkedList<String>();
            for (int j = 0; j < colonneschoisies.getTarget().size(); j++) {
                JRDesignStaticText Jrdeslibelle = new JRDesignStaticText();

                String expressioncolonne = colonneschoisies.getTarget().get(j);
                Colonneslibelleschoix.add(expressioncolonne);
                //condition
                   if (expressioncolonne.equals("DATE")) {
                    expressioncolonne = "DATE_SYS";

                }
                if (expressioncolonne.equals("ESPECE")) {
                    expressioncolonne = "MONTANT_ES";

                }
                if (expressioncolonne.equals("CHEQUE")) {
                    expressioncolonne = "MONTANT_CH";

                }

                if (expressioncolonne.equals("TRAITE")) {
                    expressioncolonne = "MONTANT_TR";

                }
                if (expressioncolonne.equals("VIREMENT")) {
                    expressioncolonne = "MONTANT_VIR";

                }
                if (expressioncolonne.equals("RAISON SOCIALE")) {
                    expressioncolonne = "tiers_com_NOM_TI";

                }
                if (expressioncolonne.equals("CODE")) {
                    expressioncolonne = "tiers_com_CODE_TI";

                }
                if (expressioncolonne.equals("BANQUE")) {
                    expressioncolonne = "banque_com_NOM_BANQUE";

                }
                if (expressioncolonne.equals("PT VENTE")) {
                    expressioncolonne = "point_vente_com_NOM_PV";

                }
                if (expressioncolonne.equals("CAISSIER")) {
                    expressioncolonne = "caissier_com_NOM_CAI";

                }
                 if (expressioncolonne.equals("REFERENCE")) {
                    expressioncolonne = "operation_com_REFERENCE_OP";

                }
                   if (expressioncolonne.equals("ECHEANCE")) {
                    expressioncolonne = "ECHANCE_OP";

                }


            if ((colonneschoisies.getTarget().get(j).equals("DATE"))||((colonneschoisies.getTarget().get(j).equals("ECHEANCE")))) {
           //     System.out.println("start with date verifiee");
                styleLibelleColumnHeaderDrag(jasperDesign, bandHeader, expressioncolonne, "$F{" + expressioncolonne + "}", Jrdeslibelle, colonneschoisies.getTarget().get(j), 60, 60, bandSomme);

                columnHeader.addElement(Jrdeslibelle);

            } else {

                styleLibelleColumnHeaderDrag(jasperDesign, bandHeader, expressioncolonne, "(($F{" + expressioncolonne + "}!=null)&&($F{" + expressioncolonne + "}.isEmpty()!=true)&&(($F{" + expressioncolonne + "}.equals(\"0.000\")!=true)))?$F{" + expressioncolonne + "}:Character.toString(' ')", Jrdeslibelle, colonneschoisies.getTarget().get(j), 70, 120, bandSomme);
//                 if ((colonneschoisies.getTarget().get(j).equals("ESPECE"))||((colonneschoisies.getTarget().get(j).equals("CHEQUE")))||(colonneschoisies.getTarget().get(j).equals("VIREMENT"))||((colonneschoisies.getTarget().get(j).equals("TRAITE")))) {
//                       styleLibelleColumnHeaderDrag(jasperDesign, bandHeader, expressioncolonne, "(($F{" + expressioncolonne + "}.equals(\"0.000\"))?$F{" + expressioncolonne + "}:Character.toString('-'))", Jrdeslibelle, colonneschoisies.getTarget().get(j), 70, 120, bandSomme);
////                 
//                 }

                columnHeader.addElement(Jrdeslibelle);
            }

            }

            colonneschoisies.setTarget(Colonneslibelleschoix);

            jasperDesign.setColumnHeader(columnHeader);
          //  jasperDesign.setDetail(bandHeader);
                  ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandHeader);
             jasperDesign.setSummary(bandSomme);
            

            /***********************************Somme Total***************************************/
            //  JRDesignTextField textFieldSomme = new JRDesignTextField(); 
            JRDesignField montant = new JRDesignField();
            montant.setName("operation_com_MONTANT_COP");
            montant.setValueClass(java.math.BigDecimal.class);
            jasperDesign.addField(montant);




            JRDesignVariable varSomme = new JRDesignVariable();
            varSomme.setName("varTotal");
            varSomme.setValueClass(java.math.BigDecimal.class);
            varSomme.setCalculation(CalculationEnum.getByValue(new Byte("2")));
            jasperDesign.addVariable(varSomme);

            JRDesignExpression expressionE = new JRDesignExpression();
            expressionE.setValueClass(java.math.BigDecimal.class);
            expressionE.setText("$F{operation_com_MONTANT_COP}");
            varSomme.setExpression(expressionE);


            JRDesignExpression expressionEI = new JRDesignExpression();
            expressionEI.setValueClass(java.math.BigDecimal.class);
            expressionEI.setText("new BigDecimal(0).setScale(3, RoundingMode.UP)");

            varSomme.setInitialValueExpression(expressionEI);


            JRDesignTextField textField = new JRDesignTextField();
            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textField.setStretchWithOverflow(true);
            textField.setFontSize(12);
            textField.setWidth(100);
            textField.setX(75);
            textField.setY(45);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setValueClass(java.math.BigDecimal.class);
            expression.setText("$V{varTotal}");
            textField.setExpression(expression);
            textField.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));


            JRDesignStaticText libelleTotalF = new JRDesignStaticText();
            libelleTotalF.setWidth(150);
            libelleTotalF.setHeight(31);
            libelleTotalF.setX(0);
            libelleTotalF.setY(45);
            libelleTotalF.setText("TOTAL GENERAL :");
            libelleTotalF.setFontSize(12);
            libelleTotalF.setForecolor(Color.BLUE);
            bandSomme.addElement(libelleTotalF);


            bandSomme.addElement(textField);
            /*************************************************************/
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
            /*************************************************************************************************/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap parameters = new HashMap();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

            DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
            Date d = new Date();
            String dd = dateFormat15.format(d);
            String nomFichier = "Rapport Reglement " + dd;
//            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
//            FacesContext context = FacesContext.getCurrentInstance();
//            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
//            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
//            response.addHeader("Content-disposition", "attachment;filename=" + nomFichier + " .pdf");
//            response.setContentLength(bytes.length);
//            response.getOutputStream().write(bytes);
//            response.setContentType("application/pdf");
//            context.responseComplete();
                    JRRtfExporter exporter = new JRRtfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, nomFichier);         
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
            exporter.exportReport();
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment;filename=" + nomFichier);
            response.setContentLength(byteArrayOutputStream.toByteArray().length);
            response.getOutputStream().write(byteArrayOutputStream.toByteArray());
            response.setContentType("application/rtf");
            context.responseComplete();
        } catch (Exception ex) {
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 ////////////////////////////// Etat Reglement Fact Bl ///////////////////////
    
    public void jasperDesignModelFactBl() throws JRException, IOException {
         try {
            
            int pageWidth = 0;
          
            if (optionOrient.equals("2")) {
                pageWidth = 842;
            } else {
                pageWidth = 595;
            }
          
            System.out.println("optionOrient" + optionOrient);
            JRDesignFrame jrFame = new JRDesignFrame();
            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

            DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d22 = new Date();
            String ddd = dateFormat3.format(d22);
            JasperDesign jasperDesign = new JasperDesign();


            JRDesignBand bandTiltle = new JRDesignBand();

            JRDesignStaticText libelleDateSys = new JRDesignStaticText();
            libelleDateSys.setWidth(150);
            libelleDateSys.setHeight(20);
            libelleDateSys.setText("DATE : " + ddd);
            libelleDateSys.setFontSize(10);
            libelleDateSys.setForecolor(Color.BLACK);
            libelleDateSys.setX(440);
            libelleDateSys.setY(10);
            jrFame.addElement(libelleDateSys);

            JRDesignBand bandBlanc = new JRDesignBand();
            bandBlanc.setHeight(200);
//            JRDesignStaticText libellePgeB = new JRDesignStaticText();
//            libellePgeB.setWidth(50);
//            libellePgeB.setHeight(20);
//            libellePgeB.setX(0);
//            libellePgeB.setY(30);
//            libellePgeB.setText("Tel :");
//            libellePgeB.setFontSize(12);
//            libellePgeB.setForecolor(Color.BLACK);
//            bandBlanc.addElement(libellePgeB);
            jasperDesign.setNoData(bandBlanc);
            
            dateDebut.setHours(0);
            dateDebut.setMinutes(0);
            dateDebut.setSeconds(0);
            dateFin.setHours(23);
            dateFin.setMinutes(59);
            dateFin.setSeconds(59);

            JRDesignBand bandSomme = new JRDesignBand();
            bandSomme.setHeight(80);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String d1 = dateFormat.format(dateDebut);
            String d2 = dateFormat.format(dateFin);
            DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            String d111 = dateFormat1.format(dateDebut);
            String d222 = dateFormat1.format(dateFin);
            Xligne = 0;
          //  System.out.println("d1" + d1);
           //  System.out.println("d2 "+d2);
            etablirconnection();
            JRDesignQuery query = new JRDesignQuery();
            String Filt_typeReg = "";
          
            String ty = "";

            //     and (mode_reglement_com.`NOM_MR` = '"+ty+"' "+Filt_typeReg+") 
            
        for (int j = 0; j < colonneschoisies.getTarget().size(); j++) {
           
           if (colonneschoisies.getTarget().get(j).equals("CHEQUE"))
                    {
                    
                        flagMontantCheque=true ;
                    }
              if (colonneschoisies.getTarget().get(j).equals("ESPECE"))
                    {
                      
                        flagMontantEspece=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("TRAITE"))
                    {
                       
                        flagMontantTraite=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("VIREMENT"))
                    {
                        
                        flagMontantVirement=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("CODE"))
                    {
                       
                        flagCodeClientF=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("BANQUE"))
                    {
                      
                        flagBanque=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("PT VENTE"))
                    {
                      
                        flagPointVente=true ;
                    }

if (colonneschoisies.getTarget().get(j).equals("CAISSIER"))
                    {
                       
                        flagCaissier=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("REFERENCE"))
                    {
                        System.out.println("REFERENCE existe");
                        flagRefCheque=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("ECHEANCE"))
                    {
                       
                        flagecheanceCheque=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("RAISON SOCIALE"))
                    {
                        System.out.println("RAISON SOCIALE existe");
                   flagNomClientF=true;
                    }
 flagDate=false;
if (colonneschoisies.getTarget().get(j).equals("DATE"))
                    {
                      
                   flagDate=true;
                    }
           

          
           
        }
       
            if ((flagMontantCheque == true) || (flagMontantTraite == true) || (flagMontantEspece == true) || (flagMontantVirement == true)) {
                Filt_typeReg = Filt_typeReg + " and ( ";

                if (flagMontantCheque == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'cheque'";
                }
                if (flagMontantTraite == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'traite'";
                }
                if (flagMontantEspece == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'espece'";
                }
                if (flagMontantVirement == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'virement'";
                }
                Filt_typeReg = Filt_typeReg + " )";


            }
//            if(bl.equalsIgnoreCase("sans bl"))
//            {
//              Reqbl=" and operation_com.`NI_OP` in (SELECT distinct(l.ni_op) FROM `lettrage_com` l where l.type_Op_al in (11,12,2)) ";
//            }
      //      System.out.println(Filt_typeReg);
          
            String q1 = "";

            if (filtreTiersEtat != null) {
                q1 = " and operation_com.`NI_TI` =" + filtreTiersEtat.getId();
            }
                String q = "SELECT operation_com.DATE_SYS,point_vente_com.`NOM_PV` AS point_vente_com_NOM_PV,caissier_com.`NOM_CAI` AS caissier_com_NOM_CAI,operation_com.`NUM_OP` AS operation_com_NUM_OP,operation_com.`DATE_OP` AS operation_com_DATE_OP,tiers_com.`CODE_TI` AS tiers_com_CODE_TI,tiers_com.`NOM_TI` AS tiers_com_NOM_TI,type_op_com.`NOM_TOP` AS type_op_com_NOM_TOP,operation_com.`MONTANT_COP` AS operation_com_MONTANT_COP,operation_com.`NI_MR` AS operation_com_NI_MR,mode_reglement_com.`NI_MR` AS mode_reglement_com_NI_MR,mode_reglement_com.`CODE_MR` AS mode_reglement_com_CODE_MR,mode_reglement_com.`NOM_MR` AS mode_reglement_com_NOM_MR,operation_com.`REFERENCE_OP` AS operation_com_REFERENCE_OP,banque_com.`NOM_BANQUE` AS banque_com_NOM_BANQUE,operation_com.MONTANT_ES,operation_com.MONTANT_TR,operation_com.MONTANT_VIR,operation_com.MONTANT_CH,operation_com.ECHANCE_OP ,IF(count(lettrage_com.ni_Let)=1,ifnull(o2.num_op,' ') ,concat(ifnull(o2.num_op,' '),'*')) as numeroFacture, horizon_sys.`id` AS horizon_sys_id,horizon_sys.`CODE` AS horizon_sys_CODE,horizon_sys.`LIBELLE_SOCIETE` AS horizon_sys_LIBELLE_SOCIETE, horizon_sys.`ADRESSE` AS horizon_sys_ADRESSE,horizon_sys.`FAX` AS horizon_sys_FAX,horizon_sys.`TEL` AS horizon_sys_TEL FROM `tiers_com` tiers_com INNER JOIN `operation_com` operation_com ON tiers_com.`NI_TI` = operation_com.`NI_TI` INNER JOIN `type_op_com` type_op_com ON operation_com.`NI_TOP` = type_op_com.`NI_TOP` INNER JOIN `mode_reglement_com` mode_reglement_com ON operation_com.`NI_MR` = mode_reglement_com.`NI_MR` INNER JOIN `point_vente_com` point_vente_com ON operation_com.`NI_PV` = point_vente_com.`NI_PV` LEFT JOIN `caissier_com` caissier_com ON operation_com.`NI_CAI` = caissier_com.`NI_CAI` LEFT JOIN `banque_com` banque_com ON operation_com.`NI_BANQUE` = banque_com.`NI_BANQUE` left join lettrage_com lettrage_com on operation_com.ni_op=lettrage_com.NI_OP left join operation_com o2 on o2.ni_op=lettrage_com.NI_PIECE ,`horizon_sys` horizon_sys  WHERE type_op_com.`NOM_TOP` = 'reglement' " + Filt_typeReg + " and operation_com.DATE_SYS >= '" + d1 + "' and operation_com.DATE_SYS <= '" + d2 + "'" + q1+ " group by operation_com.ni_op  order by DATE_SYS ";

            
            
            
          
            System.out.println("q "+q);
            query.setText(q);
            jasperDesign.setQuery(query);
            jasperDesign.setName("ReglementDesign");

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

            jrFame.setHeight(72);
            jrFame.setWidth(842);
            jrFame.getLineBox().setLeftPadding(0);
            jrFame.getLineBox().setTopPadding(0);
            jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

            JRDesignStaticText libelleTitre = new JRDesignStaticText();
            libelleTitre.setWidth(360);
            libelleTitre.setHeight(31);
            libelleTitre.setX(200);
            libelleTitre.setY(30);
            libelleTitre.setText("ETAT REGLEMENTS CLIENTS FACT/BL");
            
            libelleTitre.setFontSize(18);
            libelleTitre.setForecolor(Color.BLUE);
            jrFame.addElement(libelleTitre);

            JRDesignStaticText libelleTotal = new JRDesignStaticText();
            libelleTotal.setWidth(310);
            libelleTotal.setHeight(31);
            libelleTotal.setX(0);
            libelleTotal.setY(7);
            libelleTotal.setText("TOTAL ");
            libelleTotal.setFontSize(12);
            libelleTotal.setForecolor(Color.BLACK);
            bandSomme.addElement(libelleTotal);


            JRDesignStaticText libelleDate = new JRDesignStaticText();
            libelleDate.setWidth(200);
            libelleDate.setHeight(15);
            libelleDate.setText(" PERIODE : Du  " + d111 + "  Au  " + d222);
            libelleDate.setFontSize(10);
            libelleDate.setForecolor(Color.BLACK);
            libelleDate.setX(200);
            libelleDate.setY(55);
            jrFame.addElement(libelleDate);
            bandTiltle.setHeight(82);


            /**************************************************/
            JRDesignField fieldSTE = new JRDesignField();
            fieldSTE.setName("horizon_sys_LIBELLE_SOCIETE");
            fieldSTE.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldSTE);

            JRDesignTextField textFieldSTE = new JRDesignTextField();
            textFieldSTE.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldSTE.setStretchWithOverflow(true);
            textFieldSTE.setFontSize(10);
            textFieldSTE.setWidth(300);
            textFieldSTE.setX(0);
            textFieldSTE.setY(0);
            JRDesignExpression expressionSTE = new JRDesignExpression();
            expressionSTE.setValueClass(java.lang.String.class);
            expressionSTE.setText("$F{horizon_sys_LIBELLE_SOCIETE}");
            textFieldSTE.setExpression(expressionSTE);

            jrFame.addElement(textFieldSTE);

            /*******************************************************************/
            JRDesignField fieldAdresse = new JRDesignField();
            fieldAdresse.setName("horizon_sys_Adresse");
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
            expressionAdresse.setText("$F{horizon_sys_Adresse}");
            textFieldAdresse.setExpression(expressionAdresse);

            jrFame.addElement(textFieldAdresse);

            /*************************************************************/
            JRDesignStaticText libelleTEL = new JRDesignStaticText();
            libelleTEL.setWidth(50);
            libelleTEL.setHeight(20);
            libelleTEL.setX(2);
            libelleTEL.setY(30);
            libelleTEL.setText("Tel  :");
            libelleTEL.setFontSize(10);
            libelleTEL.setForecolor(Color.BLACK);
            jrFame.addElement(libelleTEL);

            JRDesignField fieldTel = new JRDesignField();
            fieldTel.setName("horizon_sys_Tel");
            fieldTel.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldTel);

            JRDesignTextField textFieldTel = new JRDesignTextField();
            textFieldTel.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldTel.setStretchWithOverflow(true);
            textFieldTel.setFontSize(10);
            textFieldTel.setWidth(400);
            textFieldTel.setX(30);
            textFieldTel.setY(30);
            JRDesignExpression expressionTel = new JRDesignExpression();
            expressionTel.setValueClass(java.lang.String.class);
            expressionTel.setText("$F{horizon_sys_Tel}");
            textFieldTel.setExpression(expressionTel);

            jrFame.addElement(textFieldTel);
            /*******************************************************/
            /*********************************************************/
            JRDesignStaticText libelleFax = new JRDesignStaticText();
            libelleFax.setWidth(50);
            libelleFax.setHeight(20);
            libelleFax.setX(2);
            libelleFax.setY(45);
            libelleFax.setText("Fax :");
            libelleFax.setFontSize(10);
            libelleFax.setForecolor(Color.BLACK);
            jrFame.addElement(libelleFax);

            JRDesignField fieldFax = new JRDesignField();
            fieldFax.setName("horizon_sys_Fax");
            fieldFax.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldFax);

            JRDesignTextField textFieldFax = new JRDesignTextField();
            textFieldFax.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldFax.setStretchWithOverflow(true);
            textFieldFax.setFontSize(10);
            textFieldFax.setWidth(100);
            textFieldFax.setX(30);
            textFieldFax.setY(45);
            JRDesignExpression expressionFax = new JRDesignExpression();
            expressionFax.setValueClass(java.lang.String.class);
            expressionFax.setText("$F{horizon_sys_Fax}");
            textFieldFax.setExpression(expressionFax);

            jrFame.addElement(textFieldFax);
            /********************************/
            bandTiltle.addElement(jrFame);
            jasperDesign.setTitle(bandTiltle);
            /*******************************************************/
             JRDesignBand bandHeader = new JRDesignBand();
            BigDecimal bandHeaderHeight = new BigDecimal(15).setScale(0, RoundingMode.DOWN);
            bandHeader.setHeight(bandHeaderHeight.intValue());
            //    bandHeader.setHeight(100);


            JRDesignBand columnHeader = new JRDesignBand();
            columnHeader.setHeight(15);
            List<String> Colonneslibelleschoix = new LinkedList<String>();
            for (int j = 0; j < colonneschoisies.getTarget().size(); j++) {
                JRDesignStaticText Jrdeslibelle = new JRDesignStaticText();

                String expressioncolonne = colonneschoisies.getTarget().get(j);
                Colonneslibelleschoix.add(expressioncolonne);
                //condition
                   if (expressioncolonne.equals("DATE")) {
                    expressioncolonne = "DATE_SYS";

                }
                if (expressioncolonne.equals("ESPECE")) {
                    expressioncolonne = "MONTANT_ES";

                }
                if (expressioncolonne.equals("CHEQUE")) {
                    expressioncolonne = "MONTANT_CH";

                }

                if (expressioncolonne.equals("TRAITE")) {
                    expressioncolonne = "MONTANT_TR";

                }
                if (expressioncolonne.equals("VIREMENT")) {
                    expressioncolonne = "MONTANT_VIR";

                }
                if (expressioncolonne.equals("RAISON SOCIALE")) {
                    expressioncolonne = "tiers_com_NOM_TI";

                }
                if (expressioncolonne.equals("CODE")) {
                    expressioncolonne = "tiers_com_CODE_TI";

                }
                if (expressioncolonne.equals("BANQUE")) {
                    expressioncolonne = "banque_com_NOM_BANQUE";

                }
                if (expressioncolonne.equals("PT VENTE")) {
                    expressioncolonne = "point_vente_com_NOM_PV";

                }
                if (expressioncolonne.equals("CAISSIER")) {
                    expressioncolonne = "caissier_com_NOM_CAI";

                }
                 if (expressioncolonne.equals("REFERENCE")) {
                    expressioncolonne = "operation_com_REFERENCE_OP";

                }
                   if (expressioncolonne.equals("ECHEANCE")) {
                    expressioncolonne = "ECHANCE_OP";

                }

                if (expressioncolonne.equals("LETTRAGE")) {
                    expressioncolonne = "numeroFacture";

                }
            if ((colonneschoisies.getTarget().get(j).equals("DATE"))||((colonneschoisies.getTarget().get(j).equals("ECHEANCE")))) {
             //   System.out.println("start with date verifiee");
                styleLibelleColumnHeaderDrag(jasperDesign, bandHeader, expressioncolonne, "$F{" + expressioncolonne + "}", Jrdeslibelle, colonneschoisies.getTarget().get(j), 60, 60, bandSomme);

                columnHeader.addElement(Jrdeslibelle);

            } else {

                styleLibelleColumnHeaderDrag(jasperDesign, bandHeader, expressioncolonne, "(($F{" + expressioncolonne + "}!=null)&&($F{" + expressioncolonne + "}.isEmpty()!=true)&&(($F{" + expressioncolonne + "}.equals(\"0.000\")!=true)))?$F{" + expressioncolonne + "}:Character.toString(' ')", Jrdeslibelle, colonneschoisies.getTarget().get(j), 70, 120, bandSomme);
//                 if ((colonneschoisies.getTarget().get(j).equals("ESPECE"))||((colonneschoisies.getTarget().get(j).equals("CHEQUE")))||(colonneschoisies.getTarget().get(j).equals("VIREMENT"))||((colonneschoisies.getTarget().get(j).equals("TRAITE")))) {
//                       styleLibelleColumnHeaderDrag(jasperDesign, bandHeader, expressioncolonne, "(($F{" + expressioncolonne + "}.equals(\"0.000\"))?$F{" + expressioncolonne + "}:Character.toString('-'))", Jrdeslibelle, colonneschoisies.getTarget().get(j), 70, 120, bandSomme);
////                 
//                 }

                columnHeader.addElement(Jrdeslibelle);
            }

            }

            colonneschoisies.setTarget(Colonneslibelleschoix);

            jasperDesign.setColumnHeader(columnHeader);
          //  jasperDesign.setDetail(bandHeader);
                  ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandHeader);
             jasperDesign.setSummary(bandSomme);
            

            /***********************************Somme Total***************************************/
            //  JRDesignTextField textFieldSomme = new JRDesignTextField(); 
            JRDesignField montant = new JRDesignField();
            montant.setName("operation_com_MONTANT_COP");
            montant.setValueClass(java.math.BigDecimal.class);
            jasperDesign.addField(montant);




            JRDesignVariable varSomme = new JRDesignVariable();
            varSomme.setName("varTotal");
            varSomme.setValueClass(java.math.BigDecimal.class);
            varSomme.setCalculation(CalculationEnum.getByValue(new Byte("2")));
            jasperDesign.addVariable(varSomme);

            JRDesignExpression expressionE = new JRDesignExpression();
            expressionE.setValueClass(java.math.BigDecimal.class);
            expressionE.setText("$F{operation_com_MONTANT_COP}");
            varSomme.setExpression(expressionE);


            JRDesignExpression expressionEI = new JRDesignExpression();
            expressionEI.setValueClass(java.math.BigDecimal.class);
            expressionEI.setText("new BigDecimal(0).setScale(3, RoundingMode.UP)");

            varSomme.setInitialValueExpression(expressionEI);


            JRDesignTextField textField = new JRDesignTextField();
            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textField.setStretchWithOverflow(true);
            textField.setFontSize(12);
            textField.setWidth(100);
            textField.setX(75);
            textField.setY(45);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setValueClass(java.math.BigDecimal.class);
            expression.setText("$V{varTotal}");
            textField.setExpression(expression);
            textField.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));


            JRDesignStaticText libelleTotalF = new JRDesignStaticText();
            libelleTotalF.setWidth(150);
            libelleTotalF.setHeight(31);
            libelleTotalF.setX(0);
            libelleTotalF.setY(45);
            libelleTotalF.setText("TOTAL GENERAL :");
            libelleTotalF.setFontSize(12);
            libelleTotalF.setForecolor(Color.BLUE);
            bandSomme.addElement(libelleTotalF);


            bandSomme.addElement(textField);
            /*************************************************************/
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
            /*************************************************************************************************/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap parameters = new HashMap();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

            DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
            Date d = new Date();
            String dd = dateFormat15.format(d);
            String nomFichier = "Rapport Reglement Fact Bl" + dd;
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment;filename=" + nomFichier + " .pdf");
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.setContentType("application/pdf");
            context.responseComplete();
        } catch (Exception ex) {
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 
    //////////////////////Visite Non Reglé////////////////////////////////////////
    
    /////////////Depenses////////////////////////////////////////////////////////
    public void rapportDepense() throws JRException, IOException {

         try {
            
            int pageWidth = 0;
          
            if (optionOrient.equals("2")) {
                pageWidth = 842;
            } else {
                pageWidth = 595;
            }
          
            System.out.println("optionOrient" + optionOrient);
            JRDesignFrame jrFame = new JRDesignFrame();
            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

            DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d22 = new Date();
            String ddd = dateFormat3.format(d22);
            JasperDesign jasperDesign = new JasperDesign();


            JRDesignBand bandTiltle = new JRDesignBand();

            JRDesignStaticText libelleDateSys = new JRDesignStaticText();
            libelleDateSys.setWidth(150);
            libelleDateSys.setHeight(20);
            libelleDateSys.setText("DATE : " + ddd);
            libelleDateSys.setFontSize(10);
            libelleDateSys.setForecolor(Color.BLACK);
            libelleDateSys.setX(440);
            libelleDateSys.setY(10);
            jrFame.addElement(libelleDateSys);

            JRDesignBand bandBlanc = new JRDesignBand();
            bandBlanc.setHeight(200);
            jasperDesign.setNoData(bandBlanc);
            
            dateDebut.setHours(0);
            dateDebut.setMinutes(0);
            dateDebut.setSeconds(0);
            dateFin.setHours(23);
            dateFin.setMinutes(59);
            dateFin.setSeconds(59);

            JRDesignBand bandSomme = new JRDesignBand();
            bandSomme.setHeight(80);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String d1 = dateFormat.format(dateDebut);
            String d2 = dateFormat.format(dateFin);
            DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            String d111 = dateFormat1.format(dateDebut);
            String d222 = dateFormat1.format(dateFin);
            Xligne = 0;
            etablirconnection();
            JRDesignQuery query = new JRDesignQuery();
            String Filt_typeReg = "";
          
            String ty = "";

            //     and (mode_reglement_com.`NOM_MR` = '"+ty+"' "+Filt_typeReg+") 
            
        for (int j = 0; j < colonneschoisies.getTarget().size(); j++) {
           
           if (colonneschoisies.getTarget().get(j).equals("CHEQUE"))
                    {
                    
                        flagMontantCheque=true ;
                    }
              if (colonneschoisies.getTarget().get(j).equals("ESPECE"))
                    {
                      
                        flagMontantEspece=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("TRAITE"))
                    {
                       
                        flagMontantTraite=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("VIREMENT"))
                    {
                        
                        flagMontantVirement=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("CODE"))
                    {
                       
                        flagCodeClientF=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("BANQUE"))
                    {
                      
                        flagBanque=true ;
                    }

if (colonneschoisies.getTarget().get(j).equals("REFERENCE"))
                    {
                        System.out.println("REFERENCE existe");
                        flagRefCheque=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("ECHEANCE"))
                    {
                       
                        flagecheanceCheque=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("RAISON SOCIALE"))
                    {
                        System.out.println("RAISON SOCIALE existe");
                   flagNomClientF=true;
                    }
if (colonneschoisies.getTarget().get(j).equals("LIBELLE OPERATION"))
                    {
                  //      System.out.println("RAISON SOCIALE existe");
                   flagLibelleOperation=true;
                    }
 flagDate=false;
if (colonneschoisies.getTarget().get(j).equals("DATE"))
                    {
                      
                   flagDate=true;
                    }
           

          
           
        }
       
            if ((flagMontantCheque == true) || (flagMontantTraite == true) || (flagMontantEspece == true) || (flagMontantVirement == true)) {
                Filt_typeReg = Filt_typeReg + " and ( ";

                if (flagMontantCheque == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  modereglement.`libelle` = 'cheque'";
                }
                if (flagMontantTraite == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  modereglement.`libelle` = 'traite'";
                }
                if (flagMontantEspece == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  modereglement.`libelle` = 'espece'";
                }
                if (flagMontantVirement == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  modereglement.`libelle` = 'virement'";
                }
                Filt_typeReg = Filt_typeReg + " )";


            }

            String q1 = "";

            if (filtreTiersEtat != null) {
                q1 = " and operation.`client` =" + filtreTiersEtat.getId();
            }
            String q="SELECT cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal, operation.DATE_SYS, operation.`numerooperation` AS operation_numero_operation, operation.`dateoperation` AS operation_date_operation, operation.`montantcheque` AS operation_montantcheque, operation.`montantespece` AS operation_montantespece, operation.`libelleoperation` AS operation_libelleoperation, operation.`montanttraite` AS operation_montanttraite, operation.`montantvirement` AS operation_montantvirement, operation.`dateechenace` AS operation_dateechenace, operation.`banque` AS operation_banque, operation.`montantcredite` AS operation_montant_credite,operation.`montantdebite` AS operation_montant_debite, operation.`modereglement` AS operation_mode_reglement, operation.`REFERENCE` AS operation_REFERENCE, typeoperation.`libelle` AS typeoperation_libelle, typeoperation.`id` AS typeoperation_id, client.`code` AS client_code, client.`nom` AS client_nom, modereglement.`id` AS mode_reglement_id, modereglement.`CODE` AS mode_reglement_CODE, modereglement.`libelle` AS modereglement_libelle, banque.`libelle` AS banque_libelle FROM `client` client INNER JOIN `operation` operation ON client.`id` = operation.`client` INNER JOIN `typeoperation` typeoperation ON operation.`typeoperation` = typeoperation.`id` INNER JOIN `modereglement` modereglement ON operation.`modereglement` = modereglement.`id` LEFT JOIN `banque` banque ON operation.`banque` = banque.`id` left join lettrage_com lettrage_com on operation.id=lettrage_com.NI_OP left join operation o2 on o2.id=lettrage_com.NI_PIECE ,`cabinet` cabinet "
                    + " WHERE (typeoperation.`libelle` = 'Depense Divers' or typeoperation.`libelle` = 'Depense Fournisseur'  ) " + Filt_typeReg + " and operation.`dateoperation` >= '" + d1 + "' and operation.`dateoperation` <= '" + d2 + "'" + q1 + " group by operation.id order by operation.`dateoperation`";
            
            
            
   
            System.out.println("q etat depenses"+q);
            query.setText(q);
            jasperDesign.setQuery(query);
            jasperDesign.setName("DepenseDesign");

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

            jrFame.setHeight(72);
            jrFame.setWidth(842);
            jrFame.getLineBox().setLeftPadding(0);
            jrFame.getLineBox().setTopPadding(0);
            jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

            JRDesignStaticText libelleTitre = new JRDesignStaticText();
            libelleTitre.setWidth(360);
            libelleTitre.setHeight(31);
            libelleTitre.setX(200);
            libelleTitre.setY(30);
            libelleTitre.setText("ETAT DEPENSES");
          
            libelleTitre.setFontSize(18);
            libelleTitre.setForecolor(Color.BLUE);
            jrFame.addElement(libelleTitre);

            JRDesignStaticText libelleTotal = new JRDesignStaticText();
            libelleTotal.setWidth(310);
            libelleTotal.setHeight(31);
            libelleTotal.setX(0);
            libelleTotal.setY(7);
            libelleTotal.setText("TOTAL ");
            libelleTotal.setFontSize(12);
            libelleTotal.setForecolor(Color.BLACK);
            bandSomme.addElement(libelleTotal);


            JRDesignStaticText libelleDate = new JRDesignStaticText();
            libelleDate.setWidth(200);
            libelleDate.setHeight(15);
            libelleDate.setText(" PERIODE : Du  " + d111 + "  Au  " + d222);
            libelleDate.setFontSize(10);
            libelleDate.setForecolor(Color.BLACK);
            libelleDate.setX(200);
            libelleDate.setY(55);
            jrFame.addElement(libelleDate);
            bandTiltle.setHeight(82);


            /**************************************************/
            JRDesignField fieldSTE = new JRDesignField();
            fieldSTE.setName("cabinet_libelle");
            fieldSTE.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldSTE);

            JRDesignTextField textFieldSTE = new JRDesignTextField();
            textFieldSTE.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldSTE.setStretchWithOverflow(true);
            textFieldSTE.setFontSize(10);
            textFieldSTE.setWidth(300);
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
            libelleTEL.setX(2);
            libelleTEL.setY(30);
            libelleTEL.setText("Tel  :");
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
            textFieldTel.setWidth(400);
            textFieldTel.setX(30);
            textFieldTel.setY(30);
            JRDesignExpression expressionTel = new JRDesignExpression();
            expressionTel.setValueClass(java.lang.String.class);
            expressionTel.setText("$F{cabinet_tel}");
            textFieldTel.setExpression(expressionTel);

            jrFame.addElement(textFieldTel);
            /*******************************************************/
            /*********************************************************/
            JRDesignStaticText libelleFax = new JRDesignStaticText();
            libelleFax.setWidth(50);
            libelleFax.setHeight(20);
            libelleFax.setX(2);
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
            textFieldFax.setX(30);
            textFieldFax.setY(45);
            JRDesignExpression expressionFax = new JRDesignExpression();
            expressionFax.setValueClass(java.lang.String.class);
            expressionFax.setText("$F{cabinet_fax}");
            textFieldFax.setExpression(expressionFax);

            jrFame.addElement(textFieldFax);
            /********************************/
            bandTiltle.addElement(jrFame);
            jasperDesign.setTitle(bandTiltle);
            /*******************************************************/
             JRDesignBand bandHeader = new JRDesignBand();
            BigDecimal bandHeaderHeight = new BigDecimal(15).setScale(0, RoundingMode.DOWN);
            bandHeader.setHeight(bandHeaderHeight.intValue());
            //    bandHeader.setHeight(100);


            JRDesignBand columnHeader = new JRDesignBand();
            columnHeader.setHeight(15);
            List<String> Colonneslibelleschoix = new LinkedList<String>();
            for (int j = 0; j < colonneschoisies.getTarget().size(); j++) {
                JRDesignStaticText Jrdeslibelle = new JRDesignStaticText();

                String expressioncolonne = colonneschoisies.getTarget().get(j);
                Colonneslibelleschoix.add(expressioncolonne);
                //condition
                   if (expressioncolonne.equals("DATE")) {
                    expressioncolonne = "DATE_SYS";

                }
                if (expressioncolonne.equals("ESPECE")) {
                    expressioncolonne = "montantespece";

                }
                if (expressioncolonne.equals("CHEQUE")) {
                    expressioncolonne = "montantcheque";

                }

                if (expressioncolonne.equals("TRAITE")) {
                    expressioncolonne = "montanttraite";

                }
                if (expressioncolonne.equals("VIREMENT")) {
                    expressioncolonne = "montantvirement";

                }
                if (expressioncolonne.equals("RAISON SOCIALE")) {
                    expressioncolonne = "client_nom";

                }
                if (expressioncolonne.equals("CODE")) {
                    expressioncolonne = "client_code";

                }
                if (expressioncolonne.equals("BANQUE")) {
                    expressioncolonne = "banque_libelle";

                }
               
                 if (expressioncolonne.equals("REFERENCE")) {
                    expressioncolonne = "operation_REFERENCE";

                }
                   if (expressioncolonne.equals("ECHEANCE")) {
                    expressioncolonne = "dateechenace";

                }

                if (expressioncolonne.equals("LETTRAGE")) {
                    expressioncolonne = "numeroFacture";

                }
                 if (expressioncolonne.equals("LIBELLE OPERATION")) {
                    expressioncolonne = "operation_libelleoperation";

                }
            if ((colonneschoisies.getTarget().get(j).equals("DATE"))||((colonneschoisies.getTarget().get(j).equals("ECHEANCE")))) {
             //   System.out.println("start with date verifiee");
                styleLibelleColumnHeaderDrag(jasperDesign, bandHeader, expressioncolonne, "$F{" + expressioncolonne + "}", Jrdeslibelle, colonneschoisies.getTarget().get(j), 60, 60, bandSomme);

                columnHeader.addElement(Jrdeslibelle);

            } else {

                styleLibelleColumnHeaderDrag(jasperDesign, bandHeader, expressioncolonne, "(($F{" + expressioncolonne + "}!=null)&&($F{" + expressioncolonne + "}.isEmpty()!=true)&&(($F{" + expressioncolonne + "}.equals(\"0.000\")!=true)))?$F{" + expressioncolonne + "}:Character.toString(' ')", Jrdeslibelle, colonneschoisies.getTarget().get(j), 70, 120, bandSomme);
//                 if ((colonneschoisies.getTarget().get(j).equals("ESPECE"))||((colonneschoisies.getTarget().get(j).equals("CHEQUE")))||(colonneschoisies.getTarget().get(j).equals("VIREMENT"))||((colonneschoisies.getTarget().get(j).equals("TRAITE")))) {
//                       styleLibelleColumnHeaderDrag(jasperDesign, bandHeader, expressioncolonne, "(($F{" + expressioncolonne + "}.equals(\"0.000\"))?$F{" + expressioncolonne + "}:Character.toString('-'))", Jrdeslibelle, colonneschoisies.getTarget().get(j), 70, 120, bandSomme);
////                 
//                 }

                columnHeader.addElement(Jrdeslibelle);
            }

            }

            colonneschoisies.setTarget(Colonneslibelleschoix);

            jasperDesign.setColumnHeader(columnHeader);
          //  jasperDesign.setDetail(bandHeader);
                  ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandHeader);
             jasperDesign.setSummary(bandSomme);
            

            /***********************************Somme Total***************************************/
            //  JRDesignTextField textFieldSomme = new JRDesignTextField(); 
            JRDesignField montant = new JRDesignField();
            montant.setName("operation_montant_debite");
            montant.setValueClass(java.math.BigDecimal.class);
            jasperDesign.addField(montant);




            JRDesignVariable varSomme = new JRDesignVariable();
            varSomme.setName("varTotal");
            varSomme.setValueClass(java.math.BigDecimal.class);
            varSomme.setCalculation(CalculationEnum.getByValue(new Byte("2")));
            jasperDesign.addVariable(varSomme);

            JRDesignExpression expressionE = new JRDesignExpression();
            expressionE.setValueClass(java.math.BigDecimal.class);
            expressionE.setText("$F{operation_montant_debite}");
            varSomme.setExpression(expressionE);


            JRDesignExpression expressionEI = new JRDesignExpression();
            expressionEI.setValueClass(java.math.BigDecimal.class);
            expressionEI.setText("new BigDecimal(0).setScale(3, RoundingMode.UP)");

            varSomme.setInitialValueExpression(expressionEI);


            JRDesignTextField textField = new JRDesignTextField();
            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textField.setStretchWithOverflow(true);
            textField.setFontSize(12);
            textField.setWidth(100);
            textField.setX(75);
            textField.setY(45);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setValueClass(java.math.BigDecimal.class);
            expression.setText("$V{varTotal}");
            textField.setExpression(expression);
            textField.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));


            JRDesignStaticText libelleTotalF = new JRDesignStaticText();
            libelleTotalF.setWidth(150);
            libelleTotalF.setHeight(31);
            libelleTotalF.setX(0);
            libelleTotalF.setY(45);
            libelleTotalF.setText("TOTAL GENERAL :");
            libelleTotalF.setFontSize(12);
            libelleTotalF.setForecolor(Color.BLUE);
            bandSomme.addElement(libelleTotalF);


            bandSomme.addElement(textField);
            /*************************************************************/
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
            /*************************************************************************************************/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap parameters = new HashMap();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

            DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
            Date d = new Date();
            String dd = dateFormat15.format(d);
            String nomFichier = "RapportEtatDepense " + dd;
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment;filename=" + nomFichier + " .pdf");
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.setContentType("application/pdf");
            context.responseComplete();
        } catch (Exception ex) {
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

/*******************************************************************************************************/
public void jasperDesignModel() throws JRException, IOException {

         try {
            
            int pageWidth = 0;
          
            if (optionOrient.equals("2")) {
                pageWidth = 842;
            } else {
                pageWidth = 595;
            }
          
            System.out.println("optionOrient" + optionOrient);
            JRDesignFrame jrFame = new JRDesignFrame();
            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

            DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d22 = new Date();
            String ddd = dateFormat3.format(d22);
            JasperDesign jasperDesign = new JasperDesign();


            JRDesignBand bandTiltle = new JRDesignBand();

            JRDesignStaticText libelleDateSys = new JRDesignStaticText();
            libelleDateSys.setWidth(150);
            libelleDateSys.setHeight(20);
            libelleDateSys.setText("DATE : " + ddd);
            libelleDateSys.setFontSize(10);
            libelleDateSys.setForecolor(Color.BLACK);
            libelleDateSys.setX(440);
            libelleDateSys.setY(10);
            jrFame.addElement(libelleDateSys);

            JRDesignBand bandBlanc = new JRDesignBand();
            bandBlanc.setHeight(200);
            jasperDesign.setNoData(bandBlanc);
            
            dateDebut.setHours(0);
            dateDebut.setMinutes(0);
            dateDebut.setSeconds(0);
            dateFin.setHours(23);
            dateFin.setMinutes(59);
            dateFin.setSeconds(59);

            JRDesignBand bandSomme = new JRDesignBand();
            bandSomme.setHeight(80);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String d1 = dateFormat.format(dateDebut);
            String d2 = dateFormat.format(dateFin);
            DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            String d111 = dateFormat1.format(dateDebut);
            String d222 = dateFormat1.format(dateFin);
            Xligne = 0;
            etablirconnection();
            JRDesignQuery query = new JRDesignQuery();
            String Filt_typeReg = "";
          
            String ty = "";

            //     and (mode_reglement_com.`NOM_MR` = '"+ty+"' "+Filt_typeReg+") 
            
        for (int j = 0; j < colonneschoisies.getTarget().size(); j++) {
           
           if (colonneschoisies.getTarget().get(j).equals("CHEQUE"))
                    {
                    
                        flagMontantCheque=true ;
                    }
              if (colonneschoisies.getTarget().get(j).equals("ESPECE"))
                    {
                      
                        flagMontantEspece=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("TRAITE"))
                    {
                       
                        flagMontantTraite=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("VIREMENT"))
                    {
                        
                        flagMontantVirement=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("CODE"))
                    {
                       
                        flagCodeClientF=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("BANQUE"))
                    {
                      
                        flagBanque=true ;
                    }

if (colonneschoisies.getTarget().get(j).equals("REFERENCE"))
                    {
                        System.out.println("REFERENCE existe");
                        flagRefCheque=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("ECHEANCE"))
                    {
                       
                        flagecheanceCheque=true ;
                    }
if (colonneschoisies.getTarget().get(j).equals("RAISON SOCIALE"))
                    {
                        System.out.println("RAISON SOCIALE existe");
                   flagNomClientF=true;
                    }
 flagDate=false;
if (colonneschoisies.getTarget().get(j).equals("DATE"))
                    {
                      
                   flagDate=true;
                    }
           

          
           
        }
       
            if ((flagMontantCheque == true) || (flagMontantTraite == true) || (flagMontantEspece == true) || (flagMontantVirement == true)) {
                Filt_typeReg = Filt_typeReg + " and ( ";

                if (flagMontantCheque == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  modereglement.`libelle` = 'cheque'";
                }
                if (flagMontantTraite == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  modereglement.`libelle` = 'traite'";
                }
                if (flagMontantEspece == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  modereglement.`libelle` = 'espece'";
                }
                if (flagMontantVirement == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  modereglement.`libelle` = 'virement'";
                }
                Filt_typeReg = Filt_typeReg + " )";


            }

            String q1 = "";

            if (filtreTiersEtat != null) {
                q1 = " and operation.`client` =" + filtreTiersEtat.getId();
            }
            String q="SELECT cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal, operation.DATE_SYS, operation.`numerooperation` AS operation_numero_operation, operation.`dateoperation` AS operation_date_operation, operation.`montantcheque` AS operation_montantcheque, operation.`montantespece` AS operation_montantespece, operation.`montanttraite` AS operation_montanttraite, operation.`montantvirement` AS operation_montantvirement, operation.`dateechenace` AS operation_dateechenace, operation.`banque` AS operation_banque, operation.`libelleoperation` AS operation_libelleoperation, operation.`montantcredite` AS operation_montant_credite, operation.`modereglement` AS operation_mode_reglement, operation.`REFERENCE` AS operation_REFERENCE, typeoperation.`libelle` AS typeoperation_libelle, typeoperation.`id` AS typeoperation_id, client.`code` AS client_code, client.`nom` AS client_nom, modereglement.`id` AS mode_reglement_id, modereglement.`CODE` AS mode_reglement_CODE, modereglement.`libelle` AS modereglement_libelle, banque.`libelle` AS banque_libelle FROM `client` client INNER JOIN `operation` operation ON client.`id` = operation.`client` INNER JOIN `typeoperation` typeoperation ON operation.`typeoperation` = typeoperation.`id` INNER JOIN `modereglement` modereglement ON operation.`modereglement` = modereglement.`id` LEFT JOIN `banque` banque ON operation.`banque` = banque.`id` left join lettrage_com lettrage_com on operation.id=lettrage_com.NI_OP left join operation o2 on o2.id=lettrage_com.NI_PIECE ,`cabinet` cabinet "
                    + " WHERE typeoperation.`libelle` = 'Reglement client' " + Filt_typeReg + " and operation.DATE_SYS >= '" + d1 + "' and operation.DATE_SYS <= '" + d2 + "'" + q1 + " group by operation.id order by DATE_SYS";
            
            
            
   
            System.out.println("q "+q);
            query.setText(q);
            jasperDesign.setQuery(query);
            jasperDesign.setName("ReglementDesign");

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

            jrFame.setHeight(72);
            jrFame.setWidth(842);
            jrFame.getLineBox().setLeftPadding(0);
            jrFame.getLineBox().setTopPadding(0);
            jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

            JRDesignStaticText libelleTitre = new JRDesignStaticText();
            libelleTitre.setWidth(360);
            libelleTitre.setHeight(31);
            libelleTitre.setX(200);
            libelleTitre.setY(30);
            libelleTitre.setText("ETAT REGLEMENTS PATIENTS");
          
            libelleTitre.setFontSize(18);
            libelleTitre.setForecolor(Color.BLUE);
            jrFame.addElement(libelleTitre);

            JRDesignStaticText libelleTotal = new JRDesignStaticText();
            libelleTotal.setWidth(310);
            libelleTotal.setHeight(31);
            libelleTotal.setX(0);
            libelleTotal.setY(7);
            libelleTotal.setText("TOTAL ");
            libelleTotal.setFontSize(12);
            libelleTotal.setForecolor(Color.BLACK);
            bandSomme.addElement(libelleTotal);


            JRDesignStaticText libelleDate = new JRDesignStaticText();
            libelleDate.setWidth(200);
            libelleDate.setHeight(15);
            libelleDate.setText(" PERIODE : Du  " + d111 + "  Au  " + d222);
            libelleDate.setFontSize(10);
            libelleDate.setForecolor(Color.BLACK);
            libelleDate.setX(200);
            libelleDate.setY(55);
            jrFame.addElement(libelleDate);
            bandTiltle.setHeight(82);


            /**************************************************/
            JRDesignField fieldSTE = new JRDesignField();
            fieldSTE.setName("cabinet_libelle");
            fieldSTE.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldSTE);

            JRDesignTextField textFieldSTE = new JRDesignTextField();
            textFieldSTE.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldSTE.setStretchWithOverflow(true);
            textFieldSTE.setFontSize(10);
            textFieldSTE.setWidth(300);
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
            libelleTEL.setX(2);
            libelleTEL.setY(30);
            libelleTEL.setText("Tel  :");
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
            textFieldTel.setWidth(400);
            textFieldTel.setX(30);
            textFieldTel.setY(30);
            JRDesignExpression expressionTel = new JRDesignExpression();
            expressionTel.setValueClass(java.lang.String.class);
            expressionTel.setText("$F{cabinet_tel}");
            textFieldTel.setExpression(expressionTel);

            jrFame.addElement(textFieldTel);
            /*******************************************************/
            /*********************************************************/
            JRDesignStaticText libelleFax = new JRDesignStaticText();
            libelleFax.setWidth(50);
            libelleFax.setHeight(20);
            libelleFax.setX(2);
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
            textFieldFax.setX(30);
            textFieldFax.setY(45);
            JRDesignExpression expressionFax = new JRDesignExpression();
            expressionFax.setValueClass(java.lang.String.class);
            expressionFax.setText("$F{cabinet_fax}");
            textFieldFax.setExpression(expressionFax);

            jrFame.addElement(textFieldFax);
            /********************************/
            bandTiltle.addElement(jrFame);
            jasperDesign.setTitle(bandTiltle);
            /*******************************************************/
             JRDesignBand bandHeader = new JRDesignBand();
            BigDecimal bandHeaderHeight = new BigDecimal(15).setScale(0, RoundingMode.DOWN);
            bandHeader.setHeight(bandHeaderHeight.intValue());
            //    bandHeader.setHeight(100);


            JRDesignBand columnHeader = new JRDesignBand();
            columnHeader.setHeight(15);
            List<String> Colonneslibelleschoix = new LinkedList<String>();
            for (int j = 0; j < colonneschoisies.getTarget().size(); j++) {
                JRDesignStaticText Jrdeslibelle = new JRDesignStaticText();

                String expressioncolonne = colonneschoisies.getTarget().get(j);
                Colonneslibelleschoix.add(expressioncolonne);
                //condition
                   if (expressioncolonne.equals("DATE")) {
                    expressioncolonne = "DATE_SYS";

                }
                if (expressioncolonne.equals("ESPECE")) {
                    expressioncolonne = "montantespece";

                }
                if (expressioncolonne.equals("CHEQUE")) {
                    expressioncolonne = "montantcheque";

                }

                if (expressioncolonne.equals("TRAITE")) {
                    expressioncolonne = "montanttraite";

                }
                if (expressioncolonne.equals("VIREMENT")) {
                    expressioncolonne = "montantvirement";

                }
                if (expressioncolonne.equals("RAISON SOCIALE")) {
                    expressioncolonne = "client_nom";

                }
                if (expressioncolonne.equals("CODE")) {
                    expressioncolonne = "client_code";

                }
                if (expressioncolonne.equals("BANQUE")) {
                    expressioncolonne = "banque_libelle";

                }
               
                 if (expressioncolonne.equals("REFERENCE")) {
                    expressioncolonne = "operation_REFERENCE";

                }
                   if (expressioncolonne.equals("ECHEANCE")) {
                    expressioncolonne = "dateechenace";

                }

                if (expressioncolonne.equals("LETTRAGE")) {
                    expressioncolonne = "numeroFacture";

                }
                  if (expressioncolonne.equals("LIBELLE OPERATION")) {
                    expressioncolonne = "operation_libelleoperation";

                }
            if ((colonneschoisies.getTarget().get(j).equals("DATE"))||((colonneschoisies.getTarget().get(j).equals("ECHEANCE")))) {
             //   System.out.println("start with date verifiee");
                styleLibelleColumnHeaderDrag(jasperDesign, bandHeader, expressioncolonne, "$F{" + expressioncolonne + "}", Jrdeslibelle, colonneschoisies.getTarget().get(j), 60, 60, bandSomme);

                columnHeader.addElement(Jrdeslibelle);

            } else {

                styleLibelleColumnHeaderDrag(jasperDesign, bandHeader, expressioncolonne, "(($F{" + expressioncolonne + "}!=null)&&($F{" + expressioncolonne + "}.isEmpty()!=true)&&(($F{" + expressioncolonne + "}.equals(\"0.000\")!=true)))?$F{" + expressioncolonne + "}:Character.toString(' ')", Jrdeslibelle, colonneschoisies.getTarget().get(j), 70, 120, bandSomme);
//                 if ((colonneschoisies.getTarget().get(j).equals("ESPECE"))||((colonneschoisies.getTarget().get(j).equals("CHEQUE")))||(colonneschoisies.getTarget().get(j).equals("VIREMENT"))||((colonneschoisies.getTarget().get(j).equals("TRAITE")))) {
//                       styleLibelleColumnHeaderDrag(jasperDesign, bandHeader, expressioncolonne, "(($F{" + expressioncolonne + "}.equals(\"0.000\"))?$F{" + expressioncolonne + "}:Character.toString('-'))", Jrdeslibelle, colonneschoisies.getTarget().get(j), 70, 120, bandSomme);
////                 
//                 }

                columnHeader.addElement(Jrdeslibelle);
            }

            }

            colonneschoisies.setTarget(Colonneslibelleschoix);

            jasperDesign.setColumnHeader(columnHeader);
          //  jasperDesign.setDetail(bandHeader);
                  ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandHeader);
             jasperDesign.setSummary(bandSomme);
            

            /***********************************Somme Total***************************************/
            //  JRDesignTextField textFieldSomme = new JRDesignTextField(); 
            JRDesignField montant = new JRDesignField();
            montant.setName("operation_montant_credite");
            montant.setValueClass(java.math.BigDecimal.class);
            jasperDesign.addField(montant);




            JRDesignVariable varSomme = new JRDesignVariable();
            varSomme.setName("varTotal");
            varSomme.setValueClass(java.math.BigDecimal.class);
            varSomme.setCalculation(CalculationEnum.getByValue(new Byte("2")));
            jasperDesign.addVariable(varSomme);

            JRDesignExpression expressionE = new JRDesignExpression();
            expressionE.setValueClass(java.math.BigDecimal.class);
            expressionE.setText("$F{operation_montant_credite}");
            varSomme.setExpression(expressionE);


            JRDesignExpression expressionEI = new JRDesignExpression();
            expressionEI.setValueClass(java.math.BigDecimal.class);
            expressionEI.setText("new BigDecimal(0).setScale(3, RoundingMode.UP)");

            varSomme.setInitialValueExpression(expressionEI);


            JRDesignTextField textField = new JRDesignTextField();
            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textField.setStretchWithOverflow(true);
            textField.setFontSize(12);
            textField.setWidth(100);
            textField.setX(75);
            textField.setY(45);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setValueClass(java.math.BigDecimal.class);
            expression.setText("$V{varTotal}");
            textField.setExpression(expression);
            textField.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));


            JRDesignStaticText libelleTotalF = new JRDesignStaticText();
            libelleTotalF.setWidth(150);
            libelleTotalF.setHeight(31);
            libelleTotalF.setX(0);
            libelleTotalF.setY(45);
            libelleTotalF.setText("TOTAL GENERAL :");
            libelleTotalF.setFontSize(12);
            libelleTotalF.setForecolor(Color.BLUE);
            bandSomme.addElement(libelleTotalF);


            bandSomme.addElement(textField);
            /*************************************************************/
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
            /*************************************************************************************************/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap parameters = new HashMap();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

            DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
            Date d = new Date();
            String dd = dateFormat15.format(d);
            String nomFichier = "Rapport Reglement " + dd;
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.addHeader("Content-disposition", "attachment;filename=" + nomFichier + " .pdf");
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.setContentType("application/pdf");
            context.responseComplete();
        } catch (Exception ex) {
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
 public void styleLibelleColumnHeaderDrag(JasperDesign jasperDesign, JRDesignBand bandHeader, String filename, String exp, JRDesignStaticText x, String nom, int setx, int width, JRDesignBand bandSomme) {
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
        x.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
       // x.setBorder(new Byte("1"));
        x.getLineBox().getLeftPen().setLineWidth(1);
x.getLineBox().getTopPen().setLineWidth(1);
x.getLineBox().getRightPen().setLineWidth(1);
x.getLineBox().getTopPen().setLineWidth(1);
x.getLineBox().getBottomPen().setLineWidth(1);  
     //   x.setBorderColor(Color.BLACK);
        x.getLineBox().setLeftPadding(1);


        JRDesignTextField textField = new JRDesignTextField();
        JRDesignField field = new JRDesignField();
        if (nom.startsWith("DATE")) {
            width = 60;
        } 
         if (nom.startsWith("RAISON SOCIALE")) {
            width = 200;
        } 
          if (nom.startsWith("LIBELLE OPERATION")) {
            width = 200;
        } 
        else if (nom.startsWith("Total")) {
           width = 60;
        } 
//else if (nom.equals("Client")) {
//            width = 180;
//        } else if (nom.equals("Vendeur")) {
//            width = 120;
//        } else if (nom.equals("Adresse Livraison")) {
//            width = 200;
//        } //here
        else {
            width = 60;
        }
        x.setWidth(width);
        if ((nom.startsWith("DATE"))||(nom.startsWith("ECHEANCE"))) {
            field.setValueClass(java.util.Date.class);
        } else {
            field.setValueClass(java.lang.String.class);
        }
        field.setName(filename);
        try {
            jasperDesign.addField(field);
        } catch (JRException ex) {
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }
        textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        textField.setStretchWithOverflow(true);
        textField.setHeight(15);
        textField.setFontSize(10);
//        textField.setBorder(new Byte("1"));
//        textField.setBorderColor(Color.BLACK);
        textField.getLineBox().getLeftPen().setLineWidth(1);
textField.getLineBox().getTopPen().setLineWidth(1);
textField.getLineBox().getRightPen().setLineWidth(1);
textField.getLineBox().getTopPen().setLineWidth(1);
textField.getLineBox().getBottomPen().setLineWidth(1);  
       textField.getLineBox().setTopPadding(1);
            textField.getLineBox().setLeftPadding(1);
        textField.setWidth(width);
        textField.setX(Xligne);
        JRDesignExpression expression = new JRDesignExpression();
          if ((nom.startsWith("DATE"))||(nom.startsWith("ECHEANCE"))) {
            expression.setValueClass(java.util.Date.class);
            textField.setPattern("dd/MM/yyyy");

            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        } else {
            expression.setValueClass(java.lang.String.class);
        }
        expression.setText(exp);
        textField.setExpression(expression);
        textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
        textField.setStretchWithOverflow(true);
          if ((nom.startsWith("RAISON SOCIALE"))||(nom.startsWith("BANQUE")))
          { textField.setStretchWithOverflow(false);
         //textField.setPrintWhenDetailOverflows(true); 
          }
          

        if (nom.equalsIgnoreCase("ESPECE")) {
            calcul(jasperDesign, field, width, Xligne, filename, bandSomme, "varEspece");
            textField.setExpression(expression);
            textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        }
        if (nom.equalsIgnoreCase("CHEQUE")) {
            calcul(jasperDesign, field, width, Xligne, filename, bandSomme, "varCheque");
            textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        }
        if (nom.equalsIgnoreCase("TRAITE")) {
            calcul(jasperDesign, field, width, Xligne, filename, bandSomme, "varTraite");
            textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        }
        if (nom.equalsIgnoreCase("VIREMENT")) {
            calcul(jasperDesign, field, width, Xligne, filename, bandSomme, "varVirement");
            textField.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
        }   
          
          
          
          
        bandHeader.addElement(textField);
        Xligne = Xligne + width;
    }
    /***********************************************************************************/
 
	 public void jasperDesignModelExcel() throws JRException, IOException {
        try {
            int xL = 0;
            int nb = 0;
            int pageWidth = 0;
            if (flagDate == true) {
                xL = xL + 80;
                nb++;
            }
            if (flagMontantEspece == true) {
                xL = xL + 60;
                nb++;
            }
            if (flagMontantCheque == true) {
                xL = xL + 60;
                nb++;
            }
            if (flagMontantTraite == true) {
                xL = xL + 60;
                nb++;
            }
            if (flagMontantVirement == true) {
                xL = xL + 60;
                nb++;
            }
            if (flagNomClientF == true) {
                xL = xL + 200;
                nb++;
            }
             if (flagLibelleOperation == true) {
                xL = xL + 200;
                nb++;
            }
            if (flagCodeClientF == true) {
                xL = xL + 50;
                nb++;
            }
            if (flagBanque == true) {
                xL = xL + 70;
                nb++;
            }
            if (flagRefCheque == true) {
                xL = xL + 50;
                nb++;
            }
            if (flagecheanceCheque == true) {
                xL = xL + 70;
                nb++;
            }
        //    System.out.println("xL" + xL);
            if (optionOrient.equals("2")) {
                pageWidth = 842;
            } else {
                pageWidth = 595;
            }
            int Xajout = (pageWidth - xL) / nb;
            int reste = (pageWidth - xL) - (Xajout * nb);

         //   System.out.println("optionOrient" + optionOrient);
            JRDesignFrame jrFame = new JRDesignFrame();
            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

            DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d22 = new Date();
            String ddd = dateFormat3.format(d22);
            JasperDesign jasperDesign = new JasperDesign();


            JRDesignBand bandTiltle = new JRDesignBand();

            JRDesignStaticText libelleDateSys = new JRDesignStaticText();
            libelleDateSys.setWidth(150);
            libelleDateSys.setHeight(20);
            libelleDateSys.setText("DATE : " + ddd);
            libelleDateSys.setFontSize(10);
            libelleDateSys.setForecolor(Color.BLACK);
            libelleDateSys.setX(440);
            libelleDateSys.setY(10);
            jrFame.addElement(libelleDateSys);

            JRDesignBand bandBlanc = new JRDesignBand();
            bandBlanc.setHeight(200);
            JRDesignStaticText libellePgeB = new JRDesignStaticText();
            libellePgeB.setWidth(50);
            libellePgeB.setHeight(20);
            libellePgeB.setX(0);
            libellePgeB.setY(30);
            libellePgeB.setText("Tel :");
            libellePgeB.setFontSize(12);
       //     libellePgeB.setForecolor(Color.BLACK);
            bandBlanc.addElement(libellePgeB);
            jasperDesign.setNoData(bandBlanc);
           
           dateDebut.setHours(0);
            dateDebut.setMinutes(0);
            dateDebut.setSeconds(0);
            dateFin.setHours(23);
            dateFin.setMinutes(59);
            dateFin.setSeconds(59);

            JRDesignBand bandSomme = new JRDesignBand();
            bandSomme.setHeight(80);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String d1 = dateFormat.format(dateDebut);
            String d2 = dateFormat.format(dateFin);
            DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            String d111 = dateFormat1.format(dateDebut);
            String d222 = dateFormat1.format(dateFin);
            
            Xligne = 0;
         //   System.out.println("d1" + d1);

            etablirconnection();
            //  Connection connection1 = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/onlysoft", "root", "root");
            JRDesignQuery query = new JRDesignQuery();
            String Filt_typeReg = "";
            String ty = "";

            //     and (mode_reglement_com.`NOM_MR` = '"+ty+"' "+Filt_typeReg+") 

            if ((flagMontantCheque == true) || (flagMontantTraite == true) || (flagMontantEspece == true) || (flagMontantVirement == true)) {
                Filt_typeReg = Filt_typeReg + " and ( ";

                if (flagMontantCheque == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'cheque'";
                }
                if (flagMontantTraite == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'traite'";
                }
                if (flagMontantEspece == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'espece'";
                }
                if (flagMontantVirement == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'virement'";
                }
                Filt_typeReg = Filt_typeReg + " )";


            }
            System.out.println(Filt_typeReg);
            /*  
            if(flagMontantCheque==true){ ty="cheque";
            if(flagMontantTraite==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'traite'";}
            if(flagMontantEspece==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'espece'";}
            if(flagMontantVirement==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'virement'";}
            }else{
            
            if(flagMontantCheque==false){
            if(flagMontantTraite==true){
            if(flagMontantTraite==true){ ty="traite";}
            if(flagMontantEspece==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'espece'";}
            if(flagMontantVirement==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'virement'";}
            
            }}else{
            
            
            
            if((flagMontantCheque==false) && (flagMontantTraite==false)){
            
            if(flagMontantEspece==true){ty="espece";
            if(flagMontantVirement==true){ Filt_typeReg=Filt_typeReg+=" or mode_reglement_com.`NOM_MR` = 'virement'";} }
            
            }else{
            if((flagMontantCheque==false) && (flagMontantTraite==false)&& (flagMontantEspece==false)){
            if(flagMontantVirement==true){ ty="virement";}}
            }
            }*/

            String q1 = "";

            if (filtreTiersEtat != null) {
                q1 = " and operation_com.`NI_TI` =" + filtreTiersEtat.getId();
            }
           
             String q = "SELECT DATE_SYS,point_vente_com.`NOM_PV` AS point_vente_com_NOM_PV,caissier_com.`NOM_CAI` AS caissier_com_NOM_CAI,operation_com.`NUM_OP` AS operation_com_NUM_OP,operation_com.`DATE_OP` AS operation_com_DATE_OP,tiers_com.`CODE_TI` AS tiers_com_CODE_TI,tiers_com.`NOM_TI` AS tiers_com_NOM_TI,type_op_com.`NOM_TOP` AS type_op_com_NOM_TOP,operation_com.`MONTANT_COP` AS operation_com_MONTANT_COP,operation_com.`NI_MR` AS operation_com_NI_MR,mode_reglement_com.`NI_MR` AS mode_reglement_com_NI_MR,mode_reglement_com.`CODE_MR` AS mode_reglement_com_CODE_MR,mode_reglement_com.`NOM_MR` AS mode_reglement_com_NOM_MR,operation_com.`REFERENCE_OP` AS operation_com_REFERENCE_OP,banque_com.`NOM_BANQUE` AS banque_com_NOM_BANQUE,MONTANT_ES,MONTANT_TR,MONTANT_VIR,MONTANT_CH,ECHANCE_OP , horizon_sys.`id` AS horizon_sys_id,horizon_sys.`CODE` AS horizon_sys_CODE,horizon_sys.`LIBELLE_SOCIETE` AS horizon_sys_LIBELLE_SOCIETE, horizon_sys.`ADRESSE` AS horizon_sys_ADRESSE,horizon_sys.`FAX` AS horizon_sys_FAX,horizon_sys.`TEL` AS horizon_sys_TEL FROM `tiers_com` tiers_com INNER JOIN `operation_com` operation_com ON tiers_com.`NI_TI` = operation_com.`NI_TI` INNER JOIN `type_op_com` type_op_com ON operation_com.`NI_TOP` = type_op_com.`NI_TOP` INNER JOIN `mode_reglement_com` mode_reglement_com ON operation_com.`NI_MR` = mode_reglement_com.`NI_MR` INNER JOIN `point_vente_com` point_vente_com ON operation_com.`NI_PV` = point_vente_com.`NI_PV` LEFT JOIN `caissier_com` caissier_com ON operation_com.`NI_CAI` = caissier_com.`NI_CAI` LEFT JOIN `banque_com` banque_com ON operation_com.`NI_BANQUE` = banque_com.`NI_BANQUE` ,`horizon_sys` horizon_sys  WHERE type_op_com.`NOM_TOP` = 'reglement' " + Filt_typeReg + " and DATE_SYS >= '" + d1 + "' and DATE_SYS <= '" + d2 + "'" + q1 + " order by DATE_SYS ";



            query.setText(q);
            jasperDesign.setQuery(query);
            jasperDesign.setName("ReglementDesign");

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

            jrFame.setHeight(72);
            jrFame.setWidth(842);
           jrFame.getLineBox().setLeftPadding(0);
        
            jrFame.getLineBox().setTopPadding(0);
            jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

            JRDesignStaticText libelleTitre = new JRDesignStaticText();
            libelleTitre.setWidth(360);
            libelleTitre.setHeight(31);
            libelleTitre.setX(200);
            libelleTitre.setY(30);
            libelleTitre.setText("ETAT REGLEMENTS CLIENTS");
            libelleTitre.setFontSize(18);
            libelleTitre.setForecolor(Color.BLUE);
            jrFame.addElement(libelleTitre);

            JRDesignStaticText libelleTotal = new JRDesignStaticText();
            libelleTotal.setWidth(310);
            libelleTotal.setHeight(31);
            libelleTotal.setX(0);
            libelleTotal.setY(7);
            libelleTotal.setText("TOTAL ");
            libelleTotal.setFontSize(12);
            libelleTotal.setForecolor(Color.BLACK);
            bandSomme.addElement(libelleTotal);


            JRDesignStaticText libelleDate = new JRDesignStaticText();
            libelleDate.setWidth(200);
            libelleDate.setHeight(15);
            libelleDate.setText(" PERIODE : Du  " + d111 + "  Au  " + d222);
            libelleDate.setFontSize(10);
            libelleDate.setForecolor(Color.BLACK);
            libelleDate.setX(200);
            libelleDate.setY(55);
            jrFame.addElement(libelleDate);
            bandTiltle.setHeight(82);


            /**************************************************/
            JRDesignField fieldSTE = new JRDesignField();
            fieldSTE.setName("horizon_sys_LIBELLE_SOCIETE");
            fieldSTE.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldSTE);

            JRDesignTextField textFieldSTE = new JRDesignTextField();
            textFieldSTE.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldSTE.setStretchWithOverflow(true);
            textFieldSTE.setFontSize(10);
            textFieldSTE.setWidth(150);
            textFieldSTE.setX(0);
            textFieldSTE.setY(0);
            JRDesignExpression expressionSTE = new JRDesignExpression();
            expressionSTE.setValueClass(java.lang.String.class);
            expressionSTE.setText("$F{horizon_sys_LIBELLE_SOCIETE}");
            textFieldSTE.setExpression(expressionSTE);

            jrFame.addElement(textFieldSTE);

            /*******************************************************************/
            JRDesignField fieldAdresse = new JRDesignField();
            fieldAdresse.setName("horizon_sys_Adresse");
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
            expressionAdresse.setText("$F{horizon_sys_Adresse}");
            textFieldAdresse.setExpression(expressionAdresse);

            jrFame.addElement(textFieldAdresse);

            /*************************************************************/
            JRDesignStaticText libelleTEL = new JRDesignStaticText();
            libelleTEL.setWidth(50);
            libelleTEL.setHeight(20);
            libelleTEL.setX(2);
            libelleTEL.setY(30);
            libelleTEL.setText("Tel  :");
            libelleTEL.setFontSize(10);
            libelleTEL.setForecolor(Color.BLACK);
            jrFame.addElement(libelleTEL);

            JRDesignField fieldTel = new JRDesignField();
            fieldTel.setName("horizon_sys_Tel");
            fieldTel.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldTel);

            JRDesignTextField textFieldTel = new JRDesignTextField();
            textFieldTel.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldTel.setStretchWithOverflow(true);
            textFieldTel.setFontSize(10);
            textFieldTel.setWidth(100);
            textFieldTel.setX(30);
            textFieldTel.setY(30);
            JRDesignExpression expressionTel = new JRDesignExpression();
            expressionTel.setValueClass(java.lang.String.class);
            expressionTel.setText("$F{horizon_sys_Tel}");
            textFieldTel.setExpression(expressionTel);

            jrFame.addElement(textFieldTel);
            /*******************************************************/
            /*********************************************************/
            JRDesignStaticText libelleFax = new JRDesignStaticText();
            libelleFax.setWidth(50);
            libelleFax.setHeight(20);
            libelleFax.setX(2);
            libelleFax.setY(45);
            libelleFax.setText("Fax :");
            libelleFax.setFontSize(10);
            libelleFax.setForecolor(Color.BLACK);
            jrFame.addElement(libelleFax);

            JRDesignField fieldFax = new JRDesignField();
            fieldFax.setName("horizon_sys_Fax");
            fieldFax.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldFax);

            JRDesignTextField textFieldFax = new JRDesignTextField();
            textFieldFax.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldFax.setStretchWithOverflow(true);
            textFieldFax.setFontSize(10);
            textFieldFax.setWidth(100);
            textFieldFax.setX(30);
            textFieldFax.setY(45);
            JRDesignExpression expressionFax = new JRDesignExpression();
            expressionFax.setValueClass(java.lang.String.class);
            expressionFax.setText("$F{horizon_sys_Fax}");
            textFieldFax.setExpression(expressionFax);

            jrFame.addElement(textFieldFax);
            /********************************/
            bandTiltle.addElement(jrFame);
            jasperDesign.setTitle(bandTiltle);
            /*******************************************************/
            JRDesignBand bandHeader = new JRDesignBand();
            BigDecimal bandHeaderHeight = new BigDecimal(15).setScale(0, RoundingMode.DOWN);
            bandHeader.setHeight(bandHeaderHeight.intValue());


            JRDesignBand columnHeader = new JRDesignBand();
            columnHeader.setHeight(15);



            if (flagDate == true) {
                JRDesignStaticText libelleColonneDate = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "DATE_SYS", "$F{DATE_SYS}", libelleColonneDate, "DATE", 0, 60, bandSomme);
                columnHeader.addElement(libelleColonneDate);
            }




            if (flagMontantEspece == true) {
                JRDesignStaticText libelleColonneEspece = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "MONTANT_ES", "($F{MONTANT_ES}!=null)?$F{MONTANT_ES}:Character.toString(' ')", libelleColonneEspece, "ESPECE", 70, 50, bandSomme);
                columnHeader.addElement(libelleColonneEspece);
            }

            if (flagMontantCheque == true) {
                JRDesignStaticText libelleColonneCheque = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "MONTANT_CH", "($F{MONTANT_CH}!=null)?$F{MONTANT_CH}:Character.toString(' ')", libelleColonneCheque, "CHEQUE", 140, 55, bandSomme);
                columnHeader.addElement(libelleColonneCheque);
            }

            if (flagMontantTraite == true) {
                JRDesignStaticText libelleColonneTraite = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "MONTANT_TR", "($F{MONTANT_TR}!=null)?$F{MONTANT_TR}:Character.toString(' ')", libelleColonneTraite, "TRAITE", 210, 50, bandSomme);
                columnHeader.addElement(libelleColonneTraite);
            }

            if (flagMontantVirement == true) {
                JRDesignStaticText libelleColonneVirement = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "MONTANT_VIR", "($F{MONTANT_VIR}!=null)?$F{MONTANT_VIR}:Character.toString(' ')", libelleColonneVirement, "VIREMENT", 280, 50, bandSomme);
                columnHeader.addElement(libelleColonneVirement);
            }

            if (flagNomClientF == true) {
                JRDesignStaticText libelleColonneNomTI = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "tiers_com_NOM_TI", "$F{tiers_com_NOM_TI}", libelleColonneNomTI, "RAISON SOCIALE", 350, 200 + Xajout, bandSomme);
                columnHeader.addElement(libelleColonneNomTI);
            }


            if (flagCodeClientF == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "tiers_com_CODE_TI", "$F{tiers_com_CODE_TI}", libelleColonneCode, "CODE", 500, 50 + Xajout, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }


            if (flagBanque == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "banque_com_NOM_BANQUE", "($F{banque_com_NOM_BANQUE}!=null)?$F{banque_com_NOM_BANQUE}:Character.toString(' ')", libelleColonneCode, "BANQUE", 500, 100 + Xajout + reste, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }
            if (flagPointVente == true) {
                JRDesignStaticText libelleColonnePointVente = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "point_vente_com_NOM_PV", "($F{point_vente_com_NOM_PV}!=null)?$F{point_vente_com_NOM_PV}:Character.toString(' ')", libelleColonnePointVente, "PT VENTE", 500, 60 + Xajout + reste, bandSomme);
                columnHeader.addElement(libelleColonnePointVente);
            }
            if (flagCaissier == true) {
                JRDesignStaticText libelleColonneCaissier = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "caissier_com_NOM_CAI", "($F{caissier_com_NOM_CAI}!=null)?$F{caissier_com_NOM_CAI}:Character.toString(' ')", libelleColonneCaissier, "CAISSIER", 500, 60 + Xajout + reste, bandSomme);
                columnHeader.addElement(libelleColonneCaissier);
            }

            if (flagRefCheque == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_com_REFERENCE_OP", "($F{operation_com_REFERENCE_OP}!=null)?$F{operation_com_REFERENCE_OP}:Character.toString(' ')", libelleColonneCode, "REF", 500, 60 + Xajout, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }

            if (flagecheanceCheque == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "ECHANCE_OP", "$F{ECHANCE_OP}", libelleColonneCode, "ECHEANCE", 500, 50 + Xajout, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }

            jasperDesign.setColumnHeader(columnHeader);
         //   jasperDesign.setDetail(bandHeader);
                  ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandHeader);
            jasperDesign.setSummary(bandSomme);

            /***********************************Somme Total***************************************/
            //  JRDesignTextField textFieldSomme = new JRDesignTextField(); 
            JRDesignField montant = new JRDesignField();
            montant.setName("operation_com_MONTANT_COP");
            montant.setValueClass(java.math.BigDecimal.class);
            jasperDesign.addField(montant);




            JRDesignVariable varSomme = new JRDesignVariable();
            varSomme.setName("varTotal");
            varSomme.setValueClass(java.math.BigDecimal.class);
            varSomme.setCalculation(CalculationEnum.getByValue(new Byte("2")));
            jasperDesign.addVariable(varSomme);

            JRDesignExpression expressionE = new JRDesignExpression();
            expressionE.setValueClass(java.math.BigDecimal.class);
            expressionE.setText("$F{operation_com_MONTANT_COP}");
            varSomme.setExpression(expressionE);


            JRDesignExpression expressionEI = new JRDesignExpression();
            expressionEI.setValueClass(java.math.BigDecimal.class);
            expressionEI.setText("new BigDecimal(0).setScale(3, RoundingMode.UP)");

            varSomme.setInitialValueExpression(expressionEI);


            JRDesignTextField textField = new JRDesignTextField();
            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textField.setStretchWithOverflow(true);
            textField.setFontSize(12);
            textField.setWidth(100);
            textField.setX(125);
            textField.setY(45);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setValueClass(java.math.BigDecimal.class);
            expression.setText("$V{varTotal}");
            textField.setExpression(expression);
            textField.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));


            JRDesignStaticText libelleTotalF = new JRDesignStaticText();
            libelleTotalF.setWidth(122);
            libelleTotalF.setHeight(31);
            libelleTotalF.setX(0);
            libelleTotalF.setY(45);
            libelleTotalF.setText("TOTAL GENERAL :");
            libelleTotalF.setFontSize(12);
            libelleTotalF.setForecolor(Color.BLUE);
            bandSomme.addElement(libelleTotalF);


            bandSomme.addElement(textField);
            /*************************************************************/
            /*************************************************************/
            JRDesignBand bandFooter = new JRDesignBand();
            bandFooter.setHeight(15);

            JRDesignStaticText textFieldDate = new JRDesignStaticText();
            textFieldDate.setFontSize(10);
            textFieldDate.setX(0);
            textFieldDate.setY(0);
            textFieldDate.setWidth(100);
            textFieldDate.setHeight(15);
            textFieldDate.getLineBox().getLeftPen().setLineWidth(1);
            textFieldDate.getLineBox().getTopPen().setLineWidth(1);
            textFieldDate.getLineBox().getRightPen().setLineWidth(1);
            textFieldDate.getLineBox().getTopPen().setLineWidth(1);
            textFieldDate.getLineBox().getBottomPen().setLineWidth(1);  
            textFieldDate.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
            textFieldDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date()));
            bandFooter.addElement(textFieldDate);
            JRDesignTextField textField1 = new JRDesignTextField();
            textField1.setFontSize(10);
            textField1.setX(100);
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
            /*************************************************************************************************/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap parameters = new HashMap();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

            DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
            Date d = new Date();
            String dd = dateFormat15.format(d);

            String nomFichier = "Rapport Reglement " + dd;
            OutputStream ouputStream = new FileOutputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport/") + "\\" + nomFichier));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JRXlsExporter exporterXLS = new JRXlsExporter();
            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
            exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
            exporterXLS.exportReport();
            ouputStream.write(byteArrayOutputStream.toByteArray());
            FacesContext context = FacesContext.getCurrentInstance();
            HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + nomFichier + ".xls");
            response.setContentLength(byteArrayOutputStream.toByteArray().length);
            response.getOutputStream().write(byteArrayOutputStream.toByteArray());

            context.responseComplete();







            /* byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            FacesContext context = FacesContext.getCurrentInstance();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            HttpServletResponse response = (HttpServletResponse) context
            .getExternalContext().getResponse();
            response.addHeader("Content-disposition","attachment;filename="+nomFichier+" .pdf");
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
            response.setContentType("application/pdf");
            context.responseComplete();*/
        } catch (Exception ex) {
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**************************send***************************************/
    public void jasperDesignModelSendPdf() throws JRException, IOException, MessagingException {
        try {
            int xL = 0;
            int nb = 0;
            int pageWidth = 0;
            if (flagDate == true) {
                xL = xL + 80;
                nb++;
            }
            if (flagMontantEspece == true) {
                xL = xL + 60;
                nb++;
            }
            if (flagMontantCheque == true) {
                xL = xL + 60;
                nb++;
            }
            if (flagMontantTraite == true) {
                xL = xL + 60;
                nb++;
            }
            if (flagMontantVirement == true) {
                xL = xL + 60;
                nb++;
            }
            if (flagNomClientF == true) {
                xL = xL + 200;
                nb++;
            }
             if (flagLibelleOperation == true) {
                xL = xL + 200;
                nb++;
            }
            if (flagCodeClientF == true) {
                xL = xL + 50;
                nb++;
            }
            if (flagBanque == true) {
                xL = xL + 70;
                nb++;
            }
            if (flagRefCheque == true) {
                xL = xL + 50;
                nb++;
            }
            if (flagecheanceCheque == true) {
                xL = xL + 70;
                nb++;
            }
            System.out.println("xL" + xL);
            if (optionOrient.equals("2")) {
                pageWidth = 842;
            } else {
                pageWidth = 595;
            }
            int Xajout = (pageWidth - xL) / nb;
            int reste = (pageWidth - xL) - (Xajout * nb);

         //   System.out.println("optionOrient" + optionOrient);
            JRDesignFrame jrFame = new JRDesignFrame();
            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

            DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d22 = new Date();
            String ddd = dateFormat3.format(d22);
            JasperDesign jasperDesign = new JasperDesign();


            JRDesignBand bandTiltle = new JRDesignBand();

            JRDesignStaticText libelleDateSys = new JRDesignStaticText();
            libelleDateSys.setWidth(150);
            libelleDateSys.setHeight(20);
            libelleDateSys.setText("DATE : " + ddd);
            libelleDateSys.setFontSize(10);
            libelleDateSys.setForecolor(Color.BLACK);
            libelleDateSys.setX(440);
            libelleDateSys.setY(10);
            jrFame.addElement(libelleDateSys);

            JRDesignBand bandBlanc = new JRDesignBand();
            bandBlanc.setHeight(200);
            JRDesignStaticText libellePgeB = new JRDesignStaticText();
            libellePgeB.setWidth(50);
            libellePgeB.setHeight(20);
            libellePgeB.setX(0);
            libellePgeB.setY(30);
            libellePgeB.setText("Tel :");
            libellePgeB.setFontSize(12);
            libellePgeB.setForecolor(Color.BLACK);
            bandBlanc.addElement(libellePgeB);
            jasperDesign.setNoData(bandBlanc);

            dateDebut.setHours(0);
            dateDebut.setMinutes(0);
            dateDebut.setSeconds(0);
            dateFin.setHours(23);
            dateFin.setMinutes(59);
            dateFin.setSeconds(59);

            JRDesignBand bandSomme = new JRDesignBand();
            bandSomme.setHeight(80);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String d1 = dateFormat.format(dateDebut);
            String d2 = dateFormat.format(dateFin);
            DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            String d111 = dateFormat1.format(dateDebut);
            String d222 = dateFormat1.format(dateFin);
            Xligne = 0;
         //   System.out.println("d1" + d1);
            etablirconnection();
            JRDesignQuery query = new JRDesignQuery();
            String Filt_typeReg = "";
            String ty = "";

            //     and (mode_reglement_com.`NOM_MR` = '"+ty+"' "+Filt_typeReg+") 

            if ((flagMontantCheque == true) || (flagMontantTraite == true) || (flagMontantEspece == true) || (flagMontantVirement == true)) {
                Filt_typeReg = Filt_typeReg + " and ( ";

                if (flagMontantCheque == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'cheque'";
                }
                if (flagMontantTraite == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'traite'";
                }
                if (flagMontantEspece == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'espece'";
                }
                if (flagMontantVirement == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'virement'";
                }
                Filt_typeReg = Filt_typeReg + " )";


            }
            System.out.println(Filt_typeReg);
            /*  
            if(flagMontantCheque==true){ ty="cheque";
            if(flagMontantTraite==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'traite'";}
            if(flagMontantEspece==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'espece'";}
            if(flagMontantVirement==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'virement'";}
            }else{
            
            if(flagMontantCheque==false){
            if(flagMontantTraite==true){
            if(flagMontantTraite==true){ ty="traite";}
            if(flagMontantEspece==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'espece'";}
            if(flagMontantVirement==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'virement'";}
            
            }}else{
            
            
            
            if((flagMontantCheque==false) && (flagMontantTraite==false)){
            
            if(flagMontantEspece==true){ty="espece";
            if(flagMontantVirement==true){ Filt_typeReg=Filt_typeReg+=" or mode_reglement_com.`NOM_MR` = 'virement'";} }
            
            }else{
            if((flagMontantCheque==false) && (flagMontantTraite==false)&& (flagMontantEspece==false)){
            if(flagMontantVirement==true){ ty="virement";}}
            }
            }*/

            String q1 = "";

            if (filtreTiersEtat != null) {
                q1 = " and operation_com.`NI_TI` =" + filtreTiersEtat.getId();
            }
                String q = "SELECT DATE_SYS,point_vente_com.`NOM_PV` AS point_vente_com_NOM_PV,caissier_com.`NOM_CAI` AS caissier_com_NOM_CAI,operation_com.`NUM_OP` AS operation_com_NUM_OP,operation_com.`DATE_OP` AS operation_com_DATE_OP,tiers_com.`CODE_TI` AS tiers_com_CODE_TI,tiers_com.`NOM_TI` AS tiers_com_NOM_TI,type_op_com.`NOM_TOP` AS type_op_com_NOM_TOP,operation_com.`MONTANT_COP` AS operation_com_MONTANT_COP,operation_com.`NI_MR` AS operation_com_NI_MR,mode_reglement_com.`NI_MR` AS mode_reglement_com_NI_MR,mode_reglement_com.`CODE_MR` AS mode_reglement_com_CODE_MR,mode_reglement_com.`NOM_MR` AS mode_reglement_com_NOM_MR,operation_com.`REFERENCE_OP` AS operation_com_REFERENCE_OP,banque_com.`NOM_BANQUE` AS banque_com_NOM_BANQUE,MONTANT_ES,MONTANT_TR,MONTANT_VIR,MONTANT_CH,ECHANCE_OP , horizon_sys.`id` AS horizon_sys_id,horizon_sys.`CODE` AS horizon_sys_CODE,horizon_sys.`LIBELLE_SOCIETE` AS horizon_sys_LIBELLE_SOCIETE, horizon_sys.`ADRESSE` AS horizon_sys_ADRESSE,horizon_sys.`FAX` AS horizon_sys_FAX,horizon_sys.`TEL` AS horizon_sys_TEL FROM `tiers_com` tiers_com INNER JOIN `operation_com` operation_com ON tiers_com.`NI_TI` = operation_com.`NI_TI` INNER JOIN `type_op_com` type_op_com ON operation_com.`NI_TOP` = type_op_com.`NI_TOP` INNER JOIN `mode_reglement_com` mode_reglement_com ON operation_com.`NI_MR` = mode_reglement_com.`NI_MR` INNER JOIN `point_vente_com` point_vente_com ON operation_com.`NI_PV` = point_vente_com.`NI_PV` LEFT JOIN `caissier_com` caissier_com ON operation_com.`NI_CAI` = caissier_com.`NI_CAI` LEFT JOIN `banque_com` banque_com ON operation_com.`NI_BANQUE` = banque_com.`NI_BANQUE` ,`horizon_sys` horizon_sys  WHERE type_op_com.`NOM_TOP` = 'reglement' " + Filt_typeReg + " and DATE_SYS >= '" + d1 + "' and DATE_SYS <= '" + d2 + "'" + q1 + " order by DATE_SYS ";



            query.setText(q);
            jasperDesign.setQuery(query);
            jasperDesign.setName("ReglementDesign");

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

            jrFame.setHeight(72);
            jrFame.setWidth(842);
            jrFame.getLineBox().setLeftPadding(0);
            jrFame.getLineBox().setTopPadding(0);
            jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

            JRDesignStaticText libelleTitre = new JRDesignStaticText();
            libelleTitre.setWidth(360);
            libelleTitre.setHeight(31);
            libelleTitre.setX(200);
            libelleTitre.setY(30);
            libelleTitre.setText("ETAT REGLEMENTS CLIENTS");
            libelleTitre.setFontSize(18);
            libelleTitre.setForecolor(Color.BLUE);
            jrFame.addElement(libelleTitre);

            JRDesignStaticText libelleTotal = new JRDesignStaticText();
            libelleTotal.setWidth(310);
            libelleTotal.setHeight(31);
            libelleTotal.setX(0);
            libelleTotal.setY(7);
            libelleTotal.setText("TOTAL ");
            libelleTotal.setFontSize(12);
            libelleTotal.setForecolor(Color.BLACK);
            bandSomme.addElement(libelleTotal);


            JRDesignStaticText libelleDate = new JRDesignStaticText();
            libelleDate.setWidth(200);
            libelleDate.setHeight(15);
            libelleDate.setText(" PERIODE : Du  " + d111 + "  Au  " + d222);
            libelleDate.setFontSize(10);
            libelleDate.setForecolor(Color.BLACK);
            libelleDate.setX(200);
            libelleDate.setY(55);
            jrFame.addElement(libelleDate);
            bandTiltle.setHeight(82);


            /**************************************************/
            JRDesignField fieldSTE = new JRDesignField();
            fieldSTE.setName("horizon_sys_LIBELLE_SOCIETE");
            fieldSTE.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldSTE);

            JRDesignTextField textFieldSTE = new JRDesignTextField();
            textFieldSTE.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldSTE.setStretchWithOverflow(true);
            textFieldSTE.setFontSize(10);
            textFieldSTE.setWidth(150);
            textFieldSTE.setX(0);
            textFieldSTE.setY(0);
            JRDesignExpression expressionSTE = new JRDesignExpression();
            expressionSTE.setValueClass(java.lang.String.class);
            expressionSTE.setText("$F{horizon_sys_LIBELLE_SOCIETE}");
            textFieldSTE.setExpression(expressionSTE);

            jrFame.addElement(textFieldSTE);

            /*******************************************************************/
            JRDesignField fieldAdresse = new JRDesignField();
            fieldAdresse.setName("horizon_sys_Adresse");
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
            expressionAdresse.setText("$F{horizon_sys_Adresse}");
            textFieldAdresse.setExpression(expressionAdresse);

            jrFame.addElement(textFieldAdresse);

            /*************************************************************/
            JRDesignStaticText libelleTEL = new JRDesignStaticText();
            libelleTEL.setWidth(50);
            libelleTEL.setHeight(20);
            libelleTEL.setX(2);
            libelleTEL.setY(30);
            libelleTEL.setText("Tel  :");
            libelleTEL.setFontSize(10);
            libelleTEL.setForecolor(Color.BLACK);
            jrFame.addElement(libelleTEL);

            JRDesignField fieldTel = new JRDesignField();
            fieldTel.setName("horizon_sys_Tel");
            fieldTel.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldTel);

            JRDesignTextField textFieldTel = new JRDesignTextField();
            textFieldTel.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldTel.setStretchWithOverflow(true);
            textFieldTel.setFontSize(10);
            textFieldTel.setWidth(100);
            textFieldTel.setX(30);
            textFieldTel.setY(30);
            JRDesignExpression expressionTel = new JRDesignExpression();
            expressionTel.setValueClass(java.lang.String.class);
            expressionTel.setText("$F{horizon_sys_Tel}");
            textFieldTel.setExpression(expressionTel);

            jrFame.addElement(textFieldTel);
            /*******************************************************/
            /*********************************************************/
            JRDesignStaticText libelleFax = new JRDesignStaticText();
            libelleFax.setWidth(50);
            libelleFax.setHeight(20);
            libelleFax.setX(2);
            libelleFax.setY(45);
            libelleFax.setText("Fax :");
            libelleFax.setFontSize(10);
            libelleFax.setForecolor(Color.BLACK);
            jrFame.addElement(libelleFax);

            JRDesignField fieldFax = new JRDesignField();
            fieldFax.setName("horizon_sys_Fax");
            fieldFax.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldFax);

            JRDesignTextField textFieldFax = new JRDesignTextField();
            textFieldFax.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldFax.setStretchWithOverflow(true);
            textFieldFax.setFontSize(10);
            textFieldFax.setWidth(100);
            textFieldFax.setX(30);
            textFieldFax.setY(45);
            JRDesignExpression expressionFax = new JRDesignExpression();
            expressionFax.setValueClass(java.lang.String.class);
            expressionFax.setText("$F{horizon_sys_Fax}");
            textFieldFax.setExpression(expressionFax);

            jrFame.addElement(textFieldFax);
            /********************************/
            bandTiltle.addElement(jrFame);
            jasperDesign.setTitle(bandTiltle);
            /*******************************************************/
            JRDesignBand bandHeader = new JRDesignBand();
            BigDecimal bandHeaderHeight = new BigDecimal(15).setScale(0, RoundingMode.DOWN);
            bandHeader.setHeight(bandHeaderHeight.intValue());


            JRDesignBand columnHeader = new JRDesignBand();
            columnHeader.setHeight(15);



            if (flagDate == true) {
                JRDesignStaticText libelleColonneDate = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "DATE_SYS", "$F{DATE_SYS}", libelleColonneDate, "DATE", 0, 60, bandSomme);
                columnHeader.addElement(libelleColonneDate);
            }




            if (flagMontantEspece == true) {
                JRDesignStaticText libelleColonneEspece = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "MONTANT_ES", "($F{MONTANT_ES}!=null)?$F{MONTANT_ES}:Character.toString(' ')", libelleColonneEspece, "ESPECE", 70, 50, bandSomme);
                columnHeader.addElement(libelleColonneEspece);
            }

            if (flagMontantCheque == true) {
                JRDesignStaticText libelleColonneCheque = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "MONTANT_CH", "($F{MONTANT_CH}!=null)?$F{MONTANT_CH}:Character.toString(' ')", libelleColonneCheque, "CHEQUE", 140, 55, bandSomme);
                columnHeader.addElement(libelleColonneCheque);
            }

            if (flagMontantTraite == true) {
                JRDesignStaticText libelleColonneTraite = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "MONTANT_TR", "($F{MONTANT_TR}!=null)?$F{MONTANT_TR}:Character.toString(' ')", libelleColonneTraite, "TRAITE", 210, 50, bandSomme);
                columnHeader.addElement(libelleColonneTraite);
            }

            if (flagMontantVirement == true) {
                JRDesignStaticText libelleColonneVirement = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "MONTANT_VIR", "($F{MONTANT_VIR}!=null)?$F{MONTANT_VIR}:Character.toString(' ')", libelleColonneVirement, "VIREMENT", 280, 50, bandSomme);
                columnHeader.addElement(libelleColonneVirement);
            }

            if (flagNomClientF == true) {
                JRDesignStaticText libelleColonneNomTI = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "tiers_com_NOM_TI", "$F{tiers_com_NOM_TI}", libelleColonneNomTI, "RAISON SOCIALE", 350, 200 + Xajout, bandSomme);
                columnHeader.addElement(libelleColonneNomTI);
            }


            if (flagCodeClientF == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "tiers_com_CODE_TI", "$F{tiers_com_CODE_TI}", libelleColonneCode, "CODE", 500, 50 + Xajout, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }


            if (flagBanque == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "banque_com_NOM_BANQUE", "($F{banque_com_NOM_BANQUE}!=null)?$F{banque_com_NOM_BANQUE}:Character.toString(' ')", libelleColonneCode, "BANQUE", 500, 100 + Xajout + reste, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }
            if (flagPointVente == true) {
                JRDesignStaticText libelleColonnePointVente = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "point_vente_com_NOM_PV", "($F{point_vente_com_NOM_PV}!=null)?$F{point_vente_com_NOM_PV}:Character.toString(' ')", libelleColonnePointVente, "PT VENTE", 500, 60 + Xajout + reste, bandSomme);
                columnHeader.addElement(libelleColonnePointVente);
            }
            if (flagCaissier == true) {
                JRDesignStaticText libelleColonneCaissier = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "caissier_com_NOM_CAI", "($F{caissier_com_NOM_CAI}!=null)?$F{caissier_com_NOM_CAI}:Character.toString(' ')", libelleColonneCaissier, "CAISSIER", 500, 60 + Xajout + reste, bandSomme);
                columnHeader.addElement(libelleColonneCaissier);
            }

            if (flagRefCheque == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_com_REFERENCE_OP", "($F{operation_com_REFERENCE_OP}!=null)?$F{operation_com_REFERENCE_OP}:Character.toString(' ')", libelleColonneCode, "REFERENCE", 500, 50 + Xajout, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }

            if (flagecheanceCheque == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "ECHANCE_OP", "$F{ECHANCE_OP}", libelleColonneCode, "ECHEANCE", 500, 50 + Xajout, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }

            jasperDesign.setColumnHeader(columnHeader);
        //    jasperDesign.setDetail(bandHeader);
                  ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandHeader);
            jasperDesign.setSummary(bandSomme);

            /***********************************Somme Total***************************************/
            //  JRDesignTextField textFieldSomme = new JRDesignTextField(); 
            JRDesignField montant = new JRDesignField();
            montant.setName("operation_montant_credite");
            montant.setValueClass(java.math.BigDecimal.class);
            jasperDesign.addField(montant);




            JRDesignVariable varSomme = new JRDesignVariable();
            varSomme.setName("varTotal");
            varSomme.setValueClass(java.math.BigDecimal.class);
            varSomme.setCalculation(CalculationEnum.getByValue(new Byte("2")));
            jasperDesign.addVariable(varSomme);

            JRDesignExpression expressionE = new JRDesignExpression();
            expressionE.setValueClass(java.math.BigDecimal.class);
            expressionE.setText("$F{operation_montant_credite}");
            varSomme.setExpression(expressionE);


            JRDesignExpression expressionEI = new JRDesignExpression();
            expressionEI.setValueClass(java.math.BigDecimal.class);
            expressionEI.setText("new BigDecimal(0).setScale(3, RoundingMode.UP)");

            varSomme.setInitialValueExpression(expressionEI);


            JRDesignTextField textField = new JRDesignTextField();
            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textField.setStretchWithOverflow(true);
            textField.setFontSize(12);
            textField.setWidth(100);
            textField.setX(75);
            textField.setY(45);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setValueClass(java.math.BigDecimal.class);
            expression.setText("$V{varTotal}");
            textField.setExpression(expression);
            textField.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));


            JRDesignStaticText libelleTotalF = new JRDesignStaticText();
            libelleTotalF.setWidth(150);
            libelleTotalF.setHeight(31);
            libelleTotalF.setX(0);
            libelleTotalF.setY(45);
            libelleTotalF.setText("TOTAL GENERAL :");
            libelleTotalF.setFontSize(12);
            libelleTotalF.setForecolor(Color.BLUE);
            bandSomme.addElement(libelleTotalF);


            bandSomme.addElement(textField);
            /*************************************************************/
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
            textField2.setY(0);
            textField2.setWidth(150);
            textField2.setHeight(15);
           textField2.getLineBox().setLeftPadding(0);
            textField2.getLineBox().setTopPadding(0);
            textField2.setStretchWithOverflow(false);
            textField2.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            JRDesignExpression expression2 = new JRDesignExpression();
            expression2.setValueClass(java.lang.String.class);
            expression2.setText("$V{PAGE_NUMBER}.toString()");
            textField2.setExpression(expression2);
            textField2.setEvaluationTime(EvaluationTimeEnum.REPORT);
            bandFooter.addElement(textField2);
            jasperDesign.setPageFooter(bandFooter);
            /*************************************************************************************************/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap parameters = new HashMap();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

            DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
            Date d = new Date();
            String dd = dateFormat15.format(d);
            String nomFichier = "Rapport Reglement " + dd + ".pdf";


            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            FileOutputStream fs = new FileOutputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport") + "\\" + nomFichier));
            BufferedOutputStream bs = new BufferedOutputStream(fs);
            bs.write(bytes);
            bs.close();
            File ffile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport") + "\\" + nomFichier);

            final String usernameEmail = this.emailSender;
            final String passwordEmail = this.passwordEmailSender;

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

//		Session session = Session.getInstance(props,
//		  new javax.mail.Authenticator() {
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(usernameEmail, passwordEmail);
//			}
//		  });
            Session session = Session.getInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.emailSender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.emailReceiver));
            message.setSubject("Rapport Du Logiciel Horizon");
            message.setText("cet email contient le rapport : " + nomFichier);
            Multipart mp = new MimeMultipart();
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setContent(nomFichier, "text/plain");
            mp.addBodyPart(mbp1);
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setFileName(ffile.getName());
            mbp.setDataHandler(new DataHandler(new FileDataSource(ffile)));
            mp.addBodyPart(mbp);
            message.setContent(mp);
//			Transport.send(message);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", this.emailSender, this.passwordEmailSender);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            JsfUtil.addSuccessMessage("mail envoyer");
        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Verifier votre connexion internet, votre login et votre password");
        }
    }

    /***********************************************************************************/
    public void jasperDesignModelSendExcel() throws JRException, IOException, MessagingException {
        try {
            int xL = 0;
            int nb = 0;
            int pageWidth = 0;
            if (flagDate == true) {
                xL = xL + 80;
                nb++;
            }
            if (flagMontantEspece == true) {
                xL = xL + 60;
                nb++;
            }
            if (flagMontantCheque == true) {
                xL = xL + 60;
                nb++;
            }
            if (flagMontantTraite == true) {
                xL = xL + 60;
                nb++;
            }
            if (flagMontantVirement == true) {
                xL = xL + 60;
                nb++;
            }
            if (flagNomClientF == true) {
                xL = xL + 200;
                nb++;
            }
             if (flagLibelleOperation == true) {
                xL = xL + 200;
                nb++;
            }
            if (flagCodeClientF == true) {
                xL = xL + 50;
                nb++;
            }
            if (flagBanque == true) {
                xL = xL + 70;
                nb++;
            }
            if (flagRefCheque == true) {
                xL = xL + 50;
                nb++;
            }
            if (flagecheanceCheque == true) {
                xL = xL + 70;
                nb++;
            }
         //   System.out.println("xL" + xL);
            if (optionOrient.equals("2")) {
                pageWidth = 842;
            } else {
                pageWidth = 595;
            }
            int Xajout = (pageWidth - xL) / nb;
            int reste = (pageWidth - xL) - (Xajout * nb);

          //  System.out.println("optionOrient" + optionOrient);
            JRDesignFrame jrFame = new JRDesignFrame();
            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

            DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d22 = new Date();
            String ddd = dateFormat3.format(d22);
            JasperDesign jasperDesign = new JasperDesign();


            JRDesignBand bandTiltle = new JRDesignBand();

            JRDesignStaticText libelleDateSys = new JRDesignStaticText();
            libelleDateSys.setWidth(150);
            libelleDateSys.setHeight(20);
            libelleDateSys.setText("DATE : " + ddd);
            libelleDateSys.setFontSize(10);
            libelleDateSys.setForecolor(Color.BLACK);
            libelleDateSys.setX(440);
            libelleDateSys.setY(10);
            jrFame.addElement(libelleDateSys);

            JRDesignBand bandBlanc = new JRDesignBand();
            bandBlanc.setHeight(200);
            JRDesignStaticText libellePgeB = new JRDesignStaticText();
            libellePgeB.setWidth(50);
            libellePgeB.setHeight(20);
            libellePgeB.setX(0);
            libellePgeB.setY(30);
            libellePgeB.setText("Tel :");
            libellePgeB.setFontSize(12);
            libellePgeB.setForecolor(Color.BLACK);
            bandBlanc.addElement(libellePgeB);
            jasperDesign.setNoData(bandBlanc);


          dateDebut.setHours(0);
            dateDebut.setMinutes(0);
            dateDebut.setSeconds(0);
            dateFin.setHours(23);
            dateFin.setMinutes(59);
            dateFin.setSeconds(59);

            JRDesignBand bandSomme = new JRDesignBand();
            bandSomme.setHeight(80);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String d1 = dateFormat.format(dateDebut);
            String d2 = dateFormat.format(dateFin);
            DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            String d111 = dateFormat1.format(dateDebut);
            String d222 = dateFormat1.format(dateFin);
            Xligne = 0;
          //  System.out.println("d1" + d1);
            etablirconnection();
            JRDesignQuery query = new JRDesignQuery();
            String Filt_typeReg = "";
            String ty = "";

            //     and (mode_reglement_com.`NOM_MR` = '"+ty+"' "+Filt_typeReg+") 

            if ((flagMontantCheque == true) || (flagMontantTraite == true) || (flagMontantEspece == true) || (flagMontantVirement == true)) {
                Filt_typeReg = Filt_typeReg + " and ( ";

                if (flagMontantCheque == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'cheque'";
                }
                if (flagMontantTraite == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'traite'";
                }
                if (flagMontantEspece == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'espece'";
                }
                if (flagMontantVirement == true) {
                    if (Filt_typeReg.endsWith("and ( ") == false) {
                        Filt_typeReg = Filt_typeReg + " or ";
                    }
                    Filt_typeReg = Filt_typeReg + "  mode_reglement_com.`NOM_MR` = 'virement'";
                }
                Filt_typeReg = Filt_typeReg + " )";


            }
            System.out.println(Filt_typeReg);
            /*  
            if(flagMontantCheque==true){ ty="cheque";
            if(flagMontantTraite==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'traite'";}
            if(flagMontantEspece==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'espece'";}
            if(flagMontantVirement==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'virement'";}
            }else{
            
            if(flagMontantCheque==false){
            if(flagMontantTraite==true){
            if(flagMontantTraite==true){ ty="traite";}
            if(flagMontantEspece==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'espece'";}
            if(flagMontantVirement==true){ Filt_typeReg=Filt_typeReg+" or mode_reglement_com.`NOM_MR` = 'virement'";}
            
            }}else{
            
            
            
            if((flagMontantCheque==false) && (flagMontantTraite==false)){
            
            if(flagMontantEspece==true){ty="espece";
            if(flagMontantVirement==true){ Filt_typeReg=Filt_typeReg+=" or mode_reglement_com.`NOM_MR` = 'virement'";} }
            
            }else{
            if((flagMontantCheque==false) && (flagMontantTraite==false)&& (flagMontantEspece==false)){
            if(flagMontantVirement==true){ ty="virement";}}
            }
            }*/

            String q1 = "";

            if (filtreTiersEtat != null) {
                q1 = " and operation_com.`NI_TI` =" + filtreTiersEtat.getId();
            }
               String q = "SELECT DATE_SYS,point_vente_com.`NOM_PV` AS point_vente_com_NOM_PV,caissier_com.`NOM_CAI` AS caissier_com_NOM_CAI,operation_com.`NUM_OP` AS operation_com_NUM_OP,operation_com.`DATE_OP` AS operation_com_DATE_OP,tiers_com.`CODE_TI` AS tiers_com_CODE_TI,tiers_com.`NOM_TI` AS tiers_com_NOM_TI,type_op_com.`NOM_TOP` AS type_op_com_NOM_TOP,operation_com.`MONTANT_COP` AS operation_com_MONTANT_COP,operation_com.`NI_MR` AS operation_com_NI_MR,mode_reglement_com.`NI_MR` AS mode_reglement_com_NI_MR,mode_reglement_com.`CODE_MR` AS mode_reglement_com_CODE_MR,mode_reglement_com.`NOM_MR` AS mode_reglement_com_NOM_MR,operation_com.`REFERENCE_OP` AS operation_com_REFERENCE_OP,banque_com.`NOM_BANQUE` AS banque_com_NOM_BANQUE,MONTANT_ES,MONTANT_TR,MONTANT_VIR,MONTANT_CH,ECHANCE_OP , horizon_sys.`id` AS horizon_sys_id,horizon_sys.`CODE` AS horizon_sys_CODE,horizon_sys.`LIBELLE_SOCIETE` AS horizon_sys_LIBELLE_SOCIETE, horizon_sys.`ADRESSE` AS horizon_sys_ADRESSE,horizon_sys.`FAX` AS horizon_sys_FAX,horizon_sys.`TEL` AS horizon_sys_TEL  FROM `tiers_com` tiers_com INNER JOIN `operation_com` operation_com ON tiers_com.`NI_TI` = operation_com.`NI_TI` INNER JOIN `type_op_com` type_op_com ON operation_com.`NI_TOP` = type_op_com.`NI_TOP` INNER JOIN `mode_reglement_com` mode_reglement_com ON operation_com.`NI_MR` = mode_reglement_com.`NI_MR` INNER JOIN `point_vente_com` point_vente_com ON operation_com.`NI_PV` = point_vente_com.`NI_PV` LEFT JOIN `caissier_com` caissier_com ON operation_com.`NI_CAI` = caissier_com.`NI_CAI` LEFT JOIN `banque_com` banque_com ON operation_com.`NI_BANQUE` = banque_com.`NI_BANQUE` ,`horizon_sys` horizon_sys  WHERE type_op_com.`NOM_TOP` = 'reglement' " + Filt_typeReg + " and DATE_SYS >= '" + d1 + "' and DATE_SYS <= '" + d2 + "'" + q1  + " order by DATE_SYS ";



            query.setText(q);
            jasperDesign.setQuery(query);
            jasperDesign.setName("ReglementDesign");

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

            jrFame.setHeight(72);
            jrFame.setWidth(842);
           jrFame.getLineBox().setLeftPadding(2);
          
            jrFame.getLineBox().setTopPadding(0);
            jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

            JRDesignStaticText libelleTitre = new JRDesignStaticText();
            libelleTitre.setWidth(360);
            libelleTitre.setHeight(31);
            libelleTitre.setX(200);
            libelleTitre.setY(30);
            libelleTitre.setText("ETAT REGLEMENTS CLIENTS");
            libelleTitre.setFontSize(18);
            libelleTitre.setForecolor(Color.BLUE);
            jrFame.addElement(libelleTitre);

            JRDesignStaticText libelleTotal = new JRDesignStaticText();
            libelleTotal.setWidth(310);
            libelleTotal.setHeight(31);
            libelleTotal.setX(0);
            libelleTotal.setY(7);
            libelleTotal.setText("TOTAL ");
            libelleTotal.setFontSize(12);
            libelleTotal.setForecolor(Color.BLACK);
            bandSomme.addElement(libelleTotal);


            JRDesignStaticText libelleDate = new JRDesignStaticText();
            libelleDate.setWidth(200);
            libelleDate.setHeight(15);
            libelleDate.setText(" PERIODE : Du  " + d111 + "  Au  " + d222);
            libelleDate.setFontSize(10);
            libelleDate.setForecolor(Color.BLACK);
            libelleDate.setX(200);
            libelleDate.setY(55);
            jrFame.addElement(libelleDate);
            bandTiltle.setHeight(82);


            /**************************************************/
            JRDesignField fieldSTE = new JRDesignField();
            fieldSTE.setName("horizon_sys_LIBELLE_SOCIETE");
            fieldSTE.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldSTE);

            JRDesignTextField textFieldSTE = new JRDesignTextField();
            textFieldSTE.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldSTE.setStretchWithOverflow(true);
            textFieldSTE.setFontSize(10);
            textFieldSTE.setWidth(150);
            textFieldSTE.setX(0);
            textFieldSTE.setY(0);
            JRDesignExpression expressionSTE = new JRDesignExpression();
            expressionSTE.setValueClass(java.lang.String.class);
            expressionSTE.setText("$F{horizon_sys_LIBELLE_SOCIETE}");
            textFieldSTE.setExpression(expressionSTE);

            jrFame.addElement(textFieldSTE);

            /*******************************************************************/
            JRDesignField fieldAdresse = new JRDesignField();
            fieldAdresse.setName("horizon_sys_Adresse");
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
            expressionAdresse.setText("$F{horizon_sys_Adresse}");
            textFieldAdresse.setExpression(expressionAdresse);

            jrFame.addElement(textFieldAdresse);

            /*************************************************************/
            JRDesignStaticText libelleTEL = new JRDesignStaticText();
            libelleTEL.setWidth(50);
            libelleTEL.setHeight(20);
            libelleTEL.setX(2);
            libelleTEL.setY(30);
            libelleTEL.setText("Tel  :");
            libelleTEL.setFontSize(10);
            libelleTEL.setForecolor(Color.BLACK);
            jrFame.addElement(libelleTEL);

            JRDesignField fieldTel = new JRDesignField();
            fieldTel.setName("horizon_sys_Tel");
            fieldTel.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldTel);

            JRDesignTextField textFieldTel = new JRDesignTextField();
            textFieldTel.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldTel.setStretchWithOverflow(true);
            textFieldTel.setFontSize(10);
            textFieldTel.setWidth(100);
            textFieldTel.setX(30);
            textFieldTel.setY(30);
            JRDesignExpression expressionTel = new JRDesignExpression();
            expressionTel.setValueClass(java.lang.String.class);
            expressionTel.setText("$F{horizon_sys_Tel}");
            textFieldTel.setExpression(expressionTel);

            jrFame.addElement(textFieldTel);
            /*******************************************************/
            /*********************************************************/
            JRDesignStaticText libelleFax = new JRDesignStaticText();
            libelleFax.setWidth(50);
            libelleFax.setHeight(20);
            libelleFax.setX(2);
            libelleFax.setY(45);
            libelleFax.setText("Fax :");
            libelleFax.setFontSize(10);
            libelleFax.setForecolor(Color.BLACK);
            jrFame.addElement(libelleFax);

            JRDesignField fieldFax = new JRDesignField();
            fieldFax.setName("horizon_sys_Fax");
            fieldFax.setValueClass(java.lang.String.class);
            jasperDesign.addField(fieldFax);

            JRDesignTextField textFieldFax = new JRDesignTextField();
            textFieldFax.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textFieldFax.setStretchWithOverflow(true);
            textFieldFax.setFontSize(10);
            textFieldFax.setWidth(100);
            textFieldFax.setX(30);
            textFieldFax.setY(45);
            JRDesignExpression expressionFax = new JRDesignExpression();
            expressionFax.setValueClass(java.lang.String.class);
            expressionFax.setText("$F{horizon_sys_Fax}");
            textFieldFax.setExpression(expressionFax);

            jrFame.addElement(textFieldFax);
            /********************************/
            bandTiltle.addElement(jrFame);
            jasperDesign.setTitle(bandTiltle);
            /*******************************************************/
            JRDesignBand bandHeader = new JRDesignBand();
            BigDecimal bandHeaderHeight = new BigDecimal(15).setScale(0, RoundingMode.DOWN);
            bandHeader.setHeight(bandHeaderHeight.intValue());


            JRDesignBand columnHeader = new JRDesignBand();
            columnHeader.setHeight(15);



            if (flagDate == true) {
                JRDesignStaticText libelleColonneDate = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "DATE_SYS", "$F{DATE_SYS}", libelleColonneDate, "DATE", 0, 60, bandSomme);
                columnHeader.addElement(libelleColonneDate);
            }




            if (flagMontantEspece == true) {
                JRDesignStaticText libelleColonneEspece = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "MONTANT_ES", "($F{MONTANT_ES}!=null)?$F{MONTANT_ES}:Character.toString(' ')", libelleColonneEspece, "ESPECE", 70, 50, bandSomme);
                columnHeader.addElement(libelleColonneEspece);
            }

            if (flagMontantCheque == true) {
                JRDesignStaticText libelleColonneCheque = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "MONTANT_CH", "($F{MONTANT_CH}!=null)?$F{MONTANT_CH}:Character.toString(' ')", libelleColonneCheque, "CHEQUE", 140, 55, bandSomme);
                columnHeader.addElement(libelleColonneCheque);
            }

            if (flagMontantTraite == true) {
                JRDesignStaticText libelleColonneTraite = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "MONTANT_TR", "($F{MONTANT_TR}!=null)?$F{MONTANT_TR}:Character.toString(' ')", libelleColonneTraite, "TRAITE", 210, 50, bandSomme);
                columnHeader.addElement(libelleColonneTraite);
            }

            if (flagMontantVirement == true) {
                JRDesignStaticText libelleColonneVirement = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "MONTANT_VIR", "($F{MONTANT_VIR}!=null)?$F{MONTANT_VIR}:Character.toString(' ')", libelleColonneVirement, "VIREMENT", 280, 50, bandSomme);
                columnHeader.addElement(libelleColonneVirement);
            }

            if (flagNomClientF == true) {
                JRDesignStaticText libelleColonneNomTI = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "tiers_com_NOM_TI", "$F{tiers_com_NOM_TI}", libelleColonneNomTI, "RAISON SOCIALE", 350, 200 + Xajout, bandSomme);
                columnHeader.addElement(libelleColonneNomTI);
            }


            if (flagCodeClientF == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "tiers_com_CODE_TI", "$F{tiers_com_CODE_TI}", libelleColonneCode, "CODE", 500, 50 + Xajout, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }


            if (flagBanque == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "banque_com_NOM_BANQUE", "($F{banque_com_NOM_BANQUE}!=null)?$F{banque_com_NOM_BANQUE}:Character.toString(' ')", libelleColonneCode, "BANQUE", 500, 100 + Xajout + reste, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }
            if (flagPointVente == true) {
                JRDesignStaticText libelleColonnePointVente = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "point_vente_com_NOM_PV", "($F{point_vente_com_NOM_PV}!=null)?$F{point_vente_com_NOM_PV}:Character.toString(' ')", libelleColonnePointVente, "PT VENTE", 500, 60 + Xajout + reste, bandSomme);
                columnHeader.addElement(libelleColonnePointVente);
            }
            if (flagCaissier == true) {
                JRDesignStaticText libelleColonneCaissier = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "caissier_com_NOM_CAI", "($F{caissier_com_NOM_CAI}!=null)?$F{caissier_com_NOM_CAI}:Character.toString(' ')", libelleColonneCaissier, "CAISSIER", 500, 60 + Xajout + reste, bandSomme);
                columnHeader.addElement(libelleColonneCaissier);
            }

            if (flagRefCheque == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_com_REFERENCE_OP", "($F{operation_com_REFERENCE_OP}!=null)?$F{operation_com_REFERENCE_OP}:Character.toString(' ')", libelleColonneCode, "REFERENCE", 500, 50 + Xajout, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }

            if (flagecheanceCheque == true) {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "ECHANCE_OP", "$F{ECHANCE_OP}", libelleColonneCode, "ECHEANCE", 500, 50 + Xajout, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }

            jasperDesign.setColumnHeader(columnHeader);
          //  jasperDesign.setDetail(bandHeader);
                  ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandHeader);
            jasperDesign.setSummary(bandSomme);

            /***********************************Somme Total***************************************/
            //  JRDesignTextField textFieldSomme = new JRDesignTextField(); 
            JRDesignField montant = new JRDesignField();
            montant.setName("operation_com_MONTANT_COP");
            montant.setValueClass(java.math.BigDecimal.class);
            jasperDesign.addField(montant);




            JRDesignVariable varSomme = new JRDesignVariable();
            varSomme.setName("varTotal");
            varSomme.setValueClass(java.math.BigDecimal.class);
            varSomme.setCalculation(CalculationEnum.getByValue(new Byte("2")));
            jasperDesign.addVariable(varSomme);

            JRDesignExpression expressionE = new JRDesignExpression();
            expressionE.setValueClass(java.math.BigDecimal.class);
            expressionE.setText("$F{operation_com_MONTANT_COP}");
            varSomme.setExpression(expressionE);


            JRDesignExpression expressionEI = new JRDesignExpression();
            expressionEI.setValueClass(java.math.BigDecimal.class);
            expressionEI.setText("new BigDecimal(0).setScale(3, RoundingMode.UP)");

            varSomme.setInitialValueExpression(expressionEI);


            JRDesignTextField textField = new JRDesignTextField();
            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textField.setStretchWithOverflow(true);
            textField.setFontSize(12);
            textField.setWidth(100);
            textField.setX(125);
            textField.setY(45);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setValueClass(java.math.BigDecimal.class);
            expression.setText("$V{varTotal}");
            textField.setExpression(expression);
            textField.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));


            JRDesignStaticText libelleTotalF = new JRDesignStaticText();
            libelleTotalF.setWidth(122);
            libelleTotalF.setHeight(31);
            libelleTotalF.setX(0);
            libelleTotalF.setY(45);
            libelleTotalF.setText("TOTAL GENERAL :");
            libelleTotalF.setFontSize(12);
            libelleTotalF.setForecolor(Color.BLUE);
            bandSomme.addElement(libelleTotalF);


            bandSomme.addElement(textField);
            /*************************************************************/
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
            /*************************************************************************************************/
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap parameters = new HashMap();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

            DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
            Date d = new Date();
            String dd = dateFormat15.format(d);

            String nomFichier = "Rapport Reglement " + dd + ".xls";
            OutputStream ouputStream = new FileOutputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport/") + "\\" + nomFichier));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JRXlsExporter exporterXLS = new JRXlsExporter();
            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
            exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
            exporterXLS.exportReport();

            ouputStream.write(byteArrayOutputStream.toByteArray());

            File ffile = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport") + "\\" + nomFichier);
            final String usernameEmail = this.emailSender;
            final String passwordEmail = this.passwordEmailSender;

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

//		Session session = Session.getInstance(props,
//		  new javax.mail.Authenticator() {
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(usernameEmail, passwordEmail);
//			}
//		  });
            Session session = Session.getInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.emailSender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.emailReceiver));
            message.setSubject("Rapport Du Logiciel Horizon");
            message.setText("cet email contient le rapport : " + nomFichier);
            Multipart mp = new MimeMultipart();
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setContent(nomFichier, "text/plain");
            mp.addBodyPart(mbp1);
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.setFileName(ffile.getName());
            mbp.setDataHandler(new DataHandler(new FileDataSource(ffile)));
            mp.addBodyPart(mbp);
            message.setContent(mp);
//			Transport.send(message);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", this.emailSender, this.passwordEmailSender);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            JsfUtil.addSuccessMessage("mail envoyer");







        } catch (Exception ex) {
            JsfUtil.addErrorMessage("Verifier votre connexion internet, votre login et votre password");
        }
    }

    /*****************************************************************/
    /*******}***********************************************************************/
    public String realNameCompo(String id) {
        if (id.equals("Nom")) {
            return "tiers_com_NOM_TI";
        }
        return "";
    }

    /*****************************************************************************/
       public String rechercheMultipleCriteres() {
         //  System.out.println("rechercheMultipleCriteres " +reglementzero);
          ListeReglement = new ArrayList<Operation>();
            date1.setHours(0);
            date1.setMinutes(0);
            date1.setSeconds(0);
            date2.setHours(23);
            date2.setMinutes(59);
            date2.setSeconds(59);
          //  System.out.println("date 1 = "+date1+"---- date2= "+date2);
        String requette = "Select o from Operation o where o.typeoperation.id=:nomTop and ";
        List<String> n = new ArrayList<String>();
        List<Object> o = new ArrayList<Object>();
        String order = "";
        n.add("nomTop");
         Typeoperation typeop = new Typeoperation();
            try {
                typeop = jpaTypeoperation.findByParameterSingleResult("Select u from Typeoperation u where u.libelle=:libelle", "libelle", "Reglement client");
                System.out.println("typeop " + typeop.getLibelle());
            } catch (Exception e) {
            }

        o.add(typeop.getId());
        if ((date1 != null) & (date2 != null)) {
            requette = requette + "o.dateoperation<=:date2 and o.dateoperation>=:date1 and ";
            n.add("date1");
            n.add("date2");
            o.add(date1);
            o.add(date2);
            order = order + " dateoperation";
        }
     


        if (clientListFiltrage != null) {
            if (this.clientListFiltrage.isEmpty() == false) {
                requette = requette + "o.client.nom like :nom and ";
                n.add("nom");
                o.add("%" + clientListFiltrage + "%");
                order = order + " nom";
            }
        }
         if (codeclientListFiltrage != null) {
            if (this.codeclientListFiltrage.isEmpty() == false) {
                requette = requette + "o.client.code like :code and ";
                n.add("code");
                o.add("%" + codeclientListFiltrage + "%");
                order = order + " code";
            }
        }
        
        if ((montantTtcListFiltrage != null)) {
            if (montantTtcListFiltrage.setScale(3, RoundingMode.FLOOR).compareTo(new BigDecimal(0).setScale(3, RoundingMode.FLOOR)) >= 0) {
                requette = requette + "o.montantoperation like :montantTtcOp and ";
                n.add("montantTtcOp");
                o.add("%" + montantTtcListFiltrage + "%");
                order = order + " montantTtcOp";
            }
        }
         if ((reglementzero != null)) {
            if (reglementzero==true) {
           //     System.out.println("reglement true fel requete");
                requette = requette + "o.montantoperation = '0.000' and ";
              
                order = order + " montantTtcOp";
            }
        }
        
        if (modeRegListFiltrage != null) {
            if (this.modeRegListFiltrage.isEmpty() == false) {
                requette = requette + "o.modereglement.libelle like :modereglement and ";
                n.add("modereglement");
                o.add("%" + modeRegListFiltrage + "%");
                order = order + " modereglement";
            }
        }


        if (requette.endsWith(" and ")) {
            requette = requette.substring(0, requette.lastIndexOf(" and "));
        }
        String orderByRequette = "";
        
            orderByRequette = orderByRequette + "o.dateoperation DESC,";
       
        

        if (order.contains("client")) {
            orderByRequette = orderByRequette + "o.client.nom,";
        }

        if (order.contains("montantTtcOp")) {
            orderByRequette = orderByRequette + "o.montantoperation,";
        }
        if (order.contains("nomMr")) {
            orderByRequette = orderByRequette + "o.modereglement.libelle,";
        }
        if (orderByRequette.endsWith(",")) {
            orderByRequette = orderByRequette.substring(0, orderByRequette.lastIndexOf(","));
        }
        orderByRequette = "order by " + orderByRequette;
        requette = requette + " " + orderByRequette;
        try {
            System.out.println(requette);
            List<Operation> lop = this.ejbFacade.findByParameterMultipleCreteria(requette, n, o);
            items = new ListDataModel(lop);
        } catch (Exception er) {
            er.printStackTrace();
        }
        return "";
    }
  
    public String acceePrepareCreateReglementFromList() {
       
            return acceePrepareCreateReglementClient();
        
    }

    public String acceePrepareCreateReglementClient() {
       affichetabreg=false;
       regZero=false;
       tofacture = false;
          imprimPdf=false;
         dlgOccurences = new Dialog();
        dlgOccurences.setVisible(false);
    currentModeReglement= new Modereglement();
        currentModeReglement.setId(new Long(1));
        currentModeReglement.setLibelle("ESPECE");
        currentModeReglement.setCode("1");
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Authetification userController = (Authetification) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "authetification");
            userController.refraichirEtatBouton("reglement " + "client");
        } catch (Exception rtt) {
            return "";
        }
        typeR = new String();
        typeR = "client";
        page = "Create";
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("type", typeR);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("tofacture", tofacture);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("regZero", regZero);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("imprimPdf", imprimPdf);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("currentModeReglement", currentModeReglement);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("mantantTotalAncienSolde", mantantTotalAncienSolde);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("LibelleOccurence",LibelleOccurence);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("dlgOccurences", dlgOccurences);     
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ParamTableJava", ParamTableJava);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ParamTableSql", ParamTableSql);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ParamTableLibelle", ParamTableLibelle);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("ParamTableInterface", ParamTableInterface);
        
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextBanque", inputTextBanque);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextEcheance", inputTextEcheance);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextEmetteur", inputTextEmetteur);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextRecu", inputTextRecu);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("inputTextReference", inputTextReference);
        
        return "reglement_create";
    }
 public String acceePrepareCreateReglementClient5param(Client client,BigDecimal montant,
         Long numFact,String niop, String typeOper) {
       imprimPdf=true;
     tofacture=true;
  typoop=typeOper;
    String Niop=niop;
        affichetabreg=true;  
       reglementdirect=true;
      currentModeReglement= new Modereglement();
        currentModeReglement.setId(new Long(1));
        currentModeReglement.setLibelle("ESPECE");
        currentModeReglement.setCode("1");
   
     

     try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Authetification userController = (Authetification) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "authetification");
            userController.refraichirEtatBouton("reglement " + "client");
        } catch (Exception rtt) {
            return "";
        }
        typeR = new String();
        typeR = "client";
        page = "Create";
        
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("client", client);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("numFact", numFact);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("montant", montant);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("type", typeR);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("affichetabreg", affichetabreg);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("reglementdirect", reglementdirect);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("regZero", regZero);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("montantfactdirect", montantfactdirect);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("tofacture", tofacture);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("niOp", Niop);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("currentModeReglement", currentModeReglement);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("mantantTotalAncienSolde", mantantTotalAncienSolde);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("typoop", typoop);
        
        return "reglement_create";
    }
 
 
 public String acceePrepareCreateReglementClient3param(Client client,BigDecimal montant,
         Long numFact) {
       imprimPdf=true;
     tofacture=true;
        affichetabreg=true;  
        reglementdirect=true;
       
     try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Authetification userController = (Authetification) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "authetification");
            userController.refraichirEtatBouton("reglement " + "client");
        } catch (Exception rtt) {
            return "";
        }
        typeR = new String();
        typeR = "client";
        page = "Create";
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("client", client);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("numFact", numFact);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("montant", montant);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("type", typeR);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("affichetabreg", affichetabreg);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("reglementdirect", reglementdirect);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("montantfactdirect", montantfactdirect);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("tofacture", tofacture);
  
      return "reglement_create";
    }
   
     public void initprepareRapportReglement() {

          LinkedList<String> Columns = new LinkedList<String>();
       
        Columns.add("TRAITE");
        Columns.add("VIREMENT");
        Columns.add("CODE");
        Columns.add("BANQUE");
        Columns.add("PT VENTE");
        Columns.add("CAISSIER");
        Columns.add("REFERENCE");
        Columns.add("ECHEANCE");
         Columns.add("LETTRAGE"); 
        List<String> Target = new ArrayList<String>();
        Target.add("RAISON SOCIALE");
        Target.add("DATE"); 
        Target.add("ESPECE");
        Target.add("CHEQUE");
         Target.add("LIBELLE OPERATION");
       
        colonneschoisies = new DualListModel<String>(Columns, Target);
        flagNomClientF = false;
        flagLibelleOperation = false;
        flagCodeClientF = false;
        flagMontantCheque = false;
        flagMontantEspece = false;
        flagMontantTraite = false;
        flagMontantVirement = false;
        flagDate = false;
        flagBanque = false;
        flagRefCheque = false;
        flagecheanceCheque = false;
        flagPointVente = false;
        flagCaissier = false;
        dateDebut = new Date();
        dateFin = new Date();
        filtreTiersEtat = new Client();
     
        
        typeR = "client";
       
        optionOrient = "1";
   
    }
    public void initprepareRapportReglementNonsolde() {

          LinkedList<String> Columns = new LinkedList<String>();
      
        Columns.add("TRAITE");
        Columns.add("VIREMENT");
        Columns.add("CODE");
        Columns.add("BANQUE");
        Columns.add("PT VENTE");
        Columns.add("CAISSIER");
        Columns.add("REFERENCE");
        Columns.add("ECHEANCE");
        List<String> Target = new ArrayList<String>();
        Target.add("RAISON SOCIALE");
        Target.add("DATE");  
        Target.add("ESPECE");
        Target.add("CHEQUE");
        Target.add("LIBELLE OPERATION");
        
        colonneschoisies = new DualListModel<String>(Columns, Target);
        flagNomClientF = false;
        flagCodeClientF = false;
        flagMontantCheque = false;
        flagMontantEspece = false;
        flagMontantTraite = false;
        flagMontantVirement = false;
        flagDate = false;
        flagBanque = false;
        flagRefCheque = false;
        flagecheanceCheque = false;
        flagPointVente = false;
        flagCaissier = false;
        dateDebut = new Date();
        dateFin = new Date();
        filtreTiersEtat = new Client();
      
        typeR = "client";
        optionOrient = "1";
   
    }
    
    public void initprepareCreateReglement() {
     flag_reste=false;
        try {


//            StringTokenizer getUrl = new StringTokenizer(this.ejbFacade.urlCourante(), "**");
//            String url = getUrl.nextToken();
//            String login = getUrl.nextToken();
//            String password = getUrl.nextToken();
//            Class.forName("com.mysql.jdbc.Driver").newInstance();
//            connection = DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
        }
        tableOperationM = new ArrayList<TableOperationM>();
        inputTextReference = new InputText();
     //   inputTextRecu = new InputText();
        inputTextEmetteur = new InputText();
        inputTextBanque = new AutoComplete();
        inputTextEcheance = new Calendar();
        selectedTableOperationM = new TableOperationM();      
        inputTextReference.setDisabled(true);
       inputTextRecu=true;
        inputTextEmetteur.setDisabled(true);
        inputTextBanque.setDisabled(true);
        inputTextEcheance.setDisabled(true);
        currentBanque = new Banque();
        currentBanque.setLibelle("");
        dateRegAutoDebut = new Date();
        dateRegAutoFin = new Date();
        currentTiers = new Client();   
        listOperation = new LinkedList<Operation>();
        this.selectNumPiece = new ArrayList<SelectItem[]>();
        ResteR = new BigDecimal(0).setScale(3, RoundingMode.UP);
        current = new Operation();
//        this.current.setImpaye("Non");
        this.current.setMontantoperation(new BigDecimal(0).setScale(3, RoundingMode.UP));
        this.current.setClient(new Client());
        this.current.setModereglement(new Modereglement());
        this.current.setDateSys(new Date());
          this.current.setDateoperation(new Date());
        this.current.setDateechenace(new Date());
        this.current.setTypeoperation(getLikeTypeOPComBylibelleReglement(typeR));
    //    this.current.setSociete(authentificationBean.getSociete());
        DataTableLettrage = new DataTable();
        dataTabledetailLettrage = new DataTable();
        indexDataTableLettrage = DataTableLettrage.getRowIndex();
        this.lettrage = new ArrayList<LettrageCom>();
        accompte = new ArrayList<BigDecimal>();
        DataTableLettrage.setRendered(false);
         if (typeR.equals("client")) {
            mesg = "Règlement Client";
            mesgs = "Règlements Clients";
            nomCF = "client";
        } 
  if(cl !=null)
       {
        this.setCurrentTiers(cl);     
        this.current.setMontantoperation(mon);
  
       }
       
 
   if(affichetabreg==true)
   {
   
       changeMantant2();
   }
    }

    /**************************prepareListReglement*******************************************/
    public String acceePrepareListReglementFromCreate() {
        
        
            return acceePrepareListReglementClient();
        

    }

    public String acceePrepareListReglementClientImpression() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Authetification userController = (Authetification) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "authetification");
            userController.refraichirEtatBouton("reglement " + "client");
        } catch (Exception rtt) {
            return "";
        }
        typeR = new String();
        typeR = "client";
        page = "List";
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("type", typeR);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("reglementzero", reglementzero);
        
        return "reglement_list_impression";
    }
    
    public String acceePrepareListReglementClient() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            Authetification userController = (Authetification) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "authetification");
            userController.refraichirEtatBouton("reglement " + "client");
        } catch (Exception rtt) {
            return "";
        }
        typeR = new String();
        typeR = "client";
        page = "List";
       nompage       ="Reglement client";
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
         FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("nompage", nompage);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("type", typeR);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("reglementzero", reglementzero);
        
        return "reglement_list";
    }

  
    public void initprepareListReglement() {
         
        dataTablelisteReglement = new DataTable();
        date1 = new Date();
        date2 = new Date();
        ListeReglement = new ArrayList<Operation>();
    
        items = new ListDataModel(ListeReglement);
         if (typeR.equals("client")) {
            mesg = "Règlement Client";
            mesgs = "Règlements Clients";
            nomCF = "client";
        } 
         reglementzero=false;
        chercherListeReg();
     
    }

    /**************************prepareEditReglement*******************************************/
    public String prepareEditReglement() {



        current = (Operation) getItems().getRowData();
        page = "Edit";
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("type", typeR);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("current", current);

        return "reglement_edit";
    }

    public void initprepareEditReglement() {
        changeMontantReg=false;
        
         try {
            StringTokenizer getUrl = new StringTokenizer(this.ejbFacade.urlCourante(), "**");
            String url = getUrl.nextToken();
            String login = getUrl.nextToken();
            String password = getUrl.nextToken();
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(url, login, password);
        } catch (Exception e) {
        }
        
        currentModeReglement = current.getModereglement();
        inputTextReference = new InputText();
//        inputTextRecu = new InputText();
        inputTextEmetteur = new InputText();
        inputTextBanque = new AutoComplete();
        inputTextEcheance = new Calendar();

        if ((currentModeReglement.getLibelle().equalsIgnoreCase("cheque")) || (currentModeReglement.getLibelle().equalsIgnoreCase("traite"))) {
            inputTextReference.setDisabled(false);
            inputTextRecu=false;
            inputTextEmetteur.setDisabled(false);
            inputTextBanque.setDisabled(false);
            inputTextEcheance.setDisabled(false);
            System.out.println("inputTextReference "+inputTextReference);
            System.out.println("inputTextBanque "+inputTextBanque);
        } else {
            inputTextReference.setDisabled(true);
            inputTextRecu=true;
            inputTextEmetteur.setDisabled(true);
            inputTextBanque.setDisabled(true);
            inputTextEcheance.setDisabled(true);
        }


      //  System.out.println("prepareEditReglement()");

        currentTiers = current.getClient();
//        current.setImpaye("Non");
        selectNumPiece = new ArrayList<SelectItem[]>();
        ListeLettrageAM = new ArrayList<LettrageCom>();
        tableOperationM = new ArrayList<TableOperationM>();
        selectedTableOperationM = new TableOperationM();
        DataTableLettrage = new DataTable();
        dataTabledetailLettrage = new DataTable();
        indexDataTableLettrage = DataTableLettrage.getRowIndex();
        this.lettrage = new ArrayList<LettrageCom>();
        accompte = new ArrayList<BigDecimal>();

        String qL = "Select c from LettrageCom c where c.niOp.id=:n";
        List<LettrageCom> listLettrage = (List<LettrageCom>) this.jpaLettrageCom.execCommandeList(qL, "n", current.getId());
     ListeLettrageAM = listLettrage;
        Iterator it = listLettrage.iterator();
        while (it.hasNext()) {
            LettrageCom pp = (LettrageCom) it.next();

            String q = "Select c from LettrageCom c where c.niPiece=:n";
            List<LettrageCom> listLettrage2 = (List<LettrageCom>) this.jpaLettrageCom.execCommandeList(q, "n", pp.getNiPiece());

            Iterator it1 = listLettrage2.iterator();

            BigDecimal bg = new BigDecimal(0);
            while (it1.hasNext()) {
                LettrageCom pp2 = (LettrageCom) it1.next();
                if (pp.getNiPiece().equals(pp2.getNiPiece())) {
                    bg = bg.add(pp2.getMontantReg());
                }
            }


            System.out.println(bg + "-" + pp.getNiLet());

            List<Operation> apF1 = new ArrayList<Operation>();
            apF1.add(pp.getNiPiece());
            this.selectNumPiece.add(JsfUtil.getSelectItems(apF1, true));

            BigDecimal acc = new BigDecimal(0).setScale(3, RoundingMode.UP);
            acc = bg.subtract(pp.getMontantReg());
            accompte.add(bg.subtract(pp.getMontantReg()));
            pp.setSoldePiece(pp.getMontantReg());

            pp.setMontantReg(pp.getMontantPiece().subtract(acc));

            lettrage.add(pp);
        }

        int i = 0;
        while (i < 5) {
            accompte.add(null);
            LettrageCom e = new LettrageCom();
            this.listOperation = new ArrayList<Operation>();

            this.selectNumPiece.add(JsfUtil.getSelectItems(this.listOperation, true));
          
            e.setNiPiece(new Operation());
            e.setTypeOPL(new Typeoperation());
            lettrage.add(e);
            i++;
        }
        Iterator it1 = listLettrage.iterator();
        BigDecimal aff = new BigDecimal(0).setScale(3, RoundingMode.UP);
        while (it1.hasNext()) {
            LettrageCom pp = (LettrageCom) it1.next();
            if (pp.getSoldePiece() == null) {
            } else {
                aff = aff.add(pp.getSoldePiece().setScale(3, RoundingMode.UP));
            }
            System.out.println(pp.getSoldePiece());
            i++;
        }
        ResteR = current.getMontantoperation().subtract(aff);
         if (typeR.equals("client")) {
            mesg = "Règlement Client";
            mesgs = "Règlements Clients";
            nomCF = "client";
        } 
    }
    public void changeRefChequeTraiteVirement()
    {
        System.out.println("fct changeRefChequeTraiteVirement");
        System.out.println("typeR :"+typeR);
        if(typeR.equalsIgnoreCase("client"))
        {
      boolean trouveRef=false;
    String fournisseurclient="";
      String q = "Select c from Operation c where c.typeoperation=:type and c.modereglement =:niMr  and c.client=:client and c.banque=:nibanque";
      if(typeR.equalsIgnoreCase("client"))
              {
          fournisseurclient="client";        
              }
      
      
      List<Operation> ListeReglementRef = (List<Operation>) this.ejbFacade.execCommandeList4Param(q, "type", getLikeTypeOPComBylibelleReglement("client"),"client",currentTiers,"niMr",currentModeReglement,"nibanque",currentBanque);
      Iterator it1 = ListeReglementRef.iterator();
              while (it1.hasNext()) {
            Operation pp = (Operation) it1.next();
          try{ if (current.getReference().equals(pp.getReference())) {trouveRef=true;}}catch(Exception t){}
    
         }
               if(trouveRef==true){current.setReference("");JsfUtil.addErrorMessage("Clé dupliquée");};
         
        }
        else
        {
          
        }
    }
    public void changeRefChequeTraiteVirementModif()
    {
     
        boolean trouveRef=false;
    
      String q = "Select c from Operation c where c.typeoperation=:type and c.modereglement =:niMr  and c.client=:client and c.banque=:nibanque";
      List<Operation> ListeReglementRef = (List<Operation>) getFacade().execCommandeList4Param(q, "type", getLikeTypeOPComBylibelleReglement("reglement fournisseur"),"client",currentTiers,"niMr",current.getModereglement(),"nibanque",current.getBanque());
      Iterator it1 = ListeReglementRef.iterator();
              while (it1.hasNext()) {
            Operation pp = (Operation) it1.next();
          try{ if ((current.getReference().equals(pp.getReference()))&&(current.getId().equals(pp.getId())==false))
            {
                 trouveRef=true;
            }
           
           }catch(Exception t){t.printStackTrace();}
    
         }
               if(trouveRef==true){current.setReference("");JsfUtil.addErrorMessage("Clé dupliquée");};
    }
    public void changeEcheancetest()
    {
        System.out.println("DATE ECHE"+current.getDateechenace());
    }
   
    public String changeEcheance()
    {
     Date EchanceOp =current.getDateechenace();
     Date DateOp =current.getDateoperation();
    
     EchanceOp.setHours(0);EchanceOp.setMinutes(0);EchanceOp.setSeconds(0);
     DateOp.setHours(0);DateOp.setMinutes(0);DateOp.setSeconds(0);
     boolean pm=false;
  
     if (EchanceOp.toString().equals(DateOp.toString())){pm=true;}
    if ((EchanceOp.compareTo(DateOp)==-1) &&(pm==false))
       {
        current.setDateechenace(current.getDateoperation());
           JsfUtil.addErrorMessage("date erronee");
       }
    return null;
    }
    
    public void boutonbl()
    {
    }
    
     public String changeEcheanceDate()
    {
      current.setDateechenace(current.getDateoperation());
      return null;
    }
    
             public String updatelargeurpage() {
         String c1 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("larg");
     sizePage=new BigDecimal(c1).setScale(0, RoundingMode.UP);
     sizePage=sizePage.subtract(new BigDecimal(28));
           String c2 = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("zoom");
     zoom=new BigDecimal(c2).setScale(3, RoundingMode.UP);
      
       return null;
    }
             
             public void changeDate()
             {
       
             }
 
                public void PdfImmediat() {
        if (imprimPdf!=null)
        {if (imprimPdf==true)
        {
        try {
        
             FacesContext fc = FacesContext.getCurrentInstance();                    
           
     
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        }
        }
    }
                

                   public boolean contiensOp(List<LettrageCom> l,Operation Op)
                   {boolean test =false;
                     Iterator it = l.iterator();
            while (it.hasNext()) {
                LettrageCom pp = (LettrageCom) it.next();
                if (pp.getNiPiece()!=null)
                { if(pp.getNiPiece().equals(Op))
                {return true;}
                }
                }
            
                  return test; }
                   
                   public void createOccurence() {
     
        Long max = null;
        int maxi=0;
        Boolean bool;
          if (ParamTableSql.equals("tireur_com")||ParamTableSql.equals("convoyeur_com")||ParamTableSql.equals("chauffeur_com")) {
          }
          else
          {
              try {
        } catch (Exception e) {
            max = Long.valueOf(1);
     
        } 
          }
        StringTokenizer getUrl = new StringTokenizer(this.ejbFacade.urlCourante(), "**");
        String url = getUrl.nextToken();
        String login = getUrl.nextToken();
        String password = getUrl.nextToken();
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = (Connection) DriverManager.getConnection(url, login, password);
            Statement statement = (Statement) connection.createStatement();
            String q = "";
            String q1 = "";
            bool = true;
            try {
                q1 = "Select UPPER(" + ParamTableLibelle + ") from  " + authentificationBean.baseDonneesChoisi + "." + ParamTableSql + " where  " + ParamTableLibelle + "='" + LibelleOccurence.toUpperCase() + "'";
                ResultSet rs = statement.executeQuery(q1);
                if (rs != null) {
                    if (rs.next()) {

                        if (rs.getString(1).equals(LibelleOccurence.toUpperCase())) {
                            bool = false;

                        }
                    } else {
                    }
                    rs.close();
                }
            } catch (Exception e) {
          }

            if (bool) {
                if (ParamTableJava.equals("VilleCom")) {
                    q = "insert into " + this.authentificationBean.baseDonneesChoisi + "." + ParamTableSql + " values(" + max + ",null,'" + max + "','" + LibelleOccurence + "')";
                } 

                else {
                     if (ParamTableSql.equals("tireur_com")||ParamTableSql.equals("convoyeur_com")||ParamTableSql.equals("chauffeur_com")) {
         q = "insert into " + this.authentificationBean.baseDonneesChoisi + "." + ParamTableSql + " values(" + maxi + ",'" + maxi + "','" + LibelleOccurence + "')";
    }
                     else{
                    q = "insert into " + this.authentificationBean.baseDonneesChoisi + "." + ParamTableSql + " values(" + max + ",'" + max + "','" + LibelleOccurence + "')";
                     }
                }

                int o2 = statement.executeUpdate(q);

            } else {
           }
            statement.close();
        } catch (Exception e) {
    
        }
        ParamTableInterface = "";
        ParamTableJava = "";
        ParamTableSql = "";
        LibelleOccurence = "";
    }
                             public String getLettrageLigne()
                   {Operation op = (Operation) items.getRowData();
                   try {
            String filtrageClFr = "";
          
                filtrageClFr = "facture";
            
            List<LettrageCom> l = jpaLettrageCom.findByParameter("Select l from LettrageCom l where l.niOp.id = " + op.getId() + " and l.niPiece.typeoperation.libelle=:nomTop", "nomTop", filtrageClFr);
            String s = "";
            Iterator i = l.iterator();
            while (i.hasNext()) {
                LettrageCom lt = (LettrageCom) i.next();
                String numFact = "";
                try {
                    numFact = lt.getNiPiece().getNumerooperation().toString();
                } catch (Exception r) {
         //           r.printStackTrace();
                }
               
                s = s + numFact + " / ";
            }
              if (s.endsWith(" / ")) {
                s = s.substring(0, s.lastIndexOf(" / "));
            }
            return s;
        } catch (Exception er) {
       //     er.printStackTrace();

        }
        return "";
    }
                          public void impressionPdfReglement() throws JRException, IOException {
        try {
            current = (Operation) getItems().getRowData();
          
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
           
              query.setText("SELECT cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal, operation.`id` AS operation_id, operation.`modereglement` AS operation_modereglement, operation.`libelleoperation` AS operation_libelleoperation, operation.`banque` AS operation_banque, operation.`typeoperation` AS operation_typeoperation, operation.`client` AS operation_client, operation.`numerooperation` AS operation_numerooperation, operation.`dateoperation` AS operation_dateoperation, operation.`montantoperation` AS operation_montantoperation, operation.`montantdebite` AS operation_montantdebite, operation.`montantcredite` AS operation_montantcredite, operation.`montantcheque` AS operation_montantcheque, operation.`montantespece` AS operation_montantespece, operation.`montanttraite` AS operation_montanttraite, operation.`montantvirement` AS operation_montantvirement, operation.`date_sys` AS operation_date_sys, operation.`user` AS operation_user, operation.`libelleoperation` AS operation_libelleoperation, operation.`reference` AS operation_reference, operation.`recu` AS operation_recu, operation.`emetteur` AS operation_emetteur, operation.`dateechenace` AS operation_dateechenace, operation.`detail` AS operation_detail, operation.`dateconsultation` AS operation_dateconsultation, operation.`motifconsultation` AS operation_motifconsultation, client.`nom` AS client_nom, client.`datecreation` AS client_datecreation, client.`code` AS client_code, modereglement.`id` AS modereglement_id, modereglement.`code` AS modereglement_code, modereglement.`libelle` AS modereglement_libelle FROM `operation` operation LEFT JOIN `client` client ON client.`id` = operation.`client` LEFT JOIN `modereglement` modereglement ON operation.`modereglement` = modereglement.`id`, `cabinet` cabinet  WHERE operation.`id`="+ current.getId()+"");
          
         
            jasperDesign.setQuery(query);
            jasperDesign.setName("Rapport Reglement Clients");

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
            libelleTitre.setText("Reglement Client");
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
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
               
             
                             
   public String montantTtcTouteLettres(BigDecimal ttc) {
        String mont = "";
        RuleBasedNumberFormat rbnf = new RuleBasedNumberFormat(Locale.FRENCH, RuleBasedNumberFormat.SPELLOUT);
        StringTokenizer st = new StringTokenizer(ttc.setScale(3, RoundingMode.HALF_UP).toString(), ".");
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
                   
 /*******************************************************************/                            
   /*******************************************************************/                            
   public void chercherListNumPiece() {
     
         List<Operation>  listOperation=new ArrayList<Operation>();
      try{ 
     //    String q = "Select c from Operation c where c.typeoperation=:type  and c.client=:client and c.numerooperation like '%"+filtreNumPiece+"%' and (c.montantoperation!=0.000 and ((Select SUM(l1.montantReg) from LettrageCom l1 where l1.niPiece=c) is null or c.montantoperation > (Select SUM(l.montantReg) from LettrageCom l where l.niPiece=c)))";
        String q = "Select c from Operation c where c.typeoperation=:type  and c.client=:client  and (c.montantoperation!=0.000 and ((Select SUM(l1.montantReg) from LettrageCom l1 where l1.niPiece=c) is null or c.montantoperation > (Select SUM(l.montantReg) from LettrageCom l where l.niPiece=c)))";
        
          q = q + " order by  c.dateoperation DESC ";
              System.out.println("q:"+q);
         
try
{
              listOperation = (List<Operation>) this.ejbFacade.execCommandeList2Param(q, "type", lettrage.get(index).getTypeOPAL(), "client", currentTiers);
}
catch(Exception e)
{
    
}
              System.out.println("size +++!"+listOperation.size());}catch(Exception e){e.printStackTrace();}
      
        
        List<Operation> apF = new ArrayList<Operation>();
        tableOperationM = new ArrayList<TableOperationM>();

        Iterator it4 = listOperation.iterator();
     
     try{   while (it4.hasNext()) {
   
            Operation op = (Operation) it4.next();
            if (contiensOp(lettrage, op))
            {}
            else{
            BigDecimal mantantRegAFF = new BigDecimal(0);
            boolean mp = false;
           
                      String qSum="Select SUM(l.montantReg) from LettrageCom l where l.niPiece=:n";
            mantantRegAFF=this.jpaLettrageCom.findByParameterSum(qSum, "n", op);
                 if (mantantRegAFF==null)
            {mantantRegAFF = new BigDecimal(0);}
            if ((mp == false)) {
                apF.add(op);
        //        System.out.println("false");
                TableOperationM e = new TableOperationM();

                e.setOperation(op);
                e.setAcompte(mantantRegAFF.setScale(3, RoundingMode.UP));
                e.setMontantReglement(op.getMontantoperation().subtract(mantantRegAFF));
                tableOperationM.add(e);


            }
       //     System.out.println("mp" + mp);
            }
        }
     }catch(Exception e){e.printStackTrace();}
        this.selectNumPiece.add(index, JsfUtil.getSelectItems(apF, true));
        /*******************************************/
        //lettrage.get(index).setAccompte(null); 
        System.out.println("mon index "+index);
        accompte.set(index, null);
        lettrage.get(index).setMontantPiece(null);
        lettrage.get(index).setMontantReg(null);
        lettrage.get(index).setSoldePiece(null);
        BigDecimal aff = new BigDecimal(0).setScale(3, RoundingMode.UP);
        Iterator it = lettrage.iterator();
        int i = 0;
        while (it.hasNext()) {
            LettrageCom pp = (LettrageCom) it.next();
            if (pp.getSoldePiece() == null) {
            } else {
                aff = aff.add(pp.getSoldePiece().setScale(3, RoundingMode.UP));
            }
            i++;
        }
        ResteR = current.getMontantoperation().subtract(aff);

    }                         
/////////////////////////////////////////Visite Non Regle///////////////////////////   
                  public void rapportVisiteNonregle() throws JRException, IOException {
        try {
           
  
          int  pageWidth = 595;
            optionOrient = "1";
            Xligne=0;
            JRDesignFrame jrFame = new JRDesignFrame();
            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy ");
            DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d22 = new Date();
            String ddd = dateFormat3.format(d22);
            JasperDesign jasperDesign = new JasperDesign();


            JRDesignBand bandTiltle = new JRDesignBand();

            JRDesignStaticText libelleDateSys = new JRDesignStaticText();
            libelleDateSys.setWidth(150);
            libelleDateSys.setHeight(20);
            libelleDateSys.setText("DATE : " + ddd);
            libelleDateSys.setFontSize(10);
            libelleDateSys.setForecolor(Color.BLACK);
            libelleDateSys.setX(440);
            libelleDateSys.setY(10);
            jrFame.addElement(libelleDateSys);

            JRDesignBand bandBlanc = new JRDesignBand();
            bandBlanc.setHeight(200);
            JRDesignStaticText libellePgeB = new JRDesignStaticText();
            libellePgeB.setWidth(50);
            libellePgeB.setHeight(20);
            libellePgeB.setX(0);
            libellePgeB.setY(30);
            libellePgeB.setText("Tel :");
            libellePgeB.setFontSize(12);
            libellePgeB.setForecolor(Color.BLACK);
            bandBlanc.addElement(libellePgeB);
            jasperDesign.setNoData(bandBlanc);

          
            JRDesignBand bandSomme = new JRDesignBand();
            bandSomme.setHeight(200);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            dateDebut.setHours(0);
            dateDebut.setMinutes(0);
            dateDebut.setSeconds(0);
            dateFin.setHours(23);
            dateFin.setMinutes(59);
            dateFin.setSeconds(59);
            String d1 = dateFormat.format(dateDebut);
            String d2 = dateFormat.format(dateFin);
            DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            String d111 = dateFormat1.format(dateDebut);
            String d222 = dateFormat1.format(dateFin);
            DateFormat dateFormat5 = new SimpleDateFormat("yy-MM-dd");
            String f2=dateFormat5.format(dateFin);
             String f1=dateFormat5.format(dateDebut);
            System.out.println("d1" + d1);
            
            etablirconnection();
            JRDesignQuery query = new JRDesignQuery();
           
              String q1 = "";

            if (filtreTiersEtat != null) {
                q1 = " and operation.`client` =" + filtreTiersEtat.getId();
            }
            
             
          //   String typeOperationExtrait=" and ( ( typeoperation.`libelle`='Depense Fournisseur' ) or ( typeoperation.`libelle`='Depense Divers' ) or ( typeoperation.`libelle`='Reglement client' ) )";
query.setText("Select cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal, operation.`dateoperation` AS operation_date_operation,operation.`montantoperation` AS operation_montantoperation, lettrage_com.`NI_LET` AS lettrage_com_NI_LET, lettrage_com.`NI_OP` AS lettrage_com_NI_OP, lettrage_com.`NI_PIECE` AS lettrage_com_NI_PIECE, lettrage_com.`NUM_PIECE` AS lettrage_com_NUM_PIECE, lettrage_com.`MONTANT_PIECE` AS lettrage_com_MONTANT_PIECE, lettrage_com.`MONTANT_REG` AS lettrage_com_MONTANT_REG, lettrage_com.`SOLDE_PIECE` AS lettrage_com_SOLDE_PIECE, lettrage_com.`Type_OP_L` AS lettrage_com_Type_OP_L, lettrage_com.`Type_OP_AL` AS lettrage_com_Type_OP_AL ,operation.`id` AS operation_id, operation.client, sum(MONTANT_REG) AS somme_paye, (lettrage_com.`MONTANT_PIECE`-sum(MONTANT_REG)) AS somme_restante, client.`nom` AS client_nom, client.`datecreation` AS client_datecreation, client.`code` AS client_code, DATE_FORMAT(operation.DATE_SYS,'%d/%m/%Y %H:%i:%s') as DATE_SYS, operation.`numerooperation` AS operation_numerooperation"
        + " from `cabinet` cabinet, operation operation LEFT OUTER JOIN lettrage_com lettrage_com ON operation.id=lettrage_com.NI_PIECE LEFT OUTER JOIN client client ON operation.client=client.id where operation.typeoperation=6 " +q1+ " and DATE_FORMAT(operation.`dateoperation`,'%y-%m-%d') >= '" + f1 + "' and  DATE_FORMAT(operation.`dateoperation`,'%y-%m-%d')  <= '" + f2 + "'  and ((operation.montantoperation)-(SELECT IFNULL((Select sum(ll28.MONTANT_REG) from lettrage_com ll28 where ni_piece=operation.id),0))) > 0.000 group by(operation.id) order by operation.`dateoperation`, operation.id ");
//             + " where   DATE_FORMAT(DATE_SYS,'%y-%m-%d') >= '" + f1 + "' and  DATE_FORMAT(DATE_SYS,'%y-%m-%d')  <= '" + f2 + "' "+typeOperationExtrait+"    ORDER BY operation.`client`,DATE_SYS");

            
            System.out.println("Query" + query.getText());
            jasperDesign.setQuery(query);
            jasperDesign.setName("ExtraitVisiteNonReglé");

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

            jrFame.setHeight(92);
            jrFame.setWidth(842);
            jrFame.getLineBox().setLeftPadding(0);
            jrFame.getLineBox().setTopPadding(0);
            jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

            JRDesignStaticText libelleTitre = new JRDesignStaticText();
            libelleTitre.setWidth(360);
            libelleTitre.setHeight(25);
            libelleTitre.setX(200);
            libelleTitre.setY(30);
            libelleTitre.setText("Visites Non Payées");
            libelleTitre.setFontSize(16);
            libelleTitre.setForecolor(Color.BLUE);
            jrFame.addElement(libelleTitre);




            JRDesignStaticText libelleDate = new JRDesignStaticText();
            libelleDate.setWidth(250);
            libelleDate.setHeight(15);
            libelleDate.setText(" PERIODE : Du  " + d111 + "  Au  " + d222);
            libelleDate.setFontSize(10);
            libelleDate.setForecolor(Color.BLACK);
            libelleDate.setX(200);
            libelleDate.setY(55);
            jrFame.addElement(libelleDate);
            bandTiltle.setHeight(92);


            



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

            /*******************************************************/
            bandTiltle.addElement(jrFame);
            jasperDesign.setTitle(bandTiltle);
            /*******************************************************/



             JRDesignBand bandHeader = new JRDesignBand();
                BigDecimal bandHeaderHeight = new BigDecimal(15).setScale(0, RoundingMode.DOWN);
                bandHeader.setHeight(bandHeaderHeight.intValue());
                JRDesignBand columnHeader = new JRDesignBand();
                columnHeader.setHeight(15);
            
            {
                JRDesignStaticText libelleColonneDate = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_date_operation", "$F{operation_date_operation}", libelleColonneDate, "DATE", 0, 60, bandSomme);
         //    $F{operation_date_operation}
                
                columnHeader.addElement(libelleColonneDate);
            }

   {
                    JRDesignStaticText libelleColonneClient = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "client_nom", "(($F{client_nom}!=null)&($F{client_nom}.equals(\"\")==false))?$F{client_nom}:Character.toString(' ')", libelleColonneClient, "CLIENT", 0, 120, bandSomme);
                    libelleColonneClient.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneClient);
                }
            {
                JRDesignStaticText libelleColonneMontantVisite = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_montantoperation", "(($F{operation_montantoperation}!=null)&($F{operation_montantoperation}.equals(\"0.000\")==false))?$F{operation_montantoperation}:Character.toString(' ')", libelleColonneMontantVisite, "FACTURE", 70, 60, bandSomme);
                columnHeader.addElement(libelleColonneMontantVisite);
            }

              {
                JRDesignStaticText libelleColonneMontantPaye = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "somme_paye", "(($F{somme_paye}!=null)&($F{somme_paye}.equals(\"0.000\")==false))?$F{somme_paye}:Character.toString(' ')", libelleColonneMontantPaye, "PAYE", 140, 60, bandSomme);
                columnHeader.addElement(libelleColonneMontantPaye);
            }

  
   {
                JRDesignStaticText libelleColonneMontantRestant= new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "somme_restante", "(($F{somme_restante}!=null)&($F{somme_restante}.equals(\"0.000\")==false))?$F{somme_restante}:Character.toString(' ')", libelleColonneMontantRestant, "RESTANT", 140, 60, bandSomme);
                columnHeader.addElement(libelleColonneMontantRestant);
            }
//            {
//                JRDesignStaticText libelleColonneMontantPaye = new JRDesignStaticText();
//                styleLibelleColumnHeader(jasperDesign, bandHeader, "lettrage_com_MONTANT_REG", "(($F{lettrage_com_MONTANT_REG}!=null)&($F{lettrage_com_MONTANT_REG}.equals(\"0.000\")==false))?$F{lettrage_com_MONTANT_REG}:Character.toString(' ')", libelleColonneMontantPaye, "PAYE", 140, 60, bandSomme);
//                columnHeader.addElement(libelleColonneMontantPaye);
//            }
//
//  
//   {
//                JRDesignStaticText libelleColonneMontantRestant= new JRDesignStaticText();
//                styleLibelleColumnHeader(jasperDesign, bandHeader, "lettrage_com_SOLDE_PIECE", "(($F{lettrage_com_SOLDE_PIECE}!=null)&($F{lettrage_com_SOLDE_PIECE}.equals(\"0.000\")==false))?$F{lettrage_com_SOLDE_PIECE}:Character.toString(' ')", libelleColonneMontantRestant, "RESTANT", 140, 60, bandSomme);
//                columnHeader.addElement(libelleColonneMontantRestant);
//            }
//somme_restante   
  
            jasperDesign.setColumnHeader(columnHeader);
            //             jasperDesign.setDetail(bandHeader);
            ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandHeader);
            jasperDesign.setSummary(bandSomme);

            /***********************************Somme Total***************************************/
            
          
            /*************************************************************************************************/
            jasperDesign.setNoData(bandSomme);

            jasperDesign.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap parameters = new HashMap();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

            DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
            Date d = new Date();
            String dd = dateFormat15.format(d);
            String nomFichier = "Rapport_Visite_Non_Payé" + dd.replace(" ", "_");
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
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /////////////////////Extrait Compte Patient////////////////////////////
            public void rapportExtraitPatient() throws JRException, IOException {
        try {
           
    //        AncienSolde();
      //      nonEchus();
          int  pageWidth = 595;
            optionOrient = "1";
            Xligne=0;
            JRDesignFrame jrFame = new JRDesignFrame();
            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy ");
            DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d22 = new Date();
            String ddd = dateFormat3.format(d22);
            JasperDesign jasperDesign = new JasperDesign();


            JRDesignBand bandTiltle = new JRDesignBand();

            JRDesignStaticText libelleDateSys = new JRDesignStaticText();
            libelleDateSys.setWidth(150);
            libelleDateSys.setHeight(20);
            libelleDateSys.setText("DATE : " + ddd);
            libelleDateSys.setFontSize(10);
            libelleDateSys.setForecolor(Color.BLACK);
            libelleDateSys.setX(440);
            libelleDateSys.setY(10);
            jrFame.addElement(libelleDateSys);

            JRDesignBand bandBlanc = new JRDesignBand();
            bandBlanc.setHeight(200);
            JRDesignStaticText libellePgeB = new JRDesignStaticText();
            libellePgeB.setWidth(50);
            libellePgeB.setHeight(20);
            libellePgeB.setX(0);
            libellePgeB.setY(30);
            libellePgeB.setText("Tel :");
            libellePgeB.setFontSize(12);
            libellePgeB.setForecolor(Color.BLACK);
            bandBlanc.addElement(libellePgeB);
            jasperDesign.setNoData(bandBlanc);

          
            JRDesignBand bandSomme = new JRDesignBand();
            bandSomme.setHeight(200);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            dateDebut.setHours(0);
            dateDebut.setMinutes(0);
            dateDebut.setSeconds(0);
            dateFin.setHours(23);
            dateFin.setMinutes(59);
            dateFin.setSeconds(59);
            String d1 = dateFormat.format(dateDebut);
            String d2 = dateFormat.format(dateFin);
            DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            String d111 = dateFormat1.format(dateDebut);
            String d222 = dateFormat1.format(dateFin);
            DateFormat dateFormat5 = new SimpleDateFormat("yy-MM-dd");
            String f2=dateFormat5.format(dateFin);
             String f1=dateFormat5.format(dateDebut);
            System.out.println("d1" + d1);
            
            etablirconnection();
            JRDesignQuery query = new JRDesignQuery();
            if (filtreTiersEtat == null || filtreTiersEtat.getNom().equalsIgnoreCase("")) {
                System.out.println("client vide");
                JsfUtil.addErrorMessage("Veuillez selectionner le patient");
            } else {
               
             
             String typeOperationExtrait=" and (( typeoperation.`libelle`='facture' ) or ( typeoperation.`libelle`='Depense Fournisseur' ) or ( typeoperation.`libelle`='Depense Divers' ) or ( typeoperation.`libelle`='Reglement client' ) )";
                query.setText("SELECT  operation.`client` AS operation_client, operation.`id` AS operation_id,operation.`montantcredite` AS montantcredite, operation.`libelleoperation` AS operation_libelleoperation, ifnull(operation.`montantdebite`,0) AS operation_montantdebite, ifnull(operation.`montantcredite`,0)AS operation_montantcredite, operation.`dateoperation` AS operation_date_operation, operation.`dateechenace` AS operation_dateechenace, operation.`REFERENCE` AS operation_REFERENCE, client.`code` AS client_code, client.`id` AS client_id, client.`nom` AS client_nom, banque.`libelle` AS banque_libelle, banque.`code` AS banque_code, banque.`id` AS banque_id, typeoperation.`libelle` AS typeoperation_libelle, typeoperation.`id` AS typeoperation_id, cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal FROM `operation` operation INNER JOIN `client` client ON operation.`client` = client.`id` LEFT JOIN `banque` banque ON ((operation.`banque` = banque.`id`)) INNER JOIN `typeoperation` typeoperation ON operation.`typeoperation` = typeoperation.`id`, `cabinet` cabinet"
                        + " where client.`id`='" + filtreTiersEtat.getId() + "' and  DATE_FORMAT(operation.`dateoperation`,'%y-%m-%d') >= '" + f1 + "' and  DATE_FORMAT(operation.`dateoperation`,'%y-%m-%d')  <= '" + f2 + "' "+typeOperationExtrait+"    ORDER BY operation.`client`,operation.`dateoperation`");

            }
            System.out.println("Query" + query.getText());
            jasperDesign.setQuery(query);
            jasperDesign.setName("ExtraitComptePatient");

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

            jrFame.setHeight(92);
            jrFame.setWidth(842);
            jrFame.getLineBox().setLeftPadding(0);
            jrFame.getLineBox().setTopPadding(0);
            jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

            JRDesignStaticText libelleTitre = new JRDesignStaticText();
            libelleTitre.setWidth(360);
            libelleTitre.setHeight(25);
            libelleTitre.setX(200);
            libelleTitre.setY(30);
            libelleTitre.setText("EXTRAIT PATIENT");
            libelleTitre.setFontSize(16);
            libelleTitre.setForecolor(Color.BLUE);
            jrFame.addElement(libelleTitre);




            JRDesignStaticText libelleDate = new JRDesignStaticText();
            libelleDate.setWidth(250);
            libelleDate.setHeight(15);
            libelleDate.setText(" PERIODE : Du  " + d111 + "  Au  " + d222);
            libelleDate.setFontSize(10);
            libelleDate.setForecolor(Color.BLACK);
            libelleDate.setX(200);
            libelleDate.setY(55);
            jrFame.addElement(libelleDate);
            bandTiltle.setHeight(92);


            JRDesignStaticText libelleCode = new JRDesignStaticText();
            libelleCode.setWidth(160);
            libelleCode.setHeight(15);
            libelleCode.setText("CLIENT :" + filtreTiersEtat.getCode());
            libelleCode.setFontSize(10);
            libelleCode.setForecolor(Color.BLACK);
            libelleCode.setX(10);
            libelleCode.setY(77);
            jrFame.addElement(libelleCode);


            JRDesignStaticText libelleNomClient = new JRDesignStaticText();
            libelleNomClient.setWidth(200);
            libelleNomClient.setHeight(15);
            libelleNomClient.setText(filtreTiersEtat.getNom());
            libelleNomClient.setFontSize(10);
            libelleNomClient.setForecolor(Color.BLACK);
            libelleNomClient.setX(170);
            libelleNomClient.setY(77);
            jrFame.addElement(libelleNomClient);





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

            /*******************************************************/
            bandTiltle.addElement(jrFame);
            jasperDesign.setTitle(bandTiltle);
            /*******************************************************/



             JRDesignBand bandHeader = new JRDesignBand();
                BigDecimal bandHeaderHeight = new BigDecimal(15).setScale(0, RoundingMode.DOWN);
                bandHeader.setHeight(bandHeaderHeight.intValue());
                JRDesignBand columnHeader = new JRDesignBand();
                columnHeader.setHeight(15);
            
            {
                JRDesignStaticText libelleColonneDate = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_date_operation", "$F{operation_date_operation}", libelleColonneDate, "DATE", 0, 60, bandSomme);
                columnHeader.addElement(libelleColonneDate);
            }


            {
                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_libelleoperation", "$F{operation_libelleoperation}", libelleColonneCode, "OPERATION", 500, 355, bandSomme);
                columnHeader.addElement(libelleColonneCode);
            }

            {
                JRDesignStaticText libelleColonneMontantDebite = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_montantdebite", "(($F{operation_montantdebite}!=null)&($F{operation_montantdebite}.equals(\"0.000\")==false))?$F{operation_montantdebite}:Character.toString(' ')", libelleColonneMontantDebite, "DEBIT", 70, 80, bandSomme);
                columnHeader.addElement(libelleColonneMontantDebite);
            }

            //&($F{operation_com_MONTANT_COP}.equals(\"0.000\"))
            {
                JRDesignStaticText libelleColonneMontantCredite = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_montantcredite", "(($F{operation_montantcredite}!=null)&($F{operation_montantcredite}.equals(\"0.000\")==false))?$F{operation_montantcredite}:Character.toString(' ')", libelleColonneMontantCredite, "CREDIT", 140, 80, bandSomme);
                columnHeader.addElement(libelleColonneMontantCredite);
            }






            jasperDesign.setColumnHeader(columnHeader);
            //             jasperDesign.setDetail(bandHeader);
            ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandHeader);
            jasperDesign.setSummary(bandSomme);

            /***********************************Somme Total***************************************/
            
            JRDesignField montant = new JRDesignField();
            montant.setName("montantcredite");
            montant.setValueClass(java.math.BigDecimal.class);
            jasperDesign.addField(montant);




            JRDesignVariable varSomme = new JRDesignVariable();
            varSomme.setName("varTotal");
            varSomme.setValueClass(java.math.BigDecimal.class);
            varSomme.setCalculation(CalculationEnum.getByValue(new Byte("2")));
            jasperDesign.addVariable(varSomme);

            JRDesignExpression expressionE = new JRDesignExpression();
            expressionE.setValueClass(java.math.BigDecimal.class);
            expressionE.setText("$F{montantcredite}");
            varSomme.setExpression(expressionE);


            JRDesignExpression expressionEI = new JRDesignExpression();
            expressionEI.setValueClass(java.math.BigDecimal.class);
            expressionEI.setText("new BigDecimal(0).setScale(3, RoundingMode.UP)");

            varSomme.setInitialValueExpression(expressionEI);

            JRDesignTextField textField = new JRDesignTextField();
            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textField.setStretchWithOverflow(true);
            textField.setFontSize(10);
            textField.setWidth(100);
            textField.setX(70);
            textField.setY(7);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setValueClass(java.math.BigDecimal.class);
           expression.setText("$V{varCredit}.subtract($V{varDebit})");
         //   expression.setText("($V{varEspece}!=null)&($V{varCheque}!=null)?$V{varEspece}.subtract($V{varCheque}):new BigDecimal(0).setScale(3, RoundingMode.UP)");


            textField.setExpression(expression);
            textField.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));
            bandSomme.addElement(textField);

            JRDesignStaticText libelleTotalF = new JRDesignStaticText();
            libelleTotalF.setWidth(100);
            libelleTotalF.setHeight(13);
            libelleTotalF.setX(0);
            libelleTotalF.setY(7);
            libelleTotalF.setText("SOLDE PERIODE  :");
            libelleTotalF.setFontSize(10);
            libelleTotalF.setForecolor(Color.BLUE);
            bandSomme.addElement(libelleTotalF);




//            JRDesignStaticText libelleAncienSolde = new JRDesignStaticText();
//            libelleAncienSolde.setWidth(100);
//            libelleAncienSolde.setHeight(13);
//            libelleAncienSolde.setX(0);
//            libelleAncienSolde.setY(20);
//            libelleAncienSolde.setText("ANCIEN SOLDE     :");
//            libelleAncienSolde.setFontSize(10);
//            libelleAncienSolde.setForecolor(Color.BLUE);
//            bandSomme.addElement(libelleAncienSolde);
//
//
//
//            JRDesignTextField textFieldAncienSolde = new JRDesignTextField();
//            textFieldAncienSolde.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
//            textFieldAncienSolde.setStretchWithOverflow(true);
//            textFieldAncienSolde.setFontSize(10);
//            textFieldAncienSolde.setWidth(100);
//            textFieldAncienSolde.setX(70);
//            textFieldAncienSolde.setY(20);
//            JRDesignExpression expressionAncienSolde = new JRDesignExpression();
//            expressionAncienSolde.setValueClass(java.math.BigDecimal.class);
//            expressionAncienSolde.setText("new BigDecimal(" + mantantTotalAncienSolde + ").setScale(3, RoundingMode.UP)");
//            textFieldAncienSolde.setExpression(expressionAncienSolde);
//            textFieldAncienSolde.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));
//            bandSomme.addElement(textFieldAncienSolde);
//
//
//            JRDesignStaticText libelleSoldeGEN = new JRDesignStaticText();
//            libelleSoldeGEN.setWidth(100);
//            libelleSoldeGEN.setHeight(13);
//            libelleSoldeGEN.setX(0);
//            libelleSoldeGEN.setY(33);
//            libelleSoldeGEN.setText("SOLDE GENERAL :");
//            libelleSoldeGEN.setFontSize(10);
//            libelleSoldeGEN.setForecolor(Color.BLUE);
//            bandSomme.addElement(libelleSoldeGEN);
//
//            JRDesignTextField textFieldSommeGEN = new JRDesignTextField();
//            textFieldSommeGEN.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
//            textFieldSommeGEN.setStretchWithOverflow(true);
//            textFieldSommeGEN.setFontSize(10);
//            textFieldSommeGEN.setWidth(100);
//            textFieldSommeGEN.setX(70);
//            textFieldSommeGEN.setY(33);
//            JRDesignExpression expressionSommeGN = new JRDesignExpression();
//            expressionSommeGN.setValueClass(java.math.BigDecimal.class);
//            // expressionSommeGN.setText("($V{varEspece}.subtract($V{varCheque})).add(new BigDecimal("+mantantTotalAncienSolde+").setScale(3, RoundingMode.UP))");
//            expressionSommeGN.setText("($V{varEspece}!=null)&($V{varCheque}!=null)?$V{varEspece}.subtract($V{varCheque}).add(new BigDecimal(" + mantantTotalAncienSolde + ").setScale(3, RoundingMode.UP)):new BigDecimal(0).setScale(3, RoundingMode.UP)");
//            // expressionSommeGN.setText("new BigDecimal(0).setScale(3, RoundingMode.UP)");
//            textFieldSommeGEN.setExpression(expressionSommeGN);
//            textFieldSommeGEN.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));
//            bandSomme.addElement(textFieldSommeGEN);
//
//
//            JRDesignStaticText libelleImpaye = new JRDesignStaticText();
//            libelleImpaye.setWidth(100);
//            libelleImpaye.setHeight(13);
//            libelleImpaye.setX(0);
//            libelleImpaye.setY(50);
//            libelleImpaye.setText("IMPAYE :");
//            libelleImpaye.setFontSize(10);
//            libelleImpaye.setForecolor(Color.BLUE);
//            bandSomme.addElement(libelleSoldeGEN);

//            JRDesignStaticText libelleRegNonEch = new JRDesignStaticText();
//            libelleRegNonEch.setWidth(100);
//            libelleRegNonEch.setHeight(13);
//            libelleRegNonEch.setX(0);
//            libelleRegNonEch.setY(63);
//            libelleRegNonEch.setText("REG NON ECHUS :");
//            libelleRegNonEch.setFontSize(10);
//            libelleRegNonEch.setForecolor(Color.BLUE);
//            bandSomme.addElement(libelleRegNonEch);
//
//            JRDesignTextField textFieldNonEchus = new JRDesignTextField();
//            textFieldNonEchus.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
//            textFieldNonEchus.setStretchWithOverflow(true);
//            textFieldNonEchus.setFontSize(10);
//            textFieldNonEchus.setWidth(100);
//            textFieldNonEchus.setX(70);
//            textFieldNonEchus.setY(63);
//            JRDesignExpression expressionNonEchus = new JRDesignExpression();
//            expressionNonEchus.setValueClass(java.math.BigDecimal.class);
//            expressionNonEchus.setText("new BigDecimal(" + mantantNonEchus + ").setScale(3, RoundingMode.UP)");
//            textFieldNonEchus.setExpression(expressionNonEchus);
//            textFieldNonEchus.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));
//            bandSomme.addElement(textFieldNonEchus);


//            JRDesignStaticText libelleEncour = new JRDesignStaticText();
//            libelleEncour.setWidth(100);
//            libelleEncour.setHeight(13);
//            libelleEncour.setX(0);
//            libelleEncour.setY(76);
//            libelleEncour.setText("ENCOURS             :");
//            libelleEncour.setFontSize(10);
//            libelleEncour.setForecolor(Color.BLUE);
//            bandSomme.addElement(libelleEncour);
//
//            JRDesignTextField textFieldEncours = new JRDesignTextField();
//            textFieldEncours.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
//            textFieldEncours.setStretchWithOverflow(true);
//            textFieldEncours.setFontSize(10);
//            textFieldEncours.setWidth(100);
//            textFieldEncours.setX(70);
//            textFieldEncours.setY(76);
//            JRDesignExpression expressionEncours = new JRDesignExpression();
//            expressionEncours.setValueClass(java.math.BigDecimal.class);
//            // expressionEncours.setText("($V{varEspece}!=null)&($V{varCheque}!=null)?(($V{varEspece}.subtract($V{varCheque})).add(new BigDecimal("+mantantTotalAncienSolde+").setScale(3, RoundingMode.UP))).add(new BigDecimal("+mantantNonEchus+").setScale(3, RoundingMode.UP)):new BigDecimal(0).setScale(3, RoundingMode.UP)");
//            expressionEncours.setText("($V{varEspece}!=null)&($V{varCheque}!=null)?(($V{varEspece}.subtract($V{varCheque})).add(new BigDecimal(" + mantantTotalAncienSolde + ").setScale(3, RoundingMode.UP))).add(new BigDecimal(" + mantantNonEchus + ").setScale(3, RoundingMode.UP)):new BigDecimal(5).setScale(3, RoundingMode.UP)");
//            textFieldEncours.setExpression(expressionEncours);
//            textFieldEncours.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));
//            bandSomme.addElement(textFieldEncours);


            /*************************************************************************************************/
            jasperDesign.setNoData(bandSomme);

            jasperDesign.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap parameters = new HashMap();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

            DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
            Date d = new Date();
            String dd = dateFormat15.format(d);
            String nomFichier = "Rapport_Extrait_Patient" + dd.replace(" ", "_");
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
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
   /////////////////////Caisse ///////////////////////////////
            
                     public void rapportCaisse() throws JRException, IOException {
        try {
           
  
          int  pageWidth = 595;
            optionOrient = "1";
            Xligne=0;
            JRDesignFrame jrFame = new JRDesignFrame();
            DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy ");
            DateFormat dateFormat3 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d22 = new Date();
            String ddd = dateFormat3.format(d22);
            JasperDesign jasperDesign = new JasperDesign();


            JRDesignBand bandTiltle = new JRDesignBand();

            JRDesignStaticText libelleDateSys = new JRDesignStaticText();
            libelleDateSys.setWidth(150);
            libelleDateSys.setHeight(20);
            libelleDateSys.setText("DATE : " + ddd);
            libelleDateSys.setFontSize(10);
            libelleDateSys.setForecolor(Color.BLACK);
            libelleDateSys.setX(440);
            libelleDateSys.setY(10);
            jrFame.addElement(libelleDateSys);

            JRDesignBand bandBlanc = new JRDesignBand();
            bandBlanc.setHeight(200);
            JRDesignStaticText libellePgeB = new JRDesignStaticText();
            libellePgeB.setWidth(50);
            libellePgeB.setHeight(20);
            libellePgeB.setX(0);
            libellePgeB.setY(30);
            libellePgeB.setText("Tel :");
            libellePgeB.setFontSize(12);
            libellePgeB.setForecolor(Color.BLACK);
            bandBlanc.addElement(libellePgeB);
            jasperDesign.setNoData(bandBlanc);

          
            JRDesignBand bandSomme = new JRDesignBand();
            bandSomme.setHeight(200);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            dateDebut.setHours(0);
            dateDebut.setMinutes(0);
            dateDebut.setSeconds(0);
            dateFin.setHours(23);
            dateFin.setMinutes(59);
            dateFin.setSeconds(59);
            String d1 = dateFormat.format(dateDebut);
            String d2 = dateFormat.format(dateFin);
            DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
            String d111 = dateFormat1.format(dateDebut);
            String d222 = dateFormat1.format(dateFin);
            DateFormat dateFormat5 = new SimpleDateFormat("yy-MM-dd");
            String f2=dateFormat5.format(dateFin);
             String f1=dateFormat5.format(dateDebut);
            System.out.println("d1" + d1);
            
            etablirconnection();
            JRDesignQuery query = new JRDesignQuery();
           
               
             
             String typeOperationExtrait=" and ( ( typeoperation.`libelle`='Depense Fournisseur' ) or ( typeoperation.`libelle`='Depense Divers' ) or ( typeoperation.`libelle`='Reglement client' ) )";
                query.setText("SELECT  operation.`client` AS operation_client, operation.`id` AS operation_id,operation.`montantcheque` AS operation_montantcheque, operation.`montantespece` AS operation_montantespece, operation.`montanttraite` AS operation_montanttraite, operation.`montantvirement` AS operation_montantvirement, operation.`REFERENCE` AS operation_REFERENCE,operation.`montantcredite` AS montantcredite, operation.`libelleoperation` AS operation_libelleoperation, ifnull(operation.`montantdebite`,0) AS operation_montantdebite, ifnull(operation.`montantcredite`,0)AS operation_montantcredite, operation.`dateoperation` AS operation_date_operation, operation.`dateechenace` AS operation_dateechenace, operation.`REFERENCE` AS operation_REFERENCE, client.`code` AS client_code, client.`id` AS client_id, client.`nom` AS client_nom, banque.`libelle` AS banque_libelle, banque.`code` AS banque_code, banque.`id` AS banque_id, typeoperation.`libelle` AS typeoperation_libelle, typeoperation.`id` AS typeoperation_id, cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal FROM `operation` operation INNER JOIN `client` client ON operation.`client` = client.`id` LEFT JOIN `banque` banque ON ((operation.`banque` = banque.`id`)) INNER JOIN `typeoperation` typeoperation ON operation.`typeoperation` = typeoperation.`id`, `cabinet` cabinet"
                        + " where   DATE_FORMAT(operation.`dateoperation`,'%y-%m-%d') >= '" + f1 + "' and  DATE_FORMAT(operation.`dateoperation`,'%y-%m-%d')  <= '" + f2 + "' "+typeOperationExtrait+"    ORDER BY operation.`client`,operation.`dateoperation`");

            
            System.out.println("Query" + query.getText());
            jasperDesign.setQuery(query);
            jasperDesign.setName("ExtraitCaisse");

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

            jrFame.setHeight(92);
            jrFame.setWidth(842);
            jrFame.getLineBox().setLeftPadding(0);
            jrFame.getLineBox().setTopPadding(0);
            jrFame.setMode(ModeEnum.getByValue(new Byte("1")));

            JRDesignStaticText libelleTitre = new JRDesignStaticText();
            libelleTitre.setWidth(360);
            libelleTitre.setHeight(25);
            libelleTitre.setX(200);
            libelleTitre.setY(30);
            libelleTitre.setText("CAISSE");
            libelleTitre.setFontSize(16);
            libelleTitre.setForecolor(Color.BLUE);
            jrFame.addElement(libelleTitre);




            JRDesignStaticText libelleDate = new JRDesignStaticText();
            libelleDate.setWidth(250);
            libelleDate.setHeight(15);
            libelleDate.setText(" PERIODE : Du  " + d111 + "  Au  " + d222);
            libelleDate.setFontSize(10);
            libelleDate.setForecolor(Color.BLACK);
            libelleDate.setX(200);
            libelleDate.setY(55);
            jrFame.addElement(libelleDate);
            bandTiltle.setHeight(92);


            



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

            /*******************************************************/
            bandTiltle.addElement(jrFame);
            jasperDesign.setTitle(bandTiltle);
            /*******************************************************/



             JRDesignBand bandHeader = new JRDesignBand();
                BigDecimal bandHeaderHeight = new BigDecimal(15).setScale(0, RoundingMode.DOWN);
                bandHeader.setHeight(bandHeaderHeight.intValue());
                JRDesignBand columnHeader = new JRDesignBand();
                columnHeader.setHeight(15);
            
            {
                JRDesignStaticText libelleColonneDate = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_date_operation", "$F{operation_date_operation}", libelleColonneDate, "DATE", 0, 60, bandSomme);
                columnHeader.addElement(libelleColonneDate);
            }


//            {
//                JRDesignStaticText libelleColonneCode = new JRDesignStaticText();
//                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_libelleoperation", "$F{operation_libelleoperation}", libelleColonneCode, "OPERATION", 500, 355, bandSomme);
//                columnHeader.addElement(libelleColonneCode);
//            }
   {
                    JRDesignStaticText libelleColonneClient = new JRDesignStaticText();
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "client_nom", "(($F{client_nom}!=null)&($F{client_nom}.equals(\"\")==false))?$F{client_nom}:Character.toString(' ')", libelleColonneClient, "CLIENT", 0, 120, bandSomme);
                    libelleColonneClient.setHorizontalAlignment(HorizontalAlignEnum.CENTER);
                    columnHeader.addElement(libelleColonneClient);
                }
            {
                JRDesignStaticText libelleColonneMontantDebite = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_montantdebite", "(($F{operation_montantdebite}!=null)&($F{operation_montantdebite}.equals(\"0.000\")==false))?$F{operation_montantdebite}:Character.toString(' ')", libelleColonneMontantDebite, "DEBIT", 70, 60, bandSomme);
                columnHeader.addElement(libelleColonneMontantDebite);
            }

            //&($F{operation_com_MONTANT_COP}.equals(\"0.000\"))
            {
                JRDesignStaticText libelleColonneMontantCredite = new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_montantcredite", "(($F{operation_montantcredite}!=null)&($F{operation_montantcredite}.equals(\"0.000\")==false))?$F{operation_montantcredite}:Character.toString(' ')", libelleColonneMontantCredite, "CREDIT", 140, 60, bandSomme);
                columnHeader.addElement(libelleColonneMontantCredite);
            }

  
   {
                JRDesignStaticText libelleColonneCheque= new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_montantcheque", "(($F{operation_montantcheque}!=null)&($F{operation_montantcheque}.equals(\"0.000\")==false))?$F{operation_montantcheque}:Character.toString(' ')", libelleColonneCheque, "CHEQUE", 140, 60, bandSomme);
                columnHeader.addElement(libelleColonneCheque);
            }
   {
                JRDesignStaticText libelleColonneESPECE= new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_montantespece", "(($F{operation_montantespece}!=null)&($F{operation_montantespece}.equals(\"0.000\")==false))?$F{operation_montantespece}:Character.toString(' ')", libelleColonneESPECE, "ESPECE", 140, 60, bandSomme);
                columnHeader.addElement(libelleColonneESPECE);
            }
   {
                JRDesignStaticText libelleColonneVIREMENT= new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_montantvirement", "(($F{operation_montantvirement}!=null)&($F{operation_montantvirement}.equals(\"0.000\")==false))?$F{operation_montantvirement}:Character.toString(' ')", libelleColonneVIREMENT, "VIREMENT", 140, 60, bandSomme);
                columnHeader.addElement(libelleColonneVIREMENT);
            }

    {
                JRDesignStaticText libelleColonneTRAITE= new JRDesignStaticText();
                styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_montanttraite", "(($F{operation_montanttraite}!=null)&($F{operation_montanttraite}.equals(\"0.000\")==false))?$F{operation_montanttraite}:Character.toString(' ')", libelleColonneTRAITE, "TRAITE", 140, 60, bandSomme);
                columnHeader.addElement(libelleColonneTRAITE);
            }
            jasperDesign.setColumnHeader(columnHeader);
            //             jasperDesign.setDetail(bandHeader);
            ((JRDesignSection) jasperDesign.getDetailSection()).addBand(bandHeader);
            jasperDesign.setSummary(bandSomme);

            /***********************************Somme Total***************************************/
            
            JRDesignField montant = new JRDesignField();
            montant.setName("montantcredite");
            montant.setValueClass(java.math.BigDecimal.class);
            jasperDesign.addField(montant);




            JRDesignVariable varSomme = new JRDesignVariable();
            varSomme.setName("varTotal");
            varSomme.setValueClass(java.math.BigDecimal.class);
            varSomme.setCalculation(CalculationEnum.getByValue(new Byte("2")));
            jasperDesign.addVariable(varSomme);

            JRDesignExpression expressionE = new JRDesignExpression();
            expressionE.setValueClass(java.math.BigDecimal.class);
            expressionE.setText("$F{montantcredite}");
            varSomme.setExpression(expressionE);


            JRDesignExpression expressionEI = new JRDesignExpression();
            expressionEI.setValueClass(java.math.BigDecimal.class);
            expressionEI.setText("new BigDecimal(0).setScale(3, RoundingMode.UP)");

            varSomme.setInitialValueExpression(expressionEI);

            JRDesignTextField textField = new JRDesignTextField();
            textField.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
            textField.setStretchWithOverflow(true);
            textField.setFontSize(10);
            textField.setWidth(100);
            textField.setX(70);
            textField.setY(7);
            JRDesignExpression expression = new JRDesignExpression();
            expression.setValueClass(java.math.BigDecimal.class);
           expression.setText("$V{varCredit}.subtract($V{varDebit})");
         //   expression.setText("($V{varEspece}!=null)&($V{varCheque}!=null)?$V{varEspece}.subtract($V{varCheque}):new BigDecimal(0).setScale(3, RoundingMode.UP)");


            textField.setExpression(expression);
            textField.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));
            bandSomme.addElement(textField);

            JRDesignStaticText libelleTotalF = new JRDesignStaticText();
            libelleTotalF.setWidth(100);
            libelleTotalF.setHeight(13);
            libelleTotalF.setX(0);
            libelleTotalF.setY(7);
            libelleTotalF.setText("SOLDE  :");
            libelleTotalF.setFontSize(10);
            libelleTotalF.setForecolor(Color.BLUE);
            bandSomme.addElement(libelleTotalF);




//            JRDesignStaticText libelleAncienSolde = new JRDesignStaticText();
//            libelleAncienSolde.setWidth(100);
//            libelleAncienSolde.setHeight(13);
//            libelleAncienSolde.setX(0);
//            libelleAncienSolde.setY(20);
//            libelleAncienSolde.setText("ANCIEN SOLDE     :");
//            libelleAncienSolde.setFontSize(10);
//            libelleAncienSolde.setForecolor(Color.BLUE);
//            bandSomme.addElement(libelleAncienSolde);
//
//
//
//            JRDesignTextField textFieldAncienSolde = new JRDesignTextField();
//            textFieldAncienSolde.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
//            textFieldAncienSolde.setStretchWithOverflow(true);
//            textFieldAncienSolde.setFontSize(10);
//            textFieldAncienSolde.setWidth(100);
//            textFieldAncienSolde.setX(70);
//            textFieldAncienSolde.setY(20);
//            JRDesignExpression expressionAncienSolde = new JRDesignExpression();
//            expressionAncienSolde.setValueClass(java.math.BigDecimal.class);
//            expressionAncienSolde.setText("new BigDecimal(" + mantantTotalAncienSolde + ").setScale(3, RoundingMode.UP)");
//            textFieldAncienSolde.setExpression(expressionAncienSolde);
//            textFieldAncienSolde.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));
//            bandSomme.addElement(textFieldAncienSolde);
//
//
//            JRDesignStaticText libelleSoldeGEN = new JRDesignStaticText();
//            libelleSoldeGEN.setWidth(100);
//            libelleSoldeGEN.setHeight(13);
//            libelleSoldeGEN.setX(0);
//            libelleSoldeGEN.setY(33);
//            libelleSoldeGEN.setText("SOLDE GENERAL :");
//            libelleSoldeGEN.setFontSize(10);
//            libelleSoldeGEN.setForecolor(Color.BLUE);
//            bandSomme.addElement(libelleSoldeGEN);
//
//            JRDesignTextField textFieldSommeGEN = new JRDesignTextField();
//            textFieldSommeGEN.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
//            textFieldSommeGEN.setStretchWithOverflow(true);
//            textFieldSommeGEN.setFontSize(10);
//            textFieldSommeGEN.setWidth(100);
//            textFieldSommeGEN.setX(70);
//            textFieldSommeGEN.setY(33);
//            JRDesignExpression expressionSommeGN = new JRDesignExpression();
//            expressionSommeGN.setValueClass(java.math.BigDecimal.class);
//            // expressionSommeGN.setText("($V{varEspece}.subtract($V{varCheque})).add(new BigDecimal("+mantantTotalAncienSolde+").setScale(3, RoundingMode.UP))");
//            expressionSommeGN.setText("($V{varEspece}!=null)&($V{varCheque}!=null)?$V{varEspece}.subtract($V{varCheque}).add(new BigDecimal(" + mantantTotalAncienSolde + ").setScale(3, RoundingMode.UP)):new BigDecimal(0).setScale(3, RoundingMode.UP)");
//            // expressionSommeGN.setText("new BigDecimal(0).setScale(3, RoundingMode.UP)");
//            textFieldSommeGEN.setExpression(expressionSommeGN);
//            textFieldSommeGEN.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));
//            bandSomme.addElement(textFieldSommeGEN);
//
//
//            JRDesignStaticText libelleImpaye = new JRDesignStaticText();
//            libelleImpaye.setWidth(100);
//            libelleImpaye.setHeight(13);
//            libelleImpaye.setX(0);
//            libelleImpaye.setY(50);
//            libelleImpaye.setText("IMPAYE :");
//            libelleImpaye.setFontSize(10);
//            libelleImpaye.setForecolor(Color.BLUE);
//            bandSomme.addElement(libelleSoldeGEN);

//            JRDesignStaticText libelleRegNonEch = new JRDesignStaticText();
//            libelleRegNonEch.setWidth(100);
//            libelleRegNonEch.setHeight(13);
//            libelleRegNonEch.setX(0);
//            libelleRegNonEch.setY(63);
//            libelleRegNonEch.setText("REG NON ECHUS :");
//            libelleRegNonEch.setFontSize(10);
//            libelleRegNonEch.setForecolor(Color.BLUE);
//            bandSomme.addElement(libelleRegNonEch);
//
//            JRDesignTextField textFieldNonEchus = new JRDesignTextField();
//            textFieldNonEchus.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
//            textFieldNonEchus.setStretchWithOverflow(true);
//            textFieldNonEchus.setFontSize(10);
//            textFieldNonEchus.setWidth(100);
//            textFieldNonEchus.setX(70);
//            textFieldNonEchus.setY(63);
//            JRDesignExpression expressionNonEchus = new JRDesignExpression();
//            expressionNonEchus.setValueClass(java.math.BigDecimal.class);
//            expressionNonEchus.setText("new BigDecimal(" + mantantNonEchus + ").setScale(3, RoundingMode.UP)");
//            textFieldNonEchus.setExpression(expressionNonEchus);
//            textFieldNonEchus.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));
//            bandSomme.addElement(textFieldNonEchus);


//            JRDesignStaticText libelleEncour = new JRDesignStaticText();
//            libelleEncour.setWidth(100);
//            libelleEncour.setHeight(13);
//            libelleEncour.setX(0);
//            libelleEncour.setY(76);
//            libelleEncour.setText("ENCOURS             :");
//            libelleEncour.setFontSize(10);
//            libelleEncour.setForecolor(Color.BLUE);
//            bandSomme.addElement(libelleEncour);
//
//            JRDesignTextField textFieldEncours = new JRDesignTextField();
//            textFieldEncours.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
//            textFieldEncours.setStretchWithOverflow(true);
//            textFieldEncours.setFontSize(10);
//            textFieldEncours.setWidth(100);
//            textFieldEncours.setX(70);
//            textFieldEncours.setY(76);
//            JRDesignExpression expressionEncours = new JRDesignExpression();
//            expressionEncours.setValueClass(java.math.BigDecimal.class);
//            // expressionEncours.setText("($V{varEspece}!=null)&($V{varCheque}!=null)?(($V{varEspece}.subtract($V{varCheque})).add(new BigDecimal("+mantantTotalAncienSolde+").setScale(3, RoundingMode.UP))).add(new BigDecimal("+mantantNonEchus+").setScale(3, RoundingMode.UP)):new BigDecimal(0).setScale(3, RoundingMode.UP)");
//            expressionEncours.setText("($V{varEspece}!=null)&($V{varCheque}!=null)?(($V{varEspece}.subtract($V{varCheque})).add(new BigDecimal(" + mantantTotalAncienSolde + ").setScale(3, RoundingMode.UP))).add(new BigDecimal(" + mantantNonEchus + ").setScale(3, RoundingMode.UP)):new BigDecimal(5).setScale(3, RoundingMode.UP)");
//            textFieldEncours.setExpression(expressionEncours);
//            textFieldEncours.setHorizontalAlignment(HorizontalAlignEnum.getByValue(new Byte("3")));
//            bandSomme.addElement(textFieldEncours);


            /*************************************************************************************************/
            jasperDesign.setNoData(bandSomme);

            jasperDesign.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
            HashMap parameters = new HashMap();
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection1);

            DateFormat dateFormat15 = new SimpleDateFormat("dd MM yyyy HH mm ss");
            Date d = new Date();
            String dd = dateFormat15.format(d);
            String nomFichier = "Rapport_Caisse" + dd.replace(" ", "_");
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
            Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                libelleTitre.setText("Liste des Reglements Clients");
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
                typeop = jpaTypeoperation.findByParameterSingleResult("Select u from Typeoperation u where u.libelle=:libelle", "libelle", nompage);
                System.out.println("typeop " + typeop.getLibelle());
            } catch (Exception e) {
            }
                
                JRDesignQuery query = new JRDesignQuery();           
            query.setText("SELECT cabinet.`id` AS cabinet_id, cabinet.`code` AS cabinet_code, cabinet.`libelle` AS cabinet_libelle, cabinet.`adresse` AS cabinet_adresse, cabinet.`fax` AS cabinet_fax, cabinet.`tel` AS cabinet_tel, cabinet.`matricule` AS cabinet_matricule, cabinet.`ville` AS cabinet_ville, cabinet.`matriculefiscale` AS cabinet_matriculefiscale, cabinet.`codepostal` AS cabinet_codepostal, operation.`id` AS operation_id, operation.`modereglement` AS operation_modereglement, operation.`banque` AS operation_banque, operation.`typeoperation` AS operation_typeoperation, operation.`client` AS operation_client, operation.`numerooperation` AS operation_numerooperation, operation.`dateoperation` AS operation_dateoperation, operation.`montantoperation` AS operation_montantoperation, operation.`montantdebite` AS operation_montantdebite, operation.`montantcredite` AS operation_montantcredite, operation.`montantcheque` AS operation_montantcheque, operation.`montantespece` AS operation_montantespece, operation.`libelleoperation` AS operation_libelleoperation, operation.`montanttraite` AS operation_montanttraite, operation.`montantvirement` AS operation_montantvirement, operation.`user` AS operation_user, operation.`libelleoperation` AS operation_libelleoperation, operation.`reference` AS operation_reference, operation.`recu` AS operation_recu, operation.`emetteur` AS operation_emetteur, operation.`dateechenace` AS operation_dateechenace, operation.`detail` AS operation_detail, operation.`dateconsultation` AS operation_dateconsultation, operation.`motifconsultation` AS operation_motifconsultation, client.`nom` AS client_nom, client.`datecreation` AS client_datecreation, client.`code` AS client_code, modereglement.`id` AS modereglement_id, modereglement.`code` AS modereglement_code, modereglement.`libelle` AS modereglement_libelle FROM `operation` operation LEFT JOIN `client` client ON client.`id` = operation.`client` LEFT JOIN `modereglement` modereglement ON operation.`modereglement` = modereglement.`id`, `cabinet` cabinet  WHERE operation.`typeoperation`="+typeop.getId()+"");
              System.out.println("Query" + query.getText());
                jasperDesign.setQuery(query);
                jasperDesign.setName("ListeReglements");
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
                BigDecimal bandHeaderHeight = new BigDecimal(15).setScale(0, RoundingMode.DOWN);
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
                    styleLibelleColumnHeader(jasperDesign, bandHeader, "operation_montantoperation", "(($F{operation_montantoperation}!=null)&($F{operation_montantoperation}.equals(\"\")==false))?$F{operation_montantoperation}:Character.toString(' ')", libelleColonneCode, "MONTANT", 0, 60, bandSomme);
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
                String q2 = "Liste Des Reglements Clients";

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
                String nomFichier = "RapportListeReglements.pdf";
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
                Logger.getLogger(ReglementComController.class.getName()).log(Level.SEVERE, null, ex);

            }
        
    }

   
}

        