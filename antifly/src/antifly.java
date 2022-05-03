package com.ko.antifly;

import java.io.File;
import java.util.logging.Logger;

import javax.swing.Spring;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class antifly extends JavaPlugin implements Listener{

	public void loadConfiguration(){
		String path = "enabled";
		getConfig().addDefault(path, true);
		getConfig().options().copyDefaults(true);
		saveConfig();


		String path2 = "kick-on-fly";
		getConfig().addDefault(path2, false);
		getConfig().options().copyDefaults(true);
		saveConfig();

		String path3 = "worlds-disabled-on";
		getConfig().addDefault(path3, "sample1,sample2,sample3");
		getConfig().options().copyDefaults(true);
		SaveConfig();

		String path4 = "kick-message";
		getConfig().addDefault(path4, "You have been kicked for cheating");
		getConfig().options().copyDefaults(true);
		saveConfig();

		String path5 = "notify-staff-on-fly";
		getConfig().addDefault(path5, true);
		getConfig().options().copyDefaults(true);
		saveConfig();

		String path6 = "notify-staff-on-update";
		getConfig().addDefault(path6, true);
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	@Override
	public void onDisable() {
		if(getConfig().getBoolean("enabled") == true){
			System.out.print("Antifly has been Disabled."); //Plugin disable message
		} else {
			System.out.print("Antifly is not Enabled! Please enabled it in the config file.");
		}
	}

	@Override
	public void onEnable() {
		loadConfiguration();
		if(getConfig().getBoolean("enabled") == true){
			System.out.print("Antifly has been Enabled.");  //Plugin enable message
			getServer().getPluginManager().registerEvents(this, this);
		} else {
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	@EventHandler
    public void OnPlayerMove (PlayerMoveEvent event ) {
		Player player = event.getPlayer();

		if(player.isFlying()){
			if(!player.hasPermission("antifly.fly")){
				if(getConfig().getBoolean("kick-on-fly") == true){
					player.kickPlayer("[AntiFly]" + getConfig().getString("kick-message").replace("&", "�"));
					for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
						World world1 = player.getWorld();
						String world = world1.toString();
						String dis = getConfig().getString("worlds-disabled-on");

						if(!dis.toLowerCase().contains(world.toLowerCase())){
							if(getConfig().getBoolean("notify-staff-on-fly:") == true){
								if(staff.hasPermission("antifly.notify")){
									staff.sendMessage(ChatColor.RED +"[AntiFly]" + ChatColor.GREEN + " Player " + player.getName() + " was in flying without permissions to do so and was kicked.");
								}
							} else {
								player.setFlying(false);
								player.setAllowFlight(false);
								for (Player staff1 : Bukkit.getServer().getOnlinePlayers()) {
									if(getConfig().getBoolean("notify-staff-on-fly:") == true){
										if(staff1.hasPermission("antifly.notify")){
											staff1.sendMessage(ChatColor.RED +"[AntiFly]" + ChatColor.GREEN + " Player " + player.getName() + " was in flying without permissions to do so and was switched back to walking.");
										}
									}
								}
							}
						}
					}
				}	
				if(player.getGameMode() == GameMode.CREATIVE){
					if(!player.hasPermission("antifly.creative")){
						World world1 = player.getWorld();
						String world = world1.toString();
						String dis = getConfig().getString("worlds-disabled-on");

						if(!dis.toLowerCase().contains(world.toLowerCase())){

							if(getConfig().getBoolean("kick-on-fly") == true){
								player.kickPlayer("[AntiFly]" + getConfig().getString("kick-message"));
							} else {
								player.setGameMode(GameMode.SURVIVAL);

								for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
									if(getConfig().getBoolean("notify-staff-on-fly:") == true){
										if(staff.hasPermission("antifly.notify")){
											staff.sendMessage(ChatColor.RED +"[AntiFly]" + ChatColor.GREEN + " Player " + player.getName() +  " was in creative without permissions to do so and was switched back to survival.");
										}
									}
								}
							}

						}
					}
				}
			}
		} //end of if player is flying
	}



	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("antifly")){
			if(args.length <= 1){
				if(args[0].toLowerCase().matches("reload")){
					reloadConfig();
					sender.sendMessage(ChatColor.RED + "[AntiFly] Has been reloaded");
				}
			}
		}
		return false;
	}
}