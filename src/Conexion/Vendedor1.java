/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Conexion;

import com.toedter.calendar.JCalendar;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author omarg
 */
public class Vendedor1 extends javax.swing.JFrame {
DefaultTableModel model;
private int idRegistro;
    /**
     * Creates new form Vendedor1
     */
    public Vendedor1() {
        initComponents();
       configurarDisenio();
        limpiar();
        bloquear();
        cargar("");
        inicializarFechaNacimiento();
        id.setEnabled(false);
        bactualizar.setEnabled(false);
    }
private void configurarDisenio() {
    // Configuración del diseño monocromático oscuro
    Color fondoOscuro = new Color(40, 40, 40);
    Color textoClaro = new Color(200, 200, 200);
    Color acento = new Color(70, 130, 180);

    // Configurar fondo del contenedor principal
    this.getContentPane().setBackground(fondoOscuro);

    // Crear una lista de componentes que necesitan ajustes de diseño
    Component[] componentes = {
        apellidoP, aux, bactualizar, bcancelar, beliminar, bguardar, bmenu, bmostrar, bnuevo, domicilio, fn, id,
        jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jMenuItem1, jPopupMenu1, jScrollPane1,
        nombre, rfc, tablaVendedor, tel, contraseña, user, jLabel9, jLabel10
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
}

     void cargar(String valor) {
    String[] titulos = {"ID", "Nombre", "Apellido Paterno", "RFC", "Domicilio", "Teléfono", "Fecha De Nacimiento","ID Registro"};
    String[] registro = new String[8];
    model = new DefaultTableModel(null, titulos);
    String sql = "SELECT * FROM ventanilla";

    if (!valor.isEmpty()) {
        sql += " WHERE id_ventanilla LIKE ? OR nombre LIKE ? OR apellido_p LIKE ? OR RFC LIKE ? OR direccion LIKE ? OR num_telefono LIKE ? OR id_registro LIKE ?";
    }

    conectar cc = new conectar();
    try (Connection cn = cc.getConexion();
         PreparedStatement pst = cn.prepareStatement(sql)) {
        
        if (!valor.isEmpty()) {
            String searchValue = "%" + valor + "%";
            for (int i = 1; i <= 7; i++) {
                pst.setString(i, searchValue);
            }
        }

        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            registro[0] = rs.getString("id_ventanilla");
            registro[1] = rs.getString("nombre");
            registro[2] = rs.getString("apellido_p");
            registro[3] = rs.getString("RFC");
            registro[4] = rs.getString("direccion");
            registro[5] = rs.getString("num_telefono");
            registro[6] = rs.getString("fecha_nacimiento");
            registro[7] = rs.getString("id_registro");
            model.addRow(registro);
        }
        tablaVendedor.setModel(model);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
    }
}
     private void inicializarFechaNacimiento() {
    // Agregar un MouseListener para mostrar el JCalendar al hacer clic
    fn.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            mostrarCalendario();
        }
    });
}
     private void mostrarCalendario() {
    // Crear un JDialog para el calendario
    final JDialog dialog = new JDialog((Frame) null, "Selecciona una fecha", true);
    dialog.setSize(400, 400);
    dialog.setLayout(null);

    // Crear el componente JCalendar
    final JCalendar calendar = new JCalendar();
    calendar.setBounds(20, 20, 350, 250);
    dialog.add(calendar);

    // Botón para confirmar la selección
    JButton selectButton = new JButton("Seleccionar");
    selectButton.setBounds(150, 300, 100, 30);
    dialog.add(selectButton);

    // Acción al presionar el botón Seleccionar
    selectButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        // Obtener la fecha seleccionada
        java.util.Date selectedDate = calendar.getDate();
        
        // Convertir la fecha seleccionada de java.util.Date a java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
        
        // Formatear la fecha a un formato legible
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        fn.setText(sdf.format(sqlDate));
        
        // Cierra el diálogo
        dialog.dispose();
    }
});

    // Mostrar el diálogo centrado
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
}


    void limpiar(){
        rfc.setText("");
        nombre.setText("");
        domicilio.setText("");
        tel.setText("");
        fn.setText("");
        apellidoP.setText("");
        contraseña.setText("");
        user.setText("");
    }
    
    void bloquear(){
        rfc.setEnabled(false);
        nombre.setEnabled(false);
        domicilio.setEnabled(false);
        tel.setEnabled(false);
        fn.setEnabled(false);
        bnuevo.setEnabled(true);
        bguardar.setEnabled(false);
        bcancelar.setEnabled(false);  
        apellidoP.setEnabled(false);
        contraseña.setEnabled(false);
        user.setEnabled(false);
    }
    
    void habilitar(){
        rfc.setEnabled(true);
        nombre.setEnabled(true);
        domicilio.setEnabled(true);
        tel.setEnabled(true);
        fn.setEnabled(true);
        bnuevo.setEnabled(false);
        bguardar.setEnabled(true);
        bcancelar.setEnabled(true);  
        apellidoP.setEnabled(true);
        contraseña.setEnabled(true);
        user.setEnabled(true);
    }
    
    void actualizar(){
        rfc.setEnabled(true);
        nombre.setEnabled(true);
        domicilio.setEnabled(true);
        tel.setEnabled(true);
        fn.setEnabled(true);
        bactualizar.setEnabled(true);
        apellidoP.setEnabled(true);
        contraseña.setEnabled(true);
        user.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        apellidoP = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        rfc = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        domicilio = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        tel = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        user = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        contraseña = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        fn = new javax.swing.JTextField();
        bnuevo = new javax.swing.JButton();
        bactualizar = new javax.swing.JButton();
        bguardar = new javax.swing.JButton();
        bcancelar = new javax.swing.JButton();
        beliminar = new javax.swing.JButton();
        bmenu = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        aux = new javax.swing.JTextField();
        bmostrar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaVendedor = new javax.swing.JTable();
        idregistro = new javax.swing.JTextField();

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
        jScrollPane1.setViewportView(jTable1);

        jMenuItem1.setText("Modificar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("ID:");

        id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idActionPerformed(evt);
            }
        });

        jLabel2.setText("Nombre:");

        jLabel3.setText("Apellido:");

        jLabel4.setText("RFC:");

        jLabel5.setText("Domicilio:");

        jLabel6.setText("Telefono: ");

        jLabel7.setText("Usuario");

        user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userActionPerformed(evt);
            }
        });

        jLabel8.setText("Contraseña:");

        jLabel9.setText("Fecha de Nacimiento:");

        bnuevo.setText("Nuevo");
        bnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnuevoActionPerformed(evt);
            }
        });

        bactualizar.setText("Actualizar");
        bactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bactualizarActionPerformed(evt);
            }
        });

        bguardar.setText("Guardar");
        bguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bguardarActionPerformed(evt);
            }
        });

        bcancelar.setText("Cancelar");
        bcancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcancelarActionPerformed(evt);
            }
        });

        beliminar.setText("Eliminar");
        beliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beliminarActionPerformed(evt);
            }
        });

        bmenu.setText("Regresar");
        bmenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bmenuActionPerformed(evt);
            }
        });

        jLabel10.setText("Buscar:");

        aux.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                auxKeyReleased(evt);
            }
        });

        bmostrar.setText("Mostrar Todos");
        bmostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bmostrarActionPerformed(evt);
            }
        });

        tablaVendedor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tablaVendedor);

        idregistro.setText("jTextField1");
        idregistro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idregistroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(25, 25, 25)
                        .addComponent(aux, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(bmostrar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel4)
                                            .addGap(18, 18, 18)
                                            .addComponent(rfc))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel1)
                                            .addGap(18, 18, 18)
                                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(bnuevo)
                                            .addComponent(jLabel9))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(fn, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(bactualizar)
                                                .addGap(18, 18, 18)
                                                .addComponent(bguardar)))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel3)
                                        .addGap(18, 18, 18)
                                        .addComponent(apellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addGap(18, 18, 18)
                                                .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addGap(18, 18, 18)
                                                .addComponent(domicilio, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(21, 21, 21)
                                                .addComponent(bcancelar)
                                                .addGap(18, 18, 18)
                                                .addComponent(beliminar)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(jLabel8)
                                                .addGap(18, 18, 18)
                                                .addComponent(contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 227, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addGap(29, 29, 29)
                                                        .addComponent(jLabel6)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(tel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                        .addGap(18, 18, 18)
                                                        .addComponent(bmenu)))
                                                .addGap(0, 0, Short.MAX_VALUE)))))))
                        .addGap(144, 144, 144))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(idregistro, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(apellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(rfc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(domicilio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(tel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contraseña, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(fn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(39, 39, 39)
                .addComponent(idregistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bnuevo)
                    .addComponent(bactualizar)
                    .addComponent(bguardar)
                    .addComponent(bcancelar)
                    .addComponent(beliminar)
                    .addComponent(bmenu))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(aux, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bmostrar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idActionPerformed

    private void userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userActionPerformed

    private void bmenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bmenuActionPerformed
        // TODO add your handling code here:
        new Menu("gerente").setVisible(true);  
        this.dispose();
    }//GEN-LAST:event_bmenuActionPerformed

    private void idregistroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idregistroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idregistroActionPerformed

    private void bnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnuevoActionPerformed
        // TODO add your handling code here:
        habilitar();
        rfc.requestFocus();
    }//GEN-LAST:event_bnuevoActionPerformed

    private void bcancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcancelarActionPerformed
        // TODO add your handling code here:
        limpiar();
        bloquear();
    }//GEN-LAST:event_bcancelarActionPerformed

    private void bguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bguardarActionPerformed
        // TODO add your handling code here:
        if (rfc.getText().isEmpty() || 
        nombre.getText().isEmpty() || 
        apellidoP.getText().isEmpty() || 
        domicilio.getText().isEmpty() || 
        tel.getText().isEmpty() || 
        fn.getText().isEmpty() || 
        user.getText().isEmpty() || 
        contraseña.getText().isEmpty()) {

        JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.");
        return;
    }

    conectar cc = new conectar();
    
    try (Connection cn = cc.getConexion()) {
        // Inserta en la tabla registro
        String sqlRegistro = "INSERT INTO registro(nombre_usuario, hash_contraseña) VALUES (?,?)";
        try (PreparedStatement pstRegistro = cn.prepareStatement(sqlRegistro, Statement.RETURN_GENERATED_KEYS)) {
            pstRegistro.setString(1, user.getText());
            pstRegistro.setString(2, contraseña.getText());
            pstRegistro.executeUpdate();

            // Obtener el ID generado
            ResultSet rsRegistro = pstRegistro.getGeneratedKeys();
            int idRegistro = 0;
            if (rsRegistro.next()) {
                idRegistro = rsRegistro.getInt(1);
            } else {
                JOptionPane.showMessageDialog(null, "Error al obtener el ID del registro.");
                return;
            }
            idregistro.setText(String.valueOf(idRegistro));
        }

        // Inserta en la tabla ventanilla
        String sqlVentanilla = "INSERT INTO ventanilla (RFC, nombre, apellido_p, direccion, num_telefono, fecha_nacimiento, id_registro) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstVentanilla = cn.prepareStatement(sqlVentanilla)) {
            pstVentanilla.setString(1, rfc.getText());
            pstVentanilla.setString(2, nombre.getText());
            pstVentanilla.setString(3, apellidoP.getText());
            pstVentanilla.setString(4, domicilio.getText());
            pstVentanilla.setString(5, tel.getText());
            
            // Validar formato de fecha
            try {
                Date fechaNacimiento = Date.valueOf(fn.getText().trim());
                pstVentanilla.setDate(6, fechaNacimiento);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "La fecha debe tener el formato 'AAAA-MM-DD'.");
                return;
            }
            
            pstVentanilla.setInt(7, Integer.parseInt(idregistro.getText()));
            int n = pstVentanilla.executeUpdate();
            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Registro guardado exitosamente.");
                limpiar();
                bloquear();
                cargar("");
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + e.getMessage());
    }

    }//GEN-LAST:event_bguardarActionPerformed

    private void auxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_auxKeyReleased
        // TODO add your handling code here:
        cargar(aux.getText());
    }//GEN-LAST:event_auxKeyReleased

    private void bmostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bmostrarActionPerformed
        // TODO add your handling code here:
         aux.setText("");
        cargar("");
    }//GEN-LAST:event_bmostrarActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
           int fil = tablaVendedor.getSelectedRow();
    if (fil >= 0) {
        id.setText(tablaVendedor.getValueAt(fil, 0).toString());
        nombre.setText(tablaVendedor.getValueAt(fil, 1).toString());
        apellidoP.setText(tablaVendedor.getValueAt(fil, 2).toString()); // Nuevo campo
        rfc.setText(tablaVendedor.getValueAt(fil, 3).toString());
        domicilio.setText(tablaVendedor.getValueAt(fil, 4).toString());
        tel.setText(tablaVendedor.getValueAt(fil, 5).toString());
        fn.setText(tablaVendedor.getValueAt(fil, 6).toString());
        actualizar();
    } else {
        JOptionPane.showMessageDialog(null, "No se ha seleccionado una fila.");
    }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void bactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bactualizarActionPerformed
        // TODO add your handling code here:
        String sql = "UPDATE ventanilla SET RFC = ?, nombre = ?, apellido_p = ?, direccion = ?, num_telefono = ?, fecha_nacimiento = ? WHERE id_ventanilla = ?";
    conectar cc = new conectar();
    try (Connection cn = cc.getConexion();
         PreparedStatement pst = cn.prepareStatement(sql)) {

        pst.setString(1, rfc.getText());
        pst.setString(2, nombre.getText());
        pst.setString(3, apellidoP.getText()); // Nuevo campo
        pst.setString(4, domicilio.getText());
        pst.setString(5, tel.getText());
        pst.setDate(6, Date.valueOf(fn.getText()));
        pst.setInt(7, Integer.parseInt(id.getText()));

        int filasAfectadas = pst.executeUpdate();
        if (filasAfectadas > 0) {
            JOptionPane.showMessageDialog(null, "Registro actualizado correctamente.");
            cargar("");
            bloquear();
            limpiar();
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar los datos: " + e.getMessage());
    }     

    }//GEN-LAST:event_bactualizarActionPerformed

    private void beliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beliminarActionPerformed
        // TODO add your handling code here:
        int filaSeleccionada = tablaVendedor.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione un registro para eliminar");
        return;
    }
    
    String idVendedor = tablaVendedor.getValueAt(filaSeleccionada, 0).toString(); // Suponiendo que la primera columna es el ID
    
    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este registro?", 
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    if (confirmacion == JOptionPane.YES_OPTION) {
        conectar cc = new conectar();
        Connection cn = cc.getConexion();
        String sql = "DELETE FROM Ventanilla WHERE id_ventanilla = ? ";
        
        try {
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setString(1, idVendedor);
            int filasAfectadas = pst.executeUpdate();
            
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Registro eliminado correctamente");
                cargar(""); // Refrescar la tabla
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el registro: " + ex.getMessage());
        }
    }

    }//GEN-LAST:event_beliminarActionPerformed

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
            java.util.logging.Logger.getLogger(Vendedor1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Vendedor1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Vendedor1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Vendedor1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Vendedor1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField apellidoP;
    private javax.swing.JTextField aux;
    private javax.swing.JButton bactualizar;
    private javax.swing.JButton bcancelar;
    private javax.swing.JButton beliminar;
    private javax.swing.JButton bguardar;
    private javax.swing.JButton bmenu;
    private javax.swing.JButton bmostrar;
    private javax.swing.JButton bnuevo;
    private javax.swing.JTextField contraseña;
    private javax.swing.JTextField domicilio;
    private javax.swing.JTextField fn;
    private javax.swing.JTextField id;
    private javax.swing.JTextField idregistro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField rfc;
    private javax.swing.JTable tablaVendedor;
    private javax.swing.JTextField tel;
    private javax.swing.JTextField user;
    // End of variables declaration//GEN-END:variables
}
