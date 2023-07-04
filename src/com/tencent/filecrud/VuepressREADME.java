package com.tencent.filecrud;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: VuepressREADME</p>
 * <p>
 *    Description:
 *    1. 该类实现功能为为同一分类文件夹生成 README.md 并写入固定格式内容
 *    2. 若该文件夹存在 README.md 清更新其内容
 *    3. 并延迟一秒创建 README.md 以便博客排序
 * </p>
 * @author xianxian
 * @date 2023年6月25日上午3:08:18
 */
public class VuepressREADME {
	
	final static String ABSOLUTEPATH = "E:\\Google\\vuepress-theme-hope\\docs\\zh\\programBlog";
//	final static String ABSOLUTEPATH = "E:\\Google\\vuepress-theme-hope\\test\\t1";
//	final static String ABSOLUTEPATH = "E:\\Google\\vuepress-starter\\otherBlog";

	public static void main(String[] args) {
		File[] sortDirectoryBycountFiles = sortDirectoryBycountFiles(ABSOLUTEPATH);
		
		//将数组元素反转 由多至少的排序
		Collections.reverse(Arrays.asList(sortDirectoryBycountFiles));
		
		for (int j = 0; j < sortDirectoryBycountFiles.length; j++) {
			File file_desc = sortDirectoryBycountFiles[j];
			try {
				//为README.md赋值
				creatREADMEandWriteContent(file_desc,j + 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}

	}
	
	/**
	 * <p>Title: sortDirectoryBycountFiles</p>
	 * <p>
	 *    Description:
	 *    根据给出的路径，对其含有 .md文件 的文件夹根据含有文件的数量进行排序
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
	public static String getPathVal(File file) {
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
	 * <p>Title: creatREADMEandWriteContent</p>
	 * <p>
	 *    Description: 这个方法会被重复使用，比如创建了新的文件，但是不想手动去更改对应的md功能，那么运行这程序即可
	 *    本方法实现以下功能：
	 *    1. 根据所提供的文件夹查找该文件夹内是否含有名为 README.md 的文件
	 *    2. 若存在则清空其内容写入新的内容 写入的内容 命名变量 readmeContent
	 *    3. 若不存则创建名为 README.md 文件
	 *    4. readmeContent的值为
	 *  
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param file
	 * @param starVal
	 * @author xianxian
	 * @date 2023年5月7日下午8:05:55
	 * @version 1.0
	 * @throws InterruptedException 
	 */
	private static void creatREADMEandWriteContent(File file,int starVal) throws InterruptedException {
		File readmeFile = new File(file, "README.md");
		if(isFileExistInDirectory(file, "README.md")) {
			//存在清空文件内容
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(readmeFile));
				writer.write("");
				writer.close();
				System.out.println(file.getName() + "存在readme则清空内容");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			//不存在创建文件 README.md 
			try {
				
				 Thread.sleep(1200); // 休眠1秒
				
				readmeFile.createNewFile();
				 System.out.println(file.getName() + "不存在 README.md 文件则创建文件");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//获取内容
		StringBuilder readmeContent = getReadmeContent(file,starVal);
		//写入内容
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(readmeFile, true));
			writer.write(readmeContent.toString());
			writer.close();
			System.out.println("写入成功");
//			System.out.println(readmeContent.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>Title: isFileExistInDirectory</p>
	 * <p>
	 *    Description:
	 * 		判断某个文件夹内是否含有某个文件
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param directory
	 * @param fileName
	 * @return
	 * @author xianxian
	 * @date 2023年5月7日下午8:15:38
	 * @version 1.0
	 */
    public static boolean isFileExistInDirectory(File directory, String fileName) {
        if (!directory.exists() || !directory.isDirectory()) {
            return false;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return false;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().equals(fileName)) {
                return true;
            }
        }

        return false;
    }
    
	/**
	 * <p>Title: getReadmeContent</p>
	 * <p>
	 *    Description:
	 *    根据所给的文件夹，生成 README.md 所需要的内容
---
star: 当前文件夹含有.md文件拉数量
date: 当前文件夹的创建时间
category:
    - 当前文件夹的名字
---

# 当前文件夹的名字

:::tip
number. 当前文件夹内文件的名字
:::  
	 *    
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param file
	 * @param starVal
	 * @return
	 * @author xianxian
	 * @date 2023年5月7日下午8:25:12
	 * @version 1.0
	 */
	private static StringBuilder getReadmeContent(File file,int starVal) {
		StringBuilder readmeContent = new StringBuilder();
		StringBuilder tipContent = new StringBuilder();
		//对文件进行排序 按数值由小及大
		File[] fileArray = sortMdFileByNum(file.listFiles(File::isFile));
		
		String nextLine = "\n";
		
		//当前文件夹的文件数量
//		int fileLength = -countFiles(file);
		
		//获取 README.md 的创建时间
		String creatTime = "";
		
		for (int i = 0; i < fileArray.length; i++) {
			File mdFile = fileArray[i];
			if(mdFile.getName().equals("README.md")) {
				try {
					creatTime = getFileCreatTime(mdFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}else {
				tipContent.append(i + ". " + getFileRealName(mdFile) + nextLine);
			}
		}
		
//		---
//		star: 当前文件夹含有.md文件拉数量
//		date: 当前文件夹的创建时间
//		category:
//		    - 当前文件夹的名字
//		---
//
//		# 当前文件夹的名字
//
//		:::tip
//		number. 当前文件夹内文件的名字
//		:::  
		readmeContent.append("---" + nextLine);
		readmeContent.append("star: " + starVal + nextLine);
		readmeContent.append("date: " + creatTime + nextLine);
		readmeContent.append("category:" + nextLine);
		readmeContent.append("    - " + file.getName() + nextLine);
		readmeContent.append("---" + nextLine);
		readmeContent.append(nextLine);
		readmeContent.append("# "+ file.getName() + nextLine);
		readmeContent.append(nextLine);
		readmeContent.append(":::tip" + nextLine);
		readmeContent.append(tipContent);
		readmeContent.append(":::" + nextLine);
		return readmeContent;
	}
	
	/**
	 * <p>Title: sortMdFileByNum</p>
	 * <p>
	 *    Description:
	 *    将文件夹里的Md文件按数字由小及大排序
	 *    注：本方法只考虑文件夹内含有 number-xxxx.md文件以及醉多一个README.md的情况存在
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param listFiles
	 * @return
	 * @author xianxian
	 * @date 2023年5月7日下午8:00:17
	 * @version 1.0
	 */
	private static File[] sortMdFileByNum(File[] listFiles) {
		  // 使用匿名内部类创建 Comparator 对象，根据文件名进行排序
        Arrays.sort(listFiles, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                // 提取文件名中的数字
                int n1 = extractNumber(f1.getName());
                int n2 = extractNumber(f2.getName());
                // 按照数字大小进行比较
                return Integer.compare(n1, n2);
            }

            private int extractNumber(String name) {
                // 从字符串开头开始提取数字，直到遇到非数字字符
                int i = 0;
                while (i < name.length() && Character.isDigit(name.charAt(i))) {
                    i++;
                }
                if (i == 0) {
                	return Integer.MIN_VALUE; // 如果字符串开头不是数字，则返回最小值
   
                }
                return Integer.parseInt(name.substring(0, i));
            }
        });
		return listFiles;
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
				file.getName().length()-3
				);
		return fileRealName;
	}
}
