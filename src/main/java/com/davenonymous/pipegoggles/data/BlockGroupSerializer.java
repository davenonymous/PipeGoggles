package com.davenonymous.pipegoggles.data;


import com.davenonymous.libnonymous.helper.BlockStateSerializationHelper;
import com.davenonymous.pipegoggles.PipeGoggles;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockGroupSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<BlockGroup> {
    public BlockGroupSerializer() {
    }

    private Marker mark = MarkerManager.getMarker("Serializer");

    @Override
    public BlockGroup fromJson(ResourceLocation recipeId, JsonObject json) {
        // Get item used as icon to identify the group quicker
        if(!json.has("itemIcon")) {
            PipeGoggles.LOGGER.info(mark, "Missing 'itemIcon' property in block group definition");
            return null;
        }
        Ingredient itemIcon = Ingredient.fromJson(json.getAsJsonObject("itemIcon"));

        // Get label used for this block group
        if(!json.has("translationKey")) {
            PipeGoggles.LOGGER.info(mark, "Missing 'translationKey' in block group definition");
            return null;
        }
        String translationKey = json.get("translationKey").getAsString();

        // Get blocks belonging to this block group
        if(!json.has("blocks")) {
            PipeGoggles.LOGGER.info(mark, "Missing 'blocks' array in block group definition");
            return null;
        }

        if(!json.get("blocks").isJsonArray()) {
            PipeGoggles.LOGGER.info(mark, "Property 'blocks' in block group definition is no array");
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
                PipeGoggles.LOGGER.info(mark, "Invalid value for optimizationStrategy property.");
                return null;
            }
        }

        BlockGroup result = new BlockGroup(recipeId);
        result.setOptimizationStrategy(optimizationStrategy);
        result.setItemIcon(itemIcon);
        result.setTranslationKey(translationKey);
        result.setValidBlockStates(blocks);

        return result;
    }

    @Nullable
    @Override
    public BlockGroup fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
        BlockGroup result = new BlockGroup(recipeId);

        final Ingredient ingredient = Ingredient.fromNetwork(buffer);
        result.setItemIcon(ingredient);

        String translationKey = buffer.readUtf();
        result.setTranslationKey(translationKey);

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
    public void toNetwork(FriendlyByteBuf buffer, BlockGroup bg) {
        bg.getItemIcon().toNetwork(buffer);
        buffer.writeUtf(bg.getTranslationKey());
        buffer.writeInt(bg.getOptimizationStrategy().ordinal());
        buffer.writeInt(bg.getValidStates().size());
        for(BlockState state : bg.getValidStates()) {
            BlockStateSerializationHelper.serializeBlockState(buffer, state);
        }

    }
}