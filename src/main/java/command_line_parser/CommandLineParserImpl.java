package command_line_parser;

import custom_constraint.InConstraint;
import custom_type.CustomType;
import custom_type.ListString;
import exceptions.CommandLineParseException;
import exceptions.OptionNotPresentException;
import option_config.OptionConfig;
import option_config.OptionConfigImpl;
import option_config.OptionType;

import java.util.*;

public class CommandLineParserImpl implements CommandLineParser {

    private final Map<String, OptionConfig> options = new HashMap<>();
    private final Map<String, OptionConfig> reservedOptions = new HashMap<>();
    private final Map<String, Object> parsedValues = new HashMap<>();

    public CommandLineParserImpl() {
        addReservedOption("-h", new OptionConfigImpl(false, "Display help", new String[]{"-h", "--help"}, null, null, null));
    }

    @Override
    public void addOption(String option, OptionConfig config) {
        options.put(option, config);
    }

    @Override
    public void addReservedOption(String option, OptionConfig config) {
        reservedOptions.put(option, config);
    }

    @Override
    public void parse(String[] args) throws CommandLineParseException {
        int i = 0;
        while (i < args.length) {
            String currentArg = args[i];
            OptionConfig config = options.get(currentArg);

            if (options.containsKey(currentArg)) {
                if (config.getType() == OptionType.BOOLEAN) {
                    List<String> trueOptions = List.of("yes", "1", "on");
                    List<String> falseOptions = List.of("no", "0", "off");
                    if (currentArg.isEmpty()) {
                        parsedValues.put(currentArg, false);
                    } else if (!(i + 1 < args.length)) {
                        parsedValues.put(currentArg, true);
                    } else if (i + 1 < args.length && trueOptions.contains(args[i + 1])) {
                        parsedValues.put(currentArg, true);
                    } else if (i + 1 < args.length && falseOptions.contains(args[i + 1])) {
                        parsedValues.put(currentArg, false);
                    } else {
                        throw new CommandLineParseException("Invalid value for option: " + currentArg);
                    }
                    i++;
                } else if (config.getType() == OptionType.CUSTOM) {
                    String value = args[i + 1];
                    Object parsedValue = parseCustomOptionValue(value, config);
                    parsedValues.put(currentArg, parsedValue);
                    i++;
                } else if (i + 1 < args.length) {
                    String value = args[i + 1];
                    Object parsedValue = parseOptionValue(value, config.getType(), config.getConstraints());
                    parsedValues.put(currentArg, parsedValue);
                    i++;
                } else {
                    throw new CommandLineParseException("Missing value for option: " + currentArg);
                }
            } else if (Arrays.asList(reservedOptions.get("-h").getAliases()).contains(currentArg)) {
                printOptionsList(getOptions(), getReservedOptions());
            } else {
                throw new CommandLineParseException("Unknown option: " + currentArg);
            }
            i++;
        }

        // Validace povinných voleb
        for (Map.Entry<String, OptionConfig> entry : options.entrySet()) {
            String key = entry.getKey();
            OptionConfig optionConfig = entry.getValue();

            if (optionConfig.isRequired() && !parsedValues.containsKey(key)) {
                throw new CommandLineParseException("Required option not present: " + optionConfig.getAliases()[0]);
            }
        }
    }

    private Object parseOptionValue(String value, OptionType type, Object[] constraints) throws
            CommandLineParseException {

        switch (type) {
            case INTEGER -> {
                try {
                    int intValue = Integer.parseInt(value);
                    if (constraints != null && constraints.length == 2) {
                        int minValue = (int) constraints[0];
                        int maxValue = (int) constraints[1];
                        if (intValue < minValue || intValue > maxValue) {
                            throw new CommandLineParseException("Integer value out of bounds: " + value);
                        }
                    }
                    return intValue;
                } catch (NumberFormatException e) {
                    throw new CommandLineParseException("Invalid integer value: " + value);
                }
            }
            case STRING -> {
                if (constraints != null && constraints.length == 2) {
                    int minLength = (int) constraints[0];
                    int maxLength = (int) constraints[1];
                    if (value.length() < minLength || value.length() > maxLength) {
                        throw new CommandLineParseException("String length out of bounds: " + value);
                    }
                }
                return value;
            }
            case BOOLEAN -> {
                return value;
            }
            case ENUM -> {
                return value;
            }

            default -> throw new CommandLineParseException("Unsupported option type: " + type);
        }
    }

    private Object parseCustomOptionValue(String value, OptionConfig config) throws CommandLineParseException {
        CustomType customType = config.getCustomTypeConfig().getCustomType();
        Object[] constraints = config.getCustomTypeConfig().getConstraints();

        if (customType instanceof ListString listString) {
            for (Object constraint : constraints) {
                if (constraint instanceof InConstraint<?> inConstraint) {
                    if (!inConstraint.areConstraintValuesInValue(value)) {
                        throw new CommandLineParseException("List does not contain value: " + value);
                    }
                }
            }
            listString.setValues(Arrays.asList(value.split(",")));
            return true;
        }

        throw new CommandLineParseException("Unsupported custom type: " + customType.getClass());
    }

    @Override
    public List<OptionConfig> getOptions() {
        return new ArrayList<>(options.values());
    }

    @Override
    public List<OptionConfig> getReservedOptions() {
        return new ArrayList<>(reservedOptions.values());
    }

    @Override
    public Object getOptionValue(String option) throws OptionNotPresentException {
        if (parsedValues.containsKey(option)) {
            return parsedValues.get(option);
        } else {
            OptionConfig optionConfig = options.get(option);
            if (optionConfig != null && optionConfig.getType() == OptionType.BOOLEAN) {
                return false;
            } else {
                throw new OptionNotPresentException("Required option not present: " + option);
            }
        }
    }

    @Override
    public char getOperatorSymbol(String operator) {
        return switch (operator) {
            case "PLUS" -> '+';
            case "MINUS" -> '-';
            case "MULTIPLY" -> '*';
            case "DIVIDE" -> '/';
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    @Override
    public void printOptionsList(List<OptionConfig> options, List<OptionConfig> reservedOptions) {
        System.out.println("List of options and configurations:");
        for (OptionConfig config : options) {
            System.out.println("Option: " + Arrays.toString(config.getAliases()));
            System.out.println("  Description: " + config.getDescription());
        }
        System.out.println("\nList of reserved options and configurations:");
        for (OptionConfig reservedConfig : reservedOptions) {
            System.out.println("Option: " + Arrays.toString(reservedConfig.getAliases()));
            System.out.println("  Description: " + reservedConfig.getDescription());
        }
    }
}