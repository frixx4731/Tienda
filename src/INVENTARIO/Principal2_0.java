/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package INVENTARIO;

import ConexionDB.Conexion_DB;
import DBObjetos.*;
import login.Estilos;

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
    
    private boolean isEditable = false;
    private AnimacionPanel animador; // Añade esta línea
    private Usuario usuarioLogueado;
    
    private int num;
    int numfinal;
    
    public Principal2_0() {
        initComponents();
                
        animador = new AnimacionPanel(); // Inicializa el animador

        
        configurarEncabezadosTabla();
        //actualizarTablaInventario();

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
        
//        Estilos.addPlaceholderStyle(Unidades);
//        Estilos.addPlaceholderStyle(Caducidad);
//        Estilos.addPlaceholderStyle(Contenido);
//        Estilos.addPlaceholderStyle(Precio);
//        Estilos.addPlaceholderStyle(Marca);
//        Estilos.addPlaceholderStyle(Precio);
//        Estilos.addPlaceholderStyle(Nombre);

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

        Estilos.addPlaceholderStyle(Unidades);
        Estilos.addPlaceholderStyle(Caducidad);
        Estilos.addPlaceholderStyle(Contenido);
        Estilos.addPlaceholderStyle(Precio);
        Estilos.addPlaceholderStyle(Marca);
        Estilos.addPlaceholderStyle(Precio);
        Estilos.addPlaceholderStyle(Nombre);
        
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
        if (prod.getNombre().toLowerCase().startsWith(texto.toLowerCase()) || String.valueOf(prod.getCodigoBarras()).startsWith(texto.toLowerCase())) {
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

    


public void actualizarTablaInventario() {
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
                if(actual.isBefore(unMesAntes)){
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
                
                if(actual.isBefore(unMesAntes)){
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
        Panel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablita = new javax.swing.JTable();
        add = new javax.swing.JButton();
        modificar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Unidades = new javax.swing.JTextField();
        Nombre = new javax.swing.JTextField();
        Precio = new javax.swing.JTextField();
        Caducidad = new javax.swing.JTextField();
        Marca = new javax.swing.JTextField();
        Contenido = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        Buscar = new javax.swing.JTextField();
        desplegable = new javax.swing.JComboBox<>();
        cod = new javax.swing.JLabel();

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

        Panel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablita.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "COD. BARRAS", "NOMBRE", "MARCA", "UNIDADES", "CONTENIDO", "AREA", "PRECIO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablita.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablitaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablita);

        Panel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 130, 966, 341));

        add.setText("Añadir Productos");
        add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addActionPerformed(evt);
            }
        });
        Panel2.add(add, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 480, -1, -1));

        modificar.setText("Modificar Productos");
        modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modificarActionPerformed(evt);
            }
        });
        Panel2.add(modificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 520, -1, -1));

        jPanel3.setBackground(new java.awt.Color(204, 204, 204));

        jLabel1.setBackground(new java.awt.Color(51, 51, 51));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        jLabel1.setText("INVENTARIO");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(484, 484, 484)
                .addComponent(jLabel1)
                .addContainerGap(669, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Panel2.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1300, 40));

        Unidades.setText("Unidades Disponibles");
        Unidades.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                UnidadesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                UnidadesFocusLost(evt);
            }
        });
        Panel2.add(Unidades, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 420, 160, -1));

        Nombre.setText("Nombre");
        Nombre.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                NombreFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                NombreFocusLost(evt);
            }
        });
        Panel2.add(Nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 220, 160, -1));

        Precio.setText("Precio");
        Precio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                PrecioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                PrecioFocusLost(evt);
            }
        });
        Panel2.add(Precio, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 260, 160, -1));

        Caducidad.setText("Fecha de Caducidad");
        Caducidad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                CaducidadFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                CaducidadFocusLost(evt);
            }
        });
        Panel2.add(Caducidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 340, 160, -1));

        Marca.setText("Marca");
        Marca.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                MarcaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                MarcaFocusLost(evt);
            }
        });
        Panel2.add(Marca, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 300, 160, -1));

        Contenido.setText("Contenido");
        Contenido.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                ContenidoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                ContenidoFocusLost(evt);
            }
        });
        Panel2.add(Contenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 380, 160, -1));

        jButton3.setText("Eliminar Productos");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        Panel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 560, -1, -1));

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
        Panel2.add(Buscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 320, 30));

        desplegable.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos los Productos", "Salchichonería", "Frutas y Verduras", "Panadería", "Galletas y Cereales", "Bebidas", "Productos de Limpieza", "Botanas", "Cuidado Personal", "Congelados" }));
        desplegable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                desplegableActionPerformed(evt);
            }
        });
        Panel2.add(desplegable, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 80, -1, -1));

        cod.setText("CODIGO DE BARRAS");
        cod.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Panel2.add(cod, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 170, 160, 30));

        jPanel2.add(Panel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 1300, 610));

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
        
        int num = desplegable.getSelectedIndex();
        int num1 = 0;
        
        if (desplegable.getSelectedIndex() != 0) {
            actualizarTablaInventarioPorArea(areaSeleccionada);
            
            try{
                CONSULTASDAO dao = new CONSULTASDAO(Conexion_DB.getConexion());
                int codigo = dao.getCod(num);
                num1 = codigo + 1;
                cod.setText("" + num1);
                
                
            } catch (SQLException ex) {
                //Logger.getLogger(Add.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("QUE WEY SOY X2");
            }
            
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
        
        for (Producto prod : productosFiltrados) {
            model.addRow(new Object[]{
                prod.getCodigoBarras(),
                prod.getNombre(),
                prod.getMarca(), // Asumiendo que tienes un getter getMarca en la clase Producto
                prod.getUnidadesDisponibles(),
                prod.getContenido(), // Verifica que este dato se desea mostrar
                prod.getNombreArea(), // Este es el nombre del área, asegúrate de tener este getter en Producto
                prod.getPrecio()
            });                   
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

    private void addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addActionPerformed
        insertar(evt);
        
    }//GEN-LAST:event_addActionPerformed

    private void modificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modificarActionPerformed
        modificar(evt);
        
        //boolean validar = !nombree.isEmpty() && !precioo.isEmpty(),;
        
        
    }//GEN-LAST:event_modificarActionPerformed

    private void UnidadesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_UnidadesFocusGained
        if(Unidades.getText().equals("Unidades Disponibles")){
            Unidades.setText(null);
            Unidades.requestFocus();
            Estilos.removePlaceholderStyle(Unidades);
        }
    }//GEN-LAST:event_UnidadesFocusGained

    private void UnidadesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_UnidadesFocusLost
        if(Unidades.getText().length()==0){
            Estilos.addPlaceholderStyle(Unidades);
            Unidades.setText("Unidades Disponibles");
        }
    }//GEN-LAST:event_UnidadesFocusLost

    private void NombreFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_NombreFocusGained
        if(Nombre.getText().equals("Nombre")){
            Nombre.setText(null);
            Nombre.requestFocus();
            Estilos.removePlaceholderStyle(Nombre);
        }
    }//GEN-LAST:event_NombreFocusGained

    private void NombreFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_NombreFocusLost
        if(Nombre.getText().length()==0){
            Estilos.addPlaceholderStyle(Nombre);
            Nombre.setText("Nombre");
        }
    }//GEN-LAST:event_NombreFocusLost

    private void PrecioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PrecioFocusGained
        if(Precio.getText().equals("Precio")){
            Precio.setText(null);
            Precio.requestFocus();
            Estilos.removePlaceholderStyle(Precio);
        }
    }//GEN-LAST:event_PrecioFocusGained

    private void PrecioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_PrecioFocusLost
        if(Precio.getText().length()==0){
            Estilos.addPlaceholderStyle(Precio);
            Precio.setText("Precio");
        }
    }//GEN-LAST:event_PrecioFocusLost

    private void CaducidadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CaducidadFocusGained
        if(Caducidad.getText().equals("Fecha de Caducidad")){
            Caducidad.setText(null);
            Caducidad.requestFocus();
            Estilos.removePlaceholderStyle(Caducidad);
        }
    }//GEN-LAST:event_CaducidadFocusGained

    private void CaducidadFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_CaducidadFocusLost
        if(Caducidad.getText().length()==0){
            Estilos.addPlaceholderStyle(Caducidad);
            Caducidad.setText("Fecha de Caducidad");
        }
    }//GEN-LAST:event_CaducidadFocusLost

    private void MarcaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_MarcaFocusGained
        if(Marca.getText().equals("Marca")){
            Marca.setText(null);
            Marca.requestFocus();
            Estilos.removePlaceholderStyle(Marca);
        }
    }//GEN-LAST:event_MarcaFocusGained

    private void MarcaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_MarcaFocusLost
        if(Marca.getText().length()==0){
            Estilos.addPlaceholderStyle(Marca);
            Marca.setText("Marca");
        }
    }//GEN-LAST:event_MarcaFocusLost

    private void ContenidoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ContenidoFocusGained
        if(Contenido.getText().equals("Contenido")){
            Contenido.setText(null);
            Contenido.requestFocus();
            Estilos.removePlaceholderStyle(Contenido);
        }
    }//GEN-LAST:event_ContenidoFocusGained

    private void ContenidoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ContenidoFocusLost
        if(Contenido.getText().length()==0){
            Estilos.addPlaceholderStyle(Contenido);
            Contenido.setText("Contenido");
        }
    }//GEN-LAST:event_ContenidoFocusLost

    private void tablitaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablitaMouseClicked
        int fila = tablita.getSelectedRow();
        
        if(fila == -1){
            JOptionPane.showMessageDialog(null, "No se selecciono ninguna fila");
        }else{
            int cb = Integer.parseInt((String) tablita.getValueAt(fila, 0).toString());
            String nm = (String) tablita.getValueAt(fila, 1);
            String mc = (String) tablita.getValueAt(fila, 2);
            int un = Integer.parseInt((String) tablita.getValueAt(fila, 3).toString());
            String cnt = (String) tablita.getValueAt(fila, 4);
            String ar = tablita.getValueAt(fila, 5).toString();          
            //double pr = Double.parseDouble((String) tablita.getValueAt(fila, 6));
            double pr = Double.parseDouble(tablita.getValueAt(fila, 6).toString());
            
            cod.setText("" + cb);
            Nombre.setText("" + nm);
            Precio.setText("" + pr);
            Marca.setText(mc);
            //Caducidad.setText(id3);
            Contenido.setText("" + cnt);
            Unidades.setText("" + un);
        }
    }//GEN-LAST:event_tablitaMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        eliminar(evt);
    }//GEN-LAST:event_jButton3ActionPerformed

    
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

    private void insertar(java.awt.event.ActionEvent evt){
        String nombree = Nombre.getText();
        int areaID = desplegable.getSelectedIndex();
        double precioo = Double.parseDouble(Precio.getText());
        int unidadess = Integer.parseInt(Unidades.getText());
        String fechaa = Caducidad.getText();
        int codigoB = Integer.parseInt(cod.getText());
        String marcaa = Marca.getText();
        String contenidoo = Contenido.getText();

        try {
            CONSULTASDAO dao = new CONSULTASDAO(Conexion_DB.getConexion());

            boolean exito = dao.crearProd(nombree, areaID, precioo, unidadess, fechaa, codigoB, marcaa, contenidoo);
            
            if (exito) {
                System.out.println("Producto " + nombree + " agregado correctamente");
                String areaSeleccionada = (String) desplegable.getSelectedItem();
                
                actualizarTablaInventarioPorArea(areaSeleccionada);
                //actualizarTablaInventario();
                
                desplegableActionPerformed(evt);
                Nombre.setText("Nombre");
                Precio.setText("Precio");
                Marca.setText("Marca");
                Caducidad.setText("Fecha de Caducidad");
                Contenido.setText("Contenido");
                Unidades.setText("Unidades Disponibles");
            } else {
                System.out.println("Producto no agregado");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();  // Imprimir detalles de error
        }
    }
    
    private void modificar(java.awt.event.ActionEvent evt){
        String nombree = Nombre.getText();
        int areaID = desplegable.getSelectedIndex();
        
        double precioo = Double.parseDouble(Precio.getText());
        int unidadess = Integer.parseInt(Unidades.getText());
        int codigoB = Integer.parseInt(cod.getText());
        
        try{
            CONSULTASDAO dao = new CONSULTASDAO(Conexion_DB.getConexion());
            boolean exito = dao.actualizarProd(codigoB, precioo, unidadess);
            
            if(exito){
                System.out.println("Producto " + nombree + " actualizado");
                String areaSeleccionada = (String) desplegable.getSelectedItem();
                actualizarTablaInventarioPorArea(areaSeleccionada);
                //actualizarTablaInventario();
                
            }else System.out.println("Producto " + nombree + " no actualizado");
        }catch (SQLException ex) {
            ex.printStackTrace();  // Imprimir detalles de error
        }
    }
    
    private void eliminar(java.awt.event.ActionEvent evt){
        String nombree = Nombre.getText();
        int areaID = desplegable.getSelectedIndex();
        
        int codigoB = Integer.parseInt(cod.getText());
        
        try{
            CONSULTASDAO dao = new CONSULTASDAO(Conexion_DB.getConexion());
            boolean exito = dao.eliminarProd(codigoB);
            
            if(exito){
                System.out.println("Producto " + nombree + " eliminado");
                String areaSeleccionada = (String) desplegable.getSelectedItem();
                actualizarTablaInventarioPorArea(areaSeleccionada);
                //actualizarTablaInventario();
                
            }else System.out.println("Producto " + nombree + " no eliminado");
        }catch (SQLException ex) {
            ex.printStackTrace();  // Imprimir detalles de error
        }
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Analisis;
    private javax.swing.JTextField Buscar;
    private javax.swing.JTextField Caducidad;
    private javax.swing.JLabel Configuracion;
    private javax.swing.JTextField Contenido;
    private javax.swing.JLabel Inicio;
    private javax.swing.JLabel Internet;
    private javax.swing.JTextField Marca;
    private javax.swing.JLabel Menu;
    private javax.swing.JPanel MenuPlegable;
    private javax.swing.JTextField Nombre;
    private javax.swing.JPanel Panel2;
    private javax.swing.JTextField Precio;
    private javax.swing.JTextField Unidades;
    private javax.swing.JLabel Usuarios;
    private javax.swing.JLabel Venta;
    private javax.swing.JButton add;
    private javax.swing.JLabel cod;
    private javax.swing.JComboBox<String> desplegable;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton modificar;
    private javax.swing.JTable tablita;
    // End of variables declaration//GEN-END:variables
}
