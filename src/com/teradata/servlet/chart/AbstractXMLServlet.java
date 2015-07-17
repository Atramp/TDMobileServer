package com.teradata.servlet.chart;

import com.teradata.bean.chart.ChartObject;
import com.teradata.common.utils.FreeMarkerUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public abstract class AbstractXMLServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private void writeXML(String templateName, Object data, Writer writer) {
        FreeMarkerUtil.genXML(templateName, data, writer);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String kpiId = request.getParameter("kpiid");
        String kpiBelonging = request.getAttribute("kpiBelonging").toString();

        String[] chartTypeConfig = ChartObject.getChartType(kpiId, kpiBelonging);
        if (null == chartTypeConfig || chartTypeConfig.length < 2)
            return;
        String chartTypeCode = chartTypeConfig[0];

        // 设置图表类型
        request.setAttribute("CHART_TYPE_CODE", chartTypeCode);

        // 获取数据
        ChartObject data = processDataWithTemplate(request);

        chartTypeCode = request.getAttribute("CHART_TYPE_CODE").toString();

        // 输出xml
        if (null != data) {
            Writer writer = new StringWriter();
            if (request.getParameter("debug") != null) { // 测试模式下显示XML，方便查看
                response.setContentType("text/xml");
                data.getExtensions().put("CHART_TYPE_CODE", chartTypeCode);
                writeXML(chartTypeConfig[1], data, writer);
                response.getWriter().write(writer.toString());
                return;
            }
            writeXML(chartTypeConfig[1], data, writer);
            response.getWriter().write(chartTypeCode + "\n" + writer.toString());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected abstract ChartObject processDataWithTemplate(HttpServletRequest request);

}
