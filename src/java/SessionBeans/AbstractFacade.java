/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBeans;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gassen
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public String urlCourante(){
        String url="";
        String password="";
        String user="";
        String driver="";
        Map m=getEntityManager().getEntityManagerFactory().getProperties();
        url=m.get("javax.persistence.jdbc.url").toString();
        password=m.get("javax.persistence.jdbc.password").toString();
        user=m.get("javax.persistence.jdbc.user").toString();
        String s=url+"**"+user+"**"+password;
        return s;
    }
    
    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }
   public Long findByParameterSingleResultCount(String query,String nomp,Object valeur) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
        q.setParameter(nomp,valeur);
        return (Long) q.getSingleResult();
    }
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
     public Long findByParameterSingleResultCountsansparam(String query) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
     
        return (Long) q.getSingleResult();
    }
        public BigDecimal findByParameterSingleResultCountsansparam1(String query) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
     
        return (BigDecimal) q.getSingleResult();
    }
       public Integer findByParameterSingleResultCountsansparamInteger(String query) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
     
        return (Integer) q.getSingleResult();
    }
     
     
      public List<T> findByParameterMultipleCreteria(String query,List<String> nomp,List<Object> valeur) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
        Iterator i=nomp.iterator();
        Iterator ii=valeur.iterator();
        while(i.hasNext()){
        q.setParameter((String)i.next(),(Object)ii.next());
        }
        return q.getResultList();
    }
       public List<T> findByParameterMultipleCreteria(String query,List<String> nomp,List<Object> valeur,Integer begin,Integer size) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
        Iterator i=nomp.iterator();
        Iterator ii=valeur.iterator();
        while(i.hasNext()){
        q.setParameter((String)i.next(),(Object)ii.next());
        }
        q.setFirstResult(begin);
        q.setMaxResults(size);
        return q.getResultList();
    }
        public T findByParameterSingleResultMultipleCreteria(String query,List<String> nomp,List<Object> valeur) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
        Iterator i=nomp.iterator();
        Iterator ii=valeur.iterator();
        while(i.hasNext()){
        q.setParameter((String)i.next(),(Object)ii.next());
        }
        return (T) q.getSingleResult();
    }
           public List<T> execCommandeList2Param(String query,String parameter,Object valeur,String parameter1,Object valeur1){
        Query q=this.getEntityManager().createQuery(query);
        q.setParameter(parameter,valeur);
        q.setParameter(parameter1,valeur1);
      
   
        return (List<T>) q.getResultList();
    } 
         public List<T> execCommandeList3Param(String query,String parameter,Object valeur,String parameter1,Object valeur1,String parameter2,Object valeur2){
        Query q=this.getEntityManager().createQuery(query);
        q.setParameter(parameter,valeur);
        q.setParameter(parameter1,valeur1);
        q.setParameter(parameter2,valeur2);
   
        return (List<T>) q.getResultList();
    } 
       public T execCommandeSansParam(String query){
        Query q=this.getEntityManager().createQuery(query);
        return (T) q.getSingleResult();
    } 
      public int countMultipleCritiria(String query,List<String>s,List<Object>o) {
        String apresFrom=query.substring(query.indexOf("from"));
        String select=query.substring(0,query.indexOf("from"));
        String alias=select.substring(6,select.length());
        String newChaineQuery="Select count("+alias+")"+apresFrom;
        System.out.println("newChaineQuery "+newChaineQuery);
        javax.persistence.Query q = getEntityManager().createQuery(newChaineQuery);
        Iterator i=s.iterator();
        int x=0;
        while(i.hasNext()){
            String ss=(String)i.next();
            Object oo=o.get(x);
            q.setParameter(ss,oo);
            x++;
        }
        return ((Long) q.getSingleResult()).intValue();
    }
          public List<T> findByParameterAutocomplete(String query,String nomp,Object valeur,Integer nb) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
       q.setMaxResults(nb);
        q.setParameter(nomp,valeur);
        return q.getResultList();
    }
            public T findByParameterSingleResult(String query,String nomp,Object valeur) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
        q.setParameter(nomp,valeur);
        return (T) q.getSingleResult();
    }
           
               public List<T> findByParameterMultipleCreteriaAutocomplete(String query,List<String> nomp,List<Object> valeur,Integer nb) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
        q.setMaxResults(nb);
        Iterator i=nomp.iterator();
        Iterator ii=valeur.iterator();
        while(i.hasNext()){
        q.setParameter((String)i.next(),(Object)ii.next());
        }
        return q.getResultList();
    }
               
                public List<T> execCommandeList4Param(String query,String parameter,Object valeur,String parameter1,Object valeur1,String parameter2,Object valeur2,String parameter3,Object valeur3){
        Query q=this.getEntityManager().createQuery(query);
        q.setParameter(parameter,valeur);
        q.setParameter(parameter1,valeur1);
        q.setParameter(parameter2,valeur2);
    q.setParameter(parameter3,valeur3);
       
       
        return (List<T>) q.getResultList();
    } 
                public List<Object> findByParameterMultipleCreteriaMultipleObject(String query,List<String> nomp,List<Object> valeur) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
        Iterator i=nomp.iterator();
        Iterator ii=valeur.iterator();
        while(i.hasNext()){
        q.setParameter((String)i.next(),(Object)ii.next());
        }
        return q.getResultList();
    }
             public BigDecimal findByParameterSum(String query,String nomp,Object valeur) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
        q.setParameter(nomp,valeur);
        return (BigDecimal) q.getSingleResult();
    }       
                 public Object findByParameterMultipleCreteriaObject(String query,List<String> nomp,List<Object> valeur) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
        Iterator i=nomp.iterator();
        Iterator ii=valeur.iterator();
        while(i.hasNext()){
        q.setParameter((String)i.next(),(Object)ii.next());
        }
        return q.getSingleResult();
    }
                
                public List<T> execCommandeList(String query,String parameter,Object valeur){
        Query q=this.getEntityManager().createQuery(query);
        q.setParameter(parameter,valeur);
        return (List<T>) q.getResultList();
    } 
                    public int executerRemoveInstruction(String queryString,List<String>ss,List<Object>oo){
         Query query = this.getEntityManager().createQuery(queryString);
         Iterator i=ss.iterator();
         Integer x=new Integer(0);
         while(i.hasNext()){
             query.setParameter((String)i.next(),oo.get(x));
             x++;
         }
         int deletedCount = query.executeUpdate();
         return deletedCount;
     }
                      public List<T> findByParameter(String query,String nomp,Object valeur) {
        javax.persistence.Query q = getEntityManager().createQuery(query);
        q.setParameter(nomp,valeur);
        return q.getResultList();
    }
}
