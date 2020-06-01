package me.alpho320.fabulous.exchange;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.lang.Double.*;

public class AdminCMD implements CommandExecutor {

    private Main main;

    public AdminCMD() {
        main = Main.getInstance();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] arg) {
        Player player = (Player) sender;

        if(arg.length == 0) {
            String usd = Main.getInstance().usd;
            Inventory inventory = Bukkit.createInventory(null, 9, "§6Borsa Menüsü");
            inventory.setItem(4, main.createItem(Material.BOOK,"§c» §6Borsa Durumu §c«","§5» §7Dolar: §e" + usd));
            player.openInventory(inventory);
            return true;
        }

        return true;
    }


}
