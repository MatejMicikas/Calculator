import calculator.Calculator;
import command_line_parser.CommandLineParser;
import command_line_parser.CommandLineParserImpl;
import custom_constraint.InConstraint;
import custom_type.ListString;
import custom_type_config.CustomTypeConfig;
import exceptions.CommandLineParseException;
import exceptions.OptionNotPresentException;
import option_config.OptionConfigImpl;
import option_config.OptionType;

import java.util.Arrays;
import java.util.List;

public class CalculatorApp {

    public static void main(String[] args) {

        CommandLineParser parser = new CommandLineParserImpl();

        // Definice voleb pro kalkulačku
        parser.addOption("-l", new OptionConfigImpl(true, "Left operand", new String[]{"-l", "--left"}, OptionType.INTEGER, null, new Integer[]{0, Integer.MAX_VALUE}));
        parser.addOption("-r", new OptionConfigImpl(true, "Right operand", new String[]{"-r", "--right"}, OptionType.INTEGER, null, new Integer[]{0, Integer.MAX_VALUE}));
        parser.addOption("-o", new OptionConfigImpl(true, "Operator", new String[]{"-o", "--operator"}, OptionType.ENUM, null, new String[]{"PLUS", "MINUS", "MULTIPLY", "DIVIDE"}));
        parser.addOption("-v", new OptionConfigImpl(false, "Verbose", new String[]{"-v", "--verbose"}, OptionType.BOOLEAN, null, null));
//        parser.addOption("-ls", new OptionConfigImpl(true, "List of strings", new String[]{"-ls", "--list"}, OptionType.CUSTOM, new CustomTypeConfig(new ListString(), new Object[]{new InConstraint<>("car")}), null));

        try {
            parser.parse(args);

            if (args.length > 1) {
                // Získání hodnot voleb
                int leftOperand = (int) parser.getOptionValue("-l");
                int rightOperand = (int) parser.getOptionValue("-r");
                String operator = (String) parser.getOptionValue("-o");
                boolean verbose = (boolean) parser.getOptionValue("-v");

                int result = Calculator.calculate(leftOperand, rightOperand, operator);
                if (result != Integer.MIN_VALUE) {
                    if (verbose) {
                        System.out.println(leftOperand + String.valueOf(parser.getOperatorSymbol(operator)) + rightOperand + "=" + result);
                    } else {
                        System.out.println(result);
                    }
                }
            }
        } catch (OptionNotPresentException | CommandLineParseException e) {
            e.printStackTrace();
        }
    }
}
