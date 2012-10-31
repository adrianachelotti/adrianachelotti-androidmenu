/**
 *
 */
package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import android.content.Context;

/**
 * @author Marek Multarzynski
 * 
 */
public abstract class AbstractTransactionMenu extends AbstractNavigationMenu {

    private static final long serialVersionUID = 1861928775614507409L;
    public String transaction;

    /**
     * @param reader
     * @param jsonMenu
     * @param menuType
     * @param parent
     * @param context
     * @throws JSONException
     */
    public AbstractTransactionMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final String menuType,
            final AbstractNavigationMenu parent, final Context context) throws JSONException {
        super(reader, jsonMenu, menuType, parent, context);
        // TODO Auto-generated constructor stub
    }

    public AbstractTransactionMenu(final String name, final String menuType, final Context context) {
        super(name, menuType, context);
        // TODO Auto-generated constructor stub
    }

}
