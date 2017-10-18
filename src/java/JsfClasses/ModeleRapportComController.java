package JsfClasses;

import entities.DetailDictionnaireRapportCom;
import entities.ModeleRapportCom;
import entities.DetailRapportCom;
import entities.TypeOpCom;
import entities.ZoneRapportCom;
import javax.transaction.SystemException;
import JsfClasses.util.JsfUtil;
import org.primefaces.component.datatable.DataTable;
import JsfClasses.util.PaginationHelper;
import SessionBeans.ModeleRapportComFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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

@ManagedBean(name = "modeleRapportComController")
@ViewScoped
public class ModeleRapportComController implements Serializable {
  @Resource      private UserTransaction utx;
    private ModeleRapportCom current;
      private ModeleRapportCom modelesource;
        private ModeleRapportCom modeledest;
        private TypeOpCom typedest;
    private DataModel items = null;
    @EJB
    private SessionBeans.ModeleRapportComFacade ejbFacade;
    @EJB
     private SessionBeans.DetailRapportComFacade ejbFacadeDetailRapportCom;
    @EJB
     private SessionBeans.DictionnaireModeleRapportComFacade ejbFacadeDictionnaireModeleRapportCom;
    @EJB
     private SessionBeans.DetailDictionnaireRapportComFacade ejbFacadeDetailDictionnaireRapportCom;
     @EJB
     private SessionBeans.ZoneRapportComFacade ejbFacadeZoneRapportCom;
      @EJB
     private SessionBeans.TypeOpComFacade ejbFacadeTypeOpCom;
      private List <DetailRapportCom> rapportdetails;
      private ZoneRapportCom Header;
       private ZoneRapportCom Body;
        private ZoneRapportCom ColumnHeader;
         private ZoneRapportCom Footer;
         private List <String> zonerapportchoice;
        
         private List <String> expressionsfixesrapportdetails;
         private List <String> expressionsinforapportdetails;
    private PaginationHelper pagination;
    private DataTable rapportdetailstable;
    private int selectedItemIndex;
 private List<List<String> >Style;  
  private String pageActuelle;
   
    public ModeleRapportComController() {
    }
    @PostConstruct
    public void initAfterConstructor() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        try {
            items = (DataModel) map.get("items");
        } catch (Exception e) {
        }
        try {
            rapportdetails = (List<DetailRapportCom>) map.get("rapportdetails");
        } catch (Exception e) {
        }
        try {
            Header = (ZoneRapportCom) map.get("Header");
        } catch (Exception e) {
        }
        try {
            Body = (ZoneRapportCom) map.get("Body");
        } catch (Exception e) {
        }
        try {
            ColumnHeader = (ZoneRapportCom) map.get("ColumnHeader");
        } catch (Exception e) {
        }
        try {
            Footer = (ZoneRapportCom) map.get("Footer");
        } catch (Exception e) {
        }
        try {
            zonerapportchoice = (List<String>) map.get("zonerapportchoice");
        } catch (Exception e) {
        }
        try {
            expressionsfixesrapportdetails = (List<String>) map.get("expressionsfixesrapportdetails");
        } catch (Exception e) {
        }
        try {
            expressionsinforapportdetails = (List<String>) map.get("expressionsinforapportdetails");
        } catch (Exception e) {
        }
        try {
            rapportdetailstable = (DataTable) map.get("rapportdetailstable");
        } catch (Exception e) {
        }
        try {
            Style = (List<List<String>>) map.get("Style");
        } catch (Exception e) {
        }
        try {
            pageActuelle = (String) map.get("pageActuelle");
        } catch (Exception e) {
        }
        try {
            current = (ModeleRapportCom) map.get("current");
        } catch (Exception e) {
        }
        try {
            modelesource = (ModeleRapportCom) map.get("modelesource");
        } catch (Exception e) {
        }
        try {
            modeledest = (ModeleRapportCom) map.get("modeledest");
        } catch (Exception e) {
        }
        try {
            typedest = (TypeOpCom) map.get("typedest");
        } catch (Exception e) {
        }

    }

    public void envoyerParametres() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        map.put("rapportdetails", rapportdetails);
        map.put("Header", Header);
        map.put("Body", Body);
        map.put("ColumnHeader", ColumnHeader);
        map.put("Footer", Footer);
        map.put("zonerapportchoice", zonerapportchoice);
        map.put("expressionsfixesrapportdetails", expressionsfixesrapportdetails);
        map.put("expressionsinforapportdetails", expressionsinforapportdetails);
        map.put("rapportdetailstable", rapportdetailstable);
        map.put("Style", Style);
        map.put("pageActuelle", pageActuelle);
        map.put("current", current);
        map.put("modelesource", modelesource);
        map.put("modeledest", modeledest);
        map.put("typedest", typedest);
        map.put("items", items);


    }
    public ModeleRapportCom getSelected() {
        if (current == null) {
            current = new ModeleRapportCom();
            selectedItemIndex = -1;
        }
        return current;
    }

    public ModeleRapportCom getModeledest() {
        return modeledest;
    }

    public void setModeledest(ModeleRapportCom modeledest) {
        this.modeledest = modeledest;
    }

    public ModeleRapportCom getModelesource() {
        return modelesource;
    }

    public void setModelesource(ModeleRapportCom modelesource) {
        this.modelesource = modelesource;
    }

    public TypeOpCom getTypedest() {
        return typedest;
    }

    public void setTypedest(TypeOpCom typedest) {
        this.typedest = typedest;
    }

    public String getPageActuelle() {
        return pageActuelle;
    }

    public void setPageActuelle(String pageActuelle) {
        this.pageActuelle = pageActuelle;
    }
    
