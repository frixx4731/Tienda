/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Configuraciones;
import Consultas.*;
import ConexionDB.*;
import INVENTARIO.*;
import DBObjetos.Usuario;
import DBObjetos.Usuario.Rol;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import login.*;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;



/**
 *
 * @author braul
 */
public class Configuraciones extends javax.swing.JFrame {

    
   private Principal2_0 ventanaPrincipal;
 
    public Configuraciones() {
        initComponents();
        configurarEncabezadosTabla();
        
        ajustarTamanioColumnasYFilas();
        configurarTablaYEventos();

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarYVolver();
            }
        });
      
        ajustarAlturaFilas();
        
    }
    
    
    private void ajustarAlturaFilas() {
    for (int row = 0; row < Empleados.getRowCount(); row++) {
        int rowHeight = Empleados.getRowHeight(row); // Obtén la altura actual de la fila

        for (int column = 0; column < Empleados.getColumnCount(); column++) {
            TableCellRenderer renderer = Empleados.getCellRenderer(row, column);
            Component comp = Empleados.prepareRenderer(renderer, row, column);
            rowHeight = Math.max(rowHeight, comp.getPreferredSize().height); // Encuentra el máximo entre la altura actual y la del componente
        }

        Empleados.setRowHeight(row, rowHeight); // Establece la nueva altura de la fila
    }
}

    
        public void setVentanaPrincipal(Principal2_0 principal) {
        this.ventanaPrincipal = principal;
    }

    private void cerrarYVolver() {
        if (ventanaPrincipal != null) {
            ventanaPrincipal.setVisible(true); // Hace visible la ventana principal nuevamente
        }
        dispose(); // Cierra esta ventana
    }
    
    

    private void configurarTablaYEventos() {
    Icon eliminarIcon = new ImageIcon(getClass().getResource("/icons/Eliminar.png"));
    Icon modificarIcon = new ImageIcon(getClass().getResource("/icons/Modificar.png"));

    
    ButtonRenderer buttonRendererEliminar = new ButtonRenderer(eliminarIcon);
    Empleados.getColumnModel().getColumn(5).setCellRenderer(buttonRendererEliminar);

    ButtonRenderer buttonRendererModificar = new ButtonRenderer(modificarIcon);
    Empleados.getColumnModel().getColumn(6).setCellRenderer(buttonRendererModificar);

    // Agregar un MouseListener para cambiar el estado del botón
    Empleados.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            int row = Empleados.rowAtPoint(e.getPoint());
            int column = Empleados.columnAtPoint(e.getPoint());
            if (column == 5 || column == 6) {
                if (column == 5) {
                    buttonRendererEliminar.setPressed(true, row, column);
                } else {
                    buttonRendererModificar.setPressed(true, row, column);
                }
                Empleados.repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int row = Empleados.rowAtPoint(e.getPoint());
            int column = Empleados.columnAtPoint(e.getPoint());
            if (column == 5 || column == 6) {
                if (column == 5) {
                    buttonRendererEliminar.setPressed(false, row, column);
                    int usuarioId = Integer.parseInt(Empleados.getValueAt(row, 0).toString());
                    confirmarYEliminarUsuario(usuarioId, row);
                } else if (column == 6) {
                    buttonRendererModificar.setPressed(false, row, column);
                    int usuarioId = Integer.parseInt(Empleados.getValueAt(row, 0).toString());
                    mostrarDialogoModificarUsuario(usuarioId, row);
                }
                Empleados.repaint();
            }
        }
    });
   
   actualizarTablaEmpleados();
   ajustarAlturaFilas();
}

         
    
    
    private void confirmarYEliminarUsuario(int usuarioId, int row) {
    int confirm = JOptionPane.showConfirmDialog(this, "¿Estás seguro de querer eliminar este usuario?", "Confirmación", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            CONSULTASDAO dao = new CONSULTASDAO(Conexion_DB.getConexion());
            if (dao.eliminarUsuario(usuarioId)) {
                ((DefaultTableModel) Empleados.getModel()).removeRow(row);
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar el usuario.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + ex.getMessage(), "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }
    }
}
private void mostrarDialogoModificarUsuario(int usuarioId, int row) {
    try {
        CONSULTASDAO dao = new CONSULTASDAO(Conexion_DB.getConexion());
        Usuario usuarioActual = dao.obtenerDetallesUsuario(usuarioId);
        if (usuarioActual == null) {
            JOptionPane.showMessageDialog(null, "No se encontró el usuario.");
            return;
        }

        // Recuperar datos desde la tabla y aplicar trim() para eliminar espacios extra
        String nombreCompletoNuevo = Empleados.getValueAt(row, 1).toString().trim();
        String emailNuevo = Empleados.getValueAt(row, 2).toString().trim();
        String rolStrNuevo = Empleados.getValueAt(row, 3).toString().trim(); // Recuperado como String
        String nombreUsuarioNuevo = Empleados.getValueAt(row, 4).toString().trim();

        // Convertir String a Enum
        Rol rolNuevo = Rol.valueOf(rolStrNuevo.toUpperCase()); // Asumiendo que Rol es el nombre del enum

        System.out.println("Actual: " + usuarioActual.getNombreCompleto() + ", Nuevo: " + nombreCompletoNuevo);
        System.out.println("Actual: " + usuarioActual.getEmail() + ", Nuevo: " + emailNuevo);
        System.out.println("Actual: " + usuarioActual.getRol() + ", Nuevo: " + rolNuevo);
        System.out.println("Actual: " + usuarioActual.getNombreUsuario() + ", Nuevo: " + nombreUsuarioNuevo);

        
         // Usar equalsIgnoreCase para comparar sin tener en cuenta mayúsculas y minúsculas
        if (!nombreCompletoNuevo.equalsIgnoreCase(usuarioActual.getNombreCompleto()) ||
            !emailNuevo.equalsIgnoreCase(usuarioActual.getEmail()) ||
            usuarioActual.getRol() != rolNuevo  ||
            !nombreUsuarioNuevo.equalsIgnoreCase(usuarioActual.getNombreUsuario())) {

            int confirm = JOptionPane.showConfirmDialog(null, "Cambios detectados. ¿Desea guardar los cambios?", "Confirmar Actualización", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean actualizado = dao.actualizarUsuario(usuarioId, nombreUsuarioNuevo, "", rolNuevo.toString(), emailNuevo, nombreCompletoNuevo, "", "");
                if (actualizado) {
                    JOptionPane.showMessageDialog(null, "Usuario actualizado con éxito.");
                    actualizarTablaEmpleados();
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el usuario.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "No se detectaron cambios.");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + ex.getMessage(), "Error de conexión", JOptionPane.ERROR_MESSAGE);
    }
}





private void configurarEncabezadosTabla() {
    // Nombres de columnas como en el primer código
    String[] titulo = new String[]{"UsuarioID", "Nombre Completo", "Email", "Rol", "Nombre de Usuario", "Eliminar", "Modificar"};
    DefaultTableModel dtm = (DefaultTableModel) Empleados.getModel();
    dtm.setColumnIdentifiers(titulo);
    Empleados.setModel(dtm);

    // Cargar los iconos
    Icon modificarIcon = new ImageIcon("D:/SISTEMA/Documentos/PROYECTO DE ING SOFTWARE/1.0/proyecto/src/icons/Modificar.png");
    Icon eliminarIcon = new ImageIcon("D:/SISTEMA/Documentos/PROYECTO DE ING SOFTWARE/1.0/proyecto/src/icons/Eliminar.png");

    // Asignar renderizadores a las columnas
    Empleados.getColumnModel().getColumn(5).setCellRenderer(new IconCellRenderer(eliminarIcon));
    Empleados.getColumnModel().getColumn(6).setCellRenderer(new IconCellRenderer(modificarIcon));

    
}



/**
 * Ajusta el tamaño de las columnas y filas de la tabla.
 */
private void ajustarTamanioColumnasYFilas() {
    // Ajustar el tamaño de cada columna según su contenido
    TableColumnModel columnModel = Empleados.getColumnModel();
    for (int column = 0; column < Empleados.getColumnCount(); column++) {
        int width = 50; // Min width
        for (int row = 0; row < Empleados.getRowCount(); row++) {
            TableCellRenderer renderer = Empleados.getCellRenderer(row, column);
            Component comp = Empleados.prepareRenderer(renderer, row, column);
            width = Math.max(comp.getPreferredSize().width + 1 , width);
        }
        if (width > 300)
            width = 300; // Máximo ancho permitido para cualquier columna
        columnModel.getColumn(column).setPreferredWidth(width);
    }

    // Ajustar la altura de las filas para ajustarse al contenido
    for (int row = 0; row < Empleados.getRowCount(); row++) {
        int rowHeight = Empleados.getRowHeight();
        for (int column = 0; column < Empleados.getColumnCount(); column++) {
            TableCellRenderer renderer = Empleados.getCellRenderer(row, column);
            Component comp = Empleados.prepareRenderer(renderer, row, column);
            rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
        }
        Empleados.setRowHeight(row, rowHeight);
    }
    actualizarTablaEmpleados();
}


    
    private void actualizarTablaEmpleados() {
    DefaultTableModel model = (DefaultTableModel) Empleados.getModel();
    model.setRowCount(0); // Limpia la tabla completamente.

    try {
        CONSULTASDAO dao = new CONSULTASDAO(Conexion_DB.getConexion());
         List<Usuario> listaUsuarios = dao.obtenerUsuariosSimplificado(); // Obtiene la lista de productos con su área

        // Recorre la lista y añade filas al modelo de la tabla
        for (Usuario usuario : listaUsuarios) {
            model.addRow(new Object[]{
                usuario.getUsuarioID(),
                usuario.getNombreCompleto(),
                usuario.getEmail(),
                usuario.getRol().toString(),
                usuario.getNombreUsuario(),
                "", // Campo vacío para 'Eliminar'
                ""  // Campo vacío para 'Modificar'
                
            });
        }
 
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + e.getMessage(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
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

        btnReportes = new javax.swing.JButton();
        NuevoUsuario = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Empleados = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnReportes.setText("jButton1");

        NuevoUsuario.setBackground(new java.awt.Color(153, 140, 192));
        NuevoUsuario.setText("Registrar");
        NuevoUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NuevoUsuarioActionPerformed(evt);
            }
        });

        Empleados.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(Empleados);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(NuevoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(btnReportes))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 202, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NuevoUsuario)
                    .addComponent(btnReportes))
                .addGap(35, 35, 35))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void NuevoUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NuevoUsuarioActionPerformed
        // Obtener el rol para la creación del nuevo usuario
        String[] roles = {"ADMINISTRADOR", "GERENTE", "SUPERVISOR", "EMPLEADO"};
        String rolSeleccionado = (String) JOptionPane.showInputDialog(
            this,
            "Seleccione el tipo de usuario a crear:",
            "Creación de usuario",
            JOptionPane.QUESTION_MESSAGE,
            null,
            roles,
            roles[0]
        );

        if (rolSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un rol.");
            return;
        }

        // Solicitar la llave maestra
        String contraseñaIngresada = JOptionPane.showInputDialog(this, "Ingrese la llave maestra:");
        if (contraseñaIngresada == null || contraseñaIngresada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La llave maestra no puede estar vacía.");
            return;
        }
        try {
            CONSULTASDAO productoDAO = new CONSULTASDAO(Conexion_DB.getConexion());
            String contraseñaHasheadaIngresada = HashingUtil.hashPassword(contraseñaIngresada);
            boolean esValido = false;

            // Verifica la llave maestra para cualquier rol con permiso si el usuario a crear es EMPLEADO
            if ("EMPLEADO".equals(rolSeleccionado)) {
                for (String rol : new String[]{"ADMINISTRADOR", "GERENTE", "SUPERVISOR"}) {
                    String contraseñaHasheadaAlmacenada = productoDAO.obtenerContraseñaTokenPorRol(rol);
                    if (contraseñaHasheadaIngresada.equals(contraseñaHasheadaAlmacenada)) {
                        esValido = true;
                        break;  // Sale del bucle si encuentra una coincidencia
                    }
                }
            }else {
                // Para los roles ADMINISTRADOR, GERENTE y SUPERVISOR, verifica la llave maestra directamente
                String contraseñaHasheadaAlmacenada = productoDAO.obtenerContraseñaTokenPorRol(rolSeleccionado);
                esValido = contraseñaHasheadaIngresada != null && contraseñaHasheadaIngresada.equals(contraseñaHasheadaAlmacenada);
            }

            if (esValido) {
                abrirFormularioCreacionUsuario(rolSeleccionado);
            } else {
                JOptionPane.showMessageDialog(this, "Llave maestra incorrecta.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al verificar la llave maestra.");
        }
    }//GEN-LAST:event_NuevoUsuarioActionPerformed
/*
    private void abrirFormularioCreacionUsuario(String rol) {
    FormularioNuevoUsuario formularioNuevoUsuario = new FormularioNuevoUsuario(rol);
    formularioNuevoUsuario.setVisible(true);
    this.setVisible(false); // Opcional: Oculta el formulario de login
}
*/
    private void abrirFormularioCreacionUsuario(String rol) {
        FormularioNuevoUsuario formularioNuevoUsuario = new FormularioNuevoUsuario(rol);
        formularioNuevoUsuario.setConfiguraciones(this);  // Asegúrate de definir este método en FormularioNuevoUsuario
        formularioNuevoUsuario.setVisible(true);
        this.setVisible(false);
    }

    public void mostrar() {
        this.setVisible(true);
    }
    
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
            java.util.logging.Logger.getLogger(Configuraciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Configuraciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Configuraciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Configuraciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Configuraciones().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Empleados;
    private javax.swing.JButton NuevoUsuario;
    private javax.swing.JButton btnReportes;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
