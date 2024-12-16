/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Conexion;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.sql.*;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @jairb
 */
public class Camion extends javax.swing.JFrame {
    
    DefaultTableModel model;

    /**
     * Creates new form Camion
     */
    public Camion() {
        initComponents();
        limpiar();
        bloquear();
        cargar("");
        btnActualizar.setEnabled(false);

        // Establecer un LookAndFeel moderno
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Personalización del JFrame
        this.setTitle("Gestión de Camiones");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.BLACK); // Fondo negro

        // Personalización adicional
        jScrollPane1.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1)); // Bordes de tabla
        tablaCamion.setFillsViewportHeight(true);
        tablaCamion.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaCamion.setRowHeight(25);
        tablaCamion.setBackground(Color.BLACK);
        tablaCamion.setForeground(Color.WHITE);

        // Personalizar botones con un diseño monocromático
        configurarBoton(btnNuevo, Color.DARK_GRAY);
        configurarBoton(btnGuardar, Color.DARK_GRAY);
        configurarBoton(btnActualizar, Color.DARK_GRAY);
        configurarBoton(btnCancelar, Color.DARK_GRAY);
        configurarBoton(beliminar, new Color(64, 64, 64));
        configurarBoton(btnMenu, Color.DARK_GRAY);

        // Personalizar etiquetas y campos de texto
        for (Component c : this.getContentPane().getComponents()) {
            if (c instanceof JLabel) {
                c.setFont(new Font("SansSerif", Font.BOLD, 14));
                c.setForeground(Color.WHITE);
            } else if (c instanceof JTextField) {
                ((JTextField) c).setFont(new Font("SansSerif", Font.PLAIN, 14));
                ((JTextField) c).setBackground(Color.BLACK);
                ((JTextField) c).setForeground(Color.WHITE);
                ((JTextField) c).setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
            } else if (c instanceof JComboBox) {
                ((JComboBox<?>) c).setFont(new Font("SansSerif", Font.PLAIN, 14));
                ((JComboBox<?>) c).setBackground(Color.BLACK);
                ((JComboBox<?>) c).setForeground(Color.WHITE);
            }
        }
    }

    private void configurarBoton(JButton boton, Color color) {
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(color.darker(), 1));
    }

    void cargar(String valor) {
        // Cargar categorías al inicio
String[] categorias = {"Económico", "Lujo", "Ejecutivo"}; // Estas son las categorías de ejemplo
for (String categoria : categorias) {
    cCategoria.addItem(categoria);
}

    // Se agrega todas las columnas de la base de datos
    String[] titulos = {"ID", "Matricula", "Año", "Marca", "Modelo", "Capacidad", "Estado", "Categoría"};
    String[] registro = new String[8]; // 8 columnas: 7 de los datos más el ID
    model = new DefaultTableModel(null, titulos);

    String sql = "SELECT * FROM autobus";
    
    // Si hay un valor para buscar, se agregan filtros a la consulta
    if (!valor.isEmpty()) {
        sql += " WHERE " + "id_autobus LIKE '%" + valor + "%'" +
                " OR placas LIKE '%" + valor + "%'" +
               " OR año LIKE '%" + valor + "%'" +
               " OR marca LIKE '%" + valor + "%'" +
               " OR modelo LIKE '%" + valor + "%'" +
               " OR capacidad LIKE '%" + valor + "%'";
    }

    conectar cc = new conectar();
    Connection cn = cc.getConexion();

    try {
        Statement st = cn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            // Obtener todos los datos de la base de datos
            registro[0] = rs.getString("id_autobus");  // ID
            registro[1] = rs.getString("placas");      // Matricula
            registro[2] = rs.getString("año").substring(0, 4);         // Año (esto es solo el año ya que es tipo YEAR)
            registro[3] = rs.getString("marca");       // Marca
            registro[4] = rs.getString("modelo");      // Modelo
            registro[5] = rs.getString("capacidad");   // Capacidad
            registro[6] = rs.getString("estado");      // Estado
            registro[7] = rs.getString("categoria");   // Categoría
            model.addRow(registro); // Añadir los datos a la tabla
        }

        tablaCamion.setModel(model);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
    }
}


    
    
    
    void limpiar(){
        matricula.setText("");
        año.setText("");
        marca.setText("");
        modelo.setText("");
        capacidad.setText("");
    }
    
    void bloquear(){
        matricula.setEnabled(false);
        año.setEnabled(false);
        marca.setEnabled(false);
        modelo.setEnabled(false);
        capacidad.setEnabled(false);
        btnNuevo.setEnabled(true);
        btnGuardar.setEnabled(false);
        btnCancelar.setEnabled(false);        
    }
    
    void habilitar(){
        matricula.setEnabled(true);
        año.setEnabled(true);
        marca.setEnabled(true);
        modelo.setEnabled(true);
        capacidad.setEnabled(true);
        btnNuevo.setEnabled(false);
        btnGuardar.setEnabled(true);
        btnCancelar.setEnabled(true);        
    }
    
    void actualizar(){
        matricula.setEnabled(true);
        año.setEnabled(true);
        marca.setEnabled(true);
        modelo.setEnabled(true);
        capacidad.setEnabled(true);
        btnActualizar.setEnabled(true);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        matricula = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        año = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        modelo = new javax.swing.JTextField();
        btnNuevo = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnMenu = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaCamion = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        buscar = new javax.swing.JTextField();
        btnMostrar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        capacidad = new javax.swing.JTextField();
        marca = new javax.swing.JTextField();
        btnActualizar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        beliminar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        cCategoria = new javax.swing.JComboBox<>();

        jMenuItem1.setText("Modificar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Camion");
        setResizable(false);

        jLabel1.setText("Placas:");

        jLabel2.setText("Año: ");

        jLabel3.setText("Marca: ");

        jLabel4.setText("Modelo: ");

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnMenu.setText("RMenu");
        btnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenuActionPerformed(evt);
            }
        });

        tablaCamion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaCamion.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(tablaCamion);

        jLabel5.setText("Buscar: ");

        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarKeyReleased(evt);
            }
        });

        btnMostrar.setText("Mostrar Todos");
        btnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarActionPerformed(evt);
            }
        });

        jLabel6.setText("Capacidad: ");

        capacidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                capacidadActionPerformed(evt);
            }
        });

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        jLabel7.setText("ID: ");

        id.setEnabled(false);
        id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                idActionPerformed(evt);
            }
        });

        beliminar.setText("Eliminar");
        beliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beliminarActionPerformed(evt);
            }
        });

        jLabel8.setText("Categoria:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(marca, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(jLabel4)
                .addGap(21, 21, 21)
                .addComponent(modelo)
                .addGap(12, 12, 12))
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(btnNuevo)
                .addGap(18, 18, 18)
                .addComponent(btnActualizar)
                .addGap(18, 18, 18)
                .addComponent(btnGuardar)
                .addGap(18, 18, 18)
                .addComponent(btnCancelar)
                .addGap(18, 18, 18)
                .addComponent(beliminar)
                .addGap(18, 18, 18)
                .addComponent(btnMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(año, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(matricula, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(capacidad, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(27, 27, 27)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(btnMostrar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnActualizar, btnCancelar, btnGuardar, btnMenu, btnNuevo});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(modelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(capacidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(matricula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(cCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(año, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNuevo)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar)
                    .addComponent(btnMenu)
                    .addComponent(btnActualizar)
                    .addComponent(beliminar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMostrar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(52, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // TODO add your handling code here:
        habilitar();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        limpiar();
        bloquear();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
      // Verifica si los campos están completos
    if (matricula.getText().isEmpty() || 
        año.getText().isEmpty() || 
        marca.getText().isEmpty() || 
        modelo.getText().isEmpty() || 
        capacidad.getText().isEmpty()) {
        
        JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.");
        return;
    }
    
    conectar cc = new conectar();
    Connection cn = cc.getConexion();
    
    // Modificar la consulta para incluir la columna `categoria`
    String sql = "INSERT INTO autobus (placas, año, marca, modelo, capacidad, estado, categoria) VALUES (?, ?, ?, ?, ?, 'activo', ?)";
    
    try {
        PreparedStatement pst = cn.prepareStatement(sql);
        pst.setString(1, matricula.getText());
        pst.setString(2, año.getText());
        pst.setString(3, marca.getText());
        pst.setString(4, modelo.getText());
        pst.setString(5, capacidad.getText());
        pst.setString(6, (String) cCategoria.getSelectedItem()); // Obtener la categoría seleccionada

        int n = pst.executeUpdate();
        if (n > 0) {
            JOptionPane.showMessageDialog(null, "Registro guardado exitosamente.");
            limpiar(); // Limpia los campos de texto
            bloquear(); // Bloquea los campos para evitar cambios no deseados
            cargar(""); // Actualiza la tabla para reflejar el nuevo registro
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + e.getMessage());
    }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenuActionPerformed
        // TODO add your handling code here:
        new Menu("gerente").setVisible(true);  this.dispose();
    }//GEN-LAST:event_btnMenuActionPerformed

    private void buscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarKeyReleased
        // TODO add your handling code here:
        cargar(buscar.getText());
    }//GEN-LAST:event_buscarKeyReleased

    private void btnMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarActionPerformed
        // TODO add your handling code here:
        buscar.setText("");
        cargar("");
    }//GEN-LAST:event_btnMostrarActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        int fil=tablaCamion.getSelectedRow();
        if(fil>=0){
            id.setText(tablaCamion.getValueAt(fil, 0).toString());
            matricula.setText(tablaCamion.getValueAt(fil, 1).toString());
            año.setText(tablaCamion.getValueAt(fil, 2).toString());
            marca.setText(tablaCamion.getValueAt(fil, 3).toString());
            modelo.setText(tablaCamion.getValueAt(fil, 4).toString());
            capacidad.setText(tablaCamion.getValueAt(fil, 5).toString());
            actualizar();
        }
        else{
            JOptionPane.showMessageDialog(null,"No Se A Seleccionado Una Fila");
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        conectar cc = new conectar();
    Connection cn = cc.getConexion();

        try{
            PreparedStatement pst = cn.prepareStatement("UPDATE autobus SET matricula='"+matricula.getText()+"',año='"+año.getText()+"',marca='"+marca.getText()+"',modelo='"+modelo.getText()+"',capacidad='"+capacidad.getText()+"' WHERE id_autobus='"+id.getText()+"'");
            pst.executeUpdate();
            cargar("");
            btnActualizar.setEnabled(false);
            bloquear();
            limpiar();
        }
        catch(SQLException e){JOptionPane.showMessageDialog(null,e.getMessage());}
        
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_idActionPerformed

    private void beliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beliminarActionPerformed
        int filaSeleccionada = tablaCamion.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione un registro para eliminar");
        return;
    }
    
    String idAutobus = tablaCamion.getValueAt(filaSeleccionada, 0).toString(); // Suponiendo que la primera columna es el ID
    
    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este registro?", 
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    if (confirmacion == JOptionPane.YES_OPTION) {
        conectar cc = new conectar();
        Connection cn = cc.getConexion();
        String sql = "DELETE FROM Autobus WHERE id_autobus = ? ";
        
        try {
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setString(1, idAutobus);
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

    private void capacidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_capacidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_capacidadActionPerformed

    
    
    
    
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
            java.util.logging.Logger.getLogger(Camion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Camion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Camion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Camion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Camion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField año;
    private javax.swing.JButton beliminar;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnMenu;
    private javax.swing.JButton btnMostrar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JTextField buscar;
    private javax.swing.JComboBox<String> cCategoria;
    private javax.swing.JTextField capacidad;
    private javax.swing.JTextField id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField marca;
    private javax.swing.JTextField matricula;
    private javax.swing.JTextField modelo;
    private javax.swing.JTable tablaCamion;
    // End of variables declaration//GEN-END:variables
}
