package JsfClasses;

import entities.User;
import JsfClasses.util.JsfUtil;
import JsfClasses.util.PaginationHelper;
import SessionBeans.ProfileFacade;
import SessionBeans.UserFacade;
import entities.Profile;
import entities.ProfilePK;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
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
import javax.transaction.UserTransaction;
import org.primefaces.component.datatable.DataTable;

@ManagedBean(name = "userController")
@ViewScoped
public class UserController implements Serializable {

    private User current;
    private DataModel items = null;
    @EJB
    private SessionBeans.UserFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    SelectItem[] nomPagesitems;
    private String page;
     private UserC userConverter;
    private DataTable dataTableModification;
    private Integer pageDestination;
    private Map<String, Object> mapRechercheList;
    private Boolean balancer;
    private DataTable dataTableCreation;
    private List<Profile> listProfiles;
    private List<String> etatsSelectionnees;
    @EJB
    private ProfileFacade ejbProfile;
    private User utilisateurDupplication;
    protected Boolean pageModification;
    protected List<String> listNomPages;
    protected List<String> listNomEtats;
  @Resource
    private UserTransaction utx = null;
    public Map<String, Object> getMapRechercheList() {
        return mapRechercheList;
    }

    public void setMapRechercheList(Map<String, Object> mapRechercheList) {
        this.mapRechercheList = mapRechercheList;
    }

    public Integer getPageDestination() {
        return pageDestination;
    }
     public UserC getUserConverter() {
        return userConverter;
    }

    public void setUserConverter(UserC userConverter) {
        this.userConverter = userConverter;
    }

    public void setPageDestination(Integer pageDestination) {
        this.pageDestination = pageDestination;
    }
 public List<User> autocompleteLoginUtilisateur(String code) {
        List<User> result = new ArrayList<User>();
        try {
            result = (List<User>) getLikeLoginUser(code);
            return result;
        } catch (Exception d) {
            result = new ArrayList<User>();
            return result;
        }
    }
  public List<User> getLikeLoginUser(String code) {
        String q = "Select u from User u where u.login like :login";
        List<User> l = this.ejbFacade.findByParameterAutocomplete(q, "login", code + "%", 10);
        return l;
    }
    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();

        try {
            page = (String) map.get("page");
        } catch (Exception e) {
        }
        try {
            utilisateurDupplication = (User) map.get("utilisateurDupplication");
        } catch (Exception e) {
        }
        try {
            current = (User) map.get("current");
        } catch (Exception e) {
            current = null;
        }
        try {
            items = (DataModel) map.get("items");
        } catch (Exception e) {
        }

        try {
            mapRechercheList = (Map<String, Object>) map.get("mapRechercheList");
        } catch (Exception e) {
            mapRechercheList = null;
        }
        try {
            dataTableModification = (DataTable) map.get("dataTableModification");
        } catch (Exception e) {
        }
        try {
            balancer = (Boolean) map.get("balancer");
        } catch (Exception e) {
        }
        try {
            listProfiles = (List<Profile>) map.get("listProfiles");
        } catch (Exception e) {
        }
        try {
            dataTableCreation = (DataTable) map.get("dataTableCreation");
        } catch (Exception e) {
        }
        try {
            dataTableModification = (DataTable) map.get("dataTableModification");
        } catch (Exception e) {
        }
        try {
            etatsSelectionnees = (List<String>) map.get("etatsSelectionnees");
        } catch (Exception e) {
        }
        try {
            pageModification = (Boolean) map.get("pageModification");
        } catch (Exception e) {
        }
        getNomPagesItems();
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
        current = new User();
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

    public UserController() {
            FacesContext facesContext = FacesContext.getCurrentInstance();
        ejbProfile = (ProfileFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "profileEjbJpa");   
        ejbFacade = (UserFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "userJpa");
       
             
    }

    public User getSelected() {
        if (current == null) {
            current = new User();
            selectedItemIndex = -1;
        }
        return current;
    }

