package net.hostification.znc.znchelper;

//Java imports
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

//PircBotX imports
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;

//ZNCHelper imports
import net.hostification.znc.data.*;
import net.hostification.znc.commands.*;
import net.hostification.znc.console.*;
import net.hostification.znc.features.*;

public class ZNCHelper {
	
	public static PircBotX bot = new PircBotX();
	public final static Logger logger = Logger.getLogger("ZNCHelper");
	
	static String mysql_host;
  static String mysql_db;
  static String mysql_user;
  static String mysql_pass;
  static String mysql_port;
  public static MySQL mysql;
	
  public static void main(String[] args) throws Exception,
			FileNotFoundException, IOException {
		
		//Load properties
		try {
			
			Config.loadConfig();
			
		} catch (FileNotFoundException ex) {
			//generate file if does not exist 
		}
		
		//Connect Block
		bot.setAutoNickChange(true);
		bot.setVersion("ZNCHelper 0.1 - http://znc.hostification.net");
		bot.setLogin(Config.user);
		bot.setName(Config.nick);
		bot.identify(Config.pass);
		bot.setVerbose(false);
		
		//Internal connection start
		if(Config.SSL && !Config.serverpass.isEmpty()) {
			bot.connect(Config.server, Config.port, Config.serverpass, new UtilSSLSocketFactory().trustAllCertificates());
		} else if (Config.SSL && Config.serverpass.isEmpty()) {
			bot.connect(Config.server, Config.port, new UtilSSLSocketFactory().trustAllCertificates());
		} else {
			bot.connect(Config.server, Config.port);
		}
		
		bot.setMessageDelay(Config.messagedelay);
		bot.setAutoReconnect(true);
		bot.setAutoReconnectChannels(true);
		
		joinChannels();
		
		
		setupDatabase();
		
		
		loadListeners();
		
		
		stopCommand();
	}
	
	public static void joinChannels() {
		
		for (int i = 0; i < Config.channels.length; i++) {
			bot.joinChannel(Config.channels[i]);
		}
	}
	
	public static void loadListeners() throws Exception {
		
		//#load features & commands in 'bot.getListenerManager().addListener(new FEATUREORCOMMAND_NAME());' format.
		//
		//Load Features
		
		
		//Load Commands
		bot.getListenerManager().addListener(new Request());
		
		//Load handlers
		
		
	}
	
	private static void setupDatabase() {
		mysql_host = Config.mysql_host;
		mysql_db = Config.mysql_db;
		mysql_user = Config.mysql_user;
		mysql_pass = Config.mysql_pass;
		mysql_port = Config.mysql_port;
		
		mysql = new MySQL(logger, "[ZNCHelper]", mysql_host, mysql_port, mysql_db, mysql_user, mysql_pass);
		
		System.out.println("Trying to connect to database...");
		mysql.open();
		
		if (mysql.checkConnection()) {
			System.out.println("Successfully connected to database!");
			
			if (!mysql.checkTable("zncusers")) {
				System.out.println("Creating table 'zncusers' in database " + mysql_db);
				mysql.createTable("CREATE TABLE zncusers (id int NOT NULL AUTO_INCREMENT, user VARCHAR(32) NOT NULL, lastseen VARCHAR(32) NOT NULL, PRIMARY KEY (id) ) ENGINE=MyISAM;");
			}
			if (!mysql.checkTable("zncrequests")) {
				System.out.println("Creating table 'zncrequests' in database " + mysql_db);
				mysql.createTable("CREATE TABLE zncrequests (id int NOT NULL AUTO_INCREMENT, users VARCHAR(32) NOT NULL, PRIMARY KEY (id) ) ENGINE=MyISAM;");
			}
		} else {
			System.out.println("Error connecting to database.");
		}
		mysql.close();
	}
		
	@SuppressWarnings("resource")
	public static void stopCommand() {
		Scanner reader = new Scanner(System.in);
		String command = reader.nextLine();
		if (command.equals("stop")) {
			System.exit(0);
		}
	}

	//formore
}