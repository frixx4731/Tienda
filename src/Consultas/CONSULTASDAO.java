/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Consultas;
import DBObjetos.*;
import DBObjetos.Usuario.Rol;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import login.HashingUtil;

/**
 *
 * @author braul
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */



public class CONSULTASDAO {
    private Connection con;

    public CONSULTASDAO(Connection con) {
        this.con = con;
    }

    
    
    
    
    public List<Producto> obtenerProductosConNombre(String texto) {
    List<Producto> listaProductos = new ArrayList<>();
    String sql = "SELECT p.*, a.Nombre AS NombreArea FROM productos p " +
                 "INNER JOIN area a ON p.AreaID = a.AreaID " +
                 "WHERE p.Nombre LIKE ?"; // Filtrar por nombre

    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setString(1, "%" + texto + "%"); // Configurar el parámetro del LIKE
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Producto producto = new Producto(
                rs.getInt("ProductoID"),
                rs.getString("Nombre"),
                rs.getString("Descripcion"),
                rs.getInt("AreaID"),
                rs.getDouble("Precio"),
                rs.getInt("UnidadesDisponibles"),
                rs.getInt("NivelReorden"),
                rs.getDate("FechaCaducidad") != null ? rs.getDate("FechaCaducidad").toLocalDate() : null,
                rs.getString("CodigoBarras"),
                rs.getString("TamañoNeto"),
                rs.getString("Marca"),
                rs.getString("Contenido"),
                rs.getString("NombreArea")
                );
                listaProductos.add(producto);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace(); // O maneja la excepción como consideres adecuado
    }

    return listaProductos;
}
    
    

//nueva PRUEBA
public List<Producto> obtenerProductosConNombreArea()  {
    List<Producto> listaProductos = new ArrayList<>();
    String sql = "SELECT p.*, a.Nombre AS NombreArea FROM productos p " +
                 "INNER JOIN area a ON p.AreaID = a.AreaID";

    try (PreparedStatement pstmt = con.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            Producto producto = new Producto(
                rs.getInt("ProductoID"),
                rs.getString("Nombre"),
                rs.getString("Descripcion"),
                rs.getInt("AreaID"),
                rs.getDouble("Precio"),
                rs.getInt("UnidadesDisponibles"),
                rs.getInt("NivelReorden"),
                rs.getDate("FechaCaducidad") != null ? rs.getDate("FechaCaducidad").toLocalDate() : null,
                rs.getString("CodigoBarras"),
                rs.getString("TamañoNeto"),
                rs.getString("Marca"),
                rs.getString("Contenido"),
                rs.getString("NombreArea") // Nombre del área
            );
            listaProductos.add(producto);
        }
    } catch (SQLException e) {
        e.printStackTrace(); // O maneja la excepción como consideres adecuado
    }

    return listaProductos;
}

