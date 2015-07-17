package com.teradata.servlet.application;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teradata.common.config.Configuration;
import com.teradata.common.utils.CommonUtil;
import com.teradata.common.utils.FreeMarkerUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import sun.jvmstat.perfdata.monitor.PerfStringMonitor;

public class PictureReplacementServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=UTF-8");
        // 为解析类提供配置信息
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 创建解析类的实例
        ServletFileUpload sfu = new ServletFileUpload(factory);
        // 开始解析
        sfu.setFileSizeMax(1024 * 1024 * 1024);

        String path = Configuration.get("CLIENT_SLIDE_PICTURE_DIC_PATH");

        List<String> picNameList = new ArrayList<String>();
        // 上传文件
        try {
            // 清理目录
            boolean clearFlag = false;
            File dic = new File(path);
            List<FileItem> items = sfu.parseRequest(req);
            // 区分表单域
            for (int i = 0;          i < items.size(); i++) {
                FileItem item = items.get(i);
                // isFormField为true，表示这不是文件上传表单域
                if (!item.isFormField() && item.getSize() > 0) {
                    if (!clearFlag && dic.exists()) {
                        for (File file : dic.listFiles()) {
                            file.delete();
                        }
                        clearFlag = true;
                    }
                    // 获得文件名
                    String fileName = item.getName();
                    // 该方法在某些平台(操作系统),会返回路径+文件名
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                    File file = new File(path + "/" + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    item.write(file);
                    picNameList.add(fileName);
                }
            }
            Map freemarkerData = new HashMap();
            freemarkerData.put("fileNames", picNameList);
            // 更新pictures.js
            try {
                File file = new File(Configuration.get("CLIENT_SLIDE_PICTURE_JS_PATH"));
                if (file == null || !file.exists()) {
                    file.createNewFile();
                }
                FreeMarkerUtil.genXML("jsFile.ftl", freemarkerData, new FileWriter(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String version = CommonUtil.Date.getStandardTimestamp();
            // 更新appcache
            try {
                freemarkerData.put("version", version);
                File file = new File(Configuration.get("CLIENT_SLIDE_APPCACHE_PATH"));
                if (file == null || !file.exists()) {
                    file.createNewFile();
                }
                FreeMarkerUtil.genXML("appcache.ftl", freemarkerData, new FileWriter(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // 更新index.html
            try {
                freemarkerData.put("version", version);
                File file = new File(Configuration.get("CLIENT_SLIDE_HTML_PATH"));
                if (file == null || !file.exists()) {
                    file.createNewFile();
                }
                FreeMarkerUtil.genXML("clientSlide.ftl", freemarkerData, new FileWriter(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

            resp.getWriter().write(picNameList.toString());
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("上传失败");
        }

    }
}
