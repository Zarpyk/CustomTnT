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
                image = ImageIO.read(new URL("https://minotar.net/helm/notch/256.png"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return new ByteArrayInputStream("Error".getBytes());
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
        if (Config.getBool(Config.Options.SkinsRestorerEnable)) {
            skin = SkinsRestorer.getInstance().getSkinStorage().getPlayerSkin(playerN);
            Main.debug(skin);
            BufferedImage image;
            if (skin == null) {
                skin = playerN;
                image = getMojangAPIAvatar(player, skin);
            } else {
                /*if (!skin.equals(player.getName())) {
                    for (OfflinePlayer player2 : Bukkit.getOfflinePlayers()) {
                        if (skin.equals(player2.getName())) {
                            getPlayerImage(Objects.requireNonNull(player2.getPlayer()));
                            break;
                        }
                    }
                }*/
                File skinFile = new File("plugins/SkinsRestorer/Skins", Objects.requireNonNullElse(skin, playerN)
                        .toLowerCase() + ".skin");
                Main.debug(skinFile.toString());
                if (!skinFile.exists()) {
                    skin = playerN;
                    image = getMojangAPIAvatar(player, skin);
                } else {
                    String base64 = Files.readAllLines(skinFile.toPath()).get(0);
                    byte[] decodedBytes = Base64.getDecoder().decode(base64);
                    String decodedString = new String(decodedBytes);
                    JSONObject jsonObject = new JSONObject(decodedString);
                    URL url = new URL(jsonObject.getJSONObject("textures").getJSONObject("SKIN").getString("url"));
                    Main.debug(url.toString());
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
            errorImage = ImageIO.read(new URL("https://minotar.net/helm/" + Objects.requireNonNullElse(skin, name)
                    .replaceAll("\\s", "") + "/256.png"));
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
        if (UUIDJson.isEmpty()) {
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
