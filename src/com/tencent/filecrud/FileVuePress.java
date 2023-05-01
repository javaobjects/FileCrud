package src.com.tencent.filecrud;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;


public class FileVuePress {
	public static void main(String[] args) {

	    File directory = new File("E:\\Google\\vuepress-starter\\docs\\programBlog"); // 文件夹路径
        File[] subDirectories = directory.listFiles(File::isDirectory); // 获取子文件夹
        Arrays.sort(subDirectories, Comparator.comparingInt(FileVuePress::countFiles)); // 按文件数量排序
   
        int initialOpenGroupIndex = 1;
        StringBuilder sidebar_sb = new StringBuilder();
        for (int i = 0; i < subDirectories.length; i++) {
        	String directoryAbsoultPath = subDirectories[i].getAbsolutePath();
        	String pathVal = "/programBlog/" + subDirectories[i].getName() + "/";
        	StringBuilder sb = getSiderBarChildrenItemsVals(directoryAbsoultPath,pathVal);
        	sidebar_sb.append("{\n");
        	sidebar_sb.append("   \"title\": \"" + subDirectories[i].getName() + "\",\n");
        	sidebar_sb.append("   \"initialOpenGroupIndex\": " + initialOpenGroupIndex + ",\n");
        	sidebar_sb.append("   \"children\": [\n");
        	sidebar_sb.append(sb);
        	sidebar_sb.append("   ]\n");
        	sidebar_sb.append("}");
            if (i < subDirectories.length - 1) {
            	sidebar_sb.append(",\n");
            }
            initialOpenGroupIndex--;
        }
        System.out.println(sidebar_sb.toString());
	}
	
	
	/**
	 * <p>Title: countFiles</p>
	 * <p>
	 *    Description:
	 *    统计文件夹中有多少文件
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param directory
	 * @return
	 * @author xianxian
	 * @date 2023年5月1日下午3:39:05
	 * @version 1.0
	 */
	private static int countFiles(File directory) {
		int count = 0;
		File[] files = directory.listFiles(File::isFile); // 获取文件
		if (files != null) {
			count = files.length;
		}
		return count;
	}
	

	/**
	 * <p>Title: isFirstCharDigit</p>
	 * <p>
	 *    Description:
	 *    判断字符串首个字符是否为数字
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param str
	 * @return
	 * @author xianxian
	 * @date 2023年4月30日下午12:09:54
	 * @version 1.0
	 */
	public static boolean isFirstCharDigitByReg(String str) {
	    return str.matches("^\\d.*");
	}

	
	/**
	 * <p>Title: isFirstCharDigitByCharacter</p>
	 * <p>
	 *    Description:判断字符串首个字符是否为数字
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param str
	 * @return
	 * @author xianxian
	 * @date 2023年4月30日下午1:56:40
	 * @version 1.0
	 */
    public static boolean isFirstCharDigitByCharacter(String str) {
        return Character.isDigit(str.charAt(0));
    }
	
	
	/**
	 * <p>Title: getSiderBarChildrenItemsVals</p>
	 * <p>
	 *    Description:
	 *    根据给出的文件夹读取文件夹里的文件名，生成如下的json格式并打印在控制台
	 *                 { title: "开篇", path: "/programBlog/Oracle-2023/1-Oracle-开篇"  },
	{ title: "数据库基础-基础概念", path: "/programBlog/Oracle-2023/2-Oracle-数据库基础-基础概念"  },
	{ title: "数据库安装及可视化工具plsql安装", path: "/programBlog/Oracle-2023/3-Oracle数据库安装及可视化工具plsql安装"  }
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param directoryAbsoultPath E:\\Google\\vuepress-starter\\docs\\programBlog\\Oracle-2023
	 * @param pathVal /programBlog/Oracle-2023/
	 * @author xianxian
	 * @date 2023年4月30日上午12:34:25
	 * @version 1.0
	 */
	private static StringBuilder getSiderBarChildrenItemsVals(String directoryAbsoultPath,String pathVal) {
		File directory = new File(directoryAbsoultPath);
		File[] fileArray = directory.listFiles((dir, name) -> Character.isDigit(name.charAt(0)));//首字符为数字则进入
		StringBuilder sbcJson = new StringBuilder();
		

		Arrays.sort(fileArray, new Comparator<File>() {
		    @Override
		    public int compare(File o1, File o2) {
		    	 int num1 = Integer.parseInt(o1.getName().split("-")[0]);
		         int num2 = Integer.parseInt(o2.getName().split("-")[0]);
		         return Integer.compare(num1, num2);
		    }
		});

		int lastIndex = fileArray.length - 1;
	    for (int i = 0; i < fileArray.length; i++) {
	    	File fileItem = fileArray[i];
		    if(fileItem.isFile()) {
		        if(Character.isDigit(fileItem.getName().charAt(0))) { 

		        }
	            String pathFileName = fileItem.getName().split(".md")[0];
	            String fileRealName = getFileRealName(fileItem);
	            sbcJson.append("{title: \"").append(fileRealName).append("\", path:\"").append(pathVal).append(pathFileName).append("\"}");
	            if (i != lastIndex) {
	                sbcJson.append(",\r\n");
	            }else {
	            	sbcJson.append("\r\n");
	            }
		    }
		}

	    
//		System.out.println(sbcJson.toString());
		
		return sbcJson;
	}
	
	
	/**
	 * <p>Title: getFileCreatTime</p>
	 * <p>
	 *    Description:
	 *    获取文件的创建时间
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param file
	 * @return
	 * @throws IOException
	 * @author xianxian
	 * @date 2023年4月30日上午1:14:40
	 * @version 1.0
	 */
	private static String getFileCreatTime(File file) throws IOException {
		
		BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        FileTime createTime = attrs.creationTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTimeStr = sdf.format(new Date(createTime.toMillis()));
        return createTimeStr;
	}

	
	/**
	 * <p>Title: getFileRealName</p>
	 * <p>
	 *    Description:
	 *    获取如下格式的文件的真实名
	 *    6-Oracle-SQL开发 —— 限制数据和对数据排序.md
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param file
	 * @return
	 * @author xianxian
	 * @date 2023年4月30日上午1:22:36
	 * @version 1.0
	 */
	private static String getFileRealName(File file) {
		// 
		String fileRealName = null;
		fileRealName = file.getName().substring(
				file.getName().indexOf("-") + 1, 
				file.getName().indexOf(".md")
				);
		return fileRealName;
	}
}
