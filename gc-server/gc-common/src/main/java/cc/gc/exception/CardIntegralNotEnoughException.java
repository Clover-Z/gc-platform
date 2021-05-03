package cc.gc.exception;

import org.springframework.util.StringUtils;

public class CardIntegralNotEnoughException extends RuntimeException {


    public CardIntegralNotEnoughException(Class clazz, String field, String val) {
        super(generateMessage(clazz.getSimpleName(), field, val));
    }


    private static String generateMessage(String entity, String field, String val) {
        return StringUtils.capitalize(entity)
                + " with " + field + " "+ val + " does not exist";
    }

}
