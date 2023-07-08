package com.tencent.filecrud;

import java.io.File;

/**
 * <p>Title: FileTree</p>
 * <p>
 *    Description:
 *    本类的作用是打印出树状的包结构以便写md时作说明之用
 * </p>
 * @author xianxian
 * @date 2023年7月4日上午10:10:33
 */
public class FileTree {
    private static final String[] IGNORED_FOLDERS = {
    		"node_modules", ".git",".idea","resources","target",".externalToolBuilders",
    		".gradle",".metadata",".settings",".svn"
    		};
    private static final String PROJECTABSOULTPATH = "E:\\Google\\crm";
    
    public static void main(String[] args) {
        File file = new File(PROJECTABSOULTPATH);
        listFiles(file, 0);
    }

    public static void listFiles(File file, int level) {
        if (isIgnored(file)) {
            return;
        }
        printTree(file, level);
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                listFiles(subFile, level + 1);
            }
        }
    }

    public static void printTree(File file, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("|   ");
        }
        sb.append("+- ").append(file.getName()).append(" -- ");
        System.out.println(sb.toString());
    }

    public static boolean isIgnored(File file) {
        for (String ignoredFolder : IGNORED_FOLDERS) {
            if (file.getName().equals(ignoredFolder)) {
                return true;
            }
        }
        return false;
    }
}
