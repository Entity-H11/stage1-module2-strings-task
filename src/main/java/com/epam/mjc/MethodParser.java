package com.epam.mjc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;

public class MethodParser {

    /**
     * Parses string that represents a method signature and stores all it's members into a {@link MethodSignature} object.
     * signatureString is a java-like method signature with following parts:
     * 1. access modifier - optional, followed by space: ' '
     * 2. return type - followed by space: ' '
     * 3. method name
     * 4. arguments - surrounded with braces: '()' and separated by commas: ','
     * Each argument consists of argument type and argument name, separated by space: ' '.
     * Examples:
     * accessModifier returnType methodName(argumentType1 argumentName1, argumentType2 argumentName2)
     * private void log(String value)
     * Vector3 distort(int x, int y, int z, float magnitude)
     * public DateTime getCurrentDateTime()
     *
     * @param signatureString source string to parse
     * @return {@link MethodSignature} object filled with parsed values from source string
     */
    public MethodSignature parseFunction(String signatureString) {
        final int FROM_THE_BEGINING = 0;
        int argumentDelimiter = signatureString.indexOf("(");
        String signatureArguments = signatureString.substring(argumentDelimiter).replaceAll("[()]", "");
        String signatureBody = signatureString.substring(FROM_THE_BEGINING, argumentDelimiter);

        //PARSING BODY
        StringTokenizer tokenizer = new StringTokenizer(signatureBody, " ");
        Optional<String> accessModifier = Optional.empty();
        if (tokenizer.countTokens() == 3) accessModifier = Optional.of(tokenizer.nextToken());
        String returnType = tokenizer.nextToken();
        String methodName = tokenizer.nextToken();

        //PARSING ARGUMENTS
        List<MethodSignature.Argument> arguments = new ArrayList<>();
        tokenizer = signatureArguments.contains(",") ?
                new StringTokenizer(signatureArguments, ", ") :
                new StringTokenizer(signatureArguments, " ");
        while (tokenizer.hasMoreTokens()) {
            arguments.add(new MethodSignature.Argument(tokenizer.nextToken(), tokenizer.nextToken()));
        }

        //CREATING THE METHOD SIGNATURE
        MethodSignature methodSignature = new MethodSignature(methodName, arguments);
        methodSignature.setReturnType(returnType);
        accessModifier.ifPresent(methodSignature::setAccessModifier);

        return methodSignature;
    }
}
