package org.dave.pipemaster.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.dave.pipemaster.data.blockgroups.BlockGroup;
import org.dave.pipemaster.data.blockgroups.BlockGroupSerializer;

import java.util.List;

public class SerializationHelper {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .enableComplexMapKeySerialization()
            .registerTypeAdapter(new TypeToken<List<BlockGroup>>(){}.getType(), new BlockGroupSerializer())
            .create();

}