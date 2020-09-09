package guerrero61.customtnt.mainutils;

import guerrero61.customtnt.Main;
import guerrero61.customtnt.mainutils.config.Config;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.IOUtils;
import org.bukkit.entity.Player;
import org.json.JSONObject;
import skinsrestorer.bukkit.SkinsRestorer;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Objects;

public class Avatar {
    public static InputStream getPlayerAvatar(Player player) {
        try {
            return getPlayerImage(player);
        } catch (Exception e) {
            e.printStackTrace();
            Image image;
            try {
                image = getMojangAPIAvatar(player, player.getName());
            } catch (Exception e2) {
                try {
                    image = ImageIO.read(new URL("https://imgur.com/3QZOSvJ.png"));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    return new ByteArrayInputStream("Error".getBytes());
                }
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                ImageIO.write((RenderedImage) image, "png", os);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return new ByteArrayInputStream("Error".getBytes());
            }
            return new ByteArrayInputStream(os.toByteArray());
        }
    }

    private static InputStream getPlayerImage(Player player) throws IOException {
        String playerN = player.getName();
        String skin;
        if(Config.getBool(Config.Options.SkinsRestorerEnable)) {
            if(Config.getBool(Config.Options.SkinsRestorerMySQLEnable)) {
                try {
                    PreparedStatement statement = Main.getPlugin().getSkinRestorerConnection().prepareStatement(
                            "SELECT * FROM " + Config.getString(Config.Options.SkinsRestorerMySQLPlayerTable) +
                            " WHERE Nick=?");
                    statement.setString(1, playerN.toLowerCase());
                    ResultSet resultSet = statement.executeQuery();
                    if(resultSet.next()) {
                        skin = resultSet.getString("Skin");
                    } else {
                        skin = playerN;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    skin = SkinsRestorer.getInstance().getSkinStorage().getPlayerSkin(playerN);
                }
            } else {
                skin = SkinsRestorer.getInstance().getSkinStorage().getPlayerSkin(playerN);
            }
            //Main.debug(skin);
            BufferedImage image = null;
            if(skin == null) {
                skin = playerN;
                image = getMojangAPIAvatar(player, skin);
            } else {
                String base64Text = "";
                boolean getSkinFile = true;
                boolean getImage = true;
                if(Config.getBool(Config.Options.SkinsRestorerMySQLEnable)) {
                    try {
                        PreparedStatement statement = Main.getPlugin().getSkinRestorerConnection().prepareStatement(
                                "SELECT * FROM " + Config.getString(Config.Options.SkinsRestorerMySQLSkinTable) +
                                " WHERE Nick=?");
                        statement.setString(1, skin.toLowerCase());
                        ResultSet resultSet = statement.executeQuery();
                        if(resultSet.next()) {
                            base64Text = resultSet.getString("Value");
                            getSkinFile = false;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        skin = SkinsRestorer.getInstance().getSkinStorage().getPlayerSkin(playerN);
                    }
                }
                if(getSkinFile) {
                    File skinFile = new File("plugins/SkinsRestorer/Skins",
                            Objects.requireNonNullElse(skin, playerN).toLowerCase() + ".skin");
                    //Main.debug(skinFile.toString());
                    if(!skinFile.exists()) {
                        skin = playerN;
                        image = getMojangAPIAvatar(player, skin);
                        getImage = false;
                    } else {
                        base64Text = Files.readAllLines(skinFile.toPath()).get(0);
                    }
                }
                if(getImage) {
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Text);
                    String decodedString = new String(decodedBytes);
                    JSONObject jsonObject = new JSONObject(decodedString);
                    URL url = new URL(jsonObject.getJSONObject("textures").getJSONObject("SKIN").getString("url"));
                    //Main.debug(url.toString());
                    image = ImageIO.read(url);
                    BufferedImage newImage = image.getSubimage(8, 8, 8, 8);
                    BufferedImage newImage2 = image.getSubimage(40, 8, 8, 8);
                    int w = Math.max(newImage.getWidth(), newImage2.getWidth());
                    int h = Math.max(newImage.getHeight(), newImage2.getHeight());
                    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                    Graphics g = combined.getGraphics();
                    g.drawImage(newImage, 0, 0, null);
                    g.drawImage(newImage2, 0, 0, null);
                    g.dispose();
                    image = Main.scale(combined, 256, 256);
                }
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(Objects.requireNonNull(image), "png", os);
            return new ByteArrayInputStream(os.toByteArray());
        } else {
            skin = playerN;
            BufferedImage image;
            image = getMojangAPIAvatar(player, skin);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(Objects.requireNonNull(image), "png", os);
            return new ByteArrayInputStream(os.toByteArray());
        }
    }

    private static BufferedImage getMojangAPIAvatar(Player player, @Nullable String skin) {
        String name = player.getName();
        String UUIDUrl = "https://api.mojang.com/users/profiles/minecraft/" + name;
        String UUIDJson;
        BufferedImage errorImage = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
        try {
            errorImage = ImageIO.read(
                    new URL("https://minotar.net/helm/" + Objects.requireNonNullElse(skin, name).replaceAll("\\s", "") +
                            "/256.png"));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        try {
            //noinspection deprecation
            UUIDJson = IOUtils.toString(new URL(UUIDUrl));
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return errorImage;
        }
        if(UUIDJson.isEmpty()) {
            return errorImage;
        }
        JSONObject UUIDJsonObject = new JSONObject(UUIDJson);
        String userUUID = UUIDJsonObject.getString("id");
        String base64Value;
        try {
            String skinURL = "https://sessionserver.mojang.com/session/minecraft/profile/" + userUUID;
            //noinspection deprecation
            String skinJSON = IOUtils.toString(new URL(skinURL));
            JSONObject skinJsonObject = new JSONObject(skinJSON);
            base64Value = ((JSONObject) skinJsonObject.getJSONArray("properties").get(0)).getString("value");
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return errorImage;
        }
        byte[] decodedBytes = Base64.getDecoder().decode(base64Value);
        String decodedString = new String(decodedBytes);
        JSONObject jsonObject = new JSONObject(decodedString);
        URL url;
        BufferedImage image;
        try {
            url = new URL(jsonObject.getJSONObject("textures").getJSONObject("SKIN").getString("url"));
            image = ImageIO.read(url);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return errorImage;
        }
        BufferedImage newImage = image.getSubimage(8, 8, 8, 8);
        BufferedImage newImage2 = image.getSubimage(40, 8, 8, 8);
        int w = Math.max(newImage.getWidth(), newImage2.getWidth());
        int h = Math.max(newImage.getHeight(), newImage2.getHeight());
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = combined.getGraphics();
        g.drawImage(newImage, 0, 0, null);
        g.drawImage(newImage2, 0, 0, null);
        g.dispose();
        return Main.scale(combined, 256, 256);
    }
}
