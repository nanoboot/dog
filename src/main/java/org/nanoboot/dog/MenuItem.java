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

import lombok.Getter;

/**
 *
 * @author robertvokac
 */
public class MenuItem implements Comparable<MenuItem> {

    @Getter
    private final String file;
    private final int weight;
    //
    private final String[] parents;
    @Getter
    private final String fileName;
    private final String parent;

    public MenuItem(String file, int weight) {
        this.file = file;
        this.parents = file.split("/");
        this.fileName = parents[parents.length - 1];
        this.parent = parents.length < 2 ? "Home" : parents[parents.length - 2];
        this.weight = weight;

    }

    public String getFileWithoutFileName() {
        return file.substring(0, file.length() - fileName.length());
    }

    public String getParent(int index) {
        return parents[index];
    }
    public boolean isIndex() {
        return fileName.equals(Constants.INDEXHTML);
    }

    public String createLabel() {
        String label;
        if (isIndex()) {
            label = parent;
        } else {
            label = fileName.replace(".html", "");
        }
        label = Utils.makeFirstLetterUppercase(label);
        label = Utils.replaceUnderscoresBySpaces(label);
        return label;

    }

    public int getLevel() {
        return Utils.getCountOfSlashOccurences(file) + 1;
    }

    public int getLevelForMenu() {
        if (getLevel() == 1 && isIndex()) {
            return 1;
        }
        if (getLevel() > 1 && isIndex()) {
            return getLevel() - 1;
        }
        return getLevel();
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
        boolean mi1IsIndex = mi1.isIndex();
        boolean mi2IsIndex = mi2.isIndex();

        if (mi1.getFileWithoutFileName().isEmpty() && !mi2.getFileWithoutFileName().isEmpty()) {
            return -1;
        }
        if (!mi1.getFileWithoutFileName().isEmpty() && mi2.getFileWithoutFileName().isEmpty()) {
            return 1;
        }

        int fileArrayMaxLength = mi1.parents.length > mi2.parents.length ? mi1.parents.length : mi2.parents.length;

        for (int i = 0; i < fileArrayMaxLength; i++) {
            String string1 = mi1.parents.length == fileArrayMaxLength ? mi1.parents[i] : mi2.parents[i];
            String string2 = mi2.parents.length == fileArrayMaxLength ? mi2.parents[i] : string1;
            if (!string1.equals(string2)) {
                if (mi1.getFileWithoutFileName().equals(mi2.getFileWithoutFileName())) {
                    if (mi1IsIndex) {
                        return -1;
                    }
                    if (mi2IsIndex) {
                        return 1;
                    }
                }
                return string1.toLowerCase().compareTo(string2.toLowerCase());
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "MenuItem{" + "file=" + file + ", weight=" + weight + ", fileArray=" + parents + ", fileName=" + fileName + ", parent=" + parent + '}';
    }

    public String toAsciidoc() {
        //todo
        return null;
    }

}
