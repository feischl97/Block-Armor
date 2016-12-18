package twopiradians.blockArmor.client.gui.armorDisplay;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twopiradians.blockArmor.common.BlockArmor;
import twopiradians.blockArmor.common.item.ArmorSet;
import twopiradians.blockArmor.common.item.ItemBlockArmor;
import twopiradians.blockArmor.common.item.ModItems;

@SuppressWarnings({"deprecation", "unused"})
@SideOnly(Side.CLIENT)
public class GuiArmorDisplay extends GuiScreen
{
	private final ResourceLocation backgroundPageTexture0 = new ResourceLocation(BlockArmor.MODID+":textures/gui/armor_display_background0.jpg");
	private final ResourceLocation backgroundPageTexture2 = new ResourceLocation(BlockArmor.MODID+":textures/gui/armor_display_background2.png");
	private EntityGuiPlayer guiPlayer;
	private float partialTicks;
	/**List of all armors with set effects*/
	private ArrayList<ItemBlockArmor> armors;

	public GuiArmorDisplay() {
		guiPlayer = new EntityGuiPlayer(Minecraft.getMinecraft().theWorld, Minecraft.getMinecraft().thePlayer.getGameProfile(), Minecraft.getMinecraft().thePlayer);
		//initialize armors with all armor that has a set effect
		armors = new ArrayList<ItemBlockArmor>();
		for (ArmorSet set : ArmorSet.allSets)
			if ((BlockArmor.GUI_MODE == 0 && !set.isFromModdedBlock) ||
					(BlockArmor.GUI_MODE == 1/* && set.isFromModdedBlock*/) ||
					(BlockArmor.GUI_MODE == 2 && set.hasSetEffect)) {
				armors.add(set.helmet);
				armors.add(set.chestplate);
				armors.add(set.leggings);
				armors.add(set.boots);
			}
		guiPlayer.setInvisible(BlockArmor.GUI_MODE == 0 || BlockArmor.GUI_MODE == 1);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//background
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		if (BlockArmor.GUI_MODE == 0 || BlockArmor.GUI_MODE == 1)
			mc.getTextureManager().bindTexture(backgroundPageTexture0);
		else if (BlockArmor.GUI_MODE == 2)
			mc.getTextureManager().bindTexture(backgroundPageTexture2);
		GlStateManager.pushMatrix();
		float scale = 1f;
		if (BlockArmor.GUI_MODE == 0)
			GlStateManager.scale(1.47f, 1.445f, scale);
		else if (BlockArmor.GUI_MODE == 1)
			GlStateManager.scale(1.47f, 1.445f, scale);
		else if (BlockArmor.GUI_MODE == 1)
			GlStateManager.scale(2.5f, 1.31f, scale);
		this.drawTexturedModalRect(0, 0, 0, 0, this.width, this.height);
		GlStateManager.popMatrix();

		//iterate through each set of armor
		for (int index=0; index<armors.size(); index+=4) {
			//equip gui player
			guiPlayer.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(armors.get(index)));
			guiPlayer.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(armors.get(index+1)));
			guiPlayer.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(armors.get(index+2)));
			guiPlayer.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(armors.get(index+3)));
			//draw gui player
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.pushMatrix();
			double spaceBetween = 0;
			if (BlockArmor.GUI_MODE == 0) {
				scale = 29f;
				double heightBetween = 44.3d;
				int perRow = 17;
				int row = index/4 / perRow;
				spaceBetween = 21.8d + (row==7?1.3d:0);
				GlStateManager.translate(-160+(index/4 % perRow)*spaceBetween, row*heightBetween, row);
			}
			else if (BlockArmor.GUI_MODE == 1) {
				scale = 29f;
				double heightBetween = 44.3d;
				int perRow = 17;
				int row = index/4 / perRow;
				spaceBetween = 21.8d + (row==7?1.3d:0);
				GlStateManager.translate(-160+(index/4 % perRow)*spaceBetween, row*heightBetween, row);
			}
			else if (BlockArmor.GUI_MODE == 2) {
				scale = 50f;
				spaceBetween = 22.5d;
				if (index/4 < 7)
					GlStateManager.translate(-250+index*spaceBetween, 5, 0);
				else
					GlStateManager.translate(-915+index*(spaceBetween+2), 175, 0);
			}
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.rotate(180F, 0F, 0F, 1F);
			GlStateManager.rotate(135.0F, 0.0F, 1, 0.0f);
			RenderHelper.enableStandardItemLighting();
			GlStateManager.rotate(-165.0F, 0.0F, 1, -0.0f);
			GlStateManager.rotate(-10.0F, -1F, 0F, 0.5f);
			guiPlayer.rotationYawHead = 0.0F;
			guiPlayer.renderYawOffset = 0.0F;
			this.partialTicks += 0.05F;
			mc.getRenderManager().setPlayerViewY(-20f);
			mc.getRenderManager().doRenderEntity(guiPlayer, -4D, -1.5D, 5.0D, 0.0F, this.partialTicks, true);
			RenderHelper.disableStandardItemLighting();
			this.mc.entityRenderer.disableLightmap();
			GlStateManager.popMatrix();
			//render tooltip
			if (BlockArmor.GUI_MODE == 2) {
				ItemStack stack = new ItemStack(armors.get(index));
				GlStateManager.pushMatrix();
				scale = 0.65f;
				if (index/4 < 7)
					GlStateManager.translate(40+index*spaceBetween, 120, 0);
				else
					GlStateManager.translate(-625+index*(spaceBetween+2), 290, 0);
				GlStateManager.scale(scale, scale, scale);
				int length = 0;
				ArrayList<String> tooltip = new ArrayList<String>();
				tooltip.add(TextFormatting.AQUA+""+TextFormatting.UNDERLINE+stack.getDisplayName().replace("Helmet", "Armor"));
				armors.get(index).addFullSetEffectTooltip(tooltip);
				this.addStatTooltips(tooltip, index, guiPlayer);
				for (String string : tooltip)
					if (this.fontRendererObj.getStringWidth(string) > length)
						length = this.fontRendererObj.getStringWidth(string);
				this.drawHoveringText(tooltip, -length/2, 0);
				GlStateManager.popMatrix();
				RenderHelper.disableStandardItemLighting();
			}
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	//partially copied from ItemStack.getTooltip()
	private ArrayList<String> addStatTooltips(ArrayList<String> tooltip, int index, EntityPlayer player) {
		ArrayList<Multimap<String, AttributeModifier>> list = new ArrayList<Multimap<String, AttributeModifier>>();
		list.add(new ItemStack(armors.get(index)).getAttributeModifiers(EntityEquipmentSlot.HEAD));
		list.add(new ItemStack(armors.get(index+1)).getAttributeModifiers(EntityEquipmentSlot.CHEST));
		list.add(new ItemStack(armors.get(index+2)).getAttributeModifiers(EntityEquipmentSlot.LEGS));
		list.add(new ItemStack(armors.get(index+3)).getAttributeModifiers(EntityEquipmentSlot.FEET));

		boolean flag = false;
		ArrayList<Double> finalD0s = new ArrayList<Double>();
		ArrayList<Double> finalD1s = new ArrayList<Double>();
		ArrayList<Entry<String, AttributeModifier>> entries = new ArrayList<Entry<String, AttributeModifier>>();
		for (Multimap<String, AttributeModifier> multimap : list) {
			if (!multimap.isEmpty())
			{
				for (Entry<String, AttributeModifier> entry : multimap.entries())
				{
					AttributeModifier attributemodifier = (AttributeModifier)entry.getValue();
					double d0 = attributemodifier.getAmount();
					flag = false;
					if (attributemodifier.getID() == ItemBlockArmor.ATTACK_STRENGTH_UUID)
					{
						d0 = d0 + player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
						d0 = d0 + (double)EnchantmentHelper.getModifierForCreature(new ItemStack(armors.get(index)), EnumCreatureAttribute.UNDEFINED);
						flag = true;
					}
					else if (attributemodifier.getID() == ItemBlockArmor.ATTACK_SPEED_UUID)
					{
						d0 += player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getBaseValue();
						flag = true;
					}
					double d1;
					if (attributemodifier.getOperation() != 1 && attributemodifier.getOperation() != 2)
						d1 = d0;
					else
						d1 = d0 * 100.0D;

					boolean contains = false;
					int index2 = 0;
					for (int i=0; i<entries.size(); i++) 
						if (entries.get(i).getValue().getName() == attributemodifier.getName()) {
							contains = true;
							index2 = i;
						}
					if (!contains) {
						entries.add(entry);
						finalD0s.add(d0);
						finalD1s.add(d1);
					}
					else if (entries.get(index2).getValue().getName().equalsIgnoreCase("Armor toughness") || 
							entries.get(index2).getValue().getName().equalsIgnoreCase("Armor modifier")){
						finalD0s.set(index2, finalD0s.get(index2)+d0);
						finalD1s.set(index2, finalD1s.get(index2)+d1);
					}
				}
			}
		}
		for (int i=0; i<entries.size(); i++) {
			AttributeModifier attributemodifier = (AttributeModifier)entries.get(i).getValue();
			if (flag)
				tooltip.add(TextFormatting.BLUE + " +" + I18n.translateToLocalFormatted("attribute.modifier.equals." + attributemodifier.getOperation(), new Object[] {ItemStack.DECIMALFORMAT.format(finalD1s.get(i)), I18n.translateToLocal("attribute.name." + (String)entries.get(i).getKey())}));
			else if (finalD0s.get(i) > 0.0D)
				tooltip.add(TextFormatting.BLUE + " " + I18n.translateToLocalFormatted("attribute.modifier.plus." + attributemodifier.getOperation(), new Object[] {ItemStack.DECIMALFORMAT.format(finalD1s.get(i)), I18n.translateToLocal("attribute.name." + (String)entries.get(i).getKey())}));
			else if (finalD0s.get(i) < 0.0D)
			{
				finalD1s.set(i, finalD1s.get(i) * -1.0D);
				tooltip.add(TextFormatting.RED + " " + I18n.translateToLocalFormatted("attribute.modifier.take." + attributemodifier.getOperation(), new Object[] {ItemStack.DECIMALFORMAT.format(finalD1s.get(i)), I18n.translateToLocal("attribute.name." + (String)entries.get(i).getKey())}));
			}
		}
		return tooltip;
	}
}
