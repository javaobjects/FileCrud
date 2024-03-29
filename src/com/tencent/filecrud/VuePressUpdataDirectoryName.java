package com.tencent.filecrud;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class VuePressUpdataDirectoryName {
	
//	final static String FOLDERPATH = "E:\\Google\\vuepress-theme-hope\\docs\\zh\\programBlog";
	final static String FOLDERPATH = "E:\\Google\\vuepress-theme-hope\\docs\\zh\\2023";
//	final static String FOLDERPATH = "E:\\Google\\vuepress-theme-hope\\docs\\zh\\programBlog\\LittleBlogs\\Windows";
	final static int NUMBER = 182;
	
	
	// 随机生成一个位置数组
//    private static String[] locations = {"北京", "上海", "深圳", "广州", "成都", "杭州", "香港", "台北"};
//    private static Random random = new Random();
    
    
	public static void main(String[] args) {
//		renameNumberImages(FOLDERPATH, NUMBER);//测试通过 22:13
//		
		try {
//			renameAndModifyFiles(FOLDERPATH, NUMBER);//测试通过 22:40
			
//			manipulateFiles(FOLDERPATH);//测试成功23:38
			//将所有的 number-xxxx.md 形式的md文件的头部内容实现更新或插入
			replaceHeadContentInMdFile(FOLDERPATH);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		processFiles(FOLDERPATH);//测试成功 03:47
		
//		changeTitle(FOLDERPATH);//测试成功 04:05
		
		
		
//		try {
//			for (int i = 0; i < 6; i++) {
//				deleteLineInFile(FOLDERPATH, 1); // 测试成功 04:38
//			}
//			
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	/**
	 * <p>Title: replaceHeadContentInMdFile</p>
	 * <p>
	 *    Description:
	 *    本方法实现功能，将所有的 number-xxxx.md 形式的md文件的头部内容实现更新或插入
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
				// 创建一个模式来匹配以 '---' 开头且以 '---' 结尾的内容
//				Pattern pattern = Pattern.compile("^---\\r?\\n[\\s\\S]*?\\r?\\n---\\r?\\n", Pattern.DOTALL);
				//与上面的正则等价 兼容liux和windows
//				Pattern pattern = Pattern.compile("^---\\R(.+?)\\R---\\R", Pattern.DOTALL);
				Pattern pattern = Pattern.compile("^---\\R(.+?)\\R<!-- more -->\\R", Pattern.DOTALL);
				// 创建一个匹配器来在内容中找到这个内容
				Matcher matcher = pattern.matcher(content);
				// 创建一个新的内容字符串，使用所需的格式
				String newContent = createNewContent(file);
				
				// 检查匹配器是否找到这个内容
				if (matcher.find()) {
					// 输出匹配器的结果
//					System.out.println("匹配到的字符串：" + matcher.group());

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
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	/**
	 * <p>Title: manipulateFiles</p>
	 * <p>
	 *    Description:
	 *    该方法读取文件夹的所有子文件，并做出如下操作：
			1. 找出所有number-xxxxxx.md的文件;
			2. 读取该文件的创建时间
			3. 读取该文件的title title值为 去掉头部的number-以及尾部的.md
			4. 读取该文件的内容若有以 '# '开头的行并进行如下判断对title进行赋值,
			如果此值也为number-xxxxxxx.md的格式则也要去掉头部的number-以及尾部的.md，若此值不是number-xxxxxxx.md这种格式，则title的值就为'# '后面的值
			6. 创建一个数组里面的值为[北京,上海,深圳,广州,成都,杭州,香港,台北]每次随机取一个下标的值并赋值给location
			7. 将如下格式的文件插入找到的number-xxxxx.md文件的头部
			---
			title: title
			date: XXXX-XXX-XXX XXX:XXXX:XXX
			author: 涎涎
			location: location
			---
			
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
			
	 *    
	 *    
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param folderPath
	 * @throws IOException
	 * @author xianxian
	 * @date 2023年5月3日下午11:36:54
	 * @version 1.0
	 */
	public static void manipulateFiles(String folderPath) throws IOException {
	    // 获取当前文件夹对象
	    File dir = new File(folderPath);
	    if (dir.isDirectory()) {
	        File[] files = dir.listFiles();
	        for (File file : files) {
	            if (file.isDirectory()) {
	                // 递归调用处理子目录
	                manipulateFiles(file.getAbsolutePath());
	            } else {
	                // 获取文件名
	                String fileName = file.getName();
	                // 使用正则表达式匹配文件名
	                Pattern pattern = Pattern.compile("^(\\d+)-(.+\\.md)$");
	                Matcher matcher = pattern.matcher(fileName);
	                if (matcher.matches()) {
	                    // 获取匹配结果中的数字部分
	                    int number = Integer.parseInt(matcher.group(1));
	                    // 读取文件创建时间
	                    BasicFileAttributes attrs = Files.readAttributes(Paths.get(file.getAbsolutePath()), BasicFileAttributes.class);

	                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	                    String formattedDateTime = formatter.format(new Date(attrs.creationTime().toMillis()));
	                    // 读取文件内容中的 title 和 location
	                    BufferedReader reader = new BufferedReader(new FileReader(file));
	                    String line;
	                    String title = matcher.group(2).replaceAll("^\\d+-|\\.md$", "");
//	                    String location = locations[random.nextInt(locations.length)];
	                    
	                 // 匹配以 '---' 开头和结尾的内容
                    	StringBuilder contentBuilder = new StringBuilder();
                    	boolean matched = false;
                    	boolean firstMatch = true;
	                    
	                    
	                    while ((line = reader.readLine()) != null) {
	                    	//在此处写上刚刚的逻辑
//	                    	  if (line.trim().equals("---")) {
//	                    	        if (matched && !firstMatch) {
//	                    	            // 已经匹配过了，不再继续替换
//	                    	            break;
//	                    	        } else {
//	                    	            matched = true;
//	                    	            firstMatch = false;
//	                    	        }
	                    	
//	                    	while ((line = reader.readLine()) != null) {
//	                    	  
//	                    	    } else if (matched) {
//	                    	        contentBuilder.append(line).append("\n");
//	                    	    }
//	                    	}
//	                    	if (matched) {
//	                    	    String oldContent = contentBuilder.toString().trim();
//	                    	    String newContent = "---\n" + "content: " + content + "\n---\n";
//	                    	    String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
//	                    	    String modifiedFileContent = fileContent.replace(oldContent, newContent);
//	                    	    Files.write(Paths.get(file.getAbsolutePath()), modifiedFileContent.getBytes());
//	                    	} else {
//	                    	    String fileContent = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
//	                    	    String newContent = "---\n" + "content: " + content + "\n" + fileContent;
//	                    	    Files.write(Paths.get(file.getAbsolutePath()), newContent.getBytes());
//	                    	}

	                    	
	                    	
	                        if (line.startsWith("# ")) {
	                            // 若有以 '# '开头的行则修改title
	                            String subLine = line.substring(2);
	                            Pattern innerPattern = Pattern.compile("^(\\d+)-(.+\\.md)$");
	                            Matcher innerMatcher = innerPattern.matcher(subLine);
	                            if (innerMatcher.matches()) {
	                                title = innerMatcher.group(2).replaceAll("^\\d+-|\\.md$", "");
	                            } else {
	                                title = subLine.trim();
	                            }
	                        } 
	                        

	                    }
	                    reader.close();
	                    // 插入文件头部
	                    String fileContent = "---\n" +
	                            "title: " + title + "\n" +
	                            "author: 涎涎\n" +
	                            "date: " + formattedDateTime + "\n" +
//	                            "location: " + location + "\n" +
	                            "icon: page\n" +
	                            "order:" + number + "\n" +
	                            "category:\n" +
	                            "- " + file.getParentFile().getName() + "\n" +
	                            "tag:\n" +
	                            "- " + file.getParentFile().getName() + "\n" +
	                            "head:\n" +
	                            "    - - meta\n" +
	                            "      - name: keywords\n" +
	                            "        content: " + title + "\n" +
	                            "---\n";
	                    String newContent = fileContent + new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
	                    Files.write(Paths.get(file.getAbsolutePath()), newContent.getBytes());

	                    System.out.println("文件 " + fileName + " 修改成功！");
	                }
	            }
	        }
	    }
	}

	
	/**
	 * <p>Title: deleteLineInFile</p>
	 * <p>
	 *    Description:
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param directoryPath
	 * @param lineNumber
	 * @throws IOException
	 * @author xianxian
	 * @date 2023年5月4日上午4:38:41
	 * @version 1.0
	 */
	public static void deleteLineInFile(String directoryPath, int lineNumber) throws IOException {
	    // 1. 获取目录下所有子文件
	    File directory = new File(directoryPath);
	    File[] subFiles = directory.listFiles();

	    // 2. 对所有number-xxxxxx.md的文件进行操作
	    for (File file : subFiles) {
	        if (file.isFile() && file.getName().matches("^(\\d+)-(.+\\.md)$")) {
	            Path path = Paths.get(file.getAbsolutePath());
	            // 读取文件所有行
	            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
	            // 过滤掉指定行
	            List<String> filteredLines = IntStream.range(0, lines.size())
	                    .filter(i -> i != lineNumber - 1)
	                    .mapToObj(lines::get)
	                    .collect(Collectors.toList());
	            // 将修改后的行重新写回文件
	            Files.write(path, String.join(System.lineSeparator(), filteredLines).getBytes(StandardCharsets.UTF_8));
	            System.out.println("Deleted line " + lineNumber + " in file: " + path);
	        } else if (file.isDirectory()) {
	            deleteLineInFile(file.getAbsolutePath(), lineNumber); // 递归查找子文件夹下的文件
	        }
	    }
	}

	
	
	
	
	
	
	
	/**
	 * <p>Title: changeTitle</p>
	 * <p>
	 *    Description:
	 *    消除指定文本的空格
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param directoryPath
	 * @author xianxian
	 * @date 2023年5月4日上午4:22:37
	 * @version 1.0
	 */
	@SuppressWarnings("static-access")
	private static void changeTitle(String directoryPath) {
	       // 1. 获取目录下所有子文件
        File directory = new File(directoryPath);
        File[] subFiles = directory.listFiles();
        
        // 2. 对所有number-xxxxxx.md的文件进行操作
        for (File file : subFiles) {
            if (file.isFile() && file.getName().matches("^(\\d+)-(.+\\.md)$")) {
//            	System.out.println("处理文件：" + file.getAbsolutePath());
//                processNumberFile(file);
                
            	try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            		String line = null;
            		 int lineNum = 1;
                     // 读取文件内容，找到'# '开头的行，并进行相应的操作
                     while ((line = reader.readLine()) != null) {
                    	 if (lineNum > 1 && lineNum < 6) {
                    		 System.out.println(line);
                    		 String changeLine = null;
                    		 switch (lineNum) {
							case 2:
								changeLine = line.replaceAll("title:\\s+", "title:");
								break;
							case 3:
								changeLine = line.replaceAll("date:\\s+", "date:");
							break;
							case 4:
								changeLine = line.replaceAll("author:\\s+", "author:");
								break;
							case 5:
								changeLine = line.replaceAll("location:\\s+", "location:");
								break;
							default:
								break;
							}
                    		
                    		 System.out.println("changeLine: " + changeLine);
                    		  try {
  								new AppointedLineNumberCRUD().setOrAddAppointedLineNumberDataByFiles(lineNum, changeLine, "set", file.getAbsolutePath());
  							} catch (Exception e) {
  								e.printStackTrace();
  							}
                    	 }
                    	 
                    	 
                    	 
                    	 lineNum++;
                     }
            		
            		
            	} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
                
                
                
                
            } else if (file.isDirectory()) {
            	changeTitle(file.getAbsolutePath()); // 递归查找子文件夹下的文件
            }
        }

	}
	
	
	
	
	/**
     * 读取目录下的所有子文件，并对number-xxxxxx.md的文件进行特定的操作
     	1. 找出所有number-xxxxxx.md的文件;
		2. 读取该文件的内容若有以 '# '开头的行并进行如下判断,
		若'# '后面的值为number-xxxxxx.md则对它进行判断 看'# '后面值的number值是否与该文件的名字中的number等值，
		若不等值则将该行的内容的number的值更改为该文件的number的值，否则不进行任何操作
		3. 若该文件没有以'# '开头的行则在该文件第8行插入如下数据
		# 去掉该文件头的number-以及去掉尾部的.md的值
     * 
     * @param directoryPath 目录路径
     */
    public static void processFiles(String directoryPath) {
        // 1. 获取目录下所有子文件
        File directory = new File(directoryPath);
        File[] subFiles = directory.listFiles();
        
        // 2. 对所有number-xxxxxx.md的文件进行操作
        for (File file : subFiles) {
            if (file.isFile() && file.getName().matches("^(\\d+)-(.+\\.md)$")) {
//            	System.out.println("处理文件：" + file.getAbsolutePath());
                processNumberFile(file);
            } else if (file.isDirectory()) {
                processFiles(file.getAbsolutePath()); // 递归查找子文件夹下的文件
            }
        }
    }

 
    /**
     * <p>Title: processNumberFile</p>
     * <p>
     *    Description:
     *    处理单个number-xxxxxx.md文件
     * </p>
     * <p>Copyright: Copyright (c) 2017</p>
     * <p>Company: www.baidudu.com</p>
     * @param file 待处理的文件
     * @author xianxian
     * @date 2023年5月4日上午3:49:05
     * @version 1.0
     */
    @SuppressWarnings("static-access")
	public static void processNumberFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;
            boolean isLineExist = false;
            Path path = Paths.get(file.getAbsolutePath());
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);

            String fileName = file.getName();
            System.out.println(fileName);
            Pattern pattern = Pattern.compile("^(\\d+)-(.+\\.md)$");
            Matcher matcher = pattern.matcher(fileName);
            
            String number = fileName.split("-")[0];;// 从文件名中提取number
        
//            System.out.println("number: " + number);
//            System.out.println("正在处理文件：" + fileName);

            int lineNum = 1;
            // 读取文件内容，找到'# '开头的行，并进行相应的操作
            while ((line = reader.readLine()) != null) {
            	
                if (line.startsWith("# ")) {
                    isLineExist = true;//表示 # 存在
                    /*
                     * 1. 取到 '# '后面的值 并命名为title
                     * 2. 判断 title的值是否是以 数字开头 '-' 任意若干个字符以'.md' 结尾 示例212121-abdcd2434dbd.md
                     * 3. 若符合第2条格式则取title的number的值并与 从文件名中提取number 值相比较，若大小不等则将title里的值的number替换成 从文件名中提取number
                     */
                 // 1. 取到 '# '后面的值 并命名为title
                    String title = line.substring(2);

                    // 2. 判断 title的值是否是以 数字开头 '-' 任意若干个字符以'.md' 结尾 示例212121-abdcd2434dbd.md
                    if (title.matches("^\\d+-.*\\.md$")) {
                        // 从title中提取number的值
                        String titleNumber = title.substring(0, title.indexOf("-"));
                        System.out.println("titleNumber: " + titleNumber);
                        // 从文件名中提取number值
                        if (!titleNumber.equals(number)) {
                            // 若大小不等则将title里的值的number替换成 从文件名中提取number
                            String newTitleData = "# " + number + "-" + title.substring(title.indexOf("-") + 1);
                            
                            System.out.println("lines.contains(line): " + lines.contains(line));
                            System.out.println("第 " + lineNum + "匹配上了");
                            
                            try {
								new AppointedLineNumberCRUD().setOrAddAppointedLineNumberDataByFiles(lineNum, newTitleData, "set", file.getAbsolutePath());
							} catch (Exception e) {
								e.printStackTrace();
							}
                            
                            System.out.println("修改行：" + line + " -> " + newTitleData);
                        }
                    }
                }
                lineNum++;
            }
         // 如果文件中没有以'# '开头的行，则在第8行插入相应的行
            if (!isLineExist) {
            	//这里要增加判断若该行有数据要在不影响原有数据的情况下插入
            	 if(matcher.find()) {
            		 System.out.println("# " + matcher.group(2).replaceAll("^\\d+-|\\.md$", ""));
            		 lines.add(0, "# " + matcher.group(2).replaceAll("^\\d+-|\\.md$", "")); // 在第8行插入新行
            		 Files.write(path, lines, StandardCharsets.UTF_8);
                 }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	
	
	
	

	
	
	

	/**
	 * <p>Title: renameNumberImages</p>
	 * <p>
	 *    Description:
	 * 递归遍历指定文件夹及其子文件夹下所有文件夹，将所有名为 "number-Images" 的文件夹名更改为 "(number + aNum)-Images"
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param folderPath 待遍历的文件夹路径
	 * @param aNum 用于更改文件夹名的常数
	 * @author xianxian
	 * @date 2023年5月3日下午10:08:54
	 * @version 1.0
	 */
	public static void renameNumberImages(String folderPath, int aNum) {
	    // 获取当前文件夹对象
	    File folder = new File(folderPath);

	    // 遍历当前文件夹中所有文件和文件夹
	    for (File file : folder.listFiles()) {

	        // 如果是文件夹，进行递归遍历
	        if (file.isDirectory()) {
	            renameNumberImages(file.getAbsolutePath(), aNum);
	        }

	        // 如果是 "number-Images" 文件夹，则更改文件夹名
	        if (file.isDirectory() && file.getName().matches("^\\d+-Images$")) {
	            int number = Integer.parseInt(file.getName().split("-")[0]);
	            String newFolderName = (number + aNum) + "-Images";
	            File newFolder = new File(folder.getAbsolutePath() + File.separator + newFolderName);
	            boolean isRenamed = file.renameTo(newFolder);
	            if (isRenamed) {
	                System.out.println(file.getName() + " -> " + newFolder.getName());
	            } else {
	                System.err.println("Failed to rename folder: " + file.getName());
	            }
	        }
	    }
	}
	
	/**
	 * <p>Title: renameAndModifyFiles</p>
	 * <p>
	 *    Description:
	 *    该方法读取文件夹的所有子文件，并做出如下更改:
	 *		1. 将所有的number-xxxxx.md文件名字更改为 (number + aNum)-xxxxx.md
	 *		2. 将其名字更改完成后再读取该文件的内容，并将其内容为 ![](./assets/number-Images/number.png)的行的数据更改为 
	 *      ![](./assets/(number + aNum)-Images/number.png)
	 * </p>
	 * <p>Copyright: Copyright (c) 2017</p>
	 * <p>Company: www.baidudu.com</p>
	 * @param folderPath
	 * @param aNum
	 * @throws IOException
	 * @author xianxian
	 * @date 2023年5月3日下午10:32:24
	 * @version 1.0
	 */
	public static void renameAndModifyFiles(String folderPath, int aNum) throws IOException {
		// 获取当前文件夹对象
		File dir = new File(folderPath);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					// 递归调用处理子目录
					renameAndModifyFiles(file.getAbsolutePath(), aNum);
				} else {
					// 获取文件名
					String fileName = file.getName();
					// 使用正则表达式匹配文件名
					Pattern pattern = Pattern.compile("^(\\d+)-(.+\\.md)$");
					Matcher matcher = pattern.matcher(fileName);
					if (matcher.matches()) {
						// 获取匹配结果中的数字部分
						int number = Integer.parseInt(matcher.group(1));
						// 修改文件名
						String newFileName = (number + aNum) + "-" + matcher.group(2);
						File newFile = new File(file.getParentFile(), newFileName);
						boolean isRenamed = file.renameTo(newFile);
						if (isRenamed) {
							System.out.println("File " + fileName + " has been renamed to " + newFileName);
						} else {
							System.out.println("Failed to rename file " + fileName);
						}
						// 读取文件内容
						BufferedReader reader = new BufferedReader(new FileReader(newFile));
						String line;
						StringBuilder builder = new StringBuilder();
						while ((line = reader.readLine()) != null) {
							// 替换文件内容中的图片路径
							builder.append(line.replaceAll("(?<=\\!\\[\\]\\(\\.\\/assets\\/)" + number + "-Images",
									(number + aNum) + "-Images"));
							//构建一个字符串时，插入系统的换行符
							builder.append(System.lineSeparator());
						}
						reader.close();
						// 写入修改后的文件内容
						BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
						writer.write(builder.toString());
						writer.close();
						System.out.println("Content of file " + newFileName + " has been modified.");
					}
				}
			}
		}
	}
	
	
	
	
	
	

}
