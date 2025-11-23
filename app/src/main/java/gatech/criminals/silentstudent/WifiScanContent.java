package gatech.criminals.silentstudent;

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
public class WifiScanContent {

    /**
     * An array of sample (placeholder) items.
     */
    public static final List<ScanResultItem> ITEMS = new ArrayList<ScanResultItem>();

    /**
     * A map of sample (placeholder) items, by ID.
     */
    public static final Map<String, ScanResultItem> ITEM_MAP = new HashMap<String, ScanResultItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createPlaceholderItem(i));
        }
    }

    private static void addItem(ScanResultItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static ScanResultItem createPlaceholderItem(int position) {
        return new ScanResultItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A placeholder item representing a piece of content.
     */
    public static class ScanResultItem {
        public final String id;
        public final String content;
        public final String details;

        public ScanResultItem(String id, String content, String details) {
            this.id = id;
            this.content = content;
            this.details = details;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}