public String getExpresssionchampdesignationfixe()
{try{ if(rapportdetails.get(rapportdetailstable.getRowIndex()).getType().equals("fixe"))
{return expressionsfixesrapportdetails.get(rapportdetailstable.getRowIndex());}
else
{return "";} 
}
catch(Exception e){}
return "";
}
public void setExpresssionchampdesignationfixe(String s)
{try{ if(rapportdetails.get(rapportdetailstable.getRowIndex()).getType().equals("fixe"))
{expressionsfixesrapportdetails.set(rapportdetailstable.getRowIndex(),s);}

}
catch(Exception e){}}
public String getExpresssionchampdesignationinfo()
{try{ if(rapportdetails.get(rapportdetailstable.getRowIndex()).getType().equals("info"))
{return expressionsinforapportdetails.get(rapportdetailstable.getRowIndex());}
else
{return "";} 
}
catch(Exception e){}
return "";
}
public void setExpresssionchampdesignationinfo(String s)
{try{ if(rapportdetails.get(rapportdetailstable.getRowIndex()).getType().equals("info"))
{expressionsinforapportdetails.set(rapportdetailstable.getRowIndex(),s);}
}
catch(Exception e){}

}
    public ModeleRapportComFacade getEjbFacade() {
        return ejbFacade;
    }

    private ModeleRapportComFacade getFacade() {
        return ejbFacade;
    }

    public List<String> getZonerapportchoice() {
        return zonerapportchoice;
    }

    public void setZonerapportchoice(List<String> zonerapportchoice) {
        this.zonerapportchoice = zonerapportchoice;
    }

   public String getLignezonerapportchoice()
   {int ligne=rapportdetailstable.getRowIndex();
       return zonerapportchoice.get(ligne);}
   public void setLignezonerapportchoice(String s)
   {int ligne=rapportdetailstable.getRowIndex();
        zonerapportchoice.set(ligne,s);}
    public List <String> getSelectedStyle()
   {int ligne=rapportdetailstable.getRowIndex();
       return Style.get(ligne);}
   public void setSelectedStyle(List <String> s)
   {int ligne=rapportdetailstable.getRowIndex();
        Style.set(ligne,s);}  

    public DataTable getRapportdetailstable() {
        return rapportdetailstable;
    }

    public void setRapportdetailstable(DataTable rapportdetailstable) {
        this.rapportdetailstable = rapportdetailstable;
    }

    public ZoneRapportCom getBody() {
        return Body;
    }

    public List<DetailRapportCom> getRapportdetails() {
        return rapportdetails;
    }

    public void setRapportdetails(List<DetailRapportCom> rapportdetails) {
        this.rapportdetails = rapportdetails;
    }

    public void setBody(ZoneRapportCom Body) {
        this.Body = Body;
    }

    public ZoneRapportCom getColumnHeader() {
        return ColumnHeader;
    }

    public void setColumnHeader(ZoneRapportCom ColumnHeader) {
        this.ColumnHeader = ColumnHeader;
    }

    public ZoneRapportCom getFooter() {
        return Footer;
    }

    public void setFooter(ZoneRapportCom Footer) {
        this.Footer = Footer;
    }

    public ZoneRapportCom getHeader() {
        return Header;
    }

    public void setHeader(ZoneRapportCom Header) {
        this.Header = Header;
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
        envoyerParametres();
        return "modele_rapport_list";
    }

    public String prepareView() {
        current = (ModeleRapportCom) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }
