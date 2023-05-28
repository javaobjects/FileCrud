package com.tencent.filecrud;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImgForVite {

	public static void main(String[] args) {
		//将num.png文件重新命名为 name_number_num.png或num.jpg文件重新命名为 name_number_num.jpg
//	    File dir = new File("E:\\Google\\vuepress-theme-hope\\docs\\zh\\programBlog");
//        renameFiles(dir);
        
        //将指定路径下所有的图片移动到对应的路径下
//        File srcDir = new File("E:\\Google\\vuepress-theme-hope\\docs\\zh\\programBlog");
//        File destDir = new File("E:\\Google\\vuepress-theme-hope\\docs\\.vuepress\\public\\assets");
//        moveImages(srcDir, destDir);
        
        //将内容中含有 num.png 或是 num.jpg 则将其 替换成 name_number_num.png
        String path = "E:\\Google\\vuepress-theme-hope\\docs\\zh\\programBlog";
        replaceImageName(path);

	}
	/**
	 为实现一个功能：
	 1. 查询对应路径下的文件夹看看是否有以num.png 或 num.jpg文件，如果没有则继续查找
	 2. 将num.png文件重新命名为 name_number_num.png或num.jpg文件重新命名为 name_number_num.jpg，如果图片不为num.png或num.jpg格式则不用管它
	 3. name的取值为文件的父目录的父目录的父目录的名
	 4. number的取值为文件的父目录的名字以‘-’分割的下标0的值
	 */
	
    private static void renameFiles(File dir) {
        Pattern pattern = Pattern.compile("(\\d+\\.(png|jpg))");
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    renameFiles(file);
                } else {
                    Matcher matcher = pattern.matcher(file.getName());
                    if (matcher.matches()) {
                        String[] splitPath = dir.getAbsolutePath().split("\\\\");
                        String newName = splitPath[splitPath.length - 3] + "_" +
                                splitPath[splitPath.length - 1].split("-")[0] + "_" +
                                matcher.group(1);
                        File newFile = new File(file.getParentFile(), newName);
                        if (file.renameTo(newFile)) {
                            System.out.println("Renamed file: " + file.getName() + " to " + newName);
                        } else {
                            System.out.println("Failed to rename file: " + file.getName());
                        }
                    }
                }
            }
        }
    }

    
    /**
     写一个方法实现如下功能：
     1. 将指定路径下所有的图片移动到对应的路径下 例将 E:\Google\vuepress-theme-hope\docs\zh\programBlog 路径下的所有图片 移动到 E:\Google\vuepress-theme-hope\docs\.vuepress\public\assets
     2. 要查找出 指定路包含有图片的文件夹，如果没有则 递归 调用此方法直到找到有图片的文件夹 并将其里面的所有图片移动到对应的路径下
     */
    
    private static void moveImages(File srcDir, File destDir) {
        Pattern pattern = Pattern.compile("(\\d+\\.(png|jpg))");
        File[] files = srcDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    moveImages(file, destDir);
                } else {
                    Matcher matcher = pattern.matcher(file.getName());
                    if (matcher.find()) {
//                        String[] splitPath = srcDir.getAbsolutePath().split(File.separator);
                        String[] splitPath = srcDir.getAbsolutePath().split("\\\\");
                        String newName = splitPath[splitPath.length - 3] + "_" +
                                splitPath[splitPath.length - 1].split("-")[0] + "_" +
                                matcher.group(1);
                        File destFile = new File(destDir, newName);
                        if (destFile.exists()) {
                            System.out.println("Skipping file " + file.getName() + ", a file with the same name already exists in the destination directory.");
                        } else {
                            try {
                                Files.move(file.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                                System.out.println("Successfully moved file: " + file.getName());
                            } catch (IOException e) {
                                System.out.println("Failed to move file: " + file.getName() + ", error message: " + e.getMessage());
                            }
                        }
                    }
                }
            }
        }
    }
    
    /**
     写一个方法，实现如下功能：
     1. 读取指定路径下的所有的.md文件里的内容
     2. 若内容中含有 num.png 或是 num.jpg 则将其 替换成 name_number_num.png
     3. 其中 name 的值为当前的.md文件的父目录的文件 number的值则为.md文件的名字以'-'分割后取下标0的值
     4. 指定路径下若没有.md文件，则 递归 调用自己 一直找到有.md文件为止
     */
    private static void replaceImageName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    replaceImageName(f.getAbsolutePath());
                } else if (f.isFile() && f.getName().endsWith(".md")) {
                    try {
                        String content = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
                        Matcher matcher = Pattern.compile("(\\d+\\.(png|jpg))").matcher(content);
                        String[] splitPath = f.getParent().split("\\\\");
                        String name = splitPath[splitPath.length - 1];
                        String number = f.getName().split("-")[0];
                        int i = 1;
                        StringBuffer sb = new StringBuffer();
                        while (matcher.find()) {
                            String newName = name + "_" + number + "_" + i++ + "." + matcher.group(2);
                            matcher.appendReplacement(sb, Matcher.quoteReplacement(newName));
                        }
                        matcher.appendTail(sb);
                        Files.write(f.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8));
                        System.out.println("Successfully replaced images in file: " + f.getName());
                    } catch (IOException e) {
                        System.out.println("Failed to replace images in file: " + f.getName() + ", error message: " + e.getMessage());
                    }
                } else if (f.isDirectory()) {
                    replaceImageName(f.getAbsolutePath());
                }
            }
        }
    }


    
}
