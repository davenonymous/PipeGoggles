package org.dave.pipemaster.data.blockgroups;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.dave.pipemaster.data.config.ConfigurationHandler;
import org.dave.pipemaster.util.Logz;
import org.dave.pipemaster.util.ResourceLoader;
import org.dave.pipemaster.util.SerializationHelper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class BlockGroupRegistry {
    private Map<String, BlockGroup> groupMap;
    private BlockGroup emptyBlockGroup = new BlockGroup("disabled");

    public BlockGroupRegistry() {
        reloadGroups();
    }

    public void reloadGroups() {
        this.groupMap = new HashMap<>();
        this.groupMap.put("disabled", emptyBlockGroup);

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
                    this.groupMap.put(group.getId(), group);
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
        return groupMap.keySet();
    }

    public BlockGroup getEmptyBlockGroup() {
        return this.emptyBlockGroup;
    }

    public BlockGroup getBlockGroupById(String id) {
        if(!this.groupMap.containsKey(id)) {
            return getEmptyBlockGroup();
        }

        return this.groupMap.get(id);
    }
}
