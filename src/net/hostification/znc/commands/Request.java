package net.hostification.znc.commands;

import net.hostification.znc.znchelper.ZNCHelper;
import net.hostification.znc.znchelper.Config;

import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

@SuppressWarnings("rawtypes")
public class Request extends ListenerAdapter {
	
	public void onMessage(MessageEvent event) throws Exception {
		
		String[] cSyntax;
		
		cSyntax = new String[4];
		
		
		cSyntax[1] = event.getMessage().split(" ")[1];
		cSyntax[2] = event.getMessage().split(" ")[2];
		cSyntax[3] = event.getMessage().split(" ")[3];
		cSyntax[4] = event.getMessage().split(" ")[4];
		
		if (event.getMessage().startsWith("!request")) {
				//if all args filled correctly, then insert the data in to MySQL database and PM ZNC administrator with notification of new ZNC user request. 
				
				for (int i = 0; i < Config.admins.length; i++) {
					ZNCHelper.bot.sendMessage(Config.admins[i], "Success!"); 
				}
			}else{
				ZNCHelper.bot.sendMessage(event.getUser(), "Invalid syntax! ");
				ZNCHelper.bot.sendMessage(event.getUser(), "Valid syntax is: !request <username> <email> <irc.server> <irc.port>");
			}
		}
	}
