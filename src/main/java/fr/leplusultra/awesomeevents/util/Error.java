package fr.leplusultra.awesomeevents.util;

import fr.leplusultra.awesomeevents.util.exception.UserException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class Error {
    public static void returnErrorToClient(BindingResult bindingResult){
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> errorList = bindingResult.getFieldErrors();

        for(FieldError error : errorList){
            errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage() == null ? error.getCode() : error.getDefaultMessage()).append(";");
        }
        throw new UserException(errorMessage.toString());
    }
}
