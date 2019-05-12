package org.dave.pipemaster.data.blockgroups;

import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.dave.pipemaster.items.goggles.EnumBoxOptimizationStrategy;
import org.dave.pipemaster.util.Logz;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BlockGroupSerializer implements JsonDeserializer<List<BlockGroup>> {
    @Override
    public List<BlockGroup> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject() && !json.isJsonArray()) {
            throw new JsonParseException("Root structure is neither an object nor an array.");
        }

        List<BlockGroup> result = new ArrayList<>();
        if(json.isJsonArray()) {
            for(JsonElement element : json.getAsJsonArray()) {
                result.add(deserializeBlockGroup(element));
            }
        } else if(json.isJsonObject()) {
            result.add(deserializeBlockGroup(json));
        }

        return result;
    }

    private ItemStack deserializeItemStack(JsonElement element) throws JsonParseException {
        int meta = 0;
        int count = 1;
        String itemName;

        if(element.isJsonObject()) {
            JsonObject data = element.getAsJsonObject();
            if(!data.has("name")) {
                throw new JsonParseException("ItemStack section is missing 'name' property.");
            }
            itemName = data.get("name").getAsString();
            meta = data.has("meta") ? data.get("meta").getAsInt() : 0;
            count = data.has("count") ? data.get("count").getAsInt() : 1;
        } else {
            itemName = element.getAsString();
        }

        ResourceLocation resource = new ResourceLocation(itemName);
        Item item = ForgeRegistries.ITEMS.getValue(resource);

        if(item == null) {
            throw new JsonParseException("Could not find item with name " + itemName);
        }

        ItemStack result = new ItemStack(item, count, meta);
        // TODO: Add ability to deserialize ItemStack NBT data from json
        return result;
    }

    private IBlockState deserializeBlockState(JsonElement element) throws JsonParseException {
        int meta = -1;
        String blockName;
        if(element.isJsonObject()) {
            JsonObject data = element.getAsJsonObject();
            if(!data.has("name")) {
                Logz.warn("Invalid block in blockgroup configuration! Skipping: %s", element);
                return null;
            }
            blockName = data.get("name").getAsString();
            meta = data.has("meta") ? data.get("meta").getAsInt() : -1;
        } else {
            blockName = element.getAsString();
        }

        ResourceLocation resource = new ResourceLocation(blockName);
        Block block = ForgeRegistries.BLOCKS.getValue(resource);

        if(block == null || block == Blocks.AIR) {
            throw new JsonParseException("Could not find block with name: " + blockName);
        }

        if(meta == -1) {
            return block.getDefaultState();
        }

        return block.getStateFromMeta(meta);
    }

    private BlockGroup deserializeBlockGroup(JsonElement root) throws JsonParseException {
        if(!root.isJsonObject()) {
            throw new JsonParseException("Expected BlockGroup element, but found no object");
        }

        JsonObject rootObj = root.getAsJsonObject();


        // Get ID
        if(!rootObj.has("id")) {
            throw new JsonParseException("Missing 'id' in block group definition");
        }
        String groupId = rootObj.get("id").getAsString();

        // Get and check if a mod is required and available
        if(!rootObj.has("mod")) {
            throw new JsonParseException("Missing 'mod' in block group definition");
        }

        String requiredMod = rootObj.get("mod").getAsString();
        if(requiredMod.length() <= 0) {
            throw new JsonParseException("Value for 'mod' in block group definition is empty");
        }

        if(!Loader.isModLoaded(requiredMod)) {
            throw new JsonParseException("Mod '"+requiredMod+"' for block group '"+groupId+"' is not loaded. Skipping integration!");
        }

        // Get item used as icon to identify the group quicker
        if(!rootObj.has("itemIcon")) {
            throw new JsonParseException("Missing 'itemIcon' property in block group definition");
        }
        ItemStack itemIcon = deserializeItemStack(rootObj.get("itemIcon"));

        // Get label used for this block group
        if(!rootObj.has("translationKey")) {
            throw new JsonParseException("Missing 'translationKey' in block group definition");
        }
        String translationKey = rootObj.get("translationKey").getAsString();

        // Get blocks belonging to this block group
        if(!rootObj.has("blocks")) {
            throw new JsonParseException("Missing 'blocks' array in block group definition");
        }

        if(!rootObj.get("blocks").isJsonArray()) {
            throw new JsonParseException("Property 'blocks' in block group definition is no array");
        }

        List<IBlockState> blocks = new ArrayList<>();
        for(JsonElement element : rootObj.getAsJsonArray("blocks")) {
            IBlockState state = deserializeBlockState(element);
            if(state == null) {
                continue;
            }

            blocks.add(state);
        }

        // Get optional optimization-strategy
        EnumBoxOptimizationStrategy optimizationStrategy = EnumBoxOptimizationStrategy.REMOVE_DUPLICATE_LINES;
        if(rootObj.has("optimizationStrategy")) {
            try {
                optimizationStrategy = EnumBoxOptimizationStrategy.valueOf(rootObj.get("optimizationStrategy").getAsString());
            } catch(IllegalArgumentException e) {
                throw new JsonParseException("Invalid value for optimizationStrategy property.");
            }
        }

        BlockGroup result = new BlockGroup(groupId);
        result.setOptimizationStrategy(optimizationStrategy);
        result.setItemIcon(itemIcon);
        result.setTranslationKey(translationKey);
        result.setValidBlockStates(blocks);
        result.setModId(requiredMod);

        return result;
    }


}
