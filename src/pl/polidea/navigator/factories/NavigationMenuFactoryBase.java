package pl.polidea.navigator.factories;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.BasicMenuTypes;
import pl.polidea.navigator.menu.FloatNumberMenu;
import pl.polidea.navigator.menu.IconsMenu;
import pl.polidea.navigator.menu.ListMenu;
import pl.polidea.navigator.menu.MenuImport;
import pl.polidea.navigator.menu.NumberMenu;
import pl.polidea.navigator.menu.PhoneNumberMenu;
import pl.polidea.navigator.menu.StringMenu;
import pl.polidea.navigator.menu.TransactionMenu;
import android.content.Context;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Basic reader of menu object from Json.
 */
public class NavigationMenuFactoryBase implements NavigationMenuFactoryInterface {

    @Override
    public AbstractNavigationMenu readMenuFromJsonObject(final JsonMenuReader reader, final JSONObject jsonMenu,
            final AbstractNavigationMenu parent, final Context context) throws JSONException {
        final String type = JsonMenuReader.getStringOrNull(jsonMenu, "type");
        try {
            final ObjectMapper mapper = new ObjectMapper();

            if (type == null || BasicMenuTypes.TRANSACTION.equals(type)) {
                final TransactionMenu menu = mapper.readValue(jsonMenu.toString(), TransactionMenu.class);
                menu.setOtherInformations(reader, jsonMenu, parent, context);
                return menu;
                // return new TransactionMenu(reader, jsonMenu, parent,
                // context);
            } else if (BasicMenuTypes.ICONS.equals(type)) {
                // final IconsMenu menu = mapper.readValue(jsonMenu.toString(),
                // IconsMenu.class);
                // menu.setOtherInformations(reader, jsonMenu, parent, context);
                // return menu;
                return new IconsMenu(reader, jsonMenu, parent, context);
            } else if (BasicMenuTypes.LIST.equals(type)) {
                final ListMenu menu = mapper.readValue(jsonMenu.toString(), ListMenu.class);
                menu.setOtherInformations(reader, jsonMenu, parent, context);
                return menu;
                // return new ListMenu(reader, jsonMenu, parent, context);
            } else if (BasicMenuTypes.MENU_IMPORT.equals(type)) {
                // final MenuImport menu = mapper.readValue(jsonMenu.toString(),
                // MenuImport.class);
                // menu.setOtherInformations(reader, jsonMenu, parent, context);
                // abstractNavigationMenu = menu;
                return new MenuImport(reader, jsonMenu, parent, context);
            } else if (BasicMenuTypes.STRING.equals(type)) {
                // final StringMenu menu = mapper.readValue(jsonMenu.toString(),
                // StringMenu.class);
                // menu.setOtherInformations(reader, jsonMenu, parent, context);
                // abstractNavigationMenu = menu;
                return new StringMenu(reader, jsonMenu, parent, context);
            } else if (BasicMenuTypes.NUMBER.equals(type)) {
                // final NumberMenu menu = mapper.readValue(jsonMenu.toString(),
                // NumberMenu.class);
                // menu.setOtherInformations(reader, jsonMenu, parent, context);
                // abstractNavigationMenu = menu;
                return new NumberMenu(reader, jsonMenu, parent, context);
            } else if (BasicMenuTypes.PHONE_NUMBER.equals(type)) {
                // final PhoneNumberMenu menu =
                // mapper.readValue(jsonMenu.toString(), PhoneNumberMenu.class);
                // menu.setOtherInformations(reader, jsonMenu, parent, context);
                // abstractNavigationMenu = menu;
                return new PhoneNumberMenu(reader, jsonMenu, parent, context);
            } else if (BasicMenuTypes.FLOAT_NUMBER.equals(type)) {
                // final FloatNumberMenu menu =
                // mapper.readValue(jsonMenu.toString(), FloatNumberMenu.class);
                // menu.setOtherInformations(reader, jsonMenu, parent, context);
                // abstractNavigationMenu = menu;
                return new FloatNumberMenu(reader, jsonMenu, parent, context);
            } else {
                throw new IllegalArgumentException("Type " + type + " is undefined!");
            }
        } catch (final JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