public String prepareDuplicate()
{modeledest=new ModeleRapportCom();
modelesource=new ModeleRapportCom();
envoyerParametres();
    return "modele_rapport_duplicate";
}
public String duplicate() throws IllegalStateException, SecurityException, SystemException
{      
  try {utx.begin(); 
    modeledest.setIdModeleRapport(new Integer(1));
modeledest.setMargeLeft(0);
        modeledest.setMargeTop(0);
          modeledest.setMargeRight(0);
           modeledest.setMatgeBottom(0);
           modeledest.setPageHeight(modelesource.getPageHeight());
            modeledest.setPageWidth(modelesource.getPageWidth());
            modeledest.setOrientation(modelesource.getOrientation());
            LinkedList<DetailRapportCom> rapportdetailsdest = new LinkedList<DetailRapportCom>();
       modeledest.setIdModeleRapport(new Integer(1));
        modeledest.setRequette(ejbFacadeDictionnaireModeleRapportCom.find( modeledest.getTypeOperation().getNiTop()).getRequetteSql());
            getFacade().create(modeledest);
   String q = "SELECT z FROM ZoneRapportCom z WHERE z.modele = :modele";
           List<ZoneRapportCom> li=(List<ZoneRapportCom>) ejbFacadeZoneRapportCom.execCommandeList(q,"modele",modelesource);
        for (Iterator <ZoneRapportCom> it=li.iterator();it.hasNext();)
        {ZoneRapportCom zone=it.next();
        ZoneRapportCom zonedest=new ZoneRapportCom();
        zonedest.setHeightRapport(zone.getHeightRapport());
        zonedest.setTopRapport(zone.getTopRapport());
        zonedest.setLeftRapport(zone.getLeftRapport());
        zonedest.setWidthRapport(zone.getWidthRapport());
        zonedest.setTracageLigne(zone.getTracageLigne());
        zonedest.setIdZoneRapport(new Integer(1));
        zonedest.setModele(modeledest);
        zonedest.setNomZone(zone.getNomZone());
           
        ejbFacadeZoneRapportCom.create(zonedest);
     
     q="SELECT d FROM DetailRapportCom d WHERE d.zoneRapport = :zoneRapport Order by d.topRapport ASC,d.leftRapport ASC";
               List<DetailRapportCom> l=(List<DetailRapportCom>) ejbFacadeDetailRapportCom.execCommandeList(q,"zoneRapport",zone);
          //rapportdetails.addAll(zone.getDetailRapportComList());
           for (int i=0;i<l.size();i++)
        {DetailRapportCom detail=l.get(i);
        DetailRapportCom detaildest=new DetailRapportCom();
       detaildest.setAlignement(detail.getAlignement());
       detaildest.setExpression(detail.getExpression());
       detaildest.setFontSize(detail.getFontSize());
       detaildest.setGras(detail.getGras());
       detaildest.setHeightRapport(detail.getHeightRapport());
       detaildest.setItalique(detail.getItalique());
       detaildest.setLeftRapport(detail.getLeftRapport());
       detaildest.setNom(detail.getNom());
       detaildest.setSouligner(detail.getSouligner());
       detaildest.setStretchWithOverFlow(detail.getStretchWithOverFlow());
       detaildest.setType(detail.getType());
       detaildest.setTopRapport(detail.getTopRapport());
       detaildest.setWidthRapport(detail.getWidthRapport());
        detaildest.setZoneRapport(zonedest);
        detaildest.setIdDetailCom(new Integer(1));
            System.out.println(ejbFacadeDetailRapportCom.count());
        ejbFacadeDetailRapportCom.create(detaildest);
            System.out.println(ejbFacadeDetailRapportCom.count());
        }      
        } 
         JsfUtil.addSuccessMessage("Transaction Reussie");
   utx.commit();
            return prepareList();
        } catch (Exception e) {e.printStackTrace();
        
            utx.rollback();
            JsfUtil.addErrorMessage("Transaction Echouée");
            return null;
        }}
    public String prepareCreate() {
        current = new ModeleRapportCom();
       pageActuelle="Saisie";
        current.setMargeLeft(0);
         current.setMargeTop(0);
          current.setMargeRight(0);
           current.setMatgeBottom(0);
        current.setPageHeight(842);
          current.setPageWidth(595);
        rapportdetailstable=new DataTable();
        Style=new LinkedList<List<String>>();
        Header=new ZoneRapportCom();
         Footer=new ZoneRapportCom();
          ColumnHeader=new ZoneRapportCom();
           Body=new ZoneRapportCom();
           Header.setTopRapport(0);
           Header.setLeftRapport(0);
           Header.setNomZone("Header");
           Header.setTracageLigne(Boolean.TRUE);
           Body.setNomZone("Body");
            Body.setTopRapport(0);
           Body.setLeftRapport(0);
           Body.setTracageLigne(Boolean.TRUE);
            ColumnHeader.setTopRapport(0);
           ColumnHeader.setLeftRapport(0);
           ColumnHeader.setNomZone("Column Header");
            Footer.setTopRapport(0);
           Footer.setLeftRapport(0);
           Footer.setNomZone("Footer");
           Footer.setTracageLigne(Boolean.TRUE);
           rapportdetails=new LinkedList<DetailRapportCom>();
           expressionsfixesrapportdetails=new LinkedList<String>();
           expressionsinforapportdetails=new LinkedList<String>();
              zonerapportchoice=new LinkedList<String>();
           for (int i=0;i<10;i++)
           {DetailRapportCom rapportdet = new DetailRapportCom();
           rapportdet.setGras(Boolean.FALSE);
             rapportdet.setSouligner(Boolean.FALSE);
               rapportdet.setItalique(Boolean.FALSE);
               rapportdetails.add(rapportdet);
           expressionsfixesrapportdetails.add("");
          expressionsinforapportdetails.add(""); 
          zonerapportchoice.add("");
          Style.add(new LinkedList<String>());
           }
        selectedItemIndex = -1;
        envoyerParametres();
        return "modele_rapport_creation";
    }

    public String create() throws IllegalStateException, SecurityException, SystemException {
        try {utx.begin();
        current.setIdModeleRapport(new Integer(1));
        current.setRequette(ejbFacadeDictionnaireModeleRapportCom.find(current.getTypeOperation().getNiTop()).getRequetteSql());
            getFacade().create(current);
            Header.setIdZoneRapport(new Integer(1));
            Header.setModele(current);
           ejbFacadeZoneRapportCom.create(Header);
            Body.setIdZoneRapport(new Integer(1));
            Body.setModele(current);
            ejbFacadeZoneRapportCom.create(Body);
            ColumnHeader.setIdZoneRapport(new Integer(1));
            ColumnHeader.setModele(current);
             ejbFacadeZoneRapportCom.create(ColumnHeader);
           Footer.setIdZoneRapport(new Integer(1));
           Footer.setModele(current);
           ejbFacadeZoneRapportCom.create(Footer);
           for (int i=0;i<rapportdetails.size();i++)
           {DetailRapportCom rapportdet=rapportdetails.get(i);
           if ((rapportdet.getType()!=null)&&(zonerapportchoice.get(i).equals("")==false))
           { {
               System.out.println("try");
               if (rapportdet.getType().equalsIgnoreCase("fixe"))
           {
               System.out.println("fixe");
               rapportdet.setExpression(expressionsfixesrapportdetails.get(i));   
           rapportdet.setNom(expressionsfixesrapportdetails.get(i));
      
           }
               else{rapportdet.setNom(expressionsinforapportdetails.get(i));}
          
           }
           rapportdet.setIdDetailCom(new Integer(1));
               System.out.println(rapportdet.getNom());
           ejbFacadeDetailRapportCom.create(rapportdet);
               System.out.println(i+" detail créé");}
           }
            JsfUtil.addSuccessMessage("Transaction Reussie");
            utx.commit();
            return prepareCreate();
        } catch (Exception e) {e.printStackTrace();
        
            utx.rollback();
            JsfUtil.addErrorMessage("Transaction Echouée");
            return null;
        }
    }

    public String prepareEdit() {
        current = (ModeleRapportCom) getItems().getRowData();
       pageActuelle="Modification";
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        rapportdetailstable=new DataTable();
        rapportdetails=new LinkedList<DetailRapportCom>();
        Header=new ZoneRapportCom();
         Footer=new ZoneRapportCom();
          ColumnHeader=new ZoneRapportCom();
           Body=new ZoneRapportCom();
        zonerapportchoice=new LinkedList<String>();
        Style=new LinkedList<List<String>>();
          expressionsfixesrapportdetails=new LinkedList<String>();
         expressionsinforapportdetails=new LinkedList<String>();
       // List<ZoneRapportCom> li = current.getZoneRapportComList();
        String q = "SELECT z FROM ZoneRapportCom z WHERE z.modele = :modele";
           List<ZoneRapportCom> li=(List<ZoneRapportCom>) ejbFacadeZoneRapportCom.execCommandeList(q,"modele",current);
               List<DetailRapportCom> lHeader = new LinkedList<DetailRapportCom>();
            List<DetailRapportCom> lCols = new LinkedList<DetailRapportCom>();
            List<DetailRapportCom> lBody = new LinkedList<DetailRapportCom>();
            List<DetailRapportCom> lfoot = new LinkedList<DetailRapportCom>();
        for (Iterator <ZoneRapportCom> it=li.iterator();it.hasNext();)
        {
            ZoneRapportCom zone = it.next();
        
     if (zone.getNomZone().equals("Header"))
              {Header=zone;
                    q="SELECT d FROM DetailRapportCom d WHERE d.zoneRapport = :zoneRapport Order by d.topRapport ASC,d.leftRapport ASC";
                lHeader=(List<DetailRapportCom>) ejbFacadeDetailRapportCom.execCommandeList(q,"zoneRapport",zone);
              }
             if (zone.getNomZone().equals("Column Header"))
              {ColumnHeader=zone;
                q="SELECT d FROM DetailRapportCom d WHERE d.zoneRapport = :zoneRapport Order by d.topRapport ASC,d.leftRapport ASC";
                lCols=(List<DetailRapportCom>) ejbFacadeDetailRapportCom.execCommandeList(q,"zoneRapport",zone);
              } 
             if (zone.getNomZone().equals("Body"))
              {Body=zone;
                q="SELECT d FROM DetailRapportCom d WHERE d.zoneRapport = :zoneRapport Order by d.topRapport ASC,d.leftRapport ASC";
                lBody=(List<DetailRapportCom>) ejbFacadeDetailRapportCom.execCommandeList(q,"zoneRapport",zone);
              }
             if(zone.getNomZone().equals("Footer"))
              {Footer=zone;
                 q="SELECT d FROM DetailRapportCom d WHERE d.zoneRapport = :zoneRapport Order by d.topRapport ASC,d.leftRapport ASC";
                lfoot=(List<DetailRapportCom>) ejbFacadeDetailRapportCom.execCommandeList(q,"zoneRapport",zone);
              }  
       
          //rapportdetails.addAll(zone.getDetailRapportComList());
            
        }
         rapportdetails.addAll(lHeader);       rapportdetails.addAll(lCols);        rapportdetails.addAll(lBody);        rapportdetails.addAll(lfoot);   
         for (int i=0;i<rapportdetails.size();i++)
        {DetailRapportCom detail=rapportdetails.get(i);
           
            LinkedList<String> st = new LinkedList<String>();
        if(detail.getType().equals("info"))
{expressionsinforapportdetails.add(i,detail.getNom());
 expressionsfixesrapportdetails.add(i,"");
}
        else{   expressionsfixesrapportdetails.add(i,detail.getExpression());
 expressionsinforapportdetails.add(i,"");   
        }
            System.out.println(detail.getIdDetailCom()+"id detail");
        if (detail.getZoneRapport().equals(Header))
                {zonerapportchoice.add("Entete");
                   
                }
       
         if (detail.getZoneRapport().equals(ColumnHeader))
                { zonerapportchoice.add("Colonnes");
                 }
          if (detail.getZoneRapport().equals(Body))
                { zonerapportchoice.add("Corps");
                 }
           if (detail.getZoneRapport().equals(Footer))
                {zonerapportchoice.add("Pieds");
                 }
        if (detail.getGras()==true)
        {st.add("Bold");
        }
        if (detail.getItalique()==true)
        {st.add("Italic");
        }
        if (detail.getSouligner()==true)
        {st.add("Underline");
        }
       Style.add(st); 
        }
           System.out.println(zonerapportchoice.size()+"Choix");
           System.out.println(rapportdetails.size()+"Elements");
           envoyerParametres();
        return "modele_rapport_edition";
    }

    public String update() throws IllegalStateException, SecurityException, SystemException {
        try {utx.begin();
        
            System.out.println("avant edit");
            getFacade().edit(current);
            System.out.println("apres edit");
           ejbFacadeZoneRapportCom.edit(Body);
        ejbFacadeZoneRapportCom.edit(Header);
         ejbFacadeZoneRapportCom.edit(ColumnHeader);
          ejbFacadeZoneRapportCom.edit(Footer);
            LinkedList<DetailRapportCom> rapportdetailsmodif = new LinkedList<DetailRapportCom>();
            System.out.println("update");
          String q1="SELECT d FROM DetailRapportCom d WHERE d.zoneRapport = :zoneRapport Order by d.topRapport ASC,d.leftRapport ASC";
               List<DetailRapportCom> l1=(List<DetailRapportCom>) ejbFacadeDetailRapportCom.execCommandeList(q1,"zoneRapport",Header);

          rapportdetailsmodif.addAll(l1); 
          String q2="SELECT d FROM DetailRapportCom d WHERE d.zoneRapport = :zoneRapport Order by d.topRapport ASC,d.leftRapport ASC";
               List<DetailRapportCom> l2=(List<DetailRapportCom>) ejbFacadeDetailRapportCom.execCommandeList(q2,"zoneRapport",ColumnHeader);
        
          rapportdetailsmodif.addAll(l2); 
           String q="SELECT d FROM DetailRapportCom d  WHERE d.zoneRapport = :zoneRapport Order by d.topRapport ASC,d.leftRapport ASC ";
           List<DetailRapportCom> l=(List<DetailRapportCom>) ejbFacadeDetailRapportCom.execCommandeList(q,"zoneRapport",Body);
     
          rapportdetailsmodif.addAll(l);
          String q3="SELECT d FROM DetailRapportCom d WHERE d.zoneRapport = :zoneRapport Order by d.topRapport ASC,d.leftRapport ASC";
               List<DetailRapportCom> l3=(List<DetailRapportCom>) ejbFacadeDetailRapportCom.execCommandeList(q3,"zoneRapport",Footer);
      
          rapportdetailsmodif.addAll(l3); 
         
       
            
          for (int i=0;i<rapportdetailsmodif.size();i++)
        {DetailRapportCom detail=rapportdetailsmodif.get(i);
        ejbFacadeDetailRapportCom.remove(detail);
        }
            System.out.println("suppression");
          System.out.println( ejbFacadeDetailRapportCom.count());
              for (int i=0;i<rapportdetails.size();i++)
           {DetailRapportCom rapportdet=rapportdetails.get(i);
             System.out.println(rapportdet.getNom()+"-"+rapportdet.getExpression());
           if ((rapportdet.getType()!=null)&&(zonerapportchoice.get(i).equals("")==false))
           { {
              
               if (rapportdet.getType().equalsIgnoreCase("fixe"))
           {
              
               rapportdet.setExpression(expressionsfixesrapportdetails.get(i));   
         if (!(rapportdet.getNom()!=null))
               rapportdet.setNom(expressionsfixesrapportdetails.get(i));
           }
               else{rapportdet.setNom(expressionsinforapportdetails.get(i));}
          
           }
           rapportdet.setIdDetailCom(new Integer(1));
              
           ejbFacadeDetailRapportCom.create(rapportdet);
             System.out.println( ejbFacadeDetailRapportCom.count());
              }
           }
            
            JsfUtil.addSuccessMessage("Transction Réussie");
            utx.commit();
            return prepareList();
        } catch (Exception e) {
            e.printStackTrace();
            utx.rollback();
            JsfUtil.addErrorMessage("Transaction Echouée");
            return null;
        }
    }
