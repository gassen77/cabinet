package JsfClasses;

import entities.DetailModeleSortie;
import entities.ModeleSortieFiltrage;
import entities.Modelesortie;
import entities.TypeOpCom;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.transaction.UserTransaction;
import JsfClasses.util.JsfUtil;
import org.primefaces.event.SelectEvent;
import SessionBeans.DetailModeleSortieFacade;
import SessionBeans.ModeleSortieFiltrageFacade;
import SessionBeans.ModelesortieFacade;
import SessionBeans.TypeOpComFacade;

@ManagedBean(name = "duppliquerRapportController")
@ViewScoped
public class DuppliquerRapportController implements Serializable {

    private Modelesortie modele;
    private ModelesortieC modeleSortieConverter;
    private ModelesortieFacade ejbModeleSortie;
    private ModeleSortieFiltrageFacade ejbModeleSortieFiltrage;
    private DetailModeleSortieFacade ejbDetailModeleSortie;
    private Boolean boutonAjouter;
    private Boolean boutonModifier;
    private Boolean boutonSupprimer;
    private Boolean boutonEtat;
    @Resource
    private UserTransaction utx = null;
    private TypeOpComFacade ejbTypeOpCom;
    private String codeRapport;
    private String libelleRapport;
    private Boolean detail;
    private String nouveauTypeRapport;
    private Boolean renderTypeRapport;
    private Boolean renderDetailler;
    private Boolean renderTypeTiersRapport;
    private String typeTiers;

    public DuppliquerRapportController() {
        modeleSortieConverter = new ModelesortieC();
        FacesContext fc = FacesContext.getCurrentInstance();
        ejbModeleSortie = (ModelesortieFacade) fc.getELContext().getELResolver().getValue(fc.getELContext(), null, "modeleSortieJpa");
        ejbModeleSortieFiltrage = (ModeleSortieFiltrageFacade) fc.getELContext().getELResolver().getValue(fc.getELContext(), null, "modeleSortieFiltrageJpa");
        ejbDetailModeleSortie = (DetailModeleSortieFacade) fc.getELContext().getELResolver().getValue(fc.getELContext(), null, "detailModeleSortieJpa");
//        ejbProduitDepotModeleSortie = (ProduitDepotModeleSortieFacade) fc.getELContext().getELResolver().getValue(fc.getELContext(), null, "produitDepotModeleSortieJpa");
        ejbTypeOpCom = (TypeOpComFacade) fc.getELContext().getELResolver().getValue(fc.getELContext(), null, "typeOpComJpa");
    }

