package JsfClasses;
import SessionBeans.UserFacade;
import entities.Profile;
import entities.User;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import JsfClasses.util.JsfUtil;
import SessionBeans.ProfileFacade;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
@ManagedBean(name = "authetification")
@SessionScoped
public class Authetification implements Serializable {

    private User user;
    @EJB
    private SessionBeans.UserFacade ejbFacade;
    @EJB
    private ProfileFacade ejbprofile;
    private Boolean ajouter;
    private Boolean supprimer;
    private Boolean modifier;
    private Boolean pdfConsult;
    protected String baseDonneesChoisi;
    protected String menuChoisi;
    @Resource
    protected UserTransaction utx;
    Boolean flagpatient;
    Boolean flag2patient;
     Boolean flagimagespatient;
    Boolean flag2imagespatient;
     Boolean flagantecedant;
    Boolean flag2antecedant;
    Boolean flagetatcabinet;
     Boolean flagrendezvous;
    Boolean flag2rendezvous;
   
   

    public EntityManager getEntityManager() {
        try {
            Field entityBase = this.getClass().getDeclaredField(this.baseDonneesChoisi);
            return (EntityManager) entityBase.get(this);
        } catch (Exception ex) {
        }
        return null;
    }

    public UserFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(UserFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    public Authetification() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ejbprofile = (ProfileFacade) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "profileEjbJpa");
        this.user = new User();
    }

  

    public String conn() {

        return "indexCommercial";
    }

    public String seConnecter() {
        try {
            String login = user.getLogin();
            String password = user.getPassword();
            User userr = ejbFacade.findByParameterSingleResult("Select u from User u where u.login=:login", "login", login);
            if ((userr.getLogin().equals(login)) & (userr.getPassword().equals(password))) {
                user = userr;
          //      System.out.println("mon user est "+userr.getLogin());
                affichermenu();
                return "pagemenu";
            }
            JsfUtil.addErrorMessage("mot de passe incorrect");
            return "authentification";
        } catch (Exception e) {
            JsfUtil.addErrorMessage("connexion echouee : utilisateur non trouve");
            return "authentification";
        }
    }

    public String deconnexion() {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest) fc.getExternalContext().getRequest();
            StringBuffer urlOrigin = origRequest.getRequestURL();
            String chaineUrlOrigin = urlOrigin.toString();
            String protocol = chaineUrlOrigin.substring(0, chaineUrlOrigin.indexOf(":"));
            ((HttpSession) fc.getExternalContext().getSession(true)).invalidate();
            fc.getExternalContext().redirect(protocol + "://" + origRequest.getServerName() + ":" + origRequest.getServerPort() + "/cabinetdentaire/faces/welcome.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(Authetification.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "authentification";
    }

    public String prepareIndex() {
        user.setLogin("");
        user.setPassword("");
        return "authentification";
    }

    public Boolean renderedTabAdmin() {
        return "Oui".equals(this.user.getAdministrateur());
    }

  

   

    public void securitePageListLogiciels() throws IOException {
        try {
            user.getLogin().length();
        } catch (Exception e) {
            FacesContext fc = FacesContext.getCurrentInstance();
            HttpServletRequest origRequest = (HttpServletRequest) fc.getExternalContext().getRequest();
            HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
            fc.getExternalContext().redirect("http://" + origRequest.getServerName() + ":" + origRequest.getServerPort() + "/cabinetdentaire/faces/welcome.xhtml");
        }
    }

 
    public boolean seConnecter(String login, String password) {
        boolean b = false;
        try {
            User userr = ejbFacade.findByParameterSingleResult("Select u from User u where u.login=:login", "login", login);
            if ((userr.getLogin().equals(login)) & (userr.getPassword().equals(password))) {
                user = userr;
                b = true;
            }
        } catch (Exception e) {
        }
        return b;
    }

    public String decrypt(String password) {
        try {
            String keyStr = "menyaabn";
            SecretKeySpec myDesKey = new SecretKeySpec(keyStr.getBytes(), "DES");
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.DECRYPT_MODE, myDesKey);
            byte[] textEncrypted = desCipher.doFinal(this.hexToBytes(password));
            return new String(textEncrypted);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] hexToBytes(String str) {
        if (str == null) {
            return null;
        } else if (str.length() < 2) {
            return null;
        } else {
            int len = str.length() / 2;
            byte[] buffer = new byte[len];
            for (int i = 0; i < len; i++) {
                buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
            }
            return buffer;
        }

    }

    public String bytesToHex(byte[] data) {
        if (data == null) {
            return null;
        } else {
            int len = data.length;
            String str = "";
            for (int i = 0; i < len; i++) {
                if ((data[i] & 0xFF) < 16) {
                    str = str + "0" + java.lang.Integer.toHexString(data[i] & 0xFF);
                } else {
                    str = str + java.lang.Integer.toHexString(data[i] & 0xFF);
                }
            }
            return str.toUpperCase();
        }
    }

    public void refraichirEtatBouton(String page) {
        try {
            if (this.user.getAdministrateur().equals("Oui")) {
                this.ajouter = true;
                this.modifier = true;
                this.supprimer = true;
                this.pdfConsult = true;
            } else {
                List<String> ss = new ArrayList<String>();
                List<Object> oo = new ArrayList<Object>();
                ss.add("user1");
                ss.add("tableinterne");
                oo.add(this.user);
                oo.add(page);
                Profile p = this.ejbprofile.findByParameterSingleResultMultipleCreteria("Select p from Profile p where p.user1=:user1 and p.profilePK.tableinterne=:tableinterne "
                        + "and p.profilePK.logiciel='Cabinet'", ss, oo);
                if (p.getAjouter().equals("Oui")) {
                    this.ajouter = true;
                }
                if (p.getAjouter().equals("Non")) {
                    this.ajouter = false;
                }
                if (p.getModifier().equals("Oui")) {
                    this.modifier = true;
                }
                if (p.getModifier().equals("Non")) {
                    this.modifier = false;
                }
                if (p.getSupprimer().equals("Oui")) {
                    this.supprimer = true;
                }
                if (p.getSupprimer().equals("Non")) {
                    this.supprimer = false;
                }
            }
        } catch (Exception e) {
            this.ajouter = false;
            this.modifier = false;
            this.supprimer = false;
            this.pdfConsult = false;
        }
    }

    public Boolean refraichirEtatBoutonAjouter(String page) {
        try {
            if (this.user.getAdministrateur().equals("Oui")) {
                return true;
            } else {
                List<String> ss = new ArrayList<String>();
                List<Object> oo = new ArrayList<Object>();
                ss.add("user1");
                ss.add("tableinterne");
                oo.add(this.user);
                oo.add(page);
                Profile p = this.ejbprofile.findByParameterSingleResultMultipleCreteria("Select p from Profile p where p.user1=:user1 and p.profilePK.tableinterne=:tableinterne "
                        + "and p.profilePK.logiciel='Cabinet'", ss, oo);
                if (p.getAjouter().equals("Oui")) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public Boolean refraichirEtatBoutonModifier(String page) {
        try {
            if (this.user.getAdministrateur().equals("Oui")) {
                return true;
            } else {
                List<String> ss = new ArrayList<String>();
                List<Object> oo = new ArrayList<Object>();
                ss.add("user1");
                ss.add("tableinterne");
                oo.add(this.user);
                oo.add(page);
                Profile p = this.ejbprofile.findByParameterSingleResultMultipleCreteria("Select p from Profile p where p.user1=:user1 and p.profilePK.tableinterne=:tableinterne "
                        + "and p.profilePK.logiciel='Cabinet'", ss, oo);
                if (p.getModifier().equals("Oui")) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public Boolean refraichirEtatBoutonSupprimer(String page) {

        try {
            if (this.user.getAdministrateur().equals("Oui")) {
                return true;
            } else {
                List<String> ss = new ArrayList<String>();
                List<Object> oo = new ArrayList<Object>();
                ss.add("user1");
                ss.add("tableinterne");
                oo.add(this.user);
                oo.add(page);
                Profile p = this.ejbprofile.findByParameterSingleResultMultipleCreteria("Select p from Profile p where p.user1=:user1 and p.profilePK.tableinterne=:tableinterne "
                        + "and p.profilePK.logiciel='Cabinet'", ss, oo);
                if (p.getSupprimer().equals("Oui")) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public Boolean refraichirEtatBoutonEtat(String page) {
        if (this.user.getAdministrateur().equals("Oui")) {
            return true;
        } else {
            try {
                Profile p = this.ejbprofile.findByParameterSingleResult("Select p from Profile p where p.user1.id=" + this.user.getId() + " and p.profilePK.tableinterne=:page "
                        + "and p.profilePK.logiciel='Cabinet'", "page", page);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public Boolean renderedEtatUserMenu(String page) {

        if (this.user.getAdministrateur().equals("Oui")) {
            return true;
        } else {
            try {
                Profile p = this.ejbprofile.findByParameterSingleResult("Select p from Profile p where p.user1.id=" + this.user.getId() + " and p.profilePK.tableinterne=:page "
                        + "and p.profilePK.logiciel='Cabinet'", "page", page);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public Boolean renderedEtatUserMenuLike(String page) {
        if (this.user.getAdministrateur().equals("Oui")) {
            return true;
        } else {
            try {
                List<Profile> p = this.ejbprofile.findByParameter("Select p from Profile p where p.profilePK.tableinterne like :page and p.user1.id=" + this.user.getId() + " "
                        + "and p.profilePK.logiciel='Cabinet'", "page", "%" + page + "%");
                if (p.isEmpty() == false) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
    }

    public Boolean disabledEtatUserMenuInventaireStock(String page) {
        if (this.user.getAdministrateur().equals("Oui")) {
            return true;
        } else {
            try {
                Profile p = this.ejbprofile.findByParameterSingleResult("Select p from Profile p where p.profilePK.tableinterne=:page and "
                        + "p.user1.id=" + this.user.getId() + " and ((p.ajouter='Oui') or (p.modifier='Oui') or (p.supprimer='Oui'))", "page", page);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public String affichermenu() {
        flagpatient = disabledUserMenuAjouter("Patient");
        flag2patient = disabledUserMenuModifierSupprimer("Patient");
        flagimagespatient = disabledUserMenuAjouter("Images Patient");
        flag2imagespatient = disabledUserMenuModifierSupprimer("Images Patient");
         flagrendezvous = disabledUserMenuAjouter("Rendez Vous");
        flag2rendezvous = disabledUserMenuModifierSupprimer("Rendez Vous");
         flagantecedant = disabledUserMenuAjouter("Antecedant");
        flag2antecedant  = disabledUserMenuModifierSupprimer("Antecedant");
        flagetatcabinet = disabledEtatUserMenu("etat Cabinet");
     

        return "";
    }

    public Boolean disabledUserMenuAjouter(String page) {
    //    System.out.println("l utilisateur est : "+user.getLogin());
       if (this.user.getAdministrateur().equals("Oui")) {
            return true;
        } else {
            try {
                Profile p = this.ejbprofile.findByParameterSingleResult("Select p from Profile p where p.user1.id=" + this.user.getId() + " and p.profilePK.tableinterne=:page "
                        + "and p.profilePK.logiciel='Cabinet' and p.ajouter='Oui'", "page", page);
         
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public Boolean disabledUserMenuModifierSupprimer(String page) {
        if (this.user.getAdministrateur().equals("Oui")) {
            return true;
        } else {
            try {
                Profile p = this.ejbprofile.findByParameterSingleResult("Select p from Profile p where p.user1.id=" + this.user.getId() + " and p.profilePK.tableinterne=:page "
                        + "and p.profilePK.logiciel='Cabinet' and ((p.modifier='Oui') or (p.supprimer='Oui'))", "page", page);
             
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public Boolean disabledEtatUserMenu(String page) {
        if (this.user.getAdministrateur().equals("Oui")) {
            return true;
        } else {
            try {
                Profile p = this.ejbprofile.findByParameterSingleResult("Select p from Profile p where p.profilePK.tableinterne=:page and p.user1.id=" + this.user.getId() + " "
                        + "and p.profilePK.logiciel='Cabinet'", "page", page);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    public boolean menuPrincipaleClient() {
        if (this.user.getAdministrateur().equalsIgnoreCase("oui")) {
                return true;
            }
        else
        {
return flagpatient || flag2patient || flagantecedant|| flag2antecedant|| flagimagespatient|| flag2imagespatient|| flagrendezvous|| flag2rendezvous ;
        }    
    }

  

  
 

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Boolean getAjouter() {
        return ajouter;
    }

    public void setAjouter(Boolean ajouter) {
        this.ajouter = ajouter;
    }

    public Boolean getModifier() {
        return modifier;
    }

    public void setModifier(Boolean modifier) {
        this.modifier = modifier;
    }

    public Boolean getPdfConsult() {
        return pdfConsult;
    }

    public void setPdfConsult(Boolean pdfConsult) {
        this.pdfConsult = pdfConsult;
    }

    public Boolean getSupprimer() {
        return supprimer;
    }

    public void setSupprimer(Boolean supprimer) {
        this.supprimer = supprimer;
    }

    public String getBaseDonneesChoisi() {
        return baseDonneesChoisi;
    }

    public Boolean getFlag2antecedant() {
        return flag2antecedant;
    }

    public void setFlag2antecedant(Boolean flag2antecedant) {
        this.flag2antecedant = flag2antecedant;
    }

    public Boolean getFlag2imagespatient() {
        return flag2imagespatient;
    }

    public void setFlag2imagespatient(Boolean flag2imagespatient) {
        this.flag2imagespatient = flag2imagespatient;
    }

    public Boolean getFlag2patient() {
        return flag2patient;
    }

    public void setFlag2patient(Boolean flag2patient) {
        this.flag2patient = flag2patient;
    }

    public Boolean getFlag2rendezvous() {
        return flag2rendezvous;
    }

    public void setFlag2rendezvous(Boolean flag2rendezvous) {
        this.flag2rendezvous = flag2rendezvous;
    }

    public Boolean getFlagantecedant() {
        return flagantecedant;
    }

    public void setFlagantecedant(Boolean flagantecedant) {
        this.flagantecedant = flagantecedant;
    }

    public Boolean getFlagetatcabinet() {
        return flagetatcabinet;
    }

    public void setFlagetatcabinet(Boolean flagetatcabinet) {
        this.flagetatcabinet = flagetatcabinet;
    }

    public Boolean getFlagimagespatient() {
        return flagimagespatient;
    }

    public void setFlagimagespatient(Boolean flagimagespatient) {
        this.flagimagespatient = flagimagespatient;
    }

    public Boolean getFlagpatient() {
        return flagpatient;
    }

    public void setFlagpatient(Boolean flagpatient) {
        this.flagpatient = flagpatient;
    }

    public Boolean getFlagrendezvous() {
        return flagrendezvous;
    }

    public void setFlagrendezvous(Boolean flagrendezvous) {
        this.flagrendezvous = flagrendezvous;
    }


    public Semaphore getSemaphore(String s) {
        return null;
    }

   

  

  
}