public List<Producto> obtenerProductosPorArea(String areaNombre) throws SQLException {
    List<Producto> listaProductos = new ArrayList<>();
    String sql = "SELECT p.ProductoID, p.Nombre, p.Descripcion, p.AreaID, p.Precio, "
                 + "p.UnidadesDisponibles, p.NivelReorden, p.FechaCaducidad, "
                 + "p.CodigoBarras, p.TamañoNeto, p.Marca, p.Contenido, a.Nombre AS NombreArea "
                 + "FROM productos p INNER JOIN area a ON p.AreaID = a.AreaID "
                 + "WHERE a.Nombre = ?";

    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setString(1, areaNombre);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("ProductoID"),
                    rs.getString("Nombre"),
                    rs.getString("Descripcion"),
                    rs.getInt("AreaID"),
                    rs.getDouble("Precio"),
                    rs.getInt("UnidadesDisponibles"),
                    rs.getInt("NivelReorden"),
                    rs.getObject("FechaCaducidad") != null ? rs.getDate("FechaCaducidad").toLocalDate() : null,
                    rs.getString("CodigoBarras"),
                    rs.getString("TamañoNeto"),
                    rs.getString("Marca"),
                    rs.getString("Contenido"),
                    rs.getString("NombreArea") // Este es el nuevo campo que espera el constructor
                );
                listaProductos.add(producto);
            }
        }
    }
    return listaProductos;
}


     
 
        /*
    public boolean registrarProducto(Producto producto) {
    String sql = "INSERT INTO Productos (Nombre, Descripción, CategoriaID, Precio, CantidadStock, NivelReorden, FechaCaducidad, CodigoBarras) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripción());
            pstmt.setInt(3, producto.getCategoriaID());
            pstmt.setDouble(4, producto.getPrecio());
            pstmt.setInt(5, producto.getCantidadStock());
            pstmt.setInt(6, producto.getNivelReorden());
            pstmt.setDate(7, Date.valueOf(producto.getFechaCaducidad()));
            pstmt.setString(8, producto.getCodigoBarras());


            int affectedRows = pstmt.executeUpdate();

            // Verificar si se insertó al menos un registro
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Puedes manejar la excepción de manera más específica si lo necesitas
            return false;
        }
    }
    
    public boolean actualizarProducto(Producto producto) {
    String sql = "UPDATE Productos SET Nombre = ?, Descripción = ?, CategoriaID = ?, Precio = ?, CantidadStock = ?, NivelReorden = ?, FechaCaducidad = ?, CodigoBarras = ? WHERE ProductoID = ?";

    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setString(1, producto.getNombre());
        pstmt.setString(2, producto.getDescripción());
        pstmt.setInt(3, producto.getCategoriaID());
        pstmt.setDouble(4, producto.getPrecio());
        pstmt.setInt(5, producto.getCantidadStock());
        pstmt.setInt(6, producto.getNivelReorden());
        pstmt.setDate(7, Date.valueOf(producto.getFechaCaducidad()));
        pstmt.setString(8, producto.getCodigoBarras());
        pstmt.setInt(9, producto.getProductoID());

        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}*/

    
    public boolean eliminarProducto(int productoID) {
    String sql = "DELETE FROM Productos WHERE ProductoID = ?";

    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setInt(1, productoID);

        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    
    
    //loggin metodos
    public ArrayList<String> obtenerPreguntasSeguridad(String nombreUsuario) throws SQLException {
    ArrayList<String> preguntasSeguridad = new ArrayList<>();
    
    // Suponiendo que tienes una tabla de preguntas de seguridad donde cada usuario tiene asociadas sus preguntas
        String sql = "SELECT PreguntaSeguridad FROM Usuario WHERE NombreUsuario = ?";
    
    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setString(1, nombreUsuario);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String pregunta = rs.getString("PreguntaSeguridad");
                preguntasSeguridad.add(pregunta);
            }
        }
    }
    
    return preguntasSeguridad;
}

    
    
      public String obtenerPreguntaSeguridad(String nombreUsuario) {
        String sql = "SELECT PreguntaSeguridad FROM Usuario WHERE NombreUsuario = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("PreguntaSeguridad");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // En caso de no encontrar el usuario o error
    }

      
     public boolean verificarRespuestaSeguridad(String nombreUsuario, String respuesta) throws SQLException {
    String sql = "SELECT RespuestaSeguridad FROM Usuario WHERE NombreUsuario = ?";
    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setString(1, nombreUsuario);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                String respuestaAlmacenada = rs.getString("RespuestaSeguridad");
                return respuesta.equals(respuestaAlmacenada);  // Aquí se compara la respuesta directamente
            }
        }
    }
    return false;
}
 
      
      public String obtenerRespuestasSeguridad(String nombreUsuario) throws SQLException {
    String sql = "SELECT RespuestaSeguridad FROM Usuario WHERE NombreUsuario = ?";
    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setString(1, nombreUsuario);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("RespuestaSeguridad");
            }
        }
    }
    return null; // Retornar null si el usuario no tiene respuestas de seguridad o no se encuentra
}

      /*
      public ArrayList<String> obtenerRespuestasSeguridad(String nombreUsuario) throws SQLException {
    ArrayList<String> respuestasSeguridad = new ArrayList<>();


        String sql = "SELECT * FROM Usuario WHERE NombreUsuario = ? AND RespuestaSeguridad = SHA2(?, 256)";

    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setString(1, nombreUsuario);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String respuesta = rs.getString("RespuestaSeguridad");
                respuestasSeguridad.add(respuesta);
            }
        }
    }

    return respuestasSeguridad;
}
*/

      
      

    public boolean cambiarContraseña(String nombreUsuario, String nuevaContrasena) {
        String sql = "UPDATE Usuario SET Contraseña = SHA2(?, 256) WHERE NombreUsuario = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, nuevaContrasena); // Asumiendo que quieres hashear la contraseña
            pstmt.setString(2, nombreUsuario);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // Verificar si se cambió la contraseña correctamente
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // En caso de error
    }
    
    
     // Método para crear un nuevo empleado
