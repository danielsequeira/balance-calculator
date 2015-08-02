package exception;

public class CSVParserException extends Exception {

    private static final long serialVersionUID = 1L;
    private ErrorCode errorCode;

    public CSVParserException() {
        errorCode = ErrorCode.UNKNOWN;
    }

    public CSVParserException(final String message) {
        super(message);
        errorCode = ErrorCode.UNKNOWN;
    }

    public CSVParserException(final String message, final ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getStatusCode() {
        return errorCode;
    }
}
