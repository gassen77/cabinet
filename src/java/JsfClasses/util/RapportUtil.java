package JsfClasses.util;

import entities.Cabinet;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletResponse;
//import JsfClasses.util.textGenerator.TextExporter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;


/**
 *
 * @author aymen
 */
public class RapportUtil {
    public static void downloadFile(String typeContent,String filePath,String fileName) throws FileNotFoundException, IOException{
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse)context.getExternalContext().getResponse();
        response.reset();
        response.setContentType(typeContent);
        response.setHeader("Content-Disposition","attachment; filename="+fileName);
        FileInputStream fileIn = new FileInputStream(new File(filePath));
        OutputStream out = response.getOutputStream();
        byte[] outputByte = new byte[ParametresRapports.getBufferSize()];
        while(fileIn.read(outputByte) != -1){
            out.write(outputByte);
            outputByte = new byte[ParametresRapports.getBufferSize()];
        }
        fileIn.close();
        out.flush();
        out.close();
        context.responseComplete();
    }
    public static void deleteFile(String resultatRapport){
        try{
            File f=new File(resultatRapport);
            f.delete();
        }catch(Exception e){e.printStackTrace();}
    }
    public static void sendMail(String pdfPath,String nomFichier,String emailSender,String emailReceiver,String passwordEmailSender) throws MessagingException{
        File ffile=new File(pdfPath);
        Properties props = new Properties();
	props.put("mail.smtp.auth", "true");
	props.put("mail.smtp.starttls.enable", "true");
	props.put("mail.smtp.host", "smtp.gmail.com");
	props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props,null);
	MimeMessage message = new MimeMessage(session);
	message.setFrom(new InternetAddress(emailSender));
	message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailReceiver));
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
        transport.connect("smtp.gmail.com",emailSender,passwordEmailSender);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
    public static void genererListItemsPdf(JasperPrint jasperPrint,String nomFichier){
        try{
            String resultatRapport=ParametresRapports.getPathRapport()+"\\"+nomFichier;
//            FontKey key = new FontKey("Times New Roman", true, false);
//            RtfFont font = new RtfFont("Arial", 9, RtfFont.NORMAL);
//            fontMap.put(key, font);
//          JasperExportManager.setParameter(Jasper, fontMap);
            System.out.println("resultatRapport "+resultatRapport);
            JasperExportManager.exportReportToPdfFile(jasperPrint,resultatRapport);
            downloadFile("application/pdf",resultatRapport,nomFichier);
            deleteFile(resultatRapport);
        }catch(Exception er){
        er.printStackTrace();}
    }
    public static void genererListItemsExcel(JasperPrint jasperPrint,String nomFichier){
        try{
            String resultatRapport=ParametresRapports.getPathRapport()+"\\"+nomFichier;
            OutputStream ouputStream=new FileOutputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport/")+"\\"+nomFichier));
            String[] sheetNames = {"Sheet1"};
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JRXlsExporter exporterXLS = new JRXlsExporter();
            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
            exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
            exporterXLS.setParameter(JRXlsExporterParameter.SHEET_NAMES,sheetNames);
            exporterXLS.exportReport();
            ouputStream.write(byteArrayOutputStream.toByteArray());
            ouputStream.close();
            downloadFile("application/vnd.ms-excel",resultatRapport,nomFichier);
            deleteFile(resultatRapport);
        }catch(Exception er){}
    }
//    public static void genererListItemsText(JasperPrint jasperPrint,String nomFichier,HorizonSys s){
//        try{
//            TextExporter textExp=new TextExporter(jasperPrint,jasperPrint.getPageHeight(),jasperPrint.getPageWidth(),ParametresRapports.getPathRapport()+"\\"+nomFichier,nomFichier,s);
//            textExp.generateTextFile();
//            textExp.sendFileToDownload();
//            deleteFile(ParametresRapports.getPathRapport()+"\\"+nomFichier);
//        }catch(Exception er){}
//    }
    public static void sendEmailPdf(JasperPrint jasperPrint,String nomFichier,String emailSender,String emailReceiver,String passwordEmailSender) {
        try{
            String resultatRapport=ParametresRapports.getPathRapport()+"\\"+nomFichier;
            JasperExportManager.exportReportToPdfFile(jasperPrint,resultatRapport);
            sendMail(resultatRapport,nomFichier,emailSender,emailReceiver,passwordEmailSender);
            deleteFile(resultatRapport);
            JsfUtil.addSuccessMessage("mail envoyer");
        }catch(Exception er){
            JsfUtil.addErrorMessage("Verifier votre connexion internet, votre login et votre password");
        }
    }
    public static void sendEmailExcel(JasperPrint jasperPrint,String nomFichier,String emailSender,String emailReceiver,String passwordEmailSender){
        try{
            String resultatRapport=ParametresRapports.getPathRapport()+"\\"+nomFichier;
            OutputStream ouputStream=new FileOutputStream(new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("/rapport/")+"\\"+nomFichier));
            String[] sheetNames = {"Sheet1"};
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            JRXlsExporter exporterXLS = new JRXlsExporter();
            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
            exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
            exporterXLS.setParameter(JRXlsExporterParameter.SHEET_NAMES,sheetNames);
            exporterXLS.exportReport();
            ouputStream.write(byteArrayOutputStream.toByteArray());
            ouputStream.close();
            sendMail(resultatRapport,nomFichier,emailSender,emailReceiver,passwordEmailSender);
            deleteFile(resultatRapport);
            JsfUtil.addSuccessMessage("mail envoyer");
        }catch(Exception er){
            JsfUtil.addErrorMessage("Verifier votre connexion internet, votre login et votre password");
        }
    }
}
