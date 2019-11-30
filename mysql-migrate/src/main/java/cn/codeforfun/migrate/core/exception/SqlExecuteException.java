package cn.codeforfun.migrate.core.exception;

/**
 * @author wangbin
 */
public class SqlExecuteException extends RuntimeException {
    private static final long serialVersionUID = -3835577233735719118L;

    public SqlExecuteException(String message) {
        super(message);
    }
}
