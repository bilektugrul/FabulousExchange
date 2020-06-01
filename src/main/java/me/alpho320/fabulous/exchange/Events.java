package me.alpho320.fabulous.exchange;

import me.alpho320.fabulous.exchange.file.Config;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Events implements Listener {

    private Main main;

    private Config config;

    private String prefix;
    private String signLine;

    public Events() {
        main = Main.getInstance();

        config = main.getConfig();
        prefix = config.getString("Main.prefix").replaceAll("&", "§");
        signLine = config.getString("Main.sign-line");
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if(event.getClickedInventory() != event.getWhoClicked().getInventory()) {
            event.setCancelled(true);
        }
        if (event.getInventory().getTitle().equals("§6Borsa Menüsü")) {
            event.setCancelled(true);
        }

        if (event.getCursor() == null || event.getCursor().getType() == Material.AIR) {return; }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();

        if(event.getClickedBlock() == null || event.getClickedBlock().getType() == Material.AIR) return;
        if(event.getClickedBlock().getType() != Material.SIGN_POST && event.getClickedBlock().getType() != Material.WALL_SIGN) return;

        Sign sign = (Sign) event.getClickedBlock().getState();

        if (!sign.getLine(0).equalsIgnoreCase(signLine)) return;

        int amount = Integer.parseInt(sign.getLine(1));
        String value = sign.getLine(2);
        String[] splittedValue = value.split(" ");
        int buy = Integer.parseInt(splittedValue[1]);
        int sell = Integer.parseInt(splittedValue[4]);

        ItemStack material = new ItemStack(Material.matchMaterial(sign.getLine(3)));
        String usd = Main.getInstance().usd;
        if(action.equals(Action.RIGHT_CLICK_BLOCK)) {
            if(main.getEconomy().getBalance(player) >= (buy * Double.parseDouble(usd)) ) {
                material.setAmount(amount);
                main.getEconomy().withdrawPlayer(player, buy);
                player.getInventory().addItem(material);
                player.sendMessage(prefix+"§7Başarıyla §e" + amount + " §7adet §a" + sign.getLine(3) + " §c "+ buy * Double.parseDouble(usd) +"TL §7fiyatına aldınız!");

            } else {
                double x = (buy * Double.parseDouble(usd)) - main.getEconomy().getBalance(player);
                player.sendMessage(prefix + "§cYetersiz bakiye. §4" + x + "TL §ceksik!");
            }
        } else if(action.equals(Action.LEFT_CLICK_BLOCK)) {
            if(player.getInventory().containsAtLeast(material, amount)) {
                player.getInventory().removeItem(new ItemStack(Material.matchMaterial(sign.getLine(3)), amount));

                main.getEconomy().depositPlayer(player, sell * Double.parseDouble(usd));
                double x = sell * Double.parseDouble(usd);
                player.sendMessage(prefix+"§7Başarıyla §e" + amount +" §7adet §a" + sign.getLine(3) + " §c"+ x + " TL §7fiyatına sattınız!");
            } else {
                player.sendMessage(prefix+"§cYetersiz eşya!");
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if(event.getLine(0).equalsIgnoreCase(signLine)) {
            Player player = event.getPlayer();
            if(!player.hasPermission(config.getString("Main.admin-permission"))) { event.setCancelled(true); }
        }
    }

}
