package utils;

import java.util.List;

public class UnitProp {
    public static final List<String> standardCards
        = List.of(new String[]{"Sentinel",
        "Berserker",
        "Goliath",
        "Warden"
    });

    public static final List<String> envCards
        = List.of(new String[]{"Firestorm",
        "Winterfell",
        "Heart Hound"
    });

    public static final List<String> specialCards
        = List.of(new String[]{"The Ripper",
        "Miraj",
        "The Cursed One",
        "Disciple"
    });

    public static final List<String> rangedCards
        = List.of(new String[]{"Sentinel",
        "Berserker",
        "The Cursed One",
        "Disciple"
    });

    public static final List<String> tankCards
        = List.of(new String[]{"Goliath",
        "Warden"
    });

    public static boolean isTank (String name) {
        return tankCards.contains(name);
    }

    public static UnitPos getPosition (String name) {
        if (rangedCards.contains(name))
            return UnitPos.RANGED;
        return UnitPos.CLOSE;
    }

    public static String getType(String name) {
        if (specialCards.contains(name))
            return "Special";
        if (envCards.contains(name))
            return "Environment";
        if (standardCards.contains(name))
            return "Standard";
        return "none";
    }
}
