package com.opitral.diaryapi.exceptions;

public class NoSuchEntityException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "No entity of type '%s' and params %s";

    public NoSuchEntityException(String className, String params) {
        super(String.format(DEFAULT_MESSAGE, processClassName(className), params));
    }

    private static String processClassName(String className) {
        int dot = className.lastIndexOf('.');
        if (dot > -1) {
            className = className.substring(dot + 1);
        }

        return className.toLowerCase().replace("entity", "");
    }
}