    @PostConstruct
    public void initAfterConstructor() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        try {
            modele = (Modelesortie) map.get("modele");
        } catch (Exception e) {
        }
        try {
            boutonAjouter = (Boolean) map.get("boutonAjouter");
        } catch (Exception e) {
        }
        try {
            boutonModifier = (Boolean) map.get("boutonModifier");
        } catch (Exception e) {
        }
        try {
            boutonSupprimer = (Boolean) map.get("boutonSupprimer");
        } catch (Exception e) {
        }
        try {
            boutonEtat = (Boolean) map.get("boutonEtat");
        } catch (Exception e) {
        }
        try {
            codeRapport = (String) map.get("codeRapport");
        } catch (Exception e) {
        }
        try {
            libelleRapport = (String) map.get("libelleRapport");
        } catch (Exception e) {
        }
        try {
            detail = (Boolean) map.get("detail");
        } catch (Exception e) {
        }
        try {
            nouveauTypeRapport = (String) map.get("nouveauTypeRapport");
        } catch (Exception e) {
        }
        try {
            renderTypeRapport = (Boolean) map.get("renderTypeRapport");
        } catch (Exception e) {
        }
        try {
            renderDetailler = (Boolean) map.get("renderDetailler");
        } catch (Exception e) {
        }
        try {
            renderTypeTiersRapport = (Boolean) map.get("renderTypeTiersRapport");
        } catch (Exception e) {
        }
        try {
            typeTiers = (String) map.get("typeTiers");
        } catch (Exception e) {
        }
    }

    public String prepareDuppliquer() {
        this.modele = new Modelesortie();
        this.codeRapport = "";
        this.libelleRapport = "";
        this.detail = false;
        this.nouveauTypeRapport = "";
        this.renderDetailler = false;
        this.renderTypeRapport = false;
        this.renderTypeTiersRapport=false;
        this.typeTiers="";
        this.envoyerParametres();
        return "duppliquerRapport";
    }

    public String accesDupplication() {
   
      
            return "";
        
    }

    public void envoyerParametres() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        map.put("boutonAjouter", boutonAjouter);
        map.put("boutonModifier", boutonModifier);
        map.put("boutonSupprimer", boutonSupprimer);
        map.put("boutonEtat", boutonEtat);
        map.put("modele", modele);
        map.put("codeRapport", codeRapport);
        map.put("libelleRapport", libelleRapport);
        map.put("detail", detail);
        map.put("nouveauTypeRapport", nouveauTypeRapport);
        map.put("renderTypeRapport", renderTypeRapport);
        map.put("renderDetailler", renderDetailler);
        map.put("renderTypeTiersRapport", renderTypeTiersRapport);
        map.put("typeTiers", typeTiers);
    }
    public Boolean getRenderDetailler() {
        return renderDetailler;
    }
    public void setRenderDetailler(Boolean renderDetailler) {
        this.renderDetailler = renderDetailler;
    }
    public Boolean getRenderTypeRapport() {
        return renderTypeRapport;
    }
    public void setRenderTypeRapport(Boolean renderTypeRapport) {
        this.renderTypeRapport = renderTypeRapport;
    }
    public String getNouveauTypeRapport() {
        return nouveauTypeRapport;
    }
    public void setNouveauTypeRapport(String nouveauTypeRapport) {
        this.nouveauTypeRapport = nouveauTypeRapport;
    }
    public String getCodeRapport() {
        return codeRapport;
    }
    public void setCodeRapport(String codeRapport) {
        this.codeRapport = codeRapport;
    }
    public Boolean getDetail() {
        return detail;
    }
    public void setDetail(Boolean detail) {
        this.detail = detail;
    }
    public String getLibelleRapport() {
        return libelleRapport;
    }
    public void setLibelleRapport(String libelleRapport) {
        this.libelleRapport = libelleRapport;
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
    public Modelesortie getModele() {
        return modele;
    }
    public void setModele(Modelesortie modele) {
        this.modele = modele;
    }
    public ModelesortieC getModeleSortieConverter() {
        return modeleSortieConverter;
    }
    public void setModeleSortieConverter(ModelesortieC modeleSortieConverter) {
        this.modeleSortieConverter = modeleSortieConverter;
    }
    public String duppliquer() {
        try {
            utx.begin();
/*            Modelesortie m = this.modele.duppliquer();
        //    m.setIdModeleSortie((Integer) this.clePrimaire.numInterne("cabinet.Modelesortie"));
            m.setCodeModeleSortie(codeRapport);
            if(renderTypeRapport){
                TypeOpCom t=this.ejbTypeOpCom.findByParameterSingleResult("Select t from TypeOpCom t where t.nomTop=:nom","nom",nouveauTypeRapport);
                m.setTypeModele(t);
                m.setCodeModeleSortie("operation_com");
            }
            if(renderTypeTiersRapport){
                m.setCodeModeleSortie("tiers_com");
                m.setTypeTiers(typeTiers);
            }
            if(renderDetailler){
                m.setSolde(detail);
            }
            m.setLibelleModeleSortie(libelleRapport);
            this.ejbModeleSortie.create(m);*/
            List<DetailModeleSortie> l = this.ejbDetailModeleSortie.findByParameter("Select d from DetailModeleSortie d where d.modeleSortie=:modeleSortie", "modeleSortie", modele);
            Iterator it = l.iterator();
            while (it.hasNext()) {
                DetailModeleSortie d = (DetailModeleSortie) it.next();
    //            DetailModeleSortie dNew = d.duppliquer();
//                dNew.setIdDetailModeleSortie((Integer) this.clePrimaire.numInterne(this.authentificationBean.getBaseDonneesChoisi() + ".DetailModeleSortie"));
      //          dNew.setModeleSortie(m);
        //        this.ejbDetailModeleSortie.create(dNew);
            }
            List<ModeleSortieFiltrage> lf = this.ejbModeleSortieFiltrage.findByParameter("Select d from ModeleSortieFiltrage d where d.modeleSortiee=:modeleSortiee", "modeleSortiee", modele);
            Iterator itf = lf.iterator();
            while (itf.hasNext()) {
                ModeleSortieFiltrage mf = (ModeleSortieFiltrage) itf.next();
          //      ModeleSortieFiltrage mfNew = mf.duppliquer();
          //      mfNew.setIdModeleSortieFiltrage((Integer) this.clePrimaire.numInterne(this.authentificationBean.getBaseDonneesChoisi() + ".ModeleSortieFiltrage"));
            //    mfNew.setModeleSortiee(m);
              //  this.ejbModeleSortieFiltrage.create(mfNew);
            }
        //    List<ProduitDepotModeleSortie>lprd=this.ejbProduitDepotModeleSortie.findByParameter("Select d from ProduitDepotModeleSortie d where d.modelesortie=:modelesortie","modelesortie",modele);
     //       Iterator itLprd=lprd.iterator();
//            while(itLprd.hasNext()){
//                ProduitDepotModeleSortie d=(ProduitDepotModeleSortie)itLprd.next();
//                ProduitDepotModeleSortie dNew=new ProduitDepotModeleSortie();
//                dNew.setDepotCom(d.getDepotCom());
//                dNew.setModelesortie(m);
//                dNew.setSizeQttStock(d.getSizeQttStock());
//                ProduitDepotModeleSortiePK dNewPK=new ProduitDepotModeleSortiePK();
//                dNewPK.setIdDepot(d.getDepotCom().getNiDepot());
//                dNewPK.setIdModeleSortie(m.getIdModeleSortie());
//                dNew.setProduitDepotModeleSortiePK(dNewPK);
//                this.ejbProduitDepotModeleSortie.create(dNew);
//            }
            utx.commit();
            JsfUtil.addSuccessMessage("Rapport Dupplique");
            this.prepareDuppliquer();
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Operation Echouee");
            try {
                utx.rollback();
            } catch (Exception ee) {
            }
        }
        return "";
    }

    public List<Modelesortie> autocompleteRapports(String code) {
        List<Modelesortie> result = new ArrayList<Modelesortie>();
        try {
            result = (List<Modelesortie>) getLikeNomRapports(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<Modelesortie>();
            return result;
        }
    }

 public List<Modelesortie> getLikeNomRapports(String code) {
        String q = "Select r from Modelesortie r where r.codeModeleSortie like :lib or r.libelleModeleSortie like :lib";
        List<Modelesortie> l = this.ejbModeleSortie.findByParameterAutocomplete(q, "lib", code + "%", 10);
        return l;
    }
    public Boolean getRenderTypeTiersRapport() {
        return renderTypeTiersRapport;
    }
    public void setRenderTypeTiersRapport(Boolean renderTypeTiersRapport) {
        this.renderTypeTiersRapport = renderTypeTiersRapport;
    }
    public String getTypeTiers() {
        return typeTiers;
    }
    public void setTypeTiers(String typeTiers) {
        this.typeTiers = typeTiers;
    }
    public void handleSelectRapport(SelectEvent event) {
        this.renderDetailler = false;
        this.renderTypeTiersRapport = false;
        this.renderTypeRapport = false;
        Modelesortie m = (Modelesortie) event.getObject();
        if (m != null) {
            this.modele = m;
            if (this.modele.getTypeModele() != null) {
                if (this.modele.getTypeModele().getNomTop() != null) {
                    if (this.modele.getTypeModele().getNomTop().equalsIgnoreCase("Analyse Chiffre Affaire Dyn")) {
                        this.renderDetailler = true;
                    } else {
                        if (this.modele.getTypeModele().getNomTop().equalsIgnoreCase("facture") | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("facture fournisseur")
                                | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("devis") | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("devis fournisseur")
                                | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("livraison") | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("livraison fournisseur")
                                | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("commande") | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("commande fournisseur")
                                | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("Facture Non Reglee Client") | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("Facture Non Reglee Fournisseur")
                                | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("Rapprochement Facture Client") | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("Rapprochement Facture Frns")
                                | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("Rapprochement Reglement Client") | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("Rapprochement Reglement Fr")
                                | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("Bl Non Facturee Client") | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("Bl Non Facturee Fournisseur")
                                | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("Bl Factures Client") | this.modele.getTypeModele().getNomTop().equalsIgnoreCase("Bl Factures Fournisseur")) {
                            this.renderTypeRapport = true;
                        }
                    }
                }else{
                    this.renderTypeTiersRapport = true;
                }
            }else{
                if(this.modele.getTypeTiers()!=null){
                    this.renderTypeTiersRapport = true;
                }
            }
        }
    }
}
