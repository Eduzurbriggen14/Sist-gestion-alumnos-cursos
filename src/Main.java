import DAO.DAOException;
import DB.DBConfig;

import Entidades.Curso;
import GUI.*;

public class Main {
    public static void main(String[] args) throws DAOException {

        DBConfig con = new DBConfig();
        DBConfig.getConexion();
        DBConfig.crearTablaAlumnoCurso();


        Login log = new Login();



    }
}