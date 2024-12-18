/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Conexion;

/**
 *
 * @jairb
 */
public class Menu extends javax.swing.JFrame {
    private String usuario;
    /**
     * Creates new form Menu
     */
    public Menu(String usuario) {
        this.usuario = usuario;
        initComponents();
        configurarVisibilidad();
    }
private void configurarVisibilidad() {
        if (!"gerente".equals(usuario)) {
            bconductor.setVisible(false);
            bvendedor.setVisible(false);
            bcamion.setVisible(false);
            bviajes.setVisible(false);
        }else{
            bboleto.setVisible(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jButton4 = new javax.swing.JButton();
        bboleto = new javax.swing.JButton();
        bconductor = new javax.swing.JButton();
        bvendedor = new javax.swing.JButton();
        bcamion = new javax.swing.JButton();
        bsalir = new javax.swing.JButton();
        bviajes = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        jButton4.setText("jButton4");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Menu");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        bboleto.setText("Boleto");
        bboleto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bboletoActionPerformed(evt);
            }
        });

        bconductor.setText("Conductor");
        bconductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bconductorActionPerformed(evt);
            }
        });

        bvendedor.setText("Vendedor");
        bvendedor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bvendedorActionPerformed(evt);
            }
        });

        bcamion.setText("Camion");
        bcamion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcamionActionPerformed(evt);
            }
        });

        bsalir.setText("Salir");
        bsalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsalirActionPerformed(evt);
            }
        });

        bviajes.setText("Viajes");
        bviajes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bviajesActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/1.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bcamion)
                .addGap(18, 18, 18)
                .addComponent(bconductor)
                .addGap(18, 18, 18)
                .addComponent(bboleto)
                .addGap(18, 18, 18)
                .addComponent(bviajes)
                .addGap(18, 18, 18)
                .addComponent(bvendedor)
                .addGap(18, 18, 18)
                .addComponent(bsalir, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bboleto, bcamion, bconductor, bsalir, bvendedor, bviajes});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bcamion)
                    .addComponent(bconductor)
                    .addComponent(bboleto)
                    .addComponent(bviajes)
                    .addComponent(bvendedor)
                    .addComponent(bsalir))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bboletoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bboletoActionPerformed
        // TODO add your handling code here:
        new Boleto().setVisible(true);
        this.dispose(); 
    }//GEN-LAST:event_bboletoActionPerformed

    private void bconductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bconductorActionPerformed
        // TODO add your handling code here:
        new Conductor().setVisible(true);  this.dispose();
    }//GEN-LAST:event_bconductorActionPerformed

    private void bvendedorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bvendedorActionPerformed
        // TODO add your handling code here:
        new Vendedor1().setVisible(true);  this.dispose();
    }//GEN-LAST:event_bvendedorActionPerformed

    private void bcamionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcamionActionPerformed
        // TODO add your handling code here:
        new Camion().setVisible(true);  this.dispose();
    }//GEN-LAST:event_bcamionActionPerformed

    private void bsalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsalirActionPerformed
        // TODO add your handling code here:
        new Menuprincipal().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_bsalirActionPerformed

    private void bviajesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bviajesActionPerformed
        // TODO add your handling code here:
        new Viajes().setVisible(true); this.dispose();
    }//GEN-LAST:event_bviajesActionPerformed

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
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu("").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bboleto;
    private javax.swing.JButton bcamion;
    private javax.swing.JButton bconductor;
    private javax.swing.JButton bsalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton bvendedor;
    private javax.swing.JButton bviajes;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables


}
