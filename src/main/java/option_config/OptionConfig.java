package option_config;

import custom_type_config.CustomTypeConfig;

public interface OptionConfig {

    boolean isRequired();

    String getDescription();

    String[] getAliases();

    OptionType getType();

    CustomTypeConfig getCustomTypeConfig();

    Object[] getConstraints();
}
