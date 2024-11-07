package DAO;

public class DAOException extends Exception {

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause); // This constructor allows you to pass both a message and a cause (Throwable)
    }
}