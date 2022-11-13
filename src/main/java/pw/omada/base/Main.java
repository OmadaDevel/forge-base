package pw.omada.base;

import com.mojang.realmsclient.gui.ChatFormatting;
import pw.omada.base.command.CommandManager;
import pw.omada.base.module.Module;
import pw.omada.base.util.misc.FileUtils;
import pw.omada.base.util.render.JColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

@Mod(modid = Main.MOD_ID, name = Main.NAME, version = Main.VERSION)
public class Main {

    public static ModuleManager moduleManager;

    public static Hud hud;
    public static KeyBinding ClickGUI;
    public static CommandManager commandManager;

    public static final String MOD_ID = "base";
    public static final String NAME = "Base";

    public static Minecraft mc = Minecraft.getMinecraft();

    public static final JColor CLIENT_COLOR = new JColor(255, 255, 255);
    
    @Mod.Instance
    public Main instance;


    @Mod.EventHandler
    public void PreInit(FMLPreInitializationEvent event) {
    }

    public void initFilesystem() {
        FileUtils.createDirectory();
        FileUtils.loadAll();
    }

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(instance);
        MinecraftForge.EVENT_BUS.register(new Hud());

        Display.setTitle(Main.NAME + " " + Main.VERSION);

        moduleManager = new ModuleManager();
        commandManager = new CommandManager();

        this.initFilesystem();
    }

    @Mod.EventHandler
    public void post(FMLPostInitializationEvent event) {

    }


    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent e) {
        if(Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null)
            return;
        try {
            if (Keyboard.isCreated()) {
                if (Keyboard.getEventKeyState()) {
                    int keyCode = Keyboard.getEventKey();
                    if (keyCode <= 0)
                        return;
                    for (Module m : moduleManager.modules) {
                        if (m.getKey() == keyCode && keyCode > 0) {
                            m.toggle();
                        }
                    }
                }
            }
        } catch (Exception ex) {ex.printStackTrace();}
    }

    public static void sendMessage(String msg) {

        if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null) return;

        Minecraft.getMinecraft().player.sendMessage(new TextComponentString( ChatFormatting.RESET + "[" + Main.NAME + "] " + msg));
    }

    @SubscribeEvent
    public void displayGuiScreen(TickEvent.ClientTickEvent event) {
        if (Main.ClickGUI.isPressed()) {
            mc.displayGuiScreen(new ClickGui());
        }
    }
}
