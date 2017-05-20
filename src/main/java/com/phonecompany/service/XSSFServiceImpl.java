package com.phonecompany.service;

import com.phonecompany.annotations.ServiceStereotype;
import com.phonecompany.service.interfaces.XSSFService;
import com.phonecompany.service.xssfHelper.BookDataSet;
import com.phonecompany.service.xssfHelper.RowDataSet;
import com.phonecompany.service.xssfHelper.SheetDataSet;
import com.phonecompany.service.xssfHelper.TableDataSet;
import org.apache.poi.ss.usermodel.charts.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.charts.XSSFChartLegend;
import org.apache.poi.xssf.usermodel.charts.XSSFLineChartData;
import org.javatuples.Pair;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.apache.poi.ss.usermodel.CellType.STRING;
import static org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER;
import static org.apache.poi.ss.util.CellUtil.setAlignment;

@ServiceStereotype
public class XSSFServiceImpl<K, V> implements XSSFService<K, V> {

    private static final Logger LOG = LoggerFactory.getLogger(XSSFServiceImpl.class);

    private static final int CHART_HEIGHT = 15;
    private static final int FIRST_ROW_INDEX = 0;
    private static final String PLURAL_FORM = "S";
    private int distanceBetweenTables = 25;


    @Override
    public InputStream generateReport(BookDataSet<K, V> book) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        for (SheetDataSet<K, V> sheetDataSet : book.getSheetDataSets()) {

            String sheetName = sheetDataSet.getSheetName();
            XSSFSheet sheet = workbook.createSheet(sheetName);

            int rowPosition = 0;
            for (TableDataSet<K, V> tableDataSet : sheetDataSet.getTableDataSets()) {
                this.createTable(sheet, rowPosition, tableDataSet);
                rowPosition += distanceBetweenTables;
            }
        }

