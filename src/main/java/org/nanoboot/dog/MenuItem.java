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
public class MenuItem {

    @Getter
    private final String file;
    @Getter
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

    public String createMenuParent() {
        if (getLevelForMenu() == 1) {
            return "";
        }
        boolean isIndex = fileName.equals(Constants.INDEXHTML);
        if (isIndex) {
            return file.substring(0, file.length() - 2 - fileName.length() - parent.length());
        } else {
            return file.substring(0, file.length() - 1 - fileName.length());
        }
    }

    public String getFileWithoutFileName() {
        return file.substring(0, file.length() - fileName.length());
    }

    public String getParent(int index) {
        return parents[index];
    }

    public int getParentsLength() {
        return parents.length;
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
    public String toString() {
        return "MenuItem{" + "file=" + file + ", weight=" + weight + ", fileArray=" + parents + ", fileName=" + fileName + ", parent=" + parent + '}';
    }

    public String toAsciidoc() {
        //todo
        return null;
    }

}
