package pl.polidea.navigator.menu;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import pl.polidea.navigator.Persistence;
import android.content.Context;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base class for all menu types.
 * 
 */
public abstract class AbstractNavigationMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public String description;
    public String help;
    @JsonProperty("icon") public String iconFile;
    @JsonProperty("breadcrumb_icon") public String breadCrumbIconFile;
    public File directory;
    public String menuType;
    public Map<String, String> parameters;
    public transient MenuContext menuContext;
    public transient AbstractNavigationMenu parent;

    private transient Persistence persistence;

    public AbstractNavigationMenu() {

    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setHelp(final String help) {
        this.help = help;
    }

    public void setIconFile(final String iconFile) {
        this.iconFile = iconFile;
    }

    public void setBreadCrumbIconFile(final String breadCrumbIconFile) {
        this.breadCrumbIconFile = breadCrumbIconFile;
    }

    public void setOtherInformations(final JsonMenuReader reader, final JSONObject jsonMenu, final String menuType,
            final AbstractNavigationMenu parent, final Context context) throws JSONException {
        this.directory = reader.directory;
        this.menuType = menuType;
        this.menuContext = reader.menuContext;
        this.persistence = new Persistence(context);
        this.parameters = new TreeMap<String, String>();
        JsonMenuReader.readParameters(this.parameters, jsonMenu, "parameters");
    }

    public AbstractNavigationMenu(final String name, final String menuType, final Context context) {
        this.directory = null;
        this.menuContext = null;
        this.iconFile = null;
        this.breadCrumbIconFile = null;
        this.parameters = null;
        this.help = null;

        this.name = name;
        this.menuType = menuType;
        this.persistence = new Persistence(context);
    }

    public AbstractNavigationMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final String menuType,
            final AbstractNavigationMenu parent, final Context context) throws JSONException {
        this.directory = reader.directory;
        this.menuType = menuType;
        this.menuContext = reader.menuContext;
        this.persistence = new Persistence(context);
        // Note. Tree Map here is because hashmap has a weird bug/feature which
        // results in lots of "loadFactor"
        // related logs to be printed to log file when serializing/deserializing
        // TreeMap has no such problem
        this.parameters = new TreeMap<String, String>();
        name = jsonMenu.getString("name");
        description = JsonMenuReader.getStringOrNull(jsonMenu, "description");
        iconFile = JsonMenuReader.getStringOrNull(jsonMenu, "icon");
        breadCrumbIconFile = JsonMenuReader.getStringOrNull(jsonMenu, "breadcrumb_icon");
        help = JsonMenuReader.getStringOrNull(jsonMenu, "help");
        JsonMenuReader.readParameters(this.parameters, jsonMenu, "parameters");
        this.parent = parent;
    }

    public boolean isDisabled() {
        return !persistence.getMenuVisibility(name);
    }

    public void setDescription(final String decription) {
        this.description = decription;
    }

    @Override
    public String toString() {
        return "AbstractNavigationMenu [name=" + name + ", description=" + description + ", iconFile=" + iconFile
                + ", breadCrumbIconFile=" + breadCrumbIconFile + ", directory=" + directory + ", menuType=" + menuType
                + ", parent=" + (parent == null ? "null" : parent.getClass()) + ", " + ", parameters=" + parameters
                + super.toString() + "]";
    }

    public void updateTransientAttributes(final MenuContext menuContext, final AbstractNavigationMenu parent,
            final Context context) {
        this.parent = parent;
        this.menuContext = menuContext;
        persistence = new Persistence(context);
    }
}
