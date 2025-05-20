package it.blacked.optimizer.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OptimizeTabCompleter implements TabCompleter {

    private final List<String> subCommands = Arrays.asList("help", "starttask", "stoptask", "manualtask", "info");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            String partialArg = args[0].toLowerCase();
            completions = subCommands.stream()
                    .filter(cmd -> cmd.startsWith(partialArg))
                    .collect(Collectors.toList());
        }

        return completions;
    }
}