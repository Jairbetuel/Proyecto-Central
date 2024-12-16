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
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 *@jairb
 */
public class Conductor extends javax.swing.JFrame {
    
    DefaultTableModel model;

    /**
     * Creates new form Conductor
     */

    public Conductor() {
        
        initComponents();
        limpiar();
        bloquear();
        cargar("");
        cargarCamiones();
        inicializarFechaNacimiento();

        // Establecer un LookAndFeel moderno
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Personalización del JFrame
        this.setTitle("Gestión de Conductores");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.getContentPane().setBackground(Color.BLACK); // Fondo negro

        // Personalización adicional
        jScrollPane1.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1)); // Bordes de tabla
        tablaConductor.setFillsViewportHeight(true);
        tablaConductor.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaConductor.setRowHeight(25);
        tablaConductor.setBackground(Color.BLACK);
        tablaConductor.setForeground(Color.WHITE);

        // Personalizar botones con un diseño monocromático
        configurarBoton(bnuevo, Color.DARK_GRAY);
        configurarBoton(bguardar, Color.DARK_GRAY);
        configurarBoton(bactualizar, Color.DARK_GRAY);
        configurarBoton(bcancelar, Color.DARK_GRAY);
        configurarBoton(eliminar, new Color(64, 64, 64));
        configurarBoton(bmenu, Color.DARK_GRAY);

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
            }
        }

        // Configuración del combo box
        comboCamiones.setFont(new Font("SansSerif", Font.PLAIN, 14));
        comboCamiones.setBackground(Color.BLACK);
        comboCamiones.setForeground(Color.WHITE);
    }

    private void configurarBoton(JButton boton, Color color) {
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(color.darker(), 1));
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
      

     private void inicializarFechaNacimiento() {
    // Agregar un MouseListener para mostrar el JCalendar al hacer clic
    fechaNacimiento.addMouseListener(new java.awt.event.MouseAdapter() {
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
        fechaNacimiento.setText(sdf.format(sqlDate));
        
        // Cierra el diálogo
        dialog.dispose();
    }
});

    // Mostrar el diálogo centrado
    dialog.setLocationRelativeTo(null);
    dialog.setVisible(true);
}
   void cargar(String valor) {
    // Definir los encabezados para todas las columnas
    String[] titulos = {"ID", "RFC", "Nombre", "Apellido Paterno", "Domicilio", "Teléfono", "Fecha Nacimiento", "Sueldo", "Camión Asignado", "Permiso Conducir"};
    String[] registro = new String[10]; // Cambia a 10 campos para incluir todos los valores

    DefaultTableModel model = new DefaultTableModel(null, titulos);

    // Consulta SQL actualizada para seleccionar todas las columnas que necesitas
    String sql = "SELECT c.id_chofer, c.RFC, c.nombre, c.apellido_p, c.domicilio, c.num_telefono, c.fecha_Nacimiento, c.sueldo, a.placas, c.permiso_conducir " +
                 "FROM Chofer c " +
                 "LEFT JOIN Conduccion con ON c.id_chofer = con.id_chofer " +
                 "LEFT JOIN Autobus a ON con.id_autobus = a.id_autobus";

    // Si hay un valor de búsqueda, agrega condiciones a la consulta
    if (!valor.isEmpty()) {
        sql += " WHERE c.nombre LIKE ? OR c.RFC LIKE ? OR c.num_telefono LIKE ? OR c.domicilio LIKE ? OR c.fecha_Nacimiento LIKE ? OR a.placas LIKE ?";
    }

    try (Connection cn = new conectar().getConexion(); 
         PreparedStatement pst = cn.prepareStatement(sql)) {

        // Si hay un valor de búsqueda, se asignan los parámetros
        if (!valor.isEmpty()) {
            for (int i = 1; i <= 6; i++) {
                pst.setString(i, "%" + valor + "%");
            }
        }

        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                // Rellenar cada columna con los datos obtenidos de la base de datos
                registro[0] = rs.getString("id_chofer");              // ID
                registro[1] = rs.getString("RFC");                    // RFC
                registro[2] = rs.getString("nombre");                 // Nombre
                registro[3] = rs.getString("apellido_p");             // Apellido paterno
                registro[4] = rs.getString("domicilio");              // Domicilio
                registro[5] = rs.getString("num_telefono");           // Teléfono
                registro[6] = rs.getString("fecha_Nacimiento");       // Fecha de nacimiento
                registro[7] = rs.getString("sueldo");                 // Sueldo
                registro[8] = rs.getString("placas") != null ? rs.getString("placas") : "Sin asignar"; // Camión asignado
                registro[9] = rs.getString("permiso_conducir");       // Permiso de conducir

                model.addRow(registro);  // Agregar la fila de datos a la tabla
            }
        }

        // Asignar el modelo a la tabla
        tablaConductor.setModel(model);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al cargar los datos: " + e.getMessage());
    }
}


    
    void limpiar(){
        sueldo.setText("");
        apellidoP.setText("");
        rfc.setText("");
        nombre.setText("");
        domicilio.setText("");
        telefono.setText("");
        fechaNacimiento.setText("");
    }
    
    void bloquear(){
        sueldo.setEnabled(false);
        apellidoP.setEnabled(false);
         rfc.setEnabled(false);
        nombre.setEnabled(false);
        domicilio.setEnabled(false);
        telefono.setEnabled(false);
        fechaNacimiento.setEnabled(false);
        comboCamiones.setEnabled(false);
        bnuevo.setEnabled(true);
        bguardar.setEnabled(false);
        bcancelar.setEnabled(false);   
        bactualizar.setEnabled(false);
    }
    
    void habilitar(){
        sueldo.setEnabled(true);
        apellidoP.setEnabled(true);
        rfc.setEnabled(true);
        nombre.setEnabled(true);
        domicilio.setEnabled(true);
        telefono.setEnabled(true);
        fechaNacimiento.setEnabled(true);
        comboCamiones.setEnabled(true);
        bnuevo.setEnabled(false);
        bguardar.setEnabled(true);
        bcancelar.setEnabled(true);        
    }
    
    void actualizar(){
        sueldo.setEnabled(true);
        apellidoP.setEnabled(true);
        rfc.setEnabled(true);
        nombre.setEnabled(true);
        domicilio.setEnabled(true);
        telefono.setEnabled(true);
        fechaNacimiento.setEnabled(true);
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
        rfc = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        domicilio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        telefono = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        fechaNacimiento = new javax.swing.JTextField();
        bnuevo = new javax.swing.JButton();
        bguardar = new javax.swing.JButton();
        bcancelar = new javax.swing.JButton();
        bmenu = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaConductor = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        aux = new javax.swing.JTextField();
        bmostar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        comboCamiones = new javax.swing.JComboBox();
        bactualizar = new javax.swing.JButton();
        id = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        eliminar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        sueldo = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        apellidoP = new javax.swing.JTextField();

        jMenuItem1.setText("Modificar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Conductor");
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(255, 51, 51));
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);

        jLabel1.setText("RFC: ");

        rfc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rfcActionPerformed(evt);
            }
        });

        jLabel2.setText("Nombre: ");

        nombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombreActionPerformed(evt);
            }
        });

        jLabel3.setText("Domicilio: ");

        domicilio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                domicilioActionPerformed(evt);
            }
        });

        jLabel4.setText("Telefono: ");

        telefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                telefonoActionPerformed(evt);
            }
        });

        jLabel5.setText("Fecha de Nacimiento: ");

        fechaNacimiento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fechaNacimientoMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fechaNacimientoMousePressed(evt);
            }
        });
        fechaNacimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fechaNacimientoActionPerformed(evt);
            }
        });

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

        bmenu.setText("RMenu");
        bmenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bmenuActionPerformed(evt);
            }
        });

        tablaConductor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tablaConductor.setComponentPopupMenu(jPopupMenu1);
        jScrollPane1.setViewportView(tablaConductor);

        jLabel6.setText("Buscar: ");

        aux.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                auxKeyReleased(evt);
            }
        });

        bmostar.setText("Mostrar Todos");
        bmostar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bmostarActionPerformed(evt);
            }
        });

        jLabel7.setText("Asignar Camion: ");

        comboCamiones.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Selecciona.." }));

        bactualizar.setText("Actualizar");
        bactualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bactualizarActionPerformed(evt);
            }
        });

        id.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Microsoft Sans Serif", 0, 15)); // NOI18N
        jLabel8.setText("ID: ");

        eliminar.setText("Eliminar");
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
            }
        });

        jLabel9.setText("Sueldo");

        sueldo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sueldoActionPerformed(evt);
            }
        });

        jLabel10.setText("Apellido");

        apellidoP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apellidoPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(comboCamiones, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(fechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(telefono, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 20, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addGap(18, 18, 18)
                                        .addComponent(aux, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(38, 38, 38)
                                        .addComponent(bmostar))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addComponent(bnuevo)
                                        .addGap(16, 16, 16)
                                        .addComponent(bactualizar)
                                        .addGap(18, 18, 18)
                                        .addComponent(bguardar)
                                        .addGap(18, 18, 18)
                                        .addComponent(bcancelar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(eliminar)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(bmenu, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(30, 30, 30)
                                                .addComponent(jLabel2)
                                                .addGap(18, 18, 18)
                                                .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(2, 2, 2)
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(rfc, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel9)
                                                .addGap(18, 18, 18)
                                                .addComponent(sueldo)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(domicilio, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel10)
                                                .addGap(18, 18, 18)
                                                .addComponent(apellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bactualizar, bcancelar, bguardar, bmenu, bnuevo});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel2)
                            .addComponent(nombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(apellidoP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(sueldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(domicilio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(rfc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(comboCamiones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fechaNacimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)
                        .addComponent(telefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bnuevo)
                    .addComponent(bguardar)
                    .addComponent(bcancelar)
                    .addComponent(bmenu)
                    .addComponent(bactualizar)
                    .addComponent(eliminar))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(aux, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bmostar))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void rfcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rfcActionPerformed
        // TODO add your handling code here:
        rfc.requestFocus();
    }//GEN-LAST:event_rfcActionPerformed

    private void nombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombreActionPerformed
        // TODO add your handling code here:
        nombre.requestFocus();
    }//GEN-LAST:event_nombreActionPerformed

    private void domicilioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_domicilioActionPerformed
        // TODO add your handling code here:
        domicilio.requestFocus();
    }//GEN-LAST:event_domicilioActionPerformed

    private void telefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_telefonoActionPerformed
        // TODO add your handling code here:
        telefono.requestFocus();
    }//GEN-LAST:event_telefonoActionPerformed

    private void fechaNacimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fechaNacimientoActionPerformed
 
        fechaNacimiento.requestFocus();
                        
    }//GEN-LAST:event_fechaNacimientoActionPerformed

    private void bguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bguardarActionPerformed
                                       
    // Verifica que todos los campos estén llenos, incluyendo sueldo
    if (rfc.getText().isEmpty() || 
        nombre.getText().isEmpty() || 
        apellidoP.getText().isEmpty() || 
        domicilio.getText().isEmpty() || 
        telefono.getText().isEmpty() || 
        fechaNacimiento.getText().isEmpty() ||
        sueldo.getText().isEmpty() ||    // Asegúrate de que sueldo no esté vacío
        comboCamiones.getSelectedIndex() == 0) { // Validar que se haya seleccionado un camión

        JOptionPane.showMessageDialog(null, "Por favor, completa todos los campos.");
        return;
    }

    // Conectar a la base de datos
    conectar cc = new conectar();
    Connection cn = cc.getConexion();
    
    try {
        // Insertar el chofer en la tabla `chofer`, incluyendo el permiso de conducir
        String sqlChofer = "INSERT INTO chofer (RFC, nombre, apellido_p, domicilio, num_telefono, fecha_Nacimiento, sueldo, permiso_conducir) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        PreparedStatement pstChofer = cn.prepareStatement(sqlChofer, Statement.RETURN_GENERATED_KEYS);
        pstChofer.setString(1, rfc.getText());               // RFC
        pstChofer.setString(2, nombre.getText());            // Nombre
        pstChofer.setString(3, apellidoP.getText());         // Primer Apellido
        pstChofer.setString(4, domicilio.getText());         // Domicilio
        pstChofer.setString(5, telefono.getText());          // Teléfono
        pstChofer.setString(6, fechaNacimiento.getText());   // Fecha de nacimiento
        pstChofer.setString(7, sueldo.getText());            // Sueldo
        pstChofer.setString(8, "A1");                        // Permiso de conducir (se inserta automáticamente)

        int n = pstChofer.executeUpdate();
        
        if (n > 0) {
            // Obtener el ID del chofer recién insertado
            ResultSet rs = pstChofer.getGeneratedKeys();
            if (rs.next()) {
                int idChofer = rs.getInt(1);

                // Ahora insertamos la relación en la tabla `conduccion`
                String sqlConduccion = "INSERT INTO conduccion (id_chofer, id_autobus) VALUES (?, ?)";
                PreparedStatement pstConduccion = cn.prepareStatement(sqlConduccion);

                // Extraer el ID del camión seleccionado
                String camiónSeleccionado = (String) comboCamiones.getSelectedItem();
                String idCamion = camiónSeleccionado.split(" - ")[0]; // Obtiene el ID del camión

                pstConduccion.setInt(1, idChofer);  // Asignamos el ID del chofer
                pstConduccion.setString(2, idCamion);  // Asignamos el ID del camión

                int filasAfectadas = pstConduccion.executeUpdate();
                
                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(null, "Registro guardado exitosamente.");
                    limpiar(); // Limpia los campos de texto
                    cargar(""); // Recarga la tabla de choferes
                } else {
                    JOptionPane.showMessageDialog(null, "Error al asignar camión.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Error al guardar los datos del chofer.");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al guardar los datos: " + e.getMessage());
    }

    }//GEN-LAST:event_bguardarActionPerformed

    private void bmenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bmenuActionPerformed
        // TODO add your handling code here:
        new Menu("gerente").setVisible(true);  this.dispose();
    }//GEN-LAST:event_bmenuActionPerformed

    private void auxKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_auxKeyReleased
        // TODO add your handling code here:
        cargar(aux.getText());
    }//GEN-LAST:event_auxKeyReleased

    private void bmostarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bmostarActionPerformed
        // TODO add your handling code here:
        aux.setText("");
        cargar("");
    }//GEN-LAST:event_bmostarActionPerformed

    private void bactualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bactualizarActionPerformed
     conectar cc = new conectar();
