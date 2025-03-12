package book.project.bookstore.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    
    private String firstField;
    private String secondField;
    
    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        this.firstField = constraintAnnotation.first();
        this.secondField = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object firstValue = new BeanWrapperImpl(value).getPropertyValue(firstField);
        Object secondValue = new BeanWrapperImpl(value).getPropertyValue(secondField);
        
        return firstValue != null && firstValue.equals(secondValue);
    }
}
