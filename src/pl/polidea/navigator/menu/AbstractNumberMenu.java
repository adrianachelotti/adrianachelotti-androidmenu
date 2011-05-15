package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;

/**
 * Base class for all number menu types.
 * 
 */
public abstract class AbstractNumberMenu extends AbstractNavigationMenu {

    private static final long serialVersionUID = 1L;
    public final String variable;
    public final AbstractNavigationMenu link;
    public final Integer minLength;
    public final Integer maxLength;
    public final String transaction;

    public AbstractNumberMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final String menuType,
            final AbstractNavigationMenu parent) throws JSONException {
        super(reader, jsonMenu, menuType, parent);
        minLength = JsonMenuReader.getIntOrNull(jsonMenu, "minLength");
        maxLength = JsonMenuReader.getIntOrNull(jsonMenu, "maxLength");
        variable = jsonMenu.getString("variable");
        transaction = JsonMenuReader.getStringOrNull(jsonMenu, "transaction");
        link = reader.readLink(jsonMenu, directory, this);
        if (link != null && transaction != null) {
            throw new JSONException("Exactly one of \"link\" and \"transaction\" can be defined in " + jsonMenu);
        }
    }

    @Override
    public boolean isDisabled() {
        return link == null && transaction == null;
    }

    @Override
    public String toString() {
        return "AbstractNumberMenu [variable=" + variable + ", link=" + link + ", minLength=" + minLength
                + ", maxLength=" + maxLength + ", transaction=" + transaction + ", " + super.toString() + "]";
    }

}