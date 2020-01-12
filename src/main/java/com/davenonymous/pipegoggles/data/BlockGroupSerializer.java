package com.davenonymous.pipegoggles.data;

import com.davenonymous.libnonymous.utils.BlockStateSerializationHelper;
import com.davenonymous.pipegoggles.PipeGoggles;
import com.davenonymous.pipegoggles.util.Logz;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.block.BlockState;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockGroupSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BlockGroup> {
    public BlockGroupSerializer() {
        this.setRegistryName(new ResourceLocation(PipeGoggles.MODID, "blockgroup"));
    }

    private Marker mark = MarkerManager.getMarker("Serializer");

    @Override
    public BlockGroup read(ResourceLocation recipeId, JsonObject json) {

        // Get and check if a mod is required and available
        if(!json.has("mod")) {
            Logz.info(mark, "Missing 'mod' in block group definition");
            return null;
        }

        String requiredMod = json.get("mod").getAsString();
        if(requiredMod.length() <= 0) {
            Logz.info(mark, "Value for 'mod' in block group definition is empty");
            return null;
        }

        if(!ModList.get().isLoaded(requiredMod)) {
            Logz.info(mark, "Mod '"+requiredMod+"' for block group '"+recipeId+"' is not loaded. Skipping integration!");
            return null;
        }

        // Get item used as icon to identify the group quicker
        if(!json.has("itemIcon")) {
            Logz.info(mark, "Missing 'itemIcon' property in block group definition");
            return null;
        }
        Ingredient itemIcon = Ingredient.deserialize(json.getAsJsonObject("itemIcon"));

        // Get label used for this block group
        if(!json.has("translationKey")) {
            Logz.info(mark, "Missing 'translationKey' in block group definition");
            return null;
        }
        String translationKey = json.get("translationKey").getAsString();

        // Get blocks belonging to this block group
        if(!json.has("blocks")) {
            Logz.info(mark, "Missing 'blocks' array in block group definition");
            return null;
        }

        if(!json.get("blocks").isJsonArray()) {
            Logz.info(mark, "Property 'blocks' in block group definition is no array");
            return null;
        }

        List<BlockState> blocks = new ArrayList<>();
        for(JsonElement element : json.getAsJsonArray("blocks")) {
            if(!element.isJsonObject()) {
                continue;
            }

            BlockState state = BlockStateSerializationHelper.deserializeBlockState(element.getAsJsonObject());
            if(state == null) {
                continue;
            }

            blocks.add(state);
        }

        // Get optional optimization-strategy
        EnumBoxOptimizationStrategy optimizationStrategy = EnumBoxOptimizationStrategy.REMOVE_DUPLICATE_LINES;
        if(json.has("optimizationStrategy")) {
            try {
                optimizationStrategy = EnumBoxOptimizationStrategy.valueOf(json.get("optimizationStrategy").getAsString());
            } catch(IllegalArgumentException e) {
                Logz.info(mark, "Invalid value for optimizationStrategy property.");
                return null;
            }
        }

        BlockGroup result = new BlockGroup(recipeId);
        result.setOptimizationStrategy(optimizationStrategy);
        result.setItemIcon(itemIcon);
        result.setTranslationKey(translationKey);
        result.setValidBlockStates(blocks);
        result.setModId(requiredMod);

        return result;
    }

    @Nullable
    @Override
    public BlockGroup read(ResourceLocation recipeId, PacketBuffer buffer) {
        BlockGroup result = new BlockGroup(recipeId);

        final Ingredient ingredient = Ingredient.read(buffer);
        result.setItemIcon(ingredient);

        String translationKey = buffer.readString();
        result.setTranslationKey(translationKey);

        String modId = buffer.readString();
        result.setModId(modId);

        EnumBoxOptimizationStrategy strategy = EnumBoxOptimizationStrategy.values()[buffer.readInt()];
        result.setOptimizationStrategy(strategy);

        List<BlockState> validBlockStates = new ArrayList<>();
        int validStates = buffer.readInt();
        for(int stateNum = 0; stateNum < validStates; stateNum++) {
            validBlockStates.add(BlockStateSerializationHelper.deserializeBlockState(buffer));
        }

        result.setValidBlockStates(validBlockStates);

        return result;
    }

    @Override
    public void write(PacketBuffer buffer, BlockGroup bg) {
        bg.getItemIcon().write(buffer);
        buffer.writeString(bg.getTranslationKey());
        buffer.writeString(bg.getModId());
        buffer.writeInt(bg.getOptimizationStrategy().ordinal());
        buffer.writeInt(bg.getValidStates().size());
        for(BlockState state : bg.getValidStates()) {
            BlockStateSerializationHelper.serializeBlockState(buffer, state);
        }

    }
}
