import DAO.DAOException;
import DB.DBConfig;

import Entidades.Curso;
import GUI.*;
import com.mysql.cj.log.Log;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws DAOException {

        DBConfig con = new DBConfig();
        DBConfig.getConexion();
        DBConfig.crearTablaAlumnoCurso();


        Login log = new Login();

    }
}