Connection cn = cc.getConexion();

if (cn == null) {
    JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos");
    return;
}

try {
    String sql = "UPDATE Chofer SET RFC = ?, nombre = ?, apellido_p = ?, domicilio = ?, num_telefono = ?, fecha_Nacimiento = ?, sueldo = ? WHERE id_chofer = ?";
    PreparedStatement pst = cn.prepareStatement(sql);

    // Validar que los campos no estén vacíos
    if (rfc.getText().trim().isEmpty() || nombre.getText().trim().isEmpty() || apellidoP.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos requeridos.");
        return;
    }

    // Asignar valores a los parámetros
    pst.setString(1, rfc.getText().trim());
    pst.setString(2, nombre.getText().trim());
    pst.setString(3, apellidoP.getText().trim());
    pst.setString(4, domicilio.getText().trim());
    pst.setString(5, telefono.getText().trim());
    pst.setString(6, fechaNacimiento.getText().trim());
    pst.setString(7, sueldo.getText().trim());
    pst.setInt(8, Integer.parseInt(id.getText().trim()));

    int filasAfectadas = pst.executeUpdate();

    if (filasAfectadas > 0) {
        // Actualizar la asignación de camión en la tabla `conduccion`
        String camiónSeleccionado = (String) comboCamiones.getSelectedItem();

        // Verificar si la selección del camión es válida
        if (camiónSeleccionado.contains(" - ")) {
    String idCamion = camiónSeleccionado.split(" - ")[0];  // Obtener el ID del camión
    if (idCamion.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Por favor, seleccione un camión válido.");
        return;
    }

    String sqlConduccion = "UPDATE conduccion SET id_autobus = ? WHERE id_chofer = ?";
    PreparedStatement pstConduccion = cn.prepareStatement(sqlConduccion);
    pstConduccion.setString(1, idCamion);  // id del camión
    pstConduccion.setInt(2, Integer.parseInt(id.getText()));  // id del chofer

    try {
    int filasConduccion = pstConduccion.executeUpdate();
    if (filasConduccion > 0) {
        JOptionPane.showMessageDialog(null, "Registro actualizado exitosamente");
    } else {
        JOptionPane.showMessageDialog(null, "No se pudo actualizar la asignación del camión");
    }
} catch (SQLException e) {
    JOptionPane.showMessageDialog(null, "Error al actualizar la asignación del camión: " + e.getMessage());
}

} else {
    JOptionPane.showMessageDialog(null, "Seleccione un camión válido.");
}

    } else {
        JOptionPane.showMessageDialog(null, "No se encontró el registro para actualizar");
    }
} catch (SQLException e) {
    e.printStackTrace();  // Imprimir el stack trace para diagnóstico
    JOptionPane.showMessageDialog(null, "Error al actualizar: " + e.getMessage());
} catch (NumberFormatException e) {
    JOptionPane.showMessageDialog(null, "El ID debe ser un número válido");
}

    }//GEN-LAST:event_bactualizarActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        int fila = tablaConductor.getSelectedRow(); // Obtiene la fila seleccionada

    if (fila >= 0) {
        // Asigna valores desde la tabla a los campos de texto
        id.setText(tablaConductor.getValueAt(fila, 0).toString());
        rfc.setText(tablaConductor.getValueAt(fila, 1).toString());
        nombre.setText(tablaConductor.getValueAt(fila, 2).toString());
        apellidoP.setText(tablaConductor.getValueAt(fila, 3).toString());
        domicilio.setText(tablaConductor.getValueAt(fila, 4).toString());
        telefono.setText(tablaConductor.getValueAt(fila, 5).toString());
        fechaNacimiento.setText(tablaConductor.getValueAt(fila, 6).toString());
        sueldo.setText(tablaConductor.getValueAt(fila, 7).toString());

        // Obtiene la placa del camión asignado y selecciona en el comboBox
        String placa = tablaConductor.getValueAt(fila, 8).toString();
        comboCamiones.setSelectedItem(placa);

        actualizar(); // Método que activa y desbloquea la edición
    } else {
        JOptionPane.showMessageDialog(null, "No se ha seleccionado una fila");
    }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void sueldoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sueldoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sueldoActionPerformed

    private void apellidoPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apellidoPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_apellidoPActionPerformed

    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
       int filaSeleccionada = tablaConductor.getSelectedRow();
    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione un registro para eliminar");
        return;
    }
    
    String idConductor = tablaConductor.getValueAt(filaSeleccionada, 0).toString(); // Suponiendo que la primera columna es el ID
    
    int confirmacion = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este registro?", 
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
    if (confirmacion == JOptionPane.YES_OPTION) {
        conectar cc = new conectar();
        Connection cn = cc.getConexion();
        String sql = "DELETE FROM chofer WHERE id_chofer = ? ";
        
        try {
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setString(1, idConductor);
            int filasAfectadas = pst.executeUpdate();
            
            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Registro eliminado correctamente");
                cargar(""); // Refrescar la tabla
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el registro: " + ex.getMessage());
        }
    }
    }//GEN-LAST:event_eliminarActionPerformed

    private void fechaNacimientoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fechaNacimientoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_fechaNacimientoMousePressed

    private void fechaNacimientoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fechaNacimientoMouseClicked
 
    }//GEN-LAST:event_fechaNacimientoMouseClicked

    
    
    
    
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
            java.util.logging.Logger.getLogger(Conductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Conductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Conductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Conductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Conductor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField apellidoP;
    private javax.swing.JTextField aux;
    private javax.swing.JButton bactualizar;
    private javax.swing.JButton bcancelar;
    private javax.swing.JButton bguardar;
    private javax.swing.JButton bmenu;
    private javax.swing.JButton bmostar;
    private javax.swing.JButton bnuevo;
    private javax.swing.JComboBox comboCamiones;
    private javax.swing.JTextField domicilio;
    private javax.swing.JButton eliminar;
    private javax.swing.JTextField fechaNacimiento;
    private javax.swing.JTextField id;
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
    private javax.swing.JTextField nombre;
    private javax.swing.JTextField rfc;
    private javax.swing.JTextField sueldo;
    private javax.swing.JTable tablaConductor;
    private javax.swing.JTextField telefono;
    // End of variables declaration//GEN-END:variables
}
