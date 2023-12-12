package command_line_parser;

import exceptions.CommandLineParseException;
import exceptions.OptionNotPresentException;
import option_config.OptionConfig;

import java.util.List;

public interface CommandLineParser {

    void addOption(String option, OptionConfig config);

    void addReservedOption(String option, OptionConfig config);

    void parse(String[] args) throws CommandLineParseException;

    List<OptionConfig> getOptions();

    List<OptionConfig> getReservedOptions();

    Object getOptionValue(String option) throws OptionNotPresentException;

    char getOperatorSymbol(String operator);

    void printOptionsList(List<OptionConfig> options, List<OptionConfig> reservedOptions);
}
