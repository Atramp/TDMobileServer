package com.teradata.servlet.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teradata.dao.ApplicationDao;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class AppLogServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public AppLogServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy();
	}

	

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		String username =request.getParameter("username");
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 创建解析类的实例
		ServletFileUpload sfu = new ServletFileUpload(factory);
		// 开始解析
		sfu.setFileSizeMax(1024 * 1024 * 1024);
		
		ServletContext sctx = getServletContext();
		
		
		//上传到指定的路径上
		String path = sctx.getRealPath("/") + "logs" + File.separator;
		
		
		//上传文件接口
		try {
			List<FileItem> items = sfu.parseRequest(request);
			// 区分表单域
			for (int i = 0; i < items.size(); i++) {
				FileItem item = items.get(i);
				// isFormField为true，表示这不是文件上传表单域
				if (!item.isFormField()) {
					// 获得存放文件的物理路径
					// upload下的某个文件夹 得到当前在线的用户 找到对应的文件夹

					System.out.println(path);
					// 获得文件名
//					String fileName = username;
					// 该方法在某些平台(操作系统),会返回路径+文件名
					//String time = new SimpleDateFormat("yyyy-M-ddHH:mm:ss").format(new Date());
					String fileName = username+"_"+System.currentTimeMillis()+".log";
					System.out.println(fileName);
					File file = new File(path + "/" + fileName);
					if (file.exists()) {
						file.delete();
					}
					item.write(file);
					
					// 如果插入成功后
					try {
						response.getWriter().write("success");
						List dataList =readTxtFile(path+"/" + fileName);
						//插入数据库
						int result = ApplicationDao.getService().insertAppLog(dataList);
						if(result > 0)
							file.delete();
					} catch (RuntimeException e) {
						response.getWriter().write("insert fail");
						e.printStackTrace();
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("upload fail");

		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
	public static List readTxtFile(String filePath){
		List dataList = new ArrayList();
        try {
                String encoding="GBK";
                File file=new File(filePath);
                if(file.isFile() && file.exists()){ //判断文件是否存在
                    InputStreamReader read = new InputStreamReader(
                    new FileInputStream(file),encoding);//考虑到编码格式
                    BufferedReader bufferedReader = new BufferedReader(read);
                    String lineTxt = null;
                    //组装成
                   
                    while((lineTxt = bufferedReader.readLine()) != null){
                    	try {
                    	String datas[] =lineTxt.split(",",-1);
                        HashMap dataMap = new HashMap();
                        dataMap.put("dbName", "DWTTEMP");
            			dataMap.put("PAGE_ID", datas[0]);
            			dataMap.put("USER_ID", datas[1]);
            			dataMap.put("DATA_ID", datas[2]);
            			dataMap.put("ACCESS_START_TIME", formateDate(datas[3]));
            			dataMap.put("ACCESS_END_TIME", formateDate(datas[4]));
            			dataMap.put("LGTD", datas[5]);
            			dataMap.put("LAT", datas[6]);
            			dataMap.put("USER_OPR_TYP", datas[7]);
            			dataMap.put("PAGE_TYP", datas[8]);
            			dataMap.put("CLIENT_TERMINAL", datas[9]);
            			dataMap.put("BACK1", datas[10]);
            			dataMap.put("BACK2", datas[11]);
            			dataList.add(dataMap);
                    	}
            			 catch (RuntimeException e) {
            					e.printStackTrace();
            					 return null;
            			}
                        System.out.println(lineTxt);
                    }
                    read.close();
        }else{
            System.out.println("找不到指定的文件");
        }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
            return null;
        }
        return dataList;
    }
	
	public static String formateDate(String date){
		if(!date.trim().equals("")){
			long  longDate = new Long(date);
			Date date1 = new Date(longDate);
			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(date1);
			return time;
		}else{
			return "";
		}
	}
	
	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
	}

}
