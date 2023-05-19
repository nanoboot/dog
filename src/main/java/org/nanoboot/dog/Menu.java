/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.nanoboot.dog;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import static org.asciidoctor.Asciidoctor.Factory.create;
import org.asciidoctor.Asciidoctor;

/**
 *
 * @author pc00289
 */
public class Menu {

    private List<MenuItem> menuItems;

    static class MenuItemWithTheSameParentComparator
            implements Comparator<MenuItem> {

        // Method 1
        // To compare customers
        @Override
        public int compare(MenuItem mi1, MenuItem mi2) {

            if (mi1.getWeight() != mi2.getWeight()) {
                return Integer.valueOf(mi2.getWeight()).compareTo(mi1.getWeight());
            }
            return mi1.createLabel().toLowerCase().compareTo(mi2.createLabel().toLowerCase());
        }

    }

    static class MenuItemWeightComparator
            implements Comparator<MenuItem> {

        // Method 1
        // To compare customers
        @Override
        public int compare(MenuItem mi1, MenuItem mi2) {
            return Integer.valueOf(mi2.getWeight()).compareTo(mi1.getWeight());
        }
    }

    public Menu(File rootContentDir) {
        List<File> adocFiles = Utils.listAdocFilesInDir(rootContentDir);

        this.menuItems = new ArrayList<>();

        for (File f : adocFiles) {
            String path = f.getAbsolutePath();
            if (path.endsWith(".adoc")) {
                path = path.replace(".adoc", ".html");
            }
            path = path.replace("content", "generated");

            String finalPath = path.split("/generated/")[1];

            menuItems.add(new MenuItem(finalPath, loadWeight(f)/*(int) (Math.random() * 100)*/));
        }

        menuItems = sortMenuItems(menuItems);
        System.out.println("Going to use these menu items (sorted in this order):");
        int i = 0;
        for (MenuItem mi : menuItems) {
            System.out.println((++i) + " " + mi.getFile());
        }

    }

    private static Integer loadWeight(File f) {
        String s = Utils.readTextFromFile(f);
        boolean commentStarted = false;
        for(String line:s.split("\n")) {
            if(line.trim().equals("////")) {
                commentStarted = !commentStarted;
                continue;
            }
            
            if(commentStarted) {
                if(line.trim().startsWith("weight=")) {
                    String[] keyValue = line.split("=");
                    if(keyValue.length == 2 && keyValue[0].equals("weight")) {
                        return Integer.valueOf(keyValue[1]);
                    }
                }
            }
        }
        return Integer.MAX_VALUE;
    }
    private static List<MenuItem> sortMenuItems(List<MenuItem> menuItemsUnsorted) {
        List<MenuItem> result = new ArrayList<>();

        MenuItemMap menuItemMap = new MenuItemMap(menuItemsUnsorted);
        if (menuItemMap.containsKey("")) {
            List<MenuItem> list = menuItemMap.getList("");
            for (MenuItem mi : list) {
                if (mi.getFile().equals("index.html")) {
                    list.remove(mi);
                    result.add(mi);
                    break;
                }
            }
        }
        menuItemMap.show();
        result.addAll(listChildren("", menuItemMap));
        return result;

    }

    private static List<MenuItem> listChildren(String menuParent, MenuItemMap menuItemMap) {
        System.out.println("--------calling loadChildren(" + menuParent + ", ...)");
        List<MenuItem> result = new ArrayList<>();
        if (!menuItemMap.containsKey(menuParent)) {
            //nothing to do
            System.err.println("!mapOfLists.containsKey(menuParent) - nothing to do");
            return result;
        }
        List<MenuItem> list = menuItemMap.getList(menuParent);
        if (list.isEmpty()) {
            //nothing to do
            System.err.println("list.isEmpty() - nothing to do");
            return result;
        }

        List<MenuItem> children = menuItemMap.getList(menuParent);

        for (MenuItem child : children) {
            System.out.println("Found child: " + child.getFile());
            result.add(child);

            if (child.isIndex()) {
                String key = menuParent + (child.getLevelForMenu() == 1 ? "" : "/") + child.getParent(child.getParentsLength() - 2);

                List<MenuItem> subChildren = listChildren(key, menuItemMap);
                result.addAll(subChildren);
                for (MenuItem subChild : subChildren) {
                    System.out.println("Found subChild: " + subChild.getFile());
                }

            }

        }

        return result;
    }

