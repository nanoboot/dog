///////////////////////////////////////////////////////////////////////////////////////////////
// dog: Tool generating documentation.
// Copyright (C) 2023-2023 the original author or authors.
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; version 2
// of the License only.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
///////////////////////////////////////////////////////////////////////////////////////////////
package org.nanoboot.dog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import static org.asciidoctor.Asciidoctor.Factory.create;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.asciidoctor.Asciidoctor;

/**
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.0.0
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Dog - documentation generator");
        if (args.length == 0) {
//            throw new DogException("At least one argument is expected, but no argument was provided");
            args = new String[]{"gen"};

        }
        String arg0 = args[0];
        //
        Map<String, String> map = new HashMap<>();
        for (String arg : args) {
            if (arg == null) {
                continue;
            }
            if (args[0].equals(arg)) {
                continue;
            }
            String[] keyValue = arg.split("=", 2);
            map.put(keyValue[0], keyValue.length > 1 ? keyValue[1] : null);
        }
        for (String key : map.keySet()) {
            System.out.println("Found argument: " + key + "(=)" + map.get(key));
        }
        //
        switch (arg0) {
            case "gen":
                executeCommandGen(map);
                break;
            case "server":
                executeCommandServer(map);
                break;
            case "new":
                executeCommandNew(map);
                break;
            case "help":
                executeCommandHelp(map);
                break;
            case "version":
                executeCommandVersion(map);
                break;
            case "test":
                executeCommandTest(map);
                break;
            default:
                throw new DogException("Command \"" + arg0 + "\" is not supported.");
        }
    }

    private static void executeCommandGen(Map<String, String> map) {
        if (!map.containsKey("in")) {
            map.put("in", new File(".").getAbsolutePath());
        }

        if (map.get("in") == null) {
            throw new DogException("Argument in must have a value (must not be empty).");
        }
        if (map.containsKey("out") && !(new File(map.get("out")).exists())) {
            throw new DogException("Argument out must be an existing directory.");
        }

        File inDir = new File(map.get("in"));
        if (!inDir.exists()) {
            throw new DogException("Argument in must be an existing directory, but that directory does not exist.");
        }
        File dogConfFile = new File(inDir, "dog.conf");
        if (!dogConfFile.exists()) {
            throw new DogException("File dog.conf was not found.");
        }
        File generatedDir = new File((map.containsKey("out") ? new File(map.get("out")) : inDir), "generated");

        if (generatedDir.exists()) {
            try {
                FileUtils.deleteDirectory(generatedDir);
            } catch (IOException ex) {
                ex.printStackTrace();
                throw new DogException("Deleting generated directory failed.", ex);
            }
        }
        generatedDir.mkdir();
        if (!generatedDir.exists()) {
            throw new DogException("Argument out must be an existing directory, but that directory does not exist.");
        }

        //
        Properties dogConfProperties = null;
        try (InputStream input = new FileInputStream(dogConfFile.getAbsolutePath())) {
            dogConfProperties = new Properties();
            dogConfProperties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new DogException("Loading file dog.conf failed.", ex);
        }
        writeTextToFile(readTextFromResourceFile("/dog.css"), new File(generatedDir, "dog.css"));
        File contentDir = new File(inDir, "content");
        processContentDir(contentDir, generatedDir, contentDir, dogConfProperties);
    }

    private static void processContentDir(File contentDir, File generatedDir, File rootContentDir, Properties dogConfProperties) {
        for (File inFile : contentDir.listFiles()) {
            if (inFile.isFile()) {
                if (inFile.getName().endsWith(".adoc")) {

                    Asciidoctor asciidoctor = create();
                    String asciidocText = readTextFromFile(inFile);

                    String asciidocCompiled = asciidoctor
                            .convert(asciidocText, new HashMap<String, Object>());
                    String pathToRoot = contentDir.getAbsolutePath().replace(rootContentDir.getAbsolutePath(), "");
                    System.out.println("pathToRoot=(" + pathToRoot + ")");
                    if (!pathToRoot.trim().isEmpty()) {
                        int count = 0;
                        for (char ch : pathToRoot.toCharArray()) {
                            if (ch == '/') {
                                count++;
                            }
                        }
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i <= count; i++) {
                            sb.append("../");
                        }
                        pathToRoot = sb.toString();

                    }
                    String titleSeparator = dogConfProperties.containsKey("titleSeparator") ? dogConfProperties.getProperty("titleSeparator") : "::";
                    String start
                            = """
                            <!DOCTYPE html>
                            <html lang="en">
                            <head>
                            <meta charset="UTF-8">
                            <meta http-equiv="X-UA-Compatible" content="IE=edge">
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                            <meta name="generator" content="Asciidoctor 2.0.16">
                            <title>
                            """
                            + createHumanName(inFile)
                            + (dogConfProperties.containsKey("title")
                            ? (" " + titleSeparator + " " + dogConfProperties.getProperty("title")) : "")
                            + """
                            </title>
                            <link rel="stylesheet" href="
                              """
                            + pathToRoot
                            + """
                    dog.css">
                            </head>
                            <body class="article">
                            <div id="header">
                              """
                            + createNavigation(inFile, rootContentDir)
                            + "<h1>"
                            + createHumanName(inFile)
                            + """
                            </h1>
                            </div>
                            <div id="content">
                    """;
                    String end
                            = """
                                  </div>
                                  <div id="footer">
                                     <div id="footer-text">
                                        Last updated """
                            + " "
                            + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ").format(new Date()))
                            + """
                                     </div>
                                  </div>
                               </body>
                            </html>
                            """;
                    String menu
                            = """
                                  <div id="menu">
                                     <div id="footer-text">
                                        Last updated 
                                     </div>
                                  </div>
                            """;
                    String htmlOutput = start + createMenu(rootContentDir, inFile) + asciidocCompiled + end;
                    File htmlFile = new File(generatedDir, inFile.getName().replace(".adoc", ".html"));
                    writeTextToFile(htmlOutput, htmlFile);
                    System.out.println("Going to copy (" + htmlOutput.getBytes().length + " bytes)adoc file:" + inFile.getAbsolutePath());
                    System.out.println("from:" + inFile.getAbsolutePath());
                    System.out.println("to:" + htmlFile.getAbsolutePath());
                } else {
                    copyFile(inFile, generatedDir);
                }

            }
            if (inFile.isDirectory()) {
                File generatedDir2 = new File(generatedDir, inFile.getName());
                generatedDir2.mkdir();
                processContentDir(inFile, generatedDir2, rootContentDir, dogConfProperties);
            }
        }
    }

    private static String createMenu(File rootContentDir, File inFile) {
        List<File> files = listFilesInDir(rootContentDir, new ArrayList<>());

        List<MenuItem> menuItems = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        int countOfStepsToBaseDirectory = getCountOfSlashOccurences(inFile.getAbsolutePath().split("content")[1]) - 1;
        for (File f : files) {
            if (f.isDirectory()) {
                continue;
            }
            String path = f.getAbsolutePath();
            if (path.endsWith(".adoc")) {
                path = path.replace(".adoc", ".html");
            }
            path = path.replace("content", "generated");

            String visibleName = path.split("/generated/")[1];

            String doubleDotsSlash = createDoubleDotSlash(countOfStepsToBaseDirectory);

            menuItems.add(new MenuItem(doubleDotsSlash, visibleName, f.getName()));
        }
        Collections.sort(menuItems);

        StringBuilder tempAsciidocForMenu = new StringBuilder();
        {
            //tempAsciidocForMenu.append("<pre>");
            for (MenuItem currentMenuItem : menuItems) {
 
                tempAsciidocForMenu.append(currentMenuItem.createTabs(currentMenuItem.getLevelForMenu()));
                tempAsciidocForMenu
                        .append("link:")
                        .append(currentMenuItem.doubleDotsSlash)
                        .append(currentMenuItem.visibleName)
                        .append("[")
                        .append(currentMenuItem.getLabel())
                        .append("]").append("\n");

            }
            //tempAsciidocForMenu.append("</pre>");
            
        }

        Asciidoctor asciidoctor = create();

        String tempAsciidocForMenuProcessed = asciidoctor
                .convert(tempAsciidocForMenu.toString(), new HashMap<String, Object>());
        sb.append(tempAsciidocForMenuProcessed);
        sb.append("<br><br><br><br>\n\n\n\n");

        return sb.toString();
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

    private static String createNavigation(File adocFile, File rootContentDir) {
        List<File> files = new ArrayList<>();
        File currentFile = adocFile;
        while (!currentFile.getAbsolutePath().equals(rootContentDir.getAbsolutePath())) {

            if (currentFile.getName().equals("content")) {
                continue;
            }
            files.add(currentFile);
            currentFile = currentFile.getParentFile();
        }
        StringBuilder sb = new StringBuilder("<a href=\"" + createDoubleDotSlash(files.size() - 1) + "index.html\">Home</a>");
        if (files.size() > 1 || !currentFile.getName().equals("index.adoc")) {
            sb.append(" > ");
        }
        for (int i = (files.size() - 1); i >= 0; i--) {
            File file = files.get(i);
            if (file.getName().equals("index.adoc")) {
                continue;
            }
            sb
                    .append("<a href=\"")
                    .append(createDoubleDotSlash(files.size() - i - (i > 0 ? 1 : 2)))
                    //.append(file.getName().replace(".adoc", ""))
                    .append(i == 0 ? file.getName().replace(".adoc", "") : "index")
                    .append(".html\">")
                    .append(createHumanName(file))
                    .append("</a>");
            if (i > 0) {
                sb.append(" > ");
            }

        }
        String result = sb.toString();
        if (result.endsWith(" > ")) {
            result = result.substring(0, result.length() - 3);
        }
        return result;
    }

    private static List<File> listFilesInDir(File dir, List<File> files) {
        files.add(dir);
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                listFilesInDir(f, files);
            } else {
                files.add(f);
            }
        }
        return files;
    }

    private static String createDoubleDotSlash(int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= times; i++) {
            sb.append("../");
        }
        String result = sb.toString();
        return result;//.substring(0, result.length() - 1);
    }

    private static String createHumanName(File inFile) {
        System.out.println("calling createHumanName for inFile=" + inFile.getName());
        String result = inFile.getName();
        if (result.endsWith(".adoc")) {
            result = result.substring(0, inFile.getName().length() - 5);
        }
        result = result.replace("_", " ");
        if (Character.isLetter(result.charAt(0))) {
            result = Character.toUpperCase(result.charAt(0)) + result.substring(1);
        }
        return result;
    }

    private static void copyFile(File originalFile, File copiedFile) throws DogException {
        Path originalPath = originalFile.toPath();
        Path copied = new File(copiedFile, originalFile.getName()).toPath();
        System.out.println("Going to copy:");
        System.out.println("from:" + originalPath.toString());
        System.out.println("to:" + copied.toString());
        try {
            Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new DogException("Copying file failed: " + originalFile.getAbsolutePath());
        }
    }

    private static void writeTextToFile(String text, File file) {
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new DogException("Writing to file failed: " + file.getName(), ex);
        }
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(text);
        printWriter.close();
    }

    private static String readTextFromFile(File file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        } catch (IOException ex) {
            throw new DogException("Reading file failed: " + file.getName(), ex);
        }
    }

    private static String readTextFromResourceFile(String fileName) {
        try {
            Class clazz = Main.class;
            InputStream inputStream = clazz.getResourceAsStream(fileName);
            return readFromInputStream(inputStream);
        } catch (IOException ex) {
            throw new DogException("Reading file failed: " + fileName, ex);
        }

    }

    private static String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    private static void executeCommandServer(Map<String, String> map) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static void executeCommandNew(Map<String, String> map) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static void executeCommandHelp(Map<String, String> map) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private static void executeCommandTest(Map<String, String> map) {

        String input = """
                   
                       = The Dangers of Wolpertingers
                       :url-wolpertinger: https://en.wikipedia.org/wiki/Wolpertinger
                       :linkcss:
                       :stylesheet: dog.css
                       
                       
                       
                       +++
                       title = "About"
                       date = "2019-02-28"
                       menu = "main"
                       +++
                       
                       
                       
                       
                       
                       == Asciidoctor Demo
                       
                       ////
                       Big ol' comment
                       
                       sittin' right 'tween this here title 'n header metadata
                       ////
                       Dan Allen <thedoc@asciidoctor.org>
                       :description: A demo of Asciidoctor. This document +
                                     exercises numerous features of AsciiDoc +
                                     to test Asciidoctor compliance.
                       :library: Asciidoctor
                       ifdef::asciidoctor[]
                       :source-highlighter: coderay
                       endif::asciidoctor[]
                       :idprefix:
                       :stylesheet: asciidoc.css
                       :imagesdir: images
                       //:backend: docbook45
                       //:backend: html5
                       //:doctype: book
                       //:sectids!:
                       :plus: &#43;
                       
                       [role='lead']
                       This is a demonstration of {library}. And this is the preamble of this document.
                       
                       [[purpose]]
                       .Purpose
                       ****
                       This document exercises many of the features of AsciiDoc to test the {library} implementation.
                       ****
                       
                       TIP: If you want the output to look familiar, copy (or link) the AsciiDoc stylesheet, asciidoc.css, to the output directory.
                       
                       NOTE: Items marked with TODO are either not yet supported or a work in progress.
                       
                       [[first,First Steps]]
                       == First Steps with http://asciidoc.org[AsciiDoc]
                       
                       .Inline markup
                       * single quotes around a phrase place 'emphasis'
                       * astericks around a phrase make the text *bold*
                       * double astericks around one or more **l**etters in a word make those letters bold
                       * double underscore around a __sub__string in a word emphasize that substring
                       * use carrots around characters to make them ^super^script
                       * use tildes around characters to make them ~sub~script
                       ifdef::basebackend-html[]
                       * to pass through +++<u>HTML</u>+++ directly, surround the text with triple plus
                       endif::basebackend-html[]
                       ifdef::basebackend-docbook[]
                       * to pass through +++<constant>XML</constant>+++ directly, surround the text with triple plus
                       endif::basebackend-docbook[]
                       
                       // separate two adjacent lists using a line comment (only the leading // is required)
                       
                       - characters can be escaped using a {backslash}
                       * for instance, you can escape a quote inside emphasized text like 'Here\'s Johnny!'
                       - you can safely use reserved XML characters like <, > and &, which are escaped when rendering
                       - force a space{sp}between inline elements using the {sp} attribute
                       - hold text together with an intrinsic non-breaking{nbsp}space attribute, {nbsp}
                       - handle words with unicode characters like in the name Gregory Romé
                       - claim your copyright (C), registered trademark (R) or trademark (TM)
                       
                       You can write text http://example.com[with inline links], optionally{sp}using an explicit link:http://example.com[link prefix]. In either case, the link can have a http://example.com?foo=bar&lang=en[query string].
                       
                       If you want to break a line +
                       just end it in a {plus} sign +
                       and continue typing on the next line.
                       
                       === Lists Upon Lists
                       
                       .Adjacent lists
                       * this list
                       * should join
                       
                       * to have
                       * four items
                       
                       [[numbered]]
                       .Numbered lists
                       . These items
                       . will be auto-numbered
                       .. and can be nested
                       . A numbered list can nest
                       * unordered
                       * list
                       * items
                       
                       .Statement
                       I swear I left it in 'Guy\'s' car. Let\'s go look for it.
                       
                       [[defs]]
                       term::
                         definition
                       line two
                       [[another_term]]another term::
                       
                         another definition, which can be literal (indented) or regular paragraph
                       
                       This should be a standalone paragraph, not grabbed by the definition list.
                       
                       [[nested]]
                       * first level
                       written on two lines
                       * first level
                       +
                       ....
                       with this literal text
                       ....
                       +
                       ** second level
                       *** third level
                       - fourth level
                       * back to +
                       first level
                       
                       // this is just a comment
                       
                       Let's make a horizontal rule...
                       
                       '''
                       
                       then take a break.
                       
                       ////
                       We'll be right with you...
                       
                       after this brief interruption.
                       ////
                       
                       == We're back!
                       
                       Want to see a image:tiger.png[Tiger]?
                       
                       Do you feel safer with the tiger in a box?
                       
                       .Tiger in a box
                       image::tiger.png[]
                       
                       == Included Section
                       
                       Look, I came from out of the [blue]#blue#!
                       
                       --
                       I'm keepin' it open.
                       
                       An 'open block', like this one, can contain other blocks.
                       
                       It can also act as any other block. (TODO)
                       --
                       
                       
                       .Asciidoctor usage example, should contain 3 lines
                       [source, ruby]
                       ----
                       doc = Asciidoctor::Document.new("*This* is it!", :header_footer => false)
                       
                       puts doc.render
                       ----
                       
                       // FIXME: use ifdef to show output according to backend
                       Here's what it outputs (using the built-in templates):
                       
                       ....
                       <div class="paragraph">
                         <p><strong>This</strong> is it!</p>
                       </div>
                       ....
                       
                       === ``Quotes''
                       
                       ____
                       AsciiDoc is 'so' *powerful*!
                       ____
                       
                       This verse comes to mind.
                       
                       [verse]
                       La la la
                       
                       Here's another quote:
                       
                       [quote, Sir Arthur Conan Doyle, The Adventures of Sherlock Holmes]
                       ____
                       When you have eliminated all which is impossible, then whatever remains, however improbable, must be the truth.
                       ____
                       
                       Getting Literal [[literally]]
                       -----------------------------
                       
                        Want to get literal? Just prefix a line with a space (just one will do).
                       
                       ....
                       I'll join that party, too.
                       ....
                       
                       We forgot to mention in <<numbered>> that you can change the numbering style.
                       
                       .. first item (yeah!)
                       .. second item, looking `so mono`
                       .. third item, +mono+ it is!
                       
                       // This attribute line will get reattached to the next block
                       // despite being followed by a trailing blank line
                       [id='wrapup']
                       
                       == Wrap-up
                       
                       
                       
                       NOTE: AsciiDoc is quite cool, you should try it!
                       
                       [TIP]
                       .Info
                       =====
                       Go to this URL to learn more about it:
                       
                       * http://asciidoc.org
                       
                       Or you could return to the xref:first[] or <<purpose,Purpose>>.
                       =====
                       
                       Here's a reference to the definition of <<another_term>>, in case you forgot it.
                       
                       [NOTE]
                       One more thing. Happy documenting!
                       
                       [[google]]When all else fails, head over to <http://google.com>.
                       
                       """;

        Asciidoctor asciidoctor = create();

        String output = asciidoctor
                .convert(input, new HashMap<String, Object>());
        System.out.println(output);

    }

    private static void executeCommandVersion(Map<String, String> map) {
        System.out.println("Dog 0.0.0-SNAPSHOT");
    }

}
