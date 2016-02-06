package sq.command;

import java.util.Arrays;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import radixcore.constant.Font.Color;
import radixcore.constant.Font.Format;
import radixcore.util.RadixLogic;
import sq.entity.creature.EntitySpiderEgg;
import sq.entity.creature.EntitySpiderEx;

/**
 * Defines the commands usable from the console/chat by typing /sq <command name>.
 */
public final class CommandSQ extends CommandBase
{
	@Override
	public String getCommandName() 
	{
		return "sq";
	}

	@Override
	public String getCommandUsage(ICommandSender commandSender) 
	{
		return "/sq <subcommand> <arguments>";
	}

	@Override
	public void processCommand(ICommandSender commandSender, String[] input) 
	{
		try
		{
			final EntityPlayer player = (EntityPlayer)commandSender;
			String subcommand = input[0];
			String[] arguments = Arrays.copyOfRange(input, 1, input.length);

			if (subcommand.equalsIgnoreCase("help"))
			{
				displayHelp(commandSender);
			}
			
			else if (subcommand.equalsIgnoreCase("fsl")) //Force spider level
			{
				for (Entity entity : RadixLogic.getAllEntitiesOfTypeWithinDistance(EntitySpiderEx.class, player, 15))
				{
					EntitySpiderEx spider = (EntitySpiderEx)entity;
					spider.levelUp();
				}
			}
			
			else if (subcommand.equalsIgnoreCase("feg")) //Force egg grow
			{
				for (Entity entity : RadixLogic.getAllEntitiesOfTypeWithinDistance(EntitySpiderEgg.class, player, 15))
				{
					EntitySpiderEgg egg = (EntitySpiderEgg)entity;
					ObfuscationReflectionHelper.setPrivateValue(EntitySpiderEgg.class, egg, 0, 1);
				}
			}
		}
		
		catch (Exception e)
		{
			commandSender.addChatMessage(new ChatComponentText("An invalid argument was provided. Usage: " + getCommandUsage(commandSender)));
		}
	}

	@Override
	public int getRequiredPermissionLevel() 
	{
		return 0;
	}

	private void addChatMessage(ICommandSender commandSender, String message)
	{
		commandSender.addChatMessage(new ChatComponentText(Color.GOLD + "[SQ] " + Format.RESET + message));
	}

	private void addChatMessage(ICommandSender commandSender, String message, boolean noPrefix)
	{
		if (noPrefix)
		{
			commandSender.addChatMessage(new ChatComponentText(message));			
		}

		else
		{
			addChatMessage(commandSender, message);
		}
	}

	private void displayHelp(ICommandSender commandSender)
	{
		addChatMessage(commandSender, Color.DARKRED + "--- " + Color.GOLD + "DEBUG COMMANDS" + Color.DARKRED + " ---", true);
		addChatMessage(commandSender, Color.WHITE + " /sq fsl " + Color.GOLD + " - Forces all nearby spiders to level up.", true);

		addChatMessage(commandSender, Color.DARKRED + "--- " + Color.GOLD + "OP COMMANDS" + Color.DARKRED + " ---", true);
		addChatMessage(commandSender, Color.WHITE + " /sq [cmd] " + Color.GOLD + " - [desc].", true);

		addChatMessage(commandSender, Color.DARKRED + "--- " + Color.GOLD + "GLOBAL COMMANDS" + Color.DARKRED + " ---", true);
		addChatMessage(commandSender, Color.WHITE + " /sq help " + Color.GOLD + " - Shows this list of commands.", true);
	}
}
