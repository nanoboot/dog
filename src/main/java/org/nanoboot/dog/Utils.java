/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.nanoboot.dog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pc00289
 */
public class Utils {

    private static final String UNDERSCORE = "_";

    private Utils() {
        //Not meant to be instantiated.
    }

    public static String replaceUnderscoresBySpaces(String s) {
        if (!s.contains(UNDERSCORE)) {
            //nothing to do
            return s;
        }
        return s.replace(UNDERSCORE, " ");
    }

    public static String makeFirstLetterUppercase(String s) {
        if (Character.isLetter(s.charAt(0)) && Character.isLowerCase(s.charAt(0))) {
            return Character.toUpperCase(s.charAt(0))
                    + (s.length() == 1 ? "" : s.substring(1));
        } else {
            return s;
        }
    }

    public static int getCountOfSlashOccurences(String string) {
        int i = 0;
        for (char ch : string.toCharArray()) {
            if (ch == '/') {
                i++;
            }
        }
        return i++;
    }

    public static List<File> listAdocFilesInDir(File dir) {
        return listAdocFilesInDir(dir, new ArrayList<>());
    }

    private static List<File> listAdocFilesInDir(File dir, List<File> files) {
        List<File> allFiles = listAllFilesInDir(dir, files);
        List<File> adocFiles = new ArrayList<>();
        for (File f : allFiles) {
            if (f.getName().endsWith(".adoc") && !f.isDirectory()) {
                adocFiles.add(f);
            }
        }
        return adocFiles;
    }

    public static List<File> listAllFilesInDir(File dir) {
        return listAllFilesInDir(dir, new ArrayList<>());
    }

    private static List<File> listAllFilesInDir(File dir, List<File> files) {
        files.add(dir);
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                listAllFilesInDir(f, files);
            } else {
                files.add(f);
            }
        }
        return files;
    }
    

    public static String createDoubleDotSlash(int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= times; i++) {
            sb.append("../");
        }
        String result = sb.toString();
        return result;//.substring(0, result.length() - 1);
    }

}
