package EjemploGranja;

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

    private final String servidor = "jdbc:mysql://localhost/granja";
    private final String user = "root";
    private final String pass = "";

    public Connection obtenerConexion() throws SQLException{
        return DriverManager.getConnection(servidor, user, pass);
    }

    public boolean crearTablaDatos(){

        try {
            Connection conexion = this.obtenerConexion();
            String query = "CREATE TABLE animales\n" +
                    "    (id_animal int PRIMARY KEY,\n" +
                    "    tipo varchar(40) NOT NULL,\n" +
                    "    nombre varchar(40),\n" +
                    "    color varchar(40),\n" +
                    "    edad int NOT NULL,\n" +
                    "    num_enfermedades int NOT NULL\n" +
                    ");";
            Statement stm = conexion.createStatement();
            stm.executeUpdate(query);
            return true;
        } catch (SQLException ex) {
            //ex.printStackTrace();
            return false;
        }
    }

    public boolean insertAnimal(Animal a){
        try {
            Connection conexion = this.obtenerConexion();
            String query = "insert into animales values (?,?,?,?,?,?)";
            PreparedStatement pstm = conexion.prepareStatement(query);
            pstm.setInt(1, a.getId_animal());
            pstm.setString(2, a.getTipo());
            pstm.setString(3, a.getNombre());
            pstm.setString(4, a.getColor());
            pstm.setInt(5, a.getEdad());
            pstm.setInt(6, a.getNum_enfermedades());
            int numRows = pstm.executeUpdate();
            if (numRows > 0)
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }

    public ArrayList<Integer> obtenerId_animal()
    {
        ArrayList<Integer> listado = new ArrayList<>();

        try {
            Connection conexion = this.obtenerConexion();
            String query = "select id_animal from animales";
            Statement stm = conexion.createStatement();
            ResultSet rs = stm.executeQuery(query);

            rs.beforeFirst();

            while (rs.next())
            {
                int id_animal = rs.getInt("id_animal");
                listado.add(id_animal);
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return listado;
        }
    }

    public Animal obtenerAnimal(int id_animal)
    {
        Animal animal=null;
        try {
            Connection conexion = this.obtenerConexion();
            String query = "select * from animales where id_animal = ?";
            PreparedStatement pstm = conexion.prepareStatement(query);
            pstm.setInt(1, id_animal);
            ResultSet rs = pstm.executeQuery();

            if (rs.first())
            {
                animal = new Animal(rs.getInt("id_animal"),
                        rs.getString("tipo"),
                        rs.getString("nombre"),
                        rs.getString("color"),
                        rs.getInt("edad"),
                        rs.getInt("num_enfermedades"));

            }

        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return animal;
        }
    }

    public boolean updateAnimal(Animal old, Animal nuevo)
    {
        try {
            Connection conexion = this.obtenerConexion();
            String query = "update animales set id_animal = ?, tipo = ?, nombre = ?, color = ?, edad = ?, num_enfermedades = ? where id_animal = ?";
            PreparedStatement pstm = conexion.prepareStatement(query);
            pstm.setInt(1, nuevo.getId_animal());
            pstm.setString(2, nuevo.getTipo());
            pstm.setString(3, nuevo.getNombre());
            pstm.setString(4, nuevo.getColor());
            pstm.setInt(5, nuevo.getEdad());
            pstm.setInt(6, nuevo.getNum_enfermedades());
            pstm.setInt(7, old.getId_animal());

            if (pstm.executeUpdate() == 1)
                return true;
            else
                return false;

        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean deleteAnimal(Animal animal){
        try {
            Connection conexion = this.obtenerConexion();
            String query = "delete from animales where id_animal = ?";
            PreparedStatement pstm = conexion.prepareStatement(query);
            pstm.setInt(1, animal.getId_animal());
            if (pstm.executeUpdate() == 1)
                return true;
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public ArrayList<Animal> obtenerAnimal()
    {
        ArrayList<Animal> listado = new ArrayList<>();
        try
        {
            String query = "select * from animales";
            Connection conexion = this.obtenerConexion();
            Statement stm = conexion.createStatement();
            ResultSet rs = stm.executeQuery(query);
            rs.beforeFirst();
            while(rs.next())
            {
                listado.add(new Animal(rs.getInt("id_animal"),
                        rs.getString("tipo"),
                        rs.getString("nombre"),
                        rs.getString("color"),
                        rs.getInt("edad"),
                        rs.getInt("num_enfermedades")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(BaseDatos.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return listado;
        }

    }

}
