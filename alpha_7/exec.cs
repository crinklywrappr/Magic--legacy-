using System;
using System.IO;
class exec {
	public static void Main() {
		string mem = "";
		string base_dir = "";
		string name = "";
		string args = "";
		string[]Split;
		string cmd = "java";
		string cmd_args = "";
		FileStream file;
		StreamReader sr;
		try {
			file = new FileStream("magic.conf", 
					FileMode.OpenOrCreate, FileAccess.Read);
			sr = new StreamReader(file);
			String tmp;
			while((tmp = sr.ReadLine())!=null) {
				Split = tmp.Split('=');
				if(Split[0].Equals("MEM"))
					mem = Split[1].Replace("\"","");
				else if(Split[0].Equals("DECK_BASE"))
					base_dir = Split[1].Replace("\"","");
				else {
					Console.WriteLine(
						"Variable {0} not known.", 
						Split[0]);
					throw new IOException();
				}
			}
			sr.Close();
			DirectoryInfo di = new DirectoryInfo(base_dir);
			FileInfo[] decks = di.GetFiles("*.deck");
			foreach(FileInfo deck in decks) {
				Split = deck.Name.Split('.');
				name = Split[0];
				args =  args + " " + 
					name + " " + 
					"\"" + base_dir + "\\" + 
					deck.Name + "\"";
			}
			cmd_args = "-Xmx" + mem + "m " + 
				"-classpath .\\swing-layout-1.0.1.jar;. " + 
				"frame " + "\"" + base_dir + "\"" + " " + args;
			Console.WriteLine(cmd + " " + cmd_args);
			System.Diagnostics.Process.Start(cmd, cmd_args);
		}catch(IOException e) {
			Console.WriteLine("Could not find or read magic.conf");
		}
	}
}
