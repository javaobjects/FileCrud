package src.com.tencent.filecrud;

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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadURLFile {
    public static void main(String[] args) {

//    	final String filePath = "E:\\Google\\vuepress-starter\\docs\\programBlog\\Eclipse\\7-Eclipse 配置JavaEE企业级项目.md";
//    	final String imgPath = "E:\\Google\\vuepress-starter\\docs\\programBlog\\Eclipse\\assets\\7-Images";
//    	final String newImgPath = "./assets/7-Images/";
//    	final String beginStr = "![image.png](";
//    	final String endStr = "?imageMogr2";  
        
    	//1. 先读取所有的文件看看是滞有认url形式存在的图片
    	File directory = new File("E:\\Google\\vuepress-starter\\docs\\programBlog"); // 文件夹路径
        File[] subDirectories = directory.listFiles(File::isDirectory); // 获取子文件夹
        for (File subdirectory : subDirectories) {
        	
        	File[] subFiles = subdirectory.listFiles(File::isFile);
        	
        	for (File file : subFiles) {
            	String savePath = file.getParent() + File.separator + "assets" + File.separator + file.getName().split("-")[0] + "-Images";
            	String newImgPath =  "./assets/" + file.getName().split("-")[0] + "-Images/";
            	System.out.println(savePath);
            	//2. 若有则创建对应的文件以务下载
//            	List<String> strBySubstringFromtxt = getStrBySubstringFromtxt(file);
//            	for (int i = 0; i < strBySubstringFromtxt.size(); i++) {
            	//3. 下载图片
//            		downloadByUrl(strBySubstringFromtxt.get(i), savePath, String.valueOf(i + 1));
//    				
//    			}
            	
            	
            	// 一定要让上面的下载完成方法执行完成 而后注释上面的执行 再执行更改文本的方法
            	//4. 将文件中的url图片地址更换成本地相当路径地址
    			try {
    				reNameStrBySubstringFromtxt(file, newImgPath);
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
			}

		}
    }
    
    
    /**
     * <p>Title: getStrBySubstringFromtxt</p>
     * <p>
     *    Description:
     *    将md文件中在线的url图片地址存入数组，且创建对应的图片文件夹以供后续下载,只需传入对应的md文件即可
     * </p>
     * <p>Copyright: Copyright (c) 2017</p>
     * <p>Company: www.baidudu.com</p>
     * @param file
     * @return
     * @author xianxian
     * @date 2023年5月1日下午10:41:43
     * @version 1.0
     */
	private static List<String> getStrBySubstringFromtxt(File file) {
		List<String> strArray = new ArrayList<String>();//存放Img 地址的
		String regex = "!\\[[^\\]]*\\]\\((.*?)\\)";//匹配url正则
    	Pattern pattern = Pattern.compile(regex);
		try {// try代码块，当发生异常时会转到catch代码块中
			// 创建类进行文件的读取，并指定编码格式为utf-8
			InputStreamReader read = new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "utf-8");
			BufferedReader in = new BufferedReader(read);// 可用于读取指定文件

			String linDatastr = null;// 定义一个字符串类型变量linDatastr 用于存放一行的文本数据
			while ((linDatastr = in.readLine()) != null) {// readLine()方法, 用于读取一行,只要读取内容不为空就一直执行
				
			 	Matcher matcher = pattern.matcher(linDatastr);
		    	while (matcher.find()) {
		    		/**
		    		 * 将 ![gjhgjg](https://upload-images.jianshu.io/upload_images/5227364-aa6e64559b66d342.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
		    		 * 提取为 https://upload-images.jianshu.io/upload_images/5227364-aa6e64559b66d342.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240
		    		 */
		    	    String imageUrl = matcher.group(1);
		    	    int index = imageUrl.indexOf('?'); // 找到问号
		    	    if (index >= 0) {//找不到index为-1则不存入
		    	        imageUrl = imageUrl.substring(0, index); // 去掉问号及其后面的部分
		    	        strArray.add(imageUrl);
		    	        System.out.println("imageUrl: " + imageUrl);
	
		    	        /**
		    	         * 1. 根据传入的参数 pathStr 7-Eclipse 配置JavaEE企业级项目.md"
		    	         * 判断文件的同级目录是否存在 assets 文件夹 如果不存在则创建它
		    	         * 2. 判断 assets 文件夹下是否存在 Num-Images 文件夹 若不存在则创建 num的取值为 传入的pathStr的值的名称以 - 分隔取下标0的值
		    	         * 
		    	         */
		    	        
		    	        // 1. 获取文件所在目录
		    	        String parentPath = file.getParent();
		    	        // 2. 判断 assets 目录是否存在，不存在则创建
		    	        File assetsDir = new File(parentPath, "assets");
		    	        if (!assetsDir.exists()) {
		    	            assetsDir.mkdir();
		    	            System.out.println(file.getName() + "创建了文件夹： assets" );
		    	        }
		    	        // 3. 获取 num 的值
		    	        String[] pathArr = file.getName().split("-");
		    	        String num = pathArr[0];
		    	        // 4. 判断 Num-Images 目录是否存在，不存在则创建
		    	        File numImagesDir = new File(assetsDir, num + "-Images");
		    	        if (!numImagesDir.exists()) {
		    	            numImagesDir.mkdir();
		    	            System.out.println(file.getName() + "创建了文件夹： " +  num + "-Images");
		    	        }
		    	    }
		    	}
			}
			in.close();// 关闭流
		} catch (IOException e) {// 当try代码块有异常时转到catch代码块
			e.printStackTrace();// printStackTrace()方法是打印异常信息在程序中出错的位置及原因
		}
		return strArray;
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
     * @throws Exception 
     */
    @SuppressWarnings("static-access")
	private static void reNameStrBySubstringFromtxt(File file,String newImgPath) throws Exception {
		try {// try代码块，当发生异常时会转到catch代码块中
			// 创建类进行文件的读取，并指定编码格式为utf-8
			InputStreamReader read = new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "utf-8");
			BufferedReader in = new BufferedReader(read);// 可用于读取指定文件
			String regex = "!\\[[^\\]]*\\]\\((.*?)\\)";//匹配url正则
	    	Pattern pattern = Pattern.compile(regex);
	    	
			String linDatastr = null;// 定义一个字符串类型变量linDatastr 用于存放一行的文本数据
			int newNameNum = 1;
			int lineNum = 0;
			while ((linDatastr = in.readLine()) != null) {// readLine()方法, 用于读取一行,只要读取内容不为空就一直执行
				lineNum++;
			 	Matcher matcher = pattern.matcher(linDatastr);
		    	while (matcher.find()) {
		    	    String imageUrl = matcher.group(1);
		    	    int index = imageUrl.indexOf('?'); // 找到问号
		    	    if (index >= 0) {//找不到index为-1则不存入
		    	        imageUrl = imageUrl.substring(0, index); // 去掉问号及其后面的部分

		    	        System.out.println("imageUrl: " + imageUrl);
	
		    	        String newImgAdress = "![](" + newImgPath  + String.valueOf(newNameNum) + imageUrl.substring(imageUrl.length() -4) + ")";
		    	        System.out.println("新地址： " + newImgAdress);
						
						System.out.println("更改了 ： " + lineNum + " 行数据");
						
						new AppointedLineNumberCRUD().setOrAddAppointedLineNumberDataByFiles(lineNum, newImgAdress, "set", file.getAbsolutePath());
						newNameNum++;
		    	        
		    	    }
		    	}
				
			}
			in.close();// 关闭流
		} catch (IOException e) {// 当try代码块有异常时转到catch代码块
			e.printStackTrace();// printStackTrace()方法是打印异常信息在程序中出错的位置及原因
		}
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
