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

/**
 *
 * @author robertvokac
 */
public class MenuItem implements Comparable<MenuItem> {

    String doubleDotsSlash;
    String visibleName;
    String fileName;

    public MenuItem(String doubleDotsSlash, String visibleName, String fileName) {
        this.doubleDotsSlash = doubleDotsSlash;
        this.visibleName = visibleName;
        this.fileName = fileName;
    }

    public String getVisibleNameWithoutFileName() {
        String result = visibleName.replace(fileName.replace(".adoc", ".html"), "");
        if (result.isBlank()) {
            return "aaaaa";
        } else {
            return result;
        }
    }

    public String getLabel() {
        String[] array = visibleName.split("/");
        if (fileName.equals("index.adoc")) {
            if (array.length == 1) {
                return "Home";
            } else {
                return array[array.length - 1 - 1];
            }
        }
        String result = fileName.replace(".adoc", "");;
        if (Character.isLetter(result.charAt(0)) && Character.isLowerCase(result.charAt(0))) {
            result = Character.toUpperCase(result.charAt(0))
                    + (result.length() == 1 ? "" : result.substring(1));
        }
        if (result.contains("_")) {
            result = result.replace("_", " ");
        }
        return result;

    }

    public int getLevel() {
        return Main.getCountOfSlashOccurences(visibleName) + 1;
    }

    public int getLevelForMenu() {
        if (getLevel() == 1 && fileName.equals("index.adoc")) {
            return 0;
        }
        if (getLevel() > 1 && fileName.equals("index.adoc")) {
            return getLevel() - 1 -1;
        }
        return getLevel() -1;
    }

    public String createTabs(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= count; i++) {
            sb.append("*");
        }
        sb.append(" ");
        return sb.toString();
    }

    @Override
    public int compareTo(MenuItem mi2) {
        MenuItem mi1 = this;
        boolean sameLevel = mi1.getLevel() == mi2.getLevel();
        boolean samePath = mi1.getVisibleNameWithoutFileName().equals(mi2.getVisibleNameWithoutFileName());
        boolean mi1IsIndex = mi1.fileName.equals("index.adoc");
        boolean mi2IsIndex = mi2.fileName.equals("index.adoc");
//                if(sameLevel && samePath) {
//                    if(mi1IsIndex && !mi2IsIndex) {
//                        return 1;
//                    }
//                    if(!mi1IsIndex && mi2IsIndex) {
//                        return -1;
//                    }
//                }

        int comparison5 = mi1.getVisibleNameWithoutFileName().toLowerCase().compareTo(mi2.getVisibleNameWithoutFileName().toLowerCase());
        if (comparison5 != 0) {
            return comparison5;
        }
        if (mi1IsIndex) {
            return -1;
        }
        if (mi2IsIndex) {
            return 1;
        }

        int comparison10 = mi1.fileName.toLowerCase().compareTo(mi2.fileName.toLowerCase());

        return comparison10;
    }

    @Override
    public String toString() {
        return "MenuItem{" + "..=" + doubleDotsSlash + ";visibleName=" + visibleName + ";fileName=" + fileName + '}';
    }

}
