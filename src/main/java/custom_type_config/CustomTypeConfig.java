package custom_type_config;

import custom_type.CustomType;

public class CustomTypeConfig {
    private final CustomType customType;
    private final Object[] constraints;

    public CustomTypeConfig(CustomType customType, Object[] constraints) {
        this.customType = customType;
        this.constraints = constraints;
    }

    public CustomType getCustomType() {
        return customType;
    }

    public Object[] getConstraints() {
        return constraints;
    }
}