    public UserFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            List<String> s = new ArrayList<String>();
            List<Object> o = new ArrayList<Object>();
            pagination = new PaginationHelper(10, "Select o from User o", s, o) {

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

    public String rechercherListItems() {
        String requette = "Select o from User o where ";
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
        recreateModel();
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        this.mapRechercheList = new HashMap<String, Object>();
        this.utilisateurDupplication = new User();
        dataTableModification = new DataTable();
        this.etatsSelectionnees = new ArrayList<String>();

        page = "List";
        map.put("page", page);
        map.put("current", current);
        map.put("items", items);
        map.put("mapRechercheList", mapRechercheList);
        map.put("utilisateurDupplication", utilisateurDupplication);
        map.put("dataTableCreation", dataTableCreation);
        map.put("dataTableModification", dataTableModification);
        map.put("etatsSelectionnees", etatsSelectionnees);
        map.put("pageModification", pageModification);
        return "List_user";
    }

    public String prepareView() {
        current = (User) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        page = "Create";
        dataTableModification = new DataTable();
        dataTableCreation = new DataTable();
        this.etatsSelectionnees = new ArrayList<String>();
        balancer = false;
        listProfiles = new ArrayList<Profile>();
        this.utilisateurDupplication = new User();

        Profile p1 = new Profile();
        ProfilePK pkPr = new ProfilePK();
        pkPr.setTableinterne("");
        p1.setProfilePK(pkPr);
        p1.setAjouter("Non");
        p1.setSupprimer("Non");
        p1.setModifier("Non");
        p1.setUser1(current);
        listProfiles.add(p1);
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String, Object> map = fc.getExternalContext().getRequestMap();
        map.put("page", page);
        map.put("current", current);
        map.put("balancer", balancer);
        map.put("listProfiles", listProfiles);
        map.put("utilisateurDupplication", utilisateurDupplication);
        map.put("dataTableCreation", dataTableCreation);
        map.put("dataTableModification", dataTableModification);
        map.put("etatsSelectionnees", etatsSelectionnees);
        map.put("pageModification", pageModification);
        return "Create_user";
    }

    public String create() {
        try {
           
          utx.begin();
            Integer l = new Integer(1);
            try {
                l = getFacade().findByParameterSingleResultCountsansparamInteger("Select max(c.id) from User c");
                if (l == null) {
                    l = new Integer(1);
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
            Integer intId=l+1;
            current.setId(l + 1);
            getFacade().create(current);
            Iterator it = this.listProfiles.iterator();
            System.out.println("avant boucle");
            while (it.hasNext()) {
                Profile p = (Profile) it.next();
             //   System.out.println("p " +p.getAjouter()+p.getModifier()+p.getSupprimer()+p.getUser1().getLogin()+p.getProfilePK().getLogiciel());
                  if ((p.getProfilePK().getTableinterne() != null)) {
                    Profile newProf = new Profile();
                    newProf.setAjouter(p.getAjouter());
                    newProf.setModifier(p.getModifier());
                    newProf.setSupprimer(p.getSupprimer());
                    newProf.setUser1(current);
                    ProfilePK newProfPK = new ProfilePK();
                    newProfPK.setTableinterne(p.getProfilePK().getTableinterne());
                   newProfPK.setUser(intId);
                    newProfPK.setLogiciel("Cabinet");
                    newProf.setProfilePK(newProfPK);
//                    System.out.println("user id  "+newProf.getProfilePK().getUser()+" / table "+newProf.getProfilePK().getTableinterne()+
//                            " / logiciel "+newProf.getProfilePK().getLogiciel());
//                    System.out.println("user id2  "+newProf.getUser1().getId());
                    this.ejbProfile.create(newProf);
                }
            }
            System.out.println("fin boucle");
              Iterator itEtats = this.etatsSelectionnees.iterator();
            while (itEtats.hasNext()) {
                String pitEtats = (String) itEtats.next();
                Profile profileEtat = new Profile();
                profileEtat.setAjouter("a");
                profileEtat.setModifier("a");
                profileEtat.setSupprimer("a");
                profileEtat.setUser1(current);
                ProfilePK profileEtatPk = new ProfilePK();
                profileEtatPk.setTableinterne(pitEtats);
               profileEtatPk.setUser(intId);
                profileEtatPk.setLogiciel("Cabinet");
                profileEtat.setProfilePK(profileEtatPk);
                this.ejbProfile.create(profileEtat);
            }
           utx.commit();
            JsfUtil.addSuccessMessage("Transaction reussi");
            return prepareCreate();
        } catch (Exception e) {
            System.out.println("mon catch");
            e.printStackTrace();
            JsfUtil.addErrorMessage("Transaction echouee");
            return null;
        }
    }

    public String prepareEdit() {
        current = (User) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        listProfiles = new ArrayList<Profile>();
        dataTableModification = new DataTable();
        this.utilisateurDupplication = new User();
        dataTableCreation = new DataTable();
        pageModification = true;
           List<String> listSS = new ArrayList<String>();
        listSS.add("user1");
        List<Object> listOO = new ArrayList<Object>();
        listOO.add(current);
  this.etatsSelectionnees = new ArrayList<String>();
        Iterator lpp = this.ejbProfile.findByParameterMultipleCreteriaMultipleObject("Select p.profilePK.tableinterne from Profile p where p.ajouter='a' and p.user1=:user1 and p.profilePK.logiciel='Cabinet'", listSS, listOO).iterator();
        while (lpp.hasNext()) {
            String nomEtats = (String) lpp.next();
            this.etatsSelectionnees.add(nomEtats);
        }
        try {
            if (this.etatsSelectionnees.isEmpty()) {
                this.etatsSelectionnees = new ArrayList<String>();
            }
        } catch (Exception er87) {
            this.etatsSelectionnees = new ArrayList<String>();
        }
        
        listProfiles = this.ejbProfile.findByParameter("Select p from Profile p where p.user1=:user1 and p.ajouter!='a' and p.profilePK.logiciel='Cabinet'", "user1", current);
        try {
            if (listProfiles.isEmpty()) {
                listProfiles = new ArrayList<Profile>();
                Profile p1 = new Profile();
                ProfilePK prodilePK = new ProfilePK();
                prodilePK.setTableinterne("");
                p1.setProfilePK(prodilePK);
                p1.setAjouter("Non");
                p1.setSupprimer("Non");
                p1.setModifier("Non");
                p1.setUser1(current);
                listProfiles.add(p1);
            }
        } catch (Exception erre) {
            listProfiles = new ArrayList<Profile>();
            Profile p1 = new Profile();
            ProfilePK prodilePK = new ProfilePK();
            prodilePK.setTableinterne("");
            p1.setProfilePK(prodilePK);
            p1.setAjouter("Non");
            p1.setSupprimer("Non");
            p1.setModifier("Non");
            p1.setUser1(current);
            listProfiles.add(p1);
        }
        page = "Edit";
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("page", page);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("current", current);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("balancer", balancer);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("listProfiles", listProfiles);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("utilisateurDupplication", utilisateurDupplication);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("dataTableCreation", dataTableCreation);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("dataTableModification", dataTableModification);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("etatsSelectionnees", etatsSelectionnees);
        FacesContext.getCurrentInstance().getExternalContext().getRequestMap().put("pageModification", pageModification);
        return "Edit_user";
    }

    public String update() {
        try { 
          
            utx.begin();
            getFacade().edit(current);
           
              System.out.println("user est "+current.getLogin());
            String query = "Delete from Profile p where p.user1.id=" + this.current.getId() + " and p.profilePK.logiciel='Cabinet'";
            Iterator it = this.listProfiles.iterator();
            while (it.hasNext()) {
                System.out.println("while 1");
                Profile p = (Profile) it.next();
                if (p.getProfilePK().getTableinterne() != null) {
                    String tabelInterne = p.getProfilePK().getTableinterne();
                    try {
                        Profile ppro = this.ejbProfile.findByParameterSingleResult("Select p from Profile p where p.user1=:user1 and p.profilePK.logiciel='Cabinet' and "
                                + "p.profilePK.tableinterne='" + tabelInterne + "'", "user1", this.current);
                        this.ejbProfile.edit(p);
                    } catch (Exception e) {
                        Profile p8 = new Profile();
                        p8.setAjouter(p.getAjouter());
                        p8.setModifier(p.getModifier());
                        p8.setSupprimer(p.getSupprimer());
                        p8.setUser1(current);
                        ProfilePK p8PK = new ProfilePK();
                        p8PK.setLogiciel("Cabinet");
                        p8PK.setTableinterne(tabelInterne);
                        p8PK.setUser(this.current.getId());
                        p8.setProfilePK(p8PK);
                        this.ejbProfile.create(p8);
                    }
                    query = query + " and p.profilePK.tableinterne!='" + tabelInterne + "'";
                }
            }
            /////////////////////////
           
            
            Iterator itEtats = this.etatsSelectionnees.iterator();
            while (itEtats.hasNext()) {
                String pitEtats = (String) itEtats.next();
                System.out.println("les etats ");
                try {
                    Profile prro = this.ejbProfile.findByParameterSingleResult("Select p from Profile p where p.user1=:user1 and p.profilePK.logiciel='Cabinet' and "
                            + "p.profilePK.tableinterne='" + pitEtats + "'", "user1", this.current);
                    System.out.println("prro "+prro.getProfilePK().getTableinterne());
                    //   this.ejbProfile.edit(prro);
                } catch (Exception e) {
                    Profile prro = new Profile();
                    prro.setAjouter("a");
                    prro.setModifier("a");
                    prro.setSupprimer("a");
                    prro.setUser1(current);
                    ProfilePK prro8PK = new ProfilePK();
                    prro8PK.setLogiciel("Cabinet");
                    prro8PK.setTableinterne(pitEtats);
                    prro8PK.setUser(this.current.getId());
                    prro.setProfilePK(prro8PK);
                    this.ejbProfile.create(prro);
                }
                query = query + " and p.profilePK.tableinterne!='" + pitEtats + "'";
            }
            System.out.println("suppression de :" + this.ejbProfile.executerRemoveInstruction(query, new ArrayList<String>(), new ArrayList<Object>()));
           
            utx.commit();
            JsfUtil.addSuccessMessage("Utilisateur Modifie");
            return this.prepareList();
        }catch (Exception e) {
            e.printStackTrace();
            try {
                utx.rollback();
            } catch (Exception exp) {
                exp.printStackTrace();
            }
            JsfUtil.addErrorMessage("Operation Echouee");
        }
        
        return null;
    }
   public String destroy() {
        try {
            utx.begin();
            current = (User) getItems().getRowData();
            List<String> sss = new ArrayList<String>();
            List<Object> ooo = new ArrayList<Object>();
            sss.add("uuser");
            ooo.add(current);
             System.out.println("Profile: " + (this.ejbProfile.executerRemoveInstruction("Delete from Profile p where p.user1=:uuser", sss, ooo)));		
            this.getFacade().remove(current);
            utx.commit();
            JsfUtil.addSuccessMessage("Utilisateur Supprime");
        } catch (Exception e) {
            try {
                utx.rollback();
            } catch (Exception exp) {
            }
            JsfUtil.addErrorMessage("Operation Echouee");
        }
        recreateModel();
        this.prepareList();
        return null;
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

   @FacesConverter(forClass = User.class)
    public static class UserControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
        try{    UserController controller = (UserController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "userController");
            return controller.ejbFacade.find(getKey(value));}catch(Exception e){return null;}
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
            if (object instanceof User) {
                User o = (User) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + UserController.class.getName());
            }
        }
    }
    public void balancerAction() {
        if (balancer == false) {
            balancerNon();
        } else {
            balancerOui();
        }

    }

    public String balancerOui() {

        System.out.println("size list profile " + listProfiles.size());
        Iterator it = listProfiles.iterator();
        while (it.hasNext()) {
            Profile p = (Profile) it.next();
            if (p.getProfilePK().getTableinterne() != null) {
                p.setAjouter("Oui");
                p.setModifier("Oui");
                p.setSupprimer("Oui");

            }
        }

        return null;
    }

    public String balancerNon() {

        System.out.println("size list profile " + listProfiles.size());
        Iterator it = listProfiles.iterator();
        while (it.hasNext()) {
            Profile p = (Profile) it.next();
            if (p.getProfilePK().getTableinterne() != null) {
                p.setAjouter("Non");
                p.setModifier("Non");
                p.setSupprimer("Non");

            }
        }

        return null;
    }

    public String duppliquerUser() {
        if (this.utilisateurDupplication != null) {
            try {

                if (utilisateurDupplication.getAdministrateur().equalsIgnoreCase("Oui")) {
                    System.out.println("util duplique" + utilisateurDupplication.getLogin());
                    selectionnerTout();
                } else {
                    try {
                        this.current.setAdministrateur(utilisateurDupplication.getAdministrateur());
                        List<Profile> listPages = this.ejbProfile.findByParameter("Select p from Profile p where p.user1=:user1 and p.ajouter!='a' and p.modifier!='a' and p.profilePK.logiciel='Cabinet' and p.supprimer!='a'", "user1", this.utilisateurDupplication);
                        if (listPages.isEmpty() == false) {
                            this.listProfiles = listPages;
                        } else {
                            this.listProfiles = new ArrayList<Profile>();
                            Profile prf = new Profile();
                            prf.setUser1(current);
                            ProfilePK proPK = new ProfilePK();
                            proPK.setTableinterne("");
                            prf.setProfilePK(proPK);
                            prf.setAjouter("Non");
                            prf.setModifier("Non");
                            prf.setSupprimer("Non");
                            this.listProfiles.add(new Profile());
                        }
                        List<Profile> listEtats = this.ejbProfile.findByParameter("Select p from Profile p where p.user1=:user1 and p.ajouter='a' and p.modifier='a' and p.supprimer='a'", "user1", this.utilisateurDupplication);
                        this.etatsSelectionnees = new ArrayList<String>();
                        Iterator itListEtats = listEtats.iterator();
                        while (itListEtats.hasNext()) {
                            Profile ppp = (Profile) itListEtats.next();
                            this.etatsSelectionnees.add(ppp.getProfilePK().getTableinterne());
                        }

                    } catch (Exception e) {
                        //      e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            JsfUtil.addErrorMessage("utilisateur duppliqué non identifié");
        }
        return "";
    }

    public String getNomTable() {
        try {
            if (this.pageModification) {
                return this.listProfiles.get(dataTableModification.getRowIndex()).getProfilePK().getTableinterne();
            } else {
                return this.listProfiles.get(dataTableCreation.getRowIndex()).getProfilePK().getTableinterne();
            }
        } catch (Exception e) {
            //      e.printStackTrace();
            return "---";
        }
    }

    public void setNomTable(String nomTable) {
        try {
            if (this.pageModification) {
                this.listProfiles.get(dataTableModification.getRowIndex()).getProfilePK().setTableinterne(nomTable);
            } else {
                this.listProfiles.get(dataTableCreation.getRowIndex()).getProfilePK().setTableinterne(nomTable);
            }
        } catch (Exception e) {
            //      e.printStackTrace();
        }
    }

    public String initialiserTout() {
        listProfiles = new ArrayList<Profile>();

        Profile p1 = new Profile();
        p1.setAjouter("Non");
        p1.setSupprimer("Non");
        p1.setModifier("Non");
        p1.setUser1(current);
        ProfilePK pPKp = new ProfilePK();
        pPKp.setTableinterne("");
        p1.setProfilePK(pPKp);
        listProfiles.add(p1);
        this.etatsSelectionnees = new ArrayList<String>();
        this.utilisateurDupplication = new User();

        return null;
    }

    public List<String> getListNomPages() {
        if (listNomPages == null) {
            listNomPages = new ArrayList<String>();
            listNomPages.add("Patient");
            listNomPages.add("Images Patient");
            listNomPages.add("Rendez Vous");
            listNomPages.add("Antecedant");
        }
        return listNomPages;
    }

    public List<String> getListNomEtats() {
        if (listNomEtats == null) {
            listNomEtats = new ArrayList<String>();


            listNomEtats.add("etat Cabinet");
             listNomEtats.add("etat Patient");
               listNomEtats.add("etat Rendez vous");
        }
        return listNomEtats;
    }

    public String selectionnerTout() {
        listProfiles = new ArrayList<Profile>();
        this.etatsSelectionnees = new ArrayList<String>();
        Iterator ittt = this.getListNomPages().iterator();
        while (ittt.hasNext()) {
            String stt = (String) ittt.next();
            Profile prf = new Profile();
            prf.setAjouter("Oui");
            prf.setModifier("Oui");
            prf.setSupprimer("Oui");
            ProfilePK proPK = new ProfilePK();
            proPK.setTableinterne(stt);
            prf.setUser1(current);
            prf.setProfilePK(proPK);
            this.listProfiles.add(prf);
        }
        Iterator itttEtat = this.getListNomEtats().iterator();
        while (itttEtat.hasNext()) {
            String stt = (String) itttEtat.next();
            this.etatsSelectionnees.add(stt);
        }

        return null;
    }

    public void ajouterLigneProfileEditListener() {
        if (this.dataTableModification.getRowIndex() == this.listProfiles.size() - 1) {
            int i = 0;
            while (i < 1) {
                Profile prf = new Profile();
                prf.setAjouter("Non");
                prf.setModifier("Non");
                prf.setSupprimer("Non");
                prf.setUser1(current);
                ProfilePK pkPK = new ProfilePK();
                pkPK.setTableinterne("");
                prf.setProfilePK(pkPK);
                this.listProfiles.add(prf);
                i++;
            }
        } else {
            Profile prf = new Profile();
            prf.setAjouter("Non");
            prf.setModifier("Non");
            prf.setSupprimer("Non");
            prf.setUser1(current);
            ProfilePK pkPK = new ProfilePK();
            pkPK.setTableinterne("");
            prf.setProfilePK(pkPK);
            this.listProfiles.add(this.dataTableModification.getRowIndex() + 1, prf);
        }
    }

    public void ajouterLigneProfileCreateListener() {
        if (this.dataTableCreation.getRowIndex() == this.listProfiles.size() - 1) {
            int i = 0;
            while (i < 1) {
                Profile prf = new Profile();
                prf.setAjouter("Non");
                prf.setModifier("Non");
                prf.setSupprimer("Non");
                prf.setUser1(current);
                ProfilePK pkPK = new ProfilePK();
                pkPK.setTableinterne("");
                prf.setProfilePK(pkPK);
                this.listProfiles.add(prf);
                i++;
            }
        } else {
            Profile prf = new Profile();
            prf.setAjouter("Non");
            prf.setModifier("Non");
            prf.setSupprimer("Non");
            prf.setUser1(current);
            ProfilePK pkPK = new ProfilePK();
            pkPK.setTableinterne("");
            prf.setProfilePK(pkPK);
            this.listProfiles.add(this.dataTableCreation.getRowIndex() + 1, prf);
        }
    }

    public void supprimerLigneProfileEditListener() {

        int i = this.dataTableModification.getRowIndex();

        listProfiles.remove(i);
        if (listProfiles.isEmpty()) {
            Profile dt = new Profile();
            Profile prf = new Profile();
            prf.setAjouter("Non");
            prf.setModifier("Non");
            prf.setSupprimer("Non");
            prf.setUser1(current);
            ProfilePK pkPK = new ProfilePK();
            pkPK.setTableinterne("");
            prf.setProfilePK(pkPK);
            listProfiles.add(dt);
        }
    }

    public void supprimerLigneProfileCreateListener() {

        int i = this.dataTableCreation.getRowIndex();
        listProfiles.remove(i);
        if (listProfiles.isEmpty()) {
            Profile dt = new Profile();
            Profile prf = new Profile();
            prf.setAjouter("Non");
            prf.setModifier("Non");
            prf.setSupprimer("Non");
            prf.setUser1(current);
            ProfilePK pkPK = new ProfilePK();
            pkPK.setTableinterne("");
            prf.setProfilePK(pkPK);
            listProfiles.add(dt);
        }
    }

    public List<Profile> getListProfiles() {
        return listProfiles;
    }

    public void setListProfiles(List<Profile> listProfiles) {
        this.listProfiles = listProfiles;
    }

    public User getUtilisateurDupplication() {
        return utilisateurDupplication;
    }

    public void setUtilisateurDupplication(User utilisateurDupplication) {
        this.utilisateurDupplication = utilisateurDupplication;
    }

    public DataTable getDataTableCreation() {
        return dataTableCreation;
    }

    public void setDataTableCreation(DataTable dataTableCreation) {
        this.dataTableCreation = dataTableCreation;
    }

    public DataTable getDataTableModification() {
        return dataTableModification;
    }

    public void setDataTableModification(DataTable dataTableModification) {
        this.dataTableModification = dataTableModification;
    }

    public Boolean getBalancer() {
        return balancer;
    }

    public void setBalancer(Boolean balancer) {
        this.balancer = balancer;
    }

    public Boolean getPageModification() {
        return pageModification;
    }

    public void setPageModification(Boolean pageModification) {
        this.pageModification = pageModification;
    }

    public List<String> getEtatsSelectionnees() {
        return etatsSelectionnees;
    }

    public void setEtatsSelectionnees(List<String> etatsSelectionnees) {
        this.etatsSelectionnees = etatsSelectionnees;
    }

    public SelectItem[] getEtatsItems() {
        Iterator it = this.getListNomEtats().iterator();
        int size = this.getListNomEtats().size();
        int in = 0;
        SelectItem[] resultat = new SelectItem[size];
        while (it.hasNext()) {
            String stt = (String) it.next();
            SelectItem st = new SelectItem(stt, stt);
            resultat[in] = st;
            in++;
        }
        return resultat;
    }

    public SelectItem[] getNomPagesItems() {
        Iterator i = this.getListNomPages().iterator();
        int size = this.getListNomPages().size();
        SelectItem[] result = new SelectItem[size + 1];
        int ii = 0;
        result[0] = new SelectItem("", "---");
        ii++;
        while (i.hasNext()) {
            String ss = (String) i.next();
            SelectItem sl = new SelectItem(ss, ss);
            result[ii] = sl;
            ii++;
        }
        return result;
    }
}
