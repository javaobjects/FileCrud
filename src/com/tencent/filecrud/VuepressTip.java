package com.tencent.filecrud;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VuepressTip {

//	final static String FOLDERPATH = "E:\\Google\\vuepress-theme-hope\\docs\\zh";
//	final static String FOLDERPATH = "E:\\Google\\vuepress-theme-hope\\docs\\zh\\programBlog";
//	final static String FOLDERPATH = "E:\\Google\\vuepress-theme-hope\\docs\\zh\\2023";
//	final static String FOLDERPATH = "E:\\Google\\vuepress-theme-hope\\docs\\zh\\programBlog\\ServerSideLanguage";
	final static String FOLDERPATH = "E:\\Google\\vuepress-theme-hope\\test";
	
	public static void main(String[] args) {

		
		
		try {
			findMdFile(FOLDERPATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * <p>Title: findMdFile</p>
	 * <p>
	 *    Description:
	 *    找出路径下的所有文件
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param folderPath
	 * @throws IOException
	 * @author xianxian
	 * @date 2023年6月2日上午10:56:35
	 * @version 1.0
	 */
	private static void findMdFile(String folderPath) throws IOException {
	    // 获取当前文件夹对象
	    File dir = new File(folderPath);
	    if (dir.isDirectory()) {
	        File[] files = dir.listFiles();
	        for (File file : files) {
	            if (file.isDirectory()) {
	                // 递归调用处理子目录
	            	findMdFile(file.getAbsolutePath());
	            } else {
	            	updateTip(file);
	            }
	        }
	    }
	}
	
	
	
	/**
	 * <p>Title: updateTip</p>
	 * <p>
	 *    Description:为博客增加tip
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param file
	 * @throws IOException
	 * @author xianxian
	 * @date 2023年6月2日下午3:08:51
	 * @version 1.0
	 */
	private static void updateTip(File file) throws IOException {
		// 检查文件是否是以 '.md'结尾的且名字不可以为 'README.md'
		if (file.getName().endsWith(".md") && !file.getName().equals("README.md") && !file.getName().equals("intro.md")) {
			// 读取文件内容为一个字符串
			String content = new String(Files.readAllBytes(file.toPath()));
			
			//匹配 '# '的正则
			Pattern patternH1 = Pattern.compile("\\*\\*以上就是我关于 .*", Pattern.MULTILINE);
			//匹配 '::: warning' 的正则
			Pattern patternWarning = Pattern.compile("(?m)^::: tip.*:::$", Pattern.DOTALL);
			
			// 创建一个匹配器来在内容中找到这个内容
			Matcher matcherH1 = patternH1.matcher(content);
			Matcher matcherWarning = patternWarning.matcher(content);
			
			
			//先匹配告警
			if(matcherWarning.find()) {
				//匹配上了则更新
				
//				String warningContent = matcherWarning.group(); // 匹配到的告警内容
//				content = content.replaceFirst(Pattern.quote(warningContent), WARNINGUPDATA); // 替换匹配的内容
				
				// 重置 Matcher，以便进行新的匹配操作
				matcherWarning.reset();
//				System.out.println(file.getName() + "  tip更新了");
			}else {
				//没有匹配上则进行匹配 h1
//				if(matcherH1.find()) {
//					// 匹配上了则在此行的下一行插入对应的内容，但不得覆盖原有的内容
//					String h1Content = matcherH1.group(); // 匹配到的 h1 标题内容
//					
//					String replacement = "::: tip\r\n"
//							+ h1Content + "\r\n"
//							+ ":::";
//
//					StringBuffer stringBuffer = new StringBuffer();
//				    matcherH1.appendReplacement(stringBuffer, Matcher.quoteReplacement("" + replacement));
//				    matcherH1.appendTail(stringBuffer);
//				    content = stringBuffer.toString();
//					
//					
//					// 重置 Matcher，以便进行新的匹配操作
//					matcherH1.reset();
//					System.out.println(file.getName() + "  告警插入了");
//				}else {
//					System.out.println(file.getName() + "  没有h1标题");
//				}
				
				
//				System.out.println(file.getName() + "  没有tip");

				String titleName = file.getName().substring(file.getName().split("-")[0].length())
						.substring(1, file.getName().substring(file.getName().split("-")[0].length()).length() - 3);
				
				String replacement = 
						"\r\n"
						+ "::: tip\r\n"
						+ "**以上就是我关于 *" + titleName +"*  知识点的整理与总结的全部内容，希望对你有帮助。。。。。。。**\r\n"
						+ ":::";
				content += replacement;
				
				System.out.println(file.getName() + " 添加了");
			}

			
			
	
	
			
			// 将修改后的内容写回文件
			Files.write(file.toPath(), content.getBytes());
		}
	}
}
