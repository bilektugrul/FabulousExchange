package me.alpho320.fabulous.exchange;
import me.alpho320.fabulous.exchange.file.Config;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public static Main instance;

    private Config config;

    private Economy economy;

    Logger logger = Logger.getLogger("Minecraft");
    public String usd;

    @Override
    public void onEnable() {
        instance = this;

        config = new Config();

        getServer().getPluginManager().registerEvents(new Events(), this);
        this.getCommand("borsa").setExecutor(new AdminCMD());

        if(!setupEconomy()) {
            logger.info("FabulousExchange: Vault bulunamadı!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPI().register();
        }

        logger.info("============ Fabulous ============");
        logger.info("");
        logger.info("FabulousExchange Aktif!");
        logger.info("Version: "+ getDescription().getVersion());
        logger.info("Developer: Alpho320#9202");
        logger.info("");
        logger.info("============ Fabulous ============");

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    usd = getJson("usd");
                } catch (Exception ignored){

                }
            }
        }.runTaskTimer(this, 0, 1800*20);
    }

    @Override
    public void onDisable() {
        logger.info("============ Fabulous ============");
        logger.info("");
        logger.info("FabulousExchange Deaktif!");
        logger.info("Version: "+ getDescription().getVersion());
        logger.info("Developer: Alpho320#9202");
        logger.info("");
        logger.info("============ Fabulous ============");
    }

    public ItemStack createItem(Material material, String name, String...lore){
        ItemStack item = new ItemStack(material,1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);
        return item;
    }


    public String getJson(String value) throws Exception {
        JSONObject json = readJsonFromUrl("http://www.floatrates.com/daily/try.json");
        JSONObject object = json.getJSONObject(value);
        return object.get("inverseRate").toString();
    }

    public String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public Economy getEconomy() {
        return economy;
    }

    public static Main getInstance() { return instance; }

    @Override
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

}
