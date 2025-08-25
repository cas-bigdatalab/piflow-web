package cn.cnic.base.utils;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "MdcKeyExistsFilter", category = "Core", elementType = "filter", printObject = true)
public class MdcKeyExistsFilter extends AbstractFilter {
    private final String key;

    public MdcKeyExistsFilter(String key, Result onMatch, Result onMismatch) {
        super(onMatch, onMismatch);
        this.key = key;
    }

    @Override
    public Result filter(LogEvent event) {
        if (event.getContextData().containsKey(key)) {
            return onMatch;
        }
        return onMismatch;
    }

    @PluginFactory
    public static MdcKeyExistsFilter createFilter(
            @PluginAttribute("key") String key,
            @PluginAttribute("onMatch") Result onMatch,
            @PluginAttribute("onMismatch") Result onMismatch) {
        return new MdcKeyExistsFilter(key, onMatch, onMismatch);
    }
}