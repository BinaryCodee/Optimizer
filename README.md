# Optimizer

## A comprehensive & simple performance optimization plugin for Minecraft servers

Optimizer is a powerful plugin designed to improve your Minecraft server's performance through intelligent resource management and automatic optimization routines.

## Features

- **Automatic Optimization**: Schedule periodic optimization tasks to keep your server running smoothly
- **Manual Optimization**: Run optimizations on-demand when you need an immediate performance boost
- **TPS Optimization**: Maintain higher server TPS (Ticks Per Second) by managing entities and reducing lag
- **Memory Management**: Smart RAM usage monitoring and garbage collection
- **CPU Usage Optimization**: Dynamically adjust entity AI processing based on server load
- **Disk Space Management**: Clean up old log files to prevent disk space issues
- **Chunk Management**: Control the number of loaded chunks to prevent memory overuse
- **WorldEdit Integration**: Limit WorldEdit operations to prevent server crashes during large edits

## Commands

| Command | Description |
|---------|-------------|
| `/optimize help` | Show help message with all available commands |
| `/optimize starttask` | Start the automatic optimization task |
| `/optimize stoptask` | Stop the automatic optimization task |
| `/optimize manualtask` | Run a manual optimization immediately |
| `/optimize info` | Display detailed server optimization status |

## Server Status Information

The `/optimize info` command provides comprehensive information about your server's current status:

- Task status (running/stopped) and interval
- Current TPS with color-coded indicators
- Memory usage statistics
- Loaded chunks per world
- Entity counts (total, mobs, items)
- WorldEdit configuration
- Active optimization modules

## Configuration

Edit the `config.yml` file to customize Optimizer's behavior:

```yaml
auto-task:
  enabled: true
  interval-seconds: 300
optimization:
  max-chunks-per-world: 1024
  tps: true
  ram: true
  cpu: true
  disk: true
  chunk-loading: true
worldedit:
  limit-operations: true
  max-blocks: 1000000
```

## Installation

1. Download the latest Optimizer.jar from [GitHub](https://github.com/binarycodee/optimizer/releases)
2. Place the jar file in your server's `plugins` folder
3. Restart your server or use `/reload` command
4. The plugin will generate a default configuration file
5. Edit the configuration to match your server's needs (optional)

## Requirements

- Bukkit/Spigot/Paper server (1.8 or higher)
- Java 8 or higher
- Optional: WorldEdit plugin for WorldEdit optimization features

## Performance Impact

Optimizer is designed to be lightweight and efficient. The plugin itself consumes minimal resources while providing significant performance improvements through its optimization routines.

## FAQ

**Q: How often should I run optimizations?**  
A: The default is every 5 minutes (300 seconds), but you can adjust this based on your server's needs. Busy servers may benefit from more frequent optimizations.

**Q: Will this plugin conflict with other performance plugins?**  
A: Optimizer is designed to work alongside other plugins. However, you may want to disable overlapping features if you're using multiple performance plugins.

**Q: Can this plugin fix all lag issues?**  
A: While Optimizer significantly reduces common lag sources, some issues may require server hardware upgrades or additional configuration changes.

**Q: Is it safe to use the plugin on a production server?**  
A: Yes, Optimizer has been designed with safety in mind and performs optimizations without risking data corruption or server crashes.

## Support
- **Discord**: [Join our Discord](https://discord.gg/BsYQhyv99j)
If you encounter any issues or have suggestions for improvements, please create an issue on our [GitHub repository](https://github.com/binarycodee/optimizer/issues).

## License

Optimizer is licensed under the Apache 2.0 License - see the LICENSE file for details.

## Credits

- Developed by blacked104
- Special thanks to LivingPvP italian minecraft server for feedback and testing
