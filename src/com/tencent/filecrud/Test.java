package com.tencent.filecrud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) {

		method2();
		
	}
	
	private static void method2() {
		try {//try代码块，当发生异常时会转到catch代码块中
        	//读取指定的文件
//        	Scanner s = new Scanner(System.in);//创建scanner，控制台会一直等待输入，直到敲回车结束
//        	System.out.println("请输入想要打开的文本文档：");//输入提示信息
//        	String a = s.nextLine();//定义字符串变量，并赋值为用户输入的信息
//			String pathStr = System.getProperty("user.dir") + File.separator + "log.log";
			String pathStr =  "E:\\Tencent\\FileCrud\\src\\com\\tencent\\filecrud\\Readme.md";
        	
        	
        	
        	//创建类进行文件的读取，并指定编码格式为utf-8
        	InputStreamReader read = new InputStreamReader(new FileInputStream(pathStr),"utf-8"); 
            BufferedReader in = new BufferedReader(read);//可用于读取指定文件     
            String str=null;//定义一个字符串类型变量str
            String b=null;//定义一个字符串类型变量b
            int i = 0;//定义一个整型变量,用于统计行数
            int c = 0,c1 = 0,c2 = 0,c3 = 0,c4 = 0,c5 = 0,c6 = 0;//定义整型变量，用于统计字符数
            int d = 0;//定义一个整型变量，用于统计字节数
            while ((str = in.readLine())!= null) {//readLine()方法, 用于读取一行,只要读取内容不为空就一直执行
            	i++;//每循环一次就进行一次自增，用于统计文本行数
            	c += str.length();//用于统计总字符数
            	byte[] bytes=str.getBytes();//求出该行的字节数组
            	d += bytes.length;//用于统计总字节数
            	for (int j = 0; j < str.length(); j++) {//for循环的条件，当j小于该行长度时就一直循环并自增
            		b = Character.toString(str.charAt(j));//返回一个字符串对象
            		if (b.matches("[\\u4e00-\\u9fa5]")) {//if语句的条件，判断是否为汉字
                        c1++;//若为汉字则c1自增
                    } else if(b.matches("[A-Z]")){//if语句的条件，判断是否为大写字母
            			c2++;//若为大写字母则c2自增
            		} else if(b.matches("[a-z]")){//if语句的条件，判断是否为小写字母
            			c3++;//若为小写字母则c3自增
            		} else if(b.matches("[0-9]")){//if语句的条件，判断是否为数字
            			c4++;//若为数字则c4自增
            		} else {//否则可判断为其他字符
            			c5++;//若为其他字符则c5自增
            		}
            	}
            }
            c6 = c2 + c3;//统计总的字母数
            in.close();//关闭流
            System.out.println("该文本共有"+i+"行");//输出总的行数
            System.out.println("该文本共有"+c+"个字符");//输出总的字符数
            System.out.println("其中包含：");//输出提示信息
            System.out.println(c1+"个汉字");//输出汉字数
            System.out.println(c6+"个字母，其中"+c2+"个大写字母，"+c3+"个小写字母");//输出字母数
            System.out.println(c4+"个数字");//输出数字数
            System.out.println(c5+"个其他字符");//输出其它字符数
            System.out.println("该文本共有"+d+"个字节");//输出总的字节数
        } catch (IOException e) {//当try代码块有异常时转到catch代码块
        	e.printStackTrace();//printStackTrace()方法是打印异常信息在程序中出错的位置及原因
        }

	}
	
	private static void method1() {
		try {//try代码块，当发生异常时会转到catch代码块中
        	//读取指定的文件
//        	Scanner s = new Scanner(System.in);//创建scanner，控制台会一直等待输入，直到敲回车结束
//        	System.out.println("请输入想要打开的文本文档：");//输入提示信息
//        	String a = s.nextLine();//定义字符串变量，并赋值为用户输入的信息
        	
        	String pathStr = System.getProperty("user.dir") + File.separator + "log.log";
        	
        	//创建类进行文件的读取，并指定编码格式为utf-8
        	InputStreamReader read = new InputStreamReader(new FileInputStream(pathStr),"utf-8"); 
            BufferedReader in = new BufferedReader(read);//可用于读取指定文件     
            String str=null;//定义一个字符串类型变量str
            int i = 0;//定义一个整型变量，用于统计行数
            int c = 0;//定义一个整型变量，用于统计字符数
            int d = 0;//定义一个整型变量，用于统计字节数
            while ((str = in.readLine())!= null) {//readLine()方法, 用于读取一行,只要读取内容不为空就一直执行
            	i++;//每循环一次就进行一次自增，用于统计文本行数
            	c += str.length();//用于统计总字符数
            	byte[] bytes=str.getBytes();//求出该行的字节数组
            	d += bytes.length;//用于统计总字节数
            }
            in.close();//关闭流
            System.out.println("该文本共有"+i+"行");//输出总的行数
            System.out.println("该文本共有"+c+"个字符");//输出总的字符数
            System.out.println("该文本共有"+d+"个字节");//输出总的字节数
        } catch (IOException e) {//当try代码块有异常时转到catch代码块
        	e.printStackTrace();//printStackTrace()方法是打印异常信息在程序中出错的位置及原因
        }
	}

}
