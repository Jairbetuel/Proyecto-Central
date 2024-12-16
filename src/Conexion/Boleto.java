/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Conexion;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @jairb
 */
public class Boleto extends javax.swing.JFrame {
    
    DefaultTableModel model;
  private int idAutobus; // Declarar como campo de la clase


    /**
     * Creates new form Boleto
     */
    public Boleto() {
    initComponents();
     configurarDisenioBoletos();
    limpiar();
    bloquear();
    cargarViajes("");
    cargarBoletos("");
    }
    private void configurarDisenioBoletos() {
    // Configuración del diseño monocromático oscuro
    Color fondoOscuro = new Color(40, 40, 40);
    Color textoClaro = new Color(200, 200, 200);
    Color acento = new Color(70, 130, 180);

    // Configurar fondo del contenedor principal
    this.getContentPane().setBackground(fondoOscuro);

    // Crear una lista de componentes que necesitan ajustes de diseño
    Component[] componentes = {
        Ce, Label1, Label3, Label4, Sa, apellidoP, bcancelar, bmenu, bnuevo, bpagar, cos, eliminar, id, idA, id_ruta,
        jLabel1, jLabel2, jLabel3, jLabel7, jMenuItem1, jPopupMenu1, jScrollPane1, jScrollPane2, jScrollPane3, jTable1, na, np, numTel, tBoleto, tviajes
    };

    for (Component componente : componentes) {
        if (componente instanceof JLabel) {
            // Aplicar estilo a etiquetas
            componente.setForeground(textoClaro);
        } else if (componente instanceof JButton) {
            // Aplicar estilo a botones
            componente.setBackground(acento);
            componente.setForeground(textoClaro);
            ((JButton) componente).setBorder(BorderFactory.createLineBorder(textoClaro));
        } else if (componente instanceof JTextField || componente instanceof JComboBox) {
            // Aplicar estilo a campos de texto y combos
            componente.setBackground(fondoOscuro);
            componente.setForeground(textoClaro);
            componente.setFont(new Font("SansSerif", Font.PLAIN, 14));
        } else if (componente instanceof JTable) {
            // Aplicar estilo a tablas
            JTable tabla = (JTable) componente;
            tabla.setBackground(fondoOscuro);
            tabla.setForeground(textoClaro);
            tabla.setSelectionBackground(acento);
            tabla.setSelectionForeground(fondoOscuro);
            tabla.setGridColor(acento);
        }
    }

    // Configurar fondo de los JScrollPanes
    jScrollPane1.getViewport().setBackground(fondoOscuro);
    jScrollPane2.getViewport().setBackground(fondoOscuro);
    jScrollPane3.getViewport().setBackground(fondoOscuro);
    
}
    private void enviarCorreo(String destinatario, String rutaArchivo) {
    // Configuración del servidor SMTP (esto depende de tu proveedor de correo)
    String host = "smtp.gmail.com"; // Usando Gmail como ejemplo
    final String usuario = "jairbetuelhernandezgarcia@gmail.com"; // Tu correo
    final String contrasena = "nmin mehu tkru zdrf"; // Tu contraseña

    Properties propiedades = new Properties();
    propiedades.put("mail.smtp.auth", "true");
    propiedades.put("mail.smtp.starttls.enable", "true");
    propiedades.put("mail.smtp.host", host);
    propiedades.put("mail.smtp.port", "587");

    // Obtener la sesión de correo
    Session session = Session.getInstance(propiedades, new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(usuario, contrasena);
        }
    });

    try {
        // Crear el mensaje
        Message mensaje = new MimeMessage(session);
        mensaje.setFrom(new InternetAddress(usuario));
        mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        mensaje.setSubject("Ticket de Autobús");

        // Cuerpo del correo
        MimeBodyPart cuerpo = new MimeBodyPart();
        cuerpo.setText("Estimado pasajero, adjunto su ticket de autobús.");

        // Adjuntar el archivo PDF
        MimeBodyPart adjunto = new MimeBodyPart();
        File archivoAdjunto = new File(rutaArchivo);
        adjunto.attachFile(archivoAdjunto);

        // Crear el Multipart y agregar el cuerpo y el archivo adjunto
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(cuerpo);
        multipart.addBodyPart(adjunto);

        // Configurar el contenido del mensaje
        mensaje.setContent(multipart);

        // Enviar el correo
        Transport.send(mensaje);

        JOptionPane.showMessageDialog(this, "Correo enviado con éxito a: " + destinatario, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    } catch (MessagingException | IOException e) {
        JOptionPane.showMessageDialog(this, "Error al enviar el correo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    void cargarViajes(String filtro) {
        String[] titulos = {"ID", "Origen", "Destino", "Hora Inicio", "Hora Fin", "Fecha", "Camión"};
        String[] registro = new String[7];
        model = new DefaultTableModel(null, titulos);

        String sql = "SELECT r.id_ruta, r.origen, r.destino, r.hora_inicio, r.hora_fin, r.fecha, a.placas " +
                     "FROM Ruta r " +
                     "LEFT JOIN Asignacion asi ON r.id_ruta = asi.id_ruta " +
                     "LEFT JOIN Autobus a ON asi.id_autobus = a.id_autobus";

        if (!filtro.isEmpty()) {
            sql += " WHERE r.origen LIKE ? OR r.destino LIKE ? OR r.hora_inicio LIKE ? OR r.fecha LIKE ?";
        }

        try (Connection cn = new conectar().getConexion();
             PreparedStatement pst = cn.prepareStatement(sql)) {

            if (!filtro.isEmpty()) {
                for (int i = 1; i <= 4; i++) {
                    pst.setString(i, "%" + filtro + "%");
                }
            }

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    registro[0] = rs.getString("id_ruta");
                    registro[1] = rs.getString("origen");
                    registro[2] = rs.getString("destino");
                    registro[3] = rs.getString("hora_inicio");
                    registro[4] = rs.getString("hora_fin");
                    registro[5] = rs.getString("fecha");
                    registro[6] = rs.getString("placas") != null ? rs.getString("placas") : "Sin asignar";

                    model.addRow(registro);
                }
            }

            tviajes.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
        }
    }
    
    void cargarBoletos(String valor) {
    String[] titulos = {"ID Boleto", "Nombre", "Apellido", "Número de Asiento", "Origen", "Destino", "Costo", "Fecha"};
    String[] registro = new String[8];
    DefaultTableModel model = new DefaultTableModel(null, titulos);

    String sql = "SELECT b.id_boleto, p.nombre, p.apellido_p, a.numero, r.origen, r.destino, b.costo, b.fecha " +
                 "FROM boleto b " +
                 "JOIN pasajero p ON b.id_pasajero = p.id_cliente " +
                 "JOIN asiento a ON b.id_asiento = a.id_asiento " +
                 "JOIN ruta r ON b.id_ruta = r.id_ruta";

    if (!valor.isEmpty()) {
        sql += " WHERE p.nombre LIKE ? OR p.apellido_p LIKE ? OR a.numero LIKE ? OR r.origen LIKE ? OR r.destino LIKE ? OR b.costo LIKE ? OR b.fecha LIKE ?";
    }

    try (Connection cn = new conectar().getConexion();
         PreparedStatement pst = cn.prepareStatement(sql)) {

        if (!valor.isEmpty()) {
            for (int i = 1; i <= 7; i++) {
                pst.setString(i, "%" + valor + "%");
            }
        }

        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                registro[0] = rs.getString("id_boleto");
                registro[1] = rs.getString("nombre");
                registro[2] = rs.getString("apellido_p");
                registro[3] = rs.getString("numero");
                registro[4] = rs.getString("origen");
                registro[5] = rs.getString("destino");
                registro[6] = rs.getString("costo");
                registro[7] = rs.getString("fecha");
                model.addRow(registro);
            }
        }
        tBoleto.setModel(model);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
    }
}

    void limpiar(){
        np.setText("");
        cos.setText("");
        na.setText("");
        apellidoP.setText("");
        Ce.setText("");
        numTel.setText("");
    }
    
    void bloquear(){
        np.setEnabled(false);
        cos.setEnabled(false);
        na.setEnabled(false);
        Sa.setEnabled(false);
        apellidoP.setEnabled(false);
        Ce.setEnabled(false);
        numTel.setEnabled(false);
        bnuevo.setEnabled(true);
        bcancelar.setEnabled(false);   
        bpagar.setEnabled(false);
    }
    
    void habilitar(){
        np.setEnabled(true);
        cos.setEnabled(true);
        Sa.setEnabled(true);
        na.setEnabled(true);
        apellidoP.setEnabled(true);
        Ce.setEnabled(true);
        numTel.setEnabled(true);
        bnuevo.setEnabled(false);
        bcancelar.setEnabled(true);
bpagar.setEnabled(true);
    }
    
    void actualizar(){
        np.setEnabled(true);
        cos.setEnabled(true);
        na.setEnabled(true);
        apellidoP.setEnabled(true);
        Ce.setEnabled(true);
        numTel.setEnabled(true);
        
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        Label1 = new javax.swing.JLabel();
        Label3 = new javax.swing.JLabel();
        Label4 = new javax.swing.JLabel();
        np = new javax.swing.JTextField();
        cos = new javax.swing.JTextField();
        na = new javax.swing.JTextField();
        bnuevo = new javax.swing.JButton();
        bcancelar = new javax.swing.JButton();
        bmenu = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        Sa = new javax.swing.JButton();
        eliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tviajes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        Ce = new javax.swing.JTextField();
        bpagar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        apellidoP = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        numTel = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        tBoleto = new javax.swing.JTable();
        id_ruta = new javax.swing.JTextField();
        idA = new javax.swing.JTextField();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        jMenuItem1.setText("jMenuItem1");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Boleto");
        setResizable(false);

        Label1.setText("Nombre del Pasajero: ");

        Label3.setText("Costo:");

        Label4.setText("N° Asiento: ");

        np.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                npActionPerformed(evt);
            }
        });

        cos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cosActionPerformed(evt);
            }
        });

        na.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                naActionPerformed(evt);
            }
        });

        bnuevo.setText("Nuevo");
        bnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnuevoActionPerformed(evt);
            }
        });

        bcancelar.setText("Cancelar");
        bcancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcancelarActionPerformed(evt);
            }
        });

        bmenu.setText("RMenu");
        bmenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bmenuActionPerformed(evt);
            }
        });

        jLabel7.setText("ID: ");

        id.setEnabled(false);

        Sa.setText("Seleccionar Asiento");
        Sa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaActionPerformed(evt);
            }
        });

        eliminar.setText("Eliminar");
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
            }
        });

        tviajes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tviajes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tviajesMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tviajes);

        jLabel1.setText("Correo Electronico:");

        Ce.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CeActionPerformed(evt);
            }
        });

        bpagar.setText("Pagar");
        bpagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bpagarActionPerformed(evt);
            }
        });

        jLabel3.setText("Apellido Pasajero:");

        apellidoP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apellidoPActionPerformed(evt);
            }
        });

        jLabel2.setText("Numero de Telefono: ");

        numTel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numTelActionPerformed(evt);
            }
        });

        tBoleto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tBoleto);

        id_ruta.setText("jTextField1");

        idA.setText("jTextField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(20, 20, 20)
                        .addComponent(Ce, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(52, 52, 52)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(numTel, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 98, Short.MAX_VALUE)
                        .addComponent(id_ruta, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(43, 43, 43)
                                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)
                                .addComponent(Label1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(np, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(apellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(Label3)
                                .addGap(27, 27, 27)
                                .addComponent(cos, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)
                                .addComponent(Sa)
                                .addGap(54, 54, 54)
                                .addComponent(Label4)
                                .addGap(18, 18, 18)
                                .addComponent(na, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 903, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGap(51, 51, 51)
                                    .addComponent(bnuevo)
                                    .addGap(39, 39, 39)
                                    .addComponent(bpagar)
                                    .addGap(43, 43, 43)
                                    .addComponent(bcancelar)
                                    .addGap(18, 18, 18)
                                    .addComponent(eliminar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(bmenu)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(idA, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 894, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bcancelar, bmenu, bnuevo});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Label1)
                            .addComponent(np, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(apellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(Ce, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(numTel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(id_ruta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Label3)
                    .addComponent(cos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Sa)
                    .addComponent(Label4)
                    .addComponent(na, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bnuevo)
                            .addComponent(bcancelar)
                            .addComponent(eliminar)
                            .addComponent(bmenu)
                            .addComponent(bpagar))
                        .addGap(34, 34, 34))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(idA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(213, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnuevoActionPerformed
        // TODO add your handling code here:
        habilitar();
        np.requestFocus();
    }//GEN-LAST:event_bnuevoActionPerformed

    private void bcancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcancelarActionPerformed
        // TODO add your handling code here:
        limpiar();
        bloquear();
    }//GEN-LAST:event_bcancelarActionPerformed

    private void npActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_npActionPerformed
        // TODO add your handling code here:
        np.requestFocus();
    }//GEN-LAST:event_npActionPerformed

    private void cosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cosActionPerformed
        // TODO add your handling code here:
        cos.requestFocus();
    }//GEN-LAST:event_cosActionPerformed

    private void naActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_naActionPerformed
        // TODO add your handling code here:
        na.requestFocus();
    }//GEN-LAST:event_naActionPerformed

    private void bmenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bmenuActionPerformed
        // TODO add your handling code here:
        new Menu("").setVisible(true);  this.dispose();
    }//GEN-LAST:event_bmenuActionPerformed

    private void SaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaActionPerformed
 // Crear una nueva ventana para la selección de asientos del autobús
    final JFrame ventanaAutobus = new JFrame("Selecciona Los Asientos");
    
    ventanaAutobus.setSize(500, 500);
    ventanaAutobus.setLayout(new BorderLayout());
    ventanaAutobus.setLocationRelativeTo(null); // Centrar ventana en la pantalla

    // Panel principal para los asientos
    JPanel panelAsientos = new JPanel(new GridLayout(10, 5, 10, 10)); // 10 filas, 5 columnas
      
    int idRuta = Integer.parseInt(id_ruta.getText());
    
    String sqlCapacidad = "SELECT a.capacidad, asi.id_autobus " +
                          "FROM Ruta r " +
                          "JOIN Asignacion asi ON r.id_ruta = asi.id_ruta " +
                          "JOIN Autobus a ON asi.id_autobus = a.id_autobus " +
                          "WHERE r.id_ruta = ?";
    int capacidad = 0;
    
    try (Connection cn = new conectar().getConexion();
         PreparedStatement pst = cn.prepareStatement(sqlCapacidad)) {
        pst.setInt(1, idRuta);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            capacidad = rs.getInt("capacidad");
            idAutobus = rs.getInt("id_autobus");
        } else {
            throw new SQLException("No se encontró información para la ruta proporcionada.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return;
    }

    // Consultar los asientos ocupados
    String sqlOcupados = "SELECT numero FROM asiento WHERE id_autobus = ? AND estado = 'ocupado'";
    Set<String> asientosOcupados = new HashSet<>();
    
    try (Connection cn = new conectar().getConexion();
         PreparedStatement pst = cn.prepareStatement(sqlOcupados)) {
        pst.setInt(1, idAutobus);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            asientosOcupados.add(rs.getString("numero"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return;
    }

    JButton[][] asientos = new JButton[10][5];
    final ArrayList<String> asientosSeleccionados = new ArrayList<>();
    int contadorAsiento = 1; // Contador para los asientos del 1 al capacidad

    for (int fila = 0; fila < 10; fila++) { // 10 filas
        for (int columna = 0; columna < 5; columna++) { // 5 columnas
            final JButton asiento = new JButton();

            // Configurar colores para los asientos y el pasillo central
            if (columna == 2) { // Pasillo central
                asiento.setEnabled(false); // Desactivar botón para el pasillo
                asiento.setBackground(Color.LIGHT_GRAY);
            } else {
                if (contadorAsiento > capacidad) {
                    asiento.setEnabled(false); // Deshabilitar asientos fuera de la capacidad
                    asiento.setBackground(Color.LIGHT_GRAY);
                } else {
                    final String numeroAsiento = String.valueOf(contadorAsiento++);
                    asiento.setText(numeroAsiento);
                    
                    if (asientosOcupados.contains(numeroAsiento)) {
                        asiento.setEnabled(false); // Deshabilitar si está ocupado
                        asiento.setBackground(Color.RED);
                        asiento.setForeground(Color.WHITE);
                    } else {
                        asiento.setBackground(Color.GREEN); // Disponibles inicialmente
                        asiento.setForeground(Color.BLACK);

                        // Evento de clic para seleccionar/deseleccionar
                        asiento.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if (asiento.getBackground() == Color.GREEN) {
                                    asiento.setBackground(Color.GRAY); // Marcado como seleccionado
                                    asientosSeleccionados.add(numeroAsiento);
                                } else if (asiento.getBackground() == Color.GRAY) {
                                    asiento.setBackground(Color.GREEN); // Volver a disponible
                                    asientosSeleccionados.remove(numeroAsiento);
                                }
                                // Actualizar el JTextField "na" con los asientos seleccionados
                                na.setText(String.join(", ", asientosSeleccionados)); // Mostrar selección
                            }
                        });
                    }
                }
            }

            asientos[fila][columna] = asiento;
            panelAsientos.add(asiento);
        }
    }

    // Botón "Listo" para confirmar la selección
    JButton botonListo = new JButton("Listo");
    botonListo.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            na.setText(String.join(", ", asientosSeleccionados));
            
            ventanaAutobus.dispose(); // Cierra la ventana de selección
            try (Connection cn = new conectar().getConexion()) {
                for (String numeroAsiento : asientosSeleccionados) {
                    // Insertar datos del asiento
                    String sqlAsiento = "INSERT INTO asiento (numero, estado, id_autobus) VALUES (?, ?, ?)";
                    PreparedStatement pstAsiento = cn.prepareStatement(sqlAsiento, Statement.RETURN_GENERATED_KEYS);
                    pstAsiento.setString(1, numeroAsiento);
                    pstAsiento.setString(2, "ocupado");
                    pstAsiento.setInt(3, idAutobus);
                    pstAsiento.executeUpdate();
                   // Obtener el ID del asiento insertado
                ResultSet rsAsiento = pstAsiento.getGeneratedKeys();
                int idAsiento = 0;
                if (rsAsiento.next()) {
                    idAsiento = rsAsiento.getInt(1);
                }
                
                idA.setText(String.valueOf(idAsiento)); // Concatenar IDs si es necesario
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
             
        }
    });

    // Agregar el panel de asientos y el botón "Listo" a la ventana
    ventanaAutobus.add(panelAsientos, BorderLayout.CENTER);
    ventanaAutobus.add(botonListo, BorderLayout.SOUTH);

    ventanaAutobus.setVisible(true);
    }//GEN-LAST:event_SaActionPerformed

    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
                           
    int filaSeleccionada = tBoleto.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione un registro para eliminar");
        return;
    }
    
    String idBoleto = tBoleto.getValueAt(filaSeleccionada, 0).toString(); // Suponiendo que la primera columna es el ID del boleto
    
    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este registro?", 
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    if (confirmacion == JOptionPane.YES_OPTION) {
        conectar cc = new conectar();
        Connection cn = cc.getConexion();
        String sql = "DELETE FROM boleto WHERE id_boleto = ? ";
        
        try {
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setString(1, idBoleto);
            int filasAfectadas = pst.executeUpdate();
            
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Registro eliminado correctamente");
                cargarBoletos(""); // Refrescar la tabla
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el registro: " + ex.getMessage());
        }
    }

    }//GEN-LAST:event_eliminarActionPerformed

    private void CeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CeActionPerformed

    private void bpagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bpagarActionPerformed
         String nombre = np.getText().trim();
    String apellido = apellidoP.getText().trim();
    String telefono = numTel.getText().trim();
    String correo = Ce.getText().trim();
    String costo = cos.getText().trim();
    String numeroAsiento = na.getText().trim();
    String idRuta = id_ruta.getText().trim();
    String idAsiento = idA.getText().trim(); // Campo idA

    // Validar campos vacíos
    if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || correo.isEmpty() || costo.isEmpty() || idRuta.isEmpty() || idAsiento.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos antes de guardar.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Validar que el campo idAsiento sea un número entero
    int idAsientoInt;
    try {
        idAsientoInt = Integer.parseInt(idAsiento); // Intenta convertir a entero
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El ID del asiento debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("Error: " + e.getMessage()); // Depuración
        return;
    }

    try (Connection cn = new conectar().getConexion()) {
        // Insertar datos del pasajero
        String sqlPasajero = "INSERT INTO pasajero (nombre, apellido_p, num_telefono, correo_electronico) VALUES (?, ?, ?, ?)";
        PreparedStatement pstPasajero = cn.prepareStatement(sqlPasajero, Statement.RETURN_GENERATED_KEYS);
        pstPasajero.setString(1, nombre);
        pstPasajero.setString(2, apellido);
        pstPasajero.setString(3, telefono);
        pstPasajero.setString(4, correo);
        pstPasajero.executeUpdate();
        ResultSet rsPasajero = pstPasajero.getGeneratedKeys();
        int idPasajero = 0;
        if (rsPasajero.next()) {
            idPasajero = rsPasajero.getInt(1);
        }

        // Insertar datos del boleto
        String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String sqlBoleto = "INSERT INTO boleto (id_pasajero, id_ruta, id_asiento, costo, fecha) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstBoleto = cn.prepareStatement(sqlBoleto,Statement.RETURN_GENERATED_KEYS);
        pstBoleto.setInt(1, idPasajero);
        pstBoleto.setString(2, idRuta);
        pstBoleto.setInt(3, idAsientoInt); // Usar idAsiento convertido
        pstBoleto.setString(4, costo);
        pstBoleto.setString(5, fechaActual);
        pstBoleto.executeUpdate();
        ResultSet rsBoleto = pstBoleto.getGeneratedKeys();
        int idBoleto = 0;
        if (rsBoleto.next()) {
            idBoleto = rsBoleto.getInt(1);
        }
        
       id.setText(String.valueOf(idBoleto));
        JOptionPane.showMessageDialog(this, "Boleto guardado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al guardar los datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace(); // Para depuración adicional
    }
       
        String idBoleto = id.getText().trim();

    // Validar que el ID del boleto no esté vacío
    if (idBoleto.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el ID del boleto para generar el ticket.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Consultar la información del boleto en la base de datos
    String sql = "SELECT b.id_boleto, p.nombre, p.apellido_p, a.numero AS asiento, r.origen, r.destino, " +
                 "r.hora_inicio, r.hora_fin, r.fecha, au.placas AS numero_camion, au.categoria, b.costo " +
                 "FROM boleto b " +
                 "JOIN pasajero p ON b.id_pasajero = p.id_cliente " +
                 "JOIN asiento a ON b.id_asiento = a.id_asiento " +
                 "JOIN ruta r ON b.id_ruta = r.id_ruta " +
                 "LEFT JOIN autobus au ON au.id_autobus = (SELECT asi.id_autobus FROM asignacion asi WHERE asi.id_ruta = r.id_ruta LIMIT 1) " +
                 "WHERE b.id_boleto = ?";

    try (Connection cn = new conectar().getConexion();
         PreparedStatement pst = cn.prepareStatement(sql)) {
        pst.setString(1, idBoleto);

        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                // Recuperar datos de la base de datos
                String nombrePasajero = rs.getString("nombre") + " " + rs.getString("apellido_p");
                String origen = rs.getString("origen");
                String destino = rs.getString("destino");
                String fechaSalida = rs.getString("fecha");
                String horaSalida = rs.getString("hora_inicio");
                String horaLlegada = rs.getString("hora_fin");
                String numeroAsientos = rs.getString("asiento");
                String numeroCamion = rs.getString("numero_camion");
                String tipoAutobus = rs.getString("categoria");
                String costos = rs.getString("costo");
                String numeroBoleto = rs.getString("id_boleto");
                
                // Obtener el correo del pasajero
    String correoPasajero = correo;
                // Generar el archivo PDF
                Document document = new Document();
                try {
                    String rutaArchivo = "Ticket_" + numeroBoleto + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
                    PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
                    document.open();

                    // Agregar contenido al PDF
                    document.add(new Paragraph("********** TICKET **********"));
                    document.add(new Paragraph("Línea de Autobuses: Estudiante"));
                    document.add(new Paragraph("Número de Boleto: " + numeroBoleto));
                    document.add(new Paragraph("Nombre del Pasajero: " + nombrePasajero));
                    document.add(new Paragraph("Origen: " + origen));
                    document.add(new Paragraph("Destino: " + destino));
                    document.add(new Paragraph("Fecha de Salida: " + fechaSalida));
                    document.add(new Paragraph("Hora de Salida: " + horaSalida));
                    document.add(new Paragraph("Hora Estimada de Llegada: " + horaLlegada));
                    document.add(new Paragraph("Número de Asiento: " + numeroAsiento));
                    document.add(new Paragraph("Número de Camión: " + numeroCamion));
                    document.add(new Paragraph("Tipo de Autobús: " + tipoAutobus));
                    document.add(new Paragraph("Costo del Boleto: $" + costo));
                    document.add(new Paragraph("Fecha de Emisión: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date())));

                     document.close();

        // Enviar el archivo por correo
        enviarCorreo(correoPasajero, rutaArchivo);

        JOptionPane.showMessageDialog(this, "Ticket generado y enviado por correo.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        limpiar();
        cargarBoletos("");
        bloquear();
    } catch (DocumentException | IOException e) {
        JOptionPane.showMessageDialog(this, "Error al generar el ticket: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    } finally {
                    if (document.isOpen()) {
                        document.close();
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el boleto con el ID especificado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al consultar los datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    }//GEN-LAST:event_bpagarActionPerformed

    private void tviajesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tviajesMouseClicked
        int fila = tviajes.getSelectedRow();

        if (fila >= 0) {
            // Obtener datos de la fila seleccionada
            String idRuta = tviajes.getValueAt(fila, 0).toString();
            String origen = tviajes.getValueAt(fila, 1).toString();
            String destino = tviajes.getValueAt(fila, 2).toString();
            String horaInicio = tviajes.getValueAt(fila, 3).toString();
            String horaFin = tviajes.getValueAt(fila, 4).toString();
            String fecha = tviajes.getValueAt(fila, 5).toString();
            String camion = tviajes.getValueAt(fila, 6).toString();

            
           id_ruta.setText(idRuta);
            JOptionPane.showMessageDialog(this, "Viaje seleccionado: \n" +
                "Ruta: " + origen + " -> " + destino + "\n" +
                "Hora: " + horaInicio + " - " + horaFin + "\n" +
                "Fecha: " + fecha + "\n" +
                "Camión: " + camion, "Selección de Viaje", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un viaje.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_tviajesMouseClicked

    private void apellidoPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apellidoPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_apellidoPActionPerformed

    private void numTelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numTelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numTelActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
         // Obtener la fila seleccionada de la tabla tBoleto
    int fil = tBoleto.getSelectedRow();
    if (fil >= 0) {
        id.setText(tBoleto.getValueAt(fil, 0).toString());
        np.setText(tBoleto.getValueAt(fil, 1).toString());
        apellidoP.setText(tBoleto.getValueAt(fil, 2).toString());
        na.setText(tBoleto.getValueAt(fil, 3).toString());
        cos.setText(tBoleto.getValueAt(fil, 6).toString());
        // Llamar a un método para actualizar si es necesario
        actualizar();
    } else {
        JOptionPane.showMessageDialog(null, "No se ha seleccionado una fila", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Boleto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Boleto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Boleto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Boleto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Boleto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Ce;
    private javax.swing.JLabel Label1;
    private javax.swing.JLabel Label3;
    private javax.swing.JLabel Label4;
    private javax.swing.JButton Sa;
    private javax.swing.JTextField apellidoP;
    private javax.swing.JButton bcancelar;
    private javax.swing.JButton bmenu;
    private javax.swing.JButton bnuevo;
    private javax.swing.JButton bpagar;
    private javax.swing.JTextField cos;
    private javax.swing.JButton eliminar;
    private javax.swing.JTextField id;
    private javax.swing.JTextField idA;
    private javax.swing.JTextField id_ruta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField na;
    private javax.swing.JTextField np;
    private javax.swing.JTextField numTel;
    private javax.swing.JTable tBoleto;
    private javax.swing.JTable tviajes;
    // End of variables declaration//GEN-END:variables
}
