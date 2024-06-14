package com.msgkatz.ratesapp.data.entities.mappers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamComboBase;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamEvent;
import com.msgkatz.ratesapp.data.entities.wsocks.StreamEventType;

import java.lang.reflect.Type;

/**
 * Created by msgkatz on 24/09/2018.
 */

public class StreamComboDataMapper {

    public static StreamEventType parseEvent(StreamComboBase combo)
    {
        if (combo == null)
            return StreamEventType.TYPE_UNKNOWN;

        if (combo.data == null)
            return StreamEventType.TYPE_UNKNOWN;

        Type type = new TypeToken<StreamEvent>() {}.getType();
        String data = "";
        if (combo.data instanceof JsonObject)
            data = ((JsonObject)combo.data).toString();
        StreamEvent event = new Gson().fromJson(data, type);

        if (event == null)
            return StreamEventType.TYPE_UNKNOWN;

        return StreamEventType.getTypeByName(event.eventType);
    }
}
