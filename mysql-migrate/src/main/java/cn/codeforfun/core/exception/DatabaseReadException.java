package cn.codeforfun.core.exception;

/**
 * @author wangbin
 */
public class DatabaseReadException extends RuntimeException {

    private static final long serialVersionUID = 1495259148349920631L;

    public DatabaseReadException(String message) {
        super(message);
    }
}
