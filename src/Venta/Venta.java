package Venta;


import ConexionDB.Conexion_DB;
import Configuraciones.Estilos;
import Consultas.CONSULTASDAO;
import DBObjetos.Producto;
import DBObjetos.Usuario;
import INVENTARIO.Principal2_0;
import INVENTARIO.AnimacionPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import login.SesionManager;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */


/**
 *
 * @author frix4
 */
public class Venta extends javax.swing.JFrame {

    /**
     * Creates new form Venta
     */

    private Principal2_0 ventanaPrincipal;
    private AnimacionPanel animador; // Añade esta línea

    private List<Producto> listaProductosConArea;
    private List<Producto> listaFiltrada = new ArrayList<>(); // Lista que refleja los productos filtrados
    
    private double sin;
    private double con;
    private double ivaa;
    
    double totaal;
        
    public Venta() {
        initComponents();
        
        initProductosConArea();

        animador = new AnimacionPanel(); // Inicializa el animador
        Estilos.addPlaceholderStyle(Busqueda);        
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarYVolver();
            }
        });
        
        
        // Hacer que jPanel6 no sea opaco
        jPanel6.setOpaque(false);
        // Establecer un color de fondo semitransparente (e.g., blanco semitransparente)
        Color semiTransparentColor = new Color(255, 255, 255, 123); // Cambia 123 al valor alpha deseado
        jPanel6.setBackground(semiTransparentColor);
        // Si es necesario, puedes requerir repintar el panel para asegurar que el cambio es visible
        jPanel6.repaint();
        jPanel6.setVisible(false); // Asegúrate de que el JPanel inicialmente esté oculto

        

        Busqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String textoBuscado = Busqueda.getText().trim();
                if (textoBuscado.isEmpty()) {
                    jPanel6.setVisible(false);
                } else {
                    filtrarTablaPorTexto(textoBuscado);
                    ajustarAlturaComponentes();
                    jPanel6.setVisible(true);
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_UP) && TablaBusqueda.isVisible()) {
                    TablaBusqueda.requestFocus();
                    cambiarSeleccionEnTabla(e.getKeyCode());
                    e.consume();
                }
            }
        });

        TablaBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                    case KeyEvent.VK_UP:
                        cambiarSeleccionEnTabla(e.getKeyCode());
                        e.consume();
                        break;
                    case KeyEvent.VK_ENTER:
                        if (TablaBusqueda.getSelectedRow() != -1) {
                            agregarProductoACobroYCerrarTabla();
                            e.consume();
                        }
                        break;
                }
            }
        });



        
            // Configura el listener para la tabla TablaCobro
    TablaCobro.getModel().addTableModelListener(new TableModelListener() {
        @Override
        public void tableChanged(TableModelEvent e) {
            // Se asegura de reaccionar solo a cambios relevantes en los datos
            if (e.getType() == TableModelEvent.UPDATE) {
                CalcularTotales();
            }
        }
    });

    TablaCobro.removeColumn(TablaCobro.getColumnModel().getColumn(0));    // Ocultar la columna PRODUCTOID visualmente, pero sigue estando en el modelo
   
    inicializarTablaCobro();
    
    
    cargarProductosConAjusteDePrecio();
    
    }
    



    private void cargarProductosConAjusteDePrecio() {
        try {
            CONSULTASDAO dao = new CONSULTASDAO(Conexion_DB.getConexion());
            Map<Integer, Double> porcentajes = dao.obtenerPorcentajesGanancia();
            listaProductosConArea = dao.obtenerProductosConNombreArea();
            ajustarPrecios(listaProductosConArea, porcentajes);
            actualizarTablaProductos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos: " + e.getMessage(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void ajustarPrecios(List<Producto> productos, Map<Integer, Double> porcentajes) {
        for (Producto producto : productos) {
            double precioOriginal = producto.getPrecio();
            double porcentaje = porcentajes.getOrDefault(producto.getAreaID(), 0.0);
            double precioAjustado = precioOriginal + (precioOriginal * porcentaje / 100);
            producto.setPrecio(precioAjustado);
        }
    }

    
    private void actualizarTablaProductos() {
        DefaultTableModel model = (DefaultTableModel) TablaBusqueda.getModel();
        model.setRowCount(0);
        for (Producto producto : listaProductosConArea) {
            model.addRow(new Object[]{
                producto.getProductoID(),
                producto.getNombre(),
                producto.getMarca(),
                String.format("%.2f", producto.getPrecio()),  // Formato a dos decimales
                producto.getUnidadesDisponibles()
            });
        }
    }



    
    
     private void inicializarTablaCobro() {
        DefaultTableModel modelo = (DefaultTableModel) TablaCobro.getModel();
        modelo.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 2) {  // Asumiendo que la columna 2 es la de unidades
                    int fila = e.getFirstRow();
                    actualizarImporte(fila);
                    CalcularTotales();
                }
            }
        });

    }

