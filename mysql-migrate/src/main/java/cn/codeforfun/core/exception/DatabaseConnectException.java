package cn.codeforfun.core.exception;

/**
 * @author wangbin
 */
public class DatabaseConnectException extends RuntimeException {
    private static final long serialVersionUID = -2739132126587693908L;

    public DatabaseConnectException(String message) {
        super(message);
    }
}
