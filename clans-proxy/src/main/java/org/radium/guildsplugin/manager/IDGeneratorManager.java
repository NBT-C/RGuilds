package org.radium.guildsplugin.manager;

import org.radium.guildsplugin.Core;
import org.radium.guildsplugin.manager.object.guild.Guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class IDGeneratorManager {
    private static int id = 0;
    public static int getNextId(){
        if (id == 0){
            id = 1;
        }
        return id++;
    }
    public static void loadHighestIdFromDatabase(){
        try {
            try (Connection connection = Core.getInstance().getDataManager().getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT MAX(ID) FROM " + Guild.TABLE_NAME);
                 ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    id = resultSet.getInt(1);
                }
            }
        }catch (Exception exception){exception.printStackTrace();}
    }
}
