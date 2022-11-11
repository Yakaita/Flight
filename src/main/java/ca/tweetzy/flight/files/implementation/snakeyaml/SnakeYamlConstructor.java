/*
 * Flight
 * Copyright 2022 Kiran Hart
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ca.tweetzy.flight.files.implementation.snakeyaml;

import ca.tweetzy.flight.files.configuration.serialization.ConfigurationSerialization;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/file/YamlConstructor.java">Bukkit Source</a>
 */
public class SnakeYamlConstructor extends SafeConstructor {

    public SnakeYamlConstructor() {
        this.yamlConstructors.put(Tag.MAP, new ConstructCustomObject());
    }

    @Override
    public void flattenMapping(final MappingNode node) {
        super.flattenMapping(node);
    }

    public Object construct(final Node node) {
        return super.constructObject(node);
    }

    protected boolean hasSerializedTypeKey(final MappingNode node) {
        for (final NodeTuple nodeTuple : node.getValue()) {
            final Node keyNode = nodeTuple.getKeyNode();
            if (keyNode instanceof ScalarNode) {
                final String key = ((ScalarNode) keyNode).getValue();
                if (key.equals(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                    return true;
                }
            }
        }
        return false;
    }

    private final class ConstructCustomObject extends ConstructYamlMap {

        @Override
        public Object construct(final Node node) {
            if (node.isTwoStepsConstruction()) {
                throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
            }
            final Map<?, ?> raw = (Map<?, ?>) super.construct(node);
            if (!raw.containsKey(ConfigurationSerialization.SERIALIZED_TYPE_KEY)) {
                return raw;
            }
            final Map<String, Object> typed = new LinkedHashMap<>(raw.size());
            for (final Map.Entry<?, ?> entry : raw.entrySet()) {
                typed.put(entry.getKey().toString(), entry.getValue());
            }
            try {
                return ConfigurationSerialization.deserializeObject(typed);
            } catch (final IllegalArgumentException ex) {
                throw new YAMLException("Could not deserialize object", ex);
            }
        }

        @Override
        public void construct2ndStep(final Node node, final Object object) {
            throw new YAMLException("Unexpected referential mapping structure. Node: " + node);
        }

    }

}