public boolean crearEmpleado(String nombreUsuario, String contraseñaHasheada, Usuario.Rol rol,
                             ArrayList<String> preguntas, ArrayList<String> respuestas,
                             String correo, String nombre, String apellido) {
    
    String nombreCompleto = nombre + " " + apellido;
    String preguntasConcatenadas = String.join("|", preguntas);
    String respuestasConcatenadas = String.join("|", respuestas);

           String sql = "INSERT INTO Usuario (NombreUsuario, Contraseña, Rol, Email, NombreCompleto, PreguntaSeguridad, RespuestaSeguridad) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);
            pstmt.setString(2, contraseñaHasheada);
            pstmt.setString(3, rol.name());
            pstmt.setString(4, correo);
            pstmt.setString(5, nombreCompleto);
            pstmt.setString(6, preguntasConcatenadas);
            pstmt.setString(7, respuestasConcatenadas);


        int affectedRows = pstmt.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException e) {
        if (e.getSQLState().equals("23000")) { // Código de estado para violación de restricciones únicas
            System.out.println("El nombre de usuario ya existe.");
        } else {
            e.printStackTrace();
        }
        return false;
    }
}

/*
       public boolean validarUsuario(String nombreUsuario, String contraseñaPlana) throws SQLException {
        String sql = "SELECT Contraseña FROM Usuario WHERE NombreUsuario = ?";
        
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Obtener la contraseña hasheada almacenada en la base de datos
                    String contraseñaHasheada = rs.getString("Contraseña");
                    
                    // Comparar la contraseña hasheada con la versión hasheada de la contraseña introducida
                    // Aquí asumimos que tienes una función para hashear la contraseña
                    String contraseñaVerificar = HashingUtil.hashPassword(contraseñaPlana);
                    
                    return contraseñaHasheada != null && contraseñaHasheada.equals(contraseñaVerificar);
                }
            }
        }
        return false;
    }
*/
       
public String obtenerContraseñaHashPorRol(String rol) {
    String sql = "SELECT ContraseñaHash FROM ConfiguracionRoles WHERE Rol = ?";
    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setString(1, rol);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getString("ContraseñaHash");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
      
      // Método para obtener el rol de un usuario dado su nombre de usuario
    public String obtenerRolPorNombreUsuario(String nombreUsuario) throws SQLException {
        String sql = "SELECT Rol FROM Usuario WHERE NombreUsuario = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Rol");
                }
            }
        }
        return null; // Retornar null o lanzar una excepción si el usuario no se encuentra
    }     
    
    
    
      public Usuario validarUsuario(String nombreUsuario, String contraseñaPlana) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE NombreUsuario = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, nombreUsuario);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String contraseñaHash = rs.getString("Contraseña");
                    String contraseñaVerificar = HashingUtil.hashPassword(contraseñaPlana);

                    if (contraseñaHash != null && contraseñaHash.equals(contraseñaVerificar)) {
                        int usuarioID = rs.getInt("UsuarioID");
                        String email = rs.getString("Email");
                        String nombreCompleto = rs.getString("NombreCompleto");
                        String rolString = rs.getString("Rol");
                        LocalDateTime ultimoLogin = rs.getTimestamp("UltimoLogin") != null ? rs.getTimestamp("UltimoLogin").toLocalDateTime() : null;
                        String preguntaSeguridad = rs.getString("PreguntaSeguridad");
                        String respuestaSeguridad = rs.getString("RespuestaSeguridad");

                        Usuario usuario = new Usuario(
                            usuarioID, 
                            nombreUsuario, 
                            contraseñaPlana, 
                            Usuario.Rol.valueOf(rolString), 
                            email, 
                            nombreCompleto, 
                            ultimoLogin
                        );
                        usuario.setPreguntaSeguridad(preguntaSeguridad);
                        usuario.setRespuestaSeguridad(respuestaSeguridad);

                        return usuario;
                    }
                }
            }
        }
        return null;
    }
      
      
      
      
      //Consultas a Usuarios
