package com.tencent.filecrud;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Title: VuepressMore</p>
 * <p>
 *    Description:
 *    更新或插入number-xxxx.md文件的头部固定格式内容
 *    以“<!-- more -->”结尾
 * </p>
 * @author xianxian
 * @date 2023年6月25日上午2:51:28
 */
public class VuepressMore {
	
//	final static String FOLDERPATH = "E:\\Google\\vuepress-theme-hope\\docs\\zh\\programBlog";
	final static String FOLDERPATH = "E:\\Google\\vuepress-theme-hope\\docs\\zh\\2023";
//	final static String FOLDERPATH = "E:\\Google\\vuepress-theme-hope\\test\\t1";
//	final static String FOLDERPATH = "E:\\Google\\vuepress-theme-hope\\docs\\zh\\programBlog\\LittleBlogs\\Windows";

	public static void main(String[] args) {
		try {
			//将所有的 number-xxxx.md 形式的md文件的头部内容实现更新或插入
			replaceHeadContentInMdFile(FOLDERPATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * <p>Title: replaceHeadContentInMdFile</p>
	 * <p>
	 *    Description:
	 *    本方法实现找到文件的功能
	 *    若找不到则递归调用自己直到找到
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @author xianxian
	 * @date 2023年5月8日下午1:26:40
	 * @version 1.0
	 * @throws IOException 
	 */
	private static void replaceHeadContentInMdFile(String folderPath) throws IOException {
	    // 获取当前文件夹对象
	    File dir = new File(folderPath);
	    if (dir.isDirectory()) {
	        File[] files = dir.listFiles();
	        for (File file : files) {
	            if (file.isDirectory()) {
	                // 递归调用处理子目录
	            	replaceHeadContentInMdFile(file.getAbsolutePath());
	            } else {
	            	processFileHeadcontent(file);
	            }
	        }
	    }
	}
	
	/**
	 * <p>Title: processFileHeadcontent</p>
	 * <p>
	 *    Description:
	 *    该方法实现了更新或插入头部固定格式的功能
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param file
	 * @author xianxian
	 * @date 2023年5月8日下午4:54:25
	 * @version 1.0
	 */
	private static void processFileHeadcontent(File file) {
		// 检查文件是否是以 '.md'结尾的且名字不可以为 'README.md'
		if (file.getName().endsWith(".md") && !file.getName().equals("README.md")) {
			try {
				// 读取文件内容为一个字符串
				String content = new String(Files.readAllBytes(file.toPath()));
				// 创建一个模式来匹配以 '---' 开头且以 '<!-- more -->' 结尾的内容
				Pattern pattern = Pattern.compile("^---\\R(.+?)\\R<!-- more -->\\R", Pattern.DOTALL);
				// 创建一个匹配器来在内容中找到这个内容
				Matcher matcher = pattern.matcher(content);
				// 创建一个新的内容字符串，使用所需的格式
				String newContent = createNewContent(file);
				
				// 检查匹配器是否找到这个内容
				if (matcher.find()) {
					// 用新的内容替换这个内容
					content = matcher.replaceFirst(newContent);
					 // 重置 Matcher，以便进行新的匹配操作
				    matcher.reset();
					System.out.println(file.getName() + "替换了");
				} else {
					// 在文件的开头插入新的内容
					content = newContent + content;
					System.out.println(file.getName() + "插入了");
				}
				// 将修改后的内容写回文件
				Files.write(file.toPath(), content.getBytes());
			} catch (IOException e) {
				// 处理任何IO异常
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * <p>Title: createNewContent</p>
	 * <p>
	 *    Description:
	 *    生成需要插入或更新的内容
	 *    
---
title: title
author: 涎涎
date: 2020-01-01 19:15:12
icon: page
order: number
category:
	- Oracle
tag:
	- Oracle
head:
    - - meta
      - name: keywords
        content: title
---
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param file
	 * @return
	 * @author xianxian
	 * @date 2023年5月8日下午4:53:41
	 * @version 1.0
	 */
	public static String createNewContent(File file) {
		
		// 文件父路径
		String filePName = file.getParentFile().getName();
		//获取绝对路径
		String fileP2Name = file.getParentFile().getParentFile().getName();
		String fileP3Name = file.getParentFile().getParentFile().getParentFile().getName();
		String fileXdPaht = fileP3Name + "/" + fileP2Name + "/" + filePName;

		
		// 创建一个字符串构建器来追加新的内容
		StringBuilder sb = new StringBuilder();
		// 追加开头的 ---
		sb.append("---\n");
		// 创建一个模式来从文件名中匹配数字和标题
		Pattern pattern = Pattern.compile("^(\\d+)-(.+\\.md)$");
		// 创建一个匹配器来在文件名中找到数字和标题
		Matcher matcher = pattern.matcher(file.getName());

		String oreder = null;

		// 检查匹配器是否找到数字和标题
		if (matcher.find()) {
			oreder = matcher.group(1);
			// 追加标题，使用匹配器的第二个组
			sb.append("title: ").append(matcher.group(2).replaceAll("^\\d+-|\\.md$", "")).append("\n");

		}
		// 追加图标为 page
		sb.append("icon: page\n");
		// 追加顺序，使用匹配器的第一个组
		sb.append("order: ").append(oreder).append("\n");

		// 追加作者为 涎涎
		sb.append("author: 涎涎\n");

		// 追加日期为文件创建时间，使用 yyyy-MM-dd 格式
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
			sb.append("date: ").append(sdf.format(attr.creationTime().toMillis())).append("\n");
		} catch (IOException e) {
			// 处理任何IO异常
			e.printStackTrace();
		}

		// 是否为原创
		sb.append("isOriginal: true").append("\n");
		// 永久url地址
		sb.append("permalinkPattern: " + fileXdPaht + "/" + filePName.toLowerCase() + oreder + ".html").append("\n");
		// 追加类别为文件父文件夹名字
		sb.append("category:\n    - ").append(filePName).append("\n");
		// 追加标签为文件父文件夹名字
		sb.append("tag:\n    - ").append(filePName).append("\n");
		// 追加头部，使用 meta 和关键词为标题
		sb.append("head:\n  - - meta\n    - name: keywords\n      content: ")
				.append(matcher.group(2).replaceAll("^\\d+-|\\.md$", "")).append("\n");
		// 追加结尾的 ---
		sb.append("---\n");
		// 增加more
		sb.append("<!-- more -->\n");
		// 返回字符串构建器作为一个字符串
		return sb.toString();
	}
	

}
