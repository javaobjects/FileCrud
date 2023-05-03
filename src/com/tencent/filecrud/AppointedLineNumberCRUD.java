package com.tencent.filecrud;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>Title: AppointedLineNumberCRUD</p>  
 * <p>
 *    Description: 想要实现 对 文本 的 
 *    增 指定行插入行数据 
 *    删 
 *    改 指定行更改行数据 
 *    查 统计文本字数
 * </p>
 * </p>  
 * @author xianxian 
 * @date 2023年1月27日
 */
public class AppointedLineNumberCRUD {

	public static void main(String[] args) {
		/**
		 * 1. 完成聊天记录截图
		 * 
		 * 
		 * 2. mylucky
		 * 
		 * 
		 * 
		 */
//		String content = "1. mylucky";//平时日志记录

		String content = getFullTime()+"\r\n"
				+ "睡眠\r\n"
				+ "1. 入睡\r\n"
				+ "2. 起床\r\n"
				+ "3. 深睡\r\n"
				+ "能量\r\n"
				+ "1. 早餐\r\n"
				+ "2. 午餐\r\n"
				+ "3. 晚餐\r\n"
				+ "4. 其它\r\n"
				+ "工作";//早上第一次插入使用
		appendBybufferedWriter(content, "D:\\alibaba\\studyLog\\study.log",false);//早上第一次插入使用
		
//		appendBybufferedWriter(content, "D:\\alibaba\\studyLog\\study.log",true);
		System.out.println(readAppointedLineNumberByFileReader(-1,"D:\\alibaba\\studyLog\\study.log"));
//		System.out.println(readAppointedLineNumberByFileReader(-1,null));

//		countNumOfWords("E:\\简书\\程序语言\\Git\\14-Git-如何将本地项目上传到Gitee或Github？.md");
		
		
//		try {
//			setOrAddAppointedLineNumberDataByFiles(2, "haha", "set", null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	


	
	
	/**
	 * <p>Title: countNumOfWords</p>  
	 * <p>
	 *    Description: 统计文本的字数 
	 *    可以细分到 汉字 多少 英文（大小写）多少 数据多少 其它字符多少 全文共多少字节
	 * </p>  
	 * <p>Copyright: Copyright (c) 2017</p>  
	 * <p>Company: www.baidudu.com</p>  
	 * @param pathStr  
	 * @author xianxian
	 * @date 2023年1月27日  
	 * @version 1.0
	 */
	private static void countNumOfWords(String pathStr) {
		try {// try代码块，当发生异常时会转到catch代码块中
				// 读取指定的文件
			if (pathStr == null || pathStr.equals("")) {
				pathStr = System.getProperty("user.dir") + File.separator + "log.log";
			}
			// 创建类进行文件的读取，并指定编码格式为utf-8
			InputStreamReader read = new InputStreamReader(new FileInputStream(pathStr), "utf-8");
			BufferedReader in = new BufferedReader(read);// 可用于读取指定文件

			String linDatastr = null;// 定义一个字符串类型变量linDatastr 用于存放一行的文本数据
			String wordStr = null;// 定义一个字符串类型变量wordStr
			int lineCount = 0, // 定义一个整型变量,用于统计行数
					countWords = 0, // 定义整型变量，用于统计字符数
					chineseWords = 0, upperWords = 0, lowerWords = 0, numberWords = 0, otherWords = 0,
					allEnglishWords = 0, countBytes = 0;// 定义一个整型变量，用于统计字节数
			while ((linDatastr = in.readLine()) != null) {// readLine()方法, 用于读取一行,只要读取内容不为空就一直执行
				lineCount++;// 每循环一次就进行一次自增，用于统计文本行数
				countWords += linDatastr.length();// 用于统计总字符数
				byte[] bytes = linDatastr.getBytes();// 求出该行的字节数组
				countBytes += bytes.length;// 用于统计总字节数
				for (int j = 0; j < linDatastr.length(); j++) {// for循环的条件，当j小于该行长度时就一直循环并自增
					wordStr = Character.toString(linDatastr.charAt(j));// 返回一个字符串对象
					if (wordStr.matches("[\\u4e00-\\u9fa5]")) {// if语句的条件，判断是否为汉字
						chineseWords++;// 若为汉字则c1自增
					} else if (wordStr.matches("[A-Z]")) {// if语句的条件，判断是否为大写字母
						upperWords++;// 若为大写字母则c2自增
					} else if (wordStr.matches("[a-z]")) {// if语句的条件，判断是否为小写字母
						lowerWords++;// 若为小写字母则c3自增
					} else if (wordStr.matches("[0-9]")) {// if语句的条件，判断是否为数字
						numberWords++;// 若为数字则c4自增
					} else {// 否则可判断为其他字符
						otherWords++;// 若为其他字符则c5自增
					}
				}
			}
			allEnglishWords = upperWords + lowerWords;// 统计总的字母数
			in.close();// 关闭流
			System.out.println("该文本共有" + lineCount + "行");// 输出总的行数
			System.out.println("该文本共有" + countWords + "个字符");// 输出总的字符数
			System.out.println("其中包含：");// 输出提示信息
			System.out.println(chineseWords + "个汉字");// 输出汉字数
			System.out.println(allEnglishWords + "个字母，其中" + upperWords + "个大写字母，" + lowerWords + "个小写字母");// 输出字母数
			System.out.println(numberWords + "个数字");// 输出数字数
			System.out.println(otherWords + "个其他字符");// 输出其它字符数
			System.out.println("该文本共有" + countBytes + "个字节");// 输出总的字节数

		} catch (IOException e) {// 当try代码块有异常时转到catch代码块
			e.printStackTrace();// printStackTrace()方法是打印异常信息在程序中出错的位置及原因
		}
	}

	/**
	 * <p>Title: getFullTime</p>  
	 * <p>
	 *    Description: 获取当前时间
	 * </p>  
	 * <p>Copyright: Copyright (c) 2017</p>  
	 * <p>Company: www.baidudu.com</p>  
	 * @return  
	 * @author xianxian
	 * @date 2023年1月27日  
	 * @version 1.0
	 */
	private static String getFullTime() {
		// 日期当前时间
		Date time = new Date();
		// 格式化日期
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// 转换格式
		return sdfTime.format(time);
	}

	/**
	 * <p>Title: appendBybufferedWriter</p>  
	 * <p>
	 *    Description: 
	 * </p>  
	 * <p>Copyright: Copyright (c) 2017</p>  
	 * <p>Company: www.baidudu.com</p>  
	 * @param msg
	 * @param pathStr
	 * @param onOffTime  
	 * @author xianxian
	 * @date 2023年1月27日  
	 * @version 1.0
	 */
	private static void appendBybufferedWriter(String msg, String pathStr, Boolean onOffTime) {
		if (pathStr == null || pathStr.equals("")) {
			pathStr = System.getProperty("user.dir") + File.separator + "log.log";
		}
		File file = new File(pathStr);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			// 参数true 表示是否在原文件内容后追加 若不写或写false 则覆盖原文
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(pathStr, true));
			// 内容输出到指定文件中
			if (onOffTime == null || onOffTime == false) {
				bufferedWriter.append(msg + " ");
			} else {
				bufferedWriter.append(msg + " " + getFullTime());
			}
			bufferedWriter.newLine();
			bufferedWriter.flush();
			bufferedWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * <p>Title: setOrAddAppointedLineNumberDataByFiles</p>  
	 * <p>
	 *    Description: 更新或替换指定行数据 或 在指定行数据后面添加数据 从第一行开始
	 * </p>  
	 * <p>Copyright: Copyright (c) 2017</p>  
	 * <p>Company: www.baidudu.com</p>  
	 * @param lineNumber
	 * @param data
	 * @param setOrAdd   传值set 则为 更改指定行数据 传add 则在指定行后面添加数据
	 * @param pathStr
	 * @throws Exception  
	 * @author xianxian
	 * @date 2023年1月27日  
	 * @version 1.0
	 */
	public static void setOrAddAppointedLineNumberDataByFiles(int lineNumber, String data, String setOrAdd,
			String pathStr) throws Exception {
		if (pathStr == null || pathStr.equals("")) {
			pathStr = System.getProperty("user.dir") + File.separator + "log.log";
		}
		Path path = Paths.get(pathStr);
		List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
		
		
		if (Math.abs(lineNumber) > lines.size()) {
			throw new Exception("指定行【" + lineNumber + "】不在文件行数范围内");
		}
		if (lines.size() < Math.abs(lineNumber)) {
			throw new Exception("指定列 lineNumber： " + lineNumber + "大于文件本身的行数： " + lines.size());
		} else {
			if (setOrAdd == null || setOrAdd.equals("")) {
				throw new Exception("请传入正确的 setOrAdd 值");
			} else {
				if (setOrAdd.equals("set") || setOrAdd.equals("add")) {
					if (setOrAdd.equals("set")) {
						lines.set(lineNumber < 0 ? lines.size() + lineNumber : lineNumber - 1,data);
					} else {
						lines.add(lineNumber < 0 ? lines.size() + lineNumber + 1 : lineNumber,data);
					}
					Files.write(path, lines, StandardCharsets.UTF_8);
				} else {
					throw new Exception("请传入正确的 setOrAdd 值");
				}
			}
		}
	}

	/**
	 * <p>Title: readAppointedLineNumberByFileReader</p>  
	 * <p>
	 *    Description: 1.根据指定行读数据 -N 为 倒数第N行
	 * </p>  
	 * <p>Copyright: Copyright (c) 2017</p>  
	 * <p>Company: www.baidudu.com</p>  
	 * @param lineNumber
	 * @param pathStr
	 * @return  
	 * @author xianxian
	 * @date 2023年1月27日  
	 * @version 1.0
	 */
	public static String readAppointedLineNumberByFileReader(int lineNumber, String pathStr) {
		String appointedLine = "";
		FileReader in = null;
		LineNumberReader reader = null;

		if (pathStr == null || pathStr.equals("")) {
			pathStr = System.getProperty("user.dir") + File.separator + "log.log";
		}
		try {
			in = new FileReader(pathStr);
			reader = new LineNumberReader(in);
			long totalLine = Files.lines(Paths.get(pathStr)).count();
			if (Math.abs(lineNumber) > totalLine) {
				throw new Exception("指定行【" + lineNumber + "】不在文件行数范围内");
			}

			int line = 1;
			reader.setLineNumber((int) (lineNumber < 0 ? totalLine + lineNumber + 1 : lineNumber));
			long i = reader.getLineNumber();
			String s = "";

			while ((s = reader.readLine()) != null) {
				if (i == line) {
					appointedLine = s;
					break;
				}
				line++;
			}
			return appointedLine;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeResource(in, reader);
		}
		return appointedLine;
	}

	/**
	 * <p>Title: closeResource</p>  
	 * <p>
	 *    Description: 2.关闭资源
	 * </p>  
	 * <p>Copyright: Copyright (c) 2017</p>  
	 * <p>Company: www.baidudu.com</p>  
	 * @param in
	 * @param reader  
	 * @author xianxian
	 * @date 2023年1月27日  
	 * @version 1.0
	 */
	public static void closeResource(FileReader in, LineNumberReader reader) {
		try {
			if (reader != null) {
				reader.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
