package JsfClasses.util;

//package jsfClasses.util;
//
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.StringTokenizer;
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import javax.ejb.EJB;
//import javax.faces.bean.ManagedProperty;
//import javax.faces.component.UIComponent;
//import javax.faces.component.UIPanel;
//import javax.faces.component.UIViewRoot;
//import javax.faces.component.html.HtmlInputText;
//import javax.faces.context.FacesContext;
//import javax.faces.convert.Converter;
//import javax.faces.model.DataModel;
//import javax.faces.model.ListDataModel;
//import javax.persistence.Id;
//import javax.transaction.SystemException;
//import javax.transaction.UserTransaction;
//import org.primefaces.component.autocomplete.AutoComplete;
//import SessionBeans.AbstractFacade;
//import JsfClasses.util.PaginationHelper;
//public  class GenericOccurence<T>{
//    private Class<T> entityClass;
//    private String nomEjbFacadeEntity;
//    private T current;
//    private DataModel items = null;
//    private PaginationHelper pagination;
//    @Resource
//    private UserTransaction utx=null;
//    @ManagedProperty(value="#{authetification}")
//    private Boolean boutonAjouter;
//    private Boolean boutonModifier;
//    private Boolean boutonSupprimer;
//    private Boolean boutonEtat;
//    private AbstractFacade ejbFacade;
//    private Map<String,Object>mapRechercheList;
// 
//    public GenericOccurence(Class<T> entityClass,String nomEjbFacadeEntity) {
//        this.entityClass = entityClass;
//        this.nomEjbFacadeEntity = nomEjbFacadeEntity;
//        FacesContext facesContext=FacesContext.getCurrentInstance();
//        ejbFacade = (AbstractFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null,this.nomEjbFacadeEntity);
//    }
//    public Map<String, Object> getMapRechercheList() {
//        return mapRechercheList;
//    }
//    public void setMapRechercheList(Map<String, Object> mapRechercheList) {
//        this.mapRechercheList = mapRechercheList;
//    }
//   
//    public Boolean getBoutonAjouter() {
//        return boutonAjouter;
//    }
//    public void setBoutonAjouter(Boolean boutonAjouter) {
//        this.boutonAjouter = boutonAjouter;
//    }
//    public Boolean getBoutonEtat() {
//        return boutonEtat;
//    }
//    public void setBoutonEtat(Boolean boutonEtat) {
//        this.boutonEtat = boutonEtat;
//    }
//    public Boolean getBoutonModifier() {
//        return boutonModifier;
//    }
//    public void setBoutonModifier(Boolean boutonModifier) {
//        this.boutonModifier = boutonModifier;
//    }
//    public Boolean getBoutonSupprimer() {
//        return boutonSupprimer;
//    }
//    public void setBoutonSupprimer(Boolean boutonSupprimer) {
//        this.boutonSupprimer = boutonSupprimer;
//    }
//    public T getSelected() {
//        return current;
//    }
//    public void setSelected(T current) {
//        this.current = current;
//    }
//    public AbstractFacade getFacade(){
//        return this.ejbFacade;
//    }
//    public PaginationHelper getPagination() {
//        if (pagination == null) {
//            List<String>s=new ArrayList<String>();
//            List<Object>o=new ArrayList<Object>();
//            pagination = new PaginationHelper(10,"Select o from "+entityClass.getSimpleName()+" o",s,o) {
//
//               @Override
//                public int getItemsCount() {
//                    return getFacade().countMultipleCritiria(getRequetteJpql(),getArrayNames(),getArrayValues());
//                }
//
//                @Override
//                public DataModel createPageDataModel() {
//                    return new ListDataModel(getFacade().findByParameterMultipleCreteria(getRequetteJpql(),getArrayNames(),getArrayValues(),getPageFirstItem(),getPageSize()));
//                }
//            };
//
//               
//        }
//        return pagination;
//    }
//    public DataModel getItems() {
//        if (items == null) {
//            items = getPagination().createPageDataModel();
//        }
//        return items;
//    }
//    private void recreateModel() {
//        items = null;
//    }
//    @PostConstruct
//    public void initAfterConstruction(){
//        FacesContext fc=FacesContext.getCurrentInstance();
//        Map<String,Object>map=fc.getExternalContext().getRequestMap();
//        try{
//        boutonAjouter=(Boolean) map.get("boutonAjouter");
//        }catch(Exception e){}
//        try{
//        boutonModifier=(Boolean) map.get("boutonModifier");
//        }catch(Exception e){}
//        try{
//        boutonSupprimer=(Boolean) map.get("boutonSupprimer");
//        }catch(Exception e){}
//        try{
//        boutonEtat=(Boolean) map.get("boutonEtat");
//        }catch(Exception e){}
//        try{
//        items=(DataModel) map.get("items");
//        }catch(Exception e){}
//        try{
//        current=(T) map.get("current");
//        }catch(Exception e){}
//        try{
//        mapRechercheList=(Map<String, Object>) map.get("mapRechercheList");
//        }catch(Exception e){}
//    }
//    public void envoyerParameters(){
//        System.out.println("envoi parametre occurence");
//        FacesContext fc=FacesContext.getCurrentInstance();
//        Map<String,Object>map=fc.getExternalContext().getRequestMap();
//        map.put("boutonAjouter",boutonAjouter);
//        map.put("boutonModifier",boutonModifier);
//        map.put("boutonSupprimer",boutonSupprimer);
//        map.put("boutonEtat",boutonEtat);
//        map.put("items",items);
//        map.put("current",current);      
//        map.put("mapRechercheList",mapRechercheList);
//    }
//     public void initParameters(){
//     
//        
//    }
// 
//    public void setMettreJourAttributeRecherche(String mettreJourAttributeRecherche){
//        FacesContext fc=FacesContext.getCurrentInstance();
//        UIComponent component = UIViewRoot.getCurrentComponent(fc);
//        UIPanel panel=(UIPanel)component;
//        Iterator it=panel.getChildren().iterator();
//        while(it.hasNext()){
//            UIComponent u=(UIComponent)it.next();
//            if(u.getClass().equals(HtmlInputText.class)){
//                this.mapRechercheList.put(u.getId(),mettreJourAttributeRecherche);
//            }
//        }
//    }
//    public String getMettreJourAttributeRecherche(){
//        FacesContext fc=FacesContext.getCurrentInstance();
//        HtmlInputText component = (HtmlInputText) UIViewRoot.getCurrentComponent(fc);
//        return (String) this.mapRechercheList.get(component.getId());
//    }
//    public String rechercherListItems(){
//        String requette="Select o from "+entityClass.getSimpleName()+" o where ";
//        String order="order by ";
//        Iterator it=this.mapRechercheList.entrySet().iterator();
//        List<String>ss=new ArrayList<String>();
//        List<Object>oo=new ArrayList<Object>();
//        while(it.hasNext()){
//            Object o=it.next();
//            Entry<String,Object> e=(Entry<String,Object>)o;
//            String nomComponent=e.getKey();
//            String valueComponent=e.getValue().toString();
//            if(valueComponent!=null){
//                if(valueComponent.isEmpty()==false){   
//                    StringTokenizer st=new StringTokenizer(nomComponent,"_");
//                    if(st.countTokens()==1){
//                        requette=requette+"o."+nomComponent+" like :"+nomComponent+" and ";
//                        ss.add(nomComponent);
//                        oo.add("%"+valueComponent+"%");
//                        order=order+"o."+nomComponent+",";
//                    }else{
//                        String nomCompletColonne="o.";
//                        String lastSuffixColumn="";
//                        while(st.hasMoreTokens()){
//                            String stt=st.nextToken();
//                            nomCompletColonne=nomCompletColonne+stt+".";
//                            lastSuffixColumn=stt;
//                        }
//                        if(nomCompletColonne.endsWith(".")){
//                            nomCompletColonne=nomCompletColonne.substring(0,nomCompletColonne.lastIndexOf("."));
//                        }
//                        requette=requette+nomCompletColonne+" like :"+lastSuffixColumn+" and ";
//                        ss.add(lastSuffixColumn);
//                        oo.add("%"+valueComponent+"%");
//                        order=order+nomCompletColonne+",";
//                    }
//                }
//            }
//        }
//        if(order.endsWith(",")){
//            order=order.substring(0,order.lastIndexOf(","));
//        }
//        if(order.endsWith("order by ")){
//            order=order.substring(0,order.lastIndexOf("order by "));
//        }
//        if(requette.endsWith(" and ")){
//            requette=requette.substring(0,requette.lastIndexOf(" and "));
//        }
//        if(requette.endsWith(" where ")){
//            requette=requette.substring(0,requette.lastIndexOf(" where "));
//        }
//        requette=requette.trim();
//        requette=requette+" "+order;
//        try{
//            getPagination().setRequetteJpql(requette);
//            getPagination().setArrayNames(ss);
//            getPagination().setArrayValues(oo);
//            getPagination().setPage(0);
//            items=null;
//        }catch(Exception ex){
//            
//        }
//        return null;
//    }
//    public String premierePage() {
//        getPagination().setPage(0);
//        recreateModel();
//        return null;
//    }
//    public String dernierePage() {
//        getPagination().setPage(getPagination().totalPages()-1);
//        recreateModel();
//        return null;
//    }
//    public String previous() {
//        this.pageDestination=getPagination().getPage();
//        return goPageDestination();
//    }
//    public String next() {
//        this.pageDestination=getPagination().getPage();
//        this.pageDestination=this.pageDestination+2;
//        return goPageDestination();
//    }
//    public String goPageDestination() {
//        items=null;
//        if(pageDestination!=null){
//            if(pageDestination>0){
//                if(pageDestination<=getPagination().totalPages()){
//                    getPagination().setPage(pageDestination.intValue()-1);
//                    recreateModel();
//                }
//            }
//        }
//        pageDestination=null;
//        return null;
//    }
//    private Integer pageDestination;
//    public Integer getPageDestination() {
//        return pageDestination;
//    }
//    public void setPageDestination(Integer pageDestination) {
//        this.pageDestination = pageDestination;
//    }
//    protected String idColumn;
//    protected String codeColumn;
//    protected String libelleColumn;
//    protected Class classIdColumn;
//    protected Class classCodeColumn;
//    protected Class classLibelleColumn;
//    protected String nomEjbFacade;
//    protected String nomConverter;
//    protected String packageConverter;
// 
//  public Converter obtenirConverter(){
//        try{
//            FacesContext fc=FacesContext.getCurrentInstance();
//            UIComponent component = UIViewRoot.getCurrentComponent(fc);
//            AutoComplete autocompleteComponent=(AutoComplete)component;
//            String idComponent=autocompleteComponent.getId();
//            StringTokenizer st=new StringTokenizer(idComponent,"_");
//            nomEjbFacade=st.nextToken();
//            packageConverter=st.nextToken();
//            nomConverter=st.nextToken();
//            return (Converter) Class.forName(packageConverter+"."+nomConverter).newInstance();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