public List<Usuario> obtenerUsuariosSimplificado() throws SQLException {
    List<Usuario> listaUsuarios = new ArrayList<>();
    String sql = "SELECT UsuarioID, NombreUsuario, Rol, Email, NombreCompleto FROM usuario"; // Asegúrate de que el nombre de la tabla y columnas sean correctos

    try (PreparedStatement pstmt = con.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
            Usuario usuario = new Usuario(); // Instancia con constructor vacío
            usuario.setUsuarioID(rs.getInt("UsuarioID"));
            usuario.setNombreUsuario(rs.getString("NombreUsuario"));
            usuario.setRol(Usuario.Rol.valueOf(rs.getString("Rol"))); // Suponiendo que 'Rol' es un enum en la clase Usuario
            usuario.setEmail(rs.getString("Email"));
            usuario.setNombreCompleto(rs.getString("NombreCompleto"));
            // Añadir el objeto usuario a la lista
            listaUsuarios.add(usuario);
        }
    }
    return listaUsuarios;
}

public String obtenerEmailPorNombreUsuario(String nombreUsuarioBuscado) throws SQLException {
    String email = null;
    String sql = "SELECT Email FROM usuario WHERE NombreUsuario = ?";
    
    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setString(1, nombreUsuarioBuscado);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            email = rs.getString("Email");
        }
        rs.close();
    }
    return email;
}



    // Método para actualizar los datos de un usuario
    public boolean actualizarUsuario(int usuarioId, String nombreUsuario, String contraseña, String rol, String email, String nombreCompleto, String preguntaSeguridad, String respuestaSeguridad) {
        String query = "UPDATE usuario SET NombreUsuario = ?, Contraseña = ?, Rol = ?, Email = ?, NombreCompleto = ?, PreguntaSeguridad = ?, RespuestaSeguridad = ? WHERE UsuarioID = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, nombreUsuario);
            stmt.setString(2, contraseña);
            stmt.setString(3, rol);
            stmt.setString(4, email);
            stmt.setString(5, nombreCompleto);
            stmt.setString(6, preguntaSeguridad);
            stmt.setString(7, respuestaSeguridad);
            stmt.setInt(8, usuarioId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar un usuario
    public boolean eliminarUsuario(int usuarioId) {
        String query = "DELETE FROM usuario WHERE UsuarioID = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, usuarioId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario obtenerDetallesUsuario(int usuarioId) throws SQLException {
    String query = "SELECT UsuarioID, NombreCompleto, Email, Rol, NombreUsuario FROM usuario WHERE UsuarioID = ?";
    try (PreparedStatement stmt = con.prepareStatement(query)) {
        stmt.setInt(1, usuarioId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Usuario usuario = new Usuario();
            usuario.setUsuarioID(rs.getInt("UsuarioID"));
            usuario.setNombreCompleto(rs.getString("NombreCompleto"));
            usuario.setEmail(rs.getString("Email"));
            usuario.setRol(Usuario.Rol.valueOf(rs.getString("Rol"))); // Suponiendo que 'Rol' es un enum en la clase Usuario
            usuario.setNombreUsuario(rs.getString("NombreUsuario"));
            return usuario;
        }
        return null;
    }
}

    
    
}


