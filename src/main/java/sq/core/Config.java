package sq.core;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

/**
 * Spider Queen's configuration settings. Applies the standard configuration settings and 
 * helps set up the configuration GUI.
 */
public final class Config 
{
	private final Configuration config;

	public int baseItemId;
	public int baseBlockId;
	public int baseEntityId;
	
	public boolean allowCrashReporting;
	public boolean allowUpdateChecking;

	public boolean usePlayerSkin;
	public boolean useSpiderQueenModel;
	public boolean useSpawnSystem;
	
	public boolean enableNightVision;
	
	public boolean enableYuki;
	public boolean enableJack;
	public boolean enableMandragora;
	public boolean enableFly;
	public boolean enableBeetle;
	public boolean enableAnt;
	public boolean enableHumans;
	public boolean enableFactories;
	
	public boolean showHumanName;
	public boolean showHumanSkin;
	public boolean showHumanType;
	
	public int antSpawnCap;
	public int beeSpawnCap;
	
	public Config(FMLPreInitializationEvent event)
	{
		config = new Configuration(event.getSuggestedConfigurationFile());
		addConfigValues();
	}

	public void addConfigValues()
	{
		config.setCategoryComment("Init", "Settings that affect how Spider Queen starts up.");
		baseItemId = config.get("Init", "Base Item ID", 38955, "The base ID to use for items in Spider Queen. Only applicable in 1.6.4.").getInt();
		baseBlockId = config.get("Init", "Base Block ID", 3121, "The base ID to use for blocks in Spider Queen. Only applicable in 1.6.4.").getInt();
		baseEntityId = config.get("Init", "Base Entity ID", 127, "The base ID to use for entities in Spider Queen. Only change if you know what you are doing!").getInt();
		useSpawnSystem = config.get("Init", "Use built-in spawn system?", true, "Enables or disables Spider Queen's custom spawn system (Requires restart).").getBoolean();
		enableYuki = config.get("Init", "Enable Yuki?", true, "Enables/disables Yuki.").getBoolean();
		enableJack = config.get("Init", "Enable Jack?", true, "Enables/disables Jack. WARNING: You will not be able to acquire the bug light!").getBoolean();
		enableMandragora = config.get("Init", "Enable Mandragora?", true, "Enables/disables the mandragora. WARNING: Removes friendly mandragoras as well!").getBoolean();
		enableFly = config.get("Init", "Enable Flies?", true, "Enables/disables flies.").getBoolean();
		enableBeetle = config.get("Init", "Enable Beetles?", true, "Enables/disables beetles.").getBoolean();
		enableAnt = config.get("Init", "Enable Ants?", true, "Enables/disables ants.").getBoolean();
		enableHumans = config.get("Init", "Enable Humans?", true, "Enables/disables humans.").getBoolean();
		enableFactories = config.get("Init", "Enable Factories?", true, "Enables/disables NPC factories.").getBoolean();
		
		config.setCategoryComment("Graphics", "Settings that affect graphics-related portions of the mod.");
		usePlayerSkin = config.get("Graphics", "Use player skin?", false, "True if you want your Minecraft skin to be used instead of the spider queen skin.").getBoolean();
		useSpiderQueenModel = config.get("Graphics", "Use spider queen model?", true, "False if you want to keep the standard Minecraft player model.").getBoolean();
		enableNightVision = config.get("Graphics", "Enable night vision?", true, "False if you want the Spider Queen's default night vision to be disabled").getBoolean();
		
		showHumanName = config.get("Graphics", "Show human names?", false, "True if you want the humans in the game to have the names of real players.").getBoolean();
		showHumanSkin = config.get("Graphics", "Show human skins?", true, "True if you want the humans in the game to have the skins of real players.").getBoolean();
		showHumanType = config.get("Graphics", "Show human type?", true, "True if you want to see the human's type (ex. Poor Miner) when you are near them.").getBoolean();
		
		config.setCategoryComment("Performance", "Settings that can affect your game performance.");
		antSpawnCap = config.get("Performance", "Ant spawn cap", 10, "The maximum number of ants that can spawn within a 16 block radius.").getInt();
		beeSpawnCap = config.get("Performance", "Bee spawn cap", 5, "The maximum number of bees that can spawn within a 16 block radius.").getInt();
		
		config.setCategoryComment("Privacy", "Setting pertaining to your privacy while using Spider Queen.");
		allowCrashReporting = config.get("Privacy", "Allow crash reporting", true, "True if Spider Queen can send crash reports to the mod authors. Crash reports may include your Minecraft username, OS version, Java version, and PC username.").getBoolean();
		allowUpdateChecking = config.get("Privacy", "Allow update checking", true, "True if Spider Queen can check for updates. This setting requires a restart in order to take effect.").getBoolean();
		
		config.save();
	}
	
	public void syncConfiguration()
	{
		config.load();
		addConfigValues();
		config.save();
	}
	
	public Configuration getConfigInstance()
	{
		return config;
	}

	public List<IConfigElement> getConfigCategories()
	{
		List<IConfigElement> elements = new ArrayList<IConfigElement>();

		for (String s : config.getCategoryNames())
		{
			//Don't include server only categories in the configuration menu's list of options.
			if (!s.equals("server"))
			{	
				IConfigElement element = new ConfigElement(config.getCategory(s));
				for (IConfigElement e : (List<IConfigElement>)element.getChildElements())
				{
					elements.add(e);
				}
			}
		}

		return elements;
	}
}
