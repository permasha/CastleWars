package ru.permasha.castlewars.api;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import org.apache.commons.lang.StringUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class RandomCraftAPI {

    public static String getColNickViaLPAPI(OfflinePlayer p) {
        LuckPerms lp = LuckPermsProvider.get();
        User user = lp.getUserManager().getUser(p.getUniqueId());
        QueryOptions queryOptions = lp.getContextManager().getQueryOptions(p);
        CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);
        String m = metaData.getMetaValue("rank-color");
        return (m == null) ? ("§f" + p.getName()) : (getColoredNickByColor(p, m) + "§r");
    }

    public static String getColoredNickByColor(OfflinePlayer player, String color) {
        byte i = 0;
        String name = player.getName();
        String tempVar = player.getName();
        for (i = 0; tempVar.startsWith("_") && i != tempVar.length(); i = (byte)(i + 1))
            tempVar = tempVar.replaceFirst("_", "");
        String rankColoredNick = "§f" + name.replaceFirst(String.valueOf(name.charAt(i)), String.valueOf(color) + name.charAt(i) + "§f");
        return StringUtils.replace(rankColoredNick, "&", "§");
    }

}
