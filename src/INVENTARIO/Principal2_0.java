/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package INVENTARIO;

import ConexionDB.Conexion_DB;
import DBObjetos.*;


import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import Configuraciones.Configuraciones;
import Venta.Venta;
import Consultas.CONSULTASDAO;
import java.awt.Color;
import java.time.LocalDate;


/**
 *
 * @author master
 */
public class Principal2_0 extends javax.swing.JFrame {

    /**
     * Creates new form Panel
     */

    private AnimacionPanel animador; // Añade esta línea
    private Usuario usuarioLogueado;

 
    
    
    public Principal2_0() {
        initComponents();
                
        animador = new AnimacionPanel(); // Inicializa el animador

        
        configurarEncabezadosTabla();

        Buscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String textoBuscado = Buscar.getText().trim();
                filtrarTablaPorTexto(textoBuscado);
            }
        });

        initProductosConArea();
    
        Venta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VentaMouseClicked(evt);
            }
    });

    }
    
       
       
    public Principal2_0(Usuario usuario) {
        
        this.usuarioLogueado = usuario;
        
        initComponents();
        
        animador = new AnimacionPanel(); // Inicializa el animador
        
        configurarEncabezadosTabla();
        
         ajustarTamanioColumnas();
        
        
      
    Configuracion.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            // Crea la instancia de la ventana de configuraciones
              if (MenuPlegable.getX() == 0) {
            Configuraciones ventanaConfiguraciones = new Configuraciones();

            // Configura la acción cuando se cierre la ventana de configuraciones
            ventanaConfiguraciones.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    Principal2_0.this.setVisible(true); // Hace visible la ventana principal nuevamente
                }
            });

            // Establece el comportamiento por defecto al cerrar la ventana de configuraciones
            ventanaConfiguraciones.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // Oculta la ventana principal
            Principal2_0.this.setVisible(false);

            // Muestra la ventana de configuraciones
            ventanaConfiguraciones.setVisible(true);
          }
        }
    });
    

    initProductosConArea();
    
    configurarVisibilidadComponentes();
    
    Buscar.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(KeyEvent e) {
            String textoBuscado = Buscar.getText().trim();
            filtrarTablaPorTexto(textoBuscado);
        }
    });

           
    Venta.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            VentaMouseClicked(evt);
        }
    });

}
       
    private void configurarVisibilidadComponentes() {
        // Asegúrate de que los componentes están inicializados antes de llamar este método
        if (usuarioLogueado.getRol() == Usuario.Rol.EMPLEADO) {
            Usuarios.setVisible(false);
            Configuracion.setVisible(false);
            // Hacer invisibles otros labels que no deben verse por el empleado
        } else if (usuarioLogueado.getRol() == Usuario.Rol.ADMINISTRADOR) {
            // El administrador puede ver todo, así que no necesitas hacer nada aquí
        }
    }
 
    


      
       
    private void configurarEncabezadosTabla() {
    // Nombres de columnas como en el primer código
    String[] titulo = new String[]{"COD. BARRAS", "NOMBRE", "MARCA", "UNIDADES", "CONTENIDO", "AREA", "PRECIO"};
    DefaultTableModel dtm = (DefaultTableModel) tablita.getModel();
    dtm.setColumnIdentifiers(titulo);
    tablita.setModel(dtm);
    actualizarTablaInventario();
    
    ajustarTamanioColumnas();
    
}

    
// Método que filtra la tabla basado en el texto ingresado
private List<Producto> listaProductosConArea; // Mantiene la lista de productos para evitar múltiples accesos a BD

