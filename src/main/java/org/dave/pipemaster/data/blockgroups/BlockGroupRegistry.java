package org.dave.pipemaster.data.blockgroups;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.dave.pipemaster.data.config.ConfigurationHandler;
import org.dave.pipemaster.util.Logz;
import org.dave.pipemaster.util.MultiIndexMap;
import org.dave.pipemaster.util.ResourceLoader;
import org.dave.pipemaster.util.SerializationHelper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

import static org.dave.pipemaster.data.blockgroups.BlockGroupRegistry.MapCategories.GROUP_ID;
import static org.dave.pipemaster.data.blockgroups.BlockGroupRegistry.MapCategories.MOD_ID;

public class BlockGroupRegistry {
    private MultiIndexMap<BlockGroup> groupMap;

    private BlockGroup emptyBlockGroup = new BlockGroup("disabled");


    public BlockGroupRegistry() {
        reloadGroups();
    }

    public void reloadGroups() {
        this.groupMap = new MultiIndexMap<>();
        this.groupMap.addIndex(GROUP_ID, String.class, BlockGroup::getId);
        this.groupMap.addIndex(MOD_ID, String.class, BlockGroup::getModId);

        this.groupMap.add(emptyBlockGroup);

        ResourceLoader loader = new ResourceLoader(ConfigurationHandler.blockGroupsDirectory, "assets/pipemaster/config/blockgroups/");
        for(Map.Entry<String, InputStream> entry : loader.getResources().entrySet()) {
            String filename = entry.getKey();
            InputStream is = entry.getValue();

            if (!filename.endsWith(".json")) {
                continue;
            }

            Logz.info(" > Loading blockgroups from file: '%s'", filename);
            try {
                List<BlockGroup> blockGroups = SerializationHelper.GSON.fromJson(new JsonReader(new InputStreamReader(is)), new TypeToken<List<BlockGroup>>() {}.getType());
                for(BlockGroup group : blockGroups) {
                    Logz.info(" + %s", group.getId());
                    this.groupMap.add(group);
                }
            } catch(JsonParseException e) {
                Logz.info("Could not load blockgroups from file '%s': %s", filename, e.getLocalizedMessage());
            }
        }
    }

    public Collection<BlockGroup> getBlockGroups() {
        return groupMap.values();
    }

    public Set<String> getGroupIds() {
        return groupMap.keySet(String.class, GROUP_ID);
    }

    public BlockGroup getEmptyBlockGroup() {
        return this.emptyBlockGroup;
    }

    public BlockGroup getBlockGroupById(String id) {
        if(!groupMap.contains(GROUP_ID, id)) {
            return emptyBlockGroup;
        }

        return groupMap.getFirstElement(GROUP_ID, id);
    }

    public BlockGroup getBlockGroupByModId(String modId) {
        if(!groupMap.contains(MOD_ID, modId)) {
            return emptyBlockGroup;
        }

        return groupMap.getFirstElement(MOD_ID, modId);
    }

    enum MapCategories implements MultiIndexMap.IEnumCategory {
        MOD_ID,
        GROUP_ID,
    }
}
