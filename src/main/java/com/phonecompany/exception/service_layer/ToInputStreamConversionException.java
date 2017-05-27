package com.phonecompany.exception.service_layer;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

/**
 * Gets thrown if conversion to {@code InputStream} fails.
 *
 * @see com.phonecompany.service.XSSFServiceImpl#convertToInputStream(XSSFWorkbook)
 */
public class ToInputStreamConversionException extends RuntimeException {
    public ToInputStreamConversionException(IOException e) {
        this("Failed to convert workbook to InputStream", e);
    }

    public ToInputStreamConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
