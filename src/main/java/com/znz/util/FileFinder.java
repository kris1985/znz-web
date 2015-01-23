package com.znz.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 查找文件
 */

public class FileFinder {

	public static List findFiles(String baseDirName, String targetFileName,
			int count) {

		List fileList = new ArrayList();
		File baseDir = new File(baseDirName);
		if (!baseDir.exists() || !baseDir.isDirectory()) {
			System.out.println(baseDirName);
			return fileList;
		}
		String tempName = null;
		Queue queue = new LinkedBlockingDeque();
		queue.add(baseDir);
		File tempFile = null;
		while (!queue.isEmpty()) {

			tempFile = (File) queue.poll();
			if (tempFile.exists() && tempFile.isDirectory()) {
				File[] files = tempFile.listFiles();
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						queue.add(files[i]);
					} else {
						tempName = files[i].getName();
						if (FileFinder.wildcardMatch(targetFileName, tempName)) {
							fileList.add(files[i].getAbsoluteFile());
							if ((count != 0) && (fileList.size() >= count)) {
								return fileList;
							}
						}
					}
				}
			}
		}

		return fileList;
	}

	private static boolean wildcardMatch(String pattern, String str) {
		int patternLength = pattern.length();
		int strLength = str.length();
		int strIndex = 0;
		char ch;
		for (int patternIndex = 0; patternIndex < patternLength; patternIndex++) {
			ch = pattern.charAt(patternIndex);
			if (ch == '*') {
				while (strIndex < strLength) {
					if (wildcardMatch(pattern.substring(patternIndex + 1),
							str.substring(strIndex))) {
						return true;
					}
					strIndex++;
				}
			} else if (ch == '?') {
				strIndex++;
				if (strIndex > strLength) {
					return false;
				}
			} else {
				if ((strIndex >= strLength) || (ch != str.charAt(strIndex))) {
					return false;
				}
				strIndex++;
			}
		}
		return (strIndex == strLength);
	}

	public static void main(String[] paramert) {
		   long start = new Date().getTime();
		String baseDIR = "d:/workspace";
		String fileName = "TimeServerHandler*";
		int countNumber = 5;
		List resultList = FileFinder.findFiles(baseDIR, fileName, countNumber);
		if (resultList.size() == 0) {
			System.out.println("NoFileFount.");
		} else {
			for (int i = 0; i < resultList.size(); i++) {
				System.out.println(resultList.get(i));
			}
		}
		 long end = new Date().getTime();
         System.out.println("����ʱ��[" + (end - start) + "]ms");
	}
}