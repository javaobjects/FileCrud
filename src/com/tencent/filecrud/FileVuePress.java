package com.tencent.filecrud;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * <p>Title: FileVuePress</p>
 * <p>
 *    Description:
 *    此类有如下作用：
 *    1. 按文件数量由小及大生成侧边栏的json代码
			{
                title: "",
                collapsable: true,//默认true 折叠
                initialOpenGroupIndex: -999,// 可选的, 默认值是 0
                children: [
                ]
            }
 *    2. 统计博文数量
 * </p>
 * @author xianxian
 * @date 2023年5月3日上午12:49:03
 */
public class FileVuePress {
	
	final static String ABSOLUTEPATH = "E:\\Google\\vuepress-starter\\docs\\programBlog";
//	final static String ABSOLUTEPATH = "E:\\Google\\vuepress-starter\\otherBlog";
	
	
	public static void main(String[] args) {
		
		int CountFiles = 0;
		File[] sortDirectoryBycountFiles = sortDirectoryBycountFiles(ABSOLUTEPATH);
		for (File file : sortDirectoryBycountFiles) {
			CountFiles += file.listFiles(File::isFile).length;
			// 打印文件名、目录中文件数以及所需格式的路径
//			System.out.println(file.getName() + " : " + file.listFiles(File::isFile).length + " : " + getPathVal(file));

		}
//		System.out.println("总计博客： " + CountFiles);
		System.out.println(getSidebarJsonSbr(sortDirectoryBycountFiles).toString());
	}

	
	/**
	 * <p>Title: getPathVal</p>
	 * <p>
	 *    Description:
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param file
	 * @return
	 * @author xianxian
	 * @date 2023年5月3日下午3:50:55
	 * @version 1.0
	 */
	private static String getPathVal(File file) {
		// 将当前文件的绝对路径分割成多个路径段
		String[] pathSegments = file.getAbsolutePath().split("\\\\");
		// 创建一个新的空列表来存储我们感兴趣的路径段
		List<String> pathList = new ArrayList<>();
		// 遍历拆分的路径中的每个路径段
		for (String pathSegment : pathSegments) {
			// 如果遇到"programBlog"，在路径列表中添加它并在前面加上"/"
			if (pathSegment.equals("programBlog")) {
				pathList.add("/" + pathSegment);
			}
			// 如果已经将"programBlog"添加到路径列表中，则添加当前路径段
			else if (pathList.size() > 0) {
				pathList.add(pathSegment);
			}
		}
		// 使用"/"分隔符将列表中的路径段连接起来以形成所需的路径格式
		String path = String.join("/", pathList);
		return path;
	}
	
	/**
	 * <p>Title: getSidebarJsonSbr</p>
	 * <p>
	 *    Description:
	 *    生成Sidebar所需要的Json数据
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param FileArray 只需传入文件夹的绝对路径
	 * @return
	 * @author xianxian
	 * @date 2023年5月3日下午3:46:12
	 * @version 1.0
	 */
	private static StringBuilder getSidebarJsonSbr(File[] FileArray) {
		
      int initialOpenGroupIndex = 1;
      int CountFiles = 0;
      StringBuilder sidebar_sb = new StringBuilder();
      
      for (int i = 0; i < FileArray.length; i++) {
      	String directoryAbsoultPath = FileArray[i].getAbsolutePath();
      	String pathVal = getPathVal(FileArray[i]) + "/";
      	StringBuilder sb = getSiderBarChildrenItemsVals(directoryAbsoultPath,pathVal);
      	CountFiles += FileArray[i].listFiles(File::isFile).length;
      	
//      	System.out.println(FileArray[i].getName() + " : " + FileArray[i].listFiles(File::isFile).length);
      	
      	sidebar_sb.append("{\n");
      	sidebar_sb.append("   \"title\": \"" + FileArray[i].getName() + "\",\n");
      	sidebar_sb.append("   \"initialOpenGroupIndex\": " + initialOpenGroupIndex + ",\n");
      	sidebar_sb.append("   \"children\": [\n");
      	sidebar_sb.append(sb);
      	sidebar_sb.append("   ]\n");
      	sidebar_sb.append("}");
          if (i < FileArray.length - 1) {
          	sidebar_sb.append(",\n");
          }
          initialOpenGroupIndex--;
      }
		
		
		return sidebar_sb;
	}
	
	/**
	 * <p>Title: sortDirectoryBycountFiles</p>
	 * <p>
	 *    Description:
	 *    根据给出的路径，对其含有文件的文件夹根据含有文件的数理进行排序
	 *    注：只针对含有文件的文件夹排序，若子文件夹没有文件就找子子文件夹，一直找到有文件的文件夹
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param absolutePath
	 * @return
	 * @author xianxian
	 * @date 2023年5月3日下午2:43:36
	 * @version 1.0
	 */
	private static File[] sortDirectoryBycountFiles(String absolutePath) {
		List<File> directories = findDirectoriesWithMdFiles(new File(absolutePath));
	    File[] sortedDirectories = directories.toArray(new File[0]);
	    Arrays.sort(sortedDirectories, Comparator.comparingInt(dir -> dir.listFiles(File::isFile).length));
	    return sortedDirectories;
	}

	/**
	 * <p>Title: findDirectoriesWithMdFiles</p>
	 * <p>
	 *    Description:
	 *    找到包含.md文件的文件夹
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param directory
	 * @return
	 * @author xianxian
	 * @date 2023年5月3日下午3:18:42
	 * @version 1.0
	 */
	private static List<File> findDirectoriesWithMdFiles(File directory) {
	    List<File> directories = new ArrayList<>(); // 创建一个保存文件夹的列表
	    if (directory.isDirectory()) { // 如果当前文件是一个文件夹
	        File[] subDirectories = directory.listFiles(File::isDirectory); // 获取当前文件夹下的子文件夹
	        for (File subDirectory : subDirectories) { // 遍历子文件夹
	            if (subDirectory.listFiles((dir, name) -> name.toLowerCase().endsWith(".md")).length > 0) { // 如果子文件夹中有以 .md 结尾的文件
	                directories.add(subDirectory); // 将当前子文件夹添加到列表中
	            } else { // 如果子文件夹中没有以 .md 结尾的文件
	                directories.addAll(findDirectoriesWithMdFiles(subDirectory)); // 递归查找子子文件夹中是否有以 .md 结尾的文件，将结果添加到列表中
	            }
	        }
	    }
	    return directories; // 返回所有包含 .md 文件的文件夹
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
			for (File file : files) {
	            if (file.getName().endsWith(".md")) {
	                count++;
	            }
	        }
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
//	            String pathFileName = fileItem.getName().split(".md")[0];
	            String pathFileName = fileItem.getName().substring(0, fileItem.getName().length()-3);
	            String fileRealName = getFileRealName(fileItem);
	            sbcJson.append("        {title: \"").append(fileRealName).append("\", path:\"").append(pathVal).append(pathFileName).append("\"}");
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
