package ru.permasha.castlewars.utils;

import com.google.common.base.Strings;
import net.md_5.bungee.api.ChatColor;

public class ProgressBar {

    public static String getProgressBar(int current, int max, int totalBars, char symbol, ChatColor completedColor,
                                 ChatColor notCompletedColor) {
        float percent = (float) current / max;
        int progressBars = (int) (totalBars * percent);

        return Strings.repeat("" + completedColor + symbol, progressBars)
                + Strings.repeat("" + notCompletedColor + symbol, totalBars - progressBars);
    }

}
