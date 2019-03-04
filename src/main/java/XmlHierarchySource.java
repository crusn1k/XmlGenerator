import java.util.HashMap;
import java.util.Map;

public class XmlHierarchySource {

    public static Map<String, XmlField> getHierarchyMap() {
        XmlField game = new XmlField("game", "game", "node");
        XmlField dota2 = new XmlField("dota2", "game", "node");
        XmlField players = new XmlField("players", "dota2", "node");
        XmlField player = new XmlField("player", "players", "node");
        XmlField name = new XmlField("name", "player", "node");
        XmlField items = new XmlField("items", "dota2", "node");
        XmlField item = new XmlField("item", "items", "node");
        XmlField itemName = new XmlField("itemName", "item", "attr");
        XmlField version = new XmlField("version", "dota2", "attr");
        XmlField playerId = new XmlField("id", "player", "attr");


        XmlField cs = new XmlField("cs", "game", "node");

        Map<String, XmlField> map = new HashMap<>(4);
        map.put(game.getName(), game);
        map.put(dota2.getName(), dota2);
        map.put(players.getName(), players);
        map.put(player.getName(), player);
        map.put(name.getName(), name);
        map.put(item.getName(), item);
        map.put(items.getName(), items);
        map.put(itemName.getName(), itemName);
        map.put(version.getName(), version);
        map.put(playerId.getName(), playerId);

        return map;
    }
}
