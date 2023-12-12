package custom_constraint;

import java.util.Arrays;
import java.util.List;

public class InConstraint<T> implements CustomConstraint<T> {
    private T constraintValue;

    public InConstraint(T constraintValue) {
        this.constraintValue = constraintValue;
    }

    public T getCheckedValues() {
        return constraintValue;
    }

    public void setCheckedValues(T constraintValue) {
        this.constraintValue = constraintValue;
    }

    @Override
    public boolean areConstraintValuesInValue(String value) {

        List<String> valuesList = Arrays.asList(value.split("\\s*,\\s*"));
        String constraintStringValue = String.valueOf(constraintValue);
        return valuesList.contains(constraintStringValue);
    }
}