        return this.saveWorkBook(workbook);
    }

    private void createTable(XSSFSheet sheet, int rowPosition,
                             TableDataSet<K, V> tableDataSet) {
        String tableName = tableDataSet.getTableDataSetName() + PLURAL_FORM;
        this.createTableHeading(sheet, rowPosition++, tableName);
        int initialRowPosition = rowPosition;
        for (RowDataSet<K, V> rowDataSet : tableDataSet.getRowDataSets()) {
            int colPosition = 1;
            XSSFRow row = this.generateRowHeading(sheet, rowPosition++, rowDataSet.getRowName());
            this.fillRow(row, colPosition, rowDataSet.getRowValues());
        }
        RowDataSet<K, V> firstTableRow = tableDataSet.getRowDataSets().get(FIRST_ROW_INDEX);
        this.generateColHeadings(sheet.createRow(rowPosition), firstTableRow.getRowValues());
        int rowValuesNumber = firstTableRow.getRowValues().size();
        distanceBetweenTables = rowValuesNumber + CHART_HEIGHT + 2;
        this.drawChart(sheet, initialRowPosition, rowPosition, rowValuesNumber, tableName);
    }

    /**
     * Generates a heading for the given table.
     *
     * @param sheet        sheet that the given table corresponds to
     * @param rowIndex     index of the row where heading would be placed into
     * @param tableHeading table heading
     */
    private void createTableHeading(XSSFSheet sheet, int rowIndex, String tableHeading) {
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 2, 4));
        XSSFRow tableNameRow = sheet.createRow(rowIndex);
        XSSFCell tableNameCell = tableNameRow.createCell(2);
        setAlignment(tableNameCell, CENTER);
        this.setHeadingCellStyle(tableNameCell);
        tableNameCell.setCellType(STRING);
        tableNameCell.setCellValue(tableHeading);
    }

    private void setHeadingCellStyle(XSSFCell cell) {
        XSSFSheet sheet = cell.getRow().getSheet();
        sheet.autoSizeColumn(cell.getColumnIndex());
    }

    /**
     * Generates a heading for the given row.
     *
     * @param sheet       sheet that the given row corresponds to
     * @param rowIndex    index of the row where heading would be placed into
     * @param headingName row heading
     * @return affected row
     */
    private XSSFRow generateRowHeading(XSSFSheet sheet, int rowIndex, String headingName) {
        XSSFRow row = sheet.createRow(rowIndex);
        XSSFCell cell = row.createCell(0);
        this.setHeadingCellStyle(cell);
        cell.setCellType(STRING);
        cell.setCellValue(headingName);
        return row;
    }

    /**
     * Fills the provided {@link XSSFRow} with the values corresponding to it.
     *
     * @param row      row to be populated with a set of values
     * @param colIndex index of the first column of the row
     * @param values   a list of {@code Pair<K, V>}s which represent row headings
     *                 and row values
     */
    private void fillRow(XSSFRow row, int colIndex, List<Pair<K, V>> values) {
        for (Pair<K, V> pair : values) {
            this.createCell(row, colIndex++, pair.getValue1());
        }
    }

    /**
     * Creates an {@link XSSFCell} with the specified value.
     *
     * @param row         row where the target cell is situated
     * @param colPosition column of the target cell in the row
     * @param cellValue   value to be put in the cell
     */
    private void createCell(XSSFRow row, int colPosition, V cellValue) {
        XSSFCell cell = row.createCell(colPosition);
        cell.setCellValue((Long) cellValue); //TODO: get rid of the cast
    }

    /**
     * Generates headings for all the columns of the incoming {@link XSSFRow}.
     *
     * @param row       row to generate headings for
     * @param rowValues a list of {@code Pair<K, V>}s where K is a column heading
     * @see RowDataSet
     */
    private void generateColHeadings(XSSFRow row, List<Pair<K, V>> rowValues) {
        int cellPosition = 1;
        for (Pair<K, V> pair : rowValues) {
            XSSFCell cell = row.createCell(cellPosition++);
            this.setHeadingCellStyle(cell);
            cell.setCellType(STRING);
            cell.setCellValue(pair.getValue0().toString());
        }
    }

    /**
     * Creates an {@link XSSFChart} line chart for a single table from the sheet.
     *
     * @param sheet              sheet containing data for the chart
     * @param initialRowPosition first row index of the table the chart will be generated for
     * @param rowIndex           last row index of the table the chart will be generated for
     * @param rowValuesNumber    number of values in the row
     * @param chartTitle
     */
    private void drawChart(XSSFSheet sheet, int initialRowPosition, int rowIndex,
                           int rowValuesNumber, String chartTitle) {

        XSSFChart chart = this.createChart(sheet, rowIndex);//initialRowPosition + rowValuesNumber);
        this.useGapsOnBlankCells(chart);
        this.setChartTitle(chart, chartTitle);
        // Create data for the chart
        XSSFLineChartData data = chart.getChartDataFactory().createLineChartData();

        // Define chart AXIS
        ChartAxis bottomAxis = chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
        ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        // add chart series for each
        this.addChartSeries(sheet, data, initialRowPosition, rowIndex, rowValuesNumber);

        // Plot the chart with the inputs from data and chart axis
        chart.plot(data, bottomAxis, leftAxis);
        this.noSmoothedLinesForChart(chart);
    }

    private void setChartTitle(XSSFChart chart, String chartName) {
        CTChart ctChart = chart.getCTChart();
        CTTitle title = ctChart.addNewTitle();
        CTTx tx = title.addNewTx();
        CTTextBody rich = tx.addNewRich();
        rich.addNewBodyPr();  // body properties must exist, but can be empty
        CTTextParagraph para = rich.addNewP();
        CTRegularTextRun r = para.addNewR();
        r.setT(chartName);
    }

    /**
     * Makes chart not to use smoothed lines.
     *
     * @param chart chart to be modified
     */
    private void noSmoothedLinesForChart(XSSFChart chart) {
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();
        for (CTLineChart ch : plotArea.getLineChartList()) {
            for (CTLineSer ser : ch.getSerList()) {
                CTBoolean ctBool = CTBoolean.Factory.newInstance();
                ctBool.setVal(false);
                ser.setSmooth(ctBool);
            }
        }
    }

    /**
     * Makes chart not to complement empty cell values with approximations.
     *
     * @param chart chart to be modified
     */
    private void useGapsOnBlankCells(XSSFChart chart) {
        CTDispBlanksAs disp = CTDispBlanksAs.Factory.newInstance();
        disp.setVal(STDispBlanksAs.GAP);
        chart.getCTChart().setDispBlanksAs(disp);
    }

    /**
     * /**
     * Adds chart series for each item containing in the table.
     *
     * @param sheet           sheet containing data
     * @param data            object to write the series data into
     * @param initialRowIndex index of the first table row
     * @param lastRowIndex    index of the last table row
     * @param lastColIndex    index of the last column in the row
     */
    private void addChartSeries(XSSFSheet sheet, LineChartData data,
                                int initialRowIndex, int lastRowIndex,
                                int lastColIndex) {
        ChartDataSource<String> x = this.getRangeOfDefinition(sheet, lastRowIndex, lastColIndex);
        while (initialRowIndex < lastRowIndex) {
            ChartDataSource<Number> y = this.getRangeOfValues(sheet, initialRowIndex++, lastColIndex);
            LineChartSeries lineChartSeries = data.addSeries(x, y);
            lineChartSeries.setTitle(new CellReference(initialRowIndex - 1, 0));
        }
    }

    /**
     * Creates a data range for the ordinate-axis of the subsequent graph.
     *
     * @param sheet        sheet containing data
     * @param rowIndex     index of the row to start fetching data from
     * @param lastColIndex index of the last column in the row
     * @return range of definition for the graph
     */
    private ChartDataSource<String> getRangeOfDefinition(XSSFSheet sheet,
                                                         int rowIndex,
                                                         int lastColIndex) {
        return DataSources.fromStringCellRange(sheet,
                new CellRangeAddress(rowIndex, rowIndex, 1, lastColIndex));
    }

    /**
     * Creates a data range for the abscissa-axis of the subsequent graph.
     *
     * @param sheet        sheet containing data
     * @param rowIndex     index of the row to start fetching data from
     * @param lastColIndex index of the last column in the row
     * @return range of values for the graph
     */
    private ChartDataSource<Number> getRangeOfValues(XSSFSheet sheet,
                                                     int rowIndex,
                                                     int lastColIndex) {
        return DataSources.fromNumericCellRange(sheet,
                new CellRangeAddress(rowIndex, rowIndex, 1, lastColIndex));
    }

    /**
     * Draws a line chart.
     *
     * @param sheet    sheet containing data.
     * @param rowIndex row index that helps to calculate chart position
     */
    private XSSFChart createChart(XSSFSheet sheet, int rowIndex) {
        // Create a drawing canvas on the worksheet
        XSSFDrawing drawing = sheet.createDrawingPatriarch();

        // Define anchor points in the worksheet to position the chart
        XSSFClientAnchor anchor = drawing.createAnchor(
                0, 0, 0, 0, 0, rowIndex + 2,
                9, rowIndex + CHART_HEIGHT + 2);

        // Create the chart object based on the anchor point
        XSSFChart chart = drawing.createChart(anchor);

        // Define legends for the line chart and set the position of the legend
        XSSFChartLegend legend = chart.getOrCreateLegend();
        legend.setPosition(LegendPosition.BOTTOM);
        return chart;
    }

    /**
     * Persist provided {@link XSSFWorkbook} workbook into the root of the project.
     *
     * @param workbook workbook to save.
     */
    private InputStream saveWorkBook(XSSFWorkbook workbook) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            workbook.write(byteArrayOutputStream);
            byteArrayOutputStream.flush();
            workbook.close();
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