public String valider() throws IllegalStateException, SecurityException, SystemException
{if (pageActuelle.equalsIgnoreCase("Saisie"))
{return create();}
else
{return update();}
}
    public String destroy() throws IllegalStateException, SecurityException, SystemException {
        current = (ModeleRapportCom) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreateModel();
        envoyerParametres();
        return "modele_rapport_list";
    }

    public String destroyAndView() throws IllegalStateException, SecurityException, SystemException {
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

    private void performDestroy() throws IllegalStateException, SecurityException, SystemException {
        try {utx.begin();
          String q = "SELECT z FROM ZoneRapportCom z WHERE z.modele = :modele";
           List<ZoneRapportCom> li=(List<ZoneRapportCom>) ejbFacadeZoneRapportCom.execCommandeList(q,"modele",current);
        for (Iterator <ZoneRapportCom> it=li.iterator();it.hasNext();)
        {ZoneRapportCom zone=it.next();
     
             q="SELECT d FROM DetailRapportCom d WHERE d.zoneRapport = :zoneRapport";
               List<DetailRapportCom> l=(List<DetailRapportCom>) ejbFacadeDetailRapportCom.execCommandeList(q,"zoneRapport",zone);
          //rapportdetails.addAll(zone.getDetailRapportComList());
         for (int i=0;i<l.size();i++)
        {DetailRapportCom detail=l.get(i);
        ejbFacadeDetailRapportCom.remove(detail);
        } 
         ejbFacadeZoneRapportCom.remove(zone);
        }
         
            getFacade().remove(current);
            utx.commit();
            JsfUtil.addSuccessMessage("Transaction Réussie");
        } catch (Exception e) {utx.rollback();
            JsfUtil.addErrorMessage("Transaction Echouée");
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

    @FacesConverter(forClass = ModeleRapportCom.class)
    public static class ModeleRapportComControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ModeleRapportComController controller = (ModeleRapportComController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "modeleRapportComController");
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
            if (object instanceof ModeleRapportCom) {
                ModeleRapportCom o = (ModeleRapportCom) object;
                return getStringKey(o.getIdModeleRapport());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + ModeleRapportComController.class.getName());
            }
        }
    }
    //*********************Autocomplete*******************************************************************************************//
      public List<TypeOpCom> autocompleteTypeOpCom(String pref) {
         List<TypeOpCom> result = new ArrayList<TypeOpCom>();
         
        try{
      
        result=(List<TypeOpCom>) getLikeTypeOpComComByCode(pref);
        
        return result;
        }catch(Exception d){System.out.println("Exception"+d);
            d.printStackTrace();
        result = new ArrayList<TypeOpCom>();
        
        return result;
        }}
         public List<TypeOpCom> getLikeTypeOpComComByCode(String code){
           String q="Select c from TypeOpCom c where c.nomTop like :code  ";
           List<TypeOpCom>l=(List<TypeOpCom>) ejbFacadeTypeOpCom.execCommandeList(q,"code",code+"%");
           return l;
     } 
         public List<ModeleRapportCom> getSourcesvalues()
         {return getFacade().findAll();}
         public List<String> getInfoExpressions()
         {List<String> result = new ArrayList<String>();
       int pos=rapportdetailstable.getRowIndex();
       DetailRapportCom detail=rapportdetails.get(pos);
             System.out.println(detail);
             System.out.println(detail.getType());
        try{ if(detail.getType().equals("info"))
        {  String q="SELECT d.libelle FROM DetailDictionnaireRapportCom d WHERE d.idDictionnaire.idTypeOperationCom=:type Order by d.libelle";
             List<String>ss=new ArrayList<String>();
                        List<Object>oo=new ArrayList<Object>();
                       
                        ss.add("type");
                       
                        oo.add(current.getTypeOperation().getNiTop());
        List<Object> p =ejbFacadeDetailDictionnaireRapportCom.findByParameterMultipleCreteriaMultipleObject(q,ss,oo);
      result = (List<String>)(List<?>) p;
        
        return result;}
        return new ArrayList<String>();
        }catch(Exception d){System.out.println("Exception"+d);
            d.printStackTrace();
        result = new ArrayList<String>();
        
        return result;
        }}
         public List<String> autocompleteexpressioninfo(String pref) {
         List<String> result = new ArrayList<String>();
        DetailRapportCom detail=(DetailRapportCom) rapportdetailstable.getRowData();
        try{ if(detail.getType().equals("info"))
        {
        result=(List<String>) getLikeExpression(pref);
        
        return result;}
        return new ArrayList<String>();
        }catch(Exception d){System.out.println("Exception"+d);
            d.printStackTrace();
        result = new ArrayList<String>();
        
        return result;
        }}
         public List<String> getLikeExpression(String code){
           String q="SELECT d.libelle FROM DetailDictionnaireRapportCom d WHERE d.libelle like :libelle and  d.idDictionnaire.idTypeOperationCom=:type order by d.libelle";
           List<String>ss=new ArrayList<String>();
                        List<Object>oo=new ArrayList<Object>();
                        ss.add("libelle");
                        ss.add("type");
                        oo.add(code+"%");
                        oo.add(current.getTypeOperation().getNiTop());
           List<Object> p =ejbFacadeDetailDictionnaireRapportCom.findByParameterMultipleCreteriaMultipleObject(q,ss,oo);
        List<String> l = (List<String>)(List<?>) p;
           return l;
     } 
           public List<String> autocompleteTypeDetail(String pref) {
         List<String> result = new ArrayList<String>();
         
        try{
      
        result.add("fixe");
        result.add("info");
        
        return result;
        }catch(Exception d){System.out.println("Exception"+d);
            d.printStackTrace();
        result = new ArrayList<String>();
        
        return result;
        }}
              public List<String> autocompleteTypeZone(String pref) {
         List<String> result = new ArrayList<String>();
         
        try{
      
        result.add("Entete");
        result.add("Colonnes");
         result.add("Corps");
         result.add("Pieds");
        return result;
        }catch(Exception d){System.out.println("Exception"+d);
            d.printStackTrace();
        result = new ArrayList<String>();
        
        return result;
        }}
       //******************************Listners********************************************************//
              public void zonerapportselectedlistner()
              {int pos=rapportdetailstable.getRowIndex();
              
              DetailRapportCom detail=rapportdetails.get(pos);
              if (zonerapportchoice.get(pos).equals("Entete"))
              {detail.setZoneRapport(Header);
              }
             if (zonerapportchoice.get(pos).equals("Colonnes"))
              {detail.setZoneRapport(ColumnHeader);
              } 
             if (zonerapportchoice.get(pos).equals("Corps"))
              {detail.setZoneRapport(Body);
              }
             if (zonerapportchoice.get(pos).equals("Pieds"))
              {detail.setZoneRapport(Footer);
              }
              }
             public void expressioninfoselectedlistner()
             {    String q="SELECT d FROM DetailDictionnaireRapportCom d WHERE d.libelle like :libelle and  d.idDictionnaire.idTypeOperationCom=:type";
           List<String>ss=new ArrayList<String>();
                        List<Object>oo=new ArrayList<Object>();
                        ss.add("libelle");
                        ss.add("type");
                        oo.add(expressionsinforapportdetails.get(rapportdetailstable.getRowIndex()));
                        oo.add(current.getTypeOperation().getNiTop());
          DetailDictionnaireRapportCom p =(DetailDictionnaireRapportCom)ejbFacadeDetailDictionnaireRapportCom.findByParameterMultipleCreteriaObject(q,ss,oo);
             DetailRapportCom det=rapportdetails.get(rapportdetailstable.getRowIndex());
              det.setExpression(p.getExpressionLibelle());
                 System.out.println("L'expression de "+rapportdetailstable.getRowIndex()+ "est"+p.getExpressionLibelle() );
              rapportdetails.set(rapportdetailstable.getRowIndex(),det);
            }
         public void styleselectedlistner()
         {
        
             int ligne=rapportdetailstable.getRowIndex();
        List<String> get = Style.get(ligne);
             System.out.println("get "+get);
         if (get.contains("Bold"))
         {DetailRapportCom  rapportdet=rapportdetails.get(ligne);
         rapportdet.setGras(Boolean.TRUE);
         rapportdetails.set(ligne, rapportdet);
         }
           if (get.contains("Italic"))
           {DetailRapportCom  rapportdet=rapportdetails.get(ligne);
         rapportdet.setItalique(Boolean.TRUE);
         rapportdetails.set(ligne, rapportdet);}
               if (get.contains("Underline"))
               {DetailRapportCom  rapportdet=rapportdetails.get(ligne);
         rapportdet.setSouligner(Boolean.TRUE);
         rapportdetails.set(ligne, rapportdet);}
         }
      //*****************************************************************************************************************////       
        public void ajouterLigne()
        {int pos = rapportdetailstable.getRowIndex();
        DetailRapportCom rapportdet = new DetailRapportCom();
        rapportdet.setGras(Boolean.FALSE);
             rapportdet.setSouligner(Boolean.FALSE);
               rapportdet.setItalique(Boolean.FALSE);
               rapportdetails.add(pos+1,rapportdet);
           expressionsfixesrapportdetails.add(pos+1,"");
          expressionsinforapportdetails.add(pos+1,""); 
          zonerapportchoice.add(pos+1,"");
             Style.add(new LinkedList<String>());
        }
        public void supprimerLigne()
        {int pos = rapportdetailstable.getRowIndex();
           if (rapportdetails.size()>1)
           { rapportdetails.remove(pos);
           expressionsfixesrapportdetails.remove(pos);
          expressionsinforapportdetails.remove(pos); 
          zonerapportchoice.remove(pos);
           Style.remove(pos);
           }
           else{rapportdetails.remove(pos);
           expressionsfixesrapportdetails.remove(pos);
          expressionsinforapportdetails.remove(pos); 
          zonerapportchoice.remove(pos);
         rapportdetails.add(new DetailRapportCom());
           expressionsfixesrapportdetails.add("");
          expressionsinforapportdetails.add(""); 
          zonerapportchoice.add("");  
            Style.add(new LinkedList<String>());
           }
        }
}
