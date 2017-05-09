package com.phonecompany.service;

import com.phonecompany.dao.interfaces.OrderDao;
import com.phonecompany.model.CustomerTariff;
import com.phonecompany.model.Order;
import com.phonecompany.model.Tariff;
import com.phonecompany.model.enums.OrderStatus;
import com.phonecompany.service.interfaces.XSSFService;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.charts.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.charts.XSSFChartLegend;
import org.apache.poi.xssf.usermodel.charts.XSSFLineChartData;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class XSSFServiceImpl implements XSSFService {

    private static final Logger LOG = LoggerFactory.getLogger(XSSFServiceImpl.class);
    private static final String FILE_NAME = "report-";
    private static final String FILE_FORMAT = ".xlsx";
    private static final String TARIFFS = "Tariffs";
    private static final int DISTANCE_BETWEEN_TABLES = 25;

    private OrderDao orderDao;

    @Autowired
    public XSSFServiceImpl(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @Override
    public void generateReport(long regionId, LocalDate startDate, LocalDate endDate) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        workbook = this.getTariffReport(workbook, regionId, startDate, endDate);
        this.saveWorkBook(workbook);
    }

    private XSSFWorkbook getTariffReport(XSSFWorkbook workbook, long regionId,
                                         LocalDate startDate, LocalDate endDate) {
        List<Order> tariffOrdersByRegionId = this.orderDao.getTariffOrdersByRegionId(regionId);
        List<Order> tariffOrdersFilteredByDate = this.filterOrdersByDate(
                tariffOrdersByRegionId, startDate, endDate);

        Map<String, List<Order>> tariffOrdersMap = this.getTariffOrdersMap(tariffOrdersFilteredByDate);
        List<LocalDate> timeLine = this.generateTimeLine(tariffOrdersFilteredByDate);

        XSSFSheet sheet = workbook.createSheet(TARIFFS);
        this.generateReportTables(sheet, tariffOrdersMap, timeLine);

        return workbook;
    }

    private void generateReportTables(XSSFSheet sheet,
                                      Map<String, List<Order>> ordersMap,
                                      List<LocalDate> timeLine) {
        int rowPosition = 0;
        for (OrderStatus orderStatus : OrderStatus.values()) {
            this.createTableByStatus(sheet, rowPosition, orderStatus, ordersMap, timeLine);
            rowPosition += DISTANCE_BETWEEN_TABLES;
        }
    }

    private void createTableByStatus(XSSFSheet sheet, int rowPosition,
                                     OrderStatus orderStatus,
                                     Map<String, List<Order>> ordersMap,
                                     List<LocalDate> timeLine) {
        this.createTableHeading(sheet, rowPosition++, orderStatus.toString());
        int initialRowPosition = rowPosition;
        for (String productName : ordersMap.keySet()) {
            int colPosition = 1;
            List<Order> orders = this
                    .filterOrdersByStatus(ordersMap.get(productName), orderStatus);
            XSSFRow row = this.generateRowHeading(sheet, rowPosition++, productName);
            this.fillRow(row, colPosition, orders, timeLine);
        }
        this.generateColHeadings(sheet.createRow(rowPosition), timeLine);
        this.drawChart(sheet, initialRowPosition, rowPosition, timeLine.size());
    }

    private void createTableHeading(XSSFSheet sheet, int rowPosition, String tableHeading) {
        sheet.addMergedRegion(new CellRangeAddress(rowPosition, rowPosition, 2, 4));
        XSSFRow tableNameRow = sheet.createRow(rowPosition);
        XSSFCell tableNameCell = tableNameRow.createCell(2);
        tableNameCell.setCellType(CellType.STRING);
        tableNameCell.setCellValue(tableHeading);
    }

    private XSSFRow generateRowHeading(XSSFSheet sheet, int rowPosition, String headingName) {
        XSSFRow row = sheet.createRow(rowPosition);
        XSSFCell cell = row.createCell(0);
        cell.setCellType(CellType.STRING);
        cell.setCellValue(headingName);
        return row;
    }

    private void fillRow(XSSFRow row, int colPosition, List<Order> orders, List<LocalDate> timeLine) {
        for (LocalDate timePoint : timeLine) {
            long orderNumberByDate = this.getOrderNumberByDate(orders, timePoint);
            this.createCell(row, colPosition++, orderNumberByDate);
        }
    }

    private void createCell(XSSFRow row, int cellPosition, long cellValue) {
        XSSFCell cell = row.createCell(cellPosition);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(cellValue);
    }

    private void generateColHeadings(XSSFRow row, List<LocalDate> timeLine) {
        int cellPosition = 1;
        for (LocalDate timePoint : timeLine) {
            XSSFCell cell = row.createCell(cellPosition++);
            cell.setCellValue(timePoint.toString());
        }
    }

    private void drawChart(XSSFSheet sheet, int initialRowPosition, int rowIndex, int lastColIndex) {

        XSSFChart chart = this.createChart(sheet, initialRowPosition);
        this.useGapsOnBlankCells(chart);

        // Create data for the chart
        XSSFLineChartData data = chart.getChartDataFactory().createLineChartData();

        // Define chart AXIS
        ChartAxis bottomAxis = chart.getChartAxisFactory().createCategoryAxis(AxisPosition.BOTTOM);
        ValueAxis leftAxis = chart.getChartAxisFactory().createValueAxis(AxisPosition.LEFT);
        leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);

        // add chart series for each
        this.addChartSeries(sheet, data, initialRowPosition, rowIndex, lastColIndex);

        // Plot the chart with the inputs from data and chart axis
        chart.plot(data, bottomAxis, leftAxis);
        this.makeChartNotToUserSmoothedLines(chart);
    }

    private void makeChartNotToUserSmoothedLines(XSSFChart chart) {
        CTPlotArea plotArea = chart.getCTChart().getPlotArea();
        for (CTLineChart ch : plotArea.getLineChartList()) {
            for (CTLineSer ser : ch.getSerList()) {
                CTBoolean ctBool = CTBoolean.Factory.newInstance();
                ctBool.setVal(false);
                ser.setSmooth(ctBool);
            }
        }
    }

    private void useGapsOnBlankCells(XSSFChart chart) {
        CTDispBlanksAs disp = CTDispBlanksAs.Factory.newInstance();
        disp.setVal(STDispBlanksAs.GAP);
        chart.getCTChart().setDispBlanksAs(disp);
    }

    private void addChartSeries(XSSFSheet sheet, LineChartData data,
                                int initialRowPosition, int rowIndex,
                                int lastColIndex) {
        ChartDataSource<String> x = this.getDatePointsDataSource(sheet, rowIndex, lastColIndex);
        while (initialRowPosition < rowIndex) {
            ChartDataSource<Number> y = this.getOrderNumberDatasource(sheet, initialRowPosition++, lastColIndex);
            LineChartSeries lineChartSeries = data.addSeries(x, y);
            lineChartSeries.setTitle(new CellReference(initialRowPosition - 1, 0));
        }
    }

    private ChartDataSource<String> getDatePointsDataSource(XSSFSheet sheet,
                                                            int rowIndex,
                                                            int lastCellPosition) {
        return DataSources.fromStringCellRange(sheet,
                new CellRangeAddress(rowIndex, rowIndex, 1, lastCellPosition));
    }

    private ChartDataSource<Number> getOrderNumberDatasource(XSSFSheet sheet,
                                                             int rowIndex,
                                                             int collCount) {
        return DataSources.fromNumericCellRange(sheet,
                new CellRangeAddress(rowIndex, rowIndex, 1, collCount));
    }

    private XSSFChart createChart(XSSFSheet sheet, int rowIndex) {
        // Create a drawing canvas on the worksheet
        XSSFDrawing drawing = sheet.createDrawingPatriarch();

        // Define anchor points in the worksheet to position the chart
        XSSFClientAnchor anchor = drawing.createAnchor(
                0, 0, 0, 0, 7, rowIndex - 1,
                17, rowIndex + 20);

        // Create the chart object based on the anchor point
        XSSFChart chart = drawing.createChart(anchor);

        // Define legends for the line chart and set the position of the legend
        XSSFChartLegend legend = chart.getOrCreateLegend();
        legend.setPosition(LegendPosition.BOTTOM);
        return chart;
    }

    private long getOrderNumberByDate(List<Order> orderList,
                                      LocalDate date) {
        return orderList.stream()
                .filter(o -> o.getCreationDate().equals(date))
                .count();
    }

    private List<Order> filterOrdersByStatus(List<Order> orders, OrderStatus status) {
        return orders.stream()
                .filter(o -> o.getOrderStatus().equals(status))
                .collect(Collectors.toList());
    }


    private List<Order> filterOrdersByTariffName(List<Order> orders, String tariffName) {
        return orders.stream()
                .filter(order -> order.getCustomerTariff()
                        .getTariff().getTariffName().equals(tariffName))
                .collect(Collectors.toList());
    }

    private Map<String, List<Order>> getTariffOrdersMap(List<Order> tariffOrders) {
        Map<String, List<Order>> tariffOrdersMap = new HashMap<>();
        List<String> tariffNames = this.getDistinctTariffNamesFromOrders(tariffOrders);
        for (String tariffName : tariffNames) {
            List<Order> ordersOfTariff = this.filterOrdersByTariffName(tariffOrders, tariffName);
            this.putOrdersInMap(tariffOrdersMap, tariffName, ordersOfTariff);
        }
        return tariffOrdersMap;
    }

    private void putOrdersInMap(Map<String, List<Order>> tariffOrdersMap,
                                String tariffName, List<Order> ordersOfTariff) {
        if (tariffOrdersMap.get(tariffName) == null) {
            List<Order> orders = new ArrayList<>();
            orders.addAll(ordersOfTariff);
            tariffOrdersMap.put(tariffName, orders);
        } else {
            tariffOrdersMap.get(tariffName).addAll(ordersOfTariff);
        }
    }

    private List<String> getDistinctTariffNamesFromOrders(List<Order> tariffOrders) {
        return tariffOrders
                .stream().map(this.getTariffOrderToTariffNameMapping())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<LocalDate> generateTimeLine(List<Order> orders) {
        return orders.stream()
                .map(Order::getCreationDate)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    private void saveWorkBook(XSSFWorkbook workbook) {
        try {
            FileOutputStream out = new FileOutputStream(FILE_NAME + LocalDate.now() + FILE_FORMAT);
            workbook.write(out);
            workbook.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Function<Order, String> getTariffOrderToTariffNameMapping() {
        return order -> {
            CustomerTariff customerTariff = order.getCustomerTariff();
            Tariff tariff = customerTariff.getTariff();
            return tariff.getTariffName();
        };
    }

    private List<Order> filterOrdersByDate(List<Order> orderList,
                                           LocalDate startDate, LocalDate endDate) {
        return orderList.stream()
                .filter(t -> t.getCreationDate().isAfter(startDate) ||
                        t.getCreationDate().isEqual(startDate))
                .filter(t -> t.getCreationDate().isBefore(endDate) ||
                        t.getCreationDate().isEqual(endDate))
                .collect(Collectors.toList());
    }
}