    public String toAsciidoc(String fileOfHighlightedMenuItem) {
        if (fileOfHighlightedMenuItem.endsWith(".adoc")) {
            fileOfHighlightedMenuItem = fileOfHighlightedMenuItem.substring(0, fileOfHighlightedMenuItem.length() - 5) + ".html";
        }
        int countOfStepsToBaseDirectory = Utils.getCountOfSlashOccurences(fileOfHighlightedMenuItem);
        String doubleDotsSlash = Utils.createDoubleDotSlash(countOfStepsToBaseDirectory);

        StringBuilder asciidoc = new StringBuilder();
        {
            int chapterNumber = 1;

            String wantedParent0 = fileOfHighlightedMenuItem.contains("/") ? fileOfHighlightedMenuItem.split("/")[0] : "";

            for (MenuItem e : menuItems) {

                //boolean hidden = false;
                if (e.getLevelForMenu() > 1 && !e.getParent(0).equals(wantedParent0)) {
                    continue;
                    //hidden = true;
                }

                asciidoc.append(e.createTabs(e.getLevelForMenu()));
                asciidoc
                        .append("link:")
                        .append(doubleDotsSlash)
                        .append(e.getFile())
                        .append(e.getFile().equals(fileOfHighlightedMenuItem) ? "%%%%highlighted%%%%" : "")
                        .append("[")
                        
                        .append((e.getLevelForMenu() == 1 && e.getFileName().equals("index.html") && !e.getFileWithoutFileName().isEmpty()) ? ((chapterNumber++) + ". ") : "")
                        //.append((!e.getFile().equals("index.html") && e.getFileName().equals("index.html")) ? " &#129977; " : ""/*"&#128196; "*/)
                        .append(e.createLabel())
                        //.append("::(").append(e.createMenuParent()).append(")::").append(e.getFile())
                        //.append(hidden ? " (hidden)" : "")
                        //.append(":").append(e.getWeight())
                        .append("]").append("\n");

            }

        }
        String result = asciidoc.toString();

        System.out.println(result);
        return result;

    }

    public String toHtml(String fileOfHighlightedMenuItem) {
        if (fileOfHighlightedMenuItem.startsWith("/")) {
            fileOfHighlightedMenuItem = fileOfHighlightedMenuItem.substring(1);
        }
        String asciidoc = toAsciidoc(fileOfHighlightedMenuItem);
        Asciidoctor asciidoctor = create();
        String html = asciidoctor
                .convert(asciidoc, new HashMap<String, Object>());
        String result
                = """
                <style>
                div.leftMenu {background: rgb(50, 50, 50);width: 300px;max-width:300px;padding-top:10px; padding-bottom:10px;height: 100%;min-height: 400px;float:left;}
                div.leftMenu *{font-family: Arial;color:rgb(204, 204, 204);}
                div.leftMenu ul{list-style: none;padding-left:0;}
                div.leftMenu ul li {margin-top:0px !important;margin-bottom:0px !important;padding-top:0px;padding-bottom:0px;padding-left:0px;}
                div.leftMenu ul li ul li {margin-left:10px;padding-left:15px;}
                div.leftMenu ul li a{text-decoration:none;display:block;padding: 5px 10px 5px 15px;}
                div.leftMenu ul li a:hover{color:white;}
                
                .highlightedMenuItem {/*color:#FFFF99 !important;*/font-weight:bold;background: rgb(32, 39, 43);color:rgb(70,70,70) !important;background:white;}
                div.leftMenu .highlightedMenuItem:hover{color:rgb(70,70,70); !important;}
                div.leftMenu>ul {margin-left:0 !important;}
                div.leftMenu p{margin-bottom:0 !important;padding:0;}
                
                  
                #content{padding-left:40px;}
                  
                </style>
                """
                + html.replaceFirst("<div class=\"ulist\">", "<div class=\"leftMenu\">");
        result = result.replace("%%%%highlighted%%%%\"", "\" class=\"highlightedMenuItem\"");
        StringBuilder sb = new StringBuilder();
        sb.append(result);

        sb.append("<br>\n");

        return sb.toString();
    }
}
