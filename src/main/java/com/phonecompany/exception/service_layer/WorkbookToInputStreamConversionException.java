package com.phonecompany.exception.service_layer;

import java.io.IOException;

/**
 *
 */
public class WorkbookToInputStreamConversionException extends RuntimeException {
    public WorkbookToInputStreamConversionException(IOException e) {
        this("Failed to convert workbook to InputStream", e);
    }

    public WorkbookToInputStreamConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
