/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
            System.out.println("sortMenuItemsWithTheSameParent started");
            System.out.println("before:");
            for(MenuItem mi:list) {
                System.out.println("--" + mi.getFile());
            }
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
//            Collections.sort(list, new Menu.MenuItemWeightComparator());
            System.out.println("after:");
            for(MenuItem mi:list) {
                System.out.println("--" + mi.getFile());
            }
            System.out.println("sortMenuItemsWithTheSameParent started");
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
