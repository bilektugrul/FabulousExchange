package me.alpho320.fabulous.exchange;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PAPI extends PlaceholderExpansion {

    @Override
    public String getAuthor() {
        return "Alpho320#9202";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String getIdentifier(){
        return "borsa"; //example
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {

        // %example_placeholder1%
        if(identifier.equals("durum")){
            try {
                return Main.getInstance().usd;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // We return null if an invalid placeholder (f.e. %example_placeholder3%)
        // was provided
        return "6.40";
    }

}
