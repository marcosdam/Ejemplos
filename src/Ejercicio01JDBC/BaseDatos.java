package Ejercicio01JDBC;

import EjemploGranja.Animal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BaseDatos {

    private final String servidor = "jdbc:mysql://localhost/empleados";
    private final String user = "root";
    private final String pass = "";

    public Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(servidor, user, pass);
    }

    public boolean crearTablaEmpleado() {

        try {
            Connection conexion = this.obtenerConexion();
            String query = "CREATE TABLE empleados\n" +
                    "    (dni_empleado varchar (9) PRIMARY KEY,\n" +
                    "    empleo varchar(40) NOT NULL,\n" +
                    "    nombre varchar(40),\n" +
                    "    horas_semanales int(2),\n" +
                    ");";
            Statement stm = conexion.createStatement();
            stm.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            //ex.printStackTrace();
            return false;
        }
    }

    public boolean insertEmpleado(Empleado e) {
        try {
            Connection conexion = this.obtenerConexion();
            String query = "insert into empleados values (?,?,?,?)";
            PreparedStatement pstm = conexion.prepareStatement(query);
            pstm.setString(1, e.getDni());
            pstm.setString(2, e.getEmpleo());
            pstm.setString(3, e.getNombre());
            pstm.setInt(4, e.getHoras_semanales());
            int numRows = pstm.executeUpdate();
            if (numRows > 0)
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    public ArrayList<String> obtenerDNI_empleado() {
        ArrayList<String> listado = new ArrayList<>();

        try {
            Connection conexion = this.obtenerConexion();
            String query = "select dni from empleados";
            Statement stm = conexion.createStatement();
            ResultSet rs = stm.executeQuery(query);

            rs.beforeFirst();

            while (rs.next()) {
                String dni = rs.getString("dni");
                listado.add(dni);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ejercicio01JDBC.BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return listado;
        }
    }

    public Empleado obtenerEmpleado(String dni) {
        Empleado empleado = null;
        try {
            Connection conexion = this.obtenerConexion();
            String query = "select * from empleados where dni = ?";
            PreparedStatement pstm = conexion.prepareStatement(query);
            pstm.setString(1, dni);
            ResultSet rs = pstm.executeQuery();

            if (rs.first()) {
                empleado = new Empleado(rs.getString("dni"),
                        rs.getString("empleo"),
                        rs.getString("nombre"),
                        rs.getInt("horas_semanales"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(Ejercicio01JDBC.BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return empleado;
        }
    }

    public boolean updateEmpleado(Empleado old, Empleado nuevo) {
        try {
            Connection conexion = this.obtenerConexion();
            String query = "update empleados set dni = ?, empleo = ?, nombre = ?, horas_semanales = ? where dni = ?";
            PreparedStatement pstm = conexion.prepareStatement(query);
            pstm.setString(1, nuevo.getDni());
            pstm.setString(2, nuevo.getEmpleo());
            pstm.setString(3, nuevo.getNombre());
            pstm.setInt(4, nuevo.getHoras_semanales());
            pstm.setString(5, old.getDni());

            if (pstm.executeUpdate() == 1)
                return true;
            else
                return false;

        } catch (SQLException ex) {
            Logger.getLogger(Ejercicio01JDBC.BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteEmpleado(Empleado empleado) {
        try {
            Connection conexion = this.obtenerConexion();
            String query = "delete from empleados where dni = ?";
            PreparedStatement pstm = conexion.prepareStatement(query);
            pstm.setString(1, empleado.getDni());
            if (pstm.executeUpdate() == 1)
                return true;
        } catch (SQLException ex) {
            Logger.getLogger(Ejercicio01JDBC.BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public ArrayList<Empleado> obtenerEmpleado() {
        ArrayList<Empleado> listado = new ArrayList<>();
        try {
            String query = "select * from empleados";
            Connection conexion = this.obtenerConexion();
            Statement stm = conexion.createStatement();
            ResultSet rs = stm.executeQuery(query);
            rs.beforeFirst();
            while (rs.next()) {
                listado.add(new Empleado(rs.getString("dni"),
                        rs.getString("empleo"),
                        rs.getString("nombre"),
                        rs.getInt("horas_semanales")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Ejercicio01JDBC.BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return listado;
        }

    }

}
