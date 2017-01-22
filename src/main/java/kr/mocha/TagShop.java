package kr.mocha;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.plugin.PluginBase;
import kr.mocha.manager.TagManager;
import me.onebone.economyapi.EconomyAPI;

/**
 * Created by user on 17. 1. 22.
 */
public class TagShop extends PluginBase implements Listener {
    public EconomyAPI api;

    @Override
    public void onEnable() {
        api = EconomyAPI.getInstance();
        this.getServer().getPluginManager().registerEvents(this, this);
        super.onEnable();
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        String line1 = event.getLine(0);
        String line2 = event.getLine(1);
        String line3 = event.getLine(2);
        if(line1.equals("tagshop") || line1.equals("§b[ TagShop ]")) {
            if(player.hasPermission("tag.shop.per")) {
                if(!(line2==null || line3==null)) {
                    if(isNum(line3)) {
                        event.setLine(0, "§b[ TagShop ]");
                        event.setLine(1, line2);
                        event.setLine(2, "§eprice : "+line3);
                        player.sendMessage("§eSuccessful made tag shop!");
                    }else player.sendMessage("§Price is not number?");
                }else player.sendMessage("§cline 2 : tag, line 3 : price");
            } else player.sendMessage("§cYou can't make tag shop");
        }
        return;
    }

    @EventHandler
    public void onTouch(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(block.getId() == Block.SIGN_POST || block.getId() == Block.WALL_SIGN) {
            BlockEntitySign sign = (BlockEntitySign) player.getLevel().getBlockEntity(block.getLocation());
            if(sign.getText()[0].equals("§b[ TagShop ]")) {
                String tag = sign.getText()[1];
                int price = Integer.parseInt(sign.getText()[2].replace("§eprice : ", ""));
                TagManager manager = new TagManager(player);

                if(api.myMoney(player) >= price) {
                    manager.add(tag);
                    api.reduceMoney(player, price);
                    player.sendMessage("§eBuy this tag!");
                }else
                    player.sendMessage("§clack money!");
            }
        }

    }

    public boolean isNum(String s) {
        try{Integer.parseInt(s); return true;}
        catch (Exception e) { return false; }

    }

}
