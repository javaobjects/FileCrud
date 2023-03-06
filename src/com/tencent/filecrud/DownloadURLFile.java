package com.tencent.filecrud;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DownloadURLFile {
    public static void main(String[] args) {
    	
        try {
        	List<String> getStrBySubstringFromtxt = getStrBySubstringFromtxt("E:\\简书\\程序语言\\AngularJS\\4-AngularJS入门必学（四）.md",
        			"![image.png](","?imageMogr2");
        	for (int i = 0; i < getStrBySubstringFromtxt.size(); i++) {
				String string = getStrBySubstringFromtxt.get(i);
				System.out.println(string);
	            downloadByUrl(string, 
        		"E:\\简书\\程序语言\\AngularJS\\4-Images",String.valueOf(i + 1));
			}
        	
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
//        reNameStrBySubstringFromtxt("E:\\简书\\程序语言\\AngularJS\\4-AngularJS入门必学（四）.md", "5227364-", "?imageMogr2");
    }
    
    /**
     * <p>Title: reNameStrBySubstringFromtxt</p>
     * <p>
     *    Description:专门为自己写的一个方法
     *    目的是将 https://upload-images.jianshu.io/upload_images/5227364-44ed6e8283157cbb.png
     *    转化成 num.png
     * </p>
     * <p>Copyright: Copyright (c) 2017</p>
     * <p>Company: www.baidudu.com</p>
     * @param pathStr
     * @param beginStr
     * @param endStr
     * @author xianxian
     * @date 2023年2月18日上午12:46:48
     * @version 1.0
     */
    private static void reNameStrBySubstringFromtxt(String pathStr,String beginStr,String endStr) {
		try {// try代码块，当发生异常时会转到catch代码块中
				// 读取指定的文件
			if (pathStr == null || pathStr.equals("")) {
				pathStr = System.getProperty("user.dir") + File.separator + "log.log";
			}
			// 创建类进行文件的读取，并指定编码格式为utf-8
			InputStreamReader read = new InputStreamReader(new FileInputStream(pathStr), "utf-8");
			BufferedReader in = new BufferedReader(read);// 可用于读取指定文件

			String linDatastr = null;// 定义一个字符串类型变量linDatastr 用于存放一行的文本数据
			int newNameNum = 1;
			int lineNum = 0;
			while ((linDatastr = in.readLine()) != null) {// readLine()方法, 用于读取一行,只要读取内容不为空就一直执行
				lineNum++;
				if (linDatastr.indexOf(beginStr) != -1) {
					String url = linDatastr.substring(linDatastr.indexOf(beginStr) + beginStr.length(),
							linDatastr.indexOf(endStr));
					System.out.println(url);
					
					String newImgAdress = "![](1-Images/" + String.valueOf(newNameNum) + url.substring(url.length() -4) + ")";
					
					System.out.println("新地址： " + newImgAdress);
					
					System.out.println("更改了 ： " + lineNum + " 行数据");
					
					try {
						new AppointedLineNumberCRUD().setOrAddAppointedLineNumberDataByFiles(lineNum, newImgAdress, "set", pathStr);
					} catch (Exception e) {
						e.printStackTrace();
					}
					newNameNum++;
				}
			}
			in.close();// 关闭流
		} catch (IOException e) {// 当try代码块有异常时转到catch代码块
			e.printStackTrace();// printStackTrace()方法是打印异常信息在程序中出错的位置及原因
		}
    }
    
    
    /**
     * <p>Title: getStrBySubstringFromtxt</p>  
     * <p>
     *    Description: 
     *    在文本中找到所需要的字符串 交将其存入集合
     *    例 找出   src: url(https://fonts/ieVi2ZhZI2eCN5jzbjEETS9weq8-32meGCsYb8td.woff2) format('woff2');
     *    中url 地址
     * </p>  
     * <p>Copyright: Copyright (c) 2017</p>  
     * <p>Company: www.baidudu.com</p>  
     * @param pathStr 需要读取的文本路径
     * @param beginStr 需要在每行中以 什么样的 字符串 开始找所需要的字符串
     * @param endStr 需要在每行中以 什么样的 字符串 结束 找所需要的字符串 
     * @return  
     * @author xianxian
     * @date 2023年1月28日  
     * @version 1.0
     */
	private static List<String> getStrBySubstringFromtxt(String pathStr,String beginStr,String endStr) {
		List<String> strArray = new ArrayList<String>();
		try {// try代码块，当发生异常时会转到catch代码块中
				// 读取指定的文件
			if (pathStr == null || pathStr.equals("")) {
				pathStr = System.getProperty("user.dir") + File.separator + "log.log";
			}
			// 创建类进行文件的读取，并指定编码格式为utf-8
			InputStreamReader read = new InputStreamReader(new FileInputStream(pathStr), "utf-8");
			BufferedReader in = new BufferedReader(read);// 可用于读取指定文件

			String linDatastr = null;// 定义一个字符串类型变量linDatastr 用于存放一行的文本数据
			while ((linDatastr = in.readLine()) != null) {// readLine()方法, 用于读取一行,只要读取内容不为空就一直执行
				if(linDatastr.indexOf(beginStr) != -1) {
					System.out.println(linDatastr.substring(linDatastr.indexOf(beginStr) + beginStr.length(), linDatastr.indexOf(endStr)));
					strArray.add(linDatastr.substring(linDatastr.indexOf(beginStr) + beginStr.length(), linDatastr.indexOf(endStr)));
				}
			}
			in.close();// 关闭流
		} catch (IOException e) {// 当try代码块有异常时转到catch代码块
			e.printStackTrace();// printStackTrace()方法是打印异常信息在程序中出错的位置及原因
		}
		return strArray;
	}
	
	/**
	 * <p>Title: downloadByUrl</p>
	 * <p>
	 *    Description:批量下载且重命名
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param urlStr
	 * @param savePath
	 * @param newName
	 * @author xianxian
	 * @date 2023年2月17日下午11:55:45
	 * @version 1.0
	 */
	public static void downloadByUrl(String urlStr, String savePath,String newName) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //设置部分请求头信息，根据自己的实际需要来书写，不需要的也可以删掉
            conn.setRequestProperty("api_token","Bearer_");
            conn.setRequestProperty("Cookie","XXL_JOB_LOGIN_IDENTITY=");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);
            //文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) { // 没有就创建该文件
                saveDir.mkdir();
            }
            String[] split = conn.toString().split("/");
            String fileName = URLDecoder.decode(split[split.length-1], "utf-8");
            System.out.println("fileName: " + fileName);
            System.out.println(fileName.split("\\.")[0]);
						//开始写入
            File file = new File(saveDir + File.separator + newName + "." + fileName.split("\\.")[1]);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            fos.close();
            inputStream.close();
            System.out.println("the file: " + url + " download success");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * <p>Title: downloadByUrl</p>  
	 * <p>
	 *    Description: 
	 *    从url中下载文件
	 * </p>  
	 * <p>Copyright: Copyright (c) 2017</p>  
	 * <p>Company: www.baidudu.com</p>  
	 * @param urlStr
	 * @param savePath  
	 * @author xianxian
	 * @date 2023年1月28日  
	 * @version 1.0
	 */
    public static void downloadByUrl(String urlStr, String savePath) {

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为5秒
            conn.setConnectTimeout(5 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //设置部分请求头信息，根据自己的实际需要来书写，不需要的也可以删掉
            conn.setRequestProperty("api_token","Bearer_");
            conn.setRequestProperty("Cookie","XXL_JOB_LOGIN_IDENTITY=");
            //得到输入流
            InputStream inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);
            //文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) { // 没有就创建该文件
                saveDir.mkdir();
            }
            String[] split = conn.toString().split("/");
            String fileName = URLDecoder.decode(split[split.length-1], "utf-8");
            System.out.println("fileName: " + fileName);
						//开始写入
            File file = new File(saveDir + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            fos.close();
            inputStream.close();
            System.out.println("the file: " + url + " download success");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * <p>Title: readInputStream</p>  
     * <p>
     *    Description: 
     *    从输入流中获取字节数组
     * </p>  
     * <p>Copyright: Copyright (c) 2017</p>  
     * <p>Company: www.baidudu.com</p>  
     * @param inputStream
     * @return
     * @throws IOException  
     * @author xianxian
     * @date 2023年1月28日  
     * @version 1.0
     */
    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[4 * 1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