private void initProductosConArea() {
    try {
        CONSULTASDAO dao = new CONSULTASDAO(Conexion_DB.getConexion());
        listaProductosConArea = dao.obtenerProductosConNombreArea(); // Carga inicial de productos
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

private void filtrarTablaPorTexto(String texto) {
    DefaultTableModel model = (DefaultTableModel) tablita.getModel();
    model.setRowCount(0); // Limpia la tabla primero

    // Filtra la lista basado en el texto ingresado y actualiza la tabla
    for (Producto prod : listaProductosConArea) {
        if (prod.getNombre().toLowerCase().startsWith(texto.toLowerCase()) || prod.getCodigoBarras().toLowerCase().startsWith(texto.toLowerCase())) {
            model.addRow(new Object[]{
                prod.getCodigoBarras(),
                prod.getNombre(),
                prod.getMarca(),
                prod.getUnidadesDisponibles(),
                prod.getContenido(),
                prod.getNombreArea(),
                prod.getPrecio()
            });
        }
    }
}

    


private void actualizarTablaInventario() {
    DefaultTableModel model = (DefaultTableModel) tablita.getModel();
    model.setRowCount(0); // Limpia la tabla completamente.

    try {
        CONSULTASDAO dao = new CONSULTASDAO(Conexion_DB.getConexion());
        List<Producto> listaProductosConArea = dao.obtenerProductosConNombreArea(); // Obtiene la lista de productos con su área

        // Recorre la lista y añade filas al modelo de la tabla
        for (Producto prod : listaProductosConArea) {
            LocalDate fechaa = prod.getFechaCaducidad();
            LocalDate unMesAntes = fechaa.minusMonths(1);
            LocalDate actual = LocalDate.now();
            
            if(prod.getUnidadesDisponibles()>20){
                if(actual.getYear()<= fechaa.getYear() && fechaa.getMonthValue() > actual.getMonthValue()){
                    model.addRow(new Object[]{
                        prod.getCodigoBarras(),
                        prod.getNombre(),
                        prod.getMarca(), // Asumiendo que tienes un getter getMarca en la clase Producto
                        prod.getUnidadesDisponibles(),
                        prod.getContenido(), // Verifica que este dato se desea mostrar
                        prod.getNombreArea(), // Este es el nombre del área, asegúrate de tener este getter en Producto
                        prod.getPrecio()
                    });
                }else{
                    model.addRow(new Object[]{
                        prod.getCodigoBarras(),
                        prod.getNombre(),
                        prod.getMarca(), // Asumiendo que tienes un getter getMarca en la clase Producto
                        prod.getUnidadesDisponibles(),
                        prod.getContenido(), // Verifica que este dato se desea mostrar
                        prod.getNombreArea(), // Este es el nombre del área, asegúrate de tener este getter en Producto
                        prod.getPrecio()
                    });
                    
                    System.out.println("Se sugiere poner en oferta el producto " + prod.getNombre() + ", con fecha de caducidad " + prod.getFechaCaducidad());
                }
            }else{
                model.addRow(new Object[]{
                    prod.getCodigoBarras(),
                    prod.getNombre(),
                    prod.getMarca(), // Asumiendo que tienes un getter getMarca en la clase Producto
                    prod.getUnidadesDisponibles(),
                    prod.getContenido(), // Verifica que este dato se desea mostrar
                    prod.getNombreArea(), // Este es el nombre del área, asegúrate de tener este getter en Producto
                    prod.getPrecio()
                });
                System.out.println("Reabastecer " + prod.getNombre());
            }
            
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

        jPanel2 = new javax.swing.JPanel();
        MenuPlegable = new javax.swing.JPanel();
        Menu = new javax.swing.JLabel();
        Inicio = new javax.swing.JLabel();
        Usuarios = new javax.swing.JLabel();
        Configuracion = new javax.swing.JLabel();
        Venta = new javax.swing.JLabel();
        Analisis = new javax.swing.JLabel();
        Internet = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        Panel2 = new javax.swing.JPanel();
        Buscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablita = new javax.swing.JTable();
        desplegable = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        MenuPlegable.setBackground(new java.awt.Color(204, 204, 204));
        MenuPlegable.setLayout(null);

        Menu.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Menu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu2.png"))); // NOI18N
        Menu.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        Menu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Menu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuMouseClicked(evt);
            }
        });
        MenuPlegable.add(Menu);
        Menu.setBounds(0, 0, 170, 50);

        Inicio.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        Inicio.setForeground(new java.awt.Color(255, 255, 255));
        Inicio.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Inicio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/inicio2.png"))); // NOI18N
        Inicio.setText("Inicio");
        Inicio.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        Inicio.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Inicio.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        Inicio.setIconTextGap(15);
        MenuPlegable.add(Inicio);
        Inicio.setBounds(0, 70, 170, 50);

        Usuarios.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        Usuarios.setForeground(new java.awt.Color(255, 255, 255));
        Usuarios.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Usuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/usuario2.png"))); // NOI18N
        Usuarios.setText("Usuarios");
        Usuarios.setToolTipText("");
        Usuarios.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        Usuarios.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Usuarios.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        Usuarios.setIconTextGap(15);
        Usuarios.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                UsuariosMouseClicked(evt);
            }
        });
        MenuPlegable.add(Usuarios);
        Usuarios.setBounds(0, 430, 170, 50);

        Configuracion.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        Configuracion.setForeground(new java.awt.Color(255, 255, 255));
        Configuracion.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Configuracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/configuracion2.png"))); // NOI18N
        Configuracion.setText("Configuracion");
        Configuracion.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        Configuracion.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Configuracion.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        Configuracion.setIconTextGap(15);
        MenuPlegable.add(Configuracion);
        Configuracion.setBounds(0, 520, 170, 50);

        Venta.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        Venta.setForeground(new java.awt.Color(255, 255, 255));
        Venta.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Venta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/factura2.png"))); // NOI18N
        Venta.setText("Venta");
        Venta.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        Venta.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Venta.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        Venta.setIconTextGap(15);
        Venta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                VentaMouseClicked(evt);
            }
        });
        MenuPlegable.add(Venta);
        Venta.setBounds(0, 170, 170, 50);

        Analisis.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        Analisis.setForeground(new java.awt.Color(255, 255, 255));
        Analisis.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Analisis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/analitica2.png"))); // NOI18N
        Analisis.setText("Analisis");
        Analisis.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        Analisis.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Analisis.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        Analisis.setIconTextGap(15);
        MenuPlegable.add(Analisis);
        Analisis.setBounds(0, 350, 170, 50);

        Internet.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        Internet.setForeground(new java.awt.Color(255, 255, 255));
        Internet.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Internet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/router2.png"))); // NOI18N
        Internet.setText("Internet");
        Internet.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        Internet.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Internet.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        Internet.setIconTextGap(15);
        MenuPlegable.add(Internet);
        Internet.setBounds(0, 260, 170, 50);

        jPanel2.add(MenuPlegable, new org.netbeans.lib.awtextra.AbsoluteConstraints(-120, 0, 170, 610));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Nota - 0001");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(932, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 1010, 40));

        Buscar.setText("Buscar");
        Buscar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                BuscarFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                BuscarFocusLost(evt);
            }
        });
        Buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarActionPerformed(evt);
            }
        });
        Buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                BuscarKeyReleased(evt);
            }
        });

        tablita.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "COD. BARRAS", "NOMBRE", "MARCA", "CANTIDAD", "UNIDADES", "AREA", "PRECIO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablita);

        desplegable.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos los Productos", "Salchichonería", "Frutas y Verduras", "Panadería", "Galletas y Cereales", "Bebidas", "Productos de Limpieza", "Botanas", "Cuidado Personal", "Congelados" }));
        desplegable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                desplegableActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel2Layout = new javax.swing.GroupLayout(Panel2);
        Panel2.setLayout(Panel2Layout);
        Panel2Layout.setHorizontalGroup(
            Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1010, Short.MAX_VALUE)
            .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel2Layout.createSequentialGroup()
                    .addGap(0, 25, Short.MAX_VALUE)
                    .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 966, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(Panel2Layout.createSequentialGroup()
                            .addComponent(Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(150, 150, 150)
                            .addComponent(desplegable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(0, 19, Short.MAX_VALUE)))
        );
        Panel2Layout.setVerticalGroup(
            Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 610, Short.MAX_VALUE)
            .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(Panel2Layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addGroup(Panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(Buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(desplegable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(50, 50, 50)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        jPanel2.add(Panel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 1010, 610));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
    }//GEN-LAST:event_formComponentResized

    private void MenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_MenuMouseClicked
        // Mover dependiendo de la posición actual
        if (MenuPlegable.getX() == 0) {
            // Mover ambos componentes hacia la izquierda
            animador.animar(MenuPlegable, Panel2, -120, Panel2.getX() - 120, false);
            MenuPlegable.setBackground(new Color(204, 204, 204)); // Cambiar al color cuando está plegado

        } else if (MenuPlegable.getX() == -120) {
            // Mover ambos componentes hacia la derecha
            animador.animar(MenuPlegable, Panel2, 0, Panel2.getX() + 120, true);
            MenuPlegable.setBackground(new Color(51, 51, 51)); // Cambiar al color cuando está desplegado

        }
    }//GEN-LAST:event_MenuMouseClicked

    
    private void desplegableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_desplegableActionPerformed
    String areaSeleccionada = (String) desplegable.getSelectedItem();

        if (desplegable.getSelectedIndex() != 0) {
            actualizarTablaInventarioPorArea(areaSeleccionada);
        } else {
            actualizarTablaInventario();
        }

    
    }//GEN-LAST:event_desplegableActionPerformed


    
    private void actualizarTablaInventarioPorArea(String area) {
    DefaultTableModel model = (DefaultTableModel) tablita.getModel();
    model.setRowCount(0); // Limpia la tabla completamente.

    try {
        CONSULTASDAO dao = new CONSULTASDAO(Conexion_DB.getConexion());
        List<Producto> productosFiltrados = dao.obtenerProductosPorArea(area); // Implementar este método
        
        for (Producto prod : listaProductosConArea) {
            LocalDate fechaa = prod.getFechaCaducidad();
            LocalDate unMesAntes = fechaa.minusMonths(1);
            LocalDate startDate = LocalDate.now();
            if(prod.getUnidadesDisponibles()>20){
                if( startDate.getYear()<= unMesAntes.getYear() && fechaa.getMonthValue() > startDate.getMonthValue()){
                    model.addRow(new Object[]{
                        prod.getCodigoBarras(),
                        prod.getNombre(),
                        prod.getMarca(), // Asumiendo que tienes un getter getMarca en la clase Producto
                        prod.getUnidadesDisponibles(),
                        prod.getContenido(), // Verifica que este dato se desea mostrar
                        prod.getNombreArea(), // Este es el nombre del área, asegúrate de tener este getter en Producto
                        prod.getPrecio()
                    });
                }else{
                    model.addRow(new Object[]{
                        prod.getCodigoBarras(),
                        prod.getNombre(),
                        prod.getMarca(), // Asumiendo que tienes un getter getMarca en la clase Producto
                        prod.getUnidadesDisponibles(),
                        prod.getContenido(), // Verifica que este dato se desea mostrar
                        prod.getNombreArea(), // Este es el nombre del área, asegúrate de tener este getter en Producto
                        prod.getPrecio()
                    });
                    
                    System.out.println("Se sugiere poner en oferta el producto " + prod.getNombre());
                }
            }else{
                model.addRow(new Object[]{
                    prod.getCodigoBarras(),
                    prod.getNombre(),
                    prod.getMarca(), // Asumiendo que tienes un getter getMarca en la clase Producto
                    prod.getUnidadesDisponibles(),
                    prod.getContenido(), // Verifica que este dato se desea mostrar
                    prod.getNombreArea(), // Este es el nombre del área, asegúrate de tener este getter en Producto
                    prod.getPrecio()
                });
                System.out.println("Reabastecer " + prod.getNombre());
            }
            
        }
  } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error al conectar con la base de datos: " + e.getMessage(),
                                      "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
}

    private void BuscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_BuscarFocusGained
        if(Buscar.getText().equals("Buscar")){
            Buscar.setText(null);
            Buscar.requestFocus();
            //removePlaceholderStyle(fieldUser);

        }
    }//GEN-LAST:event_BuscarFocusGained

    private void BuscarFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_BuscarFocusLost
        if(Buscar.getText().length()==0){
            //addPlaceholderStyle(fieldUser);
            Buscar.setText("Buscar");
        }
    }//GEN-LAST:event_BuscarFocusLost

    private void BuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarActionPerformed

    }//GEN-LAST:event_BuscarActionPerformed

    private void BuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BuscarKeyReleased
        
    }//GEN-LAST:event_BuscarKeyReleased

    private void VentaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_VentaMouseClicked
    Venta ventanaVenta = new Venta(); // Crea la instancia de la ventana Venta
    ventanaVenta.setVentanaPrincipal(this); // Pasa la instancia actual de Principal2_0 a la ventana Venta
    ventanaVenta.setVisible(true); // Hace visible la ventana Venta
    this.setVisible(false); // Oculta la ventana Principal2_0
    }//GEN-LAST:event_VentaMouseClicked

    private void UsuariosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_UsuariosMouseClicked
    Configuraciones ventanaConfiguraciones = new Configuraciones(); 
    ventanaConfiguraciones.setVentanaPrincipal(this); 
    ventanaConfiguraciones.setVisible(true); 
    this.setVisible(false); 
    }//GEN-LAST:event_UsuariosMouseClicked

    
    private void ajustarTamanioColumnas() {
    TableColumnModel modeloColumna = tablita.getColumnModel();
    modeloColumna.getColumn(0).setPreferredWidth(50); 
    modeloColumna.getColumn(1).setPreferredWidth(190); 
    modeloColumna.getColumn(2).setPreferredWidth(100); 
    modeloColumna.getColumn(3).setPreferredWidth(50); 
    modeloColumna.getColumn(4).setPreferredWidth(100); 
    modeloColumna.getColumn(5).setPreferredWidth(100); 
    modeloColumna.getColumn(6).setPreferredWidth(40);
 
    // Ajustar el alto de las filas
    tablita.setRowHeight(25); // Altura de 25 píxeles para las filas

    modeloColumna.getColumn(0).setResizable(false);
        modeloColumna.getColumn(6).setResizable(false);

    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    
    // Aplica este renderizador a cada columna de la tabla
    for (int i = 0; i < tablita.getColumnCount(); i++) {
        tablita.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }
      // Centrar encabezados de la tabla
    ((DefaultTableCellRenderer) tablita.getTableHeader().getDefaultRenderer())
            .setHorizontalAlignment(JLabel.CENTER);

  
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
            java.util.logging.Logger.getLogger(Principal2_0.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal2_0.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal2_0.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal2_0.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal2_0().setVisible(true);
            }
        });
        // Ajuste del ancho de columna basado en el contenido
        
        
    }

    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Analisis;
    private javax.swing.JTextField Buscar;
    private javax.swing.JLabel Configuracion;
    private javax.swing.JLabel Inicio;
    private javax.swing.JLabel Internet;
    private javax.swing.JLabel Menu;
    private javax.swing.JPanel MenuPlegable;
    private javax.swing.JPanel Panel2;
    private javax.swing.JLabel Usuarios;
    private javax.swing.JLabel Venta;
    private javax.swing.JComboBox<String> desplegable;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablita;
    // End of variables declaration//GEN-END:variables
}
