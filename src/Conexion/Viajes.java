/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Conexion;

import com.toedter.calendar.JCalendar;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @jairb
 */
public class Viajes extends javax.swing.JFrame {

    /**
     * Creates new form Viajes
     */
    DefaultTableModel model;
    
    public Viajes() {
        initComponents();
        configurarDisenio();
        limpiar();
        bloquear();
        cargar("");
        cargarCamiones();
        inicializarFechaNacimiento();
        bactualizar.setEnabled(false);
    }

    private void configurarDisenio() {
        // Configuración del diseño monocromático oscuro
        Color fondoOscuro = new Color(40, 40, 40);
        Color textoClaro = new Color(200, 200, 200);
        Color acento = new Color(70, 130, 180);

        this.getContentPane().setBackground(fondoOscuro);
        
        // Aplicar colores a los componentes
        Component[] componentes = {
            aux, destino, fecha, hora_llegada, hora_salida, origen, id,
            bactualizar, bcancelar, beliminar, bguardar, bmostrar, bnuevo, brmenu,
            comboCamiones, jLabel1, jLabel2, jLabel3, jLabel4, jLabel5, jLabel6, jLabel7, jLabel8, jScrollPane1, tviajes
        };

        for (Component componente : componentes) {
            if (componente instanceof JLabel) {
                componente.setForeground(textoClaro);
            } else if (componente instanceof JButton) {
                componente.setBackground(acento);
                componente.setForeground(textoClaro);
                ((JButton) componente).setBorder(BorderFactory.createLineBorder(textoClaro));
            } else if (componente instanceof JTextField || componente instanceof JComboBox) {
                componente.setBackground(fondoOscuro);
                componente.setForeground(textoClaro);
                componente.setFont(new Font("SansSerif", Font.PLAIN, 14));
            } else if (componente instanceof JTable) {
                JTable tabla = (JTable) componente;
                tabla.setBackground(fondoOscuro);
                tabla.setForeground(textoClaro);
                tabla.setSelectionBackground(acento);
                tabla.setSelectionForeground(fondoOscuro);
                tabla.setGridColor(acento);
            }
        }

        jScrollPane1.getViewport().setBackground(fondoOscuro);
    }
    private void cargarCamiones() {
        conectar cc = new conectar();
    String sql = "SELECT id_autobus, placas FROM autobus"; // Consulta para obtener camiones
    
    try (Connection cn = cc.getConexion(); 
         Statement st = cn.createStatement(); 
         ResultSet rs = st.executeQuery(sql)) {

        // Limpia el combo box antes de llenarlo
        comboCamiones.removeAllItems();
        comboCamiones.addItem("Seleccionar..."); // Primera opción por defecto
        
        while (rs.next()) {
            String id = rs.getString("id_autobus");
            String placas = rs.getString("placas");
            comboCamiones.addItem(id + " - " + placas); // Agrega el ID y la matrícula
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar camiones: " + e.getMessage());
    }
}
    
    void cargar(String valor) {
        String[] titulos = {"ID", "Origen", "Destino", "Hora Inicio","Hora Fin","Fecha","Camion"};
        String[] registro = new String[7];
        model = new DefaultTableModel(null, titulos);
        String sql = "SELECT r.id_ruta, r.origen, r.destino, r.hora_inicio,r.hora_fin,r.fecha, a.placas " +
                 "FROM Ruta r " +
                 "LEFT JOIN Asignacion asi ON r.id_ruta = asi.id_ruta " +
                 "LEFT JOIN Autobus a ON asi.id_autobus = a.id_autobus";

        if (!valor.isEmpty()) {
        sql += " WHERE r.origen LIKE ? OR r.destino LIKE ? OR r.hora_inicio LIKE ? OR r.hora_fin LIKE ? OR r.fecha LIKE ? OR a.placas LIKE ?";
    }
   try (Connection cn = new conectar().getConexion(); 
         PreparedStatement pst = cn.prepareStatement(sql)) {

        if (!valor.isEmpty()) {
            for (int i = 1; i <= 6; i++) {
                pst.setString(i, "%" + valor + "%");
            }
        }

        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                registro[0] = rs.getString("id_ruta");
                registro[1] = rs.getString("origen");
                registro[2] = rs.getString("destino");
                registro[3] = rs.getString("hora_inicio");
                registro[4]= rs.getString("hora_fin");
                registro[5]=rs.getString("fecha");
                registro[6] = rs.getString("placas") != null ? rs.getString("placas") : "Sin asignar";

                model.addRow(registro);
            }
        }
            tviajes.setModel(model);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
        }
   
    }
    
    private void inicializarFechaNacimiento() {
    // Agregar un MouseListener para mostrar el JCalendar al hacer clic
    fecha.addMouseListener(new java.awt.event.MouseAdapter() {
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
        fecha.setText(sdf.format(sqlDate));
        
        // Cierra el diálogo
        dialog.dispose();
    }
});

    // Mostrar el diálogo centrado
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
}
    

    void limpiar(){
        origen.setText("");
        destino.setText("");
        hora_salida.setText("");
        hora_llegada.setText("");
        fecha.setText("");
    }
    
    void bloquear(){
        hora_llegada.setEnabled(false);
        fecha.setEnabled(false);
        origen.setEnabled(false);
        destino.setEnabled(false);
        hora_salida.setEnabled(false);
        comboCamiones.setEnabled(false);
        bnuevo.setEnabled(true);
        bguardar.setEnabled(false);
        bcancelar.setEnabled(false);
        
    }
    
    void habilitar(){
        hora_llegada.setEnabled(true);
        fecha.setEnabled(true);
        origen.setEnabled(true);
        destino.setEnabled(true);
        hora_salida.setEnabled(true);
        comboCamiones.setEnabled(true);
        bnuevo.setEnabled(false);
        bguardar.setEnabled(true);
        bcancelar.setEnabled(true);
    }
    
    void actualizar(){
        hora_llegada.setEnabled(true);
        fecha.setEnabled(true);
        origen.setEnabled(true);
        destino.setEnabled(true);
        hora_salida.setEnabled(true);
        comboCamiones.setEnabled(true);
        bactualizar.setEnabled(true);
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
        origen = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        destino = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        hora_salida = new javax.swing.JTextField();
        bnuevo = new javax.swing.JButton();
        bguardar = new javax.swing.JButton();
        bcancelar = new javax.swing.JButton();
        brmenu = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        comboCamiones = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tviajes = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        bactualizar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        aux = new javax.swing.JTextField();
        bmostrar = new javax.swing.JButton();
        beliminar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        hora_llegada = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        fecha = new javax.swing.JTextField();

        jMenuItem1.setText("Modificar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Viajes");
        setResizable(false);

        jLabel1.setText("Origen: ");

        jLabel2.setText("Destino: ");

        jLabel3.setText("Salida: ");

        bnuevo.setText("Nuevo");
        bnuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bnuevoActionPerformed(evt);
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

        brmenu.setText("RMenu");
        brmenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brmenuActionPerformed(evt);
            }
        });

        jLabel4.setText("Camion Asignado: ");

        comboCamiones.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecciona.." }));

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
        tviajes.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(tviajes);

        jLabel5.setText("ID: ");

        id.setEnabled(false);

        bactualizar.setText("Actualizar");
        bactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bactualizarActionPerformed(evt);
            }
        });

        jLabel6.setText("Buscar: ");

        aux.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                auxKeyReleased(evt);
            }
        });

        bmostrar.setText("Mostrar Todo");
        bmostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bmostrarActionPerformed(evt);
            }
        });

        beliminar.setText("Eliminar");
        beliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beliminarActionPerformed(evt);
            }
        });

        jLabel7.setText("Llegada:");

        hora_llegada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hora_llegadaActionPerformed(evt);
            }
        });

        jLabel8.setText("Fecha:");

        fecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(aux, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bmostrar))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addGap(10, 10, 10)
                                        .addComponent(hora_salida, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel7))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(origen, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(hora_llegada, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(18, 18, 18)
                                        .addComponent(comboCamiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(18, 18, 18)
                                        .addComponent(destino, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 59, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(bnuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bactualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bguardar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(bcancelar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(beliminar)
                .addGap(18, 18, 18)
                .addComponent(brmenu)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bcancelar, bguardar, bnuevo, brmenu});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(origen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(destino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(hora_salida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(comboCamiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(hora_llegada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bnuevo)
                    .addComponent(bguardar)
                    .addComponent(bcancelar)
                    .addComponent(bactualizar)
                    .addComponent(brmenu)
                    .addComponent(beliminar))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(aux, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bmostrar))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(80, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bnuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bnuevoActionPerformed
        // TODO add your handling code here:
        habilitar();
        origen.requestFocus();
    }//GEN-LAST:event_bnuevoActionPerformed

    private void bcancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcancelarActionPerformed
        // TODO add your handling code here:
        limpiar();
        bloquear();
    }//GEN-LAST:event_bcancelarActionPerformed

    private void bguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bguardarActionPerformed
       // Verifica que todos los campos estén llenos
    if (origen.getText().isEmpty() || 
        destino.getText().isEmpty() || 
        hora_salida.getText().isEmpty() || 
        hora_llegada.getText().isEmpty() ||
            fecha.getText().isEmpty() ||
        comboCamiones.getSelectedIndex() == 0) { // Validar que se haya seleccionado un camión

        JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.");
        return;
    }

    // Conectar a la base de datos
    conectar cc = new conectar();
    try (Connection cn = cc.getConexion()) { // Usamos try-with-resources para asegurar el cierre de la conexión
        // Insertar la ruta en la tabla `Ruta`
        String sqlRuta = "INSERT INTO Ruta (origen, destino, hora_inicio, hora_fin,fecha) " +
                 "VALUES (?, ?, ?, ?, ?)";
try (PreparedStatement pstRuta = cn.prepareStatement(sqlRuta, Statement.RETURN_GENERATED_KEYS)) {
    pstRuta.setString(1, origen.getText());
    pstRuta.setString(2, destino.getText());
    pstRuta.setString(3, hora_salida.getText());
    pstRuta.setString(4, hora_llegada.getText());
    pstRuta.setString(5, fecha.getText());

    int n = pstRuta.executeUpdate();
    
    if (n > 0) {
        try (ResultSet rs = pstRuta.getGeneratedKeys()) {
            if (rs.next()) {
                int idRuta = rs.getInt(1);

                        // Insertar la asignación en la tabla `Asignacion`
                        String sqlAsignacion = "INSERT INTO Asignacion (id_ruta, id_autobus) VALUES (?, ?)";
                        try (PreparedStatement pstAsignacion = cn.prepareStatement(sqlAsignacion)) {
                            // Extraer el ID del camión seleccionado
                            String camiónSeleccionado = (String) comboCamiones.getSelectedItem();
                            String idCamion = camiónSeleccionado.split(" - ")[0]; // Obtiene el ID del camión

                            pstAsignacion.setInt(1, idRuta);  // Asignamos el ID de la ruta
                            pstAsignacion.setString(2, idCamion);  // Asignamos el ID del camión

                            int filasAfectadas = pstAsignacion.executeUpdate();

                            if (filasAfectadas > 0) {
                                JOptionPane.showMessageDialog(null, "Registro guardado exitosamente.");
                                limpiar(); // Limpia los campos de texto
                                cargar(""); // Recarga la tabla de rutas
                            } else {
                                JOptionPane.showMessageDialog(null, "Error al asignar camión.");
                            }
                        } catch (SQLException e) {
                            JOptionPane.showMessageDialog(null, "Error al insertar asignación: " + e.getMessage());
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al obtener el ID de la ruta: " + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Error al guardar los datos de la ruta.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al insertar ruta: " + e.getMessage());
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + e.getMessage());
    }                             
    }//GEN-LAST:event_bguardarActionPerformed

    private void brmenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brmenuActionPerformed
        // TODO add your handling code here:
        new Menu("gerente").setVisible(true);  this.dispose();
    }//GEN-LAST:event_brmenuActionPerformed

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
        int fil=tviajes.getSelectedRow();
        if(fil>=0){
            id.setText(tviajes.getValueAt(fil, 0).toString());
            origen.setText(tviajes.getValueAt(fil, 1).toString());
            destino.setText(tviajes.getValueAt(fil, 2).toString());
            hora_salida.setText(tviajes.getValueAt(fil, 3).toString());
            hora_llegada.setText(tviajes.getValueAt(fil,4).toString());
            fecha.setText(tviajes.getValueAt(fil,5).toString());
            comboCamiones.setSelectedItem(tviajes.getValueAt(fil, 6));
            actualizar();
        }
        else{
            JOptionPane.showMessageDialog(null,"No Se A Seleccionado Una Fila");
        }
        
        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void bactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bactualizarActionPerformed
        // TODO add your handling code here:
        conectar cc = new conectar();
        Connection cn = cc.getConexion();
        try{
            PreparedStatement pst = cn.prepareStatement("UPDATE Ruta SET origen=?, destino=?, hora_inicio=?, hora_fin=?, fecha=? WHERE id_ruta=?");
            pst.setString(1, origen.getText());
            pst.setString(2, destino.getText());
            pst.setString(3, hora_salida.getText()); // Ajustar si es hora de inicio
            pst.setString(4, hora_llegada.getText()); // Ajusta para la hora de fin
            pst.setString(5,fecha.getText());
            pst.setInt(6, Integer.parseInt(id.getText()));
            pst.executeUpdate();
            cargar("");
            bactualizar.setEnabled(false);
            bloquear();
            limpiar();
        }
        catch(SQLException e){JOptionPane.showMessageDialog(null,e.getMessage());}
    }//GEN-LAST:event_bactualizarActionPerformed

    private void beliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beliminarActionPerformed
        int filaSeleccionada = tviajes.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione un registro para eliminar");
        return;
    }
    
    String idViaje = tviajes.getValueAt(filaSeleccionada, 0).toString(); // Suponiendo que la primera columna es el ID
    
    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este registro?", 
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    if (confirmacion == JOptionPane.YES_OPTION) {
        conectar cc = new conectar();
        Connection cn = cc.getConexion();
        String sql = "DELETE FROM Ruta WHERE id_ruta = ? ";
        
        try {
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setString(1, idViaje);
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

    private void hora_llegadaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hora_llegadaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hora_llegadaActionPerformed

    private void fechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaActionPerformed

    
    
    
    
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
            java.util.logging.Logger.getLogger(Viajes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Viajes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Viajes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Viajes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Viajes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField aux;
    private javax.swing.JButton bactualizar;
    private javax.swing.JButton bcancelar;
    private javax.swing.JButton beliminar;
    private javax.swing.JButton bguardar;
    private javax.swing.JButton bmostrar;
    private javax.swing.JButton bnuevo;
    private javax.swing.JButton brmenu;
    private javax.swing.JComboBox comboCamiones;
    private javax.swing.JTextField destino;
    private javax.swing.JTextField fecha;
    private javax.swing.JTextField hora_llegada;
    private javax.swing.JTextField hora_salida;
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
    private javax.swing.JTextField origen;
    private javax.swing.JTable tviajes;
    // End of variables declaration//GEN-END:variables
}
