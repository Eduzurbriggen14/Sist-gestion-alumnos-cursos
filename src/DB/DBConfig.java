package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConfig {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "Zurdo123";
    private static final String URL = "jdbc:mysql://localhost:3306/tp_java";
    private static final String DRIVER_DB = "com.mysql.cj.jdbc.Driver";
    private static Connection connection;

    public static Connection getConexion() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER_DB);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("Conexión exitosa a MySQL");
            }
        } catch (SQLException e) {
            System.err.println("Error en la conexión: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar el driver", e);
        }
        return connection;
    }

    public static synchronized void resetConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            connection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cerrarConexion() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada");
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void crearTablaAlumno() {
        Connection conn = getConexion();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("USE tp_java");
            String sql = "CREATE TABLE IF NOT EXISTS alumno (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre VARCHAR(100), " +
                    "passw VARCHAR(100), " +
                    "nombreUsuario VARCHAR(100) NOT NULL UNIQUE, " +
                    "correo VARCHAR(100), " +
                    "tipoUsuario VARCHAR(50), " +
                    "abonoAlumno BOOLEAN)";

            stmt.executeUpdate(sql);
            System.out.println("Tabla 'alumno' creada con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el statement: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public static void crearTablaProfesor() {
        Connection conn = getConexion();
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("USE tp_java");
            String sql = "CREATE TABLE IF NOT EXISTS profesor ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "nombre VARCHAR(100) NOT NULL, "
                    + "passw VARCHAR(100), "
                    + "nombreUsuario VARCHAR(100) NOT NULL UNIQUE, "
                    + "correo VARCHAR(100), "
                    + "tipoUsuario VARCHAR(50)"
                    + ");";

            stmt.executeUpdate(sql);
            System.out.println("Tabla 'profesor' creada con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el statement: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public static void crearTablaAdministrador() {
        Connection conn = getConexion();
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate("USE tp_java");
            String sql = "CREATE TABLE IF NOT EXISTS administrador ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "nombre VARCHAR(100) NOT NULL, "
                    + "passw VARCHAR(100), "
                    + "nombreUsuario VARCHAR(100) NOT NULL UNIQUE, "
                    + "correo VARCHAR(100), "
                    + "tipoUsuario VARCHAR(50)"
                    + ");";

            stmt.executeUpdate(sql);
            System.out.println("Tabla 'administrador' creada con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el statement: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public static void crearTablaCurso() {
        Connection conn = getConexion();
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS curso (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombreCurso VARCHAR(100) NOT NULL UNIQUE, " +
                    "descripcionCurso TEXT, " +
                    "cupo INT NOT NULL, " +
                    "precioCurso DECIMAL(10, 2) NOT NULL, " +
                    "promocion_id INT, " +
                    "FOREIGN KEY (promocion_id) REFERENCES promocion(id) "+
                    ");";
            stmt.executeUpdate(sql);
            System.out.println("Tabla 'curso' creada con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla 'curso': " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el statement: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public static void crearTablaInscripcion() {
        Connection conn = getConexion();
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS inscripciones (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "alumno_id INT NOT NULL, " +
                    "curso_id INT NOT NULL, " +
                    "anio INT NOT NULL," +
                    "condicion VARCHAR(25) Default cursando, "+
                    "FOREIGN KEY (alumno_id) REFERENCES alumno(id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (curso_id) REFERENCES curso(id) ON DELETE CASCADE" +
                    ");";
            stmt.executeUpdate(sql);
            System.out.println("Tabla 'inscripciones' creada con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla 'inscripciones': " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el statement: " + e.getMessage());
                }
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void crearTablaCalificacionInscripcion() {
        Connection conn = getConexion();
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS calificacion_inscripcion (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "id_inscripcion int NOT NULL, "+
                    "valor_nota DOUBLE NOT NULL, "+
                    "fecha DATE NOT NULL, " +
                    "tipo ENUM('nota1', 'nota2', 'recuperatorio') NOT NULL, " +
                    "FOREIGN KEY (id_inscripcion) REFERENCES inscripciones(id) "+
                    ");";
            stmt.executeUpdate(sql);
            System.out.println("Tabla 'calificacion_inscripcion' creada con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla 'calificacion_inscripcion': " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el statement: " + e.getMessage());
                }
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void crearTablaProfesorCurso() {
        Connection conn = getConexion();
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS profesor_curso (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "profesor_id INT NOT NULL, " +
                    "curso_id INT NOT NULL, " +
                    "anio INT NOT NULL" +
                    "FOREIGN KEY (profesor_id) REFERENCES profesor(id), " +
                    "FOREIGN KEY (curso_id) REFERENCES curso(id)" +
                    ");";
            stmt.executeUpdate(sql);
            System.out.println("Tabla 'profesor_curso' creada con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla 'profesor_curso': " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el statement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    public static void creatTablaPromocion(){
        Connection conn = getConexion();
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS promocion (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nombre_promocion VARCHAR(35) NOT NULL, " +
                    "descripcion VARCHAR(100), " +
                    "descuento DECIMAL(10, 2) NOT NULL " +
                    ");";
            stmt.executeUpdate(sql);
            System.out.println("Tabla 'PROMOCION' creada con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla 'PROMOCION': " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar el statement: " + e.getMessage());
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

}