package com.demo.ecclient.placeholder;

import com.demo.ecclient.model.EdgeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PlaceholderContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<EdgeInfo> ITEMS = new ArrayList<>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<Long, EdgeInfo> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;


    private static void addItem(EdgeInfo item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getEdgeId(), item);
    }

    public static void updateItem(List<EdgeInfo> items) {
        ITEMS.clear();
        ITEMS.addAll(items);
    }

}