package pl.polidea.navigator.menu;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Menu firing a transaction.
 */
public class TransactionMenu extends AbstractNavigationMenu {

    private static final long serialVersionUID = 1L;

    public TransactionMenu(final JSONObject jsonMenu, final File directory, final AbstractNavigationMenu parent)
            throws JSONException {
        super(jsonMenu, directory, MenuType.TRANSACTION, parent);
        transaction = jsonMenu.getString("transaction");
    }

    public String transaction;

    @Override
    public boolean isDisabled() {
        return transaction == null;
    }

    @Override
    public String toString() {
        return "TransactionMenu [transaction=" + transaction + ", " + super.toString() + "]";
    }

}
