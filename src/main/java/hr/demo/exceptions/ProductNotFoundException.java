package hr.demo.exceptions;

public class ProductNotFoundException extends RuntimeException {

    private final String code;

    public ProductNotFoundException(String code) {
        super("Product with code " + code + " not found.");
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
