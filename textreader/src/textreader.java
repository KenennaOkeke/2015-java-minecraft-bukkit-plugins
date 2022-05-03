package textreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin{
	protected Logger log;
	private FileReader fr;

	public void onEnable() {
		System.out.print("[Textreader] I have been enabled!");

		//Check if folder is already created
		File f = new File("textfiles");
		if (!f.exists()) {
		    if(!f.isDirectory()){
		    	f.mkdir();
		    }
		}
	}
	public String readFile(String filename)
	{
			String text = "";
		    int read, N = 1024 * 1024;
		    char[] buffer = new char[N];

		    try {
		        fr = new FileReader(filename);
		        BufferedReader br = new BufferedReader(fr);

		        while(true) {
		            read = br.read(buffer, 0, N);
		            text += new String(buffer, 0, read);

		            if(read < N) {
		                break;
		            }
		        }
		        fr = null;
		        br = null;

		    } catch(Exception ex) {
		        return null;
		    }
		    buffer = null;
		    return text;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().toLowerCase().startsWith("textfile"))
		{
			if (args.length == 1) {
				if(args[0].matches("/([A-Za-z0-9_-]+)/")){
					String textfile = args[0] + ".txt";
					//TODO: Add validation that the file exists
					//TODO: Chunk-based reader/output, we are asusuming the file is small here
					String text = readFile("textfiles/" + textfile);

					if(text != null)
					{
						String[] split = text.split(System.getProperty("line.separator"));
						sender.sendMessage(ChatColor.GREEN + "#################### Start of file ####################");
						for(int i = 0; i < split.length; i++){
							if(i > 0){
								int last = i - 1;
								split[last] = null;
							}
							sender.sendMessage(ChatColor.YELLOW + split[i].toString());
						}
						sender.sendMessage(ChatColor.GREEN +"##################### End of file #####################");
						//Settings old values to null
						text = null;
						split = null;
					} else {
						sender.sendMessage("[TextReader] File not found!");
					}
				} else {
					//may be a dirty String
				}
			} else {
				sender.sendMessage("Proper Usage is /textfile <filename>");
			}
		}
		return false;
	}

	public void OnDisable() {
		System.out.print("[Textreader] I have been disabled!" );
	}
}
