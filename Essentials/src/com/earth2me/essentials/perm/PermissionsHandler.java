package com.earth2me.essentials.perm;

import com.earth2me.essentials.Util;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;


public class PermissionsHandler implements IPermissionsHandler
{
	private transient IPermissionsHandler handler = new NullPermissionsHandler();
	private transient String defaultGroup = "default";
	private final transient Plugin plugin;
	private final static Logger LOGGER = Logger.getLogger("Minecraft");
	//private transient boolean useSuperperms = false;

	public PermissionsHandler(final Plugin plugin)
	{
		this.plugin = plugin;
	}

	/*public PermissionsHandler(final Plugin plugin, final boolean useSuperperms)
	{
		this.plugin = plugin;
		this.useSuperperms = useSuperperms;
	}*/

	public PermissionsHandler(final Plugin plugin, final String defaultGroup)
	{
		this.plugin = plugin;
		this.defaultGroup = defaultGroup;
	}

	@Override
	public String getGroup(final Player base)
	{
		String group = handler.getGroup(base);
		if (group == null)
		{
			group = defaultGroup;
		}
		return group;
	}

	@Override
	public List<String> getGroups(final Player base)
	{
		List<String> groups = handler.getGroups(base);
		if (groups == null || groups.isEmpty())
		{
			groups = Collections.singletonList(defaultGroup);
		}
		return Collections.unmodifiableList(groups);
	}

	@Override
	public boolean canBuild(final Player base, final String group)
	{
		return handler.canBuild(base, group);
	}

	@Override
	public boolean inGroup(final Player base, final String group)
	{
		return handler.inGroup(base, group);
	}

	@Override
	public boolean hasPermission(final Player base, final String node)
	{
		return handler.hasPermission(base, node);
	}

	@Override
	public String getPrefix(final Player base)
	{
		String prefix = handler.getPrefix(base);
		if (prefix == null)
		{
			prefix = "";
		}
		return prefix;
	}

	@Override
	public String getSuffix(final Player base)
	{
		String suffix = handler.getSuffix(base);
		if (suffix == null)
		{
			suffix = "";
		}
		return suffix;
	}

	@Override
	public void checkPermissions()
	{
		final PluginManager pluginManager = plugin.getServer().getPluginManager();

		final Plugin permExPlugin = pluginManager.getPlugin("PermissionsEx");
		if (permExPlugin != null && permExPlugin.isEnabled())
		{
			if (!(handler instanceof PermissionsExHandler))
			{
				LOGGER.log(Level.INFO, "Essentials: Using PermissionsEx based permissions.");
				handler = new PermissionsExHandler();
			}
			return;
		}

		final Plugin GMplugin = pluginManager.getPlugin("GroupManager");
		if (GMplugin != null && GMplugin.isEnabled())
		{
			if (!(handler instanceof GroupManagerHandler))
			{
				LOGGER.log(Level.INFO, "Essentials: Using GroupManager based permissions.");
				handler = new GroupManagerHandler(GMplugin);
			}
			return;
		}

		final Plugin permBukkitPlugin = pluginManager.getPlugin("PermissionsBukkit");
		if (permBukkitPlugin != null && permBukkitPlugin.isEnabled())
		{
			if (!(handler instanceof PermissionsBukkitHandler))
			{
				LOGGER.log(Level.INFO, "Essentials: Using PermissionsBukkit based permissions.");
				handler = new PermissionsBukkitHandler(permBukkitPlugin);
			}
			return;
		}

		final Plugin privPlugin = pluginManager.getPlugin("Privileges");
		if (privPlugin != null && privPlugin.isEnabled())
		{
			if (!(handler instanceof PrivilegesHandler))
			{
				LOGGER.log(Level.INFO, "Essentials: Using Privileges based permissions.");
				handler = new PrivilegesHandler(privPlugin);
			}
			return;
		}

		final Plugin bPermPlugin = pluginManager.getPlugin("bPermissions");
		if (bPermPlugin != null && bPermPlugin.isEnabled())
		{
			final String bVer = bPermPlugin.getDescription().getVersion().replace(".", "");
			if (Util.isInt(bVer) && Integer.parseInt(bVer) < 284)
			{
				if (!(handler instanceof BPermissionsHandler))
				{
					LOGGER.log(Level.INFO, "Essentials: Using bPermissions based permissions.");
					handler = new BPermissionsHandler();
				}
				return;
			}
			if (!(handler instanceof BPermissions2Handler))
			{
				LOGGER.log(Level.INFO, "Essentials: Using bPermissions2 based permissions.");
				handler = new BPermissions2Handler();
			}
			return;

		}

		//if (useSuperperms)
		//{
			if (!(handler instanceof SuperpermsHandler))
			{
				LOGGER.log(Level.INFO, "Essentials: Using superperms based permissions.");
				handler = new SuperpermsHandler();
			}
		/*}
		else
		{
			if (!(handler instanceof ConfigPermissionsHandler))
			{
				LOGGER.log(Level.INFO, "Essentials: Using config based permissions. Enable superperms in config.");
				handler = new ConfigPermissionsHandler(plugin);
			}
		}*/
	}

	/*public void setUseSuperperms(final boolean useSuperperms)
	{
		this.useSuperperms = useSuperperms;
	}*/
}
