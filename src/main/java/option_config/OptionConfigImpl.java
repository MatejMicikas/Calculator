package option_config;

import custom_type.CustomType;
import custom_type_config.CustomTypeConfig;
import option_config.OptionConfig;
import option_config.OptionType;

public class OptionConfigImpl implements OptionConfig {
    private final boolean required;
    private final String description;
    private final String[] aliases;
    private final OptionType type;
    private final CustomTypeConfig customTypeConfig;
    private final Object[] constraints;

    public OptionConfigImpl(boolean required, String description, String[] aliases, OptionType type, CustomTypeConfig customTypeConfig, Object[] constraints) {
        this.required = required;
        this.description = description;
        this.aliases = aliases;
        this.type = type;
        this.customTypeConfig = customTypeConfig;
        this.constraints = constraints;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String[] getAliases() {
        return aliases;
    }

    @Override
    public OptionType getType() {
        return type;
    }

    public CustomTypeConfig getCustomTypeConfig() {
        return customTypeConfig;
    }

    @Override
    public Object[] getConstraints() {
        return constraints;
    }
}