private void actualizarImporte(int fila) {
    DefaultTableModel modelo = (DefaultTableModel) TablaCobro.getModel();
    try {
        int unidades = Integer.parseInt(modelo.getValueAt(fila, 2).toString()); // Columna 2 para unidades
        double precioUnitario = Double.parseDouble(modelo.getValueAt(fila, 4).toString()); // Columna 4 para precio unitario, siempre mantiene el valor original con IVA

        double importe = unidades * precioUnitario;  // Calcula el importe basado en las unidades

        // Almacenar el importe directamente sin ajustar por IVA aquí
        BigDecimal bd = new BigDecimal(importe);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        importe = bd.doubleValue();

        modelo.setValueAt(importe, fila, 5);  // Actualiza la columna de importe con el valor Double redondeado

        // Recalcula y actualiza los totales
        CalcularTotales();
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Error en formato numérico: " + ex.getMessage(), "Error de Formato", JOptionPane.ERROR_MESSAGE);
    }
}

    private void CalcularTotales() {
        DefaultTableModel modelo = (DefaultTableModel) TablaCobro.getModel();
        double subtotal = 0.0, totalIVA = 0.0, total = 0.0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            double importe = (Double) modelo.getValueAt(i, 5); // Columna 5 tiene el importe
            double precioSinIVA = importe / 1.16; // Calcula el precio sin IVA del importe
            subtotal += precioSinIVA;
            totalIVA += precioSinIVA * 0.16; // Calcula el IVA basado en el precio sin IVA
        }
        totaal = subtotal + totalIVA;

        lblSubtotal.setText(String.format("%.2f", subtotal));
        lblIva.setText(String.format("%.2f", totalIVA));
        lblTotal.setText(String.format("%.2f", totaal));
    }

    public double returnTotal(){
        return totaal;
    }

    
    private void cambiarSeleccionEnTabla(int keyCode) {
        int rowCount = TablaBusqueda.getRowCount();
        int selectedRow = TablaBusqueda.getSelectedRow();

        if (rowCount > 0) {
            if (keyCode == KeyEvent.VK_DOWN) {
                selectedRow = (selectedRow + 1) % rowCount;
            } else if (keyCode == KeyEvent.VK_UP) {
                selectedRow = (selectedRow - 1 + rowCount) % rowCount;
            }
            TablaBusqueda.setRowSelectionInterval(selectedRow, selectedRow);
            TablaBusqueda.scrollRectToVisible(TablaBusqueda.getCellRect(selectedRow, 0, true));
        }
    }


    
    
    
    private void ajustarAlturaComponentes() {
        int filaAltura = TablaBusqueda.getRowHeight();
        int numFilas = TablaBusqueda.getRowCount();
        int altura = filaAltura * numFilas + 24; // Asumiendo que el header tiene un alto de 24px

        if (numFilas > 5) {
            altura = filaAltura * 5 + 24; // Limita la altura si hay muchas filas
        }

        jScrollPane2.setPreferredSize(new Dimension(jScrollPane2.getWidth(), altura));
        jPanel6.setPreferredSize(new Dimension(jPanel6.getWidth(), altura + 10)); // Un poco más grande para acomodar márgenes
        jPanel6.revalidate(); // Revalida el layout para aplicar cambios de tamaño
        jPanel6.repaint(); // Redibuja el panel
    }
    


  




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
        DefaultTableModel model = (DefaultTableModel) TablaBusqueda.getModel();
        model.setRowCount(0); // Limpia la tabla primero
        listaFiltrada.clear(); // Limpia la lista filtrada

        // Filtra la lista basado en el texto ingresado y actualiza la tabla
        for (Producto prod : listaProductosConArea) {
            if (prod.getNombre().toLowerCase().contains(texto.toLowerCase()) || String.valueOf(prod.getCodigoBarras()).contains(texto.toLowerCase())) {
                model.addRow(new Object[]{
                    prod.getProductoID(),
                    prod.getCodigoBarras(),
                    prod.getNombre(),
                    prod.getMarca(),
                    prod.getPrecio(),
                    prod.getUnidadesDisponibles(),
                    prod.getContenido(),
                    prod.getNombreArea()
                });
                listaFiltrada.add(prod); // Añade al producto a la lista filtrada
            }
        }
    }

    private void agregarProductoACobroYCerrarTabla() {
        int fila = TablaBusqueda.getSelectedRow();
        boolean found = false;
        
        Producto selectedProduct = listaFiltrada.get(fila); // Usa lista filtrada
        DefaultTableModel modelCobro = (DefaultTableModel) TablaCobro.getModel();

        
//        if (fila != -1) {
//            Producto selectedProduct = listaFiltrada.get(fila); // Usa lista filtrada
//            DefaultTableModel modelCobro = (DefaultTableModel) TablaCobro.getModel();
//
//            // Preparar los datos del producto seleccionado
//            modelCobro.insertRow(0, new Object[]{
//               selectedProduct.getProductoID(), // Nuevo campo PRODUCTOID
//                selectedProduct.getCodigoBarras(),
//                1,
//                
//                selectedProduct.getNombre(),
//                selectedProduct.getPrecio(),
//                precio(1, selectedProduct.getPrecio())
//            });
//
//           // Asegura que la nueva fila sea visible en la parte superior
//            TablaCobro.scrollRectToVisible(TablaCobro.getCellRect(0, 0, true));
//
//            // Asegurarse de que el JScrollPane muestra la fila recién insertada en la parte superior
//            TablaCobro.scrollRectToVisible(TablaCobro.getCellRect(0, 0, true));
//            
//            CalcularTotales();
//
//            jPanel6.setVisible(false); // Oculta jPanel6 al seleccionar un producto
//        }
        
        if(fila == -1){
            JOptionPane.showMessageDialog(null, "No se selecciono ninguna fila");
        }else{
            int cb = Integer.parseInt((String) TablaBusqueda.getValueAt(fila, 0).toString());
                        
            for (int i = 0; i < modelCobro.getRowCount(); i++) {
                if ((int) modelCobro.getValueAt(i, 0) == cb) {
                    int currentCantidad = (int) modelCobro.getValueAt(i, 2);
                    modelCobro.setValueAt(currentCantidad + 1, i, 2);
                    found = true;
                    break;
                }
            }
        if (!found) {
            modelCobro.insertRow(0, new Object[]{
               selectedProduct.getProductoID(), // Nuevo campo PRODUCTOID
                selectedProduct.getCodigoBarras(),
                1,
                
                selectedProduct.getNombre(),
                selectedProduct.getPrecio(),
                precio(1, selectedProduct.getPrecio())
            });;
        }
                
        }
    }
        
    public double precio(int u, double p){
        double t = u * p;
        return t;
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
    
    
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TablaBusqueda = new javax.swing.JTable();
        MenuPlegable = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        sub = new javax.swing.JLabel();
        desc = new javax.swing.JLabel();
        total = new javax.swing.JLabel();
        iva = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblIva = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        Busqueda = new javax.swing.JTextField();
        btnEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TablaCobro = new javax.swing.JTable();
        btnCobro = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(102, 153, 0));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        TablaBusqueda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "", "", "", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(TablaBusqueda);
        if (TablaBusqueda.getColumnModel().getColumnCount() > 0) {
            TablaBusqueda.getColumnModel().getColumn(0).setResizable(false);
            TablaBusqueda.getColumnModel().getColumn(2).setResizable(false);
            TablaBusqueda.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, 440, 70));

        MenuPlegable.setBackground(new java.awt.Color(204, 204, 204));
        MenuPlegable.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 51, 51));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/factura2.png"))); // NOI18N
        jLabel1.setText("Factura");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel1.setIconTextGap(15);
        MenuPlegable.add(jLabel1);
        jLabel1.setBounds(0, 150, 170, 50);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu2.png"))); // NOI18N
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        jLabel4.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });
        MenuPlegable.add(jLabel4);
        jLabel4.setBounds(0, 0, 170, 50);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(51, 51, 51));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/inicio2.png"))); // NOI18N
        jLabel5.setText("Inicio");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        jLabel5.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel5.setIconTextGap(15);
        MenuPlegable.add(jLabel5);
        jLabel5.setBounds(0, 50, 170, 50);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(51, 51, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/usuario2.png"))); // NOI18N
        jLabel6.setText("Usuario");
        jLabel6.setToolTipText("");
        jLabel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        jLabel6.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jLabel6.setIconTextGap(15);
        MenuPlegable.add(jLabel6);
        jLabel6.setBounds(0, 100, 170, 50);

        jLabel7.setForeground(new java.awt.Color(51, 51, 51));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/router2.png"))); // NOI18N
        jLabel7.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        jLabel7.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        MenuPlegable.add(jLabel7);
        jLabel7.setBounds(0, 200, 170, 50);

        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/configuracion2.png"))); // NOI18N
        jLabel8.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        jLabel8.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        MenuPlegable.add(jLabel8);
        jLabel8.setBounds(0, 300, 170, 50);

        jLabel9.setForeground(new java.awt.Color(51, 51, 51));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/analitica2.png"))); // NOI18N
        jLabel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 10));
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        MenuPlegable.add(jLabel9);
        jLabel9.setBounds(0, 250, 170, 50);

        jPanel1.add(MenuPlegable, new org.netbeans.lib.awtextra.AbsoluteConstraints(-120, 0, 170, 530));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        sub.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        sub.setText("Subtotal:   $");

        desc.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        desc.setText("Descuento:   $");

        total.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        total.setText("Total:   $");

        iva.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        iva.setText("I.V.A.:   $");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(sub)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(iva)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblIva, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(desc)
                .addGap(144, 144, 144)
                .addComponent(total)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIva, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(sub)
                        .addComponent(desc)
                        .addComponent(total)
                        .addComponent(iva)
                        .addComponent(lblSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Nota - 0001");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(712, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                .addContainerGap())
        );

        Busqueda.setText("Producto");
        Busqueda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                BusquedaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                BusquedaFocusLost(evt);
            }
        });
        Busqueda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BusquedaActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        TablaCobro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        TablaCobro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "PRODUCTOID", "CODIGO", "UNIDADES", "PRODUCTO", "PRECIO UNI.", "IMPORTE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaCobro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TablaCobroKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(TablaCobro);
        if (TablaCobro.getColumnModel().getColumnCount() > 0) {
            TablaCobro.getColumnModel().getColumn(0).setHeaderValue("PRODUCTOID");
        }

        btnCobro.setText("Cobrar");
        btnCobro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCobroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 440, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(490, 490, 490)
                                .addComponent(btnCobro, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 30, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(Busqueda, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar)
                    .addComponent(btnCobro))
                .addContainerGap())
        );

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 0, 820, 530));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BusquedaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BusquedaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BusquedaActionPerformed

    private void btnCobroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCobroActionPerformed
    // Recopilación de productos desde la tabla.
        List<Producto> productosSeleccionados = obtenerProductosSeleccionadosEnTabla();
        if (productosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay productos seleccionados para la venta.");
            return;
        }
            try {
            // Obtención del usuario logueado
            Usuario usuarioActual = SesionManager.getInstance().getUsuarioLogueado();
            if (usuarioActual == null) {
                JOptionPane.showMessageDialog(this, "No hay usuario logueado.");
                return;
            }

            // Creación de la instancia DAO y ejecución de la venta
            CONSULTASDAO dao = new CONSULTASDAO(Conexion_DB.getConexion());
            if (dao.completarVenta(usuarioActual.getUsuarioID(), productosSeleccionados)) {
                //JOptionPane.showMessageDialog(this, "Venta completada con éxito.");

                Cobro c = new Cobro();
                c.setVisible(true);

                // Limpiar tabla y actualizar interfaz según sea necesario
                ((DefaultTableModel) TablaCobro.getModel()).setRowCount(0);
            } else {
                JOptionPane.showMessageDialog(this, "Error al completar la venta.");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(), "Error de Conexión", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnCobroActionPerformed


    private List<Producto> obtenerProductosSeleccionadosEnTabla() {
        List<Producto> productos = new ArrayList<>();
        DefaultTableModel modelo = (DefaultTableModel) TablaCobro.getModel();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            Producto producto = new Producto();
            try {
                int productoID = Integer.parseInt(modelo.getValueAt(i, 0).toString()); // Suponiendo que la columna 0 tiene el ID
                int cantidadUnid = Integer.parseInt(modelo.getValueAt(i, 2).toString()); // Suponiendo que la columna 2 tiene la cantidad
                double precio = Double.parseDouble(modelo.getValueAt(i, 4).toString()); // Suponiendo que la columna 5 tiene el precio unitario

                producto.setProductoID(productoID);
                producto.setCantidad(cantidadUnid);
                producto.setPrecio(precio);

                productos.add(producto);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error en formato de número en la fila " + (i + 1) + ".", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            } catch (NullPointerException e) {
                JOptionPane.showMessageDialog(this, "Valor nulo encontrado en la fila " + (i + 1) + ".", "Error de Nulo", JOptionPane.ERROR_MESSAGE);
            }
        }
        return productos;
    }


    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        // Mover dependiendo de la posición actual
        if (MenuPlegable.getX() == 0) {
            // Mover ambos componentes hacia la izquierda
            animador.animar(MenuPlegable, jPanel4, -120, jPanel4.getX() - 120, false);
            MenuPlegable.setBackground(new Color(204, 204, 204)); // Cambiar al color cuando está plegado

        } else if (MenuPlegable.getX() == -120) {
            // Mover ambos componentes hacia la derecha
            animador.animar(MenuPlegable, jPanel4, 0, jPanel4.getX() + 120, true);
            MenuPlegable.setBackground(new Color(51, 51, 51)); // Cambiar al color cuando está desplegado

        }
    }//GEN-LAST:event_jLabel4MouseClicked

    private void BusquedaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_BusquedaFocusGained
        if(Busqueda.getText().equals("Producto")){
            Busqueda.setText(null);
            Busqueda.requestFocus();
            Estilos.removePlaceholderStyle(Busqueda);            
        }
    }//GEN-LAST:event_BusquedaFocusGained

    private void BusquedaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_BusquedaFocusLost
        if(Busqueda.getText().length()==0){
            Estilos.addPlaceholderStyle(Busqueda);
            Busqueda.setText("Producto");
        }
    }//GEN-LAST:event_BusquedaFocusLost

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
    // Obtener el modelo de la tabla
    DefaultTableModel modelo = (DefaultTableModel) TablaCobro.getModel();

    // Obtener el índice de la fila seleccionada
    int filaSeleccionada = TablaCobro.getSelectedRow();

    // Verificar si se ha seleccionado alguna fila
    if (filaSeleccionada != -1) {
        // Eliminar la fila seleccionada del modelo
        modelo.removeRow(filaSeleccionada);

        // Mostrar un mensaje de confirmación (opcional)
        JOptionPane.showMessageDialog(this, "Producto eliminad correctamente.", "Eliminar", JOptionPane.INFORMATION_MESSAGE);

        // Llamar a cualquier método que necesite ejecutar después de eliminar una fila, como recalcular totales si es necesario
        CalcularTotales();
    } else {
        // Mostrar un mensaje si no hay fila seleccionada
        JOptionPane.showMessageDialog(this, "Por favor seleccione una fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
    }


    }//GEN-LAST:event_btnEliminarActionPerformed

    private void TablaCobroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TablaCobroKeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
        int filaSeleccionada = TablaCobro.getSelectedRow();
        if (filaSeleccionada != -1) {
            ((DefaultTableModel) TablaCobro.getModel()).removeRow(filaSeleccionada);
            JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No hay fila seleccionada.");
        }
    }


    }//GEN-LAST:event_TablaCobroKeyPressed

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
            java.util.logging.Logger.getLogger(Venta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Venta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Venta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Venta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Venta().setVisible(true);
            }
        });
    }
    


  


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField Busqueda;
    private javax.swing.JPanel MenuPlegable;
    private javax.swing.JTable TablaBusqueda;
    private javax.swing.JTable TablaCobro;
    private javax.swing.JButton btnCobro;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JLabel desc;
    private javax.swing.JLabel iva;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblIva;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel sub;
    private javax.swing.JLabel total;
    // End of variables declaration//GEN-END:variables
}
