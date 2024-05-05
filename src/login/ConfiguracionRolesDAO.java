/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Map;
/**
 *
 * @author braul
 */

public class ConfiguracionRolesDAO {
    private Connection con;

    public ConfiguracionRolesDAO(Connection con) {
        this.con = con;
    }

    public boolean insertarRolConContraseña(String rol, String contraseña) {
        String contraseñaHash = HashingUtil.hashPassword(contraseña);
        if (contraseñaHash == null) {
            return false;
        }

        String sql = "INSERT INTO ConfiguracionRoles (Rol, ContraseñaHash) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, rol);
            pstmt.setString(2, contraseñaHash);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void configurarRolesYContraseñas(Map<String, String> rolesYContraseñas) {
        for (Map.Entry<String, String> entrada : rolesYContraseñas.entrySet()) {
            String rol = entrada.getKey();
            String contraseña = entrada.getValue();
            if (!contraseña.isEmpty()) {
                if (!insertarRolConContraseña(rol, contraseña)) {
                    System.out.println("No se pudo insertar la configuración para el rol: " + rol);
                }
            }
        }
    }

    public static void main(String[] args) {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/TiendaAbarrotes", "root", "123456");
            ConfiguracionRolesDAO gestorRoles = new ConfiguracionRolesDAO(con);
            Map<String, String> rolesYContraseñas = new HashMap<>();
            rolesYContraseñas.put("ADMINISTRADOR", "contraseñaAdmin");
            rolesYContraseñas.put("GERENTE", "contraseñaGerente");
            rolesYContraseñas.put("SUPERVISOR", "contraseñaSupervisor");
            rolesYContraseñas.put("EMPLEADO", "");
            gestorRoles.configurarRolesYContraseñas(rolesYContraseñas);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
