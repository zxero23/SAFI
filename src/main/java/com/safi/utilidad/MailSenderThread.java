package com.safi.utilidad;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;
import com.safi.entity.Usuario;

/**
 * @author Doroñuk Gustavo Matias Zakowicz
 */
public class MailSenderThread extends Thread {

    private static Logger log = Logger.getLogger(MailSenderThread.class);
    private String destinatario;
    private String asunto;
    private String textoMensaje;
    private boolean htmlFormat;
    private boolean mailProveedores;
    private List<String> lstAdjuntos = new ArrayList<>();

    /**
     * Envía un correo electrónico con uno o varios archivos adjuntos (siempre
     * se envían desde no-reply@team.marandu.com.ar).
     *
     * @author Doroñuk Gustavo
     * @param destinatario Correo electrónico receptor.
     * @param asunto Asunto del correo electrónico.
     * @param textoMensaje Texto a enviar en el correo electrónico.
     * @param lstAdjuntos Listado de path's de archivos a enviar.
     * @param htmlFormat
     */
    public MailSenderThread(String destinatario, String asunto, String textoMensaje, List<String> lstAdjuntos, boolean htmlFormat, boolean isMailProveedores) {
        this.destinatario = destinatario.trim();
        this.asunto = asunto;
        this.textoMensaje = textoMensaje;
        this.lstAdjuntos = lstAdjuntos;
        this.htmlFormat = htmlFormat;
        this.mailProveedores = isMailProveedores;
    }

    /**
     * Envía un correo electrónico.
     *
     * @author Doroñuk Gustavo
     * @param destinatario Correo electrónico receptor.
     * @param asunto Asunto del correo electrónico.
     * @param textoMensaje Texto a enviar en el correo electrónico.
     */
    public MailSenderThread(String destinatario, String asunto, String textoMensaje, boolean isMailProveedores) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.textoMensaje = textoMensaje;
        this.mailProveedores = isMailProveedores;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getTextoMensaje() {
        return textoMensaje;
    }

    public void setTextoMensaje(String textoMensaje) {
        this.textoMensaje = textoMensaje;
    }

    public List<String> getLstAdjuntos() {
        return lstAdjuntos;
    }

    public void setLstAdjuntos(List<String> lstAdjuntos) {
        this.lstAdjuntos = lstAdjuntos;
    }

    public static Logger getLog() {
        return log;
    }

    public static void setLog(Logger log) {
        MailSenderThread.log = log;
    }

    public boolean isHtmlFormat() {
        return htmlFormat;
    }

    public void setHtmlFormat(boolean htmlFormat) {
        this.htmlFormat = htmlFormat;
    }

    public boolean isMailProveedores() {
        return mailProveedores;
    }

    public void setMailProveedores(boolean mailProveedores) {
        this.mailProveedores = mailProveedores;
    }

