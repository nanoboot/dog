/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.nanoboot.dog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import static org.asciidoctor.Asciidoctor.Factory.create;
import org.asciidoctor.Asciidoctor;

/**
 *
 * @author pc00289
 */
public class Menu {

    private final List<MenuItem> menuItems;

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

            menuItems.add(new MenuItem(finalPath, 1000));
        }
        Collections.sort(menuItems);
        for (MenuItem mi : menuItems) {
            System.out.println("Found menu item: " + mi.toString());
        }

    }

    public String toAsciidoc(String fileOfHighlightedMenuItem) {
        if (fileOfHighlightedMenuItem.endsWith(".adoc")) {
            fileOfHighlightedMenuItem = fileOfHighlightedMenuItem.substring(0, fileOfHighlightedMenuItem.length() - 5) + ".html";
        }
        int countOfStepsToBaseDirectory = Utils.getCountOfSlashOccurences(fileOfHighlightedMenuItem);
        String doubleDotsSlash = Utils.createDoubleDotSlash(countOfStepsToBaseDirectory);
        System.out.println("fileOfHighlightedMenuItem=" + fileOfHighlightedMenuItem);
        StringBuilder asciidoc = new StringBuilder();
        {
            int chapterNumber = 1;

            String wantedParent0 = fileOfHighlightedMenuItem.contains("/") ? fileOfHighlightedMenuItem.split("/")[0] : "";
            System.out.println("wantedParent0=" + wantedParent0);

            for (MenuItem e : menuItems) {

                if (e.getLevelForMenu() > 1 && !e.getParent(0).equals(wantedParent0)) {
                    continue;
                }
                System.out.println("iterating file:" + e.getFile() + " fileOfHighlightedMenuItem=" + fileOfHighlightedMenuItem);
                asciidoc.append(e.createTabs(e.getLevelForMenu()));
                asciidoc
                        .append("link:")
                        .append(doubleDotsSlash)
                        .append(e.getFile())
                        .append(e.getFile().equals(fileOfHighlightedMenuItem) ? "%%%%highlighted%%%%" : "")
                        .append("[")
                        .append((e.getLevelForMenu() == 1 && e.getFileName().equals("index.html") && !e.getFileWithoutFileName().isEmpty()) ? ((chapterNumber++) + ". ") : "")
                        .append(e.createLabel())
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
                div.leftMenu {background: rgb(50, 50, 50);max-width:300px;padding-top:10px; padding-bottom:10px;height: 100%;min-height: 400px;width: 300px;float:left;}
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
