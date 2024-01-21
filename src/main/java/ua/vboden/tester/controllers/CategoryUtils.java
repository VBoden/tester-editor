package ua.vboden.tester.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import ua.vboden.tester.entities.Category;

public class CategoryUtils {

	public static Map<Category, TreeItem<Category>> fillCategoryView(TreeView<Category> themesTree,
			List<Category> models) {
		Map<Category, TreeItem<Category>> nodes = new HashMap<>();
		Map<Category, List<Category>> subcategories = new HashMap<>();
		List<Category> rootCategories = new ArrayList<>();
		for (Category category : models) {
			Category supperCategory = category.getSupperCategory();
			if (supperCategory != null) {
				if (!subcategories.containsKey(supperCategory)) {
					subcategories.put(supperCategory, new ArrayList<>());
				}
				subcategories.get(supperCategory).add(category);
			} else {
				rootCategories.add(category);
			}
		}

		TreeItem<Category> rootNode = new TreeItem<>();
		rootNode.setValue(rootCategories.get(0));
		rootNode.setExpanded(true);
		themesTree.setRoot(rootNode);
		nodes.put(rootCategories.get(0), rootNode);
		if (rootCategories.size() > 0) {
			for (int j = 1; j < rootCategories.size(); j++) {
				Category value = rootCategories.get(j);
				addNode(value, rootNode, nodes);
			}
		}
		for (Entry<Category, List<Category>> entry : subcategories.entrySet()) {
			Category sup = entry.getKey();
			if (!nodes.containsKey(sup)) {
				TreeItem<Category> node = new TreeItem<>();
				node.setValue(sup);
//				rootNode.getChildren().add(node);
				nodes.put(sup, node);
			}
			TreeItem<Category> supNode = nodes.get(sup);
			for (Category cat : entry.getValue()) {
				addNode(cat, supNode, nodes);
			}
		}
		return nodes;
	}

	private static void addNode(Category cat, TreeItem<Category> supNode, Map<Category, TreeItem<Category>> nodes) {
		TreeItem<Category> node = new TreeItem<>();
		node.setValue(cat);
		supNode.getChildren().add(node);
		nodes.put(cat, node);
	}
}