    @Override
    public void run() {
        try {
            this.log.info("START run() " + this.toString());
            Properties propertiesAux = new Properties();
            File propsFile = new File("/home/config.properties");
            InputStream is = new FileInputStream(propsFile);
            Usuario usuarioAux = null;
            if (is != null) { //Evalua que el archivo exista
                propertiesAux.load(is);
            } else {
                log.error("Archivo properties '" + propsFile + "' no encontrado.");
                throw new FileNotFoundException("Archivo properties '" + propsFile + "' no encontrado.");
            }
            is.close();
            //Setea las propiedades para el envío del mensaje.
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.trust", propertiesAux.getProperty("emailHost"));
            props.put("mail.smtp.starttls.enable", "true");//it’s optional in Mailtrap  
            props.put("mail.smtp.host", propertiesAux.getProperty("emailHost"));
            props.put("mail.smtp.port", propertiesAux.getProperty("emailPort"));// use one of the options in the SMTP settings tab in your Mailtrap Inbox
            //Se inicia sesion.
            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(propertiesAux.getProperty("userEmail"), propertiesAux.getProperty("emailPassword"));
                        }
                    });
            try {
                //Se crea el cuerpo del mensaje.
                MimeMessage mimeMessage = new MimeMessage(session);
                //Se agrega el emisor al cuerpo del mensaje.
                mimeMessage.setFrom(new InternetAddress(propertiesAux.getProperty("userEmail"), "Contaduría General"));
                //Se crea el destinatario.
                InternetAddress[] internetAddressesTo = {propertiesAux.getProperty("entorno").equals("production")? new InternetAddress(this.getDestinatario()) : new InternetAddress(propertiesAux.getProperty("emailSoporte"))};
                //CCO 
                mimeMessage.addRecipients(Message.RecipientType.BCC, new InternetAddress[]{new InternetAddress(propertiesAux.getProperty("emailSoporte"))});
                if (this.isMailProveedores()) {
                    mimeMessage.addRecipients(Message.RecipientType.BCC, new InternetAddress[]{new InternetAddress(propertiesAux.getProperty("mailContaduria"))});

                    log.info("Mail enviado a: " + this.getDestinatario() + "con copia a: " + propertiesAux.getProperty("mailContaduria")) ;
                }

                //Se agrega el destinatario al cuerpo del mensaje.
                mimeMessage.setRecipients(Message.RecipientType.TO, internetAddressesTo);
                //Se crea la lista de correos para el envío con CCO.
                //InternetAddress[] internetAddressesBCC = {new InternetAddress(destinatario)};
                //Se agregan los CCO al cuerpo del mensaje.
                //mimeMessage.setRecipients(Message.RecipientType.BCC, internetAddressesBCC);
                //Se agrega el asunto al cuerpo del mensaje.
                mimeMessage.setSubject(propertiesAux.getProperty("asuntoExtra") + this.getAsunto());
                //Se crea un multipart para asignarle el mensaje y los adjuntos, y luego
                //éste se asigna al cuerpo del mensaje para ser enviado.
                Multipart multipart = new MimeMultipart();
                //Se crea el bodypart para asignarle el mensaje.
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                //Se setea el mensaje en el bodypart.
                if (htmlFormat) {
                    mimeMessage.setContent(textoMensaje, "text/html");
                } else {
                    mimeBodyPart.setText(textoMensaje);
                }
                //Se agrega el bodypart con el mensaje al multipart.
                multipart.addBodyPart(mimeBodyPart);
                //Si hay adjuntos, los agrega al mensaje.
                if (!this.getLstAdjuntos().isEmpty()) {
                    //Se crea un listado de bodypart para agregarle los adjuntos a enviar.
                    List<MimeBodyPart> lstMimeBodyPartAdjuntos = new LinkedList<>();
                    //Se agregan cada uno de los adjuntos al listado.
                    for (int i = 0; i <= this.getLstAdjuntos().size() - 1; i++) {
                        //Se crea un bodypart para asignarle un adjunto.
                        MimeBodyPart adjunto = new MimeBodyPart();
                        //Se obtiene el archivo adjunto del path y se lo asigna al bodypart.
                        adjunto.setDataHandler(new DataHandler(new FileDataSource(this.getLstAdjuntos().get(i))));
                        //Se obtiene el archiva para obtener sus caracteristicas como nombre, extension, path, etc.
                        File archivo = new File(this.getLstAdjuntos().get(i));
                        //Se setea el nombre del archivo adjunto de como se va a enviar.
                        //adjunto.setFileName(archivo."dni-" + i + ".pdf");
                        adjunto.setFileName(archivo.getName());
                        //Se agrega el bodypart con el adjunto al listado de bodypart.
                        lstMimeBodyPartAdjuntos.add(adjunto);
                    }
                    for (MimeBodyPart attach : lstMimeBodyPartAdjuntos) {
                        //Se agrega el bodypart con el adjunto al multipart.
                        multipart.addBodyPart(attach);
                    }
                }
                //Se agrega el multipart al cuerpo del mensaje para ser enviado.
                if (!htmlFormat) {
                    mimeMessage.setContent(multipart);
                }
                //Se envia el mensaje
                Transport transport = session.getTransport("smtp");
                transport.connect(propertiesAux.getProperty("userEmail"), propertiesAux.getProperty("emailPassword"));
                transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
                transport.close();
            } catch (UnsupportedEncodingException | MessagingException e) {
                log.error(e.getMessage());
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return "MailSenderThread=[destinatario=" + this.getDestinatario() + ", asunto=" + this.getAsunto() + ", htmlFormat=" + this.isHtmlFormat() + ", mailProveedores=" + this.isMailProveedores() + "].";
    }

}
