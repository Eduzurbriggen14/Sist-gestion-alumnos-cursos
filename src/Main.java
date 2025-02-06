import DAO.DAOException;
import DB.DBConfig;

import GUI.*;

public class Main {
    public static void main(String[] args) throws DAOException {

        DBConfig con = new DBConfig();
        DBConfig.getConexion();
        Login log = new Login();



    }
}