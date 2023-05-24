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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author pc00289
 */
public class MenuItemMap {

    private final HashMap<String, List<MenuItem>> internalMap = new HashMap<>();

    public MenuItemMap(List<MenuItem> list) {

        for (MenuItem mi : list) {
            String menuParent = mi.createMenuParent();
            if (!internalMap.containsKey(menuParent)) {
                internalMap.put(menuParent, new ArrayList<>());
            }
            internalMap.get(menuParent).add(mi);
        }
        for(String key:internalMap.keySet()) {
            sortMenuItemsWithTheSameParent(internalMap.get(key));
        }
    }

        private static void sortMenuItemsWithTheSameParent(List<MenuItem> list) {
            List<MenuItem> nonIndexFiles = new ArrayList<>();
            List<MenuItem> indexFiles = new ArrayList<>();
            for (MenuItem e : list) {
                if (e.isIndex()) {
                    
                    indexFiles.add(e);
                } else {
                    nonIndexFiles.add(e);
                }
            }
            Collections.sort(nonIndexFiles, new Menu.MenuItemWithTheSameParentComparator());
            //
            Collections.sort(indexFiles, new Menu.MenuItemWithTheSameParentComparator());
            list.clear();
            list.addAll(nonIndexFiles);
            list.addAll(indexFiles);
        }
    public void show() {
        for (String key : internalMap.keySet()) {
            System.out.println("\t key=" + key);

            for (MenuItem e : internalMap.get(key)) {
                System.out.println("\t\t menuItem=" + e.getFile());
            }

        }

    }
    public boolean containsKey(String key) {
        return this.internalMap.containsKey(key);
    }
    public List<MenuItem> getList(String key) {
        return this.internalMap.get(key);
    }
}
