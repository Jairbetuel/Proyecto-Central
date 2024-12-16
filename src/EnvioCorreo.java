/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author omarg
 */
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

public class EnvioCorreo {

    public static void enviarCorreo(String emailFrom, String passwordFrom, String correoElectronico, String subject, String ticket) {
        // Configuración del protocolo SMTP
        Properties mProperties = new Properties();
        mProperties.put("mail.smtp.host", "smtp.gmail.com");
        mProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mProperties.setProperty("mail.smtp.starttls.enable", "true");
        mProperties.setProperty("mail.smtp.port", "587");
        mProperties.setProperty("mail.smtp.user", emailFrom);
        mProperties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        mProperties.setProperty("mail.smtp.auth", "true");

        // Usar Session.getInstance() en lugar de getDefaultInstance()
        Session mSession = Session.getInstance(mProperties);

        try {
            MimeMessage mCorreo = new MimeMessage(mSession);
            mCorreo.setFrom(new InternetAddress(emailFrom));
            mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(correoElectronico));
            mCorreo.setSubject(subject);
            mCorreo.setText(ticket, "ISO-8859-1", "html");

            Transport mTransport = mSession.getTransport("smtp");
            mTransport.connect(emailFrom, passwordFrom);
            mTransport.sendMessage(mCorreo, mCorreo.getRecipients(Message.RecipientType.TO));
            mTransport.close();

            JOptionPane.showMessageDialog(null, "Correo enviado con el boleto.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (MessagingException ex) {
            JOptionPane.showMessageDialog(null, "Error al enviar el correo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Datos del ticket
        String nombrePasajero = "Juan Pérez";
        String costo = "100";
        String nAsiento = "15";
        String correoElectronico = "juan.perez@example.com";

        if (nombrePasajero.isEmpty() || costo.isEmpty() || 
            nAsiento.isEmpty() || correoElectronico.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos antes de pagar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ticket = "********** TICKET **********\n" +
                        "Línea de Autobuses: Estudiante\n" +
                        "Nombre del Pasajero: " + nombrePasajero + "\n" +
                        "Costo: $" + costo + "\n" +
                        "No. de Asiento: " + nAsiento + "\n" +
                        "Correo Electrónico: " + correoElectronico + "\n" +
                        "";

        JOptionPane.showMessageDialog(null, ticket, "Ticket Generado", JOptionPane.INFORMATION_MESSAGE);

        // Datos del correo
        String emailFrom = "tucorreo@gmail.com";
        String passwordFrom = "tucontraseña";
        String subject = "BOLETO LINEA ESTUDIANTE";

        enviarCorreo(emailFrom, passwordFrom, correoElectronico, subject, ticket);
    }
}

