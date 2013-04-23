package net.hostification.znc.commands;

import net.hostification.znc.znchelper.ZNCHelper;

import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public class Request {
	
	public void onMessage(MessageEvent event) throws Exception {
		if (event.getMessage().startsWith("!request")) {
			String[] arguments = event.getMessage().split(" ");
			if(arguments.length == 0){
				ZNCHelper.bot.sendMessage(event.getUser(), "Invalid syntax! ");
				ZNCHelper.bot.sendMessage(event.getUser(), "Valid syntax is: !request <username> <email> <irc.server> <irc.port>");
			}
			if(arguments.length == 4){
				//if all args filled correctly, then insert the data in to MySQL database and PM ZNC administrator with notification of new ZNC user request. 
			}
		}
	}